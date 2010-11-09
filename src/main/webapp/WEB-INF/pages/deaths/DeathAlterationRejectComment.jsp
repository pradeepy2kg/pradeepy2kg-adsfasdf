<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    var erroMassage = "";
    function validate() {
        var comment = document.getElementById('comment').value;

        if (comment == "") {
            erroMassage = erroMassage + document.getElementById('coomentNull').value;
        }

        if (erroMassage != "") {
            alert(erroMassage)
            erroMassage=""
            return false;
        }
        else {
            return true;
        }
        erroMassage = "";
    }
</script>
<s:form method="post" action="eprDeathAlterationReject.do" onsubmit="javascript:return validate()">
    <table>
        <caption/>
        <col width="250px"/>
        <col/>
        <tbody>
        <tr>
            <td align="left"><s:label value="%{getText('label.alteration.serial.number')}"/></td>
            <td align="left">
                <s:property value="%{deathAlteration.alterationSerialNo}"/>
            </td>
        </tr>
        <tr>
            <td align="left"><s:label value="%{getText('label.death.certificate.number')}"/></td>
            <td align="left">
                <s:property value="%{deathAlteration.deathId}"/>
            </td>
        </tr>
        </tbody>
    </table>
    <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
        <legend align="right">
            <s:label value="%{getText('lagend.add.comment.here')}"/>
        </legend>
        <s:textarea name="rejectComment" id="comment" rows="5" cols="130%"/>
    </fieldset>
    <div id="" class="form-submit">
        <s:submit value="%{getText('label.add.comment')}"/>
    </div>
    <s:hidden name="pageNumber" value="1"/>
    <s:hidden name="deathAlterationId" value="%{deathAlteration.idUKey}"/>
    <s:hidden id="coomentNull" value="%{getText('err.comment.null')}"/>
</s:form>
