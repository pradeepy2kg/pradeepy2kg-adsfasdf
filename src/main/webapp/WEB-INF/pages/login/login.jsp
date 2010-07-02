<%--
  Created amith jayasekara and duminda
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>EPR Login</title>
    <link rel="stylesheet" type="text/css" href='<s:url value="/css/layout.css"/>'/>
    <style type="text/css">
        html {
            background: <s:url value="/images/body-bg1.png"/> repeat;
        }
    </style>
</head>
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
<%-- Styling Completed --%>
