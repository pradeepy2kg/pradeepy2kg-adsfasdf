<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/common.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/agecalculator.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<script>
$(function () {
    $("#receivedDatePicker").datepicker({
        changeYear:true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'

    });
});
$(function () {
    $("#bdayDatePicker").datepicker({
        changeYear:true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31',
        onSelect:function () {
            var bday = new Date(document.getElementById('bdayDatePicker').value);
            var age = calculateAge(bday, new Date());
            document.getElementById("childAgeYears").value = age[0];
            document.getElementById("childAgeMonths").value = age[1];
        }
    });

    $('#bdayDatePicker').bind('change',function(){
        var bday = new Date(document.getElementById('bdayDatePicker').value);
        var age = calculateAge(bday, new Date());
        document.getElementById("childAgeYears").value = age[0];
        document.getElementById("childAgeMonths").value = age[1];
    });

    $('#bdayDatePicker').bind('click',function(){
        var bday = new Date(document.getElementById('bdayDatePicker').value);
        var age = calculateAge(bday, new Date());
        document.getElementById("childAgeYears").value = age[0];
        document.getElementById("childAgeMonths").value = age[1];
    });
});


$(function () {
    $("#orderIssuedDatePicker").datepicker({
        changeYear:true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});


$(function () {
    if ($('#adoptionEntryNo').val() <= 0) {
        $('#adoptionEntryNo').val('');
    }
    $('select#birthDistrictId').bind('change', function (evt1) {
        var id = $("select#birthDistrictId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:5},
                function (data) {
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

    $('select#dsDivisionId').bind('change', function (evt2) {
        var id = $("select#dsDivisionId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                function (data) {
                    var options = '';
                    var bd = data.bdDivisionList;
                    for (var i = 0; i < bd.length; i++) {
                        options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                    }
                    $("select#birthDivisionId").html(options);
                });
    });

    $(function () {
        $('img#adoption_applicant_lookup').bind('click', function (evt3) {
            var id1 = $("input#applicantPin").attr("value");

            $("textarea#applicantName").val('');
            $("textarea#applicantAddress").val('');
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                    function (data1) {
                        $("textarea#applicantName").val(data1.fullNameInOfficialLanguage);
                        $("textarea#applicantAddress").val(data1.lastAddress);
                    });
        });
    });
    $(function () {
        $('img#mother_lookup').bind('click', function (evt3) {
            var id1 = $("input#wifePINorNIC").attr("value");

            $("textarea#wifeName").val('');
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                    function (data1) {
                        $("textarea#wifeName").val(data1.fullNameInOfficialLanguage);
                    });
        });
    });

})


//these inpute can not be null
var errormsg = "";
function validate() {

    var returnval = true;
    var domObject;
    //order issued date
    domObject = document.getElementById("orderIssuedDatePicker");
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, "", 'error2');
    } else {
        isDate(domObject.value, 'error12', 'error2');
    }
    domObject = document.getElementById("receivedDatePicker");
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, "", 'error0');
    } else {
        isDate(domObject.value, 'error12', 'error0');
    }
    domObject = document.getElementById("courtOrderNumber");
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, "", 'error3');
    }
    domObject = document.getElementById("applicantName");
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, "", 'error5');
    }
    domObject = document.getElementById("applicantAddress");
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, "", 'error6');
    }
    domObject = document.getElementById("applicantPin");
    if (!isFieldEmpty(domObject)) {
        validatePINorNIC(domObject, 'error12', 'error13');
    }
    domObject = document.getElementById("childAgeYears");
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, "", 'error7');
    }
    else {
        isNumeric(domObject.value, 'error12', 'error7');
    }
    domObject = document.getElementById("childAgeMonths");
    if (!isFieldEmpty(domObject)) {
        isNumeric(domObject.value, 'error12', 'error8');
    }
    domObject = document.getElementById("childPIN");
    if (!isFieldEmpty(domObject)) {
        validatePINorNIC(domObject, 'error12', 'error20');
    }
    domObject = document.getElementById("bdayDatePicker");
    if (!isFieldEmpty(domObject)) {
        isDate(domObject.value, 'error12', 'error14');
    }
    domObject = document.getElementById("birthRegistrationSrialNum");
    if (!isFieldEmpty(domObject)) {
        isNumeric(domObject.value, 'error12', 'error15');
    }
    domObject = document.getElementById("birthCertificateNumber");
    if (!isFieldEmpty(domObject)) {
        isNumeric(domObject.value, 'error12', 'error16');
    }
    domObject = document.getElementById("childNewName");
    existingName = document.getElementById("childExistingName");
    if (isFieldEmpty(domObject) && isFieldEmpty(existingName)) {
        if ((domObject.value == null || domObject.value == "") || (existingName.value == null || existingName.value == "")) {
            errormsg = errormsg + "\n" + document.getElementById("error17").value;
        }
    }

    var issueDate = new Date(document.getElementById('orderIssuedDatePicker').value);
    var receivedDate = new Date(document.getElementById('receivedDatePicker').value);
    if (issueDate.getTime() > receivedDate.getTime()) {
        printMessage("error12", "error18");
    }
    domObject = document.getElementById('wifePINorNIC');
    if (domObject.disabled == false && !isFieldEmpty(domObject)) {
        validatePINorNIC(domObject, "error12", "error19")
    }

    domObject = document.getElementById('adoptionEntryNo');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById("error22").value;
    } else if ($('#adoptionEntryNo').val() <= 0) {
        errormsg = errormsg + "\n" + document.getElementById("error23").value;
    }

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

