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
<s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
<div id="adoption-page">
    <s:url id="approveAdoption" action="eprAdoptionDirectApproval.do">
        <s:param name="idUKey" value="#request.idUKey"/>
    </s:url>

    <s:url id="printAdoptionNotice" action="eprAdoptionNoticeDirectPrint.do">
        <s:param name="idUKey" value="#request.idUKey"/>
        <s:param name="approved" value="#request.approved"/>
    </s:url>
    <s:url id="mainUrl" action="eprAdoptionRegistrationHome.do"/>

    <s:url id="rejectSelected" action="eprRejectAdoption.do">
        <s:param name="idUKey" value="#request.idUKey"/>
        <s:param name="reject" value="true"/>
    </s:url>

    <table align="center" border = "0">
        <tr>
            <td><s:a href="%{mainUrl}"><s:label value="%{getText('goToMain_link.label')}"/></s:a></td>
          <%--  <td><s:a href="%{rejectSelected}" title="%{getText('reject.label')}"><s:label value="%{getText('reject.label')}"/></s:a></td>--%>
        <s:if test="#request.approved">
            <td><s:a href="%{printAdoptionNotice}"><s:label value="%{getText('PrintAdoptionNotice.label')}"/></s:a> &nbsp;&nbsp;&nbsp;&nbsp;</td>
        </s:if>
        <s:elseif test="#request.allowApproveAdoption">
             <td><s:a href="%{approveAdoption}"><s:label value="%{getText('approve_link.label')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;  </td>
        </s:elseif>
        </tr>
    </table>
</div>
 