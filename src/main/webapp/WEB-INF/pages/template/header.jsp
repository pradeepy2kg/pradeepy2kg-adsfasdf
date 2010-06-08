<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="user-language">
    <s:form action="eprLanguage.do" method="POST" name="eprLanguage">
        <s:select list="#@java.util.HashMap@{'en':'English','si':'සිංහල','ta':'Tamil'}" name="language"
                  value="%{#session.WW_TRANS_I18N_LOCALE.language}"></s:select>
        <s:submit value="select"></s:submit>
    </s:form>
</div>