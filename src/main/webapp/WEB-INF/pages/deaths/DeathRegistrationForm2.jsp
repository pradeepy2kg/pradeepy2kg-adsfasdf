<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<s:set value="rowNumber" name="row"/>
<script type="text/javascript">

$(function() {
    $("#submitDatePicker").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });

    $("#certifierDatePicker").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});

$(function() {
    $("#declarantDatePicker").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});
$(function() {
    $('img#declarant_lookup').bind('click', function(evt1) {
        var id1 = $("input#declarant_pinOrNic").attr("value");

        $("textarea#declarantFullName").val('');
        $("textarea#declarantAddress").val('');
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                function(data1) {
                    if (data1 != null) {
                        $("textarea#declarantFullName").val(data1.fullNameInOfficialLanguage);
                        $("textarea#declarantAddress").val(data1.lastAddress);
                    }
                });
    });

    $('img#first_witness_lookup').bind('click', function(evt2) {
        var id2 = $("input#first_witness_NICorPIN").attr("value");

        $("textarea#firstWitnessFullName").val('');
        $("textarea#firstWitnessAddress").val('');
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id2},
                function(data2) {
                    $("textarea#firstWitnessFullName").val(data2.fullNameInOfficialLanguage);
                    $("textarea#firstWitnessAddress").val(data2.lastAddress);
                });
    });

    $('img#second_witness_lookup').bind('click', function(evt3) {
        var id3 = $("input#second_witness_NICorPIN").attr("value");

        $("textarea#secondWitnessFullName").val('');
        $("textarea#secondWitnessAddress").val('');
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id3},
                function(data3) {
                    $("textarea#secondWitnessFullName").val(data3.fullNameInOfficialLanguage);
                    $("textarea#secondWitnessAddress").val(data3.lastAddress);
                });
    });

    $('img#notifying_authority_lookup').bind('click', function(evt4) {
        var id4 = $("input#notifying_authority_NICorPIN").attr("value");

        $("textarea#notifyingAuthorityName").val('');
        $("textarea#notifyingAuthorityAddress").val('');
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id4},
                function(data4) {
                    if (data4 != null) {
                        $("textarea#notifyingAuthorityName").val(data4.fullNameInOfficialLanguage);
                        $("textarea#notifyingAuthorityAddress").val(data4.lastAddress);
                    }
                });
    });

});

var informPerson;
var errormsg = "";
function validate() {
    var deathType = document.getElementById('deathTypeId').value;
    // TODO validate certifierDatePicker in JS validations
    var domObject;
    var returnval;
    var signdate = new Date(document.getElementById('submitDatePicker').value);
    var deathDate = new Date($('#dateOfDeath').val());
    var regDate = new Date($('#dateOfRegistration').val());
    var declareDate = new Date($('#declarantDatePicker').val());
    var notifyDate = new Date($('#submitDatePicker').val());

    //Validate Declarant Type
    var i,error = false;
    for (i = 0; i < 6; i++) {
        domObject = document.getElementsByName('declarant.declarantType')[i];
        if (domObject.checked) {
            error = true;
            //break;
        }
    }
    if (!error) {
        errormsg = errormsg + "\n" + document.getElementById('p2error8').value;
    }

    // validate declarant phone number
    domObject = document.getElementById('declarantPhone');
    if (!isFieldEmpty(domObject)) {
        validatePhoneNo(domObject, 'error1', 'error3');
    }
    // validate declarant email address
    domObject = document.getElementById('declarantEMail');
    if (!isFieldEmpty(domObject))
        validateEmail(domObject, 'error1', 'error2')

    //validate declarent NIC/PIN

    domObject = document.getElementById('declarant_pinOrNic');
    if (!isFieldEmpty(domObject)) {
        validatePINorNIC(domObject, 'error1', 'error4');
    }

    domObject = document.getElementById('declarantFullName');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error13').value;
    }

    domObject = document.getElementById('declarantAddress');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error15').value;
    }
    //validate declarent sign date
    domObject = document.getElementById('declarantDatePicker');
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, '', 'error21');
    } else {
        isDate(domObject.value, 'error1', 'p2error7');
    }

    if (deathType == 2) {
        validateCertifyingAuthority();
    }

    // notifier PIN or NIC
    domObject = document.getElementById('notifying_authority_NICorPIN');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('p2error1').value;
    } else {
        validatePINorNIC(domObject, 'error1', 'p2error5');
    }

    // notifier name
    domObject = document.getElementById('notifyingAuthorityName');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('p2error2').value;
    }

    // notifier address
    domObject = document.getElementById('notifyingAuthorityAddress');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('p2error4').value;
    }
    /*date related validations*/
    domObject = document.getElementById('submitDatePicker');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('p2error3').value;
    } else {
        isDate(domObject.value, 'error1', 'p2error6');
    }

    if(declareDate < deathDate){
        errormsg = errormsg + "\n" + document.getElementById('error22').value;
    }
    if(declareDate > regDate){
        errormsg = errormsg + "\n" + document.getElementById('error23').value;
    }
    if(notifyDate < deathDate){
        errormsg = errormsg + "\n" + document.getElementById('error24').value;
    }
    if(notifyDate < regDate){
        errormsg = errormsg + "\n" + document.getElementById('error25').value;
    }
    if(notifyDate < declareDate){
        errormsg = errormsg + "\n" + document.getElementById('error26').value;
    }

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }

    errormsg = "";
    return returnval;
}

