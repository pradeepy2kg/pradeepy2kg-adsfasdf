<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script type="text/javascript" src='<s:url value="/js/common.js"/>'></script>
<script type="text/javascript">

    $(function () {
        $('#adoptionSerialNo').val(new Date().getFullYear());
        $('img#adoption_applicant_lookup').bind('click', function (evt3) {
            var id1 = $("input#certifcateApplicantPin").attr("value");

            $("textarea#certificateApplicantName").val('');
            $("textarea#certificateApplicantAddress").val('');
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                    function (data1) {
                        $("textarea#certificateApplicantName").val(data1.fullNameInOfficialLanguage);
                        $("textarea#certificateApplicantAddress").val(data1.lastAddress);
                    });
        });
    });

    function setApplicantInfo() {
        if ($('#jointApplicant').val() == 'true') {
            var pin = $('#pin').val();
            var name = $('#name').val();
            var address = $('#address').val();
            var spousePin = $('#spousePin').val();
            var spouseName = $('#spouseName').val();

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
                var applicantPin = document.getElementById("certifcateApplicantPin").value = spousePin;
                var applicantName = document.getElementById("certificateApplicantName").value = spouseName;
                var applicantAddress = document.getElementById("certificateApplicantAddress").value = address;
            }
        }
    }

    var errormsg = "";
    function validate() {

        var returnval = true;
        var domObject;

        domObject = document.getElementsByName("certificateApplicantType")[0];
        domObject1 = document.getElementsByName("certificateApplicantType")[1];
        domObject2 = document.getElementsByName("certificateApplicantType")[2];
        if (!(domObject.checked || domObject1.checked || domObject2.checked)) {
            errormsg = errormsg + "\n" + document.getElementById("error3").value;
        }
        domObject = document.getElementById("certifcateApplicantPin");
        if (!isFieldEmpty(domObject)) {
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

        domObject = document.getElementById("adoptionSerialNo");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error7');
        } else if ($('#adoptionSerialNo').val().length != 9) {
            errormsg = errormsg + "\n" + document.getElementById('error8').value;
        }

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

    function initPage() {
        var domObject = document.getElementById('idUKey');
        if (domObject.value.trim() == 0) {
            domObject.value = null;
        }
        displaySave();
    }

    function disableButton(mode) {
        if (mode) {
            document.getElementById('adoptionSubmit').style.display = 'none';
        }
        else {
            document.getElementById('adoptionSubmit').style.display = 'block';
        }
    }

    function displaySave() {
        var receive = document.getElementById('receivedDate').value;
        if (receive.trim() == 0) {
            disableButton(true);
        } else {
            disableButton(false);
        }
    }
</script>

<div id="adoption-applicant-info-form-outer">
<form action="eprAdoptionFind.do" method="post">
    <table style=" border:1px solid #000000; width:400px">
        <tr><s:actionerror cssStyle="color:red;"/></tr>
        <tr>
            <td>
                සහතික පත්‍රයේ අංකය
                <br>சான்றிதழ் இல
                <br>Certificate Number
            </td>
            <td>
                <s:textfield name="idUKey" id="idUKey" onkeypress="return isNumberKey(event)" maxLength="10"/>
                <s:hidden id="receivedDate" value="%{#request.adoption.orderReceivedDate}"/>
            </td>
        </tr>
    </table>
    <table style=" width:400px">
        <tr>

        <tr>
            <td width="200px"></td>
            <td align="right" class="button"><s:submit name="search"
                                                       value="%{getText('adoption_search_button.label')}"
                                                       cssStyle="margin-right:10px;" onclick="displaySave()"/></td>
        </tr>
        <tr>
        </tr>
    </table>
</form>
<table border="1" class="adoption-applicant" cellspacing="0" cellpadding="0"
       style="border:1px solid #000; border-collapse:collapse;">
    <tr>
        <td width="662px">නියෝගය ලැබුණු දිනය <br/>
            கட்டளை கிடைக்கப்பெற்ற திகதி <br>
            Received Date
        </td>
        <td style="text-align:center;" width="350px"><s:label value="%{#request.adoption.orderReceivedDate}"/></td>
    </tr>
    <tr>
        <td>අධිකරණය<br/>
            நீதிமன்றம் <br>
            Court
        </td>
        <td style="text-align:center;"><s:label name="courtName" id="court"/></td>
    </tr>
    <tr>
        <td>නියෝගය නිකුත් කල දිනය <br/>
            <%--order Issued Date in ta <br>--%>
            கட்டளை வழங்கப்பட்ட திகதி<br/>
            Order Issued Date
        </td>
        <td style="text-align:center;"><s:label value="%{#request.adoption.orderIssuedDate}"/></td>
    </tr>
    <tr>
        <td>නියෝග අංකය<br/>
            கட்டளை வழங்கப்பட்ட திகதி <br>
            Court order number
        </td>
        <td style="text-align:center;"><s:label value="%{#request.adoption.courtOrderNumber}"
                                                id="courtOrderNumber"/></td>
    </tr>
    <tr>
        <td>විනිසුරුගේ නම <br/>
            நீதிபதியின் பெயர் <br>
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
            பிறந்த திகதி<br>
            Date of Birth
        </td>
        <td colspan="2" style="text-align:center;">
            <s:label value="%{#request.adoption.childBirthDate}"/>
        </td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br/>
            பால் <br>
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
            வயது <br>
            Age
        </td>
        <td>අවුරුදු <br/>
            வருடங்கள் <br>
            Years
        </td>
        <td><s:label value="%{#request.adoption.childAgeYears}" id="childAgeYears"/></td>
        <td>මාස <br/>
            மாதங்கள் <br>
            Months
        </td>
        <td><s:label value="%{#request.adoption.childAgeMonths}" id="childAgeMonths"/></td>
    </tr>
    <tr>
        <td>දැනට පවතින නම <br/>
            (නමක් දී ඇති නම්) <br/>
            தற்போதைய பெயர் <br/>
            (பெயர் குறிப்பிடப்பட்டிருந்தால் )
            <br/>Existing Name
            (if already given)
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.childExistingName}" id="childExistingName"/></td>
    </tr>
    <tr>
        <td>ලබා දෙන නම <br/>
            பெற்றுக் கொடுக்கப்படும் பெயர் <br>
            New name given
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.childNewName}" id="childNewName"/></td>
    </tr>
    </tbody>
