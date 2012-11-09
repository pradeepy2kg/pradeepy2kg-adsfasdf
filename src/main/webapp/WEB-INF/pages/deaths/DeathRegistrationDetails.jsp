<%-- @author Duminda Dharmakeerthi 
     @author amith jayasekara
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
    function Ignorwornings() {
        if (!document.getElementById("ignoreWarningId").checked) {
            var errormsg = document.getElementById("error1").value;
            alert(errormsg);
            return false;
        }
    }

    function initPage() {
    }

</script>

<div id="death-registration-deatails-outer">
    <div id="death-registration-details-header">
        <s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
        <s:actionerror cssStyle="color:red;font-size:10pt"/>
    </div>

    <div id="death-registration-details-body">
        <s:if test="#request.warnings.size>0 && !ignoreWarning">
            <fieldset>
            <legend><b><s:label value="%{getText('death_approval_warning.label')}"/></b></legend>
            <table class="birth-declaration-approval-warning-table">
                <s:iterator value="#request.warnings">
                    <tr>
                        <td><s:property value="message"/></td>
                    </tr>
                </s:iterator>
            </table>
            <s:form action="eprDirectApproveIgnoringWornings" onsubmit="javascript:return Ignorwornings()">
                <table align="center" border="0">
                    <tr>
                        <s:hidden value="true" name="directPrint"/>
                        <s:hidden value="%{#request.idUKey}" name="idUKey"/>
                    </tr>
                    <tr>
                        <td><s:label value="%{getText('ignoreWorning.label')}"/></td>
                        <td><s:checkbox name="ignoreWarning" id="ignoreWarningId"/></td>
                        <td class="button" align="left">
                            <s:submit name="approve" value="%{getText('approve.label')}"/>
                        </td>
                    </tr>
                </table>
                <s:hidden id="error1" value="%{getText('ignor.worning.error')}"/>
                </fieldset>
            </s:form>
        </s:if>
    </div>
    <div id="death-registration-details-footer">
        <div class="form-submit">
            <s:if test="%{pageNo}==2">
                <s:url id="newDR" action="eprInitDeathDeclaration.do">
                    <s:param name="addNewMode" value="true"/>
                    <s:param name="oldIdUKey" value="#request.idUKey"/>
                </s:url>
                <s:a href="%{newDR}"><s:label value="%{getText('newDR.label')}"/></s:a>
            </s:if>
            <s:if test="(!(#session.user_bean.role.roleId.equals('DEO'))) && pageNo == 2">
                <s:url id="approveDR" action="eprDirectApproveDeath.do">
                    <s:param name="idUKey" value="idUKey"/>
                    <s:param name="currentStatus" value="currentStatus"/>
                </s:url>
                <s:if test="editMode!= true">
                    <s:a href="%{approveDR}"><s:label value="%{getText('approveDR.label')}"/></s:a>
                </s:if>
            </s:if>
            <s:if test="pageNo==3 || pageNo==4">
                <s:url id="printDC" action="eprDeathCertificate.do">
                    <s:param name="idUKey" value="#request.idUKey"/>
                    <s:param name="directPrint" value="true"/>
                </s:url>
                <s:a href="%{printDC}"><s:label value="%{getText('printDC.label')}"/></s:a>
            </s:if>
            <s:url id="home" action="eprDeathApprovalAndPrint.do"></s:url>
            <s:a href="%{home}"><s:label value="%{getText('goToMain_link.label')}"/></s:a>
        </div>
    </div>
</div>
