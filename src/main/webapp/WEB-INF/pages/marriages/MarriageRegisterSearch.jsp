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
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="../js/validate.js"></script>
<script>
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
</script>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage cssStyle="color:blue;font-size:10pt"/>
<s:form action="eprMarriageRegisterSearch.do" method="POST" onsubmit="javascript:return validate()">
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
                        <s:label value="%{getText('serial.label')}"/>
                    </td>
                    <td>
                        <s:textfield id="noticeSerialNo" name="noticeSerialNo" cssStyle="width:232px;" maxLength="10"/>
                    </td>
                    <td></td>
                    <td>
                        <s:label value="%{getText('label.state')}"/>
                    </td>
                    <td>
                        <s:select id="state" name="state" list="stateList" headerKey="-1"
                                  cssStyle="width:98.5%; width:240px;"/>
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
                    <td></td>
                    <td>

                    </td>
                    <td>

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
    <s:if test="marriageRegisterSearchList.size > 0">
        <%--table for displaying marriage registers--%>
        <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
            <legend><b><s:label value="%{getText('searchResult.label')}"/> </b></legend>
            <table id="search-result" width="100%" cellpadding="0" cellspacing="0" class="display"
                   style="font-size:10pt;">
                <thead>
                <tr>
                    <th width="70px"><s:label value="%{getText('serial.label')}"/></th>
                    <th><s:label value="%{getText('partyName.label')}"/></th>
                    <th width="150px" align="center"><s:label value="%{getText('pin.label')}"/></th>
                    <th width="150px"></th>
                </tr>
                </thead>
                <tbody>
                <s:iterator status="approvalStatus" value="marriageRegisterSearchList">
                    <tr>
                        <td align="center">
                            <s:property value="serialNumber"/>
                        </td>
                        <td>
                            <s:property value="male.nameInOfficialLanguageMale"/>
                        </td>
                        <td align="center">
                            <s:property value="male.identificationNumberMale"/>
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
                                <s:elseif test="(state.ordinal()!=10)">   <%-- If Not rejected  --%>
                                    <%-- Edit  --%>
                                    <s:if test="serialNumber==null">
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
                                <s:if test="(state.ordinal()==9|| state.ordinal()==10 || state.ordinal()==11)"><%-- Registration rejected, approved or Extract Printed --%>
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
