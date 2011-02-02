<%--@author amith jayasekara--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/division.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/transliteration.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/personlookup.js"/>"></script>

<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<script type="text/javascript">
function initPage() {
    var noticeType = document.getElementById('notice_type').value;
    if (noticeType == "BOTH_NOTICE" || noticeType == "MALE_NOTICE") {
        document.getElementById('maleNotFound').innerHTML = "";
    }
    if (noticeType == "BOTH_NOTICE" || noticeType == "FEMALE_NOTICE") {
        document.getElementById('femaleNotFound').innerHTML = "";
    }
    var femaleNot = document.getElementById('femaleNotFoundError').value;
    var maleNot = document.getElementById('maleNotFoundError').value;
    if (femaleNot == "true") {
        document.getElementById('femaleNotFound').innerHTML = document.getElementById('notFound').value;
    }
    if (maleNot == "true") {
        document.getElementById('maleNotFound').innerHTML = document.getElementById('notFound').value;
    }
}

var errormsg = "";
function validate() {
    //following fields are must filled before submit the form
    // serial number(number) / date (length must be 10 ) / type of marriage / identification number male,female(String max 10) /DOB male, female
    //age at last BD male,female (number)/race male ,female / name in official male ,female / address male,female /civil state male,female

    //following fields must be checked for the type if available
    //date of arrival male,female (date)  / passport male.female /identification number father male,female
    //validate types
    //validate must fill fields
    var noticeType = document.getElementById('notice_type').value;

    var domObject = document.getElementById('serial_number');
    if (isFieldEmpty(domObject)) {
        printMessage("text_serial_number", "text_must_fill")
    } else {
        validateSerialNo(domObject, 'text_invalid_data', 'text_serial_number')
    }
    domObject = document.getElementById('submitDatePicker');
    if (isFieldEmpty(domObject)) {
        printMessage("text_submit_date", "text_must_fill")
    }
    else {
        isDate(domObject.value, "text_invalid_data", "text_submit_date")
    }

    validateRadioButtons(new Array(document.getElementsByName('marriage.typeOfMarriage')[0].checked,
            document.getElementsByName('marriage.typeOfMarriage')[1].checked,
            document.getElementsByName('marriage.typeOfMarriage')[2].checked), "text_marriage_type");

    //male related fields
    if (noticeType == "BOTH_NOTICE" || noticeType == "MALE_NOTICE") {

        domObject = document.getElementById('identification_male');
        if (isFieldEmpty(domObject)) {
            printMessage("text_id_male", "text_must_fill")
        }
        else {
            validatePIN(domObject, "text_invalid_data", "text_id_male")
        }

        domObject = document.getElementById('date_of_birth_male');
        if (isFieldEmpty(domObject)) {
            printMessage("text_dob_male", "text_must_fill")
        }
        else {
            isDate(domObject.value, "text_invalid_data", "text_dob_male")
        }

        domObject = document.getElementById('age_at_last_bd_male');
        if (isFieldEmpty(domObject)) {
            printMessage("text_age_at_last_bd_male", "text_must_fill")
        }
        else {
            isNumeric(domObject.value, "text_invalid_data", "text_age_at_last_bd_male")
        }

        domObject = document.getElementById('raceMaleId');
        if (domObject.value == 0) {
            printMessage("text_race_male", "text_must_select")
        }

        domObject = document.getElementById('name_official_male');
        if (isFieldEmpty(domObject)) {
            printMessage("text_name_official_male", "text_must_fill")
        }

        domObject = document.getElementById('address_male_official');
        if (isFieldEmpty(domObject)) {
            printMessage("text_address_official_male", "text_must_fill")
        }

        validateRadioButtons(new Array(document.getElementsByName('marriage.male.civilStatusMale')[0].checked,
                document.getElementsByName('marriage.male.civilStatusMale')[1].checked,
                document.getElementsByName('marriage.male.civilStatusMale')[2].checked,
                document.getElementsByName('marriage.male.civilStatusMale')[3].checked), "text_civil_state_male");

        domObject = document.getElementById('date_arrival_male');
        if (!isFieldEmpty(domObject)) {
            isDate(domObject.value, "text_invalid_data", "text_date_arrival_male")
        }

        domObject = document.getElementById('father_pin_or_nic_male');
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, "text_invalid_data", "text_fa_id_male")
        }

        domObject = document.getElementById('passport_male');
        if (!isFieldEmpty(domObject)) {
            validatePassportNo(domObject, "text_invalid_data", "text_passport_male")
        }
    }
    //female related fields
    if (noticeType == "BOTH_NOTICE" || noticeType == "FEMALE_NOTICE") {
        domObject = document.getElementById('identification_female');
        if (isFieldEmpty(domObject)) {
            printMessage("text_id_female", "text_must_fill")
        }
        else {
            validatePIN(domObject, "text_invalid_data", "text_id_female")
        }

        domObject = document.getElementById('date_of_birth_female');
        if (isFieldEmpty(domObject)) {
            printMessage("text_dob_female", "text_must_fill")
        }
        else {
            isDate(domObject.value, "text_invalid_data", "text_dob_female")
        }

        domObject = document.getElementById('age_at_last_bd_female');
        if (isFieldEmpty(domObject)) {
            printMessage("text_age_at_last_bd_female", "text_must_fill")
        }
        else {
            isNumeric(domObject.value, "text_invalid_data", "text_age_at_last_bd_female")
        }

        domObject = document.getElementById('raceFemaleId');
        if (domObject.value == 0) {
            printMessage("text_race_female", "text_must_select")
        }

        domObject = document.getElementById('name_official_female');
        if (isFieldEmpty(domObject)) {
            printMessage("text_name_official_female", "text_must_fill")
        }

        domObject = document.getElementById('address_female_official');
        if (isFieldEmpty(domObject)) {
            printMessage("text_address_official_female", "text_must_fill")
        }

        validateRadioButtons(new Array(document.getElementsByName('marriage.female.civilStatusFemale')[0].checked,
                document.getElementsByName('marriage.female.civilStatusFemale')[1].checked,
                document.getElementsByName('marriage.female.civilStatusFemale')[2].checked,
                document.getElementsByName('marriage.female.civilStatusFemale')[3].checked), "text_civil_state_female");

        validateRadioButtons(new Array(document.getElementsByName('marriage.licenseCollectType')[0].checked,
                document.getElementsByName('marriage.licenseCollectType')[1].checked,
                document.getElementsByName('marriage.licenseCollectType')[2].checked,
                document.getElementsByName('marriage.licenseCollectType')[3].checked), "text_select_how_collect_license");

        validateRadioButtons(new Array(document.getElementsByName('marriage.preferredLanguage')[0].checked,
                document.getElementsByName('marriage.preferredLanguage')[1].checked), "text_select_proffered_language");

        domObject = document.getElementById('date_arrival_female');
        if (!isFieldEmpty(domObject)) {
            isDate(domObject.value, "text_invalid_data", "text_date_arrival_female")
        }

        domObject = document.getElementById('father_pin_or_nic_female');
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, "text_invalid_data", "text_fa_id_female")
        }

        domObject = document.getElementById('passport_female');
        if (!isFieldEmpty(domObject)) {
            validatePassportNo(domObject, "text_invalid_data", "text_passport_female")
        }
    }

    validateOptionals();

    if (errormsg != "") {
        // customAlert(errormsg);
        alert(errormsg);
        errormsg = "";
        return false;
    }
    errormsg = "";
    return true;
}

