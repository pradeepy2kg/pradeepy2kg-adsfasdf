<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script>
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
                        $("select#birthDivisionId").html(options2);
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
                        $("select#birthDivisionId").html(options);
                    });
        })
    });

</script>
<script type="text/javascript" src="<s:url value="/js/selectAll.js"/>"></script>

<s:actionerror/>
<table width="100%" cellpadding="5" cellspacing="0">
    <col width="220px"/>
    <col/>
    <col width="220px"/>
    <col/>
    <tbody>
    <tr>
        <td><s:label name="district" value="%{getText('district.label')}"/></td>
        <td colspan="3">
            <s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                      value="birthDistrictId" cssStyle="width:240px;"/>
        </td>
    </tr>
    <tr>
        <td><s:label name="division" value="%{getText('select_ds_division.label')}"/></td>
        <td colspan="3">
            <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                      cssStyle="float:left;  width:240px;"/>
            <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}"
                      list="bdDivisionList"
                      cssStyle=" width:240px;float:right;"/>
        </td>

    </tr>
    <tr>
        <td><s:label value="%{getText('serial.label')}"/></td>
        <td><s:textfield value="" name="bdfSerialNo" cssStyle="width:232px;"/></td>
        <td align="right"><s:label value="%{getText('date.from.label')}"
                                   cssStyle=" margin-right:5px;"/><sx:datetimepicker name="searchStartDate"
                                                                                     displayFormat="yyyy-MM-dd"/></td>
        <td align="right"><s:label value="%{getText('date.to.label')}"
                                   cssStyle=" margin-right:5px;"/><sx:datetimepicker name="searchEndDate"
                                                                                     displayFormat="yyyy-MM-dd"/></td>
    </tr>
    <tr>
        <td colspan="4" class="button" align="right">
            <s:hidden name="searchDateRangeFlag" value=""/>
            <s:submit name="refresh" value="%{getText('refresh.label')}"/>
        </td>
    </tr>
    </tbody>
