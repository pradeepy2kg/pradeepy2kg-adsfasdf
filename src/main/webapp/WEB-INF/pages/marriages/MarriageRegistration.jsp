<%-- @author Mahesha Kalpanie --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/division.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/datePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/marriageregistervalidation.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/personlookup.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/transliteration.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/registrarlookup.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript">

    function validateMarriageDetails() {
        var errormsg = "";
        //errormsg = validateEmptyField("marriageDatePicker", "errorMarriageDate", errormsg);
        ////errormsg = isDate("marriageDatePicker", "errorMarriageDate", errormsg);
        //validate registrar details
        ////errormsg = validatePin("regPIN", "errorRegistrarPIN", errormsg);
        ////errormsg = validateEmptyField("regPlaceInOfficialLang", "errorRegistrationPlace", errormsg);
        ////errormsg = validateEmptyField("regNameInOfficialLang", "errorRegistrarName", errormsg);

        var mode = document.getElementById("mode").value;

        errormsg = validateRegistrationDetails(errormsg);
        if (mode == 'register') {
            return printErrorMessages(errormsg);
        }

        //validate Male/Female
        errormsg = validateSelectOption("raceMale", "errorMaleRace", errormsg);
        errormsg = validateSelectOption("raceFemale", "errorFemaleRace", errormsg);

        //errormsg = validateEmptyField("malePIN", "errorMalePIN", errormsg);
        //errormsg = validateEmptyField("femalePIN", "errorFemalePIN", errormsg);
        //errormsg = validateEmptyField("dateOfBirthMaleDatePicker", "errorDateOfBirthMale", errormsg);
        //errormsg = isDate("dateOfBirthMaleDatePicker", "errorDateOfBirthMale", errormsg);
        //errormsg = validateEmptyField("dateOfBirthFemaleDatePicker", "errorDateOfBirthFemale", errormsg);
        //errormsg = isDate("dateOfBirthFemaleDatePicker", "errorDateOfBirthFemale", errormsg);
        errormsg = validateEmptyField("ageMale", "errorAgeMale", errormsg);
        errormsg = validateEmptyField("ageFemale", "errorAgeFemale", errormsg);
        errormsg = validateEmptyField("nameOfficialMale", "errorNameOfficialMale", errormsg);
        errormsg = validateEmptyField("nameOfficialFemale", "errorNameOfficialFemale", errormsg);
        errormsg = validateEmptyField("addressMale", "errorAddressMale", errormsg);
        errormsg = validateEmptyField("addressFemale", "errorAddressFemale", errormsg);
        errormsg = validateSerialNo("serialNumber", "errorSerialNumber", errormsg);

        //todo: validate Path exists
        //errormsg = validateEmptyField("scannedImage", "errorscannedImage", errormsg);


        ////errormsg = isDate("registrationDatePicker", "errorRegistrationDate", errormsg);
        return printErrorMessages(errormsg);
    }

    function validateRegistrationDetails(errormsg) {
        //errormsg = validateEmptyField("marriageDatePicker", "errorMarriageDate", errormsg);
        errormsg = isDate("marriageDatePick", "errorMarriageDate", errormsg);
        //validate registrar details
        errormsg = validatePin("regPIN", "errorRegistrarPIN", errormsg);
        errormsg = validateEmptyField("regPlaceInOfficialLang", "errorRegistrationPlace", errormsg);
        errormsg = validateEmptyField("regNameInOfficialLang", "errorRegistrarName", errormsg);
        errormsg = isDate("registrationDatePicker", "errorRegistrationDate", errormsg);
        return errormsg;
    }


