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
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:3},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivisionId").html(options1);

                        var options2 = '';
                        var bd = data.divisionList;
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
                        var bd = data.divisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#divisionId").html(options);
                    });
        });
    });

    function disableAssignment(mode) {
        document.getElementById('districtId').disabled = mode;
        document.getElementById('dsDivisionId').disabled = mode;
        document.getElementById('divisionId').disabled = mode;
        document.getElementById('dateOfAppoinmentDatePicker').disabled = mode;
        document.getElementById('dateOfPermenentDatePicker').disabled = mode;
        document.getElementById('dateOfTerminationDatePicker').disabled = mode;
        document.getElementById('eprAssignmentAdd_assignMentSubmit').disabled = mode;
    }

    function searchButtonClick() {
        //  disableAssignment(false);
    }
</script>
<s:actionerror cssStyle="color:red;"/>
<s:set name="indirect" value="directAssigment"/>
<s:if test="directAssigment>0">
    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <legend align="right">
            <s:if test="%{#session.exsisting_registrar == null  &&  directAssigment!=0}">
                <s:property value="%{getText('registrar.find.by.pin')}"/>
            </s:if>
            <s:else>
                <s:property value="%{getText('acknowladgmnet.assignment')}"/>
            </s:else>
        </legend>
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
                    <td>
                        <s:actionmessage name="registrarFound" cssStyle="color:blue;font-size:10pt"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </fieldset>

</s:if>

<s:form action="eprAssignmentAdd.do" method="post">
    <fieldset>
        <legend><s:property value="%{getText('assignment.bd.marraige.division')}"/></legend>
        <table cellspacing="0" cellpadding="0">
            <caption></caption>
            <col width="500px">
            <col width="500px">
            <col>
            <tbody>
            <tr>
                <td colspan="1" align="left">
                    <s:select id="districtId" name="districtId" list="districtList" value="%{districtId}"
                              cssStyle="width:98.5%; width:240px;"/>
                </td>
                <td colspan="1" align="left"><s:select id="dsDivisionId" name="dsDivisionId"
                                                       list="dsDivisionList"
                                                       value="%{dsDivisionId}"
                                                       cssStyle="float:left;  width:240px;"/></td>
                <td>
                    <s:select id="divisionId" name="divisionId" value="%{divisionId}" list="divisionList"
                              cssStyle="float:left;  width:240px; margin:2px 5px;"/>
                </td>
            </tr>
            </tbody>
        </table>
    </fieldset>
    <fieldset>
        <legend><s:property value="%{getText('assignment.type')}"/></legend>
        <table cellspacing="0" cellpadding="0">
            <caption></caption>
            <col width="500px">
            <col width="500px">
            <tbody>
            <tr>
                <td colspan="1" align="left"><s:select
                        list="#@java.util.HashMap@{'0':getText('label.type.birth'),'1':getText('label.type.death'),'2':getText('label.type.marrage')}"
                        name="assignmentType" cssStyle="width:240px; margin-left:5px;" id="type"/></td>
            </tr>
            </tbody>
        </table>
    </fieldset>
    <s:if test="assignment != null">
        <fieldset>
            <legend><s:property value="%{getText('assignment.state')}"/></legend>
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

        </fieldset>
    </s:if>
    <fieldset>
        <legend><s:property value="%{getText('assignment.dates')}"/></legend>
        <table>
            <caption/>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <fieldset>
                    <legend><s:label value="appoinment date"/></legend>
                    <s:if test="assignment != null">
                        <s:textfield value="%{assignment.appointmentDate}" id="dateOfAppoinmentDatePicker"
                                     name="appoinmentDate"/>
                    </s:if>
                    <s:else>
                        <s:textfield name="assignment.appointmentDate" id="dateOfAppoinmentDatePicker"/> </s:else>
                </fieldset>
                <fieldset>
                    <legend><s:label value="permenent date"/></legend>
                    <s:if test="assignment != null">
                        <s:textfield value="%{assignment.permanentDate}" id="dateOfPermenentDatePicker"
                                     name="permanentDate"/>
                    </s:if>
                    <s:else>
                        <s:textfield name="assignment.permanentDate" id="dateOfPermenentDatePicker"/> </s:else>

                </fieldset>
                <fieldset>
                    <legend><s:label value="termination date"/></legend>
                    <s:if test="assignment != null">
                        <s:textfield value="%{assignment.terminationDate}" id="dateOfTerminationDatePicker"
                                     name="terminationDate"/>
                    </s:if>
                    <s:else>
                        <s:textfield name="assignment.terminationDate" id="dateOfTerminationDatePicker"/> </s:else>
                </fieldset>
            </tr>

            </tbody>
        </table>
    </fieldset>
    <s:if test="editableAssignment==false">
        <s:submit name="assignMentSubmit" value="add assignment prop"/>
    </s:if>
    <s:else>
        <s:submit name="assignMentEdit" value="edit assignment prop"/>
        <s:hidden name="editMode" value="true"/>
    </s:else>
    <s:hidden name="directAssigment" value="2"/>
    <s:hidden name="registrarSession" value="true"/>
</s:form>
<s:property value="directAssigment"/>
<s:if test="assignment != null">
    <script type="text/javascript">
        document.getElementById('dsDivisionId').disabled = true;
        document.getElementById('districtId').disabled = true;
        document.getElementById('divisionId').disabled = true;
        document.getElementById('type').disabled = true;
    </script>
</s:if>


