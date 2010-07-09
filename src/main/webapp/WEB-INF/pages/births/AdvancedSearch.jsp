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

<script type="text/javascript" language="javascript" src="../lib/jquery/jquery.js"></script>

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
<script type="text/javascript" src="../js/solrsearch.js"></script>

<script type="text/javascript" src="../lib/ajaxsolr/widgets/jquery/PagerWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/ResultWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/CalendarWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/TagcloudWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/CurrentSearchWidget.js"></script>
<script type="text/javascript" src="../lib/ajaxsolr/widgets/TextWidget.js"></script>

<div id="wrap">
    <div id="header">
        <h1>ePopulation Registry1</h1>

        <h2>Search Persons</h2>
    </div>

    <div id="queryPane">
        <h2>Current Selection</h2>
        <ul id="selection"></ul>

        <h2>Search</h2>
        <!--<span id="search_help">(press ESC to close suggestions)</span>-->
        <ul id="searchEnglish">
            <span>Search full name in English</span>
            <input type="text" id="queryEnglish" name="queryEnglish"/>
        </ul>
        <ul id="searchOfficial">
            <span>Search full name in Official Language</span>
            <input type="text" id="queryOfficial" name="queryOfficial"/>
        </ul>

        <h2>Top Gender</h2>

        <div class="tagcloud" id="gender"></div>

        <h2>By Date Of Birth</h2>
        <div id="calendar"></div>
        <div class="clear"></div>
    </div>

    <div id="result">
        <div id="navigation">
            <ul id="pager"></ul>
            <div id="pager-header"></div>
        </div>
        <div id="docs"></div>
    </div>

    <div class="clear"></div>
</div>