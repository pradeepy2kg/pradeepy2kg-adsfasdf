<%@ page import="lk.rgd.common.util.CommonUtil" %>
<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
<%@ page import="lk.rgd.common.util.GenderUtil" %>
<%@ page import="lk.rgd.crs.api.domain.DeathAlteration" %>
<%@ page import="lk.rgd.crs.api.domain.DeathRegister" %>
<%@ page import="java.util.BitSet" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css">
    #alteration-print-letter-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }
    }

    #alteration-print-letter-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>

<script type="text/javascript">
    function initPage() {
    }

    function warning() {
        var ret = true;
        ret = confirm(document.getElementById('confirmation').value)
        return ret;
    }

</script>

<div id="alteration-print-letter-outer">
<s:if test="%{!approvalPage}">
    <div class="form-submit">
        <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
        <s:hidden id="printMessage" value="%{getText('print.message')}"/>
    </div>


    <hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:20px;">
    <table border="0" cellspacing="0" width="100%">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td rowspan="8" width="200px" height="350px"></td>
            <td colspan="2" width="600px" height="100px"
                style="text-align:center;margin-left:auto;margin-right:auto;font-size:16pt">
                <label>රාජ්‍ය සේවය පිණිසයි / அரச பணி
                    On State Service</label></td>
            <td rowspan="8" width="200px"></td>
        </tr>
        <tr>
            <td><s:label name="deathAlteration.declarant.declarantFullName" cssStyle="width:600px;font-size:12pt;"
                         cssClass="disable"
                         disabled="true"/></td>
        </tr>
        <tr>
            <td><s:label name="deathAlteration.declarant.declarantAddress" cssStyle="width:600px;font-size:12pt;"
                         cssClass="disable"
                         disabled="true"/></td>
        </tr>
        <tr>
            <td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td>
                Printed On : <%= DateTimeUtils.getISO8601FormattedString(new Date()) %>
            </td>
            <td style="text-align:right;margin-left:auto;margin-right:0;">
            </td>
        </tr>
        </tbody>
    </table>
    <hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:20px;">
</s:if>

<table class="alteration-approval-list-table" cellpadding="0" cellspacing="0" id="approvalTable" width="98%">
<caption/>
<col width="200px"/>
<col width="375px"/>
<col width="375px"/>
<col/>
<tr>
    <td>

    </td>
    <td>මරණ සහතිකයේ පැවති දත්තය<br> இறப்புச் சான்றிதழில் உள்ள தரவு <br> Before change</td>
    <td>සිදුකල වෙනස්කම<br>செய்யப்பட்ட திருத்தம் <br>Alteration</td>
    <td>අනුමැතිය<br> அனுமதி <br> Approve</td>

</tr>
<tbody>
<s:form action="eprDeathAlterationSetBits" method="post" onsubmit="javascript:return warning()">
<s:iterator value="changesList">
<tr>
    <td><s:label value="%{getText('death.alteration.field.'+fieldConstant)}"/></td>
    <td>
        <s:label value="%{existingValue}"/>
    </td>
    <td><s:label value="%{alterationValue}"/>
    </td>
    <s:if test="approvalPage==true">
        <td align="center">
            <s:checkbox name="approvedIndex" fieldValue="%{fieldConstant}"/>
        </td>
    </s:if>
    <s:else>
        <td align="center">
            <s:label value="%{fieldConstant}"/>
        </td>
    </s:else>

