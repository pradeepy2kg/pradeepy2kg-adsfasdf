<%--
  Created by IntelliJ IDEA.
  User: Kosala
  Date: May 7, 2010
  Time: 1:01:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>Simple jsp page</title></head>
<body>
<div id="login-form">
    <div id="login-form-title">user login</div>
    <div id="login-form-body">
        <s:form action="eprLogin.do" method="POST"
                name="eprLogin">
            <s:label value="User Name: "></s:label>
            <s:textfield name="userName"></s:textfield>
            <s:label value="Password: "></s:label>
            <s:password name="password"></s:password>
            <div><s:submit value="login"></s:submit></div>
        </s:form>
    </div>
</div>
</body>
</html>
