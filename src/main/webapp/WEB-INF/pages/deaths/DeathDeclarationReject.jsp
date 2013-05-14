<%--@author amith jayasekara--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script type="text/javascript">
    var errormsg = "";
    function validate() {
        var returnval = true;
        var comment = document.getElementById('comment');
        isEmpty(comment, document.getElementById("error").value, "commentField")

        var out = checkActiveFieldsForSyntaxErrors('death-declaration-reject-form');
        if(out != ""){
            errormsg = errormsg + out;
        }

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }

        errormsg = "";
        return returnval;
    }

    function initPage(){}

</script>
<div id="birth-declaration-reject-outer" class="birth-declaration-reject-outer">
    <table>
        <tr>
            <td width="180px"><s:label name="serial" value="%{getText('serialNumber.label')}"/></td>
            <td width="800px"><s:label value="%{#request.serialNumber}"/></td>
        <tr>

    </table>
    <br/>

    <s:actionerror cssStyle="color:red;font-size:10pt"/>

    <s:form id="death-declaration-reject-form" action="eprRejectDeath.do" method="post" onsubmit="javascript:return validate()">
        <fieldset>
            <legend><b><s:label value="%{getText('rejectLegend.label')}"/></b></legend>
            <s:hidden name="idUKey" value="%{#request.idUKey}"/>
            <s:hidden name="pageNo" value="%{#request.pageNo}"/>
            <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
            <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
            <s:hidden name="birthDistrictId" value="%{#request.birthDistrictId}"/>
            <s:hidden name="dsDivisionId" value="%{#request.dsDivisionId}"/>
            <s:hidden name="birthDivisionId" value="%{#request.birthDivisionId}"/>
            <s:hidden name="recordCounter" value="%{#request.recordCounter}"/>
            <s:hidden name="confirmationApprovalFlag" value="%{#request.confirmationApprovalFlag}"/>
            <table>
                <tr>
                    <td width="230px"><s:label name="comment" value="%{getText('comment.label')}"/></td>
                    <td width="500px"><s:textarea id="comment" name="comment" rows="4" cols="35"/></td>
                    <td>
                        <div class="form-submit">
                            <s:submit name="reject" value="%{getText('reject.label')}"/>
                        </div>
                    </td>
                </tr>
            </table>
        </fieldset>
    </s:form>
</div>
<s:hidden id="error" value="%{getText('cannot.empty')}"/>
<s:hidden id="commentField" value="%{getText('field.comment')}"/>
