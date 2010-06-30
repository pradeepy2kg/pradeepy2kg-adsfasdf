<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<div id="User_Preference_Action">
    <s:form action="eprViewSelectedUsers.do" name="viewUsers" id="view_usrs_form" method="POST">
        <table align="center">
            <tr>
                <td>
                    <label> Role </label>
                    <s:select name="roleId" list="roleList" headerKey="0"
                              headerValue="ALL"/></td>
                <td>
                    <label>District</label>
                    <s:select name="userDistrictId" list="districtList" headerKey="0"
                              headerValue="ALL"/>
                </td>
            </tr>
            <tr>
                <td align="right"><label>OR</label></td>
                <td></td>
            </tr>
        </table>
        <table align="center">
            <tr>
                <td align="center">
                    <label>Name </label> <s:textfield name="nameOfUser"/>
                </td>
            </tr>
            <tr>
                <td align="center"><s:submit value="   SEARCH   "/></td>
            </tr>
        </table>
    </s:form>
    <s:if test="#session.viewUsers!=null">
        <s:form name="users_print" action="" method="POST">
            <table align="center">
                <tr>
                    <th><s:label name="name" value="    "/></th>
                    <th><s:label name="name" value="Name"/></th>
                    <th><s:label name="edit" value="Edit User"/></th>
                    <th><s:label name="edit" value="Delete User"/></th>
                    <s:set name="allowEdit" value="true"/>
                </tr>
                <s:iterator status="userListStatus" value="#session.viewUsers">
                    <tr>
                        <td><s:property value="%{#userListStatus.count}"/></td>
                        <td><s:property value="userName"/></td>
                        <s:url id="editSelected" action="eprInitUserCreation.do">
                            <s:param name="userId" value="userId"/>
                        </s:url>
                        <td align="center"><s:a href="%{editSelected}"><img
                                src="<s:url value='/images/edit.jpg'/>" width="25" height="25"
                                border="none"/></s:a>
                        </td>
                        <s:url id="deleteSelected" action="eprdeleteUsers.do">
                            <s:param name="userId" value="userId"/>
                        </s:url>
                        <td align="center"><s:a href="%{deleteSelected}"><img
                                src="<s:url value='/images/delete.png'/>" width="25" height="25"
                                border="none"/></s:a>
                        </td>
                    </tr>
                </s:iterator>
            </table>
        </s:form>
    </s:if>
</div>



