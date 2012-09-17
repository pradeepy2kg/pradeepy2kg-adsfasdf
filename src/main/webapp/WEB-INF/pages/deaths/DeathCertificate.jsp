<%@ page import="java.util.Date" %>
<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
<%@ page import="lk.rgd.common.util.DeathTypeUtil" %>
<%-- @author Duminda Dharmakeerthi --%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css">
    #death-certificate-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }

        .next-previous {
            display: none;
        }

        #alterations {
            display: none;
        }

        #locations {
            display: none;
        }

        td {
            font-size: 9pt;
        }
    }

    #death-certificate-outer .form-submit {
        margin: 5px 0 15px 0;
    }

    .changes-done {
        font-size: 6pt;
        font: bold;
        position: relative;
        top: 3px
    }
</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<script type="text/javascript">
    function initPage() {
    }
</script>
<script>
    $(function() {
        $('select#locationId').bind('change', function(evt1) {
            var id = $("select#locationId").attr('value');
            var options = '';
            if (id > 0) {
                $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:1,certificateId:0,type:'death'},
                        function(data) {
                            var users = data.authorizedUsers;
                            for (var i = 0; i < users.length; i++) {
                                options += '<option value="' + users[i].optionValue + '">' + users[i].optionDisplay + '</option>';
                            }
                            $("select#issueUserId").html(options);

                            var id = $('select#locationId').attr('value');
                            var user = $('select#issueUserId').attr('value');
                            var certId = $('label#certificateId').text();
                            $("text#user").html(user);
                            $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:10,userId:user,certificateId:certId,type:'death'},
                                    function(data) {
                                        var officerSign = data.officerSignature;
                                        var locationSign = data.locationSignature;
                                        var location = data.locationName;
                                        $("textarea#signature").html(officerSign);
                                        $("textarea#placeSign").html(locationSign);
                                        $("label#placeName").html(location);
                                    });
                        });

            } else {
                $("select#issueUserId").html(options);
            }
        });

        $('select#issueUserId').bind('change', function(evt2) {
            var id = $('select#locationId').attr('value');
            var user = $('select#issueUserId').attr('value');
            var certId = $('label#certificateId').text();
            $("text#user").html(user);
            $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:10,userId:user,certificateId:certId},
                    function(data) {
                        var officerSign = data.officerSignature;
                        var locationSign = data.locationSignature;
                        var location = data.locationName;
                        $("textarea#signature").html(officerSign);
                        $("textarea#placeSign").html(locationSign);
                        $("label#placeName").html(location);
                    });
        });
    });

    function initPage() {
    }
</script>

<s:if test="#request.certificateSearch">
    <s:url id="print" action="eprDeathCertificateSearch.do"/>
    <s:url id="cancel" action="eprDeathCertificateSearch.do"/>
</s:if>
<s:else>
    <s:url id="print" action="eprPrintDeathCertificate.do">
        <s:param name="idUKey" value="#request.idUKey"/>
        <s:param name="currentStatus" value="#request.currentStatus"/>
        <s:param name="pageNo" value="#request.pageNo"/>
        <s:param name="nextFlag" value="#request.nextFlag"/>
        <s:param name="previousFlag" value="#request.previousFlag"/>
        <s:param name="dsDivisionId" value="#request.dsDivisionId"/>
        <s:param name="deathDivisionId" value="#request.deathDivisionId}"/>
    </s:url>
    <s:if test="%{pageNo>0}">
        <s:url id="cancel" action="eprDeathBackToPreviousState.do">
            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
            <s:param name="pageNo" value="%{pageNo}"/>
            <s:param name="currentStatus" value="%{#request.currentStatus}"/>
            <s:param name="dsDivisionId" value="%{deathRegister.death.deathDivision.dsDivision.dsDivisionUKey}"/>
            <s:param name="deathDivisionId" value="%{deathRegister.death.deathDivision.bdDivisionUKey}"/>
        </s:url>
    </s:if>
    <s:else>
        <s:url id="cancel" action="eprDeathBackToPreviousState.do">
            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
            <s:param name="pageNo" value="%{1}"/>
            <s:param name="currentStatus" value="%{#request.currentStatus}"/>
            <s:param name="dsDivisionId" value="%{deathRegister.death.deathDivision.dsDivision.dsDivisionUKey}"/>
            <s:param name="deathDivisionId" value="%{deathRegister.death.deathDivision.bdDivisionUKey}"/>
        </s:url>
    </s:else>
