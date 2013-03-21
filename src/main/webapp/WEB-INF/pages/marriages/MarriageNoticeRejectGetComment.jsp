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

        var out = checkActiveFieldsForSyntaxErrors('marriage-notice-reject-get-comment-form');
        if (out != "") {
             errMsg = errMsg + out;
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
            <td width="180px"><s:label name="serial" value="%{getText('serial.label')}"/></td>
            <td width="800px">
                <s:if test="%{noticeType.ordinal()!=2}">
                    <s:label value="%{marriage.serialOfMaleNotice}"/>
                </s:if>
                <s:else>
                    <s:label value="%{marriage.serialOfFemaleNotice}"/>
                </s:else>
        <tr>
            <td><s:label name="name" value="%{getText('name.label')}"/></td>
            <td>
                <s:if test="%{noticeType.ordinal()!=2}">
                    <s:label value="%{marriage.male.nameInOfficialLanguageMale}"/>
                </s:if>
                <s:else>
                    <s:label value="%{marriage.female.nameInOfficialLanguageMale}"/>
                </s:else>
            </td>
        </tr>
        <tr>
            <td><s:label name="received" value="%{getText('received.label')}"/></td>
            <td>
                <s:if test="%{noticeType.ordinal()!=2}">
                    <s:label value="%{marriage.dateOfMaleNotice}"/>
                </s:if>
                <s:else>
                    <s:label value="%{marriage.dateOfFemaleNotice}"/>
                </s:else>
            </td>
        </tr>
    </table>
    <br/>

    <s:actionerror cssStyle="color:red;font-size:10pt"/>

    <s:form id="marriage-notice-reject-get-comment-form" action="eprMarriageNoticeReject.do" method="post" onsubmit="javascript: return validate()">
        <fieldset>
            <legend><b><s:label value="%{getText('rejectLegend.label')}"/></b></legend>
            <s:hidden name="idUKey" value="%{#request.idUKey}"/>
            <s:hidden name="pageNo" value="%{#request.pageNo}"/>
            <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
            <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
            <s:hidden name="noticeType" value="%{#request.noticeType}"/>
            <s:hidden name="mrDivisionId" value="%{#request.mrDivisionId}"/>
            <s:hidden name="noticeSerialNo" value="%{#request.noticeSerialNo}"/>
            <s:hidden name="dsDivisionId" value="%{#request.mrDivisionId}"/>
            <s:hidden name="districtId" value="%{#request.districtId}"/>
            <s:hidden name="searchStartDate" value="%{#request.searchStartDate}"/>
            <s:hidden name="searchEndDate" value="%{#request.searchEndDate}"/>
            <s:hidden name="pinOrNic" value="%{#request.pinOrNic}"/>
            <table>
                <tr>
                    <td width="230px"><s:label name="comments" value="%{getText('comment.label')}"/></td>
                    <td width="500px"><s:textarea id="comments" name="comment" rows="4" cols="35"/></td>
                    <td>
                        <div class="form-submit">
                            <s:submit name="reject" value="%{getText('button.reject')}"/>
                        </div>
                    </td>
                </tr>
            </table>
        </fieldset>
    </s:form>
    <s:hidden id="error1" value="%{getText('error.comment.must.not.empty')}"/>
</div>
