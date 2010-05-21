<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="page-title">
    <% out.write(session.getAttribute("page_title").toString());%>
</div>