</script>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage cssStyle="color:blue;font-size:10pt"/>
<div class="marriage-notice-outer">
<s:form method="post" enctype="multipart/form-data" onsubmit="javascript:return validateMarriageDetails()">
<s:hidden name="idUKey"/>
<s:hidden name="editMode"/>
<s:hidden name="mode" id="mode"/>
<table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="5px">
    <caption></caption>
    <col width="200px"/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td>
            <label>
                    <span class="font-8">විවාහ දිනය
                        <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                        <br>in tamil<br>Date of Marriage</span>
            </label>
        </td>
        <td>
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
            <s:textfield name="marriage.dateOfMarriage" id="marriageDatePick" maxLength="10"
                         onmouseover="datepicker('marriageDatePick')"/>
        </td>
        <td><label><span class="font-8">රෙජිස්ට්‍රාර්ගේ/දේවගැතිගේ අනන්‍යතා අංකය
                    <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                    <br>அடையாள எண் <br>Identification number of Registrar/Minister</span></label>
        </td>
        <td>
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="NIC_V" onclick="javascript:addXorV('regPIN','V','errorOnXOrVOfPIN');">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="NIC_X" onclick="javascript:addXorV('regPIN','X','errorOnXOrVOfPIN');">
            <br>
            <s:if test="marriage.registrarOrMinisterPIN==0">
                <s:textfield name="marriage.registrarOrMinisterPIN" id="regPIN" maxLength="10" value=""/>
            </s:if>
            <s:else>
                <s:textfield name="marriage.registrarOrMinisterPIN" id="regPIN" maxLength="10"/>
            </s:else>
            <img src="<s:url value='/images/search-father.png' />"
                 style="vertical-align:middle;" id="registrar_lookup"
                 onmouseover="registrarLookup('regPIN')">
        </td>
    </tr>
    <tr>
        <td>
            විවාහ ස්ථානයේ ස්වභාවය
            <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
            <br>in tamil
            <br>Type of Marriage Place
        </td>
        <td colspan="3">
            <s:radio id="typeOfMarriagePlace" name="marriage.typeOfMarriagePlace" list="typeOfMarriagePlaceList"
                     listValue="type"
                     theme="horizontal"/>
            <br>
        </td>
    </tr>
    <tr>
        <td>
            දිස්ත්‍රික්කය
            <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
            <br>மாவட்டம்
            <br>District
        </td>
        <td>
            <s:select id="districtId" name="marriageDistrictId" list="districtList"
                      value="marriageDistrictId"
                      cssStyle="width:98.5%; width:240px;"
                      onchange="populateDSDivisions('districtId','dsDivisionId','mrDivisionId', 'Marriage', false)"/>
        </td>
        <td><label><span class="font-8">
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය
                <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                <br>பிரதேச செயளாளர் பிரிவு <br>Divisional Secretariat</span>
        </label>
        </td>
        <td>
            <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="dsDivisionId"
                      cssStyle="width:98.5%; width:240px;"
                      onchange="populateDivisions('dsDivisionId', 'mrDivisionId', 'Marriage', false)"/>
        </td>
    </tr>
    <tr>
        <td><label><span class="font-8">
        ලියාපදිංචි කිරීමේ කොට්ඨාශය
               <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                    <br>பதிவுப் பிரிவு  <br>Registration Division</span>
        </label>
        </td>
        <td colspan="3">
            <s:select id="mrDivisionId" name="mrDivisionId" list="mrDivisionList"
                      value="marriage.mrDivision.mrDivisionUKey" headerKey="1"
                      cssStyle="width:98.5%; width:240px;"/>
        </td>
    </tr>
    <tr>
        <td>
            විවාහය සිදු කල ස්ථානය<br>
            in tamil <br>
            place of Marriage
        </td>
        <td colspan="3">
            <table width="100%">
                <caption/>
                <col width="150px"/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        රාජ්‍ය භාෂාවෙන්
                        <s:label value="*" cssStyle="color:red;font-size:10pt;"/><br>
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
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;"
                             id="place" onclick="transliterate('regPlaceInOfficialLang', 'regPlaceInEnglishLang')">
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
        <td colspan="3">
            <table width="100%">
                <caption/>
                <col width="150px"/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        රාජ්‍ය භාෂාවෙන්
                        <s:label value="*" cssStyle="color:red;font-size:10pt;"/><br>
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
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;"
                             id="regName" onclick="transliterate('regNameInOfficialLang', 'regNameInEnglishLang')">
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<table border="1" style="margin-top:15px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="5px">
    <caption></caption>
    <col width="200px"/>
    <col/>
    <tbody>
    <tr>
        <td>
            විවාහයේ ස්වභාවය
            <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
            <br>type of marriage in tamil
            <br>Type of Marriage
        </td>
        <td>
            <table width="100%">
                <caption/>
                <col/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:if test="mode=='register'">
                            <s:radio name="marriage.typeOfMarriage" list="marriageTypeList" listValue="type"
                                     theme="horizontal" disabled="true"/>
                        </s:if>
                        <s:else>
                            <s:radio name="marriage.typeOfMarriage" list="marriageTypeList" listValue="type"
                                     theme="horizontal"/>
                        </s:else>
                        <br>
                    </td>
                </tr>
                </tbody>
            </table>
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
    <%--section heading Male/Female details --%>
