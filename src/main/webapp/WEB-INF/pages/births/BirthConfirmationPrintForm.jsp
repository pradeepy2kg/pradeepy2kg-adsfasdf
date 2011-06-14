<%--
 @author amith jayasekara
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">
    #birth-confirmation-print-form-outer-page table tr td {
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

    #birth-confirmation-print-form-outer-page .form-submit {
        margin: 5px 0 15px 0;
    }
</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<script type="text/javascript">

    $(function() {
        $('select#locationId').bind('change', function(evt1) {
            var id = $("select#locationId").attr('value');
            var certId = $('label#certificateId').text();
            var options = '';
            if (id > 0) {
                $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:0,certificateId:certId},
                        function(data) {
                            var locationAddress = data.locationAddress;
                            $("label#retAddress").html(locationAddress);
                        });
            } else {
                $("select#retAddress").html(options);
            }
        });
    });

    function initPage() {
    }
</script>

<div id="birth-confirmation-print-form-outer-page">

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
        <s:param name="birthDivisionId" value="#request.register.birthDivision.bdDivisionUKey"/>
        <s:param name="printed" value="#request.printed"/>
        <s:param name="printStart" value="#request.printStart"/>
    </s:url>
    <s:url id="cancel" action="eprBirthCancelConfirmationPrint.do">
        <s:param name="confirmListFlag" value="true"/>
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="birthDistrictId" value="#request.register.birthDivision.dsDivision.district.districtUKey"/>
        <s:param name="birthDivisionId" value="#request.register.birthDivision.bdDivisionUKey"/>
        <s:param name="printed" value="#request.printed"/>
        <s:param name="printStart" value="#request.printStart"/>
    </s:url>
</s:else>
<div id="confermationRetAddress">
    <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
        <table>
            <caption/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td>
                    <s:label value="%{getText('placeOfIssue.label')}"/>
                </td>
                <td>
                    <s:select id="locationId" name="" list="locationList" cssStyle="width:300px;"/>
                </td>
            </tr>
            </tbody>
        </table>
    </fieldset>
</div>
<div class="form-submit" style="float:right;margin-top:15px;">
    <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
</div>
<div class="form-submit" style="margin-bottom:20px;margin-right:10px;">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    <s:hidden id="printMessage" value="%{getText('print.message')}"/>
</div>

<div class="form-submit" style="float:right;margin-left:10px; margin-top:16px;">
    <s:a href="%{cancel}"><s:label value="%{getText('previous.label')}"/></s:a>
    </fieldset>
</div>

<table style="width:70%;float:left;">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="270px"></td>
        <td align="center" style="font-size:14pt"><img src="<s:url value="/images/official-logo.png" />" alt=""/><br>
        </td>
    </tbody>
</table>

<table cellspacing="0" style="border: 1px solid #000000;height:60px;float:left;width:30%">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td style="text-align:left;margin-right:auto;width:150px;">
            <label>අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</label>
        </td>
        <td style="text-align:center;width:100px">
            <s:label name="bdId" id="certificateId"/>
        </td>
    </tr>
    </tbody>
</table>

<table style="width:100%;float:left;margin-bottom:2px">
    <col/>
    <tbody>
    <tr>
        <td align="center" style="font-size:14pt">
            <label>
                ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA<br><br>
                දෙමව්පියන් / භාරකරු විසින් උපත තහවුරු කිරීම
                ﻿﻿ <br>பெற்றோர் அல்லது பாதுகாப்பாளர் மூலம் பிறப்பை உறுதிப்படுத்தல்
                <br>Confirmation of Birth by Parents / Guardian
            </label>
        </td>
    </tbody>
