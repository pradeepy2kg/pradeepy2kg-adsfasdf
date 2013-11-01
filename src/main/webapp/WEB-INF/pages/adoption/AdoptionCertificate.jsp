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
<s:url id="back" action="eprSearchAdoptionRecord.do">
    <s:param name="courtId" value="courtId"/>
    <s:param name="adoptionEntryNumber" value="adoptionEntryNo"/>
    <s:param name="courtOrderNo" value="courtOrderNo"/>
    <s:param name="childName" value="childName"/>
    <s:param name="childBirthDate" value="childBirthDate"/>
</s:url>
<div id="adoption-certificate-form-outer">

<div cl="controls">
<%--    <s:if test="adoption.status != 'ARCHIVED_ALTERED'">--%>
<s:if test ="adoption.lifeCycleInfo.activeRecord" >
        <div id="adoption-page-a" class="form-submit" style="margin:15px 0 0 10px;">
            <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
        </div>
        <div class="form-submit">
            <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
        </div>

        <div id="adoption-page-b" class="form-submit" style="margin:15px 0 0 10px; ">
            <s:a href="%{cancel}"><s:label value="%{getText('cancel.button')}"/></s:a>
        </div>
    </s:if>
    <s:else>
        <div id="adoption-page-b" class="form-submit" style="margin:15px 0 0 10px; ">
            <s:a href="%{back}"><s:label value="%{getText('back.label')}"/></s:a>
        </div>
    </s:else>
</div>


<table style=" width:99%; border: none; border-collapse:collapse;">
    <col width="300px">
    <col width="400px">
    <col width="340px">
    <tbody>
    <tr>
        <td></td>
        <td rowspan="2" align="center">
            <img src="<s:url value="../images/official-logo.png" />"
                 style="display: block; text-align: center;" width="100" height="120">
        </td>
        <td></td>
    </tr>
    <tr>
        <td rowspan="3">
            <img src="${pageContext.request.contextPath}/prs/ImageServlet?personUKey=${adoption.idUKey}&certificateType=adoption"
                 width="100" height="100"/>
        </td>
        <td rowspan="3">
            <table style="width:50%;border-collapse:collapse;float:right;">
                <tr height="60px">
                    <td width="100%" colspan="3" style="border: 1px solid #000;">සහතික පත්‍රයේ අංකය <br>சான்றிதழ் இல
                        <br>Certificate Number
                    </td>
                </tr>
                <tr height="40px">
                    <td align="center" colspan="3" style="border: 1px solid #000;"><s:label
                            value="%{adoption.idUKey}"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td rowspan="2" align="center" style="font-size:16pt">
            ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA <br>
            දරුකමට ගැනීම පිලිබඳ සහතිකය <br>
            மகவேற்புச் சான்றிதழ் ﻿ <br>
            CERTIFICATE OF ADOPTION
        </td>
        <td></td>
    </tr>
    </tbody>
</table>
<s:if test="changedFields.size() > 0">
    <s:label class="font-7" value="මුල් ලියාපදිංචියෙන් පසු වෙනස්කම්  ** ලකුණින් පෙන්වා ඈත            "/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <s:label class="font-7" value="முதல் பதிவின் பின் நிறைவேற்றிய மாற்றங்கள்**குறியீட்டில் குறிப்பிடப்பட்டுள்ளது "/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <s:label class="font-7" value="Changes After First Registration  marked with ** "/>
</s:if>

<%--
<s:if test="changedFields.size() > 0">
    <s:if test="adoption.languageToTransliterate == \"si\"">
        <s:label value="මුල් ලියාපදිංචියෙන් පසු වෙනස්කම්  "/> <label class="font-9">**</label> ලකුණින් පෙන්වා ඈත.
    </s:if><s:elseif test="adoption.languageToTransliterate == \"ta\"">
    <s:label value="முதல் பதிவின் பின் நிறைவேற்றிய மாற்றங்கள்"/> <label class="font-9">**</label> குறியீட்டில் குறிப்பிடப்பட்டுள்ளது .
</s:elseif><s:elseif test="adoption.languageToTransliterate == \"en\"">
    <s:label value="<br>Changes After First Registration "/> marked with <label class="font-9">**</label>
