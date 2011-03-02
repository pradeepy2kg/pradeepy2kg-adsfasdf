<%@ page import="lk.rgd.crs.api.domain.BDDivision" %>
<%@ page import="lk.rgd.common.api.domain.User" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" src="../js/validate.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<script>
    $(document).ready(function() {
        $("#tabs").tabs();
    });
</script>
<script type="text/javascript">
    $(function() {
        $("#alterationRecivedToDateId").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });
    $(function() {
        $("#alterationRecivedFromDateId").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

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
                    <%--options2 += '<option value="' + 0 + '">' + <s:labe value="%{getText('select.registrationDivision.label')}"/> + '</option>';--%>
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


    $(function() {
        $('select#districtIdSerial').bind('change', function(evt1) {
            var id = $("select#districtIdSerial").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivisionIdSerial").html(options1);

                        var options2 = '';
                        var bd = data.bdDivisionList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#bdDivisionSerial").html(options2);
                    });
        });

        $('select#dsDivisionIdSerial').bind('change', function(evt2) {
            var id = $("select#dsDivisionId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#bdDivisionSerial").html(options);
                    });
        })
    });


    $(document).ready(function() {
        $('#approval-list-table').dataTable({
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

    var errormsg = "";
    var counter = 0;

    function validateForm() {

        var locationID = document.getElementById('locationId').value;
        if (locationID == 0) {
            locationID = "";
        }
        var certifcateNumber = document.getElementById('certificateNumber').value;
        var valueArray = new Array(locationID, certifcateNumber);
        for (var i = 0; i < valueArray.length; i++) {
            var c = valueArray[i];
            if (c != "") {
                counter++
            }
        }
        if (counter > 1) {
            errormsg = errormsg + document.getElementById('oneMethodErr').value;
        }

        //validate   number fields
        isNumeric(certifcateNumber, 'invalideDateErr', 'certificateNumberFi')

        if (errormsg != "") {
            alert(errormsg)
            errormsg = "";
            counter = 0;
            return false;
        }
        else {
            return true;
        }
        return false;
    }


    function initPage() {
    }
</script>


<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User) session.getAttribute("user_bean");
%>
<s:form action="eprFilterBirthAlteration" method="post" onsubmit="javascript:return validateForm()">
    <%--<s:actionerror  cssStyle="color:red;font-size:9pt;"/>--%>
    <div id="tabs" style="font-size:10pt;">
        <ul>
            <li><a href="#fragment-1"><span> <s:label
                    value="%{getText('registrationSerchTab6.label')}"/></span></a>
            </li>
            <li><a href="#fragment-2"><span><s:label
                    value="%{getText('registrationSerchTab3.label')}"/></span></a></li>
            <li><a href="#fragment-3"><span><s:label
                    value="%{getText('registrationSerchTab7.label')}"/></span></a></li>
        </ul>

        <div id="fragment-1">
            <table cellpadding="5" cellspacing="0" style="margin-left:20px;width:95%;padding:5px;">
                <tbody>
                <tr>
                    <td style="width:25%"><s:label name="district" value="%{getText('district.label')}"
                                                   cssStyle="margin-left:10px;"/></td>
                    <td style="width:25%">
                        <s:select id="districtIdSerial" name="districtId" list="districtList" cssStyle="width:240px;"/>
                    </td>
                    <td style="width:25%"><s:label name="division" value="%{getText('select_DS_division.label')}"
                                                   cssStyle="margin-left:10px;"/></td>
                    <td style="width:25%">
                        <s:select id="dsDivisionIdSerial" name="dsDivisionId" list="dsDivisionList" headerKey="0"
                                  cssStyle="float:left;  width:240px;"/>
                    </td>
                </tr>
                <tr>
                    <td><s:label name="bdDivisionSerial" value="%{getText('select_BD_division.label')}"
                                 cssStyle="margin-left:10px;"/></td>
                    <td>
                        <s:select id="bdDivisionSerial" name="birthDivisionId" list="bdDivisionList"
                                  headerKey="0" headerValue="%{getText('select.registrationDivision.label')}"
                                  cssStyle="float:left;  width:240px; margin:2px 5px;"/>
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div id="fragment-2">
            <table cellpadding="5" cellspacing="0" style="margin-left:20px;width:95%;padding:5px;">
                <tr>
                    <td width="350px"><s:label value="%{getText('certificateNumber.lable')}"/></td>
                    <td>
                        <s:textfield name="birthCertificateNumber" id="certificateNumber" value=""/>
                    </td>
                </tr>
            </table>

        </div>
        <div id="fragment-3">
            <table cellpadding="5" cellspacing="0" style="margin-left:20px;width:95%;padding:5px;">
                <tr>
                    <td width="350px"><s:label value="%{getText('alteration.submitted.label')}"/></td>
                    <td>
                            <s:label value="%{getText('from.lable')}" cssStyle="margin-right:10px;"/>
                            <s:select list="userLocations" name="locationUKey" value="%{locationUKey}" headerKey="0"
                                      headerValue="%{getText('select.location')}" id="locationId"/>
                </tr>
            </table>
        </div>
    </div>
    <div class="form-submit">
        <s:submit name="refresh" value="%{getText('refresh.label')}" cssStyle="float:right;"/>
    </div>
</s:form>
<br>
<s:actionerror cssStyle="color:red;font-size:9pt;"/>
<s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
<fieldset style="margin-bottom:10px;margin-top:50px;border:none">
    <legend></legend>
    <s:if test="birthAlterationPendingApprovalList.size==0">
        <s:label value="%{getText('no.items.found.lable')}" cssStyle="color:blue;margin-left:30px;"/>
    </s:if>
    <table id="approval-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
        <s:if test="birthAlterationPendingApprovalList.size>0">
            <thead>
            <tr>
                <th align="center" width="125px"><s:label value="%{getText('cer.number.label')}"/></th>
                <th align="center" width="75px"><s:label value="%{getText('act.label')}"/></th>
                <th align="center" width="600px"><s:label value="%{getText('child.name.lable')}"/></th>
                <th width="50px"></th>
                <th width="50px"></th>
                <th width="50px"></th>
                <th width="50px"></th>
                <th width="50px"></th>
            </tr>
            </thead>
        </s:if>
        <tbody>
        <s:iterator status="approvalStatus" value="birthAlterationPendingApprovalList" id="approvalList">
            <%--todo has to be completed--%>
            <tr>
                <td align="center"><s:property value="bdfIdUKey"/></td>
                <td align="center"><s:property value="type"/></td>
                <td><s:property value="childNameInOfficialLanguage"/></td>
                <td align="center">
                    <s:url id="approveSelected" action="eprApproveBirthAlterationInit.do">
                        <s:param name="idUKey" value="idUKey"/>
                    </s:url>
                    <s:set name="birthDivision" value="birthRecordDivision"/>
                    <s:if test="status.ordinal()==0 & (#session.user_bean.role.roleId.equals('ARG') |
                     #session.user_bean.role.roleId.equals('RG') | (!(#session.user_bean.role.roleId.equals('DEO')) &
                      type.ordinal()== 0))">
                        <%
                            BDDivision deathDivision = (BDDivision) pageContext.getAttribute("birthDivision");
                            int deathDSDivsion = deathDivision.getDsDivision().getDsDivisionUKey();
                            boolean approveRights = user.isAllowedAccessToBDDSDivision(deathDSDivsion);
                            if (approveRights) {
                        %>
                        <s:a href="%{approveSelected}" title="%{getText('approveTooltip.label')}">
                            <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                 border="none"/></s:a>
                        <%}%>
                    </s:if>
                </td>
                <td align="center">
                    <s:url id="rejectSelected" action="eprRejectBirthAlterationInit.do">
                        <s:param name="idUKey" value="idUKey"/>
                    </s:url>
                    <s:set name="birthDivision" value="birthRecordDivision"/>
                    <s:if test="status.ordinal()==0 & (#session.user_bean.role.roleId.equals('ARG') |
                     #session.user_bean.role.roleId.equals('RG') | (!(#session.user_bean.role.roleId.equals('DEO')) &
                      type.ordinal()== 0))">
                        <%
                            BDDivision deathDivision = (BDDivision) pageContext.getAttribute("birthDivision");
                            int deathDSDivsion = deathDivision.getDsDivision().getDsDivisionUKey();
                            boolean approveRights = user.isAllowedAccessToBDDSDivision(deathDSDivsion);
                            if (approveRights) {
                        %>
                        <s:a href="%{rejectSelected}" title="%{getText('rejectTooltip.label')}">
                            <img src="<s:url value='/images/reject.gif'/>" width="25" height="25"
                                 border="none"/>
                        </s:a>
                        <%}%>
                    </s:if>
                </td>
                <td align="center">
                    <s:if test="(status.ordinal()==0)">
                        <s:url id="editSelected" action="eprEditBirthAlterationInit.do">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>
                        <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                            <img src="<s:url value='/images/edit.png'/>" width="25" height="25" border="none"/></s:a>
                    </s:if>
                </td>
                <td align="center">
                    <s:if test="(status.ordinal()==0)">
                        <s:url id="deleteSelected" action="eprDeleteBirthAlteration.do">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>
                        <s:a href="%{deleteSelected}" title="%{getText('toolTip.delete')}">
                            <img src="<s:url value='/images/delete.gif'/>" width="25" height="25" border="none"/></s:a>
                    </s:if>
                </td>
                <td align="center">
                    <s:if test="(status.ordinal()==1 || status.ordinal()==2)">
                        <s:url id="applySelected" action="eprPrintBirthAlterarionNotice.do">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>
                        <s:if test="status.ordinal()==1 ">
                            <s:a href="%{applySelected}" title="%{getText('toolTip.print')}">
                                <img src="<s:url value='/images/print_icon.gif'/>" width="25" height="25"
                                     border="none"/></s:a>
                        </s:if>
                        <s:else>
                            <s:a href="%{applySelected}" title="%{getText('toolTip.rePrint')}">
                                <img src="<s:url value='/images/print_icon.gif'/>" width="25" height="25"
                                     border="none"/></s:a>
                        </s:else>
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
    <s:url id="previousUrl" action="eprBirthAlterationApprovalPrevious.do" encode="true">
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        <s:param name="pageType" value="%{#request.pageType}"/>
        <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
        <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
        <%--<s:param name="recordCounter" value="#request.recordCounter"/>--%>
    </s:url>

    <s:url id="nextUrl" action="eprBirthAlterationApprovalNext.do" encode="true">
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        <s:param name="pageType" value="%{#request.pageType}"/>
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
<s:hidden id="comError" value="%{getText('alteration.search.error.lable')}"/>
<s:hidden id="comError1" value="%{getText('date.range.error.lable')}"/>

<s:hidden id="oneMethodErr" value="%{getText('err.use.one,method.to.search')}"/>
<s:hidden id="invalideDateErr" value="%{getText('err.invalide.data')}"/>
<s:hidden id="serialNumnerFi" value="%{getText('field.serial.number')}"/>
<s:hidden id="pinNumberFi" value="%{getText('field.pin.number')}"/>
<s:hidden id="certificateNumberFi" value="%{getText('field.certificate.number')}"/>