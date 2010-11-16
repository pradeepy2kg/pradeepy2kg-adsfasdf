<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="../js/validate.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<script type="text/javascript">
    function initPage() {
    }

    $(function() {
        $("#person-approve-list").dataTable({
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
</script>

<div id="birth-register-approval">
    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <legend><b><s:label value="%{getText('searchOption.label')}"/></b></legend>
        <table width="100%" cellpadding="5" cellspacing="0">
            <col width="300px"/>
            <col/>
            <col width="100px"/>
            <col width="300px"/>
            <col/>
            <tbody>
            <tr>
                <td><s:label name="district" value="Submitted Location"/></td>
                <td>
                   <s:select name="locationId" list="locationList"/> 
                </td>
                <td></td>
                <td></td>
                <td class="button" align="right">
                    <s:hidden name="searchDateRangeFlag" value="%{#request.searchDateRangeFlag}"/>
                    <s:submit name="refresh" value="%{getText('bdfSearch.button')}"/>
                </td>
            </tr>
            </tbody>
        </table>
    </fieldset>

    <fieldset style="margin-bottom:10px;margin-top:20px;border:none">
        <legend></legend>
        <table id="person-approve-list" width="100%" cellpadding="0" cellspacing="0" class="display">
            <thead>
            <tr>
                <th>NIC</th>
                <th><s:label name="name" value="%{getText('label.personName')}"/></th>
                <th><s:label name="name" value="%{getText('label.state')}"/></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            </tbody>
        </table>
    </fieldset>
</div>