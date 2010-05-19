<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>E-Population System</title>
    <link rel="stylesheet" type="text/css" href='<s:url value="/css/style.css"/>'/>
    <link rel="stylesheet" type="text/css" href='<s:url value="/css/layout.css"/>'/>
    <link rel="stylesheet" type="text/css" media="print" href='<s:url value="/css/print.css"/>'/>
</head>
<body>
<div id="wrapper">
    <%-- Header  --%>
    <div id="layout-header">
        <tiles:insertAttribute name="header"/>
    </div>
    <%-- Menu & Body--%>
    <div id="layout-body">
        <div id="body-sidebar">
            <s:form action="eprLanguage.do" method="POST"
                    name="eprLanguage">
                <s:label value="Select your language"></s:label>
                <s:select list="{'English','Sinhala','Tamil'}" name="language"></s:select>
                <s:submit value="select"></s:submit>
            </s:form><br><br>
            <tiles:insertAttribute name="menu"/>
        </div>
        <div id="body-content">
            <tiles:insertAttribute name="body"/>
        </div>
    </div>
    <%-- Footer --%>
    <div id="layout-footer">
        <tiles:insertAttribute name="footer"/>
    </div>
</div>
</body>
</html>