</s:else>

<div id="death-certificate-outer">
<s:form action="eprPrintDeathCertificate.do" method="post">
<s:if test="#request.archivedEntryList.size>0">
    <div id="alterations">
        <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;width:400px">
            <legend><s:label value="%{getText('ArchivedData.label')}"/></legend>
            <table>
                <th></th>
                <th><s:label value="%{getText('lastupdate.time.label')}"/></th>
                <th><s:label name="viewlbl" value="%{getText('view.label')}"/></th>
                <s:iterator status="archivedStatus" value="archivedEntryList" id="searchId">
                    <tr class="<s:if test="#archivedStatus.odd == true">odd</s:if><s:else>even</s:else>">
                        <td class="table-row-index"><s:property value="%{#archivedStatus.count}"/></td>
                        <s:set value="getRegister().getStatus()" name="status"/>
                        <td><s:property value="getLifeCycleInfo().getLastUpdatedTimestamp()"/></td>
                        <s:set name="abc" value="getLifeCycleInfo().getLastUpdatedTimestamp()"/>

                        <s:url id="viewSelected" action="eprDeathCertificate.do">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>
                        <td><s:a href="%{viewSelected}" title="%{getText('view.label')}">
                            <img src="<s:url value='/images/view_1.gif'/>" width="25" height="25"
                                 border="none"/></s:a>
                        </td>
                    </tr>
                </s:iterator>
            </table>
        </fieldset>
    </div>
</s:if>
<s:if test="deathRegister.status.ordinal()==1 || #request.certificateSearch">
    <div id="locations">
        <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;width:500px">
            <legend><s:label value="%{getText('selectoption.label')}"/></legend>
            <table>
                <tr>
                    <td>
                        <s:label value="%{getText('placeOfIssue.label')}"/>
                    </td>
                    <td>
                        <s:select id="locationId" name="locationId" value="%{locationId}" list="locationList"
                                  cssStyle="width:300px;"/>
                    </td>
                </tr>
                <tr>
                    <td><s:label value="%{getText('signOfficer.label')}"/></td>
                    <td>
                        <s:select id="issueUserId" name="issueUserId" value="%{issueUserId}" list="userList"
                                  cssStyle="width:300px;"/>
                    </td>
                </tr>
            </table>
        </fieldset>
    </div>
</s:if>

