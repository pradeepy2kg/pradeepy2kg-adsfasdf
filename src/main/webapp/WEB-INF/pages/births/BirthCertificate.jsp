<%-- @author Duminda Dharmakeerthi,amith jayasekara--%>
<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>


<style type="text/css">
    #birth-certificate-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }

        #locationSignId {
            display: none;
        }

        #alterations {
            display: none;
        }
    }

    #birth-certificate-outer .form-submit {
        margin: 5px 0 15px 0;
    }

    .changes-done {
        font-size: 12pt;
        font: bold;
        float: left;
        top: 3px
    }

    .changes-noalign {
        font-size: 12pt;
        font: bold;
    }

    .center-text {
        font-size: 10pt;
        font-style: italic;
        text-align: center;
    }

</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<script>
    $(function() {
        $('select#locationId').bind('change', function(evt1) {
            var id = $("select#locationId").attr('value');
            var options = '';
            if (id > 0) {
                $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:1,certificateId:0},
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
                            $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:10,userId:user,certificateId:certId},
                                    function(data) {
                                        var officerSign = data.officerSignature;
                                        var locationSign = data.locationSignature;
                                        var location = data.locationName;
                                        var locationAddress = data.locationAddress;
                                        $("textarea#signature").html(officerSign);
                                        $("textarea#placeSign").html(locationSign);
                                        $("label#placeName").html(location);
                                        $("textarea#retAddress").html(locationAddress);
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
                        var locationAddress = data.locationAddress;
                        $("textarea#signature").html(officerSign);
                        $("textarea#placeSign").html(locationSign);
                        $("label#placeName").html(location);
                        $("textarea#retAddress").html(locationAddress);
                    });
        });
    });

    function initPage() {
    }
</script>

<div id="birth-certificate-outer">

<div id="alterations">
    <s:if test="archivedEntryList.size>0">
        <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;width:400px;float:left;">
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

                        <s:url id="viewSelected" action="eprBirthCertificate.do">
                            <s:param name="bdId" value="idUKey"/>
                        </s:url>
                        <td><s:a href="%{viewSelected}" title="%{getText('view.label')}">
                            <img src="<s:url value='/images/view_1.gif'/>" width="25" height="25" border="none"/></s:a>
                        </td>
                    </tr>
                </s:iterator>
            </table>
        </fieldset>
    </s:if>
</div>
<s:if test="directPrint">
    <s:url id="print" value="eprDirectPrintBirthCertificate.do"/>
    <s:url id="cancel" action="eprBirthRegistrationHome.do"/>
</s:if>
<s:else>
    <s:if test="#request.certificateSearch">
        <s:url id="print" value="eprMarkBirthCertificateSearch.do"/>
        <s:url id="cancel" action="eprBirthCertificateSearch.do">
        </s:url>
    </s:if>
    <s:else>
        <s:if test="%{pageNo>0}">
            <s:url id="cancel" action="eprBirthCancelCertificatePrint.do">
                <%--todo change--%>
                <s:param name="pageNo" value="%{pageNo}"/>
                <s:param name="birthDistrictId"
                         value="#request.register.birthDivision.dsDivision.district.districtUKey"/>
                <s:param name="birthDivisionId" value="#request.register.birthDivision.bdDivisionUKey"/>
                <s:param name="dsDivisionId" value="#request.register.birthDivision.dsDivision.dsDivisionUKey"/>
                <s:param name="printed" value="#request.printed"/>
                <s:param name="printStart" value="#request.printStart"/>
            </s:url>
        </s:if>
        <s:else>
            <s:url id="cancel" action="eprBirthCancelCertificatePrint.do">
                <s:param name="pageNo" value="1"/>
                <s:param name="birthDistrictId"
                         value="#request.register.birthDivision.dsDivision.district.districtUKey"/>
                <s:param name="birthDivisionId" value="#request.register.birthDivision.bdDivisionUKey"/>
                <s:param name="dsDivisionId" value="#request.register.birthDivision.dsDivision.dsDivisionUKey"/>
                <s:param name="printed" value="#request.printed"/>
                <s:param name="printStart" value="#request.printStart"/>
            </s:url>
        </s:else>
        <s:url id="print" value="eprMarkCertificateAsPrinted.do"/>
    </s:else>
</s:else>
<div style="width:65%;float:left;margin-top:5px;" id="locationSignId">
    <s:form action="%{print}" method="post">
    <s:if test="register.status.ordinal() == 8 || #request.certificateSearch">
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
    </s:if>
