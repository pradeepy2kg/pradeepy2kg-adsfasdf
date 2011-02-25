<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<link rel="stylesheet" type="text/css" href="../css/solrsearch.css" media="screen"/>

<link rel="stylesheet" type="text/css" href="ext/smoothness/jquery-ui.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="ext/smoothness/ui.theme.css" media="screen"/>

<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<script type="text/javascript" src="../lib/ajaxsolr/core/Core.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/core/AbstractManager.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/core/Parameter.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/core/ParameterStore.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/core/AbstractWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/core/AbstractFacetWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/managers/Manager.jquery.js"></script>

<script type="text/javascript" src="../lib/ajaxsolr/helpers/jquery/ajaxsolr.theme.js"></script>
<script type="text/javascript" src="../lib/livejquery/jquery.livequery.js"></script>
<script type="text/javascript" src="../lib/jqueryui/jquery-ui.min.js"></script>

<script type="text/javascript" src="../js/solrsearch.theme.js"></script>
<script type="text/javascript" src="../js/prssearch.js"></script>

<script type="text/javascript" src="../lib/ajaxsolr/widgets/jquery/PagerWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/PRSResultWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/DateRangeWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/DateSelectWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/AdvancedSearchWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/TagcloudWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/CurrentSearchWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/TextWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/TermTextWidget.js"></script>

<script type="text/javascript" src="../lib/daterangepicker/daterangepicker.jQuery.js"></script>
<link rel="stylesheet" href="../css/ui.daterangepicker.css" type="text/css" />

<script>
  $(document).ready(function() {
    $("#tabs").tabs();
    $('#queryDOBRange').daterangepicker({
        arrows:true,
        onClose: function() {
            $('#queryDOBRange').focus();
        }
    });

    $('#queryDODRange').daterangepicker({
        arrows:true,
        onClose: function() {
            $('#queryDODRange').focus();
        }
    });

    $('#queryDOB').datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31',
        onSelect: function() {
            $(this).focus();
        }
    });

    $('#queryDOD').datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31',
        onSelect: function() {
            $(this).focus();
        }
    });
  });

  function initPage(){}
</script>


<div id="wrap">

    <div id="queryPane">

        <div id="tabs">
            <ul>
                <li><a href="#fragment-1"><span>Names and Numbers</span></a></li>
                <li><a href="#fragment-2"><span>Statuses, Gender and Citizenship</span></a></li>
                <li><a href="#fragment-3"><span>Birth and Death</span></a></li>
                <li><a href="#fragment-4"><span>Address and Contact</span></a></li>
                <%--li><a href="#fragment-5"><span>Advanced</span></a></li--%>
            </ul>
            <div id="fragment-1">
                <table>
                    <tr><td><span>Search full name in English</span></td>
                    <td><div id="searchEnglish"><input type="text" id="searchEnglish" name="searchEnglish"/></div></td>
                    <td><span>PIN</span></td>
                    <td><div id="searchPIN"><input type="text" id="searchPIN" name="searchPIN" maxlength="12"/></div></td></tr>

                    <tr><td><span>Search full name in Official Language</span></td>
                    <td><div id="searchOfficial"><input type="text" id="searchOfficial" name="searchOfficial"/></div></td>
                    <td><span>NIC</span></td>
                    <td><div id="searchNIC"><input type="text" id="searchNic" name="searchNic" maxlength="10" style="text-transform:uppercase;"/></div></td></tr>

                    <tr><td><span>Search for name</span></td>
                    <td><div id="searchAllNames"><input type="text" id="searchAllNames" name="searchAllNames"/></div></td>
                    <td><span>Passport No</span></td>
                    <td><div id="searchPassport"><input type="text" id="searchPassport" name="searchPassport" maxlength="15" style="text-transform:uppercase;"/></div></td></tr>
                </table>
            </div>
            <div id="fragment-2">
                <table>
                    <tr><td><span>Gender</span></td><td><div class="tagcloud" id="gender"></div></td></tr>
                    <tr><td><span>Life Status</span></td><td><div class="tagcloud" id="lifeStatus"></div></td></tr>
                    <tr><td><span>Civil Status</span></td><td><div class="tagcloud" id="civilStatus"></div></td></tr>
                    <tr><td><span>Record Status</span></td><td><div class="tagcloud" id="recordStatus"></div></td></tr>
                    <tr><td><span>Citizenship</span></td><td><div class="tagcloud" id="citizenship"></div></td></tr>
                    <tr><td></td><td></td></tr>
                </table>
            </div>
            <div id="fragment-3">
                <table>
                    <tr><td><span>Date of Birth</span></td>
                    <td><div id="searchDOB"><input type="text" id="queryDOB" name="queryDOB" maxlength="10"/></div></td>
                        <td><span>Date of Birth Range</span></td>
                    <td><div id="searchDOBRange"><input type="text" id="queryDOBRange" name="queryDOBRange"/></div></td></tr>
                    <tr><td><span>Place of Birth</span></td>
                    <td><div id="searchPOB"><input type="text" id="queryPOB" name="queryPOB"/></div></td><td></td></tr>
                    <tr></tr>
                    <tr><td><span>Date of Death</span></td>
                    <td><div id="searchDOD"><input type="text" id="queryDOD" name="queryDOD" maxlength="10"/></div></td>
                        <td><span>Date of Death Range</span></td>
                    <td><div id="searchDODRange"><input type="text" id="queryDODRange" name="queryDODRange"/></div></td></tr>
                </table>
            </div>
            <div id="fragment-4">
                <table>
                    <tr><td><span>Current Address</span></td>
                    <td><div id="searchLastAddress"><input type="text" id="queryLastAddress" name="queryLastAddress"/></div></td>
                        <td><span>All Addresses</span></td>
                    <td><div id="searchAllAddresses"><input type="text" id="queryAllAddresses" name="queryAllAddresses"/></div></td></tr>
                    <tr><td><span>Phone</span></td>
                    <td><div id="searchPhone"><input type="text" id="queryPhone" name="queryPhone" maxlength="15"/></div></td>
                        <td><span>Email</span></td>
                    <td><div id="searchEmail"><input type="text" id="queryEmail" name="queryEmail" style="text-transform:none;"/></div></td></tr>
                </table>
            </div>
            <%--div id="fragment-5">
                <div id="searchAdvanced">
                    <span>Advanced Search</span>
                    <input type="text" id="queryAdvanced" name="queryAdvanced" size="117"  style="text-transform:none;" value="*:*"/>
                </div>
            </div--%>
        </div>

        <ul id="selection"></ul>
    </div>
    <s:actionerror cssClass="alreadyPrinted" cssStyle="font-size:10pt;"/>

    <fieldset style="border:none">
        <div id="result">
            <div id="navigation">
                <ul id="pager"></ul>
                <div id="pager-header"></div>
            </div>
            <div id="docs"></div>
        </div>
        <div class="clear"></div>
    </fieldset>
</div>