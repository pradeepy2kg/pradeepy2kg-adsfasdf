<%--
  @author Supun Nimesh Karunathilaka
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script>
    $(document).ready(function() {
        $('#approval-list-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "aaSorting": [
                [0,'desc']
            ],
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"
        });
    });

</script>
<script type="text/javascript" src="<s:url value="/js/selectAll.js"/>"></script>
<table cellpadding="5" cellspacing="0">
    <s:form action="eprAdoptionOrderFilterByStatus" method="post">

        <tbody>
        <tr>
            <td><s:label value="%{getText('select.status.label')}"/></td>
            <td>
                <s:select list="#@java.util.HashMap@{'1':getText('data.entry'),'2':getText('Approved.label'),'3':getText('notice.printed.label'),
        '4':getText('rejected.label'),'5':getText('certificate.issual.request.captured.label'),'6':getText('adoption.certificate.printed.label')}"
                          name="currentStatus" value="%{#request.currentStatus}" headerKey="0"
                          headerValue="%{getText('select.status.label')}"
                          cssStyle="width:250px; margin-left:5px;"/></td>
            <td class="button" align="left"><s:submit name="refresh" value="%{getText('refresh.label')}"/></td>
        </tr>
        </tbody>
    </s:form>

</table>
<div id="birth-register-approval-body">
    <fieldset style="margin-bottom:10px;margin-top:20px;border:none">
        <legend></legend>
        <s:actionerror cssStyle="color:red;font-size:10pt"/>
        <s:actionmessage cssStyle="color:blue;font-size:10pt"/>
        <table id="approval-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
            <thead>
            <tr>
                <th width="100px"><s:label name="serial" value="%{getText('adoption_serial.label')}"/></th>
                <th width="800px"><s:label name="name" value="%{getText('name.label')}"/></th>
                <th></th>
            </tr>
            </thead>

            <tbody>
            <s:iterator status="approvalStatus" value="adoptionApprovalAndPrintList" id="approvalList">

                <s:url id="viewAdoptionOrderDetails" action="eprAdoptionOrderDetailsViewMode.do">
                    <s:param name="idUKey" value="idUKey"/>
                    <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                </s:url>

                <tr>
                    <td><s:property value="idUKey"/></td>
                    <s:if test="childExistingName!=null">
                        <td><s:property value="getChildExistingNameToLength(30)"/></td>

                    </s:if>
                    <s:else>
                        <td><s:property value="getChildNewNameToLength(30)"/></td>
                    </s:else>
                    <td align="center">
                        <s:a href="%{viewAdoptionOrderDetails}" title="view">
                            <img id='viewImage' src="<s:url value='/images/print_icon.gif'/>" width="25" height="25"
                                 border="none"/>
                        </s:a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </fieldset>
</div>
<div class="next-previous">
    <%-- Next link to visible next records will only visible if nextFlag is
  set to 1--%>
    <s:url id="previousUrl" action="eprAdoptionPrevious.do" encode="true">
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        <s:param name="currentStatus" value="%{#request.currentStatus}"/>
        <s:param name="pageNo" value="%{#request.pageNo}"/>
    </s:url>

    <s:url id="nextUrl" action="eprAdoptionNext.do" encode="true">
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        <s:param name="currentStatus" value="%{#request.currentStatus}"/>
        <s:param name="pageNo" value="%{#request.pageNo}"/>
    </s:url>
    <s:if test="#request.previousFlag"><s:a href="%{previousUrl}">
        <img src="<s:url value='/images/previous.gif'/>"
             border="none"/></s:a><s:label value="%{getText('previous.label')}"
                                           cssStyle="margin-right:5px;"/></s:if>

    <s:if test="#request.nextFlag"><s:label value="%{getText('next.label')}"
                                            cssStyle="margin-left:5px;"/><s:a href="%{nextUrl}">
        <img src="<s:url value='/images/next.gif'/>" border="none"/></s:a></s:if>
</div>

