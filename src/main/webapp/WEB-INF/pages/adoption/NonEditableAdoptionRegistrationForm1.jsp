<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage(){}
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
    <tr>
        <td width="330px">නියෝගය ලැබුණු දිනය <br/>
            Received Date
        </td>
        <td style="text-align:left;"><s:label name="adoption.orderReceivedDate"
                                              value="%{#request.adoption.orderReceivedDate}"/></td>
    </tr>
    <tr>
        <td>අධිකරණය<br/>
            Court
        </td>
        <td><s:label name="courtName" id="court"/></td>
    </tr>
    <tr>
        <td>නියෝගය නිකුත් කල දිනය <br/>
            Issued Date
        </td>
        <td style="text-align:left;"><s:label value="%{#request.adoption.orderIssuedDate}"/></td>
    </tr>
    <tr>
        <td>නියෝග අංකය<br/>
            Serial number
        </td>
        <td><s:label value="%{#request.adoption.courtOrderNumber}"/></td>
    </tr>
    <tr>
        <td>විනිසුරු නම <br/>
            Name of the Judge
        </td>
        <td><s:label value="%{#request.adoption.judgeName}"/></td>
    </tr>
    <tr>
        <td>සහතිකය නිකුත් කල යුතු භාෂාව
            <br>சான்றிதழ் வழங்கப்பட வேண்டிய மொழி 
            <br>Preferred Language for
        </td>
        <s:set name="lang" value="%{#request.adoption.languageToTransliterate}"/>
        <td>
            <s:label value="%{getText(#lang)}"
                     cssStyle="width:190px; margin-left:5px;"/>
        </td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>අයදුම් කරුගේ විස්තර <br/>
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
            Applicant
        </td>
        <s:if test="#request.adoption.applicantMother==0">
            <td colspan="3">පියා<br/>
                Father
            </td>
        </s:if>
        <s:else>

            <td colspan="3">මව <br/>
                Mother
            </td>
        </s:else>
    </tr>
    <tr>
        <td colspan="2">අයදුම් කරුගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br/>
            தாயின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம் <br/>
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
            கடவுச் சீட்டு <br/>
            Passport No.
        </td>
        <td><s:label value="%{#request.adoption.applicantPassport}"/></td>
    </tr>
    <tr>
        <td>නම <br/>
            Name of the Applicant
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.applicantName}"/></td>
    </tr>

    <tr>
        <td>ලිපිනය <br/>
            Address
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.applicantAddress}"/></td>
    </tr>
    </tbody>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td><br/>
            අයදුම් කරු පියා නම් මවගේ විස්තර / If applicant is the father, Mother's details
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
            கடவுச் சீட்டு <br/>
            Passport No.
        </td>
        <td width="175px">
            <s:label value="%{#request.adoption.wifePassport}"/>
        </td>
    </tr>
    <tr>
        <td> මවගේ නම <br/>
            Name of Mother
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.wifeName}"/></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>ළමයාගේ විස්තර <br/>
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
            Date of Birth
        </td>
        <td colspan="2" style="text-align:left;"><s:label value="%{#request.adoption.childBirthDate}"/></td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br/>
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
            Age
        </td>
        <td>අවුරුදු <br/>
            Years
        </td>
        <td><s:label value="%{#request.adoption.childAgeYears}"/></td>
        <td>මාස <br/>
            Months
        </td>
        <td><s:label value="%{#request.adoption.childAgeMonths}"/></td>
    </tr>
    <tr>
        <td>දැනට පවතින නම <br/>
            (නමක් දී ඇති නම්) <br/>
            Existing Name <br/>
            (if already given)
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.childExistingName}"/></td>
    </tr>
    <tr>
        <td>ලබා දෙන නම <br/>
            New name given
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.childNewName}"/></td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td>උපත දැනටත් ලියාපදිංචි කර උප්පැන්න සහතිකයක් නිකුත් කර ඇතිනම් <br/>
            If the birth is already registered, and a Birth Certificate issued
        </td>
    </tr>
</table>


<table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
    <tr>
        <td width="70%">උප්පැන්න සහතිකයේ අනුක්‍රමික අංකය <br/>
            The serial number of the Birth Certificate
        </td>
        <td width="30%">
            <s:if test="#request.adoption.birthCertificateNumber!=0"><s:label
                    value="%{#request.adoption.birthCertificateNumber}" cssStyle="width:85%;"/></s:if></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>හෝ<br/>OR
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
            Birth Registration acknowledgement slip
        </td>
    </tr>
    <tr>
        <td>දිස්ත්‍රික්කය <br/>
            District
        </td>
        <td><s:label value="%{#request.birthDistrictName}"/>
        </td>
    </tr>
    <tr>
        <td>ප්‍රාදේශීය ලේකම් කොට්ටාශය <br/>
            Divisional Secretariat
        </td>
        <td>
            <s:label value="%{#request.dsDivisionName}" cssStyle="float:left;  width:240px;"/>
        </td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ටාශය <br/>
            Registration Division
        </td>
        <td>
            <s:label value="%{#request.birthDivisionName}" cssStyle=" width:240px;float:left;"/>
        </td>
    </tr>
    <tr>
        <td>අනුක්‍රමික අංකය <br/>
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