function validateOptionals() {
    //validate male party age
    var domObject = null;
    var noticeType = document.getElementById('notice_type').value;
    var dateOfRecived = document.getElementById('submitDatePicker');

    if (noticeType == "BOTH_NOTICE" || noticeType == "FEMALE_NOTICE") {
        domObject = document.getElementById('age_at_last_bd_female');
        if (!isFieldEmpty(domObject)) {
            if (domObject.value == '0') {
                printMessage("text_age_at_last_bd_female", "text_gt_zero")
            }
            else {
                if (domObject.value < 18) {
                    printMessage("text_age_at_last_bd_female", "text_gt_18")
                }
            }
        }

        var femaleBD = document.getElementById('date_of_birth_female').value;
        if (new Date(femaleBD).getTime() > new Date(dateOfRecived.value).getTime()) {
            printMessage("text_dob_female", "text_future_date")
        }
    }

    if (noticeType == "BOTH_NOTICE" || noticeType == "MALE_NOTICE") {
        domObject = document.getElementById('age_at_last_bd_male');
        if (!isFieldEmpty(domObject)) {
            if (domObject.value == '0') {
                printMessage("text_age_at_last_bd_male", "text_gt_zero")
            }
            else {
                if (domObject.value < 18) {
                    printMessage("text_age_at_last_bd_male", "text_gt_18")
                }
            }
        }
        //date of birth male or female cannot be grt that date if submit
        var maleBD = document.getElementById('date_of_birth_male').value;

        if (new Date(maleBD).getTime() > new Date(dateOfRecived.value).getTime()) {
            printMessage("text_dob_male", "text_future_date")
        }
    }

    if (isFutureDate(dateOfRecived.value)) {
        printMessage("text_submit_date", "text_future_date")
    }

}

