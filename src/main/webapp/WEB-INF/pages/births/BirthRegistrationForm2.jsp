<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:set value="rowNumber" name="row"/>

<s:if test="birthType.ordinal()==0">
    <%--still birth--%>
    <s:set name="row" value="7"/>
</s:if>
<s:elseif test="birthType.ordinal()==1">
    <%--live birth--%>
    <s:set name="row" value="9"/>
</s:elseif>
<s:elseif test="birthType.ordinal()==2">
    <%--adoption--%>
    <s:set name="row" value="11"/>
</s:elseif>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/agecalculator.js"/>"></script>
<%--  <script type="text/javascript">
        $(document).ready(function () {
            $('#parent.motherAdmissionDate').datepicker({
                changeYear: true,
                beforeShow: function (textbox, instance) {
                instance.dpDiv.css({
                    marginTop: '20 px',
                    marginLeft: '10px'
                });
                }
            });
        });
    </script>--%>
</HEAD>
<BODY onload="noBack();"
    onpageshow="if (event.persisted) noBack();" onunload="">
<%--back button disable--%>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<s:hidden id="p2error1" value="%{getText('p2.fatherName.error.value')}"/>
<s:hidden id="p2error2" value="%{getText('p2.motherName.error.value')}"/>
<s:hidden id="fatherNameEnglish" value="%{getText('fatherNameInEnglish')}"/>
<s:hidden id="motherNameEnglish" value="%{getText('motherNameInEnglish')}"/>
<s:hidden id="error18" value="%{getText('enter.fatherRace')}"/>
<s:hidden id="error19" value="%{getText('enter.motherRace')}"/>
<s:hidden id="mother_age" value="%{getText('p2.motherAge.error.value')}"/>
<s:hidden id="mother_birth_day_empty" value="%{getText('p2.motherAge.empty.error.value')}"/>
<s:hidden id="childDateOfBirth" value="%{child.dateOfBirth}"/>


<script>

$(function() {
    var currentYear = new Date().getFullYear();
    var id1 = $("input#father_pinOrNic").attr("value");


    $("#fatherDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31',
        defaultDate:-365 * 18

    });
})
        ;

$(function() {
    $("#motherDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31',
        onSelect: function(dateText, inst) {
            motherAgeBirth();
        },
        defaultDate:-365 * 18
    });

    /*trans iterators for father and mothers name*/
    $('img#name_english_father').bind('click', function(evt4) {
        var text = $("textarea#fatherFullName").attr("value");

        $.post('/ecivil/TransliterationService', {text:text,gender:'M'},
                function(data) {
                    if (data != null) {
                        var s = data.translated;
                        $("textarea#fatherFullNameInEnglish").val(s);
                    }
                });
    });

    $('img#name_english_mother').bind('click', function(evt4) {
        var text = $("textarea#motherFullName").attr("value");

        $.post('/ecivil/TransliterationService', {text:text,gender:'F'},
                function(data) {
                    if (data != null) {
                        var s = data.translated;
                        $("textarea#motherFullNameInEnglish").val(s);
                    }
                });
    });
});

$(function() {
    $("#admitDatePicker").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});

