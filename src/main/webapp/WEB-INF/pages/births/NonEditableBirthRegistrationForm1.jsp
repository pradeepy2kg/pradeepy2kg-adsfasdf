<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:set value="1" name="row"/>
<script type="text/javascript">
    function initPage() {
    }
</script>

<style type="text/css">
    #birth-certificate-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }
    }

    #birth-certificate-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<div id="birth-certificate-outer">

<s:if test="#request.archivedEntryList.size>0">
    <fieldset style="border:2px inset red; width:400px;">
        <legend style="color:red;"><s:label value="%{getText('ArchivedData.label')}"/></legend>
        <table>
            <th></th>
            <th><s:label name="statuslbl" value="%{getText('status.label')}"/></th>
            <th><s:label value="%{getText('lastupdate.time.label')}"/></th>
            <th><s:label name="viewlbl" value="%{getText('view.label')}"/></th>
            <s:iterator status="archivedStatus" value="archivedEntryList" id="searchId">
                <tr class="<s:if test="#archivedStatus.odd == true">odd</s:if><s:else>even</s:else>">
                    <td class="table-row-index"><s:property value="%{#archivedStatus.count}"/></td>
                    <s:set value="getRegister().getStatus()" name="status"/>
                    <td><s:label value="%{getText(#status)}"/></td>
                    <td><s:property value="getLifeCycleInfo().getLastUpdatedTimestamp()"/></td>
                    <s:set name="abc" value="getLifeCycleInfo().getLastUpdatedTimestamp()"/>

                    <s:url id="viewSelected" action="eprViewBDFInNonEditableMode.do">
                        <s:param name="bdId" value="idUKey"/>
                        <s:param name="advanceSearch" value="%{#request.advanceSearch}"/>
                    </s:url>
                    <td><s:a href="%{viewSelected}" title="%{getText('view.label')}">
                        <img src="<s:url value='/images/view_1.gif'/>" width="25" height="25" border="none"/></s:a></td>
                </tr>
            </s:iterator>
        </table>
    </fieldset>
</s:if>
<s:set value="%{#session.WW_TRANS_I18N_LOCALE.language}" name="userPreferedLang"/>
<table class="table_reg_header_01" style="font-size:9pt">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="300px"></td>
        <td align="center" style="font-size:12pt; width:430px"><img src="<s:url value="/images/official-logo.png"/>"
                                                                    alt=""/><br>
            <s:if test="birthType.ordinal() != 0">
                <label>
                    උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර
                    <br>பிறப்பொன்றை பதிவு செய்வதற்கான விபரங்கள்
                    <br>Particulars for Registration of a Birth</label>
            </s:if>
            <s:else>
                <label>
                    මළ උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර
                    <br>ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள்
                    <br>Particulars for Registration of a Still Birth</label>
            </s:else>
        </td>
        <td>
            <table class="table_non_editable_reg_page" cellspacing="0" cellpadding="0" style="width:300px; ">
                <tr>
                    <td style="width:60%"><label><span class="font-9">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</span></label>
                    </td>
                    <td><s:label name="bdId"/></td>
                </tr>
            </table>
            <table class="table_non_editable_reg_page" cellspacing="0" cellpadding="0"
                   style="width:300px;margin-top:10px;">
                <tr>
                    <td style="width:60%">
                        <label><span class="font-9">භාරගත් දිනය
                            <br>பெறப்பட்ட திகதி <br>Date of Acceptance </span></label>
                    </td>
                    <td><s:label value="%{#request.register.dateOfRegistration}"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3" class="font-8">
            <br>
            <s:if test="birthType.ordinal() == 1 || birthType.ordinal() == 3">
                දැනුම් දෙන්නා (දෙමවිපියන් / භාරකරු) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරියා
                / රෙජිස්ට්‍රාර් වෙත භාර දිය යුතුය. මෙම තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ උපත ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பாதுகாவலர் ) பூர்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம்
                /பதிவாளரிடம் சமர்ப்பிக்கப்படல் வேண்டும். இத்தகவலின்படி சிவில் முறையில் பிறப்பு பதிவு செய்யப்படும்.
                <br>Should be perfected by the informant (Parent / Guardian) and the duly completed form should be
                forwarded to the Officer / Registrar. The birth will be registered in the Civil Registration System
                based on the information provided in this form.
            </s:if>
            <s:elseif test="birthType.ordinal() == 0">
                දැනුම් දෙන්නා (දෙමවිපියන් / භාරකරු) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරි වෙත භාර දිය
                යුතුය. මෙම
                තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ මළ උපත ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம் சமா்ப்பித்தல் வேண்டும். இ
                த்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the informant (Parent / Guardian) and the duly completed form should be
                forwarded
                to the Notifying Authority. The still birth will be registered in the Civil Registration System based on the
                information provided in this form.
            </s:elseif>
            <s:elseif test="birthType.ordinal() == 2">
                දරුකමට හදාගන්න දෙමව්පියන් විසින් සම්පුර්ණ කර, දරුවා උපත ලැබූ දිස්ත්‍රික්කය අයත් සහකාර රෙජිස්ට්‍රාර් ජනරාල් වෙත හෝ කොළඹ රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුවේ ප්‍රධාන කාර්යාලය වෙත භාර දිය යුතුය. මෙම තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ උපත නැවත ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம் சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the adopting parents, and the duly completed form should be forwarded
                to the Assistant Registrar General in charge of the zone where the birth of the child occured; or to the
                head office of the Registrar Generals Department in Colombo. The birth will be re-registered in the
                Civil Registration System based on the information provided in this form.
            </s:elseif>
        </td>
    </tr>
    </tbody>
