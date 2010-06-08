<%--
 @author amith jayasekara
--%>
<%@ page import="lk.rgd.crs.api.domain.BirthDeclaration" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<div class="birth-confirmation-form-outer">
    <s:form action="eprBirthConfirmationPrint" name="birthConfirmationPrintForm1" id="birth-confirmation-print-form-1" method="POST">
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
            <div id="current-info-print">
                <label>සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ දැනට අඩංගු විස්තර <br>சிவில் பதிவு அமைப்பில் உள்ளடக்கப்பட்டுள்ள
                    விபரம<br>Information included in Civil Registration System </label>
            </div>

        </div>
        <div id="bcf-dob" class="font-9">
            <div class="no">3</div>
            <label>උපන් දිනය<br>பிறந்த திகதி<br>Date of birth</label>

            <div class="current-print">
                <div id="current-year" class="font-7">
                    <label>*in Sinhala<br>*in Tamil<br>Year</label>
                    <s:textfield value="%{#session.birthRegister.child.dateOfBirth.year + 1900}" cssClass="disable" disabled="true"/>
                </div>
                <div id="current-month" class="font-7">
                    <label>*in Sinhala<br>*in Tamil<br>Day</label>
                    <s:textfield value="%{#session.birthRegister.child.dateOfBirth.month + 1}" cssClass="disable" disabled="true"/>
                </div>
                <div id="current-day" class="font-7">
                    <label>*in Sinhala<br>*in Tamil<br>Day</label>
                    <s:textfield value="%{#session.birthRegister.child.dateOfBirth.date}" cssClass="disable" disabled="true" />
                </div>
            </div>
            <div class="new">

            </div>
        </div>
        <div id="bcf-gender" class="font-9">
            <div class="no">4</div>
            <label>ස්ත්‍රී පුරුෂ භාවය <br>பால்பால்<br>Gender</label>
            <div class="current-print">
                <% switch (((BirthDeclaration)(session.getAttribute("birthRegister"))).getChild().getChildGender()){case 0:%>
                <s:textfield value="%{getText('male.label')}" cssClass="disable" disabled="true"/>
                <% break;case 1: %>
                <s:textfield value="%{getText('female.label')}" cssClass="disable" disabled="true"/>
                <% break;case 2: %>
                <s:textfield value="%{getText('unknown.label')}" cssClass="disable" disabled="true"/>
                <% }%>
            </div>
        </div>
        <div id="bcf-pob" class="font-9">
            <div class="no">5</div>
            <label>උපන් ස්ථානය / பிறந்தபிறந்த இடம் / Place of birth</label>
        </div>
        <div id="bcf-district" class="font-9">
            <div class="no"></div>
            <label>දිස්ත්‍රික්කය <br>மாவட்டம் <br>District</label>
            <div class="current-print">
                <s:textfield value="%{#session.birthRegister.register.birthDivision.dsDivision.district.districtId}" cssClass="disable" disabled="true"/>
            </div>
        </div>
        <div id="bcf-ds-division" class="font-9">
            <div class="no"></div>
            <label>D.S.කොට්ඨාශය<br>பிரிவு <br>D.S. Division</label>
            <div class="current-print">
                <s:textfield value="%{#session.birthRegister.register.birthDivision.dsDivision.divisionId}" cssClass="disable" disabled="true"/>
            </div>

        </div>
        <div id="bcf-division" class="font-9">
            <div class="no"></div>
            <label>කොට්ඨාශය<br>பிரிவு <br>Registration Division</label>
            <div class="current-print">
                <s:textfield value="%{#session.birthRegister.register.birthDivision.divisionId}" cssClass="disable" disabled="true"/>
            </div>
        </div>
        <div id="bcf-place" class="font-9">
            <div class="no"></div>
            <label>ස්ථානය  <br>பிறந்த இடம் <br>Place</label>
            <div class="current-print">
                <s:textfield value="%{#session.birthRegister.child.placeOfBirth}" cssClass="disable" disabled="true"/>
            </div>

        </div>
        <div id="bcf-father-pin" class="font-9">
            <div class="no">6</div>
            <label>පියාගේ අනන්‍යතා අංකය <br>தந்நையின் தனிநபர் அடையாள எண்<br>Father's PIN</label>
            <div class="current-print">
                <s:textfield value="%{#session.birthRegister.parent.fatherNICorPIN}" cssClass="disable" disabled="true"/>
            </div>
        </div>
        <div id="bcf-father-race" class="font-9">
            <div class="no">7</div>
            <label>පියාගේ ජාතිය <br>தந்நையின் இனம்<br>Father's Race</label>
            <div class="current-print">
                <s:textfield value="%{#session.birthRegister.parent.fatherRace.raceId}" cssClass="disable" disabled="true"/>
            </div>
        </div>
        <div id="bcf-mother-pin" class="font-9">
            <div class="no">8</div>
            <label>ම‌වගේ අනන්‍යතා අංකය <br>தாயின் தனிநபர் அடையாள எண<br>Mother's PIN</label>
            <div class="current-print">
                <s:textfield value="%{#session.birthRegister.parent.motherNICorPIN}" cssClass="disable" disabled="true"/>
            </div>
        </div>
        <div id="bcf-mother-race" class="font-9">
            <div class="no">9</div>
            <label>මවගේ ජාතිය <br>தாயின் இனம்<br>Mother's Race</label>
            <div class="current-print">
                <s:textfield value="%{#session.birthRegister.parent.motherRace}" cssClass="disable" disabled="true"/>
            </div>
        </div>
        <div id="bcf-marital-status" class="font-9">
            <div class="no">10</div>
            <label>මව්පියන් විවාහකද? <br>பெற்றார் விவாகஞ் செய்தவர்களா? <br>Were Parents Married?</label>
            <div class="current-print">
                <s:textfield value="%{#session.birthRegister.marriage.parentsMarried}" cssClass="disable" disabled="true"/>
            </div>
        </div>
        <s:hidden name="pageNo" value="1"/>
        <s:submit value="%{getText('nextButton.label')}" />
            <s:submit value="%{getText('print.label')}" onclick="print()" type="button"/>
    </s:form>
</div>

