<%--@author Indunil Moremada--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="bdaw-outer">
    <div id="bdaw-content">
        <table>
            <s:iterator value="#request.warnings">
                <tr>
                    <td><s:property value="message"/></td>
                </tr>
            </s:iterator>
        </table>
    </div>
    <div id="bdaw-action">
        <s:form action="eprIgnoreWarning" name="birthDeclarationApprovalWarningForm">
            <fieldset>
            <s:hidden value="%{#request.bdId}" name="bdId"/>
            <table align="center" border="0">
                <tr>
                    <td><s:label value="%{getText('ignoreWorning.label')}" name="ignoreWorning"/></td>
                    <td><s:checkbox name="ignoreWarning"/></td>
                </tr>
                <s:hidden name="pageNo" value="%{#request.pageNo}"/>
                <s:hidden name="district" value="%{#request.district}"/>
                <s:hidden name="division" value="%{#request.division}"/>
                <s:hidden name="recordCounter" value="%{#request.recordCounter}"/>
                <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
                <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
                <s:hidden name="confirmationApprovalFlag" value="%{#request.confirmationApprovalFlag}"/>
                    <%--<tr>
                        <td><s:label value="%{getText('comment.label')}" name="comment"/></td>
                        <td><s:textarea name="comments"/> </td>
                    </tr>--%>
                <tr>
                    <td><s:submit name="approve" value="%{getText('approve.label')}"/></td>
                </tr>
            </table>
                </fieldset>
        </s:form>
    </div>
</div>