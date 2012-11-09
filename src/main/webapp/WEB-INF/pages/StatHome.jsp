<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/ecivil/lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<script type="text/javascript" src="/ecivil/lib/jquery/jquery.jqplot.js"></script>
<script type="text/javascript" src="/ecivil/lib/jquery/jqplot.categoryAxisRenderer.js"></script>
<script type="text/javascript" src="/ecivil/lib/jquery/jqplot.barRenderer.js"></script>
<script type="text/javascript" src="/ecivil/lib/jquery/jqplot.pieRenderer.min.js"></script>
<link rel="stylesheet" type="text/css" href="/ecivil/css/jquery.jqplot.css"/>
<link rel="stylesheet" type="text/css" href="/ecivil/css/statistics.css"/>

<script type="text/javascript" src="<s:url value="/js/chartCreator.js"/>"></script>

<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage cssStyle="color:blue;;font-size:10pt"/>

<script type="text/javascript">

    $(function () {
        $("#sdate").datepicker({
            changeYear:true,
            yearRange:'1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function () {
        $("#edate").datepicker({
            changeYear:true,
            yearRange:'1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function () {
        var selectedDistrict = document.getElementById('district').options[document.getElementById('district').selectedIndex].text;
        $('#search_all_district_button').val(selectedDistrict);
        $('select#district').bind('change', function (evt1) {
            var id = $("select#district").attr("value");
            var selectedDistrict = document.getElementById('district').options[document.getElementById('district').selectedIndex].text;
            $('#search_all_district_button').val(selectedDistrict);
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:13},
                    function (data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivision").html(options1);

                        var options2 = '';
                        var bd = data.deoList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#deoUser").html(options2);
                    });
        });

        $('select#dsDivision').bind('change', function (evt1) {
            var id = $("select#dsDivision").attr("value");

            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:14},
                    function (data) {
                        var options1 = '';
                        var ds = data.deoList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#deoUser").html(options1);
                    });
        });

        $('#search_button').bind('click', function (evt1) {
            var deo = $("input#userId").val();
            var startDate = $("input#sdate").val();
            var endDate = $("input#edate").val();
            var districtId = $("select#district").val();
            var dsDivisionId = $("select#dsDivision").val();
            var userRole = $("input#userRole").val();

            $.getJSON('/ecivil/crs/StatisticsLookupService', {userId:deo, startDate:startDate, endDate:endDate, districtId:districtId, dsDivisionId:dsDivisionId},
                    function (data) {
                        drawChart(data);
                    });
        });

        $('#search_all_district_button').bind('click', function (evt1) {
            var deo = $("input#userId").val();
            var startDate = $("input#sdate").val();
            var endDate = $("input#edate").val();
            var districtId = $("select#district").val();
            var dsDivisionId = 0;
            var userRole = $("input#userRole").val();

            $.getJSON('/ecivil/crs/StatisticsLookupService', {userId:deo, startDate:startDate, endDate:endDate, districtId:districtId, dsDivisionId:dsDivisionId},
                    function (data) {
                        drawChart(data);
                    });
        });

        $('#search_all_country_button').bind('click', function (evt1) {
            var deo = $("input#userId").val();
            var startDate = $("input#sdate").val();
            var endDate = $("input#edate").val();
            var districtId = 0;
            var dsDivisionId = 0;
            var userRole = $("input#userRole").val();

            $.getJSON('/ecivil/crs/StatisticsLookupService', {userId:deo, startDate:startDate, endDate:endDate, districtId:districtId, dsDivisionId:dsDivisionId},
                    function (data) {
                        drawChart(data);
                    });
        });

    });

    $(document).ready(function () {
        var data = new StatObject();
        data.userRole = $("input#userRole").val();

        data.late_b = parseInt($("input#birthsLateSubmissions").val());
        data.normal_b = parseInt($("input#birthsNormalSubmissions").val());
        data.rejected_b = parseInt($("input#birthsRejectedItems").val());
        data.approved_b = parseInt($("input#birthsApprovedItems").val());
        data.arrears_pend_b = parseInt($("input#birthsArrearsPendingItems").val());
        data.thismonth_pend_b = parseInt($("input#birthsThisMonthPendingItems").val());
        data.total_submitted_b = parseInt($("input#birthsThisMonthTotalItems").val());
        data.still_b = parseInt($("input#birthsStillSubmissions").val());
        data.confirmation_printed_b = parseInt($('#birthConfirmationPrintedItems').val());
        data.confirmation_approval_pending_b = parseInt($('#birthConfirmationApprovalPendingItems').val());
        data.confirmation_approved_b = parseInt($('#birthConfirmationApprovedItems').val());
        data.certificate_generated_b = parseInt($('#birthCertificateGenerated').val());
        data.certificate_printed_b = parseInt($('#birthCertificatePrinted').val());
        data.deleted_b = parseInt($('#birthDeletedItems').val());

        data.late_d = parseInt($("input#DeathsLateSubmissions").val());
        data.normal_d = parseInt($("input#DeathsNormalSubmissions").val());
        data.rejected_d = parseInt($("input#DeathsRejectedItems").val());
        data.approved_d = parseInt($("input#DeathsApprovedItems").val());
        data.arrears_pend_d = parseInt($("input#DeathsArrearsPendingItems").val());
        data.thismonth_pend_d = parseInt($("input#DeathsThisMonthPendingItems").val());
        data.total_submitted_d = parseInt($("input#DeathsThisMonthTotalItems").val());
        data.certificate_printed_d = parseInt($('#deathCertificatePrintedItems').val());
        data.deleted_d = parseInt($('#deathsDeletedItems').val());

        data.late_m = parseInt($("input#MrgLateSubmissions").val());
        data.normal_m = parseInt($("input#MrgNormalSubmissions").val());
        data.rejected_m = parseInt($("input#MrgRejectedItems").val());
        data.approved_m = parseInt($("input#MrgApprovedItems").val());
        data.arrears_pend_m = parseInt($("input#MrgArrearsPendingItems").val());
        data.thismonth_pend_m = parseInt($("input#MrgThisMonthPendingItems").val());
        data.total_submitted_m = parseInt($("input#MrgThisMonthTotalItems").val());

        drawChart(data);
    });

    function initPage() {
    }
