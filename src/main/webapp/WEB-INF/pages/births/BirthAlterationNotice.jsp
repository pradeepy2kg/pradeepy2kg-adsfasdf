<%--@author Tharanga Punchihewa--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">
    #birth-alteration-print-form-outer-page table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }

        td {
            font-size: 10pt;
        }
    }

    #birth-confirmation-print-form-outer-page .form-submit {
        margin: 5px 0 15px 0;
    }
</style>


<s:form action="" method="post">
    <s:if test="(birthAlterationApprovalList.size() !=0) || (birthAlterationApprovedList.size() !=0)">
        <div id="alteration-approval-list-outer">
            <table border="0" cellspacing="0" width="100%">
                <caption></caption>
                <col/>
                <col/>
                <col/>
                <col/>
                <tbody>
                <tr>
                    <td rowspan="8" width="200px" height="350px"></td>
                    <td colspan="2" width="600px" height="100px"
                        style="text-align:center;margin-left:auto;margin-right:auto;font-size:16pt">
                        <label>රාජ්‍ය සේවය පිණිසයි / அரச பணி
                            On State Service</label></td>
                    <td rowspan="8" width="200px"></td>
                </tr>
                <tr>
                    <td><s:label name="declarant.declarantFullName" cssStyle="width:600px;font-size:12pt;"
                                 cssClass="disable"
                                 disabled="true"/></td>
                </tr>
                <tr>
                    <td><s:label name="declarant.declarantAddress" cssStyle="width:600px;font-size:12pt;"
                                 cssClass="disable"
                                 disabled="true"/></td>
                </tr>

                </tbody>
            </table>


            <fieldset style="border:none">
                <table class="alteration-approval-list-table" width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="width:20%;font-size:8pt;"></td>
                        <td style="width:35%;font-size:12pt; text-align:center;"><s:label
                                value="%{getText('approval.table.row1.title')}"/></td>
                        <td style="width:35%;font-size:12pt; text-align:center;"><s:label
                                value="%{getText('approval.table.row2.title')}"/></td>
                        <td style="width:10%;font-size:12pt; text-align:center;"><s:label
                                value="%{getText('approve.label')}"/></td>
                    </tr>
                    <s:iterator status="approvalStatus" value="birthAlterationApprovalList" id="approvalList">
                        <tr>

                            <td style="padding-left:5px;">
                                <s:property
                                        value="%{getText('3.'+birthAlterationApprovalList[#approvalStatus.index][0]+'.label')}"/></td>
                            <td style="padding-left:25px;"><s:property
                                    value="birthAlterationApprovalList[#approvalStatus.index][1]"/></td>
                            <td style="padding-left:25px;"><s:property
                                    value="birthAlterationApprovalList[#approvalStatus.index][2]"/></td>
                            <td style="text-align:center;">
                                <s:label value="%{getText('rejected.lable')}"/>
                            </td>
                        </tr>
                    </s:iterator>
                    <s:iterator status="approvedStatus" value="birthAlterationApprovedList" id="approvedList">
                        <tr>
                            <td style="padding-left:5px;">
                                <s:property
                                        value="%{getText('3.'+birthAlterationApprovedList[#approvedStatus.index][0]+'.label')}"/></td>
                            <td style="padding-left:25px;"><s:property
                                    value="birthAlterationApprovedList[#approvedStatus.index][1]"/></td>
                            <td style="padding-left:25px;"><s:property
                                    value="birthAlterationApprovedList[#approvedStatus.index][2]"/></td>
                            <td style="text-align:center;">
                                <s:label value="%{getText('Approved.lable')}"/>
                            </td>
                        </tr>
                    </s:iterator>
                </table>
            </fieldset>
        </div>

    </s:if>
</s:form>