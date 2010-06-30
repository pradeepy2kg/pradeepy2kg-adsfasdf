<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <td><s:textfield name="parent.fatherPassportNo"/></td>
</tr>
<tr>
    <td><label>(11)සම්පුර්ණ නම<br>தந்தையின் முழு பெயர்<br>Full Name</label></td>
    <td colspan="6"><s:textarea name="parent.fatherFullName" id="fatherFullName"/></td>
</tr>
<tr>
    <td><label>(12)උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth</label></td>
    <td colspan="2"><sx:datetimepicker id="fatherDatePicker" name="parent.fatherDOB"
                                       displayFormat="yyyy-MM-dd"
                                       onmouseover="javascript:splitDate('fatherDatePicker')"/></td>
