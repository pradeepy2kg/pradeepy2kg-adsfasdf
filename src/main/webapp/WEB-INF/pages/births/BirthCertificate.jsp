<%@ page import="java.util.Date" %>
<%-- @author Duminda Dharmakeerthi. --%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css">
    @media print {
        .form-submit {
            display: none;
        }

        td {
            font-size: 11pt;
        }
    }
</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>

<script type="text/javascript">
    function printPage() {
        if (!jsPrintSetup) {
            var option = confirm("You don't have Printer plugin.\nDo you wan't to download it?");
            if (option) {
                window.open("https://addons.mozilla.org/en-US/firefox/addon/8966/");
            }
        } else {
            // set page orientation.
            jsPrintSetup.setOption('orientation', jsPrintSetup.kPortraitOrientation);
            // set margins.
            jsPrintSetup.setOption('marginTop', 0);
            jsPrintSetup.setOption('marginLeft', 20);


            // set page header
            jsPrintSetup.setOption('headerStrLeft', '');
            jsPrintSetup.setOption('headerStrCenter', '');
            jsPrintSetup.setOption('headerStrRight', '');
            // set empty page footer
            jsPrintSetup.setOption('footerStrLeft', '');
            jsPrintSetup.setOption('footerStrCenter', '');
            jsPrintSetup.setOption('footerStrRight', '');

            jsPrintSetup.print();

            var res = confirm(document.getElementById("printMessage").value);
            history.go(0);
        }
    }
</script>
<div id="birth-certificate-outer">
<%--<s:form action="eprBirthCetificateList.do" name="birthCertificatePrint"--%>
<%--id="birth-certificate-print-form" method="POST">--%>


<table style="width: 100%; border:none; border-collapse:collapse; ">
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
        <td><s:label name="bdId"/></td>
    </tr>
    <tr>
        <td align="center">ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA <br>
            <s:if test="birthType.ordinal() != 0">
                උප්පැන්න සහතික<br>
                பிறப்பு சான்றிதழ்﻿<br>
                BIRTH CERTIFICATE
            </s:if>
            <s:else>
                මළ උප්පැන්න සහතික<br>
                * In Tamil<br>
                STILL BIRTH CERTIFICATE
            </s:else>
        </td>
        <td></td>
    </tr>
    </tbody>
</table>

<table border="1" class="table-with-border">
    <col width="135px">
    <col>
    <col width="145px">
    <col>
    <tbody>
    <tr>
        <td width="175px" height="80px">දිස්ත්‍රික්කය<br>மாவட்டம் <br>District
        </td>
        <td width="300px"><s:label name="" value="%{childDistrict}"/><br/>
            <s:label name="" value="%{childDistrictEn}"/></td>
        <td width="200px">ප්‍රාදේශීය ලේකම් කොට්ඨාශය
            <br> பிரிவு
            <br> Divisional Secretariat
        </td>
        <td><s:label name="" value="%{childDsDivision}"/><br/>
            <s:label name="" value="%{childDsDivisionEn}"/></td>
    </tr>
    <tr>
        <td width="200px" height="90px">ලියාපදිංචි කිරීමේ කොට්ඨාශය
            <br>பிரிவு
            <br>Registration Division
        </td>
        <td><s:label name="" value="%{#request.register.birthDivision.siDivisionName}"/></td>
        <s:if test="birthType.ordinal() != 0">
            <td>මුල් ලියාපදිංචියෙන් පසු වෙනස්කම්
                <br> நிறைவேற்றிய மாற்றங்கள்
                <br> Changes after first registration
            </td>
            <%--TODO fill variable--%>
            <td><s:label name="" value="%{}"/></td>
        </s:if>
    </tr>
    </tbody>
</table>

