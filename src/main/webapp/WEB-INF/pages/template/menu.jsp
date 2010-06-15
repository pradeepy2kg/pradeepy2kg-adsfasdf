<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="main-menu">
    <a href="eprBirthConfirmationReport.do"><s:label value="%{getText('birth_conformation_report.label')}"/></a>
    <a href="eprBirthRegistration.do"><s:label value="%{getText('birth_registration.label')}"/></a>
    <a href="eprBirthConfirmation.do"><s:label value="%{getText('birth_confirmation.label')}"/></a>
    <a href="eprBirthRegisterApproval.do"><s:label value="%{getText('birth_register_approval.label')}"/></a>
    <a href="eprFilterBirthConfirmPrint.do"><s:label value="%{getText('birth_confirmation_print.label')}"/></a>
    <a href="eprUserCreation.do"><s:label value="%{getText('creat_user.label')}"/></a>
    <a href="eprUserPreferencesInit.do"><s:label value="%{getText('userPreference.label')}"/></a>
</div>