function partyAge(mode) {
    var dateOdBirthSubmitted = true;
    var person_bd = new Date();
    var dom = null;
    if (mode == '0') {
        //male party age
        dom = document.getElementById('date_of_birth_male');
        person_bd = new Date(document.getElementById('date_of_birth_male').value);
    } else {
        //female party age
        dom = document.getElementById('date_of_birth_female');
        person_bd = new Date(document.getElementById('date_of_birth_female').value);
    }
    if (isFieldEmpty(dom)) {
        dateOdBirthSubmitted = false;
    }

    var today = new Date();
    var person_age = today.getYear() - person_bd.getYear();
    if (!(dateOdBirthSubmitted)) {
        if (mode == '0') {
            document.getElementById("age_at_last_bd_male").value = 0;
        }
        else {
            document.getElementById("age_at_last_bd_female").value = 0;
        }
    }
    else {
        if (mode == '0') {
            document.getElementById("age_at_last_bd_male").value = person_age;
        }
        else {
            document.getElementById("age_at_last_bd_female").value = person_age;
        }
    }
}

function validateRadioButtons(array, errorText) {
    var atleastOneSelect = false;
    for (var i = 0; i < array.length; i++) {
        if (array[i] == true) {
            atleastOneSelect = true;
        }
    }
    if (!atleastOneSelect) {
        printMessage(errorText, "text_must_select")
    }
}

$(function() {
    $("#submitDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});

$(function() {
    $("#date_of_birth_male").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});

$(function() {
    $("#date_of_birth_female").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});

$(function() {
    $("#date_arrival_female").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});


$(function() {
    $("#date_arrival_male").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});


$('select#districtId').bind('change', function(evt1) {
    var id = $("select#districtId").attr("value");
    $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:8},
            function(data) {
                var options1 = '';
                var ds = data.dsDivisionList;
                for (var i = 0; i < ds.length; i++) {
                    options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                }
                $("select#dsDivisionId").html(options1);

                var options2 = '';
                var bd = data.mrDivisionList;
                for (var j = 0; j < bd.length; j++) {
                    options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                }
                $("select#mrDivisionId").html(options2);
            });
});

$('select#dsDivisionId').bind('change', function(evt2) {
    var id = $("select#dsDivisionId").attr("value");
    $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:7},
            function(data) {
                var options = '';
                var bd = data.mrDivisionList;
                for (var i = 0; i < bd.length; i++) {
                    options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                }
                $("select#mrDivisionId").html(options);
            });
});
</script>
<style type="text/css">
    .abc {
        position: relative;
        bottom: 15px;
        left: 5px;
    }
</style>

<s:if test="editMode">
    <s:if test="secondNotice">
        <%--second notice--%>
        <s:url action="eprMarriageSecondNoticeAdd" namespace="." id="addAction">
            <s:param name="idUKey" value="idUKey"/>
        </s:url>
    </s:if>
    <s:else>
        <s:url action="eprMarriageNoticeEdit" namespace="." id="addAction">
            <s:param name="idUKey" value="marriage.idUKey"/>
        </s:url>
    </s:else>
</s:if>
<s:else>
    <s:url action="eprMarriageNoticeAdd" namespace="." id="addAction">
        <s:param name="noticeType" value="%{noticeType}"/>
    </s:url>
</s:else>

<div class="marriage-notice-outer">
<s:form action="%{addAction}" method="post" onsubmit="javascript:return validate()">
<%--section official usage--%>
<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
    <legend align="right"><b><s:label value="%{getText('label.how.collect.license')}"/>
        <s:label value="*" cssStyle="color:red;font-size:10pt"/></b></legend>
    <s:radio list="#@java.util.HashMap@{'HAND_COLLECT_MALE':getText('label.collect.by.male'),
                        'MAIL_TO_MALE':getText('label.mail.male'),'HAND_COLLECT_FEMALE':getText('label.collect.by.female'),'MAIL_TO_FEMALE':getText('label.mail.female')}"
             name="marriage.licenseCollectType" value="%{marriage.licenseCollectType}"/>
</fieldset>
<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
    <legend align="right"><b><s:label value="%{getText('label.pref.lang.for.certificates')}"/></b>
        <s:label value="*" cssStyle="color:red;font-size:10pt"/></legend>
    <s:radio list="#@java.util.HashMap@{'si':getText('label.sinhala'),'ta':getText('label.tamil')}"
             name="marriage.preferredLanguage" value="%{marriage.preferredLanguage}"/>
