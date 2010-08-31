<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">
    #death-certificate-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }

        td {
            font-size: 8pt;
        }
    }

</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>

<div id="death-certificate-outer">
    <s:form name="deathCertificate" action="eprPrintDeath.do" id="deathCertificate-form" method="POST">
        <table style="width: 100%; border:none; border-collapse:collapse; ">
            <col width="300px"/>
            <col width="400px"/>
            <col width="130px"/>
            <col/>
            <tbody>
            <tr>
                <td rowspan="3"></td>
                <td rowspan="2" align="center">
                    <img src="<s:url value="../images/official-logo.png" />"
                         style="display: block; text-align: center;" width="80" height="100">
                </td>
                <td class="font-9" style="border:1px solid #000;" height="60px">සහතික පත්‍රයේ අංකය <br>சான்றிதழ் இல <br>Certificate
                    Number
                </td>
                <td style="border:1px solid #000;"><s:label name="" value="%{idUKey}"/></td>
            </tr>
            <tr>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td align="center" class="font-12">ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA <br>
                    මරණ ලියාපදිංචි කිරීමේ ලේඛනය <br>பிறப்பு சான்றிதழ்﻿<br>REGISTER OF DEATHS
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
                <td>දිස්ත්‍රික්කය<br>மாவட்டம் <br>District</td>
                <td><s:label name="" value="%{deathPersonDistrict}"/><br>
                    <s:label name="" value="%{deathPersonDistrictEn}"/></td>
                <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය<br> பிரிவு <br> Divisional Secretariat</td>
                <td><s:label name="" value="%{deathPersondsDivision}"/><br>
                    <s:label name="" value="%{deathPersondsDivisionEn}"/>
                </td>
            </tr>
            <tr>
                <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය<br>பிரிவு<br>Registration Division</td>
                <td><s:label name="" value="%{deathPersonDeathDivision}"/><br>
                    <s:label name="" value="%{deathPersonDeathDivisionEn}"/></td>
                <td>මුල් ලියාපදිංචියෙන් පසු වෙනස්කම්<br> நிறைவேற்றிய மாற்றங்கள்<br> Changes after first registration
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
            <col width="150px"/>
            <col width="120px"/>
            <col width="100px"/>
            <col/>
            <tbody>
            <tr>
                <td>පුද්ගල අනන්‍යතා අංකය <br>தனிநபர்அடையாள எண் <br>Person Identification Number (PIN)</td>
                <td><s:label name="" value="%{deathPerson.deathPersonPINorNIC}"/></td>
                <td>වයස<br>*in tamil<br>Age</td>
                <td><s:label name="" value="%{deathPerson.deathPersonAge}"/></td>
                <td>මරණය සිදුවූ දිනය<br>பிறந்த திகதி<br>Date of death</td>
                <td><s:label name="" value="%{death.dateOfDeath}"/></td>
                <td>ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender</td>
                <td>
                    <s:label name="" value="%{genderSi}"/>
                    <br> <s:label name="" value="%{genderEn}"/>
                </td>
            </tr>
            <tr>
                <td colspan="1">මරණය සිදුවූ ස්ථානය<br>*in tamil<br>Place of death</td>
                <td colspan="7"><s:label name="" value="%{death.placeOfDeathInOfficialLang}"/>
                    <br><s:label name="" value="%{death.placeOfDeathInEnglish}"/></td>
            </tr>
            <tr>
                <td colspan="1">මරණයට හේතුව<br>*in tamil<br>Cause of Death</td>
                <td colspan="5"><s:label name="" value="%{death.causeOfDeath}"/></td>
                <td colspan="1">හේතුවේ ICD කේත අංකය<br>*in tamil<br>ICD Code of cause</td>
                <td colspan="1"><s:label name="" value="%{death.icdCodeOfCause}"/></td>
            </tr>
            <tr>
                <td colspan="1">ආදාහන හෝ භූමදාන ස්ථානය<br>*in tamil<br>Place of burial or cremation</td>
                <td colspan="7"><s:label name="" value="%{death.placeOfBurial}"/></td>
            </tr>
            <tr>
                <td colspan="1" height="120px">නම <br>பெயர் <br>Name</td>
                <td colspan="7" style="font-size:10pt"><s:label name=""
                                                                value="%{deathPerson.deathPersonNameOfficialLang}"/></td>
            </tr>
            <tr>
                <td colspan="1" height="120px">නම ඉංග්‍රීසි භාෂාවෙන් <br>ஆங்கிலத்தில் பெயர் <br> Name in English</td>
                <td colspan="7"><s:label name="" value="%{deathPerson.deathPersonNameInEnglish}"/></td>
            </tr>
            <tr>
                <td colspan="1" height="100px">පියාගේ සම්පුර්ණ නම<br>தந்தையின்முழுப் பெயர் <br> Father's Full Name</td>
                <td colspan="5"><s:label name="" value="%{deathPerson.deathPersonFatherFullName}"/></td>
                <td colspan="1">පු.අ.අ. හෝ ජා.හැ.අ.<br>*in tamil <br>PIN / NIC</td>
                <td colspan="1"><s:label name="" value="%{deathPerson.deathPersonFatherPINorNIC}"/></td>
            </tr>
            <tr>
                <td colspan="1" height="100px">මවගේ සම්පූර්ණ නම<br> தாயின் முழுப் பெயர்<br> Mother's Full Name</td>
                <td colspan="5"><s:label name="" value="%{deathPerson.deathPersonMotherFullName}"/></td>
                <td colspan="1">පු.අ.අ. හෝ ජා.හැ.අ.<br>*in tamil <br>PIN / NIC</td>
                <td colspan="1"><s:label name="" value="%{deathPerson.deathPersonMotherPINorNIC}"/></td>
            </tr>
            <tr>
                <td colspan="1" height="100px">දැනුම් දෙන්නගේ නම<br>கொடுப்பவரின் பெயர்<br>Informant's Name</td>
                <td colspan="5"><s:label name="" value="%{declarant.declarantFullName}"/></td>
                <td colspan="1">පු.අ.අ. හෝ ජා.හැ.අ.<br>*in tamil <br>PIN / NIC</td>
                <td colspan="1"><s:label name="" value="%{declarant.declarantNICorPIN}"/></td>
            </tr>
            <tr>
                <td colspan="1" height="100px">දැනුම් ලිපිනය <br>கொடுப்பவரின் பெயர்<br>Informant's Address</td>
                <td colspan="7"><s:label name="" value="%{declarant.declarantAddress}"/></td>
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
                <td>ලියාපදිංචි කළ දිනය<br>பதிவு செய்யப்பட்ட திகதி <br> Date of Registration</td>
                <td><s:label name="" value="%{death.dateOfRegistration}"/></td>
                <td>නිකුත් කළ දිනය<br>வழங்கிய திகதி <br> Date of Issue</td>
                <td><s:label name="" value="%{death.dateOfIssue}"/></td>
            </tr>
            <tr>
                <td colspan="2">සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන <br>
                    சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்<br>
                    Name, Signature and Designation of certifying officer
                </td>
                <td colspan="2" height="120px"><s:label name="" value="%{notifyingAuthority.notifyingAuthorityName}"/>,
                    <br>
                    <s:label name="" value="%{notifyingAuthority.notifyingAuthorityAddress}"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">නිකුත් කළ ස්ථානය / வழங்கிய இடம் / Place of Issue</td>
                <td colspan="2"><s:label name="" value="%{death.placeOfIssue}"/></td>
            </tr>
            </tbody>
        </table>

        <s:label><p class="font-7">පු.අ.අ. හෝ ජා.හැ.අ. = අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය</p></s:label>
        <s:label><p class="font-8" style="width:100%; text-align:center;">උප්පැන්න හා මරණ ලියපදිංචි කිරිමේ පණත (110
            අධිකාරය)
            යටතේ රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව විසින්
            නිකුත් කරන
            ලදි.<br>
            பிறப்பு இறப்பு பதிவு செய்யும் சட்டத்தின்ப்புடி பதிவாளர் நாயகத் திணைக்களத்தினால் வழங்கப்பட்டது <br>
            Issued by Registrar General's Department according to Birth and Death Registration Act (110 Authority)</p>
        </s:label>
        <s:if test="#request.allowPrintCertificate">
            <div class="form-submit">
                <s:hidden name="idUKey" value="%{#request.idUKey}"/>
                <s:hidden name="currentStatus" value="%{#request.currentStatus}"/>
                <s:hidden name="pageNo" value="%{#request.pageNo}"/>
                <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
                <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
                <s:hidden name="dsDivisionId" value="%{#request.dsDivisionId}"/>
                <s:hidden name="deathDivisionId" value="%{#request.deathDivisionId}"/>
                <s:submit value="%{getText('mark_as_print.button')}" cssStyle="margin-top:10px;"/>
            </div>
        </s:if>
    </s:form>
    <s:form action="eprDeathBackToPreviousState.do">
        <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
        <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
        <s:hidden name="pageNo" value="%{#request.pageNo}"/>
        <s:hidden name="currentStatus" value="%{#request.currentStatus}"/>
        <s:hidden name="dsDivisionId" value="%{#request.dsDivisionId}"/>
        <s:hidden name="deathDivisionId" value="%{#request.deathDivisionId}"/>
        <div class="form-submit">
            <s:submit value="%{getText('cancel.button')}" cssStyle="margin-top:10px;"/>
        </div>
    </s:form>
    <s:if test="#request.allowPrintCertificate">
        <div class="form-submit">
            <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
        </div>
    </s:if>
</div>

<%--idUKey=5&currentStatus=0&pageNo=1&nextFlag=false&previousFlag=false&rePrint=false--%>