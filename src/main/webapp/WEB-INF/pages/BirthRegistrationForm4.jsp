<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 10, 2010
  Time: 4:01:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:form action="eprBirthRegistration.do" name="birthRegistrationForm4">
    <div id="birth-registration-form-4-body">
        <div id="serial-no"><s:textfield name="birthRegister.serialNumber"
                                         value="%{#session.birthRegister.bdfSerialNo}" disabled="true"/></div>
        <div id="child-name"><s:textfield name="birthRegister.childFullNameEnglish"
                                          value="%{#session.birthRegister.childFullNameEnglish}" disabled="true"/></div>
        <div id="child-dob"><s:textfield name="birthRegister.childDOB" value="%{#session.birthRegister.dateOfBirth}"
                                         disabled="true"/></div>
        <div id="child-gender"><s:textfield name="birthRegister.childGender"
                                            value="%{#session.birthRegister.childGender}" disabled="true"/></div>
        <div id="child-father"><s:textfield name="birthRegister.fatherFullName"
                                            value="%{#session.birthRegister.fatherFullName}" disabled="true"/></div>
        <div id="child-mother"><s:textfield name="birthRegister.motherFullName"
                                            value="%{#session.birthRegister.motherFullName}" disabled="true"/></div>
        <div id="authority"><s:textfield name="birthRegister.notifyingAuthorityName"/></div>
    </div>
    <s:hidden name="pageNo" value="4"/>
    <div class="button"><s:submit value="%{getText('nextButton.label')}"/></div>
</s:form>