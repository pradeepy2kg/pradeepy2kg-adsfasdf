<%@ page import="lk.rgd.crs.web.action.UserPreferencesAction" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script>
    // mode 1 = passing District, will return DS list
    // mode 2 = passing DsDivision, will return BD list
    // any other = passing district, will return DS list and the BD list for the first DS
    $(function() {
        $('select#districtId').bind('change', function(evt1) {
            var id = $("select#districtId").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivisionId").html(options1);

                        var options2 = '';
                        var bd = data.bdDivisionList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#birthDivisionId").html(options2);
                    });
        });

        $('select#dsDivisionId').bind('change', function(evt2) {
            var id = $("select#dsDivisionId").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#birthDivisionId").html(options);
                    });
        });
    });


</script>

<s:url id="loadDSDivList" action="../ajaxSupport_loadDSDivListUserPreferences"/>

<div id="user-preference-outer">
    <s:form action="eprUserPreferencesAction" name="user_preference_form" id="user_preference" method="POST">
        <table width="100%" cellpadding="0" cellspacing="5" class="user-preference-table">
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
                <td colspan="6" class="table_reg_cell_01">
                    <s:select id="districtId" name="birthDistrictId" list="districtList" value="birthDistrictId"
                              cssStyle="width:98.5%;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="user-ds-division" class="font-10">
                        <label>D.S.කොට්ඨාශය / பிரிவு / D.S. Division</label>
                    </div>
                </td>
                <td width="250px"><s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                            value="%{dsDivisionId}"
                                            cssStyle="float:left;  width:240px;"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <div class="form-submit">
                        <s:submit value="%{getText('submit.label')}" cssStyle="float:right;"></s:submit>
                        <s:submit action="passChangePageLoad" value="%{getText('change_pass.label')}"
                                  cssStyle="float:right;"/>
                    </div>
                </td>
            </tr>
        </table>
    </s:form>
</div>
<%-- Styling Completed --%>