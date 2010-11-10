<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    function checkAll(field) {
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
<s:if test="(birthAlterationApprovalList.size() !=0)">
    <div id="alteration-approval-list-outer">
    <fieldset style="border:none">
    <s:form action="eprApproveAlteration.do" method="post" name="alterationApproval">
        <table class="alteration-approval-list-table" width="100%" cellpadding="0" cellspacing="0">
            <tr>
                <td style="width:15%;font-size:10pt;text-align:center;" >
                    Heading <br>
                    தலைப்பு <br>
                    ශීර්ෂය
                </td>
                <td style="width:35%;font-size:10pt; text-align:center;">
                    Existing Entry <br>
                    உள்ள பதிவுக் குறிப்பு <br>
                    තිබෙන සටහන
                </td>
                <td style="width:35%;font-size:10pt; text-align:center;">
                    Requested Entry <br>
                    வேண்டப்பட்ட பதிவுக் குறிப்பு <br>
                    ඉල්ලුම් කර ඇති සටහන
                </td>
                <td style="width:15%;font-size:10pt; text-align:center;">
                    Approval Status <br>
                    அனுமதி வழங்கல் <br>
                    අනුමත කිරීම
                </td>
            </tr>
            <s:iterator status="approvalStatus" value="birthAlterationApprovalList" id="approvalList">
                <tr>

                    <td style="padding-left:5px;font-size:8pt">
                        <s:property
                                value="%{getText(sectionOfAct+'.'+birthAlterationApprovalList[#approvalStatus.index][0]+'.label')}"/></td>
                    <td style="padding-left:25px;"><s:property
                            value="birthAlterationApprovalList[#approvalStatus.index][1]"/></td>
                    <td style="padding-left:25px;"><s:property
                            value="birthAlterationApprovalList[#approvalStatus.index][2]"/></td>
                    <td style="text-align:center;">
                        <s:checkbox name="index"
                                    value="%{#index}"
                                    fieldValue="%{birthAlterationApprovalList[#approvalStatus.index][0]}"
                                    id="%{birthAlterationApprovalList[#approvalStatus.index][0]}"/>

                    </td>
                </tr>
            </s:iterator>
        </table>
        </fieldset>
        </div>
        <s:hidden name="sectionOfAct"/>
        <s:hidden name="numberOfAppPending"/>
        <s:hidden name="idUKey"/>
        <div style="width:20%;float:left;margin-left:25px;margin-top:20px;">
            <s:label value="%{getText('select_all.label')}"/>
            <s:checkbox id="selectAll" name="selectAll" onClick="checkAll(document.alterationApproval.index)"/>
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

