<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="eprChangePass.do" method="POST">
    <%--name="eprChangePass">--%>

    <s:actionerror cssStyle="color:red;font-size:10pt"/>

    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <legend><b><s:label value="%{getText('changes.lable')}"/></b></legend>

        <table width="100%" cellpadding="0" class="user-preference-table" style="margin-top:20px">
            <col width="100px">
            <col width="300px">
            <col width="300px">
            <col width="330px">
            <tbody>
            <tr>
                <td ></td>
                <td colspan="2">
                    <s:fielderror cssStyle="color:red;font-size:10pt"/>
                </td>
            </tr>

            <tr>
                <td height="40px"></td>
                <td>

                    <s:label value="%{getText('existingPassword.label')}"/>
                </td>
                <td>
                    <s:password name="existingPassword" cssStyle="width:300px"/>
                </td>
                <td>
                </td>
            </tr>
            <tr>
                <td height="40px"></td>
                <td>

                    <s:label value="%{getText('newPassword.label')}"/>
                </td>
                <td>
                    <s:password name="newPassword" cssStyle="width:300px"/>
                </td>
                <td>
                </td>
            </tr>
            <tr>
                <td height="40px"></td>
                <td>
                    <s:label value="%{getText('retypeNewPassword.label')}"/>
                </td>
                <td>
                    <s:password name="retypeNewPassword" cssStyle="width:300px"/>
                </td>
                <td>
                </td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td>
                    <div class="form-submit">
                        <s:submit action="eprBackChangePass" value="%{getText('cancel.button')}"/>
                        <s:submit value="%{getText('submit.label')}" cssStyle="float:right;"/>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <s:hidden name="changePass" value="0"/>
    </fieldset>
</s:form>
