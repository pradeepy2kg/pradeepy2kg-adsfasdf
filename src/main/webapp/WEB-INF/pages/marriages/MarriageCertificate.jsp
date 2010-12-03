<%--
@author amith
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="marriage-certificate-outer">
<%--section loag and official usage--%>
<table style="width: 100%">
    <caption/>
    <col width="400px"/>
    <col width="230px"/>
    <col/>
    <tbody>
    <tr>
        <td>
            <%--section table 1 for details of registration--%>
            <table border="2"
                   style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
                   cellpadding="5px">
                <caption/>
                <col width="200px"/>
                <col/>
                <tbody>
                <tr>
                    <td>දිස්ත්‍රික්කය <br>
                        மாவட்டம் <br>
                        District
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td>
                        ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
                        பிரதேச செயளாளர் பிரிவு <br>
                        Divisional Secretariat
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td>
                        ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
                        பதிவுப் பிரிவு <br>
                        Registration Division
                    </td>
                    <td></td>
                </tr>
                </tbody>
            </table>
        </td>
        <td align="center">
            <%--logo goes here--%>
            <img src="<s:url value="/images/official-logo.png"/>"/>
        </td>
        <td>
            <div style="position:relative;bottom:30px">
                <%--section official usage here--%>
                <table border="2"
                       style="margin-top:0px;width:100%;height:100px;border:1px solid #000;border-collapse:collapse;font-size:12px"
                       cellpadding="5px">
                    <caption/>
                    <col width="200px"/>
                    <col/>
                    <tbody>
                    <tr>
                        <td>
                            අනුක්‍රමික අංකය <br>
                            தொடர் இலக்கம் <br>
                            Serial Number
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>
                            ලියාපදිංචි දිනය<br>
                            பெறப்பட்ட திகதி <br>
                            Date of Registration
                        </td>
                        <td></td>
                    </tr>
                    <%--                <tr>
                        <td> ලියාපදිංචි දිනය<br>
                            பெறப்பட்ட திகதி <br>
                            Date of Registration
                        </td>
                        <td></td>
                    </tr>--%>
                    </tbody>
                </table>
            </div>
        </td>
    </tr>
    <tr>
        <td colspan="3" align="center">
            <br><br>
            ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA <br>
            විවාහ සහතිකය / குடிமதிப்பீட்டு ஆவணத்தில் / Certificate of Marriage
        </td>
    </tr>
    </tbody>
</table>

<table border="2" style="margin-top:0px;width:100%;height:100px;border:1px solid #000;border-collapse:collapse;
    font-size:12px" cellpadding="5px">
    <caption/>
    <col width="200px"/>
    <col width="300px"/>
    <col width="250px"/>
    <col/>
    <tbody>
    <tr>
        <td>විවාහ දිනය <br>
            Date of Marriage <br>
        </td>
        <td></td>
        <td>රෙජිස්ට්‍රාර්ගේ/දේවගැතිගේ අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification number of Registrar/Minister
        </td>
        <td></td>
    </tr>
    <tr>
        <td>විවාහයේ ස්වභාවය <br>
            in tamil <br>
            Type of Marriage
        </td>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td>විවාහය සිදු කල ස්ථානය <br>
            in tamil <br>
            Place of Marriage
        </td>
        <td colspan="3"></td>
    </tr>
    </tbody>
</table>

<%--section marriage party details--%>
<table border="2" style="margin-top:50px;margin-bottom:50px;width:100%;height:100px;border:1px solid #000;border-collapse:collapse;
    font-size:12px" cellpadding="5px">
    <caption/>
    <col width="200px"/>
    <col width="200px"/>
    <col width="200px"/>
    <col width="200px"/>
    <col width="200px"/>
    <tbody>
    <tr>
        <td colspan="1"></td>
        <td colspan="2" align="center">පුරුෂ පාර්ශ්වය / Male Party</td>
        <td colspan="2" align="center">ස්ත්‍රී පාර්ශ්වය / Female Party</td>
    </tr>
    <tr>
        <td colspan="1">
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification number <br>
        </td>
        <td colspan="2"></td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td>
            උපන් දිනය සහ වයස <br>
            பிறந்த திகதி <br>
            Date of Birth and Age <br>
        </td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>
            සිවිල් තත්වය සහ ජාතිය <br>
            சிவில் நிலைமை <br>
            Civil Status & Race <br>
        </td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td colspan="1">
            සම්පුර්ණ නම රාජ්‍ය භාෂාවෙන් <br>
            in tamil <br>
            Full Name in Official Language <br>
        </td>
        <td colspan="2"></td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td colspan="1">
            සම්පුර්ණ නම ඉංග්‍රීසි භාෂාවෙන් <br>
            in tamil <br>
            Full Name in English Language <br>
        </td>
        <td colspan="2"></td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td colspan="1">
            පදිංචි ස්ථානය <br>
            in tamil <br>
            Place of Residence <br>
        </td>
        <td colspan="2"></td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td colspan="1">
            රෙජිස්ට්‍රාර්තැන / දේවගැතිතැන <br>
            in tamil <br>
            Registrar / Minister <br>
        </td>
        <td colspan="4"></td>
    </tr>
    </tbody>
</table>
<table style="width:100%">
    <caption/>
    <tbody>
    <tr>
        <td align="center">
            සාක්ෂිකරයන්ගේ විස්තර / in tamil / Details of Witnesses
        </td>
    </tr>
    </tbody>
</table>

<table border="2" style="margin-top:50px;margin-bottom:50px;width:100%;height:100px;border:1px solid #000;border-collapse:collapse;
    font-size:12px" cellpadding="5px">
    <caption></caption>
    <col width="30px"/>
    <col width="170px"/>
    <col width="276px"/>
    <col width="276px"/>
    <col/>
    <tbody>
    <tr>
        <td rowspan="3">(1)</td>
        <td>
            සම්පුර්ණ නම <br>
            in tamil <br>
            Name in full <br>
        </td>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td>
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification number <br>
        </td>
        <td></td>
        <td>
            රක්ෂාව නොහොත් තරාතිරම <br>
            <br>
            in tamil
            Rank or Profession <br>
        </td>
        <td></td>
    </tr>
    <tr>
        <td>
            පදිංචි ස්ථානය <br>
            in tamil <br>
            Residence <br>
        </td>
        <td colspan="3"></td>
    </tr>

    <tr>
        <td rowspan="3">(2)</td>
        <td>
            සම්පුර්ණ නම <br>
            in tamil <br>
            Name in full <br>
        </td>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td>
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification number <br>
        </td>
        <td></td>
        <td>
            රක්ෂාව නොහොත් තරාතිරම <br>
            <br>
            in tamil
            Rank or Profession <br>
        </td>
        <td></td>
    </tr>
    <tr>
        <td>
            පදිංචි ස්ථානය <br>
            in tamil <br>
            Residence <br>
        </td>
        <td colspan="3"></td>
    </tr>
    </tbody>
</table>
</div>