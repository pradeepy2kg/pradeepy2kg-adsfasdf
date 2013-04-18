<%--
  @author Duminda Dharmakeerthi
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    $(function() {
        $('#approve').attr('disabled', true);
    });

    function selectCheckbox() {
        var ok = document.getElementById('ignoreWarning');
        if (ok.checked) {
            $('#approve').attr('disabled', false);
        } else {
            $('#approve').attr('disabled', true);
        }
    }
</script>

<div id="adoption-registration-approval-warning-outer">
    <fieldset>
        <legend><b><s:label value="%{getText('warnings.label')}"/></b></legend>
        <table class="warning-table">
            <s:iterator value="#request.warnings">
                <tr>
                    <td><s:property value="message"/></td>
                </tr>
            </s:iterator>
        </table>
        <s:form action="eprAdoptionIgnoreWarning" name="adoptionRegistrationApprovalWarningForm">
            <s:hidden name="idUKey"/>
            <table align="center">
                <tr>
                    <td><s:label value="%{getText('ignoreWarning.label')}"/></td>
                    <td><s:checkbox name="ignoreWarning" id="ignoreWarning" onclick="javascript:selectCheckbox()"/></td>
                    <td class="form-submit"><s:submit id="approve" name="approve" value="%{getText('approve.label')}"/></td>
                </tr>
            </table>
        </s:form>
    </fieldset>
</div>