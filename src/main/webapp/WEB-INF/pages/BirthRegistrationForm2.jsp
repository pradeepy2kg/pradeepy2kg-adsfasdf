<%--
  @author duminda
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<s:form action="eprBirthRegistration.do" method="POST" name="birthRegistrationForm2"--%>
<%--onsubmit="javascript:return ageValidator()">--%>
<%--<div id="birth-registration-form-2-body">--%>
<%--<div id="father-nic-no"><s:textfield name="birthRegister.fatherNICorPIN"/></div>--%>

<%--<div id="father-passport-no"><s:textfield name="birthRegister.fatherPassportNo"/></div>--%>
<%--<div id="father-country">--%>
<%--<s:select name="birthRegister.fatherForeignerCountry"list="countryList" headerKey="0"--%>
<%--headerValue="%{getText('select_country.label')}"/>--%>
<%--<s:if test="#session.user_lang == 'si_LK'"> <s:select name="birthRegister.fatherForeignerCountry"--%>
<%--list="countryList"--%>
<%--headerKey="0"--%>
<%--headerValue="-රට තෝරන්න-"/></s:if>--%>
<%--<s:if test="#session.user_lang == 'ta_LK'"> <s:select name="birthRegister.fatherForeignerCountry"--%>
<%--list="countryList"--%>
<%--headerKey="0"--%>
<%--headerValue="-In Tamil-"/></s:if>--%>
<%--</div>--%>
<%--<div id="father-name"><s:textfield name="birthRegister.fatherFullName"/></div>--%>
<%--<div id="father-dob">--%>
<%--<s:select list="{'2009','2010','2011'}" name="fatherYear" id="fatherYear"--%>
<%--onchange="javascript:setDate('fatherYear','2')"/>--%>
<%--<s:select list="{'01','02','03'}" name="fatherMonth" id="fatherMonth"--%>
<%--onchange="javascript:setDate('fatherMonth','2')"/>--%>
<%--<s:select list="{'01','02','03'}" name="fatherDay" id="fatherDay"--%>
<%--onchange="javascript:setDate('fatherDay','2')"/>--%>
<%--<s:datetimepicker id="fatherDatePicker" name="fatherDOB" label="Format (yyyy-MM-dd)"--%>
<%--displayFormat="yyyy-MM-dd"--%>
<%--onmouseover="javascript:splitDate('fatherDatePicker')"/>--%>
<%--</div>--%>
<%--<div id="father-birth-place"><s:textfield name="birthRegister.fatherPlaceOfBirth"/></div>--%>
<%--<div id="father-race">--%>
<%--<s:if test="#session.user_lang == 'en_US'"><s:select list="#{'1':'Male','2':'Female','3':'Unknown'}"--%>
<%--name="birthConfirm.fatherRace" headerKey="0"--%>
<%--headerValue="%{getText('select_gender.lable')}"/> </s:if>--%>
<%-- <s:if test="#session.user_lang == 'si_LK'"> <s:select list="#{'1':'පිරිමි','2':'ගැහැණු','3':'නොදනී'}"--%>
<%--name="birthConfirm.fatherRace" headerKey="0"--%>
<%--headerValue="-ලිංගභේදය තෝරන්න-"/></s:if>--%>
<%--<s:if test="#session.user_lang == 'ta_LK'"><s:select list="#{'1':'Male','2':'Female','3':'Unknown'}"--%>
<%--name="birthConfirm.fatherRace" headerKey="0"--%>
<%--headerValue="-In Tamil-"/> </s:if>    --%>
<%--</div>--%>
<%--<div id="mother-nic-no"><s:textfield name="birthRegister.motherNICorPIN"/></div>--%>
<%--<div id="mother-passport-no"><s:textfield name="birthRegister.motherPassportNo"/></div>--%>
<%--<div id="mother-country">--%>
<%--<s:if test="#session.user_lang == 'en_US'"> <s:select name="birthRegister.motherCountry"--%>
<%--list="countryList"--%>
<%--headerKey="0"--%>
<%--headerValue="%{getText('select_country.label')}"/></s:if>--%>
<%--<s:if test="#session.user_lang == 'si_LK'"> <s:select name="birthRegister.motherCountry"--%>
<%--list="countryList"--%>
<%--headerKey="0"--%>
<%--headerValue="-රට තෝරන්න-"/></s:if>--%>
<%--<s:if test="#session.user_lang == 'ta_LK'"> <s:select name="birthRegister.motherCountry"--%>
<%--list="countryList"--%>
<%--headerKey="0"--%>
<%--headerValue="-In Tamil-"/></s:if>   --%>
<%--</div>--%>
<%--<div id="mother-admision-no"><s:textfield name="birthRegister.motherAdmissionNoAndDate"/></div>--%>
<%--<div id="mother-name"><s:textfield name="birthRegister.motherFullName"/></div>--%>
<%--<div id="mother-dob">--%>
<%--<s:select list="{'2009','2010','2011'}" name="motherYear" id="motherYear"--%>
<%--onchange="javascript:setDate('motherYear','3')"/>--%>
<%--<s:select list="{'01','02','03'}" name="motherMonth" id="motherMonth"--%>
<%--onchange="javascript:setDate('motherMonth','3')"/>--%>
<%--<s:select list="{'01','02','03'}" name="motherDay" id="motherDay"--%>
<%--onchange="javascript:setDate('motherDay','3')"/>--%>
<%--<s:datetimepicker id="motherdatePicker" name="motherDOB" label="Format (yyyy-MM-dd)"--%>
<%--displayFormat="yyyy-MM-dd"--%>
<%--onmouseover="javascript:splitDate('motherdatePicker')"/>--%>
<%--</div>--%>
<%--<div id="mother-birth-place"><s:textfield name="birthRegister.motherBirthPlace"/></div>--%>
<%--<div id="mother-race">--%>
<%--<s:if test="#session.user_lang == 'en_US'"><s:select list="#{'1':'Male','2':'Female','3':'Unknown'}"--%>
<%--name="birthConfirm.motherRace" headerKey="0"--%>
<%--headerValue="%{getText('select_gender.lable')}"/> </s:if>--%>
<%-- <s:if test="#session.user_lang == 'si_LK'"> <s:select list="#{'1':'පිරිමි','2':'ගැහැණු','3':'නොදනී'}"--%>
<%--name="birthConfirm.motherRace" headerKey="0"--%>
<%--headerValue="-ලිංගභේදය තෝරන්න-"/></s:if>--%>
<%--<s:if test="#session.user_lang == 'ta_LK'"><s:select list="#{'1':'Male','2':'Female','3':'Unknown'}"--%>
<%--name="birthConfirm.motherRace" headerKey="0"--%>
<%--headerValue="-In Tamil-"/> </s:if>  --%>
<%--</div>--%>
<%--<div id="mother-age-for-birth"><s:textfield name="birthRegister.motherAgeAtBirth" id="motherAgeAtBirth"/></div>--%>
<%--<div id="mother-address"><s:textfield name="birthRegister.motherAddress"/></div>--%>
<%--<div id="mother-phone"><s:textfield name="birthRegister.motherPhoneNo"/></div>--%>
<%--<div id="mother-email"><s:textfield name="birthRegister.motherEmail"/></div>--%>
<%--</div>--%>
<%--<s:hidden name="pageNo" value="2"/>--%>
<%--<div class="button"><s:submit type="submit" value="NEXT"/></div>--%>
<%--</s:form>--%>