$(function() {
    var currentYear = new Date().getFullYear();

    $('img#father_lookup').bind('click', function(evt1) {
        var id1 = $("input#father_pinOrNic").attr("value");
        var datePicker = $('#fatherDatePicker');
        var error = document.getElementById('error10').value;

        $("textarea#fatherFullName").val('');
        $("textarea#fatherFullNameInEnglish").val('');
        $("input#fatherPlaceOfBirth").val('');
        $("select#fatherRaceId").val('');
        $("input#fatherDatePicker").val('');
        datePicker.datepicker('setDate', calculateBirthDay(id1, error));
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                function(data1) {
                    if (data1 != null) {
                        $("textarea#fatherFullName").val(data1.fullNameInOfficialLanguage);
                        $("textarea#fatherFullNameInEnglish").val(data1.fullNameInEnglishLanguage);
                        $("input#fatherPlaceOfBirth").val(data1.placeOfBirth);
                        $("select#fatherRaceId").val(data1.race);
                        var fatherDOB = data1.dateOfBirth;
                        if (fatherDOB != null) {
                            $("input#fatherDatePicker").val(fatherDOB);
                        }
                    }
                });
    });

    $('img#mother_lookup').bind('click', function(evt2) {
        var id2 = $("input#mother_pinOrNic").attr("value");
        var datePicker = $('#motherDatePicker');
        var error = document.getElementById('error11').value;

        $("textarea#motherFullName").val('');
        $("textarea#motherFullNameInEnglish").val('');
        $("input#motherPlaceOfBirth").val('');
        $("textarea#motherAddress").val('');
        $("select#motherRaceId").val('');
        $("input#motherDatePicker").val('');
        datePicker.datepicker('setDate', calculateBirthDay(id2, error));
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id2},
                function(data2) {
                    if (data2 != null) {
                        $("textarea#motherFullName").val(data2.fullNameInOfficialLanguage);
                        $("textarea#motherFullNameInEnglish").val(data2.fullNameInEnglishLanguage);
                        $("input#motherPlaceOfBirth").val(data2.placeOfBirth);
                        $("textarea#motherAddress").val(data2.lastAddress);
                        $("select#motherRaceId").val(data2.race);
                        var motherDOB = data2.dateOfBirth;
                        if (motherDOB != null) {
                            $("input#motherDatePicker").val(motherDOB);
                        }
                    }
                });
        motherAgeBirth();
    });

//    $('#mother_lookup').click(function() {
//        var child_bday = new Date(document.getElementById('childDateOfBirth').value);
//        var mother_bday = document.getElementById('motherDatePicker').value;
//        if (mother_bday != "") {
//            var motherbday = new Date(mother_bday);
//            var mother_age = child_bday.getYear() - motherbday.getYear();
//            $("input#motherAgeAtBirth").val(mother_age);
//        }
//    });

    $('select#motherDistrictId').bind('change', function(evt3) {
        var id = $("select#motherDistrictId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:16},
                function(data) {
                    var options = '';
                    var ds = data.dsDivisionList;
                    var select = document.getElementById('selectDSDivision').value;
                    options += '<option value="' + 0 + '">' + select + '</option>';
                    for (var i = 0; i < ds.length; i++) {
                        options += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                    }
                    $("select#motherDSDivisionId").html(options);

                    var options3 = '';
                    var gn = data.gnDivisionList;
                    var select = document.getElementById('selectGNDivision').value;
                    options3 += '<option value="' + 0 + '">' + select + '</option>';
                    for (var k = 0; k < gn.length; k++) {
                        options3 += '<option value="' + gn[k].optionValue + '">' + gn[k].optionDisplay + '</option>';
                    }
                    $("select#gnDivisionId").html(options3);
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
                    $("select#gnDivisionId").html(options4);
                });
    });
})

//javascript for form validation
var errormsg = "";
function validate() {
    var domObject;
    var returnval;
    var check = document.getElementById('skipjs');

    commonTags();

    if (!check.checked) {
        // validate father full name
        domObject = document.getElementById('fatherFullName');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'p2error1');
        }
        // validate father full name in english
        domObject = document.getElementById('fatherFullNameInEnglish');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'fatherNameEnglish');
        }
        // validate father's race
        domObject = document.getElementById('fatherRaceId');
        if (domObject.value == 0) {
            errormsg = errormsg + "\n" + document.getElementById('error18').value;
        }

        // validate mother full name
        domObject = document.getElementById('motherFullName');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'p2error2');
        }
        // validate mother full name in english
        domObject = document.getElementById('motherFullNameInEnglish');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'motherNameEnglish');
        }

        // validate mother date of birth
        domObject = document.getElementById('motherDatePicker');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'mother_birth_day_empty');
        }

        // validate mother age at birth
        domObject = document.getElementById('motherAgeAtBirth');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'mother_age');
        }
        // validate mother's race
        domObject = document.getElementById('motherRaceId');
        if (domObject.value == 0) {
            errormsg = errormsg + "\n" + document.getElementById('error19').value;
        }

        validateBirthYearWithNIC("father_pinOrNic", "fatherDatePicker", "error7");
        validateBirthYearWithNIC("mother_pinOrNic", "motherDatePicker", "error8");
    }

    // validate mother phone number
    domObject = document.getElementById('motherPhoneNo');
    if (!isFieldEmpty(domObject))
        validatePhoneNo(domObject, 'error2', 'error6')

    // validate mother email address
    domObject = document.getElementById('motherEmail');
    if (!isFieldEmpty(domObject))
        validateEmail(domObject, 'error2', 'error1')

    var out = checkActiveFieldsForSyntaxErrors('birth-registration-form-2');
    if(out != ""){
        errormsg = errormsg + out;
    }

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

