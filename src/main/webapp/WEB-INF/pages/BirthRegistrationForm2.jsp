<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 10, 2010
  Time: 4:00:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%session.setAttribute("birthRegister",request.getAttribute("birthRegister"));%>
<s:form action="eprBirthRegistration_birthRegistrationPreProcessor.do" method="POST"
        name="birthRegistrationForm2">
     <div id="birth-registration-form-2-body">
        <div id="father-nic-no"><s:textfield name="session.birthRegister.fathersNIC" /></div>
        <div id="father-passport-no"><s:textfield  name="session.birthRegister.fatherForeignerPassportNo"/></div>
        <div id="father-country"><s:textfield  name="session.birthRegister.fatherForeignerCountry"/></div>
        <div id="father-name"><s:textfield  name="session.birthRegister.fatherFullName"/></div>
        <div id="father-dob"><s:textfield  name="fatherDOB" value="2005/05/01" /></div>
        <div id="father-birth-place"><s:textfield  name="session.birthRegister.fatherBirthPlace"/></div>
        <div id="father-race"><s:textfield  name="session.birthRegister.fatherRace"/></div>
        <div id="mother-nic-no"><s:textfield  name="session.birthRegister.motherNIC"/></div>
        <div id="mother-passport-no"><s:textfield  name="session.birthRegister.motherPassportNo"/></div>
        <div id="mother-country"><s:textfield  name="session.birthRegister.motherCountry"/></div>
        <div id="mother-admision-no"><s:textfield  name="session.birthRegister.motherAdmissionNoAndDate"/></div>
        <div id="mother-name"><s:textfield  name="session.birthRegister.motherFullName"/></div>
        <div id="mother-dob"><s:textfield  name="motherDOB" value="2005/05/01"/></div>
        <div id="mother-birth-place"><s:textfield  name="session.birthRegister.motherBirthPlace"/></div>
        <div id="mother-race"><s:textfield  name="session.birthRegister.motherRace"/></div>
        <div id="mother-age-for-birth"><s:textfield  name="session.birthRegister.motherAgeAtBirth"/></div>
        <div id="mother-address"><s:textfield  name="session.birthRegister.motherAddress"/></div>
        <div id="mother-phone"><s:textfield  name="session.birthRegister.motherPhoneNo"/></div>
        <div id="mother-email"><s:textfield  name="session.birthRegister.motherEmail"/></div>
    </div>
    <s:hidden  name="pageNo" value="2" />
    <div class="button"><s:submit type="submit" value="NEXT"/></div>
</s:form>