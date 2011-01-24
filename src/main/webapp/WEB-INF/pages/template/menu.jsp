<%@ page import="lk.rgd.common.api.domain.Role" %>
<%@ page import="lk.rgd.common.api.domain.User" %>
<%@ page import="lk.rgd.common.util.WebUtils" %>
<%@ page import="lk.rgd.common.util.RolePermissionUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" type="text/css" href='/ecivil/css/menu.css'/>
<script src="/ecivil/lib/jquery/jquery.min.js" type="text/javascript"></script>
<script src="/ecivil/js/menu.js" type="text/javascript"></script>
<style type="text/css">

</style>
<%
    User user = (User) session.getAttribute("user_bean");
    String userRole = user.getRole().getRoleId();
%>
<%if (Role.ROLE_DEO.equals(userRole)) {%>
<%@ include file="DEOMenu.jsp" %>
<%} else if (Role.ROLE_ADR.equals(userRole)) {%>
<%@ include file="ADRMenu.jsp" %>
<%} else if (Role.ROLE_DR.equals(userRole)) {%>
<%@ include file="DRMenu.jsp" %>
<%} else if (Role.ROLE_ARG.equals(userRole)) {%>
<%@ include file="ARGMenu.jsp" %>
<%} else if (Role.ROLE_RG.equals(userRole)) {%>
<%@ include file="RGMenu.jsp"%>
<%} else if (Role.ROLE_ADMIN.equals(userRole)) {%>
<%@ include file="ADMINMenu.jsp"%>
<%}%>


