<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="user-language">
    <s:form action="eprLanguage.do" method="POST" name="eprLanguage">
        <s:select list="{'English','Sinhala','Tamil'}" name="language"></s:select>
        <s:submit value="select"></s:submit>
    </s:form>
</div>