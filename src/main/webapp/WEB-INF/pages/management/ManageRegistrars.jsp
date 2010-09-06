<%@ page import="java.util.Map" %>
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
    $(document).ready(function() {
        $('#registrars-list-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "bSort": true,
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"
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

<div id="manage_registrars"/>
<s:form action="eprRegistrarsFilter.do" method="post">
    <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
        <legend><s:property value="%{getText('filter.dsDivisions')}"/></legend>
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
                <td colspan="1" align="left"><s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                                       value="%{dsDivisionId}"
                                                       cssStyle="float:left;  width:240px;"/></td>
            </tr>
            </tbody>
        </table>
    </fieldset>
    <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
        <legend><s:property value="%{getText('filter.active.inactive')}"/></legend>
        <table cellspacing="0" cellpadding="0">
            <caption></caption>
            <col width="500px">
            <col width="500px">
            <tbody>
            <tr>
                <td colspan="1" align="left"><s:select
                        list="#@java.util.HashMap@{'0':getText('label.state.active'),'1':getText('label.state.inactive')}"
                        name="assignmentState" cssStyle="width:240px; margin-left:5px;"/></td>
                <td colspan="1" align="left"><s:select
                        list="#@java.util.HashMap@{'0':getText('label.type.birth'),'1':getText('label.type.death'),'2':getText('label.type.marrage')}"
                        name="assignmentType" cssStyle="width:240px; margin-left:5px;"/></td>
            </tr>
            </tbody>
        </table>
    </fieldset>
    </div>
    <div id="search_button" class="button">
        <s:submit name="refresh" value="%{getText('label.button.filter')}"/>
    </div>
</s:form>

<div id="manage_registrars_body">
    <table id="registrars-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
        <thead>
        <tr class="table-title">
            <th width="50px"><s:label value="%{getText('label.bdDivision')}"/></th>
            <th width="20px"><s:label value="%{getText('label.state')}"/></th>
            <th width="20px"><s:label value="%{getText('label.type')}"/></th>
            <th width="100px"><s:label value="%{getText('label.startDate')}"/></th>
            <th width="100px"><s:label value="%{getText('label.endDate')}"/></th>
        </tr>
        </thead>
        <%--        <s:if test="registrarList.size>0">
            <s:iterator status="registrarStatus" value="registrarList" id="registrarList">
                <tbody>
                <tr>
                    <td><s:property value="fullNameInOfficialLanguage"/> </td>
                    <td><s:property value="fullNameInOfficialLanguage"/> </td>
                    <td><s:property value="fullNameInOfficialLanguage"/> </td>
                    <td><s:property value="fullNameInOfficialLanguage"/> </td>
                    <td><s:property value="fullNameInOfficialLanguage"/> </td>
                    <td><s:property value="fullNameInOfficialLanguage"/> </td>
                </tr>
                </tbody>
            </s:iterator>
        </s:if>--%>
        <tbody>
        <s:url action="eprRegistrarsView.do" id="registrar"/>
        <tr>
            <td><s:a href="%{registrar}"> <s:label value="bdDivision"/></s:a></td>
            <td><s:label value="state"/></td>
            <td><s:label value="type"/></td>
            <td><s:label value="start date"/></td>
            <td><s:label value="end date"/></td>
        </tr>
        </tbody>
    </table>
</div>

