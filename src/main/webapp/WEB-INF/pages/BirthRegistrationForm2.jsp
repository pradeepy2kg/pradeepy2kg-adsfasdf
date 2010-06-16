<%--
  @author duminda
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-registration-form-outer" id="birth-registration-form-2-outer">
<s:form action="eprBirthRegistration.do" name="birthRegistrationForm2" id="birth-registration-form-2" method="POST"
        onsubmit="javascript:return validate()">
        <div id="birth-registration-form-father-info-sub-title" class="form-sub-title">
            පි‍යාගේ විස්තර
            <br>தந்தை பற்றிய தகவல்
            <br>Details of the Father
        </div>
        <div id="father-nic" class="font-9">
            <label>(10)අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br>து தனிநபர் அடையாள எண் /தேசிய அடையாள அட்டை
                இலக்கம்<br>PIN / NIC Number</label>
            <s:textfield name="parent.fatherNICorPIN"/>
        </div>
        <div id="foreign-father" class="font-9">
            <label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>

            <div id="father-country" class="font-9">
                <div class="form-label">
                    <label>රට<br>நாடு <br>Country</label>
                </div>
                <div>
                    <s:select name="fatherCountry" list="countryList" headerKey="0"
                              headerValue="%{getText('select_country.label')}"/>
                </div>
            </div>
            <div id="father-passport-no" class="font-9">
                <div class="form-label">
                    <label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label>
                </div>
                <div><s:textfield name="parent.fatherPassportNo"/></div>
            </div>
        </div>
        <div id="father-name" class="font-9">
            <div class="form-label">
                <label>(11)සම්පුර්ණ නම<br>தந்தையின் முழு பெயர்<br>Full Name</label>
            </div>
            <div>
                <s:textarea name="parent.fatherFullName" id="fatherFullName"/>
            </div>
        </div>
        <div id="father-dob" class="font-9">
            <div class="form-label">
                <label>(12)උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth</label>
            </div>
            <div>
                <sx:datetimepicker id="fatherDatePicker" name="parent.fatherDOB"
                                   displayFormat="yyyy-MM-dd" onmouseover="javascript:splitDate('fatherDatePicker')"/>
            </div>
        </div>
        <div id="father-pob" class="font-9">
            <div class="form-label">
                <label>(13)උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of Birth</label>
            </div>
            <div>
                <s:textfield name="parent.fatherPlaceOfBirth"/>
            </div>
        </div>
        <div id="father-race" class="font-9">
            <div class="form-label">
                <label>(14)පියාගේ ජාතිය<br>இனம்<br> Father's Race</label>
            </div>
            <div>
                <s:select list="raceList" name="fatherRace" headerKey="0"
                          headerValue="%{getText('select_race.label')}"/>
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
                <s:textfield name="parent.motherNICorPIN"/>
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
                <div>
                    <s:select name="motherCountry" list="countryList" headerKey="0"
                              headerValue="%{getText('select_country.label')}"/>
                </div>
            </div>
            <div id="mother-passport-no" class="font-9">
                <div class="form-label">
                    <label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label>
                </div>
                <div><s:textfield name="parent.motherPassportNo"/></div>
            </div>
        </div>
        <div id="mother-name" class="font-9">
            <div class="form-label">
                <label>(16)සම්පුර්ණ නම<br>தந்தையின் முழு பெயர்<br>Full Name</label>
            </div>
            <div>
                <s:textarea name="parent.motherFullName" id="motherFullName"/>
            </div>
        </div>
        <div id="mother-dob" class="font-9">
            <div class="form-label">
                <label>(17)උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth</label>
            </div>
            <div>
                <sx:datetimepicker id="motherDatePicker" name="parent.motherDOB" displayFormat="yyyy-MM-dd"
                                   onmouseover="javascript:splitDate('motherDatePicker')"/>

            </div>
        </div>
        <div id="mother-pob" class="font-9">
            <div class="form-label">
                <label>(18)උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of Birth</label>
            </div>
            <div>
                <s:textfield name="parent.motherPlaceOfBirth"/>
            </div>
        </div>
        <div id="mother-race" class="font-9">
            <div class="form-label">
                <label>(19)ම‌වගේ ජාතිය<br>இனம்<br> Mother's Race</label>
            </div>
            <div>
                <s:select list="raceList" name="motherRace" headerKey="0"
                          headerValue="%{getText('select_race.label')}"/>
            </div>
        </div>
        <div id="mother-age" class="font-9">
            <div class="form-label">
                <label>(20) ළමයාගේ උපන් දිනට මවගේ වයස<br> பிள்ளை பிறந்த திகதியில் மாதாவின் வயது<br>Mother's Age as at
                    the date of birth of child </label>
            </div>
            <div>
                <s:textfield name="parent.motherAgeAtBirth"/>
            </div>
        </div>
        <div id="mother-address" class="font-9">
            <div class="form-label">
                <label>(21)මවගේ ස්ථිර ලිපිනය<br>தாயின் நிரந்தர வதிவிட முகவரி<br>Permanent Address of the Mother</label>
            </div>
            <div>
                <div id="mother-district">
                    <label>*in Sinhala/*in English/District</label>
                    <s:select name="motherDistrictId" list="allDistrictList" headerKey="0"
                              headerValue="%{getText('select_district.label')}"/>
                </div>
                <div id="mother-ds-division">
                    <label>*in Sinhala/*in English/D.S Division</label>
                    <s:select name="motherDSDivisionId" list="allDSDivisionList" headerKey="0"
                              headerValue="%{getText('select_ds_division.label')}"/>
                </div>
                <s:textarea name="parent.motherAddress"/>
            </div>
        </div>
        <div id="hospital-admision-no" class="font-9">
            <div class="form-label">
                <label>(22)රෝහලට ඇතුලත් කිරිමේ අංකය<br>*in tamil<br>Hospital Admission Number</label>
            </div>
            <div>
                <s:textfield name="parent.motherAdmissionNo"/>
            </div>
        </div>
        <div id="hospital-admision-date" class="font-9">
            <div class="form-label">
                <label>(23)රෝහලට ඇතුලත් කිරිමේ දිනය<br>*in tamil<br>Hospital Admission Date</label>
            </div>
            <div>
                <sx:datetimepicker id="admitDatePicker" name="parent.motherAdmissionDate"
                                   displayFormat="yyyy-MM-dd" onmouseover="javascript:splitDate('admitDatePicker')"/>
            </div>
        </div>
        <div id="mother-contact-details" class="font-9">
            <label>(24)ම‌ව සම්බන්ධ කල හැකි තොරතුරු <br>தாயின் தொடர்பு இலக்க தகவல் <br>Contact Details of the
                Mother</label>

            <div id="mother-phone" class="form-label">
                <label>දුරකතනය <br> தொலைபேசி இலக்கம் <br> Telephone</label>
            </div>
            <div>
                <s:textfield name="parent.motherPhoneNo"/>
            </div>
            <div id="mother-email" class="form-label">
                <label>ඉ – තැපැල් <br> மின்னஞ்சல்<br>Email</label>
            </div>
            <div>
                <s:textfield name="parent.motherEmail"/>
            </div>
        </div>
        <s:hidden name="pageNo" value="2"/>

        <s:hidden id="p2error1" value="%{getText('p2.fatherName.error.value')}"/>
        <s:hidden id="p2error2" value="%{getText('p2.motherName.error.value')}"/>

        <script type="text/javascript">
            function validate()
                {
                    var errormsg="";
                    var element;
                    var returnval;
                    var check=document.getElementById('skipjs');
                    if (!check.checked) {

                        element=document.getElementById('fatherFullName');
                        if (element.value=="") {
                            errormsg = errormsg +  "\n" + document.getElementById('p2error1').value;
                        }
                        element=document.getElementById('motherFullName');
                        if (element.value=="") {
                            errormsg = errormsg + "\n" + document.getElementById('p2error2').value;
                        }
                    }
                    if(errormsg!=""){
                        alert(errormsg);
                        returnval =false;
                    }
                    return returnval;
                    }
         </script>

        <div class="form-submit">
            <s:url id="backUrl" action="eprBirthRegistration">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
            </s:url>
            <s:a href="%{backUrl}"> << </s:a>
            <s:checkbox name="skipjavaScript" id="skipjs" value="false" >
                <s:label value="%{getText('skipvalidation.label')}"/>
            </s:checkbox>
            <s:submit value="%{getText('next.label')}"/>
        </div>
    </s:form>
</div>