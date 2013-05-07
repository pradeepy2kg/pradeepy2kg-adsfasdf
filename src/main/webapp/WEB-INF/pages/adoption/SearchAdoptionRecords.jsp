<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="<s:url value="/js/common.js"/>"></script>


<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";

    #adoption-search-outer {
        width: 100%;
    }
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#searchList').dataTable({
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
    });
</script>
<div id="adoption-search-outer">
    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <legend><s:label value="%{getText('search_adoption.label')}"/></legend>
        <s:form action="eprSearchAdoptionRecord" method="POST" name="searchAdoption">
            <table width="100%">
                <tr>
                    <td>
                        <s:label value="%{getText('entry_no.label')}"/>
                    </td>
                    <td>
                        <s:textfield name="adoptionEntryNo" id="entryNo" onkeypress="return numbersOnly(event, true);"/>
                    </td>
                    <td>
                        <s:label value="%{getText('court.order.no.label')}"/>
                    </td>
                    <td>
                        <s:textfield name="courtOrderNo" id="courtOrderNo"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:label value="%{getText('court.label')}"/>
                    </td>
                    <td>
                        <s:select list="courtList" name="courtId" headerKey="0"
                                  headerValue="%{getText('select.label')}"/>
                    </td>
                    <td></td>
                    <td class="form-submit" align="right">
                        <s:submit value="%{getText('search.adoption.label')}" cssStyle="float: right;"/>
                    </td>
                </tr>
            </table>
        </s:form>
        <br/>
    </fieldset>
    <fieldset style="margin-bottom:10px;margin-top:20px;border:none">
        <table id="searchList" width="100%" cellpadding="0" cellspacing="0" class="display">
            <thead>
            <tr>
                <th width="80px">#</th>
                <th width="120px"><s:label value="%{getText('entry_no.label')}"/></th>
                <th><s:label value="%{getText('name.label')}"/></th>
                <th width="150px"><s:label value="%{getText('court.order.no.label')}"/></th>
                <th width="20px"></th>
                <th width="20px"></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator id="searchResults" value="searchResults" status="searchStatus">
                <tr>
                    <td>
                        <s:property value="idUKey"/>
                    </td>
                    <td>
                        <s:property value="adoptionEntryNo"/>
                    </td>
                    <td>
                        <s:if test="childNewName!=null">
                            <s:property value="getChildNewNameToLength(30)"/>
                        </s:if>
                        <s:elseif test="childExistingName!=null">
                            <s:property value="getChildExistingNameToLength(30)"/>
                        </s:elseif>
                    </td>
                    <td>
                        <s:property value="courtOrderNumber"/>
                    </td>
                    <td align="center">
                        <s:url id="viewSelected" action="eprAdoptionViewMode.do">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>

                        <s:a href="%{viewSelected}" title="%{getText('viewAdoptionRegistrationTooltip.label')}">
                            <img id='viewImage' src="<s:url value='/images/view.gif'/>" width="25" height="25"
                                 border="none"/>
                        </s:a>
                    </td>

                    <td align="center">

                        <s:url id="cetificatePrintUrl" action="eprRePrintApplicationForAdoption.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="certificateflag" value="false"/>
                            <s:param name="currentStatus" value="%{#request.currentStatus}"/>
                            <s:param name="pageNo" value="%{#request.pageNo}"/>
                            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                        </s:url>

                        <s:a href="%{cetificatePrintUrl}"
                             title="%{getText('reprintAdoptionRegistrationTooltip.label')}">
                            <img id="printImage" src="<s:url value='/images/print_icon.gif'/>"
                                 border="none" width="25" height="25"/>
                        </s:a>

                    </td>


                </tr>
            </s:iterator>
            </tbody>
        </table>
    </fieldset>
</div>