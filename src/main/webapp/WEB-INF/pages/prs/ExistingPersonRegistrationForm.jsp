<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script>
     //these inpute can not be null
    var errormsg = "";
    function validate() {

        var returnval = true;
        var domObject;
        domObject=document.getElementById("submitDatePicker");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error1');
        } else {
            isDate(domObject.value, 'error0', 'error1');
        }

        domObject = document.getElementById("person_PIN");
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error0', 'error2');
        }

        domObject = document.getElementById("personPassportNo");
        if (!isFieldEmpty(domObject)) {
            validatePassportNo(domObject, 'error0', 'error3');
        }

        domObject = document.getElementById("birthDatePicker");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error4');
        } else {
            isDate(domObject.value, 'error0', 'error4');
        }

        domObject = document.getElementById("placeOfBirth");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error5');
        }

        domObject = document.getElementById("personFullNameOfficialLang");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error6');
        }

        domObject = document.getElementById("personFullNameEnglish");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error7');
        }

        domObject = document.getElementById("permanentAddress");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error8');
        }

        domObject = document.getElementById("personPhoneNo");
        if (!isFieldEmpty(domObject)) {
            validatePhoneNo(domObject, "error0", 'error9');
        }

        domObject = document.getElementById("personEmail");
        if (!isFieldEmpty(domObject)) {
            validateEmail(domObject, "error0", 'error10');
        }

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

</script>
<script type="text/javascript">
    $(function() {
        $("#submitDatePicker").datepicker({
            changeYear:true,
            yearRange:'1960:2020',
            dateFormat:'yy-mm-dd'
        });
    });

    $(function() {
        $("#birthDatePicker").datepicker({
            changeYear:true,
            yearRange:'1960:2020',
            dateFormat:'yy-mm-dd'
        });
    });

    function initPage() {
    }
</script>

<div class="prs-existing-person-register-outer">
<s:form action="eprExistingPersonRegistration.do" method="POST" onsubmit="javascript:return validate()">

