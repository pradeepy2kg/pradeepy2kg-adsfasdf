<%@ page import="java.util.Date" %>
<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
<%--
 @author amith jayasekara
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">
    #birth-confirmation-print-form-outer table tr td {
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


<div id="birth-confirmation-print-form-outer">
<div class="birth-confirmation-print-form-outer" id="page1" style="page-break-after:always;">
<table style="width:65%;float:left;">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="300px"></td>
        <td align="center" style="font-size:12pt"><img src="<s:url value="/images/official-logo.png" />" alt=""/><br>
            <label>
                ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA<br><br>
                දෙමව්පියන් / භාරකරු විසින් උපත තහවුරු කිරීම
                ﻿﻿ <br>பெற்றோர் அல்லது பாதுகாப்பாளா் மூலம் பிறப்பை உறுதிப்படுத்தல்
                <br>Confirmation of Birth by Parents / Guardian

            </label></td>
    </tbody>
</table>

<table cellspacing="0" style="border: 1px solid #000000;height:60px;float:left;width:35%">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td style="text-align:left;margin-left:0;margin-right:auto;width:150px;">
            <label>අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</label>
        </td>
        <td style="text-align:right;width:100px">
            <s:textfield cssClass="disable" disabled="true" name="bdId"/>
        </td>
    </tr>
    </tbody>
</table>

<table border="0" style="margin-bottom:10px;width:1030px;float:left;">
    <caption></caption>
    <col/>
    <tbody>
    <tr>
        <td align="justify"><label>
            උපත තහවුරු කිරීම පිළිබද මෙම ලියවිල්ල, සකස් වී ඇත්තේ ළමයාගේ
            උපත ලියාපදිංචි කිරීම සඳහා ඔබ විසින් ඉදිරිපත් කරන ලද "උපතක් ලියාපදිංචි
            කිරීම සඳහා විස්තර" අකෘති පත්‍රය අනුවය. එම තොරතුරු සිවිල් ලියාපදිංචි
            කිරීමේ පද්ධතියට ඇතුලත් කර ඇත. එසේ ඇතුලත් කර ඇති තොරතුරු වල යම්
            අක්ෂර දෝෂයක් නිවැරදි කළ යුතු නම්, එම තොරතුරු අදාල ස්ථානයෙහි සදහන් කර දින
            14 ක් තුල මා වෙත ලැබෙන්න සලස්වන්න. එම කාලය තුල දී මෙම අකෘති පත්‍රය මා
            වෙත නොලැබුනහොත්, සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ ඇතුලත් කර ඇති
            තොරතුරු නිවැරදි බවට සහතික කර උපත ලියාපදිංචි කරනු ලැබේ.<br><br>

            பிறப்பை பதிவு செய்வதற்காக தங்களால் சமர்ப்பிக்கப்பட்ட "பிறப்பை பதிவு செய்வதற்கான விபரம்" எனும்
            படவத்தின்படியே இப்படிவம் தயாரிக்கப்பட்டுள்ளது. அதில் தரப்பட்டுள்ள தகவல்கள் ‘சிவில்
            பதிவு அமைப்பில்’ உள்ளடக்கப்பட்டுள்ளது. அவ்வாறு உள்ளடக்கப்பட்டுள்ள பின்வரும் தகவல்களில்
            ஏதாவது பிழைகள் இருப்பின் அது திருத்தப்பட வேண்டுமாயின், அது தொடர்பான விபரங்களை
            உரிய இடங்களில் குறிப்பிட்டு 14 நாட்களுக்குள் எனக்கு கிடைக்கக் சுடியதாக அனுப்பி வைக்கவும்.
            உரிய காலத்துள் இப்படிவம் எனக்கு கிடைக்காதுவிடின், ‘சிவில் பதிவு அமைப்பில்’
            உள்ளடக்கப்பட்ட விபரம் சரியானதெனக் கருதி பிறப்புப் பதிவு செய்யப்படும்.<br><br>

            Particulars appearing in this form are based on the information provided by you
            in the ‘Particulars for Registration of a Birth’ form. These particulars
            are included in the Civil Registration System. If there are any errors
            appearing in the given details, make the necessary corrections in the relevant
            place and submit to me within 14 days. If I do not receive this form within
            the specified period, the birth particulars in the Civil Registration System will
            be confirmed as accurate and the birth will be registered.<br>
        </label></td>
    </tr>
    </tbody>
