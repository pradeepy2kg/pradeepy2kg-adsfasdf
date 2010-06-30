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
    <div id="login-form-title">user password change</div>
    <div id="login-form-body">
        <s:form action="eprChangePass.do" method="POST"
                name="eprChangePass">
            <!--password change -->

                <s:label value="new password"/>
                <s:password name="newPassword"/>
                <s:label value="retype new password"/>
                <s:password name="retypeNewPassword"/>
                <div><s:submit value="apply"></s:submit></div>
                <s:hidden name="changePass" value="0"/>
            
        </s:form>
    </div>
</div>
</body>
</html>