</div>

    <%--TODO remove--%>
<s:hidden name="idUKey" value="%{#request.idUKey}"/>

<s:hidden name="pageNo" value="%{#request.pageNo}"/>
<s:hidden name="bdId" value="%{#request.bdId}"/>
<s:hidden name="birthDistrictId" value="%{#request.register.birthDivision.dsDivision.district.districtUKey}"/>
<s:hidden name="birthDivisionId" value="%{#request.register.birthDivision.bdDivisionUKey}"/>
<s:hidden name="dsDivisionId" value="%{#request.register.birthDivision.dsDivision.dsDivisionUKey}"/>

<div style="width:35%;float:left;">
    <s:if test="#request.allowPrintCertificate">
        <div class="form-submit" style="margin:5px 0 0 0;">
            <s:submit value="%{getText('mark_as_print.button')}" type="submit"/>
        </div>
        <div class="form-submit" style="margin:15px 0 0 5px;">
            <s:a href="%{printPage}" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>
            <s:hidden id="printMessage" value="%{getText('print.message')}"/>
        </div>
    </s:if>
    <div class="form-submit" style="margin-top:15px;">
        <s:a href="%{cancel}"><s:label value="%{getText('previous.label')}"/></s:a>
    </div>
</div>

<table style="width:100%; border:none; border-collapse:collapse;">
    <col width="300px">
    <col width="400px">
    <col width="340px">
    <tbody>
    <tr>
        <td rowspan="3"></td>
        <td rowspan="2" align="center">
            <img src="<s:url value="../images/official-logo.png" />"
                 style="display: block; text-align: center;" width="100" height="120">
        </td>
        <td>
            <table border="1" style="width:50%;border:1px solid #000;border-collapse:collapse;float:right;">
                <tr height="60px">
                    <td width="100%">සහතික පත්‍රයේ අංකය<br>சான்றிதழ் இல<br>Certificate Number</td>
                </tr>
                <tr height="40px">
                    <td align="center"><s:label id="certificateId" name="bdId" cssStyle="font-size:11pt;"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr></tr>
    <tr>
        <td align="center" style="font-size:15pt;">ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA <br>
            <s:if test="birthType.ordinal() != 0">
                උප්පැන්න සහතිකය<br>
                பிறப்பு சான்றிதழ்﻿<br>
                BIRTH CERTIFICATE
            </s:if>
            <s:else>
                මළ උප්පැන්න සහතිකය<br>
                சாப்பிள்னள பிறப்பு சான்றிதழ்﻿<br>
                STILL BIRTH CERTIFICATE
            </s:else>
        </td>
        <td></td>
    </tr>
    </tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:5px 0;font-size:10pt">
    <col width="195px">
    <col width="220px">
    <col width="210px">
    <col width="220px">
    <col width="185px">
    <tbody>
    <tr height="70px">
        <td>දිස්ත්‍රික්කය<br>மாவட்டம் <br>District</td>
        <td>
            <s:label name="" value="%{childDistrict}" cssStyle="font-size:11pt;"/><br/>
            <s:label name="" value="%{childDistrictEn}" cssStyle="text-transform:uppercase;"/>
        </td>
        <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය
            <br/>பிரதேச செயளாளர் பிரிவு
            <br/>Divisional Secretary Division
        </td>
        <td colspan="2">
            <s:label name="" value="%{childDsDivision}" cssStyle="font-size:11pt;"/><br/>
            <s:label name="" value="%{childDsDivisionEn}" cssStyle="text-transform:uppercase;"/>
        </td>
    </tr>
    <s:if test="birthType.ordinal() != 0">
        <tr height="70px">
            <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය
                <br>பதிவுப் பிரிவு
                <br>Registration Division
            </td>
            <td colspan="2">
                <s:label name="" value="%{#request.register.bdDivisionPrint}" cssStyle="font-size:11pt;"/><br>
                <s:label name="" value="%{#request.register.birthDivision.enDivisionName}"
                         cssStyle="text-transform:uppercase;"/>
            </td>
            <td style="font-size:9pt;">මුල් ලියාපදිංචියෙන් පසු වෙනස්කම්
                <br>முதல் பதிவின் பின் நிறைவேற்றிய மாற்றங்கள்
                <br>Changes After First Registration
            </td>
            <td style="font-size:9pt;">
                <s:if test="changedFields.size() > 0">
                    ඔව්, <label class="font-9">**</label> ලකුණින් පෙන්වා ඈත
                    <br>
                    ஆம், <label class="font-9">**</label> குறியீட்டில் குறிப்பிடப்பட்டுள்ளது
                    <br>
                    Yes, marked with <label class="font-9">**</label>
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
    </s:if>
    <s:else>
        <tr height="70px">
            <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය
                <br>பதிவுப் பிரிவு
                <br>Registration Division
            </td>
            <td colspan="4">
                <s:label name="" value="%{#request.register.bdDivisionPrint}" cssStyle="font-size:11pt;"/><br>
                <s:label name="" value="%{#request.register.birthDivision.enDivisionName}"
                         cssStyle="text-transform:uppercase;"/>
            </td>
        </tr>
    </s:else>
    </tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
