<%-- @author Mahesha Kalpanie --%>
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
<script type="text/javascript" src="<s:url value="/js/division.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/datePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/marriageregistervalidation.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $("#tabs").tabs();
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
            "sPaginationType": "full_numbers"
        });
    });

    //TODO : following methods has to be generalized
    function validateSerial() {
        var errormsg = "";
        errormsg = validateSerialNo("serialNumber", "errorSerialNumber", errormsg);
        return printErrorMessages(errormsg);
    }

    function validatePINNumber() {
        var errormsg = "";
        errormsg = validatePin("pinOrNic", "errorRegistrarPIN", errormsg);
        return printErrorMessages(errormsg);
    }

    function validateDateRange() {
        //TODO : Date range validation
        //TODO: Validate for leap years
        //TODO: remove validation if both date fields are empty.
        var errormsg = "";
        errormsg = isDate("searchStartDatePicker", "errorStartDate", errormsg);
        errormsg = isDate("searchEndDatePicker", "errorEndDate", errormsg);
        return printErrorMessages(errormsg);
    }

    function validateMarriageIdUkey() {
        var errormsg = "";
        errormsg = validateIdUkey("marriageIdUKey", "errorMarriageIdUKey", errormsg);
        return printErrorMessages(errormsg);
    }


</script>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage cssStyle="color:blue;font-size:10pt"/>
<div id="tabs" style="font-size:10pt;">
    <ul>
        <li>
            <a href="#fragment-1"><span><s:label value="%{getText('search.by.MRDivision.label')}"/></span></a>
        </li>
        <li>
            <a href="#fragment-2"><span> <s:label value="%{getText('label.marriageregister.search.bypin')}"/></span></a>
        </li>
        <li>
            <a href="#fragment-3"><span> <s:label value="%{getText('label.marriageregister.search.byserial')}"/></span></a>
        </li>
        <li>
            <a href="#fragment-4"><span> <s:label value="%{getText('label.marriageregister.search.byIdUKey')}"/></span></a>
        </li>
    </ul>
    <s:form action="eprMarriageRegisterSearch.do" id="generalSearch" method="POST">
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
                        <s:label value="%{getText('date.from.label')}" cssStyle=" margin-right:5px;"/>
                    </td>
                    <td>
                        <s:textfield id="searchStartDatePicker" name="searchStartDate" cssStyle="width:150px"
                                     maxLength="10" onmouseover="datepicker('searchStartDatePicker')"/>
                    </td>
                    <td></td>
                    <td>
                        <s:label value="%{getText('date.to.label')}" cssStyle=" margin-right:5px;"/>
                    </td>
                    <td>
                        <s:textfield id="searchEndDatePicker" name="searchEndDate" cssStyle="width:150px"
                                     maxLength="10" onmouseover="datepicker('searchEndDatePicker')"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:label value="%{getText('district.label')}"/>
                    </td>
                    <td>
                        <s:select id="districtId" name="districtId" list="districtList"
                                  value="districtId"
                                  cssStyle="width:98.5%; width:240px;"
                                  onchange="populateDSDivisions('districtId','dsDivisionId','mrDivisionId', 'Marriage', true)"/>
                    </td>
                    <td></td>
                    <td>
                        <s:label value="%{getText('select_DS_division.label')}"/>
                    </td>
                    <td>
                        <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  value="dsDivisionId"
                                  cssStyle="width:98.5%; width:240px;"
                                  onchange="populateDivisions('dsDivisionId', 'mrDivisionId', 'Marriage', true)"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:label value="%{getText('select_BD_division.label')}"/>
                    </td>
                    <td>
                        <s:select id="mrDivisionId" name="mrDivisionId" list="mrDivisionList"
                                  value="mrDivisionId"
                                  cssStyle="width:98.5%; width:240px;"/>
                    </td>
                    <td colspan="3">
                        &nbsp;
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:label value="%{getText('label.state')}"/>
                    </td>
                    <td>
                        <s:select id="state" name="state" list="stateList" headerKey="-1"
                                  cssStyle="width:98.5%; width:240px;"/>
                    </td>
                    <td colspan="3">
                        <div class="form-submit">
                            <s:submit value="%{getText('bdfSearch.button')}"/>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </s:form>
    <s:form action="eprMarriageRegisterSearch.do" id="searchByPIN" method="POST"
            onsubmit="javascript:return validatePINNumber()">
        <div id="fragment-2">
            <table>
                <caption/>
                <col width="280px"/>
                <col width="10px"/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:label value="%{getText('label.marriageregister.pin')}"/>
                    </td>
                    <td><s:textfield name="pinOrNic" id="pinOrNic" maxLength="10"/></td>
                    <td>
                        <div class="form-submit">
                            <s:submit value="%{getText('bdfSearch.button')}"/>
                        </div>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>
    </s:form>
    <s:form action="eprMarriageRegisterSearch.do" id="searchBySerial" method="POST"
            onsubmit="javascript:return validateSerial()">
        <div id="fragment-3">
            <table>
                <caption/>
                <col width="280px"/>
                <col width="10px"/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:label value="%{getText('label.marriageregister.serial')}"/>
                    </td>
                    <td>
                        <s:textfield id="serialNumber" name="noticeSerialNo" cssStyle="width:232px;" maxLength="10"/>
                    </td>
                    <td>
                        <div class="form-submit">
                            <s:submit value="%{getText('bdfSearch.button')}"/>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </s:form>
    <s:form action="eprMarriageRegisterSearch.do" id="searchByIdUKey" method="POST"
            onsubmit="javascript:return validateMarriageIdUkey()">
        <div id="fragment-4">
            <table>
                <caption/>
                <col width="280px"/>
                <col width="10px"/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:label value="%{getText('label.marriageregister.number')}"/>
                    </td>
                    <td>
                        <s:textfield id="marriageIdUKey" name="marriageIdUKey" cssStyle="width:232px;"
                                     maxLength="10" value=""/>
                    </td>
                    <td>
                        <div class="form-submit">
                            <s:submit value="%{getText('bdfSearch.button')}"/>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </s:form>
