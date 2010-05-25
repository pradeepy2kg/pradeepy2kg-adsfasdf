<%--
  @author duminda
--%>
<html>
<head>
    <%@ taglib prefix="s" uri="/struts-tags" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <s:head theme="ajax"/>
    <script type="text/javascript" src='<s:url value="/js/datemanipulater.js"/>'></script>
</head>
<body>
<s:form action="eprBirthRegistration.do" method="post" name="birthRegistrationForm3">
    <div id="birth-registration-form-3-body">
        <div id="place-of-marriage"><s:textfield name="birthRegister.placeOfMarriage"/></div>
        <div id="date-of-marriage"><s:textfield name="" value="2005/05/01"/></div>
        <div id="signature"><s:textfield name="motherSigned" value="true"/><input type="text" name="fatherSigned"
                                                                                  value="true"/>
        </div>
        <div id="grandfather-name_lk"><s:textfield name="birthRegister.grandFatherFullName"/></div>
        <div id="grandfather-dob_lk"><s:textfield name="birthRegister.grandFatherBirthYear"/></div>
        <div id="grandfather-birth-place_lk"><s:textfield name="birthRegister.grandFatherBirthPlace"/></div>
        <div id="grandfather-name"><s:textfield name="birthRegister.greatGrandFatherFullName"/></div>
        <div id="grandfather-dob"><s:textfield name="birthRegister.greatGrandFatherBirthYear"/></div>
        <div id="grandfather-birth-place"><s:textfield name="birthRegister.greatGrandFatherBirthPlace"/></div>
        <div id="info-person">
            <div id="mother"><s:checkbox name="birthRegister.informantType" value="1"/></div>
            <div id="father"><s:checkbox name="birthRegister.informantType" value="0"/></div>
            <div id="holder"><s:checkbox name="birthRegister.informantType" value="2"/></div>
        </div>
        <div id="info-person-name"><s:textfield name="birthRegister.informantName"/></div>
        <div id="info-person-nic"><s:textfield name="birthRegister.informantNICorPIN"/></div>
        <div id="info-person-address"><s:textfield name="birthRegister.informantAddress"/></div>
        <div id="info-person-phone"><s:textfield name="birthRegister.informantPhoneNo"/></div>
        <div id="info-person-email"><s:textfield name="birthRegister.informantEmail"/></div>
        <div id="info-person-signature">
            <s:datetimepicker id="datePicker" name="informantSignDate" label="Format (yyyy-MM-dd)" displayFormat="yyyy-MM-dd"
                              onmouseover=""/></div>
    </div>
    <s:hidden name="pageNo" value="3"/>
    <s:hidden name="birthRegister.parentsMarried" value="0"/>
    <div class="button"><s:submit value="NEXT"/></div>
</s:form>
</body>
</html>