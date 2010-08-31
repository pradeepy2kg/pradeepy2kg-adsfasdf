package lk.rgd.crs.web;

import lk.rgd.Permission;
import lk.rgd.common.api.domain.Role;

import java.util.Map;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ashoka Ekanayaka
 *         A utility class to contain all menu items for different user roles. Menus will be determined only once using a static block with final  variables
 */
public class Menu {
    private static final Map<String, Map> adminLinks = new LinkedHashMap<String, Map>();
    private static final Map<String, Map> deoLinks = new LinkedHashMap<String, Map>();
    private static final Map<String, Map> adrLinks = new LinkedHashMap<String, Map>();
    private static final Map<String, Map> argLinks = new LinkedHashMap<String, Map>();
    private static final Logger logger = LoggerFactory.getLogger(Menu.class);

    // admin menu items
    private static final Map adminLink = new LinkedHashMap();

    // common menu items
    private static final Map preferanceLink = new LinkedHashMap();
    private static final Map prsLink = new LinkedHashMap();

    // deo menu items
    private static final Map deoBirthLink = new LinkedHashMap();
    private static final Map deoAdoptionLink = new LinkedHashMap();
    private static final Map deoDeathLink = new LinkedHashMap();

    // adr menu items
    private static final Map adrBirthLink = new LinkedHashMap();
    private static final Map adrAdoptionLink = new LinkedHashMap();
    private static final Map adrDeathLink = new LinkedHashMap();

    //arg menu items
    private static final Map argBirthLink = new LinkedHashMap();
    private static final Map argDeathLink = new LinkedHashMap();
    private static final Map argAdoptionLink = new LinkedHashMap();

