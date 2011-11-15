/**
 * @author shan
 */

function StatObject() {
    this.userRole = "none";
    this.late_b = 0;
    this.normal_b = 0;
    this.rejected_b = 0;
    this.approved_b = 0;
    this.arrears_pend_b = 0;
    this.thismonth_pend_b = 0;
    this.total_submitted_b = 0;
    this.still_b = 0;
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

    // alert("Role : " + data.userRole);
    /* --- Births Pie Chart --- */
    if (data.approved_b == 0 && data.rejected_b == 0 && data.arrears_pend_b == 0 && data.thismonth_pend_b == 0) {
        if (data.userRole == 'RG' || data.userRole == 'ARG' || data.userRole == 'ADMIN') {
            var ele = document.getElementById("birth-pie");
            if (ele != null) {
                // alert("inner html data empty 1");
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("chart3");
            if (ele != null) {
                // alert("inner html data empty chart 3");
                ele.innerHTML = html_code;
            }
        }
    } else {
        if (data.userRole == 'RG' || data.userRole == 'ADMIN' || data.userRole == 'ARG') {
            var ele = document.getElementById("birth-pie");
            if (ele != null) {
                // alert("birth-pie");
                ele.innerHTML = null;
                drawPieChart(data, 'birth-pie', 'birth');
            }
        } else {
            var ele = document.getElementById("chart3");
            if (ele != null) {
                // alert("birth-pie chart3");
                drawPieChart(data, 'chart3', 'birth');
            }
        }
    }

    /* --- Deaths Pie Chart --- */
    if (data.approved_d == 0 && data.rejected_d == 0 && data.arrears_pend_d == 0 && data.thismonth_pend_d == 0) {
        if (data.userRole == 'RG' || data.userRole == 'ADMIN' || data.userRole == 'ARG') {
            var ele = document.getElementById("death-pie");
            if (ele != null) {
                // alert("death pie- no data");
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("chart6");
            if (ele != null) {
                // alert("death chart6- no data");
                ele.innerHTML = html_code;
            }
        }
    } else {
        if (data.userRole == 'RG' || data.userRole == 'ADMIN' || data.userRole == 'ARG') {
            var ele = document.getElementById("death-pie");
            if (ele != null) {
                // alert("death-pie");
                ele.innerHTML = "";
                drawPieChart(data, 'death-pie', 'death');
            }
        } else {
            var ele = document.getElementById("chart6");
            if (ele != null) {
                // alert("death-pie chart6");
                drawPieChart(data, 'chart6', 'death');
            }
        }
    }

    /* --- Marriages Pie Chart  */
    // alert(data.approved_m + ", " + data.rejected_m + ", " + data.arrears_pend_m + ", " + data.thismonth_pend_m);
    if (data.approved_m == 0 && data.rejected_m == 0 && data.arrears_pend_m == 0 && data.thismonth_pend_m == 0) {
        if (data.userRole == 'RG' || data.userRole == 'ADMIN' || data.userRole == 'ARG') {
            var ele = document.getElementById("mrg-pie");
            if (ele != null) {
                // alert("marriage-pie no data");
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("chart9");
            if (ele != null) {
                // alert("marriage chart 9 - no data");
                ele.innerHTML = html_code;
            }
        }
    } else {
        if (data.userRole == 'RG' || data.userRole == 'ADMIN' || data.userRole == 'ARG') {
            var ele = document.getElementById("mrg-pie");
            if (ele != null) {
                // alert("marriage-pie");
                drawPieChart(data, 'mrg-pie', 'mrg');
            }
        } else {
            var ele = document.getElementById("chart9");
            if (ele != null) {
                // alert("marriage-pie chart 9");
                drawPieChart(data, 'chart9', 'mrg');
            }
        }
    }

    if (data.userRole == 'RG' || data.userRole == 'ADMIN' || data.userRole == 'ARG') {

        if (data.arrears_pend_b == 0 && data.thismonth_pend_b == 0) {
            var ele = document.getElementById("birth-bar");
            if (ele != null) {
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("birth-bar");
            if (ele != null) {
                ele.innerHTML = null;
                drawHorizontalBarChart(
                        data.arrears_pend_b,
                        data.thismonth_pend_b,
                        "Arrears",
                        "This Month",
                        "birth-bar"
                        );
            }
        }

        if (data.arrears_pend_d == 0 && data.thismonth_pend_d == 0) {
            var ele = document.getElementById("death-bar");
            if (ele != null) {
                // alert("death bar-no data")
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("death-bar");
            if (ele != null) {
                // alert("death bar-with data")
                ele.innerHTML = null;
                drawHorizontalBarChart(
                        data.arrears_pend_d,
                        data.thismonth_pend_d,
                        "Arrears",
                        "This Month",
                        "death-bar"
                        );
            }
        }

        if (data.arrears_pend_m == 0 && data.thismonth_pend_m == 0) {
            var ele = document.getElementById("mrg-bar");
            if (ele != null) {
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("mrg-bar");
            if (ele != null) {
                ele.innerHTML = null;
                drawHorizontalBarChart(
                        data.arrears_pend_m,
                        data.thismonth_pend_m,
                        "Arrears",
                        "This Month",
                        "mrg-bar"
                        );
            }
        }

        var ele = document.getElementById("all_pending_b");
        if (ele != null) {
            document.getElementById("all_pending_b").setAttribute("value", (data.arrears_pend_b + data.thismonth_pend_b));
        }

        var ele = document.getElementById("arrears_b");
        if (ele != null) {
            document.getElementById("arrears_b").setAttribute("value", data.arrears_pend_b);
        }

        var ele = document.getElementById("total_submitted_b");
        if (ele != null) {
            document.getElementById("total_submitted_b").setAttribute("value", (data.late_b + data.normal_b));
//            document.getElementById("total_submitted_b").setAttribute("value", (data.total_submitted_b));
        }

        var ele = document.getElementById("late_b");
        if (ele != null) {
            document.getElementById("late_b").setAttribute("value", data.late_b);
        }

        var ele = document.getElementById("all_pending_d");
        if (ele != null) {
            document.getElementById("all_pending_d").setAttribute("value", (data.arrears_pend_d + data.thismonth_pend_d));
        }
        var ele = document.getElementById("arrears_d");
        if (ele != null) {
            document.getElementById("arrears_d").setAttribute("value", data.arrears_pend_d);
        }

        var ele = document.getElementById("total_submitted_d");
        if (ele != null) {
            document.getElementById("total_submitted_d").setAttribute("value", (data.late_d + data.normal_d));
        }
        var ele = document.getElementById("late_d");
        if (ele != null) {
            document.getElementById("late_d").setAttribute("value", data.late_d);
        }

        var ele = document.getElementById("all_pending_m");
        if (ele != null) {
            document.getElementById("all_pending_m").setAttribute("value", (data.arrears_pend_m + data.thismonth_pend_m));
        }
        var ele = document.getElementById("arrears_m");
        if (ele != null) {
            document.getElementById("arrears_m").setAttribute("value", data.arrears_pend_m);
        }

        var ele = document.getElementById("total_submitted_m");
        if (ele != null) {
            document.getElementById("total_submitted_m").setAttribute("value", (data.late_m + data.normal_m));
        }
        var ele = document.getElementById("late_m");
        if (ele != null) {
            document.getElementById("late_m").setAttribute("value", data.late_m);
        }


    } else {

        if (data.arrears_pend_b == 0 && data.thismonth_pend_b == 0) {
            var ele = document.getElementById("chart1");
            if (ele != null) {
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("chart1");
            if (ele != null) {
                drawHorizontalBarChart(
                        data.arrears_pend_b,
                        data.thismonth_pend_b,
                        "Arrears",
                        "This Month",
                        "chart1"
                        );
            }
        }

        var ele = document.getElementById("all_pending_b");
        if (ele != null) {
            document.getElementById("all_pending_b").setAttribute("value", (data.arrears_pend_b + data.thismonth_pend_b));
        }
        var ele = document.getElementById("arrears_b");
        if (ele != null) {
            document.getElementById("arrears_b").setAttribute("value", data.arrears_pend_b);
        }

        if (data.late_b == 0 && data.normal_b == 0) {
            var ele = document.getElementById("chart2");
            if (ele != null) {
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("chart2");
            if (ele != null) {
                ele.innerHTML = null;
                drawHorizontalBarChart(
                        data.late_b,
                        data.normal_b,
                        "Late",
                        "Normal",
                        "chart2"
                        );
            }
        }

//        var ele = document.getElementById("total_submitted_b");
//        if (ele != null) {
//            document.getElementById("total_submitted_b").setAttribute("value", (data.late_b + data.normal_b));
////            document.getElementById("total_submitted_b").setAttribute("value", (data.total_submitted_b));
//        }

        var ele = document.getElementById("late_b");
        if (ele != null) {
            document.getElementById("late_b").setAttribute("value", data.late_b);
        }

        // -------------------------
        if (data.arrears_pend_d == 0 && data.thismonth_pend_d == 0) {
            var ele = document.getElementById("chart4");
            if (ele != null) {
                // alert("death chart4- no data");
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("chart4");
            // alert("death bar chart");
            if (ele != null) {
                ele.innerHTML = null;
                drawHorizontalBarChart(
                        data.arrears_pend_d,
                        data.thismonth_pend_d,
                        "Arrears",
                        "This Month",
                        "chart4"
                        );
            }
        }
        var ele = document.getElementById("all_pending_d");
        if (ele != null) {
            document.getElementById("all_pending_d").setAttribute("value", (data.arrears_pend_d + data.thismonth_pend_d));
        }
        var ele = document.getElementById("arrears_d");
        if (ele != null) {
            document.getElementById("arrears_d").setAttribute("value", data.arrears_pend_d);
        }

        if (data.late_d == 0 && data.normal_d == 0) {
            var ele = document.getElementById("chart5");
            if (ele != null) {
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("chart5");
            if (ele != null) {
                ele.innerHTML = null;
                drawHorizontalBarChart(
                        data.late_d,
                        data.normal_d,
                        "Late",
                        "Normal",
                        "chart5"
                        );
            }
        }

        var ele = document.getElementById("total_submitted_d");
        if (ele != null) {
            document.getElementById("total_submitted_d").setAttribute("value", (data.late_d + data.normal_d));
        }
        var ele = document.getElementById("late_d");
        if (ele != null) {
            document.getElementById("late_d").setAttribute("value", data.late_d);
        }

        // -------------------------
        if (data.arrears_pend_m == 0 && data.thismonth_pend_m == 0) {
            var ele = document.getElementById("chart7");
            if (ele != null) {
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("chart7");
            if (ele != null) {
                ele.innerHTML = null;
                drawHorizontalBarChart(
                        data.arrears_pend_m,
                        data.thismonth_pend_m,
                        "Arrears",
                        "This Month",
                        "chart7"
                        );
            }
        }
        var ele = document.getElementById("all_pending_m");
        if (ele != null) {
            document.getElementById("all_pending_m").setAttribute("value", (data.arrears_pend_m + data.thismonth_pend_m));
        }
        var ele = document.getElementById("arrears_m");
        if (ele != null) {
            document.getElementById("arrears_m").setAttribute("value", data.arrears_pend_m);
        }

        if (data.late_m == 0 && data.normal_m == 0) {
            var ele = document.getElementById("chart8");
            if (ele != null) {
                ele.innerHTML = html_code;
            }
        } else {
            var ele = document.getElementById("chart8");
            if (ele != null) {
                ele.innerHTML = null;
                drawHorizontalBarChart(
                        data.late_m,
                        data.normal_m,
                        "Late",
                        "Normal",
                        "chart8"
                        );
            }
        }

        var ele = document.getElementById("total_submitted_m");
        if (ele != null) {
            document.getElementById("total_submitted_m").setAttribute("value", (data.late_m + data.normal_m));
        }
        var ele = document.getElementById("late_m");
        if (ele != null) {
            document.getElementById("late_m").setAttribute("value", data.late_m);
        }

    }
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
        ['Approved',approved],
        ['Rejected',rejected],
        ['Approval<br/>Pending',pending]
    ];
    plot = $.jqplot(name, [line], {
        title: '',
        seriesDefaults:{
            renderer:$.jqplot.PieRenderer,
            rendererOptions: {
                // Put data labels on the pie slices. By default, labels show the percentage of the slice.
                showDataLabels: true
            }
        },
        legend:{ show:true }
    });
}