<table class="table_reg_header_01" style="font-size:9pt">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="300px"></td>
        <td align="center" style="font-size:12pt; width:430px">
            <img src="<s:url value="/images/official-logo.png"/>"/><br>
            <label>ජනගහන ලේඛනයේ පුද්ගලයකු ලියාපදිංචි කිරීම
                <br>குடிமதிப்பீட்டு ஆவணத்தில் ஆட்களை பதிவு செய்தல்
                <br>Registration of a person in the Population Registry</label>
        </td>
        <td>
            <table class="table_reg_datePicker_page_01" style="margin-right:0">
                <tr>
                    <td colspan="2">කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>அலுவலக பாவனைக்காக மட்டும்
                        <br>For office use only
                        <hr>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label><span class="font-8">භාරගත්  දිනය<br>* Tamil<br>Submitted Date</span></label>
                    </td>
                    <td>
                        <s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
                        <s:textfield name="person.dateOfRegistration" id="submitDatePicker" maxLength="10"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_05" cellspacing="0" cellpadding="0" style="margin-bottom:0;margin-top:20px;">
    <caption></caption>
    <col width="220px"/>
    <col width="130px"/>
    <col width="130px"/>
    <col width="100px"/>
    <col width="80px"/>
    <col width="120px"/>
    <col width="120px"/>
    <col width="130px"/>
    <tbody>

    <tr>
        <td>
            (1) ජාතික හැඳුනුම්පත් අංකය
            <br>* Tamil
            <br>National Identity Card (NIC) Number
        </td>
        <td colspan="2">
            <s:textfield name="person.nic" id="person_PIN" maxLength="10"/>
        </td>
        <td rowspan="2" colspan="2">
            (3) විදේශිකයකු නම්<br>வெளிநாட்டவர் எனின் <br>If a foreigner
        </td>
        <td>රට<br>நாடு<br>Country</td>
        <td colspan="2">
            <s:select id="personCountryId" name="personCountryId" list="countryList" headerKey="0"
                      headerValue="%{getText('select_country.label')}" cssStyle="width:75%;margin-left:5px;"/>
        </td>
    </tr>
    <tr>
        <td>
            (2) ජාතිය<br>இனம்<br>Race
        </td>
        <td colspan="2">
            <s:select id="personRaceId" name="personRaceId" list="raceList" headerKey="0"
                      headerValue="%{getText('select_race.label')}" cssStyle="width:72%;margin-left:5px;"/>
        </td>
        <td>ගමන් බලපත්‍ර අංකය<br>கடவுச் சீட்டு இல.<br>Passport No.</td>
        <td colspan="2">
            <s:textfield name="personPassportNo" id="personPassportNo" maxLength="15"/>
        </td>
    </tr>
    <tr>
        <td>(4) උපන් දිනය<br>பிறந்த திகதி<br>Date of Birth</td>
        <td colspan="7">
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
            <s:textfield id="birthDatePicker" name="person.dateOfBirth" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td>(5) උපන් ස්ථානය<br>பிறந்த இடம்<br>Place of Birth</td>
        <td colspan="7">
            <s:textfield name="person.placeOfBirth" id="placeOfBirth" cssStyle="width:97.6%;" maxLength="255"/>
        </td>
    </tr>
    <tr>
        <td class="font-9">
            (6) නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
            <br>பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
            <br>Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="7">
            <s:textarea name="person.fullNameInOfficialLanguage" id="personFullNameOfficialLang"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td class="font-9">(7) නම ඉංග්‍රීසි භාෂාවෙන් <br>பெயர் ஆங்கில மொழியில்<br>Name in English</td>
        <td colspan="7">
            <s:textarea name="person.fullNameInEnglishLanguage" id="personFullNameEnglish"
                        cssStyle="width:98.2%;"/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;"
                 id="childName">
        </td>
    </tr>

    <tr>
        <td class="font-9">
            (8) ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender of the child
        </td>
        <td colspan="2">
            <s:select name="person.gender" cssStyle="width:190px; margin-left:5px;"
                      list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"/>
        </td>
        <td rowspan="2">
            (9) සිවිල් තත්ත්වය<br>சிவில் நிலைமை <br>Civil Status
        </td>
        <td colspan="5" style="border:none;padding:0">
            <table style="border:none;" cellspacing="0;" width="100%">
                <tr>
                    <td align="right">අවිවාහක<br>திருமணமாகாதவர் <br>Never Married</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"/></td>
                    <td align="right">විවාහක<br>திருமணமாணவர் <br>Married</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'MARRIED':''}"/></td>
                    <td align="right">දික්කසාද<br>திருமணம் தள்ளுபடி செய்தவர் <br>Divorced</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'DIVORCED':''}"/></td>
                </tr>
                <tr>
                    <td align="right">වැන්දබු<br>விதவை <br>Widowed</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'WIDOWED':''}"/></td>
                    <td align="right">නිෂ්ප්‍රභාකර ඇත <br>தள்ளிவைத்தல் <br>Anulled</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'ANNULLED':''}"/></td>
                    <td align="right">වෙන් වී ඇත<br>பிரிந்திருத்தல் <br>Separated</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'SEPARATED':''}"/></td>
                </tr>
            </table>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_05" cellspacing="0" cellpadding="0" style="margin-top:20px;margin-bottom:10px;">
    <caption></caption>
    <col width="220px"/>
    <col width="130px"/>
    <col width="130px"/>
    <col width="100px"/>
    <col width="100px"/>
    <col width="110px"/>
    <col width="110px"/>
    <col width="130px"/>
    <tbody>

    <tr>
        <td>(10) ස්ථිර ලිපිනය<br>நிரந்தர வதிவிட முகவரி<br>Permanent Address</td>
        <td colspan="7">
            <s:textarea name="permanentAddress" id="permanentAddress" cssStyle="width:98.2%;text-transform:uppercase;"/>
        </td>
    </tr>
    <tr>
        <td colspan="8" style="font-size:10pt;">
            වර්තමාන පදිංචිය වෙනත් ස්ථානයක නම් පමණක්, தற்போதைய முகவரி வேறு இடமாயின் மட்டும், Only if residing at a
            different location,
        </td>
    </tr>
    <tr>
        <td>(11) වර්තමාන ලිපිනය<br>தற்போதைய வதிவிட முகவரி<br>Current Address</td>
        <td colspan="7">
            <s:textarea name="currentAddress" cssStyle="width:98.2%;text-transform:uppercase;"/>
        </td>
    </tr>
    <tr>
        <td>(12) දුරකථන අංක<br>தொலைபேசி இலக்கம்<br>Telephone Numbers</td>
        <td colspan="2">
            <s:textfield name="person.personPhoneNo" id="personPhoneNo" maxLength="15"/>
        </td>
        <td>(13) ඉ – තැපැල් <br>மின்னஞ்சல்<br>Email</td>
        <td colspan="4">
            <s:textfield name="person.personEmail" id="personEmail" cssStyle="text-transform:none;" maxLength="30"/>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_05" style="border:none">
    <tr>
        <td align="center" style="font-size:10pt;border:none">
            වෙනත් රටවල පුරවැසිභාවය ඇතිනම් ඒ පිලිබඳ විස්තර
            <br>வேறு நாடுகளில் பிரஜாவுரிமை இருந்தால் அது பற்றிய தகவல்கள்
            <br>If a citizen of any other countries, such details
        </td>
    </tr>
