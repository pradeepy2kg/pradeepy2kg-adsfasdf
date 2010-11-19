<%--@author amith jayasekara--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="marriage-notice-outer">
<%--section for official usage--%>
<table class="table_reg_header_01">
    <caption></caption>
    <col width="360px"/>
    <col width="270px"/>
    <col/>
    <tbody>
    <tr style="font-size:9pt">
        <td colspan="1"></td>
        <td align="center" style="font-size:12pt;"><img src="<s:url value="/images/official-logo.png"/>"
        </td>
        <td>
            <table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;"
                   cellpadding="2px">
                <caption/>
                <col width="175px">
                <col>
                <tr>
                    <td colspan="2">
                        කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>அலுவலக பாவனைக்காக மட்டும்
                        <br>For office use only
                    </td>
                </tr>
                <tr>
                    <td><label><span class="font-8">අනුක්‍රමික අංකය
                            <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                            <br>தொடர் இலக்கம்<br>Serial Number</span></label>
                    </td>
                    <td align="center">
                        <s:if test="false==true">
                            <%-- for edit mode of the marriage notice--%>
                            <s:textfield name="#" id="mnSerial" readonly="true" maxLength="10"
                                         cssStyle="margin-left:20px"/>
                        </s:if>
                        <s:else>
                            <s:textfield name="#" id="mnSerial" maxLength="10"/>
                        </s:else>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>
                                <span class="font-8">භාරගත්  දිනය
                                    <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                                    <br>பிறப்பைப் பதிவு திகதி <br>Submitted Date</span>
                        </label>
                    </td>
                    <td>
                        <s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
                        <s:textfield name="register.dateOfRegistration" id="submitDatePicker" maxLength="10"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
    <tr style="font-size:14pt">
        <td colspan="1"></td>
        <td colspan="1" align="center">
            විවාහ දැන්වීම <br>
            குடிமதிப்பீட்டு ஆவணத்தில் <br>
            Notice of Marriage
        </td>
        <td></td>
    </tr>
    </tbody>
</table>
<br>

<%--type pf marriage--%><%--
<table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="2px">
    <caption/>
    <col width="230px"/>
    <col width="200px"/>
    <col/>
    <col width="200px"/>
    <col/>
    <col width="200px"/>
    <col/>
    <tbody>
    <tr>
        <td>
            විවාහයේ ස්වභාවය <br>
            type of marriage in tamil <br>
            Type of Marriage
        </td>
        <td>
            සාමාන්‍ය <br>
            general marriage in tamil <br>
            General
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
        <td>
            උඩරට බින්න <br>
            Kandyan binna in tamil <br>
            Kandyan Binna
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
        <td>
            උඩරට බින්න දීග <br>
            kandyan deega in tamil <br>
            Kandyan
            Deega
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
    </tr>
    </tbody>
</table>--%>

<table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="2px">
    <caption></caption>
    <col width="250px"/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td rowspan="3">
            විවාහයේ ස්වභාවය <br>
            type of marriage in tamil <br>
            Type of Marriage
        </td>
        <td colspan="6">
            සාමාන්‍ය /
            general marriage in tamil /
            General
        </td>
        <td colspan="2" align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
    </tr>
    <tr>
        <td colspan="6">
            උඩරට බින්න /
            Kandyan binna in tamil /
            Kandyan Binna
        </td>
        <td colspan="2" align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
    </tr>
    <tr>
        <td colspan="6">
            උඩරට බින්න දීග /
            kandyan deega in tamil /
            Kandyan
            Deega
        </td>
        <td colspan="2" align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
    </tr>
    <tr>
        <td>
            විවාහය සිදුකරන ස්ථානය <br>
            Intended place of Marriage <br>
        </td>
        <td>
            රෙජිස්ට්‍රාර් කන්තෝරුව <br>
            Registrars Office <br>
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
        <td>
            ප්‍රා. ලේ. කන්තෝරුව <br>
            DS Office <br>
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
        <td>
            දේවස්ථානය <br>
            Church <br>
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
        <td>වෙනත් <br>
            Other
        </td>
        <br>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
    </tr>
    </tbody>
</table>

<%--section heading male party heading--%>
<table style="margin-top:20px;margin-bottom:20px;width:100%;font-size:16px">
    <caption/>
    <tbody>
    <tr>
        <td align="center">
            පුරුෂ පාර්ශ්වය / in tamil / Male Party
        </td>
    </tr>
    </tbody>
</table>

