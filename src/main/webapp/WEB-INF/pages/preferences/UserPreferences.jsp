<%@ page import="lk.rgd.crs.web.action.UserPreferencesAction" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
 <script>
        function view_DSDivs() {
            dojo.event.topic.publish("view_DSDivs");
        }

        function view_BDDivs() {
            dojo.event.topic.publish("view_BDDivs");
        }
    </script>
    <s:url id="loadDSDivList" action="../ajaxSupport_loadDSDivListUserPreferences"/>

<div id="user-preference-outer">
    <s:form action="eprUserPreferencesAction" name="user_preference_form" id="user_preference" method="POST">
        <table width="100%" cellpadding="0" cellspacing="5">
            <tr>
                <td>
                    <div id="set-language" class="font-10">
                        <label>භාෂාව / *in tamil / Language </label>
                    </div>
                </td>
                <td><s:select list="#@java.util.HashMap@{'en':'English','si':'සිංහල','ta':'Tamil'}" name="prefLanguage"
                              value="%{#session.WW_TRANS_I18N_LOCALE.language}"></s:select>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="user-district" class="font-10">
                        <label>දිස්ත්‍රික්කය / மாவட்டம் / District</label>
                    </div>
                </td>
                <td><s:select name="birthDistrictId" list="districtList" value="birthDistrictId"
                                  onchange="javascript:view_DSDivs();return false;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="user-ds-division" class="font-10">
                        <label>D.S.කොට්ඨාශය / பிரிவு / D.S. Division</label>
                    </div>
                </td>
                <td><sx:div id="dsDivisionId" value="dsDivisionId" href="%{loadDSDivList}" theme="ajax"
                                            listenTopics="view_DSDivs" formId="user_preference"></sx:div>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <div class="form-submit">
                        <s:submit value="%{getText('submit.label')}" cssStyle="float:right;"></s:submit>                     
                        <s:submit action="passChangePageLoad" value="%{getText('change_pass.label')}" cssStyle="float:right;"/>
                    </div>
                </td>
            </tr>
        </table>
    </s:form>
</div>
<%-- Styling Completed --%>