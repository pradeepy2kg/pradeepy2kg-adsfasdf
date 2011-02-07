<%@ page import="lk.rgd.common.util.PersonStatusUtil" %>
<%@ page import="lk.rgd.prs.api.domain.Person" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<script type="text/javascript">
    function initPage() {
    }
</script>

<div id="birth-certificate-outer">

<s:url id="print" action="eprPRSCertificate.do">
    <s:param name="personUKey" value="%{person.personUKey}"/>
</s:url>
<s:url id="advanceSearch" action="eprPRSAdvancedSearch.do"/>

<div class="form-submit" style="margin-right:5px;margin-top:5px;">
    <s:a href="%{print}"><s:label value="%{getText('prs_certificate.label')}"/></s:a>
</div>
<div class="form-submit" style="margin:5px 0 0 5px;margin-right:5px;">
    <s:a href="%{printPage}" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>
</div>
<div class="form-submit" style="margin-top:5px;">
    <s:a href="%{advanceSearch}"><s:label value="%{getText('search_record.label')}"/></s:a>
</div>

<table style="width:100%; border:none; border-collapse:collapse;">
    <tbody>
    <tr>
        <td rowspan="2" align="center">
            <img src="<s:url value="../images/official-logo.png" />"
                 style="display: block; text-align: center;" width="100" height="120">
        </td>
    </tr>
    <tr></tr>
    <tr>
        <td align="center" style="font-size:12pt;">
            <s:label>
                ජනගහන ලේඛනයේ ලියාපදිංචි පුද්ගලයකු පිලිබඳ විස්තර
                <br>குடிமதிப்பீட்டு ஆவணத்தில் ஆட்களை பதிவு செய்தல்
                <br>Information about a Person registered in the Population Registry</s:label>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_05" cellspacing="0" cellpadding="0"
       style="margin-bottom:20px;margin-top:20px;font-size:10pt;">
    <col width="200px">
    <col width="200px">
    <col width="120px">
    <col width="180px">
    <col width="120px">
    <col width="210px">
    <tbody>
    <tr>
        <td rowspan="2">
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification Number
            <br>(PIN)
        </td>
        <td rowspan="2">
            <s:label value="%{person.pin}"/>
        </td>
        <td rowspan="2">
            අතීත
            <br>தேசிய
            <br>Previous
        </td>
        <td colspan="2">
            ජාතික හැඳුනුම්පත් අංකය
            <br>தேசிய அடையாள அட்டை
            <br>National Identity Card (NIC) Number
        </td>
        <td>
            <s:label value="%{person.nic}"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            තාවකාලික අනන්‍යතා අංකය
            <br>இலக்கம் அல்லது தற்காலிக அடையாள
            <br>Temporary Identification number
        </td>
        <td>
            <s:label value="%{person.temporaryPin}"/>
        </td>
    </tr>
    <tr>
        <td>
            උපන් දිනය
            <br>பிறந்த திகதி
            <br>Date of birth
        </td>
        <td>
            <s:if test="person.dateOfBirth != null">
                <s:label value="%{person.dateOfBirth}"/><br>
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </td>
        <td>
            උපන් ස්ථානය
            <br>பிறந்த இடம்
            <br>Place of Birth
        </td>
        <td colspan="3">
            <s:label value="%{person.placeOfBirth}"/>
        </td>
    </tr>
    <tr>
        <td>
            ජීවමාන තත්ත්වය
            <br>பிறந்த திகதி
            <br>Life status
        </td>
        <td>
            <s:label value="%{lifeStatus}"/>
        </td>
        <td>
            මරණ දිනය
            <br>பிறந்த திகதி
            <br>Date of Death
        </td>
        <td colspan="3">
            <s:if test="person.dateOfDeath != null">
                <s:label value="%{person.dateOfDeath}"/><br>
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </td>
    </tr>
    <tr>
        <td>
            ජාතිය
            <br>இனம்
            <br>Race
        </td>
        <td>
            <s:label value="%{race}"/>
        </td>
        <td>
            ස්ත්‍රී පුරුෂ භාවය
            <br>பால்
            <br>Gender
        </td>
        <td>
            <s:label name="" value="%{gender}"/>
        </td>
        <td>
            සිවිල් තත්ත්වය
            <br>சிவில் நிலைமை
            <br>Civil Status
        </td>
        <td>
            <s:label value="%{civilStatus}"/>
        </td>
    </tr>
    <tr>
        <td>
            නම රාජ්‍ය භාෂාවෙන්
            <br>பெயர் அரச கரும மொழியில்
            <br>Name in official language
        </td>
        <td colspan="5">
            <s:label value="%{person.fullNameInOfficialLanguage}" cssStyle="font-size:12pt;"/>
        </td>
    </tr>
    <tr>
        <td>
            නම ඉංග්‍රීසි භාෂාවෙන්
            <br>பெயர் ஆங்கில மொழியில்
            <br>Name in English
        </td>
        <td colspan="5">
            <s:label value="%{person.fullNameInEnglishLanguage}" cssStyle="font-size:10pt;"/>
        </td>
    </tr>
    </tbody>
