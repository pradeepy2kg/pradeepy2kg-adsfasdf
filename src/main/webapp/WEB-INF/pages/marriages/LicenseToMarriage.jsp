<%@ page import="lk.rgd.common.util.CivilStatusUtil" %>
<%@ page import="lk.rgd.prs.api.domain.Person" %>
<%@ page import="lk.rgd.crs.web.util.MarriageType" %>
<%@ page import="lk.rgd.crs.api.domain.MarriageNotice" %>
<%--
@author amith jayasekara
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
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

<script>
    $(function() {
        $('select#locationId').bind('change', function(evt1) {
            var id = $("select#locationId").attr('value');
            var options = '';
            if (id > 0) {
                $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:4,certificateId:0},
                        function(data) {
                            var users = data.authorizedUsers;
                            for (var i = 0; i < users.length; i++) {
                                options += '<option value="' + users[i].optionValue + '">' + users[i].optionDisplay + '</option>';
                            }
                            $("select#issueUserId").html(options);

                            var id = $('select#locationId').attr('value');
                            var user = $('select#issueUserId').attr('value');
                            var certId = $('label#serialNumber').text();
                            //set user signature for given user
                            $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:10,userId:user
                                ,certificateId:certId,type:'marriage'},
                                    function(data) {
                                        var officerSign = data.officerSignature;
                                        var locationSign = data.locationSignature;
                                        var location = data.locationName;
                                        var locationDistrictInOl = data.locationDistrictInOl;
                                        var locationDivisionInOl = data.locationDivisionInOl;
                                        var locationDistrictInEn = data.locationDistrictInEn;
                                        var locationDivisionInEn = data.locationDivisionInEn;
                                        $("label#signature").html(officerSign);
                                        $("label#placeSign").html(locationSign);
                                        $("label#dsDivisionInOL").html(locationDivisionInOl);
                                        $("label#dsDivisionInEn").html(locationDivisionInEn);
                                        $("label#districtInOl").html(locationDistrictInOl);
                                        $("label#districtInEn").html(locationDistrictInEn);
                                        $("label#placeOfIssue").html(location);
                                    });
                        });

            } else {
                $("select#issueUserId").html(options);
            }
        });

        $('select#issueUserId').bind('change', function(evt2) {
            var id = $('select#locationId').attr('value');
            var user = $('select#issueUserId').attr('value');
            var certId = $('label#serialNumber').text();
            $("text#user").html(user);
            $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:10,userId:user,certificateId:certId},
                    function(data) {
                        var officerSign = data.officerSignature;
                        var locationSign = data.locationSignature;
                        var location = data.locationName;
                        $("label#signature").html(officerSign);
                        $("label#placeSign").html(locationSign);
                        $("label#placeName").html(location);
                    });
        });
    });

    function initPage() {
    }
</script>
<%--section logo and issue date and serial number--%>
<div class="marriage-notice-outer">


<table>
    <caption/>
    <col width="450px"/>
    <col width="230px"/>
    <col/>
    <tbody>
    <s:form action="eprMarkLicenseAsPrinted.do" method="post">
    <s:if test="marriage.state.ordinal() != 7 ">
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
    </s:if>
    <tr>
        <td colspan="3" align="right">
            <div class="form-submit">
                <s:submit value="%{getText('button.mark.as.print')}"/>
                    <%--DSDivision id to be load will be changed based on the notice type to be mark as printed
                     if notice is male or both DS division would be male party ds division and if it is female notice
                      DS division would be female party related 
                     --%>
                <s:if test="noticeType.ordinal()==2">
                    <%--female--%>
                    <s:hidden name="dsDivisionId"
                              value="%{marriage.mrDivisionOfFemaleNotice.dsDivision.dsDivisionUKey}"/>
                </s:if>
                <s:else>
                    <s:hidden name="dsDivisionId"
                              value="%{marriage.mrDivisionOfMaleNotice.dsDivision.dsDivisionUKey}"/>
                </s:else>
                <s:hidden name="idUKey" value="%{marriage.idUKey}"/>
                <s:hidden name="pageNo" value="%{#request.pageNo}"/>
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
    <tr>
        <td></td>
        <td><img src="<s:url value="/images/official-logo.png"/>"/></td>
        <td>
            <table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;"
                   cellpadding="2px">
                <caption/>
                <col width="225px"/>
                <col width="225px"/>
                <tr>
                    <td> ලියාපදිංචි අංකය<br>
                        பெறப்பட்ட இலக்கம்<br>
                        Registration Number
                    </td>
                    <td align="center">
                        <s:label id="serialNumber" value="%{marriage.idUKey}"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        නිකුත් කල දිනය<br>
                        பெறப்பட்ட திகதி <br>
                        Date of Issue
                    </td>
                    <td align="center">
                        <s:label value="%{dateOfIssueLicense}"/> <br>yyyy-mm-dd
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr style="height:45px">
        <td colspan="3" align="center" style="font-size:15pt">
            විවාහ වීමට බලපත්‍රය / குடிமதிப்பீட்டு ஆவணத்தில் / License for Marriage
    </tr>
    </tbody>