<tr>
    <td>&nbsp;</td>
    <td align="center">
        පුරුෂ පාර්ශ්වය / in tamil / Male Party
    </td>
    <td align="center">
        ස්ත්‍රී පාර්ශ්වය / in tamil / Female Party
    </td>
</tr>
<tr>
    <td colspan="1">
        අනන්‍යතා අංකය
        <br>
        அடையாள எண் <br>
        Identification Number.
    </td>
    <td colspan="1" align="left">
        <s:if test="mode=='register'">
            <s:textfield name="marriage.male.identificationNumberMale" id="malePIN" maxLength="10" disabled="true"/>
        </s:if>
        <s:else>
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="NIC_V" onclick="javascript:addXorV('malePIN','V','errorOnXOrVOfPIN');">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="NIC_X" onclick="javascript:addXorV('malePIN','X','errorOnXOrVOfPIN');">
            <br>
            <s:textfield name="marriage.male.identificationNumberMale" id="malePIN" maxLength="10"/>
            <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;" id="male_lookup"
                 onclick="personLookup('malePIN', 'Male');">
        </s:else>
    </td>
    <td colspan="1" align="left">
        <s:if test="mode=='register'">
            <s:textfield name="marriage.female.identificationNumberFemale" id="femalePIN" maxLength="10"
                         disabled="true"/>
        </s:if>
        <s:else>
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="NIC_V" onclick="javascript:addXorV('femalePIN','V','errorOnXOrVOfPIN');">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="NIC_X" onclick="javascript:addXorV('femalePIN','X','errorOnXOrVOfPIN');">
            <br>
            <s:textfield name="marriage.female.identificationNumberFemale" id="femalePIN" maxLength="10"/>
            <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;" id="female_lookup"
                 onclick="personLookup('femalePIN', 'Female');">
        </s:else>
    </td>
</tr>
<tr>
    <td colspan="1">
        උපන් දිනය<br>
        பிறந்த திகதி <br>
        Date of Birth
    </td>
    <td colspan="1">
        <s:if test="mode=='register'">
            <s:textfield name="marriage.male.dateOfBirthMale" id="dateOfBirthMale" maxLength="10" disabled="true"/>
        </s:if>
        <s:else>
            <s:textfield name="marriage.male.dateOfBirthMale" id="dateOfBirthMale" maxLength="10"
                         onmouseover="datepicker('dateOfBirthMale')"
                         onchange="calculateAge('dateOfBirthMale','ageMale');"/>
        </s:else>


    </td>
    <td colspan="1">
        <s:if test="mode=='register'">
            <s:textfield name="marriage.female.dateOfBirthFemale" id="dateOfBirthFemale" maxLength="10"
                         disabled="true"/>
        </s:if>
        <s:else>
            <s:textfield name="marriage.female.dateOfBirthFemale" id="dateOfBirthFemale" maxLength="10"
                         onmouseover="datepicker('dateOfBirthFemale')"
                         onchange="calculateAge('dateOfBirthFemale','ageFemale');"/>
        </s:else>
    </td>
