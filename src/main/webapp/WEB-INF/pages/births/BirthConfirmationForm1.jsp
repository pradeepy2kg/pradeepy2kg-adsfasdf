<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script>


// mode 1 = passing District, will return DS list
// mode 2 = passing DsDivision, will return BD list
// any other = passing district, will return DS list and the BD list for the first DS
$(function() {
    $('select#districtId').bind('change', function(evt1) {
        var id = $("select#districtId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
                function(data) {
                    var options1 = '';
                    var ds = data.dsDivisionList;
                    for (var i = 0; i < ds.length; i++) {
                        options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                    }
                    $("select#dsDivisionId").html(options1);

                    var options2 = '';
                    var bd = data.bdDivisionList;
                    for (var j = 0; j < bd.length; j++) {
                        options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                    }
                    $("select#bdDivisionId").html(options2);
                });
    });

    $('select#dsDivisionId').bind('change', function(evt2) {
        var id = $("select#dsDivisionId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:2},
                function(data) {
                    var options = '';
                    var bd = data.bdDivisionList;
                    for (var i = 0; i < bd.length; i++) {
                        options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                    }
                    $("select#bdDivisionId").html(options);
                });
    });
});

// mode 1 = passing District, will return DS list
// mode 2 = passing DsDivision, will return BD list
// any other = passing district, will return DS list and the BD list for the first DS
$(function() {
    $('select#motherDistrictId').bind('change', function(evt1) {
        var id = $("select#motherDistrictId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:16},
                function(data) {
                    var options1 = '';
                    var ds = data.dsDivisionList;
                    var select = document.getElementById('selectDSDivision').value;
                    options1 += '<option value="' + 0 + '">' + select + '</option>';
                    for (var i = 0; i < ds.length; i++) {
                        options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                    }
                    $("select#motherDSDivisionId").html(options1);

                    var options3 = '';
                    var gn = data.gnDivisionList;
                    var select = document.getElementById('selectGNDivision').value;
                    options3 += '<option value="' + 0 + '">' + select + '</option>';
                    for (var k = 0; k < gn.length; k++) {
                        options3 += '<option value="' + gn[k].optionValue + '">' + gn[k].optionDisplay + '</option>';
                    }
                    $("select#motherGNDivisionId").html(options3);
                });
    });

    $('select#motherDSDivisionId').bind('change', function(evt2) {
        var id = $("select#motherDSDivisionId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:2},
                function(data) {
                    var options4 = '';
                    var gn = data.gnDivisionList;
                    var select = document.getElementById('selectGNDivision').value;
                    options4 += '<option value="' + 0 + '">' + select + '</option>';
                    for (var k = 0; k < gn.length; k++) {
                        options4 += '<option value="' + gn[k].optionValue + '">' + gn[k].optionDisplay + '</option>';
                    }
                    $("select#motherGNDivisionId").html(options4);
                });
    });
});

function disableNext(mode) {
    if (mode) {
        document.getElementById('next').style.display = 'none';
    }
    else {
        document.getElementById('next').style.display = 'block';
    }
}

function disableOk(mode) {
    if (mode) {
        document.getElementById('searchOk').style.display = 'none';
    }
    else {
        document.getElementById('searchOk').style.display = 'block';
    }
}

function okCheck() {
    var ok = document.getElementById('skipChangesCBox');
    if (ok.checked) {
        disableNext(true)
        disableOk(false)
    } else {
        disableOk(true)
        disableNext(false)
    }
}

$(function() {
    $("#submitDatePicker").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});
$(function() {
    $('img#place').bind('click', function(evt) {

        if ($('#placeOfBirthId').val() !=null && $('#placeOfBirthId').val().length>0) {

            var text = $("input#placeOfBirth").attr("value");
            $.post('/ecivil/TransliterationService', {text:text, gender:'U'},
                    function (data) {
                        if (data != null) {
                            var s = data.translated;
                            $("input#placeOfBirthEnglish").val(s);
                        }
                    });

        } else {

            var e = document.getElementById("hospitalId");
            var text = e.options[e.selectedIndex].text;         
            $.post('/ecivil/TransliterationService', {text:text, gender:'U'},
                    function(data) {
                        if (data != null) {
                            var s = data.translated;
                            $("input#placeOfBirthEnglish").val(s);
                        }
                    });
        }


    });
});

