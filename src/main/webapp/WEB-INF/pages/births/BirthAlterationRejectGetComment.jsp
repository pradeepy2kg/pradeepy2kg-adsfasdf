<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    var errMsg = "";
    function initPage() {
    }
    function validate() {
        var comment = document.getElementById('comments').value;
        if (comment == "") {
            errMsg = errMsg + document.getElementById('error1').value;
        }
        if (errMsg != "") {
            alert(errMsg)
            errMsg="";
            return false;
        }
        return true;
    }
</script>
<div id="birth-declaration-reject-outer" class="birth-declaration-reject-outer">
    <table>
        <tr>
            <td width="180px"><s:label name="serial" value="%{getText('original.bd.certificate.number.label')}"/></td>
            <td width="800px">
                <s:label value="%{birthAlteration.bdfIdUKey}"/>
        <tr>
            <td><s:label name="name" value="%{getText('name.label')}"/></td>
            <td>
                <s:label value="%{originalName}"/>
            </td>
        </tr>
        <tr>
            <td><s:label name="received" value="%{getText('received.label')}"/></td>
            <td>
                <s:label value="%{birthAlteration.dateReceived}"/>
            </td>
        </tr>
    </table>
    <br/>

    <s:actionerror cssStyle="color:red;font-size:10pt"/>

    <s:form id="birth-alteration-reject-form" action="eprRejectBirthAlteration.do" method="post" onsubmit="javascript: return validate()">
        <fieldset>
            <legend><b><s:label value="%{getText('rejectLegend.label')}"/></b></legend>
            <s:hidden name="idUKey" value="%{#request.idUKey}"/>
            <s:hidden name="pageNo" value="%{#request.pageNo}"/>
            <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
            <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
            <table>
                <tr>
                    <td width="230px"><s:label name="comments" value="%{getText('comment.label')}"/></td>
                    <td width="500px"><s:textarea id="comments" name="comment" rows="4" cols="35"/></td>
                    <td>
                        <div class="form-submit">
                            <s:submit onclick="getActiveTextFields('birth-alteration-reject-form')" name="reject" value="%{getText('button.reject')}"/>
                        </div>
                    </td>
                </tr>
            </table>
        </fieldset>
    </s:form>
    <s:hidden id="error1" value="%{getText('error.comment.must.not.empty')}"/>
</div>
