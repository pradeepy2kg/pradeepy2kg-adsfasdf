<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>
<script type="text/javascript" src="../js/validate.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript">

    $(function() {
        $('select#birthDistrictId').bind('change', function(evt1) {
            var id = $("select#birthDistrictId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
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
        })
    });


</script>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:actionerror/>
<table cellpadding="5" cellspacing="0">
    <s:form action="eprFilterAlteration" method="post">

        <tbody>
        <tr>
            <td><s:label name="district" value="%{getText('district.label')}"/></td>
            <td>
                <s:select id="birthDistrictId" name="birthDistrictId" list="districtList" value="birthDistrictId"
                          cssStyle="width:240px;"/>
            </td>
            <td></td>
            <td><s:label name="division" value="%{getText('select_DS_division.label')}"/></td>
            <td>
                <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                          cssStyle="float:left;  width:240px;"/>
            </td>
        </tr>
        <tr>
            <td><s:label name="bdDivision" value="%{getText('select_BD_division.label')}"/></td>
            <td>
                <s:select id="birthDivisionId" name="birthDivisionId" value="{birthDivisionId}" list="bdDivisionList"
                          headerValue="%{getText('all.divisions.label')}" headerKey="0"
                          cssStyle=" width:240px;float:left;"/>
            </td>
            <td class="button" align="left"><s:submit name="refresh" value="%{getText('refresh.label')}"/></td>
        </tr>
        </tbody>
    </s:form>
</table>


<fieldset style="margin-bottom:10px;margin-top:20px;border:none">
    <legend></legend>
    <table id="approval-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
        <s:if test="birthAlterationPendingApprovalList.size>0">
        <thead>
        <tr>
            <%--<th width="15px"></th>--%>
            <th width="20px"><s:label value="%{getText('division.label')}"/></th>
            <th width="100px"><s:label value="%{getText('name.label')}"/></th>
            <th></th>
            <th></th>
            <%--<th width="100px"></th>
           <th width="40px"></th>
           <th width="20px"></th>
           <th width="20px"></th>--%>
            <%--<th width="20px"></th>
           <th width="20px"></th>--%>
        </tr>
        </thead>
            </s:if>
        <tbody>
        <s:iterator status="approvalStatus" value="birthAlterationPendingApprovalList" id="approvalList">
            <%--todo has to be completed--%>
            <tr>
                <td><s:property value="alt52_1.birthDivision.bdDivisionUKey"/></td>
                <td><s:property value="alt27.childFullNameOfficialLang"/></td>
                <td align="center">
                    <s:if test="#request.allowApproveAlteration">
                        <s:url id="approveSelected" action="eprApproveSelectedAlteration.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                            <s:param name="pageNo" value="%{#request.pageNo}"/>
                            <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                            <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                            <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                            <%--<s:param name="recordCounter" value="#request.recordCounter"/>--%>
                        </s:url>
                        <s:a href="%{approveSelected}" title="%{getText('approveTooltip.label')}">
                            <img src="<s:url value='/images/approve.gif'/>" width="25" height="25" border="none"/></s:a>
                    </s:if>
                </td>
                <td align="center">
                    <s:if test="#request.allowApproveAlteration">
                        <s:url id="rejectSelected" action="#">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                            <s:param name="pageNo" value="%{#request.pageNo}"/>
                            <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                            <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                            <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                            <%--<s:param name="recordCounter" value="#request.recordCounter"/>--%>
                        </s:url>
                        <s:a href="%{rejectSelected}" title="%{getText('rejectTooltip.label')}">
                            <img src="<s:url value='/images/reject.gif'/>" width="25" height="25" border="none"/></s:a>
                    </s:if>
                </td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</fieldset>

<div class="next-previous">
    <%-- Next link to visible next records will only visible if nextFlag is
  set to 1--%>
    <s:url id="previousUrl" action="eprAlterationApprovalPrevious.do" encode="true">
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
        <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
        <%--<s:param name="recordCounter" value="#request.recordCounter"/>--%>
    </s:url>

    <s:url id="nextUrl" action="eprAlterationApprovalNext.do" encode="true">
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
        <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
        <%--<s:param name="recordCounter" value="#request.recordCounter"/>--%>
    </s:url>
    <s:if test="#request.previousFlag"><s:a href="%{previousUrl}">
        <img src="<s:url value='/images/previous.gif'/>"
             border="none"/></s:a><s:label value="%{getText('previous.label')}"
                                           cssStyle="margin-right:5px;"/></s:if>

    <s:if test="#request.nextFlag"><s:label value="%{getText('next.label')}"
                                            cssStyle="margin-left:5px;"/><s:a href="%{nextUrl}">
        <img src="<s:url value='/images/next.gif'/>" border="none"/></s:a></s:if>
</div>
    