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
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#declarantFullName").val(data1.fullNameInOfficialLanguage);
                        $("textarea#declarantAddress").val(data1.lastAddress);
                    });
        });

        $('img#first_witness_lookup').bind('click', function(evt2) {
            var id2 = $("input#first_witness_NICorPIN").attr("value");
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id2},
                    function(data2) {
                        $("textarea#firstWitnessFullName").val(data2.fullNameInOfficialLanguage);
                        $("textarea#firstWitnessAddress").val(data2.lastAddress);
                    });
        });

        $('img#second_witness_lookup').bind('click', function(evt3) {
            var id3 = $("input#second_witness_NICorPIN").attr("value");
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id3},
                    function(data3) {
                        $("textarea#secondWitnessFullName").val(data3.fullNameInOfficialLanguage);
                        $("textarea#secondWitnessAddress").val(data3.lastAddress);
                    });
        });

        $('img#notifying_authority_lookup').bind('click', function(evt4) {
            var id4 = $("input#notifying_authority_NICorPIN").attr("value");
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id4},
                    function(data4) {
                        $("textarea#notifyingAuthorityName").val(data4.fullNameInOfficialLanguage);
                        $("textarea#notifyingAuthorityAddress").val(data4.lastAddress);
                    });
        });

    });

    var informPerson;
    //    function setInformPerson(nICorPIN, name)
    //    {
    //        var informantName = document.getElementById("declarant_pinOrNic").value = nICorPIN;
    //        var informantNICorPIN = document.getElementById("declarantFullName").value = name;
    //    }
    var errormsg = "";
    function validate() {
        var domObject;
        var returnval;
        var signdate = new Date(document.getElementById('submitDatePicker').value);

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

        // notifier adderss
        domObject = document.getElementById('notifyingAuthorityAddress');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('p2error4').value;
        }

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

        /*date related validations*/
        domObject = document.getElementById('submitDatePicker');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('p2error3').value;
        } else {
            isDate(domObject.value, 'error1', 'p2error6');
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
        else {
            errormsg = errormsg + "\n" + document.getElementById('error14').value;
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
        var declarant = document.getElementById("declarantDatePicker").value;
        var notify = document.getElementById("submitDatePicker").value;
        domObject = document.getElementById('declarantDatePicker');
        if (!isFieldEmpty(domObject))
            isDate(domObject.value, 'error1', 'p2error7');

        if (notify < declarant) {
            errormsg = errormsg + "\n" + document.getElementById("error5").value;
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

</script>

<div id="death-declaration-form-2-outer">
<s:form name="deathRegistrationForm2" action="eprDeathDeclaration.do" id="death-registration-form-2" method="POST"
        onsubmit="javascript:return validate()">
<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
       class="font-9">
    <col width="150px"/>
    <col width="130px"/>
    <col width="120px"/>
    <col width="130px"/>
    <col width="130px"/>
    <col width="130px"/>
    <col/>
    <tbody>
    <tr class="form-sub-title">
        <td colspan="7">
            ප්‍රකාශකයාගේ විස්තර
            <br>அறிவிப்பு கொடுப்பவரின் தகவல்கள்
            <br>Details of the Declarant
        </td>
    </tr>
    <tr>
        <td rowspan="2" colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            දැනුම් දෙන්නේ කවරකු වශයෙන්ද <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>தகவல் வழங்குபவா்
            <br>Capacity for giving information
        </td>
        <td colspan="1">
            පියා / මව
            <br>தந்தை/ தாய்
            <br>Father / Mother
        </td>
        <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                list="#@java.util.HashMap@{'FATHER':''}"/></td>
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
            <br>உறவினா்
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
            අනන්‍යතා අංකය <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>அடையாள எண்
            <br>Identification No.
        </td>
        <td colspan="3" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="declarant_NIC_V" onclick="javascript:addXorV('declarant_pinOrNic','V','error12')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="declarant_NIC_X" onclick="javascript:addXorV('declarant_pinOrNic','X','error12')">
            <br>
            <s:textfield id="declarant_pinOrNic" name="declarant.declarantNICorPIN" maxLength="10"/><img
                src="<s:url value="/images/search-father.png"/>"
                style="vertical-align:middle; margin-left:20px;" id="declarant_lookup"></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            නම <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>பெயர்
            <br>Name
        </td>
        <td colspan="6"><s:textarea id="declarantFullName" name="declarant.declarantFullName"
                                    cssStyle="width:880px;"/></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            තැපැල් ලිපිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>தபால் முகவரி
            <br>Postal Address
        </td>
        <td colspan="6"><s:textarea id="declarantAddress" name="declarant.declarantAddress"
                                    cssStyle="width:880px;"/></td>
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
            <s:textfield id="declarantEMail" name="declarant.declarantEMail" cssStyle="text-transform:none;"
                         maxLength="240"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">ප්‍රකාශකයා අත්සන්කල දිනය<br>பிரதிக்கினையாளர் கையொப்பமிட்ட திகதி <br>Declaranat Signed Date</td>
        <td colspan="5">
            <s:label value="YYYY-MM-DD" cssStyle="font-size:10px"/><br>
            <s:textfield id="declarantDatePicker" name="" maxLength="10"/>
        </td>
    </tr>
    </tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
       class="font-9">
    <col width="150px"/>
    <col width="400px"/>
    <col width="100px"/>
    <col/>
    <tbody>
    <tr class="form-sub-title">
        <td colspan="4"><s:if test="pageType==0">
            තොරතුරු වාර්තා කරන පාර්ශවය / මරණ පරීක්ෂක හෝ අධිකරණ වෛද්‍ය නිලධාරී
            <br>தகவல் அறிக்கையிடும் திறத்தார்/ மரண பரிசோதகா் அல்லது வைத்திய அதிகாரி
            <br>Notifying Authority / Inquirer into deaths or Judicial Medical Officer </s:if>
            <s:elseif test="pageType == 1">
                දිස්ත්‍රික් රෙජිස්ට්‍රාර් / රෙජිස්ට්‍රාර් ජෙනරාල්
                <br>மாவட்ட பதிவாளா்/ பதிவாளா் நாயகம்
                <br>District Registrar / Registrar General
            </s:elseif>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            අනන්‍යතා අංකය <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>அடையாள எண்
            <br>PIN
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
                         maxLength="10"> </s:textfield>
            <img src="<s:url value="/images/search-father.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="notifying_authority_lookup"></td>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            නම <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>பெயர்
            <br>Name
        </td>
        <td colspan="3"><s:textarea id="notifyingAuthorityName"
                                    name="notifyingAuthority.notifyingAuthorityName"
                                    cssStyle="width:880px;"/></td>
    </tr>
    <tr>
        <td colspan="1">
            තැපැල් ලිපිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>தபால் முகவரி
            <br>Postal Address
        </td>
        <td colspan="3"><s:textarea id="notifyingAuthorityAddress"
                                    name="notifyingAuthority.notifyingAuthorityAddress"
                                    cssStyle="width:880px;"/></td>
    </tr>
    <tr>
        <td colspan="1">
            අත්සන හා නිල මුද්‍රාව
            <br>கையொப்பமும் அதிகார முத்திரையும்
            <br>Signature and Official Seal of the Notifying Authority
        </td>
        <td colspan="1"></td>
        <td colspan="1">
            දිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
            <br>திகதி
            <br>Date
        </td>
        <td colspan="1">
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:2%;font-size:10px"/><br>
            <s:textfield id="submitDatePicker" cssStyle="float:left;"
                         name="notifyingAuthority.notifyingAuthoritySignDate" maxLength="10"/></td>
    </tr>
    </tbody>
</table>

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


<div class="form-submit">
    <s:hidden name="pageNo" value="2"/>
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
<div class="next-previous">
    <s:url id="backUrl" action="eprDeathDeclaration.do">
        <s:param name="back" value="true"/>
        <s:param name="pageNo" value="{pageNo - 1}"/>
    </s:url>
    <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</s:form>
</div>