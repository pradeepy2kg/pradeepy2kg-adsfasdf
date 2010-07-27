<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>




<div id="adoption-registration-form-outer">
    <s:form action="eprAdoptionRegistrationAction.do" name="" id="" method="POST">
    <table class="adoption-reg-form-01-header-table">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td width="450px"></td>
             <td align="center" style="font-size:12pt; width:130px"><img src="<s:url value="/images/official-logo.png"/>"
                                                                    alt=""/> </td>
            <td width="450px"></td>
        </tr>
        <tr>
            <td colspan="3" align="center">දරුකමට හදාගැනීමේ උසාවි නියෝගය (අංක 4 දරන ආකෘති පත්‍රය) <br/>
                Adoption Order Issued by Court
            </td>
        </tr>
        </tbody>
    </table>

    <table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
        <tr>
            <td width="330px">නියෝගය ලැබුණු දිනය <br/>
                Received Date
            </td>
            <td><sx:datetimepicker id="receivedDatePicker" name="child.dateOfBirth" displayFormat="yyyy-MM-dd"
                               onchange="javascript:splitDate('receivedDatePicker')"/></td>
        </tr>
        <tr>
            <td>අධිකරණය<br/>
                Court
            </td>
            <td><s:textfield name="" id="" /></td>
        </tr>
        <tr>
            <td>නියෝගය නිකුත් කල දිනය <br/>
                Issued Date
            </td>
            <td><sx:datetimepicker id="issueDatePicker" name="" displayFormat="yyyy-MM-dd"
                               onchange="javascript:splitDate('issueDatePicker')"/></td>
        </tr>
        <tr>
            <td>නියෝග අංකය<br/>
                Serial number
            </td>
             <td><s:textfield name="" id="" /></td>
        </tr>
        <tr>
            <td>විනිසුරු නම <br/>
                Name of the Judge
            </td>
             <td><s:textfield name="" id="" /></td>
        </tr>
    </table>
    <table style="width:1030px;height:50px; text-align:center;">
        <tr>
            <td>අයදුම් කරුගේ විස්තර   <br/>
Applicants Details</td>
        </tr>
    </table>

    <table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
  <caption></caption>
  <col width="330px"/>
  <col width="175px"/>
 <col width="175px"/>
  <col width="175px"/>
  <col width="175px"/>
  <tbody>
    <tr>
      <td>අයදුම් කරු <br/>
Applicant </td>
      <td>පියා   </br>
Father</td>
      <td></td>
      <td>මව    <br/>
Mother</td>
      <td></td>
    </tr>
    <tr>
      <td >නම  <br/>
Name of the Applicant</td>
      <td colspan="4"><textarea id="a" name=""></textarea></td>
    </tr>

    <tr>
      <td>ලිපිනය     <br/>
Address</td>
      <td colspan="4"><textarea id="b" name=""></textarea></td>
    </tr>
    <tr>
      <td >බිරිඳගේ නම <br/>
(අයදුම් කරු පියා නම්)
Name of Wife
(If applicant is father)</td>
     <td  colspan="4"><textarea id="c" name=""></textarea></td>
  </tbody>
</table>
   <s:hidden name="pageNo" value="1"/>
    <div class="adoption-form-submit">
     <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
    </s:form>
</div>