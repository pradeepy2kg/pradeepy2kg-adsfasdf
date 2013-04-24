<%@ page import="java.util.Date" %>
<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
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
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<script type="text/javascript">
    function initPage() {
    }
</script>

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
<div cl="controls">
    <div id="adoption-page-a" class="form-submit" style="margin:15px 0 0 10px;">
        <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
    </div>
    <div class="form-submit">
        <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    </div>

    <div id="adoption-page-b" class="form-submit" style="margin:15px 0 0 10px; ">
        <s:a href="%{cancel}"><s:label value="%{getText('cancel.button')}"/></s:a>
    </div>
</div>

<table style=" width:99%; border:none; border-collapse:collapse;">
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
        <td>
            <table border="1" style="width:100%;border:1px solid #000;border-collapse:collapse; margin-top: 10px;">
                <tr height="60px">
                    <td>සහතික පත්‍රයේ අංකය <br>சான்றிதழ் இல <br>Certificate Number</td>
                </tr>
                <tr height="40px">
                    <td><s:label value="%{adoption.idUKey}"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
    </tr>
    <tr>
        <td align="center" style="font-size:16pt">
            ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA <br>
            දරුකමට ගැනීම පිලිබඳ සහතිකය <br>
            மகவேற்புச் சான்றிதழ் ﻿ <br>
            CERTIFICATE OF ADOPTION
        </td>
        <td></td>
    </tr>
    </tbody>
</table>
<table border="1" width="99%" style="margin-top:10px;float:left;border:1px solid #000; border-collapse:collapse;">
    <col width="250px">
    <col width="250px">
    <col width="110px">
    <col width="150px">
    <col>
    <tbody>
    <tr>
        <td>
            ලියාපදිංචි කිරීමේ අංකය<br>
            Registration No in ta<br>
            Registration Number
        </td>
        <td>
            <s:label value="%{adoption.adoptionEntryNo}"/>
        </td>
        <td colspan="2">
            ලියාපදිංචි කිරීමේ දිනය
            <br>Date of Registration in ta
            <br>Date of Registration
        </td>
        <td>
            <s:label value="%{adoption.orderReceivedDate}"/>
        </td>
    </tr>
    <tr>
        <td height="80px" rowspan="2">
            දරුකමට හදාගැනීමේ නියෝගය
            <br>மகவேற்புக் கட்டளை
            <br>Adoption Order
        </td>
        <td>
            නිකුත් කල අධිකරණය
            <br>வழங்கிய நீதிமன்றம்
            <br>Court Issuing order
        </td>
        <td colspan="3">
            <s:label name="courtName" cssStyle="margin-left:10px;"/>
        </td>
    </tr>
    <tr>
        <td>
            නියෝග දිනය <br>
            கட்டளை திகதி<br>
            Order Date
        </td>
        <td>
            <s:label value="%{adoption.orderIssuedDate}"/>
        </td>
        <td>
            නියෝග අංකය <br>
            கட்டளை இலக்கம் <br>
            Order Number
        </td>
        <td>
            <s:label value="%{adoption.courtOrderNumber}"/>
        </td>
    </tr>
    <tr>
        <td height="120px">
            නම
            <br>பெயர்
            <br>Name
        </td>
        <td colspan="4" class="bc-name" style="font-size:12pt">
            <s:if test="#request.adoption.childNewName != null">
                <s:label name="" value="%{adoption.childNewName}"/>
            </s:if>
            <s:else>
                <s:label name="" value="%{adoption.childExistingName}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td>උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth
        <td colspan="2"><s:label name="" value="%{adoption.childBirthDate}"/>
        </td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender
        </td>
        <td>
            <s:label name="" value="%{genderSi}"/> <br>
            <s:label name="" value="%{genderEn}"/>
        </td>
    </tr>
    <tr>
        <td height="120px">
            දරුකමට ගන්නා අයගේ හෝ අයවළුන්ගේ නම් සහ වාසගම් සහ රක්ෂාවන්<br/>
            Name and Surname and Occupation of adopter or adopters in ta <br/>
            Name and Surname and Occupation of adopter or adopters
        </td>
        <td colspan="4" class="bc-name" style="font-size:12pt">
            <s:if test="adoption.jointApplicant">
                <s:label name="adoption.applicantName"/><br/>
                <s:label name="adoption.applicantOccupation"/><br/><br/>
                <s:label name="adoption.spouseName"/><br/>
                <s:label name="adoption.spouseOccupation"/>
            </s:if>
            <s:else>
                <s:label name="adoption.applicantName"/><br/>
                <s:label name="adoption.applicantOccupation"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td height="120px">ලිපිනය<br>
            முகவரி<br>
            Address
        </td>
        <td colspan="4" style="font-size:12pt">
            <s:label name="" value="%{adoption.applicantAddress}"/><br/><br/>
            <s:label name="" value="%{adoption.applicantSecondAddress}"/>
        </td>
    </tr>
    </tbody>
