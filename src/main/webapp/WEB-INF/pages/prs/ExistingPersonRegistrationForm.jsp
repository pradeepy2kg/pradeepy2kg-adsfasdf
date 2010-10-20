<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>

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
<s:form action="eprExistingPersonRegistration.do" method="POST">

<table class="table_reg_header_01" style="font-size:9pt">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
        <s:actionmessage cssStyle="text-align:left;color:red;"/>
    </tr>
    <tr>
        <td width="300px"></td>
        <td align="center" style="font-size:12pt; width:430px">
            <img src="<s:url value="/images/official-logo.png"/>"/><br>
            <label>ජනගහන ලේඛනයේ පුද්ගලයකු ලියාපදිංචි කිරීම
                <br>* Tamil
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
            <s:textfield name="person.nic" maxLength="10"/>
        </td>
        <td rowspan="2" colspan="2">
            (3) විදේශිකයකු නම්<br>* Tamil<br>If a foreigner
        </td>
        <td>රට<br>* Tamil<br>Country</td>
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
        <td>ගමන් බලපත්‍ර අංකය<br>* Tamil<br>Passport No.</td>
        <td colspan="2">
            <s:textfield name="personPassportNo" id="personPassportNo" maxLength="15"/>
        </td>
    </tr>
    <tr>
        <td>(4) උපන් දිනය<br>* Tamil<br>Date of Birth</td>
        <td colspan="7">
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
            <s:textfield id="birthDatePicker" name="person.dateOfBirth" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td>(5) උපන් ස්ථානය<br>* Tamil<br>Place of Birth</td>
        <td colspan="7">
            <s:textfield name="person.placeOfBirth" id="placeOfBirth" cssStyle="width:97.6%;" maxLength="255"/>
        </td>
    </tr>
    <tr>
        <td class="font-9">
            (6) නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
            <br>பெயா் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
            <br>Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="7">
            <s:textarea name="person.fullNameInOfficialLanguage" id="personFullNameOfficialLang"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td class="font-9">(7) නම ඉංග්‍රීසි භාෂාවෙන් <br>பெயா் ஆங்கில மொழியில்<br>Name in English</td>
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
            (9) සිවිල් තත්වය<br>* Tamil<br>Civil Status
        </td>
        <td colspan="5" style="border:none;padding:0">
            <table style="border:none;" cellspacing="0;" width="100%">
                <tr>
                    <td align="right">අවිවාහක<br>* Tamil<br>Never Married</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"/></td>
                    <td align="right">විවාහක<br>* Tamil<br>Married</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'MARRIED':''}"/></td>
                    <td align="right">දික්කසාද<br>* Tamil<br>Divorced</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'DIVORCED':''}"/></td>
                </tr>
                <tr>
                    <td align="right">වැන්දබු<br>* Tamil<br>Widowed</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'WIDOWED':''}"/></td>
                    <td align="right">නිෂ්ප්‍රභාකර ඇත <br>* Tamil<br>Anulled</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'ANNULLED':''}"/></td>
                    <td align="right">වෙන් වී ඇත<br>* Tamil<br>Separated</td>
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
        <td>(10) ස්ථිර ලිපිනය<br>* Tamil<br>Permanent Address</td>
        <td colspan="7">
            <s:textarea name="permanentAddress" cssStyle="width:98.2%;text-transform:uppercase;"/>
        </td>
    </tr>
    <tr>
        <td colspan="8" style="font-size:10pt;">
            වර්තමාන පදිංචිය වෙනත් ස්ථානයක නම් පමණක්, தாயின் நிரந்தர வதிவிட முகவரி, Only if residing at a
            different location,
        </td>
    </tr>
    <tr>
        <td>(11) වර්තමාන ලිපිනය<br>* Tamil<br>Current Address</td>
        <td colspan="7">
            <s:textarea name="currentAddress" cssStyle="width:98.2%;text-transform:uppercase;"/>
        </td>
    </tr>
    <tr>
        <td>(12) දුරකථන අංක<br>* Tamil<br>Telephone Numbers</td>
        <td colspan="2">
            <s:textfield name="person.personPhoneNo" maxLength="15"/>
        </td>
        <td>(13) ඉ – තැපැල් <br>* Tamil<br>Email</td>
        <td colspan="4">
            <s:textfield name="person.personEmail" cssStyle="text-transform:none;" maxLength="30"/>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_05" style="border:none">
    <tr>
        <td align="center" style="font-size:10pt;border:none">
            වෙනත් රටවල පුරවැසිභාවය ඇතිනම් ඒ පිලිබඳ විස්තර
            <br>* Tamil
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
        <td>රට<br>* Tamil<br>Country</td>
        <td></td>
        <td>ගමන් බලපත්‍ර අංකය<br>* Tamil<br>Passport No.</td>
        <td></td>
    </tr>
    <tr>
        <td>රට<br>* Tamil<br>Country</td>
        <td></td>
        <td>ගමන් බලපත්‍ර අංකය<br>* Tamil<br>Passport No.</td>
        <td></td>
    </tr>
    <tr>
        <td>රට<br>* Tamil<br>Country</td>
        <td></td>
        <td>ගමන් බලපත්‍ර අංකය<br>* Tamil<br>Passport No.</td>
        <td></td>
    </tr>
    <tr>
        <td>රට<br>* Tamil<br>Country</td>
        <td></td>
        <td>ගමන් බලපත්‍ර අංකය<br>* Tamil<br>Passport No.</td>
        <td></td>
    </tr>
</table>

<div class="form-submit">
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>

</s:form>
</div>