<%@ page import="lk.rgd.crs.web.WebConstants" %>
<%@ page import="java.util.Locale" %>
<%@ page import="lk.rgd.common.api.domain.User" %>
<%@ page import="lk.rgd.AppConstants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    User user = (User) session.getAttribute("user_bean");
    String preLang = ((Locale) session.getAttribute(WebConstants.SESSION_USER_LANG)).getLanguage();
%>

<html>
<head>
    <title>E-Population System</title>

    <script type="text/javascript">
        if (navigator.appVersion.indexOf("Win")!=-1){
            document.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"/ecivil/css/windowsFont.css\"/>");
        }
        else if(navigator.appVersion.indexOf("X11")!=-1){
            document.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"/ecivil/css/ubuntuFont.css\"/>");
        }
    </script>
    <link rel="stylesheet" type="text/css" href='<s:url value="/css/layout.css"/>'/>
    <link rel="stylesheet" type="text/css" href='<s:url value="/css/webform.css"/> '/>
    <link rel="stylesheet" type="text/css" href='<s:url value="/css/style.css"/>'/>
    <link rel="stylesheet" type="text/css" media="print" href='<s:url value="/css/print.css"/>'/>

    <%--<style type="text/css">
        @font-face {
            font-family: Potha;
            src: url(POTHA0.eot);
        }

        @font-face {
            font-family: Bamini;
            src: url(eot/BAMINI0.eot);
        }

        @font-face {
            font-family: Latha;
            src: url(eot/LATHA0.eot);
        }
    </style>--%>

</head>
<script type="text/javascript">
    function initPage() {
    }

    // Display popup by using specified url
    function displayHelpPopup(url) {
        var w = 990;
        var h = 850;
        var left = (w / 2 - 300);
        var top = (h / 2 - 350);
        var features = "width=" + w + ",height=" + h + ",top=" + top + ",left=" + left;
        features += ",scrollbars=1,resizable=0,status=0,directories=no,menubar=0,toolbar=0";
        window.open(url, 'Help Window',features);
    }
</script>
<body onload="initPage()">
<div id="wrapper">
    <%-- Header  --%>

    <div id="layout-header">
        <tiles:insertAttribute name="header"/>
        <table>
            <caption/>
            <col width="600px"/>
            <col width="650px"/>
            <tbody>
            <tr>
                <td></td>
                <td align="right">
                    <s:label
                            value="%{#session.user_bean.userId+'@' + #session.user_bean.primaryLocation.enLocationName}"
                            cssStyle="color:#666666;"/>
                </td>
            </tr>
            </tbody>
        </table>
        <s:label value=""/>
    </div>
    <%-- Menu & Body--%>
    <div id="layout-body">
        <div id="body-sidebar">
            <tiles:insertAttribute name="menu"/>
            <br>
            <s:form action="/eprLogout.do" method="POST" name="eprLogout">
                <div class="form-submit">
                    <%
                        if (AppConstants.SINHALA.equals(preLang)) {
                    %>
                    <s:submit name="submit" value="ඉවත්වන්න" id="logout-button"></s:submit>
                    <%
                        }
                    %>
                    <%
                        if (AppConstants.TAMIL.equals(preLang)) {
                    %>
                    <s:submit name="submit" value=" நீக்குக" id="logout-button"></s:submit>
                    <%
                        }
                    %>
                    <%
                        if (AppConstants.ENGLISH.equals(preLang)) {
                    %>
                    <s:submit name="submit" value="logout" id="logout-button"></s:submit>
                    <%
                        }
                    %>

                </div>
            </s:form>
        </div>
        <div id="body-content">
            <div id="body-content-title">
                <div id="page-help">
                    <tiles:importAttribute name="help" scope="request"/>

                    <% String url = (String) request.getAttribute("help"); %>
                    <%if (url.length() > 0) {%>
                    <s:a href="#" title="%{getText('helpToolTip.label')}"><img
                            src="<s:url value='/images/help.png'/>" width="25" height="25"
                            border="none" onclick="javascript:return displayHelpPopup('<%= url%>')"/></s:a>
                    <% } %>
                </div>

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