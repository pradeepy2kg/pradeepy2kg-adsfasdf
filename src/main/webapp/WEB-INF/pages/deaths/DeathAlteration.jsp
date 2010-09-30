<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="death-alteration-outer">
<s:form method="post" action="eprCaptureDeathAlteration.do">
<table class="death-alteration-table-style01" style="width:1030px;" cellpadding="2px">
    <tr>
        <td width="30%"></td>
        <td width="35%" style="text-align:center;"></td>
        <td width="35%">
            <table class="birth-alteration-table-style02" cellspacing="0" style="float:right;width:100%">
                <tr>
                    <td colspan="2" style="text-align:center;">කාර්යාල ප්‍රයෝජනය සඳහා පමණි /<br>
                        அலுவலக பாவனைக்காக மட்டும் / <br>
                        For office use only
                    </td>
                </tr>
                <tr>
                    <td width="40%"><s:label value="අනුක්‍රමික අංකය"/><br>
                        <s:label value=" தொடர் இலக்கம் "/><br>
                        <s:label value=" Serial Number"/>
                    </td>
                    <td width="60%"><s:textfield id="bdfSerialNo" name="alterationSerialNo" maxLength="10"
                                                 /></td>
                </tr>
                <tr>
                    <td><s:label value="භාරගත් දිනය"/><br>
                        <s:label value="பிறப்பைப் பதிவு திி"/> <br>
                        <s:label value="Date of Acceptance"/>
                    </td>
                    <td><s:textfield id="acceptanceDate" name="dateReceived"/></td>
                <tr>
                    <td><s:label value="පනතේ වගන්තිය "/><br>
                        <s:label value="பிறப்பைப்"/> <br>
                        <s:label value="Section of the Act"/>
                    </td>
                    <td><%--<s:select
                                list="#@java.util.HashMap@{'1':'27','2':'52(1)','3':'27 (A)'}"
                                name="sectionOfAct" cssStyle="width:190px; margin-left:5px;" disabled="true"
                                id="sectionOfAct"/>--%></td>

                </tr>
            </table>
        </td>
    </tr>
</table>

<table class="death-alteration-table-style01" style="width:1030px;">
    <tr>
        <td colspan="3" style="font-size:12pt;text-align:center;">
            <s:label
                    value="මරණ සහතිකයක දෝෂ නිවැරදි කිරීම (උප්පැන්න හා මරණ ලියාපදිංචි කිරීමේ පනතේ 52 (1) සහ 53 වගන්ති)"/>
            <br>
            <s:label value="தந்தை பற்றிய தகவல்"/> <br>
            <s:label
                    value="Correction of Errors of a Death Certificate (under the Births and Deaths Registration Act. Sections 52 (1) and 53)"/>
        </td>
    </tr>
    <tr>
        <td>
            <s:label
                    value="සැ.යු. හදිසි මරණ සහතිකයක වෙනසක් කිරීමට ඉල්ලුම් කල හැක්කේ මරණ පරීක්ෂක හට පමණකි. in Tamil. Note: An alteration on a certificate of death for a sudden death can be requested only by an inquirer into deaths"/>
        </td>
    </tr>
    <tr>
        <td></td>
    </tr>
    <tr>
        <td></td>
    </tr>
    <tr>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;">
            <s:label value="වෙනස් කලයුතු මරණ සහතිකය පිලිබඳ විස්තර"/> <br>
            <s:label value="பிள்ளை பற்றிய தகவல்"/> <br>
            <s:label value="Particulars of the Death Certificate to amend"/>
        </td>
    </tr>