function initPage() {
}

function setInformant(id, nICorPIN, name){
    $('#declarant_pinOrNic').val(nICorPIN);
    $('#declarantFullName').val(name);
}

function validateCertifyingAuthority() {
    // certifying authority PIN or NIC
    var domObject = document.getElementById('certifying_authority_NICorPIN');
    if (!isFieldEmpty(domObject)) {
        validatePINorNIC(domObject, 'error1', 'error16');
    }

    // certifying authority name
    domObject = document.getElementById('certifyingAuthorityName');
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, '', 'error17')
    }

    // certifying authority address
    domObject = document.getElementById('certifyingAuthorityAddress');
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, '', 'error18')
    }

    // certifying authority sign date
    domObject = document.getElementById('certifierDatePicker');
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, '', 'error19');
    } else {
        isDate(domObject.value, 'error1', 'error20');
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

<div id="death-declaration-form-2-outer">
<s:form name="deathRegistrationForm2" action="eprDeathDeclaration.do" id="death-registration-form-2" method="POST"
        onsubmit="javascript:return validate()">
<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
       class="font-9">
    <col width="22%"/>
    <col width="16%"/>
    <col width="10%"/>
    <col width="16%"/>
    <col width="10%"/>
    <col width="16%"/>
    <col width="10%"/>
    <tbody>
    <tr class="form-sub-title">
        <td colspan="7">
            දැනුම් දෙන්නාගේ විස්තර
            <br>அறிவிப்பு கொடுப்பவரின் தகவல்கள்
            <br>Details of the Informant
        </td>
    </tr>
    <tr>
        <td rowspan="2" colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            දැනුම් දෙන්නේ කවරකු වශයෙන්ද
            <br>யாரால் தகவல் தரப்படுகின்றது? <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>Capacity for giving information
        </td>
        <td colspan="1">
            පියා / මව
            <br>தந்தை/ தாய்
            <br>Father / Mother
        </td>
        <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                list="#@java.util.HashMap@{'FATHER':''}"
                                                onchange="javascript:setInformant('FATHER', '%{deathPerson.deathPersonFatherPINorNIC}',
                                 '%{deathPerson.deathPersonFatherFullName}')"
                /></td>
        <td colspan="1">
            ස්වාමිපුරුෂයා / භාර්යාව
            <br>கணவன்/ மனைவி
            <br>Husband / Wife
        </td>
        <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                list="#@java.util.HashMap@{'SPOUSE':''}"/></td>
        <td colspan="1">
            සහෝදරයා සහෝදරිය
            <br>சகோதரான்/ சகோதரி
            <br>Brother / Sister
        </td>
        <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                list="#@java.util.HashMap@{'BORTHER_OR_SISTER':''}"/></td>
    </tr>
    <tr>
        <td colspan="1">
            පුත්‍රයා / දියණිය
            <br>மகன்/ மகள்
            <br>Son / Daughter
        </td>
        <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                list="#@java.util.HashMap@{'SON_OR_DAUGHTER':''}"/></td>
        <td colspan="1">
            නෑයන්
            <br>உறவினர்
            <br>Relative
        </td>
        <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                list="#@java.util.HashMap@{'RELATIVE':''}"/></td>
        <td colspan="1">
            වෙනත්
            <br>வேறு
            <br>Other
        </td>
        <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                list="#@java.util.HashMap@{'OTHER':''}"/></td>
    </tr>
    <tr>
        <td colspan="4">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification No.
        </td>
        <td colspan="3" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="declarant_NIC_V" onclick="javascript:addXorV('declarant_pinOrNic','V','error12')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="declarant_NIC_X" onclick="javascript:addXorV('declarant_pinOrNic','X','error12')">
            <br>
            <s:textfield id="declarant_pinOrNic" name="declarant.declarantNICorPIN" maxLength="12"/><img
                src="<s:url value="/images/search-father.png"/>"
                style="vertical-align:middle; margin-left:20px;" id="declarant_lookup"></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            නම <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>பெயர்
            <br>Name
        </td>
        <td colspan="6">
            <s:textarea name="declarant.declarantFullName" id="declarantFullName"
                        cssStyle="width:99%;"
                        onblur="maxLengthCalculate('declarantFullName','255','declarantFullName_div');"/>
            <div id="declarantFullName_div" style="color:red;font-size:8pt"></div>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            තැපැල් ලිපිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>தபால் முகவரி
            <br>Postal Address
        </td>
        <td colspan="6">
            <s:textarea name="declarant.declarantAddress" id="declarantAddress"
                        cssStyle="width:99%;"
                        onblur="maxLengthCalculate('declarantAddress','255','declarantAddress_div');"/>
            <div id="declarantAddress_div" style="color:red;font-size:8pt"></div>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            ඇමතුම් විස්තර
            <br>தொடா்பு விபரம்
            <br>Contact Details
        </td>
        <td colspan="1">
            දුරකතනය
            <br>தொலைபேசி இலக்கம்
            <br>Telephone
        </td>
        <td colspan="2"><s:textfield id="declarantPhone" name="declarant.declarantPhone" maxLength="10"/></td>
        <td colspan="1">
            ඉ -තැපැල
            <br>மின்னஞ்சல்
            <br>Email
        </td>
        <td colspan="2">
            <s:textfield id="declarantEMail" name="declarant.declarantEMail" cssStyle="text-transform:lowercase;"
                         maxLength="240"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">දැනුම් දෙන්නා අත්සන්කල දිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>தகவல் அளித்தவர் கையொப்பமிட்ட திகதி <br>Informant Signed Date
        </td>
        <td colspan="5">
            <s:label value="YYYY-MM-DD" cssStyle="font-size:10px"/><br>
            <s:textfield id="declarantDatePicker" name="declarant.declarantSignDate" maxLength="10"/>
        </td>
    </tr>
    </tbody>
