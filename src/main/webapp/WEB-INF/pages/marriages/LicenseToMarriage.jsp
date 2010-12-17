<%@ page import="lk.rgd.common.util.CivilStatusUtil" %>
<%@ page import="lk.rgd.prs.api.domain.Person" %>
<%@ page import="lk.rgd.common.util.MarriageTypeUtil" %>
<%@ page import="lk.rgd.crs.web.util.MarriageType" %>
<%--
@author amith jayasekara
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--section logo and issue date and serial number--%>
<div class="marriage-notice-outer">
<div style="width:45%;float:left;margin-top:5px;" id="locationSignId">
    <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
        <legend><b><s:label value="%{getText('selectoption.label')}"/></b></legend>
        <table>
            <tr>
                <td>
                    <s:label value="%{getText('placeOfIssue.label')}"/>
                </td>
                <td>

                    <s:select id="locationId" name="locationId" list="locationList" cssStyle="width:300px;"/>

                </td>
            </tr>
            <tr>
                <td><s:label value="%{getText('signOfficer.label')}"/></td>
                <td>
                    <s:select id="issueUserId" name="issueUserId" list="userList" cssStyle="width:300px;"/>
                </td>
            </tr>
        </table>
    </fieldset>
</div>

<table>
    <caption/>
    <col width="450px"/>
    <col width="230px"/>
    <col/>
    <tbody>
    <tr>
        <td colspan="3" align="right">
            <div class="form-submit">
                <s:form action="eprMarkLicenseAsPrinted.do" method="post">
                    <s:submit value="%{getText('button.mark.as.print')}"/>

                </s:form>
            </div>
            <div class="form-submit">
                <s:submit value="%{getText('button..print')}"/>
            </div>
            <div class="form-submit">
                <s:submit value="%{getText('button.back')}"/>
            </div>
        </td>
    </tr>
    <tr>
        <td></td>
        <td><img src="<s:url value="/images/official-logo.png"/>"/></td>
        <td>
            <table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;"
                   cellpadding="2px">
                <caption/>
                <col width="150px"/>
                <col width="300px"/>
                <tr>
                    <td>අනුක්‍රමික අංකය <br>
                        தொடர் இலக்கம் <br>
                        Serial Number
                    </td>
                    <td>
                        <s:label value="%{marriage.idUKey}"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        නිකුත් කල දිනය<br>
                        பெறப்பட்ட திகதி <br>
                        Date of Issue
                    </td>
                    <td>
                        <s:label value="%{dateOfIssueLicense}"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr style="height:80px">
        <td colspan="3" align="center">
            විවාහ වීමට බලපත්‍රය / குடிமதிப்பீட்டு ஆவணத்தில் / License for Marriage
    </tr>
    </tbody>
</table>

<%--section end date and type--%>
<table border="1" style="margin-top:25px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="257px"/>
    <col width="258px"/>
    <col width="257px"/>
    <col width="258px"/>
    <tr>
        <td colspan="1">
            විවාහ වීමට වලංගු වන අවසාන දිනය <br>
            in tamil <br>
            Last date for Marriage for which this license is valid
        </td>
        <td colspan="3">
            <s:label value="%{dateOfCancelLicense}"/>
        </td>
    </tr>
    <tr>
        <td>
            විවාහයේ ස්වභාවය <br>
            Type of Marriage in tamil<br>
            Type of Marriage
        </td>
        <td>                                                                                          <%--todo remove HC language--%>
            <%=MarriageTypeUtil.getMarriageTypeInOfficialLanguageAndEnglish((MarriageType)
                    request.getAttribute("marriage.typeOfMarriage"), "si")%>
            <br>
            <%=MarriageTypeUtil.getMarriageTypeInOfficialLanguageAndEnglish((MarriageType)
                    request.getAttribute("marriage.typeOfMarriage"), "en")%>
        </td>
        <td>
            විවාහය සිදුකරන ස්ථානය<br>
            in tamil <br>
            Place of Marriage
        </td>
        <td>

        </td>
    </tr>
</table>

