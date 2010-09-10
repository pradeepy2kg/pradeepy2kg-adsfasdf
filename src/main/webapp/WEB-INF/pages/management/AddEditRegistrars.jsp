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

    function disableFields(mode) {
        document.getElementById('registrarNameInOfficelaLang').disabled = mode;
        document.getElementById('registrarNameInEnglish').disabled = mode;
        document.getElementById('registrarPin').disabled = mode;
        document.getElementById('registrarNIC').disabled = mode;
        document.getElementById('registrarGender').disabled = mode;
        document.getElementById('dateOfBirthDatePicker').disabled = mode;
        document.getElementById('registrarAddress').disabled = mode;
        document.getElementById('registrarPhone').disabled = mode;
        document.getElementById('registrarEmail').disabled = mode;
        document.getElementById('prefLanguage').disabled = mode;
    }
    function initPage() {
    }

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

    form>

    .toggle_container .block {
        padding: 20px; /*--Padding of Container--*/
        background: url(../images/) no-repeat left bottom; /*--Bottom rounded corners--*/
    }

</style>
<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
    <legend align="right"><s:property value="%{getText('registrar.basic.info')}"/></legend>
    <form action="eprUpdateRegistrar.do" method="post">
        <table border="0" style="width: 100%" cellpadding="5" cellspacing="5">
            <caption></caption>
            <col width="400px"/>
            <col width="1000px"/>
            <tbody>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.full.name.officelaLang')}"/></td>
                <td align="left"><s:textfield id="registrarNameInOfficelaLang" cssStyle="width:100%"
                                              name="registrar.fullNameInOfficialLanguage"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.full.name.english')}"/></td>
                <td align="left"><s:textfield id="registrarNameInEnglish" cssStyle="width:100%"
                                              name="registrar.fullNameInEnglishLanguage"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.pin')}"/></td>
                <td align="left"><s:textfield id="registrarPin" name="registrar.pin"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.nic')}"/></td>
                <td align="left"><s:textfield id="registrarNIC" name="registrar.nic"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.gender')}"/></td>
                <td align="left"><s:select
                        list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                        name="registrar.gender" cssStyle="width:190px;" id="registrarGender"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.dateofbirth')}"/></td>
                <td align="left"><s:textfield name="registrar.dateOfBirth" id="dateOfBirthDatePicker"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.address')}"/></td>
                <td align="left"><s:textarea id="registrarAddress" cssStyle="width:100%"
                                             name="registrar.currentAddress"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.phone')}"/></td>
                <td align="left"><s:textfield id="registrarPhone" name="registrar.phoneNo"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.email')}"/></td>
                <td align="left"><s:textfield id="registrarEmail" name="registrar.emailAddress"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.prefLang')}"/></td>
                <td align="left"><s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'Tamil'}"
                                           name="registrar.preferredLanguage"
                                           cssStyle="width:190px;" id="prefLanguage"/></td>
            </tr>
            </tbody>
        </table>
        <table>
            <col width="500px">
            <col width="250px">
            <col width="250px">
            <col width="250px">
            <tbody>
            <tr>
                <td colspan="2"></td>
                <td><s:property value="%{getText('label.enable.edit.mode')}"/> <s:checkbox name="enableEditMode"
                                                                                           id="enableEditMode"
                                                                                           onselect="javascript:x(true);"/></td>
                <td><s:submit value="%{getText('label.submit')}"/></td>
            </tr>
            </tbody>
        </table>
    </form>
</fieldset>

<%--current assignments--%>
<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
    <legend align="right"><s:property value="%{getText('registrar.current.assignments')}"/></legend>
    <table id="registrars-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
        <thead>
        <tr class="table-title">
            <th width="50px"><s:label value="%{getText('label.bdDivision')}"/></th>
            <th width="20px"><s:label value="%{getText('label.active')}"/></th>
            <th width="20px"><s:label value="%{getText('label.type')}"/></th>
            <th width="100px"><s:label value="%{getText('label.startDate')}"/></th>
            <th width="100px"><s:label value="%{getText('label.endDate')}"/></th>
            <th width="100px"><s:label value="%{getText('label.edit')}"/></th>
        </tr>
        </thead>
        <s:if test="assignmentList.size>0">
            <s:iterator status="assignmentStatus" value="assignmentList" id="assignmentList">
                <tbody>
                <s:url action="eprAssignmentEdit.do" id="editSelected">
                    <s:param name="assignmentUKey" value="assignmentUKey"/>
                    <s:param name="editableAssignment" value="true"/>
                </s:url>
                <tr>
                    <s:if test="birthDivision != null">
                        <td><s:property value="birthDivision.enDivisionName"/></td>
                    </s:if>
                    <s:if test="deathDivision != null">
                        <td><s:property value="deathDivision.enDivisionName"/></td>
                    </s:if>
                    <s:if test="marriageDivision != null">
                        <td><s:property value="marriageDivision.enDivisionName"/></td>
                    </s:if>

                    <s:if test="lifeCycleInfo.active ==true">
                        <td><s:property value="%{getText('label.yes')}"/></td>
                    </s:if>
                    <s:else>
                        <td><s:property value="%{getText('label.no')}"/></td>
                    </s:else>
                    <td><s:property value="type"/></td>
                    <td><s:property value="permanentDate"/></td>
                    <td><s:property value="terminationDate"/></td>
                    <td align="center">
                        <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                            <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                 border="none"/></s:a>
                    </td>
                </tr>
                </tbody>
            </s:iterator>
        </s:if>
    </table>