</table>
<br>
<table class="death-alteration-table-style02" style=" margin-top:0px;width:100%;" cellpadding="0" cellpadding="2px"
       cellspacing="0">
    <caption></caption>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <tbody>
    <tr>
        <td colspan="2">සහතිකයේ සඳහන් පුද්ගලයාගේ අනන්‍යතා අංකය <br>
            தனிநபர்அடையாள எண் <br>
            Person Identification Number (PIN) stated in the Certificate
        </td>
        <td>
             <s:property value="deathRegister.deathPerson.deathPersonPINorNIC"/>
        </td>
        <td>සහතික පත්‍රයේ අංකය <br>
            சான்றிதழ் இல <br>
            Certificate Number
        </td>
        <td>
        <s:property value="deathRegister.idUKey"/>
        </td>
    </tr>
    <tr>
        <td>දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td><s:label name="districtName"/></td>
        <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரிவு <br>
            Divisional Secretariat
        </td>
        <td colspan="2"><s:label name="dsDivisionName"/></td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
            பிரிவு <br>
            Registration Division
        </td>
        <td><s:label name="bdDivisionName"/></td>
        <td>ලියාපදිංචි කිරීමේ අංකය <br>
            சான்றிதழ் இல <br>
            Registration Number
        </td>
        <td colspan="2"><s:label name="serialNo"/></td>
    </tr>
    </tbody>
</table>
<br>
<table class="death-alteration-table-style01" style="width:1030px;border-top:50px">
    <tr>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;">
            <s:label value="ඇතුලත් කිරීම්, වෙනස් කිරීම් සහ ඉවත් කිරීම් තිබෙන තීරු අංක පහත සඳහන් කරන්න "/> <br>
            <s:label value="in Tamil"/> <br>
            <s:label value="Specify cage numbers for Insertions, Alterations and Omissions below"/>
        </td>
    </tr>
</table>
<br>
<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;" cellpadding="2px">
    <caption></caption>
    <col width="350px">
    <col>
    <tr>
        <td>
            (7)ඇතුලත් කිරීම් සහිත තීරු අංක / in tamil
            <br>
            Cage numbers for insertions
        </td>
        <td></td>
    </tr>
    <tr>
        <td>
            (8)වෙනස් කිරීම් සහිත තීරු අංක / in tamil
            <br>
            Cage numbers for Alterations
        </td>
        <td></td>
    </tr>
    <tr>
        <td>
            (9)ඉවත් කල යුතු තීරු අංක / in tamil
            <br>
            Cage numbers to be Deleted
        </td>
        <td></td>
    </tr>
</table>
<br>
<table class="death-alteration-table-style01" style="width:1030px;border-top:50px">
    <tr>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;">
            <s:label value="මරණය පිලිබඳ විස්තර"/> <br>
            <s:label value="பிள்ளை பற்றிய தகவல்"/> <br>
            <s:label value="Information about the Death"/>
        </td>
    </tr>
</table>
<br>
<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;" cellpadding="2px">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td>
            (10)හදිසි මරණයක්ද ? <br>
            in tamil <br>
            Sudden death?
        </td>
        <td colspan="2">
            ඔව් (53 වගන්තිය) <br>
            xx <br>
            Yes (Section 53)
        </td>
        <td>xxxxxxx</td>
        <td>
            නැත (52 (1) වගන්තිය) <br>
            xx <br>
            No (Section 52 (1))
        </td>
        <td>xxxxxxx</td>
    </tr>
    <tr>
        <td colspan="2">
            (11)මරණය සිදු වූ දිනය <br>
            பிறந்த திகதி <br>
            Date of Death
        </td>
        <td colspan="2"></td>
        <td>
            වෙලාව <br>
            in tamil<br>
            Time
        </td>
        <td></td>
    </tr>
    <tr>
        <td rowspan="2">
            (12)මරණය සිදු වූ ස්ථානය <br>
            பிறந்த இடம் <br>
            Place of Death
        </td>
        <td colspan="2">
            සිංහල හෝ දෙමළ භාෂාවෙන් <br>
            சிங்களம் தமிழ் <br>
            In Sinhala or Tamil
        </td>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td colspan="2">
            ඉංග්‍රීසි භාෂාවෙන් <br>
            in tamil <br>
            In English
        </td>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td>(13)මරණයට හේතුව තහවුරුද? <br>
            in tamil <br>
            Cause of death established?
        </td>
        <td colspan="2">
            නැත / xx / No
        </td>
        <td></td>
        <td>ඔව් / xx /Yes</td>
        <td></td>
    </tr>
    <tr>
        <td>(14)මරණයට හේතුව <br>
            in tamil <br>
            Cause of death
        </td>
        <td colspan="3"></td>
        <td>
            (15)හේතුවේ ICD කේත අංකය <br>
            in tamil <br>
            ICD Code of cause
        </td>
        <td></td>
    </tr>
    <tr>
        <td>(16)ආදාහන හෝ භූමදාන ස්ථානය <br>
            in tamil <br>
            Place of burial or cremation
        </td>
        <td colspan="5"></td>
    </tr>
    </tbody>
