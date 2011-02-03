<%--
  @author Indunil Moremada
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage() {
        disableButton(true);
    }

    function disableButton(mode) {
        document.getElementById('approve').disabled = mode;
    }

    function selectCheckbox() {
        var ok = document.getElementById('ignoreWarning');
        if (ok.checked) {
            disableButton(false)
        } else {
            disableButton(true)
        }
    }
</script>

<s:if test="#request.warnings.size>0">

    <div id="bdaw-action">
        <s:form action="ConfirmationDirectApprovalIngoreWarning">
            <fieldset>
                <legend><b><s:label value="%{getText('directApprovalWarning.label')}"/></b></legend>
                <table class="birth-registration-form-details-warning-table">
                    <s:iterator value="#request.warnings">
                        <tr>
                            <td><s:property value="message"/></td>
                        </tr>
                    </s:iterator>
                </table>
                <table align="center" border="0">
                    <col width="350px;"/>
                    <col width="20px;"/>
                    <col width="130px;"/>
                    <tr>
                        <td colspan="3"><br></td>
                    </tr>
                    <tr>
                        <td height="60px;">
                            <s:label value="%{getText('ignoreWorning.label')}" cssStyle="float:right;"/>
                        </td>
                        <td>
                            <s:checkbox id="ignoreWarning" name="ignoreWarning" onclick="javascript:selectCheckbox()"/>
                        </td>
                        <td>
                            <div class="form-submit">
                                <s:submit id="approve" name="approve" value="%{getText('approve.label')}"/>
                            </div>
                        </td>
                        <s:hidden value="%{#request.bdId}" name="bdId"/>
                        <s:hidden value="true" name="directApprovalFlag"/>
                        <s:hidden value="true" name="confirmationApprovalFlag"/>
                    </tr>
                </table>
            </fieldset>
        </s:form>
    </div>
</s:if>
<br/><br/>
<%--todo certificate printstuff and warning ignoring has to be checked--%>

<div id="birth-register-approval">
    <div id="birth-register-approval-header">
        <s:actionmessage/>
        <s:actionerror cssStyle="color:red;font-size:10pt"/>
    </div>

    <div id="birth-register-approval-footer">
        <s:url id="approveConfirmation" action="eprConfrimationChangesDirectApproval.do">
            <s:param name="confirmationApprovalFlag" value="true"/>
            <s:param name="bdId" value="#request.bdId"/>
        </s:url>

        <s:url id="printBirthCertificate" action="eprBirthCertificatDirectPrint.do">
            <s:param name="bdId" value="#request.bdId"/>
            <s:param name="directPrint" value="true"/>
        </s:url>
        <s:url id="mainUrl" action="eprBirthRegistrationHome.do"/>
        <table align="center">
            <tr>
                <td><s:a href="%{mainUrl}"><s:label value="%{getText('goToMain_link.label')}"/></s:a></td>
                <td>
                    <s:if test="#request.allowApproveBDF">
                        <s:if test="approved">
                            <s:a href="%{printBirthCertificate}">
                                <s:label value="%{getText('printBirthCertificate_link.label')}"/></s:a>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                        </s:if>
                        <s:else>
                            <s:a href="%{approveConfirmation}"><s:label value="%{getText('approve_link.label')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
                        </s:else>
                    </s:if>
                    <s:if test="skipConfirmationChages">
                        <%--<s:if test="#request.allowPrintCertificate">--%>
                        <s:a href="%{printBirthCertificate}">
                            <s:label value="%{getText('printBirthCertificate_link.label')}"/></s:a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <%--</s:if>--%>
                    </s:if>
                </td>
            </tr>
        </table>
    </div>
</div>  