</table>

<s:if test="person.countries.size() != 0">
    <table style="width:100%; border:none; border-collapse:collapse;">
        <tr>
            <td align="center" style="font-size:12pt;">
                පුරවැසිභාවය සහ ගමන් බලපත්‍ර
                <br>வேறு நாடுகளில் பிரஜாவுரிமை
                <br>Citizenship and Passport Details
            </td>
        </tr>
    </table>

    <table class="table_reg_page_05" cellspacing="0" cellpadding="0"
           style="margin-bottom:20px;margin-top:10px;font-size:10pt;">
        <col width="200px">
        <col width="200px">
        <col width="300px">
        <col width="330px">
        <tbody>
        <s:iterator value="person.countries">
            <tr>
                <td height="60px">
                    රට / நாடு /Country
                </td>
                <td>
                    <s:if test="#session.user_bean.prefLanguage == 'si'">
                        <s:property value="country.siCountryName"/>
                    </s:if>
                    <s:if test="#session.user_bean.prefLanguage=='ta'">
                        <s:property value="country.taCountryName"/>
                    </s:if>
                    <s:if test="#session.user_bean.prefLanguage=='en'">
                        <s:property value="country.enCountryName"/>
                    </s:if>
                </td>
                <td>
                    ගමන් බලපත්‍ර අංකය / கடவுச் சீட்டு இல. /Passport No.
                </td>
                <td>
                    <s:property value="%{passportNo}"/>
                </td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
    <br/>
</s:if>
<s:else>
    <table style="width:100%; border:none; border-collapse:collapse;">
        <tr>
            <td align="center">
                වෙනත් රටවල පුරවැසි භාවයන් පිළිබදව විස්තර නොමැත.
                <br>வேறு நாட்டினது பிரஜாவுரிமை சம்பந்தமான விபரங்கள் இல்லை
                <br>Citizenships and Passport Details not available.
                <hr/>
            </td>
        </tr>
    </table>
</s:else>

