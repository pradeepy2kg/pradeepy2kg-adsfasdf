<%--@author Tharanga Punchihewa--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="birth-alteration-outer">
<table class="birth-alteration-table-style01" style="width:1030px;">
    <tr>
        <td width="20%"></td>
        <td width="30%"></td>
        <td width="50%">
            <table class="birth-alteration-table-style02" cellspacing="0" style="float:right;">
                <tr>
                    <td colspan="2" style="text-align:center;">කාර්යාල ප්‍රයෝජනය සඳහා පමණි / <br>
                        அலுவலக பாவனைக்காக மட்டும் / <br>
                        For office use only
                    </td>
                </tr>
                <tr>
                    <td width="40%">අනුක්‍රමික අංකය <br>
                        தொடர் இலக்கம் <br>
                        Serial Number
                    </td>
                    <td width="60%"><s:textfield/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><s:textfield/></td>
                </tr>
                <tr>
                    <td>පනතේ වගන්තිය <br>
                        பிறப்பைப் <br>
                        Section of the Act
                    </td>
                    <td><s:textfield/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3" style="font-size:14pt;text-align:center;">උප්පැන්න ලියාපදිංචි කිරීමේ සටහනක විස්තර වෙනස්
            කිරීම / ඇතුලත් කිරීම හෝ අතහැරීම <br>
            ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள் <br>
            Alteration / insertion / omission of particulars from a Birth Registration entry
        </td>
    </tr>
    <tr>
        <td colspan="3" style="font-size:12pt;text-align:center;">උප්පැන්න හා මරණ ලියාපදිංචි කිරීමේ පනත යටතේ
            උප්පැන්න ලියාපදිංචි කිරීමේ සටහනක විස්තර වෙනස්
            කිරීම / ඇතුලත් කිරීම හෝ අතහැරීම සඳහා කරනු ලබන ප්‍රකාශය <br>
            in tamil <br>
            Declaration for the alteration / insertion / omission of particulars of a birth registration entry under
            the Births and Deaths Registration Act.
        </td>
    </tr>
    <tr>
        <td colspan="3" style="font-size:13pt;text-align:center;">වෙනස් කලයුතු උප්පැන්න සහතිකය පිලිබඳ විස්තර <br>
            பிள்ளை பற்றிய தகவல் <br>
            Particulars of the Birth Certificate to amend
        </td>
    </tr>
</table>

<table class="birth-alteration-table-style02" style="width:100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col width="15%"/>
    <col width="25%"/>
    <col width="20%"/>
    <col width="20%"/>
    <col width="20%"/>
    <tbody>
    <tr>
        <td colspan="2">සහතිකයේ සඳහන් පුද්ගලයාගේ අනන්‍යතා අංකය <br>
            தனிநபர்அடையாள எண் <br>
            Person Identification Number (PIN) stated in the Certificate
        </td>
        <td><s:textfield/></td>
        <td>සහතික පත්‍රයේ අංකය <br>
            சான்றிதழ் இல <br>
            Certificate Number
        </td>
        <td><s:textfield/></td>
    </tr>
    <tr>
        <td>දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td><s:select id="districtId" name="birthDistrictId" list="districtList" value="birthDistrictId"
                      cssStyle="width:98.5%; width:240px;"/></td>
        <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரிவு <br>
            Divisional Secretariat
        </td>
        <td colspan="2"><s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  value="%{dsDivisionId}"
                                  cssStyle="float:left;  width:240px;"/></td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
            பிரிவு <br>
            Registration Division
        </td>
        <td><s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}" list="bdDivisionList"
                      cssStyle="float:left;  width:240px; margin:2px 5px;"/></td>
        <td>ලියාපදිංචි කිරීමේ අංකය <br>
            சான்றிதழ் இல <br>
            Registration Number
        </td>
        <td colspan="2"><s:textfield/></td>
    </tr>
    </tbody>
