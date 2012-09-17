<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="../js/validate.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        $('#gnDivision-list').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "aaSorting": [
                [3,'asc']
            ],
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers",
            "iDisplayLength": 15
        });

        $('select#userDistrictId').bind('change', function(evt3) {
            var id = $("select#userDistrictId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:16},
                    function(data) {
                        var options = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivisionId").html(options);
                    });
        });

        $('select#selectDistrictId').bind('change', function(evt3) {
            var id = $("select#selectDistrictId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:16},
                    function(data) {
                        var options = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#selectDSDivisionId").html(options);
                    });
        });
    });

    function initPage() {
    }

    function submitSelected(){
        // TODO submit selected gnDivisions, currently selecting items only in current page(page loaded when submitting)
    }
</script>

<s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
<s:actionerror cssStyle="color:red;"/>
<s:form name="gnDivisionLoad" method="post" action="eprInitRearrangeDivision.do">
    <fieldset style="border:2px solid #c3dcee;margin-left:15px;margin-right:5%;margin-top:1.5em;width:95%">
    <legend><b>Current Grama Niladhari Division Assignment</b></legend>

    <table cellspacing="0" style="margin-top:2px;margin-bottom:10px;width:100%">
        <col width="10%"/>
        <col width="25%"/>
        <col width="5%"/>
        <col width="25%"/>
        <col width="30%"/>
        <col width="5%"/>

        <tr style="height:35px;">
            <td align="right">District :</td>
            <td>
                <s:select id="userDistrictId" name="userDistrictId" list="districtList" cssStyle="width:90%;"/>
            </td>
            <td></td>
            <td align="right">Divisional Secretary Division :</td>
            <td>
                <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" cssStyle="width:90%;"/>
            </td>
            <td align="right">
                <div class="form-submit" style="float:right;">
                    <s:submit value="Load" cssStyle="margin-right:30px;" name="button"/>
                </div>
            </td>
        </tr>
    </table>
</s:form>

<s:form name="gnDivisionReArrange" method="post" action="eprRearrangeDivision.do">
    <table id="gnDivision-list" cellpadding="0" cellspacing="0" class="display">
        <thead>
        <tr>
            <th width="10%">Code</th>
            <th width="70%">Grama Niladhari Division</th>
            <th width="18%">District</th>
            <th width="2%"></th>
        </tr>
        </thead>
        <tbody>
        <s:iterator status="status" value="gnDivisionNameList" id="gnDivisionList">
            <tr>
                <td align="center"><s:property value="gnDivisionId"/></td>
                <td><s:property value="enGNDivisionName"/> / <s:property value="siGNDivisionName"/></td>
                <td><s:property value="dsDivision.district.enDistrictName"/></td>
                <td>
                    <s:checkbox name="gnDivisions" fieldValue="%{#gnDivisionList.gnDivisionUKey}"/>
                </td>
            </tr>
        </s:iterator>
        </tbody>
    </table>

    </fieldset>

    <fieldset style="border:2px solid #c3dcee;margin-left:15px;margin-right:5%;margin-top:1.5em;width:95%">
        <legend><b>New Grama Niladhari Division Assignment</b></legend>

        <table cellspacing="0" style="margin-top:2px;margin-bottom:10px;width:100%;">
            <col width="10%"/>
            <col width="25%"/>
            <col width="5%"/>
            <col width="25%"/>
            <col width="30%"/>
            <col width="5%"/>

            <tr style="height:35px;">
                <td align="right">District :</td>
                <td>
                    <s:select id="selectDistrictId" name="selectDistrictId" list="districtList" headerKey="0"
                              headerValue="Select" cssStyle="width:90%;"/>
                </td>
                <td></td>
                <td align="right">Divisional Secretary Division :</td>
                <td>
                    <s:select id="selectDSDivisionId" name="selectDSDivisionId" list="dsDivisionList" headerKey="0"
                              headerValue="Select" cssStyle="width:90%;"/>
                </td>
                <td>
                    <div class="form-submit">
                        <s:submit value="Re-Arrange" name="button"/>
                    </div>
                </td>
            </tr>
        </table>
    </fieldset>

    <s:hidden name="userDistrictId" value="%{userDistrictId}"/>
    <s:hidden name="dsDivisionId" value="%{dsDivisionId}"/>
</s:form>