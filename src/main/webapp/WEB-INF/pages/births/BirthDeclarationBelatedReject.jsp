<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage(){}

    var errormsg = "";
    function validate(){
        var returnval = true;
        var out = checkActiveFieldsForSyntaxErrors('belated-birth-reject-form');
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
</script>
<div id="birth-declaration-reject-outer" class="birth-declaration-reject-outer">
    <table>
        <tr>
            <td width="180px"><s:label name="serial" value="%{getText('serial.label')}"/></td>
            <td width="800px"><s:label value=":    %{bdf.register.bdfSerialNo}"/></td>
        <tr>
            <td><s:label name="name" value="%{getText('name.label')}"/></td>
            <td><s:label value=": %{bdf.child.getChildFullNameOfficialLangToLength(50)}"/></td>
        </tr>
        <tr>
            <td><s:label name="received" value="%{getText('received.label')}"/></td>
            <td><s:if test="#request.confirmationApprovalFlag == true">
                <s:label value=": %{bdf.confirmant.confirmationProcessedTimestamp}"/>
            </s:if><s:else>
                <s:label value=": %{bdf.register.dateOfRegistration}"/></td>
            </s:else>
        </tr>
    </table>
    <br/>

    <s:actionerror cssStyle="color:red;font-size:10pt"/>

    <s:form id="belated-birth-reject-form" action="eprRejectBelatedBirthDeclaration.do" method="post" onsubmit="javascript:return validate()">
        <fieldset>
            <legend><b><s:label value="%{getText('rejectLegend.label')}"/></b></legend>
            <s:hidden name="bdId" value="%{#request.bdId}"/>
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
                    <td width="500px"><s:textarea id="comments" name="comments" rows="4" cols="35"/></td>
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
