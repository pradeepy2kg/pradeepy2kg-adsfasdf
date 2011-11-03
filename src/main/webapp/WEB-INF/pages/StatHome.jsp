<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<script type="text/javascript" src="/ecivil/lib/jquery/jquery.jqplot.js"></script>
<script type="text/javascript" src="/ecivil/lib/jquery/jqplot.categoryAxisRenderer.js"></script>
<script type="text/javascript" src="/ecivil/lib/jquery/jqplot.barRenderer.js"></script>
<script type="text/javascript" src="/ecivil/lib/jquery/jqplot.pieRenderer.min.js"></script>
<link rel="stylesheet" type="text/css" href="/ecivil/css/jquery.jqplot.css"/>

<script type="text/javascript" src="<s:url value="/js/chartCreator.js"/>"></script>

<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage cssStyle="color:blue;;font-size:10pt"/>


<style type="text/css">
    .jqplot-target {
        color: #666666;
        font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
        font-size: 1em;
    }

    #stat-preferences {
        width: 100%;
        margin: 10px 5px 15px 5px;
        border: #333 1px solid;
        padding: 0;
    }

    #stat-charts {
        width: 100%;
        height: 100%;
        background-color: #f0f8ff;
        margin: 5px;
        border: #333 1px solid;
    }

    #stat-birth {
        width: 95%;
        margin: 15px auto 15px auto;
        border: #333 1px solid;
    }

    #stat-death {
        width: 95%;
        margin: 15px auto 15px auto;
        border: #333 1px solid;
    }

    #stat-marriage {
        width: 95%;
        margin: 15px auto 15px auto;
        border: #333 1px solid;
    }

    #chart3, #chart6, #chart9 {
        width: 400px;
        height: 300px;
        margin: 0;
        padding: 0;
    }

    #chart1, #chart2, #chart4, #chart5, #chart7, #chart8 {
        width: 460px;
        height: 110px;
        float: left;
        margin-right: 5px;
    }

    #rg-birth, #rg-death, #rg-mrg {
        width: 100%;
        border: black 1px solid;
        padding-bottom: 20px;
    }

    #birth-pie, #death-pie, #mrg-pie {
        width: 300px;
        height: 300px;
        padding: 0px;
        margin: 0 auto;
        display: block;
        clear: both;
        float: none;
    }

    #birth-bar, #death-bar, #mrg-bar {
        width: 90%;
        height: 200px;
        float: none;
        display: block;
        float: none;
        margin: 0 auto;
    }

</style>

<script type="text/javascript">

    $(function() {
        $("#sdate").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $("#edate").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $('select#district').bind('change', function(evt1) {
            var id = $("select#district").attr("value");

            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:13},
                    function(data) {
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

        $('select#dsDivision').bind('change', function(evt1) {
            var id = $("select#dsDivision").attr("value");

            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:14},
                    function(data) {
                        var options1 = '';
                        var ds = data.deoList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#deoUser").html(options1);
                    });
        });

        $('select#deoUser').bind('change', function(evt1) {
            var deo = $("select#deoUser").attr("value");
            var mode = 'adrStatInfo';

            $.getJSON('/ecivil/crs/StatisticsLookupService',
            {
                mode:mode,
                deo:deo
            },
                    function(data) {
                        alert("data captured");//drawChart(data);
                    }
                    );
        });

    });

    $(document).ready(function() {
        var data = new StatObject();
        data.userRole = $("input#userRole").val();

        data.late_b = parseInt($("input#birthsLateSubmissions").val());
        data.normal_b = parseInt($("input#birthsNormalSubmissions").val());
        data.rejected_b = parseInt($("input#birthsRejectedItems").val());
        data.approved_b = parseInt($("input#birthsApprovedItems").val());
        data.arrears_pend_b = parseInt($("input#birthsArrearsPendingItems").val());
        data.thismonth_pend_b = parseInt($("input#birthsThisMonthPendingItems").val());

        data.late_d = parseInt($("input#DeathsLateSubmissions").val());
        data.normal_d = parseInt($("input#DeathsNormalSubmissions").val());
        data.rejected_d = parseInt($("input#DeathsRejectedItems").val());
        data.approved_d = parseInt($("input#DeathsApprovedItems").val());
        data.arrears_pend_d = parseInt($("input#DeathsArrearsPendingItems").val());
        data.thismonth_pend_d = parseInt($("input#DeathsThisMonthPendingItems").val());

        data.late_m = parseInt($("input#MrgLateSubmissions").val());
        data.normal_m = parseInt($("input#MrgNormalSubmissions").val());
        data.rejected_m = parseInt($("input#MrgRejectedItems").val());
        data.approved_m = parseInt($("input#MrgApprovedItems").val());
        data.arrears_pend_m = parseInt($("input#MrgArrearsPendingItems").val());
        data.thismonth_pend_m = parseInt($("input#MrgThisMonthPendingItems").val());

        if ($("input#userRole").val() == 'ADMIN') {

        } else {
            drawChart(data);
        }

    });

    function initPage() {
    }
