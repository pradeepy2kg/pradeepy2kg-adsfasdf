<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% String info = session.getAttribute("info").toString();%>
<div id="birth-register-approval-header">
    <s:form action="eprBirthRegisterApproval_getList" name="birth_register_approval_head" method="POST">
        <s:label><span>District:</span><s:textfield name="district" ></s:textfield></s:label>
        <s:label><span>Division:</span><s:textfield name="division" ></s:textfield></s:label>
        <s:label><span>Show Expired:</span><s:checkbox name="expired"></s:checkbox></s:label>
        <s:submit name="refresh" value="refresh"></s:submit>
    </s:form>
</div>
<div id="birth-register-approval-body">
    <% out.write(info);%>
</div>
<div id="birth-register-approval-footer">
    
</div>