</table>
<br>
<table border="0" style="margin-bottom:10px;width:100%;">
    <caption></caption>
    <col/>
    <tbody>
    <tr>
        <td align="justify" style="font-size:9pt;"><label>
            උපත තහවුරු කිරීම පිළිබද මෙම ලියවිල්ල, සකස් වී ඇත්තේ ළමයාගේ උපත ලියාපදිංචි කිරීම සඳහා ඔබ විසින් ඉදිරිපත් කරන
            ලද "උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර" අකෘති පත්‍රය අනුවය. එම තොරතුරු සිවිල් ලියාපදිංචි කිරීමේ පද්ධතියට
            ඇතුලත් කර ඇත. එසේ ඇතුලත් කර ඇති තොරතුරු හෝ යම් අක්ෂර දෝෂයක් නිවැරදි කළ යුතු නම්, එම තොරතුරු අදාල ස්ථානයෙහි
            සදහන් කර දින 14 ක් තුල මා වෙත ලැබෙන්න සලස්වන්න. එම කාලය තුල දී මෙම අකෘති පත්‍රය මා වෙත නොලැබුනහොත්, සිවිල්
            ලියාපදිංචි කිරිමේ පද්ධතියේ ඇතුලත් කර ඇති තොරතුරු නිවැරදි බවට සහතික කර උපත ලියාපදිංචි කරනු ලැබේ.
            <br>
            பிறப்பை பதிவு செய்வதற்காக தங்களால் சமர்ப்பிக்கப்பட்ட "பிறப்பை பதிவு செய்வதற்கான விபரம்" எனும்
            பlடிவத்தின்படியே இப்படிவம் தயாரிக்கப்பட்டுள்ளது. அதில் தரப்பட்டுள்ள தகவல்கள் ‘சிவில் பதிவு அமைப்பில்’
            உள்ளடக்கப்பட்டுள்ளது. அவ்வாறு உள்ளடக்கப்பட்டுள்ள பின்வரும் தகவல்களில் ஏதாவது பிழைகள் இருப்பின் அது
            திருத்தப்பட வேண்டுமாயின், அது தொடர்பான விபரங்களை உரிய இடங்களில் குறிப்பிட்டு 14 நாட்களுக்குள் எனக்கு
            கிடைக்கக்கூடியதாக அனுப்பி வைக்கவும். உரியக்காலத்துக்குள் இப்படிவம் எனக்கு கிடைக்காதுவிடின், ‘சிவில் பதிவு
            அமைப்பில்’ உள்ளடக்கப்பட்ட விபரம் சரியானதெனக் கருதி பிறப்புப் பதிவு செய்யப்படும்.
            <br>

            Particulars appearing in this form are based on the information provided by you in the ‘Particulars for
            Registration of a Birth’ form. These particulars are included in the Civil Registration System. If there are
            any errors appearing in the given details, make the necessary corrections in the relevant place and submit
            to me within 14 days. If I do not receive this form within the specified period, the birth particulars in
            the Civil Registration System will be confirmed as accurate and the birth will be registered.
        </label></td>
    </tr>
    </tbody>
</table>

<table class="table_con_page_01" width="100%" cellspacing="0">
    <caption></caption>
    <col/>
    <col/>
    <col width="350px"/>
    <col width="250px"/>
    <tbody>
    <tr>
        <td class="cell_011">1</td>
        <td colspan="2">
            සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ අදාල “උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර” ප්‍රකාශනයේ අනුක්‍රමික අංකය හා
            දිනය
            <br>பிறப்பை பதிவு செய்வதற்கான விபரம்" எனும் படிவத்தின் தொடர் இலக்கமும் திகதியும்
            <br>Serial Number and the Date of the ‘Particulars for Registration of a Birth’ form
        </td>
        <td width="200px">
            <s:label name="register.bdfSerialNo"/><br><s:label name="register.dateOfRegistration"/>
        </td>
    </tr>
    <tr>
        <td style="text-align:center">2</td>
        <td colspan="2"><label>
            යම් වෙනසක් සිදු කලයුතු නම් රෙජිස්ට්‍රාර් ජනරාල් වෙත දැනුම් දිය යුතු අවසන් දිනය<br>
            ஏதாவது மாற்றங்கள் செய்யவேண்டியிருப்பின் பதிவாளர் அதிபதிக்கு தெரிவிக்க வேண்டிய இறுதித் திகதி<br>
            Last date by which changes should be received by the registrar generals office
        </label></td>
        <td><s:label name="register.lastDayForConfirmation"/></td>
    </tr>
    <tr height="80px">
        <td style="text-align:center">3</td>
        <td>
            ආපසු එවිය යුතු ලිපිනය<br>
            திருப்பி அனுப்ப வேண்டிய முகவரி<br>
            Address to post any changes
        </td>
        <td colspan="2">
            <s:label id="retAddress" value="%{returnAddress}"/>
        </td>
    </tr>
    </tbody>
