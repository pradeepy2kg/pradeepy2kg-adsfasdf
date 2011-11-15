<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" type="text/css" href='/ecivil/css/menu.css'/>

<div id="xmain-menu">
    <ul class="menu">
        <s:if test="%{#session.context=='admin'}">
        <li class="exp">
            </s:if>
            <s:else>
        <li>
            </s:else>
            <a href="/ecivil/eprStatHome.do">
                ADMIN TASK
            </a>
            <ul class="acitem">
                <li>
                    <a id="creat_user.label" href="/ecivil/management/eprInitUserCreation.do">
                        Create User
                    </a>
                </li>
                <li>
                    <a id="viewUsers.label" href="/ecivil/management/eprViewUsers.do">
                        Search Users
                    </a>
                </li>
                <li>
                    <a id="addDivision.label" href="/ecivil/management/eprInitAddDivisionsAndDsDivisions.do">
                        Add Divisions
                    </a>
                </li>
                <li>
                    <a id="registrar.add" href="/ecivil/management/eprRegistrarsAdd.do">
                        Add Registrar
                    </a>
                </li>
                <li>
                    <a id="search.registrar" href="/ecivil/management/eprFindRegistrar.do">
                        Search Registrar
                    </a>
                </li>
                <li>
                    <a id="registrars.managment" href="/ecivil/management/eprRegistrarsManagment.do">
                        Manage Registrars
                    </a>
                </li>
                <li>
                    <a id="eventManege.label" href="/ecivil/management/eprInitEventsManagement.do">
                        View Events
                    </a>
                </li>
                <li>
                    <a id="indexRecords.label" href="/ecivil/management/eprIndexRecords.do">
                        Index Records
                    </a>
                </li>
                <li>
                    <a id="report.title" href="/ecivil/management/eprInitCreateReports.do">
                        Report Generation
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
    </ul>
</div>
