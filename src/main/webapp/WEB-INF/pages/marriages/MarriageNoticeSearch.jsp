<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<script>
    $(document).ready(function() {
        $("#tabs").tabs();
    });

    $(function() {
        $('select#districtId').bind('change', function(evt1) {
            var id = $("select#districtId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:8},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivisionId").html(options1);

                        var options2 = '';
                        var bd = data.mrDivisionList;
                        options2 += '<option value="' + 0 + '">' + <s:label value="%{getText('all.divisions.label')}"/> + '</option>';
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#mrDivisionId").html(options2);
                    });
        });

        $('select#dsDivisionId').bind('change', function(evt2) {
            var id = $("select#dsDivisionId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:7},
                    function(data) {
                        var options = '';
                        var bd = data.mrDivisionList;
                        options += '<option value="' + 0 + '">' + <s:label value="%{getText('all.divisions.label')}"/> + '</option>';
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#mrDivisionId").html(options);
                    });
        })
    });

    function initPage() {
    }
</script>

<div id="tabs" style="font-size:10pt;">
    <ul>
        <li>
            <a href="#fragment-1"><span><s:label value="%{getText('search.by.MRDivision.label')}"/></span></a>
        </li>
        <li>
            <a href="#fragment-2"><span> <s:label value="%{getText('search.by.male.part.pin')}"/></span></a>
        </li>
        <li>
            <a href="#fragment-3"><span><s:label value="%{getText('search.by.female.part.pin')}"/></span></a>
        </li>
    </ul>

    <div id="fragment-1">
        <table>
            <caption/>
            <col width="250px"/>
            <col width="250px"/>
            <col width="24px"/>
            <col width="250px"/>
            <col width="250px"/>
            <tbody>
            <tr>
                <td>
                    <s:label value="%{getText('district.label')}"/>
                </td>
                <td>
                    <s:select id="districtId" name="districtId" list="districtList"
                              value="marriageDistrictId" cssStyle="width:98.5%; width:240px;"/>
                </td>
                <td></td>
                <td>
                    <s:label value="%{getText('select_DS_division.label')}"/>
                </td>
                <td>
                    <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="dsDivisionId"
                              cssStyle="width:98.5%; width:240px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label value="%{getText('select_BD_division.label')}"/>
                </td>
                <td>
                    <s:select id="mrDivisionId" name="mrDivisionId" list="mrDivisionList"
                              value="marriageDivisionId" headerKey="0" headerValue="%{getText('all.divisions.label')}"
                              cssStyle="width:98.5%; width:240px;"/>
                </td>
                <td></td>
                <td>
                    <s:label value="%{getText('label.serial.number')}"/>
                </td>
                <td>
                    <s:textfield value="" name="serialNumber" maxLength="10" cssStyle="width:235px"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div id="fragment-2">
        <table>
            <caption/>
            <col width="400px"/>
            <col width="75px"/>
            <col/>
            <tbody>
            <tr>
                <td>
                    <s:label value="%{getText('male.party.identification.number')}"/>
                </td>
                <td></td>
                <td>
                    <s:textfield name="#" value="" id="identification_male"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div id="fragment-3">
        <table>
            <caption/>
            <col width="400px"/>
            <col width="75px"/>
            <col/>
            <tbody>
            <tr>
                <td>
                    <s:label value="%{getText('female.party.identification.number')}"/>
                </td>
                <td></td>
                <td>
                    <s:textfield name="#" value="" id="identification_female"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

</div>
<div class="form-submit">
    <s:submit value="%{getText('bdfSearch.button')}"/>
</div>