<%--section details of the marriage--%>
<table border="1" style="margin-top:15px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="230px"/>
    <col width="225px"/>
    <col width="225px"/>
    <col width="225px"/>
    <col width="225px"/>
    <tr>
        <td></td>
        <td colspan="2" align="center">
            පුරුෂ පාර්ශ්වය / Male Party
        </td>
        <td colspan="2" align="center">
            ස්ත්‍රී පාර්ශ්වය / Female Party
        </td>
    </tr>
    <tr>
        <td>
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification number
        </td>
        <td colspan="2" align="center">
            <s:label value="%{marriage.male.identificationNumberMale}"/>
        </td>
        <td colspan="2" align="center">
            <s:label value="%{marriage.female.identificationNumberFemale}"/>
        </td>
    </tr>
    <tr>
        <td>
            උපන් දිනය සහ වයස <br>
            பிறந்த திகதி <br>
            Date of Birth and Age
        </td>
        <td align="center">
            <s:label value="%{marriage.male.dateOfBirthMale}"/>
        </td>
        <td align="center">
            <s:label value="%{marriage.male.ageAtLastBirthDayMale}"/>
        </td>
        <td align="center">
            <s:label value="%{marriage.female.dateOfBirthFemale}"/>
        </td>
        <td align="center">
            <s:label value="%{marriage.female.ageAtLastBirthDayFemale}"/>
        </td>
    </tr>
    <tr>
        <td>
            සිවිල් තත්වය සහ ජාතිය <br>
            சிவில் நிலைமை <br>
            Civil Status & Race
        </td>
        <td align="center">
            <%=CivilStatusUtil.getCivilStatus((Person.CivilStatus)
                    request.getAttribute("marriage.male.civilStatusMale"), "si")%>
        </td>
        <td align="left">
            <s:label value="%{marriage.male.maleRace.raceId}"/>
        </td>
        <td align="center">
            <%=CivilStatusUtil.getCivilStatus((Person.CivilStatus) request.
                    getAttribute("marriage.female.civilStatusFemale"), "si")%>
        </td>
        <td align="left">
            <s:label value="%{marriage.female.femaleRace.raceId}"/>
        </td>
    </tr>
    <tr style="height:100px">
        <td>
            සම්පුර්ණ නම <br>
            in tamil <br>
            Full Name
        </td>

        <td colspan="2" align="left">
            <s:label value="%{marriage.male.nameInOfficialLanguageMale}"/>
            <br>
            <br>
            <s:label value="%{marriage.male.nameInEnglishMale}"/>
        </td>
        <td colspan="2" align="left">
            <s:label value="%{marriage.female.nameInOfficialLanguageFemale}"/>
            <br>
            <br>
            <s:label value="%{marriage.female.nameInEnglishFemale}"/>
        </td>
    </tr>
    <tr>
        <td>
            තරාතිරම නොහොත් රක්ෂාව <br>
            in tamil <br>
            Rank or Profession
        </td>
        <td colspan="2" align="left">
            <s:label value="%{marriage.male.rankOrProfessionMaleInOfficialLang}"/>
            <br>
            <s:label value="%{marriage.male.rankOrProfessionMaleInEnglish}"/>
        </td>
        <td colspan="2" align="left">
            <s:label value="%{marriage.female.rankOrProfessionFemaleInOfficialLang}"/>
            <br>
            <s:label value="%{marriage.female.rankOrProfessionFemaleInEnglish}"/>
        </td>
    </tr>
    <tr>
        <td>
            පදිංචි ස්ථානය <br>
            in tamil <br>
            Place of Residence
        </td>
        <td colspan="2" align="left">
            <s:label value="%{marriage.male.residentAddressMaleInOfficialLang}"/>
            <br>
            <s:label value="%{marriage.male.residentAddressMaleInEnglish}"/>
        </td>
        <td colspan="2" align="left">
            <s:label value="%{marriage.female.residentAddressFemaleInOfficialLang}"/>
            <br>
            <s:label value="%{marriage.female.residentAddressFemaleInEnglish}"/>
        </td>
    </tr>
    <tr>
        <td>
            පියාගේ අනන්‍යතා අංකය <br>
            தந்தையின் அடையாள எண் <br>
            Fathers Identification Number
        </td>
        <td colspan="2" align="center">
            <s:label value="%{marriage.male.fatherIdentificationNumberMale}"/>
        </td>
        <td colspan="2" align="center">
            <s:label value="%{marriage.female.fatherIdentificationNumberFemale}"/>
        </td>
    </tr>
    <tr style="height:100px">
        <td>
            පියාගේ සම්පුර්ණ නම <br>
            தந்தையின் அடையாள <br>
            Fathers full name
        </td>
        <td colspan="2" align="left">
            <s:label value="%{marriage.male.fatherFullNameMaleInOfficialLang}"/>
            <br>
            <br>
            <s:label value="%{marriage.male.fatherFullNameMaleInEnglish}"/>
        </td>
        <td colspan="2" align="left">
            <s:label value="%{marriage.female.fatherFullNameFemaleInOfficialLang}"/>
            <br>
            <br>
            <s:label value="%{marriage.female.fatherFullNameFemaleInEnglish}"/>
        </td>
    </tr>

</table>

<%--section who authorized--%>
<table border="1" style="margin-top:15px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="230px"/>
    <col width="225px"/>
    <col width="225px"/>
    <col width="225px"/>
    <col width="225px"/>
    <tr>
        <td colspan="1">
            සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන <br>
            சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம் <br>
            Name, Signature and Designation of certifying officer
        </td>
        <td colspan="4"></td>
    </tr>
    <tr>
        <td colspan="1">
            නිකුත් කළ ස්ථානය <br>
            வழங்கிய இடம் <br>
            Place of Issue
        </td>
        <td colspan="4"></td>
    </tr>
    <tr>
        <td colspan="1">
            දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td colspan="1"></td>
        <td colspan="1">
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரதேச செயளாளர் பிரிவு <br>
            Divisional Secretariat <br>
        </td>
        <td colspan="2"></td>
    </tr>
</table>
</div>