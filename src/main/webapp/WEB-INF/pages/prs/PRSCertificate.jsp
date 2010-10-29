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
            font-size: 9pt;
        }
    }

    #birth-certificate-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>

<div id="birth-certificate-outer">

<div class="form-submit" style="margin:5px 0 0 0;">
    <s:submit value="%{getText('mark_as_print.button')}" type="submit"/>
</div>
<div class="form-submit" style="margin:15px 0 0 5px;">
    <s:a href="%{printPage}" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>
</div>
<div id="birthRegistration-page" class="form-submit" style="margin-top:15px;float:right;">
    <s:a href="%{cancel}"><s:label value="%{getText('cancel.button')}"/></s:a>
</div>

<table style="width:100%; border:none; border-collapse:collapse;">
    <col width="300px">
    <col width="430px">
    <col width="300px">
    <tbody>
    <tr>
        <td></td>
        <td rowspan="2" align="center">
            <img src="<s:url value="../images/official-logo.png" />"
                 style="display: block; text-align: center;" width="100" height="120">
        </td>
        <td>
            <table border="1" style="width:100%;border:1px solid #000;border-collapse:collapse;font-size:10pt;">
                <tr height="60px">
                    <td>
                        අනුක්‍රමික අංකය
                        <br>தொடர் இலக்கம்
                        <br>Serial Number
                    </td>
                    <td width="150px"><s:label name="personId"/></td>
                </tr>
            </table>
        </td>

    </tr>
    <tr></tr>
    <tr>
        <td align="center" style="font-size:12pt;" colspan="3">
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
       style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
    <col width="150px">
    <col width="100px">
    <col width="150px">
    <col width="150px">
    <col width="100px">
    <col width="150px">
    <tbody>
    <tr>
        <td>
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification Number
        </td>
        <td colspan="2">
            <s:label value="%{person.pin}"/>
        </td>
        <td>
            උපන් දිනය
            <br>பிறந்த திகதி
            <br>Date of birth
        </td>
        <td colspan="2">
            <s:label value="%{person.dateOfBirth}"/><br>
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/>
        </td>
    </tr>
    <tr>
        <td>
            ස්ත්‍රී පුරුෂ භාවය
            <br>பால்
            <br>Gender
        </td>
        <td colspan="2">
            <s:label name="" value="%{gender}"/> <br>
            <s:label name="" value="%{genderEn}"/>
        </td>
        <td>
            ජාතිය
            <br>இனம்
            <br>Race
        </td>
        <td colspan="2">
            <s:label value="%{race}"/> <br>
            <s:label value="%{raceEn}"/>
        </td>
    </tr>
    <tr>
        <td>
            උපන් ස්ථානය
            <br>பிறந்த இடம்
            <br>Place of Birth
        </td>
        <td colspan="5">
            <s:label value="%{person.placeOfBirth}"/>
        </td>
    </tr>
    <tr>
        <td>
            නම
            <br>பெயர்
            <br>Name
        </td>
        <td colspan="5">
            <s:label value="%{person.fullNameInOfficialLanguage}"/>
        </td>
    </tr>
    <tr>
        <td>
            නම ඉංග්‍රීසි භාෂාවෙන්
            <br>ஆங்கிலத்தில் பெயர்
            <br>Name in English
        </td>
        <td colspan="5">
            <s:label value="%{person.fullNameInEnglishLanguage}"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            මවගේ අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය
            <br>தாயின் அடையாள எண் அல்லது தேசிய அடையாள அட்டை இலக்கம்
            <br>Mothers Identification Number (PIN) or NIC
        </td>
        <td></td>
        <td colspan="2">
            පියාගේ අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය
            <br>தந்தையின் அடையாள எண் அல்லது தேசிய அடையாள அட்டை இலக்கம்
            <br>Fathers Identification Number (PIN) or NIC
        </td>
        <td></td>
    </tr>
    </tbody>
</table>
<table border="1"
       style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
    <col width="200px">
    <col width="300px">
    <col width="100px">
    <col>
    <tbody>
    <tr>
        <td height="120px">
            ස්ථිර ලිපිනය
            <br>நிரந்தர வதிவிட முகவரி
            <br>Permanent Address
        </td>
        <td colspan="3" height="40px">
            <s:label value="%{permanentAddress.Line1}"/>
            <br><s:label value="%{permanentAddress.Line2}"/>
            <br><s:label value="%{permanentAddress.City}"/>
        </td>
    </tr>
    <tr>
        <td colspan="4" height="40px">
            වර්තමාන පදිංචිය වෙනත් ස්ථානයක නම් පමණක්, தற்போதைய முகவரி வேறு இடமாயின் மட்டும் , Only if residing at a
            different location,
        </td>
    </tr>
    <tr>
        <td height="120px">
            වර්තමාන ලිපිනය
            <br>தற்போதைய வதிவிட முகவரி
            <br>Current Address
        </td>
        <td colspan="3" height="40px">
            <s:label value="%{person.LastAddress.Line1}"/>
            <br><s:label value="%{person.LastAddress.Line2}"/>
            <br><s:label value="%{person.LastAddress.City}"/>
        </td>
    </tr>
    <tr>
        <td>
            දුරකථන අංක
            <br>தொலைபேசி இலக்கம்
            <br>Telephone Numbers
        </td>
        <td>
            <s:label value="%{person.personPhoneNo}"/>
        </td>
        <td>
            ඉ – තැපැල්
            <br>மின்னஞ்சல்
            <br>Email
        </td>
        <td>
            <s:label value="%{person.personEmail}"/>
        </td>
    </tr>

    </tbody>
</table>

<s:if test="numberOfCountries != 0">
    <table style="width:100%; border:none; border-collapse:collapse;">
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
        <col width="180px">
        <col width="200px">
        <col width="350px">
        <col width="200px">
        <tbody>
        <s:iterator value="personCitizenship">
            <tr>
                <td height="40px">
                    රට / நாடு /Country
                </td>
                <td><s:property value="country.enCountryName"/></td>
                <td>
                    ගමන් බලපත්‍ර අංකය / கடவுச் சீட்டு இல. /Passport No.
                </td>
                <td><s:property value="passportNo"/></td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</s:if>

<div class="form-submit" style="margin:5px 0 0 0;">
    <s:submit value="%{getText('mark_as_print.button')}" type="submit"/>
</div>
<div class="form-submit" style="margin:15px 0 0 5px;">
    <s:a href="%{printPage}" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>
</div>
<div id="birthRegistration-page" class="form-submit" style="margin-top:15px;float:right;">
    <s:a href="%{cancel}"><s:label value="%{getText('cancel.button')}"/></s:a>
</div>
</div>
