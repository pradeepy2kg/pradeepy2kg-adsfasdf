<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            <s:textfield name="birthConfirm.confirmSerialNumber"/>
        </div>
        <div id="birth-confirmation-regis-serial">
            <s:textfield name="birthConfirm.serialNumber"/>
        </div>
        <div id="birth-confirmation-regis-date">
            <s:select list="{'2009','2010','2011'}" name=""/>
            <s:select list="{'January','February','March'}" name=""/>
            <s:select list="{'01','02','03'}" name=""/>
        </div>
        <div id="birth-confirmation-date-of-birth">
            <s:select list="{'2009','2010','2011'}" name=""/>
            <s:select list="{'January','February','March'}" name=""/>
            <s:select list="{'01','02','03'}" name=""/>
        </div>
        <div id="birth-confirmation-birth-place">
            <s:select name="birthRegister.childBirthDistrict" list="districtList" headerKey="0"
                      headerValue="%{getText('select_district.label')}"/>
        </div>

        <div id="birth-confirmation-name"><s:textarea name="birthConfirm.childFullNameOfficialLang" cols="38"
                                                      rows="7"/></div>
        <div id="birth-confirmation-name-in-english"><s:textarea name="birthConfirm.childFullNameEnglish" cols="38"
                                                                 rows="5"/></div>
        <div id="birth-confirmation-gender">
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
        <s:hidden name="pageNo" value="1"/>
        <div class="button"><s:submit type="submit" value="NEXT"/></div>
    </div>
</s:form>
<s:form id="print">
    <div class="button_print">
        <s:submit type="button" value="PRINT" onclick="print()"/></div>
</s:form>
</html>