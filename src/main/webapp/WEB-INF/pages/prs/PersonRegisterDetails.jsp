<%--@author Chathuranga Withana --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
    function initPage() {
    }
</script>
<%--TODO uncomment following --%>

<s:actionmessage cssStyle="text-align:left;"/>

<s:url id="print" action="eprDirectPRSCertificate.do">
    <s:param name="personId" value="#request.personUKey"/>
</s:url>
<s:url id="newEntry" action="eprExistingPersonRegInit.do"/>
<s:url id="edit" action="eprEditPerson.do">
    <s:param name="personUKey" value="#request.personUKey"/>
</s:url>
<s:url id="approve" action="eprDirectApprovePerson.do">
    <s:param name="personUKey" value="#request.personUKey"/>
</s:url>

<div id="prsCertificate-page" class="form-submit" style="margin:15px 0 0 10px;">
    <s:if test="allowPrint">
        <s:a href="%{print}"><s:label value="%{getText('print_certificate.button')}"/></s:a>
    </s:if>
    <s:if test="allowApprove">
        <s:a href="%{approve}"><s:label value="%{getText('approve_link.label')}"/></s:a>
    </s:if>
    <s:if test="allowEdit">
        <s:a href="%{edit}"><s:label value="%{getText('label.edit')}"/></s:a>
    </s:if>
    <s:a href="%{newEntry}"><s:label value="%{getText('addNewBDF_link.label')}"/></s:a>
</div>