</tr>
<tr>
    <td colspan="1">
        පසුවූ උපන් දිනයට වයස
        <s:label value="*" cssStyle="color:red;font-size:10pt;"/><br>
        in tamil <br>
        Age at last Birthday

    </td>
    <td colspan="1">
        <s:if test="mode=='register'">
            <s:textfield name="marriage.male.ageAtLastBirthDayMale" id="ageMale" maxLength="3" disabled="true"/>
        </s:if>
        <s:else>
            <s:if test="marriage.male.ageAtLastBirthDayMale==0">
                <s:textfield name="marriage.male.ageAtLastBirthDayMale" id="ageMale" maxLength="3" value=""
                             onfocus="calculateAge('dateOfBirthMale','ageMale');"/>
            </s:if>
            <s:else>
                <s:textfield name="marriage.male.ageAtLastBirthDayMale" id="ageMale" maxLength="3"
                             onfocus="calculateAge('dateOfBirthMale','ageMale');"/>
            </s:else>
        </s:else>
    </td>
    <td colspan="1">
        <s:if test="mode=='register'">
            <s:textfield name="marriage.female.ageAtLastBirthDayFemale" id="ageFemale" maxLength="3" disabled="true"/>
        </s:if>
        <s:else>
            <s:if test="marriage.female.ageAtLastBirthDayFemale==0">
                <s:textfield name="marriage.female.ageAtLastBirthDayFemale" id="ageFemale" maxLength="3"
                             value="" onfocus="calculateAge('dateOfBirthFemale','ageFemale');"/>
            </s:if>
            <s:else>
                <s:textfield name="marriage.female.ageAtLastBirthDayFemale" id="ageFemale" maxLength="3"
                             onfocus="calculateAge('dateOfBirthFemale','ageFemale');"/>
            </s:else>
        </s:else>

    </td>
</tr>
<tr>
    <td>
        ජාතිය
        <s:label value="*" cssStyle="color:red;font-size:10pt;"/><br>
        Race <br>

    </td>
    <td>
        <s:if test="mode=='register'">
            <s:select id="raceMale" list="raceList" name="marriage.male.maleRace.raceId" headerKey="0"
                      headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;" disabled="true"/>
        </s:if>
        <s:else>
            <s:select id="raceMale" list="raceList" name="marriage.male.maleRace.raceId" headerKey="0"
                      headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;"/>
        </s:else>
    </td>
    <td>
        <s:if test="mode=='register'">
            <s:select id="raceFemale" list="raceList" name="marriage.female.femaleRace.raceId" headerKey="0"
                      headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;" disabled="true"/>
        </s:if>
        <s:else>
            <s:select id="raceFemale" list="raceList" name="marriage.female.femaleRace.raceId" headerKey="0"
                      headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;"/>
        </s:else>


    </td>
</tr>
<tr>
    <td>
        සිවිල් තත්වය
        <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
        <br> சிவில் நிலைமை
        <br> Civil Status
    </td>
    <td>
        <table>
            <caption/>
            <col/>
            <tbody>
            <tr>
                <td>
                    <s:if test="mode=='register'">
                        <s:radio name="marriage.male.civilStatusMale" list="civilStatusMale" theme="horizontal"
                                 disabled="true"/>
                    </s:if>
                    <s:else>
                        <s:radio name="marriage.male.civilStatusMale" list="civilStatusMale" theme="horizontal"/>
                    </s:else>
                    <br>
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
                    <s:if test="mode=='register'">
                        <s:radio name="marriage.female.civilStatusFemale" list="civilStatusFemale" theme="horizontal"
                                 disabled="true"/>
                    </s:if>
                    <s:else>
                        <s:radio name="marriage.female.civilStatusFemale" list="civilStatusFemale" theme="horizontal"/>
                    </s:else>

                </td>
            </tr>
            </tbody>
        </table>
    </td>
</tr>
<tr>
    <td>
        නම රාජ්‍ය භාෂාවෙන්
        (සිංහල / දෙමළ)
        <s:label value="*" cssStyle="color:red;font-size:10pt;"/><br>
        பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்) <br>
        Name in official language (Sinhala / Tamil)
    </td>
    <td>
        <s:if test="mode=='register'">
            <s:textarea name="marriage.male.nameInOfficialLanguageMale" id="nameOfficialMale" cssStyle="width:98.2%;"
                        rows="5" disabled="true"/>
        </s:if>
        <s:else>
            <s:textarea name="marriage.male.nameInOfficialLanguageMale" id="nameOfficialMale" cssStyle="width:98.2%;"
                        rows="5"/>
        </s:else>
    </td>
    <td>
        <s:if test="mode=='register'">
            <s:textarea name="marriage.female.nameInOfficialLanguageFemale" id="nameOfficialFemale"
                        cssStyle="width:98.2%;" rows="5" disabled="true"/>
        </s:if>
        <s:else>
            <s:textarea name="marriage.female.nameInOfficialLanguageFemale" id="nameOfficialFemale"
                        cssStyle="width:98.2%;" rows="5"/>
        </s:else>
    </td>
