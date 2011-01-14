<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<div id="home-page-outer">
    <s:actionmessage cssClass="actionmessage"/>
    <img src="${pageContext.request.contextPath}/crs/Image?idUKey=<s:property value='idUKey'/>" />
</div>
