<%--
  @author duminda
--%>
<html>
<head>
    <%@ taglib prefix="s" uri="/struts-tags" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <s:head theme="ajax"/>
    <script type="text/javascript" src='<s:url value="/js/validation.js"/>'></script>
    <script type="text/javascript" src='<s:url value="/js/datemanipulater.js"/>'></script>
</head>
<body>
<s:form action="eprBirthRegistration.do" method="POST" name="birthRegistrationForm1"
        onsubmit="javascript:return birthRegistrationValidator()">
    <div id="birth-registration-form-1-body">
        <div id="birth-registration-form-1-serial">
            <s:textfield name="birthRegister.serialNumber" id="serialNumber"/>
        </div>
        <div id="date-of-birth">
            <s:select list="{'2009','2010','2011'}" name="year" id="year" onchange="javascript:setDate('year','1')"/>
            <s:select list="{'01','02','03'}" name="month" id="month" onchange="javascript:setDate('month','1')"/>
            <s:select list="{'01','02','03'}" name="day" id="day" onchange="javascript:setDate('day','1')"/>
            <s:datetimepicker id="datePicker" name="childDOB" label="Format (yyyy-MM-dd)" displayFormat="yyyy-MM-dd"
                              onmouseover="javascript:splitDate('datePicker')"/>
        </div>
        <div id="district">
            <s:if test="#session.user_lang == 'en_US'"> <s:select name="birthRegister.childBirthDistrict"
                                                                  list="districtList"
                                                                  headerKey="0"
                                                                  headerValue="-Select District-"/></s:if>
            <s:if test="#session.user_lang == 'si_LK'"> <s:select name="birthRegister.childBirthDistrict"
                                                                  list="districtList"
                                                                  headerKey="0"
                                                                  headerValue="-දිස්ත්‍රික්කය තෝරන්න-"/></s:if>
            <s:if test="#session.user_lang == 'ta_LK'"> <s:select name="birthRegister.childBirthDistrict"
                                                                  list="districtList"
                                                                  headerKey="0"
                                                                  headerValue="-In Tamil-"/></s:if>
        </div>
        <div id="division"><s:textfield name="birthRegister.childBirthDivision"/></div>
        <div id="place"><s:textfield name="birthRegister.childBirthPlace"/></div>
        <div id="name"><s:textfield name="birthRegister.childFullNameOfficialLang"/></div>
        <div id="name-in-english"><s:textfield name="birthRegister.childFullNameEnglish"/></div>
        <div id="gender">
            <s:if test="#session.user_lang == 'en_US'"><s:select list="#{'1':'Male','2':'Female','3':'Unknown'}"
                                                                 name="birthConfirm.childGender" headerKey="0"
                                                                 headerValue="-Select Gender-"/> </s:if>
            <s:if test="#session.user_lang == 'si_LK'"> <s:select list="#{'1':'පිරිමි','2':'ගැහැණු','3':'නොදනී'}"
                                                                  name="birthConfirm.childGender" headerKey="0"
                                                                  headerValue="-ලිංගභේදය තෝරන්න-"/></s:if>
            <s:if test="#session.user_lang == 'ta_LK'"><s:select list="#{'1':'Male','2':'Female','3':'Unknown'}"
                                                                 name="birthConfirm.childGender" headerKey="0"
                                                                 headerValue="-In Tamil-"/> </s:if>
        </div>
        <div id="birth-weight"><s:textfield name="birthRegister.childBirthWeight" id="childBirthWeight"/></div>
        <div id="no-of-children"><s:textfield name="birthRegister.noOfLiveChildren" id="noOfLiveChildren"/></div>
        <div id="multiple-no-of-children"><s:textfield name="birthRegister.noOfMultipleBirths"
                                                       id="noOfMultipleBirths"/></div>
        <div id="hospital-code"><s:textfield name="birthRegister.hospitalOrGNCode"/></div>
    </div>
    <s:hidden name="pageNo" value="1"/>
    <div class="button"><s:submit value="NEXT"/></div>
</s:form></body>
</html>