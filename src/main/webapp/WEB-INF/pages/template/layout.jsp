<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<%@ page import="lk.rgd.crs.web.WebConstants" %>
<%@ page import="lk.rgd.common.api.domain.User" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<html>
<head>
    <sx:head/>
    <title>E-Population System</title>
    <link rel="stylesheet" type="text/css" href='<s:url value="/css/style.css"/>'/>
    <link rel="stylesheet" type="text/css" href='<s:url value="/css/webform.css"/> '/>
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
    <script type="text/javascript" src="<s:url value="/js/selectAll.js"/>"></script>
    <script type="text/javascript" src='<s:url value="/js/validation.js"/>'></script>
    <script type="text/javascript" src='<s:url value="/js/datemanipulater.js"/>'></script>
     <script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
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
            <%if (((User) session.getAttribute(WebConstants.SESSION_USER_BEAN)).getUserName() != null) { %>
            <s:form action="eprLogout.do" method="POST" name="eprLogout">
                <s:submit name="submit" value="%{getText('logout.label')}"></s:submit>
            </s:form>
            <% } %>
        </div>
        <div id="body-content">
            <div id="body-content-title">
                <div id="page-title">
                    <tiles:importAttribute name="title" toName="title" scope="request"/>
                    <s:label value="%{getText(#request.title)}"/>
                </div>
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