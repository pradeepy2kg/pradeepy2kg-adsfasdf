<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
    }
</script>

<style type="text/css">
    *{
        margin:0 auto;
        padding:0;
    }
    #space{
        width:100%;
        margin:0 auto;
        height:20px;
    }
    #passwordTable{
        margin:0 auto;
        width:70%;
        height:330px;
        background-color:#C6DEFF;
        border:1px #87cefa solid;
    }
</style>
<div id="space"></div>
<div id="content">
    <s:form action="/preferences/eprChangePass.do" method="POST">
        <%--name="eprChangePass">--%>
            <table id="passwordTable" width="100%" cellpadding="30" class="user-preference-table" cellspacing="30">
                <col width="50%">
                <col width="50%">
                <tbody>
                <tr>
                    <th colspan="2"><b><s:label value="%{getText('changePassword.label')}"/></b></th>
                </tr>
                <tr>
                    <td colspan="2">
                        <s:fielderror cssStyle="color:red;font-size:10pt"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <s:actionerror cssStyle="color:red;font-size:10pt"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        <s:label value="%{getText('existingPassword.label')}"/>
                    </td>
                    <td style="padding-right:30px;">
                        <s:password name="existingPassword" cssStyle="width:100%"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        <s:label value="%{getText('newPassword.label')}"/>
                    </td>
                    <td style="padding-right:30px;">
                        <s:password name="newPassword" cssStyle="width:100%"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        <s:label value="%{getText('retypeNewPassword.label')}"/>
                    </td>
                    <td style="padding-right:30px;">
                        <s:password name="retypeNewPassword" cssStyle="width:100%"/>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td style="padding-right:30px;">
                        <div class="form-submit">
                            <s:submit action="eprBackChangePass" value="%{getText('cancel.button')}" cssStyle="margin-right:40px;"/>
                            <s:submit value="%{getText('submit.label')}" cssStyle="float:right;"/>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>
                </tbody>
            </table>
            <s:hidden name="changePass" value="0"/>

    </s:form>
</div>
