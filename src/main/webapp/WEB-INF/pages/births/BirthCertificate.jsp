<%-- @author Duminda Dharmakeerthi. --%>
<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="lk.rgd.common.util.MarriedStatusUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css">
    #birth-certificate-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }
    }

    #birth-certificate-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<script type="text/javascript">
    function initPage() {
    }
</script>

<div id="birth-certificate-outer">
<s:if test="directPrint">
    <s:url id="print" action="eprDirectPrintBirthCertificate.do">
        <s:param name="bdId" value="#request.bdId"/>
    </s:url>
    <s:url id="cancel" action="eprBirthRegistrationHome.do"/>
</s:if>
<s:else>
    <s:if test="#request.certificateSearch">
        <s:url id="print" action="eprMarkBirthCertificateSearch.do">
            <s:param name="idUKey" value="#request.idUKey"/>
        </s:url>
    </s:if>
    <s:else>
        <s:url id="print" action="eprMarkCertificateAsPrinted.do">
            <s:param name="pageNo" value="%{#request.pageNo}"/>
            <s:param name="bdId" value="%{#request.bdId}"/>
            <s:param name="birthDistrictId" value="#request.register.birthDivision.dsDivision.district.districtUKey"/>
            <s:param name="birthDivisionId" value="#request.register.birthDivision.bdDivisionUKey"/>
            <s:param name="dsDivisionId" value="#request.register.birthDivision.dsDivision.dsDivisionUKey"/>
            <s:param name="printed" value="#request.printed"/>
            <s:param name="printStart" value="#request.printStart"/>
        </s:url>
    </s:else>
    <s:url id="cancel" action="eprBirthCancelCertificatePrint.do">
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="birthDistrictId" value="#request.register.birthDivision.dsDivision.district.districtUKey"/>
        <s:param name="birthDivisionId" value="#request.register.birthDivision.bdDivisionUKey"/>
        <s:param name="dsDivisionId" value="#request.register.birthDivision.dsDivision.dsDivisionUKey"/>
        <s:param name="printed" value="#request.printed"/>
        <s:param name="printStart" value="#request.printStart"/>
    </s:url>
</s:else>

<s:if test="#request.allowPrintCertificate">
    <div class="form-submit" style="margin:15px 0 0 10px; ">
        <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
    </div>
    <div class="form-submit">
        <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
        <s:hidden id="printMessage" value="%{getText('print.message')}"/>
    </div>
</s:if>
<div class="form-submit" style="margin-top:15px;">
    <s:a href="%{cancel}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>

