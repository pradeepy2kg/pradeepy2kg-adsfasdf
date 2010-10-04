<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

<s:textarea cols="120" rows="20" name="debug" value="%{debug}" />
<s:url id="back" action="eprInitEventsManagement.do"/>
<br>
<div class="form-submit" style="margin-top:15px;">
    <s:a href="%{back}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
