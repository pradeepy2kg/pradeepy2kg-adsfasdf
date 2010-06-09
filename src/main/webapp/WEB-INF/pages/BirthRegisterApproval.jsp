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
        <%--todo edit has to be implemented--%>
       <%-- <s:if test="request.warning !=null">
            <s:iterator value="#request.warning">
            <s:property value="message"/>
            </s:iterator>
        </s:if>--%>
        <s:form action="eprApproveAllSelected" name="birth_register_approval_body" method="POST">
            <table>
                <tr>
                    <th></th>
                    <th></th>
                    <th><s:label name="serial" value="%{getText('serial.label')}"/></th>
                    <th><s:label name="name" value="%{getText('name.label')}"/></th>
                    <th><s:label name="received" value="%{getText('received.label')}"/></th>
                    <th><s:label name="edit" value="%{getText('edit.label')}"/></th>
                    <th><s:label name="approve" value="%{getText('approve.label')}"/></th>
                    <th><s:label name="reject" value="%{getText('reject.label')}"/></th>
                    <th><s:label name="delete" value="%{getText('delete.label')}"/></th>
                </tr>
                    <%--following code used for pagination--%>
                <s:if test="#session.ApprovalStart == null">
                    <s:set name="ApprovalStart" value="0" scope="session"/>
                </s:if>
                <s:iterator status="approvalStatus" value="#session.ApprovalData" id="approvalList">
                    <tr>
                        <td><s:property value="%{#approvalStatus.count + #session.ApprovalStart}"/></td>
                        <td><s:checkbox name="index"
                                        onclick="javascript:selectall(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"
                                        title="%{getText('select.label')}" value="%{#index}"
                                        fieldValue="%{#approvalList.idUKey}"/></td>
                        <td><s:property value="register.bdfSerialNo"/></td>
                        <td><s:property value="child.childFullNameOfficialLang"/></td>
                        <td><s:property value="confirmant.confirmationReceiveDate"/></td>
                        <s:url id="editSelected" action="eprBirthConfirmation.do">
                          <s:param name="bdId" value="idUKey"/>
                        </s:url>
                        <td align="center"><s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                            <img src="<s:url value='/images/edit.jpg'/>" width="25" height="25" border="none"/></s:a>
                        </td>
                        <s:url id="approveSelected" action="eprApproveBirthDeclaration.do">
                            <s:param name="bdId" value="idUKey"/>
                        </s:url>
                        <td align="center"><s:a href="%{approveSelected}" title="%{getText('approveTooltip.label')}">
                            <img src="<s:url value='/images/approve.png'/>" width="25" height="25" border="none"/></s:a>
                        </td>
                        <s:url id="rejectSelected" action="">
                            <s:param name="bdId" value="idUKey"/>
                        </s:url>
                        <td align="center"><s:a href="%{rejectSelected}"
                                                title="%{getText('rejectTooltip.label')}"><img
                                src="<s:url value='/images/reject.png'/>" width="25" height="25" border="none"/></s:a>
                        </td>
                        <s:url id="deleteSelected" action="eprDeleteApprovalPending.do">
                            <s:param name="bdId" value="idUKey"/>
                        </s:url>
                        <td align="center"><s:a href="%{deleteSelected}"
                                                title="%{getText('deleteToolTip.label')}"><img
                                src="<s:url value='/images/delete.png'/>" width="25" height="25" border="none"/></s:a>
                        </td>
                    </tr>
                    <%--select_all checkbox is visible only if
                counter is greater than one--%>
                    <s:set name="counter" scope="request" value="#approvalStatus.count"/>
                </s:iterator>
                <tr></tr>
            </table>
            <br/>
            <s:if test="#request.counter>1">
                <s:label><s:checkbox
                        name="allCheck"
                        onclick="javascript:selectallMe(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"/>
                    <span><s:label name="select_all" value="%{getText('select_all.label')}"/></span></s:label>
                <s:submit name="approveSelected" value="%{getText('approveSelected.label')}"/>
            </s:if>
            <br/>
            <%-- Next link to visible next records will only visible if nextFlag is
          set to 1--%>
            <s:url id="previousUrl" action="eprApprovalPrevious.do"/>
            <s:url id="nextUrl" action="eprApprovalNext.do"/>

            <br/><br/>
            <s:if test="#session.previousFlag==1"><s:a href="%{previousUrl}">
                <s:label value="%{getText('previous.label')}"/></s:a></s:if>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <s:if test="#session.nextFlag==1"><s:a href="%{nextUrl}">
                <s:label value="%{getText('next.label')}"/></s:a></s:if>
        </s:form>

    </div>
    <div id="birth-register-approval-footer">
    </div>
</div>