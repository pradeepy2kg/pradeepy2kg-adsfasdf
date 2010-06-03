<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<div id="birth-register-approval">
    <s:form action="eprApprovalRefresh" name="birth_register_approval_header">
        <div id="birth-register-approval-header">
            <s:label><span><s:label name="district" value="%{getText('district.label')}"/></span><s:select
                    list="districtList" name="district"/></s:label>
            <s:label><span><s:label name="division" value="%{getText('division.label')}"/></span><s:select
                    list="divisionList"
                    name="division" headerKey="0"/></s:label>
            <s:submit name="refresh" value="%{getText('refresh.label')}"/>
        </div>
    </s:form>
    <div id="birth-register-approval-body">
        <%--todo following should be re-directed to page 3 of 3 Birth Conformation--%>
        <%--todo edit has to be implemented--%>
        <s:form action="eprBirthConfirmation" name="birth_register_approval_body" method="POST">
            <table>
                <tr>
                    <th></th>
                    <th></th>
                    <th><s:label name="serial" value="%{getText('serial.label')}"/></th>
                    <th><s:label name="name" value="%{getText('name.label')}"/></th>
                    <th><s:label name="received" value="%{getText('received.label')}"/></th>
                    <th><s:label name="actions" value="%{getText('actions.label')}"/></th>
                </tr>
                    <%-- Next link to visible next records will only visible if nextFlag is
                     set to 1--%>
                <s:iterator status="approvalStatus" value="#session.ApprovalData">
                    <tr>
                        <td><s:property value="%{#approvalStatus.count}"/></td>
                        <td><s:checkbox name="index"
                                        onclick="javascript:selectall(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"/></td>
                        <td><s:property value="child.bdfSerialNo"/></td>
                        <td><s:property value="child.childFullNameOfficialLang"/></td>
                        <td><s:property value="confirmant.confirmationReceiveDate"/></td>
                        <s:url id="approveSelected" action="eprBirthConfirmation" value="">
                            <s:param name="Serial" value="child.bdfSerialNo"/>
                        </s:url>
                        <td><s:a href="%{approveSelected}"><img
                                src="<s:url value='/images/approve.png'/>" alt=""/></s:a></td>
                        <s:url id="editSelected" action="eprApproveSelected.do">
                            <s:param name="bdId" value="child.bdfSerialNo"/>
                        </s:url>
                        <td><s:a href="%{editSelected}">
                            <s:label value="%{getText('edit.label')}"/></s:a></td>
                    </tr>
                    <%--select_all checkbox is visible only if
                counter is greater than one--%>
                    <s:set name="counter" scope="request" value="#approvalStatus.count"/>
                </s:iterator>
                <tr></tr>
            </table>
            <br/>
            <s:if test="#request.counter>1">
                <s:label><span><s:label name="select_all" value="%{getText('select_all.label')}"/></span><s:checkbox
                        name="allCheck"
                        onclick="javascript:selectallMe(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"/></s:label>
                <s:submit name="approveSelected" value="%{getText('approveSelected.label')}"/>
            </s:if>
            <br/>
            <s:url id="previousUrl" action="eprApprovalPrevious.do"/>
            <s:url id="nextUrl" action="eprApprovalNext.do"/>

            <br/><br/>
            <s:if test="#session.previousFlag==1"><s:a href="%{previousUrl}">
                <s:label value="<Previous"/></s:a></s:if>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <s:if test="#session.nextFlag==1"><s:a href="%{nextUrl}">
                <s:label value="Next>"/></s:a></s:if>
        </s:form>

    </div>
    <div id="birth-register-approval-footer">
    </div>
</div>