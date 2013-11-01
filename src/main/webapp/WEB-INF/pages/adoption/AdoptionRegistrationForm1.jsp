<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/common.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/agecalculator.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<script type="text/javascript">
$(function () {
    document.getElementById('birthCertificateNumber').disabled = true;
    document.getElementById('oldBirthSLIN').disabled = true;
    document.getElementById('birthProvinceUKey').disabled = true;
    document.getElementById('birthDistrictId').disabled = true;
    document.getElementById('oldBirthDSName').disabled = true;
    document.getElementById('oldBirthRegistrationDivisionName').disabled = true;
    document.getElementById('oldBirthRegistrationDate').disabled = true;
    if ($('#birthCertificateNumber').val() > 0) {
        $('#availableNewCerttrue').attr('checked', 'checked');
        enableCertificateNumber(false);
    } else if ($('#birthProvinceUKey').val() > 0 || $('#birthDistrictId').val() > 0 || $('#oldBirthDSName').val() != '' || $('#oldBirthRegistrationDivisionName').val() != '' || $('#oldBirthRegistrationDate').val() != '') {
        $('#availableNewCertfalse').attr('checked', 'checked');
        enableCertificateInfo(false);
    }

    $("#receivedDatePicker").datepicker({
        changeYear:true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'

    });
    $("#oldBirthRegistrationDate").datepicker({
        changeYear:true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'

    });
});

$(function () {
    if ($('#idUKey').val() == 0 || $('#jointApplicantfalse').is(':checked')) {
        disable(true);
    }
    $('#clearInfo').val($('#clear').val());
    $('#clearInfo').bind('click', function () {
        $('#availableNewCerttrue').attr('checked', false);
        $('#availableNewCertfalse').attr('checked', false);
        $('#birthCertificateNumber').attr('value', ' ');
        $('#oldBirthSLIN').attr('value', ' ');
        $('#oldBirthDSName').attr('value', ' ');
        $('#oldBirthRegistrationDivisionName').attr('value', ' ');
        $('#oldBirthRegistrationDate').attr('value', ' ');
        document.getElementById('birthCertificateNumber').disabled = true;
        document.getElementById('oldBirthSLIN').disabled = true;
        document.getElementById('birthProvinceUKey').disabled = true;
        document.getElementById('birthDistrictId').disabled = true;
        document.getElementById('oldBirthDSName').disabled = true;
        document.getElementById('oldBirthRegistrationDivisionName').disabled = true;
        document.getElementById('oldBirthRegistrationDate').disabled = true;
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
            var oday = new Date(document.getElementById('orderIssuedDatePicker').value);
            if (oday != "Invalid Date") {
                var age = calculateAge(bday, oday);
                document.getElementById("childAgeYears").value = age[0];
                document.getElementById("childAgeMonths").value = age[1];
            }
        }
    });

    $('#bdayDatePicker').bind('change', function () {
        var bday = new Date(document.getElementById('bdayDatePicker').value);
        var oday = new Date(document.getElementById('orderIssuedDatePicker').value);
        if (oday != "Invalid Date") {
            var age = calculateAge(bday, oday);
            document.getElementById("childAgeYears").value = age[0];
            document.getElementById("childAgeMonths").value = age[1];
        }
    });

    $('#orderIssuedDatePicker').bind('change', function () {
        var bday = new Date(document.getElementById('bdayDatePicker').value);
        var oday = new Date(document.getElementById('orderIssuedDatePicker').value);
        if (bday != "Invalid Date") {
            var age = calculateAge(bday, oday);
            document.getElementById("childAgeYears").value = age[0];
            document.getElementById("childAgeMonths").value = age[1];
        }
    });

    $('#bdayDatePicker').bind('click', function () {
        var bday = new Date(document.getElementById('bdayDatePicker').value);
        var oday = new Date(document.getElementById('orderIssuedDatePicker').value);
        if (oday != "Invalid Date") {
            var age = calculateAge(bday, oday);
            document.getElementById("childAgeYears").value = age[0];
            document.getElementById("childAgeMonths").value = age[1];
        }
    });

    $('#orderIssuedDatePicker').bind('click', function () {
        var bday = new Date(document.getElementById('bdayDatePicker').value);
        var oday = new Date(document.getElementById('orderIssuedDatePicker').value);
        if (bday != "Invalid Date") {
            var age = calculateAge(bday, oday);
            document.getElementById("childAgeYears").value = age[0];
            document.getElementById("childAgeMonths").value = age[1];
        }
    });
});


