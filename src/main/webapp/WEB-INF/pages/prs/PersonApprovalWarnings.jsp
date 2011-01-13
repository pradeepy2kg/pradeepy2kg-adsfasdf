<%-- @author Chathuranga Withana --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
    }
</script>

<s:if test="direct">
<s:url id="approve" action="eprDirectApprovePerson" namespace="../prs"/>
<s:url id="previous" action="eprBackToRegisterDetails.do" namespace="../prs"/>
</s:if>
<s:else>
<s:url id="approve" action="eprApprovePerson" namespace="../prs"/>
<s:url id="previous" action="eprBackPRSSearchList.do" namespace="../prs">
<s:param name="pageNo" value="%{#request.pageNo}"/>
<s:param name="locationId" value="%{#request.locationId}"/>
<s:param name="printStart" value="%{#request.printStart}"/>
</s:url>
</s:else>

<fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
    <legend><b><s:label value="%{getText('message.personWarning.label')}"/></b></legend>
    <table class="birth-declaration-approval-warning-table" style="margin-left:10px;margin-top:10px;">
        <s:iterator value="#request.warnings">
        <tr>
            <td><s:property value="message"/></td>
        </tr>
        </s:iterator>
    </table>
    <s:form action="%{approve}" method="post">
    <table align="center" border="0">
        <tr>
            <s:hidden name="personUKey" value="%{#request.personUKey}"/>
            <s:hidden name="pageNo" value="%{#request.pageNo}"/>
            <s:hidden name="locationId" value="%{#request.locationId}"/>
            <s:hidden name="printStart" value="%{#request.printStart}"/>
        </tr>
        <tr>
            <td><br><s:label value="%{getText('ignoreWarning.label')}" name="ignoreWarning"/></td>
            <td><br><s:checkbox name="ignoreWarning"/></td>
            <td>
                <%--<div class="form-submit"><s:submit name="approve" value="%{getText('approve_link.label')}"/></div>--%>
            </td>
        </tr>
    </table>
</fieldset>

<div class="form-submit" style="margin-right:400px;"><s:submit
name="approve" value="%{getText('approve_link.label')}"/></div>
</s:form>
<s:form action="%{previous}" method="get">
<s:hidden name="personUKey" value="%{#request.personUKey}"/>
<div class="form-submit" style="float:right;">
    <s:submit value="%{getText('previous.label')}"/>
</div>
</s:form>
