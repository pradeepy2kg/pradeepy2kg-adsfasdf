<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>

<div id="adoption-registration-form-outer">
<s:form action="eprAdoptionApprovalAndPrint.do" name="" id="" method="POST">
<table class="adoption-reg-form-header-table">
    <tr>
        <td align="center" style="font-size:12pt">
            <img src="<s:url value="/images/official-logo.png" />" alt=""><br>
            <label>
                රෙජිස්ට්‍රාර් ජෙනරාල් දෙපාර්තමේන්තුව <br/>
                Registrar General's Department
            </label>

        </td>
    </tr>
    <tr style="text-align:right;font-size:10pt">
        <td><s:label><%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>
        </s:label></td>
    </tr>
    <tr height="50px" style="text-align:left;font-size:10pt">
        <td><s:label value="%{adoption.applicantName}"/></td>
    </tr>
    <tr height="100px" style="text-align:left;font-size:10pt">
        <td><s:label value="%{adoption.applicantAddress}"/></td>
    </tr>
    <tr>
        <td>දරුකමට හදාගත් ළමයෙකුගේ උපත නෙවත ලියපදින්ච්චි කිරීම <br/>
            Re-registration of the birth of an Adopted Child
        </td>
    </tr>

</table>

<table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0">
    <tr>
        <td height="40px" width="330px">
            <label>
                ලියාපදිංචි කිරීමේ අනුක්‍රමික අංකය
                <br>Serial number of the registration
            </label>
        </td>
        <td>
            <s:label value="%{adoption.birthCertificateSerial}"/>
        </td>
    </tr>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td>දරුකමට හදගේනීමේ උසාවි නියෝගය <br/>
            Particulars of Adoption Order
        </td>
    </tr>
</table>

<table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0">
    <tr>
        <td width="330px" height="40px">අධිකරණය <br/>
            Court
        </td>
        <td><s:label name="" value="%{adoption.court}"/></td>
    </tr>
    <tr>
        <td height="40px">නියෝගය නිකුත් කල දිනය <br/>
            Issued Date
        </td>
        <td><s:label name="" value="%{adoption.orderIssuedDate}"/>
        </td>
    </tr>
    <tr>
        <td height="40px">නියෝග අංකය <br/>
            Serial number
        </td>
        <td><s:label name="" value="%{adoption.courtOrderNumber}"/>
    </tr>
    <tr>
        <td height="100px">විනිසුරු නම <br/>
            Name of the Judge
        </td>
        <td colspan="4"><s:label name="" value="%{adoption.judgeName}"/>
        </td>
    </tr>
</table>

<table>
    <tr>
        <td height="100px">
            Body of the letter
        </td>
    </tr>
    <tr>
        <td>
            අයදුම් පත්‍රය එවිය යුතු ලිපිනය <br>
            Address to post applicant<br><br>
            Register Generals Department,<br>
            Colombo.
        </td>
    </tr>
    <tr>
        <td height="100px">
            Trailer of the letter
        </td>
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

</table>
<table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0" style="float:right;width:500px;">
    <tr>
        <td height="40px" width="250px">
            <label>
                ලියාපදිංචි කිරීමේ අනුක්‍රමික අංකය
                <br>Serial number of the registration
            </label>
        </td>
        <td width="250px">
            <s:label value="%{adoption.birthCertificateSerial}"/>
        </td>
    </tr>
</table>

<table class="adoption-reg-form-header-table">
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
        <td height="40px">පියා <br/>
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
        <td rowspan="3" colspan="3" height="120px">නම <br/>
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
        <td colspan="3" rowspan="4" height="160px">ලිපිනය <br/>
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
        <td height="100px">නම <br/>
            Name
        </td>
        <td height="40px" colspan="3"><s:label name="" value="%{adoption.childExistingName}"/></td>
    </tr>

    <tr>
        <td>උපන් දිනය <br/>
            Date of birth
        </td>
        <td height="40px"><s:label name="" value="%{adoption.childBirthDate}"/></td>
        <td height="40px" width="250px">ස්ත්‍රී පුරුෂ භාවය <br/>
            Gender
        </td>
        <td width="250px"><s:label name="" value="%{adoption.childGender}"/></td>
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
    <col/>
    <tbody>
    <tr>
        <td height="40px">අධිකරණය <br/>
            Court
        </td>
        <td colspan="3"><s:label name="" value="%{adoption.court}"/></td>
    </tr>
    <tr>
        <td height="40px">නියෝගය නිකුත් කල දිනය <br/>
            Issued Date
        </td>
        <td><s:label name="" value="%{adoption.orderIssuedDate}"/></td>
        <td width="250px">නියෝග අංකය <br/>
            Serial number
        </td>
        <td width="250px"><s:label name="" value="%{adoption.courtOrderNumber}"/></td>
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
    <col/>
    <tbody>
    <tr>
        <td height="40px" width="330px">දිනය <br/>
            Date
        </td>
        <td></td>
        <td height="40px" width="250px">අයදුම්කරුගේ අත්සන
            Signature of Applicant
        </td>
        <td width="250px"></td>
    </tr>
    </tbody>
</table>
<div class="adoption-form-submit">
    <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
    <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
    <s:hidden name="status" value="%{#request.status}"/>
    <s:hidden name="pageNo" value="%{#request.pageNo}"/>
    <s:submit onclick="printPage();" value="%{getText('print.button')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
</div>