</table>


<table class="table_non_editable_reg_page" cellspacing="0" cellpadding="0" style="font-size:9pt;">

<caption></caption>
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
    <td colspan="8" style="text-align:center;font-size:11pt;">
        <s:if test="birthType.ordinal() == 0">
            මළ උපත පිලිබඳ විස්තර
            <br>பிள்ளை பற்றிய தகவல்
            <br>Still-birth Information
        </s:if>
        <s:else>
            ළම‌යාගේ විස්තර
            <br>பிள்ளை பற்றிய தகவல்
            <br>Child's Information
        </s:else>
    </td>
</tr>
<%--TODO style not added--%>
<s:if test="birthType.ordinal() == 2">
    <tr style="border-left:1px solid #000000;">
        <td width="150px" colspan="2"><label>
            (1)දරුකමට ගැනීම පිලිබඳ සහතික පත්‍රයේ අංකය
            <br>மகவேற்புச் செய்யப்பட்டது சம்பற்தமான சான்றிதழின் இலக்கம்
            <br>Serial number of the Certificate of Adoption
        </label></td>
        <td colspan="7">
            <s:label value="%{#request.register.adoptionUKey}"/>
        </td>
    </tr>
    <tr style="height:500px;">
        <td rowspan="5"><label>(2)ළමයාගේ උපත කලින් ලියාපදිංචි කර තිබුනේනම් /
            <br>பிள்ளையின் பிறப்பு பதிவு செய்யப்பட்டிருந்தால் /
            <br>If the birth was previously registered</label></td>
        <td><label>දිස්ත්‍රික්කය / மாவட்டம் / District</label></td>
        <td colspan="6" class="table_reg_cell_01">
            <s:label value="%{#request.oldBDInfo.districtName}"/>
        </td>
    </tr>
    <tr>
        <td><label>ප්‍රාදේශීය ලේකම් කොට්ඨාශය /
            <br>பிரதேச செயளாளர் பிரிவு <br/>
            Divisional Secretary Division</label></td>
        <td colspan="6" class="table_reg_cell_01">
            <s:label value="%{#request.oldBDInfo.dsDivisionName}"/>
        </td>
    </tr>
    <tr>
        <td><label>
            ලියාපදිංචි කිරීමේ කොට්ඨාශය /
            <br>பதிவுப் பிரிவு /
            <br>Registration Division
        </label></td>
        <td colspan="6" class="table_reg_cell_01">
            <s:label value="%{#request.oldBDInfo.bdDivisionName}"/>
        </td>
    </tr>
    <tr>
        <td><label>
            ග්‍රාම නිළධාරී කොටිඨාශය /<br/>
            கிராம சேவையாளர் பிரிவு/<br/>
            Grama Niladhari Division</label>
        </td>
        <td colspan="6" class="table_reg_cell_01">
            <s:label value="%{#request.oldBDInfo.gnDivisionName}"/>
        </td>
    </tr>
    <tr>
        <td><label>අනුක්‍රමික අංකය/ தொடர் இலக்கம்<br>Serial Number</label></td>
        <td colspan="6"><s:label value="%{#request.oldBDInfo.serialNumber}"/></td>
    </tr>
</s:if>
<tr></tr>
<tr style="border-left:1px solid #000000;" height="35px">
    <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)උපන් දිනය/ பிறந்த திகதி/Date of
        Birth</label></td>
    <td colspan="6">
        <s:label value="%{#request.child.dateOfBirth}"/>
    </td>
</tr>
<tr height="35px">
    <td rowspan="7" width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        උපන් ස්ථානය පිළිබඳ විස්තර<br>பிறந்த இடம் பற்றிய விபரம்<br>
        Particulars of Place of Birth</label></td>
    <td><label>දිස්ත්‍රික්කය/ மாவட்டம் District</label></td>
    <td colspan="6" class="table_reg_cell_01">
        <s:if test="#userPreferedLang == 'si'">
            <s:label
                    value="%{#request.register.birthDivision.dsDivision.district.siDistrictName}"/></s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label
                    value="%{#request.register.birthDivision.dsDivision.district.enDistrictName}"/>
        </s:elseif>
        <s:else>
            <s:label
                    value="%{#request.register.birthDivision.dsDivision.district.taDistrictName}"/>
        </s:else></td>
</tr>
<tr height="35px">
    <td width="200px"><label>
        ප්‍රාදේශීය ලේකම් කොට්ඨාශය
        <br/>பிரதேச செயளாளர் பிரிவு
        <br/>Divisional Secretary Division
    </label></td>
    <td colspan="6" class="table_reg_cell_01" id="table_reg_cell_01"><s:if
            test="#userPreferedLang == 'si'">
        <s:label
                value="%{#request.register.birthDivision.dsDivision.siDivisionName}"
                cssStyle="float:left;  width:240px;"/></s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label
                    value="%{#request.register.birthDivision.dsDivision.enDivisionName}"
                    cssStyle="float:left;  width:240px;"/>
        </s:elseif>
        <s:else>
            <s:label
                    value="%{#request.birthRegister.register.birthDivision.dsDivision.taDivisionName}"
                    cssStyle="float:left;  width:240px;"/>
        </s:else>
    </td>
</tr>
<tr>
    <td><label>ලියාපදිංචි කිරීමේ කොටිඨාශය<br>பதிவுப் பிரிவு <br>Registration Division</label></td>
    <td colspan="6" class="table_reg_cell_01">
        <s:if test="#userPreferedLang == 'si'">
            <s:label value="%{#request.register.birthDivision.siDivisionName}"/>
        </s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label value="%{#request.register.birthDivision.enDivisionName}"/>
        </s:elseif>
        <s:else>
            <s:label value="%{#request.register.birthDivision.taDivisionName}"/>
        </s:else>
    </td>
