<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css">
    #adoption-certificate-form-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }

        td {
            font-size: 12pt;
        }
    }

    #adoption-certificate-form-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>
<script type="tex/javascript" src="<s:url value="/js/print.js"/>"></script>
<s:url id="print" action="eprMarkAdoptionCertificateAsPrinted.do">
    <s:param name="alreadyPrinted" value="%{#request.alreadyPrinted}"/>
    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
    <s:param name="pageNo" value="%{#request.pageNo}"/>
    <s:param name="currentStatus" value="%{#request.currentStatus}"/>
    <s:param name="idUKey" value="%{#request.idUKey}"/>
</s:url>
<s:url id="cancel" action="eprAdoptionBackToPreviousState.do">
    <s:param name="nextFlag" value="%{#request.nextFlag}"/>
    <s:param name="previousFlag" value="%{#request.previousFlag}"/>
    <s:param name="currentStatus" value="%{#request.currentStatus}"/>
    <s:param name="pageNo" value="%{#request.pageNo}"/>
</s:url>
<div id="adoption-certificate-form-outer">

<%--<div class="form-submit" style="margin:15px 0 0 10px;>
 <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    <s:a href="%{cancel}"><s:label value="%{getText('cancel.button')}"/></s:a>
    <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
</div>--%>

<div id="adoption-page" class="form-submit" style="margin:15px 0 0 10px; ">
    <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
</div>
<div id="adoption-page" class="form-submit" style="margin:15px 0 0 10px; ">
    <s:a href="%{cancel}"><s:label value="%{getText('cancel.button')}"/></s:a>
</div>
<div id="adoption-page" class="form-submit" style="margin:15px 0 0 10px; ">
    <s:a href="#"><s:label value="%{getText('print.button')}" onclick="printPage()"/></s:a>
</div>

<table style=" width:
100%; border:none; border-collapse:collapse; ">
    <col width="200px">
    <col width="400px">
    <col width="200px">
    <tbody>
    <tr>
        <td rowspan="3"></td>
        <td rowspan="2" align="center">
            <img src="<s:url value="../images/official-logo.png" />"
                 style="display: block; text-align: center;" width="80" height="100">
        </td>
        <td>සහතික පත්‍රයේ අංකය <br>சான்றிதழ் இல <br>Certificate Number
        </td>
    </tr>
    <tr>
        <td><s:label value="%{adoption.idUKey}"/></td>
    </tr>
    <tr>
        <td align="center" style="font-size:16pt">
            ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA <br>
            දරුකමට ගැනීම පිලිබඳ සහතිකය <br>
            பிறப்பு சான்றிதழ்﻿ <br>
            CERTIFICATE OF ADOPTION
        </td>
        <td></td>
    </tr>
    </tbody>
</table>

<table border="1" width="100%" style="margin-top:10px;float:left;border:1px solid #000; border-collapse:collapse;">
    <col width="150px">
    <col width="160px">
    <col width="100px">
    <col width="130px">
    <col width="100px">
    <col>
    <tbody>
    <tr>
        <td width="250px" height="80px" rowspan="2">
            දරුකමට හදාගැනීමේ උසාවි නියෝගය <br>
            in tamil <br>
            Adoption Order Issued by Court
        </td>
        <td width="250px">නිකුත් කල අධිකරණය <br>
            in tamil <br>
            Court Issuing Order
        </td>
        <td colspan="5">
            <s:label name="courtName" cssStyle="margin-left:10px;"/>
        </td>
    </tr>
    <tr>

        <td width="150px">
            නියෝග දිනය <br>
            in tamil <br>
            Order Date
        </td>
        <td width="250px" colspan="2">
            <s:label value="%{adoption.orderIssuedDate}"/>
        </td>

        <td>
            නියෝග අංකය <br>
            in tamil <br>
            Order Number
        </td>
        <td width="150px">
            <s:label value="%{adoption.courtOrderNumber}"/>
        </td>
    </tr>
    <tr>
        <td height="80px" width="250px">පුද්ගල අනන්‍යතා අංකය <br>தனிநபர்அடையாள எண் <br>Person Identification Number
            (PIN)
        </td>
        <td width="250px"><s:label name="" value="%{adoption.childPIN}"/></td>
        <td>උපන් දිනය <br>பிறந்த திகதி <br>Date of birth <br>YYYY-MM-DD
        </td>
        <td><s:label name="" value="%{adoption.childBirthDate}"/></td>
        <td width="150px">ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender
        </td>
        <td width="150px">
            <s:label name="" value="%{genderSi}"/> <br>
            <s:label name="" value="%{genderEn}"/>
        </td>
    </tr>


    <tr>
        <td height="120px" width="250px">නම <br>பெயர் <br>Name
        </td>
        <td colspan="6" class="bc-name" style="font-size:12pt">
            <s:if test="#request.adoption.childNewName != null">
                <s:label name="" value="%{adoption.childNewName}"/>
            </s:if>
            <s:else>
                <s:label name="" value="%{adoption.childExistingName}"/>
            </s:else>
        </td>
    </tr>

    <tr>
        <td height="120px" width="250px">පියාගේ සම්පුර්ණ නම<br>தந்தையின்முழுப் பெயர் <br> Father's Full Name
        </td>
        <td colspan="6" class="bc-name" style="font-size:12pt">
            <s:if test="#request.adoption.applicantMother==0">
                <s:label name="" value="%{adoption.applicantName}"/>
            </s:if>
        </td>
    </tr>
    <tr>
        <td height="70px" width="250px">පියාගේ අනන්‍යතා අංකය / ජාතික
            හැඳුනුම්පත් අංකය <br>
            தந்தையின் அடையாள எண் <br>
            Father's PIN / NIC
        </td>
        <td colspan="5" width="250px">
            <s:if test="#request.adoption.applicantMother==0">
                <s:label name="" value="%{adoption.applicantPINorNIC}"/>
            </s:if></td>
    </tr>
    <tr>
        <td height="120px" width="250px"> මවගේ සම්පූර්ණ නම <br>
            தாயின் முழுப் பெயர் <br>
            Mother's Full Name
        </td>
        <td colspan="6" class="bc-name" style="font-size:12pt">
            <s:if test="#request.adoption.applicantMother==1">
                <s:label name="" value="%{adoption.applicantName}"/>
            </s:if>
            <s:else>
                <s:label name="" value="%{adoption.wifeName}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td height="70px" width="250px">ම‌වගේ අනන්‍යතා අංකය / ජාතික හැඳුනුම්පත් අංකය <br>
            தந்தையின் அடையாள எண் <br>
            Mother's PIN / NIC
        </td>
        <td colspan="5">
            <s:if test="#request.adoption.applicantMother==1">
                <s:label name="" value="%{adoption.applicantPINorNIC}"/>
            </s:if>
            <s:else>
                <s:label name="" value="%{adoption.wifePINorNIC}"/>
            </s:else>
        </td>
    </tr>
    </tbody>
