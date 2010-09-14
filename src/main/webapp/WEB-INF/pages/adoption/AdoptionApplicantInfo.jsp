<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script type="text/javascript">

    $(function() {
        $('img#adoption_applicant_lookup').bind('click', function(evt3) {
            var id1 = $("input#certifcateApplicantPin").attr("value");
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#certificateApplicantName").val(data1.fullNameInOfficialLanguage);
                        //$("textarea#deathPersonNameInEnglish").val(data1.fullNameInOfficialLanguage);
                        $("textarea#certificateApplicantAddress").val(data1.lastAddress);
                    });
        });
    });

    function setApplicantInfo(pin, name, address)
    {

        var applicantPin = document.getElementById("certifcateApplicantPin").value = "";
        var applicantName = document.getElementById("certificateApplicantName").value = "";
        var applicantAddress = document.getElementById("certificateApplicantAddress").value = "";
        domObject0 = document.getElementsByName("certificateApplicantType")[0];
        if (domObject0.checked) {
            var applicantPin = document.getElementById("certifcateApplicantPin").value = pin;
            var applicantName = document.getElementById("certificateApplicantName").value = name;
            var applicantAddress = document.getElementById("certificateApplicantAddress").value = address;
        }
        domObject1 = document.getElementsByName("certificateApplicantType")[1];
        if (domObject1.checked) {
            var applicantPin = document.getElementById("certifcateApplicantPin").value = pin;
            var applicantName = document.getElementById("certificateApplicantName").value = name;
            var applicantAddress = document.getElementById("certificateApplicantAddress").value = address;
        }
    }

    var errormsg = "";
    function validate() {

        var returnval = true;
        var domObject;
        domObject = document.getElementById("courtOrderNo");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error4');
        }
        domObject = document.getElementById("certifcateApplicantPin");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error0');
        }
        else {
            validatePINorNIC(domObject, 'error5', 'error0');
        }
        domObject = document.getElementById("certificateApplicantName");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error1');
        }
        domObject = document.getElementById("certificateApplicantAddress");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error2');
        }
        domObject = document.getElementsByName("certificateApplicantType")[0];
        domObject1 = document.getElementsByName("certificateApplicantType")[1];
        domObject2 = document.getElementsByName("certificateApplicantType")[2];
        if (!(domObject.checked || domObject1.checked || domObject2.checked)) {
            errormsg = errormsg + "\n" + document.getElementById("error3").value;
        }
        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }
</script>

<div id="adoption-applicant-info-form-outer">
<form action="eprAdoptionFind.do" method="post">
    <table style=" border:1px solid #000000; width:300px">
        <tr><s:actionerror cssStyle="color:red;"/></tr>
        <tr>
            <td>
                <s:label value="%{getText('adoption_order_serial.label')}"/>
            </td>
            <td><s:textfield name="idUKey" id="idUKey"/></td>
        </tr>
    </table>
    <table style=" width:300px">
        <tr>

        <tr>
            <td width="200px"></td>
            <td align="right" class="button"><s:submit name="search"
                                                       value="%{getText('adoption_search_button.label')}"
                                                       cssStyle="margin-right:10px;"/></td>
        </tr>
        <tr>
        </tr>
    </table>
</form>
<table border="1" class="adoption-applicant" cellspacing="0" cellpadding="0"
       style="border:1px solid #000; border-collapse:collapse;">
    <tr>
        <td width="662px">නියෝගය ලැබුණු දිනය <br/>
            Received Date
        </td>
        <td style="text-align:center;" width="350px"><s:label value="%{#request.adoption.orderReceivedDate}"/></td>
    </tr>
    <tr>
        <td>අධිකරණය<br/>
            Court
        </td>
        <td style="text-align:center;"><s:label value="%{#request.adoption.court}" id="court"/></td>
    </tr>
    <tr>
        <td>නියෝගය නිකුත් කල දිනය <br/>
            Issued Date
        </td>
        <td style="text-align:center;"><s:label value="%{#request.adoption.orderIssuedDate}"/></td>
    </tr>
    <tr>
        <td>නියෝග අංකය<br/>
            Serial number
        </td>
        <td style="text-align:center;"><s:label value="%{#request.adoption.courtOrderNumber}"
                                                id="courtOrderNumber"/></td>
    </tr>
    <tr>
        <td>විනිසුරු නම <br/>
            Name of the Judge
        </td>
        <td style="text-align: center;"><s:label value="%{#request.adoption.judgeName}" id="judgeName"
                                                 disabled="true"/></td>
    </tr>

</table>
<br>
<br>
<br>
<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col style="width:330px;"/>
    <col style="width:160px;"/>
    <col style="width:190px;"/>
    <col style="width:160px;"/>
    <col style="width:190px;"/>
    <tbody>
    <tr>
        <td>උපන් දිනය<br/>
            Date of birth
        </td>
        <td colspan="2" style="text-align:center;">
            <s:label value="%{#request.adoption.childBirthDate}"/>
        </td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br/>
            Gender
        </td>
        <td>
            <s:if test="#request.adoption.childGender == 0">
                <s:label name="" value="%{getText('male.label')}"/>
            </s:if>
            <s:elseif test="#request.adoption.childGender == 1">
                <s:label name="" value="%{getText('female.label')}"/>
            </s:elseif>
            <s:elseif test="#request.adoption.childGenderr == 2">
                <s:label name="" value="%{getText('unknown.label')}"/>
            </s:elseif>
        </td>
    </tr>
    <tr>
        <td>වයස <br/>
            Age
        </td>
        <td>අවුරුදු <br/>
            Years
        </td>
        <td><s:label value="%{#request.adoption.childAgeYears}" id="childAgeYears"/></td>
        <td>මාස <br/>
            Months
        </td>
        <td><s:label value="%{#request.adoption.childAgeMonths}" id="childAgeMonths"/></td>
    </tr>
    <tr>
        <td>දැනට පවතින නම <br/>
            (නමක් දී ඇති නම්) <br/>
            Existing Name <br/>
            (if already given)
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.childExistingName}" id="childExistingName"/></td>
    </tr>
    <tr>
        <td>ලබා දෙන නම <br/>
            New name given
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.childNewName}" id="childNewName"/></td>
    </tr>
    </tbody>
