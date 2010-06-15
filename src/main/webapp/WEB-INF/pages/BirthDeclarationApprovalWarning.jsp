<%--@author Indunil Moremada--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="bdaw-outer">
    <div id="bdaw-content">
        <s:iterator value="#request.warnings">
            <s:property value="message"/>
        </s:iterator>
    </div>
    <div id="bdaw-action">
        <s:form action="eprIgnoreWarning" name="birthDeclarationApprovalWarningForm">
            <s:hidden value="%{#request.bdId}" name="bdId"/>
            <table align="center" border="0">
                <tr>
                    <td><s:label value="%{getText('ignoreWorning.label')}" name="ignoreWorning"/></td>
                    <td><s:checkbox name="ignoreWarning"/></td>
                </tr>
                <%--<tr>
                    <td><s:label value="%{getText('comment.label')}" name="comment"/></td>
                    <td><s:textarea name="comments"/> </td>
                </tr>--%>
                <tr>
                    <td><s:submit name="approve" value="%{getText('approve.label')}"/></td>
                </tr>
            </table>
        </s:form>
    </div>
</div>