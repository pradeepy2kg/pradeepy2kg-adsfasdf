<%--
 @author amith jayasekara
--%>
<%@ page import="lk.rgd.crs.api.domain.BirthDeclaration" %>
<%@ page import="lk.rgd.crs.api.domain.ChildInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<div class="birth-confirmation-form-outer">
<s:form action="eprBirthConfirmationPrint" name="birthConfirmationPrintForm1" id="birth-confirmation-print-form-1"
        method="POST">
<div id="page1" style="page-break-after:always;">
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
            <label><span class="font-8">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</span><s:textfield
                    name=""/></label>
        </div>
    </div>
    <div id="brf-serial-no-and-date" class="font-9">
        <div class="no">1</div>
        <label>
            සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ අදාල “උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර” ප්‍රකාශනයේ අනුක්‍රමික අංකය
            හා දිනය
            <br>பிறப்பை பதிவு செய்வதற்கான விபரம்" எனும் படிவத்தின் தொடா் இலக்கமும் திகதியும்
            <br>Serial Number and the Date of the ‘Particulars for Registration of a Birth’ form</label>
        <s:textfield cssClass="disable" disabled="true" value="%{#request.register.bdfSerialNo}"/>
        <s:textfield cssClass="disable" disabled="true"
                     value="%{#request.register.dateOfRegistration}"/>
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
                <s:textfield value="%{#request.child.dateOfBirth.year + 1900}" cssClass="disable"
                             disabled="true"/>
            </div>
            <div id="current-month" class="font-7">
                <label>*in Sinhala<br>*in Tamil<br>Day</label>
                <s:textfield value="%{#request.child.dateOfBirth.month + 1}" cssClass="disable"
                             disabled="true"/>
            </div>
            <div id="current-day" class="font-7">
                <label>*in Sinhala<br>*in Tamil<br>Day</label>
                <s:textfield value="%{#request.child.dateOfBirth.date}" cssClass="disable"
                             disabled="true"/>
            </div>
        </div>
        <div class="new">

        </div>
    </div>
    <div id="bcf-gender" class="font-9" style="border-top:none">
        <div class="no">4</div>
        <label>ස්ත්‍රී පුරුෂ භාවය <br>பால்பால்<br>Gender</label>

        <div class="current-print">
            <s:if test="child.childGender == 0">
                <s:textfield value="%{getText('male.label')}" cssClass="disable" disabled="true"/>
            </s:if>
            <s:elseif test="child.childGender == 1">
                <s:textfield value="%{getText('female.label')}" cssClass="disable" disabled="true"/>
            </s:elseif>
            <s:elseif test="child.childGender == 2">
                <s:textfield value="%{getText('unknown.label')}" cssClass="disable" disabled="true"/>
            </s:elseif>
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
            <s:textfield value="%{#request.register.birthDivision.dsDivision.district.siDistrictName}"
                         cssClass="disable" disabled="true"/>
        </div>
    </div>
    <div id="bcf-ds-division" class="font-9">
        <div class="no"></div>
        <label>D.S.කොට්ඨාශය<br>பிரிவு <br>D.S. Division</label>

        <div class="current-print">
            <s:textfield value="%{#request.register.birthDivision.dsDivision.siDivisionName}"
                         cssClass="disable" disabled="true"/>
        </div>

    </div>
    <div id="bcf-division" class="font-9">
        <div class="no"></div>
        <label>කොට්ඨාශය<br>பிரிவு <br>Registration Division</label>

        <div class="current-print">
            <s:textfield value="%{#request.register.birthDivision.siDivisionName}" cssClass="disable"
                         disabled="true"/>
        </div>
    </div>
    <div id="bcf-place" class="font-9">
        <div class="no"></div>
        <label>ස්ථානය <br>பிறந்த இடம் <br>Place</label>

        <div class="current-print">
            <s:textfield value="%{#request.child.placeOfBirth}" cssClass="disable" disabled="true"/>
        </div>

    </div>
    <div id="bcf-father-pin" class="font-9">
        <div class="no">6</div>
        <label>පියාගේ අනන්‍යතා අංකය <br>தந்நையின் தனிநபர் அடையாள எண்<br>Father's PIN</label>

        <div class="current-print">
            <s:textfield value="%{#request.parent.fatherNICorPIN}" cssClass="disable"
                         disabled="true"/>
        </div>
    </div>
    <div id="bcf-father-race" class="font-9">
        <div class="no">7</div>
        <label>පියාගේ ජාතිය <br>தந்நையின் இனம்<br>Father's Race</label>

        <div class="current-print">
            <s:textfield value="%{#request.parent.fatherRace.siRaceName}" cssClass="disable"
                         disabled="true"/>
        </div>
    </div>
    <div id="bcf-mother-pin" class="font-9">
        <div class="no">8</div>
        <label>ම‌වගේ අනන්‍යතා අංකය <br>தாயின் தனிநபர் அடையாள எண<br>Mother's PIN</label>

        <div class="current-print">
            <s:textfield value="%{#request.parent.motherNICorPIN}" cssClass="disable"
                         disabled="true"/>
        </div>
    </div>
    <div id="bcf-mother-race" class="font-9">
        <div class="no">9</div>
        <label>මවගේ ජාතිය <br>தாயின் இனம்<br>Mother's Race</label>

        <div class="current-print">
            <s:textfield value="%{#request.parent.motherRace.siRaceName}" cssClass="disable"
                         disabled="true"/>
        </div>
    </div>
    <div id="bcf-marital-status" class="font-9">
        <div class="no">10</div>
        <label>මව්පියන් විවාහකද? <br>பெற்றார் விவாகஞ் செய்தவர்களா? <br>Were Parents Married?</label>

        <div class="current-print">
            <s:textfield value="%{getText('married.status.'+#request.marriage.parentsMarried)}"
                         cssClass="disable" disabled="true"></s:textfield>

        </div>
    </div>