<table style="width: 99%; border:none; border-collapse:collapse; ">
    <col width="250px"/>
    <col width="530px"/>
    <col width="250px"/>

    <tbody>
    <tr>
        <td colspan="3" align="left">
            <s:if test="#request.allowPrintCertificate">
                <div id="birthRegistration-page" class="form-submit">
                    <s:submit type="button" value="%{getText('mark_as_print.button')}"/>
                </div>
                <div class="next-previous" style="margin:15px 0 0 5px;float:right;">
                    <s:a href="%{printPage}" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>
                    <s:hidden id="printMessage" value="%{getText('print.message')}"/>
                </div>
            </s:if>
            <div class="next-previous" style="margin-top:15px;float:right;">
                <s:a href="%{cancel}"><s:label value="%{getText('previous.label')}"/></s:a>
            </div>
        </td>
    </tr>
    <tr>
        <td></td>
        <td rowspan="2" align="center">
            <img src="<s:url value="../images/official-logo.png" />"
                 style="display: block; margin-top: 5px; text-align: center;" width="80" height="100">
        </td>
        <td></td>
    </tr>
    <tr>
        <td rowspan="3">
            <img src="${pageContext.request.contextPath}/prs/ImageServlet?personUKey=${idUKey}&certificateType=death"
                 width="100" height="100"/>
        </td>
        <td rowspan="3">
            <table style="width:100%;border-collapse:collapse;float:right;">
                <tr style="border: none;">
                    <td style="border: none;">
                        ලියපදිංචි කිරීම<br/>பதிவு செய்தல்<br/>Registration
                    </td>
                    <td style="border: none; font-size: 25px;">}</td>
                    <td style="border: none;">
                        B2A
                    </td>
                </tr>
                <tr height="60px">
                    <td style="border: 1px solid #000;" colspan="3">සහතික පත්‍රයේ අංකය<br>சான்றிதழ் இல<br>Certificate Number</td>
                </tr>
                <tr height="40px">
                    <td align="center" style="border: 1px solid #000;" colspan="3"><s:label name="idUKey" cssStyle="font-size:11pt;" id="certificateId"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td rowspan="2" align="center" style="font-size:15pt;">
            ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA
            <br>මරණ සහතිකය
            <br>இறப்பு சான்றிதழ்
            <br>CERTIFICATE OF DEATH
        </td>
    </tr>
    </tbody>
</table>

<table border="1" style="width: 99%; border:1px solid #000; border-collapse:collapse; " class="font-9">
    <col width="215px"/>
    <col width="300px"/>
    <col width="215px"/>
    <col width="300px"/>
    <tbody>
    <tr>
        <td>
            (1) දිස්ත්‍රික්කය
            <br>மாவட்டம்
            <br>District
        </td>
        <td><s:label name="" value="%{deathPersonDistrict}"/><br>
            <s:label name="" value="%{deathPersonDistrictEn}" cssStyle="text-transform:uppercase;"/></td>
        <td>
            (2) ප්‍රාදේශීය ලේකම් කොට්ඨාශය
            <br/>பிரதேச செயளாளர் பிரிவு
            <br/>Divisional Secretary Division
        </td>
        <td><s:label name="" value="%{deathPersondsDivision}"/><br>
            <s:label name="" value="%{deathPersondsDivisionEn}" cssStyle="text-transform:uppercase;"/>
        </td>
    </tr>
    <tr>
        <td>
            (3) ලියාපදිංචි කිරීමේ කොට්ඨාශය
            <br>பதிவுப் பிரிவு
            <br>Registration Division
        </td>
        <td><s:label name="" value="%{deathPersonDeathDivision}"/><br>
            <s:label name="" value="%{deathPersonDeathDivisionEn}" cssStyle="text-transform:uppercase;"/></td>
        <td>
            (4) මුල් ලියාපදිංචියෙන් පසු වෙනස්කම්
            <br>முதல் பதிவின் பின் நிறைவேற்றிய மாற்றங்கள்
            <br>Changes after first registration
        </td>
        <td>
            <s:if test="changedFields.size() > 0">
                ඔව් - “**” ලකුණින් පෙන්වා ඈත
                <br>
                ஆம்-”**” குறியீட்டில் குறிப்பிடப்பட்டுள்ளது
                <br>
                Yes - marked with “**”
            </s:if>
            <s:else>
                නැත
                <br>
                இல்லை
                <br>
                No
            </s:else>
        </td>
    </tr>
    </tbody>
</table>

