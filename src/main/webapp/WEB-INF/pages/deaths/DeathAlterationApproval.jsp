<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>

<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript">
    function initPage() {
    }

    function warning() {
        var ret = true;
        ret = confirm(document.getElementById('confirmation').value)
        return ret;
    }

</script>
<div id="birth-register-approval-body">
           <s:form action="eprDeathAlterationSetBits" method="post" onsubmit="javascript:return warning()">
          <table id="pendingApprovalTable" border="1" width="100%" class="table_reg_page_05" cellpadding="0"
                 cellspacing="0">
              <tr>
                  <td></td>
                  <td><s:label value="%{getText('th.exsists')}"/></td>
                  <td><s:label value="%{getText('th.alteration')}"/></td>
                  <td><s:label value="%{getText('th.approve')}"/></td>
              </tr>
              <tbody>
<%--                      <s:iterator value="approvalFieldList">
                      <tr>
                          <td align="left"><s:property value="%{getText('death.alteration.field.'+key)}"/></td>
                          <td><s:property value="%{value.get(0)}"/></td>
                          <td><s:property value="%{value.get(1)}"/></td>
                          <td align="center">
                              <s:checkbox name="approvedIndex" fieldValue="%{key}"/>
                          </td>
                      </tr>
                  </s:iterator>--%>
              <s:if test="%{requested.get()==true}">
                              <tr>
                  <td>person name</td>
                  <td><s:label value="%{deathRegister.deathPerson.deathPersonNameOfficialLang}"/></td>
                  <td><s:label value="%{deathAlteration.deathPerson.deathPersonNameOfficialLang}"/></td>
                  <td>test val</td>
              </tr>
              </s:if>


              <tr>
                  <td>person name english</td>
                  <td><s:label value="%{deathRegister.deathPerson.deathPersonNameInEnglish}"/></td>
                  <td><s:label value="%{deathAlteration.deathPerson.deathPersonNameInEnglish}"/></td>
                  <td>test val</td>
              </tr>

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
          </div>
      </s:form>

   <%-- <s:else>
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
        <s:label value="%{requested.size()}"/>
    </s:else>--%>
</div>
<s:hidden id="confirmation" value="%{getText('confirm.apply.changes')}"/>
