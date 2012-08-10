<%@ page import="lk.rgd.common.util.DeathRegisterStateUtil" %>
<%@ page import="lk.rgd.crs.api.domain.DeathRegister" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<script>

    /*date pickers for start and end dates*/
    $(function () {
        $("#fromDate").datepicker({
            changeYear:true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });
    $(function () {
        $("#endDate").datepicker({
            changeYear:true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });

    $(document).ready(function () {
        $('#approval-list-table').dataTable({
            "bPaginate":true,
            "bLengthChange":false,
            "bFilter":true,
            "bSort":true,
            "bInfo":false,
            "bAutoWidth":false,
            "bJQueryUI":true,
            "sPaginationType":"full_numbers"
        });
    });

    $(function () {
        $('#deathSerialNo').val('');
        $('select#birthDistrictId').bind('change', function (evt1) {
            var id = $("select#birthDistrictId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
                    function (data) {
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

        $('select#dsDivisionId').bind('change', function (evt2) {
            var id = $("select#dsDivisionId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                    function (data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#deathDivisionId").html(options);
                    });
        })
    });

    function initPage() {
    }

</script>
<script type="text/javascript" src="<s:url value="/js/selectAll.js"/>"></script>
<script type="text/javascript" src='<s:url value="/js/common.js"/>'></script>


<s:form action="eprDeathFilterByStatus" method="post">
    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <legend><b><s:label value="%{getText('searchOption.label')}"/></b></legend>
        <table width="100%" cellpadding="5" cellspacing="0">
            <col width="300px"/>
            <col/>
            <col width="100px"/>
            <col width="300px"/>
            <col/>
            <tbody>
            <tr>
                <td><s:label name="district" value="%{getText('district.label')}"/></td>
                <td>
                    <s:select id="birthDistrictId" name="birthDistrictId" list="districtList" value="birthDistrictId"
                              cssStyle="width:240px;"/>
                </td>
                <td></td>
                <td><s:label name="division" value="%{getText('select_DS_division.label')}"/></td>
                <td>
                    <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                              cssStyle="float:left;  width:240px;"/>
                </td>
            </tr>
            <tr>
                <td><s:label name="bdDivision" value="%{getText('select_BD_division.label')}"/></td>
                <td>
                    <s:select id="deathDivisionId" name="deathDivisionId" value="{deathDivisionId}"
                              list="bdDivisionList"
                              headerValue="%{getText('all.divisions.label')}" headerKey="0"
                              cssStyle=" width:240px;float:left;"/>
                </td>
                <td></td>


                <td colspan="1"><s:label value="%{getText('death_select_state.lable')}"/></td>
                <td colspan="2">
                    <s:select list="#@java.util.HashMap@{'1':getText('data.entry'),'2':getText('Approved.label'),
                                '3':getText('rejected.label'),'4':getText('death.certificate.printed.label')}"
                              name="currentStatus" value="%{#request.currentStatus}" headerKey="0"
                              headerValue="%{getText('death_select_all.label')}"
                              cssStyle="width:240px;"/>
                </td>
            </tr>
            <tr>
                <td align="left">
                    <s:label value="%{getText('date.from.label')}" cssStyle=" margin-right:5px;"/>
                    <s:textfield id="fromDate" name="fromDate" cssStyle="width:150px" maxLength="10"/>
                </td>
                <td align="right">
                    <s:label value="%{getText('date.to.label')}" cssStyle=" margin-right:5px;"/>
                    <s:textfield id="endDate" name="endDate" cssStyle="width:150px" maxLength="10"/>
                </td>
                <td></td>
                <td></td>
                <td colspan="4" class="button" align="right">
                    <s:hidden name="searchDateRangeFlag" value="%{#request.searchDateRangeFlag}"/>
                    <s:submit name="refresh" value="%{getText('bdfSearch.button')}"/>
                </td>
            </tr>
            </tbody>
        </table>
        <hr style="border:1px solid #c3dcee;"/>
        <table width="100%">
            <tr>
                <td>
                    <s:label value="%{getText('serial.label')}"/>
                </td>
                <td>
                    <s:textfield id="deathSerialNo" name="deathSerialNo" maxLength="10" onkeypress="return numbersOnly(event, true);"/>
                </td>
                <td class="button" align="right">
                    <s:submit name="" value="%{getText('bdfSearch.button')}"/>
                </td>
            </tr>
        </table>
    </fieldset>
</s:form>


<div id="birth-register-approval-body">

<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage cssStyle="color:blue;;font-size:10pt"/>

<fieldset style="margin-bottom:10px;margin-top:20px;border:none">
<table id="approval-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
<thead>
<tr>
    <th width="70px"><s:label name="serial" value="%{getText('serial.label')}"/></th>
    <th width="650px"><s:label name="name" value="%{getText('name.label')}"/></th>
    <th><s:label name="state" value="%{getText('state.label')}"/></th>
    <th width="20px"></th>
    <th width="20px"></th>
    <th width="20px"></th>
    <th width="20px"></th>
    <th width="20px"></th>
    <th width="20px"></th>
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
    <%=DeathRegisterStateUtil.getDeathRegisterState((DeathRegister.State) request.getAttribute("status"))
    %>
</td>

<s:if test="status.ordinal()==0">

    <s:url id="editSelected" action="eprDeathEditMode.do">
        <s:param name="idUKey" value="idUKey"/>
        <s:param name="editMode" value="true"/>
    </s:url>

    <s:url id="approveSelected" action="eprApproveDeath.do">
        <s:param name="idUKey" value="idUKey"/>
        <s:param name="currentStatus" value="%{#request.currentStatus}"/>
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
        <s:param name="deathDivisionId" value="#request.deathDivisionId"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
    </s:url>

    <s:url id="rejectSelected" action="eprRejectDeath.do">
        <s:param name="idUKey" value="idUKey"/>
        <s:param name="currentStatus" value="%{#request.currentStatus}"/>
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
        <s:param name="deathDivisionId" value="#request.deathDivisionId"/>
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        <s:param name="reject" value="true"/>
        <s:param name="serialNumber" value="death.deathSerialNo"/>
    </s:url>

    <s:url id="deleteSelected" action="eprDeleteDeath.do">
        <s:param name="idUKey" value="idUKey"/>
        <s:param name="currentStatus" value="%{#request.currentStatus}"/>
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
        <s:param name="deathDivisionId" value="#request.deathDivisionId"/>
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
    </s:url>

    <s:url id="viewSelected" action="eprDeathViewMode.do">
        <s:param name="idUKey" value="idUKey"/>
    </s:url>

    <td align="center">
        <s:if test="status.ordinal() == 0">
            <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                <img id="editImage" src="<s:url value='/images/edit.png'/>" width="25" height="25"
                     border="none"/></s:a>
        </s:if>
    </td>
    <td align="center">
        <s:if test="(!(session.user_bean.role.roleId.equals('DEO'))) && status.ordinal() == 0">
            <s:a href="%{approveSelected}"
                 title="%{getText('approveTooltip.label')}">
                <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                     border="none" id="approveImage"/></s:a>
        </s:if>
    </td>
    <td align="center">
            <%----%>
        <s:if test="(!(session.user_bean.role.roleId.equals('DEO'))) && status.ordinal() == 0">
            <s:a href="%{rejectSelected}"
                 title="%{getText('rejectTooltip.label')}"><img id="rejectImage"
                                                                src="<s:url value='/images/reject.gif'/>"
                                                                width="25"
                                                                height="25"
                                                                border="none"/></s:a>
        </s:if>
    </td>
    <td align="center">
        <s:if test="status.ordinal() == 0">
            <s:a href="%{deleteSelected}"
                 title="%{getText('deleteToolTip.label')}"><img id='deleteImage'
                                                                src="<s:url value='/images/delete.gif'/>"
                                                                width="25"
                                                                height="25"
                                                                border="none"/></s:a>
        </s:if>
    </td>
    <td align="center"><s:a href="%{viewSelected}"
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
        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
        <s:param name="deathDivisionId" value="#request.deathDivisionId"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
    </s:url>

    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td align="center"><s:a href="%{viewSelected}"
                            title="%{getText('viewDeathRegistrationTooltip.label')}">
        <img id='viewImage' src="<s:url value='/images/view.gif'/>" width="25" height="25"
             border="none"/></s:a>
    </td>
    <td align="center">
        <s:if test="#request.allowPrintCertificate">
            <s:a href="%{cetificatePrintUrl}"
                 title="%{getText('printDeathRegistrationTooltip.label')}">
                <img id="printImage" src="<s:url value='/images/print_icon.gif'/>" border="none"
                     width="25"
                     height="25"/>
            </s:a>
        </s:if>
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

    <td align="center"><s:a href="%{viewSelected}"
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

    <td align="center"><s:a href="%{viewSelected}"
                            title="%{getText('viewDeathRegistrationTooltip.label')}">
        <img id='viewImage' src="<s:url value='/images/view.gif'/>" width="25" height="25"
             border="none"/></s:a>
    </td>
    <td align="center">
        <s:url id="cetificatePrintUrl" action="eprDeathCertificate.do">
            <s:param name="idUKey" value="idUKey"/>
            <s:param name="currentStatus" value="%{#request.currentStatus}"/>
            <s:param name="pageNo" value="%{#request.pageNo}"/>
            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
            <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
            <s:param name="deathDivisionId" value="#request.deathDivisionId"/>
            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        </s:url>
        <s:if test="#request.allowPrintCertificate">
            <s:a href="%{cetificatePrintUrl}"
                 title="%{getText('printDeathRegistrationTooltipAgain.label')}">
                <img id="printImage" src="<s:url value='/images/print_icon.gif'/>" border="none"
                     width="25"
                     height="25"/>
            </s:a>
        </s:if>
    </td>
</s:if>
<s:if test="status.ordinal()==4 || status.ordinal()==5">

    <s:url id="viewSelected" action="eprDeathViewMode.do">
        <s:param name="idUKey" value="idUKey"/>
    </s:url>

    <td></td>
    <td></td>
    <td></td>
    <td></td>

    <td align="center"><s:a href="%{viewSelected}"
                            title="%{getText('viewDeathRegistrationTooltip.label')}">
        <img id='viewImage' src="<s:url value='/images/view.gif'/>" width="25" height="25"
             border="none"/></s:a>
    </td>
    <td align="center">
        <s:url id="cetificatePrintUrl" action="eprDeathCertificate.do">
            <s:param name="idUKey" value="idUKey"/>
            <s:param name="currentStatus" value="%{#request.currentStatus}"/>
            <s:param name="pageNo" value="%{#request.pageNo}"/>
            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
            <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
            <s:param name="deathDivisionId" value="#request.deathDivisionId"/>
            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        </s:url>
        <s:if test="#request.allowPrintCertificate">
            <s:a href="%{cetificatePrintUrl}"
                 title="%{getText('printDeathRegistrationTooltipAgain.label')}">
                <img id="printImage" src="<s:url value='/images/print_icon.gif'/>" border="none"
                     width="25"
                     height="25"/>
            </s:a>
        </s:if>
    </td>
</s:if>
</tr>
</s:iterator>
</tbody>
</table>
<div class="next-previous" style="float:right;margin-right:10px;clear:both;">
    <s:url id="previousUrl" action="eprDeathFilterByStatusPreviouse.do">
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="deathDistrictId" value="#request.deathDistrictId"/>
        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
        <s:param name="deathDivisionId" value="#request.deathDivisionId"/>
        <s:param name="printStart" value="#request.printStart"/>
        <s:param name="fromDate" value="#request.fromDate"/>
        <s:param name="endDate" value="#request.endDate"/>
    </s:url>
    <s:url id="nextUrl" action="eprDeathFilterByStatusNext.do">
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="deathDistrictId" value="#request.deathDistrictId"/>
        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
        <s:param name="deathDivisionId" value="#request.deathDivisionId"/>
        <s:param name="printStart" value="#request.printStart"/>
        <s:param name="fromDate" value="#request.fromDate"/>
        <s:param name="endDate" value="#request.endDate"/>
    </s:url>
    <s:if test="pageNo!=1">
        <s:a href="%{previousUrl}">
            <img src="<s:url value='/images/previous.gif'/>" border="none"/>
        </s:a>
        <s:label value="%{getText('previous.label')}"/>
    </s:if>
    <s:if test="deathApprovalAndPrintList.size >=50">
        <s:a href="%{nextUrl}">
            <img src="<s:url value='/images/next.gif'/>" border="none"/>
        </s:a>
        <s:label value="%{getText('next.label')}"/>
    </s:if>
</div>
</fieldset>
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