</tr>
<tr>
    <td><label>
        ග්‍රාම නිළධාරී කොටිඨාශය <br/>
        கிராம சேவையாளர் பிரிவு<br/>
        Grama Niladhari Division</label>
    </td>
    <td colspan="6" class="table_reg_cell_01">
        <s:if test="#userPreferedLang == 'si'">
            <s:label value="%{#request.register.gnDivision.siGNDivisionName}"/>
        </s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label value="%{#request.register.gnDivision.enGNDivisionName}"/>
        </s:elseif>
        <s:else>
            <s:label value="%{#request.register.gnDivision.taGNDivisionName}"/>
        </s:else>
    </td>
</tr>
<tr height="35px">
    <td rowspan="2"><label>උපන් ස්ථානය<br/>பிறந்த இடம்<br/>Place of Birth</label></td>
    <td><label>සිංහල හෝ දෙමළ භාෂාවෙන් <br>சிங்களம்அல்லது தமிழ் மொழியில்<br>In Sinhala or Tamil</label></td>
    <td colspan="5"><s:label value="%{#request.child.placeOfBirth}" id="placeOfBirth"
                             cssStyle="width:97.6%;"/></td>
</tr>
<tr>
    <td><label>ඉංග්‍රීසි භාෂාවෙන් <br>
        இங்கிலீஷ் <br>
        In English</label></td>
    <td colspan="6"><s:label value="%{#request.child.placeOfBirthEnglish}"
                             id="placeOfBirthEnglish" cssStyle="width:97.6%;"/></td>
</tr>
<tr>
    <td colspan="2"><label> උපත සිදුවුයේ රෝහලකද?
        <br> பிறப்பு நிகழ்ந்தது வைத்திய சாலையிலா?
        <br> Did the birth occur at a hospital?
    </label></td>

    <td colspan="3">
        <s:if test="#request.child.birthAtHospital ==true">
        <s:label value="%{getText('yes.label')}"/></td>
    </s:if>
    <s:else>
        <s:label value="%{getText('no.label')}"/>
    </s:else>
</tr>
<s:if test="birthType.ordinal() != 0">
    <tr>
        <td class="font-9"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
            <br>பெயா் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
            <br>Name in any of the official languages (Sinhala / Tamil)</label></td>
        <td colspan="7"><s:label value="%{#request.child.childFullNameOfficialLang}"
                                 cssStyle="width:98.2%;"/></td>
    </tr>

    <tr>
        <td class="font-9"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            නම ඉංග්‍රීසි භාෂාවෙන් <br>பெயா் ஆங்கில மொழியில்<br>Name in English
        </label></td>
        <td colspan="7">
            <s:label value="%{#request.child.childFullNameEnglish}" cssStyle="width:98.2%;"/></td>
    </tr>
</s:if>
<tr>
    <td class="font-9" colspan="2"><label>
        සහතිකය නිකුත් කල යුතු භාෂාව <br>பிறப்புச் சான்றிதழ் வழங்கப்பட வேண்டிய  மொழி<br>Preferred Language for Birth Certificate </label></td>
    <s:set name="lang" value="%{#request.register.preferredLanguage}"/>
    <td colspan="6"><s:label value="%{getText(#lang)}" cssStyle="float:left;  width:240px;"/></td>
</tr>
<tr>
    <td class="font-9"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)ස්ත්‍රී පුරුෂ භාවය<br>
        பால் <br>Gender of the child</label></td>
    <td>
        <s:if test="#request.child.childGender == 0">
            <s:label name="" value="%{getText('male.label')}"/>
        </s:if>
        <s:elseif test="#request.child.childGender == 1">
            <s:label name="" value="%{getText('female.label')}"/>
        </s:elseif>
        <s:elseif test="#request.child.childGender == 2">
            <s:label name="" value="%{getText('unknown.label')}"/>
        </s:elseif>
    </td>
    <s:if test="birthType.ordinal() == 1 || birthType.ordinal() == 3">
        <td width="300px;"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) උපත් බර<br>பிறப்பு நிறை<br>Birth
            Weight (kg)</label></td>
        <td><s:label value="%{#request.child.childBirthWeight}"
                     cssStyle="width:95%;"/></td>
    </s:if>
    <s:if test="birthType.ordinal() == 2">
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) උපත් බර (දන්නේ නමි)<br>பிறப்பு
            நிறை<br>Birth Weight, if known (kg)</label></td>
        <td><s:label value="%{#request.child.childBirthWeight}"/></td>
    </s:if>
    <s:elseif test="birthType.ordinal() == 0">
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) දරැවා මැරී උපදින විට ගර්භයට සති
            කීයක් වී තිබුනේද යන්න
            <br>பிள்ளை இறந்து பிறந்த பொழுது கா்ப்பத்திற்கு எத்தனை கிழமை
            <br>Number of weeks pregnant at the time of still-birth</label></td>
        <td><s:label value="%{#request.child.weeksPregnant}" cssStyle="width:95%;"/></td>
    </s:elseif>
</tr>
<tr>
    <td class="font-9" colspan="3"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        සජිවි උපත් අනුපිළි‍‍වල අනුව කීවෙනි ළමයා
        ද?
        <br>பிறப்பு ஒழுங்கு
        <br>According to Live Birth Order, rank of the child?</label></td>
    <td class="font-9"><s:label value="%{#request.child.childRank}"/></td>
