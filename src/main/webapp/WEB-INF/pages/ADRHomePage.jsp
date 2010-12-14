<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage/>
<script type="text/javascript" src="lib/jquery/jquery.jqplot.js"></script>
<script type="text/javascript" src="lib/jquery/jqplot.categoryAxisRenderer.js"></script>
<script type="text/javascript" src="lib/jquery/jqplot.barRenderer.js"></script>
<script type="text/javascript" src="lib/jquery/jqplot.pieRenderer.min.js"></script>
<%--<script language="javascript" type="text/javascript" src="lib/jquery/dateAxisRenderer.js"></script>--%>
<link rel="stylesheet" type="text/css" href="css/jquery.jqplot.css"/>
<s:hidden id="userName" value="%{userName}"/>
<style type="text/css">
    .jqplot-target {
        color: #666666;
        font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
        font-size: 1em;
    }

    #charts {
        width: 850px;
        margin: 0 auto;
        padding: 0;
    }

    #bTitle {
        width: 850px;
        color: #000;
        font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
        font-size: 16px;
        text-align: center;
        margin-top: 10px;
        height: 30px;
        clear: both;
    }

    #chart1 {
        width: 430px;
        height: 262px;
        float: left;
        margin-right: 5px;
    }

    #chart2 {
        width: 400px;
        height: 248px;
    }

    #dTitle {
        width: 850px;
        color: #000;
        font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
        font-size: 16px;
        text-align: center;
        margin-top: 286px;
        height: 30px;
        clear: both;
    }

    #chart3 {
        width: 430px;
        height: 262px;
        float: left;
        margin-right: 5px;
    }

    #chart4 {
        width: 400px;
        height: 248px;
        margin-left: 5px;
    }
</style>


