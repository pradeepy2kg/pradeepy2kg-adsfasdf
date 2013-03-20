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

<script type="text/javascript" src="../lib/timepicker/jquery.utils.js"></script>
<script type="text/javascript" src="../lib/timepicker/jquery.strings.js"></script>
<script type="text/javascript" src="../lib/timepicker/ui.timepickr.js"></script>
<link rel="stylesheet" href="../css/ui.timepickr.css" type="text/css"/>

<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>

<script>
    $(document).ready(function() {

        $('#event-management-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "aaSorting": [[0,'desc']],
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"   ,
            "sDom": 'T,C,H<"clear">lftipr'

        });
//        $('#event-management-table').sort({"idUKey":"desc"});
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

    var errormsg = "";
    function validate() {
        var domObject;
        var returnval = true;

        // validate start  and end time
        domObject = document.getElementById('startTime');
        if (isFieldEmpty(domObject))
            isEmpty(domObject, "", 'error3')

        domObject = document.getElementById('endTime');
        if (isFieldEmpty(domObject))
            isEmpty(domObject, "", 'error4')

        // validate start and end date
        domObject = document.getElementById('startDatePicker');
        if (isFieldEmpty(domObject))
            isEmpty(domObject, "", 'error2')
        else
            isDate(domObject.value, "error1", "error6");

        domObject = document.getElementById('endDatePicker');
        if (isFieldEmpty(domObject))
            isEmpty(domObject, "", 'error3')
        else
            isDate(domObject.value, "error1", "error7");

        var out = checkActiveFieldsForSyntaxErrors('events-viewer-form');
        if(out != ""){
            errormsg = errormsg + out;
        }

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

    function initPage(){}

</script>

<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
    <legend><b><s:label value="Filter Events"/></b></legend>
    <s:form id="events-viewer-form" action="eprFilterEventsList.do" method="POST" onsubmit="javascript:return validate()">
        <table width="100%" cellpadding="5" cellspacing="0">
            <col width="200px">
            <col width="200px">
            <col width="100px">
            <col>
            <tr>
                <td height="50px">
                    <s:label value="Start Date"/>
                </td>
                <td>
                    <s:textfield id="startDatePicker" maxLength="10" name="searchStartDate" cssStyle="width:180px"/>
                </td>
                <td>
                    <s:label value="Time"/>
                </td>
                <td>
                    <s:textfield type="text" id="startTime" name="startTime" maxLength="5"/>
                </td>
            </tr>
            <tr>
                <td height="50px">
                    <s:label value="End Date"/>
                </td>
                <td>
                    <s:textfield id="endDatePicker" maxLength="10" name="searchEndDate" cssStyle="width:180px"/>
                </td>
                <td>
                    <s:label value="Time"/>
                </td>
                <td>
                    <s:textfield type="text" id="endTime" name="endTime" maxLength="5"/>
                </td>
            </tr>
            <tr>
                <td height="50px">
                    <s:label>Choose Event type</s:label>

                </td>

                <td>
                    <s:select list="#@java.util.HashMap@{'AUDIT':getText('audit.label'),'ERROR':getText('error.label')}"
                              name="eventType"
                              cssStyle="width:187px;"/>
                </td>
            </tr>
            <tbody>
            </tbody>
        </table>

        <div class="form-submit" style="margin-top:15px;">
            <s:submit value="%{getText('filter.label')}"/>
        </div>
        <s:hidden id="error1" value="%{getText('invalide.inputType')}"/>
        <s:hidden id="error2" value="%{getText('startDate.error')}"/>
        <s:hidden id="error3" value="%{getText('endDate.error')}"/>
        <s:hidden id="error4" value="%{getText('startTime.error')}"/>
        <s:hidden id="error5" value="%{getText('endTime.error')}"/>
        <s:hidden id="error6" value="%{getText('invalid.startDate.error')}"/>
        <s:hidden id="error7" value="%{getText('invalid.endDate.error')}"/>
    </s:form>
</fieldset>


<s:if test="printList != null">
    <fieldset style="border:none" width="1030px">

        <div id="event-management">
            <table id="event-management-table" cellpadding="0" cellspacing="0" class="display" style="width:1030px;">
                <thead>
                <tr class="table-title">
                    <th width="30px"><s:label name="name" value=""/></th>
                    <th width="80px"><s:label value="User Id"/></th>
                    <th width="50px"><s:label value="Event Type"/></th>
                    <th width="50px"><s:label value="Event Code"/></th>
                    <th width="150px"><s:label value="Class Name"/></th>
                    <th><s:label value="Method Name"/></th>
                    <th width="100px"><s:label value="Record Id"/></th>
                    <th width="50px"><s:label value="Details"/></th>

                </tr>
                </thead>
                <s:if test="printList.size>0">
                    <tbody>
                    <s:iterator status="" value="printList" id="printList">
                        <tr>
                            <td align="center"><s:property value="idUKey"/></td>
                            <td><s:property value="user.getUserId()"/></td>
                            <td align="center"><s:property value="eventType"/></td>
                            <td align="center"><s:property value="eventCode"/></td>
                            <td><s:property value="className"/></td>
                            <td><s:property value="methodName"/></td>
                            <td align="center"><s:property value="recordId"/></td>
                            <td align="center">
                                <s:if test="debug!=null || stackTrace!=null">
                                    <s:url id="debugPageUrl" action="eprDetailsDisplay.do">
                                        <s:param name="idUKey" value="idUKey"/>
                                        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                                        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                                        <s:param name="pageNumber" value="%{#request.pageNumber}"/>
                                        <s:param name="recordCounter" value="#request.recordCounter"/>
                                        <s:param name="startDate" value="#request.startDate"/>
                                        <s:param name="endDate" value="#request.endDate"/>
                                        <s:param name="startTime" value="#request.startTime"/>
                                        <s:param name="endTime" value="#request.endTime"/>
                                        <s:param name="eventType" value="#request.eventType"/>
                                        <s:param name="goBackFlag" value="#request.goBackFlag"/>
                                        <s:param name="filterFlag" value="#request.filterFlag"/>
                                    </s:url>
                                    <s:a href="%{debugPageUrl}" title="%{getText('details.label')}">
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
                <s:param name="filterFlag" value="#request.filterFlag"/>
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
                <s:param name="filterFlag" value="#request.filterFlag"/>
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





