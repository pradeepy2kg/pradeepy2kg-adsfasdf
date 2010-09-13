<%--@author Tharanga Punchihewa--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>
<script type="text/javascript">
$(function() {
    $('select#birthDistrictId').bind('change', function(evt1) {
        var id = $("select#birthDistrictId").attr("value");
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
                    $("select#birthDivisionId").html(options2);
                });
    });

    $('select#dsDivisionId').bind('change', function(evt2) {
        var id = $("select#dsDivisionId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                function(data) {
                    var options = '';
                    var bd = data.bdDivisionList;
                    for (var i = 0; i < bd.length; i++) {
                        options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                    }
                    $("select#birthDivisionId").html(options);
                });
    });
    $('select#childBirthDistrictId').bind('change', function(evt1) {
        var id = $("select#childBirthDistrictId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
                function(data) {
                    var options1 = '';
                    var ds = data.dsDivisionList;
                    for (var i = 0; i < ds.length; i++) {
                        options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                    }
                    $("select#childDsDivisionId").html(options1);

                    var options2 = '';
                    var bd = data.bdDivisionList;
                    for (var j = 0; j < bd.length; j++) {
                        options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                    }
                    $("select#childBirthDivisionId").html(options2);
                });
    });

    $('select#childDsDivisionId').bind('change', function(evt2) {
        var id = $("select#childDsDivisionId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                function(data) {
                    var options = '';
                    var bd = data.bdDivisionList;
                    for (var i = 0; i < bd.length; i++) {
                        options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                    }
                    $("select#childBirthDivisionId").html(options);
                });
    });
});
$(function() {
    $('#mother-info-min').click(function() {
        minimize("mother-info");
    });
    $('#mother-info-max').click(function() {
        maximize("mother-info");
    });
    $('#informant-info-min').click(function() {
        minimize("informant-info");
    });
    $('#informant-info-max').click(function() {
        maximize("informant-info");
    });
    $('#father-info-min').click(function() {
        minimize("father-info");
    });
    $('#father-info-max').click(function() {
        maximize("father-info");
    });
    $('#marriage-info-min').click(function() {
        minimize("marriage-info");
    });
    $('#marriage-info-max').click(function() {
        maximize("marriage-info");
    });
    $('#grandFather-info-min').click(function() {
        minimize("grandFather-info");
    });
    $('#grandFather-info-max').click(function() {
        maximize("grandFather-info");
    });
    $('#errors-info-min').click(function() {
        minimize("errors-info");
    });
    $('#errors-info-max').click(function() {
        maximize("errors-info");
    });
    $('#error-explanation-info-min').click(function() {
        minimize("error-explanation-info");
    });
    $('#error-explanation-info-max').click(function() {
        maximize("error-explanation-info");
    });
    $('#mother-after-marriage-info-min').click(function() {
        minimize("mother-after-marriage-info");
    });
    $('#mother-after-marriage-info-max').click(function() {
        maximize("mother-after-marriage-info");
    });
    $('#header-info-min').click(function() {
        minimize("header-info");
    });
    $('#header-info-max').click(function() {
        maximize("header-info");
    });
    $('select#sectionOfAct').change(function() {
        var id = $("select#sectionOfAct").attr("value");
        document.getElementById("actNumber1").style.display = 'none';
        document.getElementById("actNumber2").style.display = 'none';
        document.getElementById("actNumber3").style.display = 'none';
        document.getElementById("actNumber" + id).style.display = 'block';

    });
});
function minimize(id) {
    document.getElementById(id).style.display = 'none';
    document.getElementById(id + "-min").style.display = 'none';
    document.getElementById(id + "-max").style.display = 'block';

}

function maximize(id, click) {
    document.getElementById(id).style.display = 'block';
    document.getElementById(id + "-max").style.display = 'none';
    document.getElementById(id + "-min").style.display = 'block';
}

function initPage() {
    var idNames = new Array('errors-info', 'mother-info', 'informant-info', 'father-info',
            'marriage-info', 'grandFather-info', 'error-explanation-info', 'mother-after-marriage-info', 'header-info');
    for (var i = 0; i < idNames.length; i++) {
        document.getElementById(idNames[i]).style.display = 'none';
        document.getElementById(idNames[i] + "-min").style.display = 'none';
    }
}

$(function() {
    $("#fatherDadeOfbirth").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});
$(function() {
    $("#dateOfMarriage").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});
$(function() {
    $("#motherDateOfBirth").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});
