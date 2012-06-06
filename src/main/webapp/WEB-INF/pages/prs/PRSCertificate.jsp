<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<style type="text/css">
    #birth-certificate-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }

        td {
            font-size: 10pt;
        }
    }

    #birth-certificate-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>
<script type="text/javascript">
    function initPage() {
    }
</script>

<s:if test="direct">
    <s:if test="%{person.status.ordinal() == 2}">
        <s:url id="markPrint" action="eprDirectMarkPRSCertificate.do" namespace="../prs"/>
    </s:if>
    <s:else>
        <s:url id="markPrint" action="eprBackRegisterDetails.do" namespace="../prs"/>
    </s:else>
    <s:url id="previous" action="eprBackRegisterDetails.do" namespace="../prs"/>
</s:if>
<s:else>
    <s:if test="%{person.status.ordinal() == 2}">
        <s:url id="markPrint" action="eprPersonDetails.do" namespace="../prs"/>
        <%--<s:url id="markPrint" action="eprMarkPRSCertificate.do" namespace="../prs"/>--%>
    </s:if>
    <s:else>
        <s:url id="markPrint" action="eprPersonDetails.do" namespace="../prs"/>
        <%--<s:url id="markPrint" action="eprBackPRSSearchList.do" namespace="../prs"/>--%>
    </s:else>
    <s:url id="previous" action="eprPersonDetails.do" namespace="../prs">
        <s:param name="personUKey" value="%{#request.personUKey}"/>
        <%--<s:param name="pageNo" value="%{#request.pageNo}"/>--%>
        <%--<s:param name="locationId" value="%{#request.locationId}"/>--%>
        <%--<s:param name="printStart" value="%{#request.printStart}"/>--%>
    </s:url>
</s:else>

<div id="birth-certificate-outer">

<s:form action="%{markPrint}" method="POST">

<div class="form-submit" style="margin:5px 0 0 0;">
    <s:submit value="%{getText('mark_as_print.button')}" type="submit"/>
</div>
<div class="form-submit" style="margin:15px 0 0 5px;">
    <s:a href="%{printPage}" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>
</div>
<div class="form-submit" style="margin-top:15px;float:right;">
    <s:a href="%{previous}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>

<s:hidden name="personUKey" value="%{person.personUKey}"/>
<s:hidden name="pageNo" value="%{#request.pageNo}"/>
<s:hidden name="locationId" value="%{#request.locationId}"/>
<s:hidden name="printStart" value="%{#request.printStart}"/>

<br/>

<table style="width:99%; border:none;border-collapse:collapse;margin-top:20px;">
    <col width="300px">
    <col width="430px">
    <col width="300px">
    <tbody>
    <tr>
        <td>
            <img src="${pageContext.request.contextPath}/prs/ImageServlet?personUKey=${person.personUKey}" width="100"
                 height="100"/>
        </td>
        <td rowspan="2" align="center">
            <img src="<s:url value="../images/official-logo.png" />"
                 style="display: block; text-align: center;" width="100" height="120">
        </td>
        <td>
            <table border="1" style="width:100%;border:1px solid #000;border-collapse:collapse;font-size:10pt;">
                <tr height="60px">
                    <td width="140px;">
                        සහතික පත්‍රයේ අංකය<br>சான்றிதழ் இல<br>Certificate Number
                    </td>
                    <td width="150px" style="font-size:11pt"><s:label name="personUKey"/></td>
                </tr>
            </table>
        </td>

    </tr>
    <tr></tr>
    <tr>
        <td align="center" style="font-size:14pt;" colspan="3">
            <s:label>
                ජනගහන ලේඛනයේ පුද්ගලයකු ලියාපදිංචි කිරීමේ සහතික පත්‍රය
                <br> குடிமதிப்பீட்டு ஆவணத்தின் ஆட் பதிவிற்கான சான்றிதழ்
                <br>Certificate issued for the registration of a person in the Population Registry
            </s:label>
        </td>
    </tr>
    </tbody>
</table>