</table>
<table border="0" style="width:100%;margin-top:2px;">
    <col/>
    <tbody>
    <tr>
        <td colspan="20" style="text-align:center;font-size:12pt">
            <label>සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ ඇතුළත් විස්තර
                <br>சிவில் பதிவு நடைமுறையில் உள்ளடக்கப்பட்டுள்ள விபரம்
                <br>Information included in Civil Registration System</label>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_con_page_01" width="100%" cellspacing="0">
    <caption></caption>
    <col width="36px"/>
    <col width="240px"/>
    <col width="64px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="80px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="70px"/>
    <tbody>

    <tr>
        <td colspan="2"><label>විස්තර <br>விபரங்கள் <br>Particulars </label></td>
        <td colspan="6"><label>දැනට අඩංගු විස්තර
            <br>தற்போதய தகவல்
            <br>Current Information </label></td>
        <td class="cell_02" colspan="11" style="font-size:9pt;"><label>
            ඇතුලත් කර ඇති තොරතුරු හෝ යම් අක්ෂර දෝෂයක් නිවැරදි කල යුතුනම්, වෙනස් විය යුතු ආකාරය ඇතුලත් කරන්න
            <br>புதிய விபரங்களை இடுக அல்லது இருக்கும் விபரத்தை திருத்துக
            <br>If there are spelling mistakes or changes in existing details.</label>
        </td>
    </tr>
    <tr>
        <td class="cell_011">4</td>
        <td class="cell_04"><label>උපන් දිනය<br>பிறந்த திகதி/Date of Birth</label></td>
        <td><label>වර්ෂය<br>வருடம்<br>Year</label></td>
        <td>
            <s:textfield value="%{child.dateOfBirth.year+1900}" cssClass="disable" disabled="true" size="4"/>
        </td>
        <td><label>මාසය<br>மாதம்<br>Month</label></td>
        <td>
            <s:textfield value="%{child.dateOfBirth.month+1}" cssClass="disable" disabled="true" size="4"/></td>
        <td><label>දිනය<br>திகதி<br>Day</label></td>
        <td>
            <s:textfield value="%{child.dateOfBirth.date}" cssClass="disable" disabled="true" size="4"/></td>
        <td><label>වර්ෂය<br>வருடம்<br>Year</label></td>
        <td>&nbsp;</td>
        <td><label>මාසය<br>மாதம்<br>Month</label></td>
        <td>&nbsp;</td>
        <td><label>දිනය<br>திகதி<br>Day</label></td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td class="cell_011">5</td>
        <td><label>ස්ත්‍රී පුරුෂ භාවය <br>பால்/Gender</label></td>
        <td colspan="6" style="font-size:12pt;">
            <s:label value="%{child.childGenderPrint}"/>
        </td>
        <td colspan="11">&nbsp;</td>
    </tr>

    <tr>
        <td rowspan="4" style="text-align:center;"> 6</td>
        <td height="40px"><label>උපන් දිස්ත්‍රික්කය<br>மாவட்டம் /District of Birth</label></td>
        <td colspan="6" style="font-size:12pt;">
            <s:label value="%{register.districtPrint}"/>
        </td>
        <td colspan="11">&nbsp;</td>
    </tr>

    <tr>
        <td height="40px"><label>ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரதேச செயளாளர் பிரிவு <br/>
            Divisional Secretary Division</label></td>
        <td colspan="6" style="font-size:12pt;">
            <s:label value="%{register.dsDivisionPrint}"/>
        </td>
        <td colspan="11">&nbsp;</td>
    </tr>

    <tr>
        <td height="40px"><label>ලියාපදිංචි කිරීමේ කොට්ඨාශය<br>
            பதிவுப் பிரிவு <br>
            Registration Division</label></td>
        <td colspan="6" style="font-size:12pt;">
            <s:label value="%{register.bdDivisionPrint}"/>
        </td>
        <td colspan="11">&nbsp;</td>
    </tr>
    <tr height="45px">
        <td height="85px"><label>උපන් ස්ථානය
            <br>பிறந்த இடம்
            <br>Place of Birth</label></td>
        <td colspan="6">
            <s:label name="child.placeOfBirth" cssStyle="font-size:10pt;"/><br>
            <s:label name="child.placeOfBirthEnglish"/>
        </td>
        <td colspan="11">&nbsp;</td>
    </tr>
    <tr>
        <td class="cell_011">7</td>
        <td>
            <label>පියාගේ අනන්‍යතා අංකය <br>தந்தையின் அடையாள எண்<br>Father's Identification No. </label>
        </td>
        <td colspan="6"><s:label name="parent.fatherNICorPIN"/></td>
        <td colspan="11">&nbsp;</td>
    </tr>
    <tr>
        <td class="cell_011">8</td>
        <td><label>පියාගේ ජන වර්ගය <br>தந்தையின் இனம்/Father's Ethnic Group</label></td>
        <td colspan="6" style="font-size:12pt;">
            <s:label value="%{parent.fatherRacePrint}"/>
        </td>
        <td colspan="11">&nbsp;</td>
    </tr>
    <tr>
    <tr>
        <td class="cell_011">9</td>
        <td><label>ම‌වගේ අනන්‍යතා අංකය<br>தாயின் அடையாள எண் <br>Mother's Identification No.</label></td>
        <td colspan="6"><s:label name="parent.motherNICorPIN"/></td>
        <td colspan="11">&nbsp;</td>
    </tr>
    </tbody>
