<%--
  Created by shan
--%>
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

<div id="outer-div">

<script type="text/javascript">
    $(function() {
        $('select#district').bind('change', function(evt1) {
            var id = $("select#district").attr("value");
            var mode = 'rgStatInfo';

            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:13},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivision").html(options1);

                    });
            $.getJSON('/ecivil/crs/StatisticsLookupService',
            {
                districtId:id,
                mode:mode
            },
                    function(data) {
                        drawHorizontalBarChart(data.late_b, data.normal_b, 'Late', 'Normal', 'nw');
                        drawHorizontalBarChart(data.late_d, data.normal_d, 'Late', 'Normal', 'sw');
                        drawPieChart(data, 'ne', 'birth');
                        drawPieChart(data, 'se', 'death');
                    }
                    );
        });
        $('select#dsDivision').bind('change', function(evt1) {
            var dsDivision = $("select#dsDivision").attr("value");
            var mode = 'rgStatInfo';

            $.getJSON('/ecivil/crs/StatisticsLookupService',
            {
                dsDivisionId:dsDivision,
                mode:mode
            },
                    function(data) {
                        drawHorizontalBarChart(data.late_b, data.normal_b, 'Late', 'Normal', 'nw');
                        drawHorizontalBarChart(data.late_d, data.normal_d, 'Late', 'Normal', 'sw');
                        drawPieChart(data, 'ne', 'birth');
                        drawPieChart(data, 'se', 'death');
                    }
                    );
        });

    });

    $(document).ready(function() {
        var dsDivision = $("select#dsDivision").attr("value");
        var userType = 'rg';
        var statType = 'all';
        var mode = 'rgStatInfo';

        $.getJSON('/ecivil/crs/StatisticsLookupService',
        {
            userType:userType,
            statType:statType,
            dsDivisionId:dsDivision,
            mode:mode
        },
                function(data) {
                    drawHorizontalBarChart(data.late_b, data.normal_b, 'Late', 'Normal', 'nw');
                    drawHorizontalBarChart(data.late_d, data.normal_d, 'Late', 'Normal', 'sw');
                    drawPieChart(data, 'ne', 'birth');
                    drawPieChart(data, 'se', 'death');
                }
                );

    });

    function initPage() {
    }

</script>

<style type="text/css">
    table, tr, td, th {
        border-collapse: collapse;
        border: 1px solid #fff;
    }

    td {
        padding-left: 10px;
        padding-right: 10px;
    }

    tr.e {
        background-color: #E4ECFC;
        height: 35px;
    }

    th {
        height: 35px;
        background-color: #A9D0F5;
        padding: 10px;
        color: #08088A;
        font-size: 14px;
        font-weight: lighter;
    }

    ul {
        margin-top: 0;
        padding: 0;
    }

    #custom {
        margin-top: 15px;
    }

    #stat {
        width: 90%;
        height: 600px;
        background-color: #f0f8ff;
        margin: 0 auto;
        margin-top: 20px;
        margin-bottom: 20px;
        display: block;
        border: #87cefa 1px solid;
    }

    #n, #s {
        width: 90%;
        height: 240px;
        padding: 0px;
        margin: 0 auto;
        margin-top: 10px;
    }

    #nw, #sw {
        width: 48%;
        height: 240px;
        position: relative;
        float: left;
        margin: auto;
        border: #87cefa 1px solid;
    }

    #ne, #se {
        width: 48%;
        height: 240px;
        position: relative;
        float: right;
        margin: auto;
        border: #87cefa 1px solid;
    }

    #births, #deaths {
        width: 90%;
        height: 30px;
        margin: 0 auto;
        text-align: center;
        color: #333;
        font-size: 16px;
        padding-top: 10px;
    }

</style>

<div id="custom">
    <%--    <form action="#">--%>
    <table align="center" cellpadding="0" cellspacing="0" width="90%">
        <tbody>
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
            <td width="20%">DSDivision</td>
            <td width="30%">
                <s:select
                        id="dsDivision"
                        name="dsDivisionId"
                        list="divisionList"
                        />
            </td>
        </tr>
        <tr class="e">
            <td>Start Date</td>
            <td>
                <s:textfield id="sdate" name="startDate"/>
            </td>
            <td>End Date</td>
            <td>
                <s:textfield id="edate" name="endDate"/>
            </td>
        </tr>
        <tr class="e">
            <%--<td>DEO</td>
            <td>
                <s:select
                        id="deoUser"
                        name="deoUserId"
                        list="deoList"
                        />
            </td>--%>
            <td colspan="4"><s:submit/></td>
            <%--<td>&nbsp;</td>--%>
        </tr>
        </tbody>
    </table>
    <%--    </form>--%>
</div>
<div id="stat">
    <div id="births">Births Statistics</div>
    <div id="n">
        <div id="nw"></div>
        <div id="ne"></div>
    </div>

    <div id="deaths">Deaths Statistics</div>
    <div id="s">
        <div id="sw"></div>
        <div id="se"></div>
    </div>
</div>

</div>