function disable(mode) {
    document.getElementById('wifePINorNIC').disabled = mode;
    document.getElementById('wifeCountryId').disabled = mode;
    document.getElementById('wifePassport').disabled = mode;
    document.getElementById('wifeName').disabled = mode;
}
//todo 
function initPage() {
    var domObject = document.getElementById('birthCertificateNumber');
    if (domObject.value.trim() == 0) {
        domObject.value = null;
    }
    domObject = document.getElementById('birthRegistrationSrialNum');
    if (domObject.value.trim() == 0) {
        domObject.value = null;
    }
    domObject = document.getElementById('childAgeMonths');
    if (domObject.value.trim() == 0) {
        domObject.value = null;
    }
    domObject = document.getElementById('childAgeYears');
    if (domObject.value.trim() == 0 && document.getElementById('childAgeMonths').value.trim() < 1) {
        domObject.value = null;
    }
    document.getElementById('birthCertificateNumber').disabled = true;
    document.getElementById('birthDistrictId').disabled = true;
    document.getElementById('dsDivisionId').disabled = true;
    document.getElementById('birthDivisionId').disabled = true;
    document.getElementById('birthRegistrationSrialNum').disabled = true;
    //document.getElementById('availabeSerial').disabled = true;
}

function enableCertificateNumber(mode) {
    document.getElementById('birthCertificateNumber').disabled = mode;
}

function enableSerialNumber(mode) {
    document.getElementById('birthDistrictId').disabled = mode;
    document.getElementById('dsDivisionId').disabled = mode;
    document.getElementById('birthDivisionId').disabled = mode;
    document.getElementById('birthRegistrationSrialNum').disabled = mode;
}
</script>


<div id="adoption-registration-form-outer">
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:form action="eprAdoptionAction.do" name="adoptionRegistrationForm" id="" method="POST"
        onsubmit="javascript:return validate()">
