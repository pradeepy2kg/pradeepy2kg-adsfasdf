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
<script type="text/javascript">
    function initPage(){}
</script>
<div id="scanned-marriage-cert-outer">
    <s:if test="listPage">
        <s:url id="previous" action="eprBackMarriageRegisterSearch" namespace="../marriages"/>
    </s:if>
    <s:else>
        <s:url id="previous" action="eprMarriageRegistrationInit" namespace="../marriages"/>
    </s:else>

    <p class="form-submit">
        <button onclick="printPage();"><s:property value="getText('button.print')"/></button>
    </p>
    <s:form action="%{previous}" method="get">
        <s:if test="listPage">
            <s:hidden name="pageNo" value="%{#request.pageNo-1}"/>
            <s:hidden name="districtId" value="%{#request.districtId}"/>
            <s:hidden name="dsDivisionId" value="%{#request.dsDivisionId}"/>
            <s:hidden name="mrDivisionId" value="%{#request.mrDivisionId}"/>
            <s:hidden name="printStart" value="%{#request.printStart}"/>
        </s:if>
        <s:else>
            <s:hidden name="idUKey" value="%{#request.idUKey}"/>
        </s:else>

        <div class="form-submit" style="float:right;">
            <s:submit value="%{getText('previous.label')}"/>
        </div>
    </s:form>
    <p>
        <img alt="Scanned Marriage Certificate"
             src="${pageContext.request.contextPath}/crs/Image?idUKey=<s:property value='idUKey'/>" width="1000"/>
    </p>
</div>
