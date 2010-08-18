<%--
  Created by IntelliJ IDEA.
  User: amith23
  Date: Jun 9, 2010
  Time: 10:18:37 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
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
                    });
        });
    });
</script>
<div id="user-create-outer">
    <fieldset style="border:3px solid #c3dcee;margin-left:12em;margin-right:20.5em;margin-top:2.5em;">
        <table class="user-create-table" cellspacing="0">
            <s:form name="userCreationForm" action="eprUserCreation" method="POST">
            <s:if test="userId == null">
            <tr>
                <td style="width:15em;">
                    <s:label value="%{getText('user_id.label')}"/></td>
                <td>
                    <s:textfield name="user.userId"/></td>
                </s:if>
            </tr>
            <tr>
                <td>
                    <s:label value="%{getText('user_name.label')}"/></td>
                <td>
                    <s:textfield name="user.userName"/></td>
            <tr>
                <td>
                    <s:label value="%{getText('user_pin.label')}"/></td>
                <td>
                    <s:textfield name="user.pin"/></td>

            <tr>
                <td>
                    <s:label value="%{getText('preffered_language.label')}"/></td>
                <td>
                        <s:select list="#@java.util.HashMap@{'en':'English','si':'සිංහල','ta':'Tamil'}"
                                  name="user.prefLanguage"/>
            </tr>
            <tr>

                <td><s:label value="%{getText('assigned_districts.label')}"/></td>
                <td><s:select list="districtList" name="assignedDistricts" multiple="true" size="10" id="districtId"/>
                    <div class="form-submit">
                        <s:submit value="%{getText('get_ds_divisions.label')}" name="divisions" cssClass="font-8"/>
                    </div>
                </td>
                <s:if test="divisionList != null">
            <tr>
                <s:label>
                    <td><s:label value="%{getText('assigned_ds_divisions.label')}"/></td>
                    <td><s:select list="divisionList" name="assignedDivisions" multiple="true" size="10" id="dsDivisionId"/></td>
                </s:label>
            </tr>
            </s:if>
            <tr>
                <td style="border-bottom:none;">
                    <s:label>
                        <s:label value="%{getText('user_role.label')}"/>
                <td style="border-bottom:none;"><s:select list="roleList" name="roleId"/>
                    </s:label>
                </td>

            </tr>
            <tr>
                <td></td>
                <td>
                    <s:if test="userId != null">
                        <s:hidden name="userId" value="%{userId}"/>
                        <div class="form-submit">
                            <s:submit value="%{getText('edit_user.label')}"/>
                        </div>
                    </s:if>
                    <s:if test="userId == null">
                        <div class="form-submit">
                            <s:submit value="%{getText('create_user.label')}" cssStyle="margin-top:10px;"/>
                        </div>
                    </s:if>
                </td>
            </tr>
        </table>
    </fieldset>
    </s:form>
</div>

