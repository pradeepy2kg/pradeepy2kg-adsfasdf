<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<div id="birth-confirm-approval">
    <s:form action="eprConfirmationApprovalRefresh" name="birth_register_approval_header">
        <div id="birth-confirm-approval-header">
            <s:label><span><s:label name="district" value="%{getText('district.label')}"/></span>
                <s:select list="districtList" name="district"/></s:label>
            <s:label><span><s:label name="division" value="%{getText('division.label')}"/></span>
                <s:select list="divisionList" name="division"/></s:label>
            <s:hidden name="confirmationApprovalFlag" value="true"/>
            <s:label value="%{getText('date.from.label')}"/><sx:datetimepicker name="searchStartDate"
                                                                               displayFormat="yyyy-MM-dd"/>&nbsp;
            <s:label value="%{getText('date.to.label')}"/><sx:datetimepicker name="searchEndDate"
                                                                             displayFormat="yyyy-MM-dd"/>&nbsp;
            <s:submit name="refresh" value="%{getText('refresh.label')}"/>
            <br><br><s:label value="%{getText('serial.label')}"/><s:textfield value="" name="bdId"/>
        </div>
        <br>
    </s:form>
    <s:actionerror/>
    <s:if test="#request.warnings != null">
        <div id="birth-confirm-approval-message" class="font-9" align="center">
            <table width="100%" cellpadding="0" cellspacing="0">
                <s:iterator value="#request.warnings">
                    <tr>
                        <td><s:property value="message"/></td>
                    </tr>
                </s:iterator>
            </table>
        </div>
    </s:if>
    <div id="birth-confirm-approval-body">
        <s:form action="eprApproveConfirmationBulk" name="birth_register_approval_body" method="post">
            <s:if test="approvalPendingList.size>0">
                <table id="confirm-list-table" width="100%" cellpadding="0" cellspacing="0">
                <tr class="table-title">
                    <th></th>
                    <th width="30px"></th>
                    <th width="100px"><s:label name="serial" value="%{getText('serial.label')}"/></th>
                    <th><s:label name="name" value="%{getText('name.label')}"/></th>
                    <th><s:label name="received" value="%{getText('received.label')}"/></th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
            </s:if>
            <s:iterator status="approvalStatus" value="approvalPendingList" id="approvalList">
                <tr class="<s:if test="#approvalStatus.odd == true">odd</s:if><s:else>even</s:else>">
                    <td class="table-row-index"><s:property value="%{#approvalStatus.count + recordCounter}"/></td>
                    <td><s:if test="register.getStatus().toString() == 'CONFIRMATION_CHANGES_CAPTURED'"><s:checkbox
                            name="index"
                            onclick="javascript:selectall(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"
                            title="%{getText('select.label')}" value="%{#index}"
                            fieldValue="%{#approvalList.idUKey}"/></s:if></td>
                    <td><s:property value="register.bdfSerialNo"/></td>
                    <td><s:property value="%{child.getChildFullNameOfficialLangToLength(50)}"/></td>
                    <td><s:property value="confirmant.confirmationReceiveDate"/></td>
                    <td>
                        <s:if test="#request.allowEditBDF">
                            <s:url id="editSelected" action="eprBirthConfirmation.do">
                                <s:param name="bdId" value="idUKey"/>
                            </s:url>

                            <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                                <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                     border="none"/></s:a>
                        </s:if>
                    </td>
                    <td>
                        <s:if test="#request.allowApproveBDFConfirmation">
                            <s:if test="register.getStatus().toString() == 'CONFIRMATION_CHANGES_CAPTURED'">
                                <s:url id="approveSelected" action="eprApproveBirthConfirmation.do">
                                    <s:param name="confirmationApprovalFlag" value="true"/>
                                    <s:param name="bdId" value="idUKey"/>
                                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                                    <s:param name="district" value="#request.district"/>
                                    <s:param name="division" value="#request.division"/>
                                    <s:param name="recordCounter" value="#request.recordCounter"/>
                                </s:url><s:a href="%{approveSelected}"
                                             title="%{getText('approveTooltip.label')}">
                                <img src="<s:url value='/images/approve.png'/>" width="25" height="25"
                                     border="none"/></s:a>
                            </s:if> </s:if>
                    </td>
                    <td>
                        <s:if test="#request.allowApproveBDFConfirmation">
                        <s:if test="register.getStatus().toString() == 'CONFIRMATION_CHANGES_CAPTURED'">
                        <s:url id="rejectSelected" action="eprRejectBirthConfirmation.do">
                            <s:param name="confirmationApprovalFlag" value="true"/>
                            <s:param name="bdId" value="idUKey"/>
                            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                            <s:param name="pageNo" value="%{#request.pageNo}"/>
                            <s:param name="district" value="#request.district"/>
                            <s:param name="division" value="#request.division"/>
                            <s:param name="recordCounter" value="#request.recordCounter"/>
                            <s:param name="reject" value="true"/>
                        </s:url><s:a href="%{rejectSelected}"
                                     title="%{getText('rejectTooltip.label')}"><img
                            src="<s:url value='/images/reject.png'/>" width="25" height="25"
                            border="none"/></s:a>
                    </td>
                    </s:if>
                    </s:if>
                </tr>
                <%--select_all checkbox is visible only if
            counter is greater than one--%>
                <s:set name="counter" scope="request" value="#approvalStatus.count"/>
            </s:iterator>
            <tr></tr>
            </table>
            <div class="form-submit">
            <s:if test="#request.counter>1">
                <s:label><s:checkbox
                        name="allCheck"
                        onclick="javascript:selectallMe(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"/>
                    <span><s:label name="select_all" value="%{getText('select_all.label')}"/></span></s:label>
                <s:hidden name="confirmationApprovalFlag" value="true"/>
                <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
                <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
                <s:hidden name="pageNo" value="%{#request.pageNo}"/>
                <s:hidden name="district" value="%{#request.district}"/>
                <s:hidden name="division" value="%{#request.division}"/>
                <s:hidden name="recordCounter" value="%{#request.recordCounter}"/>
                <s:submit name="approveSelected" value="%{getText('approveSelected.label')}"/>
            </s:if>
                </div>
            <div class="next-previous">
            <%-- Next link to visible next records will only visible if nextFlag is
          set to 1--%>
            <s:url id="previousUrl" action="eprConfirmationApprovalPrevious.do">
                <s:param name="confirmationApprovalFlag" value="true"/>
                <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                <s:param name="pageNo" value="%{#request.pageNo}"/>
                <s:param name="district" value="#request.district"/>
                <s:param name="division" value="#request.division"/>
                <s:param name="recordCounter" value="#request.recordCounter"/>
            </s:url>

            <s:url id="nextUrl" action="eprConfirmationApprovalNext.do">
                <s:param name="confirmationApprovalFlag" value="true"/>
                <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                <s:param name="pageNo" value="%{#request.pageNo}"/>
                <s:param name="district" value="#request.district"/>
                <s:param name="division" value="#request.division"/>
                <s:param name="recordCounter" value="#request.recordCounter"/>
            </s:url>

            <s:if test="#request.previousFlag"><s:a href="%{previousUrl}">
                <img src="<s:url value='/images/previous.gif'/>" width="40px" height="35px" border="none"/></s:a><s:label value="%{getText('previous.label')}" cssStyle="margin-right:5px;" /></s:if>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <s:if test="#request.nextFlag"><s:label value="%{getText('next.label')}" cssStyle="margin-left:5px;"/><s:a href="%{nextUrl}">
                    <img src="<s:url value='/images/next.gif'/>" width="40px" height="35px" border="none"/></s:a></s:if>
                </div>
        </s:form>
    </div>
</div>