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
        <s:form action="eprConfirmationIgnoreWarning" name="birthConfirmationApprovalWarningForm">
            <fieldset>
                <legend><s:label
                        value="%{getText('approvalIgnorWarning.label')}"/></legend>
                <table align="center" border="0">
                    <tr>
                        <td><s:label value="%{getText('ignoreWorning.label')}" name="ignoreWarning"/></td>
                        <td><s:checkbox name="ignoreWarning"/></td>
                    </tr>
                    <s:hidden name="pageNo" value="%{#request.pageNo}"/>
                    <s:hidden name="birthDistrictId" value="%{#request.birthDistrictId}"/>
                    <s:hidden name="birthDivisionId" value="%{#request.birthDivisionId}"/>
                    <s:hidden name="recordCounter" value="%{#request.recordCounter}"/>
                    <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
                    <s:hidden name="dsDivisionId" value="%{#request.dsDivisionId}"/>
                    <s:hidden value="%{#request.bdId}" name="bdId"/>
                    <s:hidden name="confirmationApprovalFlag" value="true"/>
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