<table style="width: 100%; border:none; border-collapse:collapse;">
    <col width="300px">
    <col width="400px">
    <col width="340px">
    <tbody>
    <tr>
        <td rowspan="3"></td>
        <td rowspan="2" align="center">
            <img src="<s:url value="../images/official-logo.png" />"
                 style="display: block; text-align: center;" width="100" height="120">
        </td>
        <td>
            <table border="1" style="width:100%;border:1px solid #000;border-collapse:collapse;">
                <tr height="60px">
                    <td>ලියාපදිංචි කිරීමේ අංකය<br>பதிவு இலக்கம்<br>Registration Number</td>
                    <td>සහතික පත්‍රයේ අංකය<br>சான்றிதழ் இல<br>Certificate Number</td>
                </tr>
                <tr height="40px">
                    <td align="center"><s:label name="register.bdfSerialNo"
                                                cssStyle="font-size:11pt;"/></td>
                    <td align="center"><s:label name="bdId" cssStyle="font-size:11pt;"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr></tr>
    <tr>
        <td align="center" style="font-size:15pt;">ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA <br>
            <s:if test="birthType.ordinal() != 0">
                උප්පැන්න සහතිකය<br>
                பிறப்பு சான்றிதழ்﻿<br>
                BIRTH CERTIFICATE
            </s:if>
            <s:else>
                මළ උප්පැන්න සහතිකය<br>
                சாப்பிள்னள பிறப்பு சான்றிதழ்﻿<br>
                STILL BIRTH CERTIFICATE
            </s:else>
        </td>
        <td></td>
    </tr>
    </tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:30px 0;font-size:10pt">
    <col width="185px">
    <col width="230px">
    <col width="180px">
    <col width="220px">
    <col width="215px">
    <tbody>
    <tr height="80px">
        <td>දිස්ත්‍රික්කය<br>மாவட்டம் <br>District</td>
        <td>
            <s:label name="" value="%{childDistrict}" cssStyle="font-size:11pt;"/><br/>
            <s:label name="" value="%{childDistrictEn}"/>
        </td>
        <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය
            <br> பிரதேச செயளாளா் பிரிவு
            <br> Divisional Secretariat
        </td>
        <td colspan="2">
            <s:label name="" value="%{childDsDivision}" cssStyle="font-size:11pt;"/><br/>
            <s:label name="" value="%{childDsDivisionEn}"/>
        </td>
    </tr>
    <s:if test="birthType.ordinal() != 0">
        <tr height="70px">
            <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය
                <br>பதிவுப் பிரிவு
                <br>Registration Division
            </td>
            <td colspan="2">
                <s:label name="" value="%{#request.register.bdDivisionPrint}" cssStyle="font-size:11pt;"/><br>
                <s:label name="" value="%{#request.register.birthDivision.enDivisionName}"/>
            </td>
            <td style="font-size:9pt;">මුල් ලියාපදිංචියෙන් පසු වෙනස්කම්
                <br>முதல் பதிவின் பின் நிறைவேற்றிய மாற்றங்கள்
                <br>Changes After First Registration
            </td>
            <td style="font-size:9pt;">ඔව් - "**" ලකුණින් පෙන්වා ඈත<br>
                ஆம் - "**" குறியீட்டில் குறிப்பிடப்பட்டுள்ளது <br>
                Yes - marked with "**"
            </td>
        </tr>
    </s:if>
    <s:else>
        <tr height="70px">
            <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය
                <br>பதிவுப் பிரிவு
                <br>Registration Division
            </td>
            <td colspan="4">
                <s:label name="" value="%{#request.register.bdDivisionPrint}" cssStyle="font-size:11pt;"/><br>
                <s:label name="" value="%{#request.register.birthDivision.enDivisionName}"/>
            </td>
        </tr>
    </s:else>
    </tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
    <col width="185px">
    <col width="230px">
    <col width="155px">
    <col width="130px">
    <col width="115px">
    <col width="115px">
    <col width="100px">
    <tbody>
    <s:if test="birthType.ordinal() != 0">
        <tr height="70px">
            <td>අනන්‍යතා අංකය<br>தனிநபர்அடையாள எண் <br>Identification Number</td>
            <td align="center" style="font-size:14pt;"><s:label name="" value="%{#request.child.pin}"/></td>
            <td>උපන් දිනය <br>பிறந்த திகதி<br>Date of birth</td>
            <td>
                <s:label name="" value="%{#request.child.childDateOfBirthForPrint}" cssStyle="font-size:12pt;"/><br>
                <s:label value="YYYY-MM-DD" cssStyle="font-size:8pt;"/>
            </td>
            <td>ස්ත්‍රී පුරුෂ භාවය<br>பால்<br>Gender</td>
            <td colspan="2">
                <s:label name="" value="%{gender}" cssStyle="font-size:11pt;"/><br/>
                <s:label name="" value="%{genderEn}"/>
            </td>
        </tr>
    </s:if>
    <s:else>
        <tr height="60px">
            <td>උපන් දිනය <br>பிறந்த திகதி<br>Date of birth</td>
            <td>
                <s:label name="" value="%{#request.child.childDateOfBirthForPrint}" cssStyle="font-size:12pt;"/><br>
                <s:label value="YYYY-MM-DD" cssStyle="font-size:8pt;"/>
            </td>
            <td>ස්ත්‍රී පුරුෂ භාවය<br>பால்<br>Gender</td>
            <td>
                <s:label name="" value="%{gender}" cssStyle="font-size:11pt;"/><br/>
                <s:label name="" value="%{genderEn}"/>
            </td>
            <td colspan="2">
                දරුවා මැරී උපදින විට ගර්භයට සති කීයක් ගත වී තිබුනේද යන්න
                <br>பிள்ளை இறந்து பிறந்த பொழுது கா்ப்பத்திற்கு எத்தனை கிழமை
                <br>Number of weeks pregnant at the time of still-birth
            </td>
            <td><s:label name="" value="%{#request.child.weeksPregnant}"/></td>
        </tr>
    </s:else>
    <tr height="70px">
        <td>උපන් ස්ථානය <br>பிறந்த இடம்<br>Place of birth
        </td>
        <td colspan="2">
            <s:label name="placeOfBirth" value="%{#request.child.placeOfBirth}" cssStyle="font-size:14pt;"/><br>
            <s:label name="placeOfBirthEnglish" value="%{#request.child.placeOfBirthEnglish}"
                     cssStyle="font-size:12pt;"/>
        </td>
        <td colspan="2">මව්පියන් විවාහකද? <br>பெற்றோர் விவாகம் செய்தவர்களா?<br>Were Parents Married?
        </td>
        <td colspan="2">
            <s:label name="" value="%{marriedStatus}" cssStyle="font-size:11pt;"/><br>
            <s:label name="" value="%{marriedStatusEn}"/>
        </td>
    </tr>
    <s:if test="birthType.ordinal() != 0">
        <tr height="150px">
            <td>නම <br>பெயர்<br>Name
            </td>
            <td colspan="6" class="bc-name" style="font-size:14pt">
                <s:label name="" value="%{#request.child.childFullNameOfficialLang}"/>
            </td>
        </tr>
        <tr height="140px">
            <td>නම ඉංග්‍රීසි භාෂාවෙන් <br>ஆங்கிலத்தில் பெயர் <br> Name in English
            </td>
            <td colspan="6" class="bc-name" style="font-size:12pt">
                <s:label name="" cssStyle="text-transform: uppercase;" value="%{#request.child.childFullNameEnglish}"/>
            </td>
        </tr>
    </s:if>
    <tr height="100px">
        <td>පියාගේ සම්පුර්ණ නම<br>தந்தையின்முழுப் பெயர்<br> Father's Full Name
        </td>
        <td colspan="6" class="bc-name" style="font-size:14pt">
            <s:label name="" value="%{#request.parent.fatherFullName}"/>
        </td>
    </tr>
    <tr height="50px">
        <td>පියාගේ අනන්‍යතා අංකය<br>
            தந்தையின் அடையாள எண்<br>
            Father's Identification No.
        </td>
        <td align="center"><s:label name="" value="%{#request.parent.fatherNICorPIN}"/></td>
        <td>පියාගේ ජාතිය<br>தந்தையின் இனம்<br> Father's Race</td>
        <td colspan="4">
            <s:label name="" value="%{fatherRacePrint}" cssStyle="font-size:14pt;"/><br/>
            <s:label name="" value="%{fatherRacePrintEn}" cssStyle="font-size:12pt;"/>
        </td>
    </tr>

    <tr height="100px">
        <td>මවගේ සම්පූර්ණ නම
            <br> தாயின் முழுப் பெயர்
            <br> Mother's Full Name
        </td>
        <td colspan="6" class="bc-name" style="font-size:14pt">
            <s:label name="" value="%{#request.parent.motherFullName}"/>
        </td>
    </tr>
    <tr height="50px">
        <td>ම‌වගේ අනන්‍යතා අංකය<br>
            தாயின் அடையாள எண் <br>
            Mother's Identification No.
        </td>
        <td align="center"><s:label name="" value="%{#request.parent.motherNICorPIN}"/></td>
        <td>මවගේ ජාතිය<br>தாயின் இனம்<br> Mother's Race
        </td>
        <td colspan="4">
            <s:label name="" value="%{motherRacePrint}" cssStyle="font-size:14pt;"/><br/>
            <s:label name="" value="%{motherRacePrintEn}" cssStyle="font-size:12pt;"/>
        </td>
    </tr>

    </tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
    <col width="185px">
    <col width="230px">
    <col width="155px">
    <col width="460px">
    <tbody>
    <tr height="60px">
        <td>ලියාපදිංචි කළ දිනය<br>பதிவு செய்யப்பட்ட திகதி<br> Date of Registration</td>
        <td>
            <s:label name="" value="%{#request.register.dateOfRegistrationForPrint}"/><br>
            <s:label value="YYYY-MM-DD" cssStyle="font-size:8pt;"/>
        </td>
        <td>නිකුත් කළ දිනය<br>வழங்கிய திகதி<br> Date of Issue
        </td>
        <td>
            <%= DateTimeUtils.getISO8601FormattedString(new Date()) %><br>
            <s:label value="YYYY-MM-DD" cssStyle="font-size:8pt;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" height="100px">
            සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන<br>
            சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்<br>
            Name, Signature and Designation of certifying officer
        </td>
        <td colspan="2" style="font-size:12pt">
            <s:label value="%{#request.register.confirmantFullName}"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" height="70px">නිකුත් කළ ස්ථානය / வழங்கிய இடம்/ Place of Issue
        </td>
        <td colspan="2">
            <s:label value="%{#request.register.originalBCPlaceOfIssuePrint}" cssStyle="font-size:11pt;"/>
        </td>
    </tr>
    </tbody>