</table>
<table class="birth-alteration-table-style01" style="width:100%;">
    <tr>
        <td style="text-align:center;font-size:13pt;">
            වෙනස් කිරීම / ඇතුලත් කිරීම හෝ අතහැරීම පිලිබඳ විස්තර <br>
            பிள்ளை பற்றிய தகவல் <br>
            Particulars about the alteration / insertion / omission
        </td>
    </tr>
    <tr>
        <td style="text-align:center;font-size:11pt;"> වෙනස් කිරීම / ඇතුලත් කිරීම සඳහා නිවැරදි විය යුතු ආකාරය අදාළ
            කොටුවේ සඳහන් කරන්න. ඉවත් කිරීමක් සඳහා "ඉවත්
            කරන්න" යන්න අදාළ කොටුවේ සඳහන් කරන්න <br>
            in Tamil <br>
            For alteration / insertion state the entry value as it should appear. For omission, state “omit” within
            the relavent cage.
        </td>
    </tr>
    <tr>
        <td style="text-align:center;font-size:13pt;">නම ඇතුලත් කිරීම හෝ වෙනස් කිරීම (27 වගන්තිය) <br>
            தந்தை பற்றிய தகவல் <br>
            Insertion or Alteration of the Name (Section 27)
        </td>
    </tr>
    <tr>
        <td style="text-align:center;font-size:11pt;">මෙම වගන්තිය යටතේ මව්පියන් හෝ භාරකරු හට අවුරුදු 21 කට අඩු
            දරුවකුගේ නම වෙනස් කිරීමට ඉල්ලීම් කල හැක. අවු
            රුදු 21 කට වැඩි පුද්ගලයෙකුගේ නම වෙනස් කිරීමට ඔහු විසින් ඉල්ලුම් පත්‍රයක් ඉදිරිපත් කල හැක. <br>
            தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை
            பற்றிய தகவல் தந்தை பற்றிய தகவல் <br>
            Under this section the name change of a child under 21 years could be requested by his parents or the
            guardian. A person over 21 years in age could request for a name change on his own.
        </td>
    </tr>
</table>
<table class="birth-alteration-table-style02" style="width:100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col width="250px"/>
    <col width="760px"/>
    <tbody>
    <tr>
        <td>නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <br>
            பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td><s:textarea/></td>
    </tr>
    <tr>
        <td>නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பிறப்பு அத்தாட்சி ….. <br>
            Name in English
        </td>
        <td><s:textarea/></td>
    </tr>
    </tbody>
</table>


<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col width="250px;"/>
    <col width="125px;"/>
    <col width="125px;"/>
    <col width="125px;"/>
    <col width="125px;"/>
    <col width="125px;"/>
    <tbody>
    <tr>
        <td style="font-size:13pt;text-align:center;" colspan="7">උප්පැන සහතිකයක දෝෂ නිවැරදි කිරීම (52 (1) වගන්තිය) <br>
            தந்தை பற்றிய தகவல் <br>
            Correction of Errors of a Birth Certificate (Section 52 (1))
        </td>
    </tr>
    <tr>
        <td>උපන් දිනය <br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td>වර්ෂය<br>
            வருடம்<br>
            Year
        </td>
        <td></td>
        <td>මාසය<br>
            மாதம்<br>
            Month
        </td>
        <td></td>
        <td>දිනය<br>
            கிழமை<br>
            Day
        </td>
        <td></td>
    </tr>
    <tr>
        <td rowspan="5">උපන් ස්ථානය<br>
            பிறந்த இடம்<br>
            Place of Birth
        </td>
        <td colspan="2">දිස්ත්‍රික්කය / <br>
            மாவட்டம் /<br>
            District
        </td>
        <td colspan="4"></td>
    </tr>
    <tr>
        <td colspan="2">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>
            பிரிவு / <br>
            Divisional Secretariat
        </td>
        <td colspan="4"></td>
    </tr>
    <tr>
        <td colspan="2">ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br>
            பிரிவு / <br>
            Registration Division
        </td>
        <td colspan="4"></td>
    </tr>
    <tr>
        <td rowspan="2">ස්ථානය <br>
            பிறந்த <br>
            Place
        </td>
        <td>සිංහල හෝ දෙමළ භාෂාවෙන්<br>
            சிங்களம் தமிழ்<br>
            In Sinhala or Tamil
        </td>
        <td colspan="4"></td>
    </tr>
    <tr>
        <td>ඉංග්‍රීසි භාෂාවෙන්<br>
            in tamil<br>
            In English
        </td>
        <td colspan="4"></td>
    </tr>
    <tr>
        <td>
            ස්ත්‍රී පුරුෂ භාවය<br>
            பால் <br>
            Gender of the child
        </td>
        <td colspan="6"></td>
    </tr>
    </tbody>
