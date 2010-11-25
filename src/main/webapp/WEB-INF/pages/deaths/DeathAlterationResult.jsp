<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage() {
    }
</script>
<div id="death-alteration-result-outer">
    <s:url action="eprDeathAlterationSearchHome.do" id="addnew"></s:url>
    <s:actionmessage/>

    <div class="form-submit">
        <s:form method="post" action="eprApproveDeathAlterationsDirect.do">
            <s:submit name="approve" value="%{getText('lable.approve')}"/>
            <s:hidden name="deathAlterationId" value="%{deathAlteration.idUKey}"/>
        </s:form>
    </div>
</div>