</fieldset>
<table>
    <caption/>
    <col width="400px"/>
    <col width="224px"/>
    <col/>
    <tbody>
    <tr>
        <td colspan="2" height="30px"></td>
    </tr>
    <tr>
        <td>
            <table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;"
                   cellpadding="2px">
                <caption/>
                <col width="200px"/>
                <col width="200px"/>
                <tbody>
                <tr>
                    <td>
                        දිස්ත්‍රික්කය <br>
                        மாவட்டம் <br>
                        District <br>
                    </td>
                    <td>
                        <s:select id="districtId" name="marriageDistrictId" list="districtList"
                                  value="marriageDistrictId"
                                  cssStyle="width:98.5%; width:195px;"
                                  onchange="populateDSDivisions('districtId','dsDivisionId','mrDivisionId', 'Marriage', false)"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
                        பிரதேச செயளாளர் பிரிவு <br>
                        Divisional Secretariat <br>
                    </td>
                    <td>
                        <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="dsDivisionId"
                                  cssStyle="width:98.5%; width:195px;"
                                  onchange="populateDivisions('dsDivisionId', 'mrDivisionId', 'Marriage', false)"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        ලියාපදිංචි කිරීමේ කොට්ඨාශය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
                        பதிவுப் பிரிவு <br>
                        Registration Division <br>
                    </td>
                    <td>
                        <s:select id="mrDivisionId" name="mrDivisionId" list="mrDivisionList" value="marriageDivisionId"
                                  cssStyle="width:98.5%; width:195px;"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
        <td align="center"><img src="<s:url value="/images/official-logo.png"/>"/></td>
        <td>
            <table border="1"
                   style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:3"
                   cellpadding="2px">
                <caption/>
                <col width="200px"/>
                <col width="200px"/>
                <tbody>
                <tr>
                    <td colspan="2">කාර්යාල ප්‍රයෝජනය සඳහා පමණි / <br>
                        அலுவலக பாவனைக்காக மட்டும் / <br>
                        For office use only <br>
                    </td>

                </tr>
                <tr>
                    <td>
                        අනුක්‍රමික අංකය<s:label value="*" cssStyle="color:red;font-size:10pt"/> <br>
                        தொடர் இலக்கம் <br>
                        Serial Number <br>
                    </td>
                    <td align="center">
                        <s:textfield name="serialNumber" id="serial_number" maxLength="10"/>
                        <br>
                        <s:fielderror name="duplicateSerialNumber" cssStyle="color:red;font-size:10pt;"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        භාරගත් දිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
                        பெறப்பட்ட திகதி <br>
                        Date of Acceptance <br>
                    </td>
                    <td>
                        <s:textfield name="noticeReceivedDate" id="submitDatePicker" maxLength="10"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3" align="center">
            විවාහ දැන්වීම <br>
            குடிமதிப்பீட்டு ஆவணத்தில் <br>
            Notice of Marriage
        </td>
    </tr>
    </tbody>
</table>

<%--section type of marriage--%>
<table border="1" style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="210px"/>
    <col width="50px"/>
    <col width="210px"/>
    <col width="50px"/>
    <col width="210px"/>
    <col width="50px"/>
    <tr>
        <td>
            විවාහයේ ස්වභාවය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            Type of Marriage <br>
        </td>
        <td>
            සාමාන්‍ය <br>
            in tamil <br>
            General
        </td>
        <td align="center">
            <s:radio name="marriage.typeOfMarriage" list="#@java.util.HashMap@{'GENERAL':''}"
                     value="%{marriage.typeOfMarriage}"/>
        </td>
        <td>
            උඩරට බින්න <br>
            in tamil <br>
            Kandyan Binna
        </td>
        <td align="center">
            <s:radio name="marriage.typeOfMarriage" list="#@java.util.HashMap@{'KANDYAN_BINNA':''}"
                     value="%{marriage.typeOfMarriage}"/>
        </td>
        <td>
            උඩරට දීග <br>
            in tamil <br>
            Kandyan Deega
        </td>
        <td align="center">
            <s:radio name="marriage.typeOfMarriage" list="#@java.util.HashMap@{'KANDYAN_DEEGA':''}"
                     value="%{marriage.typeOfMarriage}"/>
        </td>
    </tr>
</table>

<s:if test="%{noticeType.ordinal()==0 || noticeType.ordinal()==1}">

<%--section male party--%>
<table style="margin-top:20px">
    <caption/>
    <col width="500px"/>
    <col width="524px"/>
    <tr>
        <td colspan="2" align="center">
            පුරුෂ පාර්ශ්වය / in tamil / Male Party
        </td>
    </tr>
</table>