$(function() {
    $("#childBirthDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});
$(function() {
    $("#acceptanceDate").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});
var errormsg = "";

function validate() {
    var act = document.getElementById("sectionOfAct").value;
    var domObject;
    var returnval = true;
    /*validation of common fields*/
    domObject = document.getElementById('bdfSerialNo');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, "", 'comError3');
    else
        validateSerialNo(domObject, "comError1", 'comError2');
    domObject = document.getElementById('acceptanceDate');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, "", 'childError3')
    else
        isDate(domObject.value, "comError1", "comError4");
    domObject = document.getElementById('placeOfBirth');
    /*validation of the Act 27*/
    if (act == 1) {
        /*validation for child's information */
        domObject = document.getElementById('nameInOfficialLanguages');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'childError1');
        }
        domObject = document.getElementById('nameInEnglish');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'childError2');
        }
    }
    /*validation of the Act 52*/
    if (act == 2) {
        domObject = document.getElementById('childBirthDatePicker');
        if (isFieldEmpty(domObject))
            isEmpty(domObject, "", 'childError3')
        else
            isDate(domObject.value, "comError1", "childError4");
        domObject = document.getElementById('placeOfBirth');
        isEmpty(domObject, "", 'childError5');

        /*validation of mother's information*/
        domObject = document.getElementById('mother_pinOrNic');
        if (!isFieldEmpty(domObject))
            validatePINorNIC(domObject, 'comError1', 'motherError1');
        domObject = document.getElementById('motherDateOfBirth');
        if (!isFieldEmpty(domObject)) {
            isDate(domObject.value, "comError1", "motherError2");
        }
        domObject = document.getElementById('Declarant_pinOrNic');
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject.value, "comError1", "error12");
        }
        domObject = document.getElementById('motherAgeAtBirth');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'motherError3');
        }

        /*validation for informent's information */
        domObject = document.getElementById('informentName');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'informentError1');
        }
        domObject = document.getElementById('informentAddress');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'informentError2');
        }
        domObject = document.getElementById('informent_pinOrNic');
        if (!isFieldEmpty(domObject))
            validatePINorNIC(domObject, 'comError1', 'informentError3');
    }
    /*validation of the Act 27A*/
    if (act == 3) {
        /* validation of father's information */
        domObject = document.getElementById('father_pinOrNic');
        if (!isFieldEmpty(domObject))
            validatePINorNIC(domObject, 'comError1', 'fatherError1');
        domObject = document.getElementById('fatherName');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'fatherError2');
        }
        domObject = document.getElementById('fatherDadeOfbirth');
        if (!isFieldEmpty(domObject))
            isDate(domObject.value, "comError1", "fatherError3");

        /*validation of Grand Father's validation*/
        domObject = document.getElementById('grandFather_pinOrNic');
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, "comError1", "grandFatherError1");
        }
        domObject = document.getElementById('grandFatherBirthYear');
        if (!isFieldEmpty(domObject))
            validateBirthYear(domObject, 'comError1', 'grandFatherError2');


        /*validation of Grand Grand Father's validation*/
        domObject = document.getElementById('grandGrandFather_pinOrNic');
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, "comError1", "grandGrandFatherError1");
        }
        domObject = document.getElementById('grandGrandFatherBirthYear');
        if (!isFieldEmpty(domObject))
            validateBirthYear(domObject, 'comError1', 'grandGrandFatherError2');
    }
    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

function validateBirthYear(domElement, errorText, errorCode) {
    with (domElement) {
        var reg = /^([1-9][0-9]{3})$/;
        if (reg.test(value) == false) {
            printMessage(errorText, errorCode);
        }
    }
}
</script>
<s:if test="pageNo==0">
    <div id="birth-confirmation-search">
        <s:actionerror cssClass="alreadyPrinted"/>
        <s:form action="eprBirthAlterationSearch.do" method="post" onsubmit="javascript:return validate()">
            <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
                <legend>
                    <b><s:label name="confirmatinSearchLegend"
                                value="%{getText('registrationSerchLegend3.label')}"/><s:actionerror/></b>
                </legend>
                <table class="search-option-table">
                    <tr>
                        <td width="350px"><s:label name="confirmationSearch"
                                                   value="%{getText('certificateNumber.lable')}"/></td>
                        <td width="250px"><s:textfield name="idUKey" id="bdfSerialNoId2"/></td>
                        <td>
                            <s:hidden name="pageNo" value="1"/>
                            <div class="form-submit"><s:submit value="%{getText('bdfSearch.button')}"
                                                               name="search"/></div>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </s:form>
        <s:form action="eprBirthAlterationSearch.do" method="post">
            <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
                <legend>
                    <b><s:label name="confirmatinSearchLegend"
                                value="%{getText('registrationSerchLegend2.label')}"/></b>
                </legend>
                <table class="search-option-table">
                    <tr>
                        <td width="350px"><s:label name="confirmationSearch"
                                                   value="%{getText('idNumber.lable')}"/></td>
                        <td width="250px"><s:textfield name="nicOrPin" id="bdfSerialNoId2"/></td>
                        <td>
                            <s:hidden name="pageNo" value="2"/>
                            <div class="form-submit"><s:submit value="%{getText('bdfSearch.button')}"
                                                               name="search"/></div>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </s:form>
        <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
            <legend>
                <b><s:label name="registrationSerchLegend"
                            value="%{getText('registrationSerchLegend1.label')}"/></b>
            </legend>
            <s:form action="eprBirthAlterationSearch.do" method="post">
            <table class="search-option-table">
                <caption></caption>
                <col/>
                <col/>
                <col/>
                <col/>
                <col/>
                <tbody>
                <tr>
                    <td><s:label value="%{getText('searchDeclarationSearial.label')}"/></td>
                    <td><s:textfield name="serialNo" id="bdfSerialNoId1"/></td>
                    <td><s:label value="%{getText('district.label')}"/></td>
                    <td>
                        <s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                                  value="birthDistrictId" cssStyle="width:240px;float:right;"/>
                    </td>
                </tr>
                <tr>
                    <td><s:label name="division" value="%{getText('select_DS_division.label')}"/></td>
                    <td>
                        <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  value="%{dsDivisionId}"
                                  cssStyle="float:left;  width:240px;"/>
                    </td>
                    <td><s:label value="%{getText('select_BD_division.label')}"/></td>
                    <td>
                        <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}"
                                  list="bdDivisionList" cssStyle=" width:240px;float:right;"
                                  headerValue="%{getText('all.divisions.label')}" headerKey="0"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="3"></td>
                    <td>
                        <s:hidden name="pageNo" value="3"/>
                        <div class="form-submit"><s:submit value="%{getText('bdfSearch.button')}"
                                                           name="search"/></div>
                    </td>
                </tr>
                </tbody>
            </table>
        </fieldset>
        </s:form>
        <br/>

    </div>
