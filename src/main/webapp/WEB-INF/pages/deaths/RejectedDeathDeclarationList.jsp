<%@ page import="lk.rgd.common.util.NameFormatUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- TODO create the page to display REJECTED Birth Declarations --%>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<script type="text/javascript">
    $(function () {
        $('#rejectedDeathsTable').dataTable({
            "bPaginate":true,
            "bLengthChange":false,
            "bFilter":true,
            "bSort":true,
            "bInfo":false,
            "bAutoWidth":false,
            "bJQueryUI":true,
            "sPaginationType":"full_numbers"
        });
    });

    $(function () {
        $('select#deathDistrictId').bind('change', function (evt1) {
            var id = $("select#deathDistrictId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
                    function (data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        options1 += '<option value="' + 0 + '">' + <s:label value="%{getText('all.divisions.label')}"/>+'</option>';
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivisionId").html(options1);

                    });
        });
    });
</script>
<style type="text/css">
    #rejectedDeaths-outer {

    }
</style>
<div id="rejectedDeaths-outer">
    <%--Uncomment if needed to filter the results using D.S. Division --%>
    <s:form action="eprDeathsRejectedSearch" method="POST" name="rejectedDeathSearch">
        <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
            <legend><b><s:label value="%{getText('searchOption.label')}"/></b></legend>
            <table width="100%" cellpadding="5" cellspacing="0">
                <col width="200px"/>
                <col/>
                <col width="300px"/>
                <col/>
                <col width="200px"/>
                <tbody>
                <tr>
                    <td><s:label name="district" value="%{getText('district.label')}"/></td>
                    <td>
                        <s:select id="deathDistrictId" name="deathDistrictId" list="districtList"
                                  value="deathDistrictId"
                                  cssStyle="width:240px;"/>
                    </td>
                    <td><s:label name="division" value="%{getText('select_DS_division.label')}"/></td>
                    <td>
                        <s:if test="#session.user_bean.role.roleId == 'ADR'">
                            <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                      value="%{dsDivisionId}"
                                      cssStyle="float:left;  width:240px;"/>
                        </s:if><s:else>
                        <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                                  headerValue="%{getText('all.divisions.label')}" headerKey="0"
                                  cssStyle="float:left;  width:240px;"/>
                    </s:else>
                    </td>
                    <td class="button" align="right">
                        <s:submit name="refresh" value="%{getText('bdfSearch.button')}"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </fieldset>
    </s:form>

    <table id="rejectedDeathsTable" width="100%" cellpadding="0" cellspacing="0" class="display">
        <thead>
        <tr>
            <th width="30px" style="padding: 0 5px;">#</th>
            <th width="160px"><s:label value="%{getText('serial.label')}"/></th>
            <th><s:label value="%{getText('death.person.name.label')}"/></th>
        </tr>
        </thead>
        <tbody>
        <s:iterator status="searchStatus" value="searchResultList" id="searchList">
            <tr>
                <td align="right" style="padding: 0 5px;"><s:property value="#searchStatus.index + 1"/></td>
                <td align="center"><s:property value="death.deathSerialNo - 800000"/></td>
                <td>
                    <s:if test="deathPerson != null && deathPerson.deathPersonNameOfficialLang != null">
                        <s:property value="deathPerson.deathPersonNameOfficialLang"/>
                    </s:if>
                </td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</div>