</table>

<p style="font-size:9pt">
    උප්පැන්න හා මරණ ලියපදිංචි කිරිමේ පණත (110 අධිකාරය) යටතේ රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව විසින් නිකුත් කරන
    ලදි,<br>
    பிறப்பு இறப்பு பதிவு செய்யும் சட்டத்தின்(110 ஆம் அத்தியாயத்தின்) கீழ் பதிவாளர் நாயகம் திணைக்களத்தினால் வழங்கப்பட்டது<br>
    Issued by Registrar General's Department according to Birth and Death Registration Act (Chapter 110)
</p>
<%--<br>--%>
<div style="page-break-after:always;">
</div>
<hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:30px;">

<table border="0" cellspacing="0" width="100%">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td rowspan="8" width="200px" height="350px"></td>
        <td colspan="2" width="600px" height="100px"
            style="text-align:center;margin-left:auto;margin-right:auto;font-size:16pt">
            <label>රාජ්‍ය සේවය පිණිසයි / அரச பணி / On State Service</label>
        </td>
        <td rowspan="8" width="200px"></td>
    </tr>
    <tr>
        <td>
            <s:label name="informant.informantName" cssStyle="width:600px;font-size:12pt;"/>
        </td>
    </tr>
    <tr>
        <td>
            <s:label name="informant.informantAddress" cssStyle="width:600px;font-size:12pt;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2"><p></p></td>
    </tr>
    <tr>
        <td colspan="2"><p></p></td>
    </tr>
    <tr>
        <td colspan="2"><p></p></td>
    </tr>
    <tr>
        <td colspan="2"><p></p></td>
    </tr>
    <tr>
        <td>
            Printed On : <%= DateTimeUtils.getISO8601FormattedString(new Date()) %>
        </td>

        <td style="text-align:right;margin-left:auto;margin-right:0;">
            <%--<%= DateTimeUtils.getISO8601FormattedString(new Date()) %>--%>
        </td>
    </tr>
    </tbody>
</table>
<hr style="border-style:dashed ; float:left;width:100% ;margin-top:30px;"/>
<br><br>

<s:if test="#request.allowPrintCertificate">
    <div class="form-submit" style="margin:15px 0 0 10px; ">
        <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
    </div>
    <div class="form-submit">
        <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
        <s:hidden id="printMessage" value="%{getText('print.message')}"/>
    </div>
</s:if>
<div class="form-submit" style="margin-top:15px;">
    <s:a href="%{cancel}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</div>
<%-- Styling Completed --%>
