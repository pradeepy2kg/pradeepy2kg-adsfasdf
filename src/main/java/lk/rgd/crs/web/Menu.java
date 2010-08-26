package lk.rgd.crs.web;

import lk.rgd.Permission;
import lk.rgd.common.api.domain.Role;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ashoka Ekanayaka
 * A utility class to contain all menu items for different user roles. Menus will be determined only once using a static block with final  variables
 */
public class Menu {
    private static final Map<String, Map> adminLinks = new TreeMap<String, Map>();
    private static final Map<String, Map> deoLinks = new TreeMap<String, Map>();
    private static final Map<String, Map> adrLinks = new TreeMap<String, Map>();
    private static final Logger logger = LoggerFactory.getLogger(Menu.class);

    // admin menu items
    private static final Map adminLink = new TreeMap();

    // common menu items
    private static final Map preferanceLink = new TreeMap();
    private static final Map prsLink = new TreeMap();

    // deo menu items
    private static final Map deoBirthLink = new TreeMap();
    private static final Map deoAdoptionLink = new TreeMap();
    private static final Map deoDeathLink = new TreeMap();

    // adr menu items
    private static final Map adrBirthLink = new TreeMap();
    private static final Map adrAdoptionLink = new TreeMap();
    private static final Map adrDeathLink = new TreeMap();

    // arg/rg menu items

