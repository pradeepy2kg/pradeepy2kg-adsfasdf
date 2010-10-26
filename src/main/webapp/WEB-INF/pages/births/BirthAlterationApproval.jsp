<%@ page import="lk.rgd.common.util.GenderUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    /*$(function() {
     $('#selectAll').click(function() {
     var element = document.getElementById("selectAll").checked;
     if (element) {
     var birthAlterationApprovalList = document.getElementsByName("birthAlterationApprovalList");
     *//* for(var i=0 ;i<birthAlterationApprovalList.length;i++){

     }*//*
     var ind= document.getElementsByName("index");
     13.checked=true;
     }
     })
     })*/
    function checkAll(field)
    {
        if (document.getElementById("selectAll").checked) {
            for (i = 0; i < field.length; i++) {
                field[i].checked = true;
            }
        }
        else {
            for (i = 0; i < field.length; i++)
                field[i].checked = false;
        }
    }
    function initPage() {
        /* var index = document.getElementsByName("indexCheck");
         index= index[0].value ;
         var counter = 0;
         for (var i = 0; i < index.length; i++) {
         if (index.charAt(i) == ",")
         counter++;
         }
         for (var i = 0; i < counter; i++) {
         var check=index.substring(0,index.indexOf(','));
         if (check!=0) {
         alert(check);
         document.getElementById(check).checked = true;
         }
         alert(index.length)
         index=index.substring(index.indexOf(',')+1,index.length);
         alert(index);
         }
         */
    }
</script>
<s:if test="(birthAlterationApprovalList.size() !=0) || (birthAlterationApprovedList.size() !=0)">
    <div id="alteration-approval-list-outer">
    <fieldset>
    <s:form action="eprApproveAlteration.do" method="post" name="alterationApproval">
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
                        <s:label value="%{birthAlterationApprovalList[#approvalStatus.index][0]}"/>
                        <s:property
                                value="%{getText(sectionOfAct+'.'+birthAlterationApprovalList[#approvalStatus.index][0]+'.label')}"/></td>
                    <td style="padding-left:25px;"><s:property
                            value="birthAlterationApprovalList[#approvalStatus.index][1]"/></td>
                    <td style="padding-left:25px;"><s:property
                            value="birthAlterationApprovalList[#approvalStatus.index][2]"/></td>
                    <td style="text-align:center;">
                        <s:if test="pageType==2">
                            rejected
                        </s:if>
                        <s:else>
                            <s:checkbox name="index"
                                        value="%{#index}"
                                        fieldValue="%{birthAlterationApprovalList[#approvalStatus.index][0]}"
                                        id="%{birthAlterationApprovalList[#approvalStatus.index][0]}"/>
                        </s:else>
                    </td>
                </tr>
            </s:iterator>
            <s:iterator status="approvedStatus" value="birthAlterationApprovedList" id="approvedList">
                <tr>
                    <td style="padding-left:5px;">
                        <s:label value="%{birthAlterationApprovedList[#approvedStatus.index][0]}"/>
                        <s:property
                                value="%{getText(sectionOfAct+'.'+birthAlterationApprovedList[#approvedStatus.index][0]+'.label')}"/></td>
                    <td style="padding-left:25px;"><s:property
                            value="birthAlterationApprovedList[#approvedStatus.index][1]"/></td>
                    <td style="padding-left:25px;"><s:property
                            value="birthAlterationApprovedList[#approvedStatus.index][2]"/></td>
                    <td style="text-align:center;">
                        Approved
                    </td>
                </tr>
            </s:iterator>
        </table>
        </fieldset>
        </div>
        <s:hidden name="sectionOfAct"/>
        <s:hidden name="numberOfAppPending"/>
        <s:hidden name="idUKey"/>
        <div style="width:20%;float:left;margin-left:25px;">
            <s:label value="%{getText('select_all.label')}"/>
            <s:checkbox id="selectAll" name="selectAll" onClick="checkAll(document.alterationApproval.index)"/>
        </div>
        <div style="width:20%;float:left;margin-left:25px;">
            <s:label value="%{getText('apply Changes')}"/>
            <s:checkbox id="" name="applyChanges"/>
        </div>
        <div class="form-submit">
            <s:submit value="%{getText('save.label')}" cssStyle="margin-top:10px;margin-right:25px"/>
        </div>
    </s:form>
</s:if>
<s:else>
    <table align="center" style="width:50%">
        <tr>
            <td style="width:60%">
                <s:label value="%{getText('no.changes.found')}"/>
            </td>
            <td>
                <s:form action="eprBirthAlterationPendingApproval.do" method="post">
                    <div class="form-submit">
                        <s:submit value="%{getText('previous.label')}" cssStyle="margin-top:10px;margin-right:25px"/>
                    </div>
                </s:form>
            </td>
        </tr>
    </table>
</s:else>
<s:hidden name="indexCheck"/>
<s:hidden name="birthAlterationApprovalList"/>
<%--
<%

    out.print(GenderUtil.getGender(0, "si"));
%>--%>
