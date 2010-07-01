<%@ page import="lk.rgd.crs.web.action.UserPreferencesAction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>


<div id="user-preference-outer">
    <br>
    <s:form action="eprUserPreferencesAction" name="user_preference_form" id="user_preference" method="POST">
        <table  width="100%" cellpadding="0" cellspacing="5">
            <tr>
                <td>
                    <div id="set-language" class="font-10">
                        <label>Select Language </label>
                    </div>
                </td>
                <td><s:select list="#@java.util.HashMap@{'en':'English','si':'සිංහල','ta':'Tamil'}" name="prefLanguage"
                              value="%{#session.WW_TRANS_I18N_LOCALE.language}"></s:select>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="user-district" class="font-10">
                        <label>දිස්ත්‍රික්කය மாவட்டம் District</label>
                    </div>
                </td>
                <td><s:select name="prefDistrictId" list="districtList" headerKey="0"/>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="user-ds-division" class="font-10">
                        <label>D.S.කොට්ඨාශය பிரிவு D.S. Division</label>
                    </div>
                </td>
                <td><s:select name="prefDSDivisionId" list="dsDivisionList" headerKey="0"/>
                </td>
            </tr>
        </table>
        <br><br>

        <div class="form-submit">
            <s:submit value="%{getText('submit.label')}"></s:submit>
        </div>
        <br><br>

        <div id="passChange">
            <s:url id="url" action="passChangePageLoad">
            </s:url>
            <s:a href="%{url}"><s:label value="%{getText('change_pass.label')}"></s:label></s:a>
        </div>
    </s:form>
</div>