<table class="adoption-reg-form-header-table" cellspacing="1" cellpadding="1">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="450px"></td>
        <td align="center" style="font-size:12pt; width:130px"><img
                src="<s:url value="/images/official-logo.png"/>"
                alt=""/></td>
        <td width="450px"></td>
    </tr>
    <tr>
        <td colspan="3" align="center">
            දරුකමට හදාගැනීමේ උසාවි නියෝගය ලියාපදිංචි කිරීම (අංක 4 දරන ආකෘති පත්‍රය)
            <br>நீதிமன்ற மகவேற்புக் கட்டளையினை பதிவு செய்தல் ( 4 ஆம் இலக்க விண்ணப்ப படிவம்)
            <br>Registration of an Adoption Order Issued by Court
        </td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <tr>
        <td>
            සටහනේ අංකය<br>
            பதிவு இலக்கம்<br>
            Number of Entry
        </td>
        <td>
            <s:textfield id="adoptionEntryNo" name="adoption.adoptionEntryNo"
                         onkeypress="return numbersOnly(event, true);"/>
        </td>
        <td>
            සටහනේ දිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>பதிவு திகத
            <br>Date of Entry
        </td>
        <td>
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
            <s:textfield id="receivedDatePicker" name="adoption.orderReceivedDate" cssStyle="width:200px"
                         maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">අධිකරණය
            <br>நீதிமன்றம்
            <br>Court
        </td>
        <td colspan="2"><s:select list="courtList" name="courtId" cssStyle="margin-left:5px;width:300px;"
                                  value="%{courtId}"/>
    </tr>
    <tr>
        <td colspan="2">
            නියෝගය නිකුත් කල දිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>கட்டளை வழங்கப்பட்ட திகதி
            <br>Issued Date
        </td>
        <td colspan="2">
            <s:label value="YYYY-MM-DD" cssStyle="font-size:10px"/><br>
            <s:textfield id="orderIssuedDatePicker" name="adoption.orderIssuedDate"
                         cssStyle="margin-left:5px;width:200px" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            නියෝග අංකය <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>கட்டளை இலக்கம்
            <br>Court order number
        </td>
        <td colspan="2">
            <s:fielderror name="duplicateCourtOrderNumberError" cssStyle="color:red;font-size:10pt"/>
            <s:textfield name="adoption.courtOrderNumber" id="courtOrderNumber" cssStyle="margin-left:5px;"
                         maxLength="240"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            විනිසුරු නම
            <br>நீதிபதியின் பெயா்
            <br>Name of the Judge
        </td>
        <td colspan="2"><s:textfield name="adoption.judgeName" id="judgeName" cssStyle="margin-left:5px;"
                                     maxLength="250"/></td>
    </tr>
    <tr>
        <td colspan="2">සහතිකය නිකුත් කල යුතු භාෂාව <br>சான்றிதழ் வழங்கப்பட வேண்டிய மொழி <br>Preferred
            Language for
        </td>
        <td colspan="2" style="text-align:left;" width="30px">
            <s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'தமிழ்'}"
                      name="adoption.languageToTransliterate"
                      cssStyle="width:200px; margin-left:5px;"></s:select>
        </td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>
            අයදුම් කරුගේ විස්තර
            <br>விண்ணப்பதரியின் விபரங்கள்
            <br>Applicants Details
        </td>
    </tr>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col width="320px"/>
    <col width="175px"/>
    <col width="175px"/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td>
            අයදුම් කරු
            <br>விண்ணப்பதாரி
            <br>Applicant
        </td>
        <td>
            පියා
            <br>தந்தை
            <br>Father
        </td>
        <td>
            <s:radio name="adoption.applicantMother" list="#@java.util.HashMap@{'false':''}"
                     id="adoptionApplicantFather"
                     onclick="disable(false)"/>
        </td>
        <td>
            මව
            <br>தாய்
            <br>Mother
        </td>
        <td>
            <s:radio name="adoption.applicantMother" list="#@java.util.HashMap@{'true':''}" onclick="disable(true);"/>
        </td>
    </tr>
    <tr>
        <td>
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification No.
        </td>
        <td colspan="5" align="left" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="applicant_NIC_V" onclick="javascript:addXorV('applicantPin','V','error21')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="applicant_NIC_X" onclick="javascript:addXorV('applicantPin','X','error21')">
            <br>
            <s:textfield name="adoption.applicantPINorNIC"
                         id="applicantPin"
                         cssStyle="float:left;width:200px;" maxLength="12"/>
            <img src="<s:url value="/images/search-father.png" />"
                 style="vertical-align:middle; margin-left:10px;" id="adoption_applicant_lookup"></td>
    </tr>
    <tr>
        <td>
            විදේශිකය‍කු නම්
            <br>வெளிநாட்டவர் எனின்,
            <br>If a foreigner
        </td>
        <td>
            රට
            <br>நாடு
            <br>Country
        </td>
        <td>
            <s:select name="adoption.applicantCountryId" list="countryList" headerKey="0" id="applicantCountryId"
                      headerValue="%{getText('adoption.select_country.label')}" cssStyle="width:150px"/>
        </td>
        <td>
            ගමන් බලපත්‍ර අංකය
            <br>கடவுச் சீட்டு இல.
            <br>Passport No.
        </td>
        <td><s:textfield name="adoption.applicantPassport" id="applcantPassportNumber" cssStyle="width:90%"
                         maxLength="15"/></td>
    </tr>
    <tr>
        <td>
            අයදුම් කරුගේ නම <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>விண்ணப்பதாரியின் பெயா்
            <br>Name of the Applicant
        </td>
        <td colspan="4"><s:textarea id="applicantName" name="adoption.applicantName"/></td>
    </tr>

    <tr>
        <td>
            ලිපිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>முகவரி
            <br>Address
        </td>
        <td colspan="4"><s:textarea name="adoption.applicantAddress"
                                    id="applicantAddress"/></td>
    </tr>
    </tbody>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td><br/>
            අයදුම් කරු පියා නම් මවගේ විස්තර
            <br> விண்ணப்பதாரி தந்தையாயின் தாயின் விபரங்கள்
            <br>If applicant is the father,
            Mother's details
        </td>
    </tr>
