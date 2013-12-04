<%@ page import="lk.rgd.crs.web.WebConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" type="text/css" href='/ecivil/css/menu.css'/>

<div id="xmain-menu">
    <ul class="menu">
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
        <s:if test="%{#session.context=='statistics'}">
        <li class="exp">
            </s:if>
            <s:else>
        <li>
            </s:else>
            <a href="/ecivil/management/eprInitCreateReports.do">
                   සංඛ්‍යාලේඛන
            </a>
        </li>
    </ul>
</div>

