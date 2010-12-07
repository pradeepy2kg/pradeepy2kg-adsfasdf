<%--@author amith jayasekara--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<script type="text/javascript">


    function initPage() {
        //  disableFemaleWitnesses('none');
        //  disableMaleWitnesses('none');
    }
    function disableFemaleWitnesses(mode) {

        document.getElementById('f_witness_1_pin').style.display = mode;
        document.getElementById('f_witness_1_full_name').style.display = mode;
        document.getElementById('f_witness_1_rank').style.display = mode;
        document.getElementById('f_witness_1_place_residence').style.display = mode;

        document.getElementById('f_witness_2_pin').style.display = mode;
        document.getElementById('f_witness_2_full_name').style.display = mode;
        document.getElementById('f_witness_2_rank').style.display = mode;
        document.getElementById('f_witness_2_place_residence').style.display = mode;
    }

    function disableMaleWitnesses(mode) {

        document.getElementById('m_witness_1_pin').style.display = mode;
        document.getElementById('m_witness_1_full_name').style.display = mode;
        document.getElementById('m_witness_1_rank').style.display = mode;
        document.getElementById('m_witness_1_place_residence').style.display = mode;

        document.getElementById('m_witness_2_pin').style.display = mode;
        document.getElementById('m_witness_2_full_name').style.display = mode;
        document.getElementById('m_witness_2_rank').style.display = mode;
        document.getElementById('m_witness_2_place_residence').style.display = mode;
    }

    function disableWitnessBaseOnParty() {

        var male = document.getElementById('maleIdtrue').checked;
        var female = document.getElementById('maleIdfalse').checked;
        var both = document.getElementById('bothtrue').checked;


        if (male) {
            disableMaleWitnesses('')
            disableFemaleWitnesses('none')
        } else if (female) {
            disableMaleWitnesses('none')
            disableFemaleWitnesses('')
        }
        else if (both) {
            /*todo remoe this*/
            disableMaleWitnesses('')
            disableFemaleWitnesses('none')
        }
    }

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

<s:if test="editMode">
    <s:if test="secondNotice">
        <%--second notice--%>
        <s:url action="eprMarriageSecondNoticeAdd" namespace="." id="addAction">
            <s:param name="idUKey" value="idUKey"/>
        </s:url>
    </s:if>
    <s:else>
        <s:url action="eprMarriageNoticeEdit" namespace="." id="addAction">
            <s:param name="idUKey" value="marriage.idUKey"/>
        </s:url>
    </s:else>
</s:if>
<s:else>
    <s:url action="eprMarriageNoticeAdd" namespace="." id="addAction">
        <%--
                <s:hidden name="noticeType" value="%{noticeType}"/>
        --%>
        <s:param name="noticeType" value="%{noticeType}"/>
    </s:url>
</s:else>