</table>
<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col width="320px"/>
    <col width="175px"/>
    <col width="175px"/>
    <col/>
    <col/>
    <tr>
        <td>
            මවගේ අනන්‍යතා අංකය
            <br>தாயின் அடையாள எண்
            <br>Mothers Identification Number
        </td>
        <td colspan="5" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="wife_NIC_V" onclick="javascript:addXorV('wifePINorNIC','V','error21')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="wife_NIC_X" onclick="javascript:addXorV('wifePINorNIC','X','error21')">
            <br>
            <s:textfield name="adoption.wifePINorNIC" id="wifePINorNIC"
                         cssStyle="float:left;width:200px;" maxLength="12"/>
            <img src="<s:url value="/images/search-mother.png" />"
                 style="vertical-align:middle; margin-left:10px;" id="mother_lookup"></td>
    </tr>
    <tr>
        <td>විදේශිකය‍කු නම් <br/>
            வெளிநாட்டவர் எனின்<br/>
            If a foreigner
        </td>
        <td>රට <br/>
            நாடு <br/>
            Country
        </td>
        <td width="175px">
            <s:select id="wifeCountryId" name="adoption.wifeCountryId" list="countryList" headerKey="0"
                      headerValue="%{getText('adoption.select_country.label')}" cssStyle="width:90%"/>
        </td>
        <td>ගමන් බලපත්‍ර අංකය <br/>
            கடவுச் சீட்டு இல. <br/>
            Passport No.
        </td>
        <td>
            <s:textfield name="adoption.wifePassport" id="wifePassport" cssStyle="width:90%"
                         maxLength="15"> </s:textfield>
        </td>
    </tr>
    <tr>
        <td>
            මවගේ නම
            <br>தாயின் பெயா்
            <br>Name of Mother
        </td>
        <td colspan="4"><s:textarea name="adoption.wifeName" id="wifeName"/></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>
            ළමයාගේ විස්තර
            <br>பிள்ளையின் விபரங்கள்
            <br>Child's Information
        </td>
    </tr>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col style="width:320px;"/>
    <col style="width:160px;"/>
    <col style="width:190px;"/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td>
            අනන්‍යතා අංකය (තිබේ නම්)
            <br>அடையாள எண் (இருந்தால்)
            <br>Identification Number (if available)
        </td>
        <td colspan="4"><s:textfield name="adoption.childPIN" id="childPIN"
                                     cssStyle="margin-left:5px;width:250px"/></td>
    </tr>
    <tr>
        <td>
            උපන් දිනය
            <br>பிறந்த திகதி
            <br>Date of Birth
        </td>
        <td colspan="2">
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
            <s:textfield id="bdayDatePicker" name="adoption.childBirthDate"
                         cssStyle="margin-left:5px;width:200px" onchange="calYearAndMonth()" maxLength="10"/>
        </td>
        <td>
            ස්ත්‍රී පුරුෂ භාවය
            <br>பால்
            <br>Gender
        </td>
        <td><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="adoption.childGender"
                id="childGender"
                cssStyle="width:150px; margin-left:5px;"/></td>
    </tr>
    <tr>
        <td>
            වයස <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>வயது
            <br>Age
        </td>
        <td>
            අවුරුදු
            <br>வருடங்கள்
            <br>Years
        </td>
        <td><s:textfield name="adoption.childAgeYears" id="childAgeYears" cssStyle="width:87%"
                         onchange="validateNum(document.getElementById('childAgeYears').value)" maxLength="3"
                         value="%{adoption.childAgeYears}"/></td>
        <td>
            මාස
            <br>மாதங்கள்
            <br>Months
        </td>
        <td><s:textfield name="adoption.childAgeMonths" id="childAgeMonths" onclick="calYearAndMonth()"
                         cssStyle="width:91%"
                         onchange="validateNum(document.getElementById('childAgeMonths').value)" maxLength="2"
                         value="%{adoption.childAgeMonths}"/></td>
    </tr>
    <tr>
        <td>
            දැනට පවතින නම
            (නමක් දී ඇති නම්) <s:label value="*" cssStyle="color:blue;font-size:11pt"/>
            <br>தற்போதைய பெயா்
            (ஏற்கனவே பெயா் வழங்கப்பட்டிருந்தால்)
            <br>Existing Name
            (if already given)
        </td>
        <td colspan=" 4"><s:textarea name="adoption.childExistingName"
                                     id="childExistingName"/></td>
    </tr>
    <tr>
        <td>
            ලබා දෙන නම රාජ්‍ය භාෂාවෙන් <s:label value="*" cssStyle="color:blue;font-size:11pt"/>
            <br>புதிய பெயா் அரச கரும மொழியில்
            <br>New name given in Official Language
        </td>
        <td colspan="4"><s:textarea name="adoption.childNewName" id="childNewName" cssStyle="margin-left:5px"/></td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td>
            උපත දැනටත් ලියාපදිංචි කර උප්පැන්න සහතිකයක් නිකුත් කර ඇතිනම්
            <br>பிறப்பு பதியப்பட்டு பிறப்புச் சான்றிதழ் வழங்கப்பட்டிருந்தால்
            <br>If the birth is already registered, and a Birth Certificate issued
        </td>
    </tr>