</script>

<div id="home-page-outer">
<div id="stat-preferences">
    <table border="0" width="100%" cellpadding="5" cellspacing="5">
        <tr bgcolor="#eeeeee">
            <th colspan="5" align="left">Custom Search</th>
        </tr>
        <tr>
            <td width="20%" align="right">District</td>
            <td width="30%" align="left">
                <s:select id="district" name="districtId" list="districtList"/>
            </td>
            <td width="20%" align="right">DSDivision</td>
            <td width="30%" align="left">
                <s:select id="dsDivision" name="dsDivisionId" list="divisionList"/>
            </td>
        </tr>
        <tr>
            <td align="right">Start Date</td>
            <td align="left">
                <s:textfield id="sdate" name="startDate" cssStyle="width:70%;"/>
            </td>
            <td align="right">End Date</td>
            <td align="left">
                <s:textfield id="edate" name="endDate" cssStyle="width:70%;"/>
            </td>
            <td class="button"><s:submit id="search_button" align="right" value="Search"/></td>
        </tr>
        <s:if test="role == 'ADMIN' || role == 'RG' || role == 'ARG'">
            <tr>
                <td colspan="5" class="button" align="right" style="border-top: 1px solid #000;">
                    <h3 style="display: inline;">Show statistics of </h3>
                    <s:submit id="search_all_country_button" align="right" value="All Districts"/>
                    <h3 style="display: inline;">or</h3>
                    <s:submit id="search_all_district_button" align="right" value="District" cssStyle="width: 150px;"/>
                </td>
            </tr>
        </s:if>
    </table>
</div>

