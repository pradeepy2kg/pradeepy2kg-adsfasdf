<%@ page import="lk.rgd.crs.web.WebConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" type="text/css" href='/ecivil/css/menu.css'/>

<div id="xmain-menu">
<ul class="menu">
<s:if test="%{#session.context=='birth'}">
<li class="exp">
    </s:if>
    <s:else>
<li>
    </s:else>
    <a href="/ecivil/births/eprBirthRegistrationHome.do">
        උපත් ලියාපදිංචිය
    </a>
    <ul class="acitem">
        <li>
            <a id="birth_registration.label" href="/ecivil/births/eprBirthRegistrationInit.do">
                (1) සජීවී උපතක් ලියාපදිංචි කිරීම
            </a>
        </li>
        <li>
            <a id="still_birth_registration.label" href="/ecivil/births/eprStillBirthRegistrationInit.do">
                (1) මළ උපතක් ලියාපදිංචිය
            </a>
        </li>
        <li>
            <a id="birth_register_approval.label" href="/ecivil/births/eprBirthRegisterApproval.do">
                (2) උපත් ලියාපදිංචි අනුමතය
            </a>
        </li>
        <li>
            <a id="birth_confirmation_print.label" href="/ecivil/births/eprBirthConfirmationPrintList.do">
                (3) උපත් තහවුරු කිරීම් මුද්‍රණය
            </a>
        </li>
        <li>
            <a id="birth_confirmation.label" href="/ecivil/births/eprBirthConfirmationInit.do">
                (4) උපත් තහවුරු කිරීම
            </a>
        </li>
        <li>
            <a id="print_birthcertificate.label" href="/ecivil/births/eprBirthCertificateList.do">
                (6) උප්පැන්න සහතික මුද්‍රණය
            </a>
        </li>
    </ul>
</li>

<s:if test="%{#session.context=='death'}">
<li class="exp">
    </s:if>
    <s:else>
<li>
    </s:else>
    <a href="/ecivil/deaths/eprInitDeathHome.do">
        මරණ ලියාපදිංචිය
    </a>
    <ul class="acitem">
        <li>
            <a id="death_registration.label" href="/ecivil/deaths/eprInitDeathDeclaration.do">
                (1) සාමාන්‍ය මරණ ලියාපදිංචිය
            </a>
        </li>
        <li>
            <a id="late_death_registration.label" href="/ecivil/deaths/eprInitLateDeathDeclaration.do">
                (1)කාලය ඉකුත් වූ මරණ ලියාපදිංචිය
            </a>
        </li>
        <li>
            <a id="sudden_death_registration.label" href="/ecivil/deaths/eprInitSuddenDeathDeclaration.do">
                (1)හදිසි මරණ ලියාපදිංචිය
            </a>
        </li>
        <li>
            <a id="missing_person_registration.label" href="/ecivil/deaths/eprInitMissingPersonDeclaration.do">
                (1)නැතිවුණු පුද්ගලයෙකුගේ මරණ ලියාපදිංචිය
            </a>
        </li>
        <li>
            <a id="death_approve_print_list.label" href="/ecivil/deaths/eprDeathApprovalAndPrint.do">
                (2) මරණ අනුමැතිය සහ මුද්‍රණය
            </a>
        </li>
    </ul>
</li>

<s:if test="%{#session.context=='adoption'}">
<li class="exp">
    </s:if>
    <s:else>
<li>
    </s:else>
    <a href="/ecivil/adoption/eprAdoptionRegistrationHome.do">
        දරුකමට හදාගැනීම ලියාපදිංචිය
    </a>
    <ul class="acitem">
        <li>
            <a id="adoption_registration.label" href="/ecivil/adoption/eprAdoptionRegistrationAction.do">
                (1) දරුකමට හදාගැනීම ලියාපදිංචිය
            </a>
        </li>
        <li>
            <a id="adoption_approval_and_print.lable" href="/ecivil/adoption/eprAdoptionApprovalAndPrint.do">
                (2) දරුකමට හදාගැනීම අනුමත කිරීම සහ මුද්‍රණය
            </a>
        </li>
        <li>
            <a id="adoption_applicant.label" href="/ecivil/adoption/eprAdoptionApplicantInfo.do">
                (3) අයදුම්කරුගේ විස්තර
            </a>
        </li>
        <li>
            <a id="adoption_re_registration.label" href="/ecivil/adoption/eprAdoptionReRegistration.do">
                (4) දරුකමට හදාගැනීම දෙවන ලියාපදිංචිය
            </a>
        </li>
    </ul>
</li>

<s:if test="%{#session.context=='alteration'}">
<li class="exp">
    </s:if>
    <s:else>
