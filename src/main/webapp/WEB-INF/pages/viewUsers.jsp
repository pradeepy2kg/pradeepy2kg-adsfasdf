<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>


<div id="User_Preference_Action">

    <s:form action="eprViewSelectedUsers.do" name="viewUsers" id="view_usrs_form" method="POST">

        <table>

            <tr>
                <td>

                    <label>භූමිකාව Role in tamail Role</label>

                </td>
                <td>
                    <s:select name="roleId" list="roleList" headerKey="0"
                              headerValue="%{getText('select_Role.label')}"/>
                </td>
                <td>

                    <label>දිස්ත්‍රික්කය மாவட்டம் District</label>

                </td>
                <td>
                    <s:select name="userDistrictId" list="districtList" headerKey="0"
                              headerValue="%{getText('select_district.label')}"/>
                </td>
                <td>

                    <s:textarea name="nameOfUser"/>

                </td>
                <td>
                   <s:submit value="%{getText('search.label')}"/>
    </s:form>
                </td>
            </tr>

        </table>
        <s:form name="users_print" action="" method="POST">
        <table>


            <tr>
                <th><s:label name="name" value="    "/></th>
                <th><s:label name="name" value="%{getText('nameOfUser.label')}"/></th>
                <th><s:label name="edit" value="%{getText('editUser.label')}"/></th>
                <th><s:label name="edit" value="%{getText('editUser.label')}"/></th>
                <s:set name="allowEdit" value="true"/>
            </tr>

            <s:iterator status="userListStatus" value="#session.viewUsers">
                <tr>
                    <td><s:property value="%{#userListStatus.count}"/></td>
                    <td><s:property value="userName"/></td>
                    <s:if test="#allowEdit==true">
                        <s:url id="editSelected" action="eprBirthRegistration.do">
                            <s:param name="userID" value=""/>
                        </s:url>
                        <td align="center"><s:a href="%{editSelected}" title="">
                            <img src="<s:url value='/images/edit.jpg'/>" width="25" height="25"
                                 border="none"/></s:a>
                        </td>
                        <td>

                            <s:url id="deleteSelected" action="eprDeleteApprovalPending.do">
                                <s:param name="bdId" value=""/>
                            </s:url>
                        <td align="center"><s:a href="%{deleteSelected}"
                                                title="%{getText('deleteToolTip.label')}"><img
                                src="<s:url value='/images/delete.png'/>" width="25" height="25" border="none"/></s:a>
                        </td>
                    </s:if>


                </tr>
            </s:iterator>


        </table>
            </s:form>

</div>