<div class="marriage-notice-outer">
<s:form action="%{addAction}" method="post">
<%--section official usage--%>
<table>
    <caption/>
    <col width="400px"/>
    <col width="224px"/>
    <col/>
    <tbody>
    <tr>
        <td>
            <table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;"
                   cellpadding="2px">
                <caption/>
                <col width="200px"/>
                <col width="200px"/>
                <tbody>
                <tr>
                    <td>
                        දිස්ත්‍රික්කය <br>
                        மாவட்டம் <br>
                        District <br>
                    </td>
                    <td>
                        <s:select id="districtId" name="marriageDistrictId" list="districtList"
                                  value="marriageDistrictId"
                                  cssStyle="width:98.5%; width:195px;"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
                        பிரதேச செயளாளர் பிரிவு <br>
                        Divisional Secretariat <br>
                    </td>
                    <td>
                        <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="dsDivisionId"
                                  cssStyle="width:98.5%; width:195px;"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
                        பதிவுப் பிரிவு <br>
                        Registration Division <br>
                    </td>
                    <td>
                        <s:select id="mrDivisionId" name="mrDivisionId" list="mrDivisionList" value="marriageDivisionId"
                                  cssStyle="width:98.5%; width:195px;"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
        <td align="center"><img src="<s:url value="/images/official-logo.png"/>"/></td>
        <td>
            <table border="1"
                   style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:3"
                   cellpadding="2px">
                <caption/>
                <col width="200px"/>
                <col width="200px"/>
                <tbody>
                <tr>
                    <td colspan="2">කාර්යාල ප්‍රයෝජනය සඳහා පමණි / <br>
                        அலுவலக பாவனைக்காக மட்டும் / <br>
                        For office use only <br>
                    </td>

                </tr>
                <tr>
                    <td>
                        අනුක්‍රමික අංකය <br>
                        தொடர் இலக்கம் <br>
                        Serial Number <br>
                    </td>
                    <td align="center">
                        <s:textfield name="serialNumber" id="mnSerial" maxLength="10"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        භාරගත් දිනය <br>
                        பெறப்பட்ட திகதி <br>
                        Date of Acceptance <br>
                    </td>
                    <td>
                        <s:textfield name="noticeReceivedDate" id="submitDatePicker" maxLength="10"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3" align="center">
            විවාහ දැන්වීම <br>
            குடிமதிப்பீட்டு ஆவணத்தில் <br>
            Notice of Marriage
        </td>
    </tr>
    </tbody>
</table>

<%--section type of marriage--%>
<table border="1" style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="210px"/>
    <col width="50px"/>
    <col width="210px"/>
    <col width="50px"/>
    <col width="210px"/>
    <col width="50px"/>
    <tr>
        <td>
            විවාහයේ ස්වභාවය <br>
            Type of Marriage <br>
        </td>
        <td>
            සාමාන්‍ය <br>
            in tamil <br>
            General
        </td>
        <td align="center">
            <s:radio name="marriage.typeOfMarriage" list="#@java.util.HashMap@{'GENERAL':''}" value="true"/>
        </td>
        <td>
            උඩරට බින්න <br>
            in tamil <br>
            Kandyan Binna
        </td>
        <td align="center">
            <s:radio name="marriage.typeOfMarriage" list="#@java.util.HashMap@{'KANDYAN_BINNA':''}" value="true"/>
        </td>
        <td>
            උඩරට දීග <br>
            in tamil <br>
            Kandyan Deega
        </td>
        <td align="center">
            <s:radio name="marriage.typeOfMarriage" list="#@java.util.HashMap@{'KANDYAN_DEEGA':''}" value="true"/>
        </td>
    </tr>
</table>

<s:if test="%{noticeType.ordinal()==0 || noticeType.ordinal()==1}">

<%--section male party--%>
<table style="margin-top:20px">
    <caption/>
    <col width="500px"/>
    <col width="524px"/>
    <tr>
        <td colspan="2" align="center">
            පුරුෂ පාර්ශ්වය / in tamil / Male Party
        </td>
    </tr>
</table>

