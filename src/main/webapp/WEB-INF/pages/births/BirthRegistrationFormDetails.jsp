<%--
  @author Chathuranga Withana
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage() {
        if (document.getElementById('approveButtonId') != null) {
            document.getElementById('approveButtonId').disabled = true;
        }
    }
    function enableApproval() {
        var clicked = document.getElementById('ignoreWarningsId').checked
        if (clicked) {
            document.getElementById('approveButtonId').disabled = false;
        }
        else {
            document.getElementById('approveButtonId').disabled = true;
        }
    }
</script>

<div id="birth-register-approval">
    <div id="birth-register-approval-header">
        <s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
        <s:actionerror cssStyle="color:red;font-size:10pt"/>
    </div>

    <div id="birth-register-approval-body">
        <s:url id="newBDFUrl" action="eprBirthRegistrationInit.do">
            <s:param name="addNewMode" value="true"/>
            <s:param name="oldBdId" value="#request.bdId"/>
        </s:url>
        <s:url id="approveUrl" action="eprDirectApprove.do">
            <s:param name="bdId" value="#request.bdId"/>
        </s:url>
        <%--TODO shoud be redirected to confirmationPrinting... aproveAnd print--%>
        <s:url id="printBirthConfirmation" action="eprBirthConfirmationPrintPage.do">
            <s:param name="bdId" value="#request.bdId"/>
            <s:param name="directPrint" value="true"/>
        </s:url>
        <%--TODO--%>
        <s:url id="printStillBirthCertificate" action="eprStillBirthCertificatePrint.do">
            <s:param name="bdId" value="#request.bdId"/>
            <s:param name="directPrint" value="true"/>
        </s:url>
        <s:url id="mainUrl" action="eprBirthRegistrationHome.do"/>
        <s:url id="rejectSelected" action="eprRejectBirthDeclaration.do">
            <s:param name="bdId" value="#request.bdId"/>
            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
            <s:param name="pageNo" value="%{#request.pageNo}"/>
            <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
            <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
            <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
            <s:param name="recordCounter" value="#request.recordCounter"/>
            <s:param name="reject" value="true"/>
        </s:url>
        <s:url id="birthRegisterApprovalUrl" action="eprBirthRegisterApproval.do"/>

        <s:if test="#request.warnings.size>0">

            <div id="bdaw-action">
                <s:form action="eprDirectApproveIgnoreWarning">
                    <fieldset>
                        <legend><b><s:label value="%{getText('directApprovalWarning.label')}"/></b></legend>
                        <table class="birth-declaration-approval-warning-table">
                            <s:iterator value="#request.warnings">
                                <tr>
                                    <td><s:property value="message"/></td>
                                </tr>
                            </s:iterator>
                        </table>
                        <table align="center">
                            <tr>
                                <td><s:label value="%{getText('ignoreWorning.label')}" name="ignoreWorning"/></td>
                                <td><s:checkbox name="ignoreWarning" id="ignoreWarningsId"
                                                onclick="enableApproval()"/></td>
                                <s:hidden value="%{#request.bdId}" name="bdId"/>
                                <s:hidden value="true" name="directApprovalFlag"/>
                                <td class="button" align="left">
                                    <s:submit name="approve" id="approveButtonId" value="%{getText('approve.label')}"/>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </s:form>
            </div>
        </s:if>

        <br/><br/>

    </div>
    <div id="birth-register-approval-footer">
        <s:a href="%{newBDFUrl}"><s:label value="%{getText('addNewBDF_link.label')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
        <s:if test="#request.allowApproveBDF">
            <s:if test="approved">
                <s:if test="#request.birthType.ordinal() != 0">
                    <s:a href="%{printBirthConfirmation}">
                        <s:label value="%{getText('printConfirmation_link.label')}"/></s:a> &nbsp;&nbsp;&nbsp;&nbsp;
                </s:if>
                <s:else>
                    <s:a href="%{printStillBirthCertificate}"><s:label
                            value="%{getText('printStillBirthCert.label')}"/></s:a>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                </s:else>
            </s:if>
            <s:else>
                <s:if test="#request.warnings==null">
                    <s:a href="%{approveUrl}"><s:label value="%{getText('approve_link.label')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
                </s:if>
            </s:else>
        </s:if>
        <s:if test="#request.allowApproveBDF">
            <s:if test="register.birthType.ordinal() != 3">
                <s:a href="%{rejectSelected}" title="%{getText('reject.label')}"><s:label value="%{getText('reject.label')}"/></s:a>
            </s:if>
        </s:if>
        <%--<s:a href="%{mainUrl}"><s:label value="%{getText('goToMain_link.label')}"/></s:a>--%>
        <s:a href="%{birthRegisterApprovalUrl}"><s:label value="%{getText('goToMain_link.label')}"/></s:a>
    </div>
</div>

