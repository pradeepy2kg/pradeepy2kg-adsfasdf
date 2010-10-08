<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/media/css/TableTools.css";
    /*@import "../lib/datatables/media/css/ColVis.css";*/
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<%--<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>--%>
<%--<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/ZeroClipboard.js"></script>--%>
<%--<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/TableTools.js"></script>--%>
<%--<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/ColVis.js"></script>--%>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>
<script type="text/javascript">
    function initPage() {
    }
    $(document).ready(function() {

        $('#pendingApprovalTable').dataTable({
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
<div id="birth-register-approval-body">
    <s:if test="!(pendingList.size()==0)">
        <table id="pendingApprovalTable" border="1">
            <thead>
            <th><s:label value="%{getText('th.index')}"/></th>
            <th><s:label value="%{getText('th.exsists')}"/></th>
            <th><s:label value="%{getText('th.alteration')}"/></th>
            <th><s:label value="%{getText('th.approve')}"/></th>
            </thead>
            <tbody>
            <s:iterator value="pendingList">
                <tr>
                    <td align="left"><s:property value="%{key}"/></td>
                    <td><s:property value="%{value.get(0)}"/></td>
                    <td><s:property value="%{value.get(1)}"/></td>
                    <td align="center">
                        <s:checkbox name="approvedIndex" fieldValue="%{key}"
                                    id="%{key}" value="%{#approvedIndex}"/></td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
        <%-- <table id="pendingApprovalTable" width="100%" cellpadding="0" cellspacing="0" >
            <thead>
            <tr>
                <th><s:label value="%{getText('th.index')}"/></th>
                <th><s:label value="%{getText('th.exsists')}"/></th>
                <th><s:label value="%{getText('th.alteration')}"/></th>
                <th><s:label value="%{getText('th.approve')}"/></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="pendingList">
                <tr>
                    <td align="left"><s:property value="%{key}"/></td>
                    <td><s:property value="%{value.get(0)}"/></td>
                    <td><s:property value="%{value.get(1)}"/></td>
                    <td align="center"><s:checkbox name="approvedIndex" value="%{#approvedIndex}" fieldValue="%{key}"
                                                   id="%{key}"/></td>
                </tr>
            </s:iterator>
            </tbody>
        </table>--%>
    </s:if>
    <s:else>
        <table align="center" style="width:50%">
            <tr>
                <td style="width:60%">
                    <s:label value="%{getText('no.changes.found')}"/>
                </td>
                <td>
                    <s:form action="eprBirthAlterationPendingApproval.do" method="post">
                        <div class="form-submit">
                            <s:submit value="%{getText('previous.label')}"
                                      cssStyle="margin-top:10px;margin-right:25px"/>
                        </div>
                    </s:form>
                </td>
            </tr>
        </table>
    </s:else>
    <div class="form-submit">
        <s:form action="eprDeathAlterationSetBits">
            <s:submit name="submit" value="%{getText('lable.update')}"/>
            <%--    <s:hidden name="deathAlterationId" value="deathAlteration.idUKey"/>--%>
            <s:hidden name="deathId" value="100"/>
        </s:form>
    </div>

</div>