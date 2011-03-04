<%-- @author Chathuranga Withana --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
    }
</script>

<div id="death-declaration-form-1-outer">
<s:form name="nonEditableDeathRegistrationForm1" action="eprDeathViewMode.do" method="POST">
<s:set value="%{#session.WW_TRANS_I18N_LOCALE.language}" name="userPreferedLang"/>
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
        <td style="border:1px solid #000;"><s:label value="%{#session.deathRegister.death.deathSerialNo}"/></td>
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
                ප්‍රකාශයක් [30, 39(1), 41(1) (උ) වගන්ති] - සාමාන්‍ය මරණ හා හදිසි මරණ <br/>
                ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள் <br/>
                Declaration of Death [Sections 30, 39(1) and 41(1)(e)] – Normal Death or Sudden Death
            </s:elseif>
            <s:elseif test="deathType.ordinal() == 1">
                Late<br>
                මරණ ප්‍රකාශයක් [36වෙනි වගන්තිය] - කාලය ඉකුත් වූ මරණ ලියාපදිංචි කිරීම හෝ නැතිවුණු පුද්ගලයෙකුගේ මරණ <br/>
                ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள் <br/>
                Declaration of Death [Section 36] – Late registration or Death of missing person
            </s:elseif>
            <s:elseif test="deathType.ordinal() == 3">
                Missing <br>
                මරණ ප්‍රකාශයක් [36වෙනි වගන්තිය] - කාලය ඉකුත් වූ මරණ ලියාපදිංචි කිරීම හෝ නැතිවුණු පුද්ගලයෙකුගේ මරණ <br/>
                ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள் <br/>
                Declaration of Death [Section 36] – Late registration or Death of missing person
            </s:elseif>
        </td>
        <td style="border:1px solid #000;">ලියාපදිංචි කල දිනය<br>பிறப்பைப் பதிவு திகதி <br>Date of Registration
        </td>
        <td style="border:1px solid #000;"><s:label value="%{#session.deathRegister.death.dateOfRegistration}"/></td>
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
                ප්‍රකාශකයා විසින් මරණය සිදු වූ කොට්ටාශයේ මරණ රෙජිස්ට්‍රාර් තැන වෙත ලබා දිය යුතුය. මෙම තොරතුරු මත
                සිවිල්
                ලියාපදිංචි කිරිමේ පද්ධතියේ මරණය ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம்
                சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the declarant and the duly completed form should be forwarded to the
                Registrar of Deaths of the division where the death has occurred. The death will be registered in
                the
                Civil Registration System based on the information provided in this form.
            </s:elseif>

            <s:elseif test="deathType.ordinal() == 1">
                ලියාපදිංචි නොකරන ලද මරණයක් සම්බන්ධයෙන් මෙහි පහත ප්‍රකාශ කරනු ලබන විස්තර මගේ දැනීමේ හා විශ්වාසයේ ප්‍රකාර සැබෑ බව හා නිවැරදි බවද, මරණය සිදුවී, නැතහොත් ගෘහයක් හෝ ගොඩනැගිල්ලක් නොවන ස්ථානයක තිබී මෘතශරීරය සම්බවී, මාස තුඅනක් ඇතුලත දී මරණය ලියාපදිංචි කිරීමට නොහැකි වුයේ මෙහි පහත සඳහන් කාරණය හේතු කොටගෙන බවද, ..... පදිංචි .... වන මම ගාම්භීරතා පුර්වකාවද, අවංක ලෙසද, සැබෑ ලෙසද, මෙයින් ප්‍රකාශ කරමි.
                <br>பதியப்படாத மரணம் சம்பந்தமாக இங்கு கீழ் பிரதிக்கினை செய்யப்படும் விபரங்கள் எனது அறிவிக்கும் நம்பிக்கைக்கும் உரியவகையில் உண்மையானதும் சரியானதும் எனவும் இறப்பு நிகழ்ந்து அல்லது வீடு அல்லது கட்டிடம் அல்லாத இடத்திலிருந்து அப்பிரேதத்தைக் கண்டு மூன்று மாதங்களுக்குள் இறப்பினை பதிவதற்கு இயலாது போனது கீழ் குறிப்பிடப்படும் காரணத்தினால் ஆகும் என ….......................................................................வதியும் ….........................................................ஆகிய நான் நோ்மையாகவும் உண்மையாகவும் பயபக்தியுடனும் பிரதிக்கினை செய்கின்றேன்.
                <br>I …. of …. solemnly, sincerely, and truly declare that the particulars stated below relating to an unregistered death, are true and correct to the best of my knowledge and belief, and that the death has not been registered within three months from its occurrence or from the finding of the corpse in a place other than a house or a building, for this reason.
            </s:elseif>


            <s:elseif test="deathType.ordinal() == 3">
                ලියාපදිංචි නොකරන ලද මරණයක් සම්බන්ධයෙන් මෙහි පහත ප්‍රකාශ කරනු ලබන විස්තර මගේ දැනීමේ හා විශ්වාසයේ ප්‍රකාර සැබෑ බව හා නිවැරදි බවද, මරණය සිදුවී, නැතහොත් ගෘහයක් හෝ ගොඩනැගිල්ලක් නොවන ස්ථානයක තිබී මෘතශරීරය සම්බවී, මාස තුඅනක් ඇතුලත දී මරණය ලියාපදිංචි කිරීමට නොහැකි වුයේ මෙහි පහත සඳහන් කාරණය හේතු කොටගෙන බවද, ..... පදිංචි .... වන මම ගාම්භීරතා පුර්වකාවද, අවංක ලෙසද, සැබෑ ලෙසද, මෙයින් ප්‍රකාශ කරමි.
                <br>பதியப்படாத மரணம் சம்பந்தமாக இங்கு கீழ் பிரதிக்கினை செய்யப்படும் விபரங்கள் எனது அறிவிக்கும் நம்பிக்கைக்கும் உரியவகையில் உண்மையானதும் சரியானதும் எனவும் இறப்பு நிகழ்ந்து அல்லது வீடு அல்லது கட்டிடம் அல்லாத இடத்திலிருந்து அப்பிரேதத்தைக் கண்டு மூன்று மாதங்களுக்குள் இறப்பினை பதிவதற்கு இயலாது போனது கீழ் குறிப்பிடப்படும் காரணத்தினால் ஆகும் என ….......................................................................வதியும் ….........................................................ஆகிய நான் நோ்மையாகவும் உண்மையாகவும் பயபக்தியுடனும் பிரதிக்கினை செய்கின்றேன்.
                <br>I …. of …. solemnly, sincerely, and truly declare that the particulars stated below relating to an unregistered death, are true and correct to the best of my knowledge and belief, and that the death has not been registered within three months from its occurrence or from the finding of the corpse in a place other than a house or a building, for this reason.
            </s:elseif>
        </td>
    </tr>
    </tbody>