</table>

<br>
<table class="death-alteration-table-style01" style="width:1030px;border-top:50px">
    <tr>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;">
            <s:label value="මරණයට පත් වූ පුද්ගලයාගේ විස්තර"/> <br>
            <s:label value="பிள்ளை பற்றிய தகவல்"/> <br>
            <s:label value="Information about the person Departed"/>
        </td>
    </tr>
</table>
<br>
<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;" cellpadding="2px">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td rowspan="2">
            (17)අනන්‍යතා අංකය <br>
            தனிநபர் அடையாள எண் <br>
            Identification Number
        </td>
        <td colspan="3" rowspan="2"></td>
        <td rowspan="2">
            (18)විදේශිකය‍කු නම් <br>
            வெளிநாட்டவர் <br>
            If a foreigner
        </td>
        <td>
            රට <br>
            நாடு <br>
            Country
        </td>
        <td>bbbbbbbbbbbbbbbbbbbb</td>
    </tr>
    <tr>
        <td>
            ගමන් බලපත්‍ර අංකය <br>
            கடவுச் சீட்டு <br>
            Passport No.
        </td>
        <td>bbbbbbbbbbbbbbbbb</td>
    </tr>
    <tr>
        <td>
            (19)වයස හෝ අනුමාන වයස <br>
            பிறப்ப <br>
            Age or probable Age
        </td>
        <td>vvvvvvvvvvvvvvvv</td>
        <td>
            (20)ස්ත්‍රී පුරුෂ භාවය <br>
            பால் <br>
            Gender
        </td>
        <td>vvvvvvvvvvvvvvvv</td>
        <td>
            (21)ජාතිය <br>
            பிறப் <br>
            Race
        </td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td>
            (22)නම රාජ්‍ය භාෂාවෙන් <br>
            (සිංහල / දෙමළ)
            பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்) <br>
            Name in either of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="6">xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</td>
    </tr>
    <tr>
        <td>
            (23)නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பிறப்பு அத்தாட்சி ….. <br>
            Name in English
        </td>
        <td colspan="6">xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</td>
    </tr>
    <tr>
        <td>
            (24)ස්ථිර ලිපිනය <br>
            தாயின் நிரந்தர வதிவிட முகவரி <br>
            Permanent Address
        </td>
        <td colspan="6">xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</td>
    </tr>
    <tr>
        <td>
            (25)පියාගේ අනන්‍යතා අංකය <br>
            தனிநபர் அடையாள எண் <br>
            Fathers Identification Number
        </td>
        <td colspan="6">xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</td>
    </tr>
    <tr>
        <td>
            (26)පියාගේ සම්පුර්ණ නම <br>
            in tamil <br>
            Fathers full name
        </td>
        <td colspan="6">xxxxxxxxxxxxxxxxxxxxxxxxx</td>
    </tr>
    <tr>
        <td>
            (27)මවගේ අනන්‍යතා අංකය <br>
            தனிநபர் அடையாள எண் <br>
            Mothers Identification Number
        </td>
        <td colspan="6">xxxxxxxxxxxxxxxxxxxxxxxxxx</td>

    </tr>
    <tr>
        <td>
            (28)මවගේ සම්පුර්ණ නම <br>
            in tamil <br>
            Mothers full name
        </td>
        <td colspan="6">xxxxxxxxxxxxxxxxxxxxxxxxxxxxx</td>
    </tr>
    </tbody>
</table>
<br>
<table class="death-alteration-table-style01" style=" margin-top:20px;width:100%;" cellpadding="0"
       cellspacing="0">
    <tr>
        <td colspan="8" style="text-align:center;font-size:12pt;width:90%;border-right:none">
            දෝෂය හා එය සිදුවූ අන්දම පිලිබඳ ලුහුඬු විස්තර<br>
            தாத்தாவின் / பாட்டனின் விபரங்கள்<br>
            Nature of the error and a brief explanation of how the error occurred
        </td>
        <td style="border-right:none"></td>
        <td></td>
    </tr>
