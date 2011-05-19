<%--@author Tharanga Punchihewa--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
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
    $('#errors-info-check').click(function() {
        document.getElementById("errors-info-check").disabled = true;
        var fieldIds = new Array('childBirthDatePicker', 'childBirthDistrictId', 'childDsDivisionId', 'childBirthDivisionId',
                'placeOfBirth', 'placeOfBirthEnglish', 'childGender');
        enableFields(fieldIds);

    });
    $('#mother-info-check').click(function() {
        document.getElementById("mother-info-check").disabled = true;
        var fieldIds = new Array('mother_pinOrNic', 'motherCountryId', 'motherPassportNoId', 'motherFullNameId', 'motherDateOfBirth',
                'motherAgeAtBirth', 'motherRaceId', 'motherPlaceOfBirthId', 'motherAddressId');
        enableFields(fieldIds);
    });
    $('#informant-info-check').click(function() {
        document.getElementById("informant-info-check").disabled = true;
        var fieldIds = new Array('informent_pinOrNic', 'informentName', 'informentAddress', 'informantTypeFATHER', 'informantTypeMOTHER', 'informantTypeGUARDIAN');
        enableFields(fieldIds);
    });
    $('#father-info-check').click(function() {
        document.getElementById("father-info-check").disabled = true;
        var fieldIds = new Array('father_pinOrNic', 'fatherCountryId', 'fatherPassportNoId', 'fatherName', 'fatherDadeOfbirth',
                'fatherRaceId', 'fatherPlaceOfBirth');
        enableFields(fieldIds);
    });
    $('#marriage-info-check').click(function() {
        document.getElementById("marriage-info-check").disabled = true;
        var fieldIds = new Array('placeOfMarriageId', 'dateOfMarriage', 'parentsMarriedMARRIED',
                'parentsMarriedUNMARRIED', 'parentsMarriedNO_SINCE_MARRIED', 'parentsMarriedUNKNOWN');
        enableFields(fieldIds);
    });
    $('#mother-after-marriage-info-check').click(function() {
        document.getElementById("mother-after-marriage-info-check").disabled = true;
        document.getElementById('mothersNameAfterMarriageId').disabled = false;
    });
    $('#grandFather-info-check').click(function() {
        document.getElementById("grandFather-info-check").disabled = true;
        var fieldIds = new Array('grandFatherFullName', 'grandFather_pinOrNic', 'grandFatherBirthYear', 'grandFatherBirthPlaceId',
                'greatGrandFatherFullNameId', 'grandGrandFather_pinOrNic', 'grandGrandFatherBirthYear', 'greatGrandFatherBirthPlaceId')
        enableFields(fieldIds);
    });
});
function minimize(id) {
    document.getElementById(id).style.display = 'none';
    document.getElementById(id + "-min").style.display = 'none';
    document.getElementById(id + "-max").style.display = 'block';
    document.getElementById(id + "-check").style.display = 'none';
    document.getElementById(id + "-check-lable").style.display = 'none';

}

function maximize(id) {
    document.getElementById(id).style.display = 'block';
    document.getElementById(id + "-max").style.display = 'none';
    document.getElementById(id + "-min").style.display = 'block';
    if (! document.getElementById(id + "-check").checked) {
        document.getElementById(id + "-check").style.display = 'block';
        document.getElementById(id + "-check-lable").style.display = 'block';
    }
}
function enableFields(fieldIds) {
    for (var i = 0; i < fieldIds.length; i++) {
        document.getElementById(fieldIds[i]).disabled = false;
    }
}