<table border="1" style="width: 99%; border:1px solid #000; border-collapse:collapse; " class="font-9">
<col width="215px"/>
<col width="160px"/>
<col width="100px"/>
<col width="100px"/>
<col width="154px"/>
<col width="130px"/>
<col width="120px"/>
<col width="51px"/>
<tbody>
<tr>
    <td>
        (5) අනන්‍යතා අංකය
        <br>அடையாள எண்
        <br>Identification Number
    </td>
    <td><s:label name="" value="%{deathPerson.deathPersonPINorNIC}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(9)">
                **
            </s:if>
        </div>
    </td>
    <td colspan="2">
        (6) වයස
        <br>வயது
        <br>Age
    </td>
    <td colspan="4">
        <s:if test="deathPerson.deathPersonAge > 0 || deathPerson.deathPersonAgeMonth > 0 || deathPerson.deathPersonAgeDate > 0">
            <table width="100%" cellpadding="0" cellspacing="0" style="margin-bottom: 0;" height="50px">
                <tr>
                    <td width="60px" style="font-size:8pt; border-right: 1px solid #aaa;">අවුරුදු<br/>ஆண்டுகள்<br/>Years</td>
                    <td align="center" width="30px" style="border-right: 1px solid #aaa;">
                        <s:if test="deathPerson.deathPersonAge > 0">
                            <s:label name="" value="%{deathPerson.deathPersonAge}"/>
                        </s:if>
                        <s:else>
                            -
                        </s:else>
                    </td>
                    <td width="60px" style="font-size:8pt; border-right: 1px solid #aaa;">මාස<br/>மாதங்கள்<br/>Months</td>
                    <td align="center" width="30px" style="border-right: 1px solid #aaa;">
                        <s:if test="deathPerson.deathPersonAgeMonth > 0">
                            <s:label name="" value="%{deathPerson.deathPersonAgeMonth}"/>
                        </s:if>
                        <s:else>
                            -
                        </s:else>
                    </td>
                    <td width="60px" style="font-size:8pt; border-right: 1px solid #aaa;">දින<br/>நாட்கள்<br/>Days</td>
                    <td align="center" width="30px">
                        <s:if test="deathPerson.deathPersonAgeDate > 0">
                            <s:label name="" value="%{deathPerson.deathPersonAgeDate}"/>
                        </s:if>
                        <s:else>
                            -
                        </s:else>
                    </td>
                </tr>
            </table>
        </s:if>
        <s:else>
            <s:label name="unknownFieldPref" cssStyle="font-size:9pt;"/> <br/>
            <s:label name="unknownFieldEn"/>
        </s:else>
        <div class="changes-done">
            <s:if test="changedFields.get(12)">
                **
            </s:if>
        </div>
    </td>
</tr>
<tr>
    <td>
        (7) මරණය සිදුවූ දිනය
        <br>இறப்பு நிகழ்ந்த திகதி
        <br>Date of death
    </td>
    <td colspan="3"><s:label name="" value="%{death.dateOfDeath}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(1)">
                **
            </s:if>
        </div>
    </td>
    <td>
        (8) ස්ත්‍රී පුරුෂ භාවය
        <br>பால்
        <br>Gender
    </td>
    <td colspan="3">
        <s:label name="" value="%{genderSi}"/>
        <br> <s:label name="" value="%{genderEn}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(13)">
                **
            </s:if>
        </div>
    </td>
</tr>
<tr>
    <td colspan="1">
        (9) මරණයේ ස්වභාවය
        <br>மரணத்தின் தன்மை
        <br>Type of Death
    </td>

    <td colspan="7">
        <%=DeathTypeUtil.getDeathType((Integer) request.getAttribute("deathRegister.deathType.ordinal()"),
                (String) request.getAttribute("deathRegister.death.preferredLanguage")).toUpperCase()%>
        <div class="changes-done">
            <s:if test="changedFields.get(0)">
                **
            </s:if>
        </div>
    </td>
</tr>
<tr>
    <td colspan="1" height="60px">
        (10) මරණය සිදුවූ ස්ථානය
        <br>இறப்பு நிகழந்த இடம்
        <br>Place of death
    </td>
    <td colspan="7"><s:label name="" value="%{death.placeOfDeath}"/>
        <br><s:label name="" value="%{death.placeOfDeathInEnglish}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(3)">
                **
            </s:if>
        </div>
    </td>
