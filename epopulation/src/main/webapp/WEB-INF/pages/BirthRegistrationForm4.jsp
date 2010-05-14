<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 10, 2010
  Time: 4:01:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%session.setAttribute("birthRegister", request.getAttribute("birthRegister"));%>
<s:form action="eprBirthRegistration_birthRegisterFinalizer.do" name="birthRegistrationForm4">
    <div id="birth-registration-form-4-body">
        <div id="serial-no"><s:textfield name="serialNumber"/></div>
        <div id="child-name"><s:textfield name="childNameFullNameEnglish"/></div>
        <div id="child-dob"><s:textfield name="dob"/></div>
        <div id="child-gender"><s:textfield name="childGender"/></div>
        <div id="child-father"><s:textfield name="fatherFullName"/></div>
        <div id="child-mother"><s:textfield name="motherFullName"/></div>
        <div id="authority"><s:textfield name="authority"/></div>
    </div>
    <div class="button"><s:submit value="NEXT"/></div>
</s:form>