<col width="195px">
<col width="220px">
<col width="155px">
<col width="130px">
<col width="115px">
<col width="115px">
<col width="100px">
<tbody>
<s:if test="birthType.ordinal() != 0">
    <tr height="70px">
        <td>අනන්‍යතා අංකය<br>தனிநபர்அடையாள எண் <br>Identification Number</td>
        <td align="center" style="font-size:14pt;"><s:label name="" value="%{#request.child.pin}"/></td>
        <td>උපන් දිනය <br>பிறந்த திகதி<br>Date of Birth</td>
        <td>
            <div class="changes-done">
                <s:if test="changedFields.get(21)">
                    **&nbsp;<br/>&nbsp;
                </s:if>
            </div>
            <div>
                <s:label name="" value="%{#request.child.childDateOfBirthForPrint}" cssStyle="font-size:12pt;"/><br>
                <s:label value="YYYY-MM-DD" cssStyle="font-size:8pt;"/>
            </div>
        </td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br>பால்<br>Gender</td>
        <td colspan="2">
            <div class="changes-done">
                <s:if test="changedFields.get(25)">
                    **&nbsp;<br/>&nbsp;
                </s:if>
            </div>
            <div>
                <s:label name="" value="%{gender}" cssStyle="font-size:12pt;"/><br/>
                <s:label name="" value="%{genderEn}"/>
            </div>
        </td>
    </tr>
    <tr height="70px">
        <td>උපන් ස්ථානය <br>பிறந்த இடம்<br>Place of birth
        </td>
        <td colspan="2">
            <div class="changes-done">
                <s:if test="changedFields.get(22)">
                    **&nbsp;
                </s:if>
            </div>
            <s:label name="placeOfBirth" value="%{#request.child.placeOfBirth}" cssStyle="font-size:11pt;"/>
            <br>

            <div class="changes-done">
                <s:if test="changedFields.get(23)">
                    **&nbsp;
                </s:if>
            </div>
            <s:label name="placeOfBirthEnglish" value="%{#request.child.placeOfBirthEnglish}"
                     cssStyle="font-size:10pt;"/>
        </td>
        <td colspan="2">මව්පියන් විවාහකද? <br>பெற்றோர் விவாகம் செய்தவர்களா?<br>Were Parents Married?
        </td>
        <td colspan="2">
            <div class="changes-done">
                <s:if test="changedFields.get(17)">
                    **&nbsp;<br/>&nbsp;
                </s:if>
            </div>
            <div>
                <s:label name="" value="%{marriedStatus}" cssStyle="font-size:11pt;"/><br>
                <s:label name="" value="%{marriedStatusEn}"/>
            </div>
        </td>
    </tr>