<table border="1"
       style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;border-bottom:none "
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="100px"/>
    <col/>
    <col width="170px"/>
    <col/>
    <col width="170px"/>
    <col/>
    <tr>
        <td colspan="1">
            අනන්‍යතා අංකය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            அடையாள எண் <br>
            Identification number(PIN)
        </td>
        <td colspan="2">
            <s:textfield name="marriage.male.identificationNumberMale" id="identification_male" maxLength="10"/>
            <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;" id="male_lookup"
                 onclick="malePersonLookUp('identification_male');">
            <br>

            <div id="maleNotFound" style="color:red; font-size:11pt"/>
        </td>
        </td>
        <td colspan="1">
            උපන් දිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td colspan="3">
            <s:textfield name="marriage.male.dateOfBirthMale" id="date_of_birth_male" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            ජාතිය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            in tamil <br>
            Race
        </td>
        <td colspan="2">
            <s:select list="raceList" name="raceIdMale" headerKey="0" headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;" id="raceMaleId"/>
        </td>
        <td colspan="1">
            පසුවූ උපන් දිනයට වයස <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            in tamil <br>
            Age at last Birthday
        </td>
        <td colspan="3">
            <s:textfield name="marriage.male.ageAtLastBirthDayMale" id="age_at_last_bd_male" maxLength="3"
                         value="%{marriage.male.ageAtLastBirthDayMale}" onfocus="partyAge(0);"/>
        </td>
    </tr>
    <tr>
        <td>
            විදේශිකයකු නම් <br>
            வெளிநாட்டவர் எனின் <br>
            If a foreigner
        </td>
        <td>
            රට <br>
            நாடு <br>
            Country
        </td>
        <td>
            <s:select list="countryList" name="countryIdMale" headerKey="0"
                      headerValue="%{getText('select_country.label')}"
                      cssStyle="width:150px;"/>
        </td>
        <td>
            ගමන් බලපත්‍ර අංකය <br>
            கடவுச் சீட்டு இல. <br>
            Passport No.
        </td>
        <td>
            <s:textfield name="marriage.male.passport" id="passport_male" maxLength="15"
                         cssStyle="width:120px" value="%{marriage.male.passport}"/>
        </td>
        <td>ලංකාවට පැමිණි දිනය <br>
            in tamil <br>
            Date of Arrival
        </td>
        <td>
            <s:textfield name="marriage.male.dateOfArrival" id="date_arrival_male" maxLength="10"
                         cssStyle="width:80px" value="%{marriage.male.dateOfArrival}"/>
        </td>
    </tr>
    <tr>
        <td colspan="1" height="100x">
            නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="6" height="100x">
            <s:textarea name="marriage.male.nameInOfficialLanguageMale" id="name_official_male"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1" height="100x">
            නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பெயர் ஆங்கில மொழியில் <br>
            Name in English
        </td>
        <td colspan="6" height="100x">
            <s:textarea name="marriage.male.nameInEnglishMale" id="name_english_male" cssStyle="width:98.2%;"/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;"
                 id="regName" onclick="transliterate('name_official_male', 'name_english_male')">
        </td>
    </tr>
</table>
<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption></caption>
    <col width="233px"/>
    <col width="140px"/>
    <col width="50px"/>
    <col width="140px"/>
    <col width="50px"/>
    <col width="150px"/>
    <col width="50px"/>
    <col width="150px"/>
    <col width="50px"/>
    <tbody>
    <tr>
        <td rowspan="2">
            පදිංචි ලිපිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            தற்போதைய வதிவிட முகவரி <br>
            Resident Address
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language<s:label value="*"
                                                                                                cssStyle="color:red;font-size:10pt"/></td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.male.residentAddressMaleInOfficialLang" id="address_male_official"
                        cssStyle="width:98.2%;"/>
            </br>
            <img src="<s:url value="/images/transparent.png"/>"
                 style="vertical-align:middle;margin:5px;width:80px;height:30px">
        </td>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.male.residentAddressMaleInEnglish" id="address_male_english"
                        cssStyle="width:98.2%;"/>
            </br>
            <img src="<s:url value="/images/transliterate.png"/>"
                 style="vertical-align:middle;margin:5px;width:80px;height:30px"
                 onclick="transliterate('address_male_official', 'address_male_english')">
        </td>
    </tr>
    <tr>
        <td rowspan="2">
            තරාතිරම නොහොත් රක්ෂාව <br>
            in tamil <br>
            Rank or Profession
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language</td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="40px">
            <s:textfield name="marriage.male.rankOrProfessionMaleInOfficialLang" id="rank_male_official"
                         cssStyle="width:98.2%;"
                         maxLength="255"/>
            </br>
            <img src="<s:url value="/images/transparent.png"/>"
                 style="vertical-align:middle;margin:5px;width:80px;height:30px">
        </td>
        <td colspan="4" height="40px">
            <s:textfield name="marriage.male.rankOrProfessionMaleInEnglish" id="rank_male_english"
                         cssStyle="width:98.2%;"
                         maxLength="255"/>
            </br>
            <img src="<s:url value="/images/transliterate.png"/>"
                 style="vertical-align:middle;margin:5px;width:80px;height:30px"
                 onclick="transliterateTextField('rank_male_official', 'rank_male_english')">
        </td>
    </tr>
    <tr>
        <td>
            සිවිල් තත්වය<s:label value="*" cssStyle="color:red;font-size:10pt"/> <br>
            சிவில் நிலைமை <br>
            Civil Status
        </td>
        <td>
            අවිවාහක <br>
            திருமணமாகாதவர் <br>
            Never Married
        </td>
        <td>
            <s:radio name="marriage.male.civilStatusMale" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"
                     value="%{marriage.male.civilStatusMale}"/>
        </td>
        <td>
            දික්කසාද <br>
            திருமணம் தள்ளுபடி செய்தவர் <br>
            Divorced
        </td>
        <td>
            <s:radio name="marriage.male.civilStatusMale" list="#@java.util.HashMap@{'DIVORCED':''}"
                     value="%{marriage.male.civilStatusMale}"/>
        </td>
        <td>
            වැන්දබු <br>
            விதவை <br>
            Widowed
        </td>
        <td>
            <s:radio name="marriage.male.civilStatusMale" list="#@java.util.HashMap@{'WIDOWED':''}"
                     value="%{marriage.male.civilStatusMale}"/>
        </td>
        <td>
            නිෂ්ප්‍රභාකර ඇත <br>
            தள்ளிவைத்தல் <br>
            Anulled
        </td>
        <td>
            <s:radio name="marriage.male.civilStatusMale" list="#@java.util.HashMap@{'ANNULLED':''}"
                     value="%{marriage.male.civilStatusMale}"/>
        </td>
    </tr>
    <tr>
        <td>
            පියාගේ අනන්‍යතා අංකය <br>
            தந்தையின் அடையாள எண் <br>
            Fathers Identification Number
        </td>
        <td colspan="8">
            <s:textfield name="marriage.male.fatherIdentificationNumberMale" id="father_pin_or_nic_male"
                         cssStyle="width:240px;" maxLength="10"/>
            <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;" id="male_lookup"
                 onclick="malePartyFather('father_pin_or_nic_male');">
        </td>
    </tr>
    <tr>
        <td rowspan="2">
            පියාගේ සම්පුර්ණ නම <br>
            தந்தையின் அடையாள <br>
            Fathers full name
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language</td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.male.fatherFullNameMaleInOfficialLang" id="father_full_name_male_official"
                        cssStyle="width:98.2%;"/>
            </br>
            <img src="<s:url value="/images/transparent.png"/>"
                 style="vertical-align:middle;margin:5px;width:80px;height:30px">
        </td>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.male.fatherFullNameMaleInEnglish" id="father_full_name_male_english"
                        cssStyle="width:98.2%;"/>
            </br>
            <img src="<s:url value="/images/transliterate.png"/>"
                 style="vertical-align:middle;margin:5px;width:80px;height:30px"
                 onclick="transliterate('father_full_name_male_official', 'father_full_name_male_english')">
        </td>
    </tr>
    </tbody>
