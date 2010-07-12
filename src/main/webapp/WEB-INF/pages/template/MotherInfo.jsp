<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<table class="table_reg_page_02" cellspacing="0" style="margin:0; border-top:none;">
<tbody>
<tr>
    <td width="200px"><label>(16)සම්පුර්ණ නම<br>தந்தையின் முழு பெயர்<br>Full Name</label></td>
    <td colspan="8">
        <s:textarea name="parent.motherFullName" id="motherFullName" cssStyle="width:98%;"/>
    </td>
</tr>
<tr>
    <td width="200px"><label>(17)උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth</label></td>
    <td colspan="3"><sx:datetimepicker id="motherDatePicker" name="parent.motherDOB" displayFormat="yyyy-MM-dd"
                                       onmouseover="javascript:splitDate('motherDatePicker')"/></td>
    <td colspan="3" width="100px"><label>
        <s:if test="liveBirth">
            (18) ළමයාගේ උපන් දිනට මවගේ වයස<br> பிள்ளை பிறந்த திகதியில் மாதாவின் வயது<br>Mother's Age
            as at
            the date of birth of child
        </s:if>
        <s:else>
            (18) ළමයාගේ මළ උපන් දිනට මවගේ වයස<br> * Tamil<br>Mother's Age
            as at the date of still-birth of child
        </s:else>
    </label>
    </td>
    <td class="passport"><s:textfield name="parent.motherAgeAtBirth" id="motherAgeAtBirth"
                                      onclick="javascript:motherage('%{child.dateOfBirth}')"/></td>
</tr>
</tbody>
</table>

