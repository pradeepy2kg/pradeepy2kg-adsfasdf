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
            <s:if test="pageType == 0">
                මරණ ප්‍රකාශය - සාමාන්‍ය මරණ
                <br>இறப்பு பிரதிக்கினை - சாதாரண மரணம்
                <br>Declaration of Death – Normal Death
            </s:if>
            <s:elseif test="pageType == 1">
                මරණ ප්‍රකාශයක් [36වෙනි වගන්තිය] - කාලය ඉකුත් වූ මරණ ලියාපදිංචි කිරීම හෝ නැතිවුණු පුද්ගලයෙකුගේ මරණ
                <br>மரண பிரதிக்கினை [36வது பிரிவு ] - காலந் தாழ்த்திய இறப்பினை பதிவு செய்தல் அல்லது காணாமற் போன நபரின் மரணம்
                <br>Declaration of Death [Section 36] – Late registration or Death of missing person
            </s:elseif>
            <s:elseif test="pageType == 2">
                මරණ ප්‍රකාශය - හදිසි මරණ
                <br>இறப்பு பிரதிக்கினை - திடீர் மரணம்
                <br>Declaration of Death – Sudden Death
            </s:elseif>
            <s:elseif test="pageType == 3">
                missing person <br>
                මරණ ප්‍රකාශයක් [36වෙනි වගන්තිය] - කාලය ඉකුත් වූ මරණ ලියාපදිංචි කිරීම හෝ නැතිවුණු පුද්ගලයෙකුගේ මරණ
                <br>மரண பிரதிக்கினை [36வது பிரிவு ] - காலந் தாழ்த்திய இறப்பினை பதிவு செய்தல் அல்லது காணாமற் போன நபரின் மரணம்
                <br>Declaration of Death [Section 36] – Late registration or Death of missing person
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
            <s:if test="pageType == 0 ">
                දැනුම් දෙන්නා විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරියා / රෙජිස්ට්‍රාර් වෙත භාර දිය යුතුය. මෙම තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ මරණය ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் இறப்பு நிகழ்ந்த பிரிவின் அறிக்கையிடும் அதிகாரியிடம் /இறப்பு பதிவாளாரிடம் சமர்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு முறையில் இறப்பு பதிவு செய்யப்படும்.
                <br>Should be perfected by the informant and the duly completed form should be forwarded to the Officer / Registrar.
                The death will be registered in the Civil Registration System based on the information provided in this form.
            </s:if>
            <s:elseif test="pageType== 1">
                ලියාපදිංචි නොකරන ලද මරණයක් සම්බන්ධයෙන් මෙහි පහත ප්‍රකාශ කරනු ලබන විස්තර මගේ දැනීමේ හා විශ්වාසයේ ප්‍රකාර සැබෑ බව හා නිවැරදි බවද, මරණය සිදුවී, නැතහොත් ගෘහයක් හෝ ගොඩනැගිල්ලක් නොවන ස්ථානයක තිබී මෘතශරීරය සම්බවී, මාස තුඅනක් ඇතුලත දී මරණය ලියාපදිංචි කිරීමට නොහැකි වුයේ මෙහි පහත සඳහන් කාරණය හේතු කොටගෙන බවද, ..... පදිංචි .... වන මම ගාම්භීරතා පුර්වකාවද, අවංක ලෙසද, සැබෑ ලෙසද, මෙයින් ප්‍රකාශ කරමි.
                <br>பதியப்படாத மரணம் சம்பந்தமாக இங்கு கீழ் பிரதிக்கினை செய்யப்படும் விபரங்கள் எனது அறிவிக்கும் நம்பிக்கைக்கும் உரியவகையில் உண்மையானதும் சரியானதும் எனவும் இறப்பு நிகழ்ந்து அல்லது வீடு அல்லது கட்டிடம் அல்லாத இடத்திலிருந்து அப்பிரேதத்தைக் கண்டு மூன்று மாதங்களுக்குள் இறப்பினை பதிவதற்கு இயலாது போனது கீழ் குறிப்பிடப்படும் காரணத்தினால் ஆகும் என ….......................................................................வதியும் ….........................................................ஆகிய நான் நோ்மையாகவும் உண்மையாகவும் பயபக்தியுடனும் பிரதிக்கினை செய்கின்றேன்.
                <br>I …. of …. solemnly, sincerely, and truly declare that the particulars stated below relating to an unregistered death, are true and correct to the best of my knowledge and belief, and that the death has not been registered within three months from its occurrence or from the finding of the corpse in a place other than a house or a building, for this reason.
            </s:elseif>
            <s:elseif test="pageType== 2">
                දැනුම් දෙන්නා විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරියා / රෙජිස්ට්‍රාර් වෙත භාර දිය යුතුය. මෙම තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ මරණය ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் இறப்பு நிகழ்ந்த பிரிவின் அறிக்கையிடும் அதிகாரியிடம் /இறப்பு பதிவாளாரிடம் சமர்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு முறையில் இறப்பு பதிவு செய்யப்படும்.
                <br>Should be perfected by the informant and the duly completed form should be forwarded to the Officer / Registrar.
                The death will be registered in the Civil Registration System based on the information provided in this form.
            </s:elseif>
            <s:elseif test="pageType== 3">
                missing person <br>
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
    <s:if test="pageType == 0">
        <tr>
            <td colspan="4">
                මරණයේ ස්වභාවය?
                <br/>மரணத்தின் வகை? <br/>
                Type of death?
            </td>
            <td colspan="5">
                සාමාන්‍ය මරණයකි / சாதாரண மரணம் / Normal Death
            </td>
        </tr>
    </s:if>
    <s:elseif test="pageType == 2">
        <tr>
            <td colspan="4">
                මරණයේ ස්වභාවය?
                <br/>மரணத்தின் வகை? <br/>
                Type of death?
            </td>
            <td colspan="5">
                හදිසි මරණයකි / திடீர் மரணம் / Sudden Death
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
        <td colspan="3">ප්‍රාදේශීය ලේකම් කොට්ඨාශය /
            <br/>பிரதேச செயளாளர் பிரிவு
            <br/>Divisional Secretary Division
        </td>
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
        <td colspan="3">ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br>பதிவுப் பிரிவு/ <br>Registration Division</td>
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
        <td rowspan="2" colspan="1">ස්ථානය <br>பிறந்த <br>Place</td>
        <td colspan="2">සිංහල හෝ දෙමළ භාෂාවෙන්<br>சிங்களம் தமிழ்<br>In Sinhala or Tamil</td>
        <td colspan="5"><s:label value="%{#session.deathRegister.death.placeOfDeath}"/></td>
    </tr>
    <tr>
        <td colspan="2">ඉංග්‍රීසි භාෂාවෙන්<br>ஆங்கில மொழியில்<br>In English</td>
        <td colspan="5"><s:label value="%{#session.deathRegister.death.placeOfDeathInEnglish}"/></td>
    </tr>
    <tr>
        <td colspan="3">මරණය සිදුවුයේ රෝහලක් තුලදී ද?
            <br>மரணம் வைத்தியசாலையில் இடம்பெற்றதா?
            <br>Did the death occur at a Hospital?
        </td>
        <td colspan="5">
            <s:if test="session.deathRegister.death.deathOccurAtaHospital ==true">
                <s:label value="%{getText('yes.label')}"/>
            </s:if>
            <s:else>
                <s:label value="%{getText('no.label')}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td colspan="4">මරණයට හේතුව තහවුරුද?<br>இறப்பிற்கான காரணம் உறுதியானதா?<br>Is the cause of death
            established?
        </td>
        <td colspan="4">
            <s:if test="session.deathRegister.death.causeOfDeathEstablished ==true">
                <s:label value="%{getText('yes.label')}"/>
            </s:if>
            <s:else>
                <s:label value="%{getText('no.label')}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td colspan="2">මරණයට හේතුව<br>இறப்பிற்கான காரணம்<br>Cause of death</td>
        <td colspan="2"><s:label value="%{#session.deathRegister.death.causeOfDeath}"/></td>
        <td colspan="2">හේතුවේ ICD කේත අංකය<br>காரணத்திற்கான ICD குறியீட்டு இலக்கம்<br>ICD Code of cause</td>
        <td colspan="2"><s:label value="%{#session.deathRegister.death.icdCodeOfCause}"/></td>
    </tr>
    <tr>
        <td colspan="2">ආදාහන හෝ භූමදාන ස්ථානය<br>அடக்கம் செய்த அல்லது தகனஞ் செய்த இடம்<br>Place of burial or cremation
        </td>
        <td colspan="7"><s:label value="%{#session.deathRegister.death.placeOfBurial}"/></td>
    </tr>
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

    </tbody>