</table>

<%--TODO change following table--%>
<table class="table_reg_page_05" cellspacing="0" cellpadding="0" style="margin-bottom:10px;">
    <col width="220px"/>
    <col/>
    <col width="220px"/>
    <col/>
    <tr>
        <td>රට<br>நாடு <br>Country</td>
        <td></td>
        <td>ගමන් බලපත්‍ර අංකය<br>கடவுச் சீட்டு இல.<br>Passport No.</td>
        <td></td>
    </tr>
    <tr>
        <td>රට<br>நாடு <br>Country</td>
        <td></td>
        <td>ගමන් බලපත්‍ර අංකය<br>கடவுச் சீட்டு இல.<br>Passport No.</td>
        <td></td>
    </tr>
    <tr>
        <td>රට<br>நாடு <br>Country</td>
        <td></td>
        <td>ගමන් බලපත්‍ර අංකය<br>கடவுச் சீட்டு இல.<br>Passport No.</td>
        <td></td>
    </tr>
    <tr>
        <td>රට<br>நாடு <br>Country</td>
        <td></td>
        <td>ගමන් බලපත්‍ර අංකය<br>கடவுச் சீட்டு இல.<br>Passport No.</td>
        <td></td>
    </tr>
</table>

<div class="form-submit">
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>

<s:hidden id="error0" value="%{getText('er.invalid.inputType')}"/>
<s:hidden id="error1" value="%{getText('er.label.submitDatePicker')}"/>
<s:hidden id="error2" value="%{getText('er.label.person_PIN')}"/>
<s:hidden id="error3" value="%{getText('er.label.personPassportNo')}"/>
<s:hidden id="error4" value="%{getText('er.label.birthDatePicker')}"/>
<s:hidden id="error5" value="%{getText('er.label.placeOfBirth')}"/>
<s:hidden id="error6" value="%{getText('er.label.personFullNameOfficialLang')}"/>
<s:hidden id="error7" value="%{getText('er.label.personFullNameEnglish')}"/>
<s:hidden id="error8" value="%{getText('er.label.permanentAddress')}"/>
<s:hidden id="error9" value="%{getText('er.label.personPhoneNo')}"/>
<s:hidden id="error10" value="%{getText('er.label.personEmail')}"/>
</s:form>
</div>