</table>
<div style="page-break-after:always;"></div>

<table class="table_con_page_01" width="100%" cellspacing="0">
    <caption></caption>
    <col width="36px"/>
    <col width="240px"/>
    <col width="64px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="80px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="60px"/>
    <col width="70px"/>
    <tbody>

    <tr>
        <td colspan="2"><label>විස්තර /விபரங்கள் /Particulars </label></td>
        <td colspan="6"><label>දැනට අඩංගු විස්තර
            /தற்போதய தகவல்
            <br>Current Information </label></td>
        <td class="cell_02" colspan="11" style="font-size:9pt;"><label>
            ඇතුලත් කර ඇති තොරතුරු හෝ යම් අක්ෂර දෝෂයක් නිවැරදි කල යුතුනම්, වෙනස් විය යුතු ආකාරය ඇතුලත් කරන්න
            <br>புதிய விபரங்களை இடுக அல்லது இருக்கும் விபரத்தை திருத்துக
            <br>If there are spelling mistakes or changes in existing details.</label></td>
    </tr>
    <tr>
        <td class="cell_011">10</td>
        <td><label>මවගේ ජන වර්ගය <br>தாயின் இனம்/Mother's Ethnic Group</label></td>
        <td colspan="6" style="font-size:12pt;">
            <s:label value="%{parent.motherRacePrint}"/>
        </td>
        <td colspan="11">&nbsp;</td>
    </tr>
    <tr>
        <td class="cell_011">11</td>
        <td><label>මව්පියන් විවාහකද? <br>பெற்றோர் விவாகம் செய்தவர்களா?<br>Were Parents Married?</label></td>
        <td colspan="6" style="font-size:12pt;">
            <s:label value="%{marriage.parentsMarriedPrint}"/>
        </td>
        <td colspan="11">&nbsp;</td>
    </tr>
    <tr>
        <td class="cell_011">12</td>
        <td><label>මව පදිංචි ග්‍රාම නිලධාරී කොට්ඨාශය<br>
            Mother's G.N. Division in ta<br>Mother's Grama Niladhari Division</label></td>
        <td colspan="6" style="font-size:12pt;">
            <s:label value="%{parent.motherGNDivisionPrint}"/>
        </td>
        <td colspan="11">&nbsp;</td>
    </tr>
    <tr>
        <td class="cell_011">13</td>
        <td><label>මව පදිංචි ප්‍රාදේශීය ලේකම් කොට්ඨාශය<br>
            Mother's D.S.Division in ta<br>Mother's Divisional Secretary Division</label></td>
        <td colspan="6" style="font-size:12pt;">
            <s:label value="%{parent.motherDsDivisionPrint}"/>
        </td>
        <td colspan="11">&nbsp;</td>
    </tr>
    </tbody>
