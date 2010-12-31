<%@ page import="lk.rgd.common.util.AssignmentUtill" %>
<%@ page import="lk.rgd.common.api.domain.User" %>
<%@ page import="lk.rgd.crs.web.WebConstants" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/division.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>


<script>
    $(function() {
        $("#dateOfBirthDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $("#dateOfAppoinmentDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $("#dateOfPermenentDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $("#dateOfTerminationDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });


    //mode 2 = passing DsDivision, will return BD list
    //mode 5 = passing districtId, return DS List and the BD List for the 1st DS division
    //mode 6 = passing district list and return ds division list and mr division list for 1st ds division.
    //mode 7 = passing dsDivisionId, return the MR list

    /*
    $(function() {
        $('select#districtId').bind('change', function(evt1) {
            var id = $("select#districtId").attr("value");
            var type = document.getElementById("assignmentType").value;
            var mode;
            if (type == 1) {
                //this mean requesting bd division
                mode = 5;
            }
            if (type == 2) {
                //this mean requesting mr division
                mode = 6;
            }
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:mode},
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
                        $("select#divisionId").html(options2);
                    });
        });

        $('select#dsDivisionId').bind('change', function(evt2) {
            var id = $("select#dsDivisionId").attr("value");
            var type = document.getElementById("assignmentType").value;
            var mode;
            if (type == 1) {
                //this mean requesting bd division
                mode = 2;
            }
            if (type == 2) {
                //this mean requesting mr division
                mode = 7;
            }
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:mode},
                    function(data) {
                        var options = '';
                        var bd = data.mrDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#divisionId").html(options);
                    });
        });
    });

    */

    var appoinmentDate = document.getElementById('dateOfAppoinmentDatePicker');
    var permanentDate = document.getElementById('dateOfPermenentDatePicker');
    var terminationDate = document.getElementById('dateOfTerminationDatePicker');
    var errormsg = "";

    function validateForm() {
        var returnval = true;
        //validate date formats
        isDate(appoinmentDate, "invalideData", "appoinmentDate")
        isDate(permanentDate, "invalideData", "permanentDate")
        isDate(terminationDate, "invalideData", "terminationDate")

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

    function initPage() {

    }

</script>
<s:actionerror cssStyle="color:red;"/>

<s:set name="indirect" value="directAssignment"/>

<s:form action="eprAssignmentAdd.do" method="post" onsubmit="javascript:return validateForm()">

    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <table>
            <tbody>
            <s:if test="%{editableAssignment}">
                <tr>
                    <td width="200px">
                        <s:property value="%{getText('label.district')}"/>
                    </td>
                    <td width="240px">
                        <s:property value="%{districtName}"/>
                    </td>
                    <td width="200px">
                        <s:property value="%{getText('label.dsDivision')}"/>
                    </td>
                    <td>
                        <s:property value="%{dsDivisionName}"/>
                    </td>
                </tr>
                <tr>
                    <td width="200px">
                        <s:property value="%{getText('label.division')}"/>
                    </td>
                    <td><s:property value="%{divisionName}"/>
                    </td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td width="200px">
                        <s:property value="%{getText('label.district')}"/>
                    </td>
                    <td>
                        <s:if test="%{assignmentType == 1}">
                            <s:select id="districtId" name="districtId" list="districtList" value="%{districtId}"
                                      cssStyle="width:240px;"
                                      onchange="populateDSDivisions('districtId','dsDivisionId','divisionId', 'BirthWithAllDS', false)"/>

                        </s:if>
                        <s:else>
                            <s:select id="districtId" name="districtId" list="districtList" value="%{districtId}"
                                      cssStyle="width:240px;"
                                      onchange="populateDSDivisions('districtId','dsDivisionId','divisionId', 'MarriageWithAllDS', false)"/>

                        </s:else>
                    </td>
                    <td width="200px">
                        <s:property value="%{getText('label.dsDivision')}"/>
                    </td>
                    <td>
                        <s:if test="%{assignmentType == 1}">
                            <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                      value="%{dsDivisionId}" cssStyle="float:left;  width:240px;"
                                      onchange="populateDivisions('dsDivisionId', 'divisionId', 'Birth', false)"/>
                        </s:if>
                        <s:else>
                            <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                      value="%{dsDivisionId}" cssStyle="float:left;  width:240px;"
                                      onchange="populateDivisions('dsDivisionId', 'divisionId', 'Marriage', false)"/>
                        </s:else>

                    </td>
                </tr>
                <tr>
                    <td width="200px">
                        <s:property value="%{getText('label.division')}"/>
                    </td>
                    <td>
                        <s:select id="divisionId" name="divisionId" value="%{divisionId}" list="divisionList"
                                  cssStyle="float:left;  width:240px;"/>
                    </td>
                </tr>
            </s:else>
            </tbody>
        </table>
    </fieldset>
    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <table cellspacing="0" cellpadding="0">
            <caption></caption>
            <tbody>
            <tr>
                <td width="200px">
                    <s:property value="%{getText('label.type')}"/>
                </td>
                <s:if test="%{editableAssignment}">
                    <s:set name="type" value="%{assignmentType}" scope="page"/>
                    <td colspan="1" align=left>
                        <%= AssignmentUtill.getAssignmentType((Integer) request.getAttribute("type.ordinal()"),
                                ((Locale) session.getAttribute("WW_TRANS_I18N_LOCALE")).getLanguage())
                        %>
                    </td>
                </s:if>
                <s:else>
                    <s:if test="%{assignmentType == 1}">
                        <td colspan="1" align=left><s:select
                                list="#@java.util.HashMap@{'0':getText('label.type.birth'),'1':getText('label.type.death')}"
                                name="assignmentType" cssStyle="width:240px; margin-left:5px;" id="type"/></td>
                    </s:if>
                    <s:if test="%{assignmentType == 2}">
                        <td colspan="1" align=left>
                            <s:checkboxlist
                                    list="#@java.util.HashMap@{'2':getText('label.type.marriage.general'),'3':getText('label.type.marriage.kandyan'),'4':getText('label.type.marriage.muslim')}"
                                    name="mrgType" cssStyle="width:240px; margin-left:5px;" id="type"/>
                        </td>
                    </s:if>
                </s:else>
            </tr>
            </tbody>
        </table>
    </fieldset>

    <s:if test="assignment != null">
        <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
            <table cellspacing="0" cellpadding="0">
                <caption></caption>
                <tbody>
                <tr>
                    <td width="200px">
                        <s:property value="%{getText('label.state')}"/>
                    </td>
                    <td>
                        <s:if test="assignment != null">
                            <s:select
                                    list="#@java.util.HashMap@{'0':getText('label.state.active'),'1':getText('label.state.inactive')}"
                                    name="assignmentState" cssStyle="width:240px; margin-left:5px;"/>
                        </s:if>
                        <s:else>
                            <s:select
                                    list="#@java.util.HashMap@{'0':getText('label.state.active'),'1':getText('label.state.inactive')}"
                                    name="assignment.lifeCycleInfo.active" cssStyle="width:240px; margin-left:5px;"/>
                        </s:else>
                    </td>
                </tr>

                </tbody>
            </table>
        </fieldset>
    </s:if>


    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <table>
            <caption/>
            <col width="200px">
            <col>
            <tbody>
            <tr>
                <td><s:property value="%{getText('label.appoinment.date')}"/></td>
                <td>
                    <s:if test="assignment != null">
                        <s:textfield value="%{assignment.appointmentDate}" id="dateOfAppoinmentDatePicker"
                                     name="appoinmentDate"/>
                    </s:if>
                    <s:else>
                        <s:textfield name="assignment.appointmentDate" id="dateOfAppoinmentDatePicker"/> </s:else>
                </td>
            </tr>
            <tr>
                <td><s:property value="%{getText('label.permanent.date')}"/></td>
                <td>
                    <s:if test="assignment != null">
                        <s:textfield value="%{assignment.permanentDate}" id="dateOfPermenentDatePicker"
                                     name="permanentDate"/>
                    </s:if>
                    <s:else>
                        <s:textfield name="assignment.permanentDate" id="dateOfPermenentDatePicker"/> </s:else>
                </td>
            </tr>
            <tr>
                <td><s:property value="%{getText('label.termination.date')}"/></td>
                <td>
                    <s:if test="assignment != null">
                        <s:textfield value="%{assignment.terminationDate}" id="dateOfTerminationDatePicker"
                                     name="terminationDate"/>
                    </s:if>
                    <s:else>
                        <s:textfield name="assignment.terminationDate" id="dateOfTerminationDatePicker"/> </s:else>
                </td>
            </tr>
            </tbody>
        </table>
    </fieldset>
    <s:if test="%{#session.exsisting_registrar != null && !editableAssignment}">
        <div class="form-submit">
            <s:submit name="assignMentSubmit" value="%{getText('add.assignment')}" align="right"/>
        </div>
    </s:if>
    <s:elseif test="editableAssignment">
        <div class="form-submit">
            <s:submit name="assignMentEdit" value="%{getText('update.assignment')}"/>
        </div>
        <s:hidden name="editMode" value="true"/>
    </s:elseif>
    <s:hidden name="directAssignment" value="2"/>
    <s:hidden name="registrarSession" value="true"/>
</s:form>
<div class="form-submit">
    <s:form action="eprRegistrarsView.do">
        <s:submit value="%{getText('label.back')}"/>
        <s:hidden name="registrarUkey" value="%{assignment.registrar.registrarUKey}"/>
    </s:form>
</div>
<s:if test="assignment != null">
    <script type="text/javascript">
        document.getElementById('dsDivisionId').disabled = true;
        document.getElementById('districtId').disabled = true;
        document.getElementById('divisionId').disabled = true;
        document.getElementById('type').disabled = true;
    </script>
</s:if>

<s:hidden id="invalideData" value="%{getText('invalide.data')}"/>
<s:hidden id="cannotNull" value="%{getText('cannot.null')}"/>
<s:hidden id="appoinmentDate" value="%{getText('field.appoinment.date')}"/>
<s:hidden id="permanentDate" value="%{getText('field.permanent.data')}"/>
<s:hidden id="terminationDate" value="%{getText('field.termination.data')}"/>
<s:hidden id="assignmentType" value="%{assignmentType}"/>