</s:if>
<s:if test="pageNo==1">
<div id="birth-alteration-outer">
<s:form action="eprBirthAlteration.do" onsubmit="javascript:return validate()">
<table class="birth-alteration-table-style01" style="width:1030px;">
    <tr>
        <td width="30%"></td>
        <td width="35%" style="text-align:center;"><img src="<s:url value="/images/official-logo.png"/>"
                                                        alt=""/></td>
        <td width="35%">
            <table class="birth-alteration-table-style02" cellspacing="0" style="float:right;width:100%">
                <tr>
                    <td colspan="2" style="text-align:center;">කාර්යාල ප්‍රයෝජනය සඳහා පමණි / <br>
                        அலுவலக பாவனைக்காக மட்டும் / <br>
                        For office use only
                    </td>
                </tr>
                <tr>
                    <td width="40%">අනුක්‍රමික අංකය <br>
                        தொடர் இலக்கம் <br>
                        Serial Number
                    </td>
                    <td width="60%"><s:textfield id="bdfSerialNo" name="register.bdfSerialNo"/></td>
                </tr>
                <tr>
                    <td>භාරගත් දිනය <br>
                        பிறப்பைப் பதிவு திகதி <br>
                        Date of Acceptance
                    </td>
                    <td><s:textfield id="acceptanceDate" name="register.dateOfRegistration"/></td>
                </tr>
                <tr>
                    <td>පනතේ වගන්තිය <br>
                        பிறப்பைப் <br>
                        Section of the Act
                    </td>
                    <td><s:select
                            list="#@java.util.HashMap@{'1':'27','2':'52(1)','3':'27 (A)'}"
                            name="sectionOfAct" cssStyle="width:190px; margin-left:5px;" id="sectionOfAct"/></td>

                </tr>
            </table>
        </td>
    </tr>
</table>
<div class="birth-alteration-minimize-icon" id="header-info-min"></div>
<div class="birth-alteration-maximize-icon" id="header-info-max"></div>
<div id="header-info">
    <table class="birth-alteration-table-style01" style="width:1030px;">
        <tr>
            <td colspan="3" style="font-size:11pt;text-align:center;">උප්පැන්න ලියාපදිංචි කිරීමේ සටහනක විස්තර වෙනස්
                කිරීම / ඇතුලත් කිරීම හෝ අතහැරීම <br>
                ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள் <br>
                Alteration / insertion / omission of particulars from a Birth Registration entry
            </td>
        </tr>
        <tr>
            <td colspan="3" style="text-align:center;">උප්පැන්න හා මරණ ලියාපදිංචි කිරීමේ පනත යටතේ
                උප්පැන්න ලියාපදිංචි කිරීමේ සටහනක විස්තර වෙනස්
                කිරීම / ඇතුලත් කිරීම හෝ අතහැරීම සඳහා කරනු ලබන ප්‍රකාශය <br>
                in tamil <br>
                Declaration for the alteration / insertion / omission of particulars of a birth registration entry under
                the Births and Deaths Registration Act.
            </td>
        </tr>
    </table>
    <table class="birth-alteration-table-style01" style="width:100%;">
        <tr>
            <td style="text-align:center;font-size:11pt;">
                වෙනස් කිරීම / ඇතුලත් කිරීම හෝ අතහැරීම පිලිබඳ විස්තර <br>
                பிள்ளை பற்றிய தகவல் <br>
                Particulars about the alteration / insertion / omission
            </td>
        </tr>
        <tr>
            <td style="text-align:center;"> වෙනස් කිරීම / ඇතුලත් කිරීම සඳහා නිවැරදි විය යුතු ආකාරය අදාළ
                කොටුවේ සඳහන් කරන්න. ඉවත් කිරීමක් සඳහා "ඉවත්
                කරන්න" යන්න අදාළ කොටුවේ සඳහන් කරන්න <br>
                in Tamil <br>
                For alteration / insertion state the entry value as it should appear. For omission, state “omit” within
                the relavent cage.
            </td>
        </tr>
    </table>
</div>
<div id="actNumber1">
    <table class="birth-alteration-table-style01" style="width:100%">
        <tr>
            <td colspan="2" style="text-align:center;font-size:11pt;border-bottom:none;">නම ඇතුලත් කිරීම හෝ වෙනස් කිරීම
                (27
                වගන්තිය) <br>
                தந்தை பற்றிய தகவல் <br>
                Insertion or Alteration of the Name (Section 27)
            </td>
        </tr>
    </table>
    <table class="birth-alteration-table-style02" style="width:100%" cellpadding="0" cellspacing="0">
        <caption></caption>
        <col width="250px"/>
        <col width="760px"/>
        <tbody>
        <tr>
            <td colspan="2" style="text-align:center;">මෙම වගන්තිය යටතේ මව්පියන් හෝ භාරකරු හට අවුරුදු 21 කට අඩු
                දරුවකුගේ නම වෙනස් කිරීමට ඉල්ලීම් කල හැක. අවු
                රුදු 21 කට වැඩි පුද්ගලයෙකුගේ නම වෙනස් කිරීමට ඔහු විසින් ඉල්ලුම් පත්‍රයක් ඉදිරිපත් කල හැක. <br>
                தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை
                பற்றிய தகவல் தந்தை பற்றிய தகவல் <br>
                Under this section the name change of a child under 21 years could be requested by his parents or
                the
                guardian. A person over 21 years in age could request for a name change on his own.
            </td>
        </tr>
        <tr>
            <td>නම රාජ්‍ය භාෂාවෙන්
                (සිංහල / දෙමළ) <br>
                பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்) <br>
                Name in any of the official languages (Sinhala / Tamil)
            </td>
            <td><s:textarea name="alt27.childFullNameOfficialLang" id="nameInOfficialLanguages"/></td>
        </tr>
        <tr>
            <td>නම ඉංග්‍රීසි භාෂාවෙන් <br>
                பிறப்பு அத்தாட்சி ….. <br>
                Name in English
            </td>
            <td><s:textarea name="alt27.childFullNameEnglish" id="nameInEnglish"/></td>
        </tr>
        </tbody>
    </table>
