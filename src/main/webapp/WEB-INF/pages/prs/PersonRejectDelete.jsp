<%-- @author Chathuranga Withana --%>
<%@ page import="lk.rgd.common.util.GenderUtil" %>
<%@ page import="java.util.Locale" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<style type="text/css ">
    .prev_button {
        background-image: url("../images/button_bg_hover_1.png");
        height: 25px;
        width: 112px;
        margin-top: 15px;
        float: right;
        padding-top: 5px;
        text-align: center;
    }

    .prev_button:hover {
        background-image: url("../images/button_bg_hover_2.png");
    }

    .prev_button label:hover {
        color: #8b0000;
    }
</style>

<script type="text/javascript" src="../js/validate.js"></script>

<script type="text/javascript">
    function initPage() {
    }

    var errormsg = "";
    function validate() {
        var domObject;
        var returnVal = true;

        // validate comments
        domObject = document.getElementById('comments');

        if (isFieldEmpty(domObject)) {
            errormsg = document.getElementById("error").value;
        }

        var out = checkActiveFieldsForSyntaxErrors('person-reject-details-form');
        if (out != "") {
            errormsg = errormsg + out;
        }

        if (errormsg != "") {
            alert(errormsg);
            returnVal = false;
        }
        errormsg = "";
        return returnVal;
    }
</script>
<div id="prsRejectDelete-message">
    <s:actionerror cssStyle="text-align:left;color:red"/>
</div>

<fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
    <legend><b><s:label value="%{getText('person.summary.label')}"/></b></legend>
    <table>
        <col width="180px;"/>
        <col width="10px;"/>
        <col width="840px;"/>
        <tr>
            <td><s:label value="%{getText('serial.label')}"/></td>
            <td>:</td>
            <td><s:label value="%{person.personUKey}"/></td>
        </tr>
        <tr>
            <td><s:label value="%{getText('pin.label')}"/></td>
            <td>:</td>
            <td>
                <s:if test="person.pin != null"><s:label value="%{person.pin}"/></s:if>
                <s:else>-</s:else>
            </td>
        </tr>
        <tr>
            <td>NIC</td>
            <td>:</td>
            <td>
                <s:if test="person.nic != null"><s:label value="%{person.nic}"/></s:if>
                <s:else>-</s:else>
            </td>
        </tr>
        <tr>
            <td><s:label value="%{getText('dob.label')}"/></td>
            <td>:</td>
            <td><s:label value="%{person.dateOfBirth}"/></td>
        </tr>
        <tr>
            <td><s:label value="%{getText('birthPlace.label')}"/></td>
            <td>:</td>
            <td><s:label value="%{person.placeOfBirth}"/></td>
        </tr>
        <tr>
            <td><s:label value="%{getText('gender.label')}"/></td>
            <td>:</td>
            <td>
                <%
                    Integer gender = (Integer) request.getAttribute("person.gender");
                    String lang = ((Locale) session.getAttribute("WW_TRANS_I18N_LOCALE")).getLanguage();
                %>
                <%= GenderUtil.getGender(gender, lang)%>
            </td>
        </tr>
        <tr>
            <td><s:label value="%{getText('race.label')}"/></td>
            <td>:</td>
            <td>
                <%
                    String race = "";
                    if (lang.equals("si")) {
                        race = (String) request.getAttribute("person.race.siRaceName");
                    } else if (lang.equals("en")) {
                        race = (String) request.getAttribute("person.race.enRaceName");
                    } else if (lang.equals("ta")) {
                        race = (String) request.getAttribute("person.race.taRaceName");
                    }
                %>
                <%= race%>
            </td>
        </tr>
        <tr>
            <td><s:label value="%{getText('name.official.label')}"/></td>
            <td>:</td>
            <td><s:label value="%{person.fullNameInOfficialLanguage}"/></td>
        </tr>
        <tr>
            <td><s:label value="%{getText('name.english.label')}"/></td>
            <td>:</td>
            <td><s:label value="%{person.fullNameInEnglishLanguage}"/></td>
        </tr>
        <tr>
            <td><s:label value="%{getText('permanent.address.label')}"/></td>
            <td>:</td>
            <td><s:label value="%{person.lastAddress.line1}"/></td>
        </tr>
    </table>
</fieldset>

<fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
    <legend><b>
        <s:if test="delete"><s:label value="%{getText('delete.selected.label')}"/></s:if>
        <s:else><s:label value="%{getText('reject.selected.label')}"/></s:else></b>
    </legend>
    <s:if test="delete"><s:url id="perform" action="eprDeletePerson.do" namespace="../prs"/></s:if>
    <s:else><s:url id="perform" action="eprRejectPerson.do" namespace="../prs"/></s:else>
    <s:url id="previous" action="eprBackPRSSearchList.do" namespace="../prs">
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="locationId" value="%{#request.locationId}"/>
        <s:param name="printStart" value="%{#request.printStart}"/>
    </s:url>
    <table>
        <s:form id="person-reject-details-form" action="%{perform}" method="post" onsubmit="javascript:return validate()">
            <col width="180px;"/>
            <col width="10px;"/>
            <col width="840px;"/>
            <tr>
                <td><s:label name="comment" value="%{getText('comment.label')}"/></td>
                <td>:</td>
                <td><s:textarea id="comments" name="comments" rows="4" cols="115"/></td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>
                    <div class="form-submit" style="float:right;">
                        <s:if test="delete">
                            <s:submit value="%{getText('deleteToolTip.label')}"/>
                        </s:if>
                        <s:else>
                            <s:submit value="%{getText('reject.label')}"/>
                        </s:else>
                    </div>
                    <div class="prev_button">
                        <s:a href="%{previous}" cssStyle="text-decoration:none;color:#333;">
                            <s:label value="%{getText('previous.label')}"/>
                        </s:a>
                    </div>
                </td>
            </tr>
            <s:hidden name="personUKey" value="%{person.personUKey}"/>

            <s:hidden name="pageNo" value="%{#request.pageNo}"/>
            <s:hidden name="locationId" value="%{#request.locationId}"/>
            <s:hidden name="printStart" value="%{#request.printStart}"/>
        </s:form>
    </table>
</fieldset>

<s:hidden id="error" value="%{getText('enter.comment.label')}"/>