$(function () {
    $("#orderIssuedDatePicker").datepicker({
        changeYear:true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31',
        onSelect:function () {
            var bday = new Date(document.getElementById('bdayDatePicker').value);
            var oday = new Date(document.getElementById('orderIssuedDatePicker').value);
            if (bday != "Invalid Date") {
                var age = calculateAge(bday, oday);
                document.getElementById("childAgeYears").value = age[0];
                document.getElementById("childAgeMonths").value = age[1];
            }
        }
    });
});


$(function () {
    if ($('#adoptionEntryNo').val() <= 0) {
        $('#adoptionEntryNo').val('');
    }

    $('select#birthProvinceUKey').bind('change', function (evt1) {
        var id = $('#birthProvinceUKey').val();
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:19}, function (data) {
            var options = '<option value="0">' + $('#select').val() + '</option>';
            var districts = data.districtList;
            for (var i = 0; i < districts.length; i++) {
                options += '<option value="' + districts[i].optionValue + '">' + districts[i].optionDisplay + '</option>';
            }
            $("select#birthDistrictId").html(options);
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
            var id1 = $("input#spousePINorNIC").attr("value");

            $("textarea#spouseName").val('');
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                    function (data1) {
                        $("textarea#spouseName").val(data1.fullNameInOfficialLanguage);
                    });
        });
    });

})


//these inpute can not be null
var errormsg = "";
function validate() {
    var orderDate = new Date($("#orderIssuedDatePicker").val());
    var childBDay = new Date($("#bdayDatePicker").val());
    var returnval = true;
    var domObject;
<%-- validate Adoption Entry Number --%>
    domObject = document.getElementById('adoptionEntryNo');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById("error22").value;
    } else if ($('#adoptionEntryNo').val() <= 0) {
        errormsg = errormsg + "\n" + document.getElementById("error23").value;
    }
<%-- Validate Entry Date --%>
    domObject = document.getElementById("receivedDatePicker");
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, "", 'error0');
    } else {
        isDate(domObject.value, 'error12', 'error0');
    }
<%-- Validate Order details --%>
    domObject = document.getElementById("orderIssuedDatePicker");
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, "", 'error2');
    } else {
        isDate(domObject.value, 'error12', 'error2');
    }
    domObject = document.getElementById("courtOrderNumber");
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, "", 'error3');
    }

<%-- Validate applicant details --%>
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
<%-- Validate spouse details --%>
    if ($('#jointApplicanttrue').is(':checked')) {
        domObject = document.getElementById("spouseName");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error24');
        }
    }
<%-- Validate child details --%>
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
        if (childBDay > orderDate) {
            errormsg = errormsg + "\n" + document.getElementById('error25').value;
        }
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
    domObject = document.getElementById('spousePINorNIC');
    if (domObject.disabled == false && !isFieldEmpty(domObject)) {
        validatePINorNIC(domObject, "error12", "error19")
    }

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

function disable(mode) {
    if (!mode) {
        $('#spouseNameMarker').css('display', 'inline');
    } else {
        $('#spouseNameMarker').css('display', 'none');
    }
    document.getElementById('spousePINorNIC').disabled = mode;
    document.getElementById('spouseCountryId').disabled = mode;
    document.getElementById('spousePassport').disabled = mode;
    document.getElementById('spouseName').disabled = mode;
    document.getElementById('spouseOccupation').disabled = mode;
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
    document.getElementById('birthProvinceUKey').disabled = true;
    document.getElementById('birthDistrictId').disabled = true;
    document.getElementById('dsDivisionId').disabled = true;
    document.getElementById('birthDivisionId').disabled = true;
    document.getElementById('birthRegistrationSrialNum').disabled = true;
    //document.getElementById('availabeSerial').disabled = true;
}

function enableCertificateNumber(mode) {
    document.getElementById('birthCertificateNumber').disabled = mode;
    document.getElementById('oldBirthSLIN').disabled = mode;
    document.getElementById('birthProvinceUKey').disabled = !mode;
    document.getElementById('birthDistrictId').disabled = !mode;
    document.getElementById('oldBirthDSName').disabled = !mode;
    document.getElementById('oldBirthRegistrationDivisionName').disabled = !mode;
    document.getElementById('oldBirthRegistrationDate').disabled = !mode;
}