    static {
        //Admin
        adminLink.put("eprInitUserCreation.do", new Link("creat_user.label", "/popreg/management/", "eprInitUserCreation.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprViewUsers.do", new Link("viewUsers.label", "/popreg/management/", "eprViewUsers.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprInitAddDivisionsAndDsDivisions.do", new Link("addEditDivision.label", "/popreg/management/", "eprInitAddDivisionsAndDsDivisions.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));

        adminLink.put("eprUserCreation.do", new Link(null, "/popreg/management/", "eprUserCreation.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprdeleteUsers.do", new Link(null, "/popreg/management/", "eprdeleteUsers.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprViewSelectedUsers.do", new Link(null, "/popreg/management/", "eprViewSelectedUsers.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprActiveDivisionsAndDsDivisions.do", new Link(null, "/popreg/management/", "eprActiveDivisionsAndDsDivisions.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));
        adminLink.put("eprInactiveDivisionsAndDsDivisions.do", new Link(null, "/popreg/management/", "eprInactiveDivisionsAndDsDivisions.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));
        adminLink.put("eprAddDivisionsAndDsDivisions.do", new Link(null, "/popreg/management/", "eprAddDivisionsAndDsDivisions.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));
        adminLink.put("eprInitDivisionList.do", new Link(null, "/popreg/management/", "eprInitDivisionList.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));

        //User Preferance
        preferanceLink.put("eprUserPreferencesInit.do", new Link("userPreference.label", "/popreg/preferences/", "eprUserPreferencesInit.do", Permission.USER_PREFERENCES));

        preferanceLink.put("eprChangePass.do", new Link(null, "/popreg/preferences/", "eprChangePass.do", Permission.USER_PREFERENCES));
        preferanceLink.put("eprBackChangePass.do", new Link(null, "/popreg/preferences/", "eprBackChangePass.do", Permission.USER_PREFERENCES));
        preferanceLink.put("passChangePageLoad.do", new Link(null, "/popreg/preferences/", "passChangePageLoad.do", Permission.USER_PREFERENCES));
        preferanceLink.put("eprUserPreferencesAction.do", new Link(null, "/popreg/preferences/", "eprUserPreferencesAction.do", Permission.USER_PREFERENCES));

        // PRS
        prsLink.put("eprPRSAdvancedSearch.do", new Link("prs.advanceSearch.label", "/popreg/prs/", "eprPRSAdvancedSearch.do", Permission.SEARCH_PRS));

        // Birth Registration for DEO
        deoBirthLink.put("eprBirthRegistrationInit.do", new Link("birth_registration.label", "/popreg/births/", "eprBirthRegistrationInit.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprStillBirthRegistrationInit.do", new Link("still_birth_registration.label", "/popreg/births/", "eprStillBirthRegistrationInit.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprBirthConfirmationPrintList.do", new Link("birth_confirmation_print.label", "/popreg/births/", "eprBirthConfirmationPrintList.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprBirthConfirmationInit.do", new Link("birth_confirmation.label", "/popreg/births/", "eprBirthConfirmationInit.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprBirthCertificateList.do", new Link("print_birthcertificate.label", "/popreg/births/", "eprBirthCertificateList.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprSearchPageLoad.do", new Link("search.label", "/popreg/births/", "eprSearchPageLoad.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprBirthsAdvancedSearch.do", new Link("birth.advanceSearch.label", "/popreg/births/", "eprBirthsAdvancedSearch.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprBirthCertificateSearch.do", new Link("birth_certificate_search.label", "/popreg/births/", "eprBirthCertificateSearch.do", Permission.SEARCH_BDF));

        deoBirthLink.put("eprHome.do", new Link(null, "/popreg/births/", "eprHome.do", Permission.USER_PREFERENCES));
        deoBirthLink.put("eprBirthRegistration.do", new Link(null, "/popreg/births/", "eprBirthRegistration.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprBirthRegistrationHome.do", new Link(null, "/popreg/births/", "eprBirthRegistrationHome.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprStillBirth.do", new Link(null, "/popreg/births/", "eprStillBirth.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprDirectPrintBirthConfirmation.do", new Link(null, "/popreg/births/", "eprDirectPrintBirthConfirmation.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprMarkCertificateAsPrinted.do", new Link(null, "/popreg/births/", "eprMarkCertificateAsPrinted.do", Permission.PRINT_BDF));
        adrBirthLink.put("eprViewBDFInNonEditableMode.do", new Link(null, "/popreg/births/", "eprViewBDFInNonEditableMode.do", Permission.EDIT_BDF));

        deoBirthLink.put("eprBirthConfirmation.do", new Link(null, "/popreg/births/", "eprBirthConfirmation.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprDirectPrintBirthCertificate.do", new Link(null, "/popreg/births/", "eprDirectPrintBirthCertificate.do", Permission.PRINT_BDF));
        deoBirthLink.put("eprMarkBirthConfirmationAsPrint.do", new Link(null, "/popreg/births/", "eprMarkBirthConfirmationAsPrint.do", Permission.EDIT_BDF_CONFIRMATION));

        deoBirthLink.put("eprFilterBirthConfirmationPrint.do", new Link(null, "/popreg/births/", "eprFilterBirthConfirmationPrint.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprBirthConfirmationPrintPage.do", new Link(null, "/popreg/births/", "eprBirthConfirmationPrintPage.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprBirthConfirmationBulkPrint.do", new Link(null, "/popreg/births/", "eprBirthConfirmationBulkPrint.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprBirthCancelConfirmationPrint.do", new Link(null, "/popreg/births/", "eprBirthCancelConfirmationPrint.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprPrintNext.do", new Link(null, "/popreg/births/", "eprPrintNext.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprPrintPrevious.do", new Link(null, "/popreg/births/", "eprPrintPrevious.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprBirthConfirmationSkipChanges.do", new Link(null, "/popreg/births/", "eprBirthConfirmationSkipChanges.do", Permission.EDIT_BDF_CONFIRMATION));

        deoBirthLink.put("eprBDFSearchBySerialNo.do", new Link(null, "/popreg/births/", "eprBDFSearchBySerialNo.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprBDFSearchByIdUKey.do", new Link(null, "/popreg/births/", "eprBDFSearchByIdUKey.do", Permission.SEARCH_BDF));

        deoBirthLink.put("eprFilterBirthCetificateList.do", new Link(null, "/popreg/births/", "eprFilterBirthCetificateList.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprBirthCertificate.do", new Link(null, "/popreg/births/", "eprBirthCertificate.do", Permission.PRINT_BDF));
        deoBirthLink.put("eprBirthCancelCertificatePrint.do", new Link(null, "/popreg/births/", "eprBirthCancelCertificatePrint.do", Permission.PRINT_BDF));
        deoBirthLink.put("eprBirthCertificateBulkPrint.do", new Link(null, "/popreg/births/", "eprBirthCertificateBulkPrint.do", Permission.PRINT_BDF));
        deoBirthLink.put("eprCertificatePrintNext.do", new Link(null, "/popreg/births/", "eprCertificatePrintNext.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprCertificatePrintPrevious.do", new Link(null, "/popreg/births/", "eprCertificatePrintPrevious.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprStillBirthCertificatePrint.do", new Link(null, "/popreg/births/", "eprStillBirthCertificatePrint.do", Permission.PRINT_BDF));
        deoBirthLink.put("eprDirectPrintStillBirthCertificate.do", new Link(null, "/popreg/births/", "eprDirectPrintStillBirthCertificate.do", Permission.PRINT_BDF));

        // Birth for ADR
        adrBirthLink.putAll(deoBirthLink);
        adrBirthLink.put("eprBirthRegisterApproval.do", new Link("birth_register_approval.label", "/popreg/births/", "eprBirthRegisterApproval.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprBirthConfirmationApproval.do", new Link("birth_confirmation_approval.label", "/popreg/births/", "eprBirthConfirmationApproval.do", Permission.APPROVE_BDF_CONFIRMATION));

        adrBirthLink.put("eprApprovalRefresh.do", new Link(null, "/popreg/births/", "eprApprovalRefresh.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprConfirmationApprovalRefresh.do", new Link(null, "/popreg/births/", "eprConfirmationApprovalRefresh.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprConfirmationApprovalNext.do", new Link(null, "/popreg/births/", "eprConfirmationApprovalNext.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprConfirmationApprovalPrevious.do", new Link(null, "/popreg/births/", "eprConfirmationApprovalPrevious.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprApproveConfirmationBulk.do", new Link(null, "/popreg/births/", "eprApproveConfirmationBulk.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprConfrimationChangesDirectApproval.do", new Link(null, "/popreg/births/", "eprConfrimationChangesDirectApproval.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprBirthCertificatDirectPrint.do", new Link(null, "/popreg/births/", "eprBirthCertificatDirectPrint.do", Permission.PRINT_BDF));
        adrBirthLink.put("eprApproveBulk.do", new Link(null, "/popreg/births/", "eprApproveBulk.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprApproveBirthDeclaration.do", new Link(null, "/popreg/births/", "eprApproveBirthDeclaration.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprApprovalNext.do", new Link(null, "/popreg/births/", "eprApprovalNext.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprApprovalPrevious.do", new Link(null, "/popreg/births/", "eprApprovalPrevious.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprDeleteApprovalPending.do", new Link(null, "/popreg/births/", "eprDeleteApprovalPending.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprIgnoreWarning.do", new Link(null, "/popreg/births/", "eprIgnoreWarning.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprRejectBirthDeclaration.do", new Link(null, "/popreg/births/", "eprRejectBirthDeclaration.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprApproveBirthConfirmation.do", new Link(null, "/popreg/births/", "eprApproveBirthConfirmation.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprConfirmationIgnoreWarning.do", new Link(null, "/popreg/births/", "eprConfirmationIgnoreWarning.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprRejectBirthConfirmation.do", new Link(null, "/popreg/births/", "eprRejectBirthConfirmation.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprDirectApprove.do", new Link(null, "/popreg/births/", "eprDirectApprove.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprDirectApproveIgnoreWarning.do", new Link(null, "/popreg/births/", "eprDirectApproveIgnoreWarning.do", Permission.APPROVE_BDF));
        adrBirthLink.put("ConfirmationDirectApprovalIngoreWarning.do", new Link(null, "/popreg/births/", "ConfirmationDirectApprovalIngoreWarning.do", Permission.APPROVE_BDF_CONFIRMATION));

        //Birth for ARG
        argBirthLink.putAll(adrBirthLink);

        //Death Registration for DEO
        deoDeathLink.put("eprInitDeathDeclaration.do", new Link("death_registration.label", "/popreg/deaths/", "eprInitDeathDeclaration.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprInitLateDeathDeclaration.do", new Link("late_death_registration.label", "/popreg/deaths/", "eprInitLateDeathDeclaration.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathApprovalAndPrint.do", new Link("death_approve_print_list.label", "/popreg/deaths/", "eprDeathApprovalAndPrint.do", Permission.EDIT_DEATH));

        deoDeathLink.put("eprDeathFilterByStatus.do", new Link(null, "/popreg/deaths/", "eprDeathFilterByStatus.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprInitDeathHome.do", new Link(null, "/popreg/deaths/", "eprInitDeathHome.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathDeclaration.do", new Link(null, "/popreg/deaths/", "eprDeathDeclaration.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathCertificate.do", new Link(null, "/popreg/deaths/", "eprDeathCertificate.do", Permission.EDIT_DEATH));

        deoDeathLink.put("eprDeleteDeath.do", new Link(null, "/popreg/deaths/", "eprDeleteDeath.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathViewMode.do", new Link(null, "/popreg/deaths/", "eprDeathViewMode.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprPrintDeathCertificate.do", new Link(null, "/popreg/deaths/", "eprPrintDeathCertificate.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathEditMode.do", new Link(null, "/popreg/deaths/", "eprDeathEditMode.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathBackToPreviousState.do", new Link(null, "/popreg/deaths/", "eprDeathBackToPreviousState.do", Permission.EDIT_DEATH));

        // Death Registration for ADR
        adrDeathLink.putAll(deoDeathLink);
        adrDeathLink.put("eprApproveDeath.do", new Link(null, "/popreg/deaths/", "eprApproveDeath.do", Permission.APPROVE_DEATH));
        adrDeathLink.put("eprDirectApproveDeath.do", new Link(null, "/popreg/deaths/", "eprDirectApproveDeath.do", Permission.APPROVE_DEATH));
        adrDeathLink.put("eprRejectDeath.do", new Link(null, "/popreg/deaths/", "eprRejectDeath.do", Permission.APPROVE_DEATH));
        adrDeathLink.put("eprDierctPrintDeathCertificate.do", new Link(null, "/popreg/deaths/", "eprDierctPrintDeathCertificate.do", Permission.PRINT_DEATH_CERTIFICATE));
        adrDeathLink.put("eprDirectApproveIgnoringWornings.do", new Link(null, "/popreg/deaths/", "eprDirectApproveIgnoringWornings.do", Permission.APPROVE_DEATH));
        
		// Death Registration for ARG
        argDeathLink.putAll(adrDeathLink);

        // Adoption Registration for DEO
        deoAdoptionLink.put("eprAdoptionRegistrationAction.do", new Link("adoption_registration.label", "/popreg/adoption/", "eprAdoptionRegistrationAction.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionApprovalAndPrint.do", new Link("adoption_approval_and_print.lable", "/popreg/adoption/", "eprAdoptionApprovalAndPrint.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionApplicantInfo.do", new Link("adoption_applicant.label", "/popreg/adoption/", "eprAdoptionApplicantInfo.do", Permission.EDIT_ADOPTION));

        deoAdoptionLink.put("eprAdoptionAction.do", new Link(null, "/popreg/adoption/", "eprAdoptionAction.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionRegistrationHome.do", new Link(null, "/popreg/adoption/", "eprAdoptionRegistrationHome.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionProcessApplicantInfo.do", new Link(null, "/popreg/adoption/", "eprAdoptionProcessApplicantInfo.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprBirthRegistrationHome.do", new Link(null, "/popreg/adoption/", "eprBirthRegistrationHome.do", Permission.EDIT_ADOPTION));
        //    deoAdoptionLink.put(Permission.PAGE_ADOPTION_APPLICANT_INFO, new Link("adoption_certificate.label", "/popreg/adoption/", "eprAdoptionCertificate.do"));
        deoAdoptionLink.put("eprAdoptionBirthRegistrationInit.do", new Link(null, "/popreg/births/", "eprAdoptionBirthRegistrationInit.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionViewMode.do", new Link(null, "/popreg/adoption/", "eprAdoptionViewMode.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprMarkDirectlyAdoptionNoticeAsPrinted.do", new Link(null, "/popreg/adoption/", "eprMarkDirectlyAdoptionNoticeAsPrinted.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("cancelPrintAdoptionNotice.do", new Link(null, "/popreg/adoption/", "cancelPrintAdoptionNotice.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionNoticeDirectPrint.do", new Link(null, "/popreg/adoption/", "eprAdoptionNoticeDirectPrint.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionCertificateRequest.do", new Link(null, "/popreg/adoption/", "eprAdoptionCertificateRequest.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionFind.do", new Link(null, "/popreg/adoption/", "eprAdoptionFind.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprDeleteAdoption.do", new Link(null, "/popreg/adoption/", "eprDeleteAdoption.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionEditMode.do", new Link(null, "/popreg/adoption/", "eprAdoptionEditMode.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprPrintAdoptionNotice.do", new Link(null, "/popreg/adoption/", "eprPrintAdoptionNotice.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprMarkAdoptionNoticeAsPrinted.do", new Link(null, "/popreg/adoption/", "eprMarkAdoptionNoticeAsPrinted.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprPrintAdoptionCertificate.do", new Link(null, "/popreg/adoption/", "eprPrintAdoptionCertificate.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprMarkAdoptionCertificateAsPrinted.do", new Link(null, "/popreg/adoption/", "eprMarkAdoptionCertificateAsPrinted.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionNext.do", new Link(null, "/popreg/adoption/", "eprAdoptionNext.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionPrevious.do", new Link(null, "/popreg/adoption/", "eprAdoptionPrevious.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionFilterByStatus.do", new Link(null, "/popreg/adoption/", "eprAdoptionFilterByStatus.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionBackToPreviousState.do", new Link(null, "/popreg/adoption/", "eprAdoptionBackToPreviousState.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprCaptureAdoptionApplicantInfo.do", new Link(null, "/popreg/adoption/", "eprCaptureAdoptionApplicantInfo.do", Permission.EDIT_ADOPTION));
        //deoAdoptionLink.put(Permission.PAGE_ADOPTION_BDF_HOME, new Link(null, "/popreg/births/", "eprAdoptionRegistrationHome.do"));

        // Adoption Registration for ADR
        adrAdoptionLink.putAll(deoAdoptionLink);

        //Adoption Registration for ARG
        argAdoptionLink.putAll(adrAdoptionLink);
        argAdoptionLink.put("eprApproveAdoption.do", new Link(null, "/popreg/adoption/", "eprApproveAdoption.do", Permission.APPROVE_ADOPTION));
        argAdoptionLink.put("eprRejectAdoption.do", new Link(null, "/popreg/adoption/", "eprRejectAdoption.do", Permission.APPROVE_ADOPTION));
        argAdoptionLink.put("eprAdoptionDirectApproval.do", new Link(null, "/popreg/adoption/", "eprAdoptionDirectApproval.do", Permission.APPROVE_ADOPTION));

        // assemble menu for admins : insertion - order
        adminLinks.put("admin", adminLink);
        adminLinks.put("preference", preferanceLink);

        // assemble menu for deo
        deoLinks.put("birth", deoBirthLink);
        deoLinks.put("death", deoDeathLink);
        deoLinks.put("adoption", deoAdoptionLink);
        deoLinks.put("preference", preferanceLink);

        // assemble menu for adr
        adrLinks.put("birth", adrBirthLink);
        adrLinks.put("death", adrDeathLink);
        adrLinks.put("adoption", adrAdoptionLink);
        adrLinks.put("preference", preferanceLink);
        adrLinks.put("prs", prsLink);

        //assemble menu for arg
        argLinks.put("birth", argBirthLink);
        argLinks.put("death", argDeathLink);
        argLinks.put("adoption", argAdoptionLink);
        argLinks.put("preference", preferanceLink);
        argLinks.put("prs", prsLink);
    }

    public static Map<String, Map> getAllowedLinks(Role role) {
        String roleName = role.getRoleId();
        if (roleName.equals(Role.ROLE_ADMIN)) {
            return adminLinks;
        } else if (roleName.equals(Role.ROLE_DEO)) {
            return deoLinks;
        } else if (roleName.equals(Role.ROLE_ARG)) {
            return argLinks;
        } else if (roleName.equals(Role.ROLE_RG)) {  //for now arg gets the same menu as ARG
            return argLinks;
        } else {  // for now dr also gets the same menu as ADR.
            return adrLinks;
        }
    }
}