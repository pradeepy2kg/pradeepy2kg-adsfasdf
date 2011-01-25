<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>


<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

<script type="text/javascript" language="javascript" src="../lib/jquery/jquery.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script>
    $(document).ready(function() {
        $('#users-list-table').dataTable({
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

<style type="text/css">
    #search-title {
        width:100%;
        text-align:center;
        height:50px;
        font-size:16px;
        color:blue;
    }
</style>

<div id="view-users">
    <s:actionmessage cssStyle="text-align:left;"/>

    <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
        <legend>Search Options</legend>
        <s:form action="eprViewSelectedUsers.do" name="viewUsers" id="view_usrs_form" method="POST">
        <table>

            <table class="view-users-table">
                <caption></caption>
                <col/>
                <col/>
                <col/>
                <col/>
                <col/>
                <col/>
                <tbody>
                <tr>
                    <td style="width:75px;"><label> Role </label></td>
                    <td style="width:260px;"><s:select name="roleId" list="roleList" headerKey="ALL"
                                                       headerValue="ALL"/></td>
                    <td rowspan="2" style="width:75px;">OR</td>
                    <td rowspan="2"><label>Name </label></td>
                    <td rowspan="2"><s:textfield name="nameOfUser" cssStyle="text-transform:none;"/></td>
                    <td rowspan="2">
                        <div class="form-submit"><s:submit value="   SEARCH   "/></div>
                    </td>
                </tr>
                <tr>
                    <td><label>District</label></td>
                    <td><s:select name="userDistrictId" list="districtList" headerKey="0"
                                  headerValue="ALL"/></td>
                </tr>
                </tbody>
            </table>

            </s:form>
    </fieldset>
    <s:if test="#session.viewUsers!=null">
            <div id="search-title">
                 Search Results for : <s:property value="%{selectedRole}"/>
            </div>
        <fieldset style="border:none">
            <s:form name="users_print" action="" method="POST">
                <table id="users-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
                    <thead>
                    <tr>
                        <th><s:label name="name" value="    "/></th>
                        <th><s:label name="name" value="Name"/></th>
                        <th><s:label name="edit" value="Edit User"/></th>
                        <th><s:label name="edit" value="Delete User"/></th>
                        <th><s:label name="edit" value="Active User"/></th>
                        <th><s:label name="edit" value="User Location"/></th>
                        <s:set name="allowEdit" value="true"/>
                    </tr>
                    </thead>
                    <tbody>
                    <s:iterator status="userListStatus" value="#session.viewUsers">
                        <tr>
                            <td><s:property value="%{#userListStatus.count}"/></td>
                            <td><s:property value="userName"/></td>
                            <s:url id="editSelected" action="eprInitUserCreation.do">
                                <s:param name="userId" value="userId"/>
                            </s:url>
                            <td align="center"><s:a href="%{editSelected}"><img
                                    src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                    border="none"/></s:a>
                            </td>
                            <s:url id="deleteSelected" action="eprInactiveUsers.do">
                                <s:param name="userId" value="userId"/>
                            </s:url>
                            <td align="center">                                
                                    <s:a href="%{deleteSelected}"><img
                                            src="<s:url value='/images/delete.gif'/>" width="25" height="25"
                                            border="none"/></s:a>
                            </td>
                            <s:url id="activeSelected" action="eprActiveUsers.do">
                                <s:param name="userId" value="userId"/>
                            </s:url>
                            <s:url id="inactiveSelected" action="eprDoInactiveUsers.do">
                                <s:param name="userId" value="userId"/>
                            </s:url>
                            <td align="center">
                                <s:if test="!(lifeCycleInfo.active)">
                                    <s:a href="%{activeSelected}">
                                        <img src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                             border="none"/></s:a>
                                </s:if>
                                <s:if test="(lifeCycleInfo.active)">
                                    <s:a href="%{inactiveSelected}">
                                        <img src="<s:url value='/images/reject.gif'/>" width="25" height="25"
                                             border="none"/></s:a>
                                </s:if>
                            </td>

                            <s:url id="assignedUserLocation" action="eprInitAssignedUserLocation.do">
                                <s:param name="userId" value="userId"/>
                            </s:url>
                            <td align="center"><s:a href="%{assignedUserLocation}"><img
                                    src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                    border="none"/></s:a>
                            </td>
                        </tr>
                    </s:iterator>
                    </tbody>
                </table>
            </s:form>
        </fieldset>
    </s:if>

</div>



