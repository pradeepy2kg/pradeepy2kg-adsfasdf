<%--@author Tharanga Punchihewa--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">
    #alteration-approval-list-outer table tr td {
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

    #alteration-approval-list-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>

<s:form action="" method="post">
    <s:if test="(birthAlterationApprovalList.size() !=0) || (birthAlterationApprovedList.size() !=0)">
        <div id="alteration-approval-list-outer">
            <s:if test="sectionOfAct != 2">
                <table border="0" cellspacing="0" width="100%" style="margin-top:0;">
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
                        <td><s:label cssStyle="width:600px;font-size:12pt;"
                                     cssClass="disable"
                                     disabled="true"/></td>
                    </tr>
                    <tr>
                        <td><s:label cssStyle="width:600px;font-size:12pt;"
                                     cssClass="disable"
                                     disabled="true"/></td>
                    </tr>

                    </tbody>
                </table>

                <hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:30px;">
                <table class="alteration-approval-list-table" width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="width:20%;font-size:8pt;"><s:label/></td>
                        <td style="width:35%;font-size:10pt; text-align:center;"><s:label
                                value="%{getText('approval.table.row1.title')}"/></td>
                        <td style="width:35%;font-size:10pt; text-align:center;"><s:label
                                value="%{getText('approval.table.row2.title')}"/></td>
                        <td style="width:10%;font-size:10pt; text-align:center;"><s:label/></td>
                    </tr>
                    <s:iterator status="approvedStatus" value="birthAlterationApprovedList" id="approvedList">
                        <tr>
                            <td style="padding-left:5px;">
                                <s:property
                                        value="%{getText(sectionOfAct+'.'+birthAlterationApprovedList[#approvedStatus.index][0]+'.label')}"/></td>
                            <td style="padding-left:15px;"><s:property
                                    value="birthAlterationApprovedList[#approvedStatus.index][1]"/><s:label/></td>
                            <td style="padding-left:15px;"><s:property
                                    value="birthAlterationApprovedList[#approvedStatus.index][2]"/><s:label/></td>
                            <td style="text-align:center;">
                                <s:label value="%{getText('Approved.lable')}"/>
                            </td>
                        </tr>
                    </s:iterator>
                    <s:iterator status="approvalStatus" value="birthAlterationApprovalList" id="approvalList">
                        <tr>

                            <td style="padding-left:5px;">
                                <s:property
                                        value="%{getText(sectionOfAct+'.'+birthAlterationApprovalList[#approvalStatus.index][0]+'.label')}"/></td>
                            <td style="padding-left:15px;"><s:property
                                    value="birthAlterationApprovalList[#approvalStatus.index][1]"/><s:label/></td>
                            <td style="padding-left:15px;"><s:property
                                    value="birthAlterationApprovalList[#approvalStatus.index][2]"/><s:label/></td>
                            <td style="text-align:center;">
                                <s:label value="%{getText('rejected.lable')}"/>
                            </td>
                        </tr>
                    </s:iterator>

                </table>
                <div style="page-break-after:always;margin-bottom:350px;"></div>
            </s:if>
                <%--Latter for declarant   --%>

            <table border="0" cellspacing="0" width="100%">
                <caption></caption>
                <col/>
                <col/>
                <col/>
                <col/>
                <tbody>
                <tr style="margin-top:300px;">
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

            <hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:30px;">
            <fieldset style="border:none">
                <table class="alteration-approval-list-table" width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="width:20%;font-size:8pt; text-align:center;"><s:label/></td>
                        <td style="width:35%;font-size:10pt; text-align:center;"><s:label
                                value="%{getText('approval.table.row1.title')}"/></td>
                        <td style="width:35%;font-size:10pt; text-align:center;"><s:label
                                value="%{getText('approval.table.row2.title')}"/></td>
                        <td style="width:10%;font-size:10pt; text-align:center;"><s:label/></td>
                    </tr>
                    <s:iterator status="approvedStatus" value="birthAlterationApprovedList" id="approvedList">
                        <tr>
                            <td style="padding-left:5px;font-size:8pt;">
                                <s:property
                                        value="%{getText(sectionOfAct+'.'+birthAlterationApprovedList[#approvedStatus.index][0]+'.label')}"/></td>
                            <td style="padding-left:15px;font-size:9pt;"><s:property
                                    value="birthAlterationApprovedList[#approvedStatus.index][1]"/><s:label/></td>
                            <td style="padding-left:15px;font-size:9pt;"><s:property
                                    value="birthAlterationApprovedList[#approvedStatus.index][2]"/><s:label/></td>
                            <td style="text-align:center;font-size:9pt;">
                                <s:label value="%{getText('Approved.lable')}"/>
                            </td>
                        </tr>
                    </s:iterator>
                    <s:iterator status="approvalStatus" value="birthAlterationApprovalList" id="approvalList">
                        <tr>

                            <td style="padding-left:5px;">
                                <s:property
                                        value="%{getText(sectionOfAct+'.'+birthAlterationApprovalList[#approvalStatus.index][0]+'.label')}"/></td>
                            <td style="padding-left:15px;"><s:property
                                    value="birthAlterationApprovalList[#approvalStatus.index][1]"/><s:label/></td>
                            <td style="padding-left:15px;"><s:property
                                    value="birthAlterationApprovalList[#approvalStatus.index][2]"/><s:label/></td>
                            <td style="text-align:center;">
                                <s:label value="%{getText('rejected.lable')}"/>
                            </td>
                        </tr>
                    </s:iterator>

                </table>
        </div>
    </s:if>
</s:form>
<div class="form-submit" style="margin-bottom:20px;margin-right:10px;">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    <s:hidden id="printMessage" value="%{getText('print.message')}"/>
</div>