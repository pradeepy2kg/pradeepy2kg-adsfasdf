<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>
<script type="text/javascript" src="../js/validate.js"></script>

<s:hidden id="error1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error2" value="%{getText('enter.submitDate.label')}"/>
<s:hidden id="error3" value="%{getText('enter.serial.label')}"/>
<s:hidden id="error4" value="%{getText('submitDate.label')}"/>
<s:hidden id="error5" value="%{getText('enter.applicantName.label')}"/>
<s:hidden id="error6" value="%{getText('enter.applicantAddress.label')}"/>
<s:hidden id="error7" value="%{getText('enter.stampCharges.label')}"/>
<s:hidden id="error8" value="%{getText('stampCharges.label')}"/>
<s:hidden id="error9" value="%{getText('noOfCopies.label')}"/>
<s:hidden id="error10" value="%{getText('certificateIssuDate.label')}"/>
<div class="birth-certificate-search-form-outer" id="birth-certificate-search-form-outer">
<script>

    $(function() {
        $("#datePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });
    $(function() {
        $("#dateOdEvent").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });
    $(function() {
        $("#dateOfSubmission").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });
    // mode 1 = passing District, will return DS list
    $(function() {
        $('select#districtId').bind('change', function(evt1) {
            var id = $("select#districtId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:1},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivisionId").html(options1);
                    });
        });
    })

    var errormsg = "";
    function validate() {
        var domObject;
        var returnval = true;

        commonTags();

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

    function commonTags() {
        var domObject;

        // validate application number field
        domObject = document.getElementById('applicationNo');
        if (isFieldEmpty(domObject))
            isEmpty(domObject, '', 'error3');

        // validate submitted date
        domObject = document.getElementById('dateOfSubmission');
        if (isFieldEmpty(domObject))
            isEmpty(domObject, '', 'error2');
        else
            isDate(domObject.value, 'error1', 'error4');

        // validate applicant data
        domObject = document.getElementById('applicantFullName');
        if (isFieldEmpty(domObject))
            isEmpty(domObject, '', 'error5')

        domObject = document.getElementById('applicantAddress');
        if (isFieldEmpty(domObject))
            isEmpty(domObject, '', 'error6');

        // validate number of copies needed
        domObject = document.getElementById('noOfCopies');
        if (!isFieldEmpty(domObject))
            isNumeric(domObject.value, 'error1', 'error9');

        // validate certificate date of issue
        domObject = document.getElementById('datePicker');
        if (!isFieldEmpty(domObject))
            isDate(domObject.value, 'error1', 'error10');

        // validate stamp charges field
        domObject = document.getElementById('stampCharges');
        if (isFieldEmpty(domObject))
            isEmpty(domObject, '', 'error7');
        else
            validateNumber(domObject.value, 'error1', 'error8');
    }

</script>
<s:form action="eprBirthCertificateSearch.do" name="birthCertificateSearchForm" id="birth-certificate-search-form-1"
        method="POST" onsubmit="javascript:return validate()">
<table style="font-size:9pt">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
            <%--<td width="300px"></td>--%>
        <td align="center" style="font-size:12pt; width:180px">
            <img src="<s:url value="/images/official-logo.png"></s:url>" alt=""/><br>
        </td>
        <td align="center" style="font-size:12pt;">
            <s:if test="certificateType.ordinal() == 0">
                <label>උප්පැන්න සහතිකයක් ගැනීමේ සහ/නොහොත් ලේකම් පොත් සොයා බැලීමේ ඉල්ලුම් පත්‍රය
                    <br>* Tamil
                    <br>APPLICATION FOR BIRTH CERTIFICATE AND/OR SEARCH OF REGISTERS</label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label>මරණ සහතිකයක් ගැනීමේ සහ/ නොහොත් ලේකම් පොත් සොයා බැලීමේ ඉල්ලුම් පත්‍රය
                    <br>* Tamil
                    <br>APPLICATION FOR DEATH CERTIFICATE AND/OR SEARCH OF REGISTERS</label>
            </s:elseif>
        </td>
        <td width="300px">
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <s:fielderror name="duplicateApplicationNoError" cssStyle="color:red;font-size:10pt"/>
                </tr>
                <tr>
                    <td><label><span class="font-8">ඉල්ලුම් පත්‍ර අංකය<br>*Tamil<br>Application No</span></label>
                    </td>
                    <td><s:textfield name="certSearch.certificate.applicationNo" id="applicationNo"
                                     cssStyle="text-transform:uppercase;"/></td>
                </tr>
            </table>
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <td align="center" colspan="2">කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>அலுவலக பாவனைக்காக மட்டும்
                        <br>For office use only
                        <hr>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label><span class="font-8">භාරගත්  දිනය<br>* In Tamil<br>Submitted Date</span></label>
                    </td>
                    <td>
                            <s:label value="YYYY-MM-DD" cssStyle="margin-left:20px;font-size:10px"/><br>
                            <s:textfield id="dateOfSubmission"
                                         name="certSearch.certificate.dateOfSubmission" maxLength="10"/>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="8">
            <s:if test="certificateType.ordinal() == 0">
                <label>උපත සිදුවූ දිස්ත්‍රික්කයේ දිස්ත්‍රික් රෙජිස්ට්‍රාර් වෙත යැවිය යුතුය.
                    <br>* Tamil
                    <br>To be sent to the Office of the District Registrar of the District in which the birth
                    occurred.</label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label>මරණය සිදුවූ දිස්ත්‍රික්කයේ දිස්ත්‍රික් රෙජිස්ට්‍රාර් වෙත යැවිය යුතුය.
                    <br>* Tamil
                    <br>To be sent to the Office of the District Registrar of the District in which the death
                    occurred.</label>
            </s:elseif>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_01" cellspacing="0" cellpadding="0">
    <tr>
        <td class="font-9" width="400px"><label>(1) ඉල්ලුම්කරුගේ සම්පූර්ණ නම<br>*Tamil<br>
            Full Name of the Applicant</label></td>
        <td colspan="6"><s:textarea name="certSearch.certificate.applicantFullName" id="applicantFullName"
                                    cssStyle="text-transform:uppercase;"/></td>
    </tr>
    <tr>
        <td class="font-9"><label>ඉල්ලුම්කරුගේ ලිපිනය<br>*Tamil<br>
            Address of the Applicant</label></td>
        <td colspan="6"><s:textarea name="certSearch.certificate.applicantAddress" id="applicantAddress"
                                    cssStyle="text-transform:uppercase;"/></td>
    </tr>
    <tr>
        <td class="font-9">
            <s:if test="certificateType.ordinal() == 0">
                <label>(2) ඉල්ලුම්කරන්නේ කාගේ උප්පැන්නය ගැනද? <br>එම අයගේ සම්පූර්ණ නම රාජ්‍ය භාෂාවෙන්
                    (සිංහල / දෙමළ)<br>*Tamil<br>
                    Full Name of the person respecting whose birth application is made (Sinhala/Tamil) ?</label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label>(2) ඉල්ලුම් කරන්නේ කාගේ මරණය ගැනද? <br>එම අයගේ සම්පූර්ණ නම රාජ්‍ය භාෂාවෙන්
                    (සිංහල / දෙමළ)<br>*Tamil<br>
                    Full Name of the person respecting whose death application is made (Sinhala/Tamil) ?</label>
            </s:elseif>
        </td>
        <td colspan="6">
            <s:textarea name="certSearch.search.searchFullNameOfficialLang" id="searchFullNameOfficialLang"
                        cssStyle="text-transform:uppercase;"/>
        </td>
    </tr>
    <tr>
        <td class="font-9">
            <s:if test="certificateType.ordinal() == 0">
                <label> ඉල්ලුම්කරන්නේ කාගේ උප්පැන්නය ගැනද? <br>එම අයගේ සම්පූර්ණ නම ඉංග්‍රීසි භාෂාවෙන්
                    <br>*Tamil<br>
                    Full Name of the person respecting whose birth application is made in English ?</label>
            </s:if>
            <s:if test="certificateType.ordinal() == 1">
                <label> ඉල්ලුම් කරන්නේ කාගේ මරණය ගැනද? <br>එම අයගේ සම්පූර්ණ නම ඉංග්‍රීසි භාෂාවෙන්
                    <br>*Tamil<br>
                    Full Name of the person respecting whose birth application is made in English ?</label>
            </s:if>
        </td>
        <td colspan="6"><s:textarea name="certSearch.search.searchFullNameEnglish" id="searchFullNameEnglish"
                                    cssStyle="text-transform:uppercase;"/></td>
    </tr>
    <s:if test="certificateType.ordinal() == 1">
        <tr>
            <td class="font-9">
                <label> මරණය සිදුවීමට හේතුව (දන්නවා නම්)
                    <br>*Tamil<br>
                    Cause of Death (If known)</label>
            </td>
            <td colspan="6"><s:textarea name="certSearch.search.causeOfEvent"
                                        cssStyle="text-transform:uppercase;"/></td>
        </tr>
    </s:if>
    <tr>
        <td class="font-9"><label>ස්ත්‍රී පුරුෂ භාවය<br> பால் <br>Gender of the child</label></td>
        <td><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="certSearch.search.gender" headerKey="0" headerValue="%{getText('select_gender.label')}"
                cssStyle="width:190px; margin-left:5px;"/></td>
        <td><label>අවශ‍ය පිටපත් ගණන<br>* Tamil<br>No. of Copies required</label></td>
        <td><s:textfield name="certSearch.certificate.noOfCopies" id="noOfCopies" maxLength="2"/></td>
    </tr>
    <tr>
        <td class="font-9">
            <s:if test="certificateType.ordinal() == 0">
                <label>(3) පියාගේ සම්පූර්ණ නම<br>*Tamil<br>
                    Father's Full Name
                </label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label>(3) පියාගේ සම්පූර්ණ නම (දන්නවා නම්)<br>*Tamil<br>
                    Father's Full Name (If known)
                </label>
            </s:elseif>
        </td>
        <td colspan="6">
            <s:textarea name="certSearch.search.fatherFullName" id="fatherFullName"
                        cssStyle="text-transform:uppercase;"/></td>
    </tr>
    <tr>
        <td class="font-9">
            <s:if test="certificateType.ordinal() == 0">
                <label>(4) මවගේ සම්පූර්ණ නම (විවාහයට පෙර)<br>*Tamil<br>
                    Mother's Full Name (maiden name)
                </label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label>(4) මවගේ සම්පූර්ණ නම (දන්නවා නම්)<br>*Tamil<br>
                    Mother's Full Name (maiden name)
                </label>
            </s:elseif>
        </td>
        <td colspan="6">
            <s:textarea name="certSearch.search.motherFullName" id="motherFullName"
                        cssStyle="text-transform:uppercase;"/></td>
    </tr>
    <tr style="border-left:1px solid #000000;">
        <td class="font-9" width="150px">
            <s:if test="certificateType.ordinal() == 0">
                <label>(5) උපන් දිනය<br> பிறந்த திகதி <br>Date of Birth</label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label>(5) මරණය සිදු වූ දිනය<br>Tamil<br>Date of Death</label>
            </s:elseif>
        </td>
        <td>
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
            <s:textfield id="dateOdEvent" name="certSearch.search.dateOfEvent" maxLength="10"/>
        </td>
        <td class="font-9">
            <s:if test="certificateType.ordinal() == 0">
                <label> උපන් ස්ථානය<br>பிறந்த இடம்<br> Place of Birth</label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label> සිදු වූ ස්ථානය<br>Tamil<br> Place of Occurrence</label>
            </s:elseif>
        </td>
        <td><s:textfield name="certSearch.search.placeOfEvent" id="placeOfEvent"
                         cssStyle="text-transform:uppercase;"/></td>
    </tr>
    <tr>
        <td class="font-9">
            <s:if test="certificateType.ordinal() == 0">
                <label>උපත් ප්‍රකාශනයේ අනුක්‍රමික අංකය<br>*Tamil<br>Birth Declaration Serial Number</label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label>මරණ ප්‍රකාශනයේ අනුක්‍රමික අංකය<br>*Tamil<br>Birth Declaration Serial Number</label>
            </s:elseif>
        </td>
        <td colspan="6"><s:textfield name="certSearch.search.searchSerialNo" id="searchSerialNo" maxLength="10"/></td>
    </tr>
    <tr>
        <td class="font-9" rowspan="2"><label>(6) රෙජිසිට්‍රර්ගේ කොට්ඨාශය<br>*Tamil<br>Registrar's
            Division</label></td>
        <td><label>දිස්ත්‍රික්කය மாவட்டம் District</label></td>
        <td colspan="6" class="table_reg_cell_01">
            <s:select id="districtId" name="birthDistrictId" list="districtList" value="birthDistrictId"
                      cssStyle="width:98.5%;"/>
        </td>
    </tr>
    <tr>
        <td><label>ප්‍රාදේශීය ලේකමි කොටිඨාශය<br>* In Tamil<br>Divisional Secretariat</label></td>
        <td colspan="6" class="table_reg_cell_01" id="table_reg_cell_01">
            <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                      cssStyle="width:98.5%;"/>
        </td>
    <tr>
    <tr>
        <td class="font-9">
            <s:if test="certificateType.ordinal() == 0">
                <label>(7) උප්පැන්න සහතිකයේ අංකය<br>*Tamil<br>Birth Certificate Number</label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label>(7) මරණ සහතිකයේ අංකය<br>*Tamil<br>Birth Certificate Number</label>
            </s:elseif>
        </td>
        <td><s:textfield name="certSearch.search.certificateNo" id="certificateNo"/></td>
        <td class="font-9" width="150px">
            <label>නිකුත් කළ දිනය<br>* Tamil<br>Date of Issue</label></td>
        <td>
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:80px;font-size:10px"/><br>
            <s:textfield id="datePicker" name="certSearch.search.certificateIssueDate"
                         cssStyle="float:left;" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td class="font-9"><label>
            (8) මෙහි ඇලවූ මුද්දරවල වටිනාකම<br>*Tamil<br>Value of stamps affixed</label>
        </td>
        <td colspan="3"><s:textfield name="certSearch.certificate.stampCharges" id="stampCharges" maxLength="5"/></td>
    </tr>
    </tbody>
</table>
<s:hidden name="pageNo" value="1"/>

<div class="form-submit">
    <s:submit value="%{getText('bdfSearch.button')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
</div>
