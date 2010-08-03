<%--
  @author indunil moremada
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-registration-form-outer" id="birth-registration-form-2-outer">
<s:form action="eprViewBDFInNonEditableMode.do" name="nonEditableBirthRegistrationForm2" method="POST">
<s:set value="%{#session.WW_TRANS_I18N_LOCALE.language}" name="userPreferedLang"/>

<table class="table_reg_page_02" cellspacing="0">
    <caption></caption>
    <col/>
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
        <td colspan="9" style="text-align:center;font-size:12pt">පියාගේ විස්තර
            <br>தந்தை பற்றிய தகவல்
            <br>Details of the Father
        </td>
    </tr>
    <tr>
        <td rowspan="2" width="200px"><label>(10)අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br>து தனிநபர் அடையாள எண் /தேசிய
            அடையாள அட்டை
            இலக்கம்<br>PIN / NIC Number</label></td>
        <td rowspan="2" width="230px" class="find-person"><s:label
                value="%{#session.birthRegister.parent.fatherNICorPIN}"/>

        </td>
        <td colspan="2" rowspan="2" width="120px"><label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
        </td>
        <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
        <td colspan="2"><s:if test="#userPreferedLang == 'si'">
            <s:label
                    value="%{#session.birthRegister.parent.fatherCountry.siCountryName}"
                    cssStyle="width:97%;"/></s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label
                        value="%{#session.birthRegister.parent.fatherCountry.enCountryName}"
                        cssStyle="width:97%;"/>
            </s:elseif>
            <s:else>
                <s:label
                        value="%{#session.birthRegister.parent.fatherCountry.taCountryName}"
                        cssStyle="width:97%;"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
        <td class="passport"><s:label value="%{#session.birthRegister.parent.fatherPassportNo}"/></td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_02" cellspacing="0" style="margin:-10px auto; border-top:none;">
    <tr>
        <td width="200px"><label>(11)සම්පුර්ණ නම<br>தந்தையின் முழு பெயர்<br>Full Name</label></td>
        <td colspan="8">
            <s:label value="%{#session.birthRegister.parent.fatherFullName}" cssStyle="width:98%;"/>
        </td>
    </tr>
    <tr>
        <td width="200px"><label>(12)උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth</label></td>
        <td colspan="2">
            <s:label value="%{#session.birthRegister.parent.fatherDOB}"/>
        </td>
        <td colspan="2"><label>(13)උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of Birth</label></td>
        <td colspan="2"><s:label value="%{#session.birthRegister.parent.fatherPlaceOfBirth}"
                                 cssStyle="width:95%;"/></td>
    </tr>
</table>

<table class="table_reg_page_02" cellspacing="0" style="border-top:none;">
    <tbody>
    <tr>
        <td width="200px"><label>(14)පියාගේ ජාතිය<br>இனம்<br> Father's Race</label></td>
        <td colspan="6" class="table_reg_cell_02"><s:if test="#userPreferedLang == 'si'">
            <s:label
                    value="%{#session.birthRegister.parent.fatherRace.siRaceName}" cssStyle="width:200px;"/></s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label
                        value="%{#session.birthRegister.parent.fatherRace.enRaceName}" cssStyle="width:200px;"/>
            </s:elseif>
            <s:else>
                <s:label
                        value="%{#session.birthRegister.parent.fatherRace.taRaceName}" cssStyle="width:200px;"/>
            </s:else>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_02" cellspacing="0" style="margin:0;">
    <caption></caption>
    <col/>
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
        <td colspan="9" style="text-align:center;font-size:12pt"> මවගේ විස්තර <br>தாய் பற்றிய தகவல் <br>Details of the
            Mother
        </td>
    </tr>
    <tr>
        <td rowspan="2" width="200px"><label>(15)අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>து தனிநபர் அடையாள எண் /தேசிய
            அடையாள அட்டை
            இலக்கம்<br>PIN / NIC Number</label></td>
        <td colspan="2" rowspan="2" width="230px" class="find-person"><s:label
                value="%{#session.birthRegister.parent.motherNICorPIN}"/>

        </td>
        <td colspan="2" rowspan="2" width="120px"><label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
        </td>
        <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
        <td colspan="2"><s:if test="#userPreferedLang == 'si'">
            <s:label
                    value="%{#session.birthRegister.parent.motherCountry.siCountryName}"/></s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label
                        value="%{#session.birthRegister.parent.motherCountry.enCountryName}"/>
            </s:elseif>
            <s:else>
                <s:label
                        value="%{#session.birthRegister.parent.motherCountry.taCountryName}"/>
            </s:else></td>
    </tr>
    <tr>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
        <td colspan="2" class="passport"><s:label value="%{#session.birthRegister.parent.motherPassportNo}"/></td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_02" cellspacing="0" style="margin:0; border-top:none; border-bottom:none;">
    <tbody>
    <tr>
        <td width="200px"><label>(16)සම්පුර්ණ නම<br>தந்தையின் முழு பெயர்<br>Full Name</label></td>
        <td colspan="8">
            <s:label value="%{#session.birthRegister.parent.motherFullName}" cssStyle="width:98%;"/>
        </td>
    </tr>
    <tr>
        <td width="200px"><label>(17)උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth</label></td>
        <td colspan="3"><s:label value="%{#session.birthRegister.parent.motherDOB}" cssStyle="width:50%;"/></td>
        <td colspan="3" width="100px"><label>
            <s:if test="session.birthRegister.register.birthType.ordinal() != 0">
                (18) ළමයාගේ උපන් දිනට මවගේ වයස<br> பிள்ளை பிறந்த திகதியில் மாதாவின் வயது<br>Mother's Age
                as at
                the date of birth of child
            </s:if>
            <s:else>
                (18) ළමයාගේ මළ උපන් දිනට මවගේ වයස<br> * Tamil<br>Mother's Age
                as at the date of still-birth of child
            </s:else>
        </label>
        </td>
        <td class="passport"><s:label value="%{#session.birthRegister.parent.motherAgeAtBirth}"/></td>
    </tr>
    <tr style="border-bottom:none;">
        <td style="border-bottom:none;"><label>(21)මවගේ ස්ථිර ලිපිනය<br>தாயின் நிரந்தர வதிவிட முகவரி<br>Permanent
            Address of the Mother</label>
        </td>
        <td colspan="8" style="border-bottom:none;"><s:label value="%{#session.birthRegister.parent.motherAddress}"
                                                             cssStyle="width:98%;"/></td>
    </tr>

    </tbody>