</s:elseif>
</s:if>
--%>


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
            <%--Registration No in ta<br>--%>
            பதிவிலக்கம்<br>
            Registration Number
        </td>
        <td>
            <s:label value="%{adoption.adoptionEntryNo}"/>
        </td>
        <td colspan="2">
            ලියාපදිංචි කිරීමේ දිනය
            <%--<br>Date of Registration in ta--%>
            <br> பதிவுத்திகதி
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
                <div class="changes-done">
                    <s:if test="changedFields.get(0)">
                        **&nbsp;<br/>&nbsp;
                    </s:if>
                </div>
                <s:label name="" value="%{adoption.childNewName}"/>
            </s:if>
            <s:else>
                <div class="changes-done">
                    <s:if test="changedFields.get(0)">
                        **&nbsp;<br/>&nbsp;
                    </s:if>
                </div>
                <s:label name="" value="%{adoption.childExistingName}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td>උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth
        <td colspan="2">
            <div class="changes-done">
                <s:if test="changedFields.get(2)">
                    **&nbsp;<br/>&nbsp;
                </s:if>
            </div>
            <s:label name="" value="%{adoption.childBirthDate}"/>
        </td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender
        </td>
        <td>
            <div class="changes-done">
                <s:if test="changedFields.get(1)">
                    **&nbsp;<br/>&nbsp;
                </s:if>
            </div>
            <s:if test="#request.adoption.childGender == 0">
                <s:label name="" value="%{getText('male.label')}"/>
            </s:if>
            <s:elseif test="#request.adoption.childGender == 1">
                <s:label name="" value="%{getText('female.label')}"/>
            </s:elseif>
            <s:elseif test="#request.adoption.childGender == 2">
                <s:label name="" value="%{getText('unknown.label')}"/>
            </s:elseif>
        </td>
    </tr>
    <tr>
        <td height="120px">
            දරුකමට ගන්නා අයගේ හෝ අයවළුන්ගේ නම් සහ වාසගම් සහ රක්ෂාවන්<br/>
            <%--Name and Surname and Occupation of adopter or adopters in ta <br/>--%>
            மகவேற்பாளரின் அல்லது மகவேற்பாளர்களின் பெயர் மற்றும் முதலெழுத்துக்கள் குறிக்கும் பெயர் மற்றும் தொழில்<br/>
            Name and Surname and Occupation of adopter or adopters
        </td>
        <td colspan="4" class="bc-name" style="font-size:12pt">

            <s:if test="adoption.jointApplicant">
                <div class="changes-done">
                    <s:if test="changedFields.get(3)||changedFields.get(6)||changedFields.get(7)||changedFields.get(8)">
                        **&nbsp;<br/>&nbsp;
                    </s:if>
                </div>
                <s:label name="adoption.applicantName"/><br/>
                <s:label name="adoption.applicantOccupation"/><br/><br/>
                <s:label name="adoption.spouseName"/><br/>
                <s:label name="adoption.spouseOccupation"/>
            </s:if>
            <s:else>
                <div class="changes-done">
                    <s:if test="changedFields.get(3)||changedFields.get(6)">
                        **&nbsp;<br/>&nbsp;
                    </s:if>
                </div>
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
            <div class="changes-done">
                <s:if test="changedFields.get(4)||changedFields.get(5)">
                    **&nbsp;<br/>&nbsp;
                </s:if>
            </div>
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
        <%--        <td colspan="5" style="font-size:10pt"><s:label name=""
                                                  value="%{adoption.lifeCycleInfo.approvalOrRejectUser.role.name}"/><br/>
      <s:label name="" value="%{adoption.lifeCycleInfo.approvalOrRejectUser.role.roleId}"/></td>--%>
        <td colspan="5" style="font-size:10pt">
            <s:if test="adoption.languageToTransliterate == \"si\"">
                <s:label value="අත්. "/>
            </s:if><s:elseif test="adoption.languageToTransliterate == \"en\"">
            <s:label value="sgd. "/>
        </s:elseif><s:elseif test="adoption.languageToTransliterate == \"ta\"">
            <s:label value="sgd in ta. "/>
        </s:elseif>

            <s:label value="%{oderApprovedUser}"/><br/>
            <s:if test="adoption.languageToTransliterate == \"si\"">
                <s:label value="නියෝජ්‍ය  /  සහකාර රෙජිස්ට්‍රාර් ජනරාල්"/><br><br>
            </s:if><s:elseif test="adoption.languageToTransliterate == \"en\"">
            <s:label value="Deputy / Assitant Registrar Genaral"/><br><br>
        </s:elseif><s:elseif test="adoption.languageToTransliterate == \"ta\"">
            <s:label value="Deputy / Assitant Registrar Genaral in ta"/><br><br>
        </s:elseif>
        </td>
    </tr>
    <tr>
        <td width="250px" height="150px">
            නිකුත් කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන
            <%--<br/>Name, Signature and Designation of issuing officer in ta--%>
            <br/>அலுவலரின் பெயர், ஒப்பம் மற்றும் பதவி
            <br/>Name, Signature and Designation of issuing officer
            <br/>
            <br/>
            <table>
                <tr>
                    <td width="200px"> නිකුත් කළ දිනය</td>
                </tr>
                <tr>
                    <td> Date of Issue in Ta</td>
                    <td><s:label name="" value="%{certificateIssuedDate}"/></td>
                </tr>
                <tr>
                    <td>Date of Issue</td>
                </tr>
            </table>
        </td>
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

 <s:label name="" value="%{adoption.adoptionSerialNo}"/> / <s:label value="%{applicationEnteredDate}"/>