<table border="1" class="table-with-border">
    <col width="150px">
    <col width="160px">
    <col width="100px">
    <col width="130px">
    <col width="100px">
    <col>
    <tbody>
    <s:if test="birthType.ordinal() != 0">
        <tr>
            <td height="80px">පුද්ගල අනන්‍යතා අංකය <br>தனிநபர்அடையாள எண் <br>Person Identification Number (PIN)
            </td>
            <td><s:label name="" value="%{#request.child.pin}"/></td>
            <td width="100px">උපන් දිනය <br>பிறந்த திகதி <br>Date of birth <br>YYYY-MM-DD
            </td>
            <td width="150px"><s:label name="" value="%{#request.child.childDateOfBirthForPrint}"/></td>
            <td>ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender
            </td>
            <td width="150px">
                <s:label name="" value="%{gender}"/><br/>
                <s:label name="" value="%{genderEn}"/>
            </td>
        </tr>
    </s:if>
    <s:else>
        <tr>
            <td height="80px" width="100px">උපන් දිනය <br>பிறந்த திகதி <br>Date of birth <br>YYYY-MM-DD
            </td>
            <td><s:label name="" value="%{#request.child.dateOfBirth}"/></td>
            <td>ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender
            </td>
            <td width="150px">

                <s:label name="" value="%{gender}"/><br/>
                <s:label name="" value="%{genderEn}"/>
            </td>
            <td>දරැවා මැරී උපදින විට ගර්භයට සති කීයක් වී තිබුනේද යන්න
                <br>* In Tamil
                <br>Number of weeks pregnant at the time of still-birth
            </td>
            <td><s:label name="" value="%{#request.child.weeksPregnant}"/></td>

        </tr>
    </s:else>
    <tr>
        <td height="70px">උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of birth
        </td>
        <td colspan="2">
            <s:label name="placeOfBirth" value="%{#request.child.placeOfBirth}"/><br>
            <s:label name="placeOfBirthEnglish" value="%{#request.child.placeOfBirthEnglish}"/>
        </td>
        <td colspan="2">මව්පියන් විවාහකද? <br>பெற்றோர் விவாகம் செய்தவர்களா? <br>Were Parents Married?
        </td>
        <td><s:label name="" value="%{getText('married.status.'+#request.marriage.parentsMarried)}"/></td>
    </tr>
    <s:if test="birthType.ordinal() != 0">
        <tr>
            <td height="120px">නම <br>பெயர் <br>Name
            </td>
            <td colspan="5" class="bc-name" style="font-size:12pt">
                <s:label name="" value="%{#request.child.childFullNameOfficialLang}"/>
            </td>
        </tr>
        <tr>
            <td height="110px">නම ඉංග්‍රීසි භාෂාවෙන් <br>ஆங்கிலத்தில் பெயர் <br> Name in English
            </td>
            <td colspan="5" class="bc-name" style="font-size:12pt">
                <s:label name="" cssStyle="text-transform: uppercase;" value="%{#request.child.childFullNameEnglish}"/>
            </td>
        </tr>
    </s:if>
    <tr>
        <td height="120px">පියාගේ සම්පුර්ණ නම<br>தந்தையின்முழுப் பெயர் <br> Father's Full Name
        </td>
        <td colspan="5" class="bc-name" style="font-size:12pt">
            <s:label name="" value="%{#request.parent.fatherFullName}"/>
        </td>
    </tr>
    <tr>
        <td height="70px">පියාගේ අනන්‍යතා අංකය <br>தந்தையின் அடையாள எண் <br> Father's PIN
        </td>
        <td><s:label name="" value="%{#request.parent.fatherNICorPIN}"/></td>
        <td>පියාගේ ජාතිය<br>தந்தையின் இனம் <br> Father's Race
        </td>
        <td colspan="3"><s:label name="" value="%{fatherRacePrint}"/><br/><s:label name=""
                                                                                   value="%{fatherRacePrintEn}"/></td>

    </tr>

    <tr>
        <td height="120px">මවගේ සම්පූර්ණ නම
            <br> தாயின் முழுப் பெயர்
            <br> Mother's Full Name
        </td>
        <td colspan="5" class="bc-name" style="font-size:12pt">
            <s:label name="" value="%{#request.parent.motherFullName}"/>
        </td>
    </tr>
    <tr>
        <td height="70px">ම‌වගේ අනන්‍යතා අංකය <br>தாயின் அடையாள எண் <br> Mother's PIN
        </td>
        <td><s:label name="" value="%{#request.parent.motherNICorPIN}"/></td>
        <td>මවගේ ජාතිය<br>தாயின் இனம் <br> Mother's Race
        </td>
        <td colspan="3"><s:label name="" value="%{motherRacePrint}"/><br/>
            <s:label name="" value="%{motherRacePrintEn}"/></td>
    </tr>

    </tbody>
</table>

<table border="1" class="table-with-border">
    <col width="195px">
    <col width="215px">
    <col width="120px">
    <col>
    <tbody>
    <tr>
        <td height="70px">ලියාපදිංචි කළ දිනය<br>பதிவு செய்யப்பட்ட திகதி <br> Date of Registration
        </td>
        <td><s:label name="" value="%{#request.register.dateOfRegistrationForPrint}"/></td>
        <td>නිකුත් කළ දිනය<br>வழங்கிய திகதி <br> Date of Issue
        </td>
        <td><s:label name="" value="%{#request.register.originalBCDateOfIssueForPrint}"/></td>
    </tr>
    <tr>
        <td colspan="2" height="120px">
            සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන <br>
            சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்<br>
            Name, Signature and Designation of certifying officer
        </td>
        <td colspan="2" style="font-size:10pt"><s:label name=""
                                                        value="%{#request.register.confirmantFullName}"/></td>
    </tr>
    <tr>
        <td colspan="2" height="30px">නිකුත් කළ ස්ථානය / வழங்கிய இடம் / Place of Issue
        </td>
        <td colspan="2"><s:label name=""/></td>
    </tr>
    </tbody>
</table>

<p>උප්පැන්න හා මරණ ලියපදිංචි කිරිමේ පණත (110 අධිකාරය) යටතේ රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව විසින් නිකුත් කරන
    ලදි.<br>
    பிறப்பு இறப்பு பதிவு செய்யும் சட்டத்தின்ப்புடி பதிவாளர் நாயகத் திணைக்களத்தினால் வழங்கப்பட்டது <br>
    Issued by Registrar General's Department according to Birth and Death Registration Act (110 Authority)</p>

<s:if test="directPrint">
    <s:url id="print" action="eprDirectPrintStillBirthCertificate.do">
        <s:param name="bdId" value="#request.bdId"/>
    </s:url>
</s:if>
<s:elseif test="directPrintBirthCertificate">
    <s:url id="print" action="eprDirectPrintBirthCertificate.do">
        <s:param name="bdId" value="#request.bdId"/>
    </s:url>
</s:elseif>
<s:else>
    <%--TODO remove unused parameters--%>
    <s:url id="print" action="eprFilterBirthCetificateList.do">
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="birthDistrictId" value="#request.register.birthDivision.dsDivision.district.districtUKey"/>
        <s:param name="birthDivisionId" value="#request.register.birthDivision.bdDivisionUKey"/>
        <s:param name="dsDivisionId" value="#request.register.birthDivision.dsDivision.dsDivisionUKey"/>
        <s:param name="printed" value="#request.printed"/>
        <s:param name="printStart" value="#request.printStart"/>
    </s:url>
</s:else>
<div class="form-submit">
    <s:a href="%{print}" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
    <s:hidden id="printMessage" value="%{getText('print.message')}"/>
</div>
<%--</s:form>--%>
</div>
<%-- Styling Completed --%>