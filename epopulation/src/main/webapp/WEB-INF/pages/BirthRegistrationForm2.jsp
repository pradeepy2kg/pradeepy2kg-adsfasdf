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
        <div id="father-nic-no"><s:textfield name="fatherNIC" /></div>
        <div id="father-passport-no"><s:textfield  name="fatherPassportNo"/></div>
        <div id="father-country"><s:textfield  name="fatherCountry"/></div>
        <div id="father-name"><s:textfield  name="fatherFullName"/></div>
        <div id="father-dob"><s:textfield  name="fatherDOB"/></div>
        <div id="father-birth-place"><s:textfield  name="fatherBirthPlace"/></div>
        <div id="father-race"><s:textfield  name="fatherRace"/></div>
        <div id="mother-nic-no"><s:textfield  name="motherNIC"/></div>
        <div id="mother-passport-no"><s:textfield  name="motherPassportNo"/></div>
        <div id="mother-country"><s:textfield  name="motherCountry"/></div>
        <div id="mother-admision-no"><s:textfield  name="motherAdmissionNoAndDate"/></div>
        <div id="mother-name"><s:textfield  name="motherFullName"/></div>
        <div id="mother-dob"><s:textfield  name="motherDOB"/></div>
        <div id="mother-birth-place"><s:textfield  name="motherBirthPlace"/></div>
        <div id="mother-race"><s:textfield  name="motherRace"/></div>
        <div id="mother-age-for-birth"><s:textfield  name="motherAgeAtBirth"/></div>
        <div id="mother-address"><s:textfield  name="motherAddress"/></div>
        <div id="mother-phone"><s:textfield  name="motherPhoneNo"/></div>
        <div id="mother-email"><s:textfield  name="motherEmail"/></div>
    </div>
    <s:hidden  name="pageNo" value="2" />
    <div class="button"><s:submit type="submit" value="NEXT"/></div>
</s:form>