</table>

<table border="1" style="width: 100%;border-top:none;border-collapse:collapse; margin-bottom:15px;"
       class="font-9">
<col width="200px"/>
<col width="100px"/>
<col width="100px"/>
<col width="100px"/>
<col width="100px"/>
<col width="75px"/>
<col width="70px"/>
<col width="73px"/>
<col width="72px"/>
<col width="70px"/>
<col width="70px"/>
<tbody>

<tr>
    <td colspan="4">
        මැරුණු පුද්ගලයා හඳුනාගෙන තිබේද? <br>
        இறந்த நபர் அடையாளம் காணப்பட்டுள்ளாரா?<br>
        Is the departed person identified?
    </td>
    <td colspan="7">
        <s:if test="session.deathRegister.deathPerson.personIdentified ==true">
            <s:label value="%{getText('yes.label')}"/>
        </s:if>
        <s:else>
            <s:label value="%{getText('no.label')}"/>
        </s:else>
    </td>
</tr>
<tr>
    <td>
        ශ්‍රී ලාංකිකයෙකු නම්<br/>இலங்கையராயின் <br/>If Sri Lankan
    </td>
    <td colspan="3">
        අනන්‍යතා අංකය / அடையாள எண்
        <br>Identification Number
    </td>
    <td colspan="7" class="find-person">
        <s:label value="%{#session.deathRegister.deathPerson.deathPersonPINorNIC}"/>
    </td>
