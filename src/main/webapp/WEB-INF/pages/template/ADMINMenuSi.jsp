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
                පරිපාලන කටයුතු
            </a>
            <ul class="acitem">
                <li>
                    <a id="creat_user.label" href="/ecivil/management/eprInitUserCreation.do">
                        පරිශිලකයකු ඇතුල් කිරීම
                    </a>
                </li>
                <li>
                    <a id="viewUsers.label" href="/ecivil/management/eprViewUsers.do">
                        පරිශීලකයන් සෙවීම
                    </a>
                </li>
                <li>
                    <a id="addDivision.label" href="/ecivil/management/eprInitAddDivisionsAndDsDivisions.do">
                        කොට්ඨාශ  ඇතුල් කිරීම
                    </a>
                </li>
                <li>
                    <a id="registrar.add" href="/ecivil/management/eprRegistrarsAdd.do">
                        නව රෙජිස්ට්‍රාර් වරයකු ඇතුල් කිරීම
                    </a>
                </li>
                <li>
                    <a id="search.registrar" href="/ecivil/management/eprFindRegistrar.do">
                        රෙජිස්ට්‍රාර්වරයකු සෙවීම
                    </a>
                </li>
                <li>
                    <a id="registrars.managment" href="/ecivil/management/eprRegistrarsManagment.do">
                        රෙජිස්ට්‍රාර් වරු පරිපාලනය
                    </a>
                </li>
                <li>
                    <a id="eventManege.label" href="/ecivil/management/eprInitEventsManagement.do">
                        වාර්තා ගත පද්ධති ක්‍රියාකාරකම්
                    </a>
                </li>
                <li>
                    <a id="indexRecords.label" href="/ecivil/management/eprIndexRecords.do">
                        දත්ත සුචි ගත කිරීම
                    </a>
                </li>
                <li>
                    <a id="report.title" href="/ecivil/management/eprInitCreateReports.do">
                        වාර්තා සැකසීම
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
                පුද්ගලික තේරීම්
            </a>
            <ul class="acitem">
                <li>
                    <a id="userPreference.label" href="/ecivil/preferences/eprUserPreferencesInit.do">
                        පරිශීලක අභිමතය
                    </a>
                </li>
                <li>
                    <a id="changePassword.label" href="/ecivil/preferences/eprpassChangePageLoad.do">
                        මුරපදය වෙනස් කිරීම
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</div>
