<%--
  Created amith jayasekara and Dumida Dharmakeerthi
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>EPR Change Password</title>
    <link rel="stylesheet" type="text/css" href='<s:url value="/css/layout.css"/>'/>
    <style type="text/css">
        html {
            background: <s:url value="/images/body-bg1.png"/> repeat;
        }
    </style>
</head>
<body>
<img src="images/epr-header.png" />
<div id="login-form">
    <div id="login-form-title">user password change</div>
    <div id="login-form-body">
        <s:form action="eprChangePass.do" method="POST"
                name="eprChangePass">
            <!--password change -->

                <s:label value="New Password"/>
                <s:password name="newPassword"/>
                <s:label value="Retype New Password"/>
                <s:password name="retypeNewPassword"/>
                <div class="form-submit">
                    <s:submit value="apply"></s:submit>
                    <s:submit action="eprBackChangePass" value="back"/>
                </div>
                <s:hidden name="changePass" value="0"/>
            
        </s:form>
    </div>
</div>
</body>
</html>
<%-- Styling Completed --%>