</table>
<table border="1" width="99%" style="margin-top:10px;float:left;border:1px solid #000; border-collapse:collapse;">
    <col width="250px">
    <col width="215px">
    <col width="120px">
    <col>
    <col>
    <tbody>
    <tr>
        <td width="250px" height="150px">
            සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන
            <br/> சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்
            <br/>Name, Signature and Designation of certifying officer
        </td>
        <td colspan="5" style="font-size:10pt"><s:label name=""
                                                        value="%{adoption.lifeCycleInfo.approvalOrRejectUser.role.name}"/><br/>
            <s:label name="" value="%{adoption.lifeCycleInfo.approvalOrRejectUser.role.roleId}"/></td>
    </tr>
    <tr>
        <td width="250px" height="150px">
            නිකුත් කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන
            <br/>Name, Signature and Designation of issuing officer in ta
            <br/>Name, Signature and Designation of issuing officer
        </td>
        <td colspan="5" style="font-size:10pt"></td>
    </tr>
    </tbody>
</table>

<%-- Commented as mentioned that these fields are useless for the purpose. --%>
<%--<s:if test="#request.adoption.birthCertificateNumber>0 || #request.adoption.birthRegistrationSerial>0">
    <table style="font-size:12pt;text-align:center;width:99%">
        <tr></tr>
        <tr>
            <td>දැනට උප්පැන්න සහතිකයක් ඇත්නම් එහි විස්තර <br/>
                தற்பொழுது பிறப்புச் சான்றிதழ் இருக்குமாயின் அதன் விபரம் (இருக்குமாயின்)
                <br/>Birth Certificate information (if available)
            </td>
        </tr>
    </table>

    <table border="1" width="99%" style="margin-top:10px;float:left;border:1px solid #000; border-collapse:collapse;">
        <tr>
            <td width="25%">දිස්ත්‍රික්ක <br/>
                மாவட்டம் <br/>
                District
            </td>
            <td width="25%"><s:label name="" value="%{birthDistrictName}"/></td>
            <td width="25%">ප්‍රාදේශීය ලේකම් කොට්ඨාශය<br/>
                பிரதேச செயளாளர் பிரிவு <br/>
                Divisional Secretary Division
            </td>
            <td width="25%"><s:label name="" value="%{dsDivisionName}"/></td>
        </tr>
        <tr>
            <td width="25%">
                ලියාපදිංචි කිරීමේ කොට්ඨාශය<br/>
                பதிவுப் பிரிவு<br/>
                Registration Division
            </td>
            <td width="25%"><s:label name="" value="%{birthDivisionName}"/></td>
            <td>අණුක්‍රමික අංකය<br/>
                Serial Number in ta<br/>
                Serial Number
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
</s:if>--%>
<table style="width:99%; border-left:none;font-size:8pt;text-align:center;margin-bottom:10px;">
    <tr>
        <td>
            (1941 අංක 24 දරන දරුකමට ගැනීම පිලිබඳ ආඥාපනතේ 11 වෙනි වගන්තිය යටතේ නිකුත් කරන ලදී)
            <br>(1941 ஆம ஆண்டு 24 ஆம் இலக்க மகவேற்புக்கட்டளைச்சட்டத்தின் 11 ஆம் பிரிவின் கீழ் வழங்கப்பட்டது)
            <br>(Issued under Section 11 of the Adoption of Children Ordinance, No. 24 of 1941)
        </td>
    </tr>
</table>

<div style="page-break-after:always;">
</div>
<hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:30px;">

<table border="0" cellspacing="0" width="99%">
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
            <s:label value="%{adoption.certificateApplicantName}" cssStyle="width:600px;font-size:12pt;"/>
        </td>
    </tr>
    <tr>
        <td>
            <s:textarea rows="5" value="%{adoption.certificateApplicantAddress}" cssStyle="width:600px;font-size:12pt; background:#FFF; border:none; color:#000;"></s:textarea>
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
    </tbody>
</table>
<hr style="border-style:dashed ; float:left;width:100% ;margin-top:30px;"/>
<br><br>

<div cl="controls">
    <div id="adoption-page1" class="form-submit" style="margin:15px 0 0 10px;">
        <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
    </div>
    <div class="form-submit">
        <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    </div>

    <div id="adoption-page2" class="form-submit" style="margin:15px 0 0 10px; ">
        <s:a href="%{cancel}"><s:label value="%{getText('cancel.button')}"/></s:a>
    </div>
</div>
</div>

