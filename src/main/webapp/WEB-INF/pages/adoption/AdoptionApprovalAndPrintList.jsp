<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="../js/validate.js"></script>
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
    });

</script>
<script type="text/javascript" src="<s:url value="/js/selectAll.js"/>"></script>

<table cellpadding="5" cellspacing="0">
    <s:form action="eprAdoptionFilterByStatus" method="post">

        <tbody>
        <tr>
            <td><s:label value="%{getText('select.status.label')}"/></td>
            <td>
<%--                <s:select list="#@java.util.HashMap@{'1':getText('data.entry'),'2':getText('Approved.label'),
                '3':getText('order.details.printed.label'),'4':getText('notice.printed.label'),
                '5':getText('rejected.label'),'6':getText('certificate.issual.request.captured.label'),
                '7':getText('adoption.certificate.printed.label')}"
                          name="currentStatus" value="%{#request.currentStatus}" headerKey="0"
                          headerValue="%{getText('select.status.label')}"
                          cssStyle="width:250px; margin-left:5px;"/>--%>
                    <s:select list="#@java.util.HashMap@{'1':getText('data.entry'),'2':getText('Approved.label'),
                '3':getText('order.details.printed.label'),'4':getText('notice.printed.label'),
                '6':getText('certificate.issual.request.captured.label'),
                '7':getText('adoption.certificate.printed.label')}"
                          name="currentStatus" value="%{#request.currentStatus}" headerKey="0"
                          headerValue="%{getText('select.status.label')}"
                          cssStyle="width:250px; margin-left:5px;"/>
            </td>
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
                <%--    <th><s:label name="serial" value="%{getText('adoption_serial.label')}"/></th>
              <th><s:label name="name" value="%{getText('name.label')}"/></th>
              <th><s:label name="edit" value="%{getText('edit.label')}"/></th>
              <th><s:label name="approve" value="%{getText('approve.label')}"/></th>
              <th><s:label name="reject" value="%{getText('reject.label')}"/></th>
              <th><s:label name="delete" value="%{getText('delete.label')}"/></th>
              <th><s:label name="delete" value="%{getText('view.label')}"/></th>
              <th><s:label name="delete" value="%{getText('printNotice.label')}"/></th>
              <th><s:label name="delete" value="%{getText('printCertificete.label')}"/></th>
              <th><s:label name="delete" value="%{getText('ReRegistration.label')}"/></th>--%>

                <th width="80px"><s:label name="serial" value="%{getText('adoption_serial.label')}"/></th>
                <th width="100px"><s:label name="serial" value="%{getText('entry_no.label')}"/></th>
                <th width="800px"><s:label name="name" value="%{getText('name.label')}"/></th>
                <th></th>
                <th></th>
                <th></th>
                <%--<th></th>--%>
                <%--<th></th>--%>
                <th></th>
                <th><s:label name="delete" value="%{getText('ReRegistration.label')}"/></th>
                <th></th>
            </tr>
            </thead>

            <tbody>
            <s:iterator status="approvalStatus" value="adoptionApprovalAndPrintList" id="approvalList">
                <s:url id="editSelected" action="eprAdoptionEditMode.do">
                    <s:param name="idUKey" value="idUKey"/>
                    <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                </s:url>

                <%--<s:url id="approveSelected" action="eprApproveAdoption.do">
                    <s:param name="idUKey" value="idUKey"/>
                    <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                </s:url>

                <s:url id="rejectSelected" action="eprRejectAdoption.do">
                    <s:param name="idUKey" value="idUKey"/>
                    <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                </s:url>--%>

                <s:url id="deleteSelected" action="eprDeleteAdoption.do">
                    <s:param name="idUKey" value="idUKey"/>
                    <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                </s:url>

                <s:url id="viewSelected" action="eprAdoptionViewMode.do">
                    <s:param name="from" value="1"/>
                    <s:param name="idUKey" value="idUKey"/>
                    <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                </s:url>

                <s:url id="cetificatePrintUrl" action="eprPrintAdoptionNotice.do">
                    <s:param name="idUKey" value="idUKey"/>
                    <s:param name="certificateflag" value="false"/>
                    <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                </s:url>
                <tr>
                    <td>
                        <s:if test="adoptionSerialNo > 0"><s:property value="adoptionSerialNo"/></s:if>
                        <s:else> - </s:else>
                    </td>
                    <td><s:property value="adoptionEntryNo"/></td>
                    <s:if test="childNewName!=null">
                        <td><s:property value="getChildNewNameToLength(30)"/></td>

                    </s:if>
                    <s:elseif test="childExistingName!=null">
                        <td><s:property value="getChildExistingNameToLength(30)"/></td>
                    </s:elseif>
                    <td align="center">
                        <s:if test="%{allowEditAdoption && status.ordinal()==0 }">
                            <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                                <img id="editImage" src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                     border="none"/>
                            </s:a>
                        </s:if>
                    </td>

                    <%--<td align="center">
                        <s:if test="%{allowApproveAdoption && allowEditAdoption && status.ordinal()==0}">
                            <s:a href="%{approveSelected}" title="%{getText('approveTooltip.label')}">
                                <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                     border="none" id="approveImage"/>
                            </s:a>
                        </s:if>
                    </td>

                    <td align="center">
                        <s:if test="%{allowApproveAdoption && allowEditAdoption && status.ordinal()==0}">
                            <s:a href="%{rejectSelected}" title="%{getText('rejectTooltip.label')}">
                                <img id="rejectImage" src="<s:url value='/images/reject.gif'/>" width="25" height="25"
                                     border="none"/>
                            </s:a>
                        </s:if>
                    </td>--%>

                    <td align="center">
                        <s:a href="%{viewSelected}" title="%{getText('viewAdoptionRegistrationTooltip.label')}">
                            <img id='viewImage' src="<s:url value='/images/view.gif'/>" width="25" height="25"
                                 border="none"/>
                        </s:a>
                    </td>
                    <td align="center">
                        <s:if test=" (status.ordinal() ==3 ||status.ordinal() ==2 ||status.ordinal() ==1)  && allowEditAdoption">
                            <s:if test="status.ordinal() ==3">
                                <s:a href="%{cetificatePrintUrl}"
                                     title="%{getText('reprintAdoptionRegistrationTooltip.label')}">
                                    <img id="printImage" src="<s:url value='/images/print_icon.gif'/>"
                                         border="none" width="25" height="25"/>
                                </s:a>
                            </s:if>
                            <s:elseif test="status.ordinal() ==1 || status.ordinal() == 2">
                                <s:a href="%{cetificatePrintUrl}"
                                     title="%{getText('printAdoptionRegistrationTooltip.label')}">
                                    <img id="printImage" src="<s:url value='/images/print_icon.gif'/>"
                                         border="none" width="25" height="25"/>
                                </s:a>
                            </s:elseif>
                        </s:if>
                    </td>
                    <td align="center">
                        <s:url id="cetificatePrintUrl" action="eprPrintAdoptionCertificate.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="alreadyPrinted" value="true"/>
                            <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                            <s:param name="pageNo" value="%{#request.pageNo}"/>
                            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                        </s:url>
                        <s:if test="status.ordinal() >4 && allowEditAdoption">
                            <s:if test="status.ordinal() ==6">
                                <s:a href="%{cetificatePrintUrl}"
                                     title="%{getText('reprintAdoptionCertificateToolTip.label')}">
                                    <img src="<s:url value='/images/print_icon.gif'/>" border="none" width="25"
                                         height="25"/>
                                </s:a>
                            </s:if>
                            <s:else>
                                <s:a href="%{cetificatePrintUrl}"
                                     title="%{getText('printAdoptionCertificateToolTip.label')}">
                                    <img src="<s:url value='/images/print_icon.gif'/>" border="none" width="25"
                                         height="25"/>
                                </s:a>
                            </s:else>

                        </s:if>
                    </td>
                    <td align="center">
                        <s:url id="reRegisterBirthUrl" action="../births/eprAdoptionBirthRegistrationInit.do">
                            <s:param name="adoptionId" value="idUKey"/>
                        </s:url>
                        <s:if test="status.ordinal() ==6 && allowEditAdoption">
                            <s:a href="%{reRegisterBirthUrl}" title="%{getText('AdoptionReRegistrationTooltip.label')}">
                                <img src="<s:url value='/images/add_page.png'/>" border="none" width="25" height="25"/>
                            </s:a>
                        </s:if>
                    </td>

                    <td align="center">
                        <s:if test="%{allowEditAdoption && status.ordinal()==0}">
                            <s:a href="%{deleteSelected}" title="%{getText('deleteToolTip.label')}">
                                <img id='deleteImage' src="<s:url value='/images/delete.gif'/>" width="25" height="25"
                                     border="none" onclick="javascript:return deleteWarning('warning')"/>
                            </s:a>
                        </s:if>
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

<%--use to customize table base on user role--%>
<s:if test="!#request.allowEditAdoption">
    <script type="text/javascript">
        document.getElementById('editImage').style.display = 'none';
        document.getElementById('deleteImage').style.display = 'none';
    </script>
</s:if>
<s:if test="!#request.allowApproveAdoption">
    <script type="text/javascript">
        document.getElementById('approveImage').style.display = 'none';
        document.getElementById('rejectImage').style.display = 'none';
    </script>
</s:if>

<s:hidden id="warning" value="%{getText('adoptionDelete.warning.label')}"/>