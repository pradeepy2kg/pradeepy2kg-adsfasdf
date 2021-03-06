<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    var errormsg = "";
    function validate() {
        var returnVal = true;

        var out = checkActiveFieldsForSyntaxErrors('password-change-form');
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

<style type="text/css">
    * {
        margin: 0 auto;
        padding: 0;
    }

    #space {
        width: 100%;
        margin: 0 auto;
        height: 20px;
    }

    #passwordTable {
        margin: 0 auto;
        width: 70%;
        height: 330px;
        background-color: #C6DEFF;
        border: 1px #87cefa solid;
    }

</style>
<div id="space"></div>
<div id="content">
    <s:form id="password-change-form" action="/preferences/eprChangePass.do" method="POST"
            onsubmit="javascript:return validate()">
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
                <s:actionerror cssStyle="color:red;font-size:10pt"/>
                <s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
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
            <s:hidden name="fromLogin" value="%{fromLogin}"/>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td style="padding-right:30px;">
                <div class="form-submit">
                    <s:submit value="%{getText('submit.label')}" cssStyle="float:right;width:120px"/>
                </div>
                </s:form>
                <s:form action="/preferences/eprBackChangePass.do" method="post">
                    <div class="form-submit">
                        <s:submit action="eprBackChangePass" value="%{getText('cancel.button')}"
                                  cssStyle="margin-right:40px;width:120px"/>
                    </div>
                </s:form>
            </td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        </tbody>
    </table>
    <s:hidden name="changePass" value="0"/>
</div>
