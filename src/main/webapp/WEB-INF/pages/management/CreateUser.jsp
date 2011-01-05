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

        if (document.getElementById("checkUserId") != null) {
            domObject = document.getElementById("checkUserId");
            if (isFieldEmpty(domObject)) {
                errormsg = errormsg + document.getElementById("error1").value + "\n";
            }
        }
        if (document.getElementById("userName") != null) {
            domObject = document.getElementById("userName");
            if (isFieldEmpty(domObject)) {
                errormsg = errormsg + document.getElementById("error2").value + "\n";
            }
        }
        if (document.getElementById("userPin") != null) {
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
        }
        if (document.getElementById("districtId") != null) {
            domObject = document.getElementById("districtId");
            if (isFieldEmpty(domObject)) {
                errormsg = errormsg + document.getElementById("error5").value + "\n";
            }
        }
        if (document.getElementById("divisionId") != null) {
            domObject = document.getElementById("divisionId");
            if (isFieldEmpty(domObject)) {
                errormsg = errormsg + document.getElementById("error6").value + "\n";
            }
        }
        if (document.getElementById("districtIdCurrent") != null) {
            domObject = document.getElementById("districtIdCurrent");
            if (isFieldEmpty(domObject)) {
                errormsg = errormsg + document.getElementById("error5").value + "\n";
            }
        }
        if (document.getElementById("divisionIdCurrent") != null) {
            domObject = document.getElementById("divisionIdCurrent");
            if (isFieldEmpty(domObject)) {
                errormsg = errormsg + document.getElementById("error6").value + "\n";
            }
        }

        for (var i = 0; i < document.getElementById("role").options.length; i++) {
            if (document.getElementById("role").options[i].selected) {
                if (i == 0 || i == 5) {
                    if (isMultiSelected(document.getElementById("districtId")) > 1 ||
                            isMultiSelected(document.getElementById("districtIdCurrent")) > 1 ||
                            isMultiSelected(document.getElementById("divisionId")) > 1 ||
                            isMultiSelected(document.getElementById("divisionIdCurrent")) > 1) {
                        errormsg = "Multiselect not allowed for DEO/ADR";
                    }
                }
            }
        }
        if (errormsg != "") {
            alert(errormsg);
            return false;
        }
        return true;
    }

    function isMultiSelected(element) {
        var count = 0;
        if (element != null) {
            for (var i = 0; i < element.options.length; i++) {
                if (element.options[i].selected) {
                    count++;
                }
            }
        }
        return count;
    }
    function initPage() {
    }