var errormsg = "";

function validate() {
    var domObject;
    var returnval = true;

    // validate serial number
    domObject = document.getElementById('SerialNo');
    if (isFieldEmpty(domObject)) {
        //            errormsg = errormsg + "\n" + document.getElementById('error2').value;
    } else {
        isNumeric(domObject.value, 'error1', 'error2');
    }

    // validate date of birth
    domObject = document.getElementById('submitDatePicker');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, "", 'p1error3')
    else
        isDate(domObject.value, "error1", "error4");

    domObject = document.getElementById('placeOfBirth');
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, "", "error3");
    }

    domObject = document.getElementById('fatherNICorPIN');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'error1', 'error6');

    domObject = document.getElementById('motherNICorPIN');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'error1', 'error6');

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

function validateSearch() {
    var domObject;
    var returnval = true;

    // validate serial number
    domObject = document.getElementById('SerialNo');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error2').value;
    } else {
        isNumeric(domObject.value, 'error1', 'error2');
    }

    var out = checkActiveFieldsForSyntaxErrors('birth-confirmation-form-1');
    if (out != "") {
        errormsg = errormsg + out;
    }

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

function initPage() {
    var domObject = document.getElementById('SerialNo');
    if (domObject.value.trim() == 0) {
        domObject.value = null;
    }
    document.getElementById('searchOk').style.display = 'none';
}
</script>

<div id="birth-confirmation-form-outer">
<table class="table_con_header_01" width="100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="300px"></td>
        <td align="center"><img src="<s:url value="/images/official-logo.png" />" alt=""/><br> <label>
            ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA<br><br>
            දෙමව්පියන් / භාරකරු විසින් උපත තහවුරු කිරීම
            ﻿﻿ <br>பெற்றோர் அல்லது பாதுகாப்பாளர் மூலம் பிறப்பை உறுதிப்படுத்தல்
            <br>Confirmation of Birth by Parents / Guardian
        </label></td>
        <td>
            <form action="eprBirthConfirmationInit.do" method="post" onsubmit="javascript:return validateSearch()">
                <table style=" border:1px solid #000000; width:300px">
                    <tr><s:actionerror cssStyle="color:red;font-size:10pt"/></tr>
                    <tr>
                        <td><label><span class="font-8">අනුක්‍රමික අංකය<s:label value="*"
                                                                                cssStyle="color:red;font-size:20pt"/><br>தொடர் இலக்கம்<br>Serial Number</span></label>
                        </td>
                        <td><s:textfield name="bdId" id="SerialNo" value="%{bdId}"/>
                            <%--<s:label value="*" cssStyle="color:red;font-size:15pt"/>--%>
                        </td>
                    </tr>
                </table>
                <table style=" width:300px">
                    <tr>

                    <tr>
                        <td width="200px"></td>
                        <td align="right" class="button"><s:submit name="search"
                                                                   value="%{getText('searchButton.label')}"
                                                                   cssStyle="margin-right:10px;"/></td>
                    </tr>
                </table>
            </form>
            <form action="eprBirthConfirmationSkipChanges.do" onsubmit="javascript:return validate()">
                <s:if test="#session.birthConfirmation_db.register.status.ordinal() == 2">
                    <table style=" border:1px solid #000000; width:300px">
                        <tr>
                            <td style="width:55%"><s:label value="%{getText('noConfirmationChanges.label')}"/></td>
                            <td><s:checkbox name="skipConfirmationChages" id="skipChangesCBox"
                                            onclick="okCheck()"/></td>

                            <s:hidden name="pageNo" value="2"/>
                            <s:hidden name="bdId" value="%{#request.bdId}"/>
                            <td align="left" class="button">
                                <s:submit id="searchOk" name="searchOk" value="%{getText('skip.label')}"
                                          cssStyle="margin-right:8px;font-size:9.3pt;padding:0;width:115px;"/>
                            </td>
                        </tr>
                    </table>
                </s:if>
            </form>

        </td>
    </tr>
    </tbody>
</table>


<s:form action="eprBirthConfirmation" name="birthConfirmationForm1" id="birth-confirmation-form-1" method="POST"
        onsubmit="javascript:return validate()">


