<%@ page import="java.util.Date" %>
<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
<%-- @author Duminda Dharmakeerthi --%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css">
    #death-certificate-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }

        td {
            font-size: 9pt;
        }
    }

    #death-certificate-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>

<div id="death-certificate-outer">
<s:if test="directPrint">
    <s:url id="print" action="eprDierctPrintDeathCertificate.do">
        <s:param name="idUKey" value="#request.idUKey"/>
    </s:url>
    <s:url id="cancel" action="eprInitDeathHome.do"/>
</s:if>

<s:else>
    <s:if test="#request.certificateSearch">
        <s:url id="print" action="eprMarkDeathCertificateSearch.do">
            <s:param name="idUKey" value="#request.idUKey"/>
        </s:url>
        <s:url id="cancel" action="eprDeathCertificateSearch.do">
        </s:url>
    </s:if>
    <s:else>
        <s:url id="print" action="eprPrintDeathCertificate.do">
            <s:param name="idUKey" value="#request.idUKey"/>
            <s:param name="currentStatus" value="%{#request.currentStatus}"/>
            <s:param name="pageNo" value="%{#request.pageNo}"/>
            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
            <s:param name="dsDivisionId" value="%{#request.dsDivisionId}"/>
            <s:param name="deathDivisionId" value="%{#request.deathDivisionId}"/>
        </s:url>
        <s:url id="cancel" action="eprDeathBackToPreviousState.do">
            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
            <s:param name="pageNo" value="%{#request.pageNo}"/>
            <s:param name="currentStatus" value="%{#request.currentStatus}"/>
            <s:param name="dsDivisionId" value="%{#request.dsDivisionId}"/>
            <s:param name="deathDivisionId" value="%{#request.deathDivisionId}"/>
        </s:url>
    </s:else>
</s:else>
<s:if test="#request.allowPrintCertificate">
    <div id="birthRegistration-page" class="form-submit" style="margin-top:15px;float:right;">
        <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
    </div>
    <div class="form-submit">
        <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
        <s:hidden id="printMessage" value="%{getText('print.message')}"/>
    </div>
</s:if>
<div id="birthRegistration-page" class="form-submit" style="margin-top:15px;float:right;">
    <s:a href="%{cancel}"><s:label value="%{getText('cancel.button')}"/></s:a>