    static {
        //Admin
        adminLink.put(Permission.PAGE_CREATE_USER, new Link("creat_user.label", "/popreg/management/", "eprInitUserCreation.do"));
        adminLink.put(Permission.PAGE_VIEW_USERS, new Link("viewUsers.label", "/popreg/management/", "eprViewUsers.do"));
        adminLink.put(Permission.PAGE_USER_CREATION, new Link(null, "/popreg/management/", "eprUserCreation.do"));
        adminLink.put(Permission.PAGE_DELETE_USER, new Link(null, "/popreg/management/", "eprdeleteUsers.do"));
        adminLink.put(Permission.PAGE_VIEW_SELECTED_USERS, new Link(null, "/popreg/management/", "eprViewSelectedUsers.do"));
        adminLink.put(Permission.PAGE_INIT_ADD_DS_DIVISION_DIVISIONS, new Link("addEditDivision.label", "/popreg/management/", "eprInitAddDivisionsAndDsDivisions.do"));
        adminLink.put(Permission.PAGE_INIT_ADD_DIVISIONS, new Link(null, "/popreg/management/", "eprInitAddDivisions.do"));
        adminLink.put(Permission.PAGE_INIT_ADD_DS_DIVISIONS, new Link(null, "/popreg/management/", "eprInitAddDsDivisions.do"));
        adminLink.put(Permission.PAGE_INIT_ADD_DISTRICT, new Link(null, "/popreg/management/", "eprInitAddDistrict.do"));
        adminLink.put(Permission.PAGE_INIT_ADD_MR_DIVISIONS, new Link(null, "/popreg/management/", "eprInitAddMrDivisions.do"));
        adminLink.put(Permission.PAGE_ADD_DS_DIVISION_DIVISIONS, new Link(null, "/popreg/management/", "eprAddDivisionsAndDsDivisions.do"));

        //User Preferance
        preferanceLink.put(Permission.PAGE_USER_PREFERANCE_SELECT, new Link("userPreference.label", "/popreg/preferences/", "eprUserPreferencesInit.do"));
        preferanceLink.put(Permission.CHANGE_PASSWORD, new Link(null, "/popreg/preferences/", "eprChangePass.do"));
        preferanceLink.put(Permission.BACK_CHANGE_PASSWORD, new Link(null, "/popreg/preferences/", "eprBackChangePass.do"));
        preferanceLink.put(Permission.CHANGE_PASSWORD_PAGE_LOAD, new Link(null, "/popreg/preferences/", "passChangePageLoad.do"));
        preferanceLink.put(Permission.PAGE_USER_PREFERENCE_INIT, new Link(null, "/popreg/preferences/", "eprUserPreferencesAction.do"));

        // PRS
        prsLink.put(Permission.PAGE_ADVANCE_SEARCH_PRS, new Link("prs.advanceSearch.label", "/popreg/prs/", "eprPRSAdvancedSearch.do"));

        // Birth Registration for DEO
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATON_INIT, new Link("birth_registration.label", "/popreg/births/", "eprBirthRegistrationInit.do"));
        deoBirthLink.put(Permission.PAGE_STILL_BIRTH_REGISTRATION, new Link("still_birth_registration.label", "/popreg/births/", "eprStillBirthRegistrationInit.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATON_DIRECT_HOME, new Link(null, "/popreg/births/", "eprHome.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATON, new Link(null, "/popreg/births/", "eprBirthRegistration.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATON_HOME, new Link(null, "/popreg/births/", "eprBirthRegistrationHome.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATON_STILL_BIRTH_HOME, new Link(null, "/popreg/births/", "eprStillBirth.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATON_DIRECT_PRINT, new Link(null, "/popreg/births/", "eprDirectPrintBirthConfirmation.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT_MARK_BIRTH_CERTIFICATE_AS_PRINTED, new Link(null, "/popreg/births/", "eprMarkCertificateAsPrinted.do"));

        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT, new Link("birth_confirmation_print.label", "/popreg/births/", "eprBirthConfirmationPrintList.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_INIT, new Link("birth_confirmation.label", "/popreg/births/", "eprBirthConfirmationInit.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATION_APPROVAL, new Link("birth_register_approval.label", "/popreg/births/", "eprBirthRegisterApproval.do"));
        deoBirthLink.put(Permission.PAGE_USER_PREFERANCE_SELECT, new Link("userPreference.label", "/popreg/preferences/", "eprUserPreferencesInit.do"));
        deoBirthLink.put(Permission.PAGE_VIEW_USERS, new Link("viewUsers.label", "/popreg/management/", "eprViewUsers.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_SEARCH, new Link("search.label", "/popreg/births/", "eprSearchPageLoad.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL, new Link("birth_confirmation_approval.label", "/popreg/births/", "eprBirthConfirmationApproval.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT, new Link("print_birthcertificate.label", "/popreg/births/", "eprBirthCertificateList.do"));
        deoBirthLink.put(Permission.PAGE_STILL_BIRTH_REGISTRATION, new Link("still_birth_registration.label", "/popreg/births/", "eprStillBirthRegistrationInit.do"));
        deoBirthLink.put(Permission.PAGE_ADVANCE_SEARCH_BIRTHS, new Link("advanceSearch.label", "/popreg/births/", "eprBirthsAdvancedSearch.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_FORM_DETAIL_DIRECT_PRINT_BIRTH_CERTIFICATE, new Link(null, "/popreg/births/", "eprDirectPrintBirthCertificate.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT_MARK_BIRTH_CONFIRMATION_AS_PRINTED, new Link(null, "/popreg/births/", "eprMarkBirthConfirmationAsPrint.do"));

        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATON_DIRECT_HOME, new Link(null, "/popreg/births/", "eprHome.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATON, new Link(null, "/popreg/births/", "eprBirthRegistration.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION, new Link(null, "/popreg/births/", "eprBirthConfirmation.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT_LIST_REFRESH, new Link(null, "/popreg/births/", "eprFilterBirthConfirmationPrint.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT_SELECTED_ENTRY, new Link(null, "/popreg/births/", "eprBirthConfirmationPrintPage.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_BULK_PRINT, new Link(null, "/popreg/births/", "eprBirthConfirmationBulkPrint.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT_LIST_NEXT, new Link(null, "/popreg/births/", "eprPrintNext.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT_LIST_PREVIOUS, new Link(null, "/popreg/births/", "eprPrintPrevious.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_SKIP_CONFIRMATIONCHANGES, new Link(null, "/popreg/births/", "eprBirthConfirmationSkipChanges.do"));

        deoBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_SEARCH, new Link("search.label", "/popreg/births/", "eprSearchPageLoad.do"));
        deoBirthLink.put(Permission.PAGE_ADVANCE_SEARCH_BIRTHS, new Link("birth.advanceSearch.label", "/popreg/births/", "eprBirthsAdvancedSearch.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATION_SEARCH_BY_SERIALNO, new Link(null, "/popreg/births/", "eprBDFSearchBySerialNo.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATION_SEARCH_BY_IDUKEY, new Link(null, "/popreg/births/", "eprBDFSearchByIdUKey.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CERTIFICATE_SEARCH, new Link("birth_certificate_search.label", "/popreg/births/", "eprBirthCertificateSearch.do"));

        deoBirthLink.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT, new Link("print_birthcertificate.label", "/popreg/births/", "eprBirthCertificateList.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT_LIST_REFRESH, new Link(null, "/popreg/births/", "eprFilterBirthCetificateList.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT_SELECTED_ENTRY, new Link(null, "/popreg/births/", "eprBirthCertificate.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CERTIFICATE_BULK_PRINT, new Link(null, "/popreg/births/", "eprBirthCertificateBulkPrint.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT_LIST_NEXT, new Link(null, "/popreg/births/", "eprCertificatePrintNext.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT_LIST_PREVIOUS, new Link(null, "/popreg/births/", "eprCertificatePrintPrevious.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATION_STILL_BIRTH_CERTIFICATE_PRINT, new Link(null, "/popreg/births/", "eprStillBirthCertificatePrint.do"));
        deoBirthLink.put(Permission.PAGE_BIRTH_REGISTRATION_STILL_BIRTH_CERTIFICATE_DIRECT_PRINT, new Link(null, "/popreg/births/", "eprDirectPrintStillBirthCertificate.do"));

        // Birth for ADR
        adrBirthLink.putAll(deoBirthLink);
        adrBirthLink.put(Permission.PAGE_BIRTH_REGISTRATION_APPROVAL, new Link("birth_register_approval.label", "/popreg/births/", "eprBirthRegisterApproval.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL, new Link("birth_confirmation_approval.label", "/popreg/births/", "eprBirthConfirmationApproval.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL_REFRESH, new Link(null, "/popreg/births/", "eprConfirmationApprovalRefresh.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CERTIFICATE_BULK_PRINT, new Link(null, "/popreg/births/", "eprBirthCertificateBulkPrint.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_REGISTRATION_SEARCH_BY_SERIALNO, new Link(null, "/popreg/births/", "eprBDFSearchBySerialNo.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_REGISTRATION_SEARCH_BY_IDUKEY, new Link(null, "/popreg/births/", "eprBDFSearchByIdUKey.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_REGISTRATION_SEARCH_VIEW_NON_EDITABLE_MODE, new Link(null, "/popreg/births/", "eprViewBDFInNonEditableMode.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL_NEXT, new Link(null, "/popreg/births/", "eprConfirmationApprovalNext.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL_PREVIOUS, new Link(null, "/popreg/births/", "eprConfirmationApprovalPrevious.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_REFRESH, new Link(null, "/popreg/births/", "eprApprovalRefresh.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_DECLARATION_APPROVE_BULK, new Link(null, "/popreg/births/", "eprApproveConfirmationBulk.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_DIRECT_APPROVAL, new Link(null, "/popreg/births/", "eprConfrimationChangesDirectApproval.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_DIRECT_PRINT_BIRTH_CERTIFICATE, new Link(null, "/popreg/births/", "eprBirthCertificatDirectPrint.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVE_BULK, new Link(null, "/popreg/births/", "eprApproveBulk.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_DECLARATION_APPROVE_SELECTED, new Link(null, "/popreg/births/", "eprApproveBirthDeclaration.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_NEXT, new Link(null, "/popreg/births/", "eprApprovalNext.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_PREVIOUS, new Link(null, "/popreg/births/", "eprApprovalPrevious.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_DELETE, new Link(null, "/popreg/births/", "eprDeleteApprovalPending.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_IGNORING_WARNING, new Link(null, "/popreg/births/", "eprIgnoreWarning.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_REJECT_SELECTED, new Link(null, "/popreg/births/", "eprRejectBirthDeclaration.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVE_SELECTED, new Link(null, "/popreg/births/", "eprApproveBirthConfirmation.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL_IGNORING_WARNING, new Link(null, "/popreg/births/", "eprConfirmationIgnoreWarning.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL_REJECT_SELECTED, new Link(null, "/popreg/births/", "eprRejectBirthConfirmation.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_REGISTRATON_DIRECT_APPROVE, new Link(null, "/popreg/births/", "eprDirectApprove.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_REGISTRATON_DIRECT_APPROVAL_IGNORING_WARNINGS, new Link(null, "/popreg/births/", "eprDirectApproveIgnoreWarning.do"));
        adrBirthLink.put(Permission.PAGE_BIRTH_CONFIRMATION_DIRECT_APPROVAL_IGNORING_WARNINGS, new Link(null, "/popreg/births/", "ConfirmationDirectApprovalIngoreWarning.do"));
        
        //Death Registration for DEO
        deoDeathLink.put(Permission.PAGE_DEATH_REGISTRATION_INIT, new Link("death_registration.label", "/popreg/deaths/", "eprInitDeathDeclaration.do"));
        deoDeathLink.put(Permission.PAGE_DEATH_REGISTRATION, new Link(null, "/popreg/deaths/", "eprDeathDeclaration.do"));
        deoDeathLink.put(Permission.PAGE_DEATH_CERTIFICATE, new Link(null, "/popreg/deaths/", "eprDeathCertificate.do"));
        deoDeathLink.put(Permission.PAGE_LATE_DEATH_REGISTRATION, new Link("late_death_registration.label", "/popreg/deaths/", "eprInitLateDeathDeclaration.do"));
        deoDeathLink.put(Permission.PAGE_DEATH_APPROVAL_PRINT_LIST_REFRESH, new Link(null, "/popreg/deaths/", "eprDeathFilterByStatus.do"));
        deoDeathLink.put(Permission.PAGE_LATE_DEATH_HOME, new Link(null, "/popreg/deaths/", "eprInitDeathHome.do"));

        deoDeathLink.put(Permission.PAGE_DEATH_DELETE, new Link(null, "/popreg/deaths/", "eprDeleteDeath.do"));
        deoDeathLink.put(Permission.PAGE_DEATH_VEIW_MODE, new Link(null, "/popreg/deaths/", "eprDeathViewMode.do"));
        deoDeathLink.put(Permission.PAGE_DEATH_PRINT, new Link(null, "/popreg/deaths/", "eprPrintDeath.do"));
        deoDeathLink.put(Permission.PAGE_DEATH_EDIT_MODE, new Link(null, "/popreg/deaths/", "eprDeathEditMode.do"));

        // Death Registration for ADR
        adrDeathLink.putAll(deoDeathLink);
        adrDeathLink.put(Permission.PAGE_DEATH_APPROVE, new Link(null, "/popreg/deaths/", "eprApproveDeath.do"));
        adrDeathLink.put(Permission.PAGE_DEATH_REJECT, new Link(null, "/popreg/deaths/", "eprRejectDeath.do"));
        adrDeathLink.put(Permission.PAGE_DEATH_APPROVAL_PRINT, new Link("death_approve_print_list.label", "/popreg/deaths/", "eprDeathApprovalAndPrint.do"));


        // Adoption Registration for DEO
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION, new Link(null, "/popreg/adoption/", "eprAdoptionAction.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_HOME, new Link(null, "/popreg/adoption/", "eprAdoptionRegistrationHome.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_INIT, new Link("adoption_registration.label", "/popreg/adoption/", "eprAdoptionRegistrationAction.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_APPLICANT_INFO, new Link("adoption_applicant.label", "/popreg/adoption/", "eprAdoptionApplicantInfo.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_PROCESS_APPLICANT_INFO, new Link(null, "/popreg/adoption/", "eprAdoptionProcessApplicantInfo.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_HOME, new Link(null, "/popreg/adoption/", "eprBirthRegistrationHome.do"));
        //    deoAdoptionLink.put(Permission.PAGE_ADOPTION_APPLICANT_INFO, new Link("adoption_certificate.label", "/popreg/adoption/", "eprAdoptionCertificate.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_BDF_ENTRY, new Link(null, "/popreg/births/", "eprAdoptionBirthRegistrationInit.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_VIEW_MODE, new Link(null, "/popreg/adoption/", "eprAdoptionViewMode.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_MARK_ADOPTION_NOTICE_AS_PRINTED, new Link(null, "/popreg/adoption/", "eprMarkDirectlyAdoptionNoticeAsPrinted.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_CANCEL_PRINT_NOTICE, new Link(null, "/popreg/adoption/", "cancelPrintAdoptionNotice.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_DIRECT_PRINT_NOTICE_LETTER, new Link(null, "/popreg/adoption/", "eprAdoptionNoticeDirectPrint.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_CERTIFICATE_REQUEST, new Link(null, "/popreg/adoption/", "eprAdoptionCertificateRequest.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_FIND_ADOPTION_CERTIFICATE_REQUEST, new Link(null, "/popreg/adoption/", "eprAdoptionFind.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_DELETE_SELECTED, new Link(null, "/popreg/adoption/", "eprDeleteAdoption.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_EDIT_MODE, new Link(null, "/popreg/adoption/", "eprAdoptionEditMode.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_PRINT_NOTICE_LETTER, new Link(null, "/popreg/adoption/", "eprPrintAdoptionNotice.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_MARK_NOTICE_LETTER_AS_PRINTED, new Link(null, "/popreg/adoption/", "eprMarkAdoptionNoticeAsPrinted.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_PRINT_ADOPTION_CERTIFICATES, new Link(null, "/popreg/adoption/", "eprPrintAdoptionCertificate.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_MARK_CERTIFICATES_AS_PRINTED, new Link(null, "/popreg/adoption/", "eprMarkAdoptionCertificateAsPrinted.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_LOAD_NEXT_RECORDS, new Link(null, "/popreg/adoption/", "eprAdoptionNext.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_LOAD_PREVIOUS_RECORDS, new Link(null, "/popreg/adoption/", "eprAdoptionPrevious.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_FILTER_BY_STATUS, new Link(null, "/popreg/adoption/", "eprAdoptionFilterByStatus.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_BACK_TO_PREVIOUS_STATE, new Link(null, "/popreg/adoption/", "eprAdoptionBackToPreviousState.do"));
        deoAdoptionLink.put(Permission.PAGE_ADOPTION_CAPTURE_APPLICANT_INFO, new Link(null, "/popreg/adoption/", "eprCaptureAdoptionApplicantInfo.do"));
        //deoAdoptionLink.put(Permission.PAGE_ADOPTION_BDF_HOME, new Link(null, "/popreg/births/", "eprAdoptionRegistrationHome.do"));

        // Adoption Registration for ADR
        adrAdoptionLink.putAll(deoAdoptionLink);
        adrAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT, new Link("adoption_approval_and_print.lable", "/popreg/adoption/", "eprAdoptionApprovalAndPrint.do"));
        adrAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_APPROVE_SELECTED, new Link(null, "/popreg/adoption/", "eprApproveAdoption.do"));
        adrAdoptionLink.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_REJECT_SELECTED, new Link(null, "/popreg/adoption/", "eprRejectAdoption.do"));
        adrAdoptionLink.put(Permission.PAGE_ADOPTION_DIRECT_APPROVAL, new Link(null, "/popreg/adoption/", "eprAdoptionDirectApproval.do"));

        // assemble menu for admins : 1 - birth, 2 - death, 3 - marriage, 4 - adoptions, 5 - reports, 6 - management, 7 - prs
        adminLinks.put("6admin", adminLink);
        adminLinks.put("4preference", preferanceLink);

        // assemble menu for deo
        deoLinks.put("4preference", preferanceLink);
        deoLinks.put("0birth", deoBirthLink);
        deoLinks.put("1death", deoDeathLink);
        deoLinks.put("3adoption", deoAdoptionLink);

        // assemble menu for adr
        adrLinks.put("0birth", adrBirthLink);
        adrLinks.put("1death", adrDeathLink);
        adrLinks.put("3adoption", adrAdoptionLink);
        adrLinks.put("4preference", preferanceLink);
        adrLinks.put("7prs", prsLink);

        logger.debug("adminLink : {}", adminLink.size());
        logger.debug("preferanceLink : {}", preferanceLink.size());
        logger.debug("birthLink : {}", adrBirthLink.size());
        logger.debug("adrLinks : {}", adrLinks.size());
        logger.debug("adminLinks : {}", adminLinks.size());
    }

    public static Map<String, Map> getAllowedLinks(Role role) {
        String roleName = role.getRoleId();
        if (roleName.equals(Role.ROLE_ADMIN)) {
            return adminLinks;
        } else if (roleName.equals(Role.ROLE_DEO)) {
            return deoLinks;
        } else {  // for now dr, arg and rg also gets the same menu as ADR. todo : change for adotion approaval
            return adrLinks;
        }
    }
}