</table>


<table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
    <col width="320px"/>
    <col width="100px"/>
    <col width="50px">
    <col width="100px"/>
    <col width="50px">
    <col/>
    <tr>
        <td colspan="1">
            උප්පැන්න සහතිකයේ අනුක්‍රමික අංකය
            <br>பிறப்புச் சான்றிதழின் தொடா் இலக்கம்
            <br>The serial number of the Birth Certificate
        </td>
        <td colspan="1">
            ඇත
            <br>
            Available
        <td colspan="1"><s:radio list="#@java.util.HashMap@{'false':''}" id="availabe" name="available"
                                 onclick="enableCertificateNumber(false)" cssStyle="margin-left:14px"/></td>

        </td>
        <td colspan="1">නැත <br>Un-available</td>
        <td colspan="1"><s:radio list="#@java.util.HashMap@{'false':''}" id="availabe" name="available"
                                 onclick="enableCertificateNumber(true);" cssStyle="margin-left:14px"/></td>
        </td>
        <td colspan="1"><s:textfield name="adoption.birthCertificateNumber" id="birthCertificateNumber"
                                     maxLength="10"/></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>හෝ
            / அல்லது
            / OR
        </td>
    </tr>
</table>

<table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="2">
    <caption></caption>
    <col width="320px"/>
    <col/>
    <tbody>
    <tr>
        <td colspan="2">
            උපත ලියාපදිංචි කිරීමේ රිසීට් පතේ සටහන්
            <br>பிறப்பு பதிவிற்கான பற்றுச்சீட்டின் குறிப்பு
            <br>Birth Registration acknowledgment slip
        </td>
        <td> ඇත
            <br>
            Available
        </td>
        <td><s:radio list="#@java.util.HashMap@{'false':''}" id="availableSlip" name="availableSlip"
                     onclick="enableSerialNumber(false)" cssStyle="margin-left:100px"/></td>
        <td>නැත <br>Un-available</td>
        <td><s:radio list="#@java.util.HashMap@{'false':''}" id="availabeSlip" name="availableSlip"
                     onclick="enableSerialNumber(true)" cssStyle="margin-left:100px"/></td>
    </tr>
    <tr>
        <td colspan="1">
            දිස්ත්‍රික්කය
            <br>மாவட்டம்
            <br>District
        </td>
        <td colspan="5"><s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                                  value="%{birthDistrictId}"
                                  cssStyle="width:280px;margin-left:5px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය
            <br/>பிரதேச செயளாளர் பிரிவு
            <br/>Divisional Secretary Division
        </td>
        <td colspan="5">
            <s:select id="dsDivisionId" name="dsDivisionId" list="allDSDivisionList" value="%{dsDivisionId}"
                      cssStyle="float:left;  width:280px;;margin-left:5px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            ලියාපදිංචි කිරීමේ කොට්ටාශය
            <br>பதிவுப்பிரிவு
            <br>Registration Division
        </td>
        <td colspan="5">
            <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}"
                      list="bdDivisionList"
                      cssStyle=" width:280px;float:left;;margin-left:5px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            අනුක්‍රමික අංකය
            <br>தொடா் இலக்கம்
            <br>Serial Number
        </td>

        <td colspan="5">
            <s:textfield name="adoption.birthRegistrationSerial" id="birthRegistrationSrialNum"
                         cssStyle="width:273px;margin-left:5px" maxLength="10"/>
        </td>
    </tr>
    </tbody>