<div style="page-break-after:always;">
</div>
<hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:30px;">

<table border="0" cellspacing="0" width="100%">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td rowspan="7" width="10%" height="350px"></td>
        <td colspan="3" width="80%" height="100px"
            style="text-align:center;margin-left:auto;margin-right:auto;font-size:22pt">
            <label>රාජ්‍ය සේවය පිණිසයි &nbsp;&nbsp;அரச பணி &nbsp;&nbsp;ON STATE SERVICE</label><br/>
            <label style="font-size:11pt;">රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව &nbsp;&nbsp;பதிவாளர் நாயகம் திணைக்களம்
                &nbsp;&nbsp;REGISTRAR GENERAL'S DEPARTMENT</label>
        </td>
        <td rowspan="7" width="10%"></td>
    </tr>
    <tr>
        <td height="50px" width="30%">
            <s:textarea id="retAddress"
                        value="රෙජිස්ට්‍රාර් ජෙනරාල් දෙපාර්තමේන්තුව, 234 / A 3 ,             ඩෙන්සිල් කොබ්බෑකඩුව මාවත, බත්තරමුල්ල"
                        disabled="true" rows="5"
                        cssStyle="margin-top:10px;text-transform:none;width:350px;resize:none;font-size:14pt;background:transparent;border:none;"/>
        </td>
        <td width="10%">&nbsp;</td>
        <td width="30%">
            <s:label name="adoption.certificateApplicantName" cssStyle="width:600px;font-size:14pt;"/><br/>
            <s:textarea name="adoption.certificateApplicantAddress"
                        cssStyle="resize: none; color: #000; border: none; width: 300px; font-size:14pt;" rows="4"
                        disabled="true"/>
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
<%--<s:if test="adoption.status != 'ARCHIVED_ALTERED'">--%>
<s:if test ="adoption.lifeCycleInfo.activeRecord" >
    <div id="adoption-page-a" class="form-submit" style="margin:15px 0 0 10px;">
        <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
    </div>
    <div class="form-submit">
        <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    </div>

    <div id="adoption-page-b" class="form-submit" style="margin:15px 0 0 10px; ">
        <s:a href="%{cancel}"><s:label value="%{getText('cancel.button')}"/></s:a>
    </div>
</s:if>
<s:else>
    <div id="adoption-page-b" class="form-submit" style="margin:15px 0 0 10px; ">
        <s:a href="%{back}"><s:label value="%{getText('back.label')}"/></s:a>
    </div>
</s:else>
<%--
<s:property value="adoptioon.status"/>--%>
</div>

