<div id="xmain-menu">
    <ul class="menu">
        <li class="exp">
            <a href="/ecivil/births/eprBirthRegistrationHome.do">
                <s:label value="%{getText('category_birth_registration')}"/>
            </a>
            <ul class="acitem">
                <li>
                    <a id="birth_registration.label" href="/ecivil/births/eprBirthRegistrationInit.do">
                        <s:label value="%{getText('birth_registration.label')"/>
                    </a>
                </li>
                <li>
                    <a id="still_birth_registration.label" href="/ecivil/births/eprStillBirthRegistrationInit.do">
                        <s:label value="%{getText('still_birth_registration.label')"/>
                    </a>
                </li>
                <li>
                    <a id="birth_confirmation_print.label" href="/ecivil/births/eprBirthConfirmationPrintList.do">
                        <s:label value="%{getText('birth_confirmation_print.label')"/>
                    </a>
                </li>
                <li>
                    <a id="birth_confirmation.label" href="/ecivil/births/eprBirthConfirmationInit.do">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
                <%--TODO here--%>
                <li>
                    <a id="print_birthcertificate.label" href="/ecivil/births/eprBirthCertificateList.do">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
                <li>
                    <a id="birth_register_approval.label" href="/ecivil/births/eprBirthRegisterApproval.do"
                       style="color:red">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
                <li>
                    <a id="birth_confirmation_approval.label" href="/ecivil/births/eprBirthConfirmationApproval.do"
                       style="color:red">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
            </ul>
        </li>
        <li>
            <a href="/ecivil/deaths/eprInitDeathHome.do"><label>DEATH REGISTRATION</label></a>
            <ul class="acitem">
                <li>
                    <a id="death_registration.label" href="/ecivil/deaths/eprInitDeathDeclaration.do">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
                <li>
                    <a id="late_death_registration.label" href="/ecivil/deaths/eprInitLateDeathDeclaration.do">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
                <li>
                    <a id="death_approve_print_list.label" href="/ecivil/deaths/eprDeathApprovalAndPrint.do">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
            </ul>
        </li>
        <li>
            <a href="/ecivil/adoption/eprAdoptionRegistrationHome.do"><label>ADOPTION REGISTRATION</label></a>
            <ul class="acitem">
                <li>
                    <a id="adoption_registration.label" href="/ecivil/adoption/eprAdoptionRegistrationAction.do">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
                <li>
                    <a id="adoption_approval_and_print.lable" href="/ecivil/adoption/eprAdoptionApprovalAndPrint.do">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
                <li>
                    <a id="adoption_applicant.label" href="/ecivil/adoption/eprAdoptionApplicantInfo.do">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
                <li>
                    <a id="adoption_re_registration.label" href="/ecivil/adoption/eprAdoptionReRegistration.do">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
            </ul>
        </li>
        <li>
            <a href="/ecivil/alteration/eprBirthAlterationHome.do"><label>ALTERATION</label></a>
            <ul class="acitem">
                <li>
                    <a id="birth_alteration.label" href="/ecivil/alteration/eprBirthAlterationInit.do">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                    </a>
                </li>
                <li>
                    <a id="birth_alteration_pending_approval.title"
                       href="/ecivil/alteration/eprBirthAlterationPendingApproval.do">
                        <s:label value="%{getText('birth_confirmation.label')"/>
                       </a>
                </li>
                <li>
                    <a id="death.registration.alteration" href="/ecivil/alteration/eprDeathAlterationSearchHome.do">Death
                        Registration Alteration</a>
                </li>
                <li>
                    <a id="label.manage.alterations" href="/ecivil/alteration/eprApproveDeathAlterationsInit.do">Manage
                        Death Alteration</a>
                </li>
            </ul>
        </li>
        <li>
            <a href="#"><label>USER PREFERENCE</label></a>
            <ul class="acitem">
                <li>
                    <a id="userPreference.label" href="/ecivil/preferences/eprUserPreferencesInit.do">User
                        Preferences</a>
                </li>
                <li>
                    <a id="changePassword.label" href="/ecivil/preferences/eprpassChangePageLoad.do">Change Password</a>
                </li>
            </ul>
        </li>
        <li>
            <a href="#"><label>PRS</label></a>
            <ul class="acitem">
                <li>
                    <a id="prs.personRegistration.label" href="/ecivil/prs/eprExistingPersonRegInit.do">Person
                        Registration</a>
                </li>
                <li>
                    <a id="prs.advanceSearch.label" href="/ecivil/prs/eprPRSAdvancedSearch.do">Person Records Search</a>
                </li>
                <li>
                    <a id="prs.personApproval.label" href="/ecivil/prs/eprPersonApproval.do">Person Registration
                        Management</a>
                </li>
            </ul>
        </li>
        <li>
            <a href="#"><label>SEARCH RECORDS</label></a>
            <ul class="acitem">
                <li>
                    <a id="search.label" href="/ecivil/births/eprSearchPageLoad.do">Declaration/Confirmation Search</a>
                </li>
                <li>
                    <a id="birth_certificate_search.label" href="/ecivil/births/eprBirthCertificateSearch.do">Birth
                        Certificate Search</a>
                </li>
                <li>
                    <a id="death_certificate_search.label" href="/ecivil/deaths/eprDeathCertificateSearch.do">Death
                        Certificate Search</a>
                </li>
                <li>
                    <a id="birth.advanceSearch.label" href="/ecivil/births/eprBirthsAdvancedSearch.do">Birth Records
                        Search</a>
                </li>
            </ul>
        </li>
        <li>
            <a href="#"><label>ADMIN TASK</label></a>
            <ul class="acitem">
                <li>
                    <a id="registrars.managment" href="/ecivil/management/eprRegistrarsManagment.do" style="color:red">Manage
                        Assignments</a>
                </li>
                <li>
                    <a id="registrar.add" href="/ecivil/management/eprRegistrarsAdd.do" style="color:red">Add
                        registrar</a>
                </li>
                <li>
                    <a id="search.registrar" href="/ecivil/management/eprFindRegistrar.do" style="color:red">Search
                        Registrar</a>
                </li>
            </ul>
        </li>
        <li>
            <a href="#"><label>MARRIAGE REGISTRATION</label></a>
            <ul class="acitem">
                <li>
                    <a id="menu.marriage.notice" href="/ecivil/marriages/eprSelectNoticeType.do">Marriage Notice</a>
                </li>
                <li>
                    <a id="menu.marriage.register.search" href="/ecivil/marriages/eprMarriageRegisterSearchInit.do">Marriage
                        Register Search</a>
                </li>
                <li>
                    <a id="menu.marriage.registration" href="/ecivil/marriages/eprMarriageRegistrationInit.do">Marriage
                        Registration</a>
                </li>
                <li>
                    <a id="menu.marriage.notice.search" href="/ecivil/marriages/eprMarriageNoticeSearchInit.do">Search
                        Notice</a>
                </li>
            </ul>
        </li>
    </ul>
</div>