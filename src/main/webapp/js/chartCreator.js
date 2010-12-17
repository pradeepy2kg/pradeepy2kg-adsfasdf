/**
 * @author shan
 */

function drawChart(data) {
    $.jqplot.config.enablePlugins = true;

    /* --- Births - All Pending Bar Chart --- */
    drawHorizontalBarChart(data, 'chart1', 'arrears_b', 'all_pending_b', 2, 'birth');

    /* --- Births - Total Submitted Bar Chart --- */
    drawHorizontalBarChart(data, 'chart2', 'late_b', 'total_submitted_b', 1, 'birth');

    /* --- Births Pie Chart --- */
    drawPieChart(data, 'chart3', 'birth');

    /* --- Deaths - All Pending Bar Chart --- */
    drawHorizontalBarChart(data, 'chart4', 'arrears_d', 'all_pending_d', 2, 'death');

    /* --- Deaths - Total Submitted Bar Chart --- */
    drawHorizontalBarChart(data, 'chart5', 'late_d', 'total_submitted_d', 1, 'death');

    /* --- Deaths Pie Chart --- */
    drawPieChart(data, 'chart6', 'death');

}

function drawHorizontalBarChart(data, divId, elem1, elem2, type, bORd) {

    var first = 0;
    var second = 0;

    if (bORd == 'birth') {
        if (type == 1) {
            first = data.late_b;
            second = data.normal_b;

            if (elem1 != null && elem2 != null) {
                document.getElementById(elem1).setAttribute("value", first);
                document.getElementById(elem2).setAttribute("value", (first + second));
            }
        } else {
            if (type == 2) {
                first = data.arrears_b;
                second = data.this_month_b;

                if (elem1 != null && elem2 != null) {
                    document.getElementById(elem1).setAttribute("value", first);
                    document.getElementById(elem2).setAttribute("value", (first + second));
                }

            }
        }
    } else {
        if (bORd == 'death') {
            if (type == 1) {
                first = data.late_d;
                second = data.normal_d;

                if (elem1 != null && elem2 != null) {
                    document.getElementById(elem1).setAttribute("value", first);
                    document.getElementById(elem2).setAttribute("value", (first + second));
                }
            } else {
                if (type == 2) {
                    first = data.arrears_d;
                    second = data.this_month_d;

                    if (elem1 != null && elem2 != null) {
                        document.getElementById(elem1).setAttribute("value", first);
                        document.getElementById(elem2).setAttribute("value", (first + second));
                    }

                }
            }
        }
    }

    line1 = [
        [first, 1]
    ];
    line2 = [
        [second, 1]
    ];
    plot = $.jqplot(divId, [line1, line2], {
        stackSeries: true,
        legend: {show: true},
        grid:{shadow:false, borderWidth:0.0, show:false},
        seriesDefaults: {
            renderer: $.jqplot.BarRenderer,
            rendererOptions: {barDirection: 'horizontal', barWidth:50},
            border:false
        },
        series: [
            {label: elem1},
            {label: elem2}
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

}

function drawPieChart(data, name, bORd) {
    var approved = 0;
    var rejected = 0;
    var pending = 0;
    if (bORd == 'birth') {
        approved = data.approved_b;
        rejected = data.rejected_b;
    } else {
        if (bORd == 'death') {
            approved = data.approved_d;
            rejected = data.rejected_d;
        }
    }
    pending = approved + rejected;

    line = [
        ['Approved<br/>Items',approved],
        ['Rejected<br/>Items',rejected],
        ['Pending<br/>Items',pending]
    ];
    plot = $.jqplot(name, [line], {
        title: '',
        seriesDefaults:{ renderer:$.jqplot.PieRenderer},
        legend:{ show:true }
    });
}