</table>

</s:if>


<s:if test="%{noticeType.ordinal()==0 || noticeType.ordinal()==2}">
<%--section female party--%>
<table style="margin-top:20px">
    <caption/>
    <col width="500px"/>
    <col width="524px"/>
    <tr>
        <td colspan="2" align="center">
            ස්ත්‍රී පාර්ශ්වය / in tamil / Female Party
        </td>
    </tr>
</table>

<table border="1"
       style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;border-bottom:none "
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="100px"/>
    <col/>
    <col width="170px"/>
    <col/>
    <col width="170px"/>
    <col/>
    <tr>
        <td colspan="1">
            අනන්‍යතා අංකය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            அடையாள எண் <br>
            Identification number(PIN)
        </td>
        <td colspan="2">
            <s:textfield name="marriage.female.identificationNumberFemale" id="identification_female" maxLength="10"/>
            <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;" id="male_lookup"
                 onclick="femalePersonLookUp('identification_female');">
            <br>

            <div id="femaleNotFound" style="color:red; font-size:11pt"/>
        </td>
        </td>
        <td colspan="1">
            උපන් දිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td colspan="3">
            <s:textfield name="marriage.female.dateOfBirthFemale" id="date_of_birth_female" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            ජාතිය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            in tamil <br>
            Race
        </td>
        <td colspan="2">
            <s:select list="raceList" id="raceFemaleId" name="raceIdFemale" headerKey="0"
                      headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;"/>
        </td>
        <td colspan="1">
            පසුවූ උපන් දිනයට වයස <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            in tamil <br>
            Age at last Birthday
        </td>
        <td colspan="3">
            <s:textfield name="marriage.female.ageAtLastBirthDayFemale" id="age_at_last_bd_female" maxLength="3"
                         value="%{marriage.female.ageAtLastBirthDayFemale}" onfocus="partyAge(1);"/>
        </td>
    </tr>
    <tr>
        <td>
            විදේශිකයකු නම් <br>
            வெளிநாட்டவர் எனின் <br>
            If a foreigner
        </td>
        <td>
            රට <br>
            நாடு <br>
            Country
        </td>
        <td>
            <s:select list="countryList" name="countryIdFemale" headerKey="0"
                      headerValue="%{getText('select_country.label')}"
                      cssStyle="width:150px;"/>
        </td>
        <td>
            ගමන් බලපත්‍ර අංකය <br>
            கடவுச் சீட்டு இல. <br>
            Passport No.
        </td>
        <td>
            <s:textfield name="marriage.female.passport" id="passport_female" maxLength="15"
                         cssStyle="width:120px" value="%{marriage.female.passport}"/>
        </td>
        <td>ලංකාවට පැමිණි දිනය <br>
            in tamil <br>
            Date of Arrival
        </td>
        <td>
            <s:textfield name="marriage.female.dateOfArrival" id="date_arrival_female" maxLength="10"
                         cssStyle="width:80px" value="%{marriage.female.dateOfArrival}"/>
        </td>
    </tr>
    <tr>
        <td colspan="1" height="100x">
            නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="6" height="100x">
            <s:textarea name="marriage.female.nameInOfficialLanguageFemale" id="name_official_female"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1" height="100x">
            නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பெயர் ஆங்கில மொழியில் <br>
            Name in English
        </td>
        <td colspan="6" height="100x">
            <s:textarea name="marriage.female.nameInEnglishFemale" id="name_english_female" cssStyle="width:98.2%;"/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;"
                 id="regName" onclick="transliterate('name_official_female', 'name_english_female')">
        </td>
    </tr>
