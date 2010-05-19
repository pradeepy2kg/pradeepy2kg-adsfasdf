<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 10, 2010
  Time: 3:59:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:form action="eprBirthRegistration.do" method="POST" name="birthRegistrationForm1">
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
            <s:select name="birthRegister.childBirthDistrict" list="districtList" listKey="districtId"
                      listValue="districtName" headerKey="0" headerValue="-Select District-"/>
        </div>
        <div id="division"><s:textfield name="birthRegister.childBirthDivision"/></div>
        <div id="place"><s:textfield name="birthRegister.childBirthPlace"/></div>
        <div id="name"><s:textfield name="birthRegister.childFullNameOfficialLang"/></div>
        <div id="name-in-english"><s:textfield name="birthRegister.childFullNameEnglish"/></div>
        <div id="gender">
            <s:select list="{'Male','Female'}" name="birthRegister.childGender"/>
        </div>
        <div id="birth-weight"><s:textfield name="birthRegister.childBirthWeight"/></div>
        <div id="no-of-children"><s:textfield name="birthRegister.noOfLiveChildren"/></div>
        <div id="multiple-no-of-children"><s:textfield name="birthRegister.noOfMultipleBirths"/></div>
        <div id="hospital-code"><s:textfield name="birthRegister.hospitalOrGNCode"/></div>
    </div>
    <s:hidden name="pageNo" value="1" />
    <div class="button"><s:submit value="NEXT"/></div>
</s:form>
