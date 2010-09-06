<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>

<script>


    $(document).ready(function() {

        //Hide (Collapse) the toggle containers on load
        $(".toggle_container").hide();

        //Switch the "Open" and "Close" state per click then slide up/down (depending on open/close state)
        $("h2.trigger").click(function() {
            $(this).toggleClass("active").next().slideToggle("slow");
        });

    });

    $(document).ready(function()
    {
        //hide the all of the element with class msg_body
        $(".msg_body").hide();
        //toggle the componenet with class msg_body
        $(".msg_head").click(function()
        {
            $(this).next(".msg_body").slideToggle(600);
        });
    });


    $(function() {
        $("#dateOfBirthDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $("#dateOfAppoinmentDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $("#dateOfPermenentDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $("#dateOfTerminationDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });


    $(document).ready(function() {
        $('#registrars-list-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "bSort": true,
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"
        });
    });

    // mode 1 = passing District, will return DS list
    // mode 2 = passing DsDivision, will return BD list
    // any other = passing district, will return DS list and the BD list for the first DS
    $(function() {
        $('select#districtId').bind('change', function(evt1) {
            var id = $("select#districtId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:3},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivisionId").html(options1);

                        var options2 = '';
                        var bd = data.bdDivisionList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#birthDivisionId").html(options2);
                    });
        });

        $('select#dsDivisionId').bind('change', function(evt2) {
            var id = $("select#dsDivisionId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#birthDivisionId").html(options);
                    });
        });
    });

</script>

<style type="text/css">

    <%--styles for inner --%>
    p {
        padding: 0 0 1em;
    }

    .msg_list {
        margin: 0px;
        padding: 0px;
        width: 1000px;
    }

    .msg_head {
        padding: 5px 10px;
        cursor: pointer;
        position: relative;
        background-color: #3399FF;
        margin: 1px;
    }

    .msg_body {
        padding: 5px 10px 15px;
        background-color: #E0E0E0;
    }

    <%--style for inner finish--%>

    h2.trigger {
        padding: 0 0 0 50px;
        margin: 0 0 5px 0;
        background: #3366ff;
        height: 46px;
        line-height: 46px;
        width: 1000px;
        font-size: 2em;
        font-weight: normal;
        float: left;
    }

    h2.trigger a {
        color: #fff;
        text-decoration: none;
        display: block;
    }

    h2.trigger a:hover {
        color: #ccc;
    }

    h2.active {
        background-position: left bottom;
    }

    /*--When toggle is triggered, it will shift the image to the bottom to show its "opened" state--*/
    .toggle_container {
        margin: 0 0 5px;
        padding: 0;
        border-top: 1px solid #d6d6d6;
        background: #f0f0f0 url(../images/) repeat-y left top;
        overflow: hidden;
        font-size: 1.2em;
        width: 1050px;
        clear: both;
    }

    .toggle_container .block {
        padding: 20px; /*--Padding of Container--*/
        background: url(../images/) no-repeat left bottom; /*--Bottom rounded corners--*/
    }

</style>

<table border="0" style="width: 100%" cellpadding="5" cellspacing="5">
    <caption></caption>
    <col width="400px"/>
    <col width="1000px"/>
    <tbody>
    <tr>
        <td align="left">name in official language</td>
        <td align="left"><s:textfield id="registrarNameInOfficelaLang" cssStyle="width:100%"
                                      name="registrar.fullNameInOfficialLanguage"/></td>
    </tr>
    <tr>
        <td align="left">name in English</td>
        <td align="left"><s:textfield id="registrarNameInEnglish" cssStyle="width:100%"
                                      name="registrar.fullNameInEnglishLanguage"/></td>
    </tr>
    <tr>
        <td align="left">pin/NIC</td>
        <td align="left"><s:textfield id="registrarPin" name="registrar.pin"/></td>
    </tr>
    <tr>
        <td align="left">NIC</td>
        <td align="left"><s:textfield id="registrarNIC" name="registrar.nic"/></td>
    </tr>
    <tr>
        <td align="left">gender</td>
        <td align="left"><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="registrar.gender" cssStyle="width:190px; margin-left:5px;" id="registrarGender"/></td>
    </tr>
    <tr>
        <td align="left">date of birth</td>
        <td align="left"><s:textfield name="registrar.dateOfBirth" id="dateOfBirthDatePicker"/></td>
    </tr>
    <tr>
        <td align="left">address</td>
        <td align="left"><s:textarea id="registrarAddress" cssStyle="width:100%"
                                     name="registrar.currentAddress"/></td>
    </tr>
    <tr>
        <td align="left">phone</td>
        <td align="left"><s:textfield id="registrarPhone" name="registrar.phoneNo"/></td>
    </tr>
    <tr>
        <td align="left">email</td>
        <td align="left"><s:textfield id="registrarEmail" name="registrar.emailAddress"/></td>
    </tr>
    <tr>
        <td align="left">prefered language</td>
        <td align="left"><s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'Tamil'}"
                                   name="registrar.preferredLanguage"
                                   cssStyle="width:190px; margin-left:5px;"/></td>
    </tr>
    </tbody>
</table>

<%--current assignments--%>
<table id="registrars-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
    <thead>
    <tr class="table-title">
        <th width="50px"><s:label value="%{getText('label.bdDivision')}"/></th>
        <th width="20px"><s:label value="%{getText('label.state')}"/></th>
        <th width="20px"><s:label value="%{getText('label.type')}"/></th>
        <th width="100px"><s:label value="%{getText('label.startDate')}"/></th>
        <th width="100px"><s:label value="%{getText('label.endDate')}"/></th>
    </tr>
    </thead>
    <%--        <s:if test="registrarList.size>0">
        <s:iterator status="registrarStatus" value="registrarList" id="registrarList">
            <tbody>
            <tr>
                <td><s:property value="fullNameInOfficialLanguage"/> </td>
                <td><s:property value="fullNameInOfficialLanguage"/> </td>
                <td><s:property value="fullNameInOfficialLanguage"/> </td>
                <td><s:property value="fullNameInOfficialLanguage"/> </td>
                <td><s:property value="fullNameInOfficialLanguage"/> </td>
                <td><s:property value="fullNameInOfficialLanguage"/> </td>
            </tr>
            </tbody>
        </s:iterator>
    </s:if>--%>
    <tbody>
    <s:url action="eprRegistrarsView.do" id="registrar"/>
    <tr>
        <td><s:label value="bdDivision"/></td>
        <td><s:label value="state"/></td>
        <td><s:label value="type"/></td>
        <td><s:label value="start date"/></td>
        <td><s:label value="end date"/></td>
    </tr>
    </tbody>
</table>


<%--add a new assignment--%>
<s:form action="#" method="post">
    <s:include value="AddAssignment.jsp"/>
</s:form>