</table>

<table border="0" style="width:100%;">
    <caption></caption>
    <col/>
    <tbody>
    <tr>
        <td colspan="3" style="text-align:center;font-size:12pt;">
            <label><br>නම් වල අක්ෂර දෝෂ වෙනස් වීම් ඇතුලත් කිරීම / பெயரிலுள்ள எழுத்துப்பிழைகளை திருத்துதல் /
                Correction of errors in Names</label>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_con_page2_table" cellspacing="0" width="100%">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td class="cell_001">14</td>
        <td width="200px"><p></p><label>ළම‌යාගේ නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ)
            <br>குழந்தையின் பெயர் அரசகரும மொழிகளில்
            (சிங்களம்/தமிழ்)
            <br>Child's name in the official languages (Sinhala / Tamil)</label>

            <p></p></td>
        <td><s:label name="child.childFullNameOfficialLang" cssStyle="font-size:12pt;"/></td>
    </tr>
    <tr>
        <td rowspan="5" class="cell_012">&nbsp;</td>
        <td rowspan="5"><p></p><label>නම වෙනස් විය යුතු අයුරු
            <br>திருத்தப்பட்ட பெயர்
            <br>Corrected name</label>

            <p></p>
        </td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    </tbody>
</table>

<table class="table_con_page2_table" cellspacing="0" width="100%">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td class="cell_001">15</td>
        <td width="200px"><p></p>
            <label>ළම‌යාගේ නම ඉංග්‍රීසි භාෂාවෙන්
                <br>குழந்தையின் பெயர் ஆங்கிலத்தில்
                <br>Child's name in English
            </label>

            <p></p></td>
        <td>
            <s:label cssStyle="text-transform: uppercase;" name="child.childFullNameEnglish"/>
        </td>
    </tr>
    <tr>
        <td rowspan="5" class="cell_012">&nbsp;</td>
        <td rowspan="5"><p></p><label>නම වෙනස් විය යුතු අයුරු
            <br>திருத்தப்பட்ட பெயர்
            <br>Corrected name</label>

            <p></p></td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    </tbody>
</table>

<table class="table_con_page2_table" cellspacing="0" width="100%">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td class="cell_001">16</td>
        <td width="200px"><p></p><label>පියාගේ සම්පුර්ණ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
            <br>தந்தையின் முழுப் பெயர்
            <br>Father's Full Name in any of the official languages (Sinhala / Tamil)</label>
            <p></p>
        </td>
        <td><s:label name="parent.fatherFullName" cssStyle="font-size:12pt;"/></td>
    </tr>
    <tr>
        <td rowspan="5" class="cell_012">&nbsp;</td>
        <td rowspan="5"><p></p><label>නම වෙනස් විය යුතු අයුරු
            <br>திருத்தப்பட்ட பெயர்
            <br>Corrected name</label>

            <p></p></td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    </tbody>
</table>
<div style="page-break-after:always;"></div>

<table border="0" style="width:100%;">
    <caption></caption>
    <col/>
    <tbody>
    <tr>
        <td colspan="3" style="text-align:center;font-size:12pt;">
            <label><br>නම් වල අක්ෂර දෝෂ වෙනස් වීම් ඇතුලත් කිරීම / பெயரிலுள்ள எழுத்துப்பிழைகளை திருத்துதல் /
                Correction of errors in Names</label>
        </td>
    </tr>
    </tbody>