<table border="1"
       style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;border-bottom:none "
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="100px"/>
    <col/>
    <col width="170px"/>
    <col/>
    <col width="170px"/>
    <col/>
    <tr>
        <td colspan="1">
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification number
        </td>
        <td colspan="2">
            <s:textfield name="marriage.male.identificationNumberMale" id="identification_male" maxLength="10"/>
        </td>
        </td>
        <td colspan="1">
            උපන් දිනය <br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td colspan="3">
            <s:textfield name="marriage.male.dateOfBirthMale" id="date_of_birth_male" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            ජාතිය <br>
            in tamil <br>
            Race
        </td>
        <td colspan="2">
            <s:select list="raceList" name="raceIdMale" headerKey="0" headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;"/>
        </td>
        <td colspan="1">
            පසුවූ උපන් දිනයට වයස <br>
            in tamil <br>
            Age at last Birthday
        </td>
        <td colspan="3">
            <s:textfield name="marriage.male.ageAtLastBirthDayMale" id="age_at_last_bd_male" maxLength="3"
                         value=""/>
        </td>
    </tr>
    <tr>
        <td>
            විදේශිකයකු නම් <br>
            வெளிநாட்டவர் எனின் <br>
            If a foreigner
        </td>
        <td>
            රට <br>
            நாடு <br>
            Country
        </td>
        <td>
            <s:select list="countryList" name="countryIdMale" headerKey="0"
                      headerValue="%{getText('select_country.label')}"
                      cssStyle="width:150px;"/>
        </td>
        <td>
            ගමන් බලපත්‍ර අංකය <br>
            கடவுச் சீட்டு இல. <br>
            Passport No.
        </td>
        <td>
            <s:textfield name="marriage.male.passport" id="passport_male" maxLength="15" cssStyle="width:120px"/>
        </td>
        <td>ලංකාවට පැමිණි දිනය <br>
            in tamil <br>
            Date of Arrival
        </td>
        <td>
            <s:textfield name="marriage.male.dateOfArrival" id="date_arrival_male" maxLength="10"
                         cssStyle="width:80px"/>
        </td>
    </tr>
    <tr>
        <td colspan="1" height="100x">
            නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <br>
            பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="6" height="100x">
            <s:textarea name="marriage.male.nameInOfficialLanguageMale" id="name_official_male"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1" height="100x">
            නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பெயர் ஆங்கில மொழியில் <br>
            Name in English
        </td>
        <td colspan="6" height="100x">
            <s:textarea name="marriage.male.nameInEnglishMale" id="name_english_male" cssStyle="width:98.2%;"/>
        </td>
    </tr>
</table>
<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption></caption>
    <col width="233px"/>
    <col width="140px"/>
    <col width="50px"/>
    <col width="140px"/>
    <col width="50px"/>
    <col width="150px"/>
    <col width="50px"/>
    <col width="150px"/>
    <col width="50px"/>
    <tbody>
    <tr>
        <td rowspan="2">
            පදිංචි ලිපිනය <br>
            தற்போதைய வதிவிட முகவரி <br>
            Resident Address
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language</td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.male.residentAddressMaleInOfficialLang" id="address_male_official"
                        cssStyle="width:98.2%;"/>
        </td>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.male.residentAddressMaleInEnglish" id="address_male_english"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td rowspan="2">
            තරාතිරම නොහොත් රක්ෂාව <br>
            in tamil <br>
            Rank or Profession
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language</td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="40px">
            <s:textfield name="marriage.male.rankOrProfessionMaleInOfficialLang" id="rank_male_official"
                         cssStyle="width:98.2%;"
                         maxLength="255"/>
        </td>
        <td colspan="4" height="40px">
            <s:textfield name="marriage.male.rankOrProfessionMaleInEnglish" id="rank_male_english"
                         cssStyle="width:98.2%;"
                         maxLength="255"/>
        </td>
    </tr>
    <tr>
        <td>
            සිවිල් තත්වය <br>
            சிவில் நிலைமை <br>
            Civil Status
        </td>
        <td>
            අවිවාහක <br>
            திருமணமாகாதவர் <br>
            Never Married
        </td>
        <td>
            <s:radio name="marriage.male.civilStatusMale" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"
                     value="true"/>
        </td>
        <td>
            දික්කසාද <br>
            திருமணம் தள்ளுபடி செய்தவர் <br>
            Divorced
        </td>
        <td>
            <s:radio name="marriage.male.civilStatusMale" list="#@java.util.HashMap@{'DIVORCED':''}"
                     value="true"/>
        </td>
        <td>
            වැන්දබු <br>
            விதவை <br>
            Widowed
        </td>
        <td>
            <s:radio name="marriage.male.civilStatusMale" list="#@java.util.HashMap@{'WIDOWED':''}" value="true"/>
        </td>
        <td>
            නිෂ්ප්‍රභාකර ඇත <br>
            தள்ளிவைத்தல் <br>
            Anulled
        </td>
        <td>
            <s:radio name="marriage.male.civilStatusMale" list="#@java.util.HashMap@{'ANULLED':''}" value="true"/>
        </td>
    </tr>
    <tr>
        <td>
            පියාගේ අනන්‍යතා අංකය <br>
            தந்தையின் அடையாள எண் <br>
            Fathers Identification Number
        </td>
        <td colspan="8">
            <s:textfield name="marriage.male.fatherIdentificationNumberMale" id="father_pin_or_nic_male"
                         cssStyle="width:240px;" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td rowspan="2">
            පියාගේ සම්පුර්ණ නම
            தந்தையின் அடையாள
            Fathers full name
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language</td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.male.fatherFullNameMaleInOfficialLang" id="father_full_name_male_official"
                        cssStyle="width:98.2%;"/>
        </td>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.male.fatherFullNameMaleInEnglish" id="father_full_name_male_english"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    </tbody>