</tr>
<tr>
    <td class="font-9" colspan="3"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)නිවුන් දරු උපතක්
        නම්,
        දරුවන් ගනන<br>ஓரே சுழலில் ஒன்றுக்கு மேற்பட்ட பிள்ளை பிறந்திருந்தால் , பிள்ளைகளின் எண்ணிக்கை
        <br>If
        multiple births, number of children</label></td>
    <td><s:label value="%{#request.child.numberOfChildrenBorn}" cssStyle="width:95%;"/></td>
</tr>
</tbody>
</table>

<div style="page-break-after:always;"></div>
<table class="table_non_editable_reg_page" cellspacing="0" style="margin-top:20px;font-size:9pt;">
    <caption></caption>
    <col width="200px"/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td colspan="9" style="text-align:center;font-size:11pt">පියාගේ විස්තර
            <br>தந்தை பற்றிய தகவல்
            <br>Details of the Father
        </td>
    </tr>
    <tr>
        <td>
            (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            ශ්‍රී ලාංකිකයෙකු නම්<br/>இலங்கையராயின் <br/>If Sri Lankan
        </td>
        <td colspan="2"><label>
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>Identification Number</label></td>
        <td colspan="5"><s:label
                value="%{#request.parent.fatherNICorPIN}"/>

        </td>
    </tr>
    <tr>
        <td colspan="1" rowspan="2" width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
        </td>
        <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
        <td colspan="5"><s:if test="#userPreferedLang == 'si'">
            <s:label
                    value="%{#request.parent.fatherCountry.siCountryName}"
                    cssStyle="width:97%;"/></s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label
                        value="%{#request.parent.fatherCountry.enCountryName}"
                        cssStyle="width:97%;"/>
            </s:elseif>
            <s:else>
                <s:label
                        value="%{#request.parent.fatherCountry.taCountryName}"
                        cssStyle="width:97%;"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு இல.<br>Passport No.</label></td>
        <td colspan="5"><s:label value="%{#request.parent.fatherPassportNo}"/></td>
    </tr>
    </tbody>

    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            සම්පුර්ණ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
            <br>முழுப் பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
            <br>Full Name in any of the official languages (Sinhala / Tamil)</label></td>
        <td colspan="7">
            <s:label value="%{#request.parent.fatherFullName}" cssStyle="width:98%;"/>
        </td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>) සම්පුර්ණ
            නම ඉංග්‍රීසි භාෂාවෙන් (කැපිටල් අකුරෙන්)
            <br>முழுப் பெயர் ஆங்கில மொழியில்(பெரிய எழுத்துக்களில்)<br>Full Name in English (In block letters)</label>
        </td>
        <td colspan="7">
            <s:label value="%{#request.parent.fatherFullNameInEnglish}" cssStyle="width:98%;"/>
        </td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)උපන් දිනය <br>பிறந்த
            திகதி <br>Date of Birth</label></td>
        <td>
            <s:label value="%{#request.parent.fatherDOB}"/>
        </td>
        <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            ජන වර්ගය <br/>இனம்<br/>Ethnic Group</label>
        </td>
        <td colspan="2" class="table_reg_cell_02"><s:if test="#userPreferedLang == 'si'">
            <s:label
                    value="%{#request.parent.fatherRace.siRaceName}" cssStyle="width:200px;"/></s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label
                        value="%{#request.parent.fatherRace.enRaceName}" cssStyle="width:200px;"/>
            </s:elseif>
            <s:else>
                <s:label
                        value="%{#request.parent.fatherRace.taRaceName}" cssStyle="width:200px;"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)උපන් ස්ථානය <br>பிறந்த
            இடம் <br>Place of Birth</label></td>
        <td colspan="7"><s:label value="%{#request.parent.fatherPlaceOfBirth}"
                                 cssStyle="width:95%;"/></td>
    </tr>
</table>

<table class="table_non_editable_reg_page" cellspacing="0" style="margin-top:20px;font-size:9pt;">
<caption></caption>
<col width="200px"/>
<col width="150px"/>
<col width="150px"/>
<col/>
<col/>
<col/>
<col/>
<col width="200px"/>
<tbody>
<tr>
    <td colspan="9" style="text-align:center;font-size:11pt"> මවගේ විස්තර <br>தாய் பற்றிய தகவல்<br>Details of the
        Mother
    </td>
</tr>
<tr>
    <td>
        (<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>)
        ශ්‍රී ලාංකිකයෙකු නම්<br/>இலங்கையராயின் <br/>If Sri Lankan
    </td>
    <td colspan="2"><label>අනන්‍යතා
        අංකය <br>
        அடையாள எண் <br>Identification Number</label></td>
    <td colspan="5"><s:label value="%{#request.parent.motherNICorPIN}"/></td>
</tr>
<tr>
    <td rowspan="2" width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
    </td>
    <td colspan="2"><label>රට / நாடு / Country</label></td>
    <td colspan="5" width="120px"><s:if test="#userPreferedLang == 'si'">
        <s:label
                value="%{#request.parent.motherCountry.siCountryName}"
                cssStyle="width:97%;"/></s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label
                    value="%{#request.parent.motherCountry.enCountryName}"
                    cssStyle="width:97%;"/>
        </s:elseif>
        <s:else>
            <s:label
                    value="%{#request.parent.motherCountry.taCountryName}"
                    cssStyle="width:97%;"/>
        </s:else>
    </td>
</tr>
<tr>
    <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு இல.<br>Passport No.</label></td>
    <td colspan="5"><s:label value="%{#request.parent.motherPassportNo}"/></td>
