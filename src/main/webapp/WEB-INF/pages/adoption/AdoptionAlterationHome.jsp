<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<s:url value="/js/common.js"/>"></script>
<script type="text/javascript">
    $(function () {
        $('#adoptionOrderListTable').dataTable({
            "bPaginate":true,
            "bLengthChange":false,
            "bFilter":true,
            "aaSorting":[
                [0, 'desc']
            ],
            "bInfo":false,
            "bAutoWidth":false,
            "bJQueryUI":true,
            "sPaginationType":"full_numbers"
        });
        if($('#adoptionEntryNo').val() == 0){
            $('#adoptionEntryNo').val('');
        }
    });
</script>
<div id="adoption-alteration-outer">
    <s:form method="POST" name="adoptionAlterationHome">
        <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
            <table width="100%">
                <tr>
                    <td colspan="4">
                        <s:actionerror cssStyle="color: red; font-size: 10pt;"/>
                        <s:actionmessage cssStyle="color: blue; font-size: 10pt;"/>
                    </td>
                </tr>
                <tr>
                    <td><s:label value="%{getText('alteration_method.label')}"/></td>
                    <td colspan="3">
                        <s:select list="methodList" name="alterationMethod"/>
                    </td>
                </tr>
                <tr>
                    <td><s:label value="%{getText('adoption_entry_no.label')}"/></td>
                    <td><s:textfield id="adoptionEntryNo" name="adoptionEntryNo" onkeypress="return numbersOnly(event, true);"/></td>
                    <td><s:label value="%{getText('court_order_no.label')}"/></td>
                    <td><s:textfield id="courtOrderNo" name="courtOrderNo"/></td>
                </tr>
                <tr>
                    <td colspan="4" align="right">
                        <s:submit action="eprLoadAdoptionRecordsForAlteration" value="%{getText('search.label')}"/>
                    </td>
                </tr>
            </table>
        </fieldset>
    </s:form>
    <div style="float: left; width: 100%;">
    <s:if test="adoptionOrderList.size() > 0">
        <table width="100%" id="adoptionOrderListTable" cellpadding="0" cellspacing="0" class="display">
            <col width="120px"/>
            <col width="120px"/>
            <col width="300px"/>
            <col>
            <col width="50px"/>
            <thead>
            <tr align="center">
                <th><s:label value="%{getText('entry_no.label')}"/></th>
                <th><s:label value="%{getText('order_no.label')}"/></th>
                <th><s:label value="%{getText('court.label')}"/></th>
                <th><s:label value="%{getText('child_name.label')}"/></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator id="adoptionOrderList" value="adoptionOrderList" status="status">
                <tr>
                    <td><s:property value="adoptionEntryNo"/></td>
                    <td><s:property value="courtOrderNumber"/></td>
                    <td>
                        <s:if test="language == \"si\"">
                            <s:property value="court.siCourtName"/>
                        </s:if>
                        <s:elseif test="language == \"ta\"">
                            <s:property value="court.taCourtName"/>
                        </s:elseif>
                        <s:elseif test="language == \"en\"">
                            <s:property value="court.enCourtName"/>
                        </s:elseif>
                    </td>
                    <s:if test="childNewName!=null">
                        <td><s:property value="getChildNewNameToLength(30)"/></td>
                    </s:if>
                    <s:elseif test="childExistingName!=null">
                        <td><s:property value="getChildExistingNameToLength(30)"/></td>
                    </s:elseif>
                    <td align="center">
                        <s:url id="alterAdoptionUrl" action="eprPopulateAdoptionForAlteration.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="alterationMethod" value="alterationMethod"/>
                        </s:url>
                        <s:a href="%{alterAdoptionUrl}" title="%{getText('alter.label')}">
                            <img id="editImage" src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                 border="none"/>
                        </s:a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </s:if>
    </div>
</div>