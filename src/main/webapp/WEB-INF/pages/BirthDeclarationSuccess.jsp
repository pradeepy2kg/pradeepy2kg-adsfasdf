<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: amith23
  Date: Jun 7, 2010
  Time: 10:58:44 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>Simple jsp page</title></head>
  <body>
    <s:form action="eprBirthConfirmationPrint.do" name="birthRegistrationSuccess" id="birth-registration-success" method="POST">
    <s:submit type="button" value="print birth declaration"></s:submit>
    </s:form>
  </body>
</html>