<table border="1"
       style="width: 99%; border:1px solid #000; border-collapse:collapse; margin:5px 0;font-size:10pt">
    <col width="20%"/>
    <col width="20%"/>
    <col width="10%"/>
    <col width="20%"/>
    <col width="10%"/>
    <col width="20%"/>

    <tbody>
    <tr>
        <td height="60px">
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification Number
        </td>
        <td colspan="2" style="font-size:12pt">
            <s:label value="%{person.pin}"/>
        </td>
        <td>
            උපන් දිනය
            <br>பிறந்த திகதி
            <br>Date of Birth
        </td>
        <td colspan="2" style="font-size:12pt">
            <s:label value="%{person.dateOfBirth}"/><br>
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/>
        </td>
    </tr>
    <tr>
        <td height="60px">
            ස්ත්‍රී පුරුෂ භාවය
            <br>பால்
            <br>Gender
        </td>
        <td colspan="2" style="font-size:12pt">
            <s:label name="" value="%{gender}"/> <br>
            <s:label name="" value="%{genderEn}"/>
        </td>
        <td>
            ජන වර්ගය<br/>
            இனம்<br/>
            Ethnic Group
        </td>
        <td colspan="2" style="font-size:12pt">
            <s:label value="%{race}"/> <br>
            <s:label value="%{raceEn}"/>
        </td>
    </tr>
    <tr>
        <td height="60px">
            උපන් ස්ථානය
            <br>பிறந்த இடம்
            <br>Place of Birth
        </td>
        <td colspan="5" style="font-size:12pt">
            <s:label value="%{person.placeOfBirth}"/>
        </td>
    </tr>
    <tr>
        <td height="110px">
            නම
            <br>பெயர்
            <br>Name
        </td>
        <td colspan="5" style="font-size:12pt">
            <s:label value="%{person.fullNameInOfficialLanguage}"/>
        </td>
    </tr>
    <tr>
        <td height="110px">
            නම ඉංග්‍රීසි භාෂාවෙන්
            <br>ஆங்கிலத்தில் பெயர்
            <br>Name in English
        </td>
        <td colspan="5" style="font-size:11pt">
            <s:label value="%{person.fullNameInEnglishLanguage}"/>
        </td>
    </tr>
    <tr>
        <td height="60px">
            ම‌වගේ අනන්‍යතා අංකය<br>
            தாயின் அடையாள எண் <br>
            Mother's Identification No.
        </td>
        <td colspan="2" style="font-size:12pt">
            <s:label value="%{person.motherPINorNIC}"/>
        </td>
        <td>
            පියාගේ අනන්‍යතා අංකය<br>
            தந்தையின் அடையாள எண்<br>
            Father's Identification No.
        </td>
        <td colspan="2" style="font-size:12pt">
            <s:label value="%{person.fatherPINorNIC}"/>
        </td>
    </tr>
    </tbody>
</table>
<table border="1"
       style="width: 99%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
    <col width="20%"/>
    <col width="30%"/>
    <col width="20%"/>
    <col width="30%"/>
    <tbody>
    <tr>
        <td height="120px">
            ස්ථිර ලිපිනය
            <br>நிரந்தர வதிவிட முகவரி
            <br>Permanent Address
        </td>
        <td colspan="3" height="40px" style="font-size:12pt">
            <s:label value="%{permanentAddress.Line1}"/>
            <br><s:label value="%{permanentAddress.Line2}"/>
            <br><s:label value="%{permanentAddress.City}"/>
        </td>
    </tr>
    <tr>
        <td colspan="4" height="40px">
            වර්තමාන පදිංචිය වෙනත් ස්ථානයක නම් පමණක්, தற்போதைய முகவரி வேறு இடமாயின் மட்டும், Only if residing at a different location,
        </td>
    </tr>
    <tr>
        <td height="120px">
            වර්තමාන ලිපිනය
            <br>தற்போதைய வதிவிட முகவரி
            <br>Current Address
        </td>
        <td colspan="3" height="40px" style="font-size:12pt">
            <s:label value="%{person.LastAddress.Line1}"/>
            <br><s:label value="%{person.LastAddress.Line2}"/>
            <br><s:label value="%{person.LastAddress.City}"/>
        </td>
    </tr>
    <tr>
        <td height="60px">
            දුරකථන අංක
            <br>தொலைபேசி இலக்கம்
            <br>Telephone Numbers
        </td>
        <td style="font-size:12pt">
            <s:label value="%{person.personPhoneNo}"/>
        </td>
        <td>
            ඉ – තැපැල්
            <br>மின்னஞ்சல்
            <br>Email
        </td>
        <td style="font-size:12pt">
            <s:label value="%{person.personEmail}"/>
        </td>
    </tr>
    </tbody>