// check parent age is between 10 to 89
function checkParentAge(domValue, errorText, errorCode) {
    var reg = /^[1-8][0-9]$/;
    if (reg.test(domValue) == false) {
        printMessage(errorText, errorCode);
    }
}

function checkFatherAge(domElement, errorText, errorCode) {
    with (domElement) {
        var father_bday = new Date(value);
        var today = new Date();
        var father_age = today.getYear() - father_bday.getYear();
        checkParentAge(father_age, errorText, errorCode);
    }
}

function motherAgeBirth() {
    var child_bday = new Date(document.getElementById('childDateOfBirth').value);
    var mother_bday = new Date(document.getElementById('motherDatePicker').value);
    var mother_age = calculateAge(mother_bday, child_bday);
    document.getElementById("motherAgeAtBirth").value = mother_age[0];
    if (mother_age[0] <= 0 || isNaN(mother_bday)) {
        document.getElementById("motherAgeAtBirth").value = 0;
    }
}

function initPage() {

}

function commonTags() {
    var domObject;

    // validate father PIN or NIC
    domObject = document.getElementById('father_pinOrNic');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'error2', 'error4');

    // validate father passport number
    domObject = document.getElementById('fatherPassportNo');
    if (!isFieldEmpty(domObject)) {
        if (validatePassportNo(domObject, 'error2', 'error12')) {
            domObject = document.getElementById('fatherCountryId');
            if (domObject.value == 0) {
                errormsg = errormsg + "\n" + document.getElementById('error16').value;
            }
        }
    }
    domObject = document.getElementById('fatherCountryId');
    if (domObject.value != 0) {
        domObject = document.getElementById('fatherPassportNo');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, '', 'error14');
        }
    }

    // validate father date of birth
    domObject = document.getElementById('fatherDatePicker');
    if (!isFieldEmpty(domObject))
        checkFatherAge(domObject, 'error2', 'fatherDOB');

    // validate mother PIN or NIC
    domObject = document.getElementById('mother_pinOrNic');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'error2', 'error5');

    // validate mother passport number
    domObject = document.getElementById('motherPassportNo');
    if (!isFieldEmpty(domObject)) {
        if (validatePassportNo(domObject, 'error2', 'error13')) {
            domObject = document.getElementById('motherCountryId');
            if (domObject.value == 0) {
                errormsg = errormsg + "\n" + document.getElementById('error17').value;
            }
        }
    }
    domObject = document.getElementById('motherCountryId');
    if (domObject.value != 0) {
        domObject = document.getElementById('motherPassportNo');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, '', 'error15');
        }
    }

    // validate mother date of birth
    domObject = document.getElementById('motherDatePicker');
    if (!isFieldEmpty(domObject)) {
        isDate(domObject.value, "error2", "motherDOB");
    }

    // validate mother age at birth
    domObject = document.getElementById('motherAgeAtBirth');
    if (!isFieldEmpty(domObject))
        checkParentAge(domObject.value, 'error2', 'mother_age');

    // validate hospital addmission date
    domObject = document.getElementById('admitDatePicker');
    if (!isFieldEmpty(domObject)) {
        isDate(domObject.value, "error2", "dateOfAddmission");
        var submit = new Date(document.getElementById('birthDatePicker').value);
        domObject = new Date(domObject.value);
        if (domObject.getTime() > submit.getTime()) {
            errormsg = errormsg + "\n" + document.getElementById('dateOfAddmissionWrong').value;
        }
    }
}

