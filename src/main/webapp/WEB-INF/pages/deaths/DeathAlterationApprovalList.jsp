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

</script>
<s:url action="eprApproveDeathAlterations.do" id="submit">
    <s:param name="pageNumber" value="1"/>
</s:url>
<s:form method="post" action="%{submit}">
    <fieldset>
        <legend align="right"><s:label value="%{getText('lable.search.by.serial.number')}"/></legend>
        <table>
            <caption/>
            <col width="350px">
            <col width="100px">
            <col>

            <tr>
                <td><s:label value="%{getText('lable.serial.number')}"/></td>
                <td></td>
                <td><s:textfield name="serialNumber" value="" id="serial_id" maxLength="10"/></td>
            </tr>
        </table>
    </fieldset>

    <fieldset>
        <legend align="right"><s:label value="%{getText('lable.search.by.certificate.id')}"/></legend>
        <table>
            <caption/>
            <col width="350px">
            <col width="100px">
            <col>

            <tr>
                <td><s:label value="%{getText('lable.certifiate.number')}"/></td>
                <td></td>
                <td><s:textfield name="certificateNumber" value="" id="certificate_number_id" maxLength="10"/></td>
            </tr>
        </table>
    </fieldset>
    <div id="search_button" class="button" align="right">
                <s:submit name="refresh" value="%{getText('label.button.filter')}"/>
    </div>
</s:form>

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
            <tr>
                <td>aa</td>
                <td>bb</td>
                <td>vv</td>
                <td>rr</td>
                <td>ww</td>
                <td>ww</td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</s:if>