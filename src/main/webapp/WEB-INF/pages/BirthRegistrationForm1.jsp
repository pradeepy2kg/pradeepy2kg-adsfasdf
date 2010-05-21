<html><%@ page import="java.util.List" %>
<%--
  @author duminda
--%> <head>
<%@ taglib prefix="s" uri="/struts-tags" %>
    <s:head theme="ajax"/>
     <script type="text/javascript" src='<s:url value="/js/datemanipulater.js"/>'></script>
</head><body>
<s:form action="eprBirthRegistration.do" method="POST" name="birthRegistrationForm1">
    <div id="birth-registration-form-1-body">
         <div id="birth-registration-form-1-serial">
            <s:textfield name="birthRegister.serialNumber"/>
        </div>
        <div id="date-of-birth">
            <s:select list="{'2009','2010','2011'}" name="year" id="year" onchange="javascript:setDate('year')"/>
            <s:select list="{'01','02','03'}" name="month" id="month" onchange="javascript:setDate('month')"/>
            <s:select list="{'01','02','03'}" name="day" id="day" onchange="javascript:setDate('day')"/>
            <s:datetimepicker id="datePicker" name="" label="Format (yyyy-MM-dd)" displayFormat="yyyy-MM-dd"
                              onmouseover="javascript:splitDate('datePicker')"/>
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
</s:form>  </body>
</html>