</table>
<s:if test="deathType.ordinal() == 1">
    <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;" class="font-9">
        <tr>
            <td width="150px">(1)මරණය ලියාපදිංචි කිරීම ප්‍රමාද වීමට කාරණය <br/>
                இறப்பினை பதிவதற்கு தாமதித்ததற்கான காரணம்<br/>
                Reason for the late registration of the death
            </td>
            <td>
                <s:label value="%{#session.deathRegister.death.reasonForLateRegistration}"/>
            </td>
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
    <s:if test="deathType.ordinal() == 2">
        <tr>
            <td>
                හදිසි මරණයක්ද ? <br/>
                திடீர் மரணமா?<br/>
                Sudden death?
            </td>
            <td colspan="8">
                <s:label value="%{getText('yes.label')}"/>
            </td>
        </tr>
    </s:if>
    <s:elseif test="deathType.ordinal() == 3">
        <tr>
            <td colspan="2">
                නැතිවුණු පුද්ගලයෙකුගේ මරණයක්ද ? <br/>
                காணாமற்போன நபரது மரணமா?<br/>
                Is the death of a missing person?
            </td>
            <td colspan="6">
                <s:label value="%{getText('yes.label')}"/>
            </td>
        </tr>
    </s:elseif>
    <tr>
        <td>මරණය සිදුවූ දිනය<br>பிறந்த திகதி<br>Date of death</td>
        <td colspan="3" style="text-align:left;"><s:label value="%{#session.deathRegister.death.dateOfDeath}"/></td>
        <td>වෙලාව<br>நேரம்<br>Time</td>
        <td colspan="3"><s:label value="%{#session.deathRegister.death.timeOfDeath}"/></td>
    </tr>
    <tr>
        <td rowspan="6">මරණය සිදු වූ ස්ථානය<br>பிறந்த இடம்<br>Place of Death</td>
        <td colspan="3">දිස්ත්‍රික්කය /<br> மாவட்டம் / <br>District</td>
        <td colspan="5">
            <s:if test="#userPreferedLang == 'si'">
                <s:label value="%{#session.deathRegister.death.deathDivision.dsDivision.district.siDistrictName}"/>
            </s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label value="%{#session.deathRegister.death.deathDivision.dsDivision.district.enDistrictName}"/>
            </s:elseif>
            <s:else>
                <s:label value="%{#session.deathRegister.death.deathDivision.dsDivision.district.taDistrictName}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td colspan="3">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
        <td colspan="5">
                <%--TODO--%>
            <s:if test="#userPreferedLang == 'si'">
                <s:label value="%{#session.deathRegister.death.deathDivision.dsDivision.siDivisionName}"/>
            </s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label value="%{#session.deathRegister.death.deathDivision.dsDivision.enDivisionName}"/>
            </s:elseif>
            <s:else>
                <s:label value="%{#session.deathRegister.death.deathDivision.dsDivision.taDivisionName}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td colspan="3">ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br>பிரிவு / <br>Registration Division</td>
        <td colspan="5">
            <s:if test="#userPreferedLang == 'si'">
                <s:label
                        value="%{#session.deathRegister.death.deathDivision.siDivisionName}"/>
            </s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label value="%{#session.deathRegister.death.deathDivision.enDivisionName}"/>
            </s:elseif>
            <s:else>
                <s:label value="%{#session.deathRegister.death.deathDivision.taDivisionName}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td colspan="3"><label>
            ග්‍රාම නිළධාරී කොටිඨාශය
            / <br>Grama Niladhari Division in ta/<br>Grama Niladhari Division/</label></td>
        <td colspan="5">
            <s:if test="#userPreferedLang == 'si'">
                <s:label
                        value="%{#session.deathRegister.death.gnDivision.siGNDivisionName}"/>
            </s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label value="%{#session.deathRegister.death.gnDivision.enGNDivisionName}"/>
            </s:elseif>
            <s:else>
                <s:label value="%{#session.deathRegister.death.gnDivision.taGNDivisionName}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td rowspan="2" colspan="1">ස්ථානය <br>பிறந்த <br>Place</td>
        <td colspan="2">සිංහල හෝ දෙමළ භාෂාවෙන්<br>சிங்களம் தமிழ்<br>In Sinhala or Tamil</td>
        <td colspan="5"><s:label value="%{#session.deathRegister.death.placeOfDeath}"/></td>
    </tr>
    <tr>
        <td colspan="2">ඉංග්‍රීසි භාෂාවෙන්<br>ஆங்கில மொழியில்<br>In English</td>
        <td colspan="5"><s:label value="%{#session.deathRegister.death.placeOfDeathInEnglish}"/></td>
    </tr>
    <tr>
            <%--TODO --%>
        <td rowspan="2" colspan="2">මරණයට හේතුව තහවුරුද?<br>இறப்பிற்கான காரணம் உறுதியானதா?<br>Is the cause of death
            established?
        </td>
        <td rowspan="2" colspan="2">
            <s:if test="session.deathRegister.death.causeOfDeathEstablished ==true">
                <s:label value="%{getText('yes.label')}"/>
            </s:if>
            <s:else>
                <s:label value="%{getText('no.label')}"/>
            </s:else>
        </td>
        <td rowspan="2" colspan="3">මරණය දින 30 කට අඩු ළදරුවෙකුගේද?<br>இறப்பு 30 நாட்களுக்கு குறைவான சிசுவினதா?<br>Is
            the death of an infant
            less
            than 30 days?
        </td>
        <td rowspan="2">
            <s:if test="session.deathRegister.death.infantLessThan30Days ==true">
                <s:label value="%{getText('yes.label')}"/>
            </s:if>
            <s:else>
                <s:label value="%{getText('no.label')}"/>
            </s:else>
        </td>
    </tr>
    <tr>
    </tr>
    <tr>
        <td colspan="1">මරණයට හේතුව<br>இறப்பிற்கான காரணம்<br>Cause of death</td>
        <td colspan="4"><s:label value="%{#session.deathRegister.death.causeOfDeath}"/></td>
        <td colspan="2">හේතුවේ ICD කේත අංකය<br>காரணத்திற்கான ICD குறியீட்டு இலக்கம்<br>ICD Code of cause</td>
        <td colspan="2"><s:label value="%{#session.deathRegister.death.icdCodeOfCause}"/></td>
    </tr>
    <tr>
        <td colspan="1">ආදාහන හෝ භූමදාන ස්ථානය<br>அடக்கம் செய்த அல்லது தகனஞ் செய்த இடம்<br>Place of burial or cremation
        </td>
        <td colspan="8"><s:label value="%{#session.deathRegister.death.placeOfBurial}"/></td>
    </tr>
    <s:if test="deathType.ordinal() == 2 || deathType.ordinal() == 3">
        <tr>
            <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)වෙනත් තොරතුරු <br/>
                இறப்பிற்கான காரணம்<br/>
                Any other information
            </td>
            <td colspan="8">
                <s:label value="%{#session.deathRegister.death.anyOtherInformation}"/>
            </td>
        </tr>
    </s:if>
    <tr>
        <td colspan="2"><label>
            මරණ
            සහතිකය නිකුත් කල යුතු භාෂාව <br>சான்றிதழ் வழங்கப்பட வேண்டிய மொழி <br>Preferred
            Language for
            Death Certificate </label></td>
        <td colspan="7">
            <s:set name="lang" value="%{#session.deathRegister.death.preferredLanguage}"/>
            <s:label value="%{getText(#lang)}" cssStyle="float:left;  width:240px;"/>
        </td>
    </tr>
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
        <td rowspan="2" colspan="3"><s:label value="%{#session.deathRegister.deathPerson.deathPersonPINorNIC}"/></td>
        <td rowspan="2">විදේශිකය‍කු නම්<br>வெளிநாட்டவர் <br>If a foreigner</td>
        <td>රට<br>நாடு<br>Country</td>
        <td>
            <s:if test="#userPreferedLang == 'si'">
                <s:label value="%{#session.deathRegister.deathPerson.deathPersonCountry.siCountryName}"/>
            </s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label value="%{#session.deathRegister.deathPerson.deathPersonCountry.enCountryName}"/>
            </s:elseif>
            <s:else>
                <s:label value="%{#session.deathRegister.deathPerson.deathPersonCountry.taCountryName}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td>ගමන් බලපත්‍ර අංකය<br>கடவுச் சீட்டு<br>Passport No.</td>
        <td><s:label value="%{#session.deathRegister.deathPerson.deathPersonPassportNo}"/></td>
    </tr>
    <tr>
        <td colspan="1">වයස හෝ අනුමාන වයස<br>பிறப்ப<br>Age or probable Age</td>
        <td colspan="1"><s:label value="%{#session.deathRegister.deathPerson.deathPersonAge}"/></td>
        <td colspan="1">ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender</td>
        <td colspan="1">
            <s:if test="session.deathRegister.deathPerson.deathPersonGender == 0">
                <s:label name="" value="%{getText('male.label')}"/>
            </s:if>
            <s:elseif test="session.deathRegister.deathPerson.deathPersonGender == 1">
                <s:label name="" value="%{getText('female.label')}"/>
            </s:elseif>
            <s:elseif test="session.deathRegister.deathPerson.deathPersonGender == 2">
                <s:label name="" value="%{getText('unknown.label')}"/>
            </s:elseif>
        </td>
        <td colspan="1">ජාතිය<br>பிறப்<br>Race</td>
        <td colspan="2">
            <s:if test="#userPreferedLang == 'si'">
                <s:label value="%{#session.deathRegister.deathPerson.deathPersonRace.siRaceName}"/>
            </s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label value="%{#session.deathRegister.deathPerson.deathPersonRace.enRaceName}"/>
            </s:elseif>
            <s:else>
                <s:label value="%{#session.deathRegister.deathPerson.deathPersonRace.taRaceName}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td colspan="1">නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)<br>பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்)<br>Name
            in either of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="6"><s:label value="%{#session.deathRegister.deathPerson.deathPersonNameOfficialLang}"/></td>
    </tr>
    <tr>
        <td colspan="1">නම ඉංග්‍රීසි භාෂාවෙන්<br>பிறப்பு அத்தாட்சி …..<br>Name in English</td>
        <td colspan="6"><s:label value="%{#session.deathRegister.deathPerson.deathPersonNameInEnglish}"/></td>
    </tr>
    <tr>
        <td colspan="1">ස්ථිර ලිපිනය<br>தாயின் நிரந்தர வதிவிட முகவரி<br>Permanent Address</td>
        <td colspan="6"><s:label value="%{#session.deathRegister.deathPerson.deathPersonPermanentAddress}"/></td>
    </tr>
    <tr>
        <td colspan="1">
            පියාගේ අනන්‍යතා අංකය
            <br>தந்தையின் அடையாள எண்
            <br>Fathers Identification No.
        </td>
        <td colspan="6"><s:label value="%{#session.deathRegister.deathPerson.deathPersonFatherPINorNIC}"/></td>
    </tr>
    <tr>
        <td colspan="1">
            පියාගේ සම්පුර්ණ නම
            <br>தந்தையின் முழுப் பெயர்
            <br>Fathers full name
        </td>
        <td colspan="6"><s:label value="%{#session.deathRegister.deathPerson.deathPersonFatherFullName}"/></td>
    </tr>
    <tr>
        <td colspan="1">
            මවගේ අනන්‍යතා අංකය
            <br>தாயின் அடையாள எண்
            <br>Mothers Identification No.
        </td>
        <td colspan="6"><s:label value="%{#session.deathRegister.deathPerson.deathPersonMotherPINorNIC}"/></td>
    </tr>
    <tr>
        <td colspan="1">
            මවගේ සම්පුර්ණ නම
            <br>தாயின் முழுப் பெயர்
            <br>Mothers full name
        </td>
        <td colspan="6"><s:label value="%{#session.deathRegister.deathPerson.deathPersonMotherFullName}"/></td>
    </tr>
    </tbody>
</table>

<div class="form-submit">
    <s:hidden name="pageNo" value="1"/>
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
<script type="text/javascript">
    function setTime() {
        var list = document.getElementById("time");
        var array = new Array('1', '2', '3');
        list.list = array;
    }
</script>
</div>