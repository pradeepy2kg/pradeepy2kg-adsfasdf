<%@ page import="lk.rgd.common.api.domain.Role" %>
<%@ page import="lk.rgd.common.api.domain.User" %>
<%@ page import="lk.rgd.common.util.WebUtils" %>
<%@ page import="lk.rgd.common.util.RolePermissionUtils" %>
<%@ page import="java.util.Locale" %>
<%@ page import="lk.rgd.AppConstants" %>
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
    String preLang = ((Locale) session.getAttribute(WebConstants.SESSION_USER_LANG)).getLanguage();
    if(preLang == null){
        preLang = user.getPrefLanguage();
    }
%>
<%--Sinhala Links--%>
<% if (AppConstants.SINHALA.equals(preLang)) {%>
<%if (Role.ROLE_DEO.equals(userRole)) {%>
<%@ include file="DEOMenuSi.jsp" %>
<%} else if (Role.ROLE_ADR.equals(userRole)) {%>
<%@ include file="ADRMenuSi.jsp" %>
<%} else if (Role.ROLE_DR.equals(userRole)) {%>
<%@ include file="DRMenuSi.jsp" %>
<%} else if (Role.ROLE_ARG.equals(userRole)) {%>
<%@ include file="ARGMenuSi.jsp" %>
<%} else if (Role.ROLE_RG.equals(userRole)) {%>
<%@ include file="RGMenuSi.jsp" %>
<%} else if (Role.ROLE_ADMIN.equals(userRole)) {%>
<%@ include file="ADMINMenuSi.jsp" %>
<%}%>
<%--Tamil Links--%>
<%} else if (AppConstants.TAMIL.equals(preLang)) {%>
<%if (Role.ROLE_DEO.equals(userRole)) {%>
<%@ include file="DEOMenuTa.jsp" %>
<%} else if (Role.ROLE_ADR.equals(userRole)) {%>
<%@ include file="ADRMenuTa.jsp" %>
<%} else if (Role.ROLE_DR.equals(userRole)) {%>
<%@ include file="DRMenuTa.jsp" %>
<%} else if (Role.ROLE_ARG.equals(userRole)) {%>
<%@ include file="ARGMenuTa.jsp" %>
<%} else if (Role.ROLE_RG.equals(userRole)) {%>
<%@ include file="RGMenuTa.jsp" %>
<%} else if (Role.ROLE_ADMIN.equals(userRole)) {%>
<%@ include file="ADMINMenuTa.jsp" %>
<%}%>
<%--English Links--%>
<%} else {%>
<%if (Role.ROLE_DEO.equals(userRole)) {%>
<%@ include file="DEOMenuEng.jsp" %>
<%} else if (Role.ROLE_ADR.equals(userRole)) {%>
<%@ include file="ADRMenuEng.jsp" %>
<%} else if (Role.ROLE_DR.equals(userRole)) {%>
<%@ include file="DRMenuEng.jsp" %>
<%} else if (Role.ROLE_ARG.equals(userRole)) {%>
<%@ include file="ARGMenuEng.jsp" %>
<%} else if (Role.ROLE_RG.equals(userRole)) {%>
<%@ include file="RGMenuEng.jsp" %>
<%} else if (Role.ROLE_ADMIN.equals(userRole)) {%>
<%@ include file="ADMINMenuEng.jsp" %>
<%} }%>

