<%@ page import="lk.rgd.common.api.domain.User" %>
<%@ page import="lk.rgd.crs.api.domain.BDDivision" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<script>
    $(document).ready(function() {
        $("#tabs").tabs();
    });
</script>

<script type="text/javascript">
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
        $("#endDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
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


    // mode 1 = passing District, will return DS list
    // mode 2 = passing DsDivision, will return BD list
    // any other = passing district, will return DS list and the BD list for the first DS
    $(function() {
        $('select#districtId').bind('change', function(evt1) {
            var id = $("select#districtId").attr("value");
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
                        $("select#divisionId").html(options2);
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
                        $("select#divisionId").html(options);
                    });
        });
    });

    function initPage() {
    }

</script>
<%
    User user = (User) session.getAttribute("user_bean");
%>
<s:form method="post" action="eprApproveDeathAlterations.do">

    <div id="tabs" style="font-size:10pt;">
        <ul>
            <li><a href="#fragment-1"><span> <s:label
                    value="%{getText('lable.search.by.dsDivision')}"/></span></a></li>
            <li><a href="#fragment-2"><span><s:label
                    value="%{getText('label.search.by.location')}"/></span></a></li>
            <li><a href="#fragment-3"><span><s:label
                    value="%{getText('label.search.by.death.person.pin')}"/></span></a></li>
        </ul>

        <div id="fragment-1">
            <table>
                <caption></caption>
                <col width="250px">
                <col width="250px">
                <col width="100px">
                <col width="250px">
                <col width="250px">
                <tbody>
                <tr>
                    <td>
                        <s:label value="%{getText('label.district')}"/>
                    </td>
                    <td align="left">
                        <s:select id="districtId" name="districtUKey" list="districtList" value="%{districtUKey}"
                                  cssStyle="width:98.5%; width:240px;"/>
                    </td>
                    <td></td>
                    <td>
                        <s:label value="%{getText('label.dsDivision')}"/>
                    </td>
                    <td align="left">
                        <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{divisionUKey}"
                                  cssStyle="float:left;  width:240px;"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:label value="%{getText('label.bdDivision')}"/>
                    </td>
                    <td>
                        <s:select id="divisionId" name="divisionUKey" value="%{divisionUKey}"
                                  list="bdDivisionList"
                                  cssStyle="float:left;  width:240px;"/>
                    </td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                </tbody>
            </table>

        </div>
        <div id="fragment-2">
            <table>
                <caption></caption>
                <col width="250px">
                <col width="250px">
                <col width="100px">
                <col width="250px">
                <col width="250px">
                <tbody>
                <tr>
                    <td><s:label value="%{getText('label.location')}"/></td>
                    <td align="left">
                        <s:select list="userLocations" name="locationUKey" value="%{locationUKey}" headerKey="0"
                                  headerValue="%{getText('select.location')}"/>
                    </td>
                    <td>

                    </td>
                    <td align="left"></td>
                    <td>
                    </td>
                </tr>
                </tbody>
            </table>

        </div>
        <div id="fragment-3">
            <table>
                <caption></caption>
                <col width="250px">
                <col width="250px">
                <col width="100px">
                <col width="250px">
                <col width="250px">
                <tbody>
                <tr>
                    <td><s:label value="%{getText('label.death.person.pin')}"/></td>
                    <td align="left">
                        <s:textfield name="pin" maxLength="10" value=""/>
                    </td>
                    <td>

                    </td>
                    <td align="left"></td>
                    <td>
                    </td>
                </tr>
                </tbody>
            </table>

        </div>
    </div>
    <div id="search_button" class="button" align="right">
        <s:submit name="refresh" value="%{getText('label.button.filter')}"/>
    </div>
    <s:hidden name="pageNumber" value="1"/>
