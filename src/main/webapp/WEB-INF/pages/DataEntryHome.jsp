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
        font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
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
        padding-left: 10px;
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
    }

    .issue {
        color: red;
    }

    input {
        width: 40px;
    }

</style>

<div id="ADR-home-page-outer">

    <script type="text/javascript">

        $(document).ready(function() {
            var deo = $("#deoUserId").attr("value");
            var statType = 'all';
            var mode = 'deoStatInfo';
            $.getJSON('/ecivil/crs/StatisticsLookupService',
            {
                deo:deo,
                statType:statType,
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
    <s:hidden id="deoUserId" name="userName"/>
    <table border="0" width="100%">
        <tr>
            <td colspan="4" align="center">
                <div class="topic">Birth Statistics</div>
            </td>
        </tr>
        <tr>
            <td class="info">
                All Pending :
                <input type="text" id="all_pending_b" class="noStyle" readonly="true" maxlength="3"/>
            </td>
            <td class="info">
                <label class="issue">
                    Arrears :
                    <input type="text" id="arrears_b" class="noStyle_red" readonly="true" maxlength="3"/>
                </label>
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
            <td class="info">
                Total Submitted Items :
                <input type="text" id="total_submitted_b" class="noStyle" readonly="true" maxlength="3"/>
            </td>
            <td class="info">
                <label class="issue">
                    Late Items :
                    <input type="text" id="late_b" class="noStyle_red" readonly="true" maxlength="3"/>
                </label>
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
            <td class="info">All Pending : <input type="text" id="all_pending_d" class="noStyle" readonly="true"
                                                  maxlength="3"/></td>
            <td class="info"><label class="issue">Arrears : <input type="text" id="arrears_d" class="noStyle_red"
                                                                   readonly="true" maxlength="3"/></label></td>
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
            <td class="info">Total Submitted Items : <input type="text" id="total_submitted_d" class="noStyle"
                                                            readonly="true" maxlength="3"/></td>
            <td class="info"><label class="issue">Late Items : <input type="text" id="late_d" class="noStyle_red"
                                                                      readonly="true" maxlength="3"/></label></td>
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