</table>

<%--section end date and type--%>
<table border="1" style="margin-top: 5px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="500px"/>
    <col width="165px"/>
    <col width="200px"/>
    <col width="165px"/>
    <tr>
        <td colspan="1">
            විවාහ වීමට වලංගු වන අවසාන දිනය <br>
            in tamil <br>
            Last date for Marriage for which this license is valid
        </td>
        <td colspan="1" align="center">
            <s:label value="%{dateOfCancelLicense}"/> <br>yyyy-mm-dd
        </td>
        <td colspan="1">
            විවාහයේ ස්වභාවය <br>
            in tamil <br>
            Type of Marriage
        </td>
        <td align="center">
            <s:if test="marriage.preferredLanguage =='si'">
                <s:label value="%{marriage.typeOfMarriage.siType}"/>
            </s:if>
            <s:else>
                <s:label value="%{marriage.typeOfMarriage.taType}"/>
            </s:else>
            <br>
            <s:label value="%{marriage.typeOfMarriage.enType}"/>
        </td>
    </tr>
</table>

<%--section details of the marriage--%>
<table border="1" style="margin-top:15px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="250px"/>
    <col width="220px"/>
    <col width="220px"/>
    <col width="220px"/>
    <col width="220px"/>
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
            <s:label value="%{marriage.male.dateOfBirthMale}"/> <br>yyyy-mm-dd
        </td>
        <td align="center">
            <s:label value="%{marriage.male.ageAtLastBirthDayMale}"/>
        </td>
        <td align="center">
            <s:label value="%{marriage.female.dateOfBirthFemale}"/> <br>yyyy-mm-dd
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
                    request.getAttribute("marriage.male.civilStatusMale"), (String) request.getAttribute("marriage.preferredLanguage"))%>
            <br>
            <%=CivilStatusUtil.getCivilStatus((Person.CivilStatus)
                    request.getAttribute("marriage.male.civilStatusMale"), "en")%>
        </td>
        <td align="center">
            <s:label value="%{maleRaceInOL}"/>
            <br>
            <s:label value="%{maleRaceInEn}"/>
        </td>
        <td align="center">
            <%=CivilStatusUtil.getCivilStatus((Person.CivilStatus) request.
                    getAttribute("marriage.female.civilStatusFemale"), (String) request.getAttribute("marriage.preferredLanguage"))%>
            <br>
            <%=CivilStatusUtil.getCivilStatus((Person.CivilStatus)
                    request.getAttribute("marriage.female.civilStatusFemale"), "en")%>
        </td>
        <td align="center">
            <s:label value="%{femaleRaceInOL}"/>
            <br>
            <s:label value="%{femaleRaceInEn}"/>
        </td>
    </tr>
    <tr>
        <td>
            රට සහ ගමන් බලපත්‍ර අංකය<br>
            in ta<br>
            Country and passport
        </td>
        <td align="left">
            <s:if test="marriage.preferredLanguage=='si'">
                <s:label value="%{marriage.male.maleCountry.siCountryName}"/>
            </s:if><s:else>
            <s:label value="%{marriage.male.maleCountry.taCountryName}"/>
        </s:else>
            <br>
            <s:label value="%{marriage.male.maleCountry.enCountryName}"/>
        </td>
        <td align="center">
            <s:label value="%{marriage.male.malePassport}"/>
        </td>
        <td align="left">
            <s:if test="marriage.preferredLanguage=='si'">
                <s:label value="%{marriage.female.femaleCountry.siCountryName}"/>
            </s:if><s:else>
            <s:label value="%{marriage.female.femaleCountry.taCountryName}"/>
        </s:else>
            <br>
            <s:label value="%{marriage.female.femaleCountry.enCountryName}"/>
        </td>
        <td align="center">
            <s:label value="%{marriage.female.femalePassport}"/>
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
            <s:label value="%{marriage.male.nameInEnglishMale}"/>
        </td>
        <td colspan="2" align="left">
            <s:label value="%{marriage.female.nameInOfficialLanguageFemale}"/>
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
            in tamil<br>
            Fathers I.D Number
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
            <s:label value="%{marriage.male.fatherFullNameMaleInEnglish}"/>
        </td>
        <td colspan="2" align="left">
            <s:label value="%{marriage.female.fatherFullNameFemaleInOfficialLang}"/>
            <br>
            <s:label value="%{marriage.female.fatherFullNameFemaleInEnglish}"/>
        </td>
    </tr>

