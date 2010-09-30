<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

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

<div id="event-management">
    <table id="event-management-table" width="100%" cellpadding="0" cellspacing="0" class="display">
        <thead>
        <tr class="table-title">
            <th width="200px"><s:label value="User Id"/></th>
            <th width="200px"><s:label value="Time Stamp"/></th>
            <th width="200px"><s:label value="Time Stamp"/></th>
            <th width="200px"><s:label value="Time Stamp"/></th>

        </tr>
        </thead>

        <tbody>

        
        </tbody>
    </table>
    <s:label name="event" value="%{timestamp}"/>
    <s:label name="user" value="%{user}"/>
    <s:label name="eventType" value="%{eventType}"/>
    <s:label name="debug" value="%{debug}"/>
</div>
