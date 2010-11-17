<%--@author Indunil Moremada--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage(){}
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
        <s:form action="eprIgnoreWarning" name="birthDeclarationApprovalWarningForm">
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
            </tr>
            <tr>
                <td><s:label value="%{getText('ignoreWorning.label')}" name="ignoreWarning"/></td>
                <td><s:checkbox name="ignoreWarning"/></td>
                <td>
                    <div class="form-submit"><s:submit name="approve" value="%{getText('approve.label')}"/></div>
                </td>
            </tr>
                <%--<tr>
                    <td><s:label value="%{getText('comment.label')}" name="comment"/></td>
                    <td><s:textarea name="comments"/> </td>
                </tr>--%>
        </table>
    </fieldset>
    </s:form>
</div>