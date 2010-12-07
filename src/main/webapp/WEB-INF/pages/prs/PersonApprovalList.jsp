<%@ page import="lk.rgd.common.util.NameFormatUtil" %>
<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="../js/validate.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        $('#tab1').tabs();
        $('#tabs2').tabs({
            select: function(ui) {
                document.getElementById('searchNic').value = null;
                document.getElementById('searchTempPin').value = null;
            }
        });
    });

    function initPage() {
    }

    $(function() {
        $("#person-approve-list").dataTable({
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
        var returnVal = true;

        // validate serial number
        domObject = document.getElementById('searchNic');
        if (!isFieldEmpty(domObject)) {
            validateNIC(domObject, 'error1', 'error2');
        }

        // validate start and end date
        domObject = document.getElementById('searchTempPin');
        if (!isFieldEmpty(domObject)) {
            validateTemPIN(domObject.value, 'error1', 'error3');
        }

        if (errormsg != "") {
            alert(errormsg);
            returnVal = false;
        }
        errormsg = "";
        return returnVal;
    }
</script>

<div id="person-approval-list">
    <s:form action="eprPersonApproval.do" method="POST" onsubmit="javascript:return validate()">
        <div id="tab1" style="font-size:10pt;">
            <ul>
                <li><a href="#tab1"><span><b><s:label value="%{getText('searchOption.label')}"/></b></span></a></li>
            </ul>

            <div id="tabs2">
                <table width="100%" cellpadding="5" cellspacing="0" style="margin-bottom:5px;margin-left:10px;">
                    <col width="300px"/>
                    <col/>
                    <col width="100px"/>
                    <col width="300px"/>
                    <col/>
                    <tbody>
                    <tr>
                        <td><s:label value="%{getText('registrationLocation.label')}"/></td>
                        <td>
                            <s:select name="locationId" list="locationList"/>
                        </td>
                        <td></td>
                        <td></td>
                        <td class="button" align="right">
                        </td>
                    </tr>
                    </tbody>
                </table>
                <ul>
                    <li><a href="#subtab1"><span><s:label value="%{getText('by.nic.label')}"/></span></a></li>
                    <li><a href="#subtab2"><span><s:label value="%{getText('by.tempPin.label')}"/></span></a></li>
                </ul>
                <div id="subtab1" style="margin-left:10px;">
                    <table>
                        <caption/>
                        <col width="280px"/>
                        <col width="10px"/>
                        <col/>
                        <tbody>
                        <tr>
                            <td>
                                <s:label value="%{getText('nic.label')}"/>
                            </td>
                            <td></td>
                            <td>
                                <s:textfield name="searchNic" id="searchNic" maxLength="10"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div id="subtab2">
                    <table style="margin-left:10px;">
                        <caption/>
                        <col width="280px"/>
                        <col width="10px"/>
                        <col/>
                        <tbody>
                        <tr>
                            <td>
                                <s:label value="%{getText('tempPin.label')}"/>
                            </td>
                            <td></td>
                            <td>
                                <s:textfield name="searchTempPin" id="searchTempPin" maxLength="10"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="form-submit" style="float:right;">
            <s:submit value="%{getText('bdfSearch.button')}"/>
        </div>

        <div id="person-approval-search" style="margin-top:58px;">
            <s:actionmessage cssClass="alreadyPrinted"/>
            <s:if test="approvalPendingList.size > 0">
                <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
                    <legend><b><s:label value="%{getText('searchResult.label')}"/></b></legend>
                    <table id="person-approve-list" width="100%" cellpadding="0" cellspacing="0" class="display">
                        <thead>
                        <tr>
                            <th width="60px"><s:label value="%{getText('locationCode.label')}"/></th>
                            <th width="110px">NIC</th>
                            <th width="780px"><s:label value="%{getText('label.personName')}"/></th>
                            <th width="15px"></th>
                            <th width="15px"></th>
                            <th width="15px"></th>
                            <th width="15px"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <s:iterator status="approvalStatus" value="approvalPendingList">
                            <tr>
                                <td><s:property value="submittedLocation.locationCode"/></td>
                                <td align="center"><s:property value="nic"/></td>
                                <td>
                                    <s:if test="fullNameInOfficialLanguage != null">
                                        <%= NameFormatUtil.getDisplayName((String) request.getAttribute("fullNameInOfficialLanguage"), 70)%>
                                    </s:if>
                                </td>
                                <td align="center">
                                    <s:url id="editSelected" action="eprEditPerson.do">
                                        <s:param name="personUKey" value="personUKey"/>
                                    </s:url>
                                    <s:a href="%{editSelected}" title="%{getText('editToolTip.label')}">
                                        <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                             border="none"/>
                                    </s:a>
                                </td>
                                <td align="center">
                                    <s:url id="approveSelected" action="eprApprovePerson.do">
                                        <s:param name="personUKey" value="personUKey"/>
                                    </s:url>
                                    <s:a href="%{approveSelected}" title="%{getText('approveToolTip.label')}">
                                        <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                             border="none"/>
                                    </s:a>
                                </td>
                                <td align="center">
                                    <s:url id="rejectSelected">
                                        <s:param name="personUKey" value="personUKey"/>
                                    </s:url>
                                    <s:a href="%{rejectSelected}" title="%{getText('rejectToolTip.label')}">
                                        <img src="<s:url value='/images/reject.gif'/>" width="25" height="25"
                                             border="none"/>
                                    </s:a>
                                </td>
                                <td align="center">
                                    <s:url id="deleteSelected">
                                        <s:param name="personUKey" value="personUKey"/>
                                    </s:url>
                                    <s:a href="%{deleteSelected}" title="%{getText('deleteToolTip.label')}">
                                        <img src="<s:url value='/images/delete.gif'/>" width="25" height="25"
                                             border="none"/>
                                    </s:a>
                                </td>
                            </tr>
                        </s:iterator>
                        </tbody>
                    </table>
                </fieldset>
            </s:if>
        </div>
    </s:form>
</div>
<s:hidden id="error1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error2" value="%{getText('nic.label')}"/>
<s:hidden id="error3" value="%{getText('tempPin.label')}"/>
