<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage/>
<script type="text/javascript" src="lib/jquery/jquery.jqplot.js"></script>
<script type="text/javascript" src="lib/jquery/jqplot.categoryAxisRenderer.js"></script>
<script type="text/javascript" src="lib/jquery/jqplot.barRenderer.js"></script>
<%--<script language="javascript" type="text/javascript" src="lib/jquery/dateAxisRenderer.js"></script>--%>
<link rel="stylesheet" type="text/css" href="css/jquery.jqplot.css"/>
<s:hidden id="userName" value="%{userName}"/>
<style type="text/css">
    .jqplot-target {
        position: relative;
        color: #666666;
        font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
        font-size: 1em;
        height: 450px;
        width: 800px;
        float: right;
    }
</style>


<div id="ADR-home-page-outer">
    <s:hidden id="totalDeclarations" value="%{totalDeclarations}"/>
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
    <s:hidden id="SBPendingApprovals" value="%{SBPendingApprovals}"/>


    <script type="text/javascript">
        $(document).ready(function() {
            $.jqplot.config.enablePlugins = true;

            line = [
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


            plot1 = $.jqplot('chart1', [line], {
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
            });


        });

    </script>
    <div class="jqplot" id="chart1"></div>

</div>
   