<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="adoption-registration-form-outer">
<s:form action="eprAdoptionAction.do" name="" id="" method="POST">
<table class="adoption-reg-form-header-table">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="450px"></td>
        <td align="center" style="font-size:12pt; width:130px"><img
                src="<s:url value="/images/official-logo.png"/>"
                alt=""/></td>
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
        <td style="text-align:right;"><sx:datetimepicker id="receivedDatePicker" name="adoption.orderReceivedDate"
                                                         displayFormat="yyyy-MM-dd"
                                                         onchange="javascript:splitDate('receivedDatePicker')"/></td>
    </tr>
    <tr>
        <td>අධිකරණය<br/>
            Court
        </td>
        <td><s:textfield name="adoption.court" id=""/></td>
    </tr>
    <tr>
        <td>නියෝගය නිකුත් කල දිනය <br/>
            Issued Date
        </td>
        <td style="text-align:right;"><sx:datetimepicker id="issueDatePicker" name="adoption.orderIssuedDate"
                                                         displayFormat="yyyy-MM-dd"
                                                         onchange="javascript:splitDate('issueDatePicker')"/></td>
    </tr>
    <tr>
        <td>නියෝග අංකය<br/>
            Serial number
        </td>
        <td><s:textfield name="adoption.courtOrderNumber" id=""/></td>
    </tr>
    <tr>
        <td>විනිසුරු නම <br/>
            Name of the Judge
        </td>
        <td><s:textfield name="adoption.judgeName" id=""/></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>අයදුම් කරුගේ විස්තර <br/>
            Applicants Details
        </td>
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
            Applicant
        </td>
        <td>පියා   </br>
            Father
        </td>
        <td></td>
        <td>මව <br/>
            Mother
        </td>
        <td></td>
    </tr>
    <tr>
        <td colspan="3">Applicant's PIN/NIC Number</td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td>If a foreigner</td>
        <td>Country</td>
        <td></td>
        <td>Pasport Number</td>
        <td></td>
    </tr>
    <tr>
        <td>නම <br/>
            Name of the Applicant
        </td>
        <td colspan="4"><textarea id="applicantName" name="adoption.applicantName"></textarea></td>
    </tr>

    <tr>
        <td>ලිපිනය <br/>
            Address
        </td>
        <td colspan="4"><textarea name="adoption.applicantAddress"></textarea></td>
    </tr>
    </tbody>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td> <br/>
            If applicant is father,Mother's information
        </td>
    </tr>
</table>
<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
        <tr>
        <td colspan="3">Applicant's PIN/NIC Number</td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td>If a foreigner</td>
        <td>Country</td>
        <td></td>
        <td>Pasport Number</td>
        <td></td>
    </tr>
    <tr>
        <td>නම <br/>
            Name of the Applicant
        </td>
        <td colspan="4"><textarea  name=""></textarea></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>ළමයාගේ විස්තර <br/>
            Child's Information
        </td>
    </tr>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col style="width:330px;"/>
    <col style="width:160px;"/>
    <col style="width:190px;"/>
    <col style="width:160px;"/>
    <col style="width:190px;"/>
    <tbody>
    <tr>
        <td>උපන් දිනය<br/>
            Date of birth
        </td>
        <td colspan="2" style="text-align:right;"><sx:datetimepicker id="bdayDatePicker" name="adoption.childBirthDate"
                                                                     displayFormat="yyyy-MM-dd"
                                                                     onchange="javascript:splitDate('bdayDatePicker')"/></td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br/>
            Gender
        </td>
        <td><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="" headerKey="0" headerValue="%{getText('select_gender.label')}"
                cssStyle="width:190px; margin-left:5px;"/></td>
    </tr>
    <tr>
        <td>වයස <br/>
            Age
        </td>
        <td>අවුරුදු <br/>
            Years
        </td>
        <td><s:textfield name="adoption.childAgeYears" id=""/></td>
        <td>මාස <br/>
            Months
        </td>
        <td><s:textfield name="adoption.childAgeMonths" id=""/></td>
    </tr>
    <tr>
        <td>දැනට පවතින නම <br/>
            (නමක් දී ඇති නම්) <br/>
            Existing Name <br/>
            (if already given)
        </td>
        <td colspan="4"><textarea name="adoption.childExistingName"></textarea></td>
    </tr>
    <tr>
        <td>ලබා දෙන නම <br/>
            New name given
        </td>
        <td colspan="4"><textarea name="adoption.childNewName"></textarea></td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td>උපත දැනටත් ලියාපදිංචි කර උප්පැන්න සහතිකයක් නිකුත් කර ඇතිනම් <br/>
            If the birth is already registered, and a Birth Certificate issued
        </td>
    </tr>
</table>


<table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
    <tr>
        <td width="70%">උප්පැන්න සහතිකයේ අනුක්‍රමික අංකය <br/>
            The serial number of the Birth Certificate
        </td>
        <td width="30%"><s:textfield name="adoption.birthCertificateSerial" id="" cssStyle="width:85%;"/></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>හෝ<br/>OR
        </td>
    </tr>
</table>
<table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col width="330px"/>
    <col width="700px"/>
    <tbody>
    <tr>
        <td colspan="2" style="text-align:center;">උපත ලියපදින්ච්චි කිරීමේ රිසීට් පතේ සටහන් <br/>
            Birth Registration acknowledgement slip
        </td>
    </tr>
    <tr>
        <td>දිස්ත්‍රික්කය <br/>
            District
        </td>
        <td><s:select id="birthDistrictId" name="adoption.birthDistrictId" list="districtList"
                      value="birthDistrictId" cssStyle="width:240px;"/></td>
    </tr>
    <tr>
        <td>ප්‍රාදේශීය ලේකම් කොට්ටාශය <br/>
            Divisional Secretariat
        </td>
        <td></td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ටාශය <br/>
            Registration Division
        </td>
        <td></td>
    </tr>
    <tr>
        <td>අනුක්‍රමික අංකය <br/>
            Serial Number
        </td>
        <td></td>
    </tr>
    </tbody>
</table>
<s:hidden name="pageNo" value="1"/>
<div class="adoption-form-submit">
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
</div>