</tr>

<tr>
    <td>
        නම ඉංග්‍රීසි භාෂාවෙන් <br>
        பெயர் ஆங்கில மொழியில் <br>
        Name in English
    </td>
    <td>
        <s:if test="mode=='register'">
            <s:textarea name="marriage.male.nameInEnglishMale" id="name_english_male" cssStyle="width:98.2%;"
                        rows="5" disabled="true"/>
        </s:if>
        <s:else>
            <s:textarea name="marriage.male.nameInEnglishMale" id="name_english_male" cssStyle="width:98.2%;"
                        rows="5"/>
            <br>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;"
                 id="maleName" onclick="transliterate('nameOfficialMale', 'name_english_male')">
        </s:else>
    </td>
    <td>
        <s:if test="mode=='register'">
            <s:textarea name="marriage.female.nameInEnglishFemale" id="name_english_female" cssStyle="width:98.2%;"
                        rows="5" disabled="true"/>
        </s:if>
        <s:else>
            <s:textarea name="marriage.female.nameInEnglishFemale" id="name_english_female" cssStyle="width:98.2%;"
                        rows="5"/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;"
                 id="femaleName" onclick="transliterate('nameOfficialFemale', 'name_english_female')">
        </s:else>
    </td>
</tr>
<tr>
    <td>
        පදිංචි ලිපිනය
        <s:label value="*" cssStyle="color:red;font-size:10pt;"/> <br>
        தற்போதைய வதிவிட முகவரி <br>
        Resident Address
    </td>
    <td>
        <s:if test="mode=='register'">
            <s:textarea name="marriage.male.residentAddressMaleInOfficialLang" id="addressMale"
                        cssStyle="width:98.2%;" rows="5" disabled="true"/>
        </s:if>
        <s:else>
            <s:textarea name="marriage.male.residentAddressMaleInOfficialLang" id="addressMale"
                        rows="5" cssStyle="width:98.2%;"/>
        </s:else>
    </td>
    <td>
        <s:if test="mode=='register'">
            <s:textarea name="marriage.female.residentAddressFemaleInOfficialLang" id="addressFemale"
                        rows="5" cssStyle="width:98.2%;" disabled="true"/>
        </s:if>
        <s:else>
            <s:textarea name="marriage.female.residentAddressFemaleInOfficialLang" id="addressFemale"
                        rows="5" cssStyle="width:98.2%;"/>
        </s:else>
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
                        <s:label value="*"
                                 cssStyle="color:red;font-size:10pt;"/> <br>தொடர் இலக்கம்<br>Serial Number</span></label>
        </td>
        <td>
                <%-- <s:if test="idUKey==0">
                   <s:textfield name="idUKey" id="idUKey" maxLength="10" disabled="true" value=""/>
               </s:if>
               <s:else>
                   <s:textfield name="idUKey" id="idUKey" maxLength="10" disabled="true"/>
               </s:else> --%>
            <s:if test="mode=='register'">
                <s:textfield name="idUKey" id="idUKey" maxLength="10" disabled="true"/>
            </s:if>
            <s:else>
                <s:if test="marriage.serialNumber==0">
                    <s:textfield name="marriage.serialNumber" id="serialNumber" maxLength="10" value=""/>
                </s:if>
                <s:else>
                    <s:textfield name="marriage.serialNumber" id="serialNumber" maxLength="10"/>
                </s:else>
            </s:else>
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
            <s:textfield name="marriage.registrationDate" id="registrationDatePicker" maxLength="10"
                         onmouseover="datepicker('registrationDatePicker')"/>
        </td>
    </tr>
    <tr>
        <td class="font-8">
            සහතිකය නිකුත් කල යුතු භාෂාව
            <br>நபரின் பதிவிற்கான சான்றிதழினை வழங்கப்படவேண்டிய மொழி
            <br>Preferred Language for Marriage Certificate
        </td>
        <td colspan="3">
            <s:radio name="marriage.preferredLanguage" list="languageList" theme="verticle"/>
        </td>
    </tr>

    <tr>
        <td class="font-8">
            පරිලෝකනය කරන ලද පිටපත
            <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
            <br>Scanned Image in ta
            <br>Scanned Image
        </td>
        <td colspan="3">
            <s:file name="scannedImage" id="scannedImage"/>
        </td>
    </tr>
    <s:if test="mode=='reject'">
        <tr>
            <td class="font-8">
                අදහස් දක්වන්න
                <br>Comment in ta
                <br>Comment
            </td>
            <td colspan="3">
                <s:textarea name="comment" id="registrationRejectComment"
                            cssStyle="width:98.2%;"/>
            </td>
        </tr>
    </s:if>