</table>

<table border="1" width="100%" style="margin-top:10px;float:left;border:1px solid #000; border-collapse:collapse;">
    <col width="250px">
    <col width="215px">
    <col width="120px">
    <col>
    <col>
    <tbody>
    <tr>
        <td width="250px">ලියාපදිංචි කල ස්ථානය<br>
            *in tamil <br>
            Place of Registration
        </td>
        <td colspan="3">
            <s:property value="placeOfIssue"/>
        </td>
        <td height="70px" width="150px">ලියාපදිංචි කළ දිනය<br>பதிவு செய்யப்பட்ட திகதி <br> Date of Registration
        </td>
        <td width="150px"><s:label name="" value="%{adoption.lifeCycleInfo.approvalOrRejectTimestamp}"/></td>
    </tr>
    <tr>
        <td width="250px" height="120px">
            සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන <br>
            சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்<br>
            Name, Signature and Designation of certifying officer
        </td>
        <td colspan="5" style="font-size:10pt"><s:label name=""
                                                        value="%{adoption.lifeCycleInfo.approvalOrRejectUser.role.name}"/><br/>
            <s:label name="" value="%{adoption.lifeCycleInfo.approvalOrRejectUser.role.roleId}"/></td>
    </tr>
    <tr>
        <td width="250px" height="30px">නිකුත් කළ ස්ථානය / வழங்கிய இடம் / Place of Issue
        </td>
        <td colspan="3"><s:label value=""/>
        </td>

        <td width="150px">නිකුත් කළ දිනය<br>வழங்கிய திகதி <br> Date of Issue
        </td>
        <td width="150px"><s:label name="" value="%{adoption.lifeCycleInfo.lastUpdatedTimestamp}"/>
        </td>
    </tr>
    </tbody>
</table>

<s:if test="#request.adoption.birthCertificateNumber>0 || #request.adoption.birthRegistrationSerial>0">
    <table style="font-size:12pt;text-align:center;width:100%">
        <tr></tr>
        <tr>
            <td>දැනට උප්පැන්න සහතිකයක් ඇත්නම් එහි විස්තර <br/>
                Birth Certificate information (if available)
            </td>
        </tr>
    </table>

    <table border="1" width="100%" style="margin-top:10px;float:left;border:1px solid #000; border-collapse:collapse;">
        <tr>
            <td width="25%">දිස්ත්‍රික්ක <br/>
                மாவட்டம் <br/>
                District
            </td>
            <td width="25%"><s:label name="" value="%{birthDistrictName}"/></td>
            <td width="25%">ප්‍රාදේශීය ලේකම් කොට්ඨාශය<br/>
                பிரிவு <br/>
                Divisional Secretariat
            </td>
            <td width="25%"><s:label name="" value="%{dsDivisionName}"/></td>
        </tr>
        <tr>
            <td width="25%">ලියාපදිංචි කිරීමේ කොට්ඨාශය<br/>
                பிரிவு <br/>
                Registration Division
            </td>
            <td width="25%"><s:label name="" value="%{birthDivisionName}"/></td>
            <td>දැනට පවතින උප්පැන්න සහතිකයේ අණුක්‍රමික අංකය<br/>
                ** in Tamil **<br/>
                The serial number of the existing Birth Certificate
            </td>
            <td width="25%">
                <s:if test="#request.adoption.birthCertificateNumber>0">
                    <s:label name="" value="%{adoption.birthCertificateNumber}"/>
                </s:if>
                <s:elseif test="#request.adoption.birthRegistrationSerial>0">
                    <s:label name="" value="%{adoption.birthRegistrationSerial}"/>
                </s:elseif>
            </td>
        </tr>
    </table>
</s:if>
<table style="width:100%; border-left:none;font-size:10pt;text-align:center;margin-bottom:10px;">
    <tr>
        <td>
            (1941 අංක 24 දරන දරුකමට ගැනීම පිලිබඳ ආඥාපනතේ 11 වෙනි වගන්තිය යටතේ නිකුත් කරන ලදී) <br>
            in tamil <br>
            (Issued under Section 11 of the Adoption of Children Ordinance, No. 24 of 1941)
        </td>
    </tr>
</table>


<div class="form-submit">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
</div>

<div id="adoption-page" class="form-submit" style="margin:15px 0 0 10px; ">
    <s:a href="%{cancel}"><s:label value="%{getText('cancel.button')}"/></s:a>
    <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
</div>
</div>

