<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" type="text/css" href='/ecivil/css/menu.css'/>

<div id="xmain-menu">
    <ul class="menu">
        <li>
            <a href="#">
                <s:label value="%{getText('category_admin_task')}"/>
            </a>
            <ul class="acitem">
                <li>
                    <a id="creat_user.label" href="/ecivil/management/eprInitUserCreation.do">
                        <s:label value="%{getText('creat_user.label')}"/>
                    </a>
                </li>
                <li>
                    <a id="viewUsers.label" href="/ecivil/management/eprViewUsers.do">
                        <s:label value="%{getText('viewUsers.label')}"/>
                    </a>
                </li>
                <li>
                    <a id="addDivision.label" href="/ecivil/management/eprInitAddDivisionsAndDsDivisions.do">
                        <s:label value="%{getText('addDivision.label')}"/>
                    </a>
                </li>
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
                    <a id="eventManege.label" href="/ecivil/management/eprInitEventsManagement.do">
                        <s:label value="%{getText('eventManege.label')}"/>
                    </a>
                </li>
                <li>
                    <a id="indexRecords.label" href="/ecivil/management/eprIndexRecords.do">
                        <s:label value="%{getText('indexRecords.label')}"/>
                    </a>
                </li>
                <li>
                    <a id="search.registrar" href="/ecivil/management/eprFindRegistrar.do">
                        <s:label value="%{getText('search.registrar')}"/>
                    </a>
                </li>
                <li>
                    <a id="report.title" href="/ecivil/management/eprInitCreateReports.do">
                        <s:label value="%{getText('report.title')}"/>
                    </a>
                </li>
            </ul>
        </li>
        <li>
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
    </ul>
</div>