</tr>
</s:iterator>
    <%--
   <%
       String prefLang = ((DeathRegister) request.getAttribute("deathRegister")).getDeath().getPreferredLanguage();
       BitSet approval = ((DeathAlteration) request.getAttribute("deathAlteration")).getApprovalStatuses();
   %>

   <s:if test="%{requested.get(0)==true}">
       <tr>
           <td> හදිසි මරණයක්ද ? <br>
               திடீர் மரணமா?<br>
               Sudden death?
           </td>
           <td></td>
           <td></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="0"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(0), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>

   <s:if test="%{requested.get(1)==true}">
       <tr>
           <td>මරණය සිදු වූ දිනය <br>
               இறந்த திகதி <br>
               Date of Death
           </td>
           <td><s:label value="%{deathRegister.death.dateOfDeath}"/></td>
           <td><s:label value="%{deathAlteration.deathInfo.dateOfDeath}"/></td>

           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="1"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(1), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>

   <s:if test="%{requested.get(2)==true}">
       <tr>
           <td> වෙලාව <br>
               நேரம்<br>
               Time
           </td>
           <td><s:label value="%{deathRegister.death.timeOfDeath}"/></td>
           <td><s:label value="%{deathAlteration.deathInfo.timeOfDeath}"/></td>

           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="2"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(2), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>

   <s:if test="%{requested.get(3)==true}">
       <tr>
           <td>මරණය සිදු වූ ස්ථානය සිංහල හෝ දෙමළ භාෂාවෙන <br>
               இறந்த இடம் சிங்களம் தமிழ்<br>
               Place of Death In Sinhala or Tamil
           </td>
           <td>
               <s:label value="%{deathRegister.death.placeOfDeath}"/>
           </td>
           <td>
               <s:label value="%{deathAlteration.deathInfo.placeOfDeath}"/>
           </td>

           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="3"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(3), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>

   <s:if test="%{requested.get(4)==true}">
       <tr>
           <td>මරණය සිදු වූ ස්ථානය ඉංග්‍රීසි භාෂාවෙන් <br>
               இறந்த இடம் ஆங்கில மொழியில்<br>
               Place of Death In In English
           </td>
           <td><s:label value="%{deathRegister.death.placeOfDeathInEnglish}"/></td>
           <td><s:label value="%{deathAlteration.deathInfo.placeOfDeathInEnglish}"/></td>

           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="4"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(4), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(5)==true}">
       <tr>
           <td>මරණයට හේතුව තහවුරුද? <br>
               இறப்பிற்கான காரணம் உறுதியானதா? <br>
               Cause of death established?
           </td>
           <td>
               <%=CommonUtil.getYesOrNo(((DeathRegister) request.getAttribute("deathRegister")).getDeath().isCauseOfDeathEstablished(),
                       prefLang) %>
           </td>
           <td>
               <%=CommonUtil.getYesOrNo(((DeathAlteration) request.getAttribute("deathAlteration")).getDeathInfo().isCauseOfDeathEstablished(),
                       prefLang) %>
           </td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="5"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(5), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(6)==true}">
       <tr>
           <td>මරණයට හේතුව <br>
               இறப்பிற்கான காரணம் <br>
               Cause of death
           </td>
           <td><s:label value="%{deathRegister.death.causeOfDeath}"/></td>
           <td><s:label value="%{deathAlteration.deathInfo.causeOfDeath}"/></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="6"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(6), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(7)==true}">
       <tr>
           <td>හේතුවේ ICD කේත අංකය <br>
               காரணத்திற்கான ICD குறியீட்டு இலக்கம் <br>
               ICD Code of cause
           </td>
           <td><s:label value="%{deathRegister.death.icdCodeOfCause}"/></td>
           <td><s:label value="%{deathAlteration.deathInfo.icdCodeOfCause}"/></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="7"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(7), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>

   <s:if test="%{requested.get(8)==true}">
       <tr>
           <td>ආදාහන හෝ භූමදාන ස්ථානය <br>
               அடக்கம் செய்த அல்லது தகனஞ் செய்த இடம் <br>
               Place of burial or cremation <br>
           </td>
           <td><s:label value="%{deathRegister.death.placeOfBurial}"/></td>
           <td><s:label value="%{deathAlteration.deathInfo.placeOfBurial}"/></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="8"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(8), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>


   <s:if test="%{requested.get(9)==true}">
       <tr>
           <td>අනන්‍යතා අංකය <br>
               தனிநபர் அடையாள எண் <br>
               Identification Number
           </td>
           <td><s:label value="%{deathRegister.deathPerson.deathPersonPINorNIC}"/></td>
           <td><s:label value="%{deathAlteration.deathPerson.deathPersonPINorNIC}"/></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="9"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(9), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(10)==true}">
       <tr>
           <td>විදේශිකය‍කු නම් රට <br>
               வெளிநாட்டவர் எனின் நாடு <br>
               If a foreigner Country
           </td>
           <s:if test="%{deathRegister.death.preferredLanguage=='si'}">
               <td><s:label value="%{deathRegister.deathPerson.deathPersonCountry.siCountryName}"/></td>
               <td><s:label value="%{deathAlteration.deathPerson.deathPersonCountry.siCountryName}"/></td>
           </s:if>
           <s:else>
               <td><s:label value="%{deathRegister.deathPerson.deathPersonCountry.taCountryName}"/></td>
               <td><s:label value="%{deathAlteration.deathPerson.deathPersonCountry.taCountryName}"/></td>
           </s:else>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="10"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(10), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(11)==true}">
       <tr>
           <td>විදේශිකය‍කු නම් ගමන් බලපත්‍ර අංකය <br>
               வெளிநாட்டவர் எனின் கடவுச் சீட்டு இலக்கம்  <br>
               If a foreigner Passport No.
           </td>
           <td><s:label value="%{deathRegister.deathPerson.deathPersonPassportNo}"/></td>
           <td><s:label value="%{deathAlteration.deathPerson.deathPersonPassportNo}"/></td>

           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="11"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(11), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>

   <s:if test="%{requested.get(12)==true}">
       <tr>
           <td>වයස හෝ අනුමාන වයස <br>
              வயது அல்லது உத்தேச வயது  <br>
               Age or probable Age
           </td>
           <td>
               <s:label value="%{deathRegister.deathPerson.deathPersonAge}"/>
           </td>
           <td>
               <s:label value="%{deathAlteration.deathPerson.deathPersonAge}"/>
           </td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="12"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(12), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>


   <s:if test="%{requested.get(13)==true}">
       <tr>
           <td>ස්ත්‍රී පුරුෂ භාවය <br>
               பால் <br>
               Gender
           </td>
           <td><%= GenderUtil.getGender(((DeathRegister) request.getAttribute("deathRegister")).getDeathPerson().getDeathPersonGender(),
                   ((DeathRegister) request.getAttribute("deathRegister")).getDeath().getPreferredLanguage())%>
           </td>
           <td>
               <%= GenderUtil.getGender(((DeathAlteration) request.getAttribute("deathAlteration")).getDeathPerson().getDeathPersonGender(),
                       ((DeathRegister) request.getAttribute("deathRegister")).getDeath().getPreferredLanguage())%>
           </td>

           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="13"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(13), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(14)==true}">
       <tr>
           <td>ජාතිය <br>
               இனம்  <br>
               Race
           </td>
           <s:if test="%{deathRegister.death.preferredLanguage=='si'}">
               <td><s:label value="%{deathRegister.deathPerson.deathPersonRace.siRaceName}"/></td>
               <td><s:label value="%{deathAlteration.deathPerson.deathPersonRace.siRaceName}"/></td>
           </s:if>
           <s:else>
               <td><s:label value="%{deathRegister.deathPerson.deathPersonRace.taRaceName}"/></td>
               <td><s:label value="%{deathAlteration.deathPerson.deathPersonRace.taRaceName}"/></td>
           </s:else>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="14"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(14), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(15)==true}">
       <tr>
           <td>නම රාජ්‍ය භාෂාවෙන් <br>
               (සිංහල / දෙමළ)
               பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்) <br>
               Name in either of the official languages (Sinhala / Tamil)
           </td>
           <td><s:label value="%{deathRegister.deathPerson.deathPersonNameOfficialLang}"/></td>
           <td><s:label value="%{deathAlteration.deathPerson.deathPersonNameOfficialLang}"/></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="15"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(15), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(16)==true}">
       <tr>
           <td>නම ඉංග්‍රීසි භාෂාවෙන් <br>
             பெயர் ஆங்கில மொழியில் <br>
               Name in English
           </td>
           <td><s:label value="%{deathRegister.deathPerson.deathPersonNameInEnglish}"/></td>
           <td><s:label value="%{deathAlteration.deathPerson.deathPersonNameInEnglish}"/></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="16"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(16), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(17)==true}">
       <tr>
           <td>ස්ථිර ලිපිනය <br>
             நிரந்தர வதிவிட முகவரி <br>
               Permanent Address
           </td>
           <td><s:label value="%{deathRegister.deathPerson.deathPersonPermanentAddress}"/></td>
           <td><s:label value="%{deathAlteration.deathPerson.deathPersonPermanentAddress}"/></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="17"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(17), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(18)==true}">
       <tr>
           <td>පියාගේ අනන්‍යතා අංකය <br>
               தந்தையின் அடையாள எண் <br>
               Fathers Identification Number
           </td>
           <td><s:label value="%{deathRegister.deathPerson.deathPersonFatherPINorNIC}"/></td>
           <td><s:label value="%{deathAlteration.deathPerson.deathPersonFatherPINorNIC}"/></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="18"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(18), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(19)==true}">
       <tr>
           <td>පියාගේ සම්පුර්ණ නම <br>
               தந்தையின் முழுப் பெயர்<br>
               Fathers full name
           </td>
           <td><s:label value="%{deathRegister.deathPerson.deathPersonFatherFullName}"/></td>
           <td><s:label value="%{deathAlteration.deathPerson.deathPersonFatherFullName}"/></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="19"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(19), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(20)==true}">
       <tr>
           <td>මවගේ අනන්‍යතා අංකය <br>
              தாயின் அடையாள எண் <br>
               Mothers Identification Number
           </td>
           <td><s:label value="%{deathRegister.deathPerson.deathPersonMotherPINorNIC}"/></td>
           <td><s:label value="%{deathAlteration.deathPerson.deathPersonMotherPINorNIC}"/></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="20"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(20), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>
   <s:if test="%{requested.get(21)==true}">
       <tr>
          <td>මවගේ සම්පුර්ණ නම <br>
               தாயின் முழுப் பெயர்<br>
               Mothers full name
           </td>
           <td><s:label value="%{deathRegister.deathPerson.deathPersonMotherFullName}"/></td>
           <td><s:label value="%{deathAlteration.deathPerson.deathPersonMotherFullName}"/></td>
           <s:if test="%{approvalPage}">
               <td align="center">
                   <s:checkbox name="approvedIndex" fieldValue="21"/>
               </td>
           </s:if>
           <s:else>
               <td align="center">
                   <%=CommonUtil.getYesOrNo(approval.get(21), prefLang) %>
               </td>
           </s:else>
       </tr>
   </s:if>

   </tbody>
    --%>

</table>
<s:if test="%{!approvalPage}">
    <div>
        <br>
        සටහන/note/***
        <br>
        අනුමතකල වෙනස්කම් සහිත නව මරණ සහතිකයක් සඳහා ඉල්ලුම් කල හැක.
        <br>
        You can apply for a new death certificate with altered values.
        <br>
        *****
    </div>
    .
</s:if>
<s:else>
    <table>
        <caption/>
        <col>
        <col>
        <tbody>
        <tr>
            <td width="1000px" align="right"><s:label value="%{getText('label.apply.changes')}"/></td>
            <td align="right"><s:checkbox id="applyChanges" name="applyChanges"/></td>
        </tr>
        </tbody>
    </table>
    <div class="form-submit">
        <s:submit name="submit" value="%{getText('lable.update')}"/>
        <s:hidden name="deathAlterationId" value="%{deathAlteration.idUKey}"/>
    </div>
</s:else>
</s:form>
</div>

<s:hidden id="confirmation" value="%{getText('confirm.apply.changes')}"/>



