<%-- @author Chathuranga Withana --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
        disableButton(true);
    }

    function disableButton(mode) {
        document.getElementById('approve').disabled = mode;
    }

    function selectCheckbox() {
        var ok = document.getElementById('ignoreWarnings');
        if (ok.checked) {
            disableButton(false)
        } else {
            disableButton(true)
        }
    }
</script>

<s:if test="listPage">
    <s:url id="approve" action="eprApproveMarriageRegistration" namespace="../marriages"/>
    <s:url id="previous" action="eprBackMarriageRegisterSearch" namespace="../marriages"/>
</s:if>
<s:else>
    <s:if test="register">
        <s:url id="approve" action="eprRegisterAndApproveNewMarriage" namespace="../marriages"/>
    </s:if>
    <s:else>
        <s:url id="approve" action="eprUpdateAndApproveMuslimMarriage" namespace="../marriages"/>
    </s:else>
    <s:url id="previous" action="eprMarriageRegistrationInit" namespace="../marriages"/>
</s:else>

<fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
<legend><b><s:label value="%{getText('message.marriageRegister.warning.label')}"/></b></legend>
<table class="birth-declaration-approval-warning-table" style="margin-left:10px;margin-top:10px;">
    <s:iterator value="#request.warnings">
        <tr>
            <td><s:property value="message"/></td>
        </tr>
    </s:iterator>
</table>
<s:form action="%{approve}" method="post">
    <table align="center" border="0">
        <tr>
            <s:hidden name="idUKey" value="%{#request.idUKey}"/>
        </tr>
        <tr>
            <td><br><s:label value="%{getText('ignoreWarning.label')}"/></td>
            <td><br><s:checkbox id="ignoreWarnings" name="ignoreWarnings" onclick="javascript:selectCheckbox()"/></td>
            <td></td>
        </tr>
    </table>
    </fieldset>

    <div class="form-submit" style="margin-right:400px;">
        <s:submit id="approve" name="approve" value="%{getText('approve_link.label')}"/>
    </div>
</s:form>
<s:form action="%{previous}" method="get">
    <s:hidden name="idUKey" value="%{#request.idUKey}"/>
    <div class="form-submit" style="float:right;">
        <s:submit value="%{getText('previous.label')}"/>
    </div>
</s:form>