</table>

<s:if test="pageType == 2">
    <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
           class="font-9">
        <col width="150px"/>
        <col width="400px"/>
        <col width="100px"/>
        <col/>
        <tbody>
        <tr class="form-sub-title">
            <td colspan="4">
                මරණ පරීක්ෂක හෝ අධිකරණ වෛද්‍ය නිලධාරී ගේ විස්තර
                <br>மரண பரிசோதகர் அல்லது வைத்திய அதிகாரியின் விபரம்
                <br>Particulars of the Inquirer into deaths or Judicial Medical Officer
            </td>
        </tr>
        <tr>
            <td colspan="2">
                (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                අනන්‍යතා අංකය / அடையாள எண் / Identification Number
            </td>
            <td colspan="2" class="find-person">
                <img src="<s:url value="/images/alphabet-V.gif" />"
                     id="certify_authority_NIC_V"
                     onclick="javascript:addXorV('certifying_authority_NICorPIN','V','error12')">
                <img src="<s:url value="/images/alphabet-X.gif" />"
                     id="certifying_authority_NIC_X"
                     onclick="javascript:addXorV('certifying_authority_NICorPIN','X','error12')">
                <br>
                <s:textfield id="certifying_authority_NICorPIN" name="certifyingAuthority.certifyingAuthorityPIN"
                             maxLength="12"> </s:textfield>
                <img src="<s:url value="/images/search-father.png" />"
                     style="vertical-align:middle; margin-left:20px;" id="certifying_authority_lookup"></td>
            </td>
        </tr>
        <tr>
            <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                නම <s:label value="*" cssStyle="color:red;font-size:10pt"/>
                <br>பெயர்
                <br>Name
            </td>
            <td colspan="3">
                <s:textarea name="certifyingAuthority.certifyingAuthorityName" id="certifyingAuthorityName"
                            cssStyle="width:99%;"
                            onblur="maxLengthCalculate('certifyingAuthorityName','120','certifyingAuthorityName_div');"/>
                <div id="certifyingAuthorityName_div" style="color:red;font-size:8pt"></div>
            </td>
        </tr>
        <tr>
            <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                තැපැල් ලිපිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/>
                <br>தபால் முகவரி
                <br>Postal Address
            </td>
            <td colspan="3">
                <s:textarea name="certifyingAuthority.certifyingAuthorityAddress" id="certifyingAuthorityAddress"
                            cssStyle="width:99%;"
                            onblur="maxLengthCalculate('certifyingAuthorityAddress','255','certifyingAuthorityAddress_div');"/>
                <div id="certifyingAuthorityAddress_div" style="color:red;font-size:8pt"></div>
            </td>
        </tr>
        <tr>
            <td colspan="1">
                දිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
                <br>திகதி
                <br>Date
            </td>
            <td colspan="3">
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:2%;font-size:10px"/><br>
                <s:textfield id="certifierDatePicker" cssStyle="float:left;"
                             name="certifyingAuthority.certifyingAuthoritySignDate" maxLength="10"/></td>
        </tr>
        </tbody>
    </table>
</s:if>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
       class="font-9">
    <col width="150px"/>
    <col width="400px"/>
    <col width="100px"/>
    <col/>
    <tbody>
    <tr class="form-sub-title">
        <td colspan="4">
            <s:if test="pageType == 0 || pageType == 2">
                තොරතුරු වාර්තා කරන නිලධාරියාගේ / රෙජිස්ට්‍රාර්ගේ විස්තර
                <br>அறிக்கையிடும் அதிகாரி/பதிவாளர் பற்றிய விபரங்கள்
                <br>Details of the Notifying Officer / Registrar
                <s:set name="row" value="32"/>
            </s:if>
            <s:elseif test="pageType == 1">
                දිස්ත්‍රික් රෙජිස්ට්‍රාර් / රෙජිස්ට්‍රාර් ජෙනරාල්
                <br>மாவட்ட பதிவாளா்/ பதிவாளா் நாயகம்
                <br>District Registrar / Registrar General
            </s:elseif>
            <s:elseif test="pageType == 3">
                missing <br>
                දිස්ත්‍රික් රෙජිස්ට්‍රාර් / රෙජිස්ට්‍රාර් ජෙනරාල්
                <br>மாவட்ட பதிவாளா்/ பதிவாளா் நாயகம்
                <br>District Registrar / Registrar General
            </s:elseif>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            අනන්‍යතා අංකය / அடையாள எண் / Identification Number <s:label value="*" cssStyle="color:red;font-size:10pt"/>
        </td>
        <td colspan="2" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="notifying_authority_NIC_V"
                 onclick="javascript:addXorV('notifying_authority_NICorPIN','V','error12')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="notifying_authority_NIC_X"
                 onclick="javascript:addXorV('notifying_authority_NICorPIN','X','error12')">
            <br>
            <s:textfield id="notifying_authority_NICorPIN" name="notifyingAuthority.notifyingAuthorityPIN"
                         maxLength="12"> </s:textfield>
            <img src="<s:url value="/images/search-father.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="notifying_authority_lookup"></td>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            නම <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>பெயர்
            <br>Name
        </td>
        <td colspan="3">
            <s:textarea name="notifyingAuthority.notifyingAuthorityName" id="notifyingAuthorityName"
                        cssStyle="width:99%;"
                        onblur="maxLengthCalculate('notifyingAuthorityName','120','notifyingAuthorityName_div');"/>
            <div id="notifyingAuthorityName_div" style="color:red;font-size:8pt"></div>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            තැපැල් ලිපිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>தபால் முகவரி
            <br>Postal Address
        </td>
        <td colspan="3">
            <s:textarea name="notifyingAuthority.notifyingAuthorityAddress" id="notifyingAuthorityAddress"
                        cssStyle="width:99%;"
                        onblur="maxLengthCalculate('notifyingAuthorityAddress','255','notifyingAuthorityAddress_div');"/>
            <div id="notifyingAuthorityAddress_div" style="color:red;font-size:8pt"></div>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            දිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>திகதி
            <br>Date
        </td>
        <td colspan="3">
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:2%;font-size:10px"/><br>
            <s:textfield id="submitDatePicker" cssStyle="float:left;"
                         name="notifyingAuthority.notifyingAuthoritySignDate" maxLength="10"/></td>
    </tr>
    </tbody>
</table>

<div class="form-submit">
    <s:hidden name="pageNo" value="2"/>
    <s:if test="%{#session.deathRegister.idUKey==0}">
        <s:submit value="%{getText('add.label')}" cssStyle="margin-top:10px;"/>
    </s:if>
    <s:else>
        <s:submit value="%{getText('save.label')}" cssStyle="margin-top:10px;"/>
    </s:else>
</div>

<div class="next-previous">
    <s:url id="backUrl" action="eprDeathDeclaration.do">
        <s:param name="back" value="true"/>
        <s:param name="pageNo" value="{pageNo - 1}"/>
        <s:param name="idUKey" value="%{#request.idUKey}"/>
        <s:param name="pageType" value="%{pageType}"/>
        <s:param name="deathPersonPermenentAddressDistrictId" value="%{deathPersonPermenentAddressDistrictId}"/>
        <s:param name="deathPersonPermenentAddressDSDivisionId" value="%{deathPersonPermenentAddressDSDivisionId}"/>

    </s:url>
    <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</s:form>

<s:hidden id="deathTypeId" value="%{deathType.ordinal()}"/>
<s:hidden id="dateOfRegistration" value="%{death.dateOfRegistration}"/>
<s:hidden id="dateOfDeath" value="%{death.dateOfDeath}"/>
<s:hidden id="error1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error2" value="%{getText('declarant_email.text')}"/>
<s:hidden id="error3" value="%{getText('declarant_phone_no.text')}"/>
<s:hidden id="error4" value="%{getText('declarant_pinOrNic.text')}"/>
<s:hidden id="error5" value="%{getText('declarantAndNotifyDate.text')}"/>

<s:hidden id="p2error1" value="%{getText('p2.NIC.error.value')}"/>
<s:hidden id="p2error2" value="%{getText('p2.Name.error.value')}"/>
<s:hidden id="p2error3" value="%{getText('p2.submitDate.error.value')}"/>
<s:hidden id="p2error4" value="%{getText('p2.notifierAddress.text')}"/>
<s:hidden id="p2error5" value="%{getText('notifierNIC.text')}"/>
<s:hidden id="p2error6" value="%{getText('notifierDate.text')}"/>
<s:hidden id="p2error7" value="%{getText('declarentDate.text')}"/>
<s:hidden id="p2error8" value="%{getText('declarentType.text')}"/>
<s:hidden id="error12" value="%{getText('NIC.error.add.VX')}"/>
<s:hidden id="error13" value="%{getText('error.declerent.name.empty')}"/>
<s:hidden id="error14" value="%{getText('error.declerent.pin.empty')}"/>
<s:hidden id="error15" value="%{getText('error.declerent.address.empty')}"/>
<s:hidden id="error16" value="%{getText('certifierIdNo.text')}"/>
<s:hidden id="error17" value="%{getText('certifierName.empty')}"/>
<s:hidden id="error18" value="%{getText('certifierAddress.empty')}"/>
<s:hidden id="error19" value="%{getText('certifierSignDate.empty')}"/>
<s:hidden id="error20" value="%{getText('certifierSignDate.text')}"/>
<s:hidden id="error21" value="%{getText('declarantSignDate.empty')}"/>
<s:hidden id="error22" value="%{getText('informant.date.after.dod')}"/>
<s:hidden id="error23" value="%{getText('informant.date.before.reg.date')}"/>
<s:hidden id="error24" value="%{getText('notifying.date.after.dod')}"/>
<s:hidden id="error25" value="%{getText('notifying.date.after.reg.date')}"/>
<s:hidden id="error26" value="%{getText('notifying.date.after.informant.date')}"/>
<s:hidden id="maxLengthError" value="%{getText('error.max.length')}"/>

</div>