</script>
<div id="user-create-outer">
    <fieldset
            style="border:2px solid #c3dcee;margin-left:12em;margin-right:20.5em;margin-top:2.5em;margin-bottom:15px;">
        <table class="user-create-table" cellspacing="0" cellpadding="0">
            <s:form name="userCreationForm" action="eprUserCreation" method="POST"
                    onsubmit="javascript:return validate()">
            <s:if test="userId == null">
                <tr>
                    <td width="50%">
                        <s:label value="%{getText('user_id.label')}"/>
                        <s:label value="*" cssStyle="color:red;font-size:12pt;"/>
                    </td>
                    <td width="50%">
                        <s:textfield name="user.userId" id="checkUserId"
                                     cssStyle="text-transform:none;width:90%;margin-left:0;"/>
                    </td>
                </tr>
            </s:if>
            <s:else>
                <s:hidden name="user.userId" id="checkUserId"/>
            </s:else>
            <tr>
                <td>
                    <s:label value="%{getText('user_name.label')}"/>
                    <s:label value="*" cssStyle="color:red;font-size:12pt;"/>
                </td>
                <td>
                    <s:textfield name="user.userName" id="userName"
                                 cssStyle="text-transform:none;width:90%;margin-left:0;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label value="%{getText('user_pin.label')}"/>
                    <s:label value="*" cssStyle="color:red;font-size:12pt;"/>
                </td>
                <td>
                    <s:textfield name="user.pin" id="userPin" cssStyle="text-transform:none;width:90%;margin-left:0;"
                                 maxLength="10"/>
                </td>

            </tr>
            <tr>
                <td><s:label value="Signature in Sinhala And English"/></td>
                <td>
                    <s:textarea name="user.sienSignatureText" id="userName"
                                cssStyle="text-transform:none;width:90%;margin-left:0;"/>
                </td>
            </tr>
            <tr>
                <td><s:label value="Signature in Tamil And English"/></td>
                <td>
                    <s:textarea name="user.taenSignatureText" id="userPin"
                                cssStyle="text-transform:none;width:90%;margin-left:0;"/>
                </td>
            </tr>
            <tr>
                <td><s:label value="%{getText('preffered_language.label')}"/></td>
                <td>
                    <s:select list="#@java.util.HashMap@{'en':'English','si':'සිංහල','ta':'Tamil'}"
                              name="user.prefLanguage" id="prefferedLanguage" cssStyle="width:90%;margin-left:0;"/>
                </td>
            </tr>

            <div id="abc">
                <tr>
                    <td width="50%">
                        <s:if test="user != null">
                            <s:label value="%{getText('assigned_districts.label')}"/>
                            <s:label value="*" cssStyle="color:red;font-size:12pt;"/>
                        </s:if>
                    </td>
                    <td width="50%">
                        <s:if test="user != null">
                            <s:select list="districtList" name="assignedDistricts" multiple="true"
                                      cssStyle="width:90%;margin-left:0;padding-left:0;"
                                      size="10"
                                      id="districtIdCurrent" value="currentDistrictList"/>
                        </s:if>
                    </td>
                </tr>
            </div>
            <tr>
                <s:label>
                    <td>
                        <s:if test="user != null">
                            <s:label value="%{getText('assigned_ds_divisions.label')}"/>
                            <s:label value="*" cssStyle="color:red;font-size:12pt;"/>
                        </s:if>
                    </td>
                    <td>
                        <s:if test="user != null">
                            <s:select list="currentbdDivisionList" name="assignedDivisions" multiple="true"
                                      size="10"
                                      id="divisionIdCurrent" value="currentbdDivisionList"
                                      cssStyle="margin-left:0;width:90%"/>
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
                            <s:label value="*" cssStyle="color:red;font-size:12pt;"/>
                        </s:if>
                    </td>
                    <td>
                        <s:if test="user == null">
                            <s:select list="districtList" name="assignedDistricts" multiple="true" size="10"
                                      id="districtId" cssStyle="width:90%;margin-left:0;"/>
                        </s:if>
                    </td>
                </tr>
                <tr>
                    <s:label>
                        <td>
                            <s:if test="user == null">
                                <s:label value="%{getText('assigned_ds_divisions.label')}"/>
                                <s:label value="*" cssStyle="color:red;font-size:12pt;"/>
                            </s:if>
                        </td>
                        <td>
                            <s:if test="user == null">
                                <s:select list=" dsDivisionList" name="assignedDivisions" multiple="true"
                                          size="10"
                                          id="divisionId" cssStyle="width:90%;margin-left:0;"/>
                            </s:if>
                        </td>
                    </s:label>
                </tr>
            </div>
                <%--todo end--%>
            <tr>
                <td>
                    <s:label>
                    <s:label value="%{getText('user_role.label')}"/></td>
                <td><s:select list="roleList" name="roleId" id="role" cssStyle="width:90%;margin-left:0;"/>
                    </s:label>
                </td>

            </tr>
            <tr>

                <td colspan="2">
                    <s:if test="userId != null">
                        <s:hidden name="userId" value="%{userId}"/>
                        <table style="width:100%;float:right;">
                            <tr>
                                <td style="width:75%">
                                    <div>
                                        <s:label value="Change User Password"/>
                                        <s:checkbox name="changePassword"/>
                                    </div>
                                </td>
                                <td>
                                    <div class="form-submit">
                                        <s:submit value="%{getText('edit_user.label')}"
                                                  cssStyle="margin-top:10px;margin-right:20px;"/>
                                    </div>
                                </td>
                            </tr>
                        </table>

                    </s:if>
                    <s:if test="userId == null">
                        <div class="form-submit">
                            <s:submit value="%{getText('create_user.label')}"
                                      cssStyle="margin-top:10px;margin-right:20px;"/>
                        </div>
                    </s:if>
                </td>
            </tr>
        </table>

        <s:hidden id="buttonName" value="%{getText('assigned_ds_divisions.label')}"/>
        <s:hidden id="error1" value="%{getText('user.id.label')}"/>
        <s:hidden id="error2" value="%{getText('user.name.label')}"/>
        <s:hidden id="error3" value="%{getText('user.PIN.label')}"/>
        <s:hidden id="error4" value="%{getText('user.PIN.invalid')}"/>
        <s:hidden id="error5" value="%{getText('user.district')}"/>
        <s:hidden id="error6" value="%{getText('user.divisions')}"/>

        </s:form>
        <s:form action="eprViewUsers.do">
            <div class="form-submit" style="margin-right:50px;">
                <s:submit value="BACK" cssStyle="margin-top:10px;" name="button"/>
            </div>
        </s:form>
    </fieldset>
</div>

