<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="prs-person-report-outer">

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

<table border="1"
       style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
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
            <br>
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
            <br>තාවකාලික අනන්‍යතා අංකය
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
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
            <s:label value="%{person.dateOfBirth}"/>
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
            ජීවමාන තත්වය
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
            <s:label value="%{person.dateOfDeath}"/>
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
            සිවිල් තත්වය
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
            <s:label value="%{person.fullNameInOfficialLanguage}"/>
        </td>
    </tr>
    <tr>
        <td>
            නම ඉංග්‍රීසි භාෂාවෙන්
            <br>பெயர் ஆங்கில மொழியில்
            <br>Name in English
        </td>
        <td colspan="5">
            <s:label value="%{person.fullNameInEnglishLanguage}"/>
        </td>
    </tr>
    </tbody>
</table>
<s:if test="numberOfCountries != 0">
    <table style="width:100%; border:none; border-collapse:collapse;">
        <tr>
            <td align="center" style="font-size:12pt;">
                පුරවැසිභාවය සහ ගමන් බලපත්‍ර
                <br>வேறு நாடுகளில் பிரஜாவுரிமை
                <br>Citizenship and Passport Details
            </td>
        </tr>
    </table>
    <table border="1"
           style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
        <col width="200px">
        <col width="200px">
        <col width="300px">
        <col width="330px">
        <tbody>
        <tr>
            <td height="60px">
                රට / நாடு /Country
            </td>
            <td>
                <s:label value="%{}"/>
            </td>
            <td>
                ගමන් බලපත්‍ර අංකය / கடவுச் சீட்டு இல. /Passport No.
            </td>
            <td>
                <s:label value="%{person.passportNos}"/>
            </td>
        </tr>
        </tbody>
    </table>

</s:if>
<s:else>
    <table style="width:100%; border:none; border-collapse:collapse;">
        <tr>
            <td>
                වෙනත් රටවල පුරවැසි භාවයන් පිළිබදව විස්තර නොමැත.
                <br>* in tamil
                <br>Citizenships and Passport Details not available.
            </td>
        </tr>
    </table>
</s:else>
<table style="width:100%; border:none; border-collapse:collapse;">
    <tr>
        <td align="center" style="font-size:12pt;">
            පදිංචිය සහ සම්බන්ධ කිරීමේ තොරතුරු
            <br>வேறு நாடுகளில் பிரஜாவுரிமை
            <br>Residence and Contact details
        </td>
    </tr>
</table>
<table border="1"
       style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
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
            <s:label value="%{person.lastAddress.startDate}"/>
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
    <s:if test="address.size() != 0">
        <s:iterator value="address">
            <tr>
                <td>
                    පෙර පදිංචි ලිපිනය
                    <br>தற்போதைய வதிவிட
                    <br>Previous Address
                </td>
                <td>
                    <s:property value="%{}"/>
                </td>
                <td>
                    ස්ථිර
                    <br>மின்னஞ்சல்
                    <br>Permenent
                </td>
                <td>
                    ආරම්භය
                    <br> மின்னஞ்சல்
                    <br>Start
                </td>
                <td>
                    <s:property value="%{startDate}"/>

                </td>
                <td>
                    අවසානය
                    <br>மின்னஞ்சல்
                    <br>End
                </td>
                <td>
                    <s:property value="%{endDate}"/>
                </td>
            </tr>
        </s:iterator>
    </s:if>
    </tbody>
</table>
<table style="width:100%; border:none; border-collapse:collapse;">
    <tr>
        <td align="center" style="font-size:12pt;">
            දෙමාපියන්, සහෝදර සහෝදරියන් සහ දරුවන්
            <br>வேறு நாடுகளில் பிரஜாவுரிமை
            <br>Parents, Brothers & Sisters, and Children
        </td>
    </tr>
</table>
<table border="1"
       style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
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
            <br>தந்தையின்முழுப்
            <br>Father
        </td>
        <td><s:label value="%{person.father.pin}"/></td>
        <td><s:label value="%{person.father.dateOfBirth}"/></td>
        <td>
            <s:url id="personDetails" action="eprPersonDetails">
                <s:param name="personId" value="%{person.father.personUKey}"/>
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
        <td><s:label value="%{person.mother.pin}"/></td>
        <td><s:label value="%{person.mother.dateOfBirth}"/></td>
        <td>
            <s:url id="personDetails" action="eprPersonDetails">
                <s:param name="personId" value="%{person.mother.personUKey}"/>
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
            <td><s:property value="pin"/></td>
            <td><s:property value="dateOfBirth"/></td>
            <td>
                <s:url id="personDetails" action="eprPersonDetails">
                    <s:param name="personId">
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
            <td><s:property value="pin"/></td>
            <td><s:property value="dateOfBirth"/></td>
            <td>
                <s:url id="personDetails" action="eprPersonDetails">
                    <s:param name="personId">
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

<s:if test="marrage == null">
    <table style="width:100%; border:none; border-collapse:collapse;">
        <tr>
            <td align="center" style="font-size:12pt;">
                විවාහයන්
                <br>வேறு நாடுகளில்
                <br>Marriages
            </td>
        </tr>
    </table>
    <table border="1"
           style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
        <col width="130px">
        <col width="100px">
        <col width="150px">
        <col width="200px">
        <col width="150px">
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
        <s:iterator value="marrage">
        <tr>
            <td height="60px"><s:property value=""/></td>
            <td><s:property value="dateOfMarriage"/> </td>
            <td><s:property value="Law"/> </td>
            <td><s:property value="placeOfMarriage"/> </td>
            <td><s:property value="marriageUKey"/> </td>
            <td><s:property value=""/> </td>
            <td><s:property value=""/> </td>
        </tr>
        </s:iterator>
        </tbody>
    </table>
</s:if>
<s:else>
    <table style="width:100%; border:none; border-collapse:collapse;">
        <tr>
            <td>
                විවාහයන් පිළිබදව තොරතුරු වාර්තා වී නොමැත.
                <br>* in Tamil
                <br>Marriages Details not available.
            </td>
        </tr>
    </table>
</s:else>
<table style="width:100%; border:none; border-collapse:collapse;">
    <tr>
        <td align="center" style="font-size:12pt;">
            වාර්තාව පිලිබඳ ඉතිහාසය
            <br>வேறு நாடுகளில்
            <br>Record History
        </td>
    </tr>
</table>
<table border="1"
       style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
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
            <s:label value="%{person.dateOfRegistration}"/>
        </td>
        <td>
            ඇතුල් කල පුද්ගලයා
            <br>தேசிய
            <br>Added by
        </td>
        <td></td>
    </tr>
    <tr>
        <td>
            අවසන් වෙනස් කිරීම කල දිනය
            <br>தேசிய
            <br>Last Updated
        </td>
        <td></td>
        <td>
            අවසන් වෙනස් කිරීම කල පුද්ගලයා
            <br>தேசிய
            <br>Updated by
        </td>
        <td></td>
    </tr>
    <tr>
        <td>
            නිකුත් කල දිනය
            <br>தேசிய
            <br>Date Generated
        </td>
        <td></td>
        <td>
            නිකුත් කල පුද්ගලයා
            <br>தேசிய
            <br>Generated by
        </td>
        <td></td>
    </tr>
    <tr>
        <td>
            වාර්තාවේ තත්වය
            <br>தேசிய
            <br>Record status
        </td>
        <td>
            ස්ථිර
            <br>தேசிய
            <br>Verified
        </td>
        <td colspan="2"></td>
    </tr>
    </tbody>
</table>
</div>