</table>

</s:if>


<s:if test="%{noticeType.ordinal()==0 || noticeType.ordinal()==2}">
<%--section female party--%>
<table style="margin-top:20px">
    <caption/>
    <col width="500px"/>
    <col width="524px"/>
    <tr>
        <td colspan="2" align="center">
            ස්ත්‍රී පාර්ශ්වය / in tamil / Female Party
        </td>
    </tr>
</table>

<table border="1"
       style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;border-bottom:none "
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="100px"/>
    <col/>
    <col width="170px"/>
    <col/>
    <col width="170px"/>
    <col/>
    <tr>
        <td colspan="1">
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification number
        </td>
        <td colspan="2">
            <s:textfield name="marriage.female.identificationNumberFemale" id="identification_female" maxLength="10"/>
        </td>
        </td>
        <td colspan="1">
            උපන් දිනය <br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td colspan="3">
            <s:textfield name="marriage.female.dateOfBirthFemale" id="date_of_birth_female" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            ජාතිය <br>
            in tamil <br>
            Race
        </td>
        <td colspan="2">
            <s:select list="raceList" name="raceIdFemale" headerKey="0" headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;"/>
        </td>
        <td colspan="1">
            පසුවූ උපන් දිනයට වයස <br>
            in tamil <br>
            Age at last Birthday
        </td>
        <td colspan="3">
            <s:textfield name="marriage.female.ageAtLastBirthDayFemale" id="age_at_last_bd_female" maxLength="3"
                         value=""/>
        </td>
    </tr>
    <tr>
        <td>
            විදේශිකයකු නම් <br>
            வெளிநாட்டவர் எனின் <br>
            If a foreigner
        </td>
        <td>
            රට <br>
            நாடு <br>
            Country
        </td>
        <td>
            <s:select list="countryList" name="countryIdFemale" headerKey="0"
                      headerValue="%{getText('select_country.label')}"
                      cssStyle="width:150px;"/>
        </td>
        <td>
            ගමන් බලපත්‍ර අංකය <br>
            கடவுச் சீட்டு இல. <br>
            Passport No.
        </td>
        <td>
            <s:textfield name="marriage.female.passport" id="passport_female" maxLength="15" cssStyle="width:120px"/>
        </td>
        <td>ලංකාවට පැමිණි දිනය <br>
            in tamil <br>
            Date of Arrival
        </td>
        <td>
            <s:textfield name="marriage.female.dateOfArrival" id="date_arrival_female" maxLength="10"
                         cssStyle="width:80px"/>
        </td>
    </tr>
    <tr>
        <td colspan="1" height="100x">
            නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <br>
            பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="6" height="100x">
            <s:textarea name="marriage.female.nameInOfficialLanguageFemale" id="name_official_female"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1" height="100x">
            නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பெயர் ஆங்கில மொழியில் <br>
            Name in English
        </td>
        <td colspan="6" height="100x">
            <s:textarea name="marriage.female.nameInEnglishFemale" id="name_english_female" cssStyle="width:98.2%;"/>
        </td>
    </tr>