</tr>
<tr>
    <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        සම්පුර්ණ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
        <br>முழுப் பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
        <br>Full Name in any of the official languages (Sinhala / Tamil)</label></td>
    <td colspan="8">
        <s:label value="%{#request.parent.motherFullName}" cssStyle="width:98%;"/>
    </td>
</tr>
<tr>
    <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>) සම්පුර්ණ
        නම ඉංග්‍රීසි භාෂාවෙන් (කැපිටල් අකුරෙන්)
        <br>முழுப் பெயர் ஆங்கில மொழியில்(பெரிய எழுத்துக்களில்)<br>Full Name in English (In block letters)</label></td>
    <td colspan="7">
        <s:label value="%{#request.parent.motherFullNameInEnglish}" cssStyle="width:98%;"/>
    </td>
</tr>
<tr>
    <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)උපන් දිනය <br>பிறந்த
        திகதி <br>Date of Birth</label></td>
    <td colspan="2"><s:label value="%{#request.parent.motherDOB}" cssStyle="width:50%;"/></td>
    <td colspan="2"><label>
        <s:if test="#request.register.birthType.ordinal() != 0">
            (<s:property value="#row"/><s:set name="row"
                                              value="#row+1"/>) ළමයාගේ උපන් දිනට මවගේ වයස<br> பிள்ளை பிறந்த திகதியில் மாதாவின் வயது<br>Mother's Age
            as at
            the date of birth of child
        </s:if>
        <s:else>
            (<s:property value="#row"/><s:set name="row"
                                              value="#row+1"/>) ළමයාගේ මළ උපන් දිනට මවගේ වයස<br> பிள்ளை பிறந்த திகதியில் மாதாவின் வயது<br>Mother's Age
            as at the date of still-birth of child
        </s:else>
    </label>
    </td>
    <td colspan="3"><s:label value="%{#request.parent.motherAgeAtBirth}"/></td>
</tr>
<tr>
    <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i" value="#i+1"/>)
        ජන වර්ගය <br/>
        இனம்<br/>
        Ethnic Group</label></td>
    <td colspan="2">
        <s:if test="#userPreferedLang == 'si'">
            <s:label value="%{#request.parent.motherRace.siRaceName}"/>
        </s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label value="%{#request.parent.motherRace.enRaceName}"/>
        </s:elseif>
        <s:else>
            <s:label value="%{#request.parent.motherRace.taRaceName}"/>
        </s:else>
    </td>

    <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                               value="#i+1"/>
        ) උපන් ස්ථානය <br>பிறந்த இடம்
        <br>Place of Birth</label></td>
    <td colspan="3">
        <s:label value="%{#request.parent.motherPlaceOfBirth}"/>
    </td>
</tr>
<tr>
    <td rowspan="4"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)මවගේ ස්ථිර
        ලිපිනය<br>தாயின் நிரந்தர வதிவிட முகவரி<br>Permanent
        Address of the Mother</label>
    </td>
    <td colspan="7" style="border-bottom:none;" height="50px"><s:label value="%{#request.parent.motherAddress}"
                                                                       cssStyle="width:98%;"/></td>
</tr>
<tr>
    <td colspan="2" class="table_reg_cell_02" style="border-top:1px solid #000;">
        <s:label value="දිස්ත්‍රික්කය /மாவட்டம் /District"/>
    </td>
    <td colspan="5" class="table_reg_cell_02" style="border-top:1px solid #000;">

        <s:if
                test="#userPreferedLang == 'si'">
            <s:label value="%{#request.parent.motherDSDivision.district.siDistrictName}"
                     cssStyle="width:99%;"/>
        </s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label value="%{#request.parent.motherDSDivision.district.enDistrictName}"
                     cssStyle="width:99%;"/>

        </s:elseif>
        <s:else>
            <s:label value="%{#request.parent.motherDSDivision.district.taDistrictName}"
                     cssStyle="width:99%;"/>
        </s:else></td>
</tr>
<tr>
    <td colspan="2"><s:label value="ප්‍රාදේශීය ලේකම් කොට්ඨාශය /"/><br>
        <s:label value="பிரதேச செயளாளர் பிரிவு/"/><br>
        <s:label value="Divisional Secretary Division"/><br></td>
    <td colspan="5" class="table_reg_cell_02">
        <s:if
                test="#userPreferedLang == 'si'">
            <s:label value="%{#request.parent.motherDSDivision.siDivisionName}"/>
        </s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label value="%{#request.parent.motherDSDivision.enDivisionName}"/>

        </s:elseif>
        <s:else>
            <s:label value="%{#request.parent.motherDSDivision.taDivisionName}"/>
        </s:else>
    </td>
</tr>
<tr>
    <td colspan="2">
        ග්‍රාම නිලධාරී කොට්ඨාශය/<br/>
        கிராம சேவையாளர் பிரிவு/<br/>
        Grama Niladhari Division
    </td>
    <td colspan="5" class="table_reg_cell_02">
        <s:if
                test="#userPreferedLang == 'si'">
            <s:label value="%{#request.parent.motherGNDivision.siGNDivisionName}"/>
        </s:if>
        <s:elseif test="#userPreferedLang == 'en'">
            <s:label value="%{#request.parent.motherGNDivision.enGNDivisionName}"/>

        </s:elseif>
        <s:else>
            <s:label value="%{#request.parent.motherGNDivision.taGNDivisionName}"/>
        </s:else>
    </td>