<div id="statpane">
<s:if test="role == 'DEO' || role == 'ADR' || role == 'DR'">
    <div id="stat-charts">
        <div id="stat-birth">
            <table border="0" cellpadding="0" cellspacing="0" width="99%" align="center">
                <tr>
                    <td colspan="4" class="topic"><b>Birth Statistics</b></td>
                </tr>
                <tr>
                    <td width="25%">Total Approval Pending :
                        <input type="text" id="all_pending_b" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;&nbsp;Total Approved Items :
                        <input type="text" id="approved_b" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;Total Rejected Items :
                        <input type="text" id="rejected_b" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;Confirmation Printed :
                        <input type="text" id="confirmation_printed_b" readonly="true" maxlength="6"/><br/>
                        <%--Confirmation Approval Pending :--%>
                        <%--<input type="text" id="confirmation_approval_pending_b" readonly="true" maxlength="6"/><br/>--%>
                        Confirmation Approved :
                        <input type="text" id="confirmation_approved_b" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;Certificate Generated :
                        <input type="text" id="certificate_generated_b" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Certificate Printed :
                        <input type="text" id="certificate_printed_b" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;Total Deleted Items :
                        <input type="text" id="deleted_b" readonly="true" maxlength="6"/>
                    </td>
                    <td width="25%" class="pending">Arrears :
                        <input type="text" id="arrears_b" readonly="true" maxlength="6"/>
                    </td>
                    <td width="50%" colspan="2" rowspan="6">
                        <div id="chart3"></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" rowspan="2">
                        <div id="chart1"></div>
                    </td>
                </tr>
                <tr></tr>
                <tr style="height:50px" valign="bottom">
                    <td>Total Submitted Items :
                        <input type="text" id="total_submitted_b" readonly="true" maxlength="6"/>
                    </td>
                    <td class="pending">Late Items :
                        <input type="text" id="late_b" readonly="true" maxlength="6"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" rowspan="2">
                        <div id="chart2"></div>
                    </td>
                </tr>
                <tr></tr>
            </table>
        </div>

        <div id="stat-death">
            <table border="0" cellpadding="0" cellspacing="0" width="99%" align="center">
                <tr>
                    <td colspan="4" class="topic"><b>Death Statistics</b></td>
                </tr>
                <tr>
                    <td width="25%">Total Approval Pending :
                        <input type="text" id="all_pending_d" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;&nbsp;Total Approved Items :
                        <input type="text" id="approved_d" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Rejected Items :
                        <input type="text" id="rejected_d" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Certificate Printed :
                        <input type="text" id="certificate_printed_d" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Deleted Items :
                        <input type="text" id="deleted_d" readonly="true" maxlength="6"/>
                    </td>
                    <td width="25%" class="pending">Arrears :
                        <input type="text" id="arrears_d" readonly="true" maxlength="6"/>
                    </td>
                    <td width="50%" colspan="2" rowspan="6">
                        <div id="chart6"></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" rowspan="2">
                        <div id="chart4"></div>
                    </td>
                </tr>
                <tr></tr>
                <tr style="height:50px" valign="bottom">
                    <td>Total Submitted Items :
                        <input type="text" id="total_submitted_d" readonly="true" maxlength="6"/>
                    </td>
                    <td class="pending">Late Items :
                        <input type="text" id="late_d" readonly="true" maxlength="6"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" rowspan="2">
                        <div id="chart5"></div>
                    </td>
                </tr>
                <tr></tr>
            </table>
        </div>

        <div id="stat-marriage">
            <table border="0" cellpadding="0" cellspacing="0" width="99%" align="center">
                <tr>
                    <td colspan="4" class="topic"><b>Marriage Statistics</b></td>
                </tr>
                <tr>
                    <td width="25%">Total Approval Pending :
                        <input type="text" id="all_pending_m" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;&nbsp;Total Approved Items :
                        <input type="text" id="approved_m" readonly="true" maxlength="6"/><br/>
                        &nbsp;&nbsp;&nbsp;Total Rejected Items :
                        <input type="text" id="rejected_m" readonly="true" maxlength="6"/>
                    </td>
                    <td width="25%" class="pending">Arrears :
                        <input type="text" id="arrears_m" readonly="true" maxlength="6"/>
                    </td>
                    <td width="50%" colspan="2" rowspan="6">
                        <div id="chart9"></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" rowspan="2">
                        <div id="chart7"></div>
                    </td>
                </tr>
                <tr></tr>
                <tr style="height:50px" valign="bottom">
                    <td>Total Submitted Items :
                        <input type="text" id="total_submitted_m" readonly="true" maxlength="6"/>
                    </td>
                    <td class="pending">Late Items :
                        <input type="text" id="late_m" readonly="true" maxlength="6"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" rowspan="2">
                        <div id="chart8"></div>
                    </td>
                </tr>
                <tr></tr>
            </table>
        </div>
    </div>
