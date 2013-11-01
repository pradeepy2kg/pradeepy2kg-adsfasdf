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

        if (date2 < date1) {
            alert($("#date-comparison").val())
            bStatus = false;
        }
        return bStatus;
    }

</script>


<div id="adoption-search-outer">

    <s:if test="hasActionErrors()">
        <s:div id="message-box" class="message-box">
            <div class="actionerror">
                    <%--<s:label value="%{getText('error_occurred.label')}"/>--%>
                <s:actionerror/>
            </div>
        </s:div>
    </s:if>


    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <legend><s:label value="%{getText('adoption.reports.label')}"/></legend>
        <s:form action="eprAdoptionReportGeneration" method="POST" name="generateAdoptionReports"
                onsubmit="javascript:return validateDate1()">
            <table width="100%">
                <tr>
                    <td>
                        <s:label value="%{getText('court.label')}"/>
                    </td>
                    <td>
                        <s:select list="courtList" name="courtId" headerKey="0"
                                  headerValue="%{getText('select.label')}"/>
                    </td>
                    <td>
                        <!-- <s:label value="%{getText('adoption.jointapplicants.label')}"/> -->
                    </td>
                    <td>
                        <!--
                        <s:label value="%{getText('yes.label')}"/>&nbsp&nbsp
                        <s:radio id="jointApplicant" name="jointApplicant" list="#@java.util.HashMap@{'true':''}" /> &nbsp&nbsp
                        <s:label value="%{getText('no.label')}"/>&nbsp&nbsp
                        <s:radio id="jointApplicant" name="jointApplicant" list="#@java.util.HashMap@{'false':''}" />

                        -->
                    </td>
                </tr>

                <!--
                <tr>
                    <td>
                        <s:label value="%{getText('adoption.childs.dateofbirth.label')}"/>
                    </td>
                    <td>
                        <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
                        <s:textfield id="receivedDatePicker" name="childDateOfBirth" cssStyle="width:200px"
                         maxLength="10"/>
                    </td>
                    <td>
                         <s:label value="%{getText('adoption.childs.gender.label')}"/>
                    </td>
                    <td>
                        <s:select
                        list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                        name="childGender"
                        id="childGender"
                        cssStyle="width:150px; margin-left:5px;"/>

                    </td>
                </tr>
                -->
                <!--
                <tr>
                    <td>
                        <%--<s:label value="%{getText('adoption.childs.AgeRange.label')}"/>--%>
                    </td>
                    <td>
                        <br> <%--<s:label value="%{getText('from.label')}"/>&nbsp&nbsp
                             <s:textfield id="ageFrom"  cssClass="width80"  maxLength="10"/> &nbsp&nbsp
                             <s:label value="%{getText('to.label')}"/>
                             <s:textfield id="ageTo"  cssClass="width80"  maxLength="10"/>   --%>                        br>
                            
                    </td>
                    <td>
                    </td>
                    <td>
                    </td>
                </tr> -->

                <tr>
                    <td>
                        <s:label value="%{getText('adoption.data.entry.period.label')}"/>
                    </td>
                    <td>
                        <br> <s:label value="%{getText('from.label')}"/>&nbsp&nbsp
                        <s:textfield id="dataEntryPeriodFrom" name="dataEntryPeriodFrom" cssClass="width80"
                                     maxLength="10"/> &nbsp&nbsp
                        <s:label value="%{getText('to.label')}"/>
                        <s:textfield id="dataEntryPeriodTo" name="dataEntryPeriodTo" cssClass="width80" maxLength="10"/>
                    </td>
                    <td>
                    </td>
                    <td class="form-submit" align="right">
                        <s:submit value="%{getText('search.adoption.label')}" cssStyle="float: right;"/>
                    </td>
                </tr>

                <!--
                <tr>
                    <td colspan="3"></td>
                    <td class="form-submit" align="right">
                        <s:submit value="%{getText('search.adoption.label')}" cssStyle="float: right;"/>
                    </td>

                </tr>
                -->

            </table>
        </s:form>
        <br/>
    </fieldset>
    <fieldset style="margin-bottom:10px;margin-top:20px;border:none">
        <table id="searchList" width="100%" cellpadding="0" cellspacing="0" class="display">
            <thead>
            <tr>
                <th width="80px"><s:label value="%{getText('certificate_number.label')}"/></th>
                <th width="120px"><s:label value="%{getText('entry_no.label')}"/></th>
                <th><s:label value="%{getText('name.label')}"/></th>
                <th width="150px"><s:label value="%{getText('court.order.no.label')}"/></th>
                <th width="60px"><s:label value="%{getText('view.label')}"/></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator id="searchResults" value="searchResults" status="searchStatus">
                <tr>
                    <td>
                        <s:property value="idUKey"/>
                    </td>
                    <td>
                        <s:property value="adoptionEntryNo"/>
                    </td>
                    <td>
                        <s:if test="childNewName!=null">
                            <s:property value="getChildNewNameToLength(30)"/>
                        </s:if>
                        <s:elseif test="childExistingName!=null">
                            <s:property value="getChildExistingNameToLength(30)"/>
                        </s:elseif>
                    </td>
                    <td>
                        <s:property value="courtOrderNumber"/>
                    </td>
                    <td align="center">
                        <s:url id="viewSelected" action="eprAdoptionViewMode.do">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>

                        <s:a href="%{viewSelected}" title="%{getText('viewAdoptionRegistrationTooltip.label')}">
                            <img id='viewImage' src="<s:url value='/images/view.gif'/>" width="25" height="25"
                                 border="none"/>
                        </s:a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </fieldset>
</div>

<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
    <div class="form-submit">
        <s:submit type="button" value="%{getText('print.button')}" onclick="printPageById('adoption-search-report')"/>
    </div>
    <div id="adoption-search-report">
        <div align="center">
            <h2><s:label value="%{getText('adoption.order.reports.label')}"/></h2>
        </div>

        <table id="reportHeader" width="700px" border="0">
            <tr>
                <td>
                    <s:label value="%{getText('court.label')}"/>
                </td>
                <td colspan="4">
                    <s:select list="courtList" name="courtId" headerKey="0" cssStyle="font-size: 10pt;"
                              headerValue="%{getText('select.label')}" disabled="true"/>
                </td>
            </tr>

            <tr>
                <td>
                    <s:label value="%{getText('adoption.data.entry.period.label')}"/>
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
                <th width="8%"><s:label value="%{getText('certificate_number.label')}"/></th>
                <th width="12%"><s:label value="%{getText('registration.no.label')}"/></th>
                <th width="12%"><s:label value="%{getText('registration.date.label')}"/></th>
                <th width="12%"><s:label value="%{getText('court.order.no.label')}"/></th>
                <th width="16%"><s:label value="%{getText('court.order.issued.date.label')}"/></th>
                <th width="40%"><s:label value="%{getText('status.label')}"/></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator id="searchResults" value="searchResults" status="searchStatus">
                <tr style="background:#E9F2F7">
                    <td height="35px" style="background-color:#CDCDEC;">
                        <s:property value="idUKey"/>
                    </td>
                    <td height="35px">
                        <s:property value="adoptionEntryNo"/>
                    </td>
                    <td height="35px">
                        <s:property value="orderReceivedDate"/>
                    </td>
                    <td height="35px">
                        <s:property value="courtOrderNumber"/>
                    </td>
                    <td height="35px">
                        <s:property value="orderIssuedDate"/>
                    </td>
                    <td height="35px">
                        <s:property value="getText(status)"/>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </div>
</fieldset>

<s:hidden id="date-comparison" value="%{getText('date.comparison.validation.label')}"> </s:hidden>

