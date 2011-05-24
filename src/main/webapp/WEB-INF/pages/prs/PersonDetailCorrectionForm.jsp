<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<style type="text/css">
*{
    margin:auto;
}
.inner-table
{
    border-color: #000;
    border-width: 0 0 1px 1px;
    border-style: solid;
    font-size:9pt;
}
.sub-table{
    border:0;
}
hr{
    margin-bottom:2px;
    margin-top:2px;
}
td
{
    border-color: #000;
    border-width: 1px 1px 0 0;
    border-style: solid;
    margin: 0;
    padding-top:5px;
    padding-bottom:5px;
    padding-left:5px;
}

.right_align{
    padding-right:5px;
}
.special{
    padding:0 0 0 0;
    border-top:0;
    border-right:0;
    margin:0 0 0 0;
}

#main_title{
    text-align:center;
    font-size:12pt;
    padding-top:10px;
    padding-bottom:10px;
}
.title{
    text-align:center;
    font-size:12pt;
    padding-top:10px;
    padding-bottom:10px;
}

/*textarea{
    width:90%;
} */
body{

    background:#fff;
}

</style>
</head>
<body>
<table class="outer-table" width="100%" cellpadding="0" cellspacing="0">
    <tbody>
        <tr>  <!-- header information [ table ]   -->

            <table class="inner-table" width="100%" cellpadding="0" cellspacing="0">
                <tr>
		            <td colspan="2" width="40%">කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br/>
                        அலுவலக பாவனைக்காக மட்டும் <br/>
                        For office use only
                    </td>
		            <td rowspan="3" align="center">
                        <img src="<s:url value="/images/official-logo.png"/>" alt=""/>
                    </td>
		            <td colspan="2" width="40%">
                        කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br/>
                        அலுவலக பாவனைக்காக மட்டும் <br/>
                        For office use only
		            </td>
	            </tr>
	            <tr>
		            <td>ඉල්ලුම්පතේ තත්වය<br/>
                        விண்ணப்பத்தின் நிலைமை  <br/>
                        Status of Application
                    </td>
		            <td class="special">
                        <table cellpadding="0" cellspacing="0" width="100%" class="sub-table">
                            <tr>
                            <td><s:radio name="person.civilStatus_app_sts" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"/></td>
                            <td><s:label>අනුමතයි /அனுமதி / Approved</s:label></td>
                            </tr>
                            <tr>
                            <td><s:radio name="person.civilStatus_app_sts" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"/></td>
                            <td><s:label>ප්‍රතික්ෂේපිතයි / தள்ளுபடி/ Rejected</s:label></td>
                            </tr>
                        </table>
                    </td>
		            <!--<td>&nbsp;</td>-->
		            <td>අනුක්‍රමික අංකය  <br/>
                        தொடர் இலக்கம் <br/>
                        Serial Number
                    </td>
		            <td>
                        <s:textfield name="serialNo" id="serial" maxLength="10"/>
		            </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                    <!--<td>&nbsp;</td>-->
                    <td>භාරගත් දිනය  <br/>
                        பெறப்பட்ட திகதி   <br/>
                        Date of Acceptance
                    </td>
                    <td>
                        <s:textfield name="serialNo" id="serial" maxLength="10"/>
                    </td>
	            </tr>
            </table>

        </tr>
        <tr>  <!-- Form title -->
            <td>
                <div id="main_title">
                    ජනගහන ලේඛනයේ විස්තර නිවැරදි කිරීම<br/>
                    குடிமதிப்பீட்டு ஆவணத்தில் ஆட்களை பதிவு செய்தல்  <br/>
                    Correction of details in the Population Registry
                </div>
            </td>
        </tr>
        <tr>  <!-- Basic Details [ table ]   -->
            <table width="100%" class="inner-table" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        (1) අනන්‍යතා අංකය  <br/>
                        அடையாள எண்  <br/>
                        Identification number
                    </td>
                    <td width="20%">
                        <s:textfield></s:textfield>
                    </td>
                    <td width="30%">
                        (2) ජාතික හැඳුනුම්පත් අංකය  <br/>
                        தேசிய அடையாள அட்டை இலக்கம்    <br/>
                        National Identity Card (NIC) Number
                    </td>
                    <td>
                        <s:textfield></s:textfield>
                    </td>
                </tr>
                <tr>
                    <td>
                        (3) ජාතිය<br/>
                        இனம்  <br/>
                        Race
                    </td>
                    <td>
                        <s:textfield></s:textfield>
                    </td>
                    <td>
                        (4) උපන් දිනය <br/>
                        பிறந்த திகதி   <br/>
                        Date of Birth
                    </td>
                    <td>
                        <s:textfield></s:textfield>
                    </td>
                </tr>
                <tr>
                    <td>
                        (5) උපන් ස්ථානය<br/>
                        பிறந்த இடம் <br/>
                        Place of Birth
                    </td>
                    <td colspan="3">
                        <s:textfield cssStyle="width:83%"></s:textfield>
                    </td>
                </tr>
                <tr>
                    <td>
                        (6) නම රාජ්‍ය භාෂාවෙන්  <br/>
                        (සිංහල / දෙමළ)  <br/>
                        பெயர் அரச கரும மொழியில்  (சிங்களம் / தமிழ்)<br/>
                        Name in any of the official languages (Sinhala / Tamil)
                    </td>
                    <td colspan="3">
                        <s:textarea name="prsn.personFullNameOfficialLang" id="personFullNameOfficialLang" cols="85" rows="3"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        (7) නම ඉංග්‍රීසි භාෂාවෙන්  <br/>
                        பெயர் ஆங்கில மொழியில் <br/>
                        Name in English
                    </td>
                    <td colspan="3">
                        <s:textarea name="prsn.personFullNameEnglish" id="personFullNameEnglish" cols="85" rows="3"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        (8) ස්ත්‍රී පුරුෂ භාවය   <br/>
                        பால்    <br/>
                        Gender
                    </td>
                    <td>
                        <s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="prsn.personGender" cssStyle="width:190px; margin-left:5px;" id="genderList"/>
                    </td>
                    <td colspan="2" class="special">
                        <table width="100%" cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="right" class="right_align">අවිවාහක<br>திருமணமாகாதவர் <br>Never Married</td>
                                <td><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"/></td>
                                <td align="right" class="right_align">විවාහක<br>திருமணமாணவர் <br>Married</td>
                                <td><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'MARRIED':''}"/></td>
                                <td align="right" class="right_align">දික්කසාද<br>திருமணம் தள்ளுபடி செய்தவர் <br>Divorced</td>
                                <td><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'DIVORCED':''}"/></td>
                            </tr>
                            <tr>
                                <td align="right" class="right_align">වැන්දඹු<br>விதவை <br>Widowed</td>
                                <td><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'WIDOWED':''}"/></td>
                                <td align="right" class="right_align">ශුන්‍ය කර ඇත<br>தள்ளிவைத்தல் <br>Annulled</td>
                                <td><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'ANNULLED':''}"/></td>
                                <td align="right" class="right_align">වෙන් වී ඇත<br>பிரிந்திருத்தல் <br>Separated</td>
                                <td><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'SEPARATED':''}"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        (10) මවගේ අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය  <br/>
                        தாயின் அடையாள எண் அல்லது தேசிய அடையாள அட்டை இலக்கம்     <br/>
                        Mothers Identification Number (PIN) or NIC
                    </td>
                    <td>
                        <s:textfield></s:textfield>
                    </td>
                    <td>
                        (11) පියාගේ අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය  <br/>
                        தந்தையின் அடையாள எண் அல்லது தேசிய அடையாள அட்டை இலக்கம்    <br/>
                        Fathers Identification Number (PIN) or NIC
                    </td>
                    <td>
                        <s:textfield></s:textfield>
                    </td>
                </tr>

            </table>
        </tr>
        <tr>
            <td colspan="4">
                 &nbsp;
            </td>
        </tr>
        <tr>  <!-- Addresses and TP Details  [ table ]   -->
            <table width="100%" cellpadding="0" cellspacing="0"  class="inner-table">
                <tr>
                    <td width="15%">
                        (12) ස්ථිර ලිපිනය<br/>
                        நிரந்தர வதிவிட முகவரி <br/>
                        Permanent Address
                    </td>
                    <td width="50%">
                        <s:textarea name="prsn.permanentAddress" id="permanentAddress" cols="65" rows="4"/>
                    </td>
                    <td width="15%">
                        ආරම්භක දිනය <br/>
                        முகவரி  <br/>
                        Start date
                    </td>
                    <td width="20%">
                        <s:textfield></s:textfield>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        වර්තමාන පදිංචිය වෙනත් ස්ථානයක නම් පමණක්, தற்போதைய முகவரி வேறு இடமாயின் மட்டும் , Only if residing at a different location
                    </td>
                </tr>
                <tr>
                    <td>
                        (13) වර්තමාන ලිපිනය <br/>
                        தற்போதைய வதிவிட முகவரி  <br/>
                        Current Address
                    </td>
                    <td>
                        <s:textarea name="prsn.currentAddress" id="currentAddress" cols="65" rows="4"/>
                    </td>
                    <td>
                        ආරම්භක දිනය <br/>
                        முகவரி   <br/>
                        Start date
                    </td>
                    <td><s:textfield></s:textfield></td>
                </tr>
                <tr>
                    <td>
                        (14) දුරකථන අංක  <br/>
                        தொலைபேசி இலக்கம் <br/>
                        Telephone Numbers
                    </td>
                    <td colspan="3" class="special">
                        <table width="100%" cellpadding="0" cellspacing="0" class="sub-table">
                            <tr>
                                <td>
                                    <s:textfield></s:textfield>
                                </td>
                                <td width="15%">
                                    (15) ඉ – තැපැල <br/>
                                    மின்னஞ்சல்  <br/>
                                    Email
                                </td>
                                <td width="60%">
                                    <s:textfield cssStyle="width:90%"></s:textfield>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>  <!-- Date and Signature   -->
               <table width="100%" cellpadding="0" cellspacing="0"  class="inner-table">
                    <td colspan="4" class="title_td">
                        <div class="title">
                        වෙනත් රටවල පුරවැසිභාවය ඇතිනම් ඒ පිලිබඳ විස්තර  <br/>
                        வேறு நாடுகளில்  பிரஜாவுரிமை இருந்தால் அது பற்றிய தகவல்கள்   <br/>
                        If a citizen of any other countries, such details
                        </div>
                    </td>

                </tr>
                <tr>
                    <td>රට / நாடு /Country</td>
                    <td><s:textfield></s:textfield></td>
                    <td>ගමන් බලපත්‍ර අංකය / கடவுச் சீட்டு இல. /Passport No.</td>
                    <td><s:textfield></s:textfield></td>
                </tr>
                <tr>
                    <td>රට / நாடு /Country</td>
                    <td><s:textfield></s:textfield></td>
                    <td>ගමන් බලපත්‍ර අංකය / கடவுச் சீட்டு இல. /Passport No.</td>
                    <td><s:textfield></s:textfield></td>
               </tr>
            </table>
        </tr>
    </tbody>
</table>
</body>
</html>