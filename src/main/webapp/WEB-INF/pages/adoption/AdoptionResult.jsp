<%--
  @author Indunil Moremada
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage() {
    }
</script>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage cssStyle="color:black;font-size:10pt"/>
<div id="adoption-page">
    <s:url id="approveAdoption" action="eprAdoptionDirectApproval.do">
        <s:param name="idUKey" value="#request.idUKey"/>
    </s:url>

    <s:url id="printAdoptionNotice" action="eprAdoptionNoticeDirectPrint.do">
        <s:param name="idUKey" value="#request.idUKey"/>
        <s:param name="approved" value="#request.approved"/>
    </s:url>
    <s:url id="mainUrl" action="eprAdoptionRegistrationHome.do"/>
    <table align="center">
        <tr>
            <td><s:a href="%{mainUrl}"><s:label value="%{getText('goToMain_link.label')}"/></s:a></td>
            <td>
                <s:if test="#request.approved">
                    <s:a href="%{printAdoptionNotice}">
                        <s:label
                                value="%{getText('PrintAdoptionNotice.label')}"/></s:a> &nbsp;&nbsp;&nbsp;&nbsp;
                </s:if>
                <s:elseif test="#request.allowApproveAdoption">
                    <s:a href="%{approveAdoption}"><s:label value="%{getText('approve_link.label')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
                </s:elseif>
            </td>
        </tr>
    </table>
</div>
 