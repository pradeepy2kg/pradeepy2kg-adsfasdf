/**
 * @author shan
 */

function drawChart(data) {
    $.jqplot.config.enablePlugins = true;

    /* --- Births - All Pending Bar Chart --- */

    var first = data.arrears_b;
    var second = data.this_month_b;
    document.getElementById("all_pending_b").setAttribute("value", (first + second));
    document.getElementById("arrears_b").setAttribute("value", first);

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
            xaxis: {min: 0, max: (first + second), numberTicks:5}
        }
    });

    /* --- Births - Total Submitted Bar Chart --- */

    first = data.late_b;
    second = data.normal_b;
    document.getElementById("total_submitted_b").setAttribute("value", (first + second));
    document.getElementById("late_b").setAttribute("value", first);

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
            xaxis: {min: 0, max: (first + second), numberTicks:5}
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
            /*rendererOptions:{sliceMargin:8}*/},
        legend:{
            show:true,
            rendererOptions:{direction:'horizontal'},
            textColor: '#000'
        }
    });

    /* --- Deaths - All Pending Bar Chart --- */

    first = data.arrears_d;
    second = data.this_month_d;
    document.getElementById("all_pending_d").setAttribute("value", (first + second));
    document.getElementById("arrears_d").setAttribute("value", first);

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
            xaxis: {min: 0, max: (first + second), numberTicks:5}
        }
    });

    /* --- Deaths - Total Submitted Bar Chart --- */

    first = data.late_d;
    second = data.normal_d;
    document.getElementById("total_submitted_d").setAttribute("value", (first + second));
    document.getElementById("late_d").setAttribute("value", first);

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
            xaxis: {min: 0, max: (first + second), numberTicks:5}
        }
    });

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

}