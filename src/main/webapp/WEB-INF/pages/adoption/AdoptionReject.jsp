<%--
  Created by IntelliJ IDEA.
  User: widu
  Date: Sep 26, 2013
  Time: 11:26:49 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
    }
    var errorMsg = "";
    function validate() {
        var returnval = true;
        var comment = document.getElementById('comments').value;
        if (comment == "") {
            errorMsg = errorMsg + document.getElementById('commentEmpty').value;
        }

        var out = checkActiveFieldsForSyntaxErrors('birth-declaration-reject-form');
        if (out != "") {
            errorMsg = errorMsg + out;
        }

        if (errorMsg != "") {
            alert(errorMsg);
            returnval = false;
        }
        errorMsg = "";
        return returnval;
    }
</script>
<div id="birth-declaration-reject-outer" class="birth-declaration-reject-outer">

    <s:form id="birth-declaration-reject-form" action="eprRejectAdoption.do" method="post"
            onsubmit="javascript:return validate()">
        <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
            <legend><b><s:label value="%{getText('reject.label')}"/></b></legend>
            <table>
                <tr>
                    <td width="180px"><s:label name="registration" value="%{getText('registration.no.label')}"/></td>
                    <td width="180px"><s:label value="%{adoptionEntryNo}"/></td>
                <tr>
                <tr>
                    <td width="180px"><s:label value="%{getText('received.label')}"/></td>
                    <td width="180px"><s:label value="%{orderReceivedDate}"/></td>
                <tr>

            </table>
            <br/>

            <s:actionerror cssStyle="color:red;font-size:10pt"/>

            <s:hidden name="idUKey" value="%{idUKey}"/>
            <table>
                <tr>
                    <td width="230px"><s:label name="comment" value="%{getText('comments.label')}"/></td>
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
<s:hidden id="commentEmpty" value="%{getText('error.comment.not.available')}"/>