</tr>
<tr>
    <td colspan="1" height="60px">
        (11) මරණයට හේතු
        <br>இறப்பிற்கான காரணம்
        <br>Cause of Death
    </td>
    <td colspan="7" style="font-size:9pt"><s:label name="" value="%{death.causeOfDeath}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(6)">
                **
            </s:if>
        </div>
    </td>
        <%--<td colspan="1">හේතුවේ ICD කේත අංකය<br>*in tamil<br>ICD Code of cause</td>--%>
        <%--<td colspan="2"><s:label name="" value="%{death.icdCodeOfCause}"/></td>--%>
</tr>
<tr>
    <td colspan="1">
        (12) ආදාහන හෝ භූමදාන ස්ථානය
        <br>அடக்கம் செய்த அல்லது தகனஞ் செய்த இடம்
        <br>Place of burial or cremation
    </td>
    <td colspan="7" style="font-size:10pt"><s:label name="" value="%{death.placeOfBurial}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(8)">
                **
            </s:if>
        </div>
    </td>
</tr>
<tr>
    <td colspan="1" height="100px">
        (13) නම
        <br>பெயர்
        <br>Name
    </td>
    <td colspan="7" style="font-size:10pt"><s:label name=""
                                                    value="%{deathPerson.deathPersonNameOfficialLang}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(15)">
                **
            </s:if>
        </div>
    </td>
</tr>
<tr>
    <td colspan="1" height="90px">
        (14) නම ඉංග්‍රීසි භාෂාවෙන්
        <br>பெயர் ஆங்கில மொழியில்
        <br>Name in English
    </td>
    <td colspan="7" style="font-size:10pt"><s:label name="" value="%{deathPerson.deathPersonNameInEnglish}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(16)">
                **
            </s:if>
        </div>
    </td>
</tr>
<tr>
    <td colspan="1" height="80px">
        (15) පියාගේ සම්පුර්ණ නම
        <br>தந்தையின்முழுப் பெயர்
        <br>Father's Full Name
    </td>
    <td colspan="4" style="font-size:10pt"><s:label name="" value="%{deathPerson.deathPersonFatherFullName}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(19)">
                **
            </s:if>
        </div>
    </td>
    <td colspan="1">
        (16) අනන්‍යතා අංකය
        <br>அடையாள எண்
        <br>Identification No.
    </td>
    <td colspan="2"><s:label name="" value="%{deathPerson.deathPersonFatherPINorNIC}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(18)">
                **
            </s:if>
        </div>
    </td>
</tr>
<tr>
    <td colspan="1" height="80px">
        (17) මවගේ සම්පූර්ණ නම
        <br>தாயின் முழுப் பெயர்
        <br>Mother's Full Name
    </td>
    <td colspan="4" style="font-size:10pt"><s:label name="" value="%{deathPerson.deathPersonMotherFullName}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(21)">
                **
            </s:if>
        </div>
    </td>
    <td colspan="1">
        (18) අනන්‍යතා අංකය
        <br>அடையாள எண்
        <br>Identification No.
    </td>
    <td colspan="2"><s:label name="" value="%{deathPerson.deathPersonMotherPINorNIC}"/>
        <div class="changes-done">
            <s:if test="changedFields.get(20)">
                **
            </s:if>
        </div>
    </td>
</tr>
<tr>
    <td colspan="1" height="80px">
        (19) දැනුම් දෙන්නාගේ නම
        <br>தகவலளிப்பவரின் பெயர்
        <br>Informant's Name
    </td>
    <td colspan="4" style="font-size:10pt"><s:label name="" value="%{declarant.declarantFullName}"/></td>
    <td colspan="1">
        (20) අනන්‍යතා අංකය
        <br>அடையாள எண்
        <br>Identification No.
    </td>
    <td colspan="2"><s:label name="" value="%{declarant.declarantNICorPIN}"/></td>
