<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
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
<script type="text/javascript" src="../lib/ajaxsolr/widgets/BirthResultWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/DateRangeWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/AdvancedSearchWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/TagcloudWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/CurrentSearchWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/TextWidget.js"></script>

<script type="text/javascript" src="../lib/daterangepicker/daterangepicker.jQuery.js"></script>
<link rel="stylesheet" href="../css/ui.daterangepicker.css" type="text/css" />

<script>
  $(document).ready(function() {
    $("#tabs").tabs();
    $('#queryDOBRange').daterangepicker({arrows:true});
  });
</script>


<div id="wrap">

    <div id="queryPane">

        <div id="tabs">
            <ul>
                <li><a href="#fragment-1"><span>Basic Search</span></a></li>
                <li><a href="#fragment-2"><span>Name</span></a></li>
                <li><a href="#fragment-3"><span>Gender</span></a></li>
                <li><a href="#fragment-4"><span>District</span></a></li>
                <li><a href="#fragment-5"><span>Division</span></a></li>
                <li><a href="#fragment-6"><span>Date</span></a></li>
                <li><a href="#fragment-7"><span>Advanced Search</span></a></li>
            </ul>
            <div id="fragment-1">
                <div id="searchPIN">
                    <span>Search PIN Number</span>
                    <input type="text" id="queryPIN" name="queryPIN"/>
                </div>
                <div id="searchMothersNICorPIN">
                    <span>Search by Mothers NIC or PIN</span>
                    <input type="text" id="queryMothersNICorPIN" name="queryMothersNICorPIN"/>
                </div>
            </div>
            <div id="fragment-2">
                <div id="searchEnglish">
                    <span>Search full name in English</span>
                    <input type="text" id="queryEnglish" name="queryEnglish"/>
                </div>
                <div id="searchOfficial">
                    <span>Search full name in Official Language</span>
                    <input type="text" id="queryOfficial" name="queryOfficial"/>
                </div>
            </div>
            <div id="fragment-3">
                <span>Gender</span>
                <div class="tagcloud" id="childGender"></div>
                <div class="clear"></div>
            </div>
            <div id="fragment-4">
                <span>Registration District</span>
                <div class="tagcloud" id="birthDistrict"></div>
                <div class="clear"></div>
            </div>
            <div id="fragment-5">
                <span>Registration  Division</span>
                <div class="tagcloud" id="birthDivision"></div>
                <div class="clear"></div>
            </div>
            <div id="fragment-6">
                <div id="searchDOBRange">
                    <span>Choose Date of Birth Range</span>
                    <input type="text" id="queryDOBRange" name="queryDOBRange"/>
                </div>
            </div>
            <div id="fragment-7">
                <div id="searchAdvanced">
                    <span>Advanced Search</span>
                    <input type="text" id="queryAdvanced" name="queryAdvanced" size="117"/>
                </div>
            </div>
        </div>

        <ul id="selection"></ul>

    </div>

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