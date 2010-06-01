<%@ page import="java.util.ArrayList" %>
<%@ page import="lk.rgd.crs.api.domain.BirthRegisterApproval" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<div id="birth-register-approval">
    <div id="birth-register-approval-header">
        <s:form action="eprGetExpiredList" name="birth_register_approval_head" method="POST">
            <s:label><span><s:label name="district" value= "%{getText('district.label')}" /></span><s:select list="districtList" name="district"/></s:label>
            <s:label><span><s:label name="division" value= "%{getText('division.label')}" /></span><s:select list="divisionList"
                                                     name="division" headerKey="0"/></s:label>
            <s:label><span><s:label name="show_expired" value= "%{getText('show_expired.label')}" /></span><s:checkbox name="expired"/></s:label>
            <s:submit name="refresh" value="refresh"></s:submit>
        </s:form>
    </div>
    <div id="birth-register-approval-body">

        <s:form action="eprBirthRegisterApproval" name="birth_register_approval_body" method="POST">
            <table>
                <tr>
                    <th></th>
                    <th></th>
                    <th><s:label name="serial" value= "%{getText('serial.label')}" /></th>
                    <th><s:label name="name" value= "%{getText('name.label')}" /></th>
                    <th><s:label name="changes" value= "%{getText('changes.label')}" /></th>
                    <th><s:label name="received" value= "%{getText('received.label')}" /></th>
                    <th><s:label name="actions" value= "%{getText('actions.label')}" /></th>
                </tr>
                <s:if test="#session.approvalStart == null">
                    <s:set name="approvalStart" value="0" scope="session"/>
                    <%--flag session variable is used to manage the next link if content cannot display within this
                    page flag is set to 1 if all the content can be displayed within one page flag is set to 0 --%>
                    <s:set name="flag" value="1" scope="session"/>
                </s:if>
                <s:subset source="#session.ApprovalData" count="10" start="#session.approvalStart">
                    <s:iterator status="approvalStatus">
                        <tr class="<s:if test="%{status==5}" >expired</s:if>">
                            <td><s:property value="%{#approvalStatus.count+#session.approvalStart}"/></td>
                            <td><s:checkbox name="index"
                                            onclick="javascript:selectall(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"/></td>
                            <td><s:property value="bdfSerialNo"/></td>
                            <td><s:property value="childFullNameEnglish"/></td>
                            <td><s:if test="status==4"><s:label value="%{getText('yes.label')}"/></s:if><s:else><s:label
                                    value="%{getText('no.label')}"/></s:else></td>
                            <td><s:property value="confirmationReceiveDate"/></td>
                            <td><s:if test="status==3 || status==4"><s:label value="%{getText('approve.label')}"/></s:if>
                            <s:elseif test="status==5"><s:label value="%{getText('expired.label')}" /></s:elseif> </td>
                        </tr>
                        <%--counter keeps track the displayed data--%>
                        <s:set name="counter" value="#approvalStatus.count" scope="session"/>
                    </s:iterator>
                </s:subset>
                <tr></tr>
            </table>
            <br/>
            <s:label><span><s:label name="select_all" value= "%{getText('select_all.label')}" /></span><s:checkbox name="allCheck"
                                                        onclick="javascript:selectallMe(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"/></s:label>
            <br/>

            <s:url id="previousUrl" action="eprApprovalPrevious.do"/>
            <s:url id="nextUrl" action="eprApprovalNext.do"/>
            <s:property value="#session.ApprovalData.size"/> <s:label name="items_found" value= "%{getText('items_found.label')}" /> <s:property
                value="#session.approvalStart+1"/> to <s:property
                value="%{#session.counter+#session.approvalStart}"/>
            <br/><br/>
            <s:if test="#session.approvalStart!=0"><s:a href="%{previousUrl}">
                <s:label value="<Previous"/></s:a></s:if>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <s:if test="#session.flag==1 && #session.ApprovalData.size>10"><s:a href="%{nextUrl}">
                <s:label value="Next>"/></s:a></s:if>
            <br/><br/>
            <%--todo submission of approve--%>
            <s:submit name="approve" value="%{getText('approve.label')}"/>
        </s:form>
    </div>
    <div id="birth-register-approval-footer">
    </div>
</div>