function enableCertificateInfo(mode) {
    document.getElementById('birthCertificateNumber').disabled = !mode;
    document.getElementById('birthProvinceUKey').disabled = mode;
    document.getElementById('birthDistrictId').disabled = mode;
    document.getElementById('oldBirthDSName').disabled = mode;
    document.getElementById('oldBirthRegistrationDivisionName').disabled = mode;
    document.getElementById('oldBirthRegistrationDate').disabled = mode;
    document.getElementById('oldBirthSLIN').disabled = mode;
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
            <br>நீதிமன்ற மகவேற்புக் கட்டளையினை பதிவு செய்தல் (4 ஆம் இலக்க விண்ணப்ப படிவம்)
            <br>Registration of an Adoption Order Issued by Court
        </td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <tr>
        <td>
            ලියාපදිංචි කිරීමේ අංකය<s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            <%--Registration No in ta<br>--%>
            பதிவிலக்கம்  <br>
            Registration Number
        </td>
        <td>
            <s:textfield id="adoptionEntryNo" name="adoption.adoptionEntryNo"
                         onkeypress="return numbersOnly(event, true);"/>
        </td>
        <td>
            ලියාපදිංචි කිරීමේ දිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <%--<br>Date of Registration in ta--%>
            <br>பதிவுத்திகதி
            <br>Date of Registration
        </td>
        <td>
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
            <s:textfield id="receivedDatePicker" name="adoption.orderReceivedDate" cssStyle="width:200px"
                         maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">අධිකරණය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
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
            <br>நீதிபதியின் பெயர்
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
            <s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'தமிழ்', 'en':'English'}"
                      name="adoption.languageToTransliterate"
                      cssStyle="width:200px; margin-left:5px;"></s:select>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <s:if test="adoption.idUKey == 0">
                * කළාප කාර්යාලය (විශේෂයෙන් සඳහන් කර ඇත්නම්)<br/>
                <%--*Suggested Zonal Office (if any) in ta<br/>--%>
                *வலய அலுவலகம் (விசேடமாக குறிப்பிடப்பட்டிருப்பின்)<br/>
                * Zonal Office (if mentioned specially)
            </s:if>
            <s:else>
                කළාප කාර්යාලය <br/>
                Zonal Office in ta <br/>
                Zonal Office
            </s:else>
        </td>
        <td colspan="2">
            <s:select list="zonalOfficeList" name="suggesstedZonalOfficeId" cssStyle="margin-left:5px;width:300px;"
                      headerKey="0" headerValue="%{getText('adoption.select_zonal_office.label')}" value="%{suggesstedZonalOfficeId}"/>
        </td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>
            අයදුම්කරුගේ විස්තර
            <br>விண்ணப்பதாரரின் விபரங்கள்
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
            ද්විත්ව අයදුම්කරුවන්ද?
            <%--<br>Are joint applicants in ta?--%>
            <br>இணைந்த விண்ணப்பதாரர்களா?
            <br>Are Joint Applicants?
        </td>
        <td>
            ඔව්
            <br>ஆம்
            <br>Yes
        </td>
        <td>
            <s:radio id="jointApplicant" name="adoption.jointApplicant" list="#@java.util.HashMap@{'true':''}"
                     onclick="disable(false)"/>
        </td>
        <td>
            නැත
            <br>இல்லை
            <br>No
        </td>
        <td>
            <s:radio id="jointApplicant" name="adoption.jointApplicant" list="#@java.util.HashMap@{'false':''}"
                     onclick="disable(true)"/>
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
            විදේශිකයකු නම්
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
            අයදුම්කරුගේ නම <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>விண்ணப்பதாரரின் பெயர்
            <br>Name of the Applicant
        </td>
        <td colspan="4"><s:textarea id="applicantName" name="adoption.applicantName"/></td>
    </tr>
    <tr>
        <td>
            ලිපිනය 1<s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>முகவரி 1
            <br>Address 1
        </td>
        <td colspan="4"><s:textarea name="adoption.applicantAddress" cols="50" rows="4"
                                    onkeydown="return limitLines(this, event);"
                                    id="applicantAddress"/></td>
    </tr>
    <tr>
        <td>
            ලිපිනය 2
            <br>முகவரி 2
            <br>Address 2
        </td>
        <td colspan="4"><s:textarea name="adoption.applicantSecondAddress" cols="50" rows="4"
                                    onkeydown="return limitLines(this, event);"
                                    id="applicantSecondAddress"/></td>
    </tr>
    <tr>
        <td>
            රැකියාව
            <br>தொழில்
            <br>Occupation
        </td>
        <td colspan="4"><s:textarea name="adoption.applicantOccupation"
                                    id="applicantOccupation"/></td>
    </tr>
    </tbody>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td><br/>
            සහකරුගේ විස්තර
            <br>Spouse's details in ta
            <br>Spouse's details
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
            සහකරුගේ අනන්‍යතා අංකය
            <%--<br>Identification Number of Sopuse in ta--%>
            <br>கணவன் அல்லது மனைவியின் அடையாள இலக்கம்
            <br>Identification Number of Sopuse
        </td>
        <td colspan="5" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="spouse_NIC_V" onclick="javascript:addXorV('spousePINorNIC','V','error21')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="spouse_NIC_X" onclick="javascript:addXorV('spousePINorNIC','X','error21')">
            <br>
            <s:textfield name="adoption.spousePINorNIC" id="spousePINorNIC"
                         cssStyle="float:left;width:200px;" maxLength="12"/>
            <img src="<s:url value="/images/search-mother.png" />"
                 style="vertical-align:middle; margin-left:10px;" id="mother_lookup"></td>
    </tr>
    <tr>
        <td>විදේශිකයකු නම් <br/>
            வெளிநாட்டவர் எனின்<br/>
            If a foreigner
        </td>
        <td>රට <br/>
            நாடு <br/>
            Country
        </td>
        <td width="175px">
            <s:select id="spouseCountryId" name="adoption.spouseCountryId" list="countryList" headerKey="0"
                      headerValue="%{getText('adoption.select_country.label')}" cssStyle="width:90%"/>
        </td>
        <td>ගමන් බලපත්‍ර අංකය <br/>
            கடவுச் சீட்டு இல. <br/>
            Passport No.
        </td>
        <td>
            <s:textfield name="adoption.spousePassport" id="spousePassport" cssStyle="width:90%"
                         maxLength="15"> </s:textfield>
        </td>
    </tr>
    <tr>
        <td>
            සහකරුගේ නම <s:label id="spouseNameMarker" value="*" cssStyle="color:red;font-size:11pt; display:none;"/>
            <br>தாயின் பெயர்
            <br>Name of Spouse
        </td>
        <td colspan="4"><s:textarea name="adoption.spouseName" id="spouseName"/></td>
    </tr>
    <tr>
        <td>
            සහකරුගේ රැකියාව
            <br>தாயின் தொழில்
            <br>Occupation of Spouse
        </td>
        <td colspan="4"><s:textarea name="adoption.spouseOccupation"
                                    id="spouseOccupation"/></td>
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
            <br>தற்போதைய பெயர்
            (ஏற்கனவே பெயர் குறிப்பிடப்பட்டிருந்தால் )
            <br>Existing Name
            (if already given)
        </td>
        <td colspan=" 4"><s:textarea name="adoption.childExistingName"
                                     id="childExistingName"/></td>
    </tr>
    <tr>
        <td>
            ලබා දෙන නම රාජ්‍ය භාෂාවෙන් <s:label value="*" cssStyle="color:blue;font-size:11pt"/>
            <br>பெற்றுக்கொடுக்கப்படும் பெயர் அரச கரும மொழியில்
            <br>New name given in Official Language
        </td>
        <td colspan="4"><s:textarea name="adoption.childNewName" id="childNewName" cssStyle="margin-left:5px"/></td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td colspan="3">
            උපත දැනටත් ලියාපදිංචි කර උප්පැන්න සහතිකයක් නිකුත් කර ඇතිනම්
            <br>பிறப்பு பதியப்பட்டு பிறப்புச் சான்றிதழ் வழங்கப்பட்டிருந்தால்
            <br>If the birth is already registered, and a Birth Certificate issued
            <br/>
            <input type="button" id="clearInfo" value="clear" style="float: right; margin-bottom: 5px;"/>
        </td>
    </tr>
