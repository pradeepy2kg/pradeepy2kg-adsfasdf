<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script>
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
</script>

   <table width="100%" cellpadding="5" cellspacing="0">
        <col width="220px"/>
        <col/>
        <col width="220px"/>
        <col/>
        <tbody>
        <tr>
            <td><s:label name="district" value="%{getText('district.label')}"/></td>
            <td colspan="3">
               <%-- District List here--%>
            </td>
        </tr>
        <tr>
            <td><s:label name="division" value="%{getText('select_ds_division.label')}"/></td>
            <td colspan="3">
                <%-- Division List here--%>
                <%-- Division List here--%>
            </td>

        </tr>
        <tr>
            <td><s:label value="%{getText('serial.label')}"/></td>
            <td><s:textfield value="" name="bdfSerialNo" cssStyle="width:232px;"/></td>
            <td align="right"><s:label value="%{getText('date.from.label')}"
                                       cssStyle=" margin-right:5px;"/><sx:datetimepicker name="searchStartDate"
                                                                                         displayFormat="yyyy-MM-dd"/></td>
            <td align="right"><s:label value="%{getText('date.to.label')}"
                                       cssStyle=" margin-right:5px;"/><sx:datetimepicker name="searchEndDate"
                                                                                         displayFormat="yyyy-MM-dd"/></td>
        </tr>
        <tr>
            <td colspan="4" class="button" align="right">
                <s:hidden name="searchDateRangeFlag" value=""/>
                <s:submit name="refresh" value="%{getText('refresh.label')}"/>
            </td>
        </tr>
        </tbody>
    </table>
<div id="birth-register-approval-body">
    <%--todo permission handling--%>

            <fieldset style="margin-bottom:10px;margin-top:20px;border:none">
                <legend></legend>
                <table id="approval-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
                    <thead>
                    <tr>
                        
                        <th><s:label name="serial" value="%{getText('serial.label')}"/></th>
                        <th><s:label name="name" value="%{getText('name.label')}"/></th>
                        <th><s:label name="received" value="%{getText('received.label')}"/></th>
                        <th><s:label name="edit" value="%{getText('edit.label')}"/></th>
                        <th><s:label name="approve" value="%{getText('approve.label')}"/></th>
                        <th><s:label name="reject" value="%{getText('reject.label')}"/></th>
                        <th><s:label name="delete" value="%{getText('delete.label')}"/></th>
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
<%-- Styling Completed --%>