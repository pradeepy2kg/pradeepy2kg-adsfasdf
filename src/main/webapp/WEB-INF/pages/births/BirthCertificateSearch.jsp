<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/popreg/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/popreg/css/datepicker.css" type="text/css"/>

<div class="birth-certificate-search-form-outer" id="birth-certificate-search-form-outer">
    <script>
        $(function() {
            $("#datePicker").datepicker();
        });
        $(function() {
            $("#bcSearchDatePicker").datepicker();
        });
        $(function() {
            $("#dateOfSubmission").datepicker();
        });
        // mode 1 = passing District, will return DS list
        $(function() {
            $('select#districtId').bind('change', function(evt1) {
                var id = $("select#districtId").attr("value");
                $.getJSON('/popreg/crs/DivisionLookupService', {id:id,mode:1},
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
    </script>
    <%--TODO insert specific css for tables in this page still implementing--%>
    <s:form action="eprBirthCertificateSearch.do" name="birthCertificateSearchForm" id="birth-certificate-search-form-1"
            method="POST" onsubmit="javascript:return validate()">
        <table style="font-size:9pt">
            <caption></caption>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td width="300px"></td>
                <td align="center" style="font-size:12pt; width:430px"><img
                        src="<s:url value="/images/official-logo.png"></s:url>"
                        alt=""/><br><label>
                    උප්පැන්න සහතිකයක් ගැනීමේ සහ/නොහොත් ලේකම් පොත් සොයා බැලීමේ ඉල්ලුම් පත්‍රය
                    <br>* Tamil
                    <br>APPLICATION FOR BIRTH CERTIFICATE AND/OR SEARCH OF REGISTERS</label></td>
                <td>
                    <table class="table_reg_datePicker_page_01">
                        <tr>
                            <td><label><span class="font-8">*Sinhala<br>*Tamil<br>Application No</span></label>
                            </td>
                            <td><s:textfield name="bcSearch.applicationNo" id="applicationNo"/></td>
                        </tr>
                    </table>
                    <table class="table_reg_datePicker_page_01">
                        <tr>
                            <td><label><span class="font-8">යොමුකළ දිනය<br>*Tamil<br>Submitted Date</span></label>
                            </td>
                            <td><s:textfield id="dateOfSubmission" name="bcSearch.dateOfSubmission"></s:textfield>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="8">
                    * Sinhala
                    <br>* Tamil
                    <br>To be sent to the Office of the District Registrar of the District in which the birth occured.
                </td>
            </tr>
            </tbody>
        </table>

        <table class="table_reg_page_01" cellspacing="0" cellpadding="0">
            <tr>
                <td class="font-9" width="400px"><label>(1) ඉල්ලුම්කරැගේ සම්පූර්ණ නම<br>*Tamil<br>
                    Full Name of the Applicant</label></td>
                <td colspan="6"><s:textarea name="bcSearch.applicantFullName" id="applicantFullName"/></td>
            </tr>
            <tr>
                <td class="font-9"><label> ඉල්ලුම්කරැගේ ලිපිනය<br>*Tamil<br>
                    Address of the Applicant</label></td>
                <td colspan="6"><s:textarea name="bcSearch.applicantAddress" id="applicantAddress"/></td>
            </tr>
            <tr>
                <td class="font-9"><label>(2) ඉල්ලුම්කරන්නේ කාගේ උප්පැන්නය ගැනද? එම අයගේ සම්පූර්ණ නම රාජ්‍ය භාෂාවෙන්
                    (සිංහල / දෙමළ)<br>*Tamil<br>
                    Full Name of the person respecting whose birth application is made ?
                </label></td>
                <td colspan="6"><s:textarea name="bcSearch.childFullNameOfficialLang"
                                            id="childFullNameOfficialLang"/></td>
            </tr>
            <tr>
                <td class="font-9"><label> ඉල්ලුම්කරන්නේ කාගේ උප්පැන්නය ගැනද? එම අයගේ සම්පූර්ණ නම ඉංග්‍රීසි භාෂාවෙන්
                    <br>*Tamil<br>
                    Full Name of the person respecting whose birth application is made in English?
                </label></td>
                <td colspan="6"><s:textarea name="bcSearch.childFullNameEnglish" id="childFullNameEnglish"/></td>
            </tr>
            <tr>
                <td class="font-9"><label>ස්ත්‍රී පුරුෂ භාවය<br> பால் <br>Gender of the child</label></td>
                <td><s:select
                        list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                        name="bcSearch.gender" headerKey="0" headerValue="%{getText('select_gender.label')}"
                        cssStyle="width:190px; margin-left:5px;"/></td>
                <td><label> * Sinhala<br>* Tamil<br>No. of Copies required</label></td>
                <td><s:textfield name="bcSearch.noOfCopies" id="noOfCopies"/></td>
            </tr>
            <tr>
                <td class="font-9"><label>(3) පියාගේ සම්පූර්ණ නම<br>*Tamil<br>
                    Father's Full Name
                </label></td>
                <td colspan="6">
                    <s:textarea name="bcSearch.fatherFullName" id="fatherFullName"/></td>
            </tr>
            <tr>
                <td class="font-9"><label>(4) මවගේ සම්පූර්ණ නම<br>*Tamil<br>
                    Mother's Full Name (maiden name)
                </label></td>
                <td colspan="6">
                    <s:textarea name="bcSearch.motherFullName" id="motherFullName"/></td>
            </tr>
            <tr style="border-left:1px solid #000000;">
                <td class="font-9" width="150px"><label>(5) උපන් දිනය<br> பிறந்த திகதி <br>Date of Birth</label></td>
                <td><s:textfield id="bcSearchDatePicker" name="bcSearch.dateOfBirth"></s:textfield>
                </td>
                <td class="font-9"><label> උපන් ස්ථානය<br>பிறந்த இடம்<br> Place of Birth</label></td>
                <td><s:textfield name="bcSearch.placeOfBirth" id="placeOfBirth"/></td>
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
                <td><label>D.S.කොට්ඨාශය பிரிவு D.S. Division</label></td>
                <td colspan="6" class="table_reg_cell_01" id="table_reg_cell_01">
                    <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                              cssStyle="width:98.5%;"/>
                </td>
            <tr>
            <tr>
                <td class="font-9"><label>
                    (7) උප්පැන්න සහතිකයේ අංකය<br>*Tamil<br>Birth Certificate Number</label>
                </td>
                <td><s:textfield name="bcSearch.certificateNo" id="certificateNo"/></td>
                <td class="font-9" width="150px"><label>නිකුත් කළ දිනය<br>* Tamil<br>Date of Issue</label></td>
                <td><s:textfield id="datePicker" name="bcSearch.certificateIssueDate"></s:textfield>
                </td>
            </tr>
            </tbody>
        </table>
        <s:hidden name="pageNo" value="1"/>

        <div class="form-submit">
            <s:submit value="%{getText('search.label')}" cssStyle="margin-top:10px;"/>
        </div>
    </s:form>
</div>
