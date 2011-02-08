<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage() {
    }
</script>
<style type="text/css">
    #user-managment-outer {
        width: 600px;
        background-color: #f0f8ff;
        border: 2px #bddaf6 solid;
        text-align: center;
        margin-left: auto;
        margin-right: auto;
        margin-top: 30px;
        padding: 20px;
    }

    form-submit {
        background-image: url("../images/long_button_2.png");
    }
</style>
<div id="user-managment-outer">
    <table align="center">
        <s:if test="pageNo==2">
            <tr>
                <td style="color:red;">
                    <s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
                    <s:label value="%{getText('assign.another.user')}"/>
                </td>
            </tr>
        </s:if>
        <s:else>
            <tr>
                <td style="text-align:center;">
                    <s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
                </td>
            </tr>
            <s:if test="pageNo==1 || changePassword">
                <tr>
                    <td style="text-align:center;">
                        <s:label value="%{getText('user.initial.password')}"/>
                        <s:label cssStyle="background-color:black;color:white;padding:5px;" name="randomPassword"/>
                    </td>
                </tr>
            </s:if>
            <tr>
                <td>
                    <s:if test="pageNo == 3">
                        <s:label value="%{getText('updated.user.inactive')}"/><br/>
                        <s:label value="%{getText('please.assign.locations')}"/>
                    </s:if>
                    <s:else>
                        <s:label value="%{getText('new.user.inactive')}"/><br/>
                        <s:label value="%{getText('to.activate.assign.locations')}"/>
                    </s:else>
                </td>
            </tr>
            <tr>
                <td>
                    <s:url id="addLocations" action="eprInitAssignedUserLocation.do">
                        <s:param name="userId" value="#request.userId"/>
                        <s:param name="newUser" value="true"/>
                    </s:url>
                    <s:a href="%{addLocations}"><s:label value="%{getText('click.here.to.assign.locations')}"/></s:a>
                </td>
            </tr>
        </s:else>
        <tr>
            <td style="text-align:center;">
                <s:form action="eprInitUserCreation">
                    <div class="form-submit" align="center">
                        <s:submit value="%{getText('CreatUser.title')}"/>
                    </div>
                </s:form>
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>

    </table>
</div>