</table>


<table class="table_con_page_01" style="float:left;" width="100%" cellspacing="0">
    <caption></caption>
    <col/>
    <col/>
    <col width="350px"/>
    <col width="250px"/>
    <tbody>
    <tr>
        <td class="cell_011">1</td>
        <td colspan="2">සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ අදාල “උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර” ප්‍රකාශනයේ
            අනුක්‍රමික අංකය
            හා දිනය
            <br>பிறப்பை பதிவு செய்வதற்கான விபரம்" எனும் படிவத்தின் தொடா் இலக்கமும் திகதியும்
            <br>Serial Number and the Date of the ‘Particulars for Registration of a Birth’ form
        </td>
        <td width="200px"><s:textfield cssClass="disable" disabled="true" name="register.bdfSerialNo"/>
            <s:textfield cssClass="disable" disabled="true" name="register.dateOfRegistration"/></td>
    </tr>
    <tr>
        <td style="text-align:center">2</td>
        <td colspan="2"><label>
            යම් වෙනසක් සිදු කලයුතු නම් රෙජිස්ට්‍රාර් ජනරාල් වෙත දැනුම් දිය යුතු අවසන් දිනය <br>
            மாற்றங்கள் பதிவாளர் அதிகாரியின் அலுவலகத்தை அடைய வேண்டிய இறுதித் திகதி <br>
            Last date by which changes should be received by the registrar generals office
        </label></td>
        <td><s:textfield cssClass="disable" disabled="true" name="register.lastDayForConfirmation"/></td>
    </tr>
    <tr>
        <td style="text-align:center">3</td>
        <td>
            ආපසු එවිය යුතු ලිපිනය <br>
            மாற்றங்கள்.. <br>
            Address to post any changes
        </td>
        <td colspan="2">

        </td>
    </tr>
    </tbody>
</table>

