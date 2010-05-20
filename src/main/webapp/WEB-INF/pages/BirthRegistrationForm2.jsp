<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 10, 2010
  Time: 4:00:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:form action="eprBirthRegistration.do" method="POST" name="birthRegistrationForm2">
     <div id="birth-registration-form-2-body">
        <div id="father-serial-no"><s:label value="%{#session.birthRegister.serialNumber}"/></div>
        <div id="father-nic-no"><s:textfield name="birthRegister.fathersNIC" /></div>
        <div id="father-passport-no"><s:textfield name="birthRegister.fatherForeignerPassportNo"/></div>
        <div id="father-country">
            <s:select name="birthRegister.fatherForeignerCountry" list="districtList" listKey="districtId"
                      listValue="districtName" headerKey="0" headerValue="-Select District-"/>
        </div>
        <div id="father-name"><s:textfield name="birthRegister.fatherFullName"/></div>
        <%--<div id="father-dob"><s:textfield name="fatherDOB" value="2005/05/01" /></div>--%>
        <div id="father-birth-place"><s:textfield name="birthRegister.fatherBirthPlace"/></div>
        <div id="father-race"><s:textfield name="birthRegister.fatherRace"/></div>
        <div id="mother-nic-no"><s:textfield name="birthRegister.motherNIC"/></div>
        <div id="mother-passport-no"><s:textfield name="birthRegister.motherPassportNo"/></div>
        <div id="mother-country"><s:textfield name="birthRegister.motherCountry"/></div>
        <div id="mother-admision-no"><s:textfield name="birthRegister.motherAdmissionNoAndDate"/></div>
        <div id="mother-name"><s:textfield name="birthRegister.motherFullName"/></div>
        <%--<div id="mother-dob"><s:textfield name="motherDOB" value="2005/05/01"/></div>--%>
        <div id="mother-birth-place"><s:textfield name="birthRegister.motherBirthPlace"/></div>
        <div id="mother-race"><s:textfield name="birthRegister.motherRace"/></div>
        <div id="mother-age-for-birth"><s:textfield name="birthRegister.motherAgeAtBirth"/></div>
        <div id="mother-address"><s:textfield name="birthRegister.motherAddress"/></div>
        <div id="mother-phone"><s:textfield name="birthRegister.motherPhoneNo"/></div>
        <div id="mother-email"><s:textfield name="birthRegister.motherEmail"/></div>
    </div>
    <s:hidden  name="pageNo" value="2" />
    <div class="button"><s:submit type="submit" value="NEXT"/></div>
</s:form>