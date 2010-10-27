<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>
<script type="text/javascript">
    function initPage() {
    }

    function warning() {
        var alterChanges = document.getElementById('applyChanges').checked;
       var ret=true;
        if (alterChanges) {
            ret = confirm(document.getElementById('confirmation').value)
        }
        return ret;
    }

</script>
<div id="birth-register-approval-body">
    <s:if test="!(pendingList.size()==0)">
        <s:form action="eprDeathAlterationSetBits" method="post" onsubmit="javascript:return warning()">
            <table id="pendingApprovalTable" border="1" width="100%" class="table_reg_page_05" cellpadding="0" cellspacing="0">
                <tr>
                    <td><s:label value="%{getText('th.index')}"/></td>
                    <td><s:label value="%{getText('th.exsists')}"/></td>
                    <td><s:label value="%{getText('th.alteration')}"/></td>
                    <td><s:label value="%{getText('th.approve')}"/></td>
                </tr>
                <tbody>
                <s:iterator value="pendingList">
                    <tr>
                        <td align="left"><s:property value="%{getText(key.get(1))}"/></td>
                        <td><s:property value="%{value.get(0)}"/></td>
                        <td><s:property value="%{value.get(1)}"/></td>
                        <td align="center">
                            <s:checkbox value="%{value.get(2)!=null}"  name="approvedIndex" fieldValue="%{key.get(0)}"/>
                        </td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
            <table>
                <caption/>
                <col>
                <col>
                <tbody>
                <tr>
                    <td width="1000px" align="right"><s:label value="%{getText('label.apply.changes')}"/></td>
                    <td align="right"><s:checkbox id="applyChanges" name="applyChanges"/></td>
                </tr>
                </tbody>
            </table>
            <div class="form-submit">
                <s:submit name="submit" value="%{getText('lable.update')}"/>
                <s:hidden name="deathAlterationId" value="%{deathAlteration.idUKey}"/>
                <s:hidden name="pendingListSize" value="%{pendingList.size()}"/>
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
                            <s:submit value="%{getText('previous.label')}"
                                      cssStyle="margin-top:10px;margin-right:25px"/>
                        </div>
                    </s:form>
                </td>
            </tr>
        </table>
    </s:else>
</div>
<s:hidden id="confirmation" value="%{getText('confirm.apply.changes')}"/>