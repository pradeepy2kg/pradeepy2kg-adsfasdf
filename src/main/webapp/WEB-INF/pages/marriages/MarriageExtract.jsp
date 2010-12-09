<%-- @author Mahesha Kalpanie --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="marriage-notice-outer">
<s:form action="eprMarriageRegistration" method="post">
<table class="table_reg_header_01">
    <caption></caption>
    <col width="420px"/>
    <col width="200px"/>
    <col/>
    <tbody>
    <tr style="font-size:9pt">
        <td colspan="1">
        </td>
        <td align="center" style="font-size:12pt;"><img src="<s:url value="/images/official-logo.png"/>"
        </td>
        <td>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr style="font-size:14pt">
        <td colspan="3" align="center">
            ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA
        </td>
    </tr>
    <tr style="font-size:14pt">
        <td colspan="3" align="center">
            විවාහ ලේඛනයේ උපුටාගැනීම / குடிமதிப்பீட்டு ஆவணத்தில் / Extract of Marriage Register
        </td>
    </tr>
    </tbody>
</table>
<table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="5px">
    <caption></caption>
    <col width="200px"/>
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
        <td>
            දිස්ත්‍රික්කය
            <br>மாவட்டம்
            <br>District
        </td>
        <td colspan="3">

        </td>
        <td colspan="2"><label><span class="font-8">
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය
                <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                <br>பிரதேச செயளாளர் பிரிவு <br>Divisional Secretariat</span>
        </label>
        </td>
        <td align="center" colspan="3">

        </td>
    </tr>
    <tr>
        <td><label><span class="font-8">
        ලියාපදිංචි කිරීමේ කොට්ඨාශය
               <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                    <br>பதிவுப் பிரிவு  <br>Registration Division</span>
        </label>
        </td>
        <td colspan="8">
            <s:if test="preferredLanguage=='si'">
                <s:label name="marriage.mrDivision.siDivisionName" cssStyle="font-size:11pt;"/>
            </s:if>
            <s:elseif test="preferredLanguage=='si'">
                <s:label name="marriage.mrDivision.taDivisionName" cssStyle="font-size:11pt;"/>
            </s:elseif>
            <br/>
            <s:label name="marriage.mrDivision.enDivisionName"/><br/>
        </td>
    </tr>
    <tr>
        <td>
            <label>
                <span class="font-8">විවාහ දිනය
                    <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                    <br>in tamil<br>Date of Marriage</span>
            </label>
        </td>
        <td colspan="2">
            <s:label name="marriage.dateOfMarriage"/>
        </td>
        <td colspan="3"><label><span class="font-8">රෙජිස්ට්‍රාර්ගේ/දේවගැතිගේ අනන්‍යතා අංකය
                    <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                    <br>அடையாள எண் <br>Identification number of Registrar/Minister</span></label>
        </td>
        <td align="center" colspan="3">
            <s:label name="marriage.registrarOrMinisterPIN"/>
        </td>
    </tr>
    <tr>
        <td>
            විවාහ ස්ථානයේ ස්වභාවය<br>
            Type of Marriage Place<br>
        </td>
        <td colspan="8">
            <s:radio name="marriage.typeOfMarriagePlace" list="typeOfMarriagePlace" listValue="type"
                     theme="horizontal"/>
        </td>
    </tr>
    <tr>
        <td>
            විවාහය සිදු කල ස්ථානය<br>
            in tamil <br>
            place of Marriage
        </td>
        <td colspan="8">
            <table width="100%">
                <caption/>
                <col width="150px"/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        රාජ්‍ය භාෂාවෙන්<br>
                        தமிழ் மொழியில் <br>
                        Official Language
                    </td>
                    <td>
                        <s:textarea name="marriage.regPlaceInOfficialLang" id="regPlaceInOfficialLang"
                                    cssStyle="width:98.2%;"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        ඉංග්‍රීසි භාෂාවෙන්<br>
                        ஆங்கில மொழியில்<br>
                        In English
                    </td>
                    <td>
                        <s:textarea name="marriage.regPlaceInEnglishLang" id="regPlaceInEnglishLang"
                                    cssStyle="width:98.2%;"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>

    <tr>
        <td>
            රෙජිස්ට්‍රාර්තැන / දේවගැතිතැන<br>
            in tamil <br>
            Registrar / Minister
        </td>
        <td colspan="8">
            <table width="100%">
                <caption/>
                <col width="150px"/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        රාජ්‍ය භාෂාවෙන්<br>
                        தமிழ் மொழியில் <br>
                        Official Language
                    </td>
                    <td>
                        <s:textarea name="marriage.regNameInOfficialLang" id="regNameInOfficialLang"
                                    cssStyle="width:98.2%;"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        ඉංග්‍රීසි භාෂාවෙන්<br>
                        ஆங்கில மொழியில்<br>
                        In English
                    </td>
                    <td>
                        <s:textarea name="marriage.regNameInEnglishLang" id="regNameInEnglishLang"
                                    cssStyle="width:98.2%;"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>

    <tr>
        <td>
            විවාහයේ ස්වභාවය <br>
            type of marriage in tamil <br>
            Type of Marriage
        </td>
        <td colspan="8">
            <table width="100%">
                <caption/>
                <col/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:radio name="marriage.typeOfMarriage" list="marriageType" listValue="type"
                                 theme="horizontal"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>