</tr>
<tr>
    <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/> <s:set name="i" value="#i+1"/>)
        සම්බන්ධ කල හැකි තොරතුරු
        <br>தொடர்பு கொள்ளக்கூடிய தகவல்கள்
        <br>Contact Details </label></td>
    <td colspan="1"><label>දුරකතනය <br>தொலைபேசி இலக்கம் <br> Telephone</label></td>
    <td colspan="1"><s:label value="%{#request.parent.motherPhoneNo}"/></td>
    <td colspan="2"><label>ඉ – තැපැල<br> மின்னஞ்சல்<br>Email</label></td>
    <td colspan="3" class="passport"><s:label value="%{#request.parent.motherEmail}"/></td>
</tr>
<tr>
    <td colspan="3" rowspan="2">(<s:property value="#row"/><s:set name="row" value="#row+1"/> <s:set name="i"
                                                                                                     value="#i+1"/>)
        රෝහලට ඇතුලත් කිරිමේ තොරතුරු (ඔබ සතුව පවතී නම්)<br/>
        வைத்தியசாலை அனுமதி இலக்கமும் திகதியும் (இருந்தால்)<br/>
        Hospital Admission Details (if available)
    </td>
    <td colspan="2"><label>අංකය / இலக்கம் / Number </label></td>
    <td colspan="4" class="passport"><s:label value="%{#request.parent.motherAdmissionNo}"/></td>
</tr>
<tr>
    <td colspan="2"><span>දිනය/ திகதி / Date</span>
    <td colspan="3">
        <s:label value="%{#request.parent.motherAdmissionDate}"/>
    </td>
</tr>
</tbody>
</table>