</div>
<div id="actNumber2">
<table class="birth-alteration-table-style01" style="text-align:center;width:100%">
    <tr>
        <td style="font-size:11pt;text-align:center;" colspan="7">උප්පැන සහතිකයක දෝෂ නිවැරදි කිරීම (52 (1) වගන්තිය) <br>
            தந்தை பற்றிய தகவல் <br>
            Correction of Errors of a Birth Certificate (Section 52 (1))
    </tr>
</table>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td style="font-size:11pt;text-align:center;" colspan="7">ළම‌යාගේ විස්තර <br>
            பிள்ளை பற்றிய தகவல் <br>
            Child's Information
            <div class="birth-alteration-minimize-icon" id="errors-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="errors-info-max"></div>
        </td>
    </tr>
</table>
<div id="errors-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;border-top:none;" cellpadding="0"
           cellspacing="0">
        <caption></caption>
        <col width="250px;"/>
        <col width="125px;"/>
        <col width="125px;"/>
        <col width="125px;"/>
        <col width="125px;"/>
        <col width="125px;"/>
        <tbody>
        <tr>
            <td>උපන් දිනය <br>
                பிறந்த திகதி <br>
                Date of Birth
            </td>
            <td colspan="5"><s:textfield cssStyle="width:150px;" id="childBirthDatePicker"
                                         name="alt52_1.dateOfBirth"/></td>
        </tr>
        <tr>
            <td rowspan="5">උපන් ස්ථානය<br>
                பிறந்த இடம்<br>
                Place of Birth
            </td>
            <td colspan="2">දිස්ත්‍රික්කය / <br>
                மாவட்டம் /<br>
                District
            </td>
            <td colspan="4"><s:select id="childBirthDistrictId" name="birthDistrictId" list="districtList"
                                      value="districtId"
                                      cssStyle="width:95%"/></td>
        </tr>
        <tr>
            <td colspan="2">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>
                பிரிவு / <br>
                Divisional Secretariat
            </td>
            <td colspan="4"><s:select id="childDsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                      value="%{dsDivisionId}"
                                      cssStyle="float:left;  width:95%;"/></td>
        </tr>
        <tr>
            <td colspan="2">ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br>
                பிரிவு / <br>
                Registration Division
            </td>
            <td colspan="4"><s:select id="childBirthDivisionId" name="birthDivisionId" value="%{birthDivisionId}"
                                      list="bdDivisionList"
                                      cssStyle="float:left;  width:95%; "/></td>
        </tr>
        <tr>
            <td rowspan="2">ස්ථානය <br>
                பிறந்த <br>
                Place
            </td>
            <td>සිංහල හෝ දෙමළ භාෂාවෙන්<br>
                சிங்களம் தமிழ்<br>
                In Sinhala or Tamil
            </td>
            <td colspan="4"><s:textfield cssStyle="width:95%" id="placeOfBirth" name="alt52_1.placeOfBirth"/></td>
        </tr>
        <tr>
            <td>ඉංග්‍රීසි භාෂාවෙන්<br>
                in tamil<br>
                In English
            </td>
            <td colspan="4"><s:textfield cssStyle="width:95%" name="alt52_1.placeOfBirthEnglish"/></td>
        </tr>
        <tr>
            <td>
                ස්ත්‍රී පුරුෂ භාවය<br>
                பால் <br>
                Gender of the child
            </td>
            <td colspan="6"><s:select
                    list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                    name="alt52_1.childGender" cssStyle="width:50%; margin-left:5px;"/>
        </tr>
        </tbody>
    </table>
</div>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="9" style="text-align:center;font-size:11pt"> මවගේ විස්තර <br>தாய் பற்றிய தகவல் <br>Details of the
            Mother
            <div class="birth-alteration-minimize-icon" id="mother-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="mother-info-max"></div>
        </td>
    </tr>