</fieldset>
<%--add a new assignment--%>

<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
    <legend align="right"><s:property value="%{getText('registrar.add.new.assignment')}"/></legend>
    <s:url action="eprAssignmentAddDirect.do" id="x">
        <s:param value="%{registrar.pin}" name="registrarPin"></s:param>
        <s:param value="3" name="directAssigment"></s:param>
    </s:url>
    <s:a href="%{x}"><s:property value="%{getText('assignment.add.new.assignment')}"/></s:a>

    <%--    <div class="msg_list" style="position:relative;">
    <p class="msg_head">
        <s:url action="eprAddAssignmentIndirect.do" id="x">
            <s:param value="%{registrar.pin}" name="registrarPin"></s:param>
        </s:url>
        <s:a href=""><s:property value="%{getText('assignment.add.new.assignment')}"/></s:a>

    </p>--%>

    <%--        <div class="msg_body">
        <s:form action="eprAssignmentAdd.do" method="post">
            <fieldset>
                <legend><s:property value="%{getText('assignment.bd.marraige.division')}"/></legend>
                <table cellspacing="0" cellpadding="0">
                    <caption></caption>
                    <col width="500px">
                    <col width="500px">
                    <col>
                    <tbody>
                    <tr>
                        <td colspan="1" align="left">
                            <s:select id="districtId" name="districtId" list="districtList" value="%{districtId}"
                                      cssStyle="width:98.5%; width:240px;"/>
                        </td>
                        <td colspan="1" align="left"><s:select id="dsDivisionId" name="dsDivisionId"
                                                               list="dsDivisionList"
                                                               value="%{dsDivisionId}"
                                                               cssStyle="float:left;  width:240px;"/></td>
                        <td>
                            <s:select id="divisionId" name="divisionId" value="%{divisionId}" list="divisionList"
                                      cssStyle="float:left;  width:240px; margin:2px 5px;"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </fieldset>
            <fieldset>
                <legend><s:property value="%{getText('assignment.type')}"/></legend>
                <table cellspacing="0" cellpadding="0">
                    <caption></caption>
                    <col width="500px">
                    <col width="500px">
                    <tbody>
                    <tr>
                        <td colspan="1" align="left"><s:select
                                list="#@java.util.HashMap@{'0':getText('label.type.birth'),'1':getText('label.type.death'),'2':getText('label.type.marrage')}"
                                name="" cssStyle="width:240px; margin-left:5px;" id="type"/></td>
                    </tr>
                    </tbody>
                </table>
            </fieldset>

            <fieldset>
                <legend><s:property value="%{getText('assignment.state')}"/></legend>
                <s:select
                        list="#@java.util.HashMap@{'0':getText('label.state.active'),'1':getText('label.state.inactive')}"
                        name="" cssStyle="width:240px; margin-left:5px;"/>
            </fieldset>

            <fieldset>
                <legend><s:property value="%{getText('assignment.dates')}"/></legend>
                <table>
                    <caption/>
                    <col/>
                    <col/>
                    <col/>
                    <col/>
                    <col/>
                    <col/>
                    <tbody>
                    <tr>
                        <fieldset>
                            <legend><s:property value="%{getText('assignemnt.appointed.date')}"/></legend>
                            <s:textfield name="" id="dateOfAppoinmentDatePicker"/>

                        </fieldset>
                        <fieldset>
                            <legend><s:property value="%{getText('assignment.permanent.date')}"/></legend>
                            <s:textfield name="" id="dateOfPermenentDatePicker"/>

                        </fieldset>
                        <fieldset>
                            <legend><s:property value="%{getText('assignment.temination.date')}"/></legend>
                            <s:textfield name="" id="dateOfTerminationDatePicker"/>
                        </fieldset>
                    </tr>
                    </tbody>
                </table>
            </fieldset>
            <s:submit name="assignMentSubmit" value="%{getText('assignment.submit')}"/>
            <s:hidden name="directAssigment" value="2"/>
            <s:hidden name="registrarPin" value="%{registrar.pin}"/>
            <s:hidden name="registrarSession" value="false"/>
        </s:form>
    </div>
    </div>--%>
</fieldset>