</s:if>
<s:else>
    <tr height="60px">
        <td>උපන් දිනය <br>பிறந்த திகதி<br>Date of Birth</td>
        <td>
            <div class="changes-done">
                <s:if test="changedFields.get(1)">
                    **&nbsp;<br/>&nbsp;
                </s:if>
            </div>
            <div>
                <s:label name="" value="%{#request.child.childDateOfBirthForPrint}" cssStyle="font-size:12pt;"/><br>
                <s:label value="YYYY-MM-DD" cssStyle="font-size:8pt;"/>
            </div>
        </td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br>பால்<br>Gender</td>
        <td>
            <s:label name="" value="%{gender}" cssStyle="font-size:11pt;"/><br/>
            <s:label name="" value="%{genderEn}"/>
        </td>
        <td colspan="2">
            දරුවා මැරී උපදින විට ගර්භයට සති කීයක් ගත වී තිබුනේද යන්න
            <br>பிள்ளை இறந்து பிறந்த பொழுது கர்ப்பந் தரித்து எத்தனை வாரம்
            <br>Number of weeks pregnant at the time of still-birth
        </td>
        <td>
            <s:if test="child.weeksPregnant != null && child.weeksPregnant != 0">
                <s:label name="" value="%{#request.child.weeksPregnant}"/>
            </s:if>
            <s:else>
                <div class="center-text">
                    <s:label name="unknownFieldPref" cssStyle="font-size:12pt;"/> /
                    <s:label name="unknownFieldEn"/>
                </div>
            </s:else>
        </td>
    </tr>
    <tr height="70px">
        <td>උපන් ස්ථානය <br>பிறந்த இடம்<br>Place of birth
        </td>
        <td colspan="6">
            <s:label name="placeOfBirth" value="%{#request.child.placeOfBirth}" cssStyle="font-size:11pt;"/><br>
            <s:label name="placeOfBirthEnglish" value="%{#request.child.placeOfBirthEnglish}"
                     cssStyle="font-size:12pt;"/>
        </td>
    </tr>
</s:else>

<s:if test="birthType.ordinal() != 0">
    <tr height="150px">
        <td>නම <br>பெயர்<br>Name
        </td>
        <td colspan="6" class="bc-name" style="font-size:14pt">
            <s:if test="child.childFullNameOfficialLang != null">
                <div class="changes-done">
                    <s:if test="changedFields.get(0)">
                        **&nbsp;
                    </s:if>
                </div>
                <s:label name="" value="%{#request.child.childFullNameOfficialLang}"/>
            </s:if>
            <s:else>
                <div class="center-text">
                    <s:label name="unknownFieldPref" cssStyle="font-size:12pt;"/> /
                    <s:label name="unknownFieldEn"/>
                </div>
            </s:else>
        </td>
    </tr>
    <tr height="140px">
        <td>නම ඉංග්‍රීසි භාෂාවෙන් <br>ஆங்கிலத்தில் பெயர் <br> Name in English
        </td>
        <td colspan="6" class="bc-name" style="font-size:12pt">
            <s:if test="child.childFullNameEnglish != null">
                <div class="changes-done">
                    <s:if test="changedFields.get(1)">
                        **&nbsp;
                    </s:if>
                </div>
                <s:label name="" cssStyle="text-transform: uppercase;" value="%{#request.child.childFullNameEnglish}"/>
            </s:if>
            <s:else>
                <div class="center-text">
                    <s:label name="unknownFieldPref" cssStyle="font-size:12pt;"/> /
                    <s:label name="unknownFieldEn"/>
                </div>
            </s:else>
        </td>
    </tr>
</s:if>
<tr height="100px">
    <td>පියාගේ සම්පුර්ණ නම<br>தந்தையின்முழுப் பெயர்<br> Father's Full Name
    </td>
    <td colspan="6" class="bc-name">
        <s:if test="parent.fatherFullName != null || parent.fatherFullNameInEnglish != null">
            <div class="changes-done">
                <s:if test="changedFields.get(10)">
                    **&nbsp;
                </s:if>
            </div>
        <span style="padding-right:5px"><s:label cssStyle="font-size:14pt" name=""
                                                 value="%{#request.parent.fatherFullName}"/> </span>
            | <span style="padding-left:5px"><s:label cssStyle="font-size:8pt" name=""
                                                      value="%{#request.parent.fatherFullNameInEnglish}"/> </span>
        </s:if>
        <s:else>
            <div class="center-text">
                <s:label name="unknownFieldPref" cssStyle="font-size:12pt;"/> /
                <s:label name="unknownFieldEn"/>
            </div>
        </s:else>
    </td>