</table>
<div id="mother-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;border-top:none;" cellpadding="0"
           cellspacing="0">
        <caption></caption>
        <col width="250px;"/>
        <col width="100px;"/>
        <col width="150px;"/>
        <col width="10px;"/>
        <col width="50px;"/>
        <col width="100px;"/>
        <col width="50px;"/>
        <col width="50px;"/>
        <col width="100px;"/>

        <tbody>
        <tr>
            <td rowspan="2">අනන්‍යතා අංකය
                / ජාතික හැදුනුම්පත් අංකය<br>து தனிநபர் அடையாள எண் /தேசிய
                அடையாள அட்டை
                இலக்கம்<br>PIN / NIC Number
            </td>
            <td colspan="2" rowspan="2" class="find-person">
                <s:textfield id="mother_pinOrNic" name="alt52_1.mother.motherNICorPIN" cssStyle="width:80%;"/>
                <img src="<s:url value="/images/search-mother.png"/>" style="vertical-align:middle;" id="mother_lookup">
            </td>
            <td colspan="2" rowspan="2"><label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
            </td>
            <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
            <td colspan="2"><s:select name="motherCountry" list="countryList" headerKey="0"
                                      headerValue="%{getText('select_country.label')}" cssStyle="width:80%;"/></td>
        </tr>
        <tr>
            <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
            <td colspan="2" class="passport"><s:textfield name="alt52_1.mother.motherPassportNo"/></td>
        </tr>
        <tr>
            <td>සම්පුර්ණ නම <br>
                முழுப் பெயர் <br>
                Full Name
            </td>
            <td colspan="8"><s:textarea name="alt52_1.mother.motherFullName"/></td>
        </tr>
        <tr>
            <td>උපන් දිනය<br>
                பிறந்த திகதி<br>
                Date of Birth
            </td>
            <td><s:textfield cssStyle="width:125px;" id="motherDateOfBirth" name="alt52_1.mother.motherDOB"/></td>
            <td>ළමයාගේ උපන් දිනට වයස <br>
                பிள்ளை பிறந்த திகதியில் மாதாவின் வயது <br>
                Age as at the date of birth of child
            </td>
            <td colspan="2"><s:textfield cssStyle="margin-right:10px;width:80%" id="motherAgeAtBirth"
                                         name="alt52_1.mother.motherAgeAtBirth"/></td>
            <td colspan="2">ජාතිය<br>
                இனம்<br>
                Race
            </td>
            <td colspan="2"><s:select list="raceList" name="motherRace" headerKey="0"
                                      headerValue="%{getText('select_race.label')}"
                                      cssStyle="width:80%;"/></td>
        </tr>
        <tr>
            <td>උපන් ස්ථානය<br>
                பிறந்த இடம் <br>
                Place of Birth
            </td>
            <td colspan="8"><s:textarea name="alt52_1.mother.motherPlaceOfBirth"/></td>
        </tr>
        <tr>
            <td>ස්ථිර ලිපිනය<br>
                தாயின் நிரந்தர வதிவிட முகவரி<br>
                Permanent Address
            </td>
            <td colspan="8"><s:textarea name="alt52_1.mother.motherAddress"/></td>
        </tr>
        </tbody>
    </table>
</div>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col width="250px;"/>
    <col width="250px;"/>
    <col width="250px;"/>
    <col width="250px;"/>
    <tbody>
    <tr>
        <td colspan="4" style="text-align:center;font-size:11pt">දැනුම් දෙන්නාගේ විස්තර<br>
            அறிவிப்பு கொடுப்பவரின் தகவல்கள்<br>
            Details of the Informant
            <div class="birth-alteration-minimize-icon" id="informant-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="informant-info-max"></div>
        </td>
    </tr>
    </tbody>
</table>
<div id="informant-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:1030px;border-top:none;" cellpadding="0"
           cellspacing="0">
        <caption></caption>
        <col width="250px;"/>
        <col width="250px;"/>
        <col width="250px;"/>
        <col width="250px;"/>
        <tbody>
        <tr>
            <td>දැනුම් දෙන්නේ කවුරුන් විසින් ද?<br>
                தகவல் வழங்குபவா் <br>
                Person Giving Information
            </td>
            <td>
                <table style="border:none;width:100%">
                    <tr>
                        <td style="width:75%;border:none">මව <br>மாதா<br>
                            Mother
                        </td>
                        <td style="width:25%;border:none;">
                            <s:radio id="" name="alt52_1.informant.informantType"
                                     list="#@java.util.HashMap@{'MOTHER':''}"/></td>
                </table>
            </td>
            <td>
                <table style="border:none;width:100%">
                    <tr>
                        <td style="width:75%;border:none"> පියා<br> பிதா<br> Father
                        </td>
                        <td style="width:25%;border:none;">
                            <s:radio id="" name="alt52_1.informant.informantType"
                                     list="#@java.util.HashMap@{'FATHER':''}"/></td>
                </table>

            </td>
            <td>
                <table style="border:none;width:100%">
                    <tr>
                        <td style="width:75%;border:none"> භාරකරු<br> பாதுகாவலர் <br> Guardian
                        </td>
                        <td style="width:25%;border:none;">
                            <s:radio id="" name="alt52_1.informant.informantType"
                                     list="#@java.util.HashMap@{'GUARDIAN':''}"/></td>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2">දැනුම් දෙන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>
                தகவல் கொடுப்பவரின் தனிநபர் அடையாள எண் / அடையாள அட்டை இல. <br>
                PIN / NIC of the Informant
            </td>
            <td colspan="2"><s:textfield id="informent_pinOrNic" name="alt52_1.informant. informantNICorPIN"/></td>
        </tr>
        <tr>
            <td>නම<br>
                கொடுப்பவரின் பெயர்<br>
                Name
            </td>
            <td colspan="3"><s:textarea id="informentName" name="alt52_1.informant.informantName"/></td>
        </tr>
        <tr>
            <td>තැපැල් ලිපිනය<br>
                தபால் முகவரி<br>
                Postal Address
            </td>
            <td colspan="3"><s:textarea id="informentAddress" name="alt52_1.informant.informantAddress"/></td>
        </tr>
        </tbody>
    </table>
</div>
</div>
<div id="actNumber3">
<table class="birth-alteration-table-style01" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td style="text-align:center;font-size:11pt"> උප්පැන්න සහතිකයක තොරතුරු සංශෝදනය කිරීම (27 A වගන්තිය)<br>
            தந்தை பற்றிய தகவல்<br>
            Amendment of Birth Registration Entry (Section 27 A)
        </td>
    </tr>
</table>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="5" style="text-align:center;font-size:12pt">පියාගේ විස්තර<br>
            தந்தை பற்றிய தகவல்<br>
            Details of the Father
            <div class="birth-alteration-minimize-icon" id="father-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="father-info-max"></div>
        </td>
    </tr>
