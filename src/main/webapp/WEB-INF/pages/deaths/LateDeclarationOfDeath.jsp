<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="late-death-registrations-outer">
<table style="border:none;width:1030px;">
    <tr>

        <td style="width:55%;text-align:right;"><img src="<s:url value="/images/official-logo.png"/>"
                                                     alt=""/></td>
        <td style="width:45%">
            <table style="width:350px;border:1px solid #000;margin-bottom:5px;" cellspacing="0" cellpadding="0"
                   align="right">
                <tr>
                    <td style="border-right:1px solid #000;width:50%">අනුක්‍රමික අංකය <br/>
                        தொடர் இலக்கம் <br/>
                        Serial Number
                    </td>
                    <td style="width:50%;"><s:textfield name="death.deathSerialNo"
                                                        cssStyle="width:87%;float:right;margin-right:10px"/></td>
                </tr>
            </table>
            <table style="width:350px;border:1px solid #000;" cellspacing="0" cellpadding="0" align="right">
                <tr>
                    <td colspan="2" style="width:50%;border-bottom:1px solid #000;text-align:center;">කාර්යාල
                        ප්‍රයෝජනය සඳහා පමණි / <br/>
                        அலுவலக பாவனைக்காக மட்டும் / <br/>
                        For office use only
                    </td>

                </tr>
                <tr>
                    <td style="border-right:1px solid #000;width:50%;">ලියාපදිංචි කල දිනය <br/>
                        பிறப்பைப் பதிவு திகதி <br/>
                        Date of Registration
                    </td>
                    <td style="width:50%;text-align:right;"><sx:datetimepicker id="dateOfRegistration"
                                                                               name="death.dateOfRegistration"
                                                                               displayFormat="yyyy-MM-dd"
                                                                               onchange="javascript:splitDate('receivedDatePicker')"/></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<table class="death-reg-header-table">
    <tr>
        <td>Declaration of particulars relating to death for purposes of late registrations (Section 36)</td>
    </tr>
</table>
<table class="death-reg-header-table-02">
    <tr>
        <td>ලියාපදිංචි නොකරන ලද මරණයක් සම්බන්ධයෙන් මෙහි පහත ප්‍රකාශ කරනු ලබන විස්තර මගේ දැනීමේ හා විශ්වාසයේ ප්‍
            රකාර සැබෑ බව හා නිවැරදි බවද, මරණය සිදුවී, නැතහොත් ගෘහයක් හෝ ගොඩනැගිල්ලක් නොවන ස්ථානයක තිබී මෘතශරීරය
            සම්බවී,
            මාස තුඅනක් ඇතුලත දී මරණය ලියාපදිංචි කිරීමට නොහැකි වුයේ මෙහි පහත සඳහන් කාරණය හේතු කොටගෙන බවද, .....
            පදිංචි
            .... වන මම ගාම්භීරතා පුර්වකාවද,
            අවංක ලෙසද, සැබෑ ලෙසද, මෙයින් ප්‍රකාශ කරමි.
        </td>
    </tr>
    <tr>
        <td>in tamil line 1
            in tamil line 2
            in tamil line 3
        </td>
    </tr>
    <tr>
        <td>I …. of …. solemnly, sincerely, and truly declare that the particulars stated below relating to an
            unregistered death, are true and correct to the best of my knowledge and belief, and that the death has
            not been registered within three months from its occurrence or from the finding of the corpse in a place
            other than a house or a building, for this reason.
        </td>
    </tr>
</table>
<table class="death-reg-header-table">
    <tr>
        <td>මරණය පිලිබඳ විස්තර <br/>
            பிள்ளை பற்றிய தகவல் <br/>
            Information about the Death
        </td>
    </tr>
</table>


