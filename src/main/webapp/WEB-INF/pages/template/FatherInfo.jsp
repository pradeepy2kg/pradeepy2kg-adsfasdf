<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<tr>
    <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
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
    <td colspan="2"><label>(13)උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of Birth</label></td>
    <td colspan="2"><s:textfield name="parent.fatherPlaceOfBirth"/></td>

</tr>
<tr>
    <td><label>(14)පියාගේ ජාතිය<br>இனம்<br> Father's Race</label></td>
    <td colspan="6" class="table_reg_cell_02"><s:select list="raceList" name="fatherRace" headerKey="0"
                                                        headerValue="%{getText('select_race.label')}"/></td>
</tr>
