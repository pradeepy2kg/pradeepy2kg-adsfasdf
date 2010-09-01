<%--
  Created by IntelliJ IDEA.
  User: amith23
  Date: Jun 9, 2010
  Time: 10:18:37 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>

<script type="text/javascript">
    $(function() {
        $('select#districtId').bind('change', function(evt1) {
            var options1 = '';
            var oSelect = document.getElementById('districtId');
            for (var iCount = 0; oSelect.options[iCount]; iCount++) {
                if (oSelect.options[iCount].selected == true) {
                    var id = oSelect.options[iCount].value;
                    $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:3},
                            function(data) {
                                var ds = data.dsDivisionList;
                                for (var i = 0; i < ds.length; i++) {
                                    options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option> \n';
                                }
                                $("select#divisionId").html(options1);
                            });
                }
            }

        });

    });
    function validate() {
        var errormsg = "";
        var domObject;
        domObject = document.getElementById("checkUserId");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Plese Enter The User Id  \n";
        }
        domObject = document.getElementById("userName");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Plese Enter The User Name \n";
        }
        domObject = document.getElementById("userPin");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Plese Enter The User Pin \n";
        }
        else {
            var reg = /^([0-9]*)$/;
            if (reg.test(domObject.value.trim()) == false) {
                errormsg = errormsg + "User Pin is Invalid \n";
            }
        }
        domObject = document.getElementById("districtId");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Plese Select Assigned Districts\n";
        }
        domObject = document.getElementById("divisionId");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Plese Select Assigned D.S. Divisions \n";
        }

        if (errormsg != "") {
            alert(errormsg);
            return false;
        }
        return true;
    }

</script>
<div id="user-create-outer">
    <fieldset style="border:3px solid #c3dcee;margin-left:12em;margin-right:20.5em;margin-top:2.5em;">
        <table class="user-create-table" cellspacing="0">
            <s:form name="userCreationForm" action="eprUserCreation" method="POST"
                    onsubmit="javascript:return validate()">
            <s:if test="userId == null">
            <tr>
                <td style="width:15em;">
                    <s:label value="%{getText('user_id.label')}"/></td>
                <td>
                    <s:textfield name="user.userId" id="checkUserId" cssStyle="text-transform:none;"/></td>
                </s:if>
            </tr>
            <tr>
                <td>
                    <s:label value="%{getText('user_name.label')}"/></td>
                <td>
                    <s:textfield name="user.userName" id="userName" cssStyle="text-transform:none;"/></td>
            <tr>
                <td>
                    <s:label value="%{getText('user_pin.label')}"/></td>
                <td>
                    <s:textfield name="user.pin" id="userPin" cssStyle="text-transform:none;"/></td>

            <tr>
                <td>
                    <s:label value="%{getText('preffered_language.label')}"/></td>
                <td>
                        <s:select list="#@java.util.HashMap@{'en':'English','si':'සිංහල','ta':'Tamil'}"
                                  name="user.prefLanguage" id="prefferedLanguage"/>
            </tr>
            <tr>
                <td><s:label value="%{getText('assigned_districts.label')}"/></td>
                <td><s:select list="districtList" name="assignedDistricts" multiple="true" size="10" id="districtId"/>
                </td>
            <tr>
                <s:label>
                    <td><s:label value="%{getText('assigned_ds_divisions.label')}"/></td>
                    <td><s:select list=" dsDivisionList" name="assignedDivisions" multiple="true" size="10"
                                  id="divisionId"/></td>
                </s:label>
            </tr>
            <tr>
                <td style="border-bottom:none;">
                    <s:label>
                        <s:label value="%{getText('user_role.label')}"/>
                <td style="border-bottom:none;"><s:select list="roleList" name="roleId" id="role"/>
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
    <s:hidden id="buttonName" value="%{getText('assigned_ds_divisions.label')}"/>
    </s:form>
</div>