</table>
<br>
<table class="death-alteration-table-style02" style=" margin-top:20px;width:100%;" cellpadding="0" cellpadding="2px"
       cellspacing="0">
    <tr>
        <td colspan="2" style="text-align:center;font-size:12pt">
            ප්‍රකාශය සනාත කිරීමට ඇති ලේඛනගත හෝ වෙනත් සාක්ෂිවල ස්වභාවය<br>
            தாத்தாவின் / பாட்டனின் விபரங்கள் <br>
            Nature of documentary or other evidence in support of the declaration
        </td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="bcOfFather"/></td>
        <td style="width:90%"><s:label
                value=" පියාගේ උප්පැන්න සහතිකය  / in Tamil / Fathers Birth Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="bcOfMother"/></td>
        <td style="width:90%"><s:label
                value="මවගේ උප්පැන්න සහතිකය / in Tamil / Mothers Birth Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="mcOfParents"/></td>
        <td style="width:90%"><s:label
                value=" මව්පියන්ගේ විවාහ සහතිකය / in Tamil / Parents Marriage Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td colspan="2"><s:textarea name="otherDocuments"/></td>
    </tr>
</table>
<br>
<table class="death-alteration-table-style01" style="width:1030px;border-top:50px">
    <tr>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;">
            <s:label value="ප්‍රකාශය කරන්නාගේ විස්තර"/> <br>
            <s:label value="அறிவிப்பு கொடுப்பவரின் தகவல்கள்"/> <br>
            <s:label value="Details of the Declarant"/>
        </td>
    </tr>
</table>
<br>
<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;" cellpadding="2px">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td rowspan="2">
            (30)දැනුම් දෙන්නේ කවරකු වශයෙන්ද <br>
            in tamil <br>
            Capacity for giving information
        </td>
        <td>
            පියා / මව <br>
            in tamil <br>
            Father / Mother
        </td>
        <td>ssssssssssss</td>
        <td>
            ස්වාමිපුරුෂයා / භාර්යාව <br>
            in tamil <br>
            Husband / Wife
        </td>
        <td>sssssssss</td>
        <td>
            සහෝදරයා සහෝදරිය <br>
            in tamil <br>
            Brother / Sister
        </td>
        <td>ssssssssssssss</td>
    </tr>
    <tr>
        <td>
            පුත්‍රයා / දියණිය <br>
            in tamil <br>
            Son / Daughter
        </td>
        <td>ssssssssss</td>
        <td>
            නෑයන් <br>
            பாதுகாவலர் <br>
            Relative
        </td>
        <td>ssssssss</td>
        <td>
            වෙනත් <br>
            in tamil <br>
            Other
        </td>
        <td>sssssss</td>
    </tr>
    <tr>
        <td colspan="3">
            (31)අනන්‍යතා අංකය <br>
            தனிநபர் அடையாள எண் <br>
            Identification Number
        </td>
        <td colspan="4">ddddddddd</td>
    </tr>
    <tr>
        <td>
            (32)නම <br>
            கொடுப்பவரின் பெயர் <br>
            Name
        </td>
        <td colspan="6">bbbbbbbbbbbbbbb</td>
    </tr>
    <tr>
        <td>
            (33)තැපැල් ලිපිනය <br>
            தபால் முகவரி <br>
            Postal Address
        </td>
        <td colspan="6">bbbbbbbbbbbbbb</td>
    </tr>
    <tr>
        <td>(34)ඇමතුම් විස්තර <br>
            இலக்க வகை <br>
            Contact Details
        </td>
        <td>
            දුරකතනය <br>
            தொலைபேசி இலக்கம் <br>
            Telephone
        </td>
        <td colspan="2">vvvv</td>
        <td>
            ඉ -තැපැල <br>
            மின்னஞ்சல் <br>
            Email
        </td>
        <td colspan="2">vvvv</td>
    </tr>
    </tbody>
</table>
<div class="form-submit">
    <s:submit value="%{getText('save.label')}"/>
</div>
<s:hidden name="pageNumber" value="1"/>
</s:form>
</div>