<%--pageNo 3--%>
<div style="page-break-after:always;"></div>
<s:if test="#request.register.birthType.ordinal() != 0">
    <table class="table_non_editable_reg_page" cellspacing="0" style="margin-top:20px;font-size:9pt;">
        <caption></caption>
        <col width="450px"/>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td colspan="5" style="text-align:center;font-size:11pt">විවාහයේ විස්තර
                <br>திருமணத்தின் விபரங்கள்
                <br>Details of the Marriage
            </td>
        </tr>
        <tr>
            <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) මව්පියන් විවාහකද?
                <br>பெற்றோர்கள் திருமணம் முடித்தவர்களா?
                <br>Were Parent's Married?</label></td>
            <td colspan="4">
                <s:if test="#request.marriage.parentsMarried.ordinal() == 0">
                    <s:label value="%{getText('radio_married_unknown.label')}"/>
                </s:if>
                <s:if test="#request.marriage.parentsMarried.ordinal() == 1">
                    <s:label value="%{getText('radio_married_yes.label')}"/>
                </s:if>
                <s:if test="#request.marriage.parentsMarried.ordinal() == 2">
                    <s:label value="%{getText('radio_married_no.label')}"/>
                </s:if>
                <s:if test="#request.marriage.parentsMarried.ordinal() == 3">
                    <s:label value="%{getText('radio_married_since.label')}"/>
                </s:if>
            </td>
        </tr>
        <tr>
            <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                විවාහ වු ස්ථානය / விவாகம் இடம்பெற்ற இடம் <br/> Place of Marriage
            </label></td>
            <td colspan="4"><s:label value="%{#request.marriage.placeOfMarriage}"/></td>
        </tr>
        <tr>
            <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                විවාහ වු දිනය / விவாகம் இடம்பெற்ற திகதி <br/> Date of Marriage
            </label></td>
            <td colspan="4">
                <s:label value="%{#request.marriage.dateOfMarriage}"/>
            </td>
        </tr>
        <tr id="motherFatherSign">
            <td colspan="5"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                මව්පියන් විවාහ වි නොමැති නම් පියාගේ තොරතුරු ඇතුලත් කර ගැනිම සදහා මව සහ පියාගේ අත්සන්
                <br>பெற்றோர் விவாகம் செய்யாதிருப்பின், தகப்பனின் தகவல்கள் பதிவு செய்ய வேண்டுமெனின் பெற்றோரின் கையொப்பம்
                <br>If parents are not married, signatures of mother and father to include father's particulars</label>
            </td>
        </tr>
        <tr>
            <td><label>මවගේ අත්සන <br> தாயின் ஒப்பம் <br>Mother’s Signature</label></td>
            <td align="center">
                <s:if test="#request.marriage.motherSigned==true">
                    <s:label value="%{getText('yes.label')}"/>
                </s:if>
                <s:else><s:label value="%{getText('no.label')}"/></s:else>
            </td>
            <td><label>පියාගේ අත්සන <br>தகப்பனின் ஒப்பம் <br>Father’s Signature</label></td>
            <td align="center">
                <s:if test="#request.marriage.fatherSigned==true">
                    <s:label value="%{getText('yes.label')}"/></s:if>
                <s:else><s:label value="%{getText('no.label')}"/></s:else>
            </td>
        </tr>
        </tbody>
    </table>

    <table class="table_non_editable_reg_page" cellspacing="0" cellpadding="0" style="margin-top:20px;font-size:9pt;">
        <col/>
        <col width="250px"/>
        <col width="150px"/>
        <col/>
        <col/>
        <col/>
        <col width="350px"/>
        <tbody>
        <tr>
            <td colspan="7" style="text-align:center;font-size:12pt">
                මුත්තා / මී මුත්තා ගේ විස්තර
                <br>பாட்டன் /பூட்டனின் விபரங்கள்
                <br>Details of the Grand Father / Great Grand Father
            </td>
        </tr>
        <tr>
            <td colspan="7"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) ළමයාගේ මුත්තා ශ්‍රී
                ලංකාවේ උපන්නේ නම්
                / பிள்ளையின் பாட்டனார் இலங்கையில் பிறந்திருந்தால்
                / If grandfather of the child born in Sri Lanka</label>
            </td>
        </tr>
        <tr>
            <td rowspan="3" style="width:75px" colspan="1">&nbsp;</td>
            <td>අනන්‍යතා අංකය (තිබේ නම්)
                <br>அடையாள எண் (இருக்குமாயின்)
                <br>Identification Number (if available)
            </td>
            <td colspan="5">
                <s:label value="%{#request.grandFather.grandFatherNICorPIN}" cssStyle="width:98%;"/>
            </td>
        </tr>
        <tr>
            <td colspan="1"><label>ඔහුගේ සම්පුර්ණ නම<br>அவரின் முழுப் பேயர் <br>His Full Name</label></td>
            <td colspan="5">
                <s:label value="%{#request.grandFather.grandFatherFullName}" cssStyle="width:98%;"/>
            </td>
        </tr>
        <tr>
            <td><label>ඔහුගේ උපන් වර්ෂය
                <br>அவர் பிறந்த வருடம்
                <br>His Year of Birth</label></td>
            <td colspan="2"><s:label value="%{#request.grandFather.grandFatherBirthYear}"/></td>
            <td><label>උපන් ස්ථානය <br>அவர் பிறந்த இடம் <br>Place Of Birth</label></td>
            <td colspan="2"><s:label value="%{#request.grandFather.grandFatherBirthPlace}"/></td>
        </tr>
        <tr>
            <td colspan="7"><label> (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                ළමයාගේ පියා ශ්‍රී ලංකාවේ නොඉපිද මීමුත්තා ලංකාවේ උපන්නේ නම් මී මුත්තාගේ
                <br>பிள்ளையின் தந்தை இலங்கையில் பிறக்காமல் பூட்டன் இலங்கையில் பிறந்திருந்தால் பூட்டனாரின் தகவல்கள்
                <br>If the father was not born in Sri Lanka and if great grand father born in Sri Lanka, great grand
                father's</label>
            </td>
        </tr>
        <tr>
            <td rowspan="3" colspan="1">&nbsp;</td>
            <td>අනන්‍යතා අංකය (තිබේ නම්)
                <br>அடையாள எண் (இருக்குமாயின்)
                <br>Identification Number (if available)
            </td>
            <td colspan="5">
                <s:label value="%{#request.grandFather.greatGrandFatherNICorPIN}" cssStyle="width:95%;"/>
            </td>
        </tr>
        <tr>
            <td colspan="1"><label>සම්පුර්ණ නම <br>முழுப் பெயர் <br>Full Name</label></td>
            <td colspan="5">
                <s:label value="%{#request.grandFather.greatGrandFatherFullName}" cssStyle="width:98%;"/>
            </td>
        </tr>
        <tr>
            <td><label>ඔහුගේ උපන් වර්ෂය
                <br>அவர் பிறந்த வருடம்
                <br>His Year of Birth</label></td>
            <td colspan="2"><s:label value="%{#request.grandFather.greatGrandFatherBirthYear}"
                                     cssStyle="width:95%;"/></td>
            <td><label>උපන් ස්ථානය <br>அவர் பிறந்த இடம் <br>Place Of Birth</label></td>
            <td colspan="3"><s:label value="%{#request.grandFather.greatGrandFatherBirthPlace}"
                                     cssStyle="width:95%;"/></td>
        </tr>
        </tbody>
    </table>
</s:if>

<div style="page-break-after:always;"></div>
<table class="table_non_editable_reg_page" cellspacing="0" cellpadding="0" style="margin-top:20px;font-size:9pt;">
    <caption></caption>
    <col width="250px"/>
    <col width="100px"/>
    <col width="100px"/>
    <col/>
    <tbody>
    <tr>
        <td colspan="5" style="text-align:center;font-size:11pt">දැනුම් දෙන්නාගේ විස්තර<br>தகவல் தருபவரின் விபரங்கள்<br>Details
            of the Informant
        </td>
    </tr>
    <tr>
        <td colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)දැනුම් දෙන්නේ
            කවුරුන්
            විසින් ද? <br>யாரால் தகவல் தரப்படுகின்றது?<br>Person Giving
            Information</label></td>
        <td colspan="4">
            <s:if test="#request.informant.informantType.ordinal() == 1">
            <s:if test="#userPreferedLang == 'si'">
                <s:label value="මව"/>
            </s:if>
            <s:if test="#userPreferedLang == 'ta'">
                <s:label value="மாதா "/>
            </s:if>
            <s:if test="#userPreferedLang == 'en'">
                <s:label value="Mother"/>
            </s:if>
            </s:if>

            <s:elseif test="#request.informant.informantType.ordinal() == 0">
            <s:if test="#userPreferedLang == 'si'">
                <s:label value="පියා"/>
            </s:if>
            <s:if test="#userPreferedLang == 'ta'">
                <s:label value=" பிதா "/>
            </s:if>
            <s:if test="#userPreferedLang == 'en'">
                <s:label value="Father"/>
            </s:if>
            </s:elseif>
            <s:elseif test="#request.informant.informantType.ordinal() == 2">
            <s:if test="#userPreferedLang == 'si'">
                <s:label value="භාරකරු"/>
            </s:if>
            <s:if test="#userPreferedLang == 'ta'">
                <s:label value=" பாதுகாவலர்"/>
            </s:if>
            <s:if test="#userPreferedLang == 'en'">
                <s:label value="Guardian"/>
            </s:if>
            </s:elseif>

            <s:else>
            <s:if test="#userPreferedLang == 'si'">
                <s:label value="නෑයන්"/>
            </s:if>
            <s:if test="#userPreferedLang == 'ta'">
                <s:label value=" பாதுகாவலர் "/>
            </s:if>
            <s:if test="#userPreferedLang == 'en'">
                <s:label value="Relative"/>
            </s:if>
            </s:else>
    </tr>
    <tr>
        <td colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) අනන්‍යතා අංකය <br>
            அடையாள எண் <br>Identification Number</label></td>
        <td colspan="4"><s:label value="%{#request.informant.informantNICorPIN}"/></td>
    </tr>
    <tr>
        <td colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) නම <br>கொடுப்பவரின் பெயர்
            <br>Name</label></td>
        <td colspan="4"><s:label value="%{#request.informant.informantName}" cssStyle="width:98%;"/></td>
    </tr>
    <tr>
        <td colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)තැපැල් ලිපිනය
            <br>அஞ்சல் முகவரி
            <br>Postal Address</label></td>
        <td colspan="4"><s:label value="%{#request.informant.informantAddress}"
                                 cssStyle="width:98%;"/></td>
    </tr>
    <tr>
        <td>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            ඇමතුම් විස්තර<br>தொடர்பு விபரம்<br>Contact Details
        </td>
        <td colspan="1" width="200px"><label>දුරකතනය<br>தொலைபேசி இலக்கம் <br>Telephone</label></td>
        <td colspan="1" width="200px"><s:label value="%{#request.informant.informantPhoneNo}"
                                               cssStyle="width:95%;"/></td>
        <td colspan="1" width="200px"><label>ඉ -තැපැල <br>மின்னஞ்சல் <br>Email</label></td>
        <td colspan="1" width="200px"><s:label value="%{#request.informant.informantEmail}" id="informantEmail"
                                               cssStyle="width:95%;"/></td>

    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)<label>දිනය <br>திகதி
            <br>Date</label></td>
        <td colspan="4"><s:label value="%{#request.informant.informantSignDate}"/></td>
    </tr>
    </tbody>
