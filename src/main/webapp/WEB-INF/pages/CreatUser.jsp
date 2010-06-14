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
    <s:form name="userCreationForm" action="eprUserCreation" method="POST">
        <div>
            <s:label value="%{getText('user_id.label')}" />
            <s:textfield name="user.userId"/>
        </div><div>
            <s:label value="%{getText('user_name.label')}" />
            <s:textfield name="user.userName"/>
        </div>
        <div>
            <s:label value="%{getText('user_pin.label')}"/>
            <s:textfield name="user.pin" />
        </div>
        <div>
            <s:label value="%{getText('preffered_language.label')}" />
            <s:select list="#@java.util.HashMap@{'en':'English','si':'සිංහල','ta':'Tamil'}" name="user.prefLanguage" ></s:select>
        </div>
        <div>
            <s:label value="%{getText('assigned_districts.label')}" />
            <s:select list="districtList" name="assignedDistricts" multiple="true" size="10" />
            <s:submit value="%{getText('get_ds_divisions.label')}" name="divisions"/>
        </div>
        <s:if test="divisionList != null">
        <div>
            <s:label>
                <s:label value="%{getText('assigned_ds_divisions.label')}" />
                <s:select list="divisionList" name="assignedDivisions" multiple="true" size="10" />
            </s:label>
        </div>
        </s:if>
        <div>
            <s:label>
                <s:label value="%{getText('user_role.label')}" />
                <s:select list="roleList" name="roleId" />
            </s:label>
        </div>
    <%--    <s:hidden name="pageNo" value="1" />--%>
        <s:submit value="%{getText('create_user.label')}"  />
    </s:form>
</div>