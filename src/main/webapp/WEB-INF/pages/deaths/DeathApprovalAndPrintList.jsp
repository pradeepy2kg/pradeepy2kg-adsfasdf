<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/popreg/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>

<script>

    /*date pickers for start and end dates*/
    $(function() {
        $("#fromDateId").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });
    $(function() {
        $("#endDateId").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });

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

    $(function() {
        $('select#birthDistrictId').bind('change', function(evt1) {
            var id = $("select#birthDistrictId").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivisionId").html(options1);

                        var options2 = '';
                        var bd = data.bdDivisionList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#deathDivisionId").html(options2);
                    });
        });

        $('select#dsDivisionId').bind('change', function(evt2) {
            var id = $("select#dsDivisionId").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#deathDivisionId").html(options);
                    });
        })
    });

</script>
<script type="text/javascript" src="<s:url value="/js/selectAll.js"/>"></script>

<s:actionerror/>
<s:form action="eprDeathFilterByStatus" method="post">
    <table width="100%" cellpadding="5" cellspacing="0">
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td colspan="1">
                <s:label name="district" value="%{getText('district.label')}"/>
            </td>
            <td colspan="1">
                <s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                          value="birthDistrictId" cssStyle="width:240px;"/>
            </td>
            <td colspan="1"></td>
            <td colspan="1">
                <s:select id="deathDivisionId" name="deathDivisionId" value="%{deathDivisionId}"
                          list="bdDivisionList"
                          cssStyle=" width:240px;float:right;"/>
            </td>
        </tr>
        <tr>
            <td colspan="1">
                <s:label name="division" value="%{getText('select_ds_division.label')}"/>
            </td>
            <td colspan="1">
                <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                          cssStyle="float:left;  width:240px;"/>
            </td>
        </tr>
        </tbody>
    </table>
    <br>
    <br>
    <table width="100%" cellpadding="5" cellspacing="0">
        <col>
        <col>
        <col>
        <tbody>
        <tr>
            <td colspan="1"><s:label value="%{getText('death_select_state.lable')}"/></td>
            <td colspan="2" width="230px">
                <s:select list="#@java.util.HashMap@{'1':getText('data.entry.label'),'2':getText('Approved.label'),
        '3':getText('rejected.label'),'4':getText('death.certificate.printed.label')}"
                          name="currentStatus" value="%{#request.currentStatus}" headerKey="0"
                          headerValue="%{getText('death_select_all.lable')}"
                          cssStyle="width:200px; margin-left:5px;"></s:select>
            </td>
        </tr>
        </tbody>

    </table>

    <table cellpadding="5" cellspacing="0">

        <col/>
        <col/>
        <col/>
        <col/>
        <col/>

        <tbody>
        <tr>
            <td>
                <s:label value="%{getText('date.from.label')}"/>
            </td>
            <td>
                <s:textfield name="fromDate" id="fromDateId"/>
            </td>
            <td><s:label value="%{getText('date.to.label')}"/></td>
            <td>
                <s:textfield name="endDate" id="endDateId"/>
            </td>
            <td colspan="2" class="button" align="left"><s:submit name="refresh"
                                                                  value="%{getText('refresh.label')}"/></td>
        </tr>
        </tbody>
    </table>

</s:form>

<div id="birth-register-approval-body">

<fieldset style="margin-bottom:10px;margin-top:20px;border:none">
<legend></legend>
<table id="approval-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
<thead>
<tr>
    <th><s:label name="serial" value="%{getText('serial.label')}"/></th>
    <th><s:label name="name" value="%{getText('name.label')}"/></th>
    <th><s:label name="state" value="%{getText('state.label')}"/></th>
    <th><s:label name="edit" value="%{getText('edit.label')}"/></th>
    <th><s:label name="approve" value="%{getText('approve.label')}"/></th>
    <th><s:label name="reject" value="%{getText('reject.label')}"/></th>
    <th><s:label name="delete" value="%{getText('delete.label')}"/></th>
    <th><s:label name="veiw" value="%{getText('view.label')}"/></th>
    <th><s:label name="print" value="%{getText('printCertificete.label')}"/></th>
