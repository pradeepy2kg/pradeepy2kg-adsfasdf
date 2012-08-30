<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage() {
    }
</script>

<div id="adoption-registration-form-outer">
<s:form action="eprAdoptionBackToPreviousState" name="" id="" method="POST">

<table class="adoption-reg-form-header-table" cellspacing="1" cellpadding="1">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="450px"></td>
        <td align="center" style="font-size:12pt; width:130px"><img
                src="<s:url value="/images/official-logo.png"/>"
                alt=""/></td>
        <td width="450px"></td>
    </tr>
    <tr>
        <td colspan="3" align="center">දරුකමට හදාගැනීමේ උසාවි නියෝගය (අංක 4 දරන ආකෘති පත්‍රය) <br/>
            Adoption Order Issued by Court
        </td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <col width="350px">
    <col width="150px">
    <col width="300px">
    <col>
    <tr>
        <td>
            සටහනේ අංකය<br>
            பதிவு இலக்கம்<br>
            Number of Entry
        </td>
        <td>
            <s:label value="%{adoption.adoptionEntryNo}"/>
        </td>
        <td>
            සටහනේ දිනය
            <br>பதிவு திகத
            <br>Date of Entry
        </td>
        <td>
            <s:label value="%{adoption.orderReceivedDate}"/><br>
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/>
        </td>
    </tr>
    <tr>
        <td>අධිකරණය<br/>
            நீதிமன்றம் <br/>
            Court
        </td>
        <td colspan="3"><s:label name="courtName" id="court"/></td>
    </tr>
    <tr>
        <td>නියෝග අංකය<br/>
            கட்டளை இலக்கம்<br/>
            Serial number
        </td>
        <td><s:label value="%{#request.adoption.courtOrderNumber}"/></td>
        <td>නියෝගය නිකුත් කල දිනය <br/>
            கட்டளை வழங்கப்பட்ட திகதி<br/>
            Issued Date
        </td>
        <td style="text-align:left;"><s:label value="%{#request.adoption.orderIssuedDate}"/></td>
    </tr>
    <tr>
        <td>විනිසුරු නම <br/>
            நீதிபதியின் பெயர்<br/>
            Name of the Judge
        </td>
        <td colspan="3"><s:label value="%{#request.adoption.judgeName}"/></td>
    </tr>
    <tr>
        <td>සහතිකය නිකුත් කල යුතු භාෂාව
            <br>சான்றிதழ் வழங்கப்பட வேண்டிய மொழி
            <br>Preferred Language for
        </td>
        <s:set name="lang" value="%{#request.adoption.languageToTransliterate}"/>
        <td colspan="3">
            <s:label value="%{getText(#lang)}"
                     cssStyle="width:190px; margin-left:5px;"/>
        </td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>අයදුම් කරුගේ විස්තර <br/>
            விண்ணப்பதாரரின் விபரங்கள்<br/>
            Applicants Details
        </td>
    </tr>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col width="330px"/>
    <col width="175px"/>
    <col width="175px"/>
    <col width="175px"/>
    <col width="175px"/>
    <tbody>
    <tr>
        <td colspan="2">අයදුම් කරු <br/>
            விண்ணப்பதாரர்<br/>
            Applicant
        </td>
        <s:if test="#request.adoption.applicantMother==0">
            <td colspan="3">පියා<br/>
                தகப்பன்<br/>
                Father
            </td>
        </s:if>
        <s:else>

            <td colspan="3">මව <br/>
                தாய்<br/>
                Mother
            </td>
        </s:else>
    </tr>
    <tr>
        <td colspan="2">අයදුම් කරුගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br/>
            விண்ணப்பதாரரின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம் <br/>
            Applicant's PIN / NIC Number
        </td>
        <td colspan="3"><s:label value="%{#request.adoption.applicantPINorNIC}"/></td>
    </tr>
    <tr>
        <td>විදේශිකය‍කු නම් <br/>
            வெளிநாட்டவர் <br/>
            If a foreigner
        </td>
        <td>රට <br/>
            நாடு <br/>
            Country
        </td>
        <td>
            <s:label value="%{#request.applicantCountryName}"/>
        </td>
        <td>ගමන් බලපත්‍ර අංකය <br/>
            கடவுச் சீட்டு இல. <br/>
            Passport No.
        </td>
        <td><s:label value="%{#request.adoption.applicantPassport}"/></td>
    </tr>
    <tr>
        <td>නම <br/>
            விண்ணப்பதாரரின் பெயர்<br/>
            Name of the Applicant
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.applicantName}"/></td>
    </tr>

    <tr>
        <td>ලිපිනය <br/>
            முகவரி<br/>
            Address
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.applicantAddress}"/></td>
    </tr>
    </tbody>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td><br/>
            අයදුම් කරු පියා නම් මවගේ විස්තර /விண்ணப்பதாரர் தந்தையாயின் தாயின் விபரங்கள் / If applicant is the father,
            Mother's details
        </td>
    </tr>
