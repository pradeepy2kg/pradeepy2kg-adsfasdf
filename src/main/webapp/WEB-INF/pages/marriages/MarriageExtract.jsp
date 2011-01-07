<%-- @author Mahesha Kalpanie --%>
<%@ page import="lk.rgd.prs.api.domain.Person" %>
<%@ page import="lk.rgd.common.util.CivilStatusUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<style type="text/css">
    #marriage-extract-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }

        #locationSignId {
            display: none;
        }
    }
</style>
<div class="marriage-extract-outer">
<table class="table_reg_header_01">
    <caption></caption>
    <col width="420px"/>
    <col width="200px"/>
    <col/>
    <tbody>
    <s:form action="eprMarkMarriageExtractAsPrinted.do" method="post">
    <tr>
        <td colspan="3">
            <div style="width:45%;float:left;margin-top:5px;" id="locationSignId">
                <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
                    <legend><b><s:label value="%{getText('selectoption.label')}"/></b></legend>
                    <table>
                        <tr>
                            <td>
                                <s:label value="%{getText('placeOfIssue.label')}"/>
                            </td>
                            <td>

                                <s:select id="locationId" name="licensePrintedLocationId" list="locationList"
                                          cssStyle="width:300px;"/>

                            </td>
                        </tr>
                        <tr>
                            <td><s:label value="%{getText('signOfficer.label')}"/></td>
                            <td>
                                <s:select id="issueUserId" name="licenseIssuedUserId" list="userList"
                                          cssStyle="width:300px;"/>
                            </td>
                        </tr>
                    </table>
                </fieldset>
            </div>
        </td>
    </tr>
    <tr>
        <td colspan="3" align="right">
            <div class="form-submit">
                <s:submit value="%{getText('button.mark.as.print')}"/>
                <s:hidden name="idUKey" value="%{marriage.idUKey}"/>
                </s:form>
            </div>
            <div class="form-submit">
                <s:submit value="%{getText('button.print')}" onclick="printPage()"/>
            </div>
            <div class="form-submit">
                <s:submit value="%{getText('button.back')}"/>
            </div>
        </td>
    </tr>

    <tr style="font-size:9pt">
        <td colspan="1">&nbsp;</td>
        <td align="center" style="font-size:12pt;"><img src="<s:url value="/images/official-logo.png"/>"</td>
        <td> &nbsp;</td>
    </tr>
    <tr>
        <td colspan="3">&nbsp;</td>
    </tr>
    <tr style="font-size:14pt">
        <td colspan="3" align="center">
            ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA
        </td>
    </tr>
    <tr style="font-size:14pt">
        <td colspan="3" align="center">
            විවාහ ලේඛනයේ උපුටාගැනීම / குடிமதிப்பீட்டு ஆவணத்தில் / Extract of Marriage Register
        </td>
    </tr>
    </tbody>
</table>
<table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="175px"/>
    <col/>
    <col/>
    <col/>
    <tr>
        <td>
            දිස්ත්‍රික්කය
            <br>மாவட்டம்
            <br>District
        </td>
        <td>
            <s:if test="marriage.preferredLanguage=='si'">
                <s:label name="marriage.mrDivision.dsDivision.district.siDistrictName" cssStyle="font-size:11pt;"/>
            </s:if>
            <s:elseif test="marriage.preferredLanguage=='ta'">
                <s:label name="marriage.mrDivision.dsDivision.district.taDistrictName" cssStyle="font-size:11pt;"/>
            </s:elseif>
            <br/>
            <s:label name="marriage.mrDivision.dsDivision.district.enDistrictName"/><br/>
        </td>
        <td><label><span class="font-8">
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය
                <br>பிரதேச செயளாளர் பிரிவு <br>Divisional Secretariat</span>
        </label>
        </td>
        <td align="center">
            <s:if test="marriage.preferredLanguage=='si'">
                <s:label name="marriage.mrDivision.dsDivision.siDivisionName" cssStyle="font-size:11pt;"/>
            </s:if>
            <s:elseif test="marriage.preferredLanguage=='ta'">
                <s:label name="marriage.mrDivision.dsDivision.taDivisionName" cssStyle="font-size:11pt;"/>
            </s:elseif>
            <br/>
            <s:label name="marriage.mrDivision.dsDivision.enDivisionName"/><br/>
        </td>
    </tr>
    <tr>
        <td><label><span class="font-8">
            ලියාපදිංචි කිරීමේ කොට්ඨාශය
                        <br>பதிவுப் பிரிவு  <br>Registration Division</span>
        </label>
        </td>
        <td colspan="3">
            <s:if test="marriage.preferredLanguage=='si'">
                <s:label name="marriage.mrDivision.siDivisionName" cssStyle="font-size:11pt;"/>
            </s:if>
            <s:elseif test="marriage.preferredLanguage=='ta'">
                <s:label name="marriage.mrDivision.taDivisionName" cssStyle="font-size:11pt;"/>
            </s:elseif>
            <br/>
            <s:label name="marriage.mrDivision.enDivisionName"/><br/>
        </td>
    </tr>