<s:if test="person.lastAddress != null">
    <table style="width:100%; border:none; border-collapse:collapse;">
        <tr>
            <td align="center" style="font-size:12pt;">
                පදිංචිය සහ සම්බන්ධ කිරීමේ තොරතුරු
                <br>வேறு நாடுகளில் பிரஜாவுரிமை
                <br>Residence and Contact details
            </td>
        </tr>
    </table>

    <table class="table_reg_page_05" cellspacing="0" cellpadding="0"
           style="margin-bottom:20px;margin-top:10px;font-size:10pt;">
        <col width="200px">
        <col width="300px">
        <col width="100px">
        <col width="100px">
        <col width="100px">
        <col width="100px">
        <col width="130px">
        <tbody>
        <tr>
            <td>
                වර්තමාන ලිපිනය
                <br>தற்போதைய வதிவிட
                <br>Current Address
            </td>
            <td colspan="2">
                <s:label value="%{person.lastAddress}"/>
            </td>
            <td>
                ආරම්භය
                <br> மின்னஞ்சல்
                <br>Start
            </td>
            <td colspan="3">
                <s:if test="person.lastAddress.startDate != null">
                    <s:label value="%{person.lastAddress.startDate}"/><br>
                    <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
                </s:if>
                <s:else>&nbsp;</s:else>
            </td>
        </tr>
        <tr>
            <td>
                දුරකථන අංක
                <br>தொலைபேசி இலக்கம்
                <br>Telephone Numbers
            </td>
            <td colspan="2">
                <s:label value="%{person.personPhoneNo}"/>
            </td>
            <td>
                ඉ – තැපැල්
                <br>மின்னஞ்சல்
                <br>Email
            </td>
            <td colspan="3">
                <s:label value="%{person.personEmail}"/>
            </td>
        </tr>
        <s:iterator value="person.addresses">
            <s:if test="addressUKey != person.lastAddress.addressUKey ">
                <tr>
                    <td>
                        පෙර පදිංචි ලිපිනය
                        <br>தற்போதைய வதிவிட
                        <br>Previous Address
                    </td>
                    <td>
                        <s:property value="%{Line1}"/>
                    </td>
                    <td>
                        ස්ථිර
                        <br>மின்னஞ்சல்
                        <br>Permanent
                    </td>
                    <td>
                        ආරම්භය
                        <br> மின்னஞ்சல்
                        <br>Start
                    </td>
                    <td>
                        <s:if test="startDate != null">
                            <s:property value="%{startDate}"/><br>
                            <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
                        </s:if>
                        <s:else>&nbsp;</s:else>
                    </td>
                    <td>
                        අවසානය
                        <br>மின்னஞ்சல்
                        <br>End
                    </td>
                    <td>
                        <s:if test="endDate != null">
                            <s:property value="%{endDate}"/><br>
                            <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
                        </s:if>
                        <s:else>&nbsp;</s:else>
                    </td>
                </tr>
            </s:if>
        </s:iterator>
        </tbody>
    </table>
    <br/>
</s:if>
<s:else>
    <table style="width:100%; border:none; border-collapse:collapse;">
        <tr>
            <td align="center">
                වර්තමාන හෝ පෙර පදිංචි ලිපිනයන් ගැන විස්තර නොමැත
                <br>வேறு நாட்டினது பிரஜாவுரிமை சம்பந்தமான விபரங்கள் இல்லை
                <br>Current or previous Address details not available.
                <hr/>
            </td>
        </tr>
    </table>
</s:else>

<table style="width:100%; border:none; border-collapse:collapse;">
    <tr>
        <td align="center" style="font-size:12pt;">
            දෙමාපියන්, සහෝදර සහෝදරියන් සහ දරුවන්
            <br>வேறு நாடுகளில் பிரஜாவுரிமை
            <br>Parents, Brothers & Sisters, and Children
        </td>
    </tr>
</table>

