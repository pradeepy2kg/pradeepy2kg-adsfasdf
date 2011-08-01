<%-- @author Chathuranga Withana --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
    }
</script>

<div id="death-declaration-form-2-outer">
    <s:form name="nonEditableDeathRegistrationForm2" action="eprDeathApprovalAndPrint.do" method="POST">
        <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
               class="font-9">
            <col width="150px"/>
            <col width="130px"/>
            <col width="120px"/>
            <col width="130px"/>
            <col width="130px"/>
            <col width="130px"/>
            <tbody>
            <tr class="form-sub-title">
                <td colspan="6">
                    දැනුම් දෙන්නාගේ විස්තර
                    <br>அறிவிப்பு கொடுப்பவரின் தகவல்கள்
                    <br>Details of the Informant
                </td>
            </tr>
            <tr>
                <td colspan="3">දැනුම් දෙන්නේ කවරකු වශයෙන්ද / யாரால் தகவல் தரப்படுகின்றது? <br>Capacity for giving
                    information
                </td>
                <td colspan="3">
                    <s:if test="session.deathRegister.declarant.declarantType.ordinal() == 0">
                        <s:label value="%{getText('death.declarant.father.label')}"/>
                    </s:if>
                    <s:elseif test="session.deathRegister.declarant.declarantType.ordinal() == 1">
                        <s:label value="%{getText('death.declarant.mother.label')}"/>
                    </s:elseif>
                    <s:elseif test="session.deathRegister.declarant.declarantType.ordinal() == 2">
                        <s:label value="%{getText('death.declarant.sibling.label')}"/>
                    </s:elseif>
                    <s:elseif test="session.deathRegister.declarant.declarantType.ordinal() == 3">
                        <s:label value="%{getText('death.declarant.descendents.label')}"/>
                    </s:elseif>
                    <s:elseif test="session.deathRegister.declarant.declarantType.ordinal() == 4">
                        <s:label value="%{getText('death.declarant.relative.label')}"/>
                    </s:elseif>
                    <s:elseif test="session.deathRegister.declarant.declarantType.ordinal() == 5">
                        <s:label value="%{getText('death.declarant.other.label')}"/>
                    </s:elseif>
                    <s:elseif test="session.deathRegister.declarant.declarantType.ordinal() == 6">
                        <s:label value="%{getText('death.declarant.spouse.label')}"/>
                    </s:elseif>
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    අනන්‍යතා අංකය / அடையாள எண் / Identification Number
                </td>
                <td colspan="3" class="find-person"><s:label
                        value="%{#session.deathRegister.declarant.declarantNICorPIN}"/></td>
            </tr>
            <tr>
                <td>නම<br>கொடுப்பவரின் பெயர்<br>Name</td>
                <td colspan="5"><s:label value="%{#session.deathRegister.declarant.declarantFullName}"/></td>
            </tr>
            <tr>
                <td>තැපැල් ලිපිනය<br>தபால் முகவரி<br>Postal Address</td>
                <td colspan="5"><s:label value="%{#session.deathRegister.declarant.declarantAddress}"/></td>
            </tr>
            <tr>
                <td>ඇමතුම් විස්තර<br>இலக்க வகை <br>Contact Details</td>
                <td>දුරකතනය<br>தொலைபேசி இலக்கம் <br>Telephone</td>
                <td colspan="2"><s:label value="%{#session.deathRegister.declarant.declarantPhone}"/></td>
                <td>ඉ -තැපැල<br>மின்னஞ்சல்<br>Email</td>
                <td><s:label value="%{#session.deathRegister.declarant.declarantEMail}"/></td>
            </tr>
            <tr>
                <td colspan="2">දැනුම් දෙන්නා අත්සන්කල දිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
                    <br>தகவல் அளித்தவர் கையொப்பமிட்ட திகதி <br>Informant Signed Date
                </td>
                <td colspan="5">
                    <s:label value="%{#session.deathRegister.declarant.declarantSignDate}"/>
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
                <td colspan="4">
                    <s:if test="pageType==0">
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
                    <s:elseif test="pageType == 2">
                        මරණ පරීක්ෂක හෝ අධිකරණ වෛද්‍ය නිලධාරී ගේ විස්තර
                        <br>மரண பரிசோதகர் அல்லது வைத்திய அதிகாரியின் விபரம்
                        <br>Particulars of the Inquirer into deaths or Judicial Medical Officer
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
                <td>
                    අනන්‍යතා අංකය
                    <br>அடையாள எண்
                    <br>Identification Number
                </td>
                <td colspan="3"><s:label
                        value="%{#session.deathRegister.notifyingAuthority.notifyingAuthorityPIN}"/></td>
                </td>
            </tr>
            <tr>
                <td colspan="1">නම<br>கொடுப்பவரின் பெயர்<br>Name</td>
                <td colspan="3"><s:label
                        value="%{#session.deathRegister.notifyingAuthority.notifyingAuthorityName}"/></td>
            </tr>
            <tr>
                <td colspan="1">තැපැල් ලිපිනය<br>தபால் முகவரி<br>Postal Address</td>
                <td colspan="3"><s:label
                        value="%{#session.deathRegister.notifyingAuthority.notifyingAuthorityAddress}"/></td>
            </tr>
            <tr>
                <td>දිනය<br>திகதி<br>Date</td>
                <td colspan="4"><s:label
                        value="%{#session.deathRegister.notifyingAuthority.notifyingAuthoritySignDate}"/></td>
            </tr>
            </tbody>
        </table>

        <div class="form-submit">
            <s:hidden name="pageNo" value="2"/>
            <s:submit value="%{getText('data.table.label')}" cssStyle="margin-top:10px;"/>
        </div>
        <div class="next-previous">
            <s:url id="backUrl" action="eprDeathViewMode.do">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
            </s:url>
            <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
        </div>
    </s:form>
</div>