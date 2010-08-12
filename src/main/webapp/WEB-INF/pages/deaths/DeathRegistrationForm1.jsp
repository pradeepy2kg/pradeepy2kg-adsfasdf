<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/popreg/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>

<script type="text/javascript">

    $(function() {
        $("#deathDatePicker").datepicker();
    });

    $(function() {
        $("#dateOfRegistrationDatePicker").datepicker();
    });


    // mode 1 = passing District, will return DS list
    // mode 2 = passing DsDivision, will return BD list
    // any other = passing district, will return DS list and the BD list for the first DS
    $(function() {
        $('select#deathDistrictId').bind('change', function(evt1) {
            var id = $("select#deathDistrictId").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#deathDsDivisionId").html(options1);

                        var options2 = '';
                        var bd = data.bdDivisionList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#deathDivisionId").html(options2);
                    });
        });

        $('select#deathDsDivisionId').bind('change', function(evt2) {
            var id = $("select#deathDsDivisionId").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#deathDivisionId").html(options);
                    });
        });

        $('img#death_person_lookup').bind('click', function(evt3) {
            var id1 = $("input#deathPerson_PINorNIC").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#deathPersonNameOfficialLang").val(data1.fullNameInOfficialLanguage);
                        //$("textarea#deathPersonNameInEnglish").val(data1.fullNameInOfficialLanguage);
                        $("textarea#deathPersonPermanentAddress").val(data2.lastAddress);
                    });
        });
        $('img#death_person_father_lookup').bind('click', function(evt4) {
            var id1 = $("input#deathPersonFather_PINorNIC").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#deathPersonFatherFullName").val(data1.fullNameInOfficialLanguage);
                    });
        });
        $('img#death_person_mother_lookup').bind('click', function(evt5) {
            var id1 = $("input#deathPersonMother_PINorNIC").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#deathPersonMotherFullName").val(data1.fullNameInOfficialLanguage);
                    });
        });


    });
</script>


<div id="death-declaration-form-1-outer">
<s:form name="deathRegistrationForm1" id="death-registration-form-1" action="eprDeathDeclaration.do" method="POST">
<table style="width: 100%; border:none; border-collapse:collapse;" class="font-9">
    <col width="180px"/>
    <col width="350px"/>
    <col width="120px"/>
    <col width="160px"/>
    <tbody>
    <tr>
        <td rowspan="3"></td>
        <td rowspan="2" align="center">
            <img src="<s:url value="/images/official-logo.png" />" style="display: block; text-align: center;"
                 width="80" height="100">
        </td>
        <td style="border:1px solid #000;">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</td>
        <td style="border:1px solid #000;"><s:textfield name="death.deathSerialNo"/></td>
    </tr>
    <tr>
        <td colspan="2" style="border:1px solid #000;text-align:center;">කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>அலுவலக
            பாவனைக்காக மட்டும்
            <br>For office use only
        </td>
    </tr>
    <tr>
        <td align="center" class="font-12">
            <s:if test="deathType.ordinal() == 0">
                ප්‍රකාශයක් [30, 39(1), 41(1) (උ) වගන්ති] - සාමාන්‍ය මරණ හා හදිසි මරණ <br/>
                ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள் <br/>
                Declaration of Death [Sections 30, 39(1) and 41(1)(e)] – Normal Death or Sudden Death
            </s:if>
            <s:elseif test="deathType.ordinal() == 2">
                මරණ ප්‍රකාශයක් [36වෙනි වගන්තිය] - කාලය ඉකුත් වූ මරණ ලියාපදිංචි කිරීම හෝ නැතිවුණු පුද්ගලයෙකුගේ මරණ <br/>
                ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள் <br/>
                Declaration of Death [Section 36] – Late registration or Death of missing person
            </s:elseif>
        </td>
        <td style="border:1px solid #000;">ලියාපදිංචි කල දිනය<br>பிறப்பைப் பதிவு திகதி <br>Date of Registration
        </td>
        <td style="border:1px solid #000;">
            <s:textfield id="dateOfRegistrationDatePicker" name="death.dateOfRegistration"/>
        </td>
    </tr>
    <tr>
        <td colspan="4" height="15px"></td>
    </tr>
    <tr>
        <td colspan="4" class="font-9" style="text-align:justify;">
            <s:if test="deathType.ordinal() == 0">
                ප්‍රකාශකයා විසින් මරණය සිදු වූ කොට්ටාශයේ මරණ රෙජිස්ට්‍රාර් තැන වෙත ලබා දිය යුතුය. මෙම තොරතුරු මත
                සිවිල්
                ලියාපදිංචි කිරිමේ පද්ධතියේ මරණය ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம்
                சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the declarant and the duly completed form should be forwarded to the
                Registrar of Deaths of the division where the death has occurred. The death will be registered in
                the
                Civil Registration System based on the information provided in this form.
            </s:if>
            <s:elseif test="deathType.ordinal() == 2">
                ලියාපදිංචි නොකරන ලද මරණයක් සම්බන්ධයෙන් මෙහි පහත ප්‍රකාශ කරනු ලබන විස්තර මගේ දැනීමේ හා විශ්වාසයේ ප
                ්‍රකාර සැබෑ බව හා නිවැරදි බවද, මරණය සිදුවී, නැතහොත් ගෘහයක් හෝ ගොඩනැගිල්ලක් නොවන ස්ථානයක තිබී මෘතශරීරය
                සම්බවී,
                මාස තුඅනක් ඇතුලත දී මරණය ලියාපදිංචි කිරීමට නොහැකි වුයේ මෙහි පහත සඳහන් කාරණය හේතු කොටගෙන බවද
                , ..... පදිංචි .... වන මම ගාම්භීරතා පුර්වකාවද, අවංක ලෙසද, සැබෑ ලෙසද, මෙයින් ප්‍රකාශ කරමි. <br/>

                in tamil line 1
                in tamil line 2
                in tamil line 3 <br/>

                I …. of …. solemnly, sincerely, and truly declare that the particulars stated below relating to an
                unregistered death, are true and correct to the best of my knowledge and belief, and that the death has not
                been registered within three months from its occurrence or from the finding of the corpse in a place other
                than a house or a building, for this reason.
            </s:elseif>
        </td>
    </tr>
    </tbody>