<li>
    </s:else>
    <a href="/ecivil/alteration/eprBirthAlterationHome.do">
        වෙනස් කිරීම් සිදුකිරීම
    </a>
    <ul class="acitem">
        <li>
            <a id="birth_alteration.label" href="/ecivil/alteration/eprBirthAlterationInit.do">
                උප්පැන්න සහතිකයේ වෙනස් කිරීම්
            </a>
        </li>
        <li>
            <a id="birth_alteration_pending_approval.title"
               href="/ecivil/alteration/eprBirthAlterationPendingApproval.do">
                උප්පැන්න සටහනක විස්තර වෙනස් කිරීම අනුමත කිරීම
            </a>
        </li>
        <li>
            <a id="death.registration.alteration" href="/ecivil/alteration/eprDeathAlterationSearchHome.do">
                මරණ සහතිකයේ වෙනස් කම් කිරිම්
            </a>
        </li>
        <li>
            <a id="label.manage.alterations" href="/ecivil/alteration/eprApproveDeathAlterationsInit.do">
                මරණ සහතිකයේ වෙනස් කිරීම හැසිරවීම
            </a>
        </li>
    </ul>
</li>

<s:if test="%{#session.context=='preference'}">
<li class="exp">
    </s:if>
    <s:else>
<li>
    </s:else>
    <a href="#">
        පුද්ගලික තේරීම්
    </a>
    <ul class="acitem">
        <li>
            <a id="userPreference.label" href="/ecivil/preferences/eprUserPreferencesInit.do">
                පරිශීලක අභිමතය
            </a>
        </li>
        <li>
            <a id="changePassword.label" href="/ecivil/preferences/eprpassChangePageLoad.do">
                මුරපදය වෙනස් කිරීම
            </a>
        </li>
    </ul>
</li>

<s:if test="%{#session.context=='prs'}">
<li class="exp">
    </s:if>
    <s:else>
<li>
    </s:else>
    <a href="#">
        පුද්ගල ලියාපදිංචි සේවා
    </a>
    <ul class="acitem">
        <li>
            <a id="prs.personRegistration.label" href="/ecivil/prs/eprExistingPersonRegInit.do">
                පුද්ගල ලියාපදිංචිය
            </a>
        </li>
    </ul>
</li>

<s:if test="%{#session.context=='certificateSearch'}">
<li class="exp">
    </s:if>
    <s:else>
<li>
    </s:else>
    <a href="#">
        වාර්තා සෙවුම
    </a>
    <ul class="acitem">
        <li>
            <a id="search.label" href="/ecivil/births/eprSearchPageLoad.do">
                උපත් ප්‍රකාශන / තහවුරු සෙවීම
            </a>
        </li>
        <li>
            <a id="birth_certificate_search.label" href="/ecivil/births/eprBirthCertificateSearch.do">
                උප්පැන්න සහතික සෙවීම
            </a>
        </li>
        <li>
            <a id="death_certificate_search.label" href="/ecivil/deaths/eprDeathCertificateSearch.do">
                මරණ සහතික සෙවීම
            </a>
        </li>
        <li>
            <a id="birth.advanceSearch.label" href="/ecivil/births/eprBirthsAdvancedSearch.do">
                උපත් වාර්තා සෙවීම
            </a>
        </li>
    </ul>
</li>

<s:if test="%{#session.context=='marriage'}">
<li class="exp">
    </s:if>
    <s:else>
<li>
    </s:else>
    <a href="/ecivil/marriages/eprMarriageRegistrationHome.do">
        විවාහ ලියාපදිංචිය
    </a>
    <ul class="acitem">
        <li>
            <a id="menu.marriage.notice" href="/ecivil/marriages/eprSelectNoticeType.do">
                විවාහ දැන්වීම
            </a>
        </li>
        <li>
            <a id="menu.marriage.register.search" href="/ecivil/marriages/eprMarriageRegisterSearchInit.do">
                විවාහ ලේකම් පොත සේවීම
            </a>
        </li>
        <li>
            <a id="menu.marriagelicense.search" href="/ecivil/marriages/eprMarriageLicenseSearchInit.do">
                විවාහ බලපත්‍රය සෙවීම
            </a>
        </li>
        <li>
            <a id="menu.marriage.registration" href="/ecivil/marriages/eprMarriageRegistrationInit.do">
                විවාහ ලියාපදිංචිය
            </a>
        </li>
        <li>
            <a id="menu.marriage.notice.search" href="/ecivil/marriages/eprMarriageNoticeSearchInit.do">
                විවාහ දැන්වීම් සෙවීම
            </a>
        </li>
    </ul>
</li>
</ul>
</div>