</table>
<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption></caption>
    <col width="233px"/>
    <col width="140px"/>
    <col width="50px"/>
    <col width="140px"/>
    <col width="50px"/>
    <col width="150px"/>
    <col width="50px"/>
    <col width="150px"/>
    <col width="50px"/>
    <tbody>
    <tr>
        <td rowspan="2">
            පදිංචි ලිපිනය <br>
            தற்போதைய வதிவிட முகவரி <br>
            Resident Address
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language</td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.female.residentAddressFemaleInOfficialLang" id="address_female_official"
                        cssStyle="width:98.2%;"/>
        </td>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.female.residentAddressFemaleInEnglish" id="address_female_english"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td rowspan="2">
            තරාතිරම නොහොත් රක්ෂාව <br>
            in tamil <br>
            Rank or Profession
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language</td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="40px">
            <s:textfield name="marriage.female.rankOrProfessionFemaleInOfficialLang" id="rank_female_official"
                         cssStyle="width:98.2%;"
                         maxLength="255"/>
        </td>
        <td colspan="4" height="40px">
            <s:textfield name="marriage.female.rankOrProfessionFemaleInEnglish" id="rank_female_english"
                         cssStyle="width:98.2%;"
                         maxLength="255"/>
        </td>
    </tr>
    <tr>
        <td>
            සිවිල් තත්වය <br>
            சிவில் நிலைமை <br>
            Civil Status
        </td>
        <td>
            අවිවාහක <br>
            திருமணமாகாதவர் <br>
            Never Married
        </td>
        <td>
            <s:radio name="marriage.female.civilStatusFemale" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"
                     value="true"/>
        </td>
        <td>
            දික්කසාද <br>
            திருமணம் தள்ளுபடி செய்தவர் <br>
            Divorced
        </td>
        <td>
            <s:radio name="marriage.female.civilStatusFemale" list="#@java.util.HashMap@{'DIVORCED':''}"
                     value="true"/>
        </td>
        <td>
            වැන්දබු <br>
            விதவை <br>
            Widowed
        </td>
        <td>
            <s:radio name="marriage.female.civilStatusFemale" list="#@java.util.HashMap@{'WIDOWED':''}" value="true"/>
        </td>
        <td>
            නිෂ්ප්‍රභාකර ඇත <br>
            தள்ளிவைத்தல் <br>
            Anulled
        </td>
        <td>
            <s:radio name="marriage.female.civilStatusFemale" list="#@java.util.HashMap@{'ANULLED':''}" value="true"/>
        </td>
    </tr>
    <tr>
        <td>
            පියාගේ අනන්‍යතා අංකය <br>
            தந்தையின் அடையாள எண் <br>
            Fathers Identification Number
        </td>
        <td colspan="8">
            <s:textfield name="marriage.female.fatherIdentificationNumberFemale" id="father_pin_or_nic_female"
                         cssStyle="width:240px;" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td rowspan="2">
            පියාගේ සම්පුර්ණ නම
            தந்தையின் அடையாள
            Fathers full name
        </td>
        <td colspan="4" height="20px">රාජ්‍ය භාෂාවෙන් / in tamil / in Official Language</td>
        <td colspan="4" height="20px">ඉංග්‍රීසි භාෂාවෙන් / in tamil / In English</td>
    </tr>
    <tr>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.female.fatherFullNameFemaleInOfficialLang" id="father_full_name_female_official"
                        cssStyle="width:98.2%;"/>
        </td>
        <td colspan="4" height="100px">
            <s:textarea name="marriage.female.fatherFullNameFemaleInEnglish" id="father_full_name_female_english"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    </tbody>
</table>
</s:if>
<div class="form-submit">
    <s:submit value="add.notice"/>
</div>
<s:hidden name="noticeType" value="%{noticeType}"/>
</s:form>
</div>