<table class="table_con_page_01" width="100%" cellpadding="0" cellspacing="0" style="margin-bottom:20px;">
    <caption></caption>
    <col align="center"/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td class="cell_01">1</td>
        <td width="60%">සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ අදාල “උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර” ප්‍රකාශනයේ
            අනුක්‍රමික අංකය
            හා දිනය
            <br>பிறப்பை பதிவு செய்வதற்கான விபரம்" எனும் படிவத்தின் தொடர் இலக்கமும் திகதியும்
            <br>Serial Number and the Date of the ‘Particulars for Registration of a Birth’ form
        </td>
        <td><s:textfield cssClass="disable" disabled="true"
                         name="#session.birthConfirmation_db.register.bdfSerialNo"/>
            <s:textfield cssClass="disable" disabled="true"
                         name="#session.birthConfirmation_db.register.dateOfRegistration"/></td>
    </tr>
    </tbody>
</table>


<table class="table_con_page_01" width="100%" cellpadding="0" cellspacing="0">
<col/>
<col/>
<col/>
<col/>
<col/>
<col/>
<col/>
<col/>
<col/>
<col/>
<col/>
<col/>
<col/>
<col/>
<col/>
<tbody>
<tr>
    <td colspan="14" style="text-align:center;font-size:12pt"> සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ ඇතුළත් විස්තර
        <br>சிவில் பதிவு அமைப்பில் உள்ளடக்கப்பட்டுள்ள விபரம்
        <br>Information included in Civil Registration System
    </td>
</tr>
<tr>
    <td colspan="2"><label>විස්තර <br>விபரங்கள் <br>Particulars </label></td>
    <td colspan="6" width="450px"><label>සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ දැනට අඩංගු විස්තර <br>சிவில் பதிவு
        அமைப்பில்
        உள்ளடக்கப்பட்டுள்ள
        விபரம<br>Information included in Civil Registration System </label></td>
    <td class="cell_02" colspan="6"><label>
        ඇතුලත් කරඇති තොරතුරු හෝ යම් අක්ෂර දෝෂයක් නිවැරදි කල යුතුනම් , වෙනස් විය යුතු ආකාරය ඇතුලත් කරන්න
        <br>உட்புகுத்தப்பட்ட விபரங்கள் அல்லது ஏதாவது தவறு திருத்தப்பட வேண்டுமாயின், திருத்தப்படவெண்டிய விதத்தினை
        குறிப்பிடவும்
        <br>If there are spelling mistakes or changes in existing details.</label></td>
</tr>
<tr>
    <td class="cell_01">2</td>
    <td class="cell_04"><label>උපන් දිනය<s:label value="*" cssStyle="color:red;font-size:14pt"/><br>பிறந்த திகதி<br>Date
        of birth</label></td>
    <td class="cell_03"><label>අවුරුද්ද <br>வருடம் <br>Year</label></td>
    <td class="cell_03"><s:textfield value="%{#session.birthConfirmation_db.child.dateOfBirth.year+1900}"
                                     cssClass="disable" disabled="true"
                                     size="4"/></td>
    <td class="cell_03"><label>මාසය<br>மாதம்<br>Month</label></td>
    <td class="cell_03"><s:textfield value="%{#session.birthConfirmation_db.child.dateOfBirth.month+1}"
                                     cssClass="disable" disabled="true"
                                     size="4"/></td>
    <td class="cell_03"><label>දිනය<br>திகதி<br>Day</label></td>
    <td class="cell_03"><s:textfield value="%{#session.birthConfirmation_db.child.dateOfBirth.date}"
                                     cssClass="disable" disabled="true"
                                     size="4"/></td>
    <td colspan="6"><s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
        <s:textfield name="child.dateOfBirth" id="submitDatePicker" cssStyle="width:200px;float:left;"/>
    </td>
</tr>
<tr>
    <td>3</td>
    <td><label>ස්ත්‍රී පුරුෂ භාවය <br>பால்<br>Gender</label></td>
    <td colspan="6">
        <s:if test="bdId != 0">
            <s:if test="#session.birthConfirmation_db.child.childGender == 0">
                <s:textfield value="%{getText('male.label')}" cssClass="disable" disabled="true"/>
            </s:if>
            <s:elseif test="#session.birthConfirmation_db.child.childGender == 1">
                <s:textfield value="%{getText('female.label')}" cssClass="disable" disabled="true"/>
            </s:elseif>
            <s:elseif test="#session.birthConfirmation_db.child.childGender == 2">
                <s:textfield value="%{getText('unknown.label')}" cssClass="disable" disabled="true"/>
            </s:elseif>
        </s:if>
    </td>
    <td colspan="6"><s:select
            list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
            name="child.childGender" id="genderList"/>
    </td>
