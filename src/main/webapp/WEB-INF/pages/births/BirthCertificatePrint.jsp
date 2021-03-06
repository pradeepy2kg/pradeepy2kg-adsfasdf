<%@ page import="lk.rgd.common.util.NameFormatUtil" %>
<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script>
    $(document).ready(function() {
        $('#print-list-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "aaSorting": [
                [3,'desc']
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

    function initPage() {
    }
</script>
<script type="text/javascript" src="<s:url value="/js/selectAll.js"/>"></script>
<div id="birth-certificate-print">
    <div id="birth-certificate-print-header">

        <s:form action="eprFilterBirthCetificateList.do" name="birth_register_approval_head" method="POST"
                id="birth-certificate-print-form">
        <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
            <legend><b><s:label value="%{getText('searchOption.label')}"/></b></legend>
            <table width="100%" cellpadding="5" cellspacing="0">
                <col width="200px"/>
                <col/>
                <col width="200px"/>
                <col width="100px"/>
                <tbody>
                <tr>
                    <td><s:label name="district" value="%{getText('district.label')}"/></td>
                    <td>
                        <s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                                  value="birthDistrictId" cssStyle="width:240px;"/>

                    </td>
                    <td align="right">
                        <s:radio list="#@java.util.HashMap@{'false':getText('not_printed.label')}" name="printed"
                                 value="false"/>
                    </td>
                    <td align="right">
                        <s:radio list="#@java.util.HashMap@{'true':getText('printed.label')}" name="printed"/>
                    </td>
                </tr>
                <tr>
                    <td><s:label name="division" value="%{getText('select_DS_division.label')}"/></td>
                    <td>
                        <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                                  cssStyle="float:left;  width:240px;"/></td>
                    <td align="right">
                        <s:label name="bdDivision" value="%{getText('select_BD_division.label')}"/>
                    </td>
                    <td>
                        <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}"
                                  list="bdDivisionList" headerValue="%{getText('all.divisions.label')}" headerKey="0"
                                  cssStyle=" width:240px;float:right;"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" class="button" align="right">
                        <s:submit value="%{getText('bdfSearch.button')}"></s:submit>
                    </td>
                </tr>
                </tbody>
            </table>
        </fieldset>
    </div>
    </s:form>
    <s:form action="eprBirthCertificateBulkPrint" name="birth_confirm_print">
    <div id="birth-register-approval-body">
        <s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
        <s:actionerror cssStyle="color:red;font-size:10pt"/>
        <s:if test="printList.size > 0">
            <fieldset style="border:none">
                <table id="print-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
                    <thead>
                    <tr>
                            <%--todo remove
                           <th width="15px"></th>--%>
                        <th width="20px"><s:label value="%{getText('division.label')}"/></th>
                        <th width="70px"><s:label name="serial" value="%{getText('serial.label')}"/></th>
                        <th><s:label name="name" value="%{getText('name.label')}"/></th>
                        <th width="90px"><s:label name="registered_date"
                                                  value="%{getText('registered_date.label')}"/></th>
                        <th width="40px"><s:label name="live" value="%{getText('live.label')}"/></th>
                        <th width="20px"></th>
                    </tr>
                    </thead>
                    <tbody>
                        <%--following code used for pagination--%>
                    <s:iterator status="printStatus" value="printList" id="printListId">
                        <tr class="<s:if test="#printStatus.odd == true">odd</s:if><s:else>even</s:else>">
                                <%--<td class="table-row-index"><s:property value="%{#printStatus.count+printStart}"/></td>--%>
                                <%--                        todo remove
               <td><s:checkbox name="index"
                               onclick="javascript:selectall(document.birth_confirm_print,document.birth_confirm_print.allCheck)"
                               fieldValue="%{#printListId.idUKey}" value="%{#index}"/></td>--%>
                            <td><s:property value="register.birthDivision.divisionId"/></td>
                            <td align="center"><s:property value="register.bdfSerialNo"/></td>
                            <td>
                                <s:if test="child.childFullNameOfficialLang != null">
                                    <%= NameFormatUtil.getDisplayName((String) request.getAttribute("child.childFullNameOfficialLang"), 65)%>
                                </s:if>
                            </td>
                            <td align="center"><s:property value="register.dateOfRegistration"/></td>
                            <td align="center">
                                <s:if test="register.birthType.ordinal() != 0">
                                    <s:label value="%{getText('yes.label')}"/>
                                </s:if>
                                <s:else>
                                    <s:label value="%{getText('no.label')}"/>
                                </s:else>
                            </td>
                            <td align="center">
                                <s:url id="cetificatePrintUrl" action="eprBirthCertificate">
                                    <s:param name="bdId" value="idUKey"/>
                                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                                    <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                                    <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                                    <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                                    <s:param name="printed" value="#request.printed"/>
                                    <s:param name="printStart" value="#request.printStart"/>
                                </s:url>
                                <s:if test="#request.allowPrintCertificate">
                                    <s:if test="((register.status.ordinal() == 8 & (!#session.user_bean.role.roleId.equals('DEO'))))">
                                        <s:a href="%{cetificatePrintUrl}" title="%{getText('print.label')}">
                                            <img src="<s:url value='/images/print_icon.gif'/>" border="none" width="25"
                                                 height="25"/>
                                        </s:a>
                                    </s:if>
                                    <s:elseif test="register.status.ordinal() == 9">
                                        <s:a href="%{cetificatePrintUrl}" title="%{getText('reprint.label')}">
                                            <img src="<s:url value='/images/print_icon.gif'/>" border="none" width="25"
                                                 height="25"/>
                                        </s:a>
                                    </s:elseif>
                                </s:if>
                            </td>
                        </tr>
                    </s:iterator>
                    </tbody>
                </table>
            </fieldset>
            <%--        todo remove
            <div class="form-submit"><s:if test="#request.allowPrintCertificate">
                <s:label><s:checkbox name="allCheck"
                                     onclick="javascript:selectallMe(document.birth_confirm_print,document.birth_confirm_print.allCheck)"/>
                    <span><s:label name="select_all"
                                   value="%{getText('select_all.label')}"/></span></s:label> &nbsp;&nbsp;&nbsp;&nbsp;
                <s:label><span><s:label name="print_selected" value="%{getText('print_selected.label')}"/></span>
                    <s:hidden name="pageNo" value="%{#request.pageNo}"/>
                    <s:hidden name="birthDistrictId" value="%{#request.birthDistrictId}"/>
                    <s:hidden name="birthDivisionId" value="%{#request.birthDivisionId}"/>
                    <s:hidden name="printed" value="%{#request.printed}"/>
                    <s:hidden name="dsDivisionId" value="%{#request.dsDivisionId}"/>
                    <s:submit value="%{getText('print.label')}"/></s:label>
            </s:if>
            </div>--%>
            <div class="next-previous">
                <s:url id="previousUrl" action="eprCertificatePrintPrevious.do">
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                    <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                    <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                    <s:param name="printed" value="#request.printed"/>
                    <s:param name="printStart" value="#request.printStart"/>
                </s:url>
                <s:url id="nextUrl" action="eprCertificatePrintNext.do">
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                    <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                    <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                    <s:param name="printed" value="#request.printed"/>
                    <s:param name="printStart" value="#request.printStart"/>
                </s:url>
                <s:if test="printStart!=0 & printStart>0">
                    <s:a href="%{previousUrl}">
                        <img src="<s:url value='/images/previous.gif'/>" border="none"/>
                    </s:a>
                    <s:label value="%{getText('previous.label')}"/>
                </s:if>
                <s:if test="printList.size >=50">
                    <s:a href="%{nextUrl}" cssStyle="text-decoration:none;color:black;">
                        <img src="<s:url value='/images/next.gif'/>" border="none"/>
                        <s:label value="%{getText('next.label')}"/>
                    </s:a>
                </s:if>
            </div>
        </s:if>
        </s:form>
    </div>
</div>