</table>

<%--father full name in english --%>
<table class="table_con_page2_table" cellspacing="0" width="100%">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td class="cell_001">17</td>
        <td width="200px"><p></p><label>පියාගේ සම්පුර්ණ නම ඉංග්‍රීසි භාෂාවෙන් (කැපිටල් අකුරෙන්)</label>
            <br>முழுப் பெயர்
            <br>Father's Full Name in English (in block letters)</label>
            <p></p>
        </td>
        <td><s:label name="parent.fatherFullNameInEnglish" cssStyle="font-size:12pt;"/></td>
    </tr>
    <tr>
        <td rowspan="5" class="cell_012">&nbsp;</td>
        <td rowspan="5"><p></p><label>නම වෙනස් විය යුතු අයුරු
            <br>திருத்தப்பட்ட பெயர்
            <br>Corrected name</label>

            <p></p></td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    </tbody>
</table>

<%--mother full name in official language--%>
<table class="table_con_page2_table" cellspacing="0" width="100%" style="margin-top:30px;">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td class="cell_001">18</td>
        <td width="200px"><p></p><label>මවගේ සම්පුර්ණ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
            <br>தாயின் முழுப் பெயர்
            <br>Mother's Full Name in any of the official languages (Sinhala / Tamil)</label>
            <p></p>
        </td>
        <td><s:label name="parent.motherFullName" cssStyle="font-size:12pt;"/></td>
    </tr>
    <tr>
        <td rowspan="5" class="cell_012">&nbsp;</td>
        <td rowspan="5"><p></p><label>නම වෙනස් විය යුතු අයුරු
            <br>திருத்தப்பட்ட பெயர்
            <br>Corrected name</label>
            <p></p></td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    </tbody>
</table>

<%--mother full name in english--%>
<table class="table_con_page2_table" cellspacing="0" width="100%" style="margin-top:30px;">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td class="cell_001">19</td>
        <td width="200px"><p></p><label>මවගේ සම්පුර්ණ නම ඉංග්‍රීසි භාෂාවෙන් (කැපිටල් අකුරෙන්)
            <br>முழுப் பெயர்
            <br>Mother's Full Name in English (in block letters)</label>
            <p></p>
        </td>
        <td><s:label name="parent.motherFullNameInEnglish" cssStyle="font-size:12pt;"/></td>
    </tr>
    <tr>
        <td rowspan="5" class="cell_012">&nbsp;</td>
        <td rowspan="5"><p></p><label>නම වෙනස් විය යුතු අයුරු
            <br>திருத்தப்பட்ட பெயர்
            <br>Corrected name</label>
            <p></p></td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    </tbody>
</table>
<div style="page-break-after:always;"></div>

<div id="page3">
    <table border="0" style="width:100%;float:left;">
        <caption></caption>
        <col/>
        <tbody>
        <tr>
            <td colspan="3" style="text-align:center;font-size:12pt">
                <p></p><label>
                    උපත තහවුරු කරන්නාගේ විස්තර
                    <br>பிறப்பு விபரங்களை உறுதிப்படுத்துபவரின் விபரங்கள்
                    <br>Person confirming the birth details
                </label><p></p>
            </td>
        </tr>
        </tbody>
    </table>

    <table class="table_con_page_03" cellspacing="0" style="float:left; margin-bottom:30px; width:100%">
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
            <td height="80px" style="text-align:center">20</td>
            <td>උපත තහවුරු කරන්නේ කවුරුන් විසින්ද?<br>
                பிறப்பினை உறுதிப்படுத்துவது யாரால்?<br>
                Person confirming the details
            </td>
            <td>මව<br>தாய்<br> Mother</td>
            <td>&nbsp;</td>
            <td>පියා <br>தந்தை<br>Father</td>
            <td>&nbsp;</td>
            <td>භාරකරු <br>பாதுகாவலர் <br>Guardian</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td height="80px" style="text-align:center">21</td>
            <td>
                <p></p>
                <label>
                    අනන්‍යතා අංකය
                    <br>அடையாள எண்
                    <br>Identification number
                </label>
                <p></p>
            </td>
            <td colspan="7" width="350px">&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="5" class="cell_012">22</td>
            <td rowspan="5">
                <p></p>
                <label>
                    උපත් විස්තර තහවුරු කරන්නාගේ සම්පූර්ණ නම
                    <br>பிறப்பை உறுதிப்படுத்துபவரின் முழுப் பெயர்
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
            <td rowspan="2" class="cell_001">23</td>
            <td rowspan="2">
                <label> ඉහත සදහන් තොරතුරු නිවැරදි බව සහතික කරමි /
                    <br>மேற்குறிப்பிட்ட விபரங்கள் சரியானவை என இத்தால் உறுதிப்படுத்துகிறேன். /
                    <br>I hereby certify that the above information are correct
                </label>
            </td>
            <td colspan="3" width="350px"><p></p><label>දිනය <br>திகதி<br>Date </label>

                <p></p></td>
            <td colspan="4">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="3" style="height:75px">අත්සන<br>கையொப்பம்<br>Signature</td>
            <td colspan="4" style="height:75px">&nbsp;</td>
        </tr>
        </tbody>
    </table>
