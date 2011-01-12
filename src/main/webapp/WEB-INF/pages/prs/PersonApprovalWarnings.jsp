<%-- @author Chathuranga Withana --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
    }
</script>

<s:if test="direct">
    <s:url id="approve" action="eprDirectApprovePerson" namespace="../prs"/>
</s:if>
<s:else>
    <s:url id="approve" action="eprApprovePerson" namespace="../prs"/>
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
            <td><s:label value="%{getText('ignoreWarning.label')}" name="ignoreWarning"/></td>
            <td><s:checkbox name="ignoreWarning"/></td>
            <td>
                <div class="form-submit"><s:submit name="approve" value="%{getText('approve_link.label')}"/></div>
            </td>
        </tr>
        </s:form>
    </table>
</fieldset>
