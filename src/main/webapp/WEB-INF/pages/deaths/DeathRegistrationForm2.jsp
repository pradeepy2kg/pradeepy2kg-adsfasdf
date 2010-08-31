<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/popreg/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>

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
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#declarantFullName").val(data1.fullNameInOfficialLanguage);
                        $("textarea#declarantAddress").val(data1.lastAddress);
                    });
        });

        $('img#first_witness_lookup').bind('click', function(evt2) {
            var id2 = $("input#first_witness_NICorPIN").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id2},
                    function(data2) {
                        $("textarea#firstWitnessFullName").val(data2.fullNameInOfficialLanguage);
                        $("textarea#firstWitnessAddress").val(data2.lastAddress);
                    });
        });

        $('img#second_witness_lookup').bind('click', function(evt3) {
            var id3 = $("input#second_witness_NICorPIN").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id3},
                    function(data3) {
                        $("textarea#secondWitnessFullName").val(data3.fullNameInOfficialLanguage);
                        $("textarea#secondWitnessAddress").val(data3.lastAddress);
                    });
        });

        $('img#notifying_authority_lookup').bind('click', function(evt4) {
            var id4 = $("input#notifying_authority_NICorPIN").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id4},
                    function(data4) {
                        $("textarea#notifyingAuthorityName").val(data4.fullNameInOfficialLanguage);
                        $("textarea#notifyingAuthorityAddress").val(data4.lastAddress);
                    });
        });
    });

    var informPerson;
    function setInformPerson(nICorPIN, name)
    {
        var informantName = document.getElementById("declarant_pinOrNic").value = nICorPIN;
        var informantNICorPIN = document.getElementById("declarantFullName").value = name;
    }
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

        /*date related validations*/
        domObject = document.getElementById('submitDatePicker');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('p2error3').value;
        } else {
            isDate(domObject.value, 'error1', 'p2error6');
        }

        // validate declarant phone number
        domObject = document.getElementById('declarantPhone');
        if (!isFieldEmpty(domObject))
            isNumeric(domObject.value, 'error1', 'error3');

        // validate declarant email address
        domObject = document.getElementById('declarantEMail');
        if (!isFieldEmpty(domObject))
            validateEmail(domObject, 'error1', 'error2')

        //validate declarent NIC/PIN
        domObject = document.getElementById('declarant_pinOrNic');
        if (!isFieldEmpty(domObject))
            validatePINorNIC(domObject, 'error1', 'error4');

        //validate declarent sign date
        var declarant = document.getElementById("declarantDatePicker").value;
        var notify = document.getElementById("submitDatePicker").value;
        domObject = document.getElementById('declarantDatePicker');
        if (!isFieldEmpty(domObject))
                    isDate(domObject.value, 'error1', 'p2error7');

        if (notify < declarant) {
            errormsg = errormsg + "\n" +document.getElementById("error5").value;
        }

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }

        errormsg = "";
        return returnval;
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
                <td colspan="7">ප්‍රකාශකයාගේ විස්තර<br>அறிவிப்பு கொடுப்பவரின் தகவல்கள்<br>Details of the Declarant</td>
            </tr>
            <tr>
                <td rowspan="2" colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)දැනුම් දෙන්නේ
                    කවරකු
                    වශයෙන්ද<br>*in tamil<br>Capacity for giving information
                </td>
                <td colspan="1">මව <br>*in tamil<br>Mother</td>
                <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                        list="#@java.util.HashMap@{'FATHER':''}"
                                                        onchange="setInformPerson('%{deathPerson.deathPersonMotherPINorNIC}','%{deathPerson.deathPersonMotherFullName}');"/></td>
                <td colspan="1">පියා<br>*in tamil<br>Father</td>
                <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                        list="#@java.util.HashMap@{'MOTHER':''}"
                                                        onchange="setInformPerson('%{deathPerson.deathPersonFatherPINorNIC}','%{deathPerson.deathPersonFatherFullName}');"/></td>
                <td colspan="1">සහෝදරයා සහෝදරිය<br>*in tamil<br>Brother / Sister</td>
                <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                        list="#@java.util.HashMap@{'BORTHER_OR_SISTER':''}"/></td>
            </tr>
            <tr>
                <td colspan="1">පුත්‍රයා / දියණිය <br>*in tamil<br>Son / Daughter</td>
                <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                        list="#@java.util.HashMap@{'SON_OR_DAUGHTER':''}"/></td>
                <td colspan="1">නෑයන් <br>பாதுகாவலர் <br>Relative</td>
                <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                        list="#@java.util.HashMap@{'RELATIVE':''}"/></td>
                <td colspan="1">වෙනත් <br>*in tamil<br>Other</td>
                <td colspan="1" align="center"><s:radio id="declarantType" name="declarant.declarantType"
                                                        list="#@java.util.HashMap@{'OTHER':''}"/></td>
            </tr>
            <tr>
                <td colspan="4">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)පුද්ගල අනන්‍යතා අංකය /
                    ජාතික හැදුනුම්පත් අංකය<br>தகவநபர் அடையாள எண் / அடையாள அட்டை இல.
                    <br>PIN / NIC
                </td>
                <td colspan="3" class="find-person"><s:textfield id="declarant_pinOrNic"
                                                                 name="declarant.declarantNICorPIN"/><img
                        src="<s:url value="/images/search-father.png"/>"
                        style="vertical-align:middle; margin-left:20px;" id="declarant_lookup"></td>
            </tr>
            <tr>
                <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)නම<br>கொடுப்பவரின்
                    பெயர்<br>Name
                </td>
                <td colspan="6"><s:textarea id="declarantFullName" name="declarant.declarantFullName"
                                            cssStyle="width:880px;"/></td>
            </tr>
            <tr>
                <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)තැපැල් ලිපිනය<br>தபால்
                    முகவரி<br>Postal Address
                </td>
                <td colspan="6"><s:textarea id="declarantAddress" name="declarant.declarantAddress"
                                            cssStyle="width:880px;"/></td>
            </tr>
            <tr>
                <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)ඇමතුම් විස්තර<br>இலக்க வகை
                    <br>Contact Details
                </td>
                <td colspan="1">දුරකතනය<br>தொலைபேசி இலக்கம் <br>Telephone</td>
                <td colspan="2"><s:textfield id="declarantPhone" name="declarant.declarantPhone"/></td>
                <td colspan="1">ඉ -තැපැල<br>மின்னஞ்சல்<br>Email</td>
                <td colspan="2">
                    <s:textfield id="declarantEMail" name="declarant.declarantEMail" cssStyle="text-transform:none;"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">ප්‍රකාශකයා අත්සන්කල දිනය<br> *in tamil<br>Declaranat Signed Date</td>
                <td colspan="5">
                    <s:textfield id="declarantDatePicker" name=""/>
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
                <td colspan="4"><s:if test="deathType.ordinal() == 0 || deathType.ordinal() == 1">
                    තොරතුරු වාර්තා කරන පාර්ශවය<br>அதிகாரியிடம் தெரிவித்தல்<br>Notifying Authority
                </s:if>
                    <s:elseif test="deathType.ordinal() == 2 || deathType.ordinal() == 3">
                        දිස්ත්‍රික් රෙජිස්ට්‍රාර් / රෙජිස්ට්‍රාර් ජෙනරාල් <br/>
                        அதிகாரியிடம் தெரிவித்தல் <br/>
                        District Registrar / Registrar General
                    </s:elseif>
                </td>
            </tr>
            <tr>
                <td colspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>அடையாள எண் / அடையாள அட்டை இல. <br>PIN /
                    NIC
                </td>
                <td colspan="2"><s:textfield id="notifying_authority_NICorPIN"
                                             name="notifyingAuthority.notifyingAuthorityPIN"> </s:textfield>
                    <img src="<s:url value="/images/search-father.png" />"
                         style="vertical-align:middle; margin-left:20px;" id="notifying_authority_lookup"></td>
                </td>
            </tr>
            <tr>
                <td colspan="1">නම<br>கொடுப்பவரின் பெயர்<br>Name</td>
                <td colspan="3"><s:textarea id="notifyingAuthorityName"
                                            name="notifyingAuthority.notifyingAuthorityName"
                                            cssStyle="width:880px;"/></td>
            </tr>
            <tr>
                <td colspan="1">තැපැල් ලිපිනය<br>தபால் முகவரி<br>Postal Address</td>
                <td colspan="3"><s:textarea id="notifyingAuthorityAddress"
                                            name="notifyingAuthority.notifyingAuthorityAddress"
                                            cssStyle="width:880px;"/></td>
            </tr>
            <tr>
                <td colspan="1">අත්සන හා නිල මුද්‍රාව<br>தகவல் ...<br>Signature and Official Seal of the Notifying
                    Authority
                </td>
                <td colspan="1"></td>
                <td colspan="1">දිනය<br>திகதி<br>Date</td>
                <td colspan="1"><s:textfield id="submitDatePicker"
                                             name="notifyingAuthority.notifyingAuthoritySignDate"/></td>
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

        <s:label><p class="font-8">පු.අ.අ. / ජා.හැ.අ. = පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය</p></s:label>

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