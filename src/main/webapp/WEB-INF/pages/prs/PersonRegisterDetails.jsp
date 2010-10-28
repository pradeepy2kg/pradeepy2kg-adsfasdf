<%--@author Chathuranga Withana --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
    function initPage() {
    }
</script>

<s:actionmessage cssStyle="text-align:left;color:red;"/>

<s:url id="print" action="eprPRSCertificate.do">
    <s:param name="personId" value="#request.personId"/>
</s:url>

<div id="prsCertificate-page" class="form-submit" style="margin:15px 0 0 10px; ">
    <s:a href="%{print}"><s:label value="%{getText('print_certificate.button')}"/></s:a>
</div>

