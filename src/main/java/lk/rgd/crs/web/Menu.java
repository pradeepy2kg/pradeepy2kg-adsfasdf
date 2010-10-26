package lk.rgd.crs.web;

import lk.rgd.Permission;
import lk.rgd.common.api.domain.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

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
    private static final Map searchLink = new LinkedHashMap();

    // deo menu items
    private static final Map deoBirthLink = new LinkedHashMap();
    private static final Map deoAdoptionLink = new LinkedHashMap();
    private static final Map deoDeathLink = new LinkedHashMap();
    private static final Map deoAlterationLink = new LinkedHashMap();

    // adr menu items
    private static final Map adrBirthLink = new LinkedHashMap();
    private static final Map adrAdoptionLink = new LinkedHashMap();
    private static final Map adrDeathLink = new LinkedHashMap();
    private static final Map adrAlterationLink = new LinkedHashMap();
    private static final Map adrAdminLink = new LinkedHashMap();

    //arg menu items
    private static final Map argBirthLink = new LinkedHashMap();
    private static final Map argDeathLink = new LinkedHashMap();
    private static final Map argAdoptionLink = new LinkedHashMap();
    private static final Map argAlterationLink = new LinkedHashMap();
    private static final Map argAdminLink = new LinkedHashMap();

    static {
        //Admin
        adminLink.put("eprInitUserCreation.do", new Link("creat_user.label", "/ecivil/management/", "eprInitUserCreation.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprViewUsers.do", new Link("viewUsers.label", "/ecivil/management/", "eprViewUsers.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprInitAddDivisionsAndDsDivisions.do", new Link("addDivision.label", "/ecivil/management/", "eprInitAddDivisionsAndDsDivisions.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));
        adminLink.put("eprUserCreation.do", new Link(null, "/ecivil/management/", "eprUserCreation.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprdeleteUsers.do", new Link(null, "/ecivil/management/", "eprdeleteUsers.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprViewSelectedUsers.do", new Link(null, "/ecivil/management/", "eprViewSelectedUsers.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprActiveDivisionsAndDsDivisions.do", new Link(null, "/ecivil/management/", "eprActiveDivisionsAndDsDivisions.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));
        adminLink.put("eprInactiveDivisionsAndDsDivisions.do", new Link(null, "/ecivil/management/", "eprInactiveDivisionsAndDsDivisions.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));
        adminLink.put("eprAddDivisionsAndDsDivisions.do", new Link(null, "/ecivil/management/", "eprAddDivisionsAndDsDivisions.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));
        adminLink.put("eprInitDivisionList.do", new Link(null, "/ecivil/management/", "eprInitDivisionList.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));
        adminLink.put("eprRegistrarsManagment.do", new Link("registrars.managment", "/ecivil/management/", "eprRegistrarsManagment.do", Permission.REGISTRAR_MANAGEMENT));
        adminLink.put("eprRegistrarsView.do", new Link(null, "/ecivil/management/", "eprRegistrarsView.do", Permission.REGISTRAR_MANAGEMENT));
        adminLink.put("eprRegistrarsFilter.do", new Link(null, "/ecivil/management/", "eprRegistrarsFilter.do", Permission.REGISTRAR_MANAGEMENT));
        adminLink.put("eprRegistrarsAdd.do", new Link("registrar.add", "/ecivil/management/", "eprRegistrarsAdd.do", Permission.REGISTRAR_MANAGEMENT));
        adminLink.put("eprAssignmentAddDirect.do", new Link(null, "/ecivil/management/", "eprAssignmentAddDirect.do", Permission.REGISTRAR_MANAGEMENT));
        adminLink.put("eprAssignmentAdd.do", new Link(null, "/ecivil/management/", "eprAssignmentAdd.do", Permission.REGISTRAR_MANAGEMENT));
        adminLink.put("eprSearchRegistrarByPin.do", new Link(null, "/ecivil/management/", "eprSearchRegistrarByPin.do", Permission.REGISTRAR_MANAGEMENT));
        adminLink.put("eprAssignmentEdit.do", new Link(null, "/ecivil/management/", "eprAssignmentEdit.do", Permission.REGISTRAR_MANAGEMENT));
        adminLink.put("eprUpdateRegistrar.do", new Link(null, "/ecivil/management/", "eprUpdateRegistrar.do", Permission.REGISTRAR_MANAGEMENT));
        adminLink.put("eprInitEventsManagement.do", new Link("eventManege.label", "/ecivil/management/", "eprInitEventsManagement.do", Permission.EVENTS_MANAGEMENT));
        adminLink.put("eprDetailsDisplay.do", new Link(null, "/ecivil/management/", "eprDetailsDisplay.do", Permission.EVENTS_MANAGEMENT));
        adminLink.put("eprEventPrevious.do", new Link(null, "/ecivil/management/", "eprEventPrevious.do", Permission.EVENTS_MANAGEMENT));
        adminLink.put("eprEventNext.do", new Link(null, "/ecivil/management/", "eprEventNext.do", Permission.EVENTS_MANAGEMENT));
        adminLink.put("eprFilterEventsList.do", new Link(null, "/ecivil/management/", "eprFilterEventsList.do", Permission.EVENTS_MANAGEMENT));
        adminLink.put("eprInitAssignedUserLocation.do", new Link(null, "/ecivil/management/", "eprInitAssignedUserLocation.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprAssignedUserLocation.do", new Link(null, "/ecivil/management/", "eprAssignedUserLocation.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprInactiveUserLocation.do", new Link(null, "/ecivil/management/", "eprInactiveUserLocation.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprActiveUserLocation.do", new Link(null, "/ecivil/management/", "eprActiveUserLocation.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprEditAssignedUserLocation.do", new Link(null, "/ecivil/management/", "eprEditAssignedUserLocation.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprIndexRecords.do", new Link("indexRecords.label", "/ecivil/management/", "eprIndexRecords.do", Permission.INDEX_RECORDS));

        adrAdminLink.put("eprRegistrarsManagment.do", new Link("registrars.managment", "/ecivil/management/", "eprRegistrarsManagment.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprRegistrarsView.do", new Link(null, "/ecivil/management/", "eprRegistrarsView.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprRegistrarsFilter.do", new Link(null, "/ecivil/management/", "eprRegistrarsFilter.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprRegistrarsAdd.do", new Link("registrar.add", "/ecivil/management/", "eprRegistrarsAdd.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprAssignmentAddDirect.do", new Link(null, "/ecivil/management/", "eprAssignmentAddDirect.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprAssignmentAdd.do", new Link(null, "/ecivil/management/", "eprAssignmentAdd.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprSearchRegistrarByPin.do", new Link(null, "/ecivil/management/", "eprSearchRegistrarByPin.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprAssignmentEdit.do", new Link(null, "/ecivil/management/", "eprAssignmentEdit.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprUpdateRegistrar.do", new Link(null, "/ecivil/management/", "eprUpdateRegistrar.do", Permission.REGISTRAR_MANAGEMENT));

        //User Preferance
        preferanceLink.put("eprUserPreferencesInit.do", new Link("userPreference.label", "/ecivil/preferences/", "eprUserPreferencesInit.do", Permission.USER_PREFERENCES));
        preferanceLink.put("eprChangePass.do", new Link(null, "/ecivil/preferences/", "eprChangePass.do", Permission.USER_PREFERENCES));
        preferanceLink.put("eprBackChangePass.do", new Link(null, "/ecivil/preferences/", "eprBackChangePass.do", Permission.USER_PREFERENCES));
        preferanceLink.put("passChangePageLoad.do", new Link(null, "/ecivil/preferences/", "passChangePageLoad.do", Permission.USER_PREFERENCES));
        preferanceLink.put("eprUserPreferencesAction.do", new Link(null, "/ecivil/preferences/", "eprUserPreferencesAction.do", Permission.USER_PREFERENCES));

        //search links
        searchLink.put("eprSearchPageLoad.do", new Link("search.label", "/ecivil/births/", "eprSearchPageLoad.do", Permission.SEARCH_BDF));
        searchLink.put("eprBirthCertificateSearch.do", new Link("birth_certificate_search.label", "/ecivil/births/", "eprBirthCertificateSearch.do", Permission.SEARCH_BDF));
        searchLink.put("eprDeathCertificateSearch.do", new Link("death_certificate_search.label", "/ecivil/deaths/", "eprDeathCertificateSearch.do", Permission.SEARCH_DDF));
        searchLink.put("eprBirthsAdvancedSearch.do", new Link("birth.advanceSearch.label", "/ecivil/births/", "eprBirthsAdvancedSearch.do", Permission.SEARCH_BDF));

        searchLink.put("eprMarkBirthCertificateSearch.do", new Link(null, "/ecivil/births/", "eprMarkBirthCertificateSearch.do", Permission.PRINT_BDF));
        searchLink.put("eprMarkDeathCertificateSearch.do", new Link(null, "/ecivil/deaths/", "eprMarkDeathCertificateSearch.do", Permission.PRINT_DDF));

        // PRS links
        prsLink.put("eprPRSAdvancedSearch.do", new Link("prs.advanceSearch.label", "/ecivil/prs/", "eprPRSAdvancedSearch.do", Permission.SEARCH_PRS));
        prsLink.put("eprExistingPersonRegInit.do", new Link("prs.personRegistration.label", "/ecivil/prs/", "eprExistingPersonRegInit.do", Permission.PRS_ADD_PERSON));
        prsLink.put("eprExistingPersonRegistration.do", new Link(null, "/ecivil/prs/", "eprExistingPersonRegistration.do", Permission.PRS_ADD_PERSON));
        prsLink.put("eprPersonDetails.do", new Link(null, "/ecivil/prs/", "eprPersonDetails.do", Permission.PRS_VIEW_PERSON));

        // Birth Registration for DEO
        deoBirthLink.put("eprBirthRegistrationInit.do", new Link("birth_registration.label", "/ecivil/births/", "eprBirthRegistrationInit.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprStillBirthRegistrationInit.do", new Link("still_birth_registration.label", "/ecivil/births/", "eprStillBirthRegistrationInit.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprBirthConfirmationPrintList.do", new Link("birth_confirmation_print.label", "/ecivil/births/", "eprBirthConfirmationPrintList.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprBirthConfirmationInit.do", new Link("birth_confirmation.label", "/ecivil/births/", "eprBirthConfirmationInit.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprBirthCertificateList.do", new Link("print_birthcertificate.label", "/ecivil/births/", "eprBirthCertificateList.do", Permission.SEARCH_BDF));

        deoBirthLink.put("eprHome.do", new Link(null, "/ecivil/births/", "eprHome.do", Permission.USER_PREFERENCES));
        deoBirthLink.put("eprBirthRegistration.do", new Link(null, "/ecivil/births/", "eprBirthRegistration.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprBirthRegistrationHome.do", new Link(null, "/ecivil/births/", "eprBirthRegistrationHome.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprStillBirth.do", new Link(null, "/ecivil/births/", "eprStillBirth.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprDirectPrintBirthConfirmation.do", new Link(null, "/ecivil/births/", "eprDirectPrintBirthConfirmation.do", Permission.EDIT_BDF));
        deoBirthLink.put("eprMarkCertificateAsPrinted.do", new Link(null, "/ecivil/births/", "eprMarkCertificateAsPrinted.do", Permission.PRINT_BDF));
        adrBirthLink.put("eprViewBDFInNonEditableMode.do", new Link(null, "/ecivil/births/", "eprViewBDFInNonEditableMode.do", Permission.EDIT_BDF));
        adrBirthLink.put("eprBirthAlteration.do", new Link(null, "/ecivil/births/", "eprBirthAlteration.do", Permission.EDIT_BDF));

        deoBirthLink.put("eprBirthConfirmation.do", new Link(null, "/ecivil/births/", "eprBirthConfirmation.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprDirectPrintBirthCertificate.do", new Link(null, "/ecivil/births/", "eprDirectPrintBirthCertificate.do", Permission.PRINT_BDF));
        deoBirthLink.put("eprMarkBirthConfirmationAsPrint.do", new Link(null, "/ecivil/births/", "eprMarkBirthConfirmationAsPrint.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprBirthCancelConfirmationPrint.do", new Link(null, "/ecivil/births/", "eprBirthCancelConfirmationPrint.do", Permission.EDIT_BDF_CONFIRMATION));

        deoBirthLink.put("eprFilterBirthConfirmationPrint.do", new Link(null, "/ecivil/births/", "eprFilterBirthConfirmationPrint.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprBirthConfirmationPrintPage.do", new Link(null, "/ecivil/births/", "eprBirthConfirmationPrintPage.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprBirthConfirmationBulkPrint.do", new Link(null, "/ecivil/births/", "eprBirthConfirmationBulkPrint.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprPrintNext.do", new Link(null, "/ecivil/births/", "eprPrintNext.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprPrintPrevious.do", new Link(null, "/ecivil/births/", "eprPrintPrevious.do", Permission.EDIT_BDF_CONFIRMATION));
        deoBirthLink.put("eprBirthConfirmationSkipChanges.do", new Link(null, "/ecivil/births/", "eprBirthConfirmationSkipChanges.do", Permission.EDIT_BDF_CONFIRMATION));

        deoBirthLink.put("eprBDFSearchBySerialNo.do", new Link(null, "/ecivil/births/", "eprBDFSearchBySerialNo.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprBDFSearchByIdUKey.do", new Link(null, "/ecivil/births/", "eprBDFSearchByIdUKey.do", Permission.SEARCH_BDF));

        deoBirthLink.put("eprFilterBirthCetificateList.do", new Link(null, "/ecivil/births/", "eprFilterBirthCetificateList.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprBirthCertificate.do", new Link(null, "/ecivil/births/", "eprBirthCertificate.do", Permission.PRINT_BDF));
        deoBirthLink.put("eprBirthCancelCertificatePrint.do", new Link(null, "/ecivil/births/", "eprBirthCancelCertificatePrint.do", Permission.PRINT_BDF));
        deoBirthLink.put("eprBirthCertificateBulkPrint.do", new Link(null, "/ecivil/births/", "eprBirthCertificateBulkPrint.do", Permission.PRINT_BDF));
        deoBirthLink.put("eprCertificatePrintNext.do", new Link(null, "/ecivil/births/", "eprCertificatePrintNext.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprCertificatePrintPrevious.do", new Link(null, "/ecivil/births/", "eprCertificatePrintPrevious.do", Permission.SEARCH_BDF));
        deoBirthLink.put("eprStillBirthCertificatePrint.do", new Link(null, "/ecivil/births/", "eprStillBirthCertificatePrint.do", Permission.PRINT_BDF));
        deoBirthLink.put("eprDirectPrintStillBirthCertificate.do", new Link(null, "/ecivil/births/", "eprDirectPrintStillBirthCertificate.do", Permission.PRINT_BDF));

        // Birth for ADR
        adrBirthLink.putAll(deoBirthLink);
        adrBirthLink.put("eprBirthRegisterApproval.do", new Link("birth_register_approval.label", "/ecivil/births/", "eprBirthRegisterApproval.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprBirthConfirmationApproval.do", new Link("birth_confirmation_approval.label", "/ecivil/births/", "eprBirthConfirmationApproval.do", Permission.APPROVE_BDF_CONFIRMATION));

        adrBirthLink.put("eprApprovalRefresh.do", new Link(null, "/ecivil/births/", "eprApprovalRefresh.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprConfirmationApprovalRefresh.do", new Link(null, "/ecivil/births/", "eprConfirmationApprovalRefresh.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprConfirmationApprovalNext.do", new Link(null, "/ecivil/births/", "eprConfirmationApprovalNext.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprConfirmationApprovalPrevious.do", new Link(null, "/ecivil/births/", "eprConfirmationApprovalPrevious.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprApproveConfirmationBulk.do", new Link(null, "/ecivil/births/", "eprApproveConfirmationBulk.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprConfrimationChangesDirectApproval.do", new Link(null, "/ecivil/births/", "eprConfrimationChangesDirectApproval.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprBirthCertificatDirectPrint.do", new Link(null, "/ecivil/births/", "eprBirthCertificatDirectPrint.do", Permission.PRINT_BDF));
        adrBirthLink.put("eprApproveBulk.do", new Link(null, "/ecivil/births/", "eprApproveBulk.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprApproveBirthDeclaration.do", new Link(null, "/ecivil/births/", "eprApproveBirthDeclaration.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprApprovalNext.do", new Link(null, "/ecivil/births/", "eprApprovalNext.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprApprovalPrevious.do", new Link(null, "/ecivil/births/", "eprApprovalPrevious.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprDeleteApprovalPending.do", new Link(null, "/ecivil/births/", "eprDeleteApprovalPending.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprIgnoreWarning.do", new Link(null, "/ecivil/births/", "eprIgnoreWarning.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprRejectBirthDeclaration.do", new Link(null, "/ecivil/births/", "eprRejectBirthDeclaration.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprApproveBirthConfirmation.do", new Link(null, "/ecivil/births/", "eprApproveBirthConfirmation.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprConfirmationIgnoreWarning.do", new Link(null, "/ecivil/births/", "eprConfirmationIgnoreWarning.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprRejectBirthConfirmation.do", new Link(null, "/ecivil/births/", "eprRejectBirthConfirmation.do", Permission.APPROVE_BDF_CONFIRMATION));
        adrBirthLink.put("eprDirectApprove.do", new Link(null, "/ecivil/births/", "eprDirectApprove.do", Permission.APPROVE_BDF));
        adrBirthLink.put("eprDirectApproveIgnoreWarning.do", new Link(null, "/ecivil/births/", "eprDirectApproveIgnoreWarning.do", Permission.APPROVE_BDF));
        adrBirthLink.put("ConfirmationDirectApprovalIngoreWarning.do", new Link(null, "/ecivil/births/", "ConfirmationDirectApprovalIngoreWarning.do", Permission.APPROVE_BDF_CONFIRMATION));


        //Birth for ARG
        argBirthLink.putAll(adrBirthLink);
        argBirthLink.put("eprBirthRegisterBelatedApproval.do", new Link("birth_register_belated_approval.label", "/ecivil/births/", "eprBirthRegisterBelatedApproval.do", Permission.APPROVE_BDF_BELATED));

        argBirthLink.put("eprBelatedApprovalRefresh.do", new Link(null, "/ecivil/births/", "eprBelatedApprovalRefresh.do", Permission.APPROVE_BDF_BELATED));
        argBirthLink.put("eprApproveBelatedBirthDeclaration.do", new Link(null, "/ecivil/births/", "eprApproveBelatedBirthDeclaration.do", Permission.APPROVE_BDF_BELATED));
        argBirthLink.put("eprBelatedIgnoreWarning.do", new Link(null, "/ecivil/births/", "eprBelatedIgnoreWarning.do", Permission.APPROVE_BDF_BELATED));
        argBirthLink.put("eprRejectBelatedBirthDeclaration.do", new Link(null, "/ecivil/births/", "eprRejectBelatedBirthDeclaration.do", Permission.APPROVE_BDF_BELATED));
        argBirthLink.put("eprDeleteBelatedApprovalPending.do", new Link(null, "/ecivil/births/", "eprDeleteBelatedApprovalPending.do", Permission.APPROVE_BDF_BELATED));

        argAdminLink.putAll(adrAdminLink);

        //Death Registration for DEO
        deoDeathLink.put("eprInitDeathDeclaration.do", new Link("death_registration.label", "/ecivil/deaths/", "eprInitDeathDeclaration.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprInitLateDeathDeclaration.do", new Link("late_death_registration.label", "/ecivil/deaths/", "eprInitLateDeathDeclaration.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathApprovalAndPrint.do", new Link("death_approve_print_list.label", "/ecivil/deaths/", "eprDeathApprovalAndPrint.do", Permission.EDIT_DEATH));

        deoDeathLink.put("eprDeathFilterByStatus.do", new Link(null, "/ecivil/deaths/", "eprDeathFilterByStatus.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprInitDeathHome.do", new Link(null, "/ecivil/deaths/", "eprInitDeathHome.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathDeclaration.do", new Link(null, "/ecivil/deaths/", "eprDeathDeclaration.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathCertificate.do", new Link(null, "/ecivil/deaths/", "eprDeathCertificate.do", Permission.EDIT_DEATH));

        deoDeathLink.put("eprDeleteDeath.do", new Link(null, "/ecivil/deaths/", "eprDeleteDeath.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathViewMode.do", new Link(null, "/ecivil/deaths/", "eprDeathViewMode.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathEditMode.do", new Link(null, "/ecivil/deaths/", "eprDeathEditMode.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathBackToPreviousState.do", new Link(null, "/ecivil/deaths/", "eprDeathBackToPreviousState.do", Permission.EDIT_DEATH));

        // Death Registration for ADR
        adrDeathLink.putAll(deoDeathLink);
        adrDeathLink.put("eprApproveDeath.do", new Link(null, "/ecivil/deaths/", "eprApproveDeath.do", Permission.APPROVE_DEATH));
        adrDeathLink.put("eprDirectApproveDeath.do", new Link(null, "/ecivil/deaths/", "eprDirectApproveDeath.do", Permission.APPROVE_DEATH));
        adrDeathLink.put("eprRejectDeath.do", new Link(null, "/ecivil/deaths/", "eprRejectDeath.do", Permission.APPROVE_DEATH));
        adrDeathLink.put("eprDirectApproveIgnoringWornings.do", new Link(null, "/ecivil/deaths/", "eprDirectApproveIgnoringWornings.do", Permission.APPROVE_DEATH));
        adrDeathLink.put("eprPrintDeathCertificate.do", new Link(null, "/ecivil/deaths/", "eprPrintDeathCertificate.do", Permission.PRINT_DEATH_CERTIFICATE));
        adrDeathLink.put("eprDierctPrintDeathCertificate.do", new Link(null, "/ecivil/deaths/", "eprDierctPrintDeathCertificate.do", Permission.PRINT_DEATH_CERTIFICATE));

        // Death Registration for ARG
        argDeathLink.putAll(adrDeathLink);

        // Adoption Registration for DEO
        deoAdoptionLink.put("eprAdoptionRegistrationAction.do", new Link("adoption_registration.label", "/ecivil/adoption/", "eprAdoptionRegistrationAction.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionApprovalAndPrint.do", new Link("adoption_approval_and_print.lable", "/ecivil/adoption/", "eprAdoptionApprovalAndPrint.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionApplicantInfo.do", new Link("adoption_applicant.label", "/ecivil/adoption/", "eprAdoptionApplicantInfo.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionReRegistration.do", new Link("adoption_re_registration.label", "/ecivil/adoption/", "eprAdoptionReRegistration.do", Permission.EDIT_ADOPTION));

        deoAdoptionLink.put("eprAdoptionAction.do", new Link(null, "/ecivil/adoption/", "eprAdoptionAction.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionRegistrationHome.do", new Link(null, "/ecivil/adoption/", "eprAdoptionRegistrationHome.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionProcessApplicantInfo.do", new Link(null, "/ecivil/adoption/", "eprAdoptionProcessApplicantInfo.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprBirthRegistrationHome.do", new Link(null, "/ecivil/adoption/", "eprBirthRegistrationHome.do", Permission.EDIT_ADOPTION));
        //    deoAdoptionLink.put(Permission.PAGE_ADOPTION_APPLICANT_INFO, new Link("adoption_certificate.label", "/ecivil/adoption/", "eprAdoptionCertificate.do"));
        deoAdoptionLink.put("eprAdoptionBirthRegistrationInit.do", new Link(null, "/ecivil/births/", "eprAdoptionBirthRegistrationInit.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionViewMode.do", new Link(null, "/ecivil/adoption/", "eprAdoptionViewMode.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprMarkDirectlyAdoptionNoticeAsPrinted.do", new Link(null, "/ecivil/adoption/", "eprMarkDirectlyAdoptionNoticeAsPrinted.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("cancelPrintAdoptionNotice.do", new Link(null, "/ecivil/adoption/", "cancelPrintAdoptionNotice.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionNoticeDirectPrint.do", new Link(null, "/ecivil/adoption/", "eprAdoptionNoticeDirectPrint.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionCertificateRequest.do", new Link(null, "/ecivil/adoption/", "eprAdoptionCertificateRequest.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionFind.do", new Link(null, "/ecivil/adoption/", "eprAdoptionFind.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprDeleteAdoption.do", new Link(null, "/ecivil/adoption/", "eprDeleteAdoption.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionEditMode.do", new Link(null, "/ecivil/adoption/", "eprAdoptionEditMode.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprPrintAdoptionNotice.do", new Link(null, "/ecivil/adoption/", "eprPrintAdoptionNotice.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprMarkAdoptionNoticeAsPrinted.do", new Link(null, "/ecivil/adoption/", "eprMarkAdoptionNoticeAsPrinted.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprPrintAdoptionCertificate.do", new Link(null, "/ecivil/adoption/", "eprPrintAdoptionCertificate.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprMarkAdoptionCertificateAsPrinted.do", new Link(null, "/ecivil/adoption/", "eprMarkAdoptionCertificateAsPrinted.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionNext.do", new Link(null, "/ecivil/adoption/", "eprAdoptionNext.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionPrevious.do", new Link(null, "/ecivil/adoption/", "eprAdoptionPrevious.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionFilterByStatus.do", new Link(null, "/ecivil/adoption/", "eprAdoptionFilterByStatus.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionBackToPreviousState.do", new Link(null, "/ecivil/adoption/", "eprAdoptionBackToPreviousState.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprCaptureAdoptionApplicantInfo.do", new Link(null, "/ecivil/adoption/", "eprCaptureAdoptionApplicantInfo.do", Permission.EDIT_ADOPTION));
        deoAdoptionLink.put("eprAdoptionReRegistrationFindEntry.do", new Link(null, "/ecivil/adoption/", "eprAdoptionReRegistrationFindEntry.do", Permission.EDIT_ADOPTION));
        //deoAdoptionLink.put(Permission.PAGE_ADOPTION_BDF_HOME, new Link(null, "/ecivil/births/", "eprAdoptionRegistrationHome.do"));

        // Adoption Registration for ADR
        adrAdoptionLink.putAll(deoAdoptionLink);

        //Adoption Registration for ARG
        argAdoptionLink.putAll(adrAdoptionLink);
        argAdoptionLink.put("eprApproveAdoption.do", new Link(null, "/ecivil/adoption/", "eprApproveAdoption.do", Permission.APPROVE_ADOPTION));
        argAdoptionLink.put("eprRejectAdoption.do", new Link(null, "/ecivil/adoption/", "eprRejectAdoption.do", Permission.APPROVE_ADOPTION));
        argAdoptionLink.put("eprAdoptionDirectApproval.do", new Link(null, "/ecivil/adoption/", "eprAdoptionDirectApproval.do", Permission.APPROVE_ADOPTION));


        //birth alteration for DEO
        deoAlterationLink.put("eprBirthAlterationInit.do", new Link("birth_alteration.label", "/ecivil/alteration/", "eprBirthAlterationInit.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprBirthAlterationPendingApproval.do", new Link("birth_alteration_pending_approval.title", "/ecivil/alteration/", "eprBirthAlterationPendingApproval.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprBirthAlteration.do", new Link(null, "/ecivil/alteration/", "eprBirthAlteration.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprBirthAlterationSearch.do", new Link(null, "/ecivil/alteration/", "eprBirthAlterationSearch.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprBirthAlterationHome.do", new Link(null, "/ecivil/alteration/", "eprBirthAlterationHome.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprEditSelectedAlteration.do", new Link(null, "/ecivil/alteration/", "eprEditSelectedAlteration.do", Permission.EDIT_BIRTH_ALTERATION));

        //todo complete
        //links related to deo or above for death alteration
        //every one has permission to add birth alteration has permiassion to add death alteration
        deoAlterationLink.put("eprDeathAlterationSearchHome.do", new Link("death.registration.alteration", "/ecivil/alteration/", "eprDeathAlterationSearchHome.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprCaptureDeathAlteration.do", new Link(null, "/ecivil/alteration/", "eprCaptureDeathAlteration.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprApproveDeathAlterations.do", new Link("label.manage.alterations", "/ecivil/alteration/", "eprApproveDeathAlterations.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprAlterationApprovalNext.do", new Link(null, "/ecivil/alteration/", "eprAlterationApprovalNext.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprAlterationApprovalPrevious.do", new Link(null, "/ecivil/alteration/", "eprAlterationApprovalPrevious.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprFilterAlteration.do", new Link(null, "/ecivil/alteration/", "eprFilterAlteration.do", Permission.EDIT_BIRTH_ALTERATION));
        // Birth Alteration for ADR
        adrAlterationLink.putAll(deoAlterationLink);

        //Birth Alteration for ARG
        argAlterationLink.putAll(adrAlterationLink);

        //death alteration approvals
        argAlterationLink.put("eprApproveDeathAlterationsDirect.do", new Link(null, "/ecivil/alteration/", "eprApproveDeathAlterationsDirect.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprDeathAlterationSetBits.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationSetBits.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprDeathAlterationReject.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationReject.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprDeathAlterationDelate.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationDelate.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprDeathAlterationEdit.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationEdit.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprDeathAlterationApplyChanges.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationApplyChanges.do", Permission.APPROVE_BIRTH_ALTERATION));

        argAlterationLink.put("eprApproveSelectedAlteration.do", new Link(null, "/ecivil/alteration/", "eprApproveSelectedAlteration.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprApproveAlteration.do", new Link(null, "/ecivil/births/", "eprApproveAlteration.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprRejectSelectedAlteration.do", new Link(null, "/ecivil/births/", "eprRejectSelectedAlteration", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprBirthAlterationApplyChanges.do", new Link(null, "/ecivil/alteration/", "eprBirthAlterationApplyChanges.do", Permission.APPROVE_BIRTH_ALTERATION));

        // assemble menu for admins : insertion - order
        adminLinks.put("admin", adminLink);
        adminLinks.put("preference", preferanceLink);


        // assemble menu for deo
        deoLinks.put("birth", deoBirthLink);
        deoLinks.put("death", deoDeathLink);
        deoLinks.put("adoption", deoAdoptionLink);
        deoLinks.put("alteration", deoAlterationLink);
        deoLinks.put("preference", preferanceLink);
        deoLinks.put("certificateSearch", searchLink);

        // assemble menu for adr
        adrLinks.put("birth", adrBirthLink);
        adrLinks.put("death", adrDeathLink);
        adrLinks.put("adoption", adrAdoptionLink);
        adrLinks.put("alteration", adrAlterationLink);
        adrLinks.put("preference", preferanceLink);
        adrLinks.put("prs", prsLink);
        adrLinks.put("certificateSearch", searchLink);
        adrLinks.put("admin", adrAdminLink);

        //assemble menu for arg
        argLinks.put("birth", argBirthLink);
        argLinks.put("death", argDeathLink);
        argLinks.put("adoption", argAdoptionLink);
        argLinks.put("alteration", argAlterationLink);
        argLinks.put("preference", preferanceLink);
        argLinks.put("prs", prsLink);
        argLinks.put("certificateSearch", searchLink);
        argLinks.put("admin", argAdminLink);
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