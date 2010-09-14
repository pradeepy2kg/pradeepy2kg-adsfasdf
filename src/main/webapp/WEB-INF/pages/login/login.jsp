<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>EPR Login</title>
    <link rel="stylesheet" type="text/css" href="/ecivil/css/layout.css"/>
    <style type="text/css">
        html {
            background: /ecivil/images/body-bg1.png repeat;
        }

        body {
            clear: both;
        }
    </style>
    <script type="text/javascript">
        function setFocus() {
            document.forms[0].userName.focus();
        }
    </script>
</head>
<body onload="setFocus()">
<div id="wrapper">
    <%
        if (session.getAttribute("user_bean") != null) {
            response.sendRedirect("/ecivil/eprHome.do");
        }
    %>

    <img src="/ecivil/images/epr-header.png" align="center" width="1250px"/>

    <div id="login-error" style="text-align:center;">
        <s:actionerror cssStyle="color:red; line-height:30px; font-size:11pt; margin:150px auto -170px auto;"/>
    </div>

    <div id="login-form" style=" ">
        <div id="login-form-title">user login</div>
        <div id="login-form-body">
            <s:form action="/eprLogin.do" method="POST" name="eprLogin">
                <s:label value="User Name: "></s:label>
                <s:textfield name="userName"></s:textfield>
                <s:label value="Password: "></s:label>
                <s:password name="password"></s:password>
                <div><s:submit value="login"></s:submit></div>
            </s:form>
        </div>
    </div>

    <div style="margin-left:auto; margin-right:auto; width:100%; text-align:center;">
        Copyright © 2010 » Registrar General&apos;s Department of Sri Lanka. All Rights Reserved.
    </div>
</div>
</body>
</html>


<%-- Styling Completed --%>