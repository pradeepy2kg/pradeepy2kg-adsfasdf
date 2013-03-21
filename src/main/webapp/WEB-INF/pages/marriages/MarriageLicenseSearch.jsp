<%-- @author Mahesha Kalpanie --%>
<%@ page import="lk.rgd.common.util.NameFormatUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/marriageregistervalidation.js"/>"></script>
<script type="text/javascript">
    function validateMarriageIdUkey() {
        var errormsg = "";
        errormsg = validateIdUkey("marriageIdUKey", "errorEmptyMarriageIdUKey", "errorInvalidMarriageIdUKey", errormsg);

        var out = checkActiveFieldsForSyntaxErrors('searchByIdUKey');
        if (out != "") {
            errormsg = errormsg + out;
        }

        return printErrorMessages(errormsg);
    }
</script>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage cssStyle="color:blue;font-size:10pt"/>
<s:form action="eprMarriageLicenseSearch.do" id="searchByIdUKey" method="POST"
        onsubmit="javascript:return validateMarriageIdUkey()">
    <table>
        <caption/>
        <col width="280px"/>
        <col width="10px"/>
        <col/>
        <tbody>
        <tr>
            <td>
                <s:label value="%{getText('label.marriageregister.number')}"/>
            </td>
            <td>
                <s:textfield id="marriageIdUKey" name="marriageIdUKey" cssStyle="width:232px;"
                             maxLength="10" value=""/>
            </td>
            <td>
                <div class="form-submit">
                    <s:submit value="%{getText('bdfSearch.button')}"/>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</s:form>
<s:hidden id="errorEmptyMarriageIdUKey"
          value="%{getText('label.marriageregister.number') + getText('message.cannotbeempty')}"/>
<s:hidden id="errorInvalidMarriageIdUKey"
          value="%{getText('error.invalid') + getText('label.marriageregister.number')}"/>