</table>
<%--End page no 3--%>


<table class="table_non_editable_reg_page" cellspacing="0" cellpadding="0" style="margin-top:20px;font-size:9pt;">
    <caption></caption>
    <col width="250px"/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td colspan="5" style="text-align:center;font-size:11pt">
            තොරතුරු වාර්තා කරන නිලධාරියාගේ / රෙජිස්ට්‍රාර්ගේ විස්තර
            <br>அறிக்கையிடும் அதிகாரி/பதிவாளர் பற்றிய விபரங்கள்
            <br>Details of the Notifying Officer / Registrar
        </td>
    </tr>
    <tr>
        <td colspan="3"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) අනන්‍යතා අංකය <br>
            அடையாள எண் <br>Identification Number</label></td>
        <td colspan="2" class="find-person" width="200px">
            <s:label value="%{#request.notifyingAuthority.notifyingAuthorityPIN}"/>
        </td>
    </tr>
    <tr>
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) නම<br>கொடுப்பவரின் பெயர்
            <br>Name</label></td>
        <td colspan="4">
            <s:label value="%{#request.notifyingAuthority.notifyingAuthorityName}"
                     cssStyle="width:98%;"/>
        </td>
    </tr>
    <tr>
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)තැපැල් ලිපිනය
            <br>தபால் முகவரி
            <br>Postal Address</label></td>
        <td colspan="4"><s:label value="%{#request.notifyingAuthority.notifyingAuthorityAddress}"
                                 cssStyle="width:98%;"/></td>
    </tr>
    <tr>
        <td width="200px"><label>දිනය <br>திகதி <br>Date</label></td>
        <td colspan="4"><s:label
                value="%{#request.notifyingAuthority.notifyingAuthoritySignDate}"/></td>
    </tr>
    </tbody>
</table>

<s:if test="#request.register.birthType.ordinal() == 1 || #request.register.birthType.ordinal() == 3">
    <s:if test="#request.bdfLateOrBelated ==1 || #request.bdfLateOrBelated==2">
        <table class="table_reg_page_04" width="100%" cellspacing="0" style="margin-top:20px;">
            <caption></caption>
            <col width="250px"/>
            <col/>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td colspan="5" style="text-align:center;font-size:12pt">
                    <s:if test="bdfLateOrBelated==1">
                        පමා වූ උපත් ලියාපදිංචිය
                        <br>தாமதித்த பிறப்புப் பதிவு
                        <br>Late Birth Registration
                    </s:if>
                    <s:else>
                        කල් පසු වූ උපත් ලියාපදිංචිය
                        <br>காலங் கடந்த பிறப்புப் பதிவு
                        <br>Belated Birth Registration
                    </s:else>
                </td>
            </tr>
            <tr>
                <td width="200px"><label>ලිපිගොනු අංකය<br>கோவை இலக்கம் <br>Case File Number</label></td>
                <td colspan="2"><s:label value="%{#request.register.caseFileNumber}"/></td>
            </tr>
            <tr>
                <td><label>අදහස් දක්වන්න <br>கருத்தினை தெரிவிக்கவும் <br>Add Comments </label></td>
                <td><s:label value="%{#request.register.comments}"/></td>
            </tr>
            </tbody>
        </table>
    </s:if>
</s:if>
</div>

<div class="form-submit">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
</div>
<s:if test="advanceSearch">
    <s:form action="eprBirthsAdvancedSearch.do">
        <div class="form-submit">
            <s:submit value="%{getText('search_record.label')}" cssStyle="margin-top:10px;"/>
        </div>
    </s:form>
</s:if>
<s:else>
    <s:form action="eprSearchPageLoad.do">
        <div class="form-submit">
            <s:submit value="%{getText('previous.label')}" cssStyle="margin-top:10px;"/>
        </div>
    </s:form>
</s:else>