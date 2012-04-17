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
        பிறப்பு பதிவு
    </a>
    <ul class="acitem">
        <li>
            <a id="birth_registration.label" href="/ecivil/births/eprBirthRegistrationInit.do">
                (1) பிறப்பினை பதிவு செய்தல்
            </a>
        </li>
        <li>
            <a id="still_birth_registration.label" href="/ecivil/births/eprStillBirthRegistrationInit.do">
                (1) சாப்பிள்ளை பிறப்பினை பதிவு செய்தல்
            </a>
        </li>
        <li>
            <a id="birth_register_approval.label" href="/ecivil/births/eprBirthRegisterApproval.do">
                (2) பிறப்பு பதிவிற்கான அனுமதி
            </a>
        </li>
        <li>
            <a id="birth_register_belated_approval.label"
               href="/ecivil/births/eprBirthRegisterBelatedApproval.do" style="color:green">
                (2) காலங்கடந்த பிறப்பிற்கான அனுமதி
            </a>
        </li>
        <li>
            <a id="birth_confirmation_print.label" href="/ecivil/births/eprBirthConfirmationPrintList.do">
                (3) பிறப்பு உறுதிபடித்தும் அச்சு
            </a>
        </li>
        <li>
            <a id="birth_confirmation.label" href="/ecivil/births/eprBirthConfirmationInit.do">
                (4) பிறப்பினை உறுதிசெய்தல்
            </a>
        </li>
        <li>
            <a id="birth_confirmation_approval.label" href="/ecivil/births/eprBirthConfirmationApproval.do">
                (5) பிறப்பினை உறுதிபடுத்துவதினை அனுமதித்தல்
            </a>
        </li>
        <li>
            <a id="print_birthcertificate.label" href="/ecivil/births/eprBirthCertificateList.do">
                (6) பிறப்புச் சான்றிதழ் அச்சு
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
        இறப்பு பதிவு
    </a>
    <ul class="acitem">
        <li>
            <a id="death_registration.label" href="/ecivil/deaths/eprInitDeathDeclaration.do">
                (1) சாதாரண இறப்புப்பதிவு
            </a>
        </li>
        <li>
            <a id="late_death_registration.label" href="/ecivil/deaths/eprInitLateDeathDeclaration.do">
                (1) காலங்கடந்த இறப்புப்பதிவு
            </a>
        </li>
        <li>
            <a id="sudden_death_registration.label" href="/ecivil/deaths/eprInitSuddenDeathDeclaration.do">
                (1) திடீர் இறப்புப் பதிவு
            </a>
        </li>
        <li>
            <a id="missing_person_registration.label" href="/ecivil/deaths/eprInitMissingPersonDeclaration.do">
                (1) காணாமற் போன நபரின் இறப்புப்பதிவு
            </a>
        </li>
        <li>
            <a id="death_approve_print_list.label" href="/ecivil/deaths/eprDeathApprovalAndPrint.do">
                (2) இறப்பு அனுமதி மற்றும் அச்சிடல்
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
        மகவேற்பினை பதிவு செய்தல்
    </a>
    <ul class="acitem">
        <li>
            <a id="adoption_registration.label" href="/ecivil/adoption/eprAdoptionRegistrationAction.do">
                (1) மகவேற்பினை பதவு செய்தல்
            </a>
        </li>
        <li>
            <a id="adoption_approval_and_print.lable" href="/ecivil/adoption/eprAdoptionApprovalAndPrint.do">
                (2) மகவேற்பினை அனுமதித்தல் மற்றும் அச்சிடல்
            </a>
        </li>
        <li>
            <a id="adoption_applicant.label" href="/ecivil/adoption/eprAdoptionApplicantInfo.do">
                (3) விண்ணப்பதாரரின் விபரங்கள்
            </a>
        </li>
        <li>
            <a id="adoption_re_registration.label" href="/ecivil/adoption/eprAdoptionReRegistration.do">
                (4) மகவேற்பின் இரண்டாம் பதிவு
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
        மாற்றத்தினை ஏற்படுத்தல்
    </a>
    <ul class="acitem">
        <li>
            <a id="birth_alteration.label" href="/ecivil/alteration/eprBirthAlterationInit.do">
                பிறப்பு பதிவு செய்தல் குறிப்பின்விபரத்தினை மாற்றுதல்
            </a>
        </li>
        <li>
            <a id="birth_alteration_pending_approval.title"
               href="/ecivil/alteration/eprBirthAlterationPendingApproval.do">
                பிறப்புச்சான்றிதழில் குறிப்பினை மாற்றுவதற்கு அனுமதித்தல்
            </a>
        </li>
        <li>
            <a id="death.registration.alteration" href="/ecivil/alteration/eprDeathAlterationSearchHome.do">
                இறப்புச்சான்றிதழில் திருத்தம் மேற்கொள்ளல்
            </a>
        </li>
        <li>
            <a id="label.manage.alterations" href="/ecivil/alteration/eprApproveDeathAlterationsInit.do">
                இறப்புச் சான்றிதழின் மாற்றத்தினை முகாமை செய்தல்
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
        தனிநபர் தேடுதல்
    </a>
    <ul class="acitem">
        <li>
            <a id="userPreference.label" href="/ecivil/preferences/eprUserPreferencesInit.do">
                பாவனையாளரின் எண்ணம்
            </a>
        </li>
        <li>
            <a id="changePassword.label" href="/ecivil/preferences/eprpassChangePageLoad.do">
                இரகசிய சொல்லினை மாற்றுதல்
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
        நபரினை பதிவு செய்தல் சேவை
    </a>
    <ul class="acitem">
        <li>
            <a id="prs.personRegistration.label" href="/ecivil/prs/eprExistingPersonRegInit.do">
                நபரினை பதிவு செய்தல்
            </a>
        </li>
        <li>
            <a id="prs.advanceSearch.label" href="/ecivil/prs/eprPRSAdvancedSearch.do">
                நபரின் அறிக்கையினை தேடுதல்
            </a>
        </li>
        <li>
            <a id="prs.personApproval.label" href="/ecivil/prs/eprPersonApproval.do">
                நபர் பதிவு முகாமைத்துவம்
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
        அறிக்கை தேடுதல்
    </a>
    <ul class="acitem">
        <li>
            <a id="search.label" href="/ecivil/births/eprSearchPageLoad.do">
                பிறப்பு பிரதிக்கினை / உறுதிபடுத்தல் தேடுதல்
            </a>
        </li>
        <li>
            <a id="birth_certificate_search.label" href="/ecivil/births/eprBirthCertificateSearch.do">
                பிறப்புச் சான்றிதழ் தேடுதல்
            </a>
        </li>
        <li>
            <a id="death_certificate_search.label" href="/ecivil/deaths/eprDeathCertificateSearch.do">
                இறப்புச் சான்றிதழ் தேடுதல்
            </a>
        </li>
        <li>
            <a id="birth.advanceSearch.label" href="/ecivil/births/eprBirthsAdvancedSearch.do">
                பிறப்பு அறிக்கையினை தேடுதல்
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
        நிருவாக நடவடிக்கை
    </a>
    <ul class="acitem">
        <li>
            <a id="registrars.managment" href="/ecivil/management/eprRegistrarsManagment.do">
                பதிவாளார் நிருவாகம்
            </a>
        </li>
        <li>
            <a id="registrar.add" href="/ecivil/management/eprRegistrarsAdd.do">
                புதிய பதிவாளர்களை உட்புகுத்தல்
            </a>
        </li>
        <li>
            <a id="search.registrar" href="/ecivil/management/eprFindRegistrar.do">
                பதிவாளரினை தேடுதல்
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
        விவாக பதிவு
    </a>
    <ul class="acitem">
        <li>
            <a id="menu.marriage.notice" href="/ecivil/marriages/eprSelectNoticeType.do">
                திருமண அறிவித்தல்
            </a>
        </li>
        <li>
            <a id="menu.marriage.notice.search" href="/ecivil/marriages/eprMarriageNoticeSearchHome.do">
                திருமண அறிவித்தல் தேடுதல்
            </a>
        </li>
        <li>
            <a id="menu.marriagelicense.search" href="/ecivil/marriages/eprMarriageLicenseSearchInit.do">
                திருமண உத்தரவுப்பத்திரம் தேடுதல்
            </a>
        </li>
        <li>
            <a id="menu.marriage.registration" href="/ecivil/marriages/eprMarriageRegistrationInit.do">
                திருமணப்பதிவு
            </a>
        </li>
        <li>
            <a id="menu.marriage.register.search" href="/ecivil/marriages/eprMarriageRegisterSearchInit.do">
                திருமணச்சான்றிதழ் தேடுதல்
            </a>
        </li>
    </ul>
</li>

<s:if test="%{#session.context=='statistics'}">
<li class="exp">
    </s:if>
    <s:else>
<li>
    </s:else>
    <a href="/ecivil/statistics/eprStatHome.do">
        Statistics in Tamil
    </a>
</li>
</ul>
</div>