</table>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col width="250px;"/>
    <col width="100px;"/>
    <col width="150px;"/>
    <col width="10px;"/>
    <col width="50px;"/>
    <col width="100px;"/>
    <col width="50px;"/>
    <col width="50px;"/>
    <col width="100px;"/>

    <tbody>
    <tr>
        <td colspan="9" style="text-align:center;font-size:12pt"> මවගේ විස්තර <br>தாய் பற்றிய தகவல் <br>Details of the
            Mother
        </td>
    </tr>
    <tr>
        <td rowspan="2">අනන්‍යතා අංකය
            / ජාතික හැදුනුම්පත් අංකය<br>து தனிநபர் அடையாள எண் /தேசிய
            அடையாள அட்டை
            இலக்கம்<br>PIN / NIC Number
        </td>
        <td colspan="2" rowspan="2" class="find-person">
            <s:textfield id="mother_pinOrNic" name="parent.motherNICorPIN" cssStyle="width:50px;"/>
            <img src="<s:url value="/images/search-mother.png"/>" style="vertical-align:middle;" id="mother_lookup">
        </td>
        <td colspan="2" rowspan="2"><label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
        </td>
        <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
        <td colspan="2"><s:select name="motherCountry" list="countryList" headerKey="0"
                                  headerValue="%{getText('select_country.label')}"/></td>
    </tr>
    <tr>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
        <td colspan="2" class="passport"><s:textfield name="parent.motherPassportNo" cssStyle="width:30px;"/></td>
    </tr>
    <tr>
        <td>උපන් දිනය<br>
            பிறந்த திகதி<br>
            Date of Birth
        </td>
        <td><s:textfield cssStyle="width:125px;"/></td>
        <td>ළමයාගේ උපන් දිනට වයස <br>
            பிள்ளை பிறந்த திகதியில் மாதாவின் வயது <br>
            Age as at the date of birth of child
        </td>
        <td colspan="2"><s:textfield cssStyle="width:150px;margin-right:10px;"/></td>
        <td colspan="2">ජාතිය<br>
            இனம்<br>
            Race
        </td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td>උපන් ස්ථානය<br>
            பிறந்த இடம் <br>
            Place of Birth
        </td>
        <td colspan="8"><s:textarea/></td>
    </tr>
    <tr>
        <td>ස්ථිර ලිපිනය<br>
            தாயின் நிரந்தர வதிவிட முகவரி<br>
            Permanent Address
        </td>
        <td colspan="8"><s:textarea/></td>
    </tr>
    </tbody>
</table>

