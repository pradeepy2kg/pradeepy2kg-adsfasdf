<%--
  Created by IntelliJ IDEA.
  User: amith23
  Date: Jun 9, 2010
  Time: 10:18:37 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<div id="create-user-outer">
    <table>

        <s:form name="userCreationForm" action="eprUserCreation" method="POST">
        <s:if test="userId == null">
            <tr>
                <div>
                    <td><s:label value="%{getText('user_id.label')}"/></td>

                    <td><s:textfield name="user.userId"/></td>


                </div>
            </tr>
        </s:if>
        <tr>
            <div>
                <td><s:label value="%{getText('user_name.label')}"/></td>
                <td><s:textfield name="user.userName"/></td>
            </div>
        </tr>
        <tr>
            <div>
                <td><s:label value="%{getText('user_pin.label')}"/></td>
                <td></address><s:textfield name="user.pin"/></td>
            </div>
        </tr>
        <tr>
            <div>
                <td><s:label value="%{getText('preffered_language.label')}"/></td>
                <td><s:select list="#@java.util.HashMap@{'en':'English','si':'සිංහල','ta':'Tamil'}"
                              name="user.prefLanguage"></s:select></td>
            </div>
        </tr>
        <tr>
            <div>
                <td><s:label value="%{getText('assigned_districts.label')}"/></td>
                <td><s:select list="districtList" name="assignedDistricts" multiple="true" size="10"/>
                    <s:submit value="%{getText('get_ds_divisions.label')}" name="divisions"/></td>
            </div>

            <s:if test="divisionList != null">
        </tr>
        <div>
            <s:label>
            <td><s:label value="%{getText('assigned_ds_divisions.label')}"/></td>
            </tr>
            <tr>
                <td></td>
                <td><s:select list="divisionList" name="assignedDivisions" multiple="true" size="10"/></td>
                </s:label>
        </div>
        </tr>
        </s:if>
        <tr>
        <div>
            <td>
                <s:label>
                    <s:label value="%{getText('user_role.label')}"/>
            <td><s:select list="roleList" name="roleId"/>
                </s:label>
            </td>
        </div>
            </tr>
        <tr><td>
                <%--    <s:hidden name="pageNo" value="1" />--%>
            <s:if test="userId != null">
                <s:hidden name="userId" value="%{userId}"/>
                <s:submit value="%{getText('edit_user.label')}"/>
            </s:if>
            <s:if test="userId == null">
                <s:submit value="%{getText('create_user.label')}"/>
            </s:if>
            </s:form>
            </td>
        </tr>
    </table>
</div>