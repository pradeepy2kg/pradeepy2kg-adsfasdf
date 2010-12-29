<%@ page import="lk.rgd.crs.api.domain.BDDivision" %>
<%@ page import="lk.rgd.common.api.domain.User" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage() {
    }
</script>
<%
    User user = (User) session.getAttribute("user_bean");
%>
<div id="birth-alteration-result-outer">
    <s:actionmessage/>
    <s:actionerror cssStyle="color:red;"/>
    <table align="center">

        <tr>
            <td>
            </td>
            <td style="width:80%;">
                <div class="form-submit" align="center">
                    <s:form action="eprBirthAlterationInit.do">
                        <s:submit name="approve" value="%{getText('add.new.lable')}"/>
                    </s:form>

                </div>
                <div class="form-submit" align="center">
                    <s:if test="status.ordinal()<2 & (#session.user_bean.role.roleId.equals('ARG') | #session.user_bean.role.roleId.equals('RG'))">
                        <%
                            BDDivision deathDivision = (BDDivision) pageContext.getAttribute("deathDivision");
                            int deathDSDivsion = deathDivision.getDsDivision().getDsDivisionUKey();
                            boolean approveRights = user.isAllowedAccessToBDDSDivision(deathDSDivsion);
                            if (approveRights) {
                        %>
                        <s:form action="eprApproveBirthAlterationInit.do">
                            <s:hidden name="idUKey" value="%{#request.idUKey}"/>
                            <s:submit name="approve" value="%{getText('approve.label')}"/>
                        </s:form>
                        <%}%>
                    </s:if>

                </div>
            </td>
            <td></td>
        </tr>
    </table>
</div>