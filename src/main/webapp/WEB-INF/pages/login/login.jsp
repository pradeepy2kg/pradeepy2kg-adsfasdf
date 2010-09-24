<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>EPR Login</title>
    <link rel="stylesheet" type="text/css" href="/ecivil/css/layout.css"/>
    <style type="text/css">
        html {
            background: / ecivil / images / body-bg1 . png repeat;
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
                <table class="login-table">
                    <tr>
                        <td style="width:50%"><s:label value="User Name: "/></td>
                        <td style="width:50%"><s:textfield name="userName" cssStyle="width:95%"/></td>
                    </tr>
                    <tr>
                        <td><s:label value="Password: "/></td>
                        <td><s:password name="password" cssStyle="width:95%"/></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div><s:submit value="login"/></div>
                        </td>
                    </tr>
                </table>
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