<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<table  class="table_reg_page_02" cellspacing="0" style="margin:-10px auto; border-top:none;">
<tr>
    <td width="200px"><label>(11)සම්පුර්ණ නම<br>தந்தையின் முழு பெயர்<br>Full Name</label></td>
    <td colspan="8">
        <s:textarea name="parent.fatherFullName" id="fatherFullName" cssStyle="width:98%;"/>
    </td>
</tr>
<tr>
    <td width="200px"><label>(12)උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth</label></td>
    <td colspan="2">
            <sx:datetimepicker id="fatherDatePicker" name="parent.fatherDOB" displayFormat="yyyy-MM-dd"
                               onmouseover="javascript:splitDate('fatherDatePicker')"/>
    </td>
    <td colspan="2"><label>(13)උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of Birth</label></td>
    <td colspan="2"><s:textfield name="parent.fatherPlaceOfBirth" cssStyle="width:95%;"/></td>
</tr>
</table>