</tr>
<tr>
    <td rowspan="2">
        විදේශිකය‍කු නම්
        <br>வெளிநாட்டவர் எனின்
        <br>If a foreigner
    </td>
    <td colspan="3">
        රට / நாடு / Country
    </td>
    <td colspan="7">
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
    <td colspan="3">
        ගමන් බලපත්‍ර අංකය / கடவுச் சீட்டு இல. / <br/>Passport Number
    </td>
    <td colspan="7"><s:label value="%{#session.deathRegister.deathPerson.deathPersonPassportNo}"/></td>
</tr>

<tr>
    <td>
        උපන් දිනය
        <br>பிறந்த திகதி
        <br>Date of Birth
    </td>
    <td colspan="2">
            <s:label value="%{#session.deathRegister.deathPerson.deathPersonDOB}"/>
    <td colspan="2">
        වයස
        <br>வயது
        <br>Age
    </td>
    <td>අවුරුදු<br/>வருடங்கள் <br/>Years</td>
    <td>
        <s:label value="%{#session.deathRegister.deathPerson.deathPersonAge}"/>
    </td>
    <td>මාස<br/>மாதங்கள்<br/>Months</td>
    <td>
        <s:label value="%{#session.deathRegister.deathPerson.deathPersonAgeMonth}"/>
    </td>
    <td>දින<br/>திகதி<br/>Days</td>
    <td>
        <s:label value="%{#session.deathRegister.deathPerson.deathPersonAgeDate}"/>
    </td>
</tr>
<tr>
    <td>
        ස්ත්‍රී පුරුෂ භාවය
        <br>பால்
        <br>Gender
    </td>
    <td colspan="2">
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
    <td colspan="2">
        ජන වර්ගය<br/>
        இனம்<br/>
        Ethnic Group
    </td>
    <td colspan="6">
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
    <td>
        නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
        <br>பெயா் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
        <br>Name in either of the official languages (Sinhala / Tamil)
    </td>
    <td colspan="10">
        <s:label value="%{#session.deathRegister.deathPerson.deathPersonNameOfficialLang}"/>
    </td>
</tr>
<tr>
    <td>
        නම ඉංග්‍රීසි භාෂාවෙන්
        <br>பெயா் ஆங்கில மொழியில்
        <br>Name in English
    </td>
    <td colspan="10">
        <s:label value="%{#session.deathRegister.deathPerson.deathPersonNameInEnglish}"/>
    </td>
</tr>
<tr>
    <td rowspan="4">
        ස්ථිර ලිපිනය
        <br>நிரந்தர வதிவிட முகவரி
        <br>Permanent Address
    </td>
    <td colspan="10">
        <s:label value="%{#session.deathRegister.deathPerson.deathPersonPermanentAddress}"/>
    </td>
