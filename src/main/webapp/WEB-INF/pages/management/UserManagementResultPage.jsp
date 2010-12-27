<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage(){}
</script>
<style type="text/css">
#user-managment-outer{
    width:600px;
    background-color: #f0f8ff;
    border:2px #bddaf6 solid;
    text-align:center;
    margin-left:auto;
    margin-right:auto;
    margin-top:30px;
    padding:20px;
}

</style>
<div id="user-managment-outer">
    <table align="center">
        <s:if test="pageNo==2">
        <tr>
            <td style="color:red;">
                <s:actionmessage/><br>
                Please Enter another user name
            </td>
        </tr>
        </s:if>
        <s:else>
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
            <td>
                <s:label value="The newly created user still INACTIVE."/>
                <s:label value="To activate new user you should have to assign locations"/>
            </td>
        </tr>
        <tr>
            <td>
                <s:url id="addLocations" action="eprInitAssignedUserLocation.do">
                    <s:param name="userId" value="#request.userId"/>
                    <s:param name="newUser" value="true"/>
                </s:url>
                <s:a href="%{addLocations}"><s:label value="Click Here To Assign Locations"/></s:a>
            </td>
        </tr>
        </s:else>
        <tr>
            <td style="text-align:center;">
                <s:form action="eprInitUserCreation">
                    <div class="form-submit" align="center">
                        <s:submit value="Create User"/>
                    </div>
                </s:form>
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>

    </table>
</div>