<%@ page import="lk.rgd.common.util.NameFormatUtil" %>
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
<script type="text/javascript" src="../js/validate.js"></script>

<script>
    $(document).ready(function() {
        $("#tabs").tabs();
    });

    $(function() {
        $("#searchStartDatePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });

    $(function() {
        $("#searchEndDatePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
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

    $(document).ready(function() {
        $('#search-result').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "bSort": true,
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"   ,
            "aaSorting": [
                [ 3, "desc" ]
            ]

        });
    });

    var errormsg = "";
    function validate() {
        var domObject;
        var returnVal = true;

        // validate serial number
        domObject = document.getElementById('noticeSerialNo');
        if (!isFieldEmpty(domObject)) {
            validateSerialNo(domObject, 'error1', 'error2');
        }

        // validate start and end date
        domObject = document.getElementById('searchStartDatePicker');
        if (!isFieldEmpty(domObject)) {
            isDate(domObject.value, 'error1', 'error3');
        }

        domObject = document.getElementById('searchEndDatePicker');
        if (!isFieldEmpty(domObject)) {
            isDate(domObject.value, 'error1', 'error4');
        }

        domObject = document.getElementById('pinOrNic');
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error1', 'error5');
        }

        if (errormsg != "") {
            alert(errormsg);
            returnVal = false;
        }
        errormsg = "";
        return returnVal;
    }

    function initPage() {
    }
</script>

<s:form action="eprMarriageNoticeSearchInit.do" method="POST" onsubmit="javascript:return validate()">
    <div id="tabs" style="font-size:10pt;">
        <ul>
            <li>
                <a href="#fragment-1"><span><s:label value="%{getText('search.by.MRDivision.label')}"/></span></a>
            </li>
            <li>
                <a href="#fragment-2"><span> <s:label value="%{getText('search.by.pin.label')}"/></span></a>
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
                        <s:select id="districtId" name="districtId" list="districtList" value="districtId"
                                  cssStyle="width:98.5%; width:240px;"/>
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
                                  value="mrDivisionId" headerKey="0" headerValue="%{getText('all.divisions.label')}"
                                  cssStyle="width:98.5%; width:240px;"/>
                    </td>
                    <td></td>
                    <td>
                        <s:label value="%{getText('serial.label')}"/>
                    </td>
                    <td>
                        <s:textfield id="noticeSerialNo" name="noticeSerialNo" cssStyle="width:232px;" maxLength="10"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:label value="%{getText('date.from.label')}" cssStyle=" margin-right:5px;"/>
                    </td>
                    <td>
                        <s:textfield id="searchStartDatePicker" name="searchStartDate" cssStyle="width:150px"
                                     maxLength="10"/>
                    </td>
                    <td></td>
                    <td>
                        <s:label value="%{getText('date.to.label')}" cssStyle=" margin-right:5px;"/>
                    </td>
                    <td>
                        <s:textfield id="searchEndDatePicker" name="searchEndDate" cssStyle="width:150px"
                                     maxLength="10"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div id="fragment-2">
            <table>
                <caption/>
                <col width="280px"/>
                <col width="10px"/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:label value="%{getText('pin.label')}"/>
                    </td>
                    <td></td>
                    <td>
                        <s:textfield name="pinOrNic" id="pinOrNic" maxLength="10"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="form-submit">
        <s:submit value="%{getText('bdfSearch.button')}"/>
    </div>
</s:form>

<div id="marriage-notice-search" style="margin-top:58px;">
<s:actionmessage cssStyle="color:black;"/>
<s:actionerror cssStyle="color:red;"/>
<s:if test="searchList.size > 0">
    <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
        <legend><b><s:label value="%{getText('searchResult.label')}"/> </b></legend>
            <%--table for displaying marriage notices--%>
        <table id="search-result" width="100%" cellpadding="0" cellspacing="0" class="display"
               style="font-size:10pt;">
            <thead>
            <tr>
                <th width="70px"><s:label value="%{getText('serial.label')}"/></th>
                <th><s:label value="%{getText('partyName.label')}"/></th>
                <th width="50px"></th>
                <th width="15px"></th>
                <th width="15px"></th>
                <th width="15px"></th>
                <th width="15px"></th>
                <th width="15px"></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator status="approvalStatus" value="searchList">
                <tr>
                    <td align="center">
                        <s:if test="serialOfNotice != null">
                            <s:property value="serialOfNotice"/>
                        </s:if>
                        <s:else>-</s:else>
                    </td>
                    <td>
                        <s:if test="partyNameInOfficialLang != null">
                            <%= NameFormatUtil.getDisplayName((String) request.getAttribute("partyNameInOfficialLang"), 70)%>
                        </s:if>
                    </td>
                    <td align="center">
                        <s:if test="type.ordinal()==0">
                            <img src="<s:url value='/images/couple.jpg'/>" width="20" height="25" border="none"/>
                        </s:if>
                        <s:elseif test="type.ordinal()==1">
                            <img src="<s:url value='/images/groom.jpg'/>" width="20" height="25" border="none"/>
                        </s:elseif>
                        <s:elseif test="type.ordinal()==2">
                            <img src="<s:url value='/images/bride.jpg'/>" width="20" height="25" border="none"/>
                        </s:elseif>
                    </td>
                    <td>
                        <s:if test="hasSecond==true">
                            <s:url id="addNextNotice" action="eprMarriageNoticeEditInit.do">
                                <s:param name="idUKey" value="idUKey"/>
                                <s:param name="secondNotice" value="true"/>
                                <s:param name="noticeType" value="type"/>
                            </s:url>
                            <s:a href="%{addNextNotice}" title="%{getText('nextNoticeToolTip.label')}">
                                <img src="<s:url value='/images/add_page.png'/>" width="25" height="25"
                                     border="none"/>
                            </s:a>
                        </s:if>
                    </td>
                    <td align="center">
                            <%--only edit allowed
                            both at DATA_ENTRY
                            male at DATA_ENTRY or FEMALE_NOTICE_APPROVED
                            female at DATA_ENTRY or MALE_NOTICE_APPROVED
                            --%>
                        <s:if test="(type.ordinal()==0 && state.ordinal()==0) || (type.ordinal()==1 && state.ordinal()==0 ||state.ordinal()==2 )
                        || (type.ordinal()==2 && state.ordinal()==0 ||state.ordinal()==1)">
                            <s:url id="editSelected" action="eprMarriageNoticeEditInit.do">
                                <s:param name="idUKey" value="idUKey"/>
                                <s:param name="noticeType" value="type"/>
                            </s:url>
                            <s:a href="%{editSelected}" title="%{getText('editToolTip.label')}">
                                <img src="<s:url value='/images/edit.png'/>" width="25" height="25" border="none"/>
                            </s:a>
                        </s:if>
                    </td>
                    <td align="center">
                            <%--only approve allowed
                            both (ordinal 0) can only approve it is in DATA_ENTRY mode
                            male (ordinal 1) can only approve it is in DATA_ENTRY mode or FEMALE_NOTICE_APPROVE
                            female (ordinal 2) can only approve it is in DATA_ENTRY mode or MALE_NOTICE_APPROVE  
                            --%>
                        <s:if test="(type.ordinal()==0 && state.ordinal() == 0) ||
                         (type.ordinal()==1 && (state.ordinal() != 1 && state.ordinal() < 3)) ||
                         (type.ordinal()==2 && (state.ordinal() != 2 && state.ordinal() < 3))">
                            <s:url id="approveSelected" action="eprApproveMarriageNotice.do">
                                <s:param name="idUKey" value="idUKey"/>
                                <s:param name="noticeType" value="type"/>
                            </s:url>
                            <s:a href="%{approveSelected}" title="%{getText('approveToolTip.label')}">
                                <img src="<s:url value='/images/approve.gif'/>" width="25" height="25" border="none"/>
                            </s:a>
                        </s:if>
                    </td>
                    <td align="center">
                        <s:url id="rejectSelected">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>
                        <s:a href="%{rejectSelected}" title="%{getText('rejectToolTip.label')}">
                            <img src="<s:url value='/images/reject.gif'/>" width="25" height="25" border="none"/>
                        </s:a>
                    </td>
                    <td align="center">
                        <s:url id="deleteSelected" action="eprMarriageNoticeDelete.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="noticeType" value="type"/>
                        </s:url>
                        <s:a href="%{deleteSelected}" title="%{getText('deleteToolTip.label')}">
                            <img src="<s:url value='/images/delete.gif'/>" width="25" height="25" border="none"/>
                        </s:a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </fieldset>
</s:if>
<s:if test="marriageRegisterSearchList.size > 0">
    <%--table for displaying marriage registers--%>
    <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
        <legend><b><s:label value="%{getText('searchResult.label')}"/> </b></legend>
        <table id="search-result2" width="100%" cellpadding="0" cellspacing="0" class="display"
               style="font-size:10pt;">
            <thead>
            <tr>
                <th width="70px"><s:label value="%{getText('serial.label')}"/></th>
                <th><s:label value="%{getText('partyName.label')}"/></th>
                <th width="50px"></th>
                <th width="15px"></th>
                <th width="15px"></th>
                <th width="15px"></th>
                <th width="15px"></th>
                <th width="15px"></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator status="approvalStatus" value="marriageRegisterSearchList">
                <tr>
                    <td align="center">
                        <s:if test="serialOfNotice != null">
                            <s:property value="serialOfNotice"/>
                        </s:if>
                        <s:else>-</s:else>
                    </td>
                    <td>
                        <s:if test="partyNameInOfficialLang != null">
                            <%= NameFormatUtil.getDisplayName((String) request.getAttribute("partyNameInOfficialLang"), 70)%>
                        </s:if>
                    </td>
                    <td align="center">
                        <s:if test="type.ordinal()==0">
                            <img src="<s:url value='/images/couple.jpg'/>" width="20" height="25" border="none"/>
                        </s:if>
                        <s:elseif test="type.ordinal()==1">
                            <img src="<s:url value='/images/groom.jpg'/>" width="20" height="25" border="none"/>
                        </s:elseif>
                        <s:elseif test="type.ordinal()==2">
                            <img src="<s:url value='/images/bride.jpg'/>" width="20" height="25" border="none"/>
                        </s:elseif>
                    </td>
                    <td>
                        <s:if test="hasSecond==true">
                            <s:url id="addNextNotice" action="eprMarriageNoticeEditInit.do">
                                <s:param name="idUKey" value="idUKey"/>
                                <s:param name="secondNotice" value="true"/>
                                <s:param name="noticeType" value="type"/>
                            </s:url>
                            <s:a href="%{addNextNotice}" title="%{getText('nextNoticeToolTip.label')}">
                                <img src="<s:url value='/images/add_page.png'/>" width="25" height="25"
                                     border="none"/>
                            </s:a>
                        </s:if>
                    </td>
                    <td align="center">
                        <s:url id="editSelected" action="eprMarriageNoticeEditInit.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="noticeType" value="type"/>
                        </s:url>
                        <s:a href="%{editSelected}" title="%{getText('editToolTip.label')}">
                            <img src="<s:url value='/images/edit.png'/>" width="25" height="25" border="none"/>
                        </s:a>
                    </td>
                    <td align="center">
                        <s:url id="approveSelected" action="eprApproveMarriageNotice.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="noticeType" value="type"/>
                        </s:url>
                        <s:a href="%{approveSelected}" title="%{getText('approveToolTip.label')}">
                            <img src="<s:url value='/images/approve.gif'/>" width="25" height="25" border="none"/>
                        </s:a>
                    </td>
                    <td align="center">
                        <s:url id="rejectSelected">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>
                        <s:a href="%{rejectSelected}" title="%{getText('rejectToolTip.label')}">
                            <img src="<s:url value='/images/reject.gif'/>" width="25" height="25" border="none"/>
                        </s:a>
                    </td>
                    <td align="center">
                        <s:url id="deleteSelected" action="eprMarriageNoticeDelete.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="noticeType" value="type"/>
                        </s:url>
                        <s:a href="%{deleteSelected}" title="%{getText('deleteToolTip.label')}">
                            <img src="<s:url value='/images/delete.gif'/>" width="25" height="25" border="none"/>
                        </s:a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </fieldset>
</s:if>

</div>
<s:hidden id="error1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error2" value="%{getText('marriageNotice.serial.label')}"/>
<s:hidden id="error3" value="%{getText('searchStartDate.label')}"/>
<s:hidden id="error4" value="%{getText('searchEndDate.label')}"/>
<s:hidden id="error5" value="%{getText('pin.label')}"/>
