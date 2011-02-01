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
    private static final Map searchLink = new LinkedHashMap();

    // deo menu items
    private static final Map deoBirthLink = new LinkedHashMap();
    private static final Map deoAdoptionLink = new LinkedHashMap();
    private static final Map deoDeathLink = new LinkedHashMap();
    private static final Map deoAlterationLink = new LinkedHashMap();
    private static final Map deoMarriageLink = new LinkedHashMap();
    private static final Map deoPRSLink = new LinkedHashMap();

    // adr menu items
    private static final Map adrBirthLink = new LinkedHashMap();
    private static final Map adrAdoptionLink = new LinkedHashMap();
    private static final Map adrDeathLink = new LinkedHashMap();
    private static final Map adrAlterationLink = new LinkedHashMap();
    private static final Map adrAdminLink = new LinkedHashMap();
    private static final Map adrMarriageLink = new LinkedHashMap();
    private static final Map adrPRSLink = new LinkedHashMap();

    //arg menu items
    private static final Map argBirthLink = new LinkedHashMap();
    private static final Map argDeathLink = new LinkedHashMap();
    private static final Map argAdoptionLink = new LinkedHashMap();
    private static final Map argAlterationLink = new LinkedHashMap();
    private static final Map argAdminLink = new LinkedHashMap();
    private static final Map argMarriageLink = new LinkedHashMap();
    private static final Map argPRSLink = new LinkedHashMap();

    static {

        //Admin
        adminLink.put("eprInitUserCreation.do", new Link("creat_user.label", "/ecivil/management/", "eprInitUserCreation.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprViewUsers.do", new Link("viewUsers.label", "/ecivil/management/", "eprViewUsers.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprInitAddDivisionsAndDsDivisions.do", new Link("addDivision.label", "/ecivil/management/", "eprInitAddDivisionsAndDsDivisions.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));
        adminLink.put("eprUserCreation.do", new Link(null, "/ecivil/management/", "eprUserCreation.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprInactiveUsers.do", new Link(null, "/ecivil/management/", "eprInactiveUsers.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprDoInactiveUsers.do", new Link(null, "/ecivil/management/", "eprDoInactiveUsers.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprActiveUsers.do", new Link(null, "/ecivil/management/", "eprActiveUsers.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprViewSelectedUsers.do", new Link(null, "/ecivil/management/", "eprViewSelectedUsers.do", Permission.USER_MANAGEMENT));
        adminLink.put("eprActivateOrInactivateDivisionsAndDsDivisions.do", new Link(null, "/ecivil/management/", "eprActivateOrInactivateDivisionsAndDsDivisions.do", Permission.SERVICE_MASTER_DATA_MANAGEMENT));
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
        adminLink.put("eprFindRegistrar.do", new Link("search.registrar", "/ecivil/management/", "eprFindRegistrar.do", Permission.SEARCH_REGISTRAR));
        adminLink.put("eprEditPrimaryLocation.do", new Link(null, "/ecivil/management/", "eprEditPrimaryLocation.do", Permission.REGISTRAR_MANAGEMENT));
        adminLink.put("eprInitCreateReports.do", new Link("report.title", "/ecivil/management/", "eprInitCreateReports.do", Permission.GENERATE_REPORTS));
        adminLink.put("eprCreateReports.do", new Link(null, "/ecivil/management/", "eprCreateReports.do", Permission.GENERATE_REPORTS));
        adminLink.put("eprPopulateStatistics.do", new Link(null, "/ecivil/management/", "eprPopulateStatistics.do", Permission.GENERATE_REPORTS));


        adrAdminLink.put("eprRegistrarsManagment.do", new Link("registrars.managment", "/ecivil/management/", "eprRegistrarsManagment.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprRegistrarsView.do", new Link(null, "/ecivil/management/", "eprRegistrarsView.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprRegistrarsFilter.do", new Link(null, "/ecivil/management/", "eprRegistrarsFilter.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprRegistrarsAdd.do", new Link("registrar.add", "/ecivil/management/", "eprRegistrarsAdd.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprAssignmentAddDirect.do", new Link(null, "/ecivil/management/", "eprAssignmentAddDirect.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprAssignmentAdd.do", new Link(null, "/ecivil/management/", "eprAssignmentAdd.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprSearchRegistrarByPin.do", new Link(null, "/ecivil/management/", "eprSearchRegistrarByPin.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprAssignmentEdit.do", new Link(null, "/ecivil/management/", "eprAssignmentEdit.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprUpdateRegistrar.do", new Link(null, "/ecivil/management/", "eprUpdateRegistrar.do", Permission.REGISTRAR_MANAGEMENT));
        adrAdminLink.put("eprFindRegistrar.do", new Link("search.registrar", "/ecivil/management/", "eprFindRegistrar.do", Permission.REGISTRAR_MANAGEMENT));

        //User Preferance
        preferanceLink.put("eprUserPreferencesInit.do", new Link("userPreference.label", "/ecivil/preferences/", "eprUserPreferencesInit.do", Permission.USER_PREFERENCES));
        preferanceLink.put("eprpassChangePageLoad.do", new Link("changePassword.label", "/ecivil/preferences/", "eprpassChangePageLoad.do", Permission.USER_PREFERENCES));
        preferanceLink.put("eprChangePass.do", new Link(null, "/ecivil/preferences/", "eprChangePass.do", Permission.USER_PREFERENCES));
        preferanceLink.put("eprBackChangePass.do", new Link(null, "/ecivil/preferences/", "eprBackChangePass.do", Permission.USER_PREFERENCES));
        preferanceLink.put("passChangePageLoad.do", new Link(null, "/ecivil/preferences/", "passChangePageLoad.do", Permission.USER_PREFERENCES));
        preferanceLink.put("eprUserPreferencesAction.do", new Link(null, "/ecivil/preferences/", "eprUserPreferencesAction.do", Permission.USER_PREFERENCES));
        preferanceLink.put("showPasswordChangeStatistics.do", new Link(null, "/ecivil/preferences/", "showPasswordChangeStatistics.do", Permission.USER_PREFERENCES));

        //search links
        searchLink.put("eprSearchPageLoad.do", new Link("search.label", "/ecivil/births/", "eprSearchPageLoad.do", Permission.SEARCH_BDF));
        searchLink.put("eprBirthCertificateSearch.do", new Link("birth_certificate_search.label", "/ecivil/births/", "eprBirthCertificateSearch.do", Permission.SEARCH_BDF));
        searchLink.put("eprDeathCertificateSearch.do", new Link("death_certificate_search.label", "/ecivil/deaths/", "eprDeathCertificateSearch.do", Permission.SEARCH_DDF));
        searchLink.put("eprBirthsAdvancedSearch.do", new Link("birth.advanceSearch.label", "/ecivil/births/", "eprBirthsAdvancedSearch.do", Permission.SEARCH_BDF));

        searchLink.put("eprMarkBirthCertificateSearch.do", new Link(null, "/ecivil/births/", "eprMarkBirthCertificateSearch.do", Permission.PRINT_BDF));
        searchLink.put("eprMarkDeathCertificateSearch.do", new Link(null, "/ecivil/deaths/", "eprMarkDeathCertificateSearch.do", Permission.PRINT_DDF));

        // PRS links for DEO
        deoPRSLink.put("eprExistingPersonRegInit.do", new Link("prs.personRegistration.label", "/ecivil/prs/", "eprExistingPersonRegInit.do", Permission.PRS_ADD_PERSON));
        deoPRSLink.put("eprExistingPersonRegistration.do", new Link(null, "/ecivil/prs/", "eprExistingPersonRegistration.do", Permission.PRS_ADD_PERSON));

        // PRS links for ADR
        adrPRSLink.putAll(deoPRSLink);
        adrPRSLink.put("eprEditPerson.do", new Link(null, "/ecivil/prs/", "eprEditPerson.do", Permission.PRS_EDIT_PERSON));
        adrPRSLink.put("eprPRSAdvancedSearch.do", new Link("prs.advanceSearch.label", "/ecivil/prs/", "eprPRSAdvancedSearch.do", Permission.SEARCH_PRS));
        adrPRSLink.put("eprPersonApproval.do", new Link("prs.personApproval.label", "/ecivil/prs/", "eprPersonApproval.do", Permission.SEARCH_PRS));
        adrPRSLink.put("eprApprovePerson.do", new Link(null, "/ecivil/prs/", "eprApprovePerson.do", Permission.PRS_APPROVE_PERSON));
        adrPRSLink.put("eprPersonDetails.do", new Link(null, "/ecivil/prs/", "eprPersonDetails.do", Permission.PRS_VIEW_PERSON));
        adrPRSLink.put("eprPersonSearchNext.do", new Link(null, "/ecivil/prs/", "eprPersonSearchNext.do", Permission.SEARCH_PRS));
        adrPRSLink.put("eprPersonSearchPrevious.do", new Link(null, "/ecivil/prs/", "eprPersonSearchPrevious.do", Permission.SEARCH_PRS));
        adrPRSLink.put("eprDirectEditPerson.do", new Link(null, "/ecivil/prs/", "eprDirectEditPerson.do", Permission.PRS_EDIT_PERSON));
        adrPRSLink.put("eprDirectApprovePerson.do", new Link(null, "/ecivil/prs/", "eprDirectApprovePerson.do", Permission.PRS_APPROVE_PERSON));
        adrPRSLink.put("eprMarkPRSCertificate.do", new Link(null, "/ecivil/prs/", "eprMarkPRSCertificate.do", Permission.PRS_PRINT_CERT));
        adrPRSLink.put("eprDirectMarkPRSCertificate.do", new Link(null, "/ecivil/prs/", "eprDirectMarkPRSCertificate.do", Permission.PRS_PRINT_CERT));
        adrPRSLink.put("eprPRSCertificate.do", new Link(null, "/ecivil/prs/", "eprPRSCertificate.do", Permission.PRS_PRINT_CERT));
        adrPRSLink.put("eprDirectPRSCertificate.do", new Link(null, "/ecivil/prs/", "eprDirectPRSCertificate.do", Permission.PRS_PRINT_CERT));
        adrPRSLink.put("eprBackRegisterDetails.do", new Link(null, "/ecivil/prs/", "eprBackRegisterDetails.do", Permission.PRS_ADD_PERSON));
        adrPRSLink.put("eprBackToRegisterDetails.do", new Link(null, "/ecivil/prs/", "eprBackToRegisterDetails.do", Permission.PRS_ADD_PERSON));
        adrPRSLink.put("eprBackPRSSearchList.do", new Link(null, "/ecivil/prs/", "eprBackPRSSearchList.do", Permission.PRS_APPROVE_PERSON));
        adrPRSLink.put("eprLoadRejectPerson.do", new Link(null, "/ecivil/prs/", "eprLoadRejectPerson.do", Permission.PRS_REJECT_PERSON));
        adrPRSLink.put("eprRejectPerson.do", new Link(null, "/ecivil/prs/", "eprRejectPerson.do", Permission.PRS_REJECT_PERSON));
        adrPRSLink.put("eprLoadDeletePerson.do", new Link(null, "/ecivil/prs/", "eprLoadDeletePerson.do", Permission.PRS_DELETE_PERSON));
        adrPRSLink.put("eprDeletePerson.do", new Link(null, "/ecivil/prs/", "eprDeletePerson.do", Permission.PRS_DELETE_PERSON));
        adrPRSLink.put("eprApproveIgnoreWarning.do", new Link(null, "/ecivil/prs/", "eprApproveIgnoreWarning.do", Permission.PRS_APPROVE_PERSON));

        // PRS links for ARG
        argPRSLink.putAll(adrPRSLink);

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
        deoBirthLink.put("eprViewBDFInNonEditableMode.do", new Link(null, "/ecivil/births/", "eprViewBDFInNonEditableMode.do", Permission.SEARCH_BDF));

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
        deoDeathLink.put("eprInitSuddenDeathDeclaration.do", new Link("sudden_death_registration.label", "/ecivil/deaths/", "eprInitSuddenDeathDeclaration.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprInitMissingPersonDeclaration.do", new Link("missing_person_registration.label", "/ecivil/deaths/", "eprInitMissingPersonDeclaration.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathApprovalAndPrint.do", new Link("death_approve_print_list.label", "/ecivil/deaths/", "eprDeathApprovalAndPrint.do", Permission.EDIT_DEATH));

        deoDeathLink.put("eprDeathFilterByStatus.do", new Link(null, "/ecivil/deaths/", "eprDeathFilterByStatus.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathFilterByStatusNext.do", new Link(null, "/ecivil/deaths/", "eprDeathFilterByStatusNext.do", Permission.EDIT_DEATH));
        deoDeathLink.put("eprDeathFilterByStatusPreviouse.do", new Link(null, "/ecivil/deaths/", "eprDeathFilterByStatusPreviouse.do", Permission.EDIT_DEATH));
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
        deoAlterationLink.put("eprEditBirthAlteration.do", new Link(null, "/ecivil/alteration/", "eprEditBirthAlteration.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprEditBirthAlterationInit.do", new Link(null, "/ecivil/alteration/", "eprEditBirthAlterationInit.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprPrintBirthAlterarionNotice.do", new Link(null, "/ecivil/alteration/", "eprPrintBirthAlterarionNotice.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprBirthAlteration.do", new Link(null, "/ecivil/alteration/", "eprBirthAlteration.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprDeleteBirthAlteration.do", new Link(null, "/ecivil/alteration/", "eprDeleteBirthAlteration.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprMarkBirthAlterationAsPrint.do", new Link(null, "/ecivil/alteration/", "eprMarkBirthAlterationAsPrint.do", Permission.EDIT_BIRTH_ALTERATION));

        //todo complete
        //links related to deo or above for death alteration
        //every one has permission to add birth alteration has permiassion to add death alteration
        deoAlterationLink.put("eprDeathAlterationSearchHome.do", new Link("death.registration.alteration", "/ecivil/alteration/", "eprDeathAlterationSearchHome.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprCaptureDeathAlteration.do", new Link(null, "/ecivil/alteration/", "eprCaptureDeathAlteration.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprApproveDeathAlterations.do", new Link(null, "/ecivil/alteration/", "eprApproveDeathAlterations.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprApproveDeathAlterationsInit.do", new Link("label.manage.alterations", "/ecivil/alteration/", "eprApproveDeathAlterationsInit.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprBirthAlterationApprovalNext.do", new Link(null, "/ecivil/alteration/", "eprBirthAlterationApprovalNext.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprBirthAlterationApprovalPrevious.do", new Link(null, "/ecivil/alteration/", "eprBirthAlterationApprovalPrevious.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprFilterBirthAlteration.do", new Link(null, "/ecivil/alteration/", "eprFilterBirthAlteration.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprDeathAlterationPrintLetter.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationPrintLetter.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprDeathAlterationPageLoad.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationPageLoad.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprEditDeathAlteration.do", new Link(null, "/ecivil/alteration/", "eprEditDeathAlteration.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprDeathAlterationEditInit.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationEditInit.do", Permission.EDIT_BIRTH_ALTERATION));
        deoAlterationLink.put("eprDeathAlterationDelete.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationDelete.do", Permission.EDIT_BIRTH_ALTERATION));

        // Birth Alteration for ADR
        adrAlterationLink.putAll(deoAlterationLink);

        //Birth Alteration for ARG
        argAlterationLink.putAll(adrAlterationLink);

        //death alteration approvals
        argAlterationLink.put("eprApproveDeathAlterationsDirect.do", new Link(null, "/ecivil/alteration/", "eprApproveDeathAlterationsDirect.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprDeathAlterationSetBits.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationSetBits.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprDeathAlterationReject.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationReject.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprDeathAlterationApplyChanges.do", new Link(null, "/ecivil/alteration/", "eprDeathAlterationApplyChanges.do", Permission.APPROVE_BIRTH_ALTERATION));

        argAlterationLink.put("eprApproveBirthAlterationInit.do", new Link(null, "/ecivil/alteration/", "eprApproveBirthAlterationInit.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprApproveBirthAlteration.do", new Link(null, "/ecivil/births/", "eprApproveBirthAlteration.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprRejectBirthAlteration.do", new Link(null, "/ecivil/births/", "eprRejectBirthAlteration", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprRejectBirthAlterationInit.do", new Link(null, "/ecivil/births/", "eprRejectBirthAlterationInit", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprBirthAlterationApplyChanges.do", new Link(null, "/ecivil/alteration/", "eprBirthAlterationApplyChanges.do", Permission.APPROVE_BIRTH_ALTERATION));
        argAlterationLink.put("eprApproveAndApplyBirthAlteration.do", new Link(null, "/ecivil/alteration/", "eprApproveAndApplyBirthAlteration.do", Permission.APPROVE_BIRTH_ALTERATION));

        // DEO marriages links
        deoMarriageLink.put("eprMarriageNoticeInit.do", new Link(null, "/ecivil/marriages/", "eprMarriageNoticeInit.do", Permission.EDIT_MARRIAGE));
        deoMarriageLink.put("eprSelectNoticeType.do", new Link("menu.marriage.notice", "/ecivil/marriages/", "eprSelectNoticeType.do", Permission.ADD_MARRIAGE));
        deoMarriageLink.put("eprMarriageRegisterSearchInit.do", new Link("menu.marriage.register.search", "/ecivil/marriages/", "eprMarriageRegisterSearchInit.do", Permission.SEARCH_MARRIAGE));
        deoMarriageLink.put("eprMarriageLicenseSearchInit.do", new Link("menu.marriagelicense.search", "/ecivil/marriages/", "eprMarriageLicenseSearchInit.do", Permission.SEARCH_MARRIAGE));
        deoMarriageLink.put("eprMarriageNoticeAdd.do", new Link(null, "/ecivil/marriages/", "eprMarriageNoticeAdd.do", Permission.ADD_MARRIAGE));
        deoMarriageLink.put("eprMarriageRegistrationInit.do", new Link("menu.marriage.registration", "/ecivil/marriages/", "eprMarriageRegistrationInit.do", Permission.EDIT_MARRIAGE));
        deoMarriageLink.put("eprMarriageNoticeEditInit.do", new Link(null, "/ecivil/marriages/", "eprMarriageNoticeEditInit.do", Permission.EDIT_MARRIAGE));
        deoMarriageLink.put("eprMarriageNoticeEdit.do", new Link(null, "/ecivil/marriages/", "eprMarriageNoticeEdit.do", Permission.EDIT_MARRIAGE));
        deoMarriageLink.put("eprMarriageSecondNoticeAdd.do", new Link(null, "/ecivil/marriages/", "eprMarriageSecondNoticeAdd.do", Permission.EDIT_MARRIAGE));
        deoMarriageLink.put("eprMarriageNoticeDelete.do", new Link(null, "/ecivil/marriages/", "eprMarriageNoticeDelete.do", Permission.DELETE_MARRIAGE));
        deoMarriageLink.put("eprRollBackNoticeToPrevious.do", new Link(null, "/ecivil/marriages/", "eprMarriageNoticeDelete.do", Permission.EDIT_MARRIAGE));
        deoMarriageLink.put("eprRollBackNoticeToPreviousAndEditNotice.do", new Link(null, "/ecivil/marriages/", "eprRollBackNoticeToPreviousAndEditNotice.do", Permission.EDIT_MARRIAGE));
        deoMarriageLink.put("eprLicenseToMarriage.do", new Link(null, "/ecivil/marriages/", "eprLicenseToMarriage.do", Permission.PRINT_MARRIAGE_LICENSE));
        deoMarriageLink.put("eprMarkLicenseAsPrinted.do", new Link(null, "/ecivil/marriages/", "eprMarkLicenseAsPrinted.do", Permission.PRINT_MARRIAGE_LICENSE));
        deoMarriageLink.put("eprRegisterNewMarriage.do", new Link(null, "/ecivil/marriages/", "eprRegisterNewMarriage.do", Permission.ADD_MARRIAGE));
        deoMarriageLink.put("eprUpdateMarriage.do", new Link(null, "/ecivil/marriages/", "eprUpdateMarriage.do", Permission.EDIT_MARRIAGE));
        deoMarriageLink.put("eprRegisterNoticedMarriage.do", new Link(null, "/ecivil/marriages/", "eprRegisterNoticedMarriage.do", Permission.EDIT_MARRIAGE));
        deoMarriageLink.put("eprMarriageExtractInit.do", new Link(null, "/ecivil/marriages/", "eprMarriageExtractInit.do", Permission.PRINT_MARRIAGE_EXTRACT));
        deoMarriageLink.put("eprMarkMarriageExtractAsPrinted.do", new Link(null, "/ecivil/marriages/", "eprMarkMarriageExtractAsPrinted.do", Permission.PRINT_MARRIAGE_EXTRACT));
        deoMarriageLink.put("eprDisplayScannedImage.do", new Link(null, "/ecivil/marriages/", "eprDisplayScannedImage.do", Permission.VIEW_SCANNED_MARRIAGE_CERT));
        deoMarriageLink.put("eprViewMarriageRegister.do", new Link(null, "/ecivil/marriages/", " eprViewMarriageRegister.do", Permission.VIEW_MARRIAGE_REGISTER));
        deoMarriageLink.put("eprMarriageNoticeSearchInit.do", new Link("menu.marriage.notice.search", "/ecivil/marriages/", "eprMarriageNoticeSearchInit.do", Permission.SEARCH_MARRIAGE));
        deoMarriageLink.put("eprMarriageRegisterSearch.do", new Link(null, "/ecivil/marriages/", "eprMarriageRegisterSearch.do", Permission.SEARCH_MARRIAGE));
        deoMarriageLink.put("eprMarriageNoticeSearchNext.do", new Link(null, "/ecivil/marriages/", "eprMarriageNoticeSearchNext.do", Permission.SEARCH_MARRIAGE));
        deoMarriageLink.put("eprMarriageRegisterSearchNext.do", new Link(null, "/ecivil/marriages/", "eprMarriageRegisterSearchNext.do", Permission.SEARCH_MARRIAGE));
        deoMarriageLink.put("eprMarriageNoticeSearchPrevious.do", new Link(null, "/ecivil/marriages/", "eprMarriageNoticeSearchPrevious.do", Permission.SEARCH_MARRIAGE));
        deoMarriageLink.put("eprMarriageRegisterSearchPrevious.do", new Link(null, "/ecivil/marriages/", "eprMarriageRegisterSearchPrevious.do", Permission.SEARCH_MARRIAGE));
        deoMarriageLink.put("eprMarriageRegistrationHome.do", new Link(null, "/ecivil/marriages/", "eprMarriageRegistrationHome.do", Permission.SEARCH_MARRIAGE));
        deoMarriageLink.put("eprMarriageLicenseSearch.do", new Link(null, "/ecivil/marriages/", "eprMarriageLicenseSearch.do", Permission.SEARCH_MARRIAGE));

        // ADR marriage links
        adrMarriageLink.putAll(deoMarriageLink);
        adrMarriageLink.put("eprApproveMarriageNotice.do", new Link(null, "/ecivil/marriages/", "eprApproveMarriageNotice.do", Permission.APPROVE_MARRIAGE));
        adrMarriageLink.put("eprMarriageNoticeRejectInit.do", new Link(null, "/ecivil/marriages/", "eprMarriageNoticeRejectInit.do", Permission.APPROVE_MARRIAGE));
        adrMarriageLink.put("eprMarriageNoticeReject.do", new Link(null, "/ecivil/marriages/", "eprMarriageNoticeReject.do", Permission.APPROVE_MARRIAGE));
        adrMarriageLink.put("eprApproveMarriageRegistration.do", new Link(null, "/ecivil/marriages/", "eprApproveMarriageRegistration.do", Permission.APPROVE_MARRIAGE));
        adrMarriageLink.put("eprRejectMarriageRegistration.do", new Link(null, "/ecivil/marriages/", "eprRejectMarriageRegistration.do", Permission.APPROVE_MARRIAGE));
        adrMarriageLink.put("eprRegisterAndApproveNewMarriage.do", new Link(null, "/ecivil/marriages/", "eprRegisterAndApproveNewMarriage.do", Permission.APPROVE_MARRIAGE));
        adrMarriageLink.put("eprUpdateAndApproveMuslimMarriage.do", new Link(null, "/ecivil/marriages/", "eprUpdateAndApproveMuslimMarriage.do", Permission.APPROVE_MARRIAGE));
        adrMarriageLink.put("eprMarriageRegisterRejectInit.do", new Link(null, "/ecivil/marriages/", "eprMarriageRegisterRejectInit.do", Permission.APPROVE_MARRIAGE));
        adrMarriageLink.put("eprDivorce.do", new Link(null, "/ecivil/marriages/", "eprDivorce.do", Permission.DIVORCE));
        adrMarriageLink.put("eprMarriageRegisterDivorceInit.do", new Link(null, "/ecivil/marriages/", "eprMarriageRegisterDivorceInit.do", Permission.DIVORCE));

        // ARG marriage links
        argMarriageLink.putAll(adrMarriageLink);

        //marriage related links ends

        // assemble menu for admins : insertion - order
        adminLinks.put("admin", adminLink);
        adminLinks.put("preference", preferanceLink);


        // assemble menu for deo
        deoLinks.put("birth", deoBirthLink);
        deoLinks.put("death", deoDeathLink);
        deoLinks.put("adoption", deoAdoptionLink);
        deoLinks.put("alteration", deoAlterationLink);
        deoLinks.put("preference", preferanceLink);
        deoLinks.put("prs", deoPRSLink);
        deoLinks.put("certificateSearch", searchLink);
        deoLinks.put("marriage", deoMarriageLink);

        // assemble menu for adr
        adrLinks.put("birth", adrBirthLink);
        adrLinks.put("death", adrDeathLink);
        adrLinks.put("adoption", adrAdoptionLink);
        adrLinks.put("alteration", adrAlterationLink);
        adrLinks.put("preference", preferanceLink);
        adrLinks.put("prs", adrPRSLink);
        adrLinks.put("certificateSearch", searchLink);
        adrLinks.put("admin", adrAdminLink);
        adrLinks.put("marriage", adrMarriageLink);

        //assemble menu for arg
        argLinks.put("birth", argBirthLink);
        argLinks.put("death", argDeathLink);
        argLinks.put("adoption", argAdoptionLink);
        argLinks.put("alteration", argAlterationLink);
        argLinks.put("preference", preferanceLink);
        argLinks.put("prs", argPRSLink);
        argLinks.put("certificateSearch", searchLink);
        argLinks.put("admin", argAdminLink);
        argLinks.put("marriage", argMarriageLink);

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