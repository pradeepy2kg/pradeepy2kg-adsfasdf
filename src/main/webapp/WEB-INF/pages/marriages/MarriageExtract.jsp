<%-- @author Mahesha Kalpanie --%>
<%@ page import="lk.rgd.common.util.CivilStatusUtil" %>
<%@ page import="lk.rgd.prs.api.domain.Person" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/certifieduser.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/marriageregistervalidation.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/datePicker.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<style type="text/css">
    #marriage-notice-outer table tr td {
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

    #birth-certificate-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>
<script type="text/javascript">
    function initPage() {
    }

    function validateComments() {
        var errormsg = "";
        var out="";

        var mode = document.getElementById("mode").value;
        if (mode == 'divorce') {
            errormsg = validateEmptyField("divorceComment", "errorComment", errormsg);
            errormsg = validateEmptyField("effectiveDateDatePicker", "errorCommentDate", errormsg);

            out = checkActiveFieldsForSyntaxErrors('marriage-extract-form');
            if (out != "") {
                errormsg = errormsg + out;
            }
            return printErrorMessages(errormsg);
        }
        else if (mode == 'reject') {
            errormsg = validateEmptyField("registrationRejectComment", "errorComment", errormsg);

            out = checkActiveFieldsForSyntaxErrors('marriage-extract-form');
            if (out != "") {
                errormsg = errormsg + out;
            }
            return printErrorMessages(errormsg);
        }

        return false;
    }
</script>

<div id="birth-certificate-outer">
<s:if test="mode=='print'">
    <table border="0" style="margin-top:1px;width:100%;" cellpadding="2px">
        <caption></caption>
        <col width="420px"/>
        <col width="200px"/>
        <col/>
        <tbody>
        <s:form action="eprMarkMarriageExtractAsPrinted.do" method="post">
            <%-- <s:if test="marriage.state.ordinal()!=12">  --%>
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
                                              cssStyle="width:300px;" onchange="populateCertifiedUserList()"/>
                                </td>
                            </tr>
                            <tr>
                                <td><s:label value="%{getText('signOfficer.label')}"/></td>
                                <td>
                                    <s:select id="issueUserId" name="licenseIssuedUserId" list="userList"
                                              cssStyle="width:300px;" onchange="printCertifiedUser()"/>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </div>
            </td>
        </tr>
            <%-- </s:if> --%>
        <tr>
            <td colspan="3" align="right">
                <div class="form-submit" style="margin-top:0;">
                        <%-- <s:if test="marriage.state.ordinal()!=12"> --%>
                    <s:submit value="%{getText('button.markasprinted')}"/>
                        <%-- </s:if> --%>
                    <s:hidden name="idUKey" value="%{marriage.idUKey}"/>
                    </s:form>
                </div>
                <div class="form-submit" style="margin-top:0;margin-right:5px;">
                    <s:submit value="%{getText('print.label')}" onclick="printPage()"/>
                </div>
                    <%--
                    <div class="form-submit">
                        <s:submit value="%{getText('button.back')}"/>
                    </div>
                    --%>
            </td>
        </tr>

        <tr style="font-size:9pt">
            <td colspan="1">&nbsp;</td>
            <td align="center" style="font-size:12pt;">
                <img src="<s:url value='/images/official-logo.png'/>"/>
            </td>
            <td> &nbsp;</td>
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
</s:if>
<table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;" cellpadding="2px">
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
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය<br/>
            பிரதேச செயளாளர் பிரிவு <br/>
            Divisional Secretary Division</span>
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
                        <br>பதிவுப் பிரிவு<br>Registration Division</span>
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

<table border="0" style="margin-top:0px;width:100%;" cellpadding="2px">
    <caption></caption>
    <col/>
    <tbody>
    <tr style="font-size:14pt">
        <td align="center">
            විවාහ විස්තර / in tamil / Details of Marriage
        </td>
    </tr>
    </tbody>
</table>

<table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse; font-size:12px"
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
                    <br>திருமண திகதி<br>Date of Marriage</span>
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
            විවාහ ස්ථානයේ ස්වභාවය<br/>
            திருமண நிகழ்விடத்தின் வகை<br/>
            Type of Marriage Place
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
            திருமணம் நகழ்ந்த இடம்<br>
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
            பதிவாளர்/குருவானவர்<br>
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
            திருமணத்தின் தன்மை<br>
            Type of Marriage
        </td>
        <td colspan="8">
            <table width="100%">
                <caption/>
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
<table border="1"
       style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;font-size:12px"
       cellpadding="5px">
    <caption/>
    <col width="200px"/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td>&nbsp;</td>
        <td align="center">
            පුරුෂ පාර්ශ්වය / மாப்பிள்ள திறத்தார் / Male Party
        </td>
        <td align="center">
            ස්ත්‍රී පාර්ශ්වය /பெண் திறத்தார்/  Female Party
        </td>
    </tr>
    <tr>
        <td>
            අනන්‍යතා අංකය <br>
            அடையாள எண் <br>
            Identification Number.
        </td>
        <td align="left">
            <s:label name="marriage.male.identificationNumberMale"/>
        </td>
        <td align="left">
            <s:label name="marriage.female.identificationNumberFemale"/>
        </td>
    </tr>
    <s:if test="mode !='divorce'">
    <tr>
        <td>
            උපන් දිනය <br>
            பிறந்த திகதி <br>
            Date of Birth
        </td>
        <td>
            <s:label name="marriage.male.dateOfBirthMale"/>
        </td>
        <td>
            <s:label name="marriage.female.dateOfBirthFemale"/>
        </td>
    </tr>
    <tr>
        <td>
            පසුවූ උපන් දිනයට වයස <br>
            சென்ற வருட பிறந்த தினத்தில் வயதி<br>
            Age at last Birthday
        </td>
        <td>
            <s:label name="marriage.male.ageAtLastBirthDayMale"/>
        </td>
        <td>
            <s:label name="marriage.female.ageAtLastBirthDayFemale"/>
        </td>
    </tr>
    </s:if>
    <tr>
        <td>
            ජන වර්ගය<br/>
            இனம்<br/>
            Ethnic Group
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