</table>
<div id="father-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;border-top:none;" cellpadding="0"
           cellspacing="0">

        <caption></caption>
        <col width="250px;"/>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td rowspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>
                து தனிநபர் அடையாள எண் /தேசிய அடையாள அட்டை இலக்கம்<br>
                PIN / NIC Number
            </td>
            <td rowspan="2"><s:textfield name="alt27A.father.fatherNICorPIN" id="father_pinOrNic"/></td>
            <td rowspan="2">විදේශිකය‍කු නම්<br>
                வெளிநாட்டவர் எனின் <br>
                If a foreigner
            </td>
            <td>රට<br>
                நாடு<br>
                Country
            </td>
            <td><s:select name="fatherCountry" list="countryList" headerKey="0"
                          headerValue="%{getText('select_country.label')}" cssStyle="width:80%;"/></td>
        </tr>
        <tr>
            <td>ගමන් බලපත්‍ර අංකය<br>
                கடவுச் சீட்டு<br>
                Passport No.
            </td>
            <td><s:textfield name="alt27A.father.fatherPassportNo"/></td>
        </tr>
        <tr>
            <td>සම්පුර්ණ නම<br>
                தந்தையின் முழு பெயர்<br>
                Full Name
            </td>
            <td colspan="4"><s:textarea name="alt27A.father.fatherFullName" id="fatherName"/></td>
        </tr>
        <tr>
            <td>උපන් දිනය<br>
                பிறந்த திகதி <br>
                Date of Birth
            </td>
            <td><s:textfield id="fatherDadeOfbirth" name="alt27A.father.fatherDOB"/></td>
            <td colspan="2">ජාතිය<br>
                இனம்<br>
                Race
            </td>
            <td><s:select list="raceList" name="fatherRace" headerKey="0"
                          headerValue="%{getText('select_race.label')}"
                          cssStyle="width:80%;"/></td>
        </tr>
        <tr>
            <td>උපන් ස්ථානය<br>
                பிறந்த இடம்<br>
                Place of Birth
            </td>
            <td colspan="4"><s:textfield name="alt27A.father.fatherPlaceOfBirth"/></td>
        </tr>
        </tbody>
    </table>
</div>

<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="4" style="text-align:center;font-size:11pt">විවාහයේ විස්තර වෙනස් කිරීම<br>
            திருமணத்தின் விபரங்கள் <br>
            Changing of Details of the Marriage
            <div class="birth-alteration-minimize-icon" id="marriage-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="marriage-info-max"></div>
        </td>
    </tr>
</table>
<div id="marriage-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;border-top:none;" cellpadding="0"
           cellspacing="0">
        <tr>
            <td rowspan="2">මව්පියන් විවාහකද?</td>
            <td rowspan="2">
                <table style="width:100%; border:none;">
                    <col width="60px"/>
                    <col width="20px" align="right"/>
                    <tbody>
                    <tr>
                        <td style="border:none"><label>ඔව්<br>*in tamil<br>Yes</label></td>
                        <td style="border:none"><s:radio name="alt27A.marriage.parentsMarried"
                                                         list="#@java.util.HashMap@{'1':''}" value="1"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="border:none"><label>නැත<br>*in tamil<br>No</label></td>
                        <td style="border:none"><s:radio name="alt27A.marriage.parentsMarried"
                                                         list="#@java.util.HashMap@{'2':''}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="border:none"><label>නැත - පසුව විවාහවී ඇත<br>*in tamil<br>No but since
                            married</label></td>
                        <td style="border:none"><s:radio name="alt27A.marriage.parentsMarried"
                                                         list="#@java.util.HashMap@{'3':''}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="border:none"><label>නොදනී<br>*in tamil<br>Unknown</label></td>
                        <td style="border:none"><s:radio name="alt27A.marriage.parentsMarried"
                                                         list="#@java.util.HashMap@{'0':''}"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
            <td>විවාහ වු ස්ථානය<br>
                விவாகம் இடம்பெற்ற இடம்<br>
                Place of Marriage
            </td>
            <td><s:textfield name="alt27A.marriage.placeOfMarriage"/></td>
        </tr>
        <tr>
            <td>
                විවාහ වු දිනය<br>
                விவாகம் இடம்பெற்ற திகதி<br>
                Date of Marriage
            </td>
            <td><s:textfield id="dateOfMarriage" name="alt27A.marriage.dateOfMarriage"/></td>
        </tr>
    </table>
</div>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="2" style="text-align:center;font-size:11pt">
            විවාහයෙන් පසු මවගේ නම වෙනස් කිරීම<br>
            தாத்தாவின பாட்டனின் விபரங்கள் <br>
            Change of Mothers name after marriage
            <div class="birth-alteration-minimize-icon" id="mother-after-marriage-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="mother-after-marriage-info-max"></div>
        </td>
    </tr>
</table>
<div id="mother-after-marriage-info">
    <table class="birth-alteration-table-style02" style=" border-top:none;margin-top:0px;width:100%" cellpadding="0"
           cellspacing="0">
        <tr>
            <td style="width:250px;">විවාහයට පසුව මවගේ සම්පුර්ණ නම<br>
                முழுப் பெயர்<br>
                Full Name of Mother after Marriage
            </td>
            <td style="width:760px;"><s:textarea name="alt27A.mothersNameAfterMarriage"/></td>
        </tr>
    </table>
</div>


<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="8" style="text-align:center;font-size:11pt">මුත්තා / මී මුත්තා ගේ විස්තර වෙනස් කිරීම<br>
            தாத்தாவின் / பாட்டனின் விபரங்கள் <br>
            Changing of the Details of the Grand Father / Great Grand Father
            <div class="birth-alteration-minimize-icon" id="grandFather-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="grandFather-info-max"></div>
        </td>
    </tr>