</div>
<table style="width: 100%; border:none; border-collapse:collapse; ">
    <col width="250px"/>
    <col width="530px"/>
    <col width="250px"/>

    <tbody>
    <tr>
        <td rowspan="3"></td>
        <td rowspan="2" align="center">
            <img src="<s:url value="../images/official-logo.png" />"
                 style="display: block; text-align: center;" width="80" height="100">
        </td>
        <td class="font-9" height="60px">
            <table border="1" style="width:100%;border:1px solid #000;border-collapse:collapse;">
                <tr height="60px">
                    <td>ලියාපදිංචි කිරීමේ අංකය<br>பதிவு இலக்கம்<br>Registration Number</td>
                    <td>සහතික පත්‍රයේ අංකය<br>சான்றிதழ் இல<br>Certificate Number</td>
                </tr>
                <tr height="40px">
                    <td align="center"><s:label name=""
                                                cssStyle="font-size:11pt;"/></td>
                    <td align="center"><s:label name="idUKey" cssStyle="font-size:11pt;"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td align="center" class="font-12">
            ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA
            <br>මරණ ලියාපදිංචි කිරීමේ ලේඛනය
            <br>இறப்பு சான்றிதழ்
            <br>REGISTER OF DEATHS
        </td>
        <td colspan="2"></td>
    </tr>
    </tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; " class="font-9">
    <col width="135px"/>
    <col width="390px"/>
    <col width="145px"/>
    <col/>
    <tbody>
    <tr>
        <td>
            දිස්ත්‍රික්කය
            <br>மாவட்டம்
            <br>District
        </td>
        <td><s:label name="" value="%{deathPersonDistrict}"/><br>
            <s:label name="" value="%{deathPersonDistrictEn}"/></td>
        <td>
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය
            <br>பிரதேச செயலாளர் பிரிவு
            <br>Divisional Secretariat
        </td>
        <td><s:label name="" value="%{deathPersondsDivision}"/><br>
            <s:label name="" value="%{deathPersondsDivisionEn}"/>
        </td>
    </tr>
    <tr>
        <td>
            ලියාපදිංචි කිරීමේ කොට්ඨාශය
            <br>பதிவுப் பிரிவு
            <br>Registration Division
        </td>
        <td><s:label name="" value="%{deathPersonDeathDivision}"/><br>
            <s:label name="" value="%{deathPersonDeathDivisionEn}"/></td>
        <td>
            මුල් ලියාපදිංචියෙන් පසු වෙනස්කම්
            <br>நிறைவேற்றிய மாற்றங்கள்
            <br>Changes after first registration
        </td>
        <td><s:label name="" value="%{}"/></td>
    </tr>
    </tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; " class="font-9">
    <col width="150px"/>
    <col width="160px"/>
    <col width="100px"/>
    <col width="100px"/>
    <col width="180px"/>
    <col width="120px"/>
    <col width="100px"/>
    <col/>
    <tbody>
    <tr>
        <td height="60px">
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification Number
        </td>
        <td><s:label name="" value="%{deathPerson.deathPersonPINorNIC}"/></td>
        <td>
            වයස
            <br>வயது
            <br>Age
        </td>
        <td><s:label name="" value="%{deathPerson.deathPersonAge}"/></td>
        <td>
            මරණය සිදුවූ දිනය
            <br>இறப்பு நிகழ்ந்த திகதி
            <br>Date of death
        </td>
        <td><s:label name="" value="%{death.dateOfDeath}"/></td>
        <td>
            ස්ත්‍රී පුරුෂ භාවය
            <br>பால்
            <br>Gender
        </td>
        <td>
            <s:label name="" value="%{genderSi}"/>
            <br> <s:label name="" value="%{genderEn}"/>
        </td>
    </tr>
    <tr>
        <td colspan="1" height="60px">
            මරණයේ ස්වභාවය
            <br>மரணத்தின் வகை
            <br>Type of Death
        </td>
        <td colspan="7"><s:label name="" value="%{}"/>
            <br><s:label name="" value="%{}"/></td>
    </tr>
    <tr>
        <td colspan="1" height="60px">
            මරණය සිදුවූ ස්ථානය
            <br>இறப்பு நிகழந்த இடம்
            <br>Place of death
        </td>
        <td colspan="7"><s:label name="" value="%{death.placeOfDeath}"/>
            <br><s:label name="" value="%{death.placeOfDeathInEnglish}"/></td>
    </tr>
    <tr>
        <td colspan="1" height="60px">
            මරණයට හේතු
            <br>இறப்பிற்கான காரணம்
            <br>Cause of Death
        </td>
        <td colspan="7" style="font-size:9pt"><s:label name="" value="%{death.causeOfDeath}"/></td>
        <%--<td colspan="1">හේතුවේ ICD කේත අංකය<br>*in tamil<br>ICD Code of cause</td>--%>
        <%--<td colspan="2"><s:label name="" value="%{death.icdCodeOfCause}"/></td>--%>
    </tr>
    <tr>
        <td colspan="1">
            ආදාහන හෝ භූමදාන ස්ථානය
            <br>அடக்கம் செய்த அல்லது தகனஞ் செய்த இடம்
            <br>Place of burial or cremation
        </td>
        <td colspan="7" style="font-size:10pt"><s:label name="" value="%{death.placeOfBurial}"/></td>
    </tr>
    <tr>
        <td colspan="1" height="100px">
            නම
            <br>பெயர்
            <br>Name
        </td>
        <td colspan="7" style="font-size:10pt"><s:label name=""
                                                        value="%{deathPerson.deathPersonNameOfficialLang}"/></td>
    </tr>
    <tr>
        <td colspan="1" height="90px">
            නම ඉංග්‍රීසි භාෂාවෙන්
            <br>பெயர் ஆங்கில மொழியில்
            <br>Name in English
        </td>
        <td colspan="7" style="font-size:10pt"><s:label name="" value="%{deathPerson.deathPersonNameInEnglish}"/></td>
    </tr>
    <tr>
        <td colspan="1" height="80px">
            පියාගේ සම්පුර්ණ නම
            <br>தந்தையின்முழுப் பெயர்
            <br>Father's Full Name
        </td>
        <td colspan="4" style="font-size:10pt"><s:label name="" value="%{deathPerson.deathPersonFatherFullName}"/></td>
        <td colspan="1">
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification No.
        </td>
        <td colspan="2"><s:label name="" value="%{deathPerson.deathPersonFatherPINorNIC}"/></td>
    </tr>
    <tr>
        <td colspan="1" height="80px">
            මවගේ සම්පූර්ණ නම
            <br>தாயின் முழுப் பெயர்
            <br>Mother's Full Name
        </td>
        <td colspan="4" style="font-size:10pt"><s:label name="" value="%{deathPerson.deathPersonMotherFullName}"/></td>
        <td colspan="1">
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification No.
        </td>
        <td colspan="2"><s:label name="" value="%{deathPerson.deathPersonMotherPINorNIC}"/></td>
    </tr>
    <tr>
        <td colspan="1" height="80px">
            දැනුම් දෙන්නගේ නම
            <br>தகவலளிப்பவரின் பெயர்
            <br>Informant's Name
        </td>
        <td colspan="4" style="font-size:10pt"><s:label name="" value="%{declarant.declarantFullName}"/></td>
        <td colspan="1">
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification No.
        </td>
        <td colspan="2"><s:label name="" value="%{declarant.declarantNICorPIN}"/></td>
    </tr>
    <tr>
        <td colspan="1" height="80px">
            දැනුම් ලිපිනය
            <br>தகவலளிப்பவரின் முகவரி
            <br>Informant's Address
        </td>
        <td colspan="7" style="font-size:10pt"><s:label name="" value="%{declarant.declarantAddress}"/></td>
    </tr>
    </tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin-bottom:0;"
       class="font-9">
    <col width="195px"/>
    <col width="215px"/>
    <col width="120px"/>
    <col/>
    <tbody>
    <tr>
        <td height="50px">
            ලියාපදිංචි කළ දිනය
            <br>பதிவு செய்யப்பட்ட திகதி
            <br>Date of Registration
        </td>
        <td><s:label name="" value="%{death.dateOfRegistration}"/></td>
        <td>
            නිකුත් කළ දිනය
            <br>வழங்கிய திகதி
            <br>Date of Issue
        </td>
        <td><%= DateTimeUtils.getISO8601FormattedString(new Date()) %>
        </td>
    </tr>
    <tr>
        <td colspan="2" height="80px">
            සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන
            <br>சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்
            <br>Name, Signature and Designation of certifying officer
        </td>
        <td colspan="2" ><s:label name="nameOfOfficer" value="%{}"/>,
            <br>
            <s:label name="designationOfCertifyingOfficer " value="%{}"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" height="30px">
            නිකුත් කළ ස්ථානය / வழங்கிய இடம் / Place of Issue
        </td>
        <td colspan="2"><s:label name="" value="%{death.placeOfIssue}"/></td>
    </tr>
    </tbody>
</table>

<s:label><p class="font-8" style="width:100%; text-align:center;">
    උප්පැන්න හා මරණ ලියාපදිංචි කිරීමේ ආඥාපනතේ 110 වෙනි පරිච්චේදය යටතේ නිකුත් කරන ලදී
    <br>பிறப்பு இறப்பு பதிவு செய்யும் சட்டத்தின்(110 ஆம் அத்தியாயத்தின்) கீழ் பதிவாளர் நாயகம் திணைக்களத்தினால்
    வழங்கப்பட்டது
    <br>Issued under Cap. 110 of the Births and Deaths Registration Act
    </s:label>


    <s:if test="#request.allowPrintCertificate">

<div id="birthRegistration-page" class="form-submit" style="margin-top:15px;float:right;">
    <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
</div>
<div class="form-submit">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    <s:hidden id="printMessage" value="%{getText('print.message')}"/>
</div>
</s:if>
<div id="birthRegistration-page" class="form-submit" style="margin-top:15px;float:right;">
    <s:a href="%{cancel}"><s:label value="%{getText('cancel.button')}"/></s:a>
</div>

</div>

