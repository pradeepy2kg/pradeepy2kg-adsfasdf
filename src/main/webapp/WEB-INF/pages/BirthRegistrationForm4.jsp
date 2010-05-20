<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 10, 2010
  Time: 4:01:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="eprBirthRegistration.do" name="birthRegistrationForm4">
   <div id="birth-registration-form-4-body">
        <div id="serial-no"><s:textfield name="birthRegister.serialNumber"/></div>
        <div id="child-name"><s:textfield name="birthRegister.childFullNameEnglish"/></div>
        <div id="child-dob"><s:textfield name="birthRegister.childDOB"/></div>
        <div id="child-gender"><s:textfield name="birthRegister.childGender"/></div>
        <div id="child-father"><s:textfield name="birthRegister.fatherFullName"/></div>
        <div id="child-mother"><s:textfield name="birthRegister.motherFullName"/></div>
        <div id="authority"><s:textfield name="birthRegister.authority"/></div>
    </div>
    <s:hidden  name="pageNo" value="4" />
    <div class="button"><s:submit value="NEXT"/></div>
   </s:form>