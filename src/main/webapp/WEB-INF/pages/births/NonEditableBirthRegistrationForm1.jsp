<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-registration-form-outer" id="birth-registration-form-1-outer">

<s:if test="#request.archivedEntryList.size>0">
<fieldset style="border:2px inset red; width:400px;" >
    <legend style="color:red;">
        <s:label value="%{getText('ArchivedData.label')}"/>
    </legend>
    <table>
        <th></th>
        <th><s:label name="statuslbl" value="%{getText('status.label')}"/></th>
        <th><s:label value="%{getText('lastupdate.time.label')}"/> </th>
        <th><s:label name="viewlbl" value="%{getText('view.label')}"/></th>
        <s:iterator status="archivedStatus" value="archivedEntryList" id="searchId">
            <tr class="<s:if test="#archivedStatus.odd == true">odd</s:if><s:else>even</s:else>">
                <td class="table-row-index"><s:property value="%{#archivedStatus.count}"/></td>
                <s:set value="getRegister().getStatus()" name="status"/>
                <td><s:label value="%{getText(#status)}"/></td>
                <td><s:property value="lastUpdatedTime"/> </td>
                <s:set name="abc" value="lastUpdatedTime"/>
                
                <s:url id="viewSelected" action="eprViewBDFInNonEditableMode.do">
                    <s:param name="bdId" value="idUKey"/>
                </s:url>
                <td><s:a href="%{viewSelected}" title="%{getText('view.label')}">
                    <img src="<s:url value='/images/view_1.gif'/>" width="25" height="25"
                         border="none"/></s:a></td>
            </tr>
        </s:iterator>
    </table>
</fieldset>
</s:if>
<s:form action="eprViewBDFInNonEditableMode" name="nonEditableBirthRegistrationForm1" method="POST">
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
            <s:if test="liveBirth">
                <label>
                    උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර
                    <br>ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள்
                    <br>Particulars for Registration of a Birth</label>
            </s:if>
            <s:else>
                <label>
                    මළ උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර
                    <br>* In Tamil
                    <br>Particulars for Registration of a Still Birth</label>
            </s:else>
        </td>
        <td>
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <td><label><span class="font-8">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</span></label>
                    </td>
                    <td><s:label value="%{#session.birthRegister.register.bdfSerialNo}"/></td>
                </tr>
            </table>
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <td>
                        <s:if test="liveBirth">
                            <label><span class="font-8">යොමුකළ දිනය<br>----------<br>Submitted Date</span></label>
                        </s:if>
                        <s:else>
                            <label><span class="font-8">ලියාපදිංචි කල දිනය<br>* In Tamil<br>Date of Registration</span></label>
                        </s:else>
                    </td>
                    <td><s:label value="%{#session.birthRegister.register.dateOfRegistration}"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <s:if test="liveBirth">
                දැනුම් දෙන්නා (දෙමවිපියන් / භාරකරු) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරි වෙත භාර දිය
                යුතුය. මෙම
                තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ උපත ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம்
                சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the informant (Parent / Guardian) and the duly completed form should be
                forwarded
                to the Notifying Authority. The birth will be registered in the Civil Registration System based on the
                information provided in this form.
            </s:if>
            <s:else>
                දැනුම් දෙන්නා (දෙමවිපියන් / භාරකරු) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරි වෙත භාර දිය
                යුතුය. මෙම
                තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ මළ උපත ලියාපදිංචි කරනු ලැබේ.
                <br>* In Tamil
                <br>Should be perfected by the informant (Parent / Guardian) and the duly completed form should be
                forwarded
                to the Notifying Authority. The still birth will be registered in the Civil Registration System based on the
                information provided in this form.
            </s:else>
        </td>
    </tr>
    </tbody>
</table>