<table border="0" style="width: 100%; float:left;">
    <caption></caption>
    <col/>
    <tbody>
    <tr>
        <td colspan="20" style="text-align:center;font-size:12pt"><label>සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ ඇතුළත් විස්තර
            <br>சிவில் பதிவு அமைப்பில் உள்ளடக்கப்பட்டுள்ள விபரம்
            <br>Information included in Civil Registration System</label>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_con_page_01" width="100%" cellspacing="0" style="margin-top:10px;float:left;">
    <caption></caption>
    <col/>
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
        <td colspan="2"><label>විස්තර <br>விபரங்கள் <br>Particulars </label></td>
        <td colspan="6"><label>සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ දැනට අඩංගු විස්තර <br>சிவில் பதிவு அமைப்பில்
            உள்ளடக்கப்பட்டுள்ள
            விபரம<br>Information included in Civil Registration System </label></td>
        <td class="cell_02" colspan="11"><label>
            ඇතුලත් කරඇති තොරතුරු හෝ යම් අක්ෂර දෝෂයක් නිවැරදි කල යුතුනම් .
            <br>புதியசிவில் பதிவ..ண்டிய விப...
            <br>If there are spelling mistakes or changes in existing details.</label></td>
    </tr>
    <tr>
        <td class="cell_011">4</td>
        <td class="cell_04"><label>උපන් දිනය<br>பிறந்த திகதி<br>Date of birth</label></td>
        <td><label>අවුරුද්ද  <br>இயர் <br>Year</label></td>
        <td width="60px"><s:textfield value="%{child.dateOfBirth.year+1900}" cssClass="disable" disabled="true"
                                      size="4"/></td>
        <td><label>මාසය<br>மாதம்<br>Month</label></td>
        <td width="60px"><s:textfield value="%{child.dateOfBirth.month+1}" cssClass="disable" disabled="true"
                                      size="4"/></td>
        <td><label>දිනය<br>திகதி<br>Day</label></td>
        <td width="60px"><s:textfield value="%{child.dateOfBirth.date}" cssClass="disable" disabled="true"
                                      size="4"/></td>
        <td colspan="6"><label>අවුරුද්ද  <br>இயர் <br>Year</label></td>
        <td width="60px"></td>
        <td><label>මාසය<br>மாதம்<br>Month</label></td>
        <td width="60px"></td>
        <td><label>දිනය<br>திகதி<br>Day</label></td>
        <td width="60px"></td>

    </tr>
    <tr>
        <td class="cell_011">5</td>
        <td><label>ස්ත්‍රී පුරුෂ භාවය <br>பால்பால்<br>Gender</label></td>
        <td colspan="6"><s:if test="child.childGender == 0">
            <s:textfield value="%{getText('male.label')}" cssClass="disable" disabled="true"/>
        </s:if>
            <s:elseif test="child.childGender == 1">
                <s:textfield value="%{getText('female.label')}" cssClass="disable" disabled="true"/>
            </s:elseif>
            <s:elseif test="child.childGender == 2">
                <s:textfield value="%{getText('unknown.label')}" cssClass="disable" disabled="true"/>
            </s:elseif></td>
        <td colspan="11"></td>
    </tr>

    <tr>
        <td rowspan="4" style="text-align:center;"> 6</td>
        <td height="40px"><label>දිස්ත්‍රික්කය <br>மாவட்டம் <br>District</label></td>
        <td colspan="6"><s:textfield value="%{getDsDivisionList().get(dsDivisionId)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="11"></td>
    </tr>

    <tr>
        <td height="40px"><label>කොට්ඨාශය<br>பிரிவு <br>Registration Division</label></td>
        <td colspan="6"><s:textfield value="%{getBdDivisionList().get(birthDivisionId)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="11"></td>
    </tr>
    <tr>
        <td height="40px"><label>ස්ථානය <br>பிறந்த இடம் <br>Place</label></td>
        <td colspan="6"><s:textfield name="child.placeOfBirth" cssClass="disable" disabled="true" size="30"/></td>
        <td colspan="11"></td>
    </tr>
    <tr>
        <td><label>ඉංග්‍රීසි භාෂාවෙන් <br>இங்கிலீஷ் <br>In English</label></td>
        <td colspan="6"><s:textfield name="child.placeOfBirthEnglish" cssClass="disable" disabled="true"
                                     size="30"/></td>
        <td colspan="11"></td>
    </tr>
    <tr>
        <td class="cell_011">7</td>
        <td><label>පියාගේ අනන්‍යතා අංකය <br>தந்நையின் தனிநபர் அடையாள எண்<br>Father's PIN</label></td>
        <td colspan="6"><s:textfield name="parent.fatherNICorPIN" cssClass="disable" disabled="true"/></td>
        <td colspan="11"></td>
    </tr>
    <tr>
        <td class="cell_011">8</td>
        <td><label>පියාගේ ජාතිය <br>தந்நையின் இனம்<br>Father's Race</label></td>
        <td colspan="6"><s:textfield value="%{getRaceList().get(fatherRace)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="11"></td>
    </tr>
    <tr>

    <tr>
        <td class="cell_011">9</td>
        <td><label>ම‌වගේ අනන්‍යතා අංකය <br>தாயின் தனிநபர் அடையாள எண<br>Mother's PIN</label></td>
        <td colspan="6"><s:textfield name="parent.motherNICorPIN" cssClass="disable" disabled="true"/></td>
        <td colspan="11"></td>
    </tr>
    <tr>
        <td class="cell_011">10</td>
        <td><label>මවගේ ජාතිය <br>தாயின் இனம்<br>Mother's Race</label></td>
        <td colspan="6"><s:textfield value="%{getRaceList().get(motherRace)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="11"></td>
    </tr>
    <tr>
        <td class="cell_011">11</td>
        <td><label>මව්පියන් විවාහකද? <br>பெற்றார் விவாகஞ் செய்தவர்களா? <br>Were Parents Married?</label></td>
        <td colspan="6"><s:textfield cssClass="disable" disabled="true" name="marriage.parentsMarried"
                                     value="%{getText('married.status.'+marriage.parentsMarried)}"/></td>
        <td colspan="11"></td>
    </tr>

    </tbody>
