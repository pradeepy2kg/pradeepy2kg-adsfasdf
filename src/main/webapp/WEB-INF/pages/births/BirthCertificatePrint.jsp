<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script>
    $(document).ready(function() {
        $('#print-list-table').dataTable({
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


<div id="birth-certificate-print">
    <s:url id="loadDSDivList" action="../ajaxSupport_loadDSDivListBCPrint"/>
    <div id="birth-certificate-print-header">

        <s:form action="eprFilterBirthCetificateList.do" name="birth_register_approval_head" method="POST"
                id="birth-certificate-print-form">
        <table width="100%" cellpadding="5" cellspacing="0">
            <col width="220px"/>
            <col/>
            <col width="160px"/>
            <col width="160px"/>
            <tbody>
            <tr>
                <td><s:label name="district" value="%{getText('district.label')}"/></td>
                <td>
                    <s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                              value="birthDistrictId" cssStyle="width:240px;"/>

                </td>
                <td align="right"><s:radio list="#@java.util.HashMap@{'false':getText('not_printed.label')}"
                                           name="printed"
                                           value="false"/></td>
                <td align="right"><s:radio list="#@java.util.HashMap@{'true':getText('printed.label')}"
                                           name="printed"/></td>
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
                <td colspan="4" class="button" align="right">
                    <s:submit value="%{getText('view.label')}"></s:submit>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    </s:form>
    <s:form action="eprBirthCertificateBulkPrint" name="birth_confirm_print">
    <div id="birth-register-approval-body">
        <s:actionerror/>
        <s:actionmessage/>
        <s:if test="printList.size==0 && printStart==0">
            <p class="alreadyPrinted"><s:label value="%{getText('noitemMsg.label')}"/></p>
        </s:if>
        <s:else>
            <fieldset style="border:none">
                <table id="print-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
                    <thead>
                    <tr>
                        <th></th>
                        <th width="30px"></th>
                        <th width="100px"><s:label name="serial" value="%{getText('serial.label')}"/></th>
                        <th><s:label name="name" value="%{getText('name.label')}"/></th>
                        <th width="110px"><s:label name="registered_date"
                                                   value="%{getText('registered_date.label')}"/></th>
                        <th width="50px"><s:label name="live" value="%{getText('live.label')}"/></th>
                        <th width="100px"><s:label value="%{getText('print.label')}"/></th>
                    </tr>
                    </thead>
                    <tbody>
                        <%--following code used for pagination--%>
                    <s:iterator status="printStatus" value="printList" id="printListId">
                        <tr class="<s:if test="#printStatus.odd == true">odd</s:if><s:else>even</s:else>">
                            <td class="table-row-index"><s:property value="%{#printStatus.count+printStart}"/></td>
                            <td><s:checkbox name="index"
                                            onclick="javascript:selectall(document.birth_confirm_print,document.birth_confirm_print.allCheck)"
                                            fieldValue="%{#printListId.idUKey}" value="%{#index}"/></td>
                            <td align="center"><s:property value="register.bdfSerialNo"/></td>
                            <td><s:property value="child.childFullNameOfficialLang"/></td>
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
                                    <s:param name="printed" value="#request.printed"/>
                                    <s:param name="printStart" value="#request.printStart"/>
                                </s:url>
                                <s:a href="%{cetificatePrintUrl}">
                                    <img src="<s:url value='/images/print_icon.gif'/>" border="none" width="25"
                                         height="25"/>
                                </s:a>
                            </td>
                        </tr>
                    </s:iterator>
                    </tbody>
                </table>
            </fieldset>
            <div class="form-submit">
                <s:label><s:checkbox name="allCheck"
                                     onclick="javascript:selectallMe(document.birth_confirm_print,document.birth_confirm_print.allCheck)"/>
                    <span><s:label name="select_all" value="%{getText('select_all.label')}"/></span></s:label> &nbsp;&nbsp;&nbsp;&nbsp;
                <s:label><span><s:label name="print_selected" value="%{getText('print_selected.label')}"/></span>
                    <s:hidden name="pageNo" value="%{#request.pageNo}"/>
                    <s:hidden name="birthDistrictId" value="%{#request.birthDistrictId}"/>
                    <s:hidden name="birthDivisionId" value="%{#request.birthDivisionId}"/>
                    <s:hidden name="printed" value="%{#request.printed}"/>
                    <s:submit value="%{getText('print.label')}"/></s:label>
            </div>
            <div class="next-previous">
                <s:url id="previousUrl" action="eprCertificatePrintPrevious.do">
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                    <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                    <s:param name="printed" value="#request.printed"/>
                    <s:param name="printStart" value="#request.printStart"/>
                </s:url>
                <s:url id="nextUrl" action="eprCertificatePrintNext.do">
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
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

                    <s:a href="%{nextUrl}">
                        <img src="<s:url value='/images/next.gif'/>" border="none"/>
                        <s:label value="%{getText('next.label')}"/>
                    </s:a>
                </s:if>
            </div>
        </s:else>
        </s:form>
    </div>
</div>
<%-- Styling Completed --%>