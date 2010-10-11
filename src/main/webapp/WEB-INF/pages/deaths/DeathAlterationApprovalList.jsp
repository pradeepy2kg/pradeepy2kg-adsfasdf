<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        $('#approval-list-table').dataTable({
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

    $(function() {
        $("#endDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $("#startDatePicker").datepicker({
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
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
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

</script>
<s:form method="post" action="eprApproveDeathAlterations.do">
    <fieldset>
        <legend align="right"><s:label value="%{getText('lable.search.by.dsDivision')}"/></legend>
        <table>
            <caption></caption>
            <col width="250px">
            <col width="250px">
            <col width="100px">
            <col width="250px">
            <col width="250px">
            <tbody>
            <tr>
                <td>
                    <s:label value="%{getText('label.district')}"/>
                </td>
                <td align="left">
                    <s:select id="districtId" name="districtUKey" list="districtList" value="%{districtUKey}"
                              cssStyle="width:98.5%; width:240px;"/>
                </td>
                <td></td>
                <td>
                    <s:label value="%{getText('label.dsDivision')}"/>
                </td>
                <td align="left">
                    <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{divisionUKey}"
                              cssStyle="float:left;  width:240px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label value="%{getText('label.bdDivision')}"/>
                </td>
                <td>
                    <s:select id="divisionId" name="divisionUKey" value="%{divisionUKey}"
                              list="bdDivisionList"
                              cssStyle="float:left;  width:240px;"/>
                </td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            </tbody>
        </table>
    </fieldset>

    <fieldset>
        <legend align="right"><s:label value="%{getText('lable.search.by.date')}"/></legend>
        <table>
            <caption></caption>
            <col width="250px">
            <col width="250px">
            <col width="100px">
            <col width="250px">
            <col width="250px">
            <tbody>
            <tr>
                <td><s:label value="%{getText('label.start.date')}"/></td>
                <td align="left">
                    <s:textfield name="startDate" id="startDatePicker" maxLength="10"/>
                </td>
                <td>

                </td>
                <td align="left"><s:label value="%{getText('label.end.date')}"/></td>
                <td>
                    <s:textfield name="endDate" id="endDatePicker" maxLength="10"/>
                </td>
            </tr>
            </tbody>
        </table>
    </fieldset>
    <div id="search_button" class="button" align="right">
        <s:submit name="refresh" value="%{getText('label.button.filter')}"/>
    </div>
    <s:hidden name="pageNumber" value="1"/>
</s:form>
<s:actionerror/>
<s:if test="approvalList.size()>0">
    <table id="approval-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
        <thead>
        <tr>
            <th width="20px"><s:label value="%{getText('alt.serial.number')}"/></th>
            <th width="650px"><s:label value="%{getText('name.label')}"/></th>
            <th width="100px"><s:label value="%{getText('edit.label')}"/></th>
            <th width="100px"><s:label value="%{getText('delete.label')}"/></th>
            <th width="100px"><s:label value="%{getText('reject.label')}"/></th>
            <th width="100px"><s:label value="%{getText('approve.label')}"/></th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="approvalList">
            <%--todo remove--%>
            <s:if test="status.ordinal()>-1">
                <s:url id="editSelected" action="eprDeathAlterationEdit"></s:url>
                <s:url id="deleteSelected" action="eprDeathAlterationDelate"></s:url>
                <s:url id="rejectSelected" action="eprDeathAlterationReject"></s:url>
                <s:url id="approveSelected" action="eprApproveDeathAlterationsDirect">
                    <s:param name="deathAlterationId" value="idUKey"/>
                </s:url>
                <tr>
                    <td><s:property value="alterationSerialNo"/></td>
                    <td><s:property value="status"/></td>
                    <td align="center">
                        <s:if test="status.ordinal()==0">
                            <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                                <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                     border="none"/></s:a>
                        </s:if>
                    </td>

                    <td align="center">
                        <s:if test="status.ordinal()==0">
                            <s:a href="%{deleteSelected}"
                                 title="%{getText('deleteToolTip.label')}"><img
                                    src="<s:url value='/images/delete.gif'/>" width="25" height="25"
                                    border="none"/></s:a>
                        </s:if>
                    </td>
                    <td align="center">
                        <s:if test="status.ordinal()==0">
                            <s:a href="%{rejectSelected}" title="%{getText('rejectTooltip.label')}">
                                <img src="<s:url value='/images/reject.gif'/>" width="25" height="25" border="none"/>
                            </s:a>
                        </s:if>
                    </td>
                    <td align="center">
                        <s:if test="status.ordinal()<2">
                            <s:a href="%{approveSelected}" title="%{getText('approveTooltip.label')}">
                                <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                     border="none"/></s:a>
                        </s:if>
                    </td>
                </tr>
            </s:if>
        </s:iterator>
        </tbody>
    </table>
</s:if>