</table>
<div class="form-submit">
    <s:if test="idUKey==0">
        <s:submit action="eprRegisterNewMarriage" value="%{getText('button.marriageregister.register')}"/>
        <s:if test="!#session.user_bean.role.roleId.equals('DEO')">
            <s:submit action="eprRegisterAndApproveNewMarriage"
                      value="%{getText('button.marriageregister.registerandapprove')}"/>
        </s:if>
    </s:if>
    <s:else>
        <s:if test="mode=='register'">
            <s:submit action="eprRegisterNoticedMarriage" value="%{getText('button.marriageregister.register')}"/>
        </s:if>
        <%-- todo : to be removed--%>
        <s:elseif test="mode=='reject'">
            <s:submit action="eprRejectMarriageRegistration" value="%{getText('button.reject')}"/>
        </s:elseif>
        <s:else>
            <s:submit action="eprUpdateMarriage" value="%{getText('button.marriageregister.update')}"/>
            <s:if test="!#session.user_bean.role.roleId.equals('DEO')">
                <s:submit action="eprUpdateAndApproveMuslimMarriage"
                          value="%{getText('button.marriageregister.updateandapprove')}"/>
            </s:if>
        </s:else>
    </s:else>
</div>
</s:form>
<s:hidden id="errorOnXOrVOfPIN" value="%{getText('NIC.error.add.VX')}"/>

<s:hidden id="errorMarriageDate"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_marriagedate')}"/>
<s:hidden id="errorRegistrationDate"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_registrationdate')}"/>
<s:hidden id="errorRegistrarPIN"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_registrarPIN')}"/>
<s:hidden id="errorRegistrarName"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_registrarName')}"/>
<s:hidden id="errorRegistrationPlace"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_registrationPlace')}"/>

<%--
<s:hidden id="errorMalePIN" value="%{getText('error.invalid') + getText('error_js_marriageregister_malePIN')}"/>
<s:hidden id="errorFemalePIN" value="%{getText('error.invalid') + getText('error_js_marriageregister_femalePIN')}"/>

<s:hidden id="errorDateOfBirthMale"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_DateOfBirthMale')}"/>
<s:hidden id="errorDateOfBirthFemale"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_dateOfBirthFemale')}"/>

 --%>
<s:hidden id="errorAgeMale" value="%{getText('error.invalid') + getText('error_js_marriageregister_ageMale')}"/>
<s:hidden id="errorAgeFemale" value="%{getText('error.invalid') + getText('error_js_marriageregister_ageFemale')}"/>
<s:hidden id="errorNameOfficialMale"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_nameOfficialMale')}"/>
<s:hidden id="errorNameOfficialFemale"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_nameOfficialFemale')}"/>
<s:hidden id="errorAddressMale" value="%{getText('error.invalid') + getText('error_js_marriageregister_addressMale')}"/>
<s:hidden id="errorAddressFemale"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_addressFemale')}"/>

<s:hidden id="errorMaleRace" value="%{getText('error.invalid') + getText('error_js_marriageregister_maleRace')}"/>
<s:hidden id="errorFemaleRace" value="%{getText('error.invalid') + getText('error_js_marriageregister_femaleRace')}"/>

<s:hidden id="errorSerialNumber"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_serialNumber')}"/>

<s:hidden id="errorscannedImage"
          value="%{getText('error.invalid') + getText('error_js_marriageregister_scannedImage')}"/>

</div>