<table class="death-late-reg-table" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col style="width:20%"/>
    <col style="width:10%"/>
    <col style="width:10%"/>
    <col style="width:10%"/>
    <col style="width:10%"/>
    <col style="width:10%"/>
    <col style="width:10%"/>
    <col style="width:10%"/>
    <col style="width:10%"/>
    <tbody>
    <tr>
        <td>මරණය සිදු වූ දිනය <br/>
            பிறந்த திகதி <br/>
            Date of Death
        </td>

        <td colspan="4" style="text-align:right;">
            <sx:datetimepicker id="dateOfDeath" name="death.dateOfDeath"
                               displayFormat="yyyy-MM-dd"
                               onchange="javascript:splitDate('issueDatePicker')"/>
        </td>

        <td>වෙලාව
            in tamil
            Time
        </td>
        <td colspan="3"><s:textfield name="death.timeOfDeath"/></td>
    </tr>
    <tr>
        <td rowspan="5">මරණය සිදු වූ ස්ථානය <br/>
            பிறந்த இடம் <br/>
            Place of Death
        </td>
        <td colspan="3">දිස්ත්‍රික්කය/<br/> மாவட்டம் /<br/> District</td>
        <td colspan="5"><s:select id="deagthDistrictId" name="deathDistrictId" list="districtList"
                                  cssStyle="width:240px;"/></td>
    </tr>
    <tr>
        <td colspan="3">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br/>
            பிரிவு / <br/>
            Divisional Secretariat
        </td>
        <td colspan="5"><s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  cssStyle="float:left;  width:240px;"/></td>
    </tr>
    <tr>
        <td colspan="3">ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br/>
            பிரிவு / <br/>
            Registration Division
        </td>
        <td colspan="5"><s:select id="birthDivisionId" name="birthDivisionId" value="%{deathDivisionId}"
                                  list="bdDivisionList"
                                  cssStyle=" width:240px;float:left;"/></td>
    </tr>
    <tr>
        <td rowspan="2">ස්ථානය <br/>
            பிறந்த <br/>
            Place
        </td>
        <td colspan="2">සිංහල හෝ දෙමළ භාෂාවෙන් <br/>
            சிங்களம் தமிழ் <br/>
            In Sinhala or Tamil
        </td>
        <td colspan="5"><s:textfield name="death.placeOfDeathInOfficialLang" cssStyle="width:95%"/></td>
    </tr>
    <tr>
        <td colspan="2">ඉංග්‍රීසි භාෂාවෙන් <br/>
            in tamil <br/>
            In English
        </td>
        <td colspan="5"><s:textfield name="death.placeOfDeathInEnglish" cssStyle="width:95%"/></td>
    </tr>
    </tbody>
</table>
<table class="death-late-reg-table" cellspacing="0" cellpadding="0" style="border-top:none; margin-bottom:0px;">
    <caption></caption>
    <col style="width:20%;"/>
    <col style="width:16%;"/>
    <col style="width:16%;"/>
    <col style="width:8%;"/>
    <col style="width:8%;"/>
    <col style="width:8%;"/>
    <col style="width:8%;"/>
    <col style="width:16%;"/>
    <tbody>
    <tr>
        <td rowspan="2">මරණයට හේතුව තහවුරුද? <br/>
            in tamil <br/>
            Is the cause of death established?
        </td>
        <td>නැත / xx / No</td>
        <td><s:radio name="death.causeOfDeathEstablished" list="#@java.util.HashMap@{'false':''}"
                     id="causeOfDeathEstablished"
                     onclick="disable(false)"/>
        </td>
        <td colspan="2" rowspan="2">මරණය දින 30 කට අඩු ළදරුවෙකුගේද? <br/>
            in tamil <br/>
            Is the death of an infant less than 30 days?
        </td>
        <td colspan="2">නැත / xx / No</td>
        <td><s:radio name="death.infantLessThan30Days" list="#@java.util.HashMap@{'false':''}" value="false"
                     id="infantLessThan30Days"
                     onclick="disable(false)"/>
        </td>
    </tr>
    <tr>
        <td>ඔව් / xx /Yes</td>
        <td><s:radio name="death.causeOfDeathEstablished" list="#@java.util.HashMap@{'false':''}" value="false"
                     id="causeOfDeathEstablished"
                     onclick="disable(false)"/>
        </td>
        <td colspan="2">ඔව් / xx /Yes</td>
        <td><s:radio name="death.infantLessThan30Days" list="#@java.util.HashMap@{'false':''}"
                     id="infantLessThan30Days"
                     onclick="disable(false)"/>
        </td>
    </tr>
    <tr>
        <td>මරණයට හේතුව <br/>
            in tamil <br/>
            Cause of death
        </td>
        <td colspan="3"><s:textfield name="death.causeOfDeath" cssStyle=";width:94%"/></td>
        <td colspan="2">හේතුවේ ICD කේත අංකය <br/>
            in tamil <br/>
            ICD Code of cause
        </td>
        <td colspan="2"><s:textfield name="death.icdCodeOfCause" cssStyle="width:90%"/></td>
    </tr>
    <tr>
        <td>ආදාහන හෝ භූමදාන ස්ථානය<br/>
            in tamil <br/>
            Place of burial or cremation
        </td>
        <td colspan="7"><s:textfield name="death.placeOfBurial" cssStyle="width:97%"/></td>
    </tr>
    </tbody>