</table>


</div>
<div class="birth-confirmation-print-form-outer" id="page2" style="page-break-after:always;">
    <table border="0" style="width: 100%;float:left;">
        <caption></caption>
        <col/>
        <tbody>
        <tr>
            <td colspan="3" style="text-align:center;font-size:12pt;">
                <label><br>නම්වල අක්ෂර දෝශ වෙනස් වීම් ඇතුලත් කිරීම/ பெயரிலுள்ள மாற்றங்கள்/ Changes in Names</label>
            </td>
        </tr>
        </tbody>
    </table>

    <table class="table_con_page2_table" cellspacing="0">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td class="cell_001">12</td>
            <td width="200px"><p></p><label>ළම‌යාගේ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ) <br>பிறப்பு அத்... (சிங்களம் /
                தமிழ்)
                <br>Childs name in the official languages (Sinhala / Tamil)</label>

                <p></p></td>
            <td><s:textarea cssClass="disable" disabled="true" name="child.childFullNameOfficialLang"/></td>
        </tr>
        <tr>
            <td rowspan="5" class="cell_012"></td>
            <td rowspan="5"><p></p><label>නම වෙනස් විය යුතු අයුරු
                <br>திருத்தப்பட்ட பெயர்
                <br>Corrected name</label>

                <p></p></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        </tbody>
    </table>

    <table class="table_con_page2_table" cellspacing="0">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td class="cell_001">13</td>
            <td width="200px"><p></p>
                <label>ළම‌යාගේ නම ඉංග්‍රීසි භාෂාවෙන්
                    <br>பிறப்பு ...
                    <br>Childs name in English
                </label>

                <p></p></td>
            <td>
                <s:textarea cssClass="disable" disabled="true" cssStyle="text-transform: uppercase;"
                            name="child.childFullNameEnglish"/>
            </td>
        </tr>
        <tr>
            <td rowspan="5" class="cell_012"></td>
            <td rowspan="5"><p></p><label>නම වෙනස් විය යුතු අයුරු
                <br>திருத்தப்பட்ட பெயர்
                <br>Corrected name</label>

                <p></p></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        </tbody>
    </table>

    <table class="table_con_page2_table" cellspacing="0">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td class="cell_001">14</td>
            <td width="200px"><p></p><label>පියාගේ සම්පුර්ණ නම
                <br>தந்நையின் முழுப் பெயர
                ்<br>Father's Full Name</label>

                <p></p>
            </td>
            <td><s:textarea cssClass="disable" disabled="true" name="parent.fatherFullName"/></td>
        </tr>
        <tr>
            <td rowspan="5" class="cell_012"></td>
            <td rowspan="5"><p></p><label>නම වෙනස් විය යුතු අයුරු
                <br>திருத்தப்பட்ட பெயர்
                <br>Corrected name</label>

                <p></p></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        </tbody>
    </table>

    <table class="table_con_page2_table" cellspacing="0">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td class="cell_001">15</td>
            <td width="200px"><p></p><label>මවගේ සම්පූර්ණ නම
                <br>தாயின் முழுப் பெயர
                ்<br>Mother's Full Name</label>

                <p></p>
            </td>
            <td><s:textarea cssClass="disable" disabled="true" name="parent.motherFullName"/></td>
        </tr>
        <tr>
            <td rowspan="5" class="cell_012"></td>
            <td rowspan="5"><p></p><label>නම වෙනස් විය යුතු අයුරු
                <br>திருத்தப்பட்ட பெயர்
                <br>Corrected name</label>

                <p></p></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        </tbody>
    </table>

