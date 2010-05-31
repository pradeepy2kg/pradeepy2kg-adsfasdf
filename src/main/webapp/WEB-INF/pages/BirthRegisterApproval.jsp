<%@ page import="java.util.ArrayList" %>
<%@ page import="lk.rgd.crs.api.domain.BirthRegisterApproval" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<div id="birth-register-approval">
    <div id="birth-register-approval-header">
        <s:form action="eprGetExpiredList" name="birth_register_approval_head" method="POST">
            <s:label><span>District:</span><s:select list="districtList" name="district"/></s:label>
            <s:label><span>Division:</span><s:select list="divisionList"
                                                     name="division" headerKey="0"/></s:label>
            <s:label><span>Show Expired:</span><s:checkbox name="expired"/></s:label>
            <s:submit name="refresh" value="refresh"></s:submit>
        </s:form>
    </div>
    <div id="birth-register-approval-body">

        <s:form action="eprBirthRegisterApproval" name="birth_register_approval_body" method="POST">
            <table>
                <tr>
                    <th></th>
                    <th></th>
                    <th>Serial</th>
                    <th>Name</th>
                    <th>Changes</th>
                    <th>Received</th>
                    <th>Actions</th>
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
                            <td><s:if test="status==4"><s:label value="Yes"/></s:if><s:else><s:label
                                    value="No"/></s:else></td>
                            <td><s:property value="confirmationReceiveDate"/></td>
                            <td><s:if test="status==3 || status==4"><s:label value="Approve"/></s:if>
                            <s:elseif test="status==5"><s:label value="Expired" /></s:elseif> </td>
                        </tr>
                        <%--counter keeps track the displayed data--%>
                        <s:set name="counter" value="#approvalStatus.count" scope="session"/>
                    </s:iterator>
                </s:subset>
                <tr></tr>
            </table>
            <br/>
            <s:label><span>Select All</span><s:checkbox name="allCheck"
                                                        onclick="javascript:selectallMe(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"/></s:label>
            <br/>

            <s:url id="previousUrl" action="eprApprovalPrevious.do"/>
            <s:url id="nextUrl" action="eprApprovalNext.do"/>
            <s:property value="#session.ApprovalData.size"/> Items Found, displaying <s:property
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
            <s:submit name="approve" value="Approve"/>
        </s:form>
    </div>
    <div id="birth-register-approval-footer">
    </div>
</div>