</table>

<table class="table_reg_header_01">
    <caption></caption>
    <col width="420px"/>
    <col width="200px"/>
    <col/>
    <tbody>
    <tr>
        <td colspan="3">&nbsp;</td>
    </tr>
    <tr style="font-size:14pt">
        <td colspan="3" align="center">
            විවාහ විස්තර / in tamil / Details of Marriage
        </td>
    </tr>
    </tbody>
</table>
<table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="5px">
    <caption></caption>
    <col width="200px"/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td>
            <label>
                <span class="font-8">විවාහ දිනය
                    <br>in tamil<br>Date of Marriage</span>
            </label>
        </td>
        <td colspan="2">
            <s:label name="marriage.dateOfMarriage"/>
        </td>
        <td colspan="3"><label><span class="font-8">රෙජිස්ට්‍රාර්ගේ/දේවගැතිගේ අනන්‍යතා අංකය
                    <br>அடையாள எண் <br>Identification number of Registrar/Minister</span></label>
        </td>
        <td align="center" colspan="3">
            <s:label name="marriage.registrarOrMinisterPIN"/>
        </td>
    </tr>
    <tr>
        <td>
            විවාහ ස්ථානයේ ස්වභාවය<br>
            Type of Marriage Place<br>
        </td>
        <td colspan="8">
            <s:if test="marriage.preferredLanguage=='si'">
                <s:label name="marriage.typeOfMarriagePlace.siType"/>
            </s:if>
            <s:elseif test="marriage.preferredLanguage=='ta'">
                <s:label name="marriage.typeOfMarriagePlace.taType"/>
            </s:elseif>
            <s:label name="marriage.typeOfMarriagePlace.enType"/>
        </td>
    </tr>
    <tr>
        <td>
            විවාහය සිදු කල ස්ථානය<br>
            in tamil <br>
            place of Marriage
        </td>
        <td colspan="8">
            <table width="100%">
                <caption/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:label name="marriage.regPlaceInOfficialLang"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:label name="marriage.regPlaceInEnglishLang"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>

    <tr>
        <td>
            රෙජිස්ට්‍රාර්තැන / දේවගැතිතැන<br>
            in tamil <br>
            Registrar / Minister
        </td>
        <td colspan="8">
            <table width="100%">
                <caption/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:label name="marriage.regNameInOfficialLang"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:label name="marriage.regNameInEnglishLang"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>

    <tr>
        <td>
            විවාහයේ ස්වභාවය <br>
            type of marriage in tamil <br>
            Type of Marriage
        </td>
        <td colspan="8">
            <table width="100%">
                <caption/>
                <col/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:if test="marriage.preferredLanguage=='si'">
                            <s:label name="marriage.typeOfMarriage.siType"/>
                        </s:if>
                        <s:elseif test="marriage.preferredLanguage=='ta'">
                            <s:label name="marriage.typeOfMarriage.taType"/>
                        </s:elseif>
                        <s:label name="marriage.typeOfMarriage.enType"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>

<%--section heading Male/Female details --%>
<table style="margin-top:20px;margin-bottom:20px;width:100%;font-size:16px">
    <caption/>
    <tbody>
    <tr>
        <td></td>
        <td align="center">
            පුරුෂ පාර්ශ්වය / in tamil / Male Party
        </td>
        <td align="center">
            ස්ත්‍රී පාර්ශ්වය / in tamil / Female Party
        </td>
    </tr>
    </tbody>
</table>

