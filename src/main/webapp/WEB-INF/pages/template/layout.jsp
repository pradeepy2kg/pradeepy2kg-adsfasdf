<%@ page import="lk.rgd.crs.web.WebConstants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>E-Population System</title>
    <link rel="stylesheet" type="text/css" href='<s:url value="/css/style.css"/>'/>
    <link rel="stylesheet" type="text/css" href='<s:url value="/css/layout.css"/>'/>
    <link rel="stylesheet" type="text/css" media="print" href='<s:url value="/css/print.css"/>'/>
    <style type="text/css">
        @font-face
        {
            font-family: Potha
        ;
            src: url(POTHA0.eot)
        ;
        }
        @font-face
        {
            font-family: Bamini
        ;
            src: url(eot/BAMINI0.eot)
        ;
        @font-face
        {
        font-family
        :
        Latha
        ;
            src: url(eot/LATHA0.eot)
        ;
    </style>

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
            <tiles:insertAttribute name="menu"/>
            <br>
            <%if (session.getAttribute(WebConstants.SESSION_USER_NAME) != null) { %>
            <s:form action="eprLogout.do" method="POST" name="eprLogout">
                <s:submit name="submit" value="logout"></s:submit>
            </s:form>
            <% } %>
        </div>
        <div id="body-content">
            <div id="body-content-title">
                <tiles:insertAttribute name="title"/>
            </div>
            <div id="body-content-data">
                <tiles:insertAttribute name="body"/>
            </div>
        </div>
    </div>
    <%-- Footer --%>
    <div id="layout-footer">
        <tiles:insertAttribute name="footer"/>
    </div>
</div>
</body>
</html>