</table>

<table class="death-reg-header-table">
    <tr>
        <td>මරණයට පත් වූ පුද්ගලයාගේ විස්තර<br/>
            பிள்ளை பற்றிய தகவல் <br/>
            Information about the person Departed
        </td>
    </tr>
</table>


<table class="death-late-reg-table" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col style="width:20%"/>
    <col style="width:10%"/>
    <col style="width:10%"/>
    <col style="width:10%"/>
    <col style="width:15%"/>
    <col style="width:15%"/>
    <col style="width:20%"/>
    <tbody>
    <tr>
        <td rowspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br/>
            தனிநபர் அடையாள எண் / அடையாள அட்டை இல. <br/>
            PIN / NIC
        </td>
        <td colspan="3" rowspan="2"><s:textfield name="deathPerson.deathPersonPINorNIC" cssStyle="width:92%"/></td>
        <td rowspan="2">විදේශිකය‍කු නම් <br/>
            வெளிநாட்டவர் <br/>
            If a foreigner
        </td>
        <td> රට <br/>
            நாடு <br/>
            Country
        </td>
        <td>
            <s:select id="deathPersonCountryId" name="deathPerson.deathPersonCountryId" list="countryList" headerKey="0"
                      headerValue="%{getText('select_country.label')}"/>
        </td>
    </tr>
    <tr>
        <td>ගමන් බලපත්‍ර අංකය <br/>
            கடவுச் சீட்டு <br/>
            Passport No.
        </td>
        <td><s:textfield name="deathPerson.deathPersonPassportNo" cssStyle="width:90%"/></td>
    </tr>
    <tr>
        <td>වයස හෝ අනුමාන වයස <br/>
            பிறப்ப <br/>
            Age or probable Age
        </td>
        <td><s:textfield name="deathPerson.deathPersonAge"/></td>
        <td>ස්ත්‍රී පුරුෂ භාවය <br/>
            பால் <br/>
            Gender
        </td>
        <td><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="death.deathPersonGender" headerKey="0" headerValue="%{getText('select_gender.label')}"
                cssStyle="width:190px; margin-left:5px;"/>
        </td>
        <td>ජාතිය <br/>
            பிறப் <br/>
            Race
        </td>
        <td colspan="2"><s:textfield name="deathPerson.deathPersonRace"/></td>
    </tr>
    <tr>
        <td>නම රාජ්‍ය භාෂාවෙන් <br/>
            (සිංහල / දෙමළ) <br/>
            பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்)
            Name in either of the official languages
        </td>
        <td colspan="6"><s:textarea name="deathPerson.deathPersonNameOfficialLang"/></td>
    </tr>
    </tbody>
</table>

<table class="death-late-reg-table" cellspacing="0" cellpadding="0" style="border-top:none">
    <caption></caption>
    <col style="width:20%"/>
    <col style="width:80%"/>
    <tbody>
    <tr>
        <td>නම ඉංග්‍රීසි භාෂාවෙන් <br/>
            பிறப்பு அத்தாட்சி ….. <br/>
            Name in English
        </td>
        <td><s:textarea name="deathPerson.deathPersonNameInEnglish"/></td>
    </tr>
    <tr>
        <td>ස්ථිර ලිපිනය <br/>
            தாயின் நிரந்தர வதிவிட முகவரி <br/>
            Permanent Address
        </td>
        <td><s:textarea name="deathPerson.deathPersonPermanentAddress"/></td>
    </tr>
    <tr>
        <td>පියාගේ පු.අ.අ. / ජා.හැ.අ. <br/>
            Fathers PIN / NIC <br/>
        </td>
        <td><s:textfield name="deathPerson.deathPersonFatherPINorNIC" cssStyle="width:40%"/></td>
    </tr>
    <tr>
        <td>පියාගේ සම්පුර්ණ නම <br/>
            in tamil <br/>
            Fathers full name
        </td>
        <td><s:textarea name="deathPerson.deathPersonFatherFullName"/></td>
    </tr>
    <tr>
        <td>මවගේ පු.අ.අ. / ජා.හැ.අ. <br/>
            Mothers PIN / NIC
        </td>
        <td><s:textfield name="deathPerson.deathPersonMotherPINorNIC" cssStyle="width:40%"/></td>
    </tr>
    <tr>
        <td>මවගේ සම්පුර්ණ නම <br/>
            in tamil <br/>
            Mothers full name
        </td>
        <td><s:textarea name="deathPerson.deathPersonMotherFullName"/></td>
    </tr>
    </tbody>
