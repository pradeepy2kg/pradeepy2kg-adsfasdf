<%@ page import="lk.rgd.common.util.NameFormatUtil" %>
<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="../js/validate.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<script type="text/javascript">
    function initPage() {
    }

    $(function() {
        $("#person-approve-list").dataTable({
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
</script>

<div id="birth-register-approval">
    <s:form action="eprPersonApproval.do" method="POST">
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
                    <td><s:label value="%{getText('registrationLocation.label')}"/></td>
                    <td>
                        <s:select name="locationId" list="locationList"/>
                    </td>
                    <td></td>
                    <td></td>
                    <td class="button" align="right">
                        <s:hidden name="searchDateRangeFlag" value="%{#request.searchDateRangeFlag}"/>
                        <s:submit name="refresh" value="%{getText('bdfSearch.button')}"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </fieldset>
    </s:form>

    <s:actionmessage cssClass="alreadyPrinted"/>
    <s:if test="approvalPendingList.size > 0">
        <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
            <legend><b><s:label value="%{getText('searchResult.label')}"/></b></legend>
            <table id="person-approve-list" width="100%" cellpadding="0" cellspacing="0" class="display">
                <thead>
                <tr>
                    <th width="60px"><s:label value="%{getText('locationCode.label')}"/></th>
                    <th width="110px">NIC</th>
                    <th width="780px"><s:label value="%{getText('label.personName')}"/></th>
                    <th width="20px"></th>
                    <th width="20px"></th>
                    <th width="20px"></th>
                    <th width="20px"></th>
                </tr>
                </thead>
                <tbody>
                <s:iterator status="approvalStatus" value="approvalPendingList">
                    <tr>
                        <td><s:property value="submittedLocation.locationCode"/></td>
                        <td align="center"><s:property value="nic"/></td>
                        <td>
                            <s:if test="fullNameInOfficialLanguage != null">
                                <%= NameFormatUtil.getDisplayName((String) request.getAttribute("fullNameInOfficialLanguage"), 70)%>
                            </s:if>
                        </td>
                        <td align="center">
                            <s:url id="editSelected" action="eprEditPerson.do">
                                <s:param name="personUKey" value="personUKey"/>
                            </s:url>
                            <s:a href="%{editSelected}" title="%{getText('editToolTip.label')}">
                                <img src="<s:url value='/images/edit.png'/>" width="25" height="25" border="none"/>
                            </s:a>
                        </td>
                        <td align="center">
                            <s:a href="%{approveSelected}" title="%{getText('approveToolTip.label')}">
                                <img src="<s:url value='/images/approve.gif'/>" width="25" height="25" border="none"/>
                            </s:a>
                        </td>
                        <td align="center">
                            <s:a href="%{rejectSelected}" title="%{getText('rejectToolTip.label')}">
                                <img src="<s:url value='/images/reject.gif'/>" width="25" height="25" border="none"/>
                            </s:a>
                        </td>
                        <td align="center">
                            <s:a href="%{deleteSelected}" title="%{getText('deleteToolTip.label')}">
                                <img src="<s:url value='/images/delete.gif'/>" width="25" height="25" border="none"/>
                            </s:a>
                        </td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
        </fieldset>
    </s:if>
</div>