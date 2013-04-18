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
            ලියාපදිංචි කිරීමේ අංකය<br>
            Registration No in ta<br>
            Registration Number
        </td>
        <td>
            <s:label value="%{adoption.adoptionEntryNo}"/>
        </td>
        <td>
            ලියාපදිංචි කිරීමේ දිනය
            <br>Date of Registration in ta
            <br>Date of Registration
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
            Court Order Number
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
        <td>අයදුම්කරුගේ විස්තර <br/>
            விண்ணப்பதாரரின் விபரங்கள்<br/>
            Applicants Details
        </td>
    </tr>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col width="320px"/>
    <col width="175px"/>
    <col width="175px"/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td>
            ද්විත්ව අයදුම්කරුවන්ද?
            <br>Are joint applicants in ta?
            <br>Are Joint Applicants?
        </td>
        <td colspan="4">
            <s:if test="adoption.jointApplicant">
                ඔව්
                <br>Yes in ta
                <br>Yes
            </s:if>
            <s:else>
                නැත
                <br>No in ta
                <br>No
            </s:else>
        </td>
    </tr>
    <tr>
        <td>
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification No.
        </td>
        <td colspan="4" align="left" class="find-person">
            <s:label value="%{#request.adoption.applicantPINorNIC}"/>
        </td>
    </tr>
    <tr>
        <td>
            විදේශිකයකු නම්
            <br>வெளிநாட்டவர் எனின்,
            <br>If a foreigner
        </td>
        <td>
            රට
            <br>நாடு
            <br>Country
        </td>
        <td><s:label value="%{#request.applicantCountryName}"/></td>
        <td>
            ගමන් බලපත්‍ර අංකය
            <br>கடவுச் சீட்டு இல.
            <br>Passport No.
        </td>
        <td><s:label value="%{#request.adoption.applicantPassport}"/></td>
    </tr>
    <tr>
        <td>
            අයදුම්කරුගේ නම
            <br>விண்ணப்பதாரரின் பெயர்
            <br>Name of the Applicant
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.applicantName}"/></td>
    </tr>
    <tr>
        <td>
            ලිපිනය 1
            <br>முகவரி 1
            <br>Address 1
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.applicantAddress}"/></td>
    </tr>
    <tr>
        <td>
            ලිපිනය 2
            <br>முகவரி 2
            <br>Address 2
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.applicantSecondAddress}"/></td>
    </tr>
    <tr>
        <td>
            රැකියාව
            <br>தொழில்
            <br>Occupation
        </td>
        <td colspan="4"><s:label value="%{#request.adoption.applicantOccupation}"/></td>
    </tr>
    </tbody>
</table>
<s:if test="adoption.jointApplicant">
    <table class="adoption-reg-form-header-table">
        <tr>
            <td><br/>
                සහකරුගේ විස්තර
                <br>Spouse's details in ta
                <br>Spouse's details
            </td>
        </tr>
    </table>
    <table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
        <caption></caption>
        <col width="320px"/>
        <col width="175px"/>
        <col width="175px"/>
        <col/>
        <col/>
        <tr>
            <td>
                සහකරුගේ අනන්‍යතා අංකය
                <br>Identification Number of Sopuse in ta
                <br>Identification Number of Sopuse
            </td>
            <td colspan="5" class="find-person">
                <s:label value="%{#request.adoption.spousePINorNIC}"/></td>
        </tr>
        <tr>
            <td>විදේශිකයකු නම් <br/>
                வெளிநாட்டவர் எனின்<br/>
                If a foreigner
            </td>
            <td>රට <br/>
                நாடு <br/>
                Country
            </td>
            <td width="175px"><s:label value="%{#request.spouseCountryName}"/></td>
            <td>ගමන් බලපත්‍ර අංකය <br/>
                கடவுச் சீட்டு இல. <br/>
                Passport No.
            </td>
            <td><s:label value="%{#request.adoption.spousePassport}"/></td>
        </tr>
        <tr>
            <td>
                සහකරුගේ නම
                <br>தாயின் பெயர்
                <br>Name of Spouse
            </td>
            <td colspan="4"><s:label value="%{#request.adoption.spouseName}"/></td>
        </tr>
        <tr>
            <td>
                සහකරුගේ රැකියාව
                <br>தாயின் தொழில்
                <br>Occupation of Spouse
            </td>
            <td colspan="4"><s:label value="%{#request.adoption.spouseOccupation}"/></td>
        </tr>
    </table>
</s:if>
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
    <caption></caption>
    <col width="330px"/>
    <col width="700px"/>
    <tbody>
    <tr>
        <td>
            උප්පැන්න සහතිකයේ අංකය<br/>
            Birth Certificate Number in ta<br/>
            Birth Certificate Number
        </td>
        <td colspan="4">
            <s:if test="#request.adoption.birthCertificateNumber!=0">
                <s:label value="%{#request.adoption.birthCertificateNumber}"/>
            </s:if>
        </td>
    </tr>
    <tr>
        <td>
            අනන්‍යතා අංකය <br/>
            அடையாள எண் <br/>
            Identification Number
        </td>
        <td colspan="4">
            <s:label value="%{#request.adoption.oldBirthSLIN}"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            පළාත
            <br>Province in ta
            <br>Province
        </td>
        <td colspan="4"><s:label value="%{#request.birthProvinceName}"/></td>
    </tr>
    <tr>
        <td colspan="1">
            දිස්ත්‍රික්කය
            <br>மாவட்டம்
            <br>District
        </td>
        <td colspan="4"><s:label value="%{#request.birthDistrictName}"/></td>
    </tr>
    <tr>
        <td colspan="1">
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය
            <br/>பிரதேச செயளாளர் பிரிவு
            <br/>Divisional Secretary Division
        </td>
        <td colspan="4">
            <s:label value="%{#request.adoption.oldBirthDSName}"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            ලියාපදිංචි කිරීමේ කොට්ටාශය
            <br>பதிவுப்பிரிவு
            <br>Registration Division
        </td>
        <td colspan="4">
            <s:label value="%{#request.adoption.oldBirthRegistrationDivisionName}"/>
        </td>
    </tr>
    <tr>
        <td>
            ලියාපදිංචි කළ දිනය<br/>
            Registration Date in ta<br/>
            Registration Date
        </td>
        <td colspan="4">
            <s:label value="%{#request.adoption.oldBirthRegistrationDate}"/>
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
    <s:submit action="eprSearchAdoptionRecord" value="%{getText('another_search.label')}"/>
</div>
</s:form>

</div>