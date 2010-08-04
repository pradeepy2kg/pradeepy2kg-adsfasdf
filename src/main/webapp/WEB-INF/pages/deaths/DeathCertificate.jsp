<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="death-certificate-outer">
    <table style="width: 100%; border:none; border-collapse:collapse; ">
        <col width="200px">
        <col width="400px">
        <col width="200px">
        <tbody>
        <tr>
            <td rowspan="3"></td>
            <td rowspan="2" align="center">
                <img src="<s:url value="../images/official-logo.png" />"
                     style="display: block; text-align: center;" width="80" height="100">
            </td>
            <td>සහතික පත්‍රයේ අංකය <br>சான்றிதழ் இல <br>Certificate Number
            </td>
        </tr>
        <tr>
            <td><s:label name=""/></td>
        </tr>
        <tr>
            <td align="center">ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA <br>
                මරණ ලියාපදිංචි කිරීමේ ලේඛනය <br>பிறப்பு சான்றிதழ்﻿<br>REGISTER OF DEATHS
            </td>
            <td></td>
        </tr>
        </tbody>
    </table>

    <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; ">
        <col width="135px"/>
        <col/>
        <col width="145px"/>
        <col/>
        <tbody>
        <tr>
            <td>දිස්ත්‍රික්කය<br>மாவட்டம் <br>District</td>
            <td></td>
            <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය<br> பிரிவு <br> Divisional Secretariat</td>
            <td></td>
        </tr>
        <tr>
            <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය<br>பிரிவு<br>Registration Division</td>
            <td></td>
            <td>මුල් ලියාපදිංචියෙන් පසු වෙනස්කම්<br> நிறைவேற்றிய மாற்றங்கள்<br> Changes after first registration</td>
            <td></td>
        </tr>
        </tbody>
    </table>

    <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; ">
        <col width="150px"/>
        <col width="160px"/>
        <col width="100px"/>
        <col width="100px"/>
        <col width="150px"/>
        <col width="120px"/>
        <col width="100px"/>
        <col/>
        <tbody>
        <tr>
            <td>පුද්ගල අනන්‍යතා අංකය <br>தனிநபர்அடையாள எண் <br>Person Identification Number (PIN)</td>
            <td></td>
            <td>වයස<br>*in tamil<br>Age</td>
            <td></td>
            <td>මරණය සිදුවූ දිනය<br>பிறந்த திகதி<br>Date of death</td>
            <td></td>
            <td>ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender</td>
            <td></td>
        </tr>
        <tr>
            <td colspan="1">මරණය සිදුවූ ස්ථානය<br>*in tamil<br>Place of death</td>
            <td colspan="7"></td>
        </tr>
        <tr>
            <td colspan="1">මරණයට හේතුව<br>*in tamil<br>Cause of Death</td>
            <td colspan="5"></td>
            <td colspan="1">හේතුවේ ICD කේත අංකය<br>*in tamil<br>ICD Code of cause</td>
            <td colspan="1"></td>
        </tr>
        <tr>
            <td colspan="1">ආදාහන හෝ භූමදාන ස්ථානය<br>*in tamil<br>Place of burial or cremation</td>
            <td colspan="7"></td>
        </tr>
        <tr>
            <td colspan="1">නම <br>பெயர் <br>Name</td>
            <td colspan="7"></td>
        </tr>
        <tr>
            <td colspan="1">නම ඉංග්‍රීසි භාෂාවෙන් <br>ஆங்கிலத்தில் பெயர் <br> Name in English</td>
            <td colspan="7"></td>
        </tr>
        <tr>
            <td colspan="1">පියාගේ සම්පුර්ණ නම<br>தந்தையின்முழுப் பெயர் <br> Father's Full Name</td>
            <td colspan="5"></td>
            <td colspan="1">පු.අ.අ. හෝ ජා.හැ.අ.<br>*in tamil <br>PIN / NIC</td>
            <td colspan="1"></td>
        </tr>
        <tr>
            <td colspan="1">මවගේ සම්පූර්ණ නම<br> தாயின் முழுப் பெயர்<br> Mother's Full Name</td>
            <td colspan="5"></td>
            <td colspan="1">පු.අ.අ. හෝ ජා.හැ.අ.<br>*in tamil <br>PIN / NIC</td>
            <td colspan="1"></td>
        </tr>
        <tr>
            <td colspan="1">දැනුම් දෙන්නගේ නම<br>கொடுப்பவரின் பெயர்<br>Informant's Name</td>
            <td colspan="5"></td>
            <td colspan="1">පු.අ.අ. හෝ ජා.හැ.අ.<br>*in tamil <br>PIN / NIC</td>
            <td colspan="1"></td>
        </tr>
        <tr>
            <td colspan="1">දැනුම් ලිපිනය <br>கொடுப்பவரின் பெயர்<br>Informant's Address</td>
            <td colspan="7"></td>
        </tr>
        </tbody>
    </table>

    <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; ">
        <col width="195px"/>
        <col width="215px"/>
        <col width="120px"/>
        <col/>
        <tbody>
        <tr>
            <td>ලියාපදිංචි කළ දිනය<br>பதிவு செய்யப்பட்ட திகதி <br> Date of Registration</td>
            <td></td>
            <td>නිකුත් කළ දිනය<br>வழங்கிய திகதி <br> Date of Issue</td>
            <td></td>
        </tr>
        <tr>
            <td colspan="2">සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන <br>
                சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்<br>
                Name, Signature and Designation of certifying officer
            </td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td colspan="2">නිකුත් කළ ස්ථානය / வழங்கிய இடம் / Place of Issue</td>
            <td colspan="2"></td>
        </tr>
        </tbody>
    </table>

    <s:label>පු.අ.අ. හෝ ජා.හැ.අ. = අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය</s:label>
    <s:label><p>උප්පැන්න හා මරණ ලියපදිංචි කිරිමේ පණත (110 අධිකාරය) යටතේ රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව විසින්
        නිකුත් කරන
        ලදි.<br>
        பிறப்பு இறப்பு பதிவு செய்யும் சட்டத்தின்ப்புடி பதிவாளர் நாயகத் திணைக்களத்தினால் வழங்கப்பட்டது <br>
        Issued by Registrar General's Department according to Birth and Death Registration Act (110 Authority)</p>
    </s:label>
</div>