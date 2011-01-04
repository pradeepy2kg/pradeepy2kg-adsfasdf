<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:actionmessage cssStyle="color:black;"/>
<br>
<s:actionerror cssErrorStyle="color:red;"/>

<div class="form-submit">
    <s:form action="eprSelectNoticeType.do" method="post">
        <s:submit value="%{getText('button.addNew')}"/>
    </s:form>
</div>
<div class="form-submit">
    <s:form action="eprApproveMarriageNotice.do" method="post">
        <s:submit value="%{getText('button.approve')}"/>
        <s:hidden name="idUKey" value="%{#request.idUKey}"/>
        <s:hidden name="noticeType" value="%{#request.noticeType}"/>
        <s:hidden name="ignoreWarnings" value="false"/>
    </s:form>
</div>