</div>
<div id="marriage-notice-search" style="margin-top:58px;">
    <s:if test="marriageRegisterSearchList.size > 0">
        <%--table for displaying marriage registers--%>
        <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
            <legend><b><s:label value="%{getText('searchResult.label')}"/> </b></legend>
            <table id="search-result" width="100%" cellpadding="0" cellspacing="0" class="display"
                   style="font-size:10pt;">
                <thead>
                <tr>
                    <th width="70px"><s:label value="%{getText('serial.label')}"/></th>
                    <th><s:label value="%{getText('label.marriageregister.nameofmale')}"/></th>
                    <th><s:label value="%{getText('label.marriageregister.nameoffemale')}"/></th>
                    <th width="150px"></th>
                </tr>
                </thead>
                <tbody>
                <s:iterator status="approvalStatus" value="marriageRegisterSearchList">
                    <tr>
                        <td align="center">
                            <s:if test="serialNumber==0">
                                &nbsp;
                            </s:if>
                            <s:else>
                                <s:property value="serialNumber"/>
                            </s:else>
                        </td>
                        <td>
                                <%-- todo : veryfy the below method returns the correct value --%>
                            <%=NameFormatUtil.getDisplayName(request.getAttribute("male.nameInOfficialLanguageMale").toString(), 60)%>
                        </td>
                        <td>
                            <%=NameFormatUtil.getDisplayName(request.getAttribute("female.nameInOfficialLanguageFemale").toString(), 60)%>
                        </td>
                        <td>
                                <%-- Register licenced marriage  --%>
                            <s:if test="(state.ordinal()==7)">  <%-- Licence printed --%>
                                <s:url id="registerSelected" action="eprMarriageRegistrationInit.do">
                                    <s:param name="idUKey" value="idUKey"/>
                                    <s:param name="mode">register</s:param>
                                </s:url>
                                <s:a href="%{registerSelected}" title="%{getText('tooltip.marriageregister.register')}">
                                    <img src="<s:url value='/images/couple.jpg'/>" width="25" height="25"
                                         border="none"/>
                                </s:a>
                            </s:if>
                            <s:else>
                                <%-- print extract of marriage  --%>
                                <s:if test="(state.ordinal()==9 || state.ordinal()==11)"> <%-- Registration approved or Extract Printed --%>
                                    <s:url id="printExtract" action="eprMarriageExtractInit.do">
                                        <s:param name="idUKey" value="idUKey"/>
                                        <s:param name="mode">print</s:param>
                                    </s:url>
                                    <s:a href="%{printExtract}" title="%{getText('tooltip.marriageextract.print')}">
                                        <img src="<s:url value='/images/print_icon.gif'/>" border="none" height="25"/>
                                    </s:a>
                                </s:if>
                                <s:elseif
                                        test="(state.ordinal()!=10 && state.ordinal()!=12)">   <%-- If Not rejected  --%>
                                    <%-- Edit  --%>
                                    <s:if test="serialNumber==0">
                                        <s:url id="editSelected" action="eprMarriageRegistrationInit.do">
                                            <s:param name="idUKey" value="idUKey"/>
                                            <s:param name="mode">register</s:param>
                                        </s:url>
                                    </s:if>
                                    <s:else>
                                        <s:url id="editSelected" action="eprMarriageRegistrationInit.do">
                                            <s:param name="idUKey" value="idUKey"/>
                                        </s:url>
                                    </s:else>
                                    <s:a href="%{editSelected}" title="%{getText('editToolTip.label')}">
                                        <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                             border="none"/>
                                    </s:a>

                                    <%-- Approve  --%>
                                    <s:if test="(!#session.user_bean.role.roleId.equals('DEO'))">
                                        <s:url id="approveSelected" action="eprApproveMarriageRegistration.do">
                                            <s:param name="idUKey" value="idUKey"/>
                                        </s:url>
                                        <s:a href="%{approveSelected}" title="%{getText('approveToolTip.label')}">
                                            <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                                 border="none"/>
                                        </s:a>

                                        <%-- Reject --%>
                                        <s:url id="rejectSelected" action="eprMarriageRegisterRejectInit.do">
                                            <s:param name="idUKey" value="idUKey"/>
                                            <s:param name="mode">reject</s:param>
                                        </s:url>
                                        <s:a href="%{rejectSelected}" title="%{getText('rejectToolTip.label')}">
                                            <img src="<s:url value='/images/reject.gif'/>" width="25" height="25"
                                                 border="none"/>
                                        </s:a>
                                    </s:if>
                                </s:elseif>

                                <%-- View  --%>
                                <s:if test="(state.ordinal()==9|| state.ordinal()==10 || state.ordinal()==11 ||
                                 state.ordinal()==12)"><%-- Registration rejected, approved or Extract Printed --%>
                                    <s:url id="view" action="eprViewMarriageRegister.do">
                                        <s:param name="idUKey" value="idUKey"/>
                                        <s:param name="mode">view</s:param>
                                    </s:url>
                                    <s:a href="%{view}" title="%{getText('tooltip.view')}">
                                        <img src="<s:url value='/images/view_icon.png'/>" width="25" height="25"
                                             border="none"/>
                                    </s:a>
                                </s:if>
                                <%-- View scanned marriage cert  --%>
                                <s:if test="(scannedImagePath != null) && state.ordinal()!=10">
                                    <s:url id="printCert" action="eprDisplayScannedImage.do">
                                        <s:param name="idUKey" value="idUKey"/>
                                    </s:url>
                                    <s:a href="%{printCert}" title="%{getText('tooltip.scannedmarriagecert.print')}">
                                        <img src="<s:url value='/images/print_image.jpeg'/>" width="30" height="30"
                                             border="none"/>
                                    </s:a>
                                </s:if>
                            </s:else>
                            <s:if test="(!#session.user_bean.role.roleId.equals('DEO')) && state.ordinal()==11">
                                <s:url id="divorce" action="eprMarriageRegisterDivorceInit">
                                    <s:param name="idUKey" value="idUKey"/>
                                    <s:param name="mode">divorce</s:param>
                                </s:url>
                                <s:a href="%{divorce}" title="%{getText('tooltip.marriageregister.divorce')}">
                                    <img src="<s:url value='/images/warning.png'/>" width="30" height="30"
                                         border="none"/>
                                </s:a>
                            </s:if>
                        </td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
        </fieldset>
    </s:if>
</div>
<s:hidden id="errorSerialNumber"
          value="%{getText('error.invalid') + getText('label.marriageregister.serial')}"/>
<s:hidden id="errorRegistrarPIN"
          value="%{getText('error.invalid') + getText('label.marriageregister.pin')}"/>
<s:hidden id="errorStartDate"
          value="%{getText('error.invalid') + getText('date.from.label')}"/>
<s:hidden id="errorEndDate"
          value="%{getText('error.invalid') + getText('date.to.label')}"/>
<s:hidden id="errorMarriageIdUKey"
          value="%{getText('error.invalid') + getText('label.marriageregister.number')}"/>


