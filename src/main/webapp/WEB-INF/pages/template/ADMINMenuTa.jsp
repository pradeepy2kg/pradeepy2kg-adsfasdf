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
            <a href="#">
                நிருவாக நடவடிக்கை
            </a>
            <ul class="acitem">
                <li>
                    <a id="creat_user.label" href="/ecivil/management/eprInitUserCreation.do">
                        பாவனையாளரை உட்புகுத்தல்
                    </a>
                </li>
                <li>
                    <a id="viewUsers.label" href="/ecivil/management/eprViewUsers.do">
                        பாவனையாளர் தேடுதல்
                    </a>
                </li>
                <li>
                    <a id="addDivision.label" href="/ecivil/management/eprInitAddDivisionsAndDsDivisions.do">
                        பிரிவினை உட்புகுத்தல்
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
                <li>
                    <a id="registrars.managment" href="/ecivil/management/eprRegistrarsManagment.do">
                        பதிவாளார் நிருவாகம்
                    </a>
                </li>
                <li>
                    <a id="eventManege.label" href="/ecivil/management/eprInitEventsManagement.do">
                        முகாமைத்துவம்
                    </a>
                </li>
                <li>
                    <a id="indexRecords.label" href="/ecivil/management/eprIndexRecords.do">
                        சுட்டெண் அறிக்கை
                    </a>
                </li>
                <li>
                    <a id="report.title" href="/ecivil/management/eprInitCreateReports.do">
                        Report Generation ta
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