</script>

<div id="home-page-outer">
<s:if test="role == 'ADMIN'">
    <img src="<s:url value="/images/dashboard.png" />">
</s:if>
<s:else>
<s:if test="role != 'DEO'">
    <%--<s:form action="eprShowStatistics.do" method="post">
        <div id="stat-preferences">
            <table border="0" width="100%" cellpadding="5" cellspacing="5">
                <tr bgcolor="#eeeeee">
                    <th colspan="4" align="left">Custom Search</th>
                </tr>
                <tr>
                    <td width="20%" align="right">District</td>
                    <td width="30%" align="left">
                        <s:select
                                id="district"
                                name="districtId"
                                list="districtList"
                                />
                    </td>
                    <td width="20%" align="right">DSDivision</td>
                    <td width="30%" align="left">
                        <s:select
                                id="dsDivision"
                                name="dsDivisionId"
                                list="divisionList"
                                />
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
                </tr>
                <tr>
                    <s:if test="role == 'RG' || role == 'ARG'">
                        <td align="right">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                    </s:if>
                    <s:else>
                        <td align="right">DEO</td>
                        <td align="left">
                            <s:select
                                    id="deoUser"
                                    name="deoUserId"
                                    list="deoList"
                                    />
                        </td>
                    </s:else>
                    <td>&nbsp;</td>
                    <td><s:submit align="right"/></td>
                </tr>
            </table>
        </div>
    </s:form>--%>
