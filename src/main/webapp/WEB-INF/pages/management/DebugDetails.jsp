<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<s:if test="debug!=null">
    <s:textarea cols="120" rows="20" name="debug" value="%{debug}" cssStyle="text-transform:none;"/>
</s:if>
<s:if test="stackTrace!=null">
    <s:textarea cols="120" rows="20" name="stackTrace" value="%{stackTrace}" cssStyle="text-transform:none;"/>
</s:if>
<s:url id="back" action="eprInitEventsManagement.do">
    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
    <s:param name="pageNumber" value="%{#request.pageNumber}"/>
    <s:param name="recordCounter" value="#request.recordCounter"/>
</s:url>
<br>

<div class="form-submit" style="margin-top:15px;">
    <s:a href="%{back}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
<br>