</table>

<s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
<s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
<s:hidden name="currentStatus" value="%{#request.currentStatus}"/>
<s:hidden name="pageNo" value="%{#request.pageNo}"/>
<s:hidden name="idUKey" value="%{#request.idUKey}"/>
<%--<s:hidden name="pageNo" value="1"/>--%>
<div class="button" align="right">
    <s:submit value="%{getText('adoption.submit')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
<s:hidden id="error0" value="%{getText('er.label.receivedDate')}"/>
<s:hidden id="error2" value="%{getText('er.label.orderIssuedDate')}"/>
<s:hidden id="error3" value="%{getText('er.label.courtOrderNumber')}"/>
<s:hidden id="error4" value="%{getText('er.label.judgeName')}"/>
<s:hidden id="error5" value="%{getText('er.label.applicantName')}"/>
<s:hidden id="error6" value="%{getText('er.label.applicantAddress')}"/>
<s:hidden id="error7" value="%{getText('er.label.childAgeYears')}"/>
<s:hidden id="error8" value="%{getText('er.label.childAgeMonths')}"/>
<s:hidden id="error9" value="%{getText('er.label.childName')}"/>
<s:hidden id="error10" value="%{getText('er.label.childAgeYearsValid')}"/>
<s:hidden id="error11" value="%{getText('er.label.childAgeMonthsValid')}"/>
<s:hidden id="lable01" value="%{getText('label.childAgeYear')}"/>
<s:hidden id="lable02" value="%{getText('label.childAgeMonth')}"/>
<s:hidden id="error12" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error13" value="%{getText('er.label.applicantPINorNIC')}"/>
<s:hidden id="error14" value="%{getText('er.label.childBirthDate')}"/>
<s:hidden id="error15" value="%{getText('er.label.birthRegistrationSrialNum')}"/>
<s:hidden id="error16" value="%{getText('er.label.birthCertificateNumber')}"/>
<s:hidden id="error17" value="%{getText('er.label.child.newNameOrExistingName')}"/>
<s:hidden id="error18" value="%{getText('er.label.orderIssuedDate.orderRecievedDate')}"/>
<s:hidden id="error19" value="%{getText('er.label.wifePINorNIC')}"/>
<s:hidden id="error20" value="%{getText('er.label.childPIN')}"/>
<s:hidden id="error21" value="%{getText('NIC.error.add.VX')}"/>
<s:hidden id="error22" value="%{getText('er.label.no.adoption.entry.number')}"/>
<s:hidden id="error23" value="%{getText('er.label.invalid.adoption.entry.number')}"/>

</div>