</tr>
<tr>
    <td>4</td>
    <td colspan="7"><label> උප්පැන්න සහතිකය නිකුත් කල යුතු භාෂාව / பிறப்புச் சான்றிதழ் வழங்கப்பட வேண்டிய மொழி/<br>Preferred
        Language for
        Birth Certificate </label></td>
    <td colspan="6">
        <s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'தமிழ்'}" name="register.preferredLanguage"
                  cssStyle="width:190px; margin-left:5px;"/>
    </td>
</tr>
<tr>
    <td>5</td>
    <td colspan="14"><label>උපන් ස්ථානය පිළිබඳ විස්තර / பிறந்த இடம் பற்றிய விபரம் / Particulars of Place of Birth
        <s:label value="*" cssStyle="color:red;font-size:14pt"/></label>
    </td>
</tr>
<tr>
    <td rowspan="6"></td>
    <td><label>දිස්ත්‍රික්කය <br>மாவட்டம் <br>District</label></td>
    <td colspan="6">
        <s:if test="bdId != 0">
            <s:textfield value="%{getDistrictList().get(birthDistrictId)}" cssClass="disable" disabled="true"/>
        </s:if>
    </td>
    <td colspan="6"><s:select list="districtList" name="birthDistrictId" id="districtId"/></td>
</tr>
<tr>
    <td><label>ප්‍රාදේශීය ලේකම් කොට්ඨාශය<br>பிரதேச செயளாளர் பிரிவு<br>Divisional Secretary Division</label></td>
    <td colspan="6">
        <s:if test="bdId != 0">
            <s:textfield value="%{getDsDivisionList().get(dsDivisionId)}" cssClass="disable" disabled="true"/>
        </s:if>
    </td>
    <td colspan="6"><s:select list="dsDivisionList" name="dsDivisionId" id="dsDivisionId"/></td>
</tr>
<tr>
    <td><label>ලියාපදිංචි කිරීමේ කොට්ඨාශය<br>பதிவுப் பிரிவு<br>Registration Division</label></td>
    <td colspan="6">
        <s:if test="bdId != 0">
            <s:textfield value="%{getBdDivisionList().get(birthDivisionId)}" cssClass="disable" disabled="true"/>
        </s:if>
    </td>
    <td colspan="6"><s:select name="birthDivisionId" list="bdDivisionList" id="bdDivisionId"/></td>
</tr>
<tr>
    <td colspan="12"><label>උපන් ස්ථානය / பிறந்த இடம்/ Place of Birth</label></td>
</tr>
<tr>
    <td colspan="1"><label>සිංහල හෝ දෙමළ භාෂාවෙන් <br>சிங்களம்அல்லது தமிழ் மொழியில்<br>In Sinhala or Tamil</label></td>

    <s:if test="child.birthAtHospital == 0">
        <td colspan="6"><s:textfield name="#session.birthConfirmation_db.child.placeOfBirth" cssClass="disable"
                                     disabled="true" size="45"/></td>
        <td colspan="5"><s:textfield name="child.placeOfBirth" size="35" id="placeOfBirth"
                                     onchange="checkSyntax('placeOfBirth')"/></td>
    </s:if>
    <s:else>
        <s:if test="register.preferredLanguage == 'si'">
            <td colspan="6"><s:textfield name="#session.birthConfirmation_db.child.birthHospital.hospitalNameSi"
                                         cssClass="disable"
                                         disabled="true" size="45"/></td>
        </s:if>
        <s:elseif test="register.preferredLanguage == 'ta'">
            <td colspan="6"><s:textfield name="#session.birthConfirmation_db.child.birthHospital.hospitalNameTa"
                                         cssClass="disable"
                                         disabled="true" size="45"/></td>
        </s:elseif>

        <td colspan="5"><s:select id="hospitalId" name="child.birthHospital.hospitalUKey" list="hospitalList"
                                  cssStyle="float:left;  width:285px; margin:2px 5px;"/></td>

    </s:else>

        <%--
   <td colspan="6"><s:textfield name="#session.birthConfirmation_db.child.placeOfBirth" cssClass="disable"
                                disabled="true" size="45"/></td>
   <td colspan="5"><s:textfield name="child.placeOfBirth" size="35" id="placeOfBirth" onchange="checkSyntax('placeOfBirth')"/></td>--%>
