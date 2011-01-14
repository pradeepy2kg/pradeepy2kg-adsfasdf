<%@ page import="lk.rgd.common.util.NameFormatUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" src="../js/validate.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script>
    function initPage() {
    }

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
                         options2 += '<option value="' + 0 + '">' + <s:label value="%{getText('all.divisions.label')}"/> + '</option>';
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
                         options += '<option value="' + 0 + '">' + <s:label value="%{getText('all.divisions.label')}"/>  + '</option>';
                         for (var i = 0; i < bd.length; i++) {
                             options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                         }
                         $("select#birthDivisionId").html(options);
                     });
        })
    });


    var fieldName = 'index';

    function
            selectall(form, allCheck) {
        var i = form.elements.length;
        var e = form.elements;
        var name = new Array();
        var value = new Array();
        var j = 0;
        for (var k = 0; k < i; k++) {
            if (form.elements[k].name == fieldName) {
                if (form.elements[k].checked == true) {
                    value[j] = form.elements[k].value;
                    j++;
                }
            }
        }
        checkSelect(form, allCheck);
    }
    function selectCheck(obj, form, allCheck) {
        var i = form.elements.length;
        for (var k = 0; k < i; k++) {
            if (form.elements[k].name == fieldName) {
                form.elements[k].checked = obj;
            }
        }
        selectall(form, allCheck);
    }

    function selectallMe(form, allCheck) {
        if (allCheck.checked == true) {
            selectCheck(true, form, allCheck);
        }
        else {
            selectCheck(false, form, allCheck);
        }
    }
    function checkSelect(form, allCheck) {
        var i = form.elements.length;
        var berror = true;
        for (var k = 0; k < i; k++) {
            if (form.elements[k].name == fieldName) {
                if (form.elements[k].checked == false) {
                    berror = false;
                    break;
                }
            }
        }
        if (berror == false) {
            allCheck.checked = false;
        }
        else {
            allCheck.checked = true;
        }
    }
    var errormsg = "";
    function validate() {
        var domObject;
        var returnval = true;

        // validate serial number
        domObject = document.getElementById('bdfSerialNoId');
        if (!isFieldEmpty(domObject))
            validateSerialNo(domObject, 'error1', 'error2');

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

    function deleteWarning() {
        var ret = true;
        ret = confirm(document.getElementById('warning').value)
        return ret;
    }
</script>

<div id="birth-register-approval">
<s:form action="eprApprovalRefresh" name="birth_register_approval_header" id="birth-register-approval-form"
        onsubmit="javascript:return validate()">
<div id="birth-register-approval-header">
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
                <s:select id="birthDivisionId" name="birthDivisionId" value="{birthDivisionId}" list="bdDivisionList"
                          headerValue="%{getText('all.divisions.label')}" headerKey="0"
                          cssStyle=" width:240px;float:left;"/>
            </td>
            <td></td>
            <td><s:label value="%{getText('serial.label')}"/></td>
            <td><s:textfield value="" name="bdfSerialNo" cssStyle="width:232px;" id="bdfSerialNoId"
                             maxLength="10"/></td>
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
            <td colspan="4" class="button" align="right">
                <s:hidden name="searchDateRangeFlag" value="%{#request.searchDateRangeFlag}"/>
                <s:submit name="refresh" value="%{getText('bdfSearch.button')}"/>
            </td>
        </tr>
        </tbody>
    </table>
</fieldset>
</s:form>
<s:actionmessage/>
<s:if test="#request.warnings != null">
    <div id="birth-register-approval-message" class="font-9" align="center">
        <table width="100%" cellpadding="0" cellspacing="0">
            <s:iterator value="#request.warnings">
                <tr>
                    <td><s:property value="message"/></td>
                </tr>
            </s:iterator>
        </table>
    </div>
</s:if>
<div id="birth-register-approval-body">
    <%--todo permission handling--%>
    <%--<s:if test="approvalPendingList.size==0">--%>
    <%--<p class="alreadyPrinted" align="center"><s:label value="%{getText('noItemMsg.label')}"/></p>--%>
    <%--</s:if>--%>
    <s:form action="eprApproveBulk" name="birth_register_approval_body" method="POST">
        <s:if test="approvalPendingList.size>0">
            <fieldset style="margin-bottom:10px;margin-top:20px;border:none">
            <legend></legend>
            <table id="approval-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
            <thead>
            <tr>
                <th width="15px"></th>
                <th width="20px"><s:label value="%{getText('division.label')}"/></th>
                <th width="70px"><s:label name="serial" value="%{getText('serial.label')}"/></th>
                <th><s:label name="name" value="%{getText('name.label')}"/></th>
                <th width="90px"><s:label name="received" value="%{getText('received.label')}"/></th>
                <th width="40px"><s:label name="live" value="%{getText('live.label')}"/></th>
                <th width="20px"></th>
                <th width="20px"></th>
                <th width="20px"></th>
                <th width="20px"></th>
            </tr>
            </thead>
        </s:if>
        <tbody>
        <s:iterator status="approvalStatus" value="approvalPendingList" id="approvalList">
            <tr>
                    <%--<td><s:property value="%{#approvalStatus.count + recordCounter}"/></td>--%>
                <td>
                    <s:if test="(register.birthType.ordinal() != 3) && (approvalPendingList.size>1)">
                        <s:checkbox name="index"
                                    onclick="javascript:selectall(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"
                                    title="%{getText('select.label')}" value="%{#index}"
                                    fieldValue="%{#approvalList.idUKey}"/>
                    </s:if>
                </td>
                <td><s:property value="register.birthDivision.divisionId"/></td>
                <td><s:property value="register.bdfSerialNo"/></td>
                <td>
                    <s:if test="child.childFullNameOfficialLang != null">
                        <%= NameFormatUtil.getDisplayName((String) request.getAttribute("child.childFullNameOfficialLang"), 40)%>
                    </s:if>
                </td>
                <td align="center"><s:property value="register.dateOfRegistration"/></td>
                <td align="center">
                    <s:if test="register.birthType.ordinal() != 0">
                        <s:label value="%{getText('yes.label')}"/>
                    </s:if>
                    <s:elseif test="register.birthType.ordinal() == 0">
                        <s:label value="%{getText('no.label')}"/>
                    </s:elseif>
                </td>
                <s:if test="#request.allowEditBDF">
                    <s:url id="editSelected" action="eprBirthRegistrationInit.do">
                        <s:param name="bdId" value="idUKey"/>
                    </s:url>
                    <td align="center"><s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                        <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                             border="none"/></s:a>
                    </td>
                </s:if>
                <s:if test="#request.allowApproveBDF">
                    <s:url id="approveSelected" action="eprApproveBirthDeclaration.do">
                        <s:param name="bdId" value="idUKey"/>
                        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                        <s:param name="pageNo" value="%{#request.pageNo}"/>
                        <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                        <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                        <s:param name="recordCounter" value="#request.recordCounter"/>
                    </s:url>
                    <td align="center">
                        <s:if test="register.birthType.ordinal() != 3">
                            <s:a href="%{approveSelected}" title="%{getText('approveTooltip.label')}">
                                <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                     border="none"/></s:a>
                        </s:if>
                    </td>
                </s:if>
                <s:if test="#request.allowApproveBDF">
                    <s:url id="rejectSelected" action="eprRejectBirthDeclaration.do">
                        <s:param name="bdId" value="idUKey"/>
                        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                        <s:param name="pageNo" value="%{#request.pageNo}"/>
                        <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                        <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                        <s:param name="recordCounter" value="#request.recordCounter"/>
                        <s:param name="reject" value="true"/>
                    </s:url>
                    <td align="center">
                        <s:if test="register.birthType.ordinal() != 3">
                            <s:a href="%{rejectSelected}" title="%{getText('rejectTooltip.label')}">
                                <img src="<s:url value='/images/reject.gif'/>" width="25" height="25" border="none"/>
                            </s:a>
                        </s:if>
                    </td>
                </s:if>
                <s:if test="#request.allowApproveBDF">
                    <s:url id="deleteSelected" action="eprDeleteApprovalPending.do">
                        <s:param name="bdId" value="idUKey"/>
                        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                        <s:param name="pageNo" value="%{#request.pageNo}"/>
                        <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                        <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                        <s:param name="recordCounter" value="#request.recordCounter"/>
                    </s:url>
                    <td align="center">
                        <s:a href="%{deleteSelected}" title="%{getText('deleteToolTip.label')}"><img
                                src="<s:url value='/images/delete.gif'/>" width="25" height="25"
                                border="none" onclick="javascript:return deleteWarning()"/></s:a>
                    </td>
                </s:if>
            </tr>
            <%--select_all checkbox is visible only if counter is greater than one--%>
            <s:set name="counter" scope="request" value="#approvalStatus.count"/>
        </s:iterator>
        </tbody>
        </table>
        </fieldset>

        <div class="form-submit">
            <s:if test="#request.counter>1">
                <s:label><s:checkbox name="allCheck"
                                     onclick="javascript:selectallMe(document.birth_register_approval_body,document.birth_register_approval_body.allCheck)"/>
                    <span><s:label name="select_all" value="%{getText('select_all.label')}"/></span></s:label>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <s:label><span><s:label name="print_selected"
                                        value="%{getText('selected_all.label')}"/></span></s:label>
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
            <s:url id="previousUrl" action="eprApprovalPrevious.do" encode="true">
                <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                <s:param name="pageNo" value="%{#request.pageNo}"/>
                <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                <s:param name="recordCounter" value="#request.recordCounter"/>
                <s:param name="startDate" value="#request.startDate"/>
                <s:param name="endDate" value="#request.endDate"/>
                <s:param name="searchDateRangeFlag" value="#request.searchDateRangeFlag"/>
            </s:url>

            <s:url id="nextUrl" action="eprApprovalNext.do" encode="true">
                <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                <s:param name="pageNo" value="%{#request.pageNo}"/>
                <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                <s:param name="recordCounter" value="#request.recordCounter"/>
                <s:param name="startDate" value="#request.startDate"/>
                <s:param name="endDate" value="#request.endDate"/>
                <s:param name="searchDateRangeFlag" value="#request.searchDateRangeFlag"/>
            </s:url>
            <s:if test="#request.previousFlag"><s:a href="%{previousUrl}">
                <img src="<s:url value='/images/previous.gif'/>"
                     border="none"/></s:a><s:label value="%{getText('previous.label')}"
                                                   cssStyle="margin-right:5px;"/></s:if>

            <s:if test="#request.nextFlag"><s:label value="%{getText('next.label')}"
                                                    cssStyle="margin-left:5px;"/><s:a href="%{nextUrl}">
                <img src="<s:url value='/images/next.gif'/>" border="none"/></s:a></s:if>
        </div>
    </s:form>
    <s:hidden id="error1" value="%{getText('p1.invalide.inputType')}"/>
    <s:hidden id="error2" value="%{getText('p1.serial.text')}"/>
    <s:hidden id="error3" value="%{getText('searchStartDate.label')}"/>
    <s:hidden id="error4" value="%{getText('searchEndDate.label')}"/>
    <s:hidden id="warning" value="%{getText('birthDelete.warning.label')}"/>
</div>
</div>
<%-- Styling Completed --%>