</tr>
</thead>
<tbody>
<s:iterator value="deathApprovalAndPrintList" status="approvedAndPrint" id="approveAndPrintList">

    <tr>
        <td>
            <s:property value="death.deathSerialNo"/>
        </td>
        <td>
            <s:property value="deathPerson.deathPersonNameOfficialLang"/>
        </td>
        <td>
            <s:property value="status"/>
        </td>

        <s:if test="status.ordinal()==0">

            <s:url id="editSelected" action="eprDeathEditMode.do">
                <s:param name="idUKey" value="idUKey"/>
                <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                <s:param name="pageNo" value="%{#request.pageNo}"/>
                <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                <s:param name="previousFlag" value="%{#request.previousFlag}"/>
            </s:url>

            <s:url id="approveSelected" action="eprApproveDeath.do">
                <s:param name="idUKey" value="idUKey"/>
                <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                <s:param name="pageNo" value="%{#request.pageNo}"/>
                <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                <s:param name="previousFlag" value="%{#request.previousFlag}"/>
            </s:url>

            <s:url id="rejectSelected" action="eprRejectDeath.do">
                <s:param name="idUKey" value="idUKey"/>
                <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                <s:param name="pageNo" value="%{#request.pageNo}"/>
                <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                <s:param name="previousFlag" value="%{#request.previousFlag}"/>
            </s:url>

            <s:url id="deleteSelected" action="eprDeleteDeath.do">
                <s:param name="idUKey" value="idUKey"/>
                <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                <s:param name="pageNo" value="%{#request.pageNo}"/>
                <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                <s:param name="previousFlag" value="%{#request.previousFlag}"/>
            </s:url>

            <s:url id="viewSelected" action="eprDeathViewMode.do">
                <s:param name="idUKey" value="idUKey"/>
            </s:url>

            <td align="center" width="60px">
                <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                    <img id="editImage" src="<s:url value='/images/edit.png'/>" width="25" height="25"
                         border="none"/></s:a>
            </td>
            <td align="center" width="60px"><s:a href="%{approveSelected}"
                                    title="%{getText('approveTooltip.label')}">
                <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                     border="none" id="approveImage"/></s:a>
            </td>
            <td align="center" width="60px"><s:a href="%{rejectSelected}"
                                    title="%{getText('rejectTooltip.label')}"><img id="rejectImage"
                                                                                   src="<s:url value='/images/reject.gif'/>"
                                                                                   width="25"
                                                                                   height="25"
                                                                                   border="none"/></s:a>
            </td>
            <td align="center" width="60px"><s:a href="%{deleteSelected}"
                                    title="%{getText('deleteToolTip.label')}"><img id='deleteImage'
                                                                                   src="<s:url value='/images/delete.gif'/>"
                                                                                   width="25"
                                                                                   height="25"
                                                                                   border="none"/></s:a>
            </td>
            <td align="center" width="60px"><s:a href="%{viewSelected}"
                                    title="%{getText('viewDeathRegistrationTooltip.label')}">
                <img id='viewImage' src="<s:url value='/images/view.gif'/>" width="25" height="25"
                     border="none"/></s:a>
            </td>
            <td></td>
        </s:if>

            <%--only veiw and print if approved--%>
        <s:if test="status.ordinal()==1">

            <s:url id="viewSelected" action="eprDeathViewMode.do">
                <s:param name="idUKey" value="idUKey"/>
            </s:url>

            <s:url id="cetificatePrintUrl" action="eprDeathCertificate.do">
                <s:param name="idUKey" value="idUKey"/>
                <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                <s:param name="pageNo" value="%{#request.pageNo}"/>
                <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                <s:param name="rePrint" value="false"/>
            </s:url>

            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td align="center" width="60px"><s:a href="%{viewSelected}"
                                    title="%{getText('viewDeathRegistrationTooltip.label')}">
                <img id='viewImage' src="<s:url value='/images/view.gif'/>" width="25" height="25"
                     border="none"/></s:a>
            </td>
            <td align="center" width="60px">
                <s:a href="%{cetificatePrintUrl}"
                     title="%{getText('printDeathRegistrationTooltip.label')}">
                    <img id="printImage" src="<s:url value='/images/print_icon.gif'/>" border="none"
                         width="25"
                         height="25"/>
                </s:a>
            </td>

        </s:if>

        <s:if test="status.ordinal()==2">

            <s:url id="viewSelected" action="eprDeathViewMode.do">
                <s:param name="idUKey" value="idUKey"/>
            </s:url>

            <td></td>
            <td></td>
            <td></td>
            <td></td>

            <td align="center" width="60px"><s:a href="%{viewSelected}"
                                    title="%{getText('viewDeathRegistrationTooltip.label')}">
                <img id='viewImage' src="<s:url value='/images/view.gif'/>" width="25" height="25"
                     border="none"/></s:a>
            </td>

            <td></td>
        </s:if>
            <%--already printed--%>
        <s:if test="status.ordinal()==3">

            <s:url id="viewSelected" action="eprDeathViewMode.do">
                <s:param name="idUKey" value="idUKey"/>
            </s:url>

            <td></td>
            <td></td>
            <td></td>
            <td></td>

            <td align="center" width="60px"><s:a href="%{viewSelected}"
                                    title="%{getText('viewDeathRegistrationTooltip.label')}">
                <img id='viewImage' src="<s:url value='/images/view.gif'/>" width="25" height="25"
                     border="none"/></s:a>
            </td>
            <td align="center" width="60px">
                <s:url id="cetificatePrintUrl" action="eprPrintDeath.do">
                    <s:param name="idUKey" value="idUKey"/>
                    <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                </s:url>
                <s:a href="%{cetificatePrintUrl}"
                     title="%{getText('printDeathRegistrationTooltipAgain.label')}">
                    <img id="printImage" src="<s:url value='/images/print_icon.gif'/>" border="none"
                         width="25"
                         height="25"/>
                </s:a>
            </td>
        </s:if>
    </tr>
</s:iterator>
</tbody>

<%--customize based on user role--%>

<s:if test="!#request.allowEditDeath">
<script type="text/javascript">
    document.getElementById('editImage').style.display = 'none';
</script>
</s:if>
<s:if test="!#request.allowApproveDeath">
<script type="text/javascript">
    document.getElementById('approveImage').style.display = 'none';
    document.getElementById('rejectImage').style.display = 'none';
    document.getElementById('deleteImage').style.display = 'none';
    document.getElementById('printImage').style.display = 'none';
</script>
</s:if>



