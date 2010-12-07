<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:actionmessage cssStyle="color:black;"/>
<br>
<s:actionerror cssErrorStyle="color:red;"/>
<s:if test="idUKey>0">
    <div class="form-submit">
        <s:form action="eprMarriageNoticeEditInit" method="post">

            <s:submit value="%{getText('button.edit')}"/>
            <s:hidden name="idUKey" value="%{#request.idUKey}"/>
            <s:hidden name="secondNotice" value="%{#request.secondNotice}"/>
        </s:form>
    </div>
    <div class="form-submit">
        <s:form action="" method="post">
            <s:submit value="%{getText('button.back')}"/>
        </s:form>
    </div>
</s:if>


