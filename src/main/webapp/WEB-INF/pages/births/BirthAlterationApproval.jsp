<%@ page import="lk.rgd.common.util.GenderUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="alteration-approval-list-outer">
<fieldset>
<s:form action="eprApproveAlteration.do">
    <table class="alteration-approval-list-table" width="100%" cellpadding="0" cellspacing="0">
        <tr>
            <td style="width:10%"></td>
            <td style="width:40%;font-size:12pt; text-align:center;"><s:label value="Birth Name"/></td>
            <td style="width:40%;font-size:12pt; text-align:center;"><s:label
                    value="%{getText('name.label')}"/></td>
            <td style="width:10%;font-size:12pt; text-align:center;"><s:label
                    value="%{getText('approve.label')}"/></td>
        </tr>
        <s:iterator status="approvalStatus" value="birthAlterationApprovalList" id="approvalList">
            <tr>
                    <%--<td><s:property value="birthChangeList[#approvalStatus.index]"/></td>--%>
                <td><s:property value="birthAlterationApprovalList[#approvalStatus.index][0]"/></td>
                <td><s:property value="birthAlterationApprovalList[#approvalStatus.index][1]"/></td>
                <td><s:property value="birthAlterationApprovalList[#approvalStatus.index][2]"/></td>
                <td align="center">
                    <s:checkbox name="index"
                                value="%{#index}"
                                fieldValue="%{birthAlterationApprovalList[#approvalStatus.index][0]}"/>
                </td>
            </tr>
        </s:iterator>
    </table>

    </fieldset>
    </div>
    <s:hidden name="sectionOfAct"/>
    <s:hidden name="idUKey"/>
    <div class="form-submit">
        <s:submit value="%{getText('submit.label')}" cssStyle="margin-top:10px;"/>
    </div>
</s:form>
<%--
<%

    out.print(GenderUtil.getGender(0, "si"));
%>--%>