</table>
<div id="birth-register-approval-body">
    <%--todo permission handling--%>

    <fieldset style="margin-bottom:10px;margin-top:20px;border:none">
        <legend></legend>
        <table id="approval-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
            <thead>
            <tr>
                <th><s:label name="serial" value="%{getText('serial.label')}"/></th>
                <th><s:label name="name" value="%{getText('name.label')}"/></th>
                <%-- <th><s:label name="received" value="%{getText('received.label')}"/></th>--%>
                <th><s:label name="edit" value="%{getText('edit.label')}"/></th>
                <th><s:label name="approve" value="%{getText('approve.label')}"/></th>
                <th><s:label name="reject" value="%{getText('reject.label')}"/></th>
                <th><s:label name="delete" value="%{getText('delete.label')}"/></th>
                <th><s:label name="delete" value="%{getText('view.label')}"/></th>
                <th><s:label name="delete" value="%{getText('print.label')}"/></th>
            </tr>
            </thead>

            <tbody>
            <s:iterator status="approvalStatus" value="adoptionPendingApprovalList" id="approvalList">
                <tr>
                    <td><s:property value="courtOrderNumber"/></td>
                    <s:if test="childExistingName!=null">
                        <td><s:property value="childExistingName"/></td>
                    </s:if>
                    <s:else>
                        <td><s:property value="childNewName"/></td>
                    </s:else>
                    <s:if test="status.ordinal()==0">
                        <s:if test="#request.allowEditAdoption">
                            <s:url id="editSelected" action="eprAdoptionEditMode.do">
                                <s:param name="idUKey" value="idUKey"/>
                            </s:url>
                            <td align="center"><s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                                <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                     border="none"/></s:a>
                            </td>
                        </s:if>
                        <s:if test="#request.allowApproveAdoption">
                            <s:url id="approveSelected" action="eprApproveAdoption.do">
                                <s:param name="idUKey" value="idUKey"/>
                            </s:url>
                            <td align="center"><s:a href="%{approveSelected}"
                                                    title="%{getText('approveTooltip.label')}">
                                <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                     border="none"/></s:a>
                            </td>
                        </s:if>
                        <s:if test="#request.allowApproveAdoption">
                            <s:url id="rejectSelected" action="eprRejectAdoption.do">
                                <s:param name="idUKey" value="idUKey"/>
                            </s:url>
                            <td align="center"><s:a href="%{rejectSelected}"
                                                    title="%{getText('rejectTooltip.label')}"><img
                                    src="<s:url value='/images/reject.gif'/>" width="25" height="25"
                                    border="none"/></s:a>
                            </td>
                        </s:if>
                        <s:if test="#request.allowApproveAdoption">
                            <s:url id="deleteSelected" action="eprDeleteAdoption.do">
                                <s:param name="idUKey" value="idUKey"/>
                            </s:url>
                            <td align="center"><s:a href="%{deleteSelected}"
                                                    title="%{getText('deleteToolTip.label')}"><img
                                    src="<s:url value='/images/delete.gif'/>" width="25" height="25"
                                    border="none"/></s:a>
                            </td>
                        </s:if>
                        <td></td>
                        <td></td>
                    </s:if>

                    <s:elseif test="status.ordinal()==1">
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <s:url id="viewSelected" action="eprAdoptionViewMode.do">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>
                        <td align="center"><s:a href="%{viewSelected}" title="%{getText('viewAdoptionRegistrationTooltip.label')}">
                            <img src="<s:url value='/images/view.gif'/>" width="25" height="25"
                                 border="none"/></s:a>
                        </td>
                        <td>
                            <s:url id="cetificatePrintUrl" action="">
                                <s:param name="idUKey" value="idUKey"/>
                            </s:url>
                            <s:a href="%{cetificatePrintUrl}" title="%{getText('printAdoptionRegistrationTooltip.label')}">
                                <img src="<s:url value='/images/print_icon.gif'/>" border="none" width="25"
                                     height="25"/>
                            </s:a>
                        </td>
                    </s:elseif>

                    <s:elseif test="status.ordinal()==2">
                        <s:url id="viewSelected" action="">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td align="center"><s:a href="%{viewSelected}" title="%{getText('viewAdoptionRegistrationTooltip.label')}">
                            <img src="<s:url value='/images/view.gif'/>" width="25" height="25"
                                 border="none"/></s:a>
                        </td>
                        <td>
                            <s:url id="cetificatePrintUrl" action="">
                                <s:param name="idUKey" value="idUKey"/>
                            </s:url>
                            <s:a href="%{cetificatePrintUrl}" title="%{getText('printAdoptionRegistrationTooltip.label')}">
                                <img src="<s:url value='/images/print_icon.gif'/>" border="none" width="25"
                                     height="25"/>
                            </s:a>
                        </td>
                    </s:elseif>

                    <s:elseif test="status.ordinal()==4">
                        <s:url id="viewSelected" action="">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td align="center"><s:a href="%{viewSelected}" title="%{getText('viewAdoptionCertificateTooltip.label')}">
                            <img src="<s:url value='/images/view.gif'/>" width="25" height="25"
                                 border="none"/></s:a>
                        </td>
                        <td>
                            <s:url id="cetificatePrintUrl" action="">
                                <s:param name="idUKey" value="idUKey"/>
                            </s:url>
                            <s:a href="%{cetificatePrintUrl}" title="%{getText('printAdoptionCertificateToolTip.label')}">
                                <img src="<s:url value='/images/print_icon.gif'/>" border="none" width="25"
                                     height="25"/>
                            </s:a>
                        </td>
                    </s:elseif>

                    <s:elseif test="status.ordinal()==5">
                        <s:url id="viewSelected" action="">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td align="center"><s:a href="%{viewSelected}" title="%{getText('viewAdoptionCertificateTooltip.label')}">
                            <img src="<s:url value='/images/view.gif'/>" width="25" height="25"
                                 border="none"/></s:a>
                        </td>
                        <td>
                            <s:url id="cetificatePrintUrl" action="">
                                <s:param name="idUKey" value="idUKey"/>
                            </s:url>
                            <s:a href="%{cetificatePrintUrl}" title="%{getText('viewAdoptionCertificateTooltip.label')}">
                                <img src="<s:url value='/images/print_icon.gif'/>" border="none" width="25"
                                     height="25"/>
                            </s:a>
                        </td>
                    </s:elseif>
                </tr>
            </s:iterator>

            </tbody>
        </table>
    </fieldset>
</div>