</table>

<table class="table_reg_page_02" cellspacing="0" style="margin:0; border-top:none;">
    <tbody>
    <tr>
        <td width="200px" style="border-top:none; border-bottom:none;"></td>
        <td colspan="2" class="table_reg_cell_02" style="border-top:1px solid #000;"><label>*in Sinhala/*in
            English/District</label></td>
        <td colspan="6" class="table_reg_cell_02" style="border-top:1px solid #000;">
            <s:if
                    test="#userPreferedLang == 'si'">
                <s:label value="%{#session.birthRegister.parent.motherDSDivision.district.siDistrictName}"
                         cssStyle="width:99%;"/>
            </s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label value="%{#session.birthRegister.parent.motherDSDivision.district.enDistrictName}"
                         cssStyle="width:99%;"/>

            </s:elseif>
            <s:else>
                <s:label value="%{#session.birthRegister.parent.motherDSDivision.district.taDistrictName}"
                         cssStyle="width:99%;"/>
            </s:else></td>
    </tr>
    <tr>
        <td width="200px" style="border-top:none;"></td>
        <td colspan="2"><label>*in Sinhala/*in English/D.S Division</label></td>
        <td colspan="6" class="table_reg_cell_02">
            <s:if
                    test="#userPreferedLang == 'si'">
                <s:label value="%{#session.birthRegister.parent.motherDSDivision.siDivisionName}"/>
            </s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label value="%{#session.birthRegister.parent.motherDSDivision.enDivisionName}"/>

            </s:elseif>
            <s:else>
                <s:label value="%{#session.birthRegister.parent.motherDSDivision.taDivisionName}"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td width="200px"><label>(19)ම‌වගේ ජාතිය<br>இனம்<br> Mother's Race</label></td>
        <td colspan="3">
            <s:if
                    test="#userPreferedLang == 'si'">
                <s:label value="%{#session.birthRegister.parent.motherRace.siRaceName}"/>
            </s:if>
            <s:elseif test="#userPreferedLang == 'en'">
                <s:label value="%{#session.birthRegister.parent.motherRace.enRaceName}"/>
            </s:elseif>
            <s:else>
                <s:label value="%{#session.birthRegister.parent.motherRace.taRaceName}"/>
            </s:else></td>

        <td colspan="3"><label>(20)උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of Birth</label></td>
        <td colspan="3" class="passport"><s:label value="%{#session.birthRegister.parent.motherPlaceOfBirth}"/></td>
    </tr>
    <tr>
        <td><label>(22)රෝහලට ඇතුලත් කිරිමේ අංකය<br>*in tamil<br>Hospital Admission Number</label></td>
        <td colspan="3" class="passport"><s:label value="%{#session.birthRegister.parent.motherAdmissionNo}"/></td>
        <td colspan="2"><label>(23)රෝහලට ඇතුලත් කිරිමේ දිනය<br>*in tamil<br>Hospital Admission Date</label></td>
        <td colspan="3"><s:label value="%{#session.birthRegister.parent.motherAdmissionDate}"/></td>
    </tr>
    <tr>
        <td><label>(24)ම‌ව සම්බන්ධ කල හැකි තොරතුරු <br>தாயின் தொடர்பு இலக்க தகவல் <br>Contact Details of the
            Mother</label></td>
        <td><label>දුරකතනය <br> தொலைபேசி இலக்கம் <br> Telephone</label></td>
        <td colspan="3"><s:label value="%{#session.birthRegister.parent.motherPhoneNo}"/></td>
        <td colspan="2"><label>ඉ – තැපැල් <br> மின்னஞ்சல்<br>Email</label></td>
        <td colspan="2" class="passport"><s:label value="%{#session.birthRegister.parent.motherEmail}"/></td>
    </tr>
    </tbody>
</table>

<s:hidden name="pageNo" value="2"/>

<div class="form-submit">
    <s:submit value="%{getText('next.label')}"/>
</div>
<div class="next-previous">
    <s:url id="backUrl" action="eprViewBDFInNonEditableMode">
        <s:param name="back" value="true"/>
        <s:param name="pageNo" value="{pageNo - 1}"/>
    </s:url>
    <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</s:form>
</div>