function maxLengthCalculate(id, max, divId) {
    var dom = document.getElementById(id).value;
    if (dom.length > max) {
        document.getElementById(divId).innerHTML = document.getElementById('maxLengthError').value + " : " + max
    }
    else {
        document.getElementById(divId).innerHTML = "";
    }
}

</script>

<div class="birth-registration-form-outer" id="birth-registration-form-2-outer">
<s:form action="eprBirthRegistration.do" name="birthRegistrationForm2" id="birth-registration-form-2" method="POST"
        onsubmit="javascript:return validate()">

<table class="table_reg_page_02" cellspacing="0">
    <caption></caption>
    <col width="200px"/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td colspan="9" style="text-align:center;font-size:12pt;">පියාගේ විස්තර
            <br>தந்தை பற்றிய தகவல்
            <br>Details of the Father
        </td>
    </tr>
    <tr>
        <td>
            (<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>)
            ශ්‍රී ලාංකිකයෙකු නම්<br/>இலங்கையராயின் <br/>If Sri Lankan
        </td>
        <td colspan="2"><label>අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification Number</label></td>
        <td colspan="5" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="father_NIC_V" onclick="javascript:addXorV('father_pinOrNic','V','error9')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="father_NIC_X" onclick="javascript:addXorV('father_pinOrNic','X','error9')">
            <br>
            <s:textfield id="father_pinOrNic" name="parent.fatherNICorPIN" maxLength="12"/>
            <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;" id="father_lookup">
        </td>
    </tr>
    <tr>
        <td rowspan="2"><label>
            (<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>)
            විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If a foreigner</label>
        </td>
        <td colspan="2"><label>රට<br>நாடு<br>Country</label></td>
        <td colspan="5">
            <s:select id="fatherCountryId" name="fatherCountry" list="countryList" headerKey="0"
                      headerValue="%{getText('select_country.label')}" cssStyle="width:97%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு இல.<br>Passport Number</label></td>
        <td colspan="5" class="passport"><s:textfield name="parent.fatherPassportNo" id="fatherPassportNo"
                                                      maxLength="15"/></td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>)
            සම්පුර්ණ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
            <br>முழுப் பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
            <br>Full Name in any of the official languages (Sinhala / Tamil)</label></td>
        <td colspan="7">
            <s:textarea name="parent.fatherFullName" id="fatherFullName" cssStyle="width:98%;"
                        onblur="maxLengthCalculate('fatherFullName','600','fatherFullName_div');"/>
            <div id="fatherFullName_div" style="color:red;font-size:8pt"></div>
        </td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>) සම්පුර්ණ
            නම ඉංග්‍රීසි භාෂාවෙන් (කැපිටල් අකුරෙන්)
            <br>முழுப் பெயர் ஆங்கில மொழியில்(பெரிய எழுத்துக்களில்)<br>Full Name in English (In block letters)</label>
        </td>
        <td colspan="7">
            <s:textarea name="parent.fatherFullNameInEnglish" id="fatherFullNameInEnglish" cssStyle="width:98%;"
                        onblur="maxLengthCalculate('fatherFullNameInEnglish','600','fatherFullNameInEnglish_div');"/>
            <div id="fatherFullNameInEnglish_div" style="color:red;font-size:8pt"></div>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;"
                 id="name_english_father">
        </td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>) උපන්
            දිනය <br>பிறந்த
            திகதி <br>Date of Birth</label></td>
        <td colspan="2">
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br/>
            <s:textfield name="parent.fatherDOB" id="fatherDatePicker" cssStyle="float:left;margin-left:5px;"
                         maxLength="10"/>
        </td>
        <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                   value="#i+1"/>)
            ජන වර්ගය <br/>
            இனம்<br/>
            Ethnic Group</label></td>
        <td colspan="2" class="table_reg_cell_02">
            <s:select id="fatherRaceId" list="raceList" name="fatherRace" headerKey="0"
                      headerValue="%{getText('select_race.label')}"
                      cssStyle="width:95%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                   value="#i+1"/>) උපන්
            ස්ථානය <br>பிறந்த இடம்
            <br>Place of Birth</label></td>
        <td colspan="7"><s:textfield id="fatherPlaceOfBirth" name="parent.fatherPlaceOfBirth"
                                     cssStyle="width:97%;"/></td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_02" cellspacing="0">
    <caption></caption>
    <col width="200px"/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td colspan="9" style="text-align:center;font-size:12pt">මවගේ විස්තර
            <br>தாய் பற்றிய தகவல்
            <br>Details of the Mother
        </td>
    </tr>
    <tr>
        <td>
            (<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>)
            ශ්‍රී ලාංකිකයෙකු නම්<br/>இலங்கையராயின் <br/>If Sri Lankan
        </td>
        <td colspan="2"><label>අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification Number</label></td>
        <td colspan="5" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="mother_NIC_V" onclick="javascript:addXorV('mother_pinOrNic','V','error9')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="mother_NIC_X" onclick="javascript:addXorV('mother_pinOrNic','X','error9')">
            <br>
            <s:textfield id="mother_pinOrNic" name="parent.motherNICorPIN" maxLength="12"/>
            <img src="<s:url value="/images/search-mother.png"/>" style="vertical-align:middle;" id="mother_lookup">
        </td>
    </tr>
    <tr>
        <td rowspan="2"><label>
            (<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>)
            විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If a foreigner</label>
        </td>
        <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
        <td colspan="5"><s:select id="motherCountryId" name="motherCountry" list="countryList" headerKey="0"
                                  headerValue="%{getText('select_country.label')}" cssStyle="width:97%;"/></td>
    </tr>
    <tr>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு இல.<br>Passport Number</label></td>
        <td colspan="5" class="passport"><s:textfield name="parent.motherPassportNo" id="motherPassportNo"
                                                      maxLength="15"/></td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>)
            සම්පුර්ණ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
            <br>முழுப் பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
            <br>Full Name in any of the official languages (Sinhala / Tamil)</label></td>
        <td colspan="7">
            <s:textarea name="parent.motherFullName" id="motherFullName" cssStyle="width:98%;"
                        onblur="maxLengthCalculate('motherFullName','600','motherFullName_div');"/>
            <div id="motherFullName_div" style="color:red;font-size:8pt"></div>
        </td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>) සම්පුර්ණ
            නම ඉංග්‍රීසි භාෂාවෙන් (කැපිටල් අකුරෙන්)
            <br>முழுப் பெயர் ஆங்கில மொழியில்(பெரிய எழுத்துக்களில்)<br>Full Name in English (In block letters)</label>
        </td>
        <td colspan="7">
            <s:textarea name="parent.motherFullNameInEnglish" id="motherFullNameInEnglish" cssStyle="width:98%;"
                        onblur="maxLengthCalculate('motherFullNameInEnglish','600','motherFullNameInEnglish_div');"/>
            <div id="motherFullNameInEnglish_div" style="color:red;font-size:8pt"></div>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px"
                 id="name_english_mother">
        </td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                       value="#i+1"/>)
            උපන් දිනය
            <br>பிறந்த திகதி
            <br>Date of Birth</label></td>
        <td colspan="1">
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
                <s:textfield name="parent.motherDOB" id="motherDatePicker" cssStyle="float:left;margin-left:5px;"
                             maxLength="10"/>
        <td colspan="3" width="100px"><label>
            <s:if test="%{#session.birthRegister.register.birthType.ordinal() != 0}">
                (<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>)
                ළමයාගේ උපන් දිනට වයස
                <br>பிள்ளை பிறந்த திகதியில் மாதாவின் வயது
                <br>Age as at the date of birth of child
            </s:if>
            <s:else>
                (<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>)
                මළ උපත් දිනට වයස
                <br>பிள்ளை பிறந்த திகதியில் மாதாவின் வயது
                <br>Age as at the date of still-birth
            </s:else>
        </label>
        </td>
        <td class="passport">
            <s:textfield name="parent.motherAgeAtBirth" id="motherAgeAtBirth" onfocus="motherAgeBirth();"
                         maxLength="2"/>
            <div id="motherAgeAtChildBirth" style="color:red;"/>
        </td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>)
            ජන වර්ගය <br/>
            இனம்<br/>
            Ethnic Group</label></td>
        <td colspan="2"><s:select id="motherRaceId" list="raceList" name="motherRace" headerKey="0"
                                  headerValue="%{getText('select_race.label')}"/></td>

        <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                   value="#i+1"/>
            ) උපන් ස්ථානය <br>பிறந்த இடம்
            <br>Place of Birth</label></td>
        <td colspan="3" class="passport"><s:textfield id="motherPlaceOfBirth"  name="parent.motherPlaceOfBirth"/></td>
    </tr>
    <tr>
        <td rowspan="4"><label>(<s:property value="#row"/><s:set name="row"
                                                                 value="#row+1"/><s:set
                name="i" value="#i+1"/>) ස්ථිර ලිපිනය<br>தாயின் நிரந்தர வதிவிட முகவரி<br>Permanent Address</label>
        </td>
        <td colspan="7">
            <s:textarea name="parent.motherAddress" id="motherAddress" cssStyle="width:98%;"
                        onblur="maxLengthCalculate('motherAddress','255','motherAddress_div');"/>
            <div id="motherAddress_div" style="color:red;font-size:8pt"></div>
        </td>
    <tr>
        <td colspan="2" class="table_reg_cell_02"><label>දිස්ත්‍රික්කය /<br>மாவட்டம்
            /<br>District</label></td>
        <td colspan="5" class="table_reg_cell_02">
            <s:if test="#parent.motherDSDivision.district.districtUKey >0">
            <s:select id="motherDistrictId" name="motherDistrictId" list="allDistrictList"
                      cssStyle="width:99%;"/></td>
        </s:if>
        <s:else>
            <s:select id="motherDistrictId" name="motherDistrictId" list="allDistrictList" headerKey="0"
                      headerValue="%{getText('select_district.label')}" cssStyle="width:99%;"/></td>
        </s:else>
    </tr>
    <tr>
        <td colspan="2"><label>ප්‍රාදේශීය ලේකම් කොට්ඨාශය /<br>
            பிரதேச செயளாளர் பிரிவு <br/>
            Divisional Secretary Division
        </label>
        </td>
        <td colspan="5" class="table_reg_cell_02">
            <s:select id="motherDSDivisionId" name="motherDSDivisionId" list="allDSDivisionList"
                      headerKey="0" headerValue="%{getText('select_ds_division.label')}" cssStyle="width:99%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            ග්‍රාම නිලධාරී කොට්ඨාශය/<br/>
            கிராம சேவையாளர் பிரிவு/<br/>
            Grama Niladhari Division
        </td>
        <td colspan="5" align="center">
            <s:select id="gnDivisionId" name="gnDivisionId" value="%{gnDivisionId}" list="gnDivisionList"
                      cssStyle="float:left;  width:99%; margin:2px 5px;" headerKey="0"
                      headerValue="%{getText('select.gn.division')}"/>
            <a href="javascript:displayGNSearch()">
                <span><s:label value="%{getText('searchGNDivision.label')}"/></span>
            </a>
        </td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/> <s:set name="i" value="#i+1"/>)
            සම්බන්ධ කල හැකි තොරතුරු
            <br>தொடர்பு கொள்ளக்கூடிய தகவல்கள்
            <br>Contact Details </label></td>
        <td colspan="1"><label>දුරකතනය <br>தொலைபேசி இலக்கம் <br> Telephone</label></td>
        <td colspan="1"><s:textfield id="motherPhoneNo" name="parent.motherPhoneNo" maxLength="10"/></td>
        <td colspan="2"><label>ඉ – තැපැල <br> மின்னஞ்சல்<br>Email</label></td>
        <td colspan="3" class="passport"><s:textfield name="parent.motherEmail" id="motherEmail"
                                                      cssStyle="text-transform:none;"/></td>
    </tr>
    <tr>
        <td colspan="3" rowspan="2">(<s:property value="#row"/><s:set name="row" value="#row+1"/> <s:set name="i"
                                                                                                         value="#i+1"/>)
            රෝහලට ඇතුලත් කිරිමේ තොරතුරු (ඔබ සතුව පවතී නම්)<br/>
            வைத்தியசாலை அனுமதி இலக்கமும் திகதியும் (இருந்தால்)<br/>
            Hospital Admission Details (if available)
        </td>
        <td colspan="2"><label>අංකය / இலக்கம் / Number </label></td>
        <td colspan="4" class="passport"><s:textfield name="parent.motherAdmissionNo" maxLength="15"/></td>
    </tr>
    <tr>
        <td colspan="2"><span>දිනය/ திகதி / Date</span>
        <td colspan="3">
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
            <s:textfield name="parent.motherAdmissionDate" id="admitDatePicker" cssStyle="float:left;margin-left:5px;"
                         maxLength="10"/>
        </td>
    </tr>
    </tbody>