</table>
<div id="grandFather-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;border-top:none;" cellpadding="0"
           cellspacing="0">
        <caption></caption>
        <col width="15px"/>
        <col width="230px"/>
        <col width="50px"/>
        <col width="150px"/>
        <col width="100px"/>
        <col width="150px"/>
        <col width="100px"/>
        <tbody>
        <tr>
            <td colspan="8">ළමයාගේ මුත්තා ශ්‍රී ලංකාවේ උපන්නේ නම්<br>
                பிள்ளையின் பாட்டனார் இலங்கையில் பிறந்திருந்தால் <br>
                If grandfather of the child born in Sri Lanka
            </td>
        </tr>
        <tr>
            <td rowspan="2" style="background-color:darkgray;"></td>
            <td>ඔහුගේ සම්පුර්ණ නම<br>
                அவரின் முழுப் பேயர்<br>
                His Full Name
            </td>
            <td colspan="6"><s:textarea name="alt27A.grandFather.grandFatherFullName"/></td>
        </tr>
        <tr>
            <td colspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය (තිබේ නම්)<br>
                தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம்<br>
                PIN / NIC Number (if available)
            </td>
            <td><s:textfield id="grandFather_pinOrNic" name="alt27A.grandFather.grandFatherNICorPIN"/></td>
            <td>ඔහුගේ උපන් වර්ෂය<br>
                அவர் பிறந்த வருடம்<br>
                His Year of Birth
            </td>
            <td><s:textfield id="grandFatherBirthYear" name="alt27A.grandFather.grandFatherBirthYear"/></td>
            <td>උපන් ස්ථානය<br>
                அவர் பிறந்த இடம்<br>
                Place Of Birth
            </td>
            <td><s:textfield name="alt27A.grandFather.grandFatherBirthPlace"/></td>
        </tr>
        <tr>
            <td colspan="8">ළමයාගේ පියා ශ්‍රී ලංකාවේ නොඉපිද මීමුත්තා ලංකාවේ උපන්නේ නම් මී මුත්තාගේ<br>
                பிள்ளையின் தந்தை இலங்கையில் பிறக்காமல் பூட்டன் இலங்கையில் பிறந்திருந்தால் பூட்டனாரின் தகவல்கள் <br>
                If the father was not born in Sri Lanka and if great grand father born in Sri Lanka, great grand
                father's
            </td>
        </tr>
        <tr>
            <td rowspan="2" style="background-color:darkgray;"></td>
            <td>ඔහුගේ සම්පුර්ණ නම<br>
                முழுப் பெயர்<br>
                His Full Name
            </td>
            <td colspan="6"><s:textarea name="alt27A.grandFather.greatGrandFatherFullName"/></td>
        </tr>
        <tr>
            <td colspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය (තිබේ නම්)<br>
                தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம்<br>
                PIN / NIC Number (if available)
            </td>
            <td><s:textfield id="grandGrandFather_pinOrNic" name="alt27A.grandFather.greatGrandFatherNICorPIN"/></td>
            <td>ඔහුගේ උපන් වර්ෂය<br>
                அவர் பிறந்த வருடம்<br>
                His Year of Birth
            </td>
            <td><s:textfield id="grandGrandFatherBirthYear" name="alt27A.grandFather.greatGrandFatherBirthYear"/></td>
            <td>උපන් ස්ථානය<br>
                அவர் பிறந்த இடம்<br>
                Place Of Birth
            </td>
            <td><s:textfield name="alt27A.grandFather.greatGrandFatherBirthPlace"/></td>
        </tr>
        </tbody>
    </table>
</div>
</div>
<div id="actNumber4">
    <div id="error-explanation-info">
        <table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%;" cellpadding="0"
               cellspacing="0">
            <tr>
                <td colspan="8" style="text-align:center;font-size:12pt">
                    දෝෂය හා එය සිදුවූ අන්දම පිලිබඳ ලුහුඬු විස්තර<br>
                    தாத்தாவின் / பாட்டனின் விபரங்கள்<br>
                    Nature of the error and a brief explanation of how the error occurred
                    <div class="birth-alteration-minimize-icon" id="error-explanation-info-min"></div>
                    <div class="birth-alteration-maximize-icon" id="error-explanation-info-max"></div>
                </td>
            </tr>
            <tr>
                <td>
                    <s:textarea cssStyle="height:150px;"/>
                </td>
            </tr>
        </table>
    </div>
</div>