</table>

<s:if test="person.countries.size() != 0">
    <table style="width:99%; border:none; border-collapse:collapse;margin-bottom:2px;">
        <tr>
            <td align="center" style="font-size:12pt;">
                වෙනත් රටවල පුරවැසිභාවය ඇතිනම් ඒ පිලිබඳ විස්තර
                <br>வேறு நாடுகளில் பிரஜாவுரிமை இருந்தால் அது பற்றிய தகவல்கள்
                <br>If a citizen of any other countries, such details
            </td>
        </tr>
    </table>
    <table border="1"
           style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
        <col width="20%"/>
        <col width="23%"/>
        <col width="34%"/>
        <col width="23%"/>
        <tbody>
        <s:iterator value="person.countries">
            <tr>
                <td height="40px">
                    රට / நாடு /Country
                </td>
                <td style="font-size:12pt">
                    <s:if test="person.preferredLanguage=='si'">
                        <s:property value="country.siCountryName"/>
                    </s:if>
                    <s:if test="person.preferredLanguage=='ta'">
                        <s:property value="country.taCountryName"/>
                    </s:if>
                </td>
                <td>
                    ගමන් බලපත්‍ර අංකය / கடவுச் சீட்டு இல. /Passport No.
                </td>
                <td style="font-size:12pt"><s:property value="passportNo"/></td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</s:if>

<table border="1" style="width: 99%; border:1px solid #000; border-collapse:collapse; margin:5px 0;font-size:10pt">
    <col width="20%"/>
    <col width="20%"/>
    <col width="20%"/>
    <col width="40%"/>
    <tbody>
    <tr height="60px">
        <td>ලියාපදිංචි කළ දිනය<br>பதிவு செய்யப்பட்ட திகதி<br> Date of Registration</td>
        <td>
            <s:label name="" value="%{person.dateOfRegistration}" cssStyle="font-size:12pt;"/><br>
            <s:label value="YYYY-MM-DD" cssStyle="font-size:8pt;"/>
        </td>
        <td>නිකුත් කළ දිනය<br>வழங்கிய திகதி<br> Date of Issue
        </td>
        <td>
            <label style="font-size:12pt;"><%= DateTimeUtils.getISO8601FormattedString(new Date()) %>
            </label><br>
            <s:label value="YYYY-MM-DD" cssStyle="font-size:8pt;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" height="85px">
            සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන<br>
            சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்<br>
            Name, Signature and Designation of certifying officer
        </td>
        <td colspan="2" style="font-size:11pt">
            <s:textarea id="signature" value="%{person.originalBCIssueUserSignPrint}" disabled="true"
                        rows="4"
                        cssStyle="margin-top:10px;text-transform:none;width:100%;font-size:10pt;background:transparent;border:none;padding:0;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" height="40px">නිකුත් කළ ස්ථානය / வழங்கிய இடம்/ Place of Issue
        </td>
        <td colspan="2" cssStyle="font-size:11pt;">
            <s:textarea id="placeSign" value="%{#request.register.originalBCPlaceOfIssueSignPrint}" disabled="true"
                        rows="3"
                        cssStyle="margin-top:10px;text-transform:none;width:100%;font-size:10pt;background:transparent;border:none;padding:0;"/>
        </td>
    </tr>
    </tbody>
</table>

<p style="font-size:9pt">
    <%--උප්පැන්න හා මරණ ලියපදිංචි කිරිමේ පණත (110 අධිකාරය) යටතේ රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව විසින් නිකුත් කරන
    ලදි,<br>
    பிறப்பு இறப்பு பதிவு செய்யும் சட்டத்தின் (110 வது அதிகாரத்தின் ) கீழ் பதிவாளர் நாயகத் திணைக்களத்தினால் வழங்கப்பட்டது<br>
    Issued by Registrar General's Department according to Birth and Death Registration Act (Chapter 110)--%>
</p>

<div class="form-submit" style="margin:5px 0 0 0;">
    <s:submit value="%{getText('mark_as_print.button')}" type="submit"/>
</div>
<div class="form-submit" style="margin:15px 0 0 5px;">
    <s:a href="%{printPage}" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>
</div>
<div class="form-submit" style="margin-top:15px;float:right;">
    <s:a href="%{previous}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</s:form>
<br/><br/>&nbsp;
</div>