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
            <s:label><span>Show Expired:</span><s:checkbox name="expired" /></s:label>
            <s:submit name="refresh" value="refresh"></s:submit>
        </s:form>
    </div>
    <div id="birth-register-approval-body">

        <s:form action="eprBirthRegisterApproval" name="birth_register_approval_body" method="POST">
            <table>
                <tr>
                    <th></th>
                    <th>Serial</th>
                    <th>Name</th>
                    <th>Changes</th>
                    <th>Received</th>
                    <th>Actions</th>
                </tr>
                <s:iterator status="stat" value="#session.ApprovelData">
                    <tr class="<s:if test="%{actions=='Expired'}" >expired</s:if>">
                        <td><s:checkbox name="index"
                                        onclick="javascript:selectall('birth_register_approval_body')"/></td>
                        <td><s:property value="serial"/></td>
                        <td><s:property value="name"/></td>
                        <td><s:property value="changes"/></td>
                        <td><s:property value="recievedDate"/></td>
                        <td><s:property value="actions"/></td>
                    </tr>
                </s:iterator>
                <tr></tr>
            </table>
            <s:label><span>Select All</span><s:checkbox name="allCheck"
                                 onclick="javascript:selectallMe('birth_register_approval_body')"/></s:label>
            <br/>
            <s:property value="%{#session.ApprovelData.size}" /><s:label value=" Items found , displaying 1 to " />
            <s:property value="%{#session.ApprovelData.size}" /><s:a href="#" name="next" > Next</s:a>

            <br/><br/>
            <s:submit name="approve" value="Approve"/>
        </s:form>
    </div>
    <div id="birth-register-approval-footer">
    </div>
</div>