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
                        var bd = data.bdDivisionList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#birthDivisionId").html(options2);
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
                        $("select#birthDivisionId").html(options);
                    });
        });
    });

</script>

<div class="msg_list" style="position:relative;">
    <p class="msg_head">add a assignment now </p>

    <div class="msg_body">
        <fieldset>
            <legend><s:label value="Select BDDivision/MarriageDivision"/></legend>
            <table cellspacing="0" cellpadding="0">
                <caption></caption>
                <col width="500px">
                <col width="500px">
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
                </tr>
                </tbody>
            </table>
        </fieldset>
        <fieldset>
            <legend><s:label value="Type"/></legend>
            <table cellspacing="0" cellpadding="0">
                <caption></caption>
                <col width="500px">
                <col width="500px">
                <tbody>
                <tr>
                    <td colspan="1" align="left"><s:select
                            list="#@java.util.HashMap@{'0':getText('label.type.birth'),'1':getText('label.type.death'),'2':getText('label.type.marrage')}"
                            name="assignmentType" cssStyle="width:240px; margin-left:5px;"/></td>
                </tr>
                </tbody>
            </table>
        </fieldset>
        <fieldset>
            <legend><s:label value="Date"/></legend>
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
                        <s:textfield name="" id="dateOfAppoinmentDatePicker"/>
                    </fieldset>
                    <fieldset>
                        <legend><s:label value="permenent date"/></legend>
                        <s:textfield name="" id="dateOfPermenentDatePicker"/>
                    </fieldset>
                    <fieldset>
                        <legend><s:label value="termination date"/></legend>
                        <s:textfield name="" id="dateOfTerminationDatePicker"/>
                    </fieldset>
                </tr>

                </tbody>
            </table>
        </fieldset>
        <s:submit name="assignMentSubmit" value="add assignment"/>
    </div>
</div>