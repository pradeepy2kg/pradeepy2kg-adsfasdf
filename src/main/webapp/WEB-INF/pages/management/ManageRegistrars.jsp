<%@ page import="java.util.Map" %>
<%@ page import="lk.rgd.common.util.AssignmentUtill" %>
<%@ page import="lk.rgd.crs.api.domain.Assignment" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/media/css/TableTools.css";
    @import "../lib/datatables/media/css/ColVis.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/ZeroClipboard.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/TableTools.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/ColVis.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

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
            "sPaginationType": "full_numbers",
            "sDom":'T,C,H<"clear">lftipr'
            /**
             * plugin and short key
             * TableTools T
             * Colvis C (show hide colloum)
             */
            /*
             * Variable: sDom
             * Purpose:  Dictate the positioning that the created elements will take
             * Scope:    jQuery.dataTable.classSettings
             * Notes:
             *   The following options are allowed:
             *     'l' - Length changing
             *     'f' - Filtering input
             *     't' - The table!
             *     'i' - Information
             *     'p' - Pagination
             *     'r' - pRocessing
             *   The following constants are allowed:
             *     'H' - jQueryUI theme "header" classes
             *     'F' - jQueryUI theme "footer" classes
             *   The following syntax is expected:
             *     '<' and '>' - div elements
             *     '<"class" and '>' - div with a class
             *   Examples:
             *     '<"wrapper"flipt>', '<lf<t>ip>'
             */
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
                        //adding header value for DSDivision
                        //use option value as -1 because if nt set it could be 0(default value for int)
                        options1 += '<option value="' + -1 + '">' + document.getElementById('divisionHeaderValue').value + '</option>';
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

    function initPage() {
    }


</script>

<div id="manage_registrars"/>
<s:form action="eprRegistrarsFilter.do" method="post">
    <fieldset style="margin-bottom:0px;margin-top:2px;border:2px solid #c3dcee;">
        <table cellspacing="0" cellpadding="0">
            <caption></caption>
            <col width="20%">
            <col width="25%">
            <col width="10%">
            <col width="20%">
            <col width="25%">
            <tbody>
            <tr height="50px">
                <td><s:property value="%{getText('label.district')}"/></td>
                <td align="left">
                    <s:if test="%{#session.user_bean.role.roleId.equals('ADMIN')}">
                        <s:select id="districtId" name="districtId" list="districtList" value="%{districtId}"
                                  cssStyle="width:98.5%; width:240px;" headerKey="0"
                                  headerValue="%{getText('all.district.label')}"/>
                    </s:if>
                    <s:else>
                        <s:select id="districtId" name="districtId" list="districtList" value="%{districtId}"
                                  cssStyle="width:98.5%; width:240px;"/>
                    </s:else>
                </td>
                <td></td>
                <td><s:property value="%{getText('label.DSDivision')}"/></td>
                <td align="left">
                    <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                              value="%{dsDivisionId}" headerValue="%{getText('all.divisions.label')}" headerKey="-1"
                              cssStyle="float:left;  width:240px;"/>
                </td>
            </tr>
            <tr>
                <td><s:property value="%{getText('label.state')}"/></td>
                <td align="left"><s:select
                        list="#@java.util.HashMap@{'0':getText('label.state.active'),'1':getText('label.state.inactive')}"
                        name="assignmentState" cssStyle="width:240px;"/></td>
                <td></td>
                <td><s:property value="%{getText('label.type')}"/></td>
                <td align="left"><s:select
                        list="#@java.util.HashMap@{'0':getText('label.type.birth'),'1':getText('label.type.death'),'2':getText('label.type.marriage.general'),'3':getText('label.type.marriage.kandyan'),'4':getText('label.type.marriage.muslim')}"
                        name="assignmentType" cssStyle="width:240px;" headerKey="-1"
                        headerValue="%{getText('label.all.types')}"/></td>
            </tr>
            <tr>
                <td colspan="5" align="right">
                    <div id="search_button" class="button">
                        <s:submit name="refresh" value="%{getText('label.button.filter')}"/>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </fieldset>
</s:form>

<div id="manage_registrars_body">
    <fieldset style="margin-bottom:0px;margin-top:5px;border:2px solid #c3dcee;">
        <table id="registrars-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
            <thead>
            <tr class="table-title">
                <th width="200px"><s:label value="%{getText('label.bdDivision')}"/></th>
                <th width="200px"><s:label value="%{getText('label.name')}"/></th>
                <th width="20px"><s:label value="%{getText('label.active')}"/></th>
                <th width="75px"><s:label value="%{getText('label.type')}"/></th>
                <th width="100px"><s:label value="%{getText('label.startDate')}"/></th>
                <th width="100px"><s:label value="%{getText('label.endDate')}"/></th>
            </tr>
            </thead>
            <s:if test="assignmentList.size>0">
                <tbody>
                <s:iterator status="assignmentStatus" value="assignmentList" id="assignmentList">
                    <s:url action="eprRegistrarsView.do" id="assign">
                        <s:param name="registrarUkey" value="registrar.registrarUKey"/>
                    </s:url>
                    <tr>
                        <s:if test="birthDivision != null">
                            <td><s:property value="birthDivision.enDivisionName"/></td>
                        </s:if>
                        <s:if test="deathDivision != null">
                            <td><s:property value="deathDivision.enDivisionName"/></td>
                        </s:if>
                        <s:if test="marriageDivision != null">
                            <td><s:property value="marriageDivision.enDivisionName"/></td>
                        </s:if>

                        <td><s:a href="%{assign}"><s:property value="registrar.fullNameInEnglishLanguage"/></s:a></td>
                        <s:if test="lifeCycleInfo.active ==true">
                            <td><s:property value="%{getText('label.yes')}"/></td>
                        </s:if>
                        <s:else>
                            <td><s:property value="%{getText('label.no')}"/></td>
                        </s:else>
                        <td>
                            <%= AssignmentUtill.getAssignmentType((Integer) request.getAttribute("type.ordinal()"),
                                    ((Locale) session.getAttribute("WW_TRANS_I18N_LOCALE")).getLanguage())
                            %>
                        </td>
                        <td><s:property value="permanentDate"/></td>
                        <td><s:property value="terminationDate"/></td>
                    </tr>
                </s:iterator>
                </tbody>

            </s:if>
        </table>
    </fieldset>
</div>
<s:hidden id="divisionHeaderValue" value="%{getText('all.divisions.label')}"/>