</tr>
<tr>
    <td colspan="4">
        දිස්ත්‍රික්කය <br>
        மாவட்டம் <br>
        District
    </td>
    <td colspan="7">
        <s:if test="#userPreferedLang == 'si'">
            <s:label
                    value="%{#session.deathRegister.deathPerson.dsDivisionOfPermanentAddress.district.siDistrictName}"/>
        </s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label
                    value="%{#session.deathRegister.deathPerson.dsDivisionOfPermanentAddress.district.senDistrictName}"/>
        </s:elseif>
        <s:else>
            <s:label
                    value="%{#session.deathRegister.deathPerson.dsDivisionOfPermanentAddress.district.taDistrictName}"/>
        </s:else>
    </td>
</tr>
<tr>
    <td colspan="4">
        ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
        பிரதேச செயளாளர் பிரிவு <br/>
        Divisional Secretary Division
    </td>
    <td colspan="7">
        <s:if test="#userPreferedLang == 'si'">
            <s:label value="%{#session.deathRegister.deathPerson.dsDivisionOfPermanentAddress.siDivisionName}"/>
        </s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label value="%{#session.deathRegister.deathPerson.dsDivisionOfPermanentAddress.enDivisionName}"/>
        </s:elseif>
        <s:else>
            <s:label value="%{#session.deathRegister.deathPerson.dsDivisionOfPermanentAddress.taDivisionName}"/>
        </s:else>
    </td>
</tr>
<tr>
    <td colspan="4"><label>
        ග්‍රාම නිළධාරී කොටිඨාශය / கிராம சேவையாளர் பிரிவு/ Grama Niladhari Division</label>
    </td>
    <td colspan="7">
        <s:if test="#userPreferedLang == 'si'">
            <s:label
                    value="%{#session.deathRegister.deathPerson.gnDivision.siGNDivisionName}"/>
        </s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label value="%{#session.deathRegister.deathPerson.gnDivision.enGNDivisionName}"/>
        </s:elseif>
        <s:else>
            <s:label value="%{#session.deathRegister.deathPerson.gnDivision.taGNDivisionName}"/>
        </s:else>
    </td>
</tr>

<s:if test="pageType==3">
    <tr>
        <td colspan="11" height="35px">
            නැතිවූ පුද්ගලයෙකුගේ මරණයක් නම් / காணாமற் போன நபரின் இறப்பாயின் / If the death is for a Missing Person
        </td>
    </tr>
    <tr>
        <td>
            අවසන් වරට පදිංචි ලිපිනය <br>
            கடைசியாக குடியிருந்த இடம் <br>
            Last address
        </td>
        <td colspan="10">
            <s:label value="%{#session.deathRegister.deathPerson.lastAddressOfMissingPerson}"/>
        </td>
    </tr>
</s:if>
<tr>
    <td>
        තත්වය නොහොත් වෘත්තීය <br>
        நிலவரம் அல்லது தொழில் <br>
        Rank or Profession
    </td>
    <td colspan="4">
        <s:label value="%{#session.deathRegister.deathPerson.rankOrProfession}"/>
    </td>
    <td colspan="2">විශ්‍රාම වැටුප් ලාභියෙකුද? <br>
        இளைப்பாற்று ஊதியம் பெறுபவரா? <br>
        Was a Pensioner?
    </td>
    <td colspan="4">
        <s:if test="session.deathRegister.deathPerson.pensioner ==true">
            <s:label value="%{getText('yes.label')}"/>
        </s:if>
        <s:else>
            <s:label value="%{getText('no.label')}"/>
        </s:else>
    </td>
</tr>

<tr>
    <td colspan="5">
        පියාගේ අනන්‍යතා අංකය / தந்தையின் அடையாள எண் / Fathers Identification No.
    </td>
    <td colspan="6" class="find-person">
        <s:label value="%{#session.deathRegister.deathPerson.deathPersonFatherPINorNIC}"/>
    </td>
</tr>
<tr>
    <td colspan="1">
        පියාගේ සම්පුර්ණ නම
        <br>தந்தையின் முழுப் பெயர்
        <br>Fathers full name
    </td>
    <td colspan="10">
        <s:label value="%{#session.deathRegister.deathPerson.deathPersonFatherFullName}"/>
    </td>
