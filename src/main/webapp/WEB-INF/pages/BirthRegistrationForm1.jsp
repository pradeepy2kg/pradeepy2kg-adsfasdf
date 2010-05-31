<%--
  @author duminda
--%>
    <%@ taglib prefix="s" uri="/struts-tags" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:form action="eprBirthRegistration.do" method="POST" name="birthRegistrationForm1"
        onsubmit="javascript:return birthRegistrationValidator()">
    <div id="birth-registration-form-1-body">
        <div id="birth-registration-form-1-serial">
            <s:textfield name="birthRegister.bdfSerialNo" id="serialNumber"/>
        </div>
        <div id="date-of-birth">
            <s:select list="{'2009','2010','2011'}" name="" id="year" onchange="javascript:setDate('year','1')"/>
            <s:select list="{'01','02','03'}" name="" id="month" onchange="javascript:setDate('month','1')"/>
            <s:select list="{'01','02','03'}" name="" id="day" onchange="javascript:setDate('day','1')"/>
            <s:datetimepicker id="datePicker" name="birthRegister.childDOB" label="Format (yyyy-MM-dd)" displayFormat="yyyy-MM-dd" value="2010-05-27"
                              onmouseover="javascript:splitDate('datePicker')"/>
        </div>
        <div id="district">
            <s:select name="birthRegister.childBirthDistrict" list="districtList" headerKey="0"
                headerValue="%{getText('select_district.label')}"/>
        </div>
        <div id="division"><s:textfield name="birthRegister.birthDivision"/></div>
        <div id="place"><s:textfield name="birthRegister.placeOfBirth"/></div>
        <div id="name"><s:textfield name="birthRegister.childFullNameOfficialLang"/></div>
        <div id="name-in-english"><s:textfield name="birthRegister.childFullNameEnglish"/></div>
        <div id="gender">
            <s:select list="#{'1':getText('male.label'),'2':getText('female.label'),
                '3':getText('unknown.label')}" name="" headerKey="0" headerValue="%{getText('select_gender.label')}"/>
        </div>
        <div id="birth-weight"><s:textfield name="birthRegister.childBirthWeight" id="childBirthWeight"/></div>
        <div id="no-of-children"><s:textfield name="birthRegister.childRank" id="noOfLiveChildren"/></div>
        <div id="multiple-no-of-children"><s:textfield name="birthRegister.numberOfChildrenBorn"
                                                       id="noOfMultipleBirths"/></div>
        <div id="hospital-code"><s:textfield name="birthRegister.hospitalOrGNCode"/></div>
    </div>
    <s:hidden name="pageNo" value="1"/>
    <div class="button"><s:submit value="NEXT"/></div>
</s:form>