</table>
<br>
<br>

<s:form action="eprCaptureAdoptionApplicantInfo.do" onsubmit="javascript:return validate()">
    <s:hidden name="pageNo" value="1"/>
    <table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0"
           style="float:right;width:450px; margin-right: 10px;">
        <tr>
            <s:actionerror cssStyle="color:red;"/>
        </tr>
        <tr>
            <td height="40px" width="160px">
                <label>
                    අනුක්‍රමික අංකය
                    <br>தொடர் இலக்கம்
                    <br>Serial Number
                </label>
            </td>
            <td width="240px" height="40px">
                <s:textfield id="adoptionSerialNo" name="adoptionSerialNo" maxlength="9"
                             onkeypress="return numbersOnly(event, true);"/>
            </td>
        </tr>
    </table>
    <br/>
    <table width="99%" border="1" class="adoption-applicant" cellspacing="0" cellpadding="0"
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
            <td colspan="2">අයදුම්කරු <s:label value="*" cssStyle="color:red;font-size:10pt;"/><br/>
                விண்ணப்பதாரர் <br>
                Applicant
            </td>
            <td>පියා   </br>
                தகப்பன் <br>
                Father
            </td>
            <td><s:if test="#request.adoption.applicantMother==false">
                <s:radio name="certificateApplicantType" id="certificateApplicantType"
                         list="#@java.util.HashMap@{'FATHER':''}"
                         onchange="setApplicantInfo();"/>
            </s:if>
                <s:else><s:radio name="certificateApplicantType" id="certificateApplicantType"
                                 list="#@java.util.HashMap@{'FATHER':''}"
                                 onchange="setApplicantInfo();"/>
                </s:else>
            </td>
            <td>මව <br/>
                தாய் <br>
                Mother
            </td>
            <td><s:if test="#request.adoption.applicantMother==true">
                <s:radio name="certificateApplicantType" id="certificateApplicantType"
                         list="#@java.util.HashMap@{'MOTHER':''}"
                         onchange="setApplicantInfo();"/>
            </s:if>
                <s:else>
                    <s:radio name="certificateApplicantType" id="certificateApplicantType"
                             list="#@java.util.HashMap@{'MOTHER':''}"
                             onchange="setApplicantInfo();"/>
                </s:else>
            </td>
            <td>වෙනත් <br/>
                வேறு <br>
                Other
            </td>
            <td>
                <s:radio name="certificateApplicantType" id="certificateApplicantType"
                         list="#@java.util.HashMap@{'OTHER':''}" onchange="setApplicantInfo();"/>
            </td>
        </tr>
        <tr>
            <td colspan="6">
                අයදුම්කරුගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
                <br>
                தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம்
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
                <s:textfield id="certifcateApplicantPin" name="certificateApplicantPINorNIC" maxLength="12"/> <img
                    src="<s:url value="/images/search-father.png" />"
                    style="vertical-align:middle; margin-left:20px;" id="adoption_applicant_lookup">
            </td>
        </tr>
        <td colspan="2">
            අයදුම්කරුගේ නම<s:label value="*" cssStyle="color:red;font-size:10pt;"/>
            <br>
            விண்ணப்பதாரரின் பெயர் <br>
            Name of the Applicant
        </td>
        <td colspan="6">
            <s:textarea id="certificateApplicantName" name="certificateApplicantName"
                        cssStyle="width:98.2%;"/>
        </td>
        </tr>
        <tr>
            <td colspan="2">
                ලිපිනය<s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                <br>
                முகவரி <br>
                Address
            </td>
            <td colspan="6">
                <s:textarea id="certificateApplicantAddress" name="certificateApplicantAddress" cols="50" rows="4"
                            onkeydown="return limitLines(this, event);"
                            cssStyle="width:98.2%;"/>
            </td>
        </tr>
        </tbody>
    </table>

    <div class="button" align="right">
        <s:submit id="adoptionSubmit" value="%{getText('adoption.submit')}" cssStyle="margin-top:10px;"/>
    </div>
    <s:hidden id="error0" value="%{getText('er.label.applicantPINorNIC')}"/>
    <s:hidden id="error1" value="%{getText('er.label.applicantName')}"/>
    <s:hidden id="error2" value="%{getText('er.label.applicantAddress')}"/>
    <s:hidden id="error3" value="%{getText('er.label.applicantType')}"/>
    <s:hidden id="error4" value="%{getText('er.label.courtOrderNumber')}"/>
    <s:hidden id="error5" value="%{getText('p1.invalide.inputType')}"/>
    <s:hidden id="error6" value="%{getText('NIC.error.add.VX')}"/>
    <s:hidden id="error7" value="%{getText('er.label.serialNo.required')}"/>
    <s:hidden id="error8" value="%{getText('er.label.serialNo.format')}"/>

    <%-- Values to auto populate applicant info --%>
    <s:hidden id="pin" name="adoption.applicantPINorNIC"/>
    <s:hidden id="name" name="adoption.applicantName"/>
    <s:hidden id="address" name="adoption.applicantAddress"/>
    <s:hidden id="spouseName" name="adoption.spouseName"/>
    <s:hidden id="spousePin" name="adoption.spousePINorNIC"/>
    <s:hidden id="jointApplicant" value="%{adoption.jointApplicant}"/>
</s:form>
</div>