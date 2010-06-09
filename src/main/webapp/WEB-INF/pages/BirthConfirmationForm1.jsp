<%@ page import="lk.rgd.crs.api.domain.BirthDeclaration" %>
<%@ page import="lk.rgd.common.api.domain.District" %>
<%@ page import="java.util.Map" %>
<%@ page import="lk.rgd.crs.web.WebConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  User: chathuranga
  Date: May 13, 2010
  Time: 10:38:39 AM
  Birth Registration Confirmation Page 1
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<div class="birth-confirmation-form-outer">
    <s:form action="eprBirthConfirmation" name="birthConfirmationForm1" id="birth-confirmation-form-1" method="POST">
        <div id="birth-confirmation-form-header">
            <div id="birth-confirmation-form-header-logo">
                <img src="<s:url value="/images/official-logo.png" />" alt=""/>
            </div>
            <div id="birth-confirmation-form-header-title" class="font-12">
                <label>
                    ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA<br><br>
                    දෙමව්පියන් / භාරකරු විසින් උපත තහවුරු කිරීම
                    ﻿﻿ <br>பெற்றோர் அல்லது பாதுகாப்பாளா் மூலம் பிறப்பை உறுதிப்படுத்தல்
                    <br>Confirmation of Birth by Parents / Guardian
                </label>
            </div>
            <div id="bcf-serial-no">
                <label><span class="font-8">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</span><s:textfield name="" /></label>
            </div>
        </div>
        <div id="brf-serial-no-and-date" class="font-9">
            <div class="no">1</div>
            <label>
                සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ අදාල “උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර” ප්‍රකාශනයේ අනුක්‍රමික අංකය
                හා දිනය
                <br>பிறப்பை பதிவு செய்வதற்கான விபரம்" எனும் படிவத்தின் தொடா் இலக்கமும் திகதியும்
                <br>Serial Number and the Date of the ‘Particulars for Registration of a Birth’ form</label>
            <s:textfield cssClass="disable" disabled="true" value="%{#session.birthRegister.register.bdfSerialNo}"/>
            <s:textfield cssClass="disable" disabled="true" value="%{#session.birthRegister.register.dateOfRegistration}"/>
        </div>
        <div id="last-date" class="font-9">
            <div class="no">2</div>
            <label>
                *in Sinhala
                <br>* in Tamil
                <br>Last date by which changes should be received by the registrar generals office.
            </label>
            <s:textfield cssClass="disable" disabled="true" value=""/>
        </div>
        <div id="civil-confirmation-info-sub-title" class="form-sub-title">
            සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ ඇතුළත් විස්තර
            <br>சிவில் பதிவு அமைப்பில் உள்ளடக்கப்பட்டுள்ள விபரம்
            <br>Information included in Civil Registration System
        </div>
        <div id="title-head" class="font-9">
            <div id="particulars">
                <label>විස්තර <br>விபரங்கள் <br>Particulars </label>
            </div>
            <div id="current-info">
                <label>සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ දැනට අඩංගු විස්තර <br>சிவில் பதிவு அமைப்பில் உள்ளடக்கப்பட்டுள்ள
                    விபரம<br>Information included in Civil Registration System </label>
            </div>
            <div id="new-info">
                <label>
                    වෙනස් විය යුතු විස්තර අතුලත් කරන්න.
                    <br>புதியசிவில் பதிவ..ண்டிய விப...
                    <br>Insert new details or modify existing details</label>
            </div>
        </div>
        <div id="bcf-dob" class="font-9">
            <div class="no">3</div>
            <label>උපන් දිනය<br>பிறந்த திகதி<br>Date of birth</label>

            <div class="current">
                <div id="current-year" class="font-7">
                    <label>*in Sinhala<br>*in Tamil<br>Year</label>
                    <s:textfield value="%{#session.birthConfirmation.child.dateOfBirth.year + 1900}" cssClass="disable" disabled="true"/>
                </div>
                <div id="current-month" class="font-7">
                    <label>*in Sinhala<br>*in Tamil<br>Day</label>
                    <s:textfield value="%{#session.birthConfirmation.child.dateOfBirth.month + 1}" cssClass="disable" disabled="true"/>
                </div>
                <div id="current-day" class="font-7">
                    <label>*in Sinhala<br>*in Tamil<br>Day</label>
                    <s:textfield value="%{#session.birthConfirmation.child.dateOfBirth.date}" cssClass="disable" disabled="true"/>
                </div>
            </div>
            <div class="new">
                <div id="new-year" class="font-7">
                    <label>*in Sinhala<br>*in Tamil<br>Year</label>
                    <s:select list="{'2009','2010','2011'}" name="" id="submitYear"
                              onchange="javascript:setDate('year','1')"/>
                </div>
                <div id="new-month" class="font-7">
                    <label>*in Sinhala<br>*in Tamil<br>Month</label>
                    <s:select list="{'01','02','03'}" name="" id="submitMonth"
                              onchange="javascript:setDate('month','1')"/>
                </div>
                <div id="new-day" class="font-7">
                    <label>*in Sinhala<br>*in Tamil<br>Day</label>
                    <s:select list="{'01','02','03'}" name="" id="submitDay"
                              onchange="javascript:setDate('day','1')"/>
                </div>
            </div>
        </div>
        <div id="new-dob">
            <sx:datetimepicker id="submitDatePicker" name="child.dateOfBirth" displayFormat="yyyy-MM-dd"
                               value="2010-05-27"
                onmouseover="javascript:splitDate('submitDatePicker')"/>
        </div>
        <div id="bcf-gender" class="font-9">
            <div class="no">4</div>
            <label>ස්ත්‍රී පුරුෂ භාවය <br>பால்பால்<br>Gender</label>
            <div class="current">
                <s:if test="#session.birthConfirmation.child.childGender == 0" >
                    <s:textfield value="%{getText('male.label')}" cssClass="disable" disabled="true"/>
                </s:if>
                <s:elseif test="#session.birthConfirmation.child.childGender == 1">
                    <s:textfield value="%{getText('female.label')}" cssClass="disable" disabled="true"/>
                </s:elseif>
                <s:elseif test="#session.birthConfirmation.child.childGender == 2">
                    <s:textfield value="%{getText('unknown.label')}" cssClass="disable" disabled="true"/>
                </s:elseif>
            </div>
            <div class="new">
                <s:select list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                          name="child.childGender" headerKey="0" headerValue="%{getText('select_gender.label')}"/>
            </div>
        </div>
        <div id="bcf-pob" class="font-9">
            <div class="no">5</div>
            <label>උපන් ස්ථානය / பிறந்தபிறந்த இடம் / Place of birth</label>
        </div>
        <div id="bcf-district" class="font-9">
            <div class="no"></div>
            <label>දිස්ත්‍රික්කය <br>மாவட்டம் <br>District</label>
            <div class="current">
                <s:textfield value="%{#session.birthConfirmation.register.birthDivision.dsDivision.district.districtId}" cssClass="disable" disabled="true"/>
            </div>
            <div class="new">
                <s:select list="districtList" name="birthDistrictId" headerKey="0" headerValue="%{getText('select_district.label')}" />
            </div>
        </div>
        <div id="bcf-ds-division" class="font-9">
            <div class="no"></div>
            <label>D.S.කොට්ඨාශය<br>பிரிவு <br>D.S. Division</label>
            <div class="current">
                <s:textfield value="%{#session.birthConfirmation.register.birthDivision.dsDivision.divisionId}" cssClass="disable" disabled="true"/>
            </div>
            <div class="new">
                <s:select list="dsDivisionList" name="dsDivisionId" headerKey="0" headerValue="%{getText('select_ds_division.label')}" />
            </div>
        </div>
        <div id="bcf-division" class="font-9">
            <div class="no"></div>
            <label>කොට්ඨාශය<br>பிரிவு <br>Registration Division</label>
            <div class="current">
                <s:textfield value="%{#session.birthConfirmation.register.birthDivision.divisionId}" cssClass="disable" disabled="true"/>
            </div>
            <div class="new">
                <s:select name="birthDivisionId" list="bdDivisionList" headerKey="0" headerValue="%{getText('select_division.label')}"/>  
            </div>
        </div>
        <div id="bcf-place" class="font-9">
            <div class="no"></div>
            <label>ස්ථානය  <br>பிறந்த இடம் <br>Place</label>
            <div class="current">
                <s:textfield value="%{#session.birthConfirmation.child.placeOfBirth}" cssClass="disable" disabled="true"/>
            </div>
            <div class="new">
                <s:textfield name="child.placeOfBirth"/>
            </div>
        </div>
        <div id="bcf-father-pin" class="font-9">
            <div class="no">6</div>
            <label>පියාගේ අනන්‍යතා අංකය <br>தந்நையின் தனிநபர் அடையாள எண்<br>Father's PIN</label>
            <div class="current">
                <s:textfield value="%{#session.birthConfirmation.parent.fatherNICorPIN}" cssClass="disable" disabled="true"/>
            </div>
            <div class="new">
                <s:textfield name="parent.fatherNICorPIN"/>
            </div>
        </div>
        <div id="bcf-father-race" class="font-9">
            <div class="no">7</div>
            <label>පියාගේ ජාතිය <br>தந்நையின் இனம்<br>Father's Race</label>
            <div class="current">
                <s:textfield value="%{getRaceList().get(#session.birthConfirmation.parent.fatherRace.raceId)}" cssClass="disable" disabled="true"/>
            </div>
            <div class="new">
                <s:select list="raceList" name="fatherRace" headerKey="0" headerValue="%{getText('select_race.label')}"/> 
            </div>
        </div>
        <div id="bcf-mother-pin" class="font-9">
            <div class="no">8</div>
            <label>ම‌වගේ අනන්‍යතා අංකය <br>தாயின் தனிநபர் அடையாள எண<br>Mother's PIN</label>
            <div class="current">
                <s:textfield value="%{#session.birthConfirmation.parent.motherNICorPIN}" cssClass="disable" disabled="true"/>
            </div>
            <div class="new">
                <s:textfield name="parent.motherNICorPIN"/>
            </div>
        </div>
        <div id="bcf-mother-race" class="font-9">
            <div class="no">9</div>
            <label>මවගේ ජාතිය <br>தாயின் இனம்<br>Mother's Race</label>
            <div class="current">
                <s:textfield value="%{getRaceList().get(#session.birthConfirmation.parent.motherRace.raceId)}" cssClass="disable" disabled="true"/>
            </div>
            <div class="new">
                <s:select list="raceList" name="motherRace" headerKey="0" headerValue="%{getText('select_race.label')}"/> 
            </div>
        </div>
        <div id="bcf-marital-status" class="font-9">
            <div class="no">10</div>
            <label>මව්පියන් විවාහකද? <br>பெற்றார் விவாகஞ் செய்தவர்களா? <br>Were Parents Married?</label>
            <div class="current">
                <s:textfield value="%{#session.birthConfirmation.marriage.parentsMarried}" cssClass="disable" disabled="true"/>
            </div>
            <div class="new">
                <label id="yes" class="label">*in sinhala<br>*in tamil<br>Yes</label>
                <s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'1':''}"/>
                <label class="label">*in sinhala<br>*in tamil<br>No</label>
                <s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'0':''}"/>
                <label class="label">*in sinhala<br>*in tamil<br>Since Married</label>
                <s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'2':''}"/>
            </div>
        </div>
        <s:hidden name="pageNo" value="1"/>
        <s:submit value="%{getText('nextButton.label')}" />
    </s:form>
</div>