<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage/>

<script type="text/javascript" src="lib/jquery/jquery.jqplot.js"></script>
<script type="text/javascript" src="lib/jquery/jqplot.categoryAxisRenderer.js"></script>
<script type="text/javascript" src="lib/jquery/jqplot.barRenderer.js"></script>
<script type="text/javascript" src="lib/jquery/jqplot.pieRenderer.min.js"></script>

<link rel="stylesheet" type="text/css" href="css/jquery.jqplot.css"/>
<s:hidden id="userName" value="%{userName}"/>

<style type="text/css">
    .jqplot-target {
        color: #666666;
        font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
        font-size: 1em;
    }

    #chart1, #chart2, #chart4, #chart5 {
        width: 430px;
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

</style>

<div id="ADR-home-page-outer">

<script type="text/javascript">

    $(document).ready(function() {
        var user = 'deo';
        var mode = 'all';
        $.getJSON('/ecivil/crs/StatisticsLookupService',
        {
            userType:user,
            statType:mode
        },
                function(data) {
                    $.jqplot.config.enablePlugins = true;

                    /* --- Births - All Pending Bar Chart --- */

                    var first = 4;
                    var second = 3;

                    line1_1 = [
                        [first, 1]
                    ];
                    line1_2 = [
                        [second, 1]
                    ];
                    plot1 = $.jqplot('chart1', [line1_1, line1_2], {
                        stackSeries: true,
                        legend: {show: true},
                        grid:{shadow:false, borderWidth:0.0, show:false},
                        seriesDefaults: {
                            renderer: $.jqplot.BarRenderer,
                            rendererOptions: {barDirection: 'horizontal', barWidth:50},
                            border:false
                        },
                        series: [
                            {label: 'Arrears'},
                            {label: 'This month'}
                        ],
                        seriesColors: [ "#C11B17", "#FAAFBE" ],
                        axes: {
                            yaxis: {
                                renderer: $.jqplot.CategoryAxisRenderer,
                                ticks: ['.']
                            },
                            xaxis: {min: 0, max: (first + second), numberTicks:2}
                        }
                    });

                    /* --- Births - Total Submitted Bar Chart --- */

                    first = 9;
                    second = 1;

                    line2_1 = [
                        [first, 1]
                    ];
                    line2_2 = [
                        [second, 1]
                    ];
                    plot2 = $.jqplot('chart2', [line2_1, line2_2], {
                        stackSeries: true,
                        legend: {show: true},
                        grid:{shadow:false, borderWidth:0.0, show:false},
                        seriesDefaults: {
                            renderer: $.jqplot.BarRenderer,
                            rendererOptions: {barDirection: 'horizontal', barWidth:50},
                            border:false
                        },
                        series: [
                            {label: 'Late'},
                            {label: 'Normal'}
                        ],
                        seriesColors: [ "#C11B17", "#FAAFBE" ],
                        axes: {
                            yaxis: {
                                renderer: $.jqplot.CategoryAxisRenderer,
                                ticks: ['.']
                            },
                            xaxis: {min: 0, max: (first + second), numberTicks:2}
                        }
                    });

                    /* --- Births Pie Chart --- */

                    line3 = [
                        ['approved<br/>Items',data.approved_b],
                        ['rejected<br/>Items',data.rejected_b],
                        ['pending<br/>Items',data.pending_b]
                    ];
                    plot3 = $.jqplot('chart3', [line3], {
                        title: '',
                        seriesDefaults:{ renderer:$.jqplot.PieRenderer
                            /* rendererOptions:{sliceMargin:8}*/},
                        legend:{
                            show:true,
                            rendererOptions:{direction:'horizontal'},
                            textColor: '#000'
                        }
                    });

                    /* --- Deaths - All Pending Bar Chart --- */

                    first = 2;
                    second = 3;

                    line4_1 = [
                        [first, 1]
                    ];
                    line4_2 = [
                        [second, 1]
                    ];
                    plot4 = $.jqplot('chart4', [line4_1, line4_2], {
                        stackSeries: true,
                        legend: {show: true},
                        grid:{shadow:false, borderWidth:0.0, show:false},
                        seriesDefaults: {
                            renderer: $.jqplot.BarRenderer,
                            rendererOptions: {barDirection: 'horizontal', barWidth:50},
                            border:false
                        },
                        series: [
                            {label: 'Arrears'},
                            {label: 'This month'}
                        ],
                        seriesColors: [ "#C11B17", "#FAAFBE" ],
                        axes: {
                            yaxis: {
                                renderer: $.jqplot.CategoryAxisRenderer,
                                ticks: ['.']
                            },
                            xaxis: {min: 0, max: (first + second), numberTicks:2}
                        }
                    });
                    ;

                    /* --- Deaths - Total Submitted Bar Chart --- */

                    first = 1;
                    second = 4;

                    line5_1 = [
                        [first, 1]
                    ];
                    line5_2 = [
                        [second, 1]
                    ];
                    plot5 = $.jqplot('chart5', [line5_1, line5_2], {
                        stackSeries: true,
                        legend: {show: true},
                        grid:{shadow:false, borderWidth:0.0, show:false},
                        seriesDefaults: {
                            renderer: $.jqplot.BarRenderer,
                            rendererOptions: {barDirection: 'horizontal', barWidth:50},
                            border:false
                        },
                        series: [
                            {label: 'Late'},
                            {label: 'Normal'}
                        ],
                        seriesColors: [ "#C11B17", "#FAAFBE" ],
                        axes: {
                            yaxis: {
                                renderer: $.jqplot.CategoryAxisRenderer,
                                ticks: ['.']
                            },
                            xaxis: {min: 0, max: (first + second), numberTicks:2}
                        }
                    });
                    ;

                    /* --- Deaths Pie Chart --- */

                    line6 = [
                        ['approved<br/>Items',data.approved_d],
                        ['rejected<br/>Items',data.rejected_d],
                        ['pending<br/>Items',data.pending_d]
                    ];
                    plot6 = $.jqplot('chart6', [line6], {
                        title: '',
                        seriesDefaults:{ renderer:$.jqplot.PieRenderer},
                        legend:{ show:true }
                    });

                });

    });

    function initPage() {
    }

</script>

<table border="0" width="100%">
    <tr>
        <td colspan="2" align="center">
            <div class="topic">Birth Statistics</div>
        </td>
    </tr>
    <tr>
        <td class="info">All Pending : 20</td>
        <td rowspan="4" class="pie">
            <div id="chart3"></div>
        </td>
    </tr>
    <tr>
        <td>
            <div id="chart1"></div>
        </td>
    </tr>
    <tr>
        <td class="info">Total Submitted Items : 16</td>
    </tr>
    <tr>
        <td>
            <div id="chart2"></div>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center">
            <div class="topic">Death Statistics</div>
        </td>
    </tr>
    <tr>
        <td class="info">All Pending : 23</td>
        <td rowspan="4" class="pie">
            <div id="chart6"></div>
        </td>
    </tr>
    <tr>
        <td>
            <div id="chart4"></div>
        </td>
    </tr>
    <tr>
        <td class="info">Total Submitted Items : 21</td>
    </tr>
    <tr>
        <td>
            <div id="chart5"></div>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>

</div>