<table border="1" style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="300px"/>
    <col width="200px"/>
    <col width="300px"/>
    <col/>
    <s:if test="mode=='print'">  <%-- & marriage.state.ordinal()!=12 --%>
        <tr>
            <td><span class="font-8">
            සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන <br>
            சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம் <br>
            Name, Signature and Designation of certifying officer
        </span>
            </td>
            <td colspan="3">
                <s:if test="marriage.preferredLanguage=='si'">
                    <s:label id="signature" name="marriage.extractCertifiedUser.sienSignatureText"/>
                </s:if>
                <s:elseif test="marriage.preferredLanguage=='ta'">
                    <s:label id="signature" name="marriage.extractCertifiedUser.taenSignatureText"/>
                </s:elseif>
            </td>
        </tr>
        <tr>
            <td><span class="font-8">
            නිකුත් කළ ස්ථානය <br>
            வழங்கிய இடம் <br>
            Place of Issue
        </span>
            </td>
            <td colspan="3">
                <s:if test="marriage.preferredLanguage=='si'">
                    <s:label id="placeOfIssue" name="marriage.extractIssuedLocation.sienLocationSignature"/><br>
                    <s:label id="placeName" name="marriage.extractIssuedLocation.siLocationName"/>
                </s:if>
                <s:elseif test="marriage.preferredLanguage=='ta'">
                    <s:label id="placeOfIssue" name="marriage.extractIssuedLocation.taenLocationSignature"/><br>
                    <s:label id="placeName" name="marriage.extractIssuedLocation.taLocationName"/>
                </s:elseif>
            </td>
        </tr>
    </s:if>
    <tr>
        <td><label><span class="font-8">  ලියාපදිංචි අංකය
                <br>பெறப்பட்ட இலக்கம்<br>Registration Number</span></label>
        </td>
        <td align="center">
            <s:label id="serialNumber" name="marriage.idUKey"/>
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
<s:if test="mode=='reject'">
    <s:form id="marriage-extract-form" method="post" onsubmit="javascript:return validateComments()">
        <s:hidden name="mode" id="mode"/>
        <s:hidden name="idUKey"/>
        <table border="1" style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;"
               cellpadding="2px">
            <caption/>
            <col width="200px">
            <col>
            <tr>
                <td class="font-8">
                    අදහස් දක්වන්න
                    <br>கருத்தினை தெரிவிக்கவும்
                    <br>Add Comments
                </td>
                <td>
                    <s:textarea name="comment" id="registrationRejectComment"
                                cssStyle="width:98.2%;" rows="5"/>
                </td>
            </tr>
        </table>
        <div class="form-submit">
            <s:submit action="eprRejectMarriageRegistration"
                      value="%{getText('button.reject')}"/>
        </div>
    </s:form>
</s:if>
<s:if test="mode=='divorce'">
    <s:form id="marriage-extract-form" method="post" onsubmit="javascript:return validateComments()">
        <s:hidden name="mode" id="mode"/>
        <s:hidden name="idUKey"/>
        <table border="1" style="margin-top:10px;width:100%;border:1px solid #000;border-collapse:collapse;"
               cellpadding="2px">
            <caption/>
            <col width="200px">
            <col>
            <tr>
                <td class="font-8">
                    අදහස් දක්වන්න
                    <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                    <br>கருத்தினை தெரிவிக்கவும்
                    <br>Add Comments
                </td>
                <td>
                    <s:textarea name="comment" id="divorceComment"
                                cssStyle="width:98.2%;" rows="15"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label>
                    <span class="font-8">බලපැවැත්වෙන දිනය
                         <s:label value="*" cssStyle="color:red;font-size:10pt;"/>
                        <br>விவாகரத்து செல்லுபடியகும் திகதி<br>Effective Date of Divorce</span>
                    </label>
                </td>
                <td>
                    <s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
                    <s:textfield name="effectiveDateOfDivorce" id="effectiveDateDatePicker" maxLength="10"
                                 onmouseover="datepicker('effectiveDateDatePicker')"/>
                </td>
            </tr>
        </table>
        <div class="form-submit">
            <s:submit action="eprDivorce"
                      value="%{getText('button.marriageregister.divorced')}"/>
        </div>
    </s:form>
</s:if>
<s:hidden id="errorComment"
          value="%{getText('error.js.marriageregister.comment') + getText('message.cannotbeempty')}"/>
<s:hidden id="errorCommentDate"
          value="%{getText('error.js.marriageregister.date') + getText('message.cannotbeempty')}"/>
</div>