<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%;" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col style="width:250px;"/>
    <col style="width:190px;"/>
    <col style="width:190px;"/>
    <col style="width:190px;"/>
    <col style="width:190px;"/>
    <tbody>
    <tr>
        <td colspan="5" style="text-align:center;font-size:11pt">ප්‍රකාශය කරන්නාගේ විස්තර<br>
            அறிவிப்பு கொடுப்பவரின் தகவல்கள்<br>
            Details of the Declarant
        </td>
    </tr>
    <tr>
        <td>ප්‍රකාශය කරන්නේ කවුරුන් විසින් ද?<br>
            தகவல் வழங்குபவா் <br>
            Person Giving declaration
        <td>
            <table style="border:none;width:100%">
                <tr>
                    <td style="width:75%;border:none">මව <br>மாதா<br>
                        Mother
                    </td>
                    <td style="width:25%;border:none;">
                        <s:radio id="declarantType" name="declarant.declarantType"
                                 list="#@java.util.HashMap@{'FATHER':''}"/></td>
            </table>
        </td>
        <td>
            <table style="border:none;width:100%">
                <tr>
                    <td style="width:75%;border:none"> පියා<br> பிதா<br> Father
                    </td>
                    <td style="width:25%;border:none;">
                        <s:radio id="declarantType" name="declarant.declarantType"
                                 list="#@java.util.HashMap@{'MOTHER':''}"/></td>
            </table>

        </td>
        <td>
            <table style="border:none;width:100%">
                <tr>
                    <td style="width:75%;border:none"> භාරකරු<br> பாதுகாவலர் <br> Guardian
                    </td>
                    <td style="width:25%;border:none;">
                        <s:radio id="declarantType" name="declarant.declarantType"
                                 list="#@java.util.HashMap@{'OTHER':''}"/></td>
            </table>
        </td>
        <td>
            <table style="border:none;width:100%">
                <tr>
                    <td style="width:75%;border:none"> තමුන්<br> மாதா<br> Self
                    </td>
                    <td style="width:25%;border:none;">
                        <s:radio id="declarantType" name="declarant.declarantType"
                                 list="#@java.util.HashMap@{'RELATIVE':''}"/></td>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3">ප්‍රකාශය දෙන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>
            தகவல் கொடுப்பவரின் தனிநபர் அடையாள எண் / அடையாள அட்டை இல. <br>
            PIN / NIC of the declarant
        </td>
        <td colspan="2"><s:textfield id="Declarant_pinOrNic" name="declarant.declarantNICorPIN"/>
        </td>
    </tr>
    <tr>
        <td>නම<br>
            கொடுப்பவரின் பெயர்<br>
            Name<br></td>
        <td colspan="4"><s:textarea id="declarantName" name="declarant.declarantFullName"/></td>
    </tr>
    <tr>
        <td>තැපැල් ලිපිනය<br>
            தபால் முகவரி<br>
            Postal Address
        </td>
        <td colspan="4"><s:textarea id="" name="declarant.declarantAddress"/></td>
    </tr>
    </tbody>
</table>


<%--<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%;" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col width="330px;"/>
    <col width="330px;"/>
    <col width="330px;"/>
    <tbody>
    <tr>
        <td colspan="3" style="text-align:center;font-size:12pt;border-bottom:none;">ආඥාව <br>
            அதிகாரியிடம் தெரிவித்தல்<br>
            Order
        </td>
    </tr>
    <tr>
        <td colspan="3">
            මෙම ප්‍රකාශ පත්‍රයේ සඳහන් වන උප්පැන්නය ලියාපදිංචි සටහන වෙනස් කිරීමට මෙයින් බලය දෙනු / නොදෙනු ලැබේ
            * in Tamil * මෙම ප්‍රකාශ පත්‍රයේ සඳහන් වන උප්පැන්නය ලියාපදිංචි සටහන වෙනස් කිරීමට මෙයින් බලය දෙනු / නොදෙනු
            ලැබේ
            I hereby order* / refuse* to update the register of the birth of which particulars are given in the
            declaration
        </td>
    </tr>
    <tr>
        <td colspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
            தகவல் கொடுப்பவரின் தனிநபர் அடையாள எண் / அடையாள அட்டை இல.
            PIN / NIC of the Authority
        </td>
        <td><s:textfield/></td>
    </tr>
    <tr>
        <td>ආඥාව කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන
            சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்
            Name, Signature and Designation of officer making the order
        </td>
        <td colspan="2"><s:textarea/></td>
    </tr>
    </tbody>
</table>--%>
</div>
<%--common errors--%>
<s:hidden id="comError1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="comError2" value="%{getText('p1.serial.text')}"/>
<s:hidden id="comError3" value="%{getText('p1.SerialNum.error.value')}"/>
<s:hidden id="comError4" value="%{getText('p1.submitDate.error.value')}"/>
<%--child error--%>
<s:hidden id="childError1" value="%{getText('p1.childName.error.value')}"/>
<s:hidden id="childError2" value="%{getText('p1.NameEnglish.error.value')}"/>
<s:hidden id="childError3" value="%{getText('p1.dob.error.value')}"/>
<s:hidden id="childError4" value="%{getText('p1.dob')}"/>
<s:hidden id="childError5" value="%{getText('p1.placeOfBirth.error.value')}"/>
<%--informent errors--%>
<s:hidden id="informentError1" value="%{getText('p3.Informent.Name.error.value')}"/>
<s:hidden id="informentError2" value="%{getText('p3.Informent.Address.error.value')}"/>
<s:hidden id="informentError3" value="%{getText('informantNIC.text')}"/>
<%--mother errors--%>
<s:hidden id="motherError1" value="%{getText('motherPINorNIC.label')}"/>
<s:hidden id="motherError2" value="%{getText('p2.mother.dob')}"/>
<s:hidden id="motherError3" value="%{getText('p2.motherAge.error.value')}"/>
<%--father errors--%>
<s:hidden id="fatherError1" value="%{getText('fatherPINorNIC.label')}"/>
<s:hidden id="fatherError2" value="%{getText('p2.fatherName.error.value')}"/>
<s:hidden id="fatherError3" value="%{getText('p2.father.dob')}"/>


<s:hidden id="grandFatherError1" value="%{getText('grandFatherNICorPIN.text')}"/>
<s:hidden id="grandFatherError2" value="%{getText('p1.YearofBirthOfGrandFather')}"/>

<s:hidden id="grandGrandFatherError1" value="%{getText('greatGrandFatherNICorPIN.text')}"/>
<s:hidden id="grandGrandFatherError2" value="%{getText('p1.YearofBirthOfGreatGrandFather')}"/>
<div class="form-submit">
<s:submit value="%{getText('submit.label')}"/>
</s:form>
</div>
</s:if>