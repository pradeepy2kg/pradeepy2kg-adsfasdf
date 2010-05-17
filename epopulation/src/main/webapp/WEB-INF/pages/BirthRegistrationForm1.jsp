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
            <s:textfield name="serialNumber" />
        </div>
        <div id="date-of-birth">
            <s:select list="{'2009','2010','2011'}" name="year" />
            <s:select list="{'January','February','March'}" name="month" />
            <s:select list="{'01','02','03'}" name="day" />
        </div>
        <div id="district">
            <s:select list="{'Colombo','Gampaha','Kaluthara'}" />
        </div>
        <div id="division"><s:textfield name="childBirthDivision" /></div>
        <div id="place"><s:textfield name="childBirthPlace" /></div>
        <div id="name"><s:textfield name="childNameOFullNameOfficialLang" /></div>
        <div id="name-in-english"><s:textfield name="childNameFullNameEnglish" /></div>
        <div id="gender">
            <s:select list="{'Male','Female'}" />
        </div>
        <div id="birth-weight"><s:textfield name="childBirthWeight" /></div>
        <div id="no-of-children"><s:textfield name="numOfLiveChildren" /></div>
        <div id="multiple-no-of-children"><s:textfield name="numOfMultipleBiths" /></div>
        <div id="hospital-code"><s:textfield name="hospitalOrGNCode" /></div>
    </div>
    <s:hidden name="pageNo" value="1" />
    <div class="button"><s:submit value="NEXT"/></div>
</s:form>