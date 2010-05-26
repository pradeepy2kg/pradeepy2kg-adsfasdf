<%--
  @author duminda
--%>
    <%@ taglib prefix="s" uri="/struts-tags" %>

    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:form action="eprBirthRegistration.do" method="POST" name="birthRegistrationForm2"
        onsubmit="javascript:return ageValidator()">
    <div id="birth-registration-form-2-body">
        <div id="father-nic-no"><s:textfield name="birthRegister.fatherNICorPIN"/></div>

        <div id="father-passport-no"><s:textfield name="birthRegister.fatherPassportNo"/></div>
        <div id="father-country">
            <s:select name="birthRegister.fatherForeignerCountry"list="countryList" headerKey="0"
                      headerValue="%{getText('select_country.label')}"/>
            <%--<s:if test="#session.user_lang == 'si_LK'"> <s:select name="birthRegister.fatherForeignerCountry"--%>
                                                                  <%--list="countryList"--%>
                                                                  <%--headerKey="0"--%>
                                                                  <%--headerValue="-රට තෝරන්න-"/></s:if>--%>
            <%--<s:if test="#session.user_lang == 'ta_LK'"> <s:select name="birthRegister.fatherForeignerCountry"--%>
                                                                  <%--list="countryList"--%>
                                                                  <%--headerKey="0"--%>
                                                                  <%--headerValue="-In Tamil-"/></s:if>--%>
        </div>
        <div id="father-name"><s:textfield name="birthRegister.fatherFullName"/></div>
        <div id="father-dob">
            <s:select list="{'2009','2010','2011'}" name="fatherYear" id="fatherYear"
                      onchange="javascript:setDate('fatherYear','2')"/>
            <s:select list="{'01','02','03'}" name="fatherMonth" id="fatherMonth"
                      onchange="javascript:setDate('fatherMonth','2')"/>
            <s:select list="{'01','02','03'}" name="fatherDay" id="fatherDay"
                      onchange="javascript:setDate('fatherDay','2')"/>
            <s:datetimepicker id="fatherDatePicker" name="fatherDOB" label="Format (yyyy-MM-dd)"
                              displayFormat="yyyy-MM-dd"
                              onmouseover="javascript:splitDate('fatherDatePicker')"/>
        </div>
        <div id="father-birth-place"><s:textfield name="birthRegister.fatherPlaceOfBirth"/></div>
        <div id="father-race">
            <s:if test="#session.user_lang == 'en_US'"><s:select list="#{'1':'Male','2':'Female','3':'Unknown'}"
                                                                 name="birthConfirm.fatherRace" headerKey="0"
                                                                  headerValue="%{getText('select_gender.lable')}"/> </s:if>
           <%-- <s:if test="#session.user_lang == 'si_LK'"> <s:select list="#{'1':'පිරිමි','2':'ගැහැණු','3':'නොදනී'}"
                                                                  name="birthConfirm.fatherRace" headerKey="0"
                                                                  headerValue="-ලිංගභේදය තෝරන්න-"/></s:if>
            <s:if test="#session.user_lang == 'ta_LK'"><s:select list="#{'1':'Male','2':'Female','3':'Unknown'}"
                                                                 name="birthConfirm.fatherRace" headerKey="0"
                                                                 headerValue="-In Tamil-"/> </s:if>    --%>
        </div>
        <div id="mother-nic-no"><s:textfield name="birthRegister.motherNICorPIN"/></div>
        <div id="mother-passport-no"><s:textfield name="birthRegister.motherPassportNo"/></div>
        <div id="mother-country">
            <s:if test="#session.user_lang == 'en_US'"> <s:select name="birthRegister.motherCountry"
                                                                  list="countryList"
                                                                  headerKey="0"
                                                                  headerValue="%{getText('select_country.label')}"/></s:if>
            <%--<s:if test="#session.user_lang == 'si_LK'"> <s:select name="birthRegister.motherCountry"
                                                                  list="countryList"
                                                                  headerKey="0"
                                                                  headerValue="-රට තෝරන්න-"/></s:if>
            <s:if test="#session.user_lang == 'ta_LK'"> <s:select name="birthRegister.motherCountry"
                                                                  list="countryList"
                                                                  headerKey="0"
                                                                  headerValue="-In Tamil-"/></s:if>   --%>
        </div>
        <div id="mother-admision-no"><s:textfield name="birthRegister.motherAdmissionNoAndDate"/></div>
        <div id="mother-name"><s:textfield name="birthRegister.motherFullName"/></div>
        <div id="mother-dob">
            <s:select list="{'2009','2010','2011'}" name="motherYear" id="motherYear"
                      onchange="javascript:setDate('motherYear','3')"/>
            <s:select list="{'01','02','03'}" name="motherMonth" id="motherMonth"
                      onchange="javascript:setDate('motherMonth','3')"/>
            <s:select list="{'01','02','03'}" name="motherDay" id="motherDay"
                      onchange="javascript:setDate('motherDay','3')"/>
            <s:datetimepicker id="motherdatePicker" name="motherDOB" label="Format (yyyy-MM-dd)"
                              displayFormat="yyyy-MM-dd"
                              onmouseover="javascript:splitDate('motherdatePicker')"/>
        </div>
        <div id="mother-birth-place"><s:textfield name="birthRegister.motherBirthPlace"/></div>
        <div id="mother-race">
            <s:if test="#session.user_lang == 'en_US'"><s:select list="#{'1':'Male','2':'Female','3':'Unknown'}"
                                                                 name="birthConfirm.motherRace" headerKey="0"
                                                                 headerValue="%{getText('select_gender.lable')}"/> </s:if>
           <%-- <s:if test="#session.user_lang == 'si_LK'"> <s:select list="#{'1':'පිරිමි','2':'ගැහැණු','3':'නොදනී'}"
                                                                  name="birthConfirm.motherRace" headerKey="0"
                                                                  headerValue="-ලිංගභේදය තෝරන්න-"/></s:if>
            <s:if test="#session.user_lang == 'ta_LK'"><s:select list="#{'1':'Male','2':'Female','3':'Unknown'}"
                                                                 name="birthConfirm.motherRace" headerKey="0"
                                                                 headerValue="-In Tamil-"/> </s:if>  --%>
        </div>
        <div id="mother-age-for-birth"><s:textfield name="birthRegister.motherAgeAtBirth" id="motherAgeAtBirth"/></div>
        <div id="mother-address"><s:textfield name="birthRegister.motherAddress"/></div>
        <div id="mother-phone"><s:textfield name="birthRegister.motherPhoneNo"/></div>
        <div id="mother-email"><s:textfield name="birthRegister.motherEmail"/></div>
    </div>
    <s:hidden name="pageNo" value="2"/>
    <div class="button"><s:submit type="submit" value="NEXT"/></div>
</s:form>