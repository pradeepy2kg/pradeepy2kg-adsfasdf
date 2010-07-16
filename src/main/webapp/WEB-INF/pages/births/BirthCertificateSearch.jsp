<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-certificate-search-form-outer" id="birth-certificate-search-form-outer">

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
                            <td><sx:datetimepicker id="dateOfSubmittion" name="bcSearch.dateOfSubmittion"
                                                   displayFormat="yyyy-MM-dd" onmouseover="javascript:splitDate()"/>
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
                <td class="font-9" width="400px"><label>(1) *Sinhala<br>*Tamil<br>
                    Full Name of the Applicant</label></td>
                <td colspan="6"><s:textarea name="bcSearch.applicantFullName" id="applicantFullName"/></td>
            </tr>
            <tr>
                <td class="font-9"><label> *Sinhala<br>*Tamil<br>
                    Address of the Applicant</label></td>
                <td colspan="6"><s:textarea name="bcSearch.applicantAddress" id="applicantAddress"/></td>
            </tr>
            <tr>
                <td class="font-9"><label>(2) *Sinhala<br>*Tamil<br>
                    Full Name of the person respecting whose birth application is made ?
                </label></td>
                <td colspan="6"><s:textarea name="bcSearch.fullName" id="fullName"/></td>
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
                <td class="font-9"><label>(3) *Sinhala<br>*Tamil<br>
                    Father's Full Name
                </label></td>
                <td colspan="6">
                    <s:textarea name="bcSearch.fatherFullName" id="fatherFullName"/></td>
            </tr>
            <tr>
                <td class="font-9"><label>(4) *Sinhala<br>*Tamil<br>
                    Mother's Full Name (maiden name)
                </label></td>
                <td colspan="6">
                    <s:textarea name="bcSearch.motherFullName" id="motherFullName"/></td>
            </tr>
            <tr style="border-left:1px solid #000000;">
                <td width="150px"><label>(5) උපන් දිනය<br> பிறந்த திகதி <br>Date of Birth</label></td>
                <td>
                    <sx:datetimepicker id="datePicker" name="bcSearch.dateOfBirth" displayFormat="yyyy-MM-dd"
                                       onchange="javascript:splitDate('datePicker')"/>
                </td>
                <td><label> උපන් ස්ථානය<br>பிறந்த இடம்<br> Place of Birth</label></td>
                <td><s:textfield name="bcSearch.placeOfBirth" id="placeOfBirth"/></td>
            </tr>
            <tr>
                <td><label><span class="font-8">(6) *Sinhala<br>*Tamil<br>Registrar's Division</span></label></td>
                <td colspan="6" align="center">DS Division List here</td>
            </tr>
            <tr>
                <td><label><span class="font-8">(7) *Sinhala<br>*Tamil<br>Birth Certificate Number</span></label>
                </td>
                <td><s:textfield name="bcSearch.certificateNo" id="certificateNo"/></td>
                <td width="150px"><label>* Sinhala<br>* Tamil<br>Date of Issue</label></td>
                <td>
                    <sx:datetimepicker id="datePicker" name="bcSearch.certificateIssueDate" displayFormat="yyyy-MM-dd"
                                       onchange="javascript:splitDate('datePicker')"/>
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