</s:if>
<s:if test="role == 'DEO' || role == 'ADR' || role == 'DR'">
    <div id="stat-charts">
        <div id="stat-birth">
            <table border="0" cellpadding="0" cellspacing="0" width="99%" align="center">
                <tr>
                    <td colspan="4">Birth Statistics</td>
                </tr>
                <tr>
                    <td width="25%">All Pending :
                        <input style="border:none;width:40px;" type="text" id="all_pending_b" readonly="true"
                               maxlength="3"/>
                    </td>
                    <td width="25%">Arrears :
                        <input style="border:none;width:40px;" type="text" id="arrears_b" readonly="true"
                               maxlength="3"/>
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
                <tr>
                    <td>Total Submitted Items :
                        <input style="border:none;width:40px;" type="text" id="total_submitted_b"
                               readonly="true"
                               maxlength="3"/>
                    </td>
                    <td>Late Items :
                        <input style="border:none;width:40px;" type="text" id="late_b" readonly="true"
                               maxlength="3"/>
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
                    <td colspan="4">Death Statistics</td>
                </tr>
                <tr>
                    <td width="25%">All Pending :
                        <input style="border:none;width:40px;" type="text" id="all_pending_d" readonly="true"
                               maxlength="3"/>
                    </td>
                    <td width="25%">Arrears :
                        <input style="border:none;width:40px;" type="text" id="arrears_d" readonly="true"
                               maxlength="3"/>
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
                <tr>
                    <td>Total Submitted Items :
                        <input style="border:none;width:40px;" type="text" id="total_submitted_d"
                               readonly="true"
                               maxlength="3"/>
                    </td>
                    <td>Late Items :
                        <input style="border:none;width:40px;" type="text" id="late_d" readonly="true"
                               maxlength="3"/>
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
                    <td colspan="4">Marriage Statistics</td>
                </tr>
                <tr>
                    <td width="25%">All Pending :
                        <input style="border:none;width:40px;" type="text" id="all_pending_m" readonly="true"
                               maxlength="3"/>
                    </td>
                    <td width="25%">Arrears :
                        <input style="border:none;width:40px;" type="text" id="arrears_m" readonly="true"
                               maxlength="3"/>
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
                <tr>
                    <td>Total Submitted Items :
                        <input style="border:none;width:40px;" type="text" id="total_submitted_m"
                               readonly="true"
                               maxlength="3"/>
                    </td>
                    <td>Late Items :
                        <input style="border:none;width:40px;" type="text" id="late_m" readonly="true"
                               maxlength="3"/>
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
            <td>Birth Statistics</td>
            <td>Death Statistics</td>
            <td>Marriage Statistics</td>
        </tr>
        <tr>
            <td>
                <div id="rg-birth">
                    <div id="birth-pie">

                    </div>
                    <div id="birth-bar">

                    </div>
                </div>
            </td>
            <td>
                <div id="rg-death">
                    <div id="death-pie">

                    </div>
                    <div id="death-bar">

                    </div>
                </div>
            </td>
            <td>
                <div id="rg-mrg">
                    <div id="mrg-pie">

                    </div>
                    <div id="mrg-bar">

                    </div>
                </div>
            </td>
        </tr>
    </table>
</s:else>
</s:else>
</div>

<s:hidden id="birthsLateSubmissions" value="%{statistics.birthsLateSubmissions}"/>
<s:hidden id="birthsNormalSubmissions" value="%{statistics.birthsNormalSubmissions}"/>
<s:hidden id="birthsApprovedItems" value="%{statistics.birthsApprovedItems}"/>
<s:hidden id="birthsRejectedItems" value="%{statistics.birthsRejectedItems}"/>
<s:hidden id="birthsArrearsPendingItems" value="%{statistics.birthsArrearsPendingItems}"/>
<s:hidden id="birthsThisMonthPendingItems" value="%{statistics.birthsThisMonthPendingItems}"/>

<s:hidden id="DeathsLateSubmissions" value="%{statistics.DeathsLateSubmissions}"/>
<s:hidden id="DeathsNormalSubmissions" value="%{statistics.DeathsNormalSubmissions}"/>
<s:hidden id="DeathsApprovedItems" value="%{statistics.DeathsApprovedItems}"/>
<s:hidden id="DeathsRejectedItems" value="%{statistics.DeathsRejectedItems}"/>
<s:hidden id="DeathsArrearsPendingItems" value="%{statistics.DeathsArrearsPendingItems}"/>
<s:hidden id="DeathsThisMonthPendingItems" value="%{statistics.DeathsThisMonthPendingItems}"/>

<s:hidden id="MrgLateSubmissions" value="%{statistics.MrgLateSubmissions}"/>
<s:hidden id="MrgNormalSubmissions" value="%{statistics.MrgNormalSubmissions}"/>
<s:hidden id="MrgApprovedItems" value="%{statistics.MrgApprovedItems}"/>
<s:hidden id="MrgRejectedItems" value="%{statistics.MrgRejectedItems}"/>
<s:hidden id="MrgArrearsPendingItems" value="%{statistics.MrgArrearsPendingItems}"/>
<s:hidden id="MrgThisMonthPendingItems" value="%{statistics.MrgThisMonthPendingItems}"/>

<s:hidden id="userRole" value="%{role}"/>