function initPage() {
    var sectionOfAct = document.getElementById("sectionOfAct").value;
    var idNames;
    var checkIdNames;
    var fieldIds;
    if (sectionOfAct == 1) {
        idNames = new Array('error-explanation-info');
    }
    if (sectionOfAct == 2) {
        idNames = new Array('errors-info', 'mother-info', 'informant-info');
        checkIdNames = new Array('errors-info-check', 'mother-info-check', 'informant-info-check');
        fieldIds = new Array('childBirthDatePicker', 'childBirthDistrictId', 'childDsDivisionId', 'childBirthDivisionId',
                'placeOfBirth', 'placeOfBirthEnglish', 'childGender', 'mother_pinOrNic', 'motherCountryId', 'motherPassportNoId', 'motherFullNameId',
                'motherDateOfBirth', 'motherAgeAtBirth', 'motherRaceId', 'motherPlaceOfBirthId', 'motherAddressId', 'informent_pinOrNic',
                'informentName', 'informentAddress', 'informantTypeFATHER', 'informantTypeMOTHER', 'informantTypeGUARDIAN');

    }
    if (sectionOfAct == 3) {
        idNames = new Array('father-info', 'marriage-info', 'mother-after-marriage-info',
                'grandFather-info');
        checkIdNames = new Array('father-info-check', 'marriage-info-check', 'mother-after-marriage-info-check', 'grandFather-info-check');
        fieldIds = new Array('father_pinOrNic', 'fatherCountryId', 'fatherPassportNoId', 'fatherName', 'fatherDadeOfbirth',
                'fatherRaceId', 'fatherPlaceOfBirth', 'placeOfMarriageId', 'dateOfMarriage', 'grandFatherFullName', 'grandFather_pinOrNic',
                'grandFatherBirthYear', 'grandFatherBirthPlaceId', 'greatGrandFatherFullNameId', 'grandGrandFather_pinOrNic',
                'grandGrandFatherBirthYear', 'greatGrandFatherBirthPlaceId', 'mothersNameAfterMarriageId', 'parentsMarriedMARRIED',
                'parentsMarriedUNMARRIED', 'parentsMarriedNO_SINCE_MARRIED', 'parentsMarriedUNKNOWN')

    }
    document.getElementById("header-info-max").style.display = 'none';
    for (var i = 0; i < idNames.length; i++) {
        document.getElementById(idNames[i]).style.display = 'none';
        document.getElementById(idNames[i] + "-min").style.display = 'none';
    }
    if (!(sectionOfAct == 1)) {
        for (var i = 0; i < checkIdNames.length; i++) {
            document.getElementById(checkIdNames[i]).style.display = 'none';
            document.getElementById(checkIdNames[i] + "-lable").style.display = 'none';
        }
        for (var i = 0; i < fieldIds.length; i++) {
            document.getElementById(fieldIds[i]).disabled = true;
        }
    }
    if (sectionOfAct == 2) {
        checkIdNames = new Array('errors-info-check', 'mother-info-check', 'informant-info-check');
    }
    if (sectionOfAct == 3) {
        checkIdNames = new Array('father-info-check', 'marriage-info-check', 'mother-after-marriage-info-check',
                'mother-after-marriage-info-check', 'grandFather-info-check');
    }
    if (sectionOfAct != 1) {
        for (var i = 0; i < checkIdNames.length; i++) {
            checkBoxCheck(document.getElementById(checkIdNames[i]));
        }
    }
}
function checkBoxCheck(id) {
    if (id.checked) {
        id.click();
        id.checked = true;
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
    domObject = document.getElementById('acceptanceDate');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, "", 'comError4')
    else
        isDate(domObject.value, "comError1", "comError4");
    domObject = document.getElementById('placeOfBirth');
    /*validation of the Act 27*/
    /* if (act == 1) {
     */
    /*validation for child's information */
    /*
     domObject = document.getElementById('nameInOfficialLanguages');
     if (isFieldEmpty(domObject)) {
     isEmpty(domObject, "", 'childError1');
     }
     domObject = document.getElementById('nameInEnglish');
     if (isFieldEmpty(domObject)) {
     isEmpty(domObject, "", 'childError2');
     }
     }
     */
    /*validation of the Act 52*/
    /*
     if (act == 2) {
     domObject = document.getElementById('childBirthDatePicker');
     if (isFieldEmpty(domObject))
     isEmpty(domObject, "", 'childError3')
     else
     isDate(domObject.value, "comError1", "childError4");
     domObject = document.getElementById('placeOfBirth');
     isEmpty(domObject, "", 'childError5');

     */
    /*validation of mother's information*/
    /*
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

     */
    /*validation for informent's information */
    /*
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
     */
    /*validation of the Act 27A*/
    /*
     if (act == 3) {
     */
    /* validation of father's information */
    /*
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

     */
    /*validation of Grand Father's validation*/
    /*
     domObject = document.getElementById('grandFather_pinOrNic');
     if (!isFieldEmpty(domObject)) {
     validatePINorNIC(domObject, "comError1", "grandFatherError1");
     }
     domObject = document.getElementById('grandFatherBirthYear');
     if (!isFieldEmpty(domObject))
     validateBirthYear(domObject, 'comError1', 'grandFatherError2');


     */
    /*validation of Grand Grand Father's validation*/
    /*
     domObject = document.getElementById('grandGrandFather_pinOrNic');
     if (!isFieldEmpty(domObject)) {
     validatePINorNIC(domObject, "comError1", "grandGrandFatherError1");
     }
     domObject = document.getElementById('grandGrandFatherBirthYear');
     if (!isFieldEmpty(domObject))
     validateBirthYear(domObject, 'searchCertificateError1', 'grandGrandFatherError2');
     }*/
    var mother = document.getElementById('declarantTypeMOTHER').checked;
    var father = document.getElementById('declarantTypeFATHER').checked;
    var other = document.getElementById('declarantTypeOTHER').checked;
    var self = document.getElementById('declarantTypeRELATIVE').checked;
    if (!(mother || father || other || self)) {
        errormsg = errormsg + "\n" + document.getElementById("declarantError1").value;
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

<script>
    $(document).ready(function() {
        $("#tabs").tabs();
    });
</script>

<script type="text/javascript">
    var errormsg = "";
    function validate2() {
        var text = 0;
        var domObject;
        var returnval = true;
        domObject = document.getElementById("bdfSerialNoIdSearch");
        if (!isFieldEmpty(domObject)) {
            isNumeric(domObject.value, 'searchCertificateError1', 'comError1');
            text ++;
        }
        domObject = document.getElementById("idNumberSearch");
        if (!isFieldEmpty(domObject)) {
            isNumeric(domObject.value, 'searchCertificateError2', 'comError1');
            text ++;
        }
        domObject = document.getElementById("bdfSearchSerialNoId");
        if (!isFieldEmpty(domObject)) {
            validateSerialNo(domObject, 'searchCertificateError2', 'comError1');
            text++;
        }
        if (text != 1) {
            alert(document.getElementById("comError5").value);
            errormsg = "";
            return false;
        }
        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

    function setDeclarantPerson(id) {
        var NICorPIN;
        var name;
        var address;
        if (id == 1) {
            NICorPIN = document.getElementById("motherNICorPIN").value;
            name = document.getElementById("motherName").value;
            address = document.getElementById("motherAddress").value;
        }
        if (id == 2) {
            NICorPIN = document.getElementById("fatherNICorPIN").value;
            name = document.getElementById("fatherName").value;
            address = "";
        }
        if (id == 3) {
            NICorPIN = "";
            name = "";
            address = "";
        }
        if (id == 4) {
            name = document.getElementById("childName").value;
            NICorPIN = "";
            address = "";
        }
        document.getElementById("Declarant_pinOrNic").value = NICorPIN;
        document.getElementById("declarantName").value = name;
        document.getElementById("declarantAddress").value = address;
    }
    function setDetailsOfInformation(id) {
        var NICorPIN;
        var name;
        var address;
        if (id == 1) {
            NICorPIN = document.getElementById("motherNICorPIN").value;
            name = document.getElementById("motherName").value;
            address = document.getElementById("motherAddress").value;
        }
        if (id == 2) {
            NICorPIN = document.getElementById("fatherNICorPIN").value;
            name = document.getElementById("fatherName").value;
            address = "";
        }
        if (id == 3) {
            NICorPIN = "";
            name = "";
            address = "";
        }
        document.getElementById("informent_pinOrNic").value = NICorPIN;
        document.getElementById("informentName").value = name;
        document.getElementById("informentAddress").value = address;
    }
    function initSerialNumber() {
        var domobject = document.getElementById('bdfSerialNo');
        if (isFieldEmpty(domobject)) {
            domobject.value = new Date().getFullYear() + "0";
        }
    }
</script>

<div id="birth-alteration-outer">
<s:if test="%{!editMode}">
    <s:url value="eprBirthAlteration.do" id="alteration"/>
</s:if>
<s:else>
    <s:url value="eprEditBirthAlteration.do" id="alteration"/>
</s:else>
<s:form action="%{alteration}" onsubmit="javascript:return validate()">
<table class="birth-alteration-table-style01" style="width:1030px;">
    <tr>
        <td width="30%"></td>
        <td width="35%" style="text-align:center;"><img src="<s:url value="/images/official-logo.png"/>"
                                                        alt=""/></td>
        <td width="35%">
            <s:fielderror name="duplicateSerialNumberError" cssStyle="color:red;font-size:10pt"/>
            <table class="birth-alteration-table-style02" cellspacing="0" style="float:right;width:100%">
                <tr>
                    <td colspan="2" style="text-align:center;">කාර්යාල ප්‍රයෝජනය සඳහා පමණි / <br>
                        அலுவலக பாவனைக்காக மட்டும் / <br>
                        For office use only
                    </td>
                </tr>
                <tr>
                    <td><s:label value="භාරගත් දිනය"/><br>
                        <s:label value="பெறப்பட்ட திகதி "/> <br>
                        <s:label value="Date of Acceptance"/>
                    </td>
                    <td><s:textfield id="acceptanceDate" name="birthAlteration.dateReceived"/></td>
                <tr>
                    <td><s:label value="පනතේ වගන්තිය "/><br>
                        <s:label value="பிறப்பைப்"/> <br>
                        <s:label value="Section of the Act"/>
                    </td>
                    <td align="center">
                        <s:if test="alterationType.ordinal()!=1 && alterationType.ordinal()!=0">
                            <s:select
                                    list="#@java.util.HashMap@{'TYPE_52_1_A':'52(1)A','TYPE_52_1_B':'52(1)B','TYPE_52_1_D':'52(1)D',
                                    'TYPE_52_1_E':'52(1)E','TYPE_52_1_H':'52(1)H','TYPE_52_1_I':'52(1)I'}"
                                    name="alterationType" cssStyle="width:190px; margin-left:5px"
                                    />
                        </s:if>
                        <s:elseif test="alterationType.ordinal()==0">
                            <s:hidden value="%{alterationType}" name="alterationType"/>
                            <s:label value="27"/>
                        </s:elseif>
                        <s:elseif test="alterationType.ordinal()==1">
                            <s:hidden value="%{alterationType}" name="alterationType"/>
                            <s:label value="27A"/>
                        </s:elseif>
                    </td>

                </tr>
            </table>
        </td>
    </tr>
</table>
<div class="birth-alteration-minimize-icon" id="header-info-min">[-]</div>
<div class="birth-alteration-maximize-icon" id="header-info-max">[+]</div>
<div id="header-info">
    <table class="birth-alteration-table-style01" style="width:1030px;">
        <tr>
            <td colspan="3" style="font-size:12pt;text-align:center;">
                <s:if test="alterationType.ordinal()==0">
                    <s:hidden id="sectionOfAct" value="1"/>
                    <s:label value="නම ඇතුලත් කිරීම හෝ වෙනස් කිරීම (27 වගන්තිය)"/> <br>
                    <s:label value="தந்தை பற்றிய தகவல்"/>் <br>
                    <s:label value="Insertion or Alteration of the Name (Section 27)"/>
                </s:if>
                <s:if test="alterationType.ordinal()!=1 && alterationType.ordinal()!=0 ">
                    <s:hidden id="sectionOfAct" value="2"/>
                    <s:label value="උප්පැන සහතිකයක දෝෂ නිවැරදි කිරීම (52 (1) වගන්තිය)"/> <br>
                    <s:label value="தந்தை பற்றிய தகவல்"/> <br>
                    <s:label value="Correction of Errors of a Birth Certificate (Section  52 (1))"/>
                </s:if>
                <s:if test="alterationType.ordinal()==1">
                    <s:hidden id="sectionOfAct" value="3"/>
                    <s:label value="උප්පැන්න සහතිකයක තොරතුරු සංශෝදනය කිරීම (27 A වගන්තිය)"/> <br>
                    <s:label value="தந்தை பற்றிய தகவல்"/> <br>
                    <s:label value="Amendment of Birth Registration Entry (Section 27 A)"/>
                </s:if>
            </td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;">
                <s:label value="වෙනස් කලයුතු උප්පැන්න සහතිකය පිලිබඳ විස්තර"/> <br>
                <s:label value="பிள்ளை பற்றிய தகவல்"/> <br>
                <s:label value="Particulars of the Birth Certificate to amend"/>
            </td>
        </tr>
    </table>
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;" cellpadding="0"
           cellspacing="0">
        <caption></caption>
        <col style="width:20%"/>
        <col style="width:20%"/>
        <col style="width:20%"/>
        <col style="width:20%"/>
        <col style="width:20%"/>
        <tbody>
        <tr>
            <td colspan="2">සහතිකයේ සඳහන් පුද්ගලයාගේ අනන්‍යතා අංකය <br>
                தனிநபர்அடையாள எண் <br>
                Person Identification Number (PIN) stated in the Certificate
            </td>
            <td><s:label value="%{#request.nicOrPin}"/></td>
            <td>සහතික පත්‍රයේ අංකය <br>
                சான்றிதழ் இல <br>
                Certificate Number
            </td>
            <td><s:label name="bdId"/></td>
        </tr>
        <tr>
            <td>දිස්ත්‍රික්කය <br>
                மாவட்டம் <br>
                District
            </td>
            <td><s:label value="%{#request.districtName}"/></td>
            <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
                பிரதேச செயலாளர் பிரிவு<br>
                Divisional Secretariat
            </td>
            <td colspan="2"><s:label value="%{#request.dsDivisionName}"/></td>
        </tr>
        <tr>
            <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
                பதிவுப் பிரிவு<br>
                Registration Division
            </td>
            <td><s:label value="%{#request.bdDivisionName}"/></td>
            <td>ලියාපදිංචි කිරීමේ අංකය <br>
                பதிவு இலக்கம்<br>
                Registration Number
            </td>
            <td colspan="2"><s:label value="%{#request.serialNo}"/></td>
        </tr>
        </tbody>
    </table>
    <s:hidden name="nicOrPin"/>
    <s:hidden name="districtName"/>
    <s:hidden name="dsDivisionName"/>
    <s:hidden name="bdDivisionName"/>
    <s:hidden name="serialNo"/>
    <s:hidden name="bdId"/>
</div>
<s:if test="alterationType.ordinal()==0">
    <s:hidden name=" birthDivisionId"/>
<div id="actNumber1" style="margin-top:10px">
    <table class="birth-alteration-table-style02" style="width:100%" cellpadding="0" cellspacing="0">
        <caption></caption>
        <col width="250px"/>
        <col width="760px"/>
        <tbody>
        <tr>
            <td>
                නම රාජ්‍ය භාෂාවෙන්
                (සිංහල / දෙමළ)
                <br>பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
                <br>Name in any of the official languages (Sinhala / Tamil)
            </td>
            <td><s:textarea name="birthAlteration.alt27.childFullNameOfficialLang"
                            id="nameInOfficialLanguages"/></td>
        </tr>
        <tr>
            <td>
                නම ඉංග්‍රීසි භාෂාවෙන්
                <br>பெயர் ஆங்கில மொழியில்
                <br>Name in English
            </td>
            <td><s:textarea name="birthAlteration.alt27.childFullNameEnglish" id="nameInEnglish"/></td>
        </tr>
        </tbody>
    </table>
</div>
</s:if>
<s:if test="alterationType.ordinal()!=1 && alterationType.ordinal()!=0 ">
<div id="actNumber2">
<s:hidden name="birthAlteration.alt27.childFullNameOfficialLang"/>
<s:hidden name="birthAlteration.alt27.childFullNameEnglish"/>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td style="width:25%;border-right:none;"></td>
        <td style="font-size:11pt;text-align:center;width:50%;border-right:none" colspan="6">
            ළම‌යාගේ විස්තර <br>
            பிள்ளை பற்றிய தகவல் <br>
            Child's Information
        </td>
        <td style="width:20%;text-align:right;border-right:none">
            <div id="errors-info-check-lable">
                <s:label value="%{getText('edit.lable')}"/></div>
        </td>
        <td style="border-right:none;width:3%">
            <s:checkbox id="errors-info-check" name="editChildInfo" cssStyle="float:right;"/>
        </td>
        <td style="width:2%">
            <div class="birth-alteration-minimize-icon" id="errors-info-min">[-]</div>
            <div class="birth-alteration-maximize-icon" id="errors-info-max">[+]</div>
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
            <td><s:label value="උපන් දිනය"/> <br>
                <s:label value="பிறந்த திகத"/><br>
                <s:label value="Date of Birth"/>
            </td>
            <td colspan="5"><s:textfield cssStyle="width:150px;" id="childBirthDatePicker"
                                         name="birthAlteration.alt52_1.dateOfBirth"/></td>
        </tr>
        <tr>
            <td rowspan="5"><s:label value=" උපන් ස්ථානය"/><br>
                <s:label value="பிறந்த இடம்"/><br>
                <s:label value="Place of Birth"/>
                <s:textfield name="birthDivisionId" cssStyle="visibility:hidden;margin:0px;" id="tempBirthDivisionId"/>
            </td>
            <td colspan="2"><s:label value=" දිස්ත්‍රික්කය /"/> <br>
                <s:label value="மாவட்டம் /"/><br>
                <s:label value=" District"/>
            </td>
            <td colspan="4">
                <s:select id="childBirthDistrictId" name="birthDistrictId" list="allDistrictList"
                          value="%{birthDistrictId}"
                          cssStyle="width:95%"/></td>
        </tr>
        <tr>
            <td colspan="2"><s:label value="ප්‍රාදේශීය ලේකම් කොට්ඨාශය /"/> <br>
                <s:label value="பிரதேச செயலாளர் பிரிவு/"/> <br>
                <s:label value="Divisional Secretariat"/>
            </td>
            <td colspan="4"><s:select id="childDsDivisionId" name="dsDivisionId" list="allDsDivisionList"
                                      value="%{dsDivisionId}"
                                      cssStyle="float:left;  width:95%;"/></td>
        </tr>
        <tr>
            <td colspan="2"><s:label value=" ලියාපදිංචි කිරීමේ කොට්ඨාශය /"/> <br>
                <s:label value="பதிவுப் பிரிவு/"/> <br>
                <s:label value=" Registration Division"/>
            </td>
            <td colspan="4"><s:select id="childBirthDivisionId" name="divisionAltaration"
                                      list="allBdDivisionList"
                                      cssStyle="float:left;  width:95%; "/>
            </td>
        </tr>
        <tr>
            <td rowspan="2"><s:label value=" ස්ථානය "/><br>
                <s:label value="பிறந்த "/> <br>
                <s:label value=" Place"/>
            </td>
            <td><s:label value=" සිංහල හෝ දෙමළ භාෂාවෙන්"/><br>
                <s:label value="சிங்களம் தமிழ்"/> <br>
                <s:label value="In Sinhala or Tamil"/>
            </td>
            <td colspan="4"><s:textfield cssStyle="width:95%" id="placeOfBirth"
                                         name="birthAlteration.alt52_1.placeOfBirth"/></td>
        </tr>
        <tr>
            <td><s:label value="ඉංග්‍රීසි භාෂාවෙන්"/><br>
                <s:label value="இங்கிலீஷ் "/><br>
                <s:label value="In English"/>
            </td>
            <td colspan="4"><s:textfield cssStyle="width:95%" name="birthAlteration.alt52_1.placeOfBirthEnglish"
                                         id="placeOfBirthEnglish"/></td>
        </tr>
        <tr>
            <td>
                <s:label value=" ස්ත්‍රී පුරුෂ භාවය"/><br>
                <s:label value="பால் "/> <br>
                <s:label value="Gender of the child"/>
            </td>
            <td colspan="6"><s:select
                    list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                    name="birthAlteration.alt52_1.childGender" cssStyle="width:50%; margin-left:5px;" id="childGender"
                    value="%{birthAlteration.alt52_1.childGender}"/>
        </tr>
        </tbody>
    </table>
</div>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td style="border-right:none;width:25%"></td>
        <td colspan="9" style="text-align:center;font-size:11pt ;width:50%;border-right:none"> මවගේ විස්තර <br>தாய்
            பற்றிய தகவல் <br>Details of the
            Mother
        </td>
        <td style="width:20%;text-align:right;border-right:none">
            <div id="mother-info-check-lable">
                <s:label value="%{getText('edit.lable')}"/></div>
        </td>
        <td style="border-right:none;width:3%">
            <s:checkbox id="mother-info-check" name="editMotherInfo" cssStyle="float:right;"/>
        </td>
        <td style="width:2%">
            <div class="birth-alteration-minimize-icon" id="mother-info-min">[-]</div>
            <div class="birth-alteration-maximize-icon" id="mother-info-max">[+]</div>
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
            <td rowspan="2"><s:label value="අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය"/> <br>
                <s:label value="து தனிநபர் அடையாள எண/தேசிய அடையாள அட்டை     இலக்கம்"/><br>
                <s:label value="PIN / NIC Number"/>
            </td>
            <td colspan="2" rowspan="2" class="find-person">
                <s:textfield id="mother_pinOrNic" name="birthAlteration.alt52_1.mother.motherNICorPIN"
                             cssStyle="width:80%;"/>
            </td>
            <td colspan="2" rowspan="2"><s:label value="විදේශිකය‍කු නම්"/><br><s:label value="வெளிநாட்டவர் எனின் "/><br><s:label
                    value="If foreigner"/>
            </td>
            <td colspan="2"><s:label value="රට"/><br><s:label value="நாடு "/><br><s:label value="Country"/></td>
            <td colspan="2"><s:select name="motherCountryId" list="countryList" headerKey="0" id="motherCountryId"
                                      headerValue="%{getText('select_country.label')}" cssStyle="width:80%;"/></td>
        </tr>
        <tr>
            <td colspan="2"><s:label value="ගමන් බලපත්‍ර අංකය "/><br><s:label value="கடவுச் சீட்டு "/><br><s:label
                    value="Passport No."/></label></td>
            <td colspan="2" class="passport"><s:textfield name="birthAlteration.alt52_1.mother.motherPassportNo"
                                                          id="motherPassportNoId"/></td>
        </tr>
        <tr>
            <td><s:label value="සම්පුර්ණ නම "/><br>
                <s:label value="முழுப் பெயர் "/><br>
                <s:label value="Full Name    "/>
            </td>
            <td colspan="8"><s:textarea name="birthAlteration.alt52_1.mother.motherFullName"
                                        id="motherFullNameId"/></td>
        </tr>
        <tr>
            <td><s:label value="උපන් දිනය"/><br>
                <s:label value="பிறந்த திகதி"/><br>
                <s:label value="Date of Birth"/>
            </td>
            <td><s:textfield cssStyle="width:125px;" id="motherDateOfBirth"
                             name="birthAlteration.alt52_1.mother.motherDOB"/></td>
            <td><s:label value="ළමයාගේ උපන් දිනට වයස"/> <br>
                <s:label value="பிள்ளை பிறந்த திகதியில் மாதாவின் வயது"/><br>
                <s:label value="Age as at the date of birth of child "/>
            </td>
            <td colspan="2"><s:textfield cssStyle="margin-right:10px;width:80%" id="motherAgeAtBirth"
                                         name="birthAlteration.alt52_1.mother.motherAgeAtBirth"/></td>
            <td colspan="2"><s:label value="ජාතිය"/><br>
                <s:label value="இனம்"/><br>
                <s:label value="Race     "/>
            </td>
            <td colspan="2"><s:select list="raceList" name="motherRaceId" headerKey="0" id="motherRaceId"
                                      headerValue="%{getText('select_race.label')}"
                                      cssStyle="width:80%;"/></td>
        </tr>
        <tr>
            <td><s:label value="උපන් ස්ථානය"/><br>
                <s:label value="பிறந்த இடம் "/><br>
                <s:label value="Place of Birth "/>
            </td>
            <td colspan="8"><s:textarea name="birthAlteration.alt52_1.mother.motherPlaceOfBirth"
                                        id="motherPlaceOfBirthId"/></td>
        </tr>
        <tr>
            <td>ස්ථිර ලිපිනය<br>
                தாயின் நிரந்தர வதிவிட முகவரி<br>
                Permanent Address
            </td>
            <td colspan="8"><s:textarea name="birthAlteration.alt52_1.mother.motherAddress" id="motherAddressId"/></td>
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
        <td style="width:25%;border-right:none;"></td>
        <td colspan="4" style="text-align:center;font-size:11pt; width:50%;border-right:none;">දැනුම් දෙන්නාගේ
            විස්තර<br>
            அறிவிப்பு கொடுப்பவரின் தகவல்கள்<br>
            Details of the Informant
        </td>
        <td style="width:20%;text-align:right;border-right:none">
            <div id="informant-info-check-lable">
                <s:label value="%{getText('edit.lable')}"/></div>
        </td>
        <td style="border-right:none;width:3%">
            <s:checkbox id="informant-info-check" name="editInformantInfo" cssStyle="float:right;"/>
        </td>
        <td style="width:2%">
            <div class="birth-alteration-minimize-icon" id="informant-info-min">[-]</div>
            <div class="birth-alteration-maximize-icon" id="informant-info-max">[+]</div>
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
                            <s:radio id="informantType" name="birthAlteration.alt52_1.informant.informantType"
                                     list="#@java.util.HashMap@{'MOTHER':''}"
                                     onchange="javascript:setDetailsOfInformation(1)"/></td>
                </table>
            </td>
            <td>
                <table style="border:none;width:100%">
                    <tr>
                        <td style="width:75%;border:none"> පියා<br> பிதா<br> Father
                        </td>
                        <td style="width:25%;border:none;">
                            <s:radio id="informantType" name="birthAlteration.alt52_1.informant.informantType"
                                     list="#@java.util.HashMap@{'FATHER':''}"
                                     onchange="javascript:setDetailsOfInformation(2)"/></td>
                </table>

            </td>
            <td>
                <table style="border:none;width:100%">
                    <tr>
                        <td style="width:75%;border:none"> භාරකරු<br> பாதுகாவலர் <br> Guardian
                        </td>
                        <td style="width:25%;border:none;">
                            <s:radio id="informantType" name="birthAlteration.alt52_1.informant.informantType"
                                     list="#@java.util.HashMap@{'GUARDIAN':''}"
                                     onchange="javascript:setDetailsOfInformation(3)"/></td>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2">දැනුම් දෙන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>
                தகவல் கொடுப்பவரின் தனிநபர் அடையாள எண் / அடையாள அட்டை இல. <br>
                PIN / NIC of the Informant
            </td>
            <td colspan="2"><s:textfield id="informent_pinOrNic"
                                         name="birthAlteration.alt52_1.informant.informantNICorPIN"/></td>
        </tr>
        <tr>
            <td>නම<br>
                கொடுப்பவரின் பெயர்<br>
                Name
            </td>
            <td colspan="3"><s:textarea id="informentName" name="birthAlteration.alt52_1.informant.informantName"/></td>
        </tr>
        <tr>
            <td>තැපැල් ලිපිනය<br>
                தபால் முகவரி<br>
                Postal Address
            </td>
            <td colspan="3"><s:textarea id="informentAddress"
                                        name="birthAlteration.alt52_1.informant.informantAddress"/></td>
        </tr>
        </tbody>
    </table>
</div>
</div>
</s:if>
<s:if test="alterationType.ordinal()==1">
<div id="actNumber3">
<s:textarea name="birthAlteration.alt27.childFullNameOfficialLang" cssStyle="visibility:hidden;"/>
<s:textarea name="birthAlteration.alt27.childFullNameEnglish" cssStyle="visibility:hidden;"/>
<s:hidden name=" birthDivisionId"/>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%;" cellpadding="0" cellspacing="0">
    <tr>
        <td style="width:25%;border-right:none;"></td>
        <td colspan="5" style="width:50%;text-align:center;font-size:12pt;border-right:none;">
            පියාගේ විස්තර<br>
            தந்தை பற்றிய தகவல்<br>
            Details of the Father
        </td>
        <td style="width:20%;text-align:right;border-right:none">
            <div id="father-info-check-lable">
                <s:label value="%{getText('edit.lable')}"/></div>
        </td>
        <td style="width:3%;border-right:none;">
            <s:checkbox id="father-info-check" name="editFatherInfo" cssStyle="float:right;"/>
        </td>
        <td style="width:2%">
            <div class="birth-alteration-minimize-icon" id="father-info-min">[-]</div>
            <div class="birth-alteration-maximize-icon" id="father-info-max">[+]</div>
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
            <td rowspan="2">
                අනන්‍යතා අංකය
                <br>அடையாள எண்
                <br>Identification Number
            </td>
            <td rowspan="2"><s:textfield name="birthAlteration.alt27A.father.fatherNICorPIN" id="father_pinOrNic"/></td>
            <td rowspan="2">විදේශිකය‍කු නම්<br>
                வெளிநாட்டவர் எனின் <br>
                If a foreigner
            </td>
            <td>රට<br>
                நாடு<br>
                Country
            </td>
            <td><s:select name="fatherCountryId" list="countryList" headerKey="0" id="fatherCountryId"
                          headerValue="%{getText('select_country.label')}" cssStyle="width:80%;"
                          value="%{fatherCountryId}"/></td>
        </tr>
        <tr>
            <td>ගමන් බලපත්‍ර අංකය<br>
                கடவுச் சீட்டு<br>
                Passport No.
            </td>
            <td><s:textfield name="birthAlteration.alt27A.father.fatherPassportNo" id="fatherPassportNoId"/></td>
        </tr>
        <tr>
            <td>සම්පුර්ණ නම<br>
                தந்தையின் முழு பெயர்<br>
                Full Name
            </td>
            <td colspan="4"><s:textarea name="birthAlteration.alt27A.father.fatherFullName" id="fatherName"/></td>
        </tr>
        <tr>
            <td>උපන් දිනය<br>
                பிறந்த திகதி <br>
                Date of Birth
            </td>
            <td><s:textfield id="fatherDadeOfbirth" name="birthAlteration.alt27A.father.fatherDOB"/></td>
            <td colspan="2">ජාතිය<br>
                இனம்<br>
                Race
            </td>
            <td><s:select list="raceList" name="fatherRaceId" headerKey="0" id="fatherRaceId"
                          headerValue="%{getText('select_race.label')}"
                          cssStyle="width:80%;" value="%{fatherRaceId}"/></td>
        </tr>
        <tr>
            <td>උපන් ස්ථානය<br>
                பிறந்த இடம்<br>
                Place of Birth
            </td>
            <td colspan="4"><s:textfield name="birthAlteration.alt27A.father.fatherPlaceOfBirth"
                                         id="fatherPlaceOfBirth"/></td>
        </tr>
        </tbody>
    </table>
</div>

<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td style="width:25%;border-right:none"></td>
        <td colspan="4" style="width:50%;text-align:center;font-size:11pt;border-right:none;">විවාහයේ විස්තර වෙනස් කිරීම<br>
            திருமணத்தின் விபரங்கள் <br>
            Changing of Details of the Marriage
        </td>
        <td style="width:20%;text-align:right;border-right:none">
            <div id="marriage-info-check-lable">
                <s:label value="%{getText('edit.lable')}"/></div>
        </td>
        <td style="border-right:none;width:2%">
            <s:checkbox id="marriage-info-check" name="editMarriageInfo" cssStyle="float:right;"/>
        </td>
        <td style="width:2%;">
            <div class="birth-alteration-minimize-icon" id="marriage-info-min">[-]</div>
            <div class="birth-alteration-maximize-icon" id="marriage-info-max">[+]</div>
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
                        <td style="border:none"><label>
                            ඔව්
                            <br>ஆம்
                            <br>Yes
                        </label></td>
                        <td style="border:none"><s:radio name="birthAlteration.alt27A.marriage.parentsMarried"
                                                         id="parentsMarried"
                                                         list="#@java.util.HashMap@{'MARRIED':''}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="border:none"><label>
                            නැත
                            <br>இல்லை
                            <br>No
                        </label></td>
                        <td style="border:none"><s:radio name="birthAlteration.alt27A.marriage.parentsMarried"
                                                         id="parentsMarried"
                                                         list="#@java.util.HashMap@{'UNMARRIED':''}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="border:none"><label>
                            නැත, නමුත් පසුව විවාහවී ඇත
                            <br>இல்லை, பின் விவாகமானவர்கள்
                            <br>No, but since married
                        </label></td>
                        <td style="border:none"><s:radio name="birthAlteration.alt27A.marriage.parentsMarried"
                                                         id="parentsMarried"
                                                         list="#@java.util.HashMap@{'NO_SINCE_MARRIED':''}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="border:none"><label>නොදනී<br>தெரியாது<br>Unknown</label></td>
                        <td style="border:none"><s:radio name="birthAlteration.alt27A.marriage.parentsMarried"
                                                         id="parentsMarried"
                                                         list="#@java.util.HashMap@{'UNKNOWN':''}"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
            <td>විවාහ වු ස්ථානය<br>
                விவாகம் இடம்பெற்ற இடம்<br>
                Place of Marriage
            </td>
            <td><s:textfield name="birthAlteration.alt27A.marriage.placeOfMarriage" id="placeOfMarriageId"/></td>
        </tr>
        <tr>
            <td>
                විවාහ වු දිනය<br>
                விவாகம் இடம்பெற்ற திகதி<br>
                Date of Marriage
            </td>
            <td><s:textfield id="dateOfMarriage" name="birthAlteration.alt27A.marriage.dateOfMarriage"/></td>
        </tr>
    </table>
</div>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0"
       cellspacing="0">
    <tr>
        <td style="width:25%; border-right:none"></td>
        <td colspan="2" style="text-align:center;font-size:11pt;border-right:none;width:50%;">
            විවාහයෙන් පසු මවගේ නම වෙනස් කිරීම<br>
            தாத்தாவின பாட்டனின் விபரங்கள் <br>
            Change of Mothers name after marriage
        </td>
        <td style="width:20%;text-align:right;border-right:none">
            <div id="mother-after-marriage-info-check-lable">
                <s:label value="%{getText('edit.lable')}"/></div>
        </td>
        <td style="border-right:none;width:2%"><s:checkbox id="mother-after-marriage-info-check"
                                                           name="editMothersNameAfterMarriageInfo"
                                                           cssStyle="float:right;"/></td>
        <td style="width:2%">
            <div class="birth-alteration-minimize-icon" id="mother-after-marriage-info-min">[-]</div>
            <div class="birth-alteration-maximize-icon" id="mother-after-marriage-info-max">[+]</div>

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
            <td style="width:760px;"><s:textarea name="birthAlteration.alt27A.mothersNameAfterMarriage"
                                                 id="mothersNameAfterMarriageId"/></td>
        </tr>
    </table>
</div>


<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td style="width:25%; border-right:none"></td>
        <td colspan="8" style="width:50%;text-align:center;font-size:11pt;border-right:none;">මුත්තා / මී මුත්තා ගේ
            විස්තර වෙනස් කිරීම<br>
            தாத்தாவின் / பாட்டனின் விபரங்கள் <br>
            Changing of the Details of the Grand Father / Great Grand Father
        </td>
        <td style="width:20%;text-align:right;border-right:none">
            <div id="grandFather-info-check-lable">
                <s:label value="%{getText('edit.lable')}"/></div>
        </td>
        <td style="border-right:none;width:3%">
            <s:checkbox id="grandFather-info-check" name="editGrandFatherInfo"/>
        </td>
        <td style="width:2%">
            <div class="birth-alteration-minimize-icon" id="grandFather-info-min">[-]</div>
            <div class="birth-alteration-maximize-icon" id="grandFather-info-max">[+]</div>
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
            <td colspan="6"><s:textarea name="birthAlteration.alt27A.grandFather.grandFatherFullName"
                                        id="grandFatherFullName"/></td>
        </tr>
        <tr>
            <td colspan="2">
                අනන්‍යතා අංකය (තිබේ නම්)
                <br>அடையாள எண்(இருந்தால்)
                <br>Identification Number (if available)
            </td>
            <td><s:textfield id="grandFather_pinOrNic" name="birthAlteration.alt27A.grandFather.grandFatherNICorPIN"
                             maxLength="12"/></td>
            <td>ඔහුගේ උපන් වර්ෂය<br>
                அவர் பிறந்த வருடம்<br>
                His Year of Birth
            </td>
            <td><s:textfield id="grandFatherBirthYear" name="birthAlteration.alt27A.grandFather.grandFatherBirthYear"
                             maxLength="4"
                             onkeypress="return isNumberKey(event)"/></td>
            <td>උපන් ස්ථානය<br>
                அவர் பிறந்த இடம்<br>
                Place Of Birth
            </td>
            <td><s:textfield name="birthAlteration.alt27A.grandFather.grandFatherBirthPlace"
                             id="grandFatherBirthPlaceId"/></td>
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
            <td colspan="6"><s:textarea name="birthAlteration.alt27A.grandFather.greatGrandFatherFullName"
                                        id="greatGrandFatherFullNameId"/></td>
        </tr>
        <tr>
            <td colspan="2">
                අනන්‍යතා අංකය (තිබේ නම්)
                <br>அடையாள எண்(இருந்தால்)
                <br>Identification Number (if available)
            </td>
            <td><s:textfield id="grandGrandFather_pinOrNic"
                             name="birthAlteration.alt27A.grandFather.greatGrandFatherNICorPIN"
                             maxLength="12"/></td>
            <td>ඔහුගේ උපන් වර්ෂය<br>
                அவர் பிறந்த வருடம்<br>
                His Year of Birth
            </td>
            <td><s:textfield id="grandGrandFatherBirthYear"
                             name="birthAlteration.alt27A.grandFather.greatGrandFatherBirthYear"
                             maxLength="4"
                             onkeypress="return isNumberKey(event)"/></td>
            <td>උපන් ස්ථානය<br>
                அவர் பிறந்த இடம்<br>
                Place Of Birth
            </td>
            <td><s:textfield name="birthAlteration.alt27A.grandFather.greatGrandFatherBirthPlace"
                             id="greatGrandFatherBirthPlaceId"/></td>
        </tr>
        </tbody>
    </table>
</div>
</div>
</s:if>
<s:if test="alterationType.ordinal()!=0">
    <%--
    <s:if test="(sectionOfAct != 1)">
    --%>
<div id="actNumber4">
    <table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%;" cellpadding="0"
           cellspacing="0">
        <tr>
            <td colspan="8" style="text-align:center;font-size:12pt;width:90%;border-right:none">
                දෝෂය හා එය සිදුවූ අන්දම පිලිබඳ ලුහුඬු විස්තර
                <br>தவறு மற்றும் அவை நிகழ்ந்த விதம் பற்றிய விரிவான விபரங்கள்
                <br>Nature of the error and a brief explanation of how the error occurred
            </td>
            <td style="border-right:none"></td>
            <td></td>
        </tr>
    </table>
    <div id="error-explanation-info">
        <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;border-top:none;"
               cellpadding="0"
               cellspacing="0">
            <tr>
                <td>
                    <s:textarea name="birthAlteration.comments" cssStyle="height:100px;"/>
                </td>
            </tr>
        </table>
    </div>
</div>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%;" cellpadding="0"
       cellspacing="0">
    <tr>
        <td colspan="2" style="text-align:center;font-size:12pt">
            ප්‍රකාශය සනාත කිරීමට ඇති ලේඛනගත හෝ වෙනත් සාක්ෂිවල ස්වභාවය
            <br>பிரதிக்கினையினை உறுதிப்படுத்துவதற்கு போதுமான ஆவணங்கள் அல்லது வேறு சாட்சிகளின் தன்மைகள்
            <br>Nature of documentary or other evidence in support of the declaration
        </td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="birthAlteration.bcOfFather"/></td>
        <td style="width:90%"><s:label
                value=" පියාගේ උප්පැන්න සහතිකය  / தகப்பனின் பிறப்புச் சான்றிதழ் / Fathers Birth Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="birthAlteration.bcOfMother"/></td>
        <td style="width:90%"><s:label
                value="මවගේ උප්පැන්න සහතිකය / தாயின் பிறப்புச் சான்றிதழ் / Mothers Birth Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="birthAlteration.mcOfParents"/></td>
        <td style="width:90%"><s:label
                value=" මව්පියන්ගේ විවාහ සහතිකය / பொற்றோரின் திருமணச் சான்றிதழ் / Parents Marriage Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td colspan="2"><s:textarea name="birthAlteration.otherDocuments"/></td>
    </tr>
</table>
</s:if>
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
                        <s:radio id="declarantType" name="birthAlteration.declarant.declarantType"
                                 list="#@java.util.HashMap@{'MOTHER':''}"
                                 onchange="javascript:setDeclarantPerson(1)"/></td>
            </table>
        </td>
        <td>
            <table style="border:none;width:100%">
                <tr>
                    <td style="width:75%;border:none"> පියා<br> பிதா<br> Father
                    </td>
                    <td style="width:25%;border:none;">
                        <s:radio id="declarantType" name="birthAlteration.declarant.declarantType"
                                 list="#@java.util.HashMap@{'FATHER':''}"
                                 onchange="javascript:setDeclarantPerson(2)"/></td>
            </table>

        </td>
        <td>
            <table style="border:none;width:100%">
                <tr>
                    <td style="width:75%;border:none"> භාරකරු<br> பாதுகாவலர் <br> Guardian
                    </td>
                    <td style="width:25%;border:none;">
                        <s:radio id="declarantType" name="birthAlteration.declarant.declarantType"
                                 list="#@java.util.HashMap@{'OTHER':''}"
                                 onchange="javascript:setDeclarantPerson(3) "/></td>
            </table>
        </td>
        <td>
            <table style="border:none;width:100%">
                <tr>
                    <td style="width:75%;border:none"> තමුන්<br> மாதா<br> Self
                    </td>
                    <td style="width:25%;border:none;">
                        <s:radio id="declarantType" name="birthAlteration.declarant.declarantType"
                                 list="#@java.util.HashMap@{'RELATIVE':''}"
                                 onchange="javascript:setDeclarantPerson(4)"/></td>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification Number
        </td>
        <td colspan="2"><s:textfield id="Declarant_pinOrNic" name="birthAlteration.declarant.declarantNICorPIN"
                                     maxLength="12"/>
        </td>
    </tr>
    <tr>
        <td>නම<br>
            கொடுப்பவரின் பெயர்<br>
            Name<br></td>
        <td colspan="4"><s:textarea id="declarantName" name="birthAlteration.declarant.declarantFullName"/></td>
    </tr>
    <tr>
        <td>තැපැල් ලිපිනය<br>
            தபால் முகவரி<br>
            Postal Address
        </td>
        <td colspan="4"><s:textarea id="declarantAddress" name="birthAlteration.declarant.declarantAddress"/></td>
    </tr>
    </tbody>
</table>


    <s:hidden name="idUKey"/>
    <s:hidden name="sectionOfAct"/>
<div class="form-submit">
    <s:submit value="%{getText('save.label')}"/>
    </s:form>
</div>
<%--common errors--%>
<s:hidden id="comError1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="comError2" value="%{getText('p1.serial.text')}"/>
<s:hidden id="comError3" value="%{getText('p1.SerialNum.error.value')}"/>
<s:hidden id="comError4" value="%{getText('p1.submitDate.error.value')}"/>
<s:hidden id="comError5" value="%{getText('search.error.lable')}"/>
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


<s:hidden id="declarantError1" value="%{getText('declarantType.lable')}"/>


<%--Search Errors--%>
<s:hidden id="searchCertificateError1" value="%{getText('certificateNumber.lable')}"/>
<s:hidden id="searchCertificateError2" value="%{getText('idNumber.lable')}"/>
<s:hidden id="searchCertificateError2" value="%{getText('searchDeclarationSearial.label')}"/>

<s:hidden id="fatherName" value="%{#request.parent.fatherFullName}"/>
<s:hidden id="fatherNICorPIN" value="%{#request.parent.fatherNICorPIN}"/>
<s:hidden id="motherName" value="%{#request.parent.motherFullName}"/>
<s:hidden id="motherAddress" value="%{#request.parent.motherAddress}"/>
<s:hidden id="motherNICorPIN" value="%{#request.parent.motherNICorPIN}"/>
<s:hidden id="childName" value="%{#request.child.childFullNameOfficialLang}"/>
