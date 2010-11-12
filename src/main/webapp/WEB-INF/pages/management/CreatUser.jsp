<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<s:set name="usr" value="user"/>

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

        $('select#districtIdCurrent').bind('change', function(evt1) {
            var options1 = '';
            var oSelect = document.getElementById('districtIdCurrent');
            for (var iCount = 0; oSelect.options[iCount]; iCount++) {
                if (oSelect.options[iCount].selected == true) {
                    var id = oSelect.options[iCount].value;
                    $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:3},
                            function(data) {
                                var ds = data.dsDivisionList;
                                for (var i = 0; i < ds.length; i++) {
                                    options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option> \n';
                                }
                                $("select#divisionIdCurrent").html(options1);
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
            errormsg = errormsg + document.getElementById("error1").value + "\n";
        }
        domObject = document.getElementById("userName");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + document.getElementById("error2").value + "\n";
        }
        domObject = document.getElementById("userPin");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + document.getElementById("error3").value + "\n";
        }
        else {
            var reg = /^([0-9]*)$/;
            if (reg.test(domObject.value.trim()) == false) {
                errormsg = errormsg + document.getElementById("error4").value + "\n";
            }
        }
        domObject = document.getElementById("districtId");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + document.getElementById("error5").value + "\n";
        }
        domObject = document.getElementById("divisionId");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + document.getElementById("error6").value + "\n";
        }

        if (errormsg != "") {
            alert(errormsg);
            return false;
        }
        return true;
    }

    function initPage() {
    }
</script>
<div id="user-create-outer">
    <fieldset style="border:3px solid #c3dcee;margin-left:12em;margin-right:20.5em;margin-top:2.5em;">
        <table class="user-create-table" cellspacing="0">
            <s:form name="userCreationForm" action="eprUserCreation" method="POST"
                    onsubmit="javascript:return validate()">
            <s:if test="userId == null">
            <tr>
                <td style="width:20em;">
                    <s:label value="%{getText('user_id.label')}"/></td>
                <td>
                    <s:textfield name="user.userId" id="checkUserId" cssStyle="text-transform:none;"/></td>
                </s:if>
            </tr>
            <tr>
                <td style="width:25em;">
                    <s:label value="%{getText('user_name.label')}"/></td>
                <td>
                    <s:textfield name="user.userName" id="userName" cssStyle="text-transform:none;"/></td>
            <tr>
                <td>
                    <s:label value="%{getText('user_pin.label')}"/></td>
                <td>
                    <s:textfield name="user.pin" id="userPin" cssStyle="text-transform:none;"/></td>

            <tr>
            <tr>
                <td>
                    <s:label value="Signature in Sinhala And English"/></td>
                <td>
                    <s:textarea name="user.sienSignatureText" id="userName" cssStyle="text-transform:none;"/></td>
            <tr>
                <td>
                    <s:label value="Signature in Tamil And English"/></td>
                <td>
                    <s:textarea name="user.taenSignatureText" id="userPin" cssStyle="text-transform:none;"/></td>

            <tr>
                <td>
                    <s:label value="%{getText('preffered_language.label')}"/></td>
                <td>
                        <s:select list="#@java.util.HashMap@{'en':'English','si':'සිංහල','ta':'Tamil'}"
                                  name="user.prefLanguage" id="prefferedLanguage"/>
            </tr>
            <div id="abc">
                <tr>
                    <td>
                        <s:if test="user != null">
                            <s:label value="%{getText('assigned_districts.label')}"/>
                        </s:if>
                    </td>
                    <td>
                        <s:if test="user != null">
                            <s:select list="districtList" name="assignedDistricts" multiple="true"
                                      size="10"
                                      id="districtIdCurrent" value="currentDistrictList"/>
                        </s:if>
                    </td>
                <tr>
            </div>
            <s:label>
                <td>
                    <s:if test="user != null">
                        <s:label value="%{getText('assigned_ds_divisions.label')}"/>
                    </s:if>
                </td>
                <s:if test="user != null">
                    <td><s:select list=" currentbdDivisionList" name="assignedDivisions" multiple="true"
                                  size="10"
                                  id="divisionIdCurrent" value="currentbdDivisionList"/>
                </s:if>
                </td>
            </s:label>
            </tr>
                <%--todo--%>
            <div id="edit">
                <tr>
                    <td>
                        <s:if test="user == null">
                            <s:label value="%{getText('assigned_districts.label')}"/>
                        </s:if>
                    </td>
                    <td>
                        <s:if test="user == null">
                            <s:select list="districtList" name="assignedDistricts" multiple="true" size="10"
                                      id="districtId"/>
                        </s:if>
                    </td>
                <tr>
                    <s:label>
                        <td>
                            <s:if test="user == null">
                                <s:label value="%{getText('assigned_ds_divisions.label')}"/>
                            </s:if>
                        </td>
                        <s:if test="user == null">
                            <td><s:select list=" dsDivisionList" name="assignedDivisions" multiple="true" size="10"
                                          id="divisionId"/>
                        </s:if>
                        </td>
                    </s:label>
                </tr>
            </div>
                <%--todo end--%>


            <tr>
                <td style="border-bottom:none;">
                    <s:label>
                        <s:label value="%{getText('user_role.label')}"/>
                <td style="border-bottom:none;"><s:select list="roleList" name="roleId" id="role"/>
                    </s:label>
                </td>

            </tr>
            <tr>

                <td colspan="2">
                    <s:if test="userId != null">
                        <s:hidden name="userId" value="%{userId}"/>
                        <table style="width:65%;float:right;">
                            <tr>
                                <td style="width:75%">
                                    <div>
                                        <s:label value="Change User Password"/>
                                        <s:checkbox name="changePassword"/>
                                    </div>
                                </td>
                                <td>
                                    <div class="form-submit">
                                        <s:submit value="%{getText('edit_user.label')}"/>
                                    </div>
                                </td>
                            </tr>
                        </table>

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
    <s:hidden id="error1" value="%{getText('user.id.label')}"/>
    <s:hidden id="error2" value="%{getText('user.name.label')}"/>
    <s:hidden id="error3" value="%{getText('user.PIN.label')}"/>
    <s:hidden id="error4" value="%{getText('user.PIN.invalid')}"/>
    <s:hidden id="error5" value="%{getText('user.district')}"/>
    <s:hidden id="error6" value="%{getText('user.divisions')}"/>

    </s:form>
</div>