</div>

<div class="birth-confirmation-print-form-outer" id="page3" class="page_break">

<table border="0" style="width: 100% ;float:left;">
    <caption></caption>
    <col/>
    <tbody>
    <tr>
        <td colspan="3" style="text-align:center;font-size:12pt">
            <p></p>
            <label>
                උපත තහවුරු කරන්නාගේ විස්තර <br>* in Tamil
                <br>Person confirming the birth details
            </label>

            <p></p>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_con_page_03" cellspacing="0" style="float:left; margin-bottom:30px;">
    <caption></caption>
    <col/>
    <col width="250px"/>
    <col style="width:12.5%"/>
    <col style="width:12.5%"/>
    <col style="width:12.5%"/>
    <col style="width:12.5%"/>
    <col style="width:12.5%"/>
    <col style="width:12.5%"/>
    <tbody>

    <tr>
        <td height="80px" style="text-align:center">16</td>
        <td> උපත තහවුරු කරන්නේ කවුරුන් විසින්ද?<br>
            *in tamil* <br>
            Person confirming the details
        </td>
        <td>මව<br>*in tamin <br> Mother</td>
        <td></td>
        <td>පියා <br>*in tamil<br>Father</td>
        <td></td>
        <td>භාරකරු <br>*in tamil<br>Guardian</td>
        <td></td>
    </tr>
    <tr>
        <td height="80px" style="text-align:center">17</td>
        <td colspan="4">
            <p></p>
            <label>
                උපත තහවුරු කරන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
                <br>பிறப்​பை உறுதிப்படுத்துபவரின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை எண்
                <br>PIN / NIC of person confirming the birth details
            </label>

            <p></p>
        </td>
        <td colspan="3" width="350px"></td>
    </tr>

    <tr>
        <td rowspan="5" class="cell_012">18</td>
        <td rowspan="5">
            <p></p>
            <label>
                උපත තහවුරු කරන්නාගේ සම්පූර්ණ නම
                <br>பிறப்​பை உறுதிப்படுத்துபவரின் முழுப் பெயர்
                <br>Full Name of the person confirming the birth details
            </label>

            <p></p>
        </td>

        <td colspan="6"><p></p></td>
    </tr>
    <tr>
        <td colspan="6"><p></p></td>
    </tr>
    <tr>
        <td colspan="6"><p></p></td>
    </tr>
    <tr>
        <td colspan="6"><p></p></td>
    </tr>
    <tr>
        <td colspan="6"><p></p></td>
    </tr>

    <tr>
        <td rowspan="2" class="cell_001">19</td>
        <td rowspan="2">
            <label> ඉහත සදහන් තොරතුරු නිවැරදි බව සහතික කරමි
                <br>மேற்குறிப்பிட்ட விபரங்கள் சரியானவை என இத்தால் உறுதிப்படுத்துகிறேன்.
                <br>I hereby certify that the above information are correct
            </label>
        </td>
        <td colspan="3" width="350px"><p></p><label>දිනය <br>திகதி <br>Date </label>

            <p></p></td>
        <td colspan="4"></td>
    </tr>
    <tr>
        <td colspan="3" style="height:75px">අත්සන<br>கையொப்பம்<br>Signature</td>
        <td colspan="4" style="height:75px"></td>
    </tr>
    </tbody>
</table>

<hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:30px;">
<table border="0" style="width: 100%;">
    <caption></caption>
    <col/>
    <tbody>
    <tr>
        <td colspan="3" style="text-align:center;font-size:12pt;">
            <label>කාර්යාලයේ ප්‍රයෝජනය සඳහා පමණි <br>
                அலுவலக பாவனைக்காக மட்டும் <br>
                Only for office use

            </label>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_con_page_03" cellspacing="0" style="margin-top:20px ; margin-bottom:30px;">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>

    <tr>
        <td rowspan="2" class="cell_001">20</td>
        <td rowspan="2"><p></p><label>ඉහත සදහන් තොරතුරු සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියට ඇතුලත් කරන ලදී.
            (දිස්ත්‍රික් රෙජිස්ත්‍රාර් / අතිරේක දිස්ත්‍රික් රෙජිස්ත්‍රාර්)<br>
            மேற்குறிப்பிட்ட விபரங்கள் ‘சிவில் பதிவு அமைப்பில்’ உள்ளடக்கப்பட்டன (மாவட்டப் பதிவாளா் / மேலதிக மாவட்டப்
            பதிவாளர்)<br>
            Above information has been entered into the Civil Registration System.
            (District Registrar / Additional District Registrar)</label>

            <p></p>
        </td>
        <td width="350px"><label>දිනය<br> திகதி <br>Date</label>
        </td>
        <td width="350px"></td>
    </tr>
    <tr>
        <td><label>අත්සන<br>
            கையொப்பம்<br>
            Signature</label>
        </td>
        <td width="350px"></td>
    </tr>
    </tbody>
</table>

<table border="0" cellspacing="0">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td rowspan="8" width="200px" height="350px"></td>
        <td colspan="2" width="600px" height="100px"
            style="text-align:center;margin-left:auto;margin-right:auto;font-size:12pt">
            <label>රාජ්‍ය සේවය පිණිසයි<br>
                / *in Tamil * <br>
                / On State Service<br>
            </label></td>
        <td rowspan="8" width="200px"></td>
    </tr>
    <tr>
        <td><s:textarea name="informant.informantName" cssStyle="width:600px;" cssClass="disable"
                        disabled="true"/></td>
    </tr>
    <tr>
        <td><s:textarea name="informant.informantAddress" cssStyle="width:600px;" cssClass="disable"
                        disabled="true"/></td>
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
        <td>Printed On</td>

        <td style="text-align:right;margin-left:auto;margin-right:0;"><%= DateTimeUtils.getISO8601FormattedString(new Date()) %>
        </td>
    </tr>
    </tbody>
</table>

<s:if test="directPrint">
<s:url id="print" action="eprDirectPrintBirthConfirmation.do">
    <s:param name="bdId" value="#request.bdId"/>
    <s:param name="confirmListFlag" value="true"/>
</s:url>
    <s:url id="cancel" action="eprBirthRegistrationHome.do"/>
</s:if>
<s:else>
    <%--TODO remove unused parameters--%>
<s:url id="print" action="eprMarkBirthConfirmationAsPrint.do">
    <s:param name="bdId" value="#request.bdId"/>
    <s:param name="confirmListFlag" value="true"/>
    <s:param name="pageNo" value="%{#request.pageNo}"/>
    <s:param name="birthDistrictId" value="#request.register.birthDivision.dsDivision.district.districtUKey"/>
    <s:param name="birthDivisionId" value="#request.register.birthDivision.dsDivision.dsDivisionUKey"/>
    <s:param name="printed" value="#request.printed"/>
    <s:param name="printStart" value="#request.printStart"/>
</s:url>
<s:url id="cancel" action="eprBirthCancelConfirmationPrint.do">
    <s:param name="confirmListFlag" value="true"/>
    <s:param name="pageNo" value="%{#request.pageNo}"/>
    <s:param name="birthDistrictId" value="#request.register.birthDivision.dsDivision.district.districtUKey"/>
    <s:param name="birthDivisionId" value="#request.register.birthDivision.dsDivision.dsDivisionUKey"/>
    <s:param name="printed" value="#request.printed"/>
    <s:param name="printStart" value="#request.printStart"/>
</s:url>
</s:else>

<div id="birthRegistration-page" style="float:right;margin-left:10px; margin-top:25px;">
    <s:a href="%{print}" ><s:label value="%{getText('mark_as_print.button')}"/></s:a>
</div>
<div class="form-submit" style="margin-bottom:20px;margin-right:10px;">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    <s:hidden id="printMessage" value="%{getText('print.message')}"/>
</div>
</div>
<div id="birthRegistration-page" style="float:right;margin-left:10px; margin-top:14px;">
    <s:a href="%{cancel}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