<table class="table_reg_page_01" cellspacing="0" cellpadding="0">

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
        <td class="font-9" colspan="8" style="text-align:center;">
            <s:if test="liveBirth">
                ළම‌යාගේ විස්තර
                <br>பிள்ளை பற்றிய தகவல்
                <br>Child's Information
            </s:if>
            <s:else>
                මළ උපත පිලිබඳ විස්තර
                <br>* In Tamil
                <br>Still-birth Information
            </s:else>
        </td>
    </tr>
    <tr style="border-left:1px solid #000000;">
        <td width="150px"><label>(1)උපන් දිනය<br> பிறந்த திகதி <br>Date of Birth</label></td>
        <td colspan="7">
            <s:label value="%{#session.birthRegister.child.dateOfBirth}"/>
        </td>
    </tr>
    <tr>
        <td rowspan="5"><label>(2) උපන් ස්ථානය<br>பிறந்த இடம்<br> Place of Birth</label></td>
        <td><label>දිස්ත්‍රික්කය மாவட்டம் District</label></td>
        <td colspan="6" class="table_reg_cell_01">
            <s:if test="#userPreferedLang == 'si'">
                <s:label
                        value="%{#session.birthRegister.register.birthDivision.dsDivision.district.siDistrictName}"/></s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label
                        value="%{#session.birthRegister.register.birthDivision.dsDivision.district.enDistrictName}"/>
            </s:elseif>
            <s:else>
                <s:label
                        value="%{#session.birthRegister.register.birthDivision.dsDivision.district.taDistrictName}"/>
            </s:else></td>
    </tr>
    <tr>
        <td><label>D.S.කොට්ඨාශය பிரிவு D.S. Division</label></td>
        <td colspan="6" class="table_reg_cell_01" id="table_reg_cell_01"><s:if
                test="#userPreferedLang == 'si'">
            <s:label
                    value="%{#session.birthRegister.register.birthDivision.dsDivision.siDivisionName}"
                    cssStyle="float:left;  width:240px;"/></s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label
                        value="%{#session.birthRegister.register.birthDivision.dsDivision.enDivisionName}"
                        cssStyle="float:left;  width:240px;"/>
            </s:elseif>
            <s:else>
                <s:label
                        value="%{#session.birthRegister.register.birthDivision.dsDivision.taDivisionName}"
                        cssStyle="float:left;  width:240px;"/>
            </s:else>
        </td>
    <tr>
        <td><label>ස්ථානය பிறந்த இடம் Place</label></td>
        <td colspan="6"><s:label value="%{#session.birthRegister.child.placeOfBirth}" id="placeOfBirth"
                                 cssStyle="width:97.6%;"/></td>
    </tr>
    <tr>
        <td><label>*in sinhala/ *in tamil/ Place in English</label></td>
        <td colspan="6"><s:label value="%{#session.birthRegister.child.placeOfBirthEnglish}"
                                 id="placeOfBirthEnglish" cssStyle="width:97.6%;"/></td>
    </tr>
    <tr>
        <td colspan="3"><label> *in Sinhala/*in Tamil/In a Hospital</label></td>
        <td colspan="1"><label>ඔව් / *in Tamil / Yes </label></td>
        <td>
            <s:if test="session.birthRegister.child.birthAtHospital ==true">
            <s:label value="%{getText('yes.label')}"/></td>
        </s:if>
        <s:else>
            <s:label value="%{getText('no.label')}"/>
        </s:else>
    </tr>
    <s:if test="liveBirth">
        <tr>
            <td class="font-9"><label>(3) නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)<br>பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம்
                / தமிழ்) <br>Name in
                any of the official languages (Sinhala / Tamil)</label></td>
            <td colspan="7"><s:label value="%{#session.birthRegister.child.childFullNameOfficialLang}"
                                     cssStyle="width:98.2%;"/></td>
        </tr>

        <tr>
            <td class="font-9"><label>(4) නම ඉංග්‍රීසි භාෂාවෙන් <br>பிறப்பு அத்தாட்சி ….. <br>Name in English
            </label></td>
            <td colspan="7">
                <s:label value="%{#session.birthRegister.child.childFullNameEnglish}" cssStyle="width:98.2%;"/></td>
        </tr>
    </s:if>
    <tr>
        <td class="font-9" colspan="2"><label>(5) උප්පැන්න සහතිකය නිකුත් කල යුතු භාෂාව <br>பிறப்பு அத்தாட்சி …..
            <br>Preferred
            Language for
            Birth Certificate </label></td>
        <s:set name="lang" value="%{#session.birthRegister.register.preferredLanguage}"/>
        <td colspan="6"><s:label value="%{getText(#lang)}" cssStyle="float:left;  width:240px;"/></td>
    </tr>
    <tr>
        <td class="font-9"><label>(6)ස්ත්‍රී පුරුෂ භාවය<br> பால் <br>Gender of the child</label></td>
        <td colspan="3">
            <s:if test="session.birthRegister.child.childGender == 0">
                <s:label name="" value="%{getText('male.label')}"/>
            </s:if>
            <s:elseif test="session.birthRegister.child.childGender == 1">
                <s:label name="" value="%{getText('female.label')}"/>
            </s:elseif>
            <s:elseif test="session.birthRegister.child.childGender == 2">
                <s:label name="" value="%{getText('unknown.label')}"/>
            </s:elseif></td>
        <s:if test="liveBirth">
            <td colspan="2"><label>(7) උපත් බර<br>பிறப்பு நிறை<br>Birth Weight (kg)</label></td>
            <td colspan="2"><s:label value="%{#session.birthRegister.child.childBirthWeight}"
                                     cssStyle="width:95%;"/></td>
        </s:if>
        <s:else>
            <td colspan="2"><label>(4) දරැවා මැරී උපදින විට ගර්භයට සති කීයක් වී තිබුනේද යන්න
                <br>* In Tamil
                <br>Number of weeks pregnant at the time of still-birth</label></td>
            <td colspan="2"><s:label value="%{#session.birthRegister.child.weeksPregnant}" cssStyle="width:95%;"/></td>
        </s:else>
    </tr>
    <tr>
        <td class="font-9"><label>(8)සජිවි උපත් අනුපිළි‍‍වල අනුව කීවෙනි ළමයා ද? <br>பிறப்பு ஒழுங்கு <br>According
            to Live Birth Order,
            rank of the child?</label></td>
        <td colspan="3" class="font-9"><s:label value="%{#session.birthRegister.child.childRank}"/></td>
        <td colspan="2" class="font-9"><label>(9)නිවුන් දරු උපතක් නම්, දරුවන් ගනන<br>பல்வகைத்தன்மை (இரட்டையர்கள்
            எனின்),<br> பிள்னளகளின் எண்ணிக்கை<br>If
            multiple births, number of children</label></td>
        <td colspan="2"><s:label value="%{#session.birthRegister.child.numberOfChildrenBorn}"
                                 cssStyle="width:95%;"/></td>
    </tr>

    </tbody>
</table>

    <s:hidden name="pageNo" value="1"/>

<div class="form-submit">
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>

