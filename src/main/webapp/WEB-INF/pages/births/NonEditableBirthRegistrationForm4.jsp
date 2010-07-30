<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-registration-form-outer" id="birth-registration-form-4-outer">
    <s:form action="eprSearchPageLoad.do" name="nonEditableBirthRegistrationForm4" method="POST">

        <table class="table_reg_page_04" width="100%" cellspacing="0">
            <caption></caption>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td colspan="5" style="text-align:center;font-size:12pt"> තොරතුරු වාර්තා කරන පාර්ශවය
                    <br>அதிகாரியிடம் தெரிவித்தல்
                    <br>Notifying Authority
                </td>
            </tr>
            <tr>
                <td colspan="3"><label>(33) පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br>தகவல் கொடுப்பவரின் தனிநபர்
                    அடையாள எண் / அடையாள
                    அட்டை இல.<br>PIN / NIC of the Notifying Authority</label></td>
                <td colspan="2" class="find-person" width="200px">
                    <s:label value="%{#session.birthRegister.notifyingAuthority.notifyingAuthorityPIN}"/>
                </td>
            </tr>
            </tbody>
        </table>
        <table class="table_reg_page_04" width="100%" cellspacing="0" style="border-top:none;">
            <tbody>
            <tr>
                <td width="200px"><label>(34) නම<br>கொடுப்பவரின் பெயர் <br>Name</label></td>
                <td colspan="4">
                    <s:label value="%{#session.birthRegister.notifyingAuthority.notifyingAuthorityName}"
                             cssStyle="width:98%;"/>
                </td>
            </tr>
            <tr>
                <td width="200px"><label>තැපැල් ලිපිනය<br>தபால் முகவரி <br>Postal Address</label></td>
                <td colspan="4"><s:label value="%{#session.birthRegister.notifyingAuthority.notifyingAuthorityAddress}"
                                         cssStyle="width:98%;"/></td>
            </tr>
            </tbody>
        </table>
        <table class="table_reg_page_04" width="100%" cellspacing="0" style="border-top:none;">
            <tbody>
            <tr>
                <td width="200px"><label>දිනය <br>*in tamil <br>Date</label></td>
                <td colspan="4"><s:label
                        value="%{#session.birthRegister.notifyingAuthority.notifyingAuthoritySignDate}"/></td>
            </tr>
            </tbody>
        </table>

        <s:hidden name="pageNo" value="4"/>

        <s:if test="session.birthRegister.register.liveBirth">
            <s:if test="bdfLateOrBelated ==1 || bdfLateOrBelated==2">
                <div id="late-belated-registration" class="font-9">
                    <div id="late-belated-registration-title" class="font-12">
                        <s:if test="bdfLateOrBelated==1">Late Registration</s:if>
                        <s:else>Belated Registration</s:else>
                    </div>
                    <div id="late-belated-case-file-num">
                        <label>*in sinhala<br>*in tamil<br>Case File Number</label>
                        <s:label name="caseFileNumber"/>
                    </div>
                    <div id="late-belated-prev-comments">
                        <label>*in sinhala<br>* in tamil<br>Comments </label>
                        <s:label name="%{#session.birthRegister.register.comments}" />
                    </div>
                </div>
            </s:if>
        </s:if>

        <div class="form-submit">
            <s:submit value="%{getText('next.label')}"/>
        </div>
        <div class="next-previous">
            <s:url id="backUrl" action="eprViewBDFInNonEditableMode">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
            </s:url>
            <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
        </div>
    </s:form>
</div>