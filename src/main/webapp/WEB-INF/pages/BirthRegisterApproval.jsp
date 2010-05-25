<html>
<head>
    <%@ page import="java.util.ArrayList" %>
    <%@ page import="lk.rgd.crs.api.domain.BirthRegisterApproval" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="s" uri="/struts-tags" %>
    <script type="text/javascript" src='<s:url value="/js/SelectAll.js"/>'></script>
</head>
<body>
<div id="birth-register-approval">
    <div id="birth-register-approval-header">
        <s:form action="eprBirthRegisterApproval_getList" name="birth_register_approval_head" method="POST">
            <s:label><span>District:</span><s:textfield name="district"></s:textfield></s:label>
            <s:label><span>Division:</span><s:textfield name="division"></s:textfield></s:label>
            <s:label><span>Show Expired:</span><s:checkbox name="expired"></s:checkbox></s:label>
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
                <s:iterator value="#session.ApprovelData">
                    <tr class="<s:if test="%{actions=='Expired'}" >expired</s:if>">
                        <td><s:checkbox name="index" onclick="javascript:selectall()"/></td>
                        <td><s:property value="serial"/></td>
                        <td><s:property value="name"/></td>
                        <td><s:property value="changes"/></td>
                        <td><s:property value="recievedDate"/></td>
                        <td><s:property value="actions"/></td>
                    </tr>
                </s:iterator>
                <tr></tr>
            </table>
            <s:label><s:checkbox name="allCheck" onclick="javascript:selectallMe()"/><span>Select All</span></s:label>
            <s:submit name="approve" value="approve"/>
        </s:form>
    </div>
    <div id="birth-register-approval-footer">

    </div>
</div>
</body>
</html>