<%-- @author Mahesha Kalpanie --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/division.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript">
    $(function() {
        $("#submitDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });
</script>
<s:actionerror/>
<div class="marriage-notice-outer">
<s:form action="eprMarriageRegistration" method="post">
<s:hidden name="marriage.idUKey"/>
<%--section for official usage--%>
<table class="table_reg_header_01">
    <caption></caption>
    <col width="420px"/>
    <col width="200px"/>
    <col/>
    <tbody>
    <tr style="font-size:9pt">
        <td colspan="1">
            <table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;"
                   cellpadding="2px">
                <caption/>
                <col width="175px">
                <col>
                <tr>
                    <td>
                        දිස්ත්‍රික්කය
                        <br>மாவட்டம்
                        <br>District
                    </td>
                    <td>
                        <s:select id="districtId" name="marriageDistrictId" list="districtList"
                                  value="marriageDistrictId"
                                  cssStyle="width:98.5%; width:240px;"
                                  onclick="populateDSDivisions('districtId','dsDivisionId','mrDivisionId')"/>
                    </td>
                </tr>
                <tr>
                    <td><label><span class="font-8">
                        ප්‍රාදේශීය ලේකම් කොට්ඨාශය
                            <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                            <br>பிரதேச செயளாளர் பிரிவு <br>Divisional Secretariat</span>
                    </label>
                    </td>
                    <td align="center">
                        <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="dsDivisionId"
                                  cssStyle="width:98.5%; width:240px;"
                                  onchange="populateDivisions('dsDivisionId', 'mrDivisionId')"/>
                    </td>
                </tr>
                <tr>
                    <td><label><span class="font-8">
                        ලියාපදිංචි කිරීමේ කොට්ඨාශය
                               <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                                    <br>பதிவுப் பிரிவு  <br>Registration Division</span>
                    </label>
                    </td>
                    <td>
                        <s:select id="mrDivisionId" name="marriageDivisionId" list="mrDivisionList"
                                  value="marriageDivisionId" headerKey="1"
                                  cssStyle="width:98.5%; width:240px;"/>
                    </td>
                </tr>
            </table>
        </td>
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
                        <s:textfield name="marriage.regSerial" id="regSerial" maxLength="10"/>
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
                        <s:textfield name="marriage.regSubmittedDate" id="submitDatePicker" maxLength="10"
                                     onclick="popupCalendar('submitDatePicker')"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr style="font-size:14pt">
        <td colspan="3" align="center">
            විවාහ ලේකම් පොත / குடிமதிப்பீட்டு ஆவணத்தில் / Register of Marriages
        </td>
    </tr>
    </tbody>
</table>
<br>
<%--type of marriage--%>
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
            විවාහයේ ස්වභාවය <br>
            type of marriage in tamil <br>
            Type of Marriage
        </td>
        <td colspan="8">
            <table width="100%">
                <caption/>
                <col/>
                <tbody>
                <s:iterator value="marriageType">
                    <tr>
                        <td>
                            <s:radio name="marriage.typeOfMarriage.Id" list="#{id:type}"/>
                        </td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
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
                <col width="200px"/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        රාජ්‍ය භාෂාවෙන්<br>
                        தமிழ் மொழியில் <br>
                        Official Language
                    </td>
                    <td>
                        <s:textarea name="marriage.regPlaceInOfficialLang" id="reg_place_official"
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
                        <s:textarea name="marriage.regPlaceInEnglishLang" id="reg_place_English"
                                    cssStyle="width:98.2%;"/>
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
    <col width="400px"/>
    <col/>
    <tbody>
    <tr>
        <td colspan="1">
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification Number.
        </td>
        <td colspan="1" align="left">
            <s:textfield name="marriage.male.identificationNumberMale" id="identification_male" maxLength="10"/>
        </td>
        <td colspan="1" align="left">
            <s:textfield name="marriage.female.identificationNumberFemale" id="identification_female" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            උපන් දිනය <br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td colspan="1">
            <s:textfield name="marriage.male.dateOfBirthMale" id="date_of_birth_male" maxLength="10"/>
        </td>
        <td colspan="1">
            <s:textfield name="marriage.female.dateOfBirthFemale" id="date_of_birth_female" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            පසුවූ උපන් දිනයට වයස <br>
            in tamil <br>
            Age at last Birthday

        </td>
        <td colspan="1">
            <s:textfield name="marriage.male.ageAtLastBirthDayMale" id="age_at_last_bd_male" maxLength="10"
                         value=""/>
        </td>
        <td colspan="1">
            <s:textfield name="marriage.female.ageAtLastBirthDayFemale" id="age_at_last_bd_female" maxLength="10"
                         value=""/>
        </td>
    </tr>
    <tr>
        <td>
            ජාතිය <br>
            Race <br>

        </td>
        <td>
            <s:select list="raceList" name="fatherRace" headerKey="0" headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;"/>
        </td>
        <td>
            <s:select list="raceList" name="fatherRace" headerKey="0" headerValue="%{getText('select_race.label')}"
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
                <s:iterator value="civilStatusMale">
                    <tr>
                        <td>
                            <s:radio name="marriage.male.civilStatusMale" list="#{key:value}"/>
                        </td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
        </td>

        <td>
            <table>
                <caption/>
                <col/>
                <tbody>
                <s:iterator value="civilStatusFemale">
                    <tr>
                        <td>
                            <s:radio name="marriage.female.civilStatusFemale" list="#{key:value}"/>
                        </td>
                    </tr>
                </s:iterator>
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
            <s:textarea name="marriage.male.residentAddressMale" id="address_male" cssStyle="width:98.2%;"/>
        </td>
        <td>
            <s:textarea name="marriage.female.residentAddressFemale" id="address_female" cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td>
            පියාගේ සම්පුර්ණ නම <br>
            தந்தையின் அடையாள <br>
            Fathers full name
        </td>
        <td>
            <s:textarea name="marriage.male.fatherFullNameMale" id="father_full_name_male"
                        cssStyle="width:98.2%;"/>
        </td>
        <td>
            <s:textarea name="marriage.female.fatherFullNameFemale" id="father_full_name_female"
                        cssStyle="width:98.2%;"/>
        </td>
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
<br>
<table border="2" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="5px">
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
        <td>
            <s:textfield name="marriage.witness1.identificationNumber" id="m_witness_1_pin"
                         cssStyle="width:240px;"
                         maxLength="10"/>
        </td>
        <td>
            <s:textfield name="marriage.witness2.identificationNumber" id="f_witness_2_pin"
                         cssStyle="width:240px;"
                         maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td>
            සම්පුර්ණ නම <br>
            Full Name <br>
        </td>
        <td>
            <s:textarea name="marriage.witness1.fullName" id="m_witness_1_full_name" cssStyle="width:98.2%;"
                    />
        </td>
        <td>
            <s:textarea name="marriage.witness2.fullName" id="f_witness_2_full_name" cssStyle="width:98.2%;"
                    />
        </td>
    </tr>
    <tr>
        <td>
            තරාතිරම හෝ රක්ෂාව <br>
            Rank or Profession <br>
        </td>
        <td>
            <s:textarea name="marriage.witness1.rankOrProfession" id="m_witness_1_rank" cssStyle="width:98.2%;"
                    />
        </td>
        <td>
            <s:textarea name="marriage.witness2.rankOrProfession" id="f_witness_2_rank" cssStyle="width:98.2%;"
                    />
        </td>
    </tr>
    <tr>
        <td>
            පදිංචි ස්ථානය <br>
            Place of Residence <br>
        </td>
        <td>
            <s:textarea name="marriage.witness1.address" id="m_witness_1_place_residence" cssStyle="width:98.2%;"
                    />
        </td>
        <td>
            <s:textarea name="marriage.witness2.address" id="f_witness_2_place_residence" cssStyle="width:98.2%;"
                    />
        </td>
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
<div class="form-submit">
    <s:submit/>
</div>
</s:form>
</div>