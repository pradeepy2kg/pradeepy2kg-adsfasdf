<%--@author Indunil Moremada--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div>
    <table>
        <tr>
            <th><s:label name="serial" value="%{getText('serial.label')}"/></th>
            <th><s:label name="name" value="%{getText('name.label')}"/></th>
            <th><s:label name="received" value="%{getText('received.label')}"/></th>
        </tr>
        <tr>
            <td><s:label value="%{bdf.register.bdfSerialNo}"/></td>
            <td><s:label value="%{bdf.child.getChildFullNameOfficialLangToLength(50)}"/></td>
            <s:if test="#request.confirmationApprovalFlag == true">
                <td><s:label value="%{bdf.confirmant.confirmationReceiveDate}"/></td>
            </s:if><s:else>
            <td><s:label value="%{bdf.register.dateOfRegistration}"/></td>
        </s:else>
        </tr>

    </table>
</div>
<br/>

<s:actionerror/>
<div>
    <s:form action="eprRejectBirthDeclaration.do" method="post">
        <fieldset>
            <legend><s:label value="%{getText('rejectLegend.label')}"/></legend>
            <s:hidden name="bdId" value="%{#request.bdId}"/>
            <s:hidden name="pageNo" value="%{#request.pageNo}"/>
            <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
            <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
            <s:hidden name="birthDistrictId" value="%{#request.birthDistrictId}"/>
            <s:hidden name="birthDivisionId" value="%{#request.birthDivisionId}"/>
            <s:hidden name="recordCounter" value="%{#request.recordCounter}"/>
            <s:hidden name="confirmationApprovalFlag" value="%{#request.confirmationApprovalFlag}"/>
            <table>
                <tr>
                    <td><s:label name="comment" value="%{getText('comment.label')}"/></td>
                    <td><s:textarea name="comments" rows="4" cols="35"/></td>
                </tr>
                <tr>
                    <td><s:submit name="reject" value="%{getText('reject.label')}"/></td>
                </tr>
            </table>
        </fieldset>
    </s:form>


</div>