<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col width="250px;"/>
    <col width="250px;"/>
    <col width="250px;"/>
    <col width="250px;"/>
    <tbody>
    <tr>
        <td colspan="4" style="text-align:center;font-size:12pt">දැනුම් දෙන්නාගේ විස්තර<br>
            அறிவிப்பு கொடுப்பவரின் தகவல்கள்<br>
            Details of the Informant
        </td>
    </tr>
    <tr>
        <td>දැනුම් දෙන්නේ කවුරුන් විසින් ද?<br>
            தகவல் வழங்குபவா் <br>
            Person Giving Information
        </td>
        <td>මව <br>மாதா<br>
            Mother
        </td>
        <td>පියා<br> பிதா<br>
            Father
        </td>
        <td>භාරකරු<br> பாதுகாவலர் <br>
            Guardian
        </td>
    </tr>
    <tr>
        <td colspan="2">දැනුම් දෙන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>
            தகவல் கொடுப்பவரின் தனிநபர் அடையாள எண் / அடையாள அட்டை இல. <br>
            PIN / NIC of the Informant
        </td>
        <td colspan="2"><s:textfield/></td>
    </tr>
    <tr>
        <td>නම<br>
            கொடுப்பவரின் பெயர்<br>
            Name
        </td>
        <td colspan="3"><s:textarea/></td>
    </tr>
    <tr>
        <td>තැපැල් ලිපිනය<br>
            தபால் முகவரி<br>
            Postal Address
        </td>
        <td colspan="3"><s:textarea/></td>
    </tr>
    </tbody>
</table>
<table class="birth-alteration-table-style01" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td style="text-align:center;font-size:12pt"> උප්පැන්න සහතිකයක තොරතුරු සංශෝදනය කිරීම (27 A වගන්තිය)<br>
            தந்தை பற்றிய தகவல்<br>
            Amendment of Birth Registration Entry (Section 27 A)
        </td>
    </tr>
</table>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">

    <caption></caption>
    <col width="250px;"/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td colspan="5" style="text-align:center;font-size:12pt">පියාගේ විස්තර<br>
            தந்தை பற்றிய தகவல்<br>
            Details of the Father
        </td>
    </tr>
    <tr>
        <td rowspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>
            து தனிநபர் அடையாள எண் /தேசிய அடையாள அட்டை இலக்கம்<br>
            PIN / NIC Number
        </td>
        <td rowspan="2"><s:textfield/></td>
        <td rowspan="2">විදේශිකය‍කු නම්<br>
            வெளிநாட்டவர் எனின் <br>
            If a foreigner
        </td>
        <td>රට<br>
            நாடு<br>
            Country
        </td>
        <td></td>
    </tr>
    <tr>
        <td>ගමන් බලපත්‍ර අංකය<br>
            கடவுச் சீட்டு<br>
            Passport No.
        </td>
        <td></td>
    </tr>
    <tr>
        <td>සම්පුර්ණ නම<br>
            தந்தையின் முழு பெயர்<br>
            Full Name
        </td>
        <td colspan="4"><s:textarea/></td>
    </tr>
    <tr>
        <td>උපන් දිනය<br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td><s:textfield/></td>
        <td colspan="2">ජාතිය<br>
            இனம்<br>
            Race
        </td>
        <td><s:textfield/></td>
    </tr>
    <tr>
        <td>උපන් ස්ථානය<br>
            பிறந்த இடம்<br>
            Place of Birth
        </td>
        <td colspan="4"></td>
    </tr>
    </tbody>
</table>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="4" style="text-align:center;font-size:12pt">විවාහයේ විස්තර වෙනස් කිරීම<br>
            திருமணத்தின் விபரங்கள் <br>
            Changing of Details of the Marriage
        </td>
    </tr>
    <tr>
        <td rowspan="2">මව්පියන් විවාහකද?</td>
        <td rowspan="2">
            பெற்றோர்கள் மணம் முடித்தவர்களா? <br>
            Were Parent's Married?
            ඔව්<br>
            Yes<br>

            නැත<br>
            No

            නැත, නමුත් පසුව විවාහවී ඇත<br>
            No, but since married<br></td>
        <td>විවාහ වු ස්ථානය<br>
            விவாகம் இடம்பெற்ற இடம்<br>
            Place of Marriage
        </td>
        <td><s:textfield/></td>
    </tr>
    <tr>
        <td>
            විවාහ වු දිනය<br>
            விவாகம் இடம்பெற்ற திகதி<br>
            Date of Marriage
        </td>
        <td><s:textfield/></td>
    </tr>
</table>
</div>