</tr>
<tr>
    <td colspan="5">
        මවගේ අනන්‍යතා අංකය / தாயின் அடையாள எண் / Mothers Identification No.
    </td>
    <td colspan="6" class="find-person">
        <s:label value="%{#session.deathRegister.deathPerson.deathPersonMotherPINorNIC}"/>
    </td>
</tr>
<tr>
    <td colspan="1">
        මවගේ සම්පුර්ණ නම
        <br>தாயின் முழுப் பெயர்
        <br>Mothers full name
    </td>
    <td colspan="10">
        <s:label value="%{#session.deathRegister.deathPerson.deathPersonMotherFullName}"/>
    </td>
</tr>
</tbody>
</table>

<div class="font-9">
    මියගියේ වයස අවු. 49ට අඩු කාන්තාවක් නම් පමණක් මෙම කොටස සම්පුර්ණ කල යුතුය
    <br>இறந்த நபர் 49வயதிற்கு குறைந்த பெண்ணாயிருந்தால் மடடும் இப்பகுதி பூரணப்படுத்தப்படல்வேண்டும்
    <br>Fill this section only If the departed is a woman below 49 years
</div>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin-bottom:10; margin-top:10;"
       class="font-9">
    <caption/>
    <col width="4%"/>
    <col width="66%"/>
    <col width="15%"/>
    <col width="15%"/>
    <tbody>
    <tr>
        <td colspan="2">
            මරණය සිදුවනවිට ඇය දරුවකු ලැබීමට (ගර්භනීව) සිටියේද? <br>
            இறப்பு நிகழ்த பொழுது அவர் பிள்ளை பிறசவிக்க (கர்ப்பிணி) இருந்தாரா? <br>
            Was she pregnant at time of death?
        </td>
        <td align="center" colspan="2">
            <s:if test="session.deathRegister.deathPerson.pregnantAtTimeOfDeath == null">
                ----
            </s:if>
            <s:elseif test="session.deathRegister.deathPerson.pregnantAtTimeOfDeath ==true">
                <s:label value="%{getText('yes.label')}"/>
            </s:elseif>
            <s:else>
                <s:label value="%{getText('no.label')}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td style="background:#cccccc;"></td>
        <td>
            මරණයට පෙර සති 6ක් (දින 42ක්) ඇතුලත දී ඇය විසින් දරුවකු ප්‍රසුත කරනු ලැබුවාද? <br>
            இறப்பிற்கு முன் 6 கிழமைகளுக்குள் (42 நாட்களுக்கிடையில் ) அவர் மூலம் பிள்ளை பிரசவிக்கப்பட்டதா?<br>
            Has she given birth in the previous 6 weeks (42 days) ?
        </td>
        <td align="center" colspan="2">
            <s:if test="session.deathRegister.deathPerson.givenABirthWithInPreviouse6Weeks == null">
                ----
            </s:if>
            <s:elseif test="session.deathRegister.deathPerson.givenABirthWithInPreviouse6Weeks ==true">
                <s:label value="%{getText('yes.label')}"/>
            </s:elseif>
            <s:else>
                <s:label value="%{getText('no.label')}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td style="background:#cccccc;"></td>
        <td>
            නැතහොත් ගබ්සාවක් සිදුවී තිබේද? / அல்லது கருக்கலைப்பு நடைப்பெற்றிருந்ததா?<br>
            Has an abortion taken place?
        </td>
        <td align="center" colspan="2">
            <s:if test="session.deathRegister.deathPerson.anAbortionTakenPlace == null">
                ----
            </s:if>
            <s:elseif test="session.deathRegister.deathPerson.anAbortionTakenPlace ==true">
                <s:label value="%{getText('yes.label')}"/>
            </s:elseif>
            <s:else>
                <s:label value="%{getText('no.label')}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td style="background:#cccccc;"></td>
        <td>
            දරු ප්‍රසුතිය හෝ ගබ්සාව සිදුවුයේ මරණය සිදුවීමට දින කීයකට පෙරද?
            <br>பிரசவம் அல்லது கருக்கலைப்பு நடைப்பெற்றது இறப்பு நடைப்பெறுவதற்கு எத்தனை நாட்களுக்கு முன்?
            <br>If a birth or abortion took place, how many days before the death has it occurred?
        </td>
        <td colspan="2" align="left">
            <s:label value="%{#session.deathRegister.deathPerson.daysBeforeAbortionOrBirth}"/>
        </td>
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