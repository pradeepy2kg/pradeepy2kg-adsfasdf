<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

<script type="text/javascript" language="javascript" src="../lib/jquery/jquery.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script>
    $(document).ready(function() {
        $('#users-list-table').dataTable({
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

<div id="manage_registrars"/>
<fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
    <legend><s:property value="%{getText('filter.dsDivisions')}"/></legend>
</fieldset>
<fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
    <legend><s:property value="%{getText('filter.active.inactive')}"/></legend>
</fieldset>

</div>