<table class="table_reg_page_05" cellspacing="0" cellpadding="0"
       style="margin-bottom:20px;margin-top:10px;font-size:10pt;">
    <col width="130px">
    <col width="150px">
    <col width="150px">
    <col>
    <tbody>
    <tr>
        <td>
            නෑදෑකම
            <br>தந்தையின்முழுப்
            <br>Relationship
        </td>
        <td>
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification Number
        </td>
        <td>
            උපන් දිනය
            <br>பிறந்த திகதி
            <br>Date of birth
        </td>
        <td>
            නම
            <br>பெயர்
            <br>Name
        </td>
    </tr>
    <tr>
        <td>
            පියා
            <br>தந்தை
            <br>Father
        </td>
        <td>
            <s:if test="person.father.pin != null">
                <s:label value="%{person.father.pin}"/>
            </s:if>
            <s:else>
                <s:label value="%{person.father.nic}"/>
            </s:else>
            </td>
        <td>
            <s:if test="person.father.dateOfBirth != null">
                <s:label value="%{person.father.dateOfBirth}"/><br>
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </td>
        <td>
            <s:url id="personDetails" action="eprPersonDetails">
                <s:param name="personUKey" value="%{person.father.personUKey}"/>
            </s:url>
            <s:a href="%{personDetails}">
                <s:label value="%{person.father.fullNameInOfficialLanguage}"/>
            </s:a>
        </td>
    </tr>
    <tr>
        <td>
            මව
            <br>தாயின்
            <br>Mother
        </td>
        <td>
            <s:if test="person.mother.pin != null">
                <s:label value="%{person.mother.pin}"/>
            </s:if>
            <s:else>
                <s:label value="%{person.mother.nic}"/>
            </s:else>
        </td>
        <td>
            <s:if test="person.mother.dateOfBirth != null">
                <s:label value="%{person.mother.dateOfBirth}"/><br>
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </td>
        <td>
            <s:url id="personDetails" action="eprPersonDetails">
                <s:param name="personUKey" value="%{person.mother.personUKey}"/>
            </s:url>
            <s:a href="%{personDetails}">
                <s:label value="%{person.mother.fullNameInOfficialLanguage}"/>
            </s:a>
        </td>
    </tr>

    <s:iterator value="siblings" var="sibling">
        <tr>
            <td>
                <s:if test="%{#sibling.gender == 0}">
                    සහෝදරයා
                    <br>தாயின்
                    <br>Brother
                </s:if>
                <s:elseif test="%{#sibling.gender == 1}">
                    සහෝදරිය
                    <br>தாயின்
                    <br>Sister
                </s:elseif>
                <s:else>
                    සහෝදරයා/සහෝදරිය
                    <br>தாயின்/தாயின்
                    <br>Brother/Sister
                </s:else>
            </td>
            <td>
                <s:if test="pin != null">
                    <s:property value="pin"/>
                </s:if>
                <s:else>
                    <s:property value="nic"/>
                </s:else>
            </td>
            <td>
                <s:if test="dateOfBirth != null">
                    <s:property value="dateOfBirth"/><br>
                    <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
                </s:if>
                <s:else>&nbsp;</s:else>
            </td>
            <td>
                <s:url id="personDetails" action="eprPersonDetails">
                    <s:param name="personUKey">
                        <s:property value="personUKey"/>
                    </s:param>
                </s:url>
                <s:a href="%{personDetails}">
                    <s:property value="fullNameInOfficialLanguage"/>
                </s:a>
            </td>
        </tr>
    </s:iterator>

    <s:iterator value="children" var="child">
        <tr>
            <td>
                <s:if test="%{#child.gender == 1}">
                    දියණිය
                    <br>தாயின்
                    <br>Daughter
                </s:if>
                <s:elseif test="%{#child.gender == 0}">
                    පුත්‍රයා
                    <br>தாயின்
                    <br>Son
                </s:elseif>
                <s:else>
                    පුත්‍රයා/දියණිය
                    <br>தாயின்/தாயின்
                    <br>Son/Daughter
                </s:else>
            </td>
            <td>
                <s:if test="pin != null">
                    <s:property value="pin"/>
                </s:if>
                <s:else>
                    <s:property value="nic"/>
                </s:else>
            </td>
            <td>
                <s:if test="dateOfBirth != null">
                    <s:property value="dateOfBirth"/><br>
                    <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
                </s:if>
                <s:else>&nbsp;</s:else>
            </td>
            <td>
                <s:url id="personDetails" action="eprPersonDetails">
                    <s:param name="personUKey">
                        <s:property value="personUKey"/>
                    </s:param>
                </s:url>
                <s:a href="%{personDetails}">
                    <s:property value="fullNameInOfficialLanguage"/>
                </s:a>
            </td>
        </tr>
    </s:iterator>
    </tbody>
</table>
<br/>

