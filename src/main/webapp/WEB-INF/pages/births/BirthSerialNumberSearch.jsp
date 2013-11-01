<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="<s:url value="/js/common.js"/>"></script>


<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
    #adoption-search-outer {
        width: 100%;
    }

    @media print {
        #report thead {
            background: #DDD;
        }
    }
</style>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>

<script type="text/javascript">

    $(function () {
        $('#searchList').dataTable({
            "bPaginate":true,
            "bLengthChange":false,
            "bFilter":true,
            "aaSorting":[
                [0, 'desc']
            ],
            "bInfo":false,
            "bAutoWidth":false,
            "bJQueryUI":true,
            "sPaginationType":"full_numbers"
        });

        $("#dataEntryPeriodFrom").datepicker({
            changeYear:true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2030-12-31'
        });

        $("#dataEntryPeriodTo").datepicker({
            changeYear:true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2030-12-31'
        });

        /*
         $("#dataEntryPeriodFrom").change(function() {
         $("#dataEntryPeriodFromReport").val($(this).val());
         });

         $("#dataEntryPeriodTo").change(function() {
         $("#dataEntryPeriodToReport").val($(this).val());
         });
         */
    });

    function validateDate1() {
        var bStatus = true;

        var date1 = $("#dataEntryPeriodFrom").val();
        var date2 = $("#dataEntryPeriodTo").val();

        if(!date1 || 0 === date1.length){
            alert("Please enter a date range.");
            return false;
        }

        if(!date2 || 0 === date2.length){
            alert("Please enter a date range.");
            return false;
        }

        if (date2 < date1) {
            alert($("#date-comparison").val())
            bStatus = false;
        }
        return bStatus;
    }

</script>

<div id="serial-number-search-outer">

    <s:if test="hasActionErrors()">
        <s:div id="message-box" class="message-box">
            <div class="actionerror">
                    &lt;%&ndash;<s:label value="%{getText('error_occurred.label')}"/>&ndash;%&gt;
                <s:actionerror/>
            </div>
        </s:div>
    </s:if>


    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <%--<legend><s:label value="%{getText('adoption.reports.label')}"/></legend>--%>
        <s:form action="generateBirthSerialNumberReport" method="POST" name="generateAdoptionReports"
                onsubmit="javascript:return validateDate1()">
            <table width="100%">

                <tr>
                    <td><s:label name="district" value="%{getText('district.label')}"/></td>
                    <td>
                        <s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                                   cssStyle="width:240px;float:left;"/>
                    </td>
                    <td><s:label name="division" value="%{getText('select_DS_division.label')}"/></td>
                    <td>
                        <s:select id="dsDivisionId" name="dsDivisionId" list="allDSDivisionList" value="%{dsDivisionId}"
                              cssStyle="float:left;  width:240px;"/>
                    </td>
                </tr>

                <tr>
                    <td>
                        <s:label value="%{getText('data.entry.period.label')}"/>
                    </td>
                    <td>
                        <s:label value="%{getText('from.label')}"/>
                        <s:textfield id="dataEntryPeriodFrom" name="dataEntryPeriodFrom" cssClass="width80"
                                     maxLength="10"/> &nbsp&nbsp
                        <s:label value="%{getText('to.label')}"/>
                        <s:textfield id="dataEntryPeriodTo" name="dataEntryPeriodTo" cssClass="width80" maxLength="10"/>
                    </td>
                    <td>
                    </td>
                    <td class="form-submit" align="right">
                          <s:submit value="%{getText('search1.label')}" cssStyle="float: right;"/>
                    </td>
                </tr>

                <%--
                <tr>
                    <td colspan="3"></td>
                    <td class="form-submit" align="right">
                        <s:submit value="%{getText('search.adoption.label')}" cssStyle="float: right;"/>
                    </td>

                </tr>--%>


            </table>
        </s:form>
        <br/>
    </fieldset>

</div>


<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
    <div class="form-submit">
        <s:submit type="button" value="%{getText('print.button')}" onclick="printPageById('adoption-search-report')"/>
    </div>
    <div id="adoption-search-report">
        <div align="center">
            <h2><s:label value="%{getText('system.genarated.serial.number.report.label')}"/></h2>
        </div>

        <table id="reportHeader" width="700px" border="0">
            <tr>
                <td>
                    <s:label value="%{getText('select_DS_division.label')}"/>
                </td>
                <td colspan="4">
                     <s:label value="%{dsDivisionName}"/>
                </td>
            </tr>

            <tr>
                <td>
                    <s:label value="%{getText('data.entry.period.label')}"/>
                </td>
                <td width="50px"><s:label value="%{#request.dataEntryPeriodFrom}"/></td>
                <td><s:label value="%{getText('from.label')}"/></td>
                <td width="50px"><s:label value="%{#request.dataEntryPeriodTo}"/></td>
                <td><s:label value="%{getText('to.label')}"/></td>
            </tr>

        </table>
        <br/>
        <br/>
        <table id="report" width="99%" border="1" cellpadding="2" cellspacing="0"
               style="border: 1px solid #000; border-collapse: collapse;">
            <thead>
            <tr style="background: #DDD;">
                <th width="8%"><s:label value="%{getText('registration.division.label')}"/></th>
                <th width="12%"><s:label value="%{getText('serial.number.range.label')}"/></th>
                <th width="12%"><s:label value="%{getText('certificate.number.range.label')}"/></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator id="searchResults" value="searchResults" status="searchStatus">
                <tr style="background:#E9F2F7">
                    <td height="35px" >
                        <s:property value="bdDivisionName"/>
                    </td>
                    <td height="35px">
                        <s:property value="serialNumberRange"/>
                    </td>
                    <td height="35px">

                    </td>
                </tr>
            </s:iterator>

            </tbody>
        </table>
    </div>
</fieldset>

<s:hidden id="date-comparison" value="%{getText('date.comparison.validation.label')}"> </s:hidden>