</table>

<table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
    <col/>
    <col width="140px"/>
    <col width="60px">
    <col width="140px"/>
    <col width="60px">
    <tr>
        <td>
            උපත දැනටමත් දත්ත පද්ධතියට ඇතුළත් කර ඇතිද?<br/>
            <%--Is Birth already registered in the system? in ta<br/>--%>
            ஏற்கனவே முறைமையின் கீழ் பிறப் பதியப்பட்டுள்ளது?<br/>
            Is Birth already registered in the system?
        </td>
        <td>
            ඇත
            <br>
            உள்ளது<br/>
            Available
        </td>
        <td>
            <s:radio list="#@java.util.HashMap@{'true':''}" onclick="enableCertificateNumber(false)"
                     id="availableNewCert" name="availableNewCert"/>
        </td>
        <td>
            නැත <br>
            <%--Un-available in ta<br/>--%>
            கிடைக்கவில்லை <br/>
            Un-available
        </td>
        <td>
            <s:radio list="#@java.util.HashMap@{'false':''}" onclick="enableCertificateInfo(false)"
                     id="availableNewCert"
                     name="availableNewCert"/>
        </td>
    </tr>
    <tr>
        <td>
            උප්පැන්න සහතිකයේ අංකය<br/>
            <%--Birth Certificate Number in ta<br/>--%>
            பிறப்புச் சான்றிதழின் இலக்கம்<br/>
            Birth Certificate Number
        </td>
        <td colspan="4">
            <s:textfield name="adoption.birthCertificateNumber" id="birthCertificateNumber" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td>
            අනන්‍යතා අංකය <br/>
            அடையாள எண் <br/>
            Identification Number
        </td>
        <td colspan="4">
            <s:textfield name="adoption.oldBirthSLIN" id="oldBirthSLIN" maxLength="12"
                         onkeypress="return numbersOnly(event, true);"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            පළාත
            <%--<br>Province in ta--%>
            <br>மாவட்டம்
            <br>Province
        </td>
        <td colspan="4"><s:select id="birthProvinceUKey" name="adoption.birthProvinceUKey" list="provinceList"
                                  value="%{adoption.birthProvinceUKey}" headerKey="0"
                                  headerValue="%{getText('select.label')}"
                                  cssStyle="width:280px;margin-left:5px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            දිස්ත්‍රික්කය
            <br>மாவட்டம்
            <br>District
        </td>
        <td colspan="4"><s:select id="birthDistrictId" name="adoption.birthDistrictId" list="districtList"
                                  value="%{adoption.birthDistrictId}" headerKey="0"
                                  headerValue="%{getText('select.label')}"
                                  cssStyle="width:280px;margin-left:5px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය
            <br/>பிரதேச செயளாளர் பிரிவு
            <br/>Divisional Secretary Division
        </td>
        <td colspan="4">
            <s:textfield id="oldBirthDSName" name="adoption.oldBirthDSName"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            ලියාපදිංචි කිරීමේ කොට්ටාශය
            <br>பதிவுப்பிரிவு
            <br>Registration Division
        </td>
        <td colspan="4">
            <s:textfield id="oldBirthRegistrationDivisionName" name="adoption.oldBirthRegistrationDivisionName"/>
        </td>
    </tr>
    <tr>
        <td>
            ලියාපදිංචි කළ දිනය<br/>
            பதிவுத்திகதி<br/>
            Registration Date
        </td>
        <td colspan="4">
            <s:textfield id="oldBirthRegistrationDate" name="adoption.oldBirthRegistrationDate"/>
        </td>
    </tr>
</table>

<s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
<s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
<s:hidden name="currentStatus" value="%{#request.currentStatus}"/>
<s:hidden name="pageNo" value="%{#request.pageNo}"/>
<s:hidden id="idUKey" name="idUKey" value="%{#request.idUKey}"/>
<%--<s:hidden name="pageNo" value="1"/>--%>
<div class="button" align="right">
    <s:submit value="%{getText('adoption.submit')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
<s:hidden id="select" value="%{getText('select.label')}"/>
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
<s:hidden id="error19" value="%{getText('er.label.spousePINorNIC')}"/>
<s:hidden id="error20" value="%{getText('er.label.childPIN')}"/>
<s:hidden id="error21" value="%{getText('NIC.error.add.VX')}"/>
<s:hidden id="error22" value="%{getText('er.label.no.adoption.entry.number')}"/>
<s:hidden id="error23" value="%{getText('er.label.invalid.adoption.entry.number')}"/>
<s:hidden id="error24" value="%{getText('er.label.spouse.name')}"/>
<s:hidden id="error25" value="%{getText('er.label.child.bday')}"/>

<s:hidden id="clear" value="%{getText('clear.label')}"/>
</div>