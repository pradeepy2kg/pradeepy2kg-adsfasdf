<%--@author Indunil Moremada--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
        disableButton(true);
    }

    function disableButton(mode) {
        if (mode) {
            document.getElementById('approve').style.display = 'none';
        }
        else {
            document.getElementById('approve').style.display = 'block';
        }
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

<div id="birth-declaration-approval-warning-outer">
    <fieldset>
        <legend><b><s:label value="%{getText('approvalIgnorWarning.label')}"/></b></legend>
        <table class="birth-declaration-approval-warning-table">
            <s:iterator value="#request.warnings">
                <tr>
                    <td><s:property value="message"/></td>
                </tr>
            </s:iterator>
        </table>
        <s:form action="eprConfirmationIgnoreWarning" name="birthConfirmationApprovalWarningForm">
        <table align="center" border="0">
            <tr>
                <s:hidden name="pageNo" value="%{#request.pageNo}"/>
                <s:hidden name="birthDistrictId" value="%{#request.birthDistrictId}"/>
                <s:hidden name="birthDivisionId" value="%{#request.birthDivisionId}"/>
                <s:hidden name="recordCounter" value="%{#request.recordCounter}"/>
                <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
                <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
                <s:hidden name="dsDivisionId" value="%{#request.dsDivisionId}"/>
                <s:hidden value="%{#request.bdId}" name="bdId"/>
                <s:hidden name="confirmationApprovalFlag" value="true"/>
            </tr>
            <tr>
                <td colspan="3"><br></td>
            </tr>
            <tr>
                <td><s:label value="%{getText('ignoreWorning.label')}" name="ignoreWarning"/></td>
                <td><s:checkbox id="ignoreWarning" name="ignoreWarning" onclick="javascript:selectCheckbox()"/></td>
                <td>
                </td>
            </tr>
        </table>
    </fieldset>
    <div class="form-submit" style="margin-right:400px;">
        <s:submit id="approve" name="approve" value="%{getText('approve.label')}"/>
    </div>
    </s:form>
</div>
</div>