</table>
<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption></caption>
    <col width="233px"/>
    <col width="140px"/>
    <col width="50px"/>
    <col width="140px"/>
    <col width="50px"/>
    <col width="150px"/>
    <col width="50px"/>
    <col width="150px"/>
    <col width="50px"/>
    <tbody>
    <tr>
        <td rowspan="2">
            පදිංචි ලිපිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            தற்போதைய வதிவிட முகவரி <br>
            Resident Address
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language<s:label value="*"
                                                                                                cssStyle="color:red;font-size:10pt"/></td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.female.residentAddressFemaleInOfficialLang" id="address_female_official"
                        cssStyle="width:98.2%;"/>
            <br>
            <img src="<s:url value="/images/transparent.png"/>"
                 style="vertical-align:bottom;margin:5px;width:80px;height:30px">

        </td>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.female.residentAddressFemaleInEnglish" id="address_female_english"
                        cssStyle="width:75%;"/>
            <br/>
            <img src="<s:url value="/images/transliterate.png"/>"
                 style="vertical-align:middle;margin:5px;width:80px;height:30px"
                 id="regName2" onclick="transliterate('address_female_official', 'address_female_english')">
        </td>
    </tr>
    <tr>
        <td rowspan="2">
            තරාතිරම නොහොත් රක්ෂාව <br>
            in tamil <br>
            Rank or Profession
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language</td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="40px">
            <s:textfield name="marriage.female.rankOrProfessionFemaleInOfficialLang" id="rank_female_official"
                         cssStyle="width:98.2%;"
                         maxLength="255"/>
            <br/>
            <img src="<s:url value="/images/transparent.png"/>"
                 style="vertical-align:middle;margin:5px;width:80px;height:30px">
        </td>
        <td colspan="4" height="40px">
            <s:textfield name="marriage.female.rankOrProfessionFemaleInEnglish" id="rank_female_english"
                         cssStyle="width:98.2%;"
                         maxLength="255"/>
            <br/>
            <img src="<s:url value="/images/transliterate.png"/>"
                 style="vertical-align:middle;margin:5px;width:80px;height:30px"
                 onclick="transliterateTextField('rank_female_official', 'rank_female_english')">
        </td>
    </tr>
    <tr>
        <td>
            සිවිල් තත්වය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            சிவில் நிலைமை <br>
            Civil Status
        </td>
        <td>
            අවිවාහක <br>
            திருமணமாகாதவர் <br>
            Never Married
        </td>
        <td>
            <s:radio name="marriage.female.civilStatusFemale" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"
                     value="%{marriage.female.civilStatusFemale}"/>
        </td>
        <td>
            දික්කසාද <br>
            திருமணம் தள்ளுபடி செய்தவர் <br>
            Divorced
        </td>
        <td>
            <s:radio name="marriage.female.civilStatusFemale" list="#@java.util.HashMap@{'DIVORCED':''}"
                     value="%{marriage.female.civilStatusFemale}"/>
        </td>
        <td>
            වැන්දබු <br>
            விதவை <br>
            Widowed
        </td>
        <td>
            <s:radio name="marriage.female.civilStatusFemale" list="#@java.util.HashMap@{'WIDOWED':''}"
                     value="%{marriage.female.civilStatusFemale}"/>
        </td>
        <td>
            නිෂ්ප්‍රභාකර ඇත <br>
            தள்ளிவைத்தல் <br>
            Anulled
        </td>
        <td>
            <s:radio name="marriage.female.civilStatusFemale" list="#@java.util.HashMap@{'ANNULLED':''}"
                     value="%{marriage.female.civilStatusFemale}"/>
        </td>
    </tr>
    <tr>
        <td>
            පියාගේ අනන්‍යතා අංකය <br>
            தந்தையின் அடையாள எண் <br>
            Fathers Identification Number
        </td>
        <td colspan="8">
            <s:textfield name="marriage.female.fatherIdentificationNumberFemale" id="father_pin_or_nic_female"
                         cssStyle="width:240px;" maxLength="10"/>
            <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;" id="male_lookup"
                 onclick="femalePartyFather('father_pin_or_nic_female');">
        </td>
    </tr>
    <tr>
        <td rowspan="2">
            පියාගේ සම්පුර්ණ නම <br>
            தந்தையின் அடையாள <br>
            Fathers full name
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language</td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.female.fatherFullNameFemaleInOfficialLang" id="father_full_name_female_official"
                        cssStyle="width:98.2%;"/>
            </br>
            <img src="<s:url value="/images/transparent.png"/>"
                 style="vertical-align:middle;margin:5px;width:80px;height:30px">
        </td>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.female.fatherFullNameFemaleInEnglish" id="father_full_name_female_english"
                        cssStyle="width:98.2%;"/>
            </br>
            <img src="<s:url value="/images/transliterate.png"/>"
                 style="vertical-align:middle;margin:5px;width:80px;height:30px"
                 onclick="transliterate('father_full_name_female_official', 'father_full_name_female_english')">
        </td>
    </tr>
    </tbody>
