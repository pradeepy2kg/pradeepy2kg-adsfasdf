<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 10, 2010
  Time: 4:00:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="eprBirthRegistration_birthRegistrationPreProcessor.do" method="post" name="birthRegistrationForm3">
    <div id="birth-registration-form-3-body"><s:textfield name="birthRegister.fathersNIC"/><s:textfield name="birthRegister.serialNumber"/>
        <div id="place-of-marriage"><s:textfield name="birthRegister.placeOfMarriage"/></div>
        <div id="date-of-marriage"><s:textfield name="dateOfMarriage" value="2005/05/01"/></div>
        <div id="signature"><s:textfield name="motherSignature"/><input type="text" name="fatherSignature"/>
        </div>
        <div id="grandfather-name_lk"><s:textfield name="birthRegister.grandFatherFullName"/></div>
        <div id="grandfather-dob_lk"><s:textfield name="birthRegister.grandFatherBirthYear"/></div>
        <div id="grandfather-birth-place_lk"><s:textfield name="birthRegister.grandFatherBirthPlace"/></div>
        <div id="grandfather-name"><s:textfield name="birthRegister.greatGrandFatherFullName"/></div>
        <div id="grandfather-dob"><s:textfield name="birthRegister.greatGrandFatherBirthYear"/></div>
        <div id="grandfather-birth-place"><s:textfield name="birthRegister.greatGrandFatherBirthPlace"/></div>
        <div id="info-person">
            <div id="mother"><s:checkbox name="informantMother"/></div>
            <div id="father"><s:checkbox name="informantFather"/></div>
            <div id="holder"><s:checkbox name="informantGuardian"/></div>
        </div>
        <div id="info-person-name"><s:textfield name="birthRegister.informantName"/></div>
        <div id="info-person-nic"><s:textfield name="birthRegister.informantNIC"/></div>
        <div id="info-person-address"><s:textfield name="birthRegister.informantPostalAddress"/></div>
        <div id="info-person-phone"><s:textfield name="birthRegister.informantPhoneNo"/></div>
        <div id="info-person-email"><s:textfield name="birthRegister.informantEmail"/></div>
        <div id="info-person-signature">Info Person signature</div>
    </div>
    <s:hidden  name="pageNo" value="3" />
    <div class="button"><s:submit value="NEXT"/></div>
</s:form>