</table>
<s:if test="deathType.ordinal() == 2">
    <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;" class="font-9">
        <tr>
            <td width="150px">මරණය ලියාපදිංචි කිරීම ප්‍රමාද වීමට කාරණය <br/>
                in tamil <br/>
                Reason for the late registration of the death
            </td>
            <td><s:textarea name="death.reasonForLateRegistration"/></td>
        </tr>
    </table>
</s:if>
<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;" class="font-9">
    <col width="150px"/>
    <col width="100px"/>
    <col width="120px"/>
    <col width="90px"/>
    <col width="120px"/>
    <col width="90px"/>
    <col width="120px"/>
    <col width="100px"/>
    <col/>
    <tbody>

    <tr>
        <td colspan="9" class="form-sub-title">
            මරණය පිලිබඳ විස්තර
            <br>பிள்ளை பற்றிய தகவல்
            <br>Information about the Death
        </td>
    </tr>
    <s:if test="deathType.ordinal() == 0">
        <tr>
            <td>
                හදිසි මරණයක්ද ? <br/>
                in tamil <br/>
                Sudden death?
            </td>
            <td colspan="3">ඔව් xx Yes</td>
            <td><s:radio name="deathType" list="#@java.util.HashMap@{'SUDDEN':''}" value="false"/></td>
            <td colspan="3">නැත xx No</td>
            <td><s:radio name="deathType" list="#@java.util.HashMap@{'NORMAL':''}" value="false"/></td>
        </tr>
    </s:if>
    <s:elseif test="deathType.ordinal() == 2">
        <tr>
            <td>
                නැතිවුණු පුද්ගලයෙකුගේ මරණයක්ද ? <br/>
                in tamil <br/>
                Is the death of a missing person?
            </td>
            <td colspan="3">ඔව් xx Yes</td>
            <td><s:radio name="deathType" list="#@java.util.HashMap@{'MISSING':''}" value="false"/></td>
            <td colspan="3">නැත xx No</td>
            <td><s:radio name="deathType" list="#@java.util.HashMap@{'LATE':''}" value="false"/></td>
        </tr>
    </s:elseif>
    <tr>
        <td>මරණය සිදුවූ දිනය<br>பிறந்த திகதி<br>Date of death</td>
        <td colspan="5" style="text-align:right;">
            <s:textfield id="deathDatePicker" name="death.dateOfDeath"/>
        </td>
        <td>වෙලාව<br>*in tamil<br>Time</td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td rowspan="5">මරණය සිදු වූ ස්ථානය<br>பிறந்த இடம்<br>Place of Death</td>
        <td colspan="3">දිස්ත්‍රික්කය / மாவட்டம் / District</td>
        <td colspan="5"><s:select id="deathDistrictId" name="deathDistrictId" list="districtList"
                                  cssStyle="width:240px;"/></td>
    </tr>
    <tr>
        <td colspan="3">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
        <td colspan="5"><s:select id="deathDsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  cssStyle="float:left;  width:240px;"/></td>
    </tr>
    <tr>
        <td colspan="3">ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br>பிரிவு / <br>Registration Division</td>
        <td colspan="5"><s:select id="deathDivisionId" name="deathDivisionId" list="bdDivisionList"
                                  cssStyle=" width:240px;float:left;"/></td>
    </tr>
    <tr>
        <td rowspan="2" colspan="1">ස්ථානය <br>பிறந்த <br>Place</td>
        <td colspan="2">සිංහල හෝ දෙමළ භාෂාවෙන්<br>சிங்களம் தமிழ்<br>In Sinhala or Tamil</td>
        <td colspan="5"><s:textarea name="death.placeOfDeath" cssStyle="width:550px;"/></td>
    </tr>
    <tr>
        <td colspan="2">ඉංග්‍රීසි භාෂාවෙන්<br>*in tamil<br>In English</td>
        <td colspan="5"><s:textarea name="death.placeOfDeathInEnglish" cssStyle="width:550px;"/></td>
    </tr>
    <tr>
        <td rowspan="2" colspan="1">මරණයට හේතුව තහවුරුද?<br>*in tamil<br>Is the cause of death established?</td>
        <td colspan="1">නැත / xx / No</td>
        <td colspan="2"><s:radio name="death.causeOfDeathEstablished" list="#@java.util.HashMap@{'false':''}"
                                 id=""/></td>
        <td rowspan="2" colspan="3">මරණය දින 30 කට අඩු ළදරුවෙකුගේද?<br>*in tamil<br>Is the death of an infant
            less
            than 30 days?
        </td>
        <td colspan="1">නැත / xx / No</td>
        <td colspan="1"><s:radio name="death.infantLessThan30Days" list="#@java.util.HashMap@{'false':''}"/></td>
    </tr>
    <tr>
        <td colspan="1">ඔව් / xx /Yes</td>
        <td colspan="2"><s:radio name="death.causeOfDeathEstablished" list="#@java.util.HashMap@{'true':''}"/></td>
        <td colspan="1">ඔව් / xx /Yes</td>
        <td colspan="1"><s:radio name="death.infantLessThan30Days" list="#@java.util.HashMap@{'true':''}"/></td>
    </tr>
    <tr>
        <td colspan="1">මරණයට හේතුව<br>*in tamil<br>Cause of death</td>
        <td colspan="4"><s:textarea name="death.causeOfDeath" cssStyle="width:400px; "/></td>
        <td colspan="2">හේතුවේ ICD කේත අංකය<br>*in tamil<br>ICD Code of cause</td>
        <td colspan="2"><s:textfield name="death.icdCodeOfCause"/></td>
    </tr>
    <tr>
        <td colspan="1">ආදාහන හෝ භූමදාන ස්ථානය<br>*in tamil<br>Place of burial or cremation</td>
        <td colspan="8"><s:textarea name="death.placeOfBurial"/></td>
    </tr>
    <s:if test="deathType.ordinal() == 2">
        <tr>
            <td colspan="1">වෙනත් තොරතුරු <br/>
                in tamil <br/>
                Any other information
            </td>
            <td colspan="8"><s:textarea name="death.placeOfBurial"/></td>
        </tr>
    </s:if>
    </tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin-bottom:0;" class="font-9">
    <col width="150px"/>
    <col width="100px"/>
    <col width="100px"/>
    <col width="100px"/>
    <col width="150px"/>
    <col width="130px"/>
    <col/>
    <tbody>
    <tr class="form-sub-title">
        <td colspan="7">මරණයට පත් වූ පුද්ගලයාගේ විස්තර<br>பிள்ளை பற்றிய தகவல்<br>Information about the person
            Departed
        </td>
    </tr>
    <tr>
        <td rowspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>தனிநபர் அடையாள எண் / அடையாள அட்டை இல.
            <br>PIN / NIC
        </td>
        <td rowspan="2" colspan="3"><s:textfield name="deathPerson.deathPersonPINorNIC" id="deathPerson_PINorNIC"
                                                 cssStyle="float:left;margin-left:225px;"/>
            <img src="<s:url value="/images/search-father.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="death_person_lookup"></td>
        <td rowspan="2">විදේශිකය‍කු නම්<br>வெளிநாட்டவர் <br>If a foreigner</td>
        <td>රට<br>நாடு<br>Country</td>
        <td><s:select id="deathPersonCountryId" name="deathPersonCountry" list="countryList" headerKey="0"
                      headerValue="%{getText('select_country.label')}"/></td>
    </tr>
    <tr>
        <td>ගමන් බලපත්‍ර අංකය<br>கடவுச் சீட்டு<br>Passport No.</td>
        <td><s:textfield name="deathPerson.deathPersonPassportNo"/></td>
    </tr>
    <tr>
        <td colspan="1">වයස හෝ අනුමාන වයස<br>பிறப்ப<br>Age or probable Age</td>
        <td colspan="1"><s:textfield name="deathPerson.deathPersonAge"/></td>
        <td colspan="1">ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender</td>
        <td colspan="1"><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="deathPerson.deathPersonGender" headerKey="0" headerValue="%{getText('select_gender.label')}"
                cssStyle="width:190px; margin-left:5px;"/></td>
        <td colspan="1">ජාතිය<br>பிறப்<br>Race</td>
        <td colspan="2">
            <s:select list="raceList" name="deathPersonRace" headerKey="0" headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)<br>பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்)<br>Name
            in either of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="6"><s:textarea name="deathPerson.deathPersonNameOfficialLang" id="deathPersonNameOfficialLang"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">නම ඉංග්‍රීසි භාෂාවෙන්<br>பிறப்பு அத்தாட்சி …..<br>Name in English</td>
        <td colspan="6">
            <s:textarea name="deathPerson.deathPersonNameInEnglish" id="deathPersonNameInEnglish"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">ස්ථිර ලිපිනය<br>தாயின் நிரந்தர வதிவிட முகவரி<br>Permanent Address</td>
        <td colspan="6">
            <s:textarea name="deathPerson.deathPersonPermanentAddress" id="deathPersonPermanentAddress"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">පියාගේ පු.අ.අ. / ජා.හැ.අ.<br>*in tamil<br>Fathers PIN / NIC</td>
        <td colspan="6">
            <s:textfield name="deathPerson.deathPersonFatherPINorNIC" id="deathPersonFather_PINorNIC"
                         cssStyle="float:left;margin-left:695px;"/>
            <img src="<s:url value="/images/search-mother.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="death_person_father_lookup"></td>
    </tr>
    <tr>
        <td colspan="1">පියාගේ සම්පුර්ණ නම<br>*in tamil <br>Fathers full name</td>
        <td colspan="6">
            <s:textarea name="deathPerson.deathPersonFatherFullName" id="deathPersonFatherFullName"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">මවගේ පු.අ.අ. / ජා.හැ.අ.<br>*in tamil<br>Mothers PIN / NIC</td>
        <td colspan="6">
            <s:textfield name="deathPerson.deathPersonMotherPINorNIC" id="deathPersonMother_PINorNIC"
                         cssStyle="float:left;margin-left:695px;"/>
            <img src="<s:url value="/images/search-mother.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="death_person_mother_lookup"></td>
    </tr>
    <tr>
        <td colspan="1">මවගේ සම්පුර්ණ නම<br>*in tamil <br>Mothers full name</td>
        <td colspan="6">
            <s:textarea name="deathPerson.deathPersonMotherFullName" id="deathPersonMotherFullName"/>
        </td>
    </tr>
    </tbody>
</table>

<div class="form-submit">
    <s:hidden name="pageNo" value="1"/>
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
</div>