</div>
<div id="page2" style="page-break-after:always;">
    <div id="civil-confirmation-info-sub-title" class="form-sub-title">
        * in Sinhala <br>* in Tamil *<br> Changes in Names
    </div>
    <div id="bcf-child-current-name" class="font-9">
        <div class="no">11</div>
        <label>ළම‌යාගේ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ) <br>பிறப்பு அத்... (சிங்களம் / தமிழ்) <br>Childs name in the
            official languages (Sinhala / Tamil)</label>
        <s:textfield cssClass="disable" disabled="true"
                     value="%{#request.child.childFullNameOfficialLang}"></s:textfield>
    </div>
    <div id="bcf-child-current-name-in-english" class="font-9">
        <div class="no">12</div>
        <label>ළම‌යාගේ නම ඉංග්‍රීසි භාෂාවෙන්<br>பிறப்பு ... <br>Childs name in English</label>
        <s:textfield cssClass="disable" disabled="true"
                     value="%{#request.child.childFullNameEnglish}" size="50"></s:textfield>
    </div>
    <div id="bcf-father-current-name" class="font-9">
        <div class="no">13</div>
        <label>පියාගේ සම්පුර්ණ නම <br>தந்நையின் முழுப் பெயர்<br>Father's Full Name</label>
        <s:textfield cssClass="disable" disabled="true"
                     value="%{#request.parent.fatherFullName}"></s:textfield>
    </div>

    <div id="bcf-mother-current-name" class="font-9">
        <div class="no">14</div>
        <label>මවගේ සම්පූර්ණ නම <br>தாயின் முழுப் பெயர்<br>Mother's Full Name</label>
        <s:textfield cssClass="disable" disabled="true"
                     value="%{#request.parent.motherFullName}"></s:textfield>
    </div>
</div>
<div id="page3" class="page_break">
    <div id="civil-confirmation-info-sub-title" class="form-sub-title">
        උපත තහවුරු කරන්නාගේ විස්තර <br>* in Tamil <br>Person confirming the birth details
    </div>
    <div id="bcf-confirm-person-nic" class="font-9">
        <div class="no">15</div>
        <label>
            උපත තහවුරු කරන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
            <br>பிறப்​பை உறுதிப்படுத்துபவரின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை எண்
            <br>PIN / NIC of person confirming the birth details
        </label>
        <s:textfield cssClass="disable" disabled="true"
                     value="%{#request.confirmant.confirmantNICorPIN}"></s:textfield>
    </div>
    <div id="bcf-confirm-person-name" class="font-9">
        <div class="no">16</div>
        <label>
            උපත තහවුරු කරන්නාගේ සම්පූර්ණ නම
            <br>பிறப்​பை உறுதிப்படுத்துபவரின் முழுப் பெயர்
            <br>Full Name of the person confirming the birth details</label>
        <s:textfield cssClass="disable" disabled="true"
                     value="%{#request.confirmant.confirmantFullName}"></s:textfield>
    </div>
    <div id="bcf-confirm-person-certify" class="font-9">
        <div class="no">17</div>
        <label>
            ඉහත සදහන් තොරතුරු නිවැරදි බව සහතික කරමි
            <br>மேற்குறிப்பிட்ட விபரங்கள் சரியானவை என இத்தால் உறுதிப்படுத்துகிறேன்.
            <br>I hereby certify that the above information are correct </label>

        <div id="bcf-confirm-person-certify-date">
            <label>දිනය<br>திகதி <br>Date</label>

        </div>
    </div>
</div>

<s:submit value="%{getText('print.label')}" onclick="print()" type="button"/>
</s:form>
</div>