</table>

<table class="death-reg-header-table">
    <tr>
        <td>ප්‍රකාශකයාගේ විස්තර <br/>
            அறிவிப்பு கொடுப்பவரின் தகவல்கள் <br/>
            Details of the Declarant
        </td>
    </tr>
</table>

<table class="death-late-reg-table" cellspacing="0" cellpadding="0" style="margin-top:10px;">
    <caption></caption>
    <col style="width:20%"/>
    <col style="width:14%"/>
    <col style="width:5%"/>
    <col style="width:7%"/>
    <col style="width:13%"/>
    <col style="width:14%"/>
    <col style="width:13%"/>
    <col style="width:14%"/>
    <tbody>
    <tr>
        <td colspan="3">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br/>
            தகவநபர் அடையாள எண் / அடையாள அட்டை இல. <br/>
            PIN / NIC
        </td>
        <td colspan="5"><s:textfield name="declarant.declarantPINorNIC" cssStyle="width:40%"/></td>
    </tr>
    <tr>
        <td>නම <br/>
            கொடுப்பவரின் பெயர் <br/>
            Name

        </td>
        <td colspan="7"><s:textarea name="declarant.declarantName"/></td>
    </tr>
    <tr>
        <td>තැපැල් ලිපිනය <br/>
            தபால் முகவரி <br/>
            Postal Address
        </td>
        <td colspan="7"><s:textarea name="declarant.declarantAddress"/></td>
    </tr>
    <tr>
        <td>ඇමතුම් විස්තර <br/>
            இலக்க வகை <br/>
            Contact Details
        </td>
        <td>දුරකතනය <br/>
            தொலைபேசி இலக்கம் <br/>
            Telephone
        </td>
        <td colspan="3"><s:textfield name="declarant.declarantPhoneNo" cssStyle="width:90%"/></td>
        <td>ඉ -තැපැල <br/>
            மின்னஞ்சல் <br/>
            Email
        </td>
        <td colspan="2"><s:textfield name="declarant.declarantEmail" cssStyle="width:90%"/></td>
    </tr>
    <tr>
        <td rowspan="2">දැනුම් දෙන්නේ කවරකු වශයෙන්ද <br/>
            in tamil <br/>
            Capacity for giving information
        </td>
        <td>මව
            in tamil <br/>
            Mother
        </td>
        <td colspan="2">
            <s:radio name="declarant.declarantType" list="#@java.util.HashMap@{'false':''}"
                     id="declarantType"
                     onclick="disable(false)"/></td>
        <td>පියා <br/>
            in tamil <br/>
            Father
        </td>
        <td><s:radio name="declarant.declarantType" list="#@java.util.HashMap@{'false':''}"
                     id="declarantType"
                     onclick="disable(false)"/></td>
        <td>සහෝදරයා සහෝදරිය <br/>
            in tamil <br/>
            Brother / Sister
        </td>
        <td><s:radio name="declarant.declarantType" list="#@java.util.HashMap@{'false':''}"
                     id="declarantType"
                     onclick="disable(false)"/></td>
    </tr>
    <tr>
        <td>පුත්‍රයා / දියණිය <br/>
            in tamil <br/>
            Son / Daughter
        </td>
        <td colspan="2"><s:radio name="declarant.declarantType" list="#@java.util.HashMap@{'false':''}"
                                 id="declarantType"
                                 onclick="disable(false)"/></td>
        <td>නෑයන <br/>් பாதுகாவலர் <br/>
            Relative
        </td>
        <td><s:radio name="declarant.declarantType" list="#@java.util.HashMap@{'false':''}"
                     id="declarantType"
                     onclick="disable(false)"/></td>
        <td>වෙනත් <br/>
            in tamil <br/>
            Other
        </td>
        <td><s:radio name="declarant.declarantType" list="#@java.util.HashMap@{'false':''}" value="false"
                     id="declarantType"
                     onclick="disable(false)"/></td>
    </tr>
    </tbody>
</table>

</div>