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
            <a href="/ecivil/management/eprInitCreateReports.do">
                  புள்ளிவிபரவியல்
            </a>
        </li>
    </ul>
</div>

