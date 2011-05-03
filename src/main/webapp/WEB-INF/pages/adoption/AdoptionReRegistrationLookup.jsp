<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script>
    var errormsg = "";
    function validate() {
        var domObject;
        var returnval = true;
        domObject = document.getElementById("idUKey");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error2');
        } else {
            isNumeric(domObject.value, 'error1', 'error2');
        }

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

    function initPage() {
        var domObject = document.getElementById('idUKey');
        if (domObject.value.trim() == 0) {
            domObject.value = null;
        }
    }

</script>
<br/>
<br/>
<s:form action="eprAdoptionReRegistrationFindEntry" method="post" onsubmit="javascript:return validate()">
    <table align="center">
        <tr><s:actionerror cssStyle="color:red;"/></tr>
        <tr>
            <td>
                <s:label value="%{getText('adoption_order_serial.label')}"/>
            </td>
            <td><s:textfield name="idUKey" id="idUKey"/></td>
            <td class="button"><s:submit name="search"
                                         value="%{getText('adoption_re_register.label')}"
                                         cssStyle="margin-left:10px;"/></td>
        </tr>
    </table>
</s:form>
<s:hidden id="error1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error2" value="%{getText('adoption.re.registeration.idUKey')}"/>