</table>
<br>
<br>
<br>

<s:form action="eprCaptureAdoptionApplicantInfo.do" onsubmit="javascript:return validate()">
    <s:hidden name="pageNo" value="1"/>
    <table border="1" class="adoption-applicant" cellspacing="0" cellpadding="0"
           style="border:1px solid #000; border-collapse:collapse;">
        <caption></caption>
        <col/>
        <col width="330px"/>
        <col width="175px"/>
        <col width="175px"/>
        <col width="175px"/>
        <col width="175px"/>
        <col width="175px"/>
        <col width="175px"/>
        <tbody>

        <tr>
            <td colspan="2">අයදුම් කරු <br/>
                Applicant
            </td>
            <td>පියා   </br>
                Father
            </td>
            <td><s:if test="#request.adoption.applicantMother==false">
                <s:radio name="certificateApplicantType" id="certificateApplicantType"
                         list="#@java.util.HashMap@{'FATHER':''}"
                         onchange="setApplicantInfo('%{#request.adoption.applicantPINorNIC}','%{#request.adoption.applicantName}','%{#request.adoption.applicantAddress}');"/>
            </s:if>
                <s:else><s:radio name="certificateApplicantType" id="certificateApplicantType"
                                 list="#@java.util.HashMap@{'FATHER':''}" onchange="setApplicantInfo('','','');"/>
                </s:else>
            </td>
            <td>මව <br/>
                Mother
            </td>
            <td><s:if test="#request.adoption.applicantMother==true">
                <s:radio name="certificateApplicantType" id="certificateApplicantType"
                         list="#@java.util.HashMap@{'MOTHER':''}"
                         onchange="setApplicantInfo('%{#request.adoption.applicantPINorNIC}','%{#request.adoption.applicantName}','%{#request.adoption.applicantAddress}');"/>
            </s:if>
                <s:else>
                    <s:radio name="certificateApplicantType" id="certificateApplicantType"
                             list="#@java.util.HashMap@{'MOTHER':''}"
                             onchange="setApplicantInfo('','','');"/>
                </s:else>
            </td>
            <td>වෙනත් <br/>
                Other
            </td>
            <td>
                <s:radio name="certificateApplicantType" id="certificateApplicantType"
                         list="#@java.util.HashMap@{'OTHER':''}" onchange="setApplicantInfo('','','');"/>
            </td>
        </tr>
        <tr>
            <td colspan="6">
                අයදුම් කරුගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
                <br>
                தாயின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம்
                <br>
                Applicant's PIN / NIC Number
                <br>
            </td>
            <td colspan="4" class="find-person">
                <img src="<s:url value="/images/alphabet-V.gif" />"
                     id="applicant_NIC_V" onclick="javascript:addXorV('certifcateApplicantPin','V','error6')">
                <img src="<s:url value="/images/alphabet-X.gif" />"
                     id="applicant_NIC_X" onclick="javascript:addXorV('certifcateApplicantPin','X','error6')">
                <br>
                <s:textfield id="certifcateApplicantPin" name="certificateApplicantPINorNIC"/> <img
                    src="<s:url value="/images/search-father.png" />"
                    style="vertical-align:middle; margin-left:20px;" id="adoption_applicant_lookup">
            </td>
        </tr>
        <td colspan="2">
            අයදුම් කරුගේ නම
            <br>
            Name of the Applicant
        </td>
        <td colspan="6">
            <s:textarea id="certificateApplicantName" name="certificateApplicantName"
                        cssStyle="width:98.2%;"/>
        </td>
        </tr>
        <tr>
            <td colspan="2">
                ලිපිනය
                <br>
                Address
            </td>
            <td colspan="6">
                <s:textarea id="certificateApplicantAddress" name="certificateApplicantAddress"
                            cssStyle="width:98.2%;"/>
            </td>
        </tr>
        </tbody>
    </table>

    <div class="button" align="right">
        <s:submit value="%{getText('adoption.submit')}" cssStyle="margin-top:10px;"/>
    </div>
    <s:hidden id="error0" value="%{getText('er.label.applicantPINorNIC')}"/>
    <s:hidden id="error1" value="%{getText('er.label.applicantName')}"/>
    <s:hidden id="error2" value="%{getText('er.label.applicantAddress')}"/>
    <s:hidden id="error3" value="%{getText('er.label.applicantType')}"/>
    <s:hidden id="error4" value="%{getText('er.label.courtOrderNumber')}"/>
    <s:hidden id="error5" value="%{getText('p1.invalide.inputType')}"/>
    <s:hidden id="error6" value="%{getText('NIC.error.add.VX')}"/>

</s:form>
</div>