</div>
<hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:30px;">

<table style="width:100%;">
    <caption></caption>
    <col/>
    <tbody>
    <tr>
        <td colspan="3" style="text-align:center;font-size:12pt;">
            <label>කාර්යාලයේ ප්‍රයෝජනය සඳහා පමණි<br>
                அலுவலக பாவனைக்காக மட்டும்<br>
                Only for office use

            </label>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_con_page_03" cellspacing="0" width="100%">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col width="350px"/>
    <col width="350px"/>
    <tbody>

    <tr>
        <td rowspan="2" class="cell_001">24</td>
        <td colspan="2" rowspan="2">
            <p></p>
            <label>
                ඉහත සදහන් තොරතුරු සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියට ඇතුලත් කරන ලදී.
                /மேற்குறிப்பிட்ட விபரங்கள் ‘சிவில் பதிவு முறையில்’ உள்ளடக்கப்பட்டன (மாவட்டப் பதிவாளர் / மேலதிக
                மாவட்டப் பதிவாளர்)
                /Above information has been entered into the Civil Registration System.
            </label>
            <p></p>
        </td>
        <td><label>දිනය<br>திகதி<br>Date</label>
        </td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td><label>අත්සන සහ නිල මුද්‍රාව<br>
            கையொப்பம்<br>
            Signature</label>
        </td>
        <td>&nbsp;</td>
    </tr>
    </tbody>
</table>

<hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:105px;">
<table border="0" cellspacing="0" width="100%">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td rowspan="7" width="200px" height="350px"></td>
        <td colspan="3" width="600px" height="100px"
            style="text-align:center;margin-left:auto;margin-right:auto;font-size:16pt">
            <label>රාජ්‍ය සේවය පිණිසයි / அரச பணி
                On State Service</label></td>
        <td rowspan="7" width="200px"></td>
    </tr>
    <tr>
        <td height="100px" width="30%">
            <s:label id="retAddress" value="%{returnAddress}" cssStyle="margin-top:30px;"/>
        </td>
        <td width="10%">&nbsp;</td>
        <td width="30%">
            <s:label name="informant.informantName" cssStyle="width:600px;font-size:12pt;"/>,<br/>
            <s:label name="informant.informantAddress" cssStyle="width:600px;font-size:12pt;"/>
        </td>
    </tr>
    <tr>
        <td height="100px" valign="top"></td>
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

<hr style="border-style:dashed ; float:left;width:100% ;margin-top:15px;"/>
<br>

<div class="form-submit" style="float:right;margin-top:15px;">
    <s:a href="%{print}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>
</div>
<div class="form-submit" style="margin-bottom:20px;margin-right:10px;">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    <s:hidden id="printMessage" value="%{getText('print.message')}"/>
</div>

<div class="form-submit" style="float:right;margin-left:10px; margin-top:15px;">
    <s:a href="%{cancel}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</div>
