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
    function validate() {
        var domObject;
        var count = 0;
        var checkDate = 0;
        domObject = document.getElementById("serialAltNumberId");
        if (! isFieldEmpty(domObject)) {
            count ++;
        }
        domObject = document.getElementById("alterationRecivedFromDateId");
        var domObject1 = document.getElementById("alterationRecivedToDateId");
        if (! isFieldEmpty(domObject) || !isFieldEmpty(domObject1)) {
            count ++;
            if (isFieldEmpty(domObject) || isFieldEmpty(domObject1)) {
                checkDate ++;
            }
        }
        domObject = document.getElementById("serialNumberId");
        if (! isFieldEmpty(domObject)) {
            count ++;
        }
        if (count > 1) {
            alert(document.getElementById("comError").value);
            return false;
        }
        if (checkDate > 0) {
            alert(document.getElementById("comError1").value);
            return false;
        }
    }

    function isNumberKey(evt)
    {
        var charCode = (evt.which) ? evt.which : event.keyCode
        if (charCode > 31 && (charCode < 48 || charCode > 57))
            return false;

        return true;
    }
</script>


<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:actionerror cssStyle="color:red;font-size:9pt;"/>
<s:form action="eprFilterAlteration" method="post" onsubmit="javascript:return validate()">
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
                        <s:textfield name="idUKey"/>
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
                                      headerValue="%{getText('select.location')}"/>
                </tr>
            </table>
        </div>
    </div>
    <div class="form-submit">
        <s:submit name="refresh" value="%{getText('refresh.label')}" cssStyle="float:right;"/>
    </div>
</s:form>
<br>
<fieldset style="margin-bottom:10px;margin-top:50px;border:none">
    <legend></legend>
    <s:if test="birthAlterationPendingApprovalList.size==0">
        <s:label value="%{getText('no.items.found.lable')}" cssStyle="color:red;margin-left:30px;"/>
    </s:if>
    <table id="approval-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
        <s:if test="birthAlterationPendingApprovalList.size>0">
            <thead>
            <tr>
                <th width="20px"></th>
                <th width="650px"><s:label value="%{getText('child.name.lable')}"/></th>
                <th width="100px"></th>
                <th width="100px"></th>
                <th width="100px"></th>
                <th width="100px"></th>
            </tr>
            </thead>
        </s:if>
        <tbody>
        <s:iterator status="approvalStatus" value="birthAlterationPendingApprovalList" id="approvalList">
            <%--todo has to be completed--%>
            <tr>
                <td><s:property value="#approvalStatus.index +1"/></td>
                <td><s:property value="alt27.childFullNameOfficialLang"/></td>
                <td align="center">
                    <s:if test="#request.allowApproveAlteration &&
                    alterationApprovalPermission.get(#approvalStatus.index) && (status.ordinal() ==0)">
                        <s:url id="approveSelected" action="eprApproveSelectedAlteration.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="bdId" value="bdfIDUKey"/>
                            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                            <s:param name="pageType" value="%{#request.pageType}"/>
                            <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                            <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                            <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                        </s:url>
                        <s:a href="%{approveSelected}" title="%{getText('approveTooltip.label')}">
                            <img src="<s:url value='/images/approve.gif'/>" width="25" height="25" border="none"/></s:a>
                    </s:if>
                </td>
                <td align="center">
                    <s:if test="#request.allowApproveAlteration &&
                    alterationApprovalPermission.get(#approvalStatus.index) && (status.ordinal() ==0)">
                        <s:url id="rejectSelected" action="eprRejectSelectedAlteration.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="bdId" value="bdfIDUKey"/>
                            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                            <s:param name="pageType" value="%{#request.pageType}"/>
                            <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                            <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                            <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                        </s:url>
                        <s:a href="%{rejectSelected}" title="%{getText('rejectTooltip.label')}">
                            <img src="<s:url value='/images/reject.gif'/>" width="25" height="25" border="none"/></s:a>
                    </s:if>
                </td>

                <td align="center">
                    <s:if test="(status.ordinal() ==0)">
                        <s:url id="editSelected" action="eprEditSelectedAlteration.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="bdId" value="bdfIDUKey"/>
                            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                            <s:param name="pageType" value="%{#request.pageType}"/>
                            <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                            <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                            <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                        </s:url>
                        <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                            <img src="<s:url value='/images/edit.png'/>" width="25" height="25" border="none"/></s:a>
                    </s:if>
                </td>
                <td align="center">
                    <s:if test="#request.allowApproveAlteration &&
                    alterationApprovalPermission.get(#approvalStatus.index) && (status.ordinal() == 1)">
                        <s:url id="applySelected" action="eprPrintBirthAlterarionNotice.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="bdId" value="bdfIDUKey"/>
                            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
                            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
                            <s:param name="pageType" value="2"/>
                            <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                            <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                            <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
                        </s:url>
                        <s:a href="%{applySelected}">
                            <img src="<s:url value='/images/print_icon.gif'/>" width="25" height="25" border="none"/></s:a>
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
        <s:param name="pageType" value="%{#request.pageType}"/>
        <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
        <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
        <%--<s:param name="recordCounter" value="#request.recordCounter"/>--%>
    </s:url>

    <s:url id="nextUrl" action="eprAlterationApprovalNext.do" encode="true">
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