<%--section heading Male/Female details --%>
<table style="margin-top:20px;margin-bottom:20px;width:100%;font-size:16px">
    <caption/>
    <tbody>
    <tr>
        <td></td>
        <td align="center">
            පුරුෂ පාර්ශ්වය / in tamil / Male Party
        </td>
        <td align="center">
            ස්ත්‍රී පාර්ශ්වය / in tamil / Female Party
        </td>
    </tr>
    </tbody>
</table>

<table border="2"
       style="margin-top:10px;margin-bottom:20px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="5px">
    <caption/>
    <col width="200px"/>
    <col width="410px"/>
    <col/>
    <tbody>
    <tr>
        <td colspan="1">
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification Number.
        </td>
        <td colspan="1" align="left">
            <s:label name="marriage.male.identificationNumberMale"/>
        </td>
        <td colspan="1" align="left">
            <s:label name="marriage.female.identificationNumberFemale"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            උපන් දිනය <br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td colspan="1">
            <s:label name="marriage.male.dateOfBirthMale"/>
        </td>
        <td colspan="1">
            <s:label name="marriage.female.dateOfBirthFemale"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            පසුවූ උපන් දිනයට වයස <br>
            in tamil <br>
            Age at last Birthday

        </td>
        <td colspan="1">
            <s:label name="marriage.male.ageAtLastBirthDayMale"/>
        </td>
        <td colspan="1">
            <s:label name="marriage.female.ageAtLastBirthDayFemale"/>
        </td>
    </tr>
    <tr>
        <td>
            ජාතිය <br>
            Race <br>

        </td>
        <td>
            <s:select list="raceList" name="marriage.male.maleRace.raceId" headerKey="0"
                      headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;"/>
        </td>
        <td>
            <s:select list="raceList" name="marriage.female.femaleRace.raceId" headerKey="0"
                      headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;"/>
        </td>
    </tr>
    <tr>
        <td>
            සිවිල් තත්වය<br>
            சிவில் நிலைமை <br>
            Civil Status
        </td>
        <td>
            <table>
                <caption/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:radio name="marriage.male.civilStatusMale" list="civilStatusMale" theme="horizontal"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>

        <td>
            <table>
                <caption/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:radio name="marriage.female.civilStatusFemale" list="civilStatusFemale" theme="horizontal"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <br>
            பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td>
            <s:textarea name="marriage.male.nameInOfficialLanguageMale" id="name_official_male"
                        cssStyle="width:98.2%;"/>
        </td>
        <td>
            <s:textarea name="marriage.female.nameInOfficialLanguageFemale" id="name_official_female"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>

    <tr>
        <td>
            නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பெயர் ஆங்கில மொழியில் <br>
            Name in English
        </td>
        <td>
            <s:textarea name="marriage.male.nameInEnglishMale" id="name_english_male" cssStyle="width:98.2%;"/>
        </td>
        <td>
            <s:textarea name="marriage.female.nameInEnglishFemale" id="name_english_female" cssStyle="width:98.2%;"/>
        </td>
    </tr>

    <tr>
        <td>
            පදිංචි ලිපිනය <br>
            தற்போதைய வதிவிட முகவரி <br>
            Resident Address
        </td>
        <td>
            <s:textarea name="marriage.male.residentAddressMaleInOfficialLang" id="address_male"
                        cssStyle="width:98.2%;"/>
        </td>
        <td>
            <s:textarea name="marriage.female.residentAddressFemaleInOfficialLang" id="address_female"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    </tbody>
</table>
<table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="200px">
    <col>
    <col>
    <col>
    <tr>
        <td><label><span class="font-8">අනුක්‍රමික අංකය
                <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                <br>தொடர் இலக்கம்<br>Serial Number</span></label>
        </td>
        <td align="center">
            <s:label name="marriage.idUKey"/>
        </td>
        <td>
            <label>
                    <span class="font-8">ලියාපදිංචි දිනය
                        <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                        <br>பெறப்பட்ட திகதி  <br>Date of Registration</span>
            </label>
        </td>
        <td>
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
            <s:label name="marriage.registrationDate"/>
        </td>
    </tr>
</table>
<div class="form-submit">
</div>
</s:form>
</div>