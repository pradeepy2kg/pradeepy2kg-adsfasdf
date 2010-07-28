<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="adoption-registration-form-outer">
     <s:form action="eprAdoptionRegistrationAction.do" name="" id="" method="POST">
    <table class="adoption-reg-form-header-table">
        <tr>
            <td>දරුකමට හදාගත් ළමයෙකුගේ උපත නෙවත ලියපදින්ච්චි කිරීම <br/>
                Re-registration of the birth of an Adopted Child
            </td>
        </tr>
        <tr>
            <td style="text-align:left;">දරුකමට හදගේනීමේ උසාවි නියෝගය <br/>
                Particulars of Adoption Order
            </td>
        </tr>
    </table>
        <table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0">
        <tr>
            <td width="330px">අධිකරණය   <br/>
Court</td>
            <td><s:textfield name="" id=""/></td>
        </tr>
        <tr>
            <td>නියෝගය නිකුත් කල දිනය     <br/>
Issued Date</td>
            <td style="text-align:right;"><sx:datetimepicker id="issueDatePicker" name="" displayFormat="yyyy-MM-dd"
                               onchange="javascript:splitDate('issueDatePicker')"/></td>
        </tr>
        <tr>
            <td>නියෝග අංකය     <br/>
Serial number</td>
             <td><s:textfield name="" id="" /></td>
        </tr>
        <tr>
            <td>විනිසුරු නම  <br/>
Name of the Judge
</td>
            <td colspan="4"><textarea id="a" name=""></textarea></td>
        </tr>
    </table>

          <table class="adoption-reg-form-header-table">
        <tr>
            <td>රෙජිස්ට්‍රාර් ජෙනරාල් දෙපාර්තමේන්තුව <br/>
                Registrar General's Department
            </td>
        </tr>
        <tr>
            <td>දරුකමට ගැනීම පිලිබඳ ලේඛනයේ සහතික පිටපත් ලබා ගැනීම සඳහා අයදුම්පත <br/>
                Application to obtain a certified copy of the Certificate of Adoption
            </td>
        </tr>
        <tr>
            <td>අයදුම් කරුගේ විස්තර <br/>
                Applicants Details
            </td>
        </tr>
    </table>


    <table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
        <caption></caption>
        <col width="110px"/>
        <col width="110px"/>
        <col width="110px"/>
        <col width="110px"/>
        <col width="310px"/>
        <col width="310px"/>
        <tbody>
        <tr>
            <td>පියා <br/>
                Father
            </td>
            <td></td>
            <td>මව <br/>
                Mother
            </td>
            <td></td>
            <td>වෙනත් (කවුරුන්දැයි සටහන් කරන්න) <br/>
                Other (Specify whom)
            </td>
            <td></td>
        </tr>
        <tr>
            <td rowspan="4" colspan="3" height="120px">නම <br/>
                Name of the Applicant
            </td>
            <td colspan="3"></td>
        </tr>
        <tr>
            <td colspan="3"></td>
        </tr>
        <tr>
            <td colspan="3"></td>
        </tr>
        <tr>
            <td colspan="3"></td>
        </tr>
        <tr>
            <td colspan="3" rowspan="3" height="90px">ලිපිනය <br/>
                Address
            </td>
            <td colspan="3"></td>
        </tr>
        <tr>
            <td colspan="3"></td>
        </tr>
        <tr>
            <td colspan="3"></td>
        </tr>
        </tbody>
    </table>

    <table class="adoption-reg-form-header-table">
        <tr>
            <td>ළමයාගේ විස්තර <br/>
                Child's Information
            </td>
        </tr>
    </table>


     <table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
        <caption></caption>
        <col width="327px"/>
        <col/>
        <tbody>
        <tr>
            <td rowspan="5" height="150px">නම    <br/>
Name</td>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td>උපන් දිනය         <br/>
Date of birth</td>
            <td></td>
        </tr>
        <tr>
            <td>ස්ත්‍රී  පුරුෂ භාවය                 <br/>
Gender</td>
            <td></td>
        </tr>
        </tbody>
    </table>

       <table class="adoption-reg-form-header-table">
        <tr>
            <td>අධිකරණ නියෝගය පිලිබඳ විස්තර<br/>
                Information about the Adoption Order
            </td>
        </tr>
    </table>

   <table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
  <caption></caption>
  <col width="327"/>
  <col />
  <tbody>
    <tr>
      <td>අධිකරණය   <br/>
Court</td>
      <td></td>
    </tr>
    <tr>
      <td>නියෝගය නිකුත් කල දිනය  <br/>
Issued Date</td>
      <td></td>
    </tr>
    <tr>
      <td>නියෝග අංකය    <br/>
Serial number</td>
      <td></td>
    </tr>
  </tbody>
</table>



       <table style="width:1030px; text-align:left;border:none; margin-top:15px;margin-bottom:15px;">
        <tr>
            <td>මුද්දර ගාස්තු (එක පිටපතක් සඳහා රු. 25/- වටිනා මුද්දර අලවන්න)
            </td>
        </tr>
    </table>

   <table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
  <caption></caption>
  <col width="327"/>
  <col />
  <tbody>
    <tr>
      <td>දිනය         <br/>
Date</td>
      <td></td>
    </tr>
    <tr>
      <td>අයදුම්කරුගේ අත්සන
Signature of Applicant</td>
      <td></td>
    </tr>
  </tbody>
</table>
    <s:hidden name="pageNo" value="3"/>
    <div class="adoption-form-submit">
        <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
    </div>
    </s:form>
</div>