<s:if test="person.marriages.size() != 0">
    <table style="width:100%; border:none; border-collapse:collapse;">
        <tr>
            <td align="center" style="font-size:12pt;">
                විවාහයන්
                <br>வேறு நாடுகளில்
                <br>Marriages
            </td>
        </tr>
    </table>

    <table class="table_reg_page_05" cellspacing="0" cellpadding="0"
           style="margin-bottom:20px;margin-top:10px;font-size:10pt;">
        <col width="130px">
        <col width="100px">
        <col width="150px">
        <col width="150px">
        <col width="130px">
        <col>
        <tbody>
        <tr>
            <td>
                දිනය
                <br>தாயின்
                <br>Date
            </td>
            <td>
                නීතිය
                <br>தாயின்
                <br>Law
            </td>
            <td>
                ස්ථානය
                <br>தாயின்
                <br>Place
            </td>
            <td>
                අනන්‍යතා අංකය
                <br>அடையாள எண்
                <br>Identification Number
            </td>
            <td>
                උපන් දිනය
                <br>பிறந்த திகதி
                <br>Date of birth
            </td>
            <td>
                නම
                <br>பெயர்
                <br>Name
            </td>
        </tr>
        <tr>
            <s:iterator value="person.marriages">
                <td height="60px">
                    <s:if test="dateOfMarriage != null">
                        <s:property value="dateOfMarriage"/><br>
                        <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </td>
                <td>
                    <s:if test="preferredLanguage == 'si'">
                        <s:property value="typeOfMarriage.siType"/>
                    </s:if>
                    <s:elseif test="preferredLanguage == 'en'">
                        <s:property value="typeOfMarriage.enType"/>
                    </s:elseif>
                    <s:elseif test="preferredLanguage == 'ta'">
                        <s:property value="typeOfMarriage.taType"/>
                    </s:elseif>
                </td>
                <td><s:property value="placeOfMarriage"/></td>
                <s:if test="person.gender == 1">
                    <td>
                        <s:if test="groom.pin != null">
                            <s:property value="groom.pin"/>
                        </s:if>
                        <s:else>
                            <s:property value="groom.nic"/>
                        </s:else>
                    </td>
                    <td>
                        <s:if test="groom.dateOfBirth != null">
                            <s:property value="groom.dateOfBirth"/><br>
                            <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
                        </s:if>
                        <s:else>&nbsp;</s:else>
                    </td>
                    <td>
                        <s:url id="groomDetails" action="eprPersonDetails">
                            <s:param name="personUKey">
                                <s:property value="groom.personUKey"/>
                            </s:param>
                        </s:url>
                        <s:a href="%{groomDetails}">
                            <s:property value="groom.fullNameInOfficialLanguage"/>
                        </s:a>
                    </td>
                </s:if>
                <s:elseif test="person.gender == 0">
                    <td>
                        <s:if test="bride.pin != null">
                            <s:property value="bride.pin"/>
                        </s:if>
                        <s:else>
                            <s:property value="bride.nic"/>
                        </s:else>
                    </td>
                    <td>
                        <s:if test="bride.dateOfBirth != null">
                            <s:property value="bride.dateOfBirth"/><br>
                            <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
                        </s:if>
                        <s:else>&nbsp;</s:else>
                    </td>
                    <td>
                        <s:url id="brideDetails" action="eprPersonDetails">
                            <s:param name="personUKey">
                                <s:property value="bride.personUKey"/>
                            </s:param>
                        </s:url>
                        <s:a href="%{brideDetails}">
                            <s:property value="bride.fullNameInOfficialLanguage"/>
                        </s:a>
                    </td>
                </s:elseif>
            </s:iterator>
        </tr>
        </tbody>
    </table>
    <br/>
</s:if>
<s:else>
    <table style="width:100%; border:none; border-collapse:collapse;">
        <tr>
            <td align="center">
                විවාහයන් පිළිබදව තොරතුරු වාර්තා වී නොමැත.
                <br>திருமணம் சம்மந்தமான விபரங்கள் அறிக்கையிடப்படவில்லை
                <br>Marriages Details not available.
                <hr/>
            </td>
        </tr>
    </table>
