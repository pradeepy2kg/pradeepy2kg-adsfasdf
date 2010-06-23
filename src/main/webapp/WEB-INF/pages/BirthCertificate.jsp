<%-- @author Duminda Dharmakeerthi. --%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css">
    @media screen {
        #birth-certificate-outer {
            width: auto;
            height: auto;
            padding: 10px;
            background: #fff;
            font-size: 9pt;
            font-family: BhashitaComplex;
        }
    }

    @media print {
        #birth-certificate-outer {
            width: 210mm;
            height: 297mm;
            padding: 10px;
            background: #fff;
            font-size: 9pt;
            font-family: BhashitaComplex;
        }
    }

    #birth-certificate-outer table {
        font-size: 10pt;
        font-family: BhashitaComplex;
        margin-bottom: 10px;
    }

    #birth-certificate-outer p {
        font-size: 8pt;
        font-family: BhashitaComplex;
        text-align: center;
        margin-top: -3px;
    }

    .bc-name {
        font-size: 12pt;
        font-family: BhashitaComplex;
    }

    #birth-certificate-outer .table-with-border {
        width: 100%;
        border: 1px solid #000;
        border-collapse: collapse;
    }
</style>
<div id="birth-certificate-outer">

    <table style="width: 100%; border:none; border-collapse:collapse; ">
        <col width="200px">
        <col width="400px">
        <col width="200px">
        <tbody>
        <tr>
            <td rowspan="3"></td>
            <td rowspan="2" align="center">
                <img alt="Official Logo" src="<s:url value="images/official-logo.png" />"
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
                උප්පැන්න සහතික<br>
                பிறப்பு சான்றிதழ்﻿<br>
                BIRTH CERTIFICATE
            </td>
            <td></td>
        </tr>
        </tbody>
    </table>

    <table border="1" class="table-with-border">
        <col width="135px">
        <col>
        <col width="145px">
        <col>
        <tbody>
        <tr>
            <td>දිස්ත්‍රික්කය<br>மாவட்டம் <br>District
            </td>
            <td><s:label name=""/></td>
            <td>කොට්ඨාශය<br>பிரிவு <br>D.S. Division
            </td>
            <td><s:label name=""/></td>
        </tr>
        <tr>
            <td>කොට්ඨාශය<br>பிரிவு <br>Division
            </td>
            <td><s:label name=""/></td>
            <td>සහතිකයේ වෙනස්කම් <br>நிறைவேற்றிய மாற்றங்கள் <br>Changes performed
            </td>
            <td><s:label name=""/></td>
        </tr>
        </tbody>
    </table>

    <table border="1" class="table-with-border">
        <col width="150px">
        <col width="160px">
        <col width="100px">
        <col width="130px">
        <col width="100px">
        <col>
        <tbody>
        <tr>
            <td>පුද්ගල අනන්‍යතා අංකය <br>தனிநபர்அடையாள எண் <br>Person Identification Number (PIN)
            </td>
            <td><s:label name=""/></td>
            <td>උපන් දිනය <br>பிறந்த திகதி <br>Date of birth <br>YYYY-MM-DD
            </td>
            <td><s:label name=""/></td>
            <td>ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender
            </td>
            <td><s:label name=""/></td>
        </tr>
        <tr>
            <td>උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of birth
            </td>
            <td colspan="2"><s:label name=""/></td>
            <td colspan="2">මව්පියන් විවාහකද? <br>பெற்றோர் விவாகம் செய்தவர்களா? <br>Were Parents Married?
            </td>
            <td><s:label name=""/></td>
        </tr>
        <tr>
            <td>නම <br>பெயர் <br>Name
            </td>
            <td colspan="5" class="bc-name">
                අංගම්මන රන්පන්හිඳ සමරදිවාකර වික්‍රමසිංහ ඉලන්කෝන් සේනානායක රාජපක්ෂ
                රාජකරුනා වාසල මුදියන්සේ රාලහාමිල්ලාගේ ලක්ෂ්මන් බණ්ඩාර අංගම්මන<br>
                අංගම්මන රන්පන්හිඳ සමරදිවාකර වික්‍රමසිංහ ඉලන්කෝන් සේනානායක රාජපක්ෂ
                රාජකරුනා වාසල මුදියන්සේ රාලහාමිල්ලාගේ ලක්ෂ්මන් බණ්ඩාර අංගම්මන
            </td>
        </tr>
        <tr>
            <td>නම ඉංග්‍රීසි භාෂාවෙන් <br>ஆங்கிலத்தில் பெயர் <br> Name in English
            </td>
            <td colspan="5" class="bc-name">
                අංගම්මන රන්පන්හිඳ සමරදිවාකර වික්‍රමසිංහ ඉලන්කෝන් සේනානායක රාජපක්ෂ
                රාජකරුනා වාසල මුදියන්සේ රාලහාමිල්ලාගේ ලක්ෂ්මන් බණ්ඩාර අංගම්මන<br>
                අංගම්මන රන්පන්හිඳ සමරදිවාකර වික්‍රමසිංහ ඉලන්කෝන් සේනානායක රාජපක්ෂ
                රාජකරුනා වාසල මුදියන්සේ රාලහාමිල්ලාගේ ලක්ෂ්මන් බණ්ඩාර අංගම්මන
            </td>
        </tr>
        <tr>
            <td>පියාගේ අනන්‍යතා අංකය <br>தந்தையின் அடையாள எண் <br> Father's PIN
            </td>
            <td><s:label name=""/></td>
            <td>පියාගේ ජාතිය<br>தந்தையின் இனம் <br> Father's Race
            </td>
            <td colspan="3"><s:label name=""/></td>
        </tr>
        <tr>
            <td>පියාගේ සම්පුර්ණ නම<br>தந்தையின்முழுப் பெயர் <br> Father's Full Name
            </td>
            <td colspan="5" class="bc-name">
                අංගම්මන රන්පන්හිඳ සමරදිවාකර වික්‍රමසිංහ ඉලන්කෝන් සේනානායක රාජපක්ෂ
                රාජකරුනා වාසල මුදියන්සේ රාලහාමිල්ලාගේ ලක්ෂ්මන් බණ්ඩාර අංගම්මන<br>
                අංගම්මන රන්පන්හිඳ සමරදිවාකර වික්‍රමසිංහ ඉලන්කෝන් සේනානායක රාජපක්ෂ
                රාජකරුනා වාසල මුදියන්සේ රාලහාමිල්ලාගේ ලක්ෂ්මන් බණ්ඩාර අංගම්මන
            </td>
        </tr>
        <tr>
            <td>ම‌වගේ අනන්‍යතා අංකය <br>தாயின் அடையாள எண் <br> Mother's PIN
            </td>
            <td><s:label name=""/></td>
            <td>මවගේ ජාතිය<br>தாயின் இனம் <br> Mother's Race
            </td>
            <td colspan="3"><s:label name=""/></td>
        </tr>
        <tr>
            <td>මවගේ සම්පූර්ණ නම <br>தாயின் முழுப் பெயர் <br> Mother's Full Name
            </td>
            <td colspan="5" class="bc-name">
                අංගම්මන රන්පන්හිඳ සමරදිවාකර වික්‍රමසිංහ ඉලන්කෝන් සේනානායක රාජපක්ෂ
                රාජකරුනා වාසල මුදියන්සේ රාලහාමිල්ලාගේ ලක්ෂ්මන් බණ්ඩාර අංගම්මන<br>
                අංගම්මන රන්පන්හිඳ සමරදිවාකර වික්‍රමසිංහ ඉලන්කෝන් සේනානායක රාජපක්ෂ
                රාජකරුනා වාසල මුදියන්සේ රාලහාමිල්ලාගේ ලක්ෂ්මන් බණ්ඩාර අංගම්මන
            </td>
        </tr>
        </tbody>
    </table>

    <table border="1" class="table-with-border">
        <col width="195px">
        <col width="215px">
        <col width="120px">
        <col>
        <tbody>
        <tr>
            <td>ලියාපදිංචි කළ දිනය<br>பதிவு செய்யப்பட்ட திகதி <br> Date of Registration
            </td>
            <td><s:label name=""/></td>
            <td>නිකුත් කළ දිනය<br>வழங்கிய திகதி <br> Date of Issue
            </td>
            <td><s:label name=""/></td>
        </tr>
        <tr>
            <td colspan="2">
                සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන <br>
                சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்<br>
                Name, Signature and Designation of certifying officer
            </td>
            <td colspan="2"><s:label name=""/></td>
        </tr>
        <tr>
            <td colspan="2">නිකුත් කළ ස්ථානය / வழங்கிய இடம் / Place of Issue
            </td>
            <td colspan="2"><s:label name=""/></td>
        </tr>
        </tbody>
    </table>

    <p>උප්පැන්න හා මරණ ලියපදිංචි කිරිමේ පණත (110 අධිකාරය) යටතේ රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව විසින් නිකුත් කරන
        ලදි.<br>
        பிறப்பு இறப்பு பதிவு செய்யும் சட்டத்தின்ப்புடி பதிவாளர் நாயகத் திணைக்களத்தினால் வழங்கப்பட்டது <br>
        Issued by Registrar General's Department according to Birth and Death Registration Act (110 Authority)</p>

</div>