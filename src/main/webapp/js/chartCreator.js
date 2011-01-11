/**
 * @author shan
 */

function StatObject() {
    this.late_b = 0;
    this.normal_b = 0;
    this.rejected_b = 0;
    this.approved_b = 0;
    this.arrears_pend_b = 0;
    this.thismonth_pend_b = 0;
    this.late_d = 0;
    this.normal_d = 0;
    this.rejected_d = 0;
    this.approved_d = 0;
    this.arrears_pend_d = 0;
    this.thismonth_pend_d = 0;
    this.late_m = 0;
    this.normal_m = 0;
    this.rejected_m = 0;
    this.approved_m = 0;
    this.arrears_pend_m = 0;
    this.thismonth_pend_m = 0;
}

var html_code = "<i><font color='red'>No Data Available</font></i>";

function drawChart(data) {
    $.jqplot.config.enablePlugins = true;

    /* --- Births Pie Chart --- */
    if (data.approved_b == 0 && data.rejected_b == 0 && data.arrears_pend_b == 0 && data.thismonth_pend_b == 0) {
        document.getElementById("chart3").innerHTML = html_code;
    } else {
        drawPieChart(data, 'chart3', 'birth');
    }

    /* --- Deaths Pie Chart --- */
    if (data.approved_d == 0 && data.rejected_d == 0 && data.arrears_pend_d == 0 && data.thismonth_pend_d == 0) {
        document.getElementById("chart6").innerHTML = html_code;
    } else {
        drawPieChart(data, 'chart6', 'death');
    }

    /* --- Marriages Pie Chart  */
    if (data.approved_m == 0 && data.rejected_m == 0 && data.arrears_pend_m == 0 && data.thismonth_pend_m == 0) {
        document.getElementById("chart9").innerHTML = html_code;
    } else {
        drawPieChart(data, 'chart9', 'mrg');
    }

    if (data.arrears_pend_b == 0 && data.thismonth_pend_b == 0) {
        document.getElementById("chart1").innerHTML = html_code;
    } else {
        drawHorizontalBarChart(
                data.arrears_pend_b,
                data.thismonth_pend_b,
                "Arrears",
                "This Month",
                "chart1"
                );
    }

    document.getElementById("all_pending_b").setAttribute("value", (data.arrears_pend_b + data.thismonth_pend_b));
    document.getElementById("arrears_b").setAttribute("value", data.arrears_pend_b);

    if (data.late_b == 0 || data.normal_b == 0) {
        document.getElementById("chart2").innerHTML = html_code;
    } else {
        drawHorizontalBarChart(
                data.late_b,
                data.normal_b,
                "Late",
                "Normal",
                "chart2"
                );
    }

    document.getElementById("total_submitted_b").setAttribute("value", (data.late_b + data.normal_b));
    document.getElementById("late_b").setAttribute("value", data.late_b);

    // -------------------------

    if (data.arrears_pend_d == 0 && data.thismonth_pend_d == 0) {
        document.getElementById("chart4").innerHTML = html_code;
    } else {
        drawHorizontalBarChart(
                data.arrears_pend_d,
                data.thismonth_pend_d,
                "Arrears",
                "This Month",
                "chart4"
                );
    }

    document.getElementById("all_pending_d").setAttribute("value", (data.arrears_pend_d + data.thismonth_pend_d));
    document.getElementById("arrears_d").setAttribute("value", data.arrears_pend_d);

    if (data.late_d == 0 || data.normal_d == 0) {
        document.getElementById("chart5").innerHTML = html_code;
    } else {
        drawHorizontalBarChart(
                data.late_d,
                data.normal_d,
                "Arrears",
                "This Month",
                "chart5"
                );
    }

    document.getElementById("total_submitted_d").setAttribute("value", (data.late_d + data.normal_d));
    document.getElementById("late_d").setAttribute("value", data.late_d);

    // -------------------------
    if (data.arrears_pend_m == 0 && data.thismonth_pend_m == 0) {
        document.getElementById("chart7").innerHTML = html_code;
    } else {
        drawHorizontalBarChart(
                data.arrears_pend_m,
                data.thismonth_pend_m,
                "Arrears",
                "This Month",
                "chart7"
                );
    }

    document.getElementById("all_pending_m").setAttribute("value", (data.arrears_pend_m + data.thismonth_pend_m));
    document.getElementById("arrears_m").setAttribute("value", data.arrears_pend_m);

    if (data.late_m == 0 || data.normal_m == 0) {
        document.getElementById("chart8").innerHTML = html_code;
    } else {
        drawHorizontalBarChart(
                data.late_m,
                data.normal_m,
                "Late",
                "Normal",
                "chart8"
                );
    }

    document.getElementById("total_submitted_m").setAttribute("value", (data.late_m + data.normal_m));
    document.getElementById("late_m").setAttribute("value", data.late_m);

}

function drawHorizontalBarChart(val_1, val_2, name_1, name_2, position) {
    line1 = [
        [val_1, 1]
    ];
    line2 = [
        [val_2, 1]
    ];
    plot = $.jqplot(position, [line1, line2], {
        stackSeries: true,
        legend: {show: true},
        grid:{shadow:false, borderWidth:0.0, show:false},
        seriesDefaults: {
            renderer: $.jqplot.BarRenderer,
            rendererOptions: {barDirection: 'horizontal', barWidth:50},
            border:false
        },
        series: [
            {label: name_1},
            {label: name_2}
        ],
        seriesColors: [ "#43BFC7", "#8EEBEC" ],
        axes: {
            yaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: ['.']
            },
            xaxis: {min: 0, max: (val_1 + val_2), numberTicks:5}
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
        pending = data.arrears_pend_b + data.thismonth_pend_b;
    } else {
        if (bORd == 'death') {
            approved = data.approved_d;
            rejected = data.rejected_d;
            pending = data.arrears_pend_d + data.thismonth_pend_d;
        } else {
            if (bORd == 'mrg') {
                approved = data.approved_m;
                rejected = data.rejected_m;
                pending = data.arrears_pend_m + data.thismonth_pend_m;
            }
        }
    }

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