</s:form>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
<div id="cc">
    <s:if test="approvalList.size()>0">
        <fieldset>
            <table id="approval-list-table" width="100%" cellpadding="0" cellspacing="0" class="display"
                   style="font-size:10pt;">
                <thead>
                <tr><%--
                    <th width="20px"><s:label value="%{getText('alt.serial.number')}"/></th>
                    <th width="650px"><s:label value="%{getText('name.label')}"/></th>
                    <th width="100px"><s:label value="%{getText('edit.label')}"/></th>
                    <th width="100px"><s:label value="%{getText('delete.label')}"/></th>
                    <th width="100px"><s:label value="%{getText('reject.label')}"/></th>
                    <th width="100px"><s:label value="%{getText('approve.label')}"/></th>
                    <th width="100px"><s:label value="%{getText('print.label')}"/></th>--%>

                    <th width="100px"><s:label value="%{getText('alt.serial.number')}"/></th>
                    <th width="870px"><s:label value="%{getText('name.label')}"/></th>
                    <th width="40px"></th>
                    <th width="40px"></th>
                    <th width="40px"></th>
                    <th width="40px"></th>
                    <th width="40px"></th>
                </tr>
                </thead>
                <tbody>
                <s:iterator value="approvalList">
                    <%--todo remove--%>
                    <s:if test="status.ordinal()>-1">
                        <s:url id="editSelected" action="eprDeathAlterationEditInit">
                            <s:param name="deathAlterationId" value="idUKey"/>
                        </s:url>
                        <s:url id="deleteSelected" action="eprDeathAlterationDelete">
                            <s:param name="deathAlterationId" value="idUKey"/>
                        </s:url>
                        <s:url id="rejectSelected" action="eprDeathAlterationReject">
                            <s:param name="deathAlterationId" value="idUKey"/>
                            <s:param name="pageNo" value="%{#request.pageNo}"/>
                        </s:url>
                        <s:url id="approveSelected" action="eprApproveDeathAlterationsDirect">
                            <s:param name="deathAlterationId" value="idUKey"/>
                        </s:url>
                        <s:url id="printSelected" action="eprDeathAlterationPrintLetter">
                            <s:param name="deathAlterationId" value="idUKey"/>
                        </s:url>

                        <tr>

                            <td><s:property value="idUKey"/></td>
                            <td><s:property value="deathPersonName"/></td>
                            <td align="center">
                                <s:if test="status.ordinal()==0">
                                    <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                                        <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                             border="none"/></s:a>
                                </s:if>
                            </td>

                            <td align="center">
                                <s:if test="status.ordinal()==0">
                                    <s:a href="%{deleteSelected}"
                                         title="%{getText('deleteToolTip.label')}"><img
                                            src="<s:url value='/images/delete.gif'/>" width="25" height="25"
                                            border="none"/></s:a>
                                </s:if>
                            </td>
                            <td align="center">
                                <s:set name="deathDivision" value="deathRecordDivision"/>
                                <s:if test="status.ordinal()<2 & (#session.user_bean.role.roleId.equals('ARG') | #session.user_bean.role.roleId.equals('RG'))">
                                    <%
                                        BDDivision deathDivision = (BDDivision) pageContext.getAttribute("deathDivision");
                                        int deathDSDivsion = deathDivision.getDsDivision().getDsDivisionUKey();
                                        boolean approveRights = user.isAllowedAccessToBDDSDivision(deathDSDivsion);
                                        if (approveRights) {
                                    %>
                                    <s:a href="%{rejectSelected}" title="%{getText('rejectTooltip.label')}">
                                        <img src="<s:url value='/images/reject.gif'/>" width="25" height="25"
                                             border="none"/>
                                    </s:a>
                                    <%}%>

                                </s:if>
                            </td>
                            <td align="center">
                                <s:set name="deathDivision" value="deathRecordDivision"/>
                                <s:if test="status.ordinal()<2 & (#session.user_bean.role.roleId.equals('ARG') | #session.user_bean.role.roleId.equals('RG'))">
                                    <% BDDivision deathDivision = (BDDivision) pageContext.getAttribute("deathDivision");
                                        int deathDSDivsion = deathDivision.getDsDivision().getDsDivisionUKey();
                                        boolean approveRights = user.isAllowedAccessToBDDSDivision(deathDSDivsion);
                                        if (true) {
                                    %>
                                    <s:a href="%{approveSelected}" title="%{getText('approveTooltip.label')}">
                                        <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                             border="none"/></s:a>
                                    <%}%>
                                </s:if>
                            </td>
                            <td align="center">
                                <s:if test="status.ordinal()== 2">
                                    <s:a href="%{printSelected}" title="%{getText('printConfirmation.label')}">
                                        <img src="<s:url value='/images/print_icon.gif'/>" width="25" height="25"
                                             border="none"/></s:a>
                                </s:if>
                                <s:elseif test="status.ordinal()==3">
                                    <s:a href="%{printSelected}" title="%{getText('printConfirmation.reprint.label')}">
                                        <img src="<s:url value='/images/print_icon.gif'/>" width="25" height="25"
                                             border="none"/></s:a>
                                </s:elseif>
                            </td>
                        </tr>
                    </s:if>
                </s:iterator>
                </tbody>
            </table>
        </fieldset>
    </s:if>
</div>