</table>
<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <tr>
        <td colspan="3" width="680px">මවගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br/>
            தாயின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம் <br/>
            Wife's PIN / NIC Number
        </td>
        <td colspan="2"><s:label value="%{#request.adoption.wifePINorNIC}"/></td>
    </tr>
    <tr>
        <td width="330px">විදේශිකය‍කු නම් <br/>
            வெளிநாட்டவர் <br/>
            If a foreigner
        </td>
        <td width="175px">රට <br/>
            நாடு <br/>
            Country
        </td>
        <td width="175px">
            <s:label value="%{#request.wifeCountryName}"/>
        </td>
        <td width="175px">ගමන් බලපත්‍ර අංකය <br/>
            கடவுச் சீட்டு இல. <br/>
            Passport No.
        </td>
        <td width="175px">
            <s:label value="%{#request.adoption.wifePassport}"/>
        </td>
    </tr>
    <tr>
        <td> මවගේ නම <br/>
            தாயின் பெயர்<br/>
            Name of Mother
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.wifeName}"/></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>ළමයාගේ විස්තර <br/>
            பிள்ளையின் விபரங்கள்<br/>
            Child's Information
        </td>
    </tr>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col style="width:330px;"/>
    <col style="width:160px;"/>
    <col style="width:190px;"/>
    <col style="width:160px;"/>
    <col style="width:190px;"/>
    <tbody>
    <tr>
        <td>උපන් දිනය<br/>
            பிறந்த திகதி<br/>
            Date of Birth
        </td>
        <td colspan="2" style="text-align:left;"><s:label value="%{#request.adoption.childBirthDate}"/></td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br/>
            பால்<br/>
            Gender
        </td>
        <td>
            <s:if test="#request.adoption.childGender == 0">
                <s:label name="" value="%{getText('male.label')}"/>
            </s:if>
            <s:elseif test="#request.adoption.childGender == 1">
                <s:label name="" value="%{getText('female.label')}"/>
            </s:elseif>
            <s:elseif test="#request.adoption.childGender == 2">
                <s:label name="" value="%{getText('unknown.label')}"/>
            </s:elseif></td>
    </tr>
    <tr>
        <td>වයස <br/>
            வயது<br/>
            Age
        </td>
        <td>අවුරුදු <br/>
            வருடங்கள்<br/>
            Years
        </td>
        <td><s:label value="%{#request.adoption.childAgeYears}"/></td>
        <td>මාස <br/>
            மாதங்கள்<br/>
            Months
        </td>
        <td><s:label value="%{#request.adoption.childAgeMonths}"/></td>
    </tr>
    <tr>
        <td>දැනට පවතින නම <br/>
            (නමක් දී ඇති නම්) <br/>
            தற்போதைய பெயர்<br/>
            (ஏற்கனவே பெயர் குறிப்பிடப்பட்டிருந்தால் )<br/>
            Existing Name <br/>
            (if already given)
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.childExistingName}"/></td>
    </tr>
    <tr>
        <td>ලබා දෙන නම <br/>
            பெற்றுக் கொடுக்கப்படும் பெயர்<br/>
            New name given
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.childNewName}"/></td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td>උපත දැනටත් ලියාපදිංචි කර උප්පැන්න සහතිකයක් නිකුත් කර ඇතිනම් <br/>
            பிறப்பு ஏற்கனவே பதியப்பட்டு பிறப்புச் சான்றிதழ் வழங்கப்பட்டிருந்தால்<br/>
            If the birth is already registered, and a Birth Certificate issued
        </td>
    </tr>
</table>


<table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
    <tr>
        <td width="70%">උප්පැන්න සහතිකයේ අනුක්‍රමික අංකය <br/>
            பிறப்புச் சான்றிதழின் தொடர் இலக்கம்<br/>
            The serial number of the Birth Certificate
        </td>
        <td width="30%">
            <s:if test="#request.adoption.birthCertificateNumber!=0"><s:label
                    value="%{#request.adoption.birthCertificateNumber}" cssStyle="width:85%;"/></s:if></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>හෝ<br/> அல்லது <br/>OR
        </td>
    </tr>
</table>
<table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col width="330px"/>
    <col width="700px"/>
    <tbody>
    <tr>
        <td colspan="2" style="text-align:center;">උපත ලියපදින්ච්චි කිරීමේ රිසීට් පතේ සටහන් <br/>
            பிறப்பு பதிவு பற்றுச்சீட்டின் குறிப்பு<br/>
            Birth Registration acknowledgement slip
        </td>
    </tr>
    <tr>
        <td>දිස්ත්‍රික්කය <br/>
            மாவட்டம்<br/>
            District
        </td>
        <td><s:label value="%{#request.birthDistrictName}"/>
        </td>
    </tr>
    <tr>
        <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය
            பிரதேச செயலாளர் பிரிவு<br/>
            <br/>Divisional Secretary Division
        </td>
        <td>
            <s:label value="%{#request.dsDivisionName}" cssStyle="float:left;  width:240px;"/>
        </td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ටාශය <br/>
            பதிவுப் பிரிவு<br/>
            Registration Division
        </td>
        <td>
            <s:label value="%{#request.birthDivisionName}" cssStyle=" width:240px;float:left;"/>
        </td>
    </tr>
    <tr>
        <td>අනුක්‍රමික අංකය <br/>
            தொடர் இலக்கம்<br/>
            Serial Number
        </td>
        <td>
            <s:if test="#request.adoption.birthRegistrationSerial !=0">
                <s:label value="%{#request.adoption.birthRegistrationSerial}" cssStyle="width:200px"/>
            </s:if>
        </td>
    </tr>
    </tbody>
</table>

<div class="button" align="right">
    <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
    <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
    <s:hidden name="currentStatus" value="%{#request.currentStatus}"/>
    <s:hidden name="pageNo" value="%{#request.pageNo}"/>
    <s:submit value="%{getText('back.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>

</div>