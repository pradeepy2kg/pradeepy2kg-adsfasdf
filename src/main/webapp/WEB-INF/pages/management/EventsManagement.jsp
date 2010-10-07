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

<script type="text/javascript" src="../lib/timepicker/jquery.utils.js"></script>
<script type="text/javascript" src="../lib/timepicker/jquery.strings.js"></script>
<script type="text/javascript" src="../lib/timepicker/ui.timepickr.js"></script>
<link rel="stylesheet" href="../css/ui.timepickr.css" type="text/css"/>


<script>
    $(document).ready(function() {

        $('#event-management-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "bSort": false,
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"   ,
            "sDom": 'T,C,H<"clear">lftipr'

        });
    });

    $(function() {
        $('#startTime').timepickr()
    });

    $(function() {
        $('#endTime').timepickr()
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
    $(function() {
        $("#endDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });


</script>

<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
    <legend><b><s:label value="Filter Events"/></b></legend>
    <s:form action="eprFilterEventsList.do" method="POST">
        <table width="100%" cellpadding="5" cellspacing="0">
            <tr>
                <td>
                    <s:label value="Start Date"/>
                </td>
                <td>
                    <s:textfield id="startDatePicker" maxLength="10" name="searchStartDate"/>
                </td>
                <td>
                    <s:label value="Time"/>
                </td>
                <td>
                    <s:textfield type="text" id="startTime" name="startTime" maxLength="5"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label value="End Date"/>
                </td>
                <td>
                    <s:textfield id="endDatePicker" maxLength="10" name="searchEndDate"/>
                </td>
                <td>
                    <s:label value="Time"/>
                </td>
                <td>
                    <s:textfield type="text" id="endTime" name="endTime" maxLength="5"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Choose Event type</s:label>

                </td>

                <td>
                     <s:select list="#@java.util.HashMap@{'AUDIT':getText('audit.label'),'ERROR':getText('error.label')}" name="eventType"
                  cssStyle="width:190px; margin-left:5px;"/>
                </td>
            </tr>
            <tbody>
            </tbody>
        </table>

        <div class="form-submit" style="margin-top:15px;">
            <s:submit value="%{getText('filter.label')}"/>
        </div>
    </s:form>
</fieldset>


<s:if test="printList != null">
    <fieldset style="border:none" width="100%">

        <div id="event-management">
            <table id="event-management-table" width="1024px" cellpadding="0" cellspacing="0" class="display">
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
                    <th><s:label value="Stack Trace"/></th>

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
                                    <s:a href="%{debugPageUrl}" title="%{getText('debug.label')}">
                                        <img src="<s:url value='/images/debug.jpg'/>" border="none" width="25"
                                             height="25"/>
                                    </s:a>
                                </s:if>
                            </td>
                            <td align="center">
                                <s:if test="stackTrace!=null">
                                    <s:url id="stackTracePageUrl" action="eprStackTraceDisplay.do">
                                        <s:param name="idUKey" value="idUKey"/>
                                    </s:url>
                                    <s:a href="%{stackTracePageUrl}" title="%{getText('stacTrace.label')}">
                                        <img src="<s:url value='/images/debug.jpg'/>" border="none" width="25"
                                             height="25"/>
                                    </s:a>
                                </s:if>
                            </td>
                        </tr>
                    </s:iterator>

                    </tbody>
                </s:if>
            </table>
        </div>
        <div class="next-previous">
            <s:url id="previousUrl" action="eprEventPrevious.do" encode="true">
                <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                <s:param name="pageNumber" value="%{#request.pageNumber}"/>
                <s:param name="recordCounter" value="#request.recordCounter"/>
                <s:param name="startDate" value="#request.startDate"/>
                <s:param name="endDate" value="#request.endDate"/>
                <s:param name="startTime" value="#request.startTime"/>
                <s:param name="endTime" value="#request.endTime"/>
                <s:param name="eventType" value="#request.eventType"/>
            </s:url>

            <s:url id="nextUrl" action="eprEventNext.do" encode="true">
                <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                <s:param name="pageNumber" value="%{#request.pageNumber}"/>
                <s:param name="recordCounter" value="#request.recordCounter"/>
                <s:param name="startDate" value="#request.startDate"/>
                <s:param name="endDate" value="#request.endDate"/>
                <s:param name="startTime" value="#request.startTime"/>
                <s:param name="endTime" value="#request.endTime"/>
                <s:param name="eventType" value="#request.eventType"/>
            </s:url>

            <s:if test="#request.previousFlag"><s:a href="%{previousUrl}">
                <img src="<s:url value='/images/previous.gif'/>"
                     border="none"/></s:a><s:label value="%{getText('previous.label')}"
                                                   cssStyle="margin-right:5px;"/></s:if>

            <s:if test="#request.nextFlag"><s:label value="%{getText('next.label')}"
                                                    cssStyle="margin-left:5px;"/><s:a href="%{nextUrl}">
                <img src="<s:url value='/images/next.gif'/>" border="none"/></s:a></s:if>
        </div>
    </fieldset>
</s:if>