<%--section male party--%>
<table border="2"
       style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px;border-bottom:none"
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="265px"/>
    <col width="250px"/>
    <col/>
    <tbody>
    <tr>
        <td colspan="1" rowspan="2">
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification Number.
        </td>
        <td colspan="1" rowspan="2">
            dddd
        </td>
        <td colspan="1">
            උපන් දිනය <br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td colspan="1">dddd</td>
    </tr>
    <tr>
        <td colspan="1">
            පසුවූ උපන් දිනයට වයස <br>
            in tamil <br>
            Age at last Birthday

        </td>
        <td colspan="1">
            xxxx
        </td>
    </tr>

    <tr>
        <td>
            නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <br>
            பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="3"></td>
    </tr>

    <tr>
        <td>
            නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பெயர் ஆங்கில மொழியில் <br>
            Name in English
        </td>
        <td colspan="3"></td>
    </tr>

    <tr>
        <td>
            පදිංචි ලිපිනය <br>
            தற்போதைய வதிவிட முகவரி <br>
            Resident Address
        </td>
        <td colspan="3"></td>
    </tr>

    <tr>
        <td>
            දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td>aaa</td>
        <td>
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரதேச செயளாளர் பிரிவு <br>
            Divisional Secretariat
        </td>
        <td>ddd</td>
    </tr>
    <tr>
        <td>
            ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
            பதிவுப் பிரிவு <br>
            Registration Division
        </td>
        <td>aaa</td>
        <td>
            පදිංචි කාලය <br>
            தற்போதைய <br>
            Duration
        </td>
        <td>ddd</td>
    </tr>
    <tr>
        <td>
            දුරකථන අංක <br>
            தொலைபேசி இலக்கம் <br>
            Telephone Numbers
        </td>
        <td>aaa</td>
        <td>
            ඉ – තැපැල් <br>
            மின்னஞ்சல் <br>
            Email
        </td>
        <td>ddd</td>
    </tr>
    </tbody>
</table>
<table border="2" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="143px"/>
    <col width="50px"/>
    <col width="142px"/>
    <col width="50px"/>
    <col width="143px"/>
    <col width="50px"/>
    <col width="150px"/>
    <col/>
    <tbody>
    <tr>
        <td>
            සිවිල් තත්වය<br>
            சிவில் நிலைமை <br>
            Civil Status
        </td>
        <td>
            අවිවාහක <br>
            திருமணமாகாதவர் <br>
            Never Married

        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
        <td>
            දික්කසාද <br>
            திருமணம் தள்ளுபடி செய்தவர் <br>
            Divorced
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
        <td>
            වැන්දබු <br>
            விதவை <br>
            Widowed
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
        <td>
            නිෂ්ප්‍රභාකර ඇත <br>
            தள்ளிவைத்தல் <br>
            Anulled
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
    </tr>

    <tr>
        <td colspan="2">
            පියාගේ අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය <br>
            தந்தையின் அடையாள எண் அல்லது தேசிய அடையாள அட்டை இலக்கம் <br>
            Fathers Identification Number (PIN) or NIC
        </td>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="2">
            පියාගේ සම්පුර්ණ නම <br>
            தந்தையின் அடையாள <br>
            Fathers full name
        </td>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="2">
            පියාගේ තරාතිරම නොහොත් රක්ෂාව <br>
            தந்தையின் அடையாள <br>
            Fathers rank or profession
        </td>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="2">
            යමෙකු විසින් කැමැත්ත දුන්නා නම්, ඒ කා විසින්ද යන වග <br>
            in tamil <br>
            Consent if any, by whom given
        </td>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="2">
            කැමැත්ත දුන් අයගේ අත්සන, නොහොත් කැමැත්ත දුන් ලියවිල්ල <br>
            in tamil <br>
            Signature of the person, or reference to the document giving consent
        </td>
        <td colspan="7"></td>
    </tr>

    </tbody>
</table>
<%--section heading female party heading--%>
<table style="margin-top:20px;margin-bottom:20px;width:100%;font-size:16px">
    <caption/>
    <tbody>
    <tr>
        <td align="center">
            ස්ත්‍රී පාර්ශ්වය / in tamil / Female Party
        </td>
    </tr>
    </tbody>
</table>

