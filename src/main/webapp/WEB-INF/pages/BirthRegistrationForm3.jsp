<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 10, 2010
  Time: 4:00:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%session.setAttribute("birthRegister",request.getAttribute("birthRegister"));%>
<s:form action="eprBirthRegistration_birthRegistrationPreProcessor.do" name="birthRegistrationForm3">
    <div id="birth-registration-form-3-body">
        <div id="place-of-marriage"><s:textfield name="placeOfMarriage"/></div>
        <div id="date-of-marriage"><s:textfield name="dateOfMarriage"/></div>
        <div id="signature"><s:textfield name="mother_signature"/><input type="text" name="father_signature"/>
        </div>
        <div id="grandfather-name_lk"><s:textfield name="grandFatherFullName"/></div>
        <div id="grandfather-dob_lk"><s:textfield name="grandFatherBirthYear"/></div>
        <div id="grandfather-birth-place_lk"><s:textfield name="grandFatherBirthPlace"/></div>
        <div id="grandfather-name"><s:textfield name="greatGrandFatherFullName"/></div>
        <div id="grandfather-dob"><s:textfield name="greatGrandFatherBirthYear"/></div>
        <div id="grandfather-birth-place"><s:textfield name="greatGrandFatherBirthPlace"/></div>
        <div id="info-person">
            <div id="mother"><s:checkbox name="informantMother"/></div>
            <div id="father"><s:checkbox name="informantFather"/></div>
            <div id="holder"><s:checkbox name="informantGuardian"/></div>
        </div>
        <div id="info-person-name"><s:textfield name="informantName"/></div>
        <div id="info-person-nic"><s:textfield name="informantNIC"/></div>
        <div id="info-person-address"><s:textfield name="informantPostalAddress"/></div>
        <div id="info-person-phone"><s:textfield name="informantPhoneNo"/></div>
        <div id="info-person-email"><s:textfield name="informantEmail"/></div>
        <div id="info-person-signature">Info Person signature</div>
    </div>
    <s:hidden  name="pageNo" value="3" />
    <div class="button"><s:submit value="NEXT"/></div>
</s:form>