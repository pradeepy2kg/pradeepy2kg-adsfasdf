<%--@author amith jayasekara--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
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

    $(function() {
        $("#date_of_birth_male").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $("#date_of_birth_female").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $('select#districtId').bind('change', function(evt1) {
        var id = $("select#districtId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:8},
                function(data) {
                    var options1 = '';
                    var ds = data.dsDivisionList;
                    for (var i = 0; i < ds.length; i++) {
                        options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                    }
                    $("select#dsDivisionId").html(options1);

                    var options2 = '';
                    var bd = data.mrDivisionList;
                    for (var j = 0; j < bd.length; j++) {
                        options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                    }
                    $("select#mrDivisionId").html(options2);
                });
    });

    $('select#dsDivisionId').bind('change', function(evt2) {
        var id = $("select#dsDivisionId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:7},
                function(data) {
                    var options = '';
                    var bd = data.mrDivisionList;
                    for (var i = 0; i < bd.length; i++) {
                        options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                    }
                    $("select#mrDivisionId").html(options);
                });
    });
</script>

<div class="marriage-notice-outer">
<s:form action="eprMarriageNoticeAdd" method="post">
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
                            <s:textfield name="marriageNotice.serialNumber" id="mnSerial" readonly="true" maxLength="10"
                                         cssStyle="margin-left:20px"/>
                        </s:if>
                        <s:else>
                            <s:textfield name="marriageNotice.serialNumber" id="mnSerial" maxLength="10"/>
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
                        <s:textfield name="marriageNotice.receivedDate" id="submitDatePicker" maxLength="10"/>
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

<%--type of marriage--%>

<table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="5px">
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
            <s:radio name="marriageNotice.typeOfMarriage" list="#@java.util.HashMap@{'GENERAL':''}" value="true"/>
        </td>
    </tr>
    <tr>
        <td colspan="6">
            උඩරට බින්න /
            Kandyan binna in tamil /
            Kandyan Binna
        </td>
        <td colspan="2" align="center">
            <s:radio name="marriageNotice.typeOfMarriage" list="#@java.util.HashMap@{'KANDYAN_BINNA':''}" value="true"/>
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
            <s:radio name="marriageNotice.typeOfMarriage" list="#@java.util.HashMap@{'KANDYAN_DEEGA':''}" value="true"/>
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
            <s:radio name="marriageNotice.placeOfMarriage" list="#@java.util.HashMap@{'REGISTRAR_OFFICE':''}"
                     value="true"/>
        </td>
        <td>
            ප්‍රා. ලේ. කන්තෝරුව <br>
            DS Office <br>
        </td>
        <td align="center">
            <s:radio name="marriageNotice.placeOfMarriage" list="#@java.util.HashMap@{'DS_OFFICE':''}" value="true"/>
        </td>
        <td>
            දේවස්ථානය <br>
            Church <br>
        </td>
        <td align="center">
            <s:radio name="marriageNotice.placeOfMarriage" list="#@java.util.HashMap@{'CHURCH':''}" value="true"/>
        </td>
        <td>වෙනත් <br>
            Other
        </td>
        <br>
        <td align="center">
            <s:radio name="marriageNotice.placeOfMarriage" list="#@java.util.HashMap@{'OTHER':''}" value="true"/>
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
       cellpadding="5px">
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
        <td colspan="1" rowspan="2" align="left">
            <s:textfield name="marriageNotice.male.identificationNumberMale" id="identification_male" maxLength="10"/>
        </td>
        <td colspan="1">
            උපන් දිනය <br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td colspan="1">
            <s:textfield name="marriageNotice.male.dateOfBirthMale" id="date_of_birth_male" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            පසුවූ උපන් දිනයට වයස <br>
            in tamil <br>
            Age at last Birthday

        </td>
        <td colspan="1">
            <s:textfield name="marriageNotice.male.ageAtLastBirthDayMale" id="age_at_last_bd_male" maxLength="10"/>
        </td>
    </tr>

    <tr>
        <td>
            නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <br>
            பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="3">
            <s:textarea name="marriageNotice.male.nameInOfficialLanguageMale" id="name_official_male"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>

    <tr>
        <td>
            නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பெயர் ஆங்கில மொழியில் <br>
            Name in English
        </td>
        <td colspan="3">
            <s:textarea name="marriageNotice.male.nameInEnglishMale" id="name_english_male" cssStyle="width:98.2%;"/>
        </td>
    </tr>

    <tr>
        <td>
            පදිංචි ලිපිනය <br>
            தற்போதைய வதிவிட முகவரி <br>
            Resident Address
        </td>
        <td colspan="3">
            <s:textarea name="marriageNotice.male.residentAddressMale" id="address_male" cssStyle="width:98.2%;"/>
        </td>
    </tr>

    <tr>
        <td>
            දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td>
            <s:select id="districtId" name="marriageDistrictId" list="districtList" value="marriageDistrictId"
                      cssStyle="width:98.5%; width:240px;"/>
        </td>
        <td>
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரதேச செயளாளர் பிரிவு <br>
            Divisional Secretariat
        </td>
        <td>
            <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="dsDivisionId"
                      cssStyle="width:98.5%; width:240px;"/>
        </td>
    </tr>
    <tr>
        <td>
            ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
            பதிவுப் பிரிவு <br>
            Registration Division
        </td>
        <td>
            <s:select id="mrDivisionId" name="marriageDivisionId" list="mrDivisionList" value="marriageDivisionId"
                      cssStyle="width:98.5%; width:240px;"/>
        </td>
        <td>
            පදිංචි කාලය <br>
            தற்போதைய <br>
            Duration
        </td>
        <td>
            <s:textfield name="marriageNotice.male.durationMale" id="duration_male" cssStyle="width:98.2%;"
                         maxLength="3"/>
        </td>
    </tr>
    <tr>
        <td>
            තරාතිරම නොහොත් රක්ෂාව <br>
            Rank or Profession <br>
        </td>
        <td>
            <s:textfield name="marriageNotice.male.rankOrProfessionMale" id="rank_male"
                         cssStyle="width:98.2%;"
                         maxLength="10"/>
        </td>
        <td>
            ජාතිය <br>
            Race <br>

        </td>
        <td>
                <%--        todo--%>
        </td>
    </tr>
    <tr>
        <td>
            දුරකථන අංක <br>
            தொலைபேசி இலக்கம் <br>
            Telephone Numbers
        </td>
        <td>
            <s:textfield name="marriageNotice.male.tpNumberMale" id="tp_number_male" cssStyle="width:98.2%;"
                         maxLength="10"/>
        </td>
        <td>
            ඉ – තැපැල් <br>
            மின்னஞ்சல் <br>
            Email
        </td>
        <td><s:textfield name="marriageNotice.male.emailMale" id="email_male" cssStyle="width:98.2%;"/></td>
    </tr>
    </tbody>
</table>
<table border="2" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="5px">
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
            <s:radio name="marriageNotice.male.civilStatusMale" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"
                     value="true"/>
        </td>
        <td>
            දික්කසාද <br>
            திருமணம் தள்ளுபடி செய்தவர் <br>
            Divorced
        </td>
        <td align="center">
            <s:radio name="marriageNotice.male.civilStatusMale" list="#@java.util.HashMap@{'DIVORCED':''}"
                     value="true"/>
        </td>
        <td>
            වැන්දබු <br>
            விதவை <br>
            Widowed
        </td>
        <td align="center">
            <s:radio name="marriageNotice.male.civilStatusMale" list="#@java.util.HashMap@{'WIDOWED':''}" value="true"/>
        </td>
        <td>
            නිෂ්ප්‍රභාකර ඇත <br>
            தள்ளிவைத்தல் <br>
            Anulled
        </td>
        <td align="center">
            <s:radio name="marriageNotice.male.civilStatusMale" list="#@java.util.HashMap@{'ANULLED':''}" value="true"/>
        </td>
    </tr>

    <tr>
        <td colspan="2">
            පියාගේ අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය <br>
            தந்தையின் அடையாள எண் அல்லது தேசிய அடையாள அட்டை இலக்கம் <br>
            Fathers Identification Number (PIN) or NIC
        </td>
        <td colspan="7" align="left">
            <s:textfield name="marriageNotice.male.fatherIdentificationNumberMale" id="father_pin_or_nic_male"
                         cssStyle="width:240px;" maxLength="10"/>
        </td>

    </tr>
    <tr>
        <td colspan="2">
            පියාගේ සම්පුර්ණ නම <br>
            தந்தையின் அடையாள <br>
            Fathers full name
        </td>
        <td colspan="7">
            <s:textarea name="marriageNotice.male.fatherFullNameMale" id="father_full_name_male"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            පියාගේ තරාතිරම නොහොත් රක්ෂාව <br>
            தந்தையின் அடையாள <br>
            Fathers rank or profession
        </td>
        <td colspan="7">
            <s:textarea name="marriageNotice.male.fatherRankOrProfessionMale" id="father_rank_male"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            යමෙකු විසින් කැමැත්ත දුන්නා නම්, ඒ කා විසින්ද යන වග <br>
            in tamil <br>
            Consent if any, by whom given
        </td>
        <td colspan="7">
            <s:textarea name="marriageNotice.male.consentIfAnyMale" id="consent_if_any_male" cssStyle="width:98.2%;"/>
        </td>
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
       cellpadding="5px">
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
        <td colspan="1" rowspan="2" align="left">
            <s:textfield name="marriageNotice.female.identificationNumberFemale" id="identification_female"
                         maxLength="10"/>
        </td>
        <td colspan="1">
            උපන් දිනය <br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td colspan="1">
            <s:textfield name="marriageNotice.female.dateOfBirthFemale" id="date_of_birth_female" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            පසුවූ උපන් දිනයට වයස <br>
            in tamil <br>
            Age at last Birthday

        </td>
        <td colspan="1">
            <s:textfield name="marriageNotice.female.ageAtLastBirthDayFemale" id="age_at_last_bd_female"
                         maxLength="10"/>
        </td>
    </tr>

    <tr>
        <td>
            නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <br>
            பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="3">
            <s:textarea name="marriageNotice.female.nameInOfficialLanguageFemale" id="name_official_female"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>

    <tr>
        <td>
            නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பெயர் ஆங்கில மொழியில் <br>
            Name in English
        </td>
        <td colspan="3">
            <s:textarea name="marriageNotice.female.nameInEnglishFemale" id="name_english_female"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>

    <tr>
        <td>
            පදිංචි ලිපිනය <br>
            தற்போதைய வதிவிட முகவரி <br>
            Resident Address
        </td>
        <td colspan="3">
            <s:textarea name="marriageNotice.female.residentAddressFemale" id="address_female" cssStyle="width:98.2%;"/>
        </td>
    </tr>

    <tr>
        <td>
            දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td>
            <s:select id="districtId" name="marriageDistrictIdFemale" list="districtList" value="marriageDistrictId"
                      cssStyle="width:98.5%; width:240px;"/>
        </td>
        <td>
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரதேச செயளாளர் பிரிவு <br>
            Divisional Secretariat
        </td>
        <td>
            <s:select id="dsDivisionId" name="dsDivisionIdFemale" list="dsDivisionList" value="dsDivisionId"
                      cssStyle="width:98.5%; width:240px;"/>
        </td>
    </tr>
    <tr>
        <td>
            ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
            பதிவுப் பிரிவு <br>
            Registration Division
        </td>
        <td>
            <s:select id="mrDivisionId" name="marriageDivisionIdFemale" list="mrDivisionList" value="marriageDivisionId"
                      cssStyle="width:98.5%; width:240px;"/>
        </td>
        <td>
            පදිංචි කාලය <br>
            தற்போதைய <br>
            Duration
        </td>
        <td>
            <s:textfield name="marriageNotice.female.durationFemale" id="duration_female" cssStyle="width:98.2%;"
                         maxLength="3"/>
        </td>
    </tr>
    <tr>
        <td>
            තරාතිරම නොහොත් රක්ෂාව <br>
            Rank or Profession <br>
        </td>
        <td>
            <s:textfield name="marriageNotice.female.rankOrProfessionFemale" id="rank_female"
                         cssStyle="width:98.2%;"
                         maxLength="10"/>
        </td>
        <td>
            ජාතිය <br>
            Race <br>

        </td>
        <td>
                <%--        todo--%>
        </td>
    </tr>
    <tr>
        <td>
            දුරකථන අංක <br>
            தொலைபேசி இலக்கம் <br>
            Telephone Numbers
        </td>
        <td>
            <s:textfield name="marriageNotice.female.tpNumberFemale" id="tp_number_female" cssStyle="width:98.2%;"
                         maxLength="10"/>
        </td>
        <td>
            ඉ – තැපැල් <br>
            மின்னஞ்சல் <br>
            Email
        </td>
        <td><s:textfield name="marriageNotice.female.emailFemale" id="email_female" cssStyle="width:98.2%;"/></td>
    </tr>
    </tbody>
</table>
<table border="2" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="5px">
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
            <s:radio name="marriageNotice.female.civilStatusFemale" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"
                     value="true"/>
        </td>
        <td>
            දික්කසාද <br>
            திருமணம் தள்ளுபடி செய்தவர் <br>
            Divorced
        </td>
        <td align="center">
            <s:radio name="marriageNotice.female.civilStatusFemale" list="#@java.util.HashMap@{'DIVORCED':''}"
                     value="true"/>
        </td>
        <td>
            වැන්දබු <br>
            விதவை <br>
            Widowed
        </td>
        <td align="center">
            <s:radio name="marriageNotice.female.civilStatusFemale" list="#@java.util.HashMap@{'WIDOWED':''}"
                     value="true"/>
        </td>
        <td>
            නිෂ්ප්‍රභාකර ඇත <br>
            தள்ளிவைத்தல் <br>
            Anulled
        </td>
        <td align="center">
            <s:radio name="marriageNotice.female.civilStatusFemale" list="#@java.util.HashMap@{'ANULLED':''}"
                     value="true"/>
        </td>
    </tr>

    <tr>
        <td colspan="2">
            පියාගේ අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය <br>
            தந்தையின் அடையாள எண் அல்லது தேசிய அடையாள அட்டை இலக்கம் <br>
            Fathers Identification Number (PIN) or NIC
        </td>
        <td colspan="7" align="left">
            <s:textfield name="marriageNotice.female.fatherIdentificationNumberFemale" id="father_pin_or_nic_female"
                         cssStyle="width:240px;" maxLength="10"/>
        </td>

    </tr>
    <tr>
        <td colspan="2">
            පියාගේ සම්පුර්ණ නම <br>
            தந்தையின் அடையாள <br>
            Fathers full name
        </td>
        <td colspan="7">
            <s:textarea name="marriageNotice.female.fatherFullNameFemale" id="father_full_name_female"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            පියාගේ තරාතිරම නොහොත් රක්ෂාව <br>
            தந்தையின் அடையாள <br>
            Fathers rank or profession
        </td>
        <td colspan="7">
            <s:textarea name="marriageNotice.female.fatherRankOrProfessionFemale" id="father_rank_female"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            යමෙකු විසින් කැමැත්ත දුන්නා නම්, ඒ කා විසින්ද යන වග <br>
            in tamil <br>
            Consent if any, by whom given
        </td>
        <td colspan="7">
            <s:textarea name="marriageNotice.female.consentIfAnyFemale" id="consent_if_any_female"
                        cssStyle="width:98.2%;"/>
        </td>
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
<s:if test="secondNotice">
    <%--this is second notice--%>
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
                <s:textfield name="witness1OfSecondNotice.identificationNumber" id="witness_1_pin"
                             cssStyle="width:240px;"
                             maxLength="10"/>
            </td>
            <td>
                <s:textfield name="witness2OfSecondNotice.identificationNumber" id="witness_2_pin"
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
                <s:textarea name="witness1OfSecondNotice.fullName" id="witness_1_full_name"
                            cssStyle="width:98.2%;"
                        />
            </td>
            <td>
                <s:textarea name="witness2OfSecondNotice.fullName" id="witness_2_full_name"
                            cssStyle="width:98.2%;"
                        />
            </td>
        </tr>
        <tr>
            <td>
                තරාතිරම හෝ රක්ෂාව <br>
                Rank or Profession <br>
            </td>
            <td>
                <s:textarea name="witness1OfSecondNotice.rankOrProfession" id="witness_1_rank"
                            cssStyle="width:98.2%;"
                        />
            </td>
            <td>
                <s:textarea name="witness2OfSecondNotice.rankOrProfession" id="witness_2_rank"
                            cssStyle="width:98.2%;"
                        />
            </td>
        </tr>
        <tr>
            <td>
                පදිංචි ස්ථානය <br>
                Place of Residence <br>
            </td>
            <td>
                <s:textarea name="witness1OfSecondNotice.address" id="witness_1_place_residence"
                            cssStyle="width:98.2%;"
                        />
            </td>
            <td>
                <s:textarea name="witness2OfSecondNotice.address" id="witness_2_place_residence"
                            cssStyle="width:98.2%;"
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
    <%--todo hidden--%>
</s:if><s:else>
    <%--this is first notice--%>
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
                <s:textfield name="witness1OfFirstNotice.identificationNumber" id="witness_1_pin"
                             cssStyle="width:240px;"
                             maxLength="10"/>
            </td>
            <td>
                <s:textfield name="witness2OfFirstNotice.identificationNumber" id="witness_2_pin"
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
                <s:textarea name="witness1OfFirstNotice.fullName" id="witness_1_full_name" cssStyle="width:98.2%;"
                        />
            </td>
            <td>
                <s:textarea name="witness2OfFirstNotice.fullName" id="witness_2_full_name" cssStyle="width:98.2%;"
                        />
            </td>
        </tr>
        <tr>
            <td>
                තරාතිරම හෝ රක්ෂාව <br>
                Rank or Profession <br>
            </td>
            <td>
                <s:textarea name="witness1OfFirstNotice.rankOrProfession" id="witness_1_rank" cssStyle="width:98.2%;"
                        />
            </td>
            <td>
                <s:textarea name="witness2OfFirstNotice.rankOrProfession" id="witness_2_rank" cssStyle="width:98.2%;"
                        />
            </td>
        </tr>
        <tr>
            <td>
                පදිංචි ස්ථානය <br>
                Place of Residence <br>
            </td>
            <td>
                <s:textarea name="witness1OfFirstNotice.address" id="witness_1_place_residence" cssStyle="width:98.2%;"
                        />
            </td>
            <td>
                <s:textarea name="witness2OfFirstNotice.address" id="witness_2_place_residence" cssStyle="width:98.2%;"
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
</s:else>

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
            <s:textfield name="noticeWitness_1.identificationNumberWitness1" id="witness_1_pin" cssStyle="width:240px;"
                         maxLength="10"/>
        </td>
        <td>
            <s:textfield name="noticeWitness_2.identificationNumberWitness2" id="witness_2_pin" cssStyle="width:240px;"
                         maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td>
            සම්පුර්ණ නම <br>
            Full Name <br>
        </td>
        <td>
            <s:textarea name="noticeWitness_1.fullNameWitness1" id="witness_1_full_name" cssStyle="width:98.2%;"
                    />
        </td>
        <td>
            <s:textarea name="noticeWitness_2.fullNameWitness2" id="witness_2_full_name" cssStyle="width:98.2%;"
                    />
        </td>
    </tr>
    <tr>
        <td>
            තරාතිරම හෝ රක්ෂාව <br>
            Rank or Profession <br>
        </td>
        <td>
            <s:textarea name="noticeWitness_1.rankOrProfessionWitness1" id="witness_1_rank" cssStyle="width:98.2%;"
                    />
        </td>
        <td>
            <s:textarea name="noticeWitness_2.rankOrProfessionWitness1" id="witness_2_rank" cssStyle="width:98.2%;"
                    />
        </td>
    </tr>
    <tr>
        <td>
            පදිංචි ස්ථානය <br>
            Place of Residence <br>
        </td>
        <td>
            <s:textarea name="noticeWitness_1.addressWitness" id="witness_1_place_residence" cssStyle="width:98.2%;"
                    />
        </td>
        <td>
            <s:textarea name="noticeWitness_2.addressWitness" id="witness_2_place_residence" cssStyle="width:98.2%;"
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
    <s:submit value="add.notice"/>
</div>
</s:form>
</div>