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
        BIRTH REGISTRATION
    </a>
    <ul class="acitem">
        <li>
            <a id="birth_registration.label" href="/ecivil/births/eprBirthRegistrationInit.do">
                (1) Birth Registration
            </a>
        </li>
        <li>
            <a id="still_birth_registration.label" href="/ecivil/births/eprStillBirthRegistrationInit.do">
                (1) Still Birth Registration
            </a>
        </li>
        <li>
            <a id="birth_register_approval.label" href="/ecivil/births/eprBirthRegisterApproval.do"
               style="color:red">
                (2) Birth Registration Approval
            </a>
        </li>
        <li>
            <a id="birth_confirmation_print.label" href="/ecivil/births/eprBirthConfirmationPrintList.do">
                (3) Birth Confirmation Print
            </a>
        </li>
        <li>
            <a id="birth_confirmation.label" href="/ecivil/births/eprBirthConfirmationInit.do">
                (4) Birth Confirmation
            </a>
        </li>
        <li>
            <a id="birth_confirmation_approval.label" href="/ecivil/births/eprBirthConfirmationApproval.do"
               style="color:red">
                (5) Birth Confirmation Pending Approval
            </a>
        </li>
        <li>
            <a id="print_birthcertificate.label" href="/ecivil/births/eprBirthCertificateList.do">
                (6) Print Birth Certificate
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
        DEATH REGISTRATION
    </a>
    <ul class="acitem">
        <li>
            <a id="death_registration.label" href="/ecivil/deaths/eprInitDeathDeclaration.do">
                (1) Death Registration
            </a>
        </li>
        <li>
            <a id="late_death_registration.label" href="/ecivil/deaths/eprInitLateDeathDeclaration.do">
                (1) Late Death Registration
            </a>
        </li>
        <li>
            <a id="sudden_death_registration.label" href="/ecivil/deaths/eprInitSuddenDeathDeclaration.do">
                (1) Sudden Death Registration
            </a>
        </li>
        <li>
            <a id="missing_person_registration.label" href="/ecivil/deaths/eprInitMissingPersonDeclaration.do">
                (1) Missing Person Registration
            </a>
        </li>
        <li>
            <a id="death_approve_print_list.label" href="/ecivil/deaths/eprDeathApprovalAndPrint.do">
                (2) Death Approve And Print List
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
        ADOPTION REGISTRATION
    </a>
    <ul class="acitem">
        <li>
            <a id="adoption_registration.label" href="/ecivil/adoption/eprAdoptionRegistrationAction.do">
                (1) Adoption Registration
            </a>
        </li>
        <li>
            <a id="adoption_approval_and_print.lable" href="/ecivil/adoption/eprAdoptionApprovalAndPrint.do">
                (2) Adoption Approval And Print
            </a>
        </li>
        <li>
            <a id="adoption_applicant.label" href="/ecivil/adoption/eprAdoptionApplicantInfo.do">
                (3) Adoption Applicant
            </a>
        </li>
        <li>
            <a id="adoption_re_registration.label" href="/ecivil/adoption/eprAdoptionReRegistration.do">
                (4) Adoption Reregistration
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
        ALTERATION
    </a>
    <ul class="acitem">
        <li>
            <a id="birth_alteration.label" href="/ecivil/alteration/eprBirthAlterationInit.do">
                Birth Registration Alteration
            </a>
        </li>
        <li>
            <a id="birth_alteration_pending_approval.title"
               href="/ecivil/alteration/eprBirthAlterationPendingApproval.do">
                Birth Alteration Approval
            </a>
        </li>
        <li>
            <a id="death.registration.alteration" href="/ecivil/alteration/eprDeathAlterationSearchHome.do">
                Death Registration Alteration
            </a>
        </li>
        <li>
            <a id="label.manage.alterations" href="/ecivil/alteration/eprApproveDeathAlterationsInit.do">
                Manage Death Alteration
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
        USER PREFERENCE
    </a>
    <ul class="acitem">
        <li>
            <a id="userPreference.label" href="/ecivil/preferences/eprUserPreferencesInit.do">
                User Preferences
            </a>
        </li>
        <li>
            <a id="changePassword.label" href="/ecivil/preferences/eprpassChangePageLoad.do">
                Change Password
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
        PRS
    </a>
    <ul class="acitem">
        <li>
            <a id="prs.personRegistration.label" href="/ecivil/prs/eprExistingPersonRegInit.do">
                Person Registration
            </a>
        </li>
        <li>
            <a id="prs.advanceSearch.label" href="/ecivil/prs/eprPRSAdvancedSearch.do">
                Person Records Search
            </a>
        </li>
        <li>
            <a id="prs.personApproval.label" href="/ecivil/prs/eprPersonApproval.do">
                Person Registration Management
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
        SEARCH RECORDS
    </a>
    <ul class="acitem">
        <li>
            <a id="search.label" href="/ecivil/births/eprSearchPageLoad.do">
                Birth Declaration / Confirmation Search
            </a>
        </li>
        <li>
            <a id="birth_certificate_search.label" href="/ecivil/births/eprBirthCertificateSearch.do">
                Birth Certificate Search
            </a>
        </li>
        <li>
            <a id="death_certificate_search.label" href="/ecivil/deaths/eprDeathCertificateSearch.do">
                Death Certificate Search
            </a>
        </li>
        <li>
            <a id="birth.advanceSearch.label" href="/ecivil/births/eprBirthsAdvancedSearch.do">
                Birth Records Search
            </a>
        </li>
    </ul>
</li>

<s:if test="%{#session.context=='admin'}">
<li class="exp">
    </s:if>
    <s:else>
<li>
    </s:else>
    <a href="#">
        ADMIN TASK
    </a>
    <ul class="acitem">
        <li>
            <a id="registrars.managment" href="/ecivil/management/eprRegistrarsManagment.do" style="color:red">
                Manage Assignments
            </a>
        </li>
        <li>
            <a id="registrar.add" href="/ecivil/management/eprRegistrarsAdd.do" style="color:red">
                Add Registrar
            </a>
        </li>
        <li>
            <a id="search.registrar" href="/ecivil/management/eprFindRegistrar.do" style="color:red">
                Search Registrar
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
        MARRIAGE REGISTRATION
    </a>
    <ul class="acitem">
        <li>
            <a id="menu.marriage.notice" href="/ecivil/marriages/eprSelectNoticeType.do">
                Marriage Notice
            </a>
        </li>
        <li>
            <a id="menu.marriage.register.search" href="/ecivil/marriages/eprMarriageRegisterSearchInit.do">
                Marriage Register Search
            </a>
        </li>
        <li>
            <a id="menu.marriage.registration" href="/ecivil/marriages/eprMarriageRegistrationInit.do">
                Marriage Registration
            </a>
        </li>
        <li>
            <a id="menu.marriage.notice.search" href="/ecivil/marriages/eprMarriageNoticeSearchInit.do">
                Search Notice
            </a>
        </li>
    </ul>
</li>
</ul>
</div>