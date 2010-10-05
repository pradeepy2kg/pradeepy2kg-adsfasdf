<%-- @author Chathuranga Withana --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

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
            <col/>
            <tbody>
            <tr class="form-sub-title">
                <td colspan="7">ප්‍රකාශකයාගේ විස්තර<br>அறிவிப்பு கொடுப்பவரின் தகவல்கள்<br>Details of the Declarant</td>
            </tr>
            <tr>
                <td colspan="4">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>தகவநபர் அடையாள எண் / அடையாள அட்டை இல.
                    <br>PIN / NIC
                </td>
                <td colspan="3" class="find-person"><s:label
                        value="%{#session.deathRegister.declarant.declarantNICorPIN}"/></td>
            </tr>
            <tr>
                <td colspan="1">නම<br>கொடுப்பவரின் பெயர்<br>Name</td>
                <td colspan="6"><s:label value="%{#session.deathRegister.declarant.declarantFullName}"/></td>
            </tr>
            <tr>
                <td colspan="1">තැපැල් ලිපිනය<br>தபால் முகவரி<br>Postal Address</td>
                <td colspan="6"><s:label value="%{#session.deathRegister.declarant.declarantAddress}"/></td>
            </tr>
            <tr>
                <td colspan="1">ඇමතුම් විස්තර<br>இலக்க வகை <br>Contact Details</td>
                <td colspan="1">දුරකතනය<br>தொலைபேசி இலக்கம் <br>Telephone</td>
                <td colspan="2"><s:label value="%{#session.deathRegister.declarant.declarantPhone}"/></td>
                <td colspan="1">ඉ -තැපැල<br>மின்னஞ்சல்<br>Email</td>
                <td colspan="2"><s:label value="%{#session.deathRegister.declarant.declarantEMail}"/></td>
            </tr>
                <%--TODO--%>
            <tr>
                <td colspan="3">දැනුම් දෙන්නේ කවරකු වශයෙන්ද<br>*in tamil<br>Capacity for giving information
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
                    <s:if test="#session.deathRegister.deathType.ordinal() == 0 || #session.deathRegister.deathType.ordinal() == 1">
                    තොරතුරු වාර්තා කරන පාර්ශවය<br>அதிகாரியிடம் தெரிவித்தல்<br>Notifying Authority
                </td>
                </s:if>
                <s:elseif test="#session.deathRegister.deathType.ordinal() == 2 || #session.deathRegister.deathType.ordinal() == 3">
                    දිස්ත්‍රික් රෙජිස්ට්‍රාර් / රෙජිස්ට්‍රාර් ජෙනරාල් <br/>
                    அதிகாரியிடம் தெரிவித்தல் <br/>
                    District Registrar / Registrar General
                </s:elseif>
            </tr>
            <tr>
                <td colspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>அடையாள எண் / அடையாள அட்டை இல. <br>PIN /
                    NIC
                </td>
                <td colspan="2"><s:label
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
                <td colspan="1">අත්සන හා නිල මුද්‍රාව<br>தகவல் ...<br>Signature and Official Seal of the Notifying
                    Authority
                </td>
                <td colspan="1"></td>
                <td colspan="1">දිනය<br>திகதி<br>Date</td>
                <td colspan="1"><s:label
                        value="%{#session.deathRegister.notifyingAuthority.notifyingAuthoritySignDate}"/></td>
            </tr>
            </tbody>
        </table>


        <s:label><p class="font-8">පු.අ.අ. / ජා.හැ.අ. = පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය</p></s:label>

        <div class="form-submit">
            <s:hidden name="pageNo" value="2"/>
            <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
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