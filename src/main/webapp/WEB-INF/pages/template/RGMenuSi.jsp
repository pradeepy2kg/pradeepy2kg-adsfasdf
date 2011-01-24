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
                (1)සජීවී උපතක් ලියාපදිංචි කිරීම
            </a>
        </li>
        <li>
            <a id="still_birth_registration.label" href="/ecivil/births/eprStillBirthRegistrationInit.do">
                (1)මළ උපතක් ලියාපදිංචිය
            </a>
        </li>
        <li>
            <a id="birth_confirmation_print.label" href="/ecivil/births/eprBirthConfirmationPrintList.do">
                (3)උපත් තහවුරු කිරීම් මුද්‍රණය
            </a>
        </li>
        <li>
            <a id="birth_confirmation.label" href="/ecivil/births/eprBirthConfirmationInit.do">
                (4)උපත් තහවුරු කිරීම
            </a>
        </li>
        <li>
            <a id="print_birthcertificate.label" href="/ecivil/births/eprBirthCertificateList.do">
                (6)උප්පැන්න සහතික මුද්‍රණය
            </a>
        </li>
        <li>
            <a id="birth_register_approval.label" href="/ecivil/births/eprBirthRegisterApproval.do">
                (2)උපත් ලියාපදිංචි අනුමතය
            </a>
        </li>
        <li>
            <a id="birth_confirmation_approval.label" href="/ecivil/births/eprBirthConfirmationApproval.do">
                (5)උපත් තහවුරු අනුමත කිරීම
            </a>
        </li>
        <li>
            <a id="birth_register_belated_approval.label"
               href="/ecivil/births/eprBirthRegisterBelatedApproval.do" style="color:green">
                කල්පසු වූ උපත් ලියාපදිංචි අනුමතය
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
        <s:label value="%{getText('category_death_registraion')}"/>
    </a>
    <ul class="acitem">
        <li>
            <a id="death_registration.label" href="/ecivil/deaths/eprInitDeathDeclaration.do">
                <s:label value="%{getText('death_registration.label')}"/>
            </a>
        </li>
        <li>
            <a id="late_death_registration.label" href="/ecivil/deaths/eprInitLateDeathDeclaration.do">
                <s:label value="%{getText('late_death_registration.label')}"/>
            </a>
        </li>
        <li>
            <a id="death_approve_print_list.label" href="/ecivil/deaths/eprDeathApprovalAndPrint.do">
                <s:label value="%{getText('death_approve_print_list.label')}"/>
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
        <s:label value="%{getText('category_adoption')}"/>
    </a>
    <ul class="acitem">
        <li>
            <a id="adoption_registration.label" href="/ecivil/adoption/eprAdoptionRegistrationAction.do">
                <s:label value="%{getText('adoption_registration.label')}"/>
            </a>
        </li>
        <li>
            <a id="adoption_approval_and_print.lable" href="/ecivil/adoption/eprAdoptionApprovalAndPrint.do">
                <s:label value="%{getText('adoption_approval_and_print.lable')}"/>
            </a>
        </li>
        <li>
            <a id="adoption_applicant.label" href="/ecivil/adoption/eprAdoptionApplicantInfo.do">
                <s:label value="%{getText('adoption_applicant.label')}"/>
            </a>
        </li>
        <li>
            <a id="adoption_re_registration.label" href="/ecivil/adoption/eprAdoptionReRegistration.do">
                <s:label value="%{getText('adoption_re_registration.label')}"/>
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
        <s:label value="%{getText('category_alteration')}"/>
    </a>
    <ul class="acitem">
        <li>
            <a id="birth_alteration.label" href="/ecivil/alteration/eprBirthAlterationInit.do">
                <s:label value="%{getText('birth_alteration.title')}"/>
            </a>
        </li>
        <li>
            <a id="birth_alteration_pending_approval.title"
               href="/ecivil/alteration/eprBirthAlterationPendingApproval.do">
                <s:label value="%{getText('birth_alteration_pending_approval.title')}"/>
            </a>
        </li>
        <li>
            <a id="death.registration.alteration" href="/ecivil/alteration/eprDeathAlterationSearchHome.do">
                <s:label value="%{getText('death.registration.alteration')}"/>
            </a>
        </li>
        <li>
            <a id="label.manage.alterations" href="/ecivil/alteration/eprApproveDeathAlterationsInit.do">
                <s:label value="%{getText('label.manage.alterations')}"/>
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
        <s:label value="%{getText('category_user_preferance')}"/>
    </a>
    <ul class="acitem">
        <li>
            <a id="userPreference.label" href="/ecivil/preferences/eprUserPreferencesInit.do">
                <s:label value="%{getText('userPreference.label')}"/>
            </a>
        </li>
        <li>
            <a id="changePassword.label" href="/ecivil/preferences/eprpassChangePageLoad.do">
                <s:label value="%{getText('changePassword.label')}"/>
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
        <s:label value="%{getText('category_prs')}"/>
    </a>
    <ul class="acitem">
        <li>
            <a id="prs.personRegistration.label" href="/ecivil/prs/eprExistingPersonRegInit.do">
                <s:label value="%{getText('prs.personRegistration.label')}"/>
            </a>
        </li>
        <li>
            <a id="prs.advanceSearch.label" href="/ecivil/prs/eprPRSAdvancedSearch.do">
                <s:label value="%{getText('prs.advanceSearch.label')}"/>
            </a>
        </li>
        <li>
            <a id="prs.personApproval.label" href="/ecivil/prs/eprPersonApproval.do">
                <s:label value="%{getText('prs.personApproval.label')}"/>
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
        <s:label value="%{getText('category_certificate_search')}"/>
    </a>
    <ul class="acitem">
        <li>
            <a id="search.label" href="/ecivil/births/eprSearchPageLoad.do">
                <s:label value="%{getText('search.label')}"/>
            </a>
        </li>
        <li>
            <a id="birth_certificate_search.label" href="/ecivil/births/eprBirthCertificateSearch.do">
                <s:label value="%{getText('birth_certificate_search.label')}"/>
            </a>
        </li>
        <li>
            <a id="death_certificate_search.label" href="/ecivil/deaths/eprDeathCertificateSearch.do">
                <s:label value="%{getText('death_certificate_search.label')}"/>
            </a>
        </li>
        <li>
            <a id="birth.advanceSearch.label" href="/ecivil/births/eprBirthsAdvancedSearch.do">
                <s:label value="%{getText('birth.advanceSearch.label')}"/>
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
        <s:label value="%{getText('category_admin_task')}"/>
    </a>
    <ul class="acitem">
        <li>
            <a id="registrars.managment" href="/ecivil/management/eprRegistrarsManagment.do">
                <s:label value="%{getText('registrars.managment')}"/>
            </a>
        </li>
        <li>
            <a id="registrar.add" href="/ecivil/management/eprRegistrarsAdd.do">
                <s:label value="%{getText('registrar.add')}"/>
            </a>
        </li>
        <li>
            <a id="search.registrar" href="/ecivil/management/eprFindRegistrar.do">
                <s:label value="%{getText('search.registrar')}"/>
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
    <a href="#">
        <s:label value="%{getText('category_marrage_registraion')}"/>
    </a>
    <ul class="acitem">
        <li>
            <a id="menu.marriage.notice" href="/ecivil/marriages/eprSelectNoticeType.do">
                <s:label value="%{getText('menu.marriage.notice')}"/>
            </a>
        </li>
        <li>
            <a id="menu.marriage.register.search" href="/ecivil/marriages/eprMarriageRegisterSearchInit.do">
                <s:label value="%{getText('menu.marriage.register.search')}"/>
            </a>
        </li>
        <li>
            <a id="menu.marriage.registration" href="/ecivil/marriages/eprMarriageRegistrationInit.do">
                <s:label value="%{getText('menu.marriage.registration')}"/>
            </a>
        </li>
        <li>
            <a id="menu.marriage.notice.search" href="/ecivil/marriages/eprMarriageNoticeSearchInit.do">
                <s:label value="%{getText('menu.marriage.notice.search')}"/>
            </a>
        </li>
    </ul>
</li>
</ul>
</div>