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
a:link,a:visited{
    text-decoration:none;
    color:black;
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
#citizen_main{
    display:block;
    position:relative;    
}
.citizen_title{
    text-align:center;
    font-size:12pt;
    padding-top:10px;
    padding-bottom:10px;
    height:70px;
    float:left;
    margin-left:180px;
}
#citizen_icon{
    padding:0;
    width:100px;
    height:90px;
    float:right;
    margin:auto;
}
#citizen_blank{
    padding:0;
    width:100px;
    height:90px;
    float:left;
    margin:auto;
}
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
        <tr>
            <td>
                &nbsp;
            </td>
        </tr>
        <tr>  <!-- Form title -->
            <td>
                <div id="main_title">
                    ජනගහන ලේඛනයේ විස්තර වෙනස් කිරීම<br/>
                    குடிமதிப்பீட்டு ஆவணத்தில் ஆட்களை பதிவு செய்தல்   <br/>
                    Updating details in the Population Registry
                </div>
            </td>
        </tr>
        <tr>  <!-- Basic Details [ table ]   -->
            <table width="100%" class="inner-table" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="25%" colspan="2">
                        (1) අනන්‍යතා අංකය  <br/>
                        அடையாள எண்  <br/>
                        Identification number
                    </td>
                    <td width="25%" colspan="2">
                        <s:textfield></s:textfield>
                    </td>
                    <td width="25%" colspan="2">
                        (2) ජාතික හැඳුනුම්පත් අංකය  <br/>
                        தேசிய அடையாள அட்டை இலக்கம்    <br/>
                        National Identity Card (NIC) Number
                    </td>
                    <td width="25%" colspan="2">
                        <s:textfield></s:textfield>
                    </td>
                </tr>
                <tr>
                    <td rowspan="2" colspan="2">
                        (3) සිවිල් තත්වය  <br/>
                        சிவில் நிலைமை   <br/>
                        Civil Status
                    </td>
                    <td align="right" class="right_align">අවිවාහක<br>திருமணமாகாதவர் <br>Never Married</td>
                    <td width="5%"><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"/></td>
                    <td align="right" class="right_align">විවාහක<br>திருமணமாணவர் <br>Married</td>
                    <td width="5%"><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'MARRIED':''}"/></td>
                    <td align="right" class="right_align">දික්කසාද<br>திருமணம் தள்ளுபடி செய்தவர் <br>Divorced</td>
                    <td width="5%"><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'DIVORCED':''}"/></td>
                </tr>
                <tr>
                    <td align="right" class="right_align">වැන්දඹු<br>விதவை <br>Widowed</td>
                    <td><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'WIDOWED':''}"/></td>
                    <td align="right" class="right_align">නිෂ්ප්‍රභාකර ඇත <br>தள்ளிவைத்தல் <br>Annulled</td>
                    <td><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'ANNULLED':''}"/></td>
                    <td align="right" class="right_align">වෙන් වී ඇත<br>பிரிந்திருத்தல் <br>Separated</td>
                    <td><s:radio name="person.civilStatus_c" list="#@java.util.HashMap@{'SEPARATED':''}"/></td>
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
                                    (15) ඉ – තැපැල්  <br/>
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
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" id="citizen_table">
                    
                </table>
            </td>
        </tr>
        <tr>
            &nbsp;
        </tr>
        
        <tr>
            &nbsp;
        </tr>
    </tbody>
</table>
</body>
</html>