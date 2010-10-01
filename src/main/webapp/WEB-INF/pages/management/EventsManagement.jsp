<%@ page import="java.util.Locale" %>
<%@ page import="lk.rgd.common.util.AssignmentUtill" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/media/css/TableTools.css";
    @import "../lib/datatables/media/css/ColVis.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/ZeroClipboard.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/TableTools.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/ColVis.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>

<script>
    $(document).ready(function() {

        $('#event-management-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "bSort": true,
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"   ,
            "sDom": 'T,C,H<"clear">lftipr'

        });
    });
</script>
<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">

</fieldset>

<fieldset style="border:none">

    <div id="event-management">
        <table id="event-management-table" width="100%" cellpadding="0" cellspacing="0" class="display">
            <thead>
            <tr class="table-title">
                <th><s:label name="name" value=""/></th>
                <th><s:label value="User Id"/></th>
                <th><s:label value="Time Stamp"/></th>
                <th><s:label value="Event Type"/></th>
                <th><s:label value="Event Code"/></th>
                <th><s:label value="Class Name"/></th>
                <th><s:label value="Method Name"/></th>
                <th><s:label value="Recode Id"/></th>
                <th><s:label value="Event Data"/></th>
                <th><s:label value="Debug"/></th>

            </tr>
            </thead>
            <s:if test="printList.size>0">
                <tbody>
                <s:iterator status="" value="printList" id="printList">
                    <tr>
                        <td><s:property value="idUKey"/></td>
                        <td><s:property value="user.getUserId()"/></td>
                        <td><s:property value="timestamp"/></td>
                        <td><s:property value="eventType"/></td>
                        <td><s:property value="eventCode"/></td>
                        <td><s:property value="className"/></td>
                        <td><s:property value="methodName"/></td>
                        <td><s:property value="recordId"/></td>
                        <td><s:property value="eventData"/></td>
                        <td align="center">
                            <s:if test="debug!=null">
                                 <s:url id="debugPageUrl" action="eprDebugDisplay.do">
                                    <s:param name="idUKey" value="idUKey"/>
                                </s:url>
                                <s:a href="%{debugPageUrl}" title="%{getText('print.label')}">
                                   <img src="<s:url value='/images/print_icon.gif'/>" border="none" width="25"
                                             height="25"/>
                                </s:a>
                            </s:if>
                            <%--<s:property value="debug"/>--%>
                        </td>
                    </tr>
                </s:iterator>

                </tbody>
            </s:if>
        </table>
    </div>
</fieldset>

    <s:label name="debug" value="%{debug}"/>


