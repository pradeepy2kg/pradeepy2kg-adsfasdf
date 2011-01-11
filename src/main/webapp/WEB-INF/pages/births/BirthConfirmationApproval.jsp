<%@ page import="lk.rgd.common.util.NameFormatUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" src="../js/validate.js"></script>

<s:hidden id="error1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error2" value="%{getText('p1.serial.text')}"/>
<s:hidden id="error3" value="%{getText('searchStartDate.label')}"/>
<s:hidden id="error4" value="%{getText('searchEndDate.label')}"/>

<script>

    $(function() {
        $("#searchStartDatePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });

    $(function() {
        $("#searchEndDatePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });
    $(document).ready(function() {
        $('#confirm-list-table').dataTable({
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
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
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
                        $("select#birthDivisionId").html(options2);
                    });
        });

        $('select#dsDivisionId').bind('change', function(evt2) {
            var id = $("select#dsDivisionId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#birthDivisionId").html(options);
                    });
        })
    })

    var errormsg = "";
    function validate() {
        var domObject;
        var returnval = true;

        // validate serial number
        domObject = document.getElementById('bdfSerialNoId');
        if (!isFieldEmpty(domObject))
            isNumeric(domObject.value, 'error1', 'error2');

        // validate start and end date
        domObject = document.getElementById('searchStartDatePicker');
        if (!isFieldEmpty(domObject))
            isDate(domObject.value, 'error1', 'error3');

        domObject = document.getElementById('searchEndDatePicker');
        if (!isFieldEmpty(domObject))
            isDate(domObject.value, 'error1', 'error4');

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

</script>
<script type="text/javascript" src="<s:url value="/js/selectAll.js"/>"></script>

<div id="birth-confirm-approval">

<s:form action="eprConfirmationApprovalRefresh" name="birth_register_approval_header"
        id="birth-confirmation-approval-form" onsubmit="javascript:return validate()">

<div id="birth-confirm-approval-header">
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
                <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}"
                          list="bdDivisionList" headerValue="%{getText('all.divisions.label')}" headerKey="0"
                          cssStyle=" width:240px;float:left;"/>
            </td>
            <td></td>
            <td><s:label value="%{getText('serial.label')}"/></td>
            <td><s:textfield value="" name="bdId" cssStyle="width:232px;" id="bdfSerialNoId" maxLength="10"/></td>
        </tr>
        <tr>
            <td align="left">
                <s:label value="%{getText('date.from.label')}" cssStyle=" margin-right:5px;"/>
                <s:textfield id="searchStartDatePicker" name="searchStartDate" cssStyle="width:150px" maxLength="10"/>
            </td>
            <td align="right">
                <s:label value="%{getText('date.to.label')}" cssStyle=" margin-right:5px;"/>
                <s:textfield id="searchEndDatePicker" name="searchEndDate" cssStyle="width:150px" maxLength="10"/>
            </td>
            <td></td>
            <td></td>
            <td class="button" align="right">
                <s:hidden name="confirmationApprovalFlag" value="true"/>
                <s:hidden name="searchDateRangeFlag" value="%{#request.searchDateRangeFlag}"/>
                <s:submit name="refresh" value="%{getText('bdfSearch.button')}"/>
            </td>
        </tr>
        </tbody>
    </table>
</fieldset>
</s:form>
<%--<s:actionerror/>--%>
<s:actionmessage cssClass="alreadyPrinted"/>
<s:if test="#request.warnings != null">
    <div id="birth-confirm-approval-message" class="font-9" align="center">
        <table width="100%" cellpadding="0" cellspacing="0">
            <s:iterator value="#request.warnings">
                <tr>
                    <td><s:property value="message"/></td>
                </tr>
            </s:iterator>
        </table>
    </div>
</s:if>
<fieldset style="border:none">
    <div id="birth-confirm-approval-body">
        <s:form action="eprApproveConfirmationBulk" name="birth_register_approval_body" method="post">
            <s:if test="approvalPendingList.size>0">
                <table id="confirm-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
                <thead>
                <tr class="table-title">
                    <th width="15px"></th>
                    <th width="20px"><s:label value="%{getText('division.label')}"/></th>
                    <th width="70px"><s:label name="serial" value="%{getText('serial.label')}"/></th>
                    <th><s:label name="name" value="%{getText('name.label')}"/></th>
                    <th width="90px"><s:label name="received" value="%{getText('received.label')}"/></th>
                    <th width="20px"></th>
                    <th width="20px"></th>
                    <th width="20px"></th>
                </tr>
                </thead>
            </s:if>
            <tbody>
            <s:iterator status="approvalStatus" value="approvalPendingList" id="approvalList">
                <tr class="<s:if test="#approvalStatus.odd == true">odd</s:if><s:else>even</s:else>">
                        <%--<td class="table-row-index"><s:property value="%{#approvalStatus.count + recordCounter}"/></td>--%>
                    <td>
                        <s:if test="register.getStatus().toString() == 'CONFIRMATION_CHANGES_CAPTURED' & (approvalPendingList.size>1)">
                            <s:checkbox name="index"
                                        onclick="javascript:selectall(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"
                                        title="%{getText('select.label')}" value="%{#index}"
                                        fieldValue="%{#approvalList.idUKey}"/>
                        </s:if>
                    </td>
                    <td><s:property value="register.birthDivision.divisionId"/></td>
                    <td><s:property value="idUKey"/></td>
                    <td>
                        <s:if test="child.childFullNameOfficialLang != null">
                            <%= NameFormatUtil.getDisplayName((String) request.getAttribute("child.childFullNameOfficialLang"), 40)%>
                        </s:if>
                    </td>
                    <td><s:property value="confirmant.confirmationProcessedTimestamp"/></td>
                    <td align="center">
                        <s:if test="#request.allowEditBDF">
                            <s:url id="editSelected" action="eprBirthConfirmationInit.do">
                                <s:param name="bdId" value="idUKey"/>
                            </s:url>

                            <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                                <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                     border="none"/></s:a>
                        </s:if>
                    </td>
                    <td align="center">
                        <s:if test="#request.allowApproveBDFConfirmation">
                            <s:if test="register.getStatus().toString() == 'CONFIRMATION_CHANGES_CAPTURED'">
                                <s:url id="approveSelected" action="eprApproveBirthConfirmation.do">
                                    <s:param name="confirmationApprovalFlag" value="true"/>
                                    <s:param name="bdId" value="idUKey"/>
                                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                                    <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                                    <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                                    <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                                    <s:param name="recordCounter" value="#request.recordCounter"/>
                                </s:url><s:a href="%{approveSelected}"
                                             title="%{getText('approveTooltip.label')}">
                                <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                     border="none"/></s:a>
                            </s:if> </s:if>
                    </td>
                    <td align="center">
                        <s:if test="#request.allowApproveBDFConfirmation">
                        <s:if test="register.getStatus().toString() == 'CONFIRMATION_CHANGES_CAPTURED'">
                        <s:url id="rejectSelected" action="eprRejectBirthConfirmation.do">
                            <s:param name="confirmationApprovalFlag" value="true"/>
                            <s:param name="bdId" value="idUKey"/>
                            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                            <s:param name="pageNo" value="%{#request.pageNo}"/>
                            <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                            <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                            <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                            <s:param name="recordCounter" value="#request.recordCounter"/>
                            <s:param name="reject" value="true"/>
                        </s:url><s:a href="%{rejectSelected}"
                                     title="%{getText('rejectTooltip.label')}"><img
                            src="<s:url value='/images/reject.gif'/>" width="25" height="25"
                            border="none"/></s:a>
                    </td>
                    </s:if>
                    </s:if>
                </tr>
                <%--select_all checkbox is visible only if
            counter is greater than one--%>
                <s:set name="counter" scope="request" value="#approvalStatus.count"/>
            </s:iterator>
            </tbody>
            </table>

            <div class="form-submit">
                <s:if test="#request.counter>1">
                    <s:label><s:checkbox
                            name="allCheck"
                            onclick="javascript:selectallMe(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"/>
                        <span><s:label name="select_all" value="%{getText('select_all.label')}"/></span></s:label>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <s:label><span><s:label name="print_selected"
                                            value="%{getText('selected_all.label')}"/></span></s:label>
                    <s:hidden name="confirmationApprovalFlag" value="true"/>
                    <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
                    <s:hidden name="pageNo" value="%{#request.pageNo}"/>
                    <s:hidden name="birthDistrictId" value="%{#request.birthDistrictId}"/>
                    <s:hidden name="birthDivisionId" value="%{#request.birthDivisionId}"/>
                    <s:hidden name="dsDivisionId" value="%{#request.dsDivisionId}"/>
                    <s:hidden name="recordCounter" value="%{#request.recordCounter}"/>
                    <s:submit name="approveSelected" value="%{getText('approve.label')}"/>
                </s:if>
            </div>
            <div class="next-previous">
                    <%-- Next link to visible next records will only visible if nextFlag is
                  set to 1--%>
                <s:url id="previousUrl" action="eprConfirmationApprovalPrevious.do">
                    <s:param name="confirmationApprovalFlag" value="true"/>
                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                    <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                    <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                    <s:param name="recordCounter" value="#request.recordCounter"/>
                    <s:param name="searchDateRangeFlag" value="#request.searchDateRangeFlag"/>
                </s:url>

                <s:url id="nextUrl" action="eprConfirmationApprovalNext.do">
                    <s:param name="confirmationApprovalFlag" value="true"/>
                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                    <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                    <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                    <s:param name="recordCounter" value="#request.recordCounter"/>
                    <s:param name="searchDateRangeFlag" value="#request.searchDateRangeFlag"/>
                </s:url>
                <s:if test="#request.previousFlag"><s:a href="%{previousUrl}">
                    <img src="<s:url value='/images/previous.gif'/>" width="40px" height="35px"
                         border="none"/></s:a><s:label value="%{getText('previous.label')}"
                                                       cssStyle="margin-right:5px;"/></s:if>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <s:if test="#request.nextFlag"><s:label value="%{getText('next.label')}"
                                                        cssStyle="margin-left:5px;"/><s:a href="%{nextUrl}">
                    <img src="<s:url value='/images/next.gif'/>" width="40px" height="35px"
                         border="none"/></s:a></s:if>
            </div>
        </s:form>
    </div>
</fieldset>
</div>
<%-- Styling Completed --%>