<table border="2"
       style="margin-top:10px;margin-bottom:20px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="5px">
    <caption/>
    <col width="200px"/>
    <col width="410px"/>
    <col/>
    <tbody>
    <tr>
        <td colspan="1">
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification Number.
        </td>
        <td colspan="1" align="left">
            <s:label name="marriage.male.identificationNumberMale"/>
        </td>
        <td colspan="1" align="left">
            <s:label name="marriage.female.identificationNumberFemale"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            උපන් දිනය <br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td colspan="1">
            <s:label name="marriage.male.dateOfBirthMale"/>
        </td>
        <td colspan="1">
            <s:label name="marriage.female.dateOfBirthFemale"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            පසුවූ උපන් දිනයට වයස <br>
            in tamil <br>
            Age at last Birthday

        </td>
        <td colspan="1">
            <s:label name="marriage.male.ageAtLastBirthDayMale"/>
        </td>
        <td colspan="1">
            <s:label name="marriage.female.ageAtLastBirthDayFemale"/>
        </td>
    </tr>
    <tr>
        <td>
            ජාතිය <br>
            Race <br>

        </td>
        <td>
            <s:if test="marriage.preferredLanguage=='si'">
                <s:label name="marriage.male.maleRace.siRaceName" cssStyle="font-size:11pt;"/>
            </s:if>
            <s:elseif test="marriage.preferredLanguage=='ta'">
                <s:label name="marriage.male.maleRace.taRaceName" cssStyle="font-size:11pt;"/>
            </s:elseif>
            <br/>
            <s:label name="marriage.male.maleRace.enRaceName"/><br/>
        </td>
        <td>
            <s:if test="marriage.preferredLanguage=='si'">
                <s:label name="marriage.female.femaleRace.siRaceName" cssStyle="font-size:11pt;"/>
            </s:if>
            <s:elseif test="marriage.preferredLanguage=='ta'">
                <s:label name="marriage.female.femaleRace.taRaceName" cssStyle="font-size:11pt;"/>
            </s:elseif>
            <br/>
            <s:label name="marriage.female.femaleRace.enRaceName"/><br/>
        </td>
    </tr>
    <tr>
        <td>
            සිවිල් තත්වය<br>
            சிவில் நிலைமை <br>
            Civil Status
        </td>
        <td>
            <table>
                <caption/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:if test="marriage.preferredLanguage=='si'">
                            <%=CivilStatusUtil.getCivilStatus((Person.CivilStatus) request.getAttribute("marriage.male.civilStatusMale"), "si")%>
                        </s:if>
                        <s:elseif test="marriage.preferredLanguage=='ta'">
                            <%=CivilStatusUtil.getCivilStatus((Person.CivilStatus) request.getAttribute("marriage.male.civilStatusMale"), "ta")%>
                        </s:elseif>
                        <br/>
                        <%=CivilStatusUtil.getCivilStatus((Person.CivilStatus) request.getAttribute("marriage.male.civilStatusMale"), "en")%>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>

        <td>
            <table>
                <caption/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:if test="marriage.preferredLanguage=='si'">
                            <%=CivilStatusUtil.getCivilStatus((Person.CivilStatus) request.getAttribute("marriage.female.civilStatusFemale"), "si")%>
                        </s:if>
                        <s:elseif test="marriage.preferredLanguage=='ta'">
                            <%=CivilStatusUtil.getCivilStatus((Person.CivilStatus) request.getAttribute("marriage.female.civilStatusFemale"), "ta")%>
                        </s:elseif>
                        <br/>
                        <%=CivilStatusUtil.getCivilStatus((Person.CivilStatus) request.getAttribute("marriage.female.civilStatusFemale"), "en")%>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <br>
            பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td>
            <s:label name="marriage.male.nameInOfficialLanguageMale"/>
        </td>
        <td>
            <s:label name="marriage.female.nameInOfficialLanguageFemale"/>
        </td>
    </tr>

    <tr>
        <td>
            නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பெயர் ஆங்கில மொழியில் <br>
            Name in English
        </td>
        <td>
            <s:label name="marriage.male.nameInEnglishMale"/>
        </td>
        <td>
            <s:label name="marriage.female.nameInEnglishFemale"/>
        </td>
    </tr>

    <tr>
        <td>
            පදිංචි ලිපිනය <br>
            தற்போதைய வதிவிட முகவரி <br>
            Resident Address
        </td>
        <td>
            <s:label name="marriage.male.residentAddressMaleInOfficialLang"/>
        </td>
        <td>
            <s:label name="marriage.female.residentAddressFemaleInOfficialLang"/>
        </td>
    </tr>
    </tbody>
</table>
<table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="200px">
    <col>
    <col>
    <col>
    <tr>
        <td><label><span class="font-8">අනුක්‍රමික අංකය
                <br>தொடர் இலக்கம்<br>Serial Number</span></label>
        </td>
        <td align="center">
            <s:label name="marriage.idUKey"/>
        </td>
        <td>
            <label>
                    <span class="font-8">ලියාපදිංචි දිනය
                        <br>பெறப்பட்ட திகதி  <br>Date of Registration</span>
            </label>
        </td>
        <td>
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
            <s:label name="marriage.registrationDate"/>
        </td>
    </tr>
</table>
<div class="form-submit">
</div>
</div>