</s:else>
<br>

<%--<div style="page-break-after:always;"></div>--%>
<table style="width:100%; border:none; border-collapse:collapse;">
    <tr>
        <td align="center" style="font-size:12pt;">
            වාර්තාව පිලිබඳ ඉතිහාසය
            <br>வேறு நாடுகளில்
            <br>Record History
        </td>
    </tr>
</table>

<table class="table_reg_page_05" cellspacing="0" cellpadding="0"
       style="margin-bottom:10px;margin-top:10px;font-size:10pt;">
    <col width="250px">
    <col width="250px">
    <col width="250px">
    <col>
    <tbody>
    <tr>
        <td>
            ඇතුල් කල දිනය
            <br>தேசிய
            <br>Date Added
        </td>
        <td>
            <s:if test="person.lifeCycleInfo.createdTimestamp != null">
                <s:label value="%{person.lifeCycleInfo.createdTimestamp}"/><br>
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </td>
        <td>
            ඇතුල් කල පුද්ගලයා
            <br>தேசிய
            <br>Added by
        </td>
        <td><s:label value="%{person.lifeCycleInfo.createdUser.userName}"/></td>
    </tr>
    <tr>
        <td>
            අවසන් වෙනස් කිරීම කල දිනය
            <br>தேசிய
            <br>Last Updated
        </td>
        <td>
            <s:if test="person.lifeCycleInfo.lastUpdatedTimestamp != null">
                <s:label value="%{person.lifeCycleInfo.lastUpdatedTimestamp}"/><br>
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </td>
        <td>
            අවසන් වෙනස් කිරීම කල පුද්ගලයා
            <br>தேசிய
            <br>Updated by
        </td>
        <td><s:label value="%{person.lifeCycleInfo.lastUpdatedUser.userName}"/></td>
    </tr>
    <tr>
        <td>
            අනුමත හෝ ප්‍රතික්ෂේප කල දිනය
            <br>தேசிய
            <br>Approve or Reject date
        </td>
        <td>
            <s:if test="person.lifeCycleInfo.approvalOrRejectTimestamp != null">
                <s:label value="%{person.lifeCycleInfo.approvalOrRejectTimestamp}"/><br>
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:2px;font-size:10px"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </td>
        <td>
            අනුමත හෝ ප්‍රතික්ෂේප කල පුද්ගලයා
            <br>தேசிய
            <br>Approved or Rejected by
        </td>
        <td>
            <s:if test="person.lifeCycleInfo.approvalOrRejectUser != null">
                <s:label value="%{person.lifeCycleInfo.approvalOrRejectUser.userName}"/>
            </s:if>
        </td>
    </tr>
    <tr>
        <td>
            වාර්තාවේ තත්ත්වය
            <br>தேசிய
            <br>Record status
        </td>
        <td colspan="3">
            <%= PersonStatusUtil.getPersonStatusString((Person.Status) request.getAttribute("person.status"))%>
        </td>
    </tr>
    <tr>
        <td>
            වාර්තාව පිළිබඳ අදහස්
            <br>Record Comments in Tamil
            <br>Record Comments
        </td>
        <td colspan="3">
            <s:label value="%{person.comments}"/>
        </td>
    </tr>
    </tbody>
</table>

<div class="form-submit" style="margin-right:5px;margin-top:5px;">
    <s:a href="%{print}"><s:label value="%{getText('prs_certificate.label')}"/></s:a>
</div>
<div class="form-submit" style="margin:5px 0 0 5px;margin-right:5px;">
    <s:a href="%{printPage}" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>
</div>
<div class="form-submit" style="margin-top:5px;">
    <s:a href="%{advanceSearch}"><s:label value="%{getText('search_record.label')}"/></s:a>
</div>
<br><br>
</div>