</table>
</s:if>
<div class="form-submit">

    <s:if test="editMode">
        <s:submit value="%{getText('button.edit.notice')}"/>
    </s:if>
    <s:else>
        <s:submit value="%{getText('button.add.notice')}"/>
    </s:else>
</div>

<s:hidden id="notice_type" name="noticeType" value="%{noticeType}"/>
<s:hidden name="licenseReqByMale" value="%{licenseReqByMale}"/>
</s:form>
<s:hidden id="text_invalid_data" value="%{getText('error.invalid.data')}"/>
<s:hidden id="text_must_fill" value="%{getText('error.must.fill.data')}"/>
<s:hidden id="text_must_select" value="%{getText('error.must.select.data')}"/>
<s:hidden id="text_serial_number" value="%{getText('field.serial.number')}"/>
<s:hidden id="text_submit_date" value="%{getText('field.notice.submit.date')}"/>
<s:hidden id="text_dob_male" value="%{getText('field.date.of.birth.male')}"/>
<s:hidden id="text_dob_female" value="%{getText('field.date.of.birth.female')}"/>
<s:hidden id="text_id_male" value="%{getText('field.id.number.male')}"/>
<s:hidden id="text_id_female" value="%{getText('field.id.number.female')}"/>
<s:hidden id="text_fa_id_male" value="%{getText('field.fa.id.number.male')}"/>
<s:hidden id="text_fa_id_female" value="%{getText('field.fa.id.number.female')}"/>
<s:hidden id="text_date_arrival_male" value="%{getText('field.date.of.arrival.male')}"/>
<s:hidden id="text_date_arrival_female" value="%{getText('field.date.of.arrival.female')}"/>
<s:hidden id="text_passport_male" value="%{getText('field.passport.number.male')}"/>
<s:hidden id="text_passport_female" value="%{getText('field.passport.number.female')}"/>
<s:hidden id="text_age_at_last_bd_male" value="%{getText('field.age.at.last.bd.male')}"/>
<s:hidden id="text_age_at_last_bd_female" value="%{getText('field.age.at.last.bd.female')}"/>
<s:hidden id="text_race_male" value="%{getText('field.race.male')}"/>
<s:hidden id="text_race_female" value="%{getText('field.race.female')}"/>
<s:hidden id="text_name_official_male" value="%{getText('field.name.official.male')}"/>
<s:hidden id="text_name_official_female" value="%{getText('field.name.official.female')}"/>
<s:hidden id="text_address_official_male" value="%{getText('field.address.official.male')}"/>
<s:hidden id="text_address_official_female" value="%{getText('field.address.official.female')}"/>
<s:hidden id="text_marriage_type" value="%{getText('field.marriage.type')}"/>
<s:hidden id="text_civil_state_male" value="%{getText('field.civil.state.male')}"/>
<s:hidden id="text_civil_state_female" value="%{getText('field.civil.state.female')}"/>
<s:hidden id="text_gt_zero" value="%{getText('error.must.gt.zero')}"/>
<s:hidden id="text_gt_18" value="%{getText('error.must.lt.18')}"/>
<s:hidden id="text_future_date" value="%{getText('error.future.date')}"/>

<%--additional validations--%>
<s:hidden id="text_select_how_collect_license" value="%{getText('field.how.collect.license')}"/>

<s:hidden id="text_select_proffered_language" value="%{getText('field.what.is.the.pref.language')}"/>
<s:hidden id="notFound" value="%{getText('message.unable.to.find.person.for.given.pin')}"/>
<s:hidden id="maleNotFoundError" value="%{malePartyNotFound}"/>
<s:hidden id="femaleNotFoundError" value="%{femalePartyNotFound}"/>
</div>




