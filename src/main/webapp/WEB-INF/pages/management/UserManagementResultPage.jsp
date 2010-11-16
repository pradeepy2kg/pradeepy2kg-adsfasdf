<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage(){}
</script>

<div id="user-managment-outer">
    <table style="width:50%;" align="center">
        <tr>
            <td style="text-align:center;">
                <s:actionmessage/>
            </td>
        </tr>
        <s:if test="pageNo==1 || changePassword">
            <tr>
                <td style="text-align:center;">
                    <s:label value="User Initial Password :"/>
                    <s:label name="randomPassword"/>
                </td>
            </tr>
        </s:if>
        <tr>
            <td style="text-align:center;">
                <s:form action="eprInitUserCreation">
                    <div class="form-submit" align="center">
                        <s:submit value="Create User"/>
                    </div>
                </s:form>
            </td>
        </tr>
    </table>
</div>