<div class="birth-registration-form-outer">
    <form action="eprBirthRegistration.do" name="birthRegistrationForm2" id="birth-registration-form-2" method="POST">
        <div id="birth-registration-form-father-info-sub-title" class="form-sub-title">
            පි‍යාගේ විස්තර
            <br>தந்தை பற்றிய தகவல்
            <br>Details of the Father
        </div>
        <div id="father-nic" class="font-9">
            <div class="form-label">
                <label>(10)අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br>து தனிநபர் அடையாள எண் /தேசிய அடையாள அட்டை
                    இலக்கம்<br>PIN / NIC Number</label>
            </div>
            <div>

            </div>
        </div>
        <div id="foreign-father" class="font-9">
            <div class="form-label">
                <label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
            </div>
            <div id="father-country" class="font-9">
                <div class="form-label">
                    <label>රට<br>நாடு <br>Country</label>
                </div>
                <div><input type="text" name=""/></div>
            </div>
            <div id="father-passport-no" class="font-9">
                <div class="form-label">
                    <label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label>
                </div>
                <div><input type="text" name=""/></div>
            </div>
        </div>
        <div id="father-name" class="font-9">
            <div class="form-label">
                <label>(11)සම්පුර්ණ නම<br>தந்தையின் முழு பெயர்<br>Full Name</label>
            </div>
            <div>
                <textarea name=""></textarea>
            </div>
        </div>
        <div id="father-dob" class="font-9">
            <div class="form-label">
                <label>(12)උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth</label>
            </div>
            <div>
                <input type="text" name=""/>
            </div>
        </div>
        <div id="father-pob" class="font-9">
            <div class="form-label">
                <label>(13)උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of Birth</label>
            </div>
            <div>
                <input type="text" name=""/>
            </div>
        </div>
        <div id="father-race" class="font-9">
            <div class="form-label">
                <label>(14)පියාගේ ජාතිය<br>இனம்<br> Father's Race</label>
            </div>
            <div>
                <input type="text" name=""/>
            </div>
        </div>
        <br>

        <div id="birth-registration-form-mother-info-sub-title" class="form-sub-title">
            මවගේ විස්තර <br>தாய் பற்றிய தகவல் <br>Details of the Mother
        </div>
        <div id="mother-nic" class="font-9">
            <div class="form-label">
                <label>(15)අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>து தனிநபர் அடையாள எண் /தேசிய அடையாள அட்டை
                    இலக்கம்<br>PIN / NIC Number</label>
            </div>
            <div>

            </div>
        </div>
        <div id="foreign-mother" class="font-9">
            <div class="form-label">
                <label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
            </div>
            <div id="mother-country" class="font-9">
                <div class="form-label">
                    <label>රට<br>நாடு <br>Country</label>
                </div>
                <div><input type="text" name=""/></div>
            </div>
            <div id="mother-passport-no" class="font-9">
                <div class="form-label">
                    <label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label>
                </div>
                <div><input type="text" name=""/></div>
            </div>
        </div>
        <div id="mother-name" class="font-9">
            <div class="form-label">
                <label>(16)සම්පුර්ණ නම<br>தந்தையின் முழு பெயர்<br>Full Name</label>
            </div>
            <div>
                <textarea name=""></textarea>
            </div>
        </div>
        <div id="mother-dob" class="font-9">
            <div class="form-label">
                <label>(17)උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth</label>
            </div>
            <div>
                <input type="text" name=""/>
            </div>
        </div>
        <div id="mother-pob" class="font-9">
            <div class="form-label">
                <label>(18)උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of Birth</label>
            </div>
            <div>
                <input type="text" name=""/>
            </div>
        </div>
        <div id="mother-race" class="font-9">
            <div class="form-label">
                <label>(19)ම‌වගේ ජාතිය<br>இனம்<br> Mother's Race</label>
            </div>
            <div>
                <input type="text" name=""/>
            </div>
        </div>
        <div id="mother-age" class="font-9">
            <div class="form-label">
                <label>(20) ළමයාගේ උපන් දිනට මවගේ වයස<br> பிள்ளை பிறந்த திகதியில் மாதாவின் வயது<br>Mother's Age as at
                    the date of birth of child </label>
            </div>
            <div>
                <input type="text" name=""/>
            </div>
        </div>
        <div id="mother-address" class="font-9">
            <div class="form-label">
                <label>(21)මවගේ ස්ථිර ලිපිනය<br>தாயின் நிரந்தர வதிவிட முகவரி<br>Permanent Address of the Mother</label>
            </div>
            <div>
                <textarea name=""></textarea>
            </div>
        </div>
        <div id="hospital-admision-no" class="font-9">
            <div class="form-label">
                <label>(22)රෝහලට ඇතුලත් කිරිමේ අංකය<br>*in tamil<br>Hospital Admission Number</label>
            </div>
            <div>
                <input type="text" name=""/>
            </div>
        </div>
        <div id="hospital-admision-date" class="font-9">
            <div class="form-label">
                <label>(23)රෝහලට ඇතුලත් කිරිමේ දිනය<br>*in tamil<br>Hospital Admission Date</label>
            </div>
            <div>
                <input type="text" name=""/>
            </div>
        </div>
        <div id="mother-contact-details" class="font-9">
            <label>(24)ම‌ව සම්බන්ධ කල හැකි තොරතුරු <br>தாயின் தொடர்பு இலக்க தகவல் <br>Contact Details of the
                Mother</label>

            <div id="mother-phone" class="form-label">
                <label>දුරකතනය <br> தொலைபேசி இலக்கம் <br> Telephone</label>
            </div>
            <div>
                <input type="text" name=""/>
            </div>
            <div id="mother-email" class="form-label">
                <label>ඉ – තැපැල් <br> மின்னஞ்சல்<br>Email</label>
            </div>
            <div>
                <input type="text" name=""/>
            </div>
            <s:hidden name="pageNo" value="2"/>
            <div class="button"><s:submit type="submit" value="NEXT"/></div>
    </form>
</div>