</table>

<s:hidden name="pageNo" value="2"/>
<s:hidden name="rowNumber" value="%{row}"/>
<s:hidden name="counter" value="%{i}"/>
<div class="skip-validation">
    <s:checkbox name="skipjavaScript" id="skipjs" value="false">
        <s:label value="%{getText('skipvalidation.label')}"/>
    </s:checkbox>
</div>

<div class="form-submit">
    <s:submit value="%{getText('next.label')}"/>
</div>
<div class="next-previous">
    <s:url id="backUrl" action="eprBirthRegistration">
        <s:param name="back" value="true"/>
        <s:param name="pageNo" value="{pageNo - 1}"/>
    </s:url>
    <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
<s:set value="0" name="counter"/>
</s:form>

<s:hidden id="error1" value="%{getText('p1.invalid.emailMother.text')}"/>
<s:hidden id="error2" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error3" value="%{getText('p2.motherAgeAthBirthBelowZero.error')}"/>
<s:hidden id="error4" value="%{getText('fatherPINorNIC.label')}"/>
<s:hidden id="error5" value="%{getText('motherPINorNIC.label')}"/>
<s:hidden id="error6" value="%{getText('motherPhoneNo.label')}"/>
<s:hidden id="error7" value="%{getText('p2.father.NIC.DOB.notMatch')}"/>
<s:hidden id="error8" value="%{getText('p2.mother.NIC.DOB.notMatch')}"/>
<s:hidden id="fatherDOB" value="%{getText('p2.father.dob')}"/>
<s:hidden id="motherDOB" value="%{getText('p2.mother.dob')}"/>
<s:hidden id="dateOfAddmission" value="%{getText('p2.hospital.addmission.date')}"/>
<s:hidden id="dateOfAddmissionWrong" value="%{getText('p2.hospital.addmission.date.wrong')}"/>
<s:hidden id="birthDatePicker" value="%{child.dateOfBirth}"/>
<s:hidden id="error9" value="%{getText('NIC.error.add.VX')}"/>
<s:hidden id="error10" value="%{getText('p2.NIC.error.format.father')}"/>
<s:hidden id="error11" value="%{getText('p2.NIC.error.format.mother')}"/>
<s:hidden id="error12" value="%{getText('fatherPassportNo.label')}"/>
<s:hidden id="error13" value="%{getText('motherPassportNo.label')}"/>
<s:hidden id="error14" value="%{getText('enter.fatherPassportNo.label')}"/>
<s:hidden id="error15" value="%{getText('enter.motherPassportNo.label')}"/>
<s:hidden id="error16" value="%{getText('enter.fatherCountry.label')}"/>
<s:hidden id="error17" value="%{getText('enter.motherCountry.label')}"/>
<s:hidden id="selectGNDivision" value="%{getText('select.gn.division')}"/>
<s:hidden id="maxLengthError" value="%{getText('error.max.length')}"/>
<s:hidden id="selectDSDivision" value="%{getText('select_DS_division1.label')}"/>
</div>
<%-- Styling Completed --%>