<div id="ADR-home-page-outer">
    <%--<s:hidden id="totalDeclarations" value="%{totalDeclarations}"/>
    <s:hidden id="totalDecArrivals" value="%{totalDecArrivals}"/>
    <s:hidden id="approvalPendings" value="%{approvalPendings}"/>
    <s:hidden id="totalConfirmChagess" value="%{totalConfirmChages}"/>
    <s:hidden id="confirmApproved" value="%{confirmApproved}"/>
    <s:hidden id="confirmApprovedPending" value="%{confirmApprovedPending}"/>
    <s:hidden id="confirmPrinted" value="%{confirmPrinted}"/>
    <s:hidden id="confimPrintingPending" value="%{confimPrintingPending}"/>
    <s:hidden id="BCprinted" value="%{BCprinted}"/>
    <s:hidden id="BCPrintPendings" value="%{BCPrintPendings}"/>
    <s:hidden id="stillBirths" value="%{stillBirths}"/>
    <s:hidden id="SBPendingApprovals" value="%{SBPendingApprovals}"/>--%>


    <script type="text/javascript">

        $(document).ready(function() {
            var user = 'adr';
            var mode = 'all';
            $.getJSON('/ecivil/crs/StatisticsLookupService',
            {
                userType:user,
                statType:mode
            },
                    function(data) {
                        $("#totalDeclarations").attr("value", data.totalDeclarations);
                        $("#totalDecArrivals").attr("value", data.totalDecArrivals);
                        $("#approvalPendings").attr("value", data.approvalPendings);
                        $("#totalConfirmChagess").attr("value", data.totalConfirmChagess);
                        $("#confirmApproved").attr("value", data.confirmApproved);

                        $.jqplot.config.enablePlugins = true;

                        /*line1 = [
                         ['Total <br/> Declarations <br/> This Month', document.getElementById("totalDeclarations").value],
                         ['Total <br/> Declaration <br/> Arrivals', document.getElementById("totalDecArrivals").value],
                         [' Pending <br/> Approvals',document.getElementById("approvalPendings").value],
                         ['Total <br/> Confirmation <br/> Changes <br/> Entered', document.getElementById("totalConfirmChagess").value],
                         ['Confirmation <br/> Changes <br/> Approved',  document.getElementById("confirmApproved").value],
                         ['Confirmation <br/> Changes <br/> Approval <br/> Pending', document.getElementById("confirmApprovedPending").value],
                         ['Confirmations <br/> Printed',document.getElementById("confirmPrinted").value],
                         ['Confirmation  <br/> Printing <br/> Pending', document.getElementById("confimPrintingPending").value],
                         [ 'Birth <br/> Certifications <br/> Printed', document.getElementById("BCprinted").value],
                         ['Birth <br/> Certifications <br/> Print <br/> Pending', document.getElementById("BCPrintPendings").value],
                         ['Still <br/> Births', document.getElementById("stillBirths").value],
                         ['Still <br/> Births <br/> Pending <br/> Approvals', document.getElementById("SBPendingApprovals").value]
                         ];


                         plot1 = $.jqplot('chart1', [line1], {
                         title: 'ADR',
                         series:[
                         {
                         renderer:$.jqplot.BarRenderer
                         }
                         ],
                         axes: {
                         xaxis: {
                         renderer: $.jqplot.CategoryAxisRenderer,
                         label: ''


                         },
                         yaxis: {
                         autoscale:true,
                         label: ''

                         }
                         }
                         });*/

                        /* --- Births Bar Chart --- */

                        line1 = [
                            ['submitted<br/>Items',data.submitted_b],
                            ['approved<br/>Items',data.approved_b],
                            ['rejected<br/>Items',data.rejected_b],
                            ['pending<br/>Items',data.pending_b]
                        ];

                        plot1 = $.jqplot('chart1', [line1], {
                            title: '',
                            series:[
                                {
                                    renderer:$.jqplot.BarRenderer
                                }
                            ],
                            axes: {
                                xaxis: {
                                    renderer: $.jqplot.CategoryAxisRenderer,
                                    label: ''
                                },
                                yaxis: {
                                    autoscale:true,
                                    label: ''
                                }
                            }
                        });

                        /* --- Births Pie Chart --- */

                        line2 = [
                            ['submitted<br/>Items',data.submitted_b],
                            ['approved<br/>Items',data.approved_b],
                            ['rejected<br/>Items',data.rejected_b],
                            ['pending<br/>Items',data.pending_b]
                        ];
                        plot2 = $.jqplot('chart2', [line2], {
                            title: '',
                            seriesDefaults:{ renderer:$.jqplot.PieRenderer
                                /* rendererOptions:{sliceMargin:8}*/},
                            legend:{ show:true }
                        });

                        /* --- Deaths Bar Chart --- */

                        line3 = [
                            ['submitted<br/>Items',data.submitted_d],
                            ['approved<br/>Items',data.approved_d],
                            ['rejected<br/>Items',data.rejected_d],
                            ['pending<br/>Items',data.pending_d]
                        ];

                        plot3 = $.jqplot('chart3', [line3], {
                            title: '',
                            series:[
                                {
                                    renderer:$.jqplot.BarRenderer
                                }
                            ],
                            axes: {
                                xaxis: {
                                    renderer: $.jqplot.CategoryAxisRenderer,
                                    label: ''
                                },
                                yaxis: {
                                    autoscale:true,
                                    label: ''
                                }
                            }
                        });

                        /* --- Deaths Pie Chart --- */

                        line4 = [
                            ['submitted<br/>Items',data.submitted_d],
                            ['approved<br/>Items',data.approved_d],
                            ['rejected<br/>Items',data.rejected_d],
                            ['pending<br/>Items',data.pending_d]
                        ];
                        plot4 = $.jqplot('chart4', [line4], {
                            title: '',
                            seriesDefaults:{ renderer:$.jqplot.PieRenderer
                                /* rendererOptions:{sliceMargin:8}*/},
                            legend:{ show:true }
                        });

                    });

        });

        function initPage() {
        }

    </script>

    <div id="charts">
        <div id="bTitle">Births Statistics</div>
        <div class="jqplot" id="chart1"></div>
        <div class="jqplot" id="chart2"></div>
        <div id="dTitle">Deaths Statistics</div>
        <div class="jqplot" id="chart3"></div>
        <div class="jqplot" id="chart4"></div>
    </div>

</div>
   