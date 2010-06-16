<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="main-menu">
    <s:iterator value="#session.allowed_menue" id="menue">
     <s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/> </s:a>
    </s:iterator>
</div>