<%--
  User: chathuranga
  Date: May 13, 2010
  Time: 10:38:39 AM
  Birth Registration Confirmation Page 1
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form name="birthConfirmationForm1" action="eprBirthConfirmation.do" method="POST">
    <div id="birth-confirmation-form-1-body">
        <div id="birth-confirmation-form-1-serial">
            <s:textfield name="birthRegister.confirmSerialNumber"/>
        </div>
        <div id="birth-confirmation-regis-serial">
            <s:textfield name="birthRegister.serialNumber"/>
        </div>
        <div id="birth-confirmation-regis-date">
            <s:select list="{'2009','2010','2011'}" name="birthRegister.year"/>
            <s:select list="{'January','February','March'}" name="birthRegister.month"/>
            <s:select list="{'01','02','03'}" name="birthRegister.day"/>
        </div>
        <div id="birth-confirmation-date-of-birth">
            <s:select list="{'2009','2010','2011'}" name="birthRegister.dobYear"/>
            <s:select list="{'January','February','March'}" name="birthRegister.dobMonth"/>
            <s:select list="{'01','02','03'}" name="birthRegister.dobDay"/>
        </div>
        <div id="birth-confirmation-birth-place">
            <s:select name="birthRegister.childBirthDistrict" list="districtList" listKey="raceId"
                      listValue="raceName" headerKey="0" headerValue="-Select District-"/>
        </div>
        <div id="birth-confirmation-name"><s:textarea name="birthRegister.childFullNameOfficialLang" cols="38"
                                                      rows="7"/></div>
        <div id="birth-confirmation-name-in-english"><s:textarea name="birthRegister.childFullNameEnglish" cols="38"
                                                                 rows="5"/></div>
        <div id="birth-confirmation-gender">
            <s:select list="{'Male','Female'}" name="birthRegister.childGender" headerKey="0"
                      headerValue="-Select Gender-"/>
        </div>
        <s:hidden name="pageNo" value="1"/>
        <div class="button"><s:submit type="submit" value="NEXT"/></div>
    </div>
</s:form>
</html>