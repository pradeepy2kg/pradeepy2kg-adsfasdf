<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage() {
    }
</script>
<div id="birth-alteration-result-outer">
    <s:actionmessage/>
    <s:actionerror cssStyle="color:red;"/>
    <table align="center">
        <tr>
            <td></td>
            <td style="width:80%;">
                <div class="form-submit" align="center">
                    <s:form action="eprApproveBirthAlterationInit.do">
                        <s:hidden name="idUKey"/>
                        <s:hidden name="bdId"/>
                        <s:submit name="approve" value="%{getText('approve.label')}"/>
                    </s:form>
                </div>
                <div class="form-submit" align="center">
                    <s:form action="eprBirthAlterationInit.do">
                        <s:submit name="approve" value="%{getText('add.new.lable')}"/>
                    </s:form>
                </div>
            </td>
            <td></td>
        </tr>
    </table>
</div>