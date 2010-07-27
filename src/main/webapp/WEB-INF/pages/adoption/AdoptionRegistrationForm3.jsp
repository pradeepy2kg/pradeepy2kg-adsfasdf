<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="adoption-registration-form-outer">
     <s:form action="eprAdoptionRegistrationAction.do" name="" id="" method="POST">
    <table style="width:1030px; text-align:center;border:none">
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
            <td><sx:datetimepicker id="issueDatePicker" name="" displayFormat="yyyy-MM-dd"
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
    <s:hidden name="pageNo" value="3"/>
    <div class="adoption-form-submit">
        <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
    </div>
    </s:form>
</div>