</tr>
<tr>
    <td colspan="1" height="80px">
        (21) දැනුම් දෙන්නාගේ ලිපිනය
        <br>தகவலளிப்பவரின் முகவரி
        <br>Informant's Address
    </td>
    <td colspan="7" style="font-size:10pt"><s:label name="" value="%{declarant.declarantAddress}"/></td>
</tr>
</tbody>
</table>

<table border="1" style="width: 99%; border:1px solid #000; border-collapse:collapse; margin-bottom:0;"
       class="font-9">
    <col width="215px"/>
    <col width="160px"/>
    <col width="140px"/>
    <col/>
    <tbody>
    <tr>
        <td height="50px">
            (22) ලියාපදිංචි කළ දිනය
            <br>பதிவு செய்யப்பட்ட திகதி
            <br>Date of Registration
        </td>
        <td><s:label name="" value="%{death.dateOfRegistration}"/></td>
        <td>
            (23) නිකුත් කළ දිනය
            <br>வழங்கிய திகதி
            <br>Date of Issue
        </td>
        <td><%= DateTimeUtils.getISO8601FormattedString(new Date()) %>
        </td>
    </tr>
    <tr>
        <td colspan="2" height="75px">
            (24) සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන
            <br>சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்
            <br>Name, Signature and Designation of certifying officer
        </td>
        <td colspan="2" style="font-size:11pt">
            <s:textarea id="signature" value="%{deathRegister.originalDCIssueUserSignPrint}" disabled="true"
                        rows="4"
                        cssStyle="margin-top:10px;text-transform:none;width:100%;font-size:10pt;background:transparent;border:none;padding:0;text-transform:uppercase;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" height="30px">
            (25) නිකුත් කළ ස්ථානය / வழங்கிய இடம் / Place of Issue
        </td>
        <td colspan="2" style="font-size:11pt">
            <s:textarea id="placeSign" value="%{deathRegister.originalDCPlaceOfIssueSignPrint}" disabled="true"
                        rows="3"
                        cssStyle="margin-top:10px;text-transform:none;width:100%;font-size:10pt;background:transparent;border:none;padding:0;text-transform:uppercase;"/>
        </td>
    </tr>
    </tbody>
</table>

<s:label><p class="font-8" style="width:99%; text-align:center;">
    උප්පැන්න හා මරණ ලියපදිංචි කිරිමේ පණත (110 අධිකාරය) යටතේ රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව විසින් නිකුත් කරන
    ලදි,<br>
    பிறப்பு இறப்பு பதிவு செய்யும் சட்டத்தின் (110 வது அதிகாரத்தின் ) கீழ் பதிவாளர் நாயகத் திணைக்களத்தினால் வழங்கப்பட்டது<br>
    Issued by Registrar General's Department according to Birth and Death Registration Act (Chapter 110)
</s:label>
<s:if test="#request.allowPrintCertificate">
    <div id="birthRegistration-page" class="form-submit">
        <s:submit type="button" value="%{getText('mark_as_print.button')}"/>
    </div>
    <div class="next-previous" style="margin:15px 0 0 5px;float:right;">
        <s:a href="%{printPage}" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>
        <s:hidden id="printMessage" value="%{getText('print.message')}"/>
    </div>
</s:if>
<div class="next-previous" style="margin-top:15px;float:right">
    <s:a href="%{cancel}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</div>
<s:hidden value="%{idUKey}" name="idUKey"/>
<s:hidden name="currentStatus" value="%{#request.currentStatus}"/>
<s:hidden name="pageNo" value="%{#request.pageNo}"/>
<s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
<s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
<s:hidden name="dsDivisionId" value="%{deathRegister.death.deathDivision.dsDivision.dsDivisionUKey}"/>
<s:hidden name="deathDivisionId" value="%{deathRegister.death.deathDivision.bdDivisionUKey}"/>
<s:hidden name="certificateSearch" value="%{certificateSearch}"/>
</s:form>



