<%@ page import="lk.rgd.crs.web.util.Constant" %>
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
    <style type="text/css">
        @font-face {font-family: Potha;src: url(POTHA0.eot);}
        @font-face { font-family:Bamini; src:url(eot/BAMINI0.eot);@font-face { font-family:Latha; src:url(eot/LATHA0.eot);
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
            <s:form action="eprLanguage.do" method="POST"
                    name="eprLanguage">
                <s:label value="Select your language"></s:label>
                <s:select list="{'English','Sinhala','Tamil'}" name="language"></s:select>
                <s:submit value="select"></s:submit>
            </s:form><br><br>
            <tiles:insertAttribute name="menu"/>
            <br><br>
            <%if(session.getAttribute(Constant.SESSION_USER_NAME)!= null){ %>
                <s:form action="eprLogout.do" method="POST" name="eprLogout">
                    <s:submit name="submit" value="logout"></s:submit>
                </s:form>
            <% } %>
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