</tr>
<tr>
    <td colspan="1"><label>ඉංග්‍රීසි භාෂාවෙන් <br>ஆங்கில மொழியில்<br>In English</label></td>
    <td colspan="6"><s:textfield name="#session.birthConfirmation_db.child.placeOfBirthEnglish" cssClass="disable"
                                 disabled="true" size="45"/></td>
    <td colspan="5">
        <s:textfield name="child.placeOfBirthEnglish" size="35" id="placeOfBirthEnglish"
                     cssStyle="margin-top:10px;"/>
        <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;" id="place">
    </td>
</tr>
<tr>
    <td>6</td>
    <td><label>පියාගේ අනන්‍යතා අංකය <br>தந்நையின் தனிநபர் அடையாள எண்<br>Father's Identification Number</label></td>
    <td colspan="6"><s:textfield name="#session.birthConfirmation_db.parent.fatherNICorPIN" cssClass="disable"
                                 disabled="true"/></td>
    <td colspan="6"><s:textfield name="parent.fatherNICorPIN" size="35" id="fatherNICorPIN" maxLength="12"/></td>
</tr>
<tr>
    <td>7</td>
    <td><label>පියාගේ ජන වර්ගය <br>தந்நையின் இனம்<br>Father's Ethnic Group</label></td>
    <td colspan="6">
        <s:textfield value="%{getRaceList().get(fatherRace)}" cssClass="disable" disabled="true"/>
    </td>
    <td colspan="6">
        <s:select list="raceList" name="fatherRace" headerKey="0" headerValue="%{getText('select_race.label')}"/>
    </td>
</tr>
<tr>

<tr>
    <td>8</td>
    <td><label>ම‌වගේ අනන්‍යතා අංකය <br>தாயின் அடையாள எண்<br>Mother's Identification Number</label></td>
    <td colspan="6"><s:textfield name="#session.birthConfirmation_db.parent.motherNICorPIN" cssClass="disable"
                                 disabled="true"/></td>
    <td colspan="6"><s:textfield name="parent.motherNICorPIN" size="35" id="motherNICorPIN" maxLength="12"/></td>
</tr>
<tr>
    <td>9</td>
    <td><label>මවගේ ජන වර්ගය <br>தாயின் இனம்<br>Mother's Ethnic Group</label></td>
    <td colspan="6"><s:textfield value="%{getRaceList().get(motherRace)}" cssClass="disable"
                                 disabled="true"/></td>
    <td colspan="6">
        <s:select list="raceList" name="motherRace" headerKey="0" headerValue="%{getText('select_race.label')}"/>
    </td>
</tr>
<tr>
    <td>10</td>
    <td><label>මව්පියන් විවාහකද? <br>பெற்றோர்கள் திருமணம் முடித்தவர்களா?<br>Were Parents Married?</label></td>
    <td colspan="6">
        <s:if test="bdId != 0">
            <s:textfield name="#session.birthConfirmation_db.marriage.parentsMarried" cssClass="disable"
                         disabled="true" value="%{getText('married.status.'+marriage.parentsMarried.ordinal())}"/>
        </s:if>
    </td>
    <td style="border:none;padding:0">
        <table style="border:none;" cellspacing="0">
            <col width="200px"/>
            <col/>
            <col width="150px"/>
            <col/>
            <tr>
                <td><label id="yes" class="label">ඔව්<br>ஆம்<br>Yes</label></td>
                <td>
                    <s:radio name="marriage.parentsMarried" id="parentsMarried"
                             list="#@java.util.HashMap@{'MARRIED':''}"
                             value="marriage.parentsMarried"/>

                </td>
                <td><label class="label">නැත<br>இல்லை<br>No</label></td>
                <td>
                    <s:radio name="marriage.parentsMarried" id="parentsMarried"
                             list="#@java.util.HashMap@{'UNMARRIED':''}"
                             value="marriage.parentsMarried"/>
                </td>
            </tr>
            <tr>
                <td><label class="label">නැත, නමුත් පසුව විවාහවී ඇත<br>
                    இல்லை, பின் விவாகமாணவா்கள்<br>
                    No, but since married</label></td>
                <td>
                    <s:radio name="marriage.parentsMarried" id="parentsMarried"
                             list="#@java.util.HashMap@{'NO_SINCE_MARRIED':''}"
                             value="marriage.parentsMarried"/>
                </td>
                <td><label>නොදනී<br>தெரியாது<br>Unknown</label></td>
                <td><s:radio name="marriage.parentsMarried" id="parentsMarried"
                             list="#@java.util.HashMap@{'UNKNOWN':''}"
                             value="marriage.parentsMarried"/></td>
            </tr>
        </table>
    </td>
