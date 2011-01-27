<%-- @author Mahesha Kalpanie --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<style type="text/css">
    @media print {
        .form-submit {
            display: none;
        }
    }
</style>
<div id="scanned-marriage-cert-outer">
    <p class="form-submit">
        <button onclick="printPage();"><s:property value="getText('button.print')"/></button>
        <input type=button value="Back" onClick="history.go(-1)">
    </p>
    <p>
        <img alt="Scanned Marriage Certificate"
             src="${pageContext.request.contextPath}/crs/Image?idUKey=<s:property value='idUKey'/>" width="1000"/>
    </p>
</div>