</tr>
<tr height="50px">
    <td>පියාගේ අනන්‍යතා අංකය<br>
        தந்தையின் அடையாள எண்<br>
        Father's Identification No.
    </td>
    <td align="center">
        <s:if test="parent.fatherNICorPIN !=null">
            <s:if test="changedFields.get(11)">
                <label class="changes-noalign">**&nbsp;</label>
            </s:if>
            <s:label name="" value="%{#request.parent.fatherNICorPIN}" cssStyle="font-size:12pt;"/>
        </s:if>
        <s:else>
            <div class="center-text">
                <s:label name="unknownFieldPref" cssStyle="font-size:12pt;"/> /
                <s:label name="unknownFieldEn"/>
            </div>
        </s:else>
    </td>
    <td colspan="2">පියාගේ ජන වර්ගය<br>தந்தையின் இனம்<br> Father's Ethnic Group</td>
    <td colspan="3">
        <s:if test="parent.fatherRace != null">
            <div class="changes-done">
                <s:if test="changedFields.get(16)">
                    **&nbsp;<br/>&nbsp;
                </s:if>
            </div>
            <div>
                <s:label name="" value="%{fatherRacePrint}" cssStyle="font-size:12pt;"/><br/>
                <s:label name="" value="%{fatherRacePrintEn}" cssStyle="text-transform:uppercase;"/>
            </div>
        </s:if>
        <s:else>
            <div class="center-text">
                <s:label name="unknownFieldPref" cssStyle="font-size:12pt;"/> /
                <s:label name="unknownFieldEn"/>
            </div>
        </s:else>
    </td>
</tr>

<tr height="100px">
    <td>මවගේ සම්පූර්ණ නම
        <br> தாயின் முழுப் பெயர்
        <br> Mother's Full Name
    </td>
    <td colspan="6" class="bc-name" style="font-size:14pt">
        <s:if test="parent.motherFullName != null || parent.motherFullNameInEnglish != null">
            <div class="changes-done">
                <s:if test="changedFields.get(26)">
                    **&nbsp;
                </s:if>
            </div>
        <span style="padding-right:5px"><s:label cssStyle="font-size:14pt" name=""
                                                 value="%{#request.parent.motherFullName}"/> </span>
            | <span style="padding-left:5px"><s:label cssStyle="font-size:8pt" name=""
                                                      value="%{#request.parent.motherFullNameInEnglish}"/> </span>
        </s:if>
        <s:else>
            <div class="center-text">
                <s:label name="unknownFieldPref" cssStyle="font-size:12pt;"/> /
                <s:label name="unknownFieldEn"/>
            </div>
        </s:else>
    </td>
</tr>
<tr height="50px">
    <td>ම‌වගේ අනන්‍යතා අංකය<br>
        தாயின் அடையாள எண் <br>
        Mother's Identification No.
    </td>
    <td align="center">
        <s:if test="parent.motherNICorPIN != null">
            <s:if test="changedFields.get(27)">
                <label class="changes-noalign">**&nbsp;</label>
            </s:if>
            <s:label name="" value="%{#request.parent.motherNICorPIN}" cssStyle="font-size:12pt;"/>
        </s:if>
        <s:else>
            <div class="center-text">
                <s:label name="unknownFieldPref" cssStyle="font-size:12pt;"/> /
                <s:label name="unknownFieldEn"/>
            </div>
        </s:else>
    </td>
    <td colspan="2">මවගේ ජන වර්ගය<br>தாயின் இனம்<br> Mother's Ethnic Group
    </td>
    <td colspan="3">
        <s:if test="parent.motherRace != null">
            <div class="changes-done">
                <s:if test="changedFields.get(32)">
                    **&nbsp;<br/>&nbsp;
                </s:if>
            </div>
            <div>
                <s:label name="" value="%{motherRacePrint}" cssStyle="font-size:12pt;"/><br/>
                <s:label name="" value="%{motherRacePrintEn}" cssStyle="text-transform:uppercase;"/>
            </div>
        </s:if>
        <s:else>
            <div class="center-text">
                <s:label name="unknownFieldPref" cssStyle="font-size:12pt;"/> /
                <s:label name="unknownFieldEn"/>
            </div>
        </s:else>
    </td>
</tr>

</tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
    <col width="195px">
    <col width="220px">
    <col width="155px">
    <col width="460px">
    <tbody>
    <tr height="60px">
        <td>ලියාපදිංචි කළ දිනය<br>பதிவு செய்யப்பட்ட திகதி<br> Date of Registration</td>
        <td>
            <s:label name="" value="%{#request.register.dateOfRegistrationForPrint}" cssStyle="font-size:12pt;"/><br>
            <s:label value="YYYY-MM-DD" cssStyle="font-size:8pt;"/>
        </td>
        <td>නිකුත් කළ දිනය<br>வழங்கிய திகதி<br> Date of Issue
        </td>
        <td>
            <label style="font-size:12pt;"><%= DateTimeUtils.getISO8601FormattedString(new Date()) %>
            </label><br>
            <s:label value="YYYY-MM-DD" cssStyle="font-size:8pt;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" height="85px">
            සහතික කරනු ලබන නිලධාරියා ගේ නම, තනතුර සහ අත්සන<br>
            சான்றிதழ் அளிக்கும் அதிகாரியின் பெயர், பதவி, கையொப்பம்<br>
            Name, Signature and Designation of certifying officer
        </td>
        <td colspan="2" style="font-size:11pt">
            <s:textarea id="signature" value="%{#request.register.originalBCIssueUserSignPrint}" disabled="true"
                        rows="4"
                        cssStyle="margin-top:10px;text-transform:none;width:100%;font-size:10pt;background:transparent;border:none;padding:0;text-transform:uppercase;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" height="40px">නිකුත් කළ ස්ථානය / வழங்கிய இடம்/ Place of Issue
        </td>
        <td colspan="2" cssStyle="font-size:11pt;">
            <s:textarea id="placeSign" value="%{#request.register.originalBCPlaceOfIssueSignPrint}" disabled="true"
                        rows="3"
                        cssStyle="margin-top:10px;text-transform:none;width:100%;font-size:10pt;background:transparent;border:none;padding:0;text-transform:uppercase;"/>
        </td>
    </tr>
    </tbody>
</table>

<p style="font-size:9pt">
    උප්පැන්න හා මරණ ලියපදිංචි කිරිමේ පණත (110 අධිකාරය) යටතේ රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව විසින් නිකුත් කරන
    ලදි,<br>
    பிறப்பு இறப்பு பதிவு செய்யும் சட்டத்தின் (110 வது அதிகாரத்தின் ) கீழ் பதிவாளர் நாயகத் திணைக்களத்தினால் வழங்கப்பட்டது<br>
    Issued by Registrar General's Department according to Birth and Death Registration Act (Chapter 110)
</p>

<div style="page-break-after:always;">
    <s:if test="!#request.certificateSearch">
</div>
<hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:100px;">

<table border="0" cellspacing="0" width="100%">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td rowspan="7" width="10%" height="350px"></td>
        <td colspan="3" width="80%" height="100px"
            style="text-align:center;margin-left:auto;margin-right:auto;font-size:22pt">
            <label>රාජ්‍ය සේවය පිණිසයි &nbsp;&nbsp;அரச பணி &nbsp;&nbsp;ON STATE SERVICE</label><br/>
            <label style="font-size:11pt;">රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව &nbsp;&nbsp;பதிவாளர் நாயகம் திணைக்களம்
                &nbsp;&nbsp;REGISTRAR GENERAL'S DEPARTMENT</label>
        </td>
        <td rowspan="7" width="10%"></td>
    </tr>
    <tr>
        <td></td>
        <td width="10%">&nbsp;</td>
        <td width="30%">
            <s:label name="informant.informantName" cssStyle="width:600px;font-size:14pt;"/><br/>
            <s:label name="informant.informantAddress" cssStyle="width:600px;font-size:14pt;"/>
        </td>
    </tr>
    <tr>
        <td height="50px" width="30%">
            <s:textarea id="retAddress" value="%{returnAddress}" disabled="true" rows="5"
                        cssStyle="margin-top:10px;text-transform:none;width:100%;font-size:14pt;background:transparent;border:none;"/>
        </td>
        <td width="10%">&nbsp;</td>
        <td width="30%">
        </td>
    </tr>
    <tr>
        <td colspan="2"><p></p></td>
    </tr>
    <tr>
        <td colspan="2"><p></p></td>
    </tr>
    <tr>
        <td colspan="2"><p></p></td>
    </tr>
    <tr>
        <td colspan="2"><p></p></td>
    </tr>
    </tbody>
</table>
<hr style="border-style:dashed ; float:left;width:100% ;margin-top:30px;"/>
<br><br>
</s:if>

<s:if test="#request.allowPrintCertificate">
    <div class="form-submit" style="margin:5px 0 0 0;">
        <s:submit value="%{getText('mark_as_print.button')}" type="submit"/>
    </div>
    <div class="form-submit" style="margin:15px 0 0 5px;">
        <s:a href="%{printPage}" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>
        <s:hidden id="printMessage" value="%{getText('print.message')}"/>
    </div>
</s:if>
<div class="form-submit" style="margin-top:15px;">
    <s:a href="%{cancel}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</s:form>

</div>
<%-- Styling Completed --%>
