<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>


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

    // mode 1 = passing District, will return DS list
    // mode 2 = passing DsDivision, will return BD list
    // any other = passing district, will return DS list and the BD list for the first DS
    $(function() {
        $('select#districtId').bind('change', function(evt1) {
            var id = $("select#districtId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:5},
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
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#divisionId").html(options);
                    });
        });
    });

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

<s:actionmessage name="registrarFound" cssStyle="color:blue;font-size:10pt"/><s:property
        value="%{#session.exsisting_registrar.fullNameInOfficialLanguage}"/>

<s:set name="indirect" value="directAssigment"/>
<%--<s:if test="directAssigment>0">

    <form action="eprSearchRegistrarByPin.do" method="post">
        <table>
            <caption/>
            <col width="300px"/>
            <col width="200px"/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td>
                    <s:if test="%{(#session.exsisting_registrar == null  &&  directAssigment!=0)}">
                        <s:textfield id="registrarPin" name="registrarPin" value="%{registrarPin}"/>
                    </s:if>
                    <s:else>
                    </s:else>
                </td>
                <td>
                    <div id="search_button" class="button">
                        <s:if test="%{#session.exsisting_registrar == null  &&  directAssigment!=0}">
                            <s:submit name="refresh" value="%{getText('label.button.searchr')}"
                                      onclick="javascript:searchButtonClick();"/>
                        </s:if>
                        <s:else>

                        </s:else>
                    </div>
                </td>
                <td>
                    <s:fielderror name="noRegistrar" cssStyle="color:red;font-size:10pt" id="noregistrarError"/>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
    --%><%--    </fieldset>--%><%--

</s:if>--%>

<s:form action="eprAssignmentAdd.do" method="post" onsubmit="javascript:return validateForm()">

    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <table>
            <tbody>
            <tr>
                <td width="200px">
                    <s:property value="%{getText('label.district')}"/>
                </td>
                <td>
                    <s:select id="districtId" name="districtId" list="districtList" value="%{districtId}"
                              cssStyle="width:240px;"/>
                </td>
                <td width="200px">
                    <s:property value="%{getText('label.dsDivision')}"/>
                </td>
                <td>
                    <s:select id="dsDivisionId" name="dsDivisionId"
                              list="dsDivisionList"
                              value="%{dsDivisionId}"
                              cssStyle="float:left;  width:240px;"/></td>
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
                <td colspan="1" align=left><s:select
                        list="#@java.util.HashMap@{'0':getText('label.type.birth'),'1':getText('label.type.death'),'2':getText('label.type.marriage.general'),'3':getText('label.type.marriage.kandyan'),'4':getText('label.type.marriage.muslim')}"
                        name="assignmentType" cssStyle="width:240px; margin-left:5px;" id="type"/></td>
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
        <s:submit name="assignMentSubmit" value="%{getText('add.assignment')}" align="right"/>
    </s:if>
    <s:elseif test="editableAssignment">
        <s:submit name="assignMentEdit" value="%{getText('update.assignment')}"/>
        <s:hidden name="editMode" value="true"/>
    </s:elseif>
    <s:hidden name="directAssigment" value="2"/>
    <s:hidden name="registrarSession" value="true"/>
</s:form>
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


