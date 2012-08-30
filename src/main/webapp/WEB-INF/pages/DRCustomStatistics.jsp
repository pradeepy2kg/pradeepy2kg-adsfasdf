<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage cssStyle="color:blue;;font-size:10pt"/>


<script type="text/javascript" src="/ecivil/lib/jquery/jquery.jqplot.js"></script>
<script type="text/javascript" src="/ecivil/lib/jquery/jqplot.categoryAxisRenderer.js"></script>
<script type="text/javascript" src="/ecivil/lib/jquery/jqplot.barRenderer.js"></script>
<script type="text/javascript" src="/ecivil/lib/jquery/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/chartCreator.js"/>"></script>

<link rel="stylesheet" type="text/css" href="/ecivil/css/jquery.jqplot.css"/>
<s:hidden id="userName" value="%{userName}"/>

<style type="text/css">
    .jqplot-target {
        color: #666666;
        font-family: Bhashitha, BhashithaBold, BhashithaSans, BhashithaSansBold, SriTamil, SriTamilBold, sans-serif;
        font-size: 1em;
    }

    #chart1, #chart2, #chart4, #chart5 {
        width: 460px;
        height: 120px;
        float: left;
        margin-right: 5px;
    }

    #chart3, #chart6 {
        width: 400px;
        height: 300px;
        margin: 0;
        padding: 0;
    }

    .pie {
        padding-left: 0;
        text-align: left;
    }

    .info {
        padding-left: 20px;
    }

    .topic {
        height: 30px;
        border: #000 1px solid;
        background-color: #E0FFFF;
        text-align: center;
        padding-top: 10px;
        margin-top: 10px;
        margin-bottom: 10px;
    }

    .noStyle {
        border-style: none;
    }

    .noStyle_red {
        border-style: none;
        color: red;
        text-align: right;
    }

    .issue {
        color: red;
    }

    input {
        width: 40px;
    }

    table, tr, td, th {
        border-collapse: collapse;
        border: 1px solid #fff;
    }

    td {
        padding-left: 10px;
        padding-right: 10px;
    }

    th {
        height: 35px;
        background-color: #A9D0F5;
        padding: 10px;
        color: #08088A;
        font-size: 14px;
        font-weight: lighter;
    }

    tr.e {
        background-color: #E4ECFC;
        height: 35px;
    }

    #space {
        height: 20px;
    }

</style>

<div id="ADR-home-page-outer">

<script type="text/javascript">

    $(function() {
        $('select#district').bind('change', function(evt1) {
            var id = $("select#district").attr("value");

            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:15},
                    function(data) {
                        var options2 = '';
                        var bd = data.adrList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#adrUser").html(options2);
                    });
        });

        $('select#adrUser').bind('change', function(evt1) {
            var userName = $("select#adrUser").attr("value");
            var userType = 'dr';
            var statType = 'all';
            var mode = 'drStatInfo';
            $.getJSON('/ecivil/crs/StatisticsLookupService',
            {
                userType:userType,
                statType:statType,
                userName:userName,
                mode:mode
            },
                    function(data) {
                        drawChart(data);
                    }
                    );
        });
    });

    $(document).ready(function() {
        var userName = $("select#adrUser").attr("value");
        var userType = 'dr';
        var statType = 'all';
        var mode = 'drStatInfo';
        $.getJSON('/ecivil/crs/StatisticsLookupService',
        {
            userType:userType,
            statType:statType,
            userName:userName,
            mode:mode
        },
                function(data) {
                    drawChart(data);
                }
                );

    });

    function initPage() {
    }

</script>
<div id="space"></div>
<table width="100%" cellpadding="0" cellspacing="0">
    <%-- start --%>
    <tr class="e" bgcolor="#eeeeee">
        <th colspan="4" align="left">Custom Search</th>
    </tr>
    <tr class="e">
        <td width="20%">District</td>
        <td width="30%">
            <s:select
                    id="district"
                    name="districtId"
                    list="districtList"
                    />
        </td>
        <td>ADR</td>
        <td>
            <s:select
                    id="adrUser"
                    name="adrUserId"
                    list="adrList"
                    />
        </td>
    </tr>
    <tr class="e">
        <td>Start Date</td>
        <td>
            <s:textfield id="sdate" name="startDate" cssStyle="width:70%;"/>
        </td>
        <td>End Date</td>
        <td>
            <s:textfield id="edate" name="endDate" cssStyle="width:70%;"/>
        </td>
    </tr>
    <%-- end --%>
    <tr>
        <td colspan="4" align="center">
            <div class="topic">Birth Statistics</div>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <table width="100%">
                <tr>
                    <td class="info" width="60%">
                        All Pending :
                        <input type="text" id="all_pending_b" class="noStyle" readonly="true" maxlength="3"/>
                    </td>
                    <td class="info" width="40%">
                        <label class="issue">
                            Arrears :
                            <input type="text" id="arrears_b" class="noStyle_red" readonly="true" maxlength="3"/>
                        </label>
                    </td>
                </tr>
            </table>
        </td>
        <td colspan="2" rowspan="4" class="pie">
            <div id="chart3"></div>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div id="chart1"></div>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <table width="100%">
                <tr>
                    <td class="info" width="60%">
                        Total Submitted Items :
                        <input type="text" id="total_submitted_b" class="noStyle" readonly="true" maxlength="3"/>
                    </td>
                    <td class="info" width="40%">
                        <label class="issue">
                            Late Items :
                            <input type="text" id="late_b" class="noStyle_red" readonly="true" maxlength="3"/>
                        </label>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div id="chart2"></div>
        </td>
    </tr>
    <tr>
        <td colspan="4" align="center">
            <div class="topic">Death Statistics</div>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <table width="100%">
                <tr>
                    <td class="info" width="60%">All Pending : <input type="text" id="all_pending_d" class="noStyle"
                                                                      readonly="true"
                                                                      maxlength="3"/></td>
                    <td class="info" width="40%"><label class="issue">Arrears : <input type="text" id="arrears_d"
                                                                                       class="noStyle_red"
                                                                                       readonly="true"
                                                                                       maxlength="3"/></label></td>
                </tr>
            </table>
        </td>
        <td colspan="2" rowspan="4" class="pie">
            <div id="chart6"></div>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div id="chart4"></div>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <table width="100%">
                <tr>
                    <td class="info" width="60%">Total Submitted Items : <input type="text" id="total_submitted_d"
                                                                                class="noStyle"
                                                                                readonly="true" maxlength="3"/></td>
                    <td class="info" width="40%"><label class="issue">Late Items : <input type="text" id="late_d"
                                                                                          class="noStyle_red"
                                                                                          readonly="true"
                                                                                          maxlength="3"/></label>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div id="chart5"></div>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>

</div>
