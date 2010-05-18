<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 10, 2010
  Time: 3:59:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:form action="eprBirthRegistration_birthRegistrationPreProcessor.do" method="POST"
        name="birthRegistrationForm1">
    <div id="birth-registration-form-1-body">
         <div id="birth-registration-form-1-serial">
            <s:textfield name="birthRegister.serialNumber"/>
        </div>
        <div id="date-of-birth">
            <s:select list="{'2009','2010','2011'}" name="year"/>
            <s:select list="{'01','02','03'}" name="month"/>
            <s:select list="{'01','02','03'}" name="day"/>
        </div>
        <div id="district">
            <s:select list="{'Colombo','Gampaha','Kaluthara'}" name="session.birthRegister.childBirthDistrict"/>
        </div>
        <div id="division"><s:textfield name="session.birthRegister.childBirthDivision"/></div>
        <div id="place"><s:textfield name="session.birthRegister.childBirthPlace"/></div>
        <div id="name"><s:textfield name="session.birthRegister.childFullNameOfficialLang"/></div>
        <div id="name-in-english"><s:textfield name="session.birthRegister.childFullNameEnglish"/></div>
        <div id="gender">
            <s:select list="{'Male','Female'}" name="session.birthRegister.childGender"/>
        </div>
        <div id="birth-weight"><s:textfield name="session.birthRegister.childBirthWeight"/></div>
        <div id="no-of-children"><s:textfield name="session.birthRegister.noOfLiveChildren"/></div>
        <div id="multiple-no-of-children"><s:textfield name="session.birthRegister.noOfMultipleBirths"/></div>
        <div id="hospital-code"><s:textfield name="session.birthRegister.hospitalOrGNCode"/></div>
    </div>
    <s:hidden name="pageNo" value="1" />
    <div class="button"><s:submit value="NEXT"/></div>
</s:form>