</tr>

<tr>
    <td>11</td>
    <td>
        මව පදිංචි දිස්ත්‍රික්කය <br>
        தாயின் பதிவு மாவட்டம்<br>
        Mother's District
    </td>
    <td colspan="6">
        <s:textfield name="#session.birthConfirmation_db.parent.motherDistrictPrint" cssClass="disable"
                     disabled="true"/>
    </td>
    <td>
        <s:select list="allDistrictList" name="motherDistrictId" headerValue="%{getText('select_district.label')}"
                  headerKey="0" id="motherDistrictId"/>
    </td>

</tr>
<tr>
    <td>12</td>
    <td><label>මව පදිංචි ප්‍රාදේශීය ලේකම් කොට්ඨාශය<br>
        தாயின் பிரதேச செயலக பிரிவு<br>Mother's Divisional Secretary Division</label></td>
    <td colspan="6">
        <s:if test="bdId != 0">
            <s:textfield name="#session.birthConfirmation_db.parent.motherDsDivisionPrint" cssClass="disable"
                         disabled="true"/>
        </s:if>
    </td>
    <td>
        <s:select list="allDSDivisionList" name="motherDSDivisionId"
                  headerValue="%{getText('select_DS_division1.label')}"
                  headerKey="0" id="motherDSDivisionId"/>
    </td>
</tr>
<tr>
    <td>13</td>
    <td><label>මව පදිංචි ග්‍රාම නිලධාරී කොට්ඨාශය<br>
        தாயின் கிராம சேவையாளர் பிரிவு<br>Mother's Grama Niladhari Division</label></td>
    <td colspan="6">
        <s:if test="bdId != 0">
            <s:textfield name="#session.birthConfirmation_db.parent.motherGNDivisionPrint" cssClass="disable"
                         disabled="true"/>
        </s:if>
    </td>
    <td align="center">
        <s:select list="gnDivisionList" name="motherGNDivisionId" headerKey="0"
                  headerValue="%{getText('select.gn.division')}" id="motherGNDivisionId"/>
        <a href="javascript:displayGNSearch()">
            <span><s:label value="%{getText('searchGNDivision.label')}"/></span>
        </a>
    </td>
</tr>


</tbody>
</table>

<s:hidden name="pageNo" value="1"/>
<s:hidden id="placeOfBirthId" name="#session.birthConfirmation_db.child.placeOfBirth"/>

<s:if test="bdId != 0">
    <div class="form-submit">
        <s:submit value="%{getText('next.label')}" id="next"/>
        <s:hidden value="%{#request.bdId}" name="bdId"/>
    </div>
</s:if>
</s:form>

<s:hidden id="error2" value="%{getText('cp1.error.serialNum.value')}"/>
<s:hidden id="error3" value="%{getText('cp1.placeOfBirth.error.value')}"/>
<s:hidden id="p1error3" value="%{getText('cp1.date.error.value')}"/>
<s:hidden id="p1error4" value="%{getText('cp1.parents.marriage.error.value')}"/>
<s:hidden id="p1errorckbx" value="%{getText('cp1.skipChanges.checked.error.value')}"/>
<s:hidden id="error5" value="%{getText('p1.serial.text')}"/>
<s:hidden id="error1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error4" value="%{getText('p1.dob')}"/>
<s:hidden id="error6" value="%{getText('fatherPINorNIC.label')}"/>
<s:hidden id="error7" value="%{getText('motherPINorNIC.label')}"/>
<s:hidden id="selectGNDivision" value="%{getText('select.gn.division')}"/>
<s:hidden id="selectDSDivision" value="%{getText('select_DS_division1.label')}"/>
</div>

<%-- Styling Completed --%>