</s:if>
<s:else>
    <table border="0" width="100%" style="margin-left:5px;">
        <tr>
            <td><b>Birth Statistics</b></td>
            <td><b>Death Statistics</b></td>
            <td><b>Marriage Statistics</b></td>
        </tr>
        <tr>
            <td>
                <div id="rg-birth">
                    <div id="birth-pie">

                    </div>
                    <div id="birth-bar">

                    </div>
                    <table align="center" style="font-size: 12px;">
                        <tr>
                            <td align="right">All Pending</td>
                            <td>:</td>
                            <td>
                                <input type="text" id="all_pending_b" readonly="true" maxlength="6"/>
                            </td>
                            <td align="right" class="pending">Arrears</td>
                            <td>:</td>
                            <td class="pending">
                                <input type="text" id="arrears_b" readonly="true" maxlength="6"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">Total Approved</td>
                            <td>:</td>
                            <td>
                                <input type="text" id="approved_b" readonly="true" maxlength="6"/>
                            </td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td align="right">Total Rejected</td>
                            <td>:</td>
                            <td><input type="text" id="rejected_b" readonly="true" maxlength="6"/></td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td align="right">Confirmation Printed</td>
                            <td>:</td>
                            <td><input type="text" id="confirmation_printed_b" readonly="true" maxlength="6"/></td>
                            <td colspan="3"></td>
                        </tr>
                        <%--<tr>--%>
                            <%--<td align="right">Confirmation Approval Pending</td>--%>
                            <%--<td>:</td>--%>
                            <%--<td><input type="text" id="confirmation_approval_pending_b" readonly="true" maxlength="6"/></td>--%>
                            <%--<td colspan="3"></td>--%>
                        <%--</tr>--%>
                        <tr>
                            <td align="right">Confirmation Approved</td>
                            <td>:</td>
                            <td><input type="text" id="confirmation_approved_b" readonly="true" maxlength="6"/></td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td align="right">Certificate Generated</td>
                            <td>:</td>
                            <td><input type="text" id="certificate_generated_b" readonly="true" maxlength="6"/></td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td align="right">Certificate Printed</td>
                            <td>:</td>
                            <td><input type="text" id="certificate_printed_b" readonly="true" maxlength="6"/></td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td align="right">Deleted Items</td>
                            <td>:</td>
                            <td><input type="text" id="deleted_b" readonly="true" maxlength="6"/></td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td align="right">Total Submitted Items</td>
                            <td>:</td>
                            <td>
                                <input type="text" id="total_submitted_b" readonly="true" maxlength="6"/>
                            </td>
                            <td align="right" class="pending">Late Items</td>
                            <td>:</td>
                            <td class="pending">
                                <input type="text" id="late_b" readonly="true" maxlength="6"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
            <td>
                <div id="rg-death" style="padding-bottom: 85px;">
                    <div id="death-pie">

                    </div>
                    <div id="death-bar">

                    </div>
                    <table align="center" style="font-size: 12px;">
                        <tr>
                            <td align="right">All Pending</td>
                            <td>:</td>
                            <td>
                                <input type="text" id="all_pending_d" readonly="true" maxlength="6"/>
                            </td>
                            <td align="right" class="pending">Arrears</td>
                            <td>:</td>
                            <td class="pending">
                                <input type="text" id="arrears_d" readonly="true" maxlength="6"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">Total Approved</td>
                            <td>:</td>
                            <td>
                                <input type="text" id="approved_d" readonly="true" maxlength="6"/>
                            </td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td align="right">Total Rejected</td>
                            <td>:</td>
                            <td><input type="text" id="rejected_d" readonly="true" maxlength="6"/></td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td align="right">Certificate Printed</td>
                            <td>:</td>
                            <td><input type="text" id="certificate_printed_d" readonly="true" maxlength="6"/></td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td align="right">Deleted Items</td>
                            <td>:</td>
                            <td><input type="text" id="deleted_d" readonly="true" maxlength="6"/></td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td align="right">Total Submitted Items</td>
                            <td>:</td>
                            <td>
                                <input type="text" id="total_submitted_d" readonly="true" maxlength="6"/>
                            </td>
                            <td align="right" class="pending">Late Items</td>
                            <td>:</td>
                            <td class="pending">
                                <input type="text" id="late_d" readonly="true" maxlength="6"/>
                            </td>
                        </tr>
                        <tr><td colspan="6"></td></tr>
                    </table>
                </div>
            </td>
            <td>
                <div id="rg-mrg" style="padding-bottom: 135px;">
                    <div id="mrg-pie">

                    </div>
                    <div id="mrg-bar">

                    </div>
                    <table align="center" style="font-size: 12px;">
                        <tr>
                            <td align="right">All Pending</td>
                            <td>:</td>
                            <td>
                                <input type="text" id="all_pending_m" readonly="true" maxlength="6"/>
                            </td>
                            <td align="right" class="pending">Arrears</td>
                            <td>:</td>
                            <td class="pending">
                                <input type="text" id="arrears_m" readonly="true" maxlength="6"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">Total Approved</td>
                            <td>:</td>
                            <td>
                                <input type="text" id="approved_m" readonly="true" maxlength="6"/>
                            </td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td>Total Rejected</td>
                            <td>:</td>
                            <td><input type="text" id="rejected_m" readonly="true" maxlength="6"/></td>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td align="right">Total Submitted Items</td>
                            <td>:</td>
                            <td>
                                <input type="text" id="total_submitted_m" readonly="true" maxlength="6"/>
                            </td>
                            <td align="right" class="pending">Late Items</td>
                            <td>:</td>
                            <td class="pending">
                                <input type="text" id="late_m" readonly="true" maxlength="6"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </table>
</s:else>
</div>
</div>

<s:hidden id="birthsLateSubmissions" value="%{statistics.birthsLateSubmissions}"/>
<s:hidden id="birthsNormalSubmissions" value="%{statistics.birthsNormalSubmissions}"/>
<s:hidden id="birthsStillSubmissions" value="%{statistics.birthsStillSubmissions}"/>
<s:hidden id="birthsApprovedItems" value="%{statistics.birthsApprovedItems}"/>
<s:hidden id="birthsRejectedItems" value="%{statistics.birthsRejectedItems}"/>
<s:hidden id="birthsArrearsPendingItems" value="%{statistics.birthsArrearsPendingItems}"/>
<s:hidden id="birthsThisMonthPendingItems" value="%{statistics.birthsThisMonthPendingItems}"/>
<s:hidden id="birthsThisMonthTotalItems" value="%{statistics.birthsTotalSubmissions}"/>
<s:hidden id="birthConfirmationPrintedItems" value="%{statistics.birthConfirmationPrintedItems}"/>
<s:hidden id="birthConfirmationApprovalPendingItems" value="%{statistics.birthConfirmationApprovalPendingItems}"/>
<s:hidden id="birthConfirmationApprovedItems" value="%{statistics.birthConfirmationApprovedItems}"/>
<s:hidden id="birthCertificateGenerated" value="%{statistics.birthCertificateGenerated}"/>
<s:hidden id="birthCertificatePrinted" value="%{statistics.birthCertificatePrinted}"/>
<s:hidden id="birthDeletedItems" value="%{statistics.birthDeletedItems}"/>

<s:hidden id="DeathsLateSubmissions" value="%{statistics.DeathsLateSubmissions}"/>
<s:hidden id="DeathsNormalSubmissions" value="%{statistics.DeathsNormalSubmissions}"/>
<s:hidden id="DeathsApprovedItems" value="%{statistics.DeathsApprovedItems}"/>
<s:hidden id="DeathsRejectedItems" value="%{statistics.DeathsRejectedItems}"/>
<s:hidden id="DeathsArrearsPendingItems" value="%{statistics.DeathsArrearsPendingItems}"/>
<s:hidden id="DeathsThisMonthPendingItems" value="%{statistics.DeathsThisMonthPendingItems}"/>
<s:hidden id="DeathsThisMonthTotalItems" value="%{statistics.DeathsTotalSubmissions}"/>
<s:hidden id="deathCertificatePrintedItems" value="%{statistics.deathCertificatePrintedItems}"/>
<s:hidden id="deathsDeletedItems" value="%{statistics.deathsDeletedItems}"/>

<s:hidden id="MrgLateSubmissions" value="%{statistics.MrgLateSubmissions}"/>
<s:hidden id="MrgNormalSubmissions" value="%{statistics.MrgNormalSubmissions}"/>
<s:hidden id="MrgApprovedItems" value="%{statistics.MrgApprovedItems}"/>
<s:hidden id="MrgRejectedItems" value="%{statistics.MrgRejectedItems}"/>
<s:hidden id="MrgArrearsPendingItems" value="%{statistics.MrgArrearsPendingItems}"/>
<s:hidden id="MrgThisMonthPendingItems" value="%{statistics.MrgThisMonthPendingItems}"/>
<s:hidden id="MrgThisMonthTotalItems" value="%{statistics.MrgTotalSubmissions}"/>

<s:hidden id="userRole" value="%{role}"/>
<s:hidden id="userId" value="%{user.userId}"/>








