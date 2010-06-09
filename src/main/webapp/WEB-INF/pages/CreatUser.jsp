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
            <s:label value="%{getText('preffered_district.label')}" />

        </div>
        <div>
            <s:label value="%{getText('preffered_division.label')}" />
            
        </div>
        <s:hidden name="pageNo" value="1" />
        <s:submit value="%{getText('create_user.label')}"  />
    </s:form>
</div>