<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="user-language">
    <s:form action="eprLanguage.do" method="POST" name="eprLanguage">
        <s:select list="#{'en_US':'English','si_LK':'Sinhala','ta_LK':'Tamil'}" name="language" value="%{#session.user_lang}"></s:select>
        <s:submit value="select"></s:submit>
    </s:form>
</div>