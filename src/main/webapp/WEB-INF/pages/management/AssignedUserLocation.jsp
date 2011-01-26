<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>


<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage/>
<%--<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" language="javascript" src="../lib/jquery/jquery.js"></script>--%>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script>

    $(function() {
        $("#startDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $("#endDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });
    $(document).ready(function() {
        $('#user-location-list-table').dataTable({
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

    function initPage() {
    }

</script>
<div id="user-locaton-outer">
    <table style="border:none;height:10px;" align="center">
        <tr>
            <td style="border:none;text-align:center;">
                <s:fielderror name="duplicateIdNumberError" cssStyle="color:red;font-size:10pt"/>
            </td>
        </tr>
    </table>
    <s:form action="eprAssignedUserLocation.do">
        <table cellpadding="0" cellspacing="0" style="" align="center" id="">
            <tr>
                <td colspan="1" style="height:30px;"><s:label value="userId" cssClass="user-locaton-outer-lable"/></td>
                <td colspan="2"><s:label name="userId" cssClass="user-locaton-outer-lable"/></td>
            </tr>
            <tr>
                <td colspan="1"><s:label value="Location List" cssClass="user-locaton-outer-lable"/></td>
                <td colspan="2">
                    <s:if test="pageType==1">
                        <s:select list="locationList" name="locationId" disabled="true"/>
                        <s:hidden name="locationId"/>
                    </s:if>
                    <s:else>
                        <s:select list="locationList" name="locationId"/>
                    </s:else>
                </td>
            </tr>
            <tr>
                <td rowspan="2"><s:label value="sign" cssClass="user-locaton-outer-lable"/></td>
                <td><s:label value="Birth Certificate" cssClass="user-locaton-outer-lable"/><s:checkbox
                        name="userLocation.signBirthCert"
                        cssStyle="float:right;"/></td>
                <td><s:label value="Death Certificate" cssClass="user-locaton-outer-lable"/><s:checkbox
                        name="userLocation.signDeathCert"
                        cssStyle="float:right"/></td>
            </tr>
            <tr>

                <td><s:label value="Marrage Certificate" cssClass="user-locaton-outer-lable"/><s:checkbox
                        name="userLocation.signMarriageCert"
                        cssStyle="float:right;"/></td>
                <td><s:label value="Adoption Certificate" cssClass="user-locaton-outer-lable"/><s:checkbox
                        name="userLocation.signAdoptionCert"
                        cssStyle="float:right;"/></td>
            </tr>
            <tr>
                <td style="height:40px;"><s:label value="Date" cssClass="user-locaton-outer-lable"/></td>
                <td><s:label value="Start :" cssClass="user-locaton-outer-lable"/><s:textfield
                        name="userLocation.startDate" cssStyle="float:right;"
                        id="startDatePicker"/></td>
                <td><s:label value="End :" cssClass="user-locaton-outer-lable"/>
                    <s:textfield name="userLocation.endDate"
                                 cssStyle="float:right;"
                                 id="endDatePicker"/>
                </td>
            </tr>
        </table>
        <s:hidden name="userId"/>
        <s:hidden name="pageType"/>
        <div class="form-submit" style="margin-right:85px;">
            <s:if test="pageType==0">
                <s:submit value="SAVE"/>
            </s:if>
            <s:if test="pageType==1">
                <s:submit value="EDIT"/>
            </s:if>
        </div>
    </s:form>
    <s:if test="pageType==0">
        <s:form name="editDivisions" action="eprViewUsers.do" method="POST">
            <s:hidden name="pageType" value="0"/>
            <div class="form-submit">
                <s:submit value="BACK" cssStyle="margin-top:10px;" name="button"/>
            </div>
        </s:form>
    </s:if>
    <s:if test="pageType==1">
        <s:form name="editDivisions" action="eprInitAssignedUserLocation.do" method="POST">
            <s:hidden name="pageType" value="0"/>
            <s:hidden name="userId"/>
            <div class="form-submit">
                <s:submit value="BACK" cssStyle="margin-top:10px;" name="button"/>
            </div>
        </s:form>
    </s:if>

</div>
<s:if test="userLocationNameList !=null">
    <s:if test="userLocationNameList.size()!=0">
        <div style="margin-top:75px;width:80%;margin-left:10%">
            <fieldset style="border:none;">
                <s:form name="users_print" action="" method="POST">
                    <table id="user-location-list-table" cellpadding="0" cellspacing="0" class="display">
                        <thead>
                        <tr>
                            <th style="width:2%;"><s:label name="name" value="    "/></th>
                            <th style="width:70%"><s:label name="name" value="Location Name"/></th>
                            <th></th>
                            <th></th>
                            <th></th>
                            <s:set name="allowEdit" value="true"/>
                        </tr>
                        </thead>
                        <tbody>
                        <s:iterator status="userListStatus" value="userLocationNameList">
                            <tr>
                                <td><s:property value="%{#userListStatus.count}"/></td>
                                <td><s:property value="location.enLocationName"/></td>

                                <s:url id="editPLocation" action="eprEditPrimaryLocation.do">
                                    <s:param name="userId" value="userId"/>
                                    <s:param name="locationId" value="locationId"/>
                                </s:url>
                                    <%--<s:url id="setPLocation" action="eprSetPrimaryLocation.do">
                                        <s:param name="userId" value="userId"/>
                                        <s:param name="locationId" value="locationId"/>
                                    </s:url>--%>
                                <s:url id="editSelected" action="eprInitUserCreation.do">
                                    <s:param name="userId" value="userId"/>
                                </s:url>
                                <s:url id="inactiveSelected" action="eprInactiveUserLocation.do">
                                    <s:param name="userId" value="userId"/>
                                    <s:param name="locationId" value="locationId"/>
                                </s:url>
                                <s:url id="activeSelected" action="eprActiveUserLocation.do">
                                    <s:param name="userId" value="userId"/>
                                    <s:param name="locationId" value="locationId"/>
                                </s:url>
                                <s:url id="editSelected" action="eprEditAssignedUserLocation.do">
                                    <s:param name="userId" value="userId"/>
                                    <s:param name="locationId" value="locationId"/>
                                </s:url>
                                <td align="center">
                                    <s:if test="primaryLocation==location.locationUKey">
                                        <s:a href="%{editPLocation}"><img
                                                src="<s:url value='/images/red_key.png'/>" width="25" height="25"
                                                border="none"/></s:a>
                                    </s:if>
                                    <s:else>
                                        <s:a href="%{editPLocation}"><img
                                                src="<s:url value='/images/yellow_key.png'/>" width="25" height="25"
                                                border="none"/></s:a>
                                    </s:else>
                                </td>
                                <td align="center">
                                    <s:if test="lifeCycleInfo.active">
                                        <s:a href="%{inactiveSelected}"><img
                                                src="<s:url value='/images/reject.gif'/>" width="25" height="25"
                                                border="none"/></s:a>
                                    </s:if>
                                    <s:else>
                                        <s:a href="%{activeSelected}"><img
                                                src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                                border="none"/></s:a>
                                    </s:else>
                                </td>
                                <td align="center">
                                    <s:if test="lifeCycleInfo.active">
                                        <s:a href="%{editSelected}"><img
                                                src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                                border="none"/></s:a>
                                    </s:if>
                                </td>
                            </tr>
                        </s:iterator>
                        </tbody>
                    </table>
                </s:form>
            </fieldset>
        </div>
    </s:if>
</s:if>