</table>

<%--section who authorized--%>
<table border="1" style="margin-top:15px;width:100%;border:1px solid #000;border-collapse:collapse;"
       cellpadding="2px">
    <caption/>
    <col width="450px"/>
    <col width="225px"/>
    <col width="300px"/>
    <col width="250px"/>
    <col width="175px"/>
    <tr>
        <td colspan="1">
            සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන <br>
            சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம் <br>
            Name, Signature and Designation of certifying officer
        </td>
        <td colspan="4">
            <s:label id="signature" value="%{licenseIssueUserSignature}"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            නිකුත් කළ ස්ථානය <br>
            வழங்கிய இடம் <br>
            Place of Issue
        </td>
        <td colspan="4">
            <s:label id="placeOfIssue" value="%{licenseIssuePlace}"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">
            දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td colspan="1">
            <s:label id="districtInOl" value="%{licenseIssueDistrictInOL}"/>
            <br>
            <s:label id="districtInEn" value="%{licenseIssueDistrictInEN}"/>
        </td>
        <td colspan="1">
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரதேச செயளாளர் பிரிவு <br>
            Divisional Secretariat <br>
        </td>
        <td colspan="2">
            <s:label id="dsDivisionInOL" value="%{licenseIssueDivisionInOL}"/>
            <br>
            <s:label id="dsDivisionInEn" value="%{licenseIssueDivisionInEN}"/>
        </td>
    </tr>
</table>

<%--       in follow case we have to display address of the receiver
--%>
<div style="page-break-after:always;margin-bottom:150px;"></div>

<s:if test="marriage.licenseCollectType.ordinal()==1 || marriage.licenseCollectType.ordinal()==3 ">
    <hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:60px;">
    <%--Latter for declarant   --%>
    <table border="0" cellspacing="0" width="100%" style="margin-top:0;">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td rowspan="8" width="200px" height="350px"></td>
            <td colspan="2" width="600px" height="100px"
                style="text-align:center;margin-left:auto;margin-right:auto;font-size:16pt">
                <label>රාජ්‍ය සේවය පිණිසයි / அரச பணி
                    On State Service</label></td>
            <td rowspan="8" width="200px"></td>
        </tr>
        <s:if test="marriage.licenseCollectType.ordinal()==1">
            <tr>
                <td><s:label cssStyle="width:600px;font-size:10pt;" name="marriage.male.nameInOfficialLanguageMale"
                             disabled="true"/></td>
            </tr>
            <tr>
                <td><s:label cssStyle="width:600px;font-size:10pt;"
                             name="marriage.male.residentAddressMaleInOfficialLang"
                             cssClass="disable"
                             disabled="true"/></td>
            </tr>
        </s:if>
        <s:else>
            <tr>
                <td><s:label cssStyle="width:600px;font-size:10pt;" name="marriage.female.nameInOfficialLanguageFemale"
                             disabled="true"/></td>
            </tr>
            <tr>
                <td><s:label cssStyle="width:600px;font-size:10pt;"
                             name="marriage.female.residentAddressFemaleInOfficialLang"
                             cssClass="disable"
                             disabled="true"/></td>
            </tr>
        </s:else>
        </tbody>
    </table>

    <hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:0px;margin-top:0px;">
</s:if>

</div>