<%--section female party--%>
<table border="2"
       style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px;border-bottom:none"
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="265px"/>
    <col width="250px"/>
    <col/>
    <tbody>
    <tr>
        <td colspan="1" rowspan="2">
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification Number.
        </td>
        <td colspan="1" rowspan="2">
            dddd
        </td>
        <td colspan="1">
            උපන් දිනය <br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td colspan="1">dddd</td>
    </tr>
    <tr>
        <td colspan="1">
            පසුවූ උපන් දිනයට වයස <br>
            in tamil <br>
            Age at last Birthday

        </td>
        <td colspan="1">
            xxxx
        </td>
    </tr>

    <tr>
        <td>
            නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <br>
            பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="3"></td>
    </tr>

    <tr>
        <td>
            නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பெயர் ஆங்கில மொழியில் <br>
            Name in English
        </td>
        <td colspan="3"></td>
    </tr>

    <tr>
        <td>
            පදිංචි ලිපිනය <br>
            தற்போதைய வதிவிட முகவரி <br>
            Resident Address
        </td>
        <td colspan="3"></td>
    </tr>

    <tr>
        <td>
            දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td>aaa</td>
        <td>
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரதேச செயளாளர் பிரிவு <br>
            Divisional Secretariat
        </td>
        <td>ddd</td>
    </tr>
    <tr>
        <td>
            ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
            பதிவுப் பிரிவு <br>
            Registration Division
        </td>
        <td>aaa</td>
        <td>
            පදිංචි කාලය <br>
            தற்போதைய <br>
            Duration
        </td>
        <td>ddd</td>
    </tr>
    <tr>
        <td>
            දුරකථන අංක <br>
            தொலைபேசி இலக்கம் <br>
            Telephone Numbers
        </td>
        <td>aaa</td>
        <td>
            ඉ – තැපැල් <br>
            மின்னஞ்சல் <br>
            Email
        </td>
        <td>ddd</td>
    </tr>
    </tbody>
</table>
<table border="2" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="143px"/>
    <col width="50px"/>
    <col width="142px"/>
    <col width="50px"/>
    <col width="143px"/>
    <col width="50px"/>
    <col width="150px"/>
    <col/>
    <tbody>
    <tr>
        <td>
            සිවිල් තත්වය<br>
            சிவில் நிலைமை <br>
            Civil Status
        </td>
        <td>
            අවිවාහක <br>
            திருமணமாகாதவர் <br>
            Never Married

        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
        <td>
            දික්කසාද <br>
            திருமணம் தள்ளுபடி செய்தவர் <br>
            Divorced
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
        <td>
            වැන්දබු <br>
            விதவை <br>
            Widowed
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
        <td>
            නිෂ්ප්‍රභාකර ඇත <br>
            தள்ளிவைத்தல் <br>
            Anulled
        </td>
        <td align="center">
            <s:radio name="#" list="#@java.util.HashMap@{'true':''}" value="true"/>
        </td>
    </tr>

    <tr>
        <td colspan="2">
            පියාගේ අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය <br>
            தந்தையின் அடையாள எண் அல்லது தேசிய அடையாள அட்டை இலக்கம் <br>
            Fathers Identification Number (PIN) or NIC
        </td>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="2">
            පියාගේ සම්පුර්ණ නම <br>
            தந்தையின் அடையாள <br>
            Fathers full name
        </td>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="2">
            පියාගේ තරාතිරම නොහොත් රක්ෂාව <br>
            தந்தையின் அடையாள <br>
            Fathers rank or profession
        </td>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="2">
            යමෙකු විසින් කැමැත්ත දුන්නා නම්, ඒ කා විසින්ද යන වග <br>
            in tamil <br>
            Consent if any, by whom given
        </td>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="2">
            කැමැත්ත දුන් අයගේ අත්සන, නොහොත් කැමැත්ත දුන් ලියවිල්ල <br>
            in tamil <br>
            Signature of the person, or reference to the document giving consent
        </td>
        <td colspan="7"></td>
    </tr>

    </tbody>
</table>

<%--section heading witness --%>

<table style="margin-top:20px;margin-bottom:20px;width:100%;font-size:16px">
    <caption/>
    <tbody>
    <tr>
        <td align="center">
            සහතික කරන සාක්ෂිකාරයෝ / in tamil / Attesting Witnesses
        </td>
    </tr>
    </tbody>
</table>

<table border="2" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="390px"/>
    <col width="390px"/>
    <tbody>
    <tr>
        <td></td>
        <td align="center">(1)</td>
        <td align="center">(2)</td>
    </tr>
    <tr>
        <td>
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification Number
        </td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>
            සම්පුර්ණ නම <br>
            Full Name <br>
        </td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>
            තරාතිරම හෝ රක්ෂාව <br>
            Rank or Profession <br>
        </td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>
            පදිංචි ස්ථානය <br>
            Place of Residence <br>
        </td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>
            අත්සන <br>
            Signature <br>
        </td>
        <td></td>
        <td></td>
    </tr>
    </tbody>
</table>
</div>