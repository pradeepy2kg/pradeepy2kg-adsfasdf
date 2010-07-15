<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-certificate-search-form-outer" id="birth-certificate-search-form-outer">

<%--TODO insert specific css for tables in this page still implementing--%>
<s:form action="" name="birthCertificateSearchForm" id="birth-certificate-search-form-1" method="POST"
        onsubmit="javascript:return validate()">
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
            * Sinhala
            <br>* Tamil
            <br>APPLICATION FOR BIRTH CERTIFICATE AND/OR SEARCH OF REGISTERS</label></td>
        <td>
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <td><label><span class="font-8">*Sinhala<br>*Tamil<br>Application Number</span></label>
                    </td>
                    <td><s:textfield name="register.bdfSerialNo" id="bdfSerialNo"/></td>
                </tr>
            </table>
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <td><label><span class="font-8">යොමුකළ දිනය<br>*Tamil<br>Submitted Date</span></label>
                    </td>
                    <td><sx:datetimepicker id="submitDatePicker" name="register.dateOfRegistration"
                                           displayFormat="yyyy-MM-dd"
                                           onmouseover="javascript:splitDate()"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            * Sinhala
            <br>* Tamil
            <br>To be sent to the Office of the District Registrar of the District in which the birth occured.
        </td>
    </tr>
    </tbody>
</table>


<table class="table_reg_page_01" cellspacing="0" cellpadding="0">
    <tr>
        <td class="font-9" width="250px"><label>(1) *Sinhala<br>*Tamil<br>
            Name of the Applicant and Address</label></td>
        <td colspan="2"><s:textarea name="child.childFullNameOfficialLang" id="childFullNameOfficialLang"
                                    cssStyle="width:98.2%;"/></td>
    </tr>
    <tr>
        <td class="font-9"><label>(2) *Sinhala<br>*Tamil<br>
            Full Name of the person respecting whose birth application is made ?
        </label></td>
        <td>
            <s:textarea name="child.childFullNameEnglish" id="childFullNameEnglish" cssStyle="width:98.2%;"/></td>
    </tr>
    <tr>
        <td class="font-9"><label>(3) *Sinhala<br>*Tamil<br>
            Father's Full Name
        </label></td>
        <td>
            <s:textarea name="child.childFullNameEnglish" id="childFullNameEnglish" cssStyle="width:98.2%;"/></td>
    </tr>
    <tr>
        <td class="font-9"><label>(4) *Sinhala<br>*Tamil<br>
            Mother's Full Name (maiden name)
        </label></td>
        <td>
            <s:textarea name="child.childFullNameEnglish" id="childFullNameEnglish" cssStyle="width:98.2%;"/></td>
    </tr>
    <tr style="border-left:1px solid #000000;">
        <td width="150px"><label>(5)උපන් දිනය<br> பிறந்த திகதி <br>Date of Birth</label></td>
        <td colspan="2">
            <sx:datetimepicker id="datePicker" name="child.dateOfBirth" displayFormat="yyyy-MM-dd"
                               onchange="javascript:splitDate('datePicker')"/>
        </td>
    </tr>
    <tr>
        <td><label>(6) උපන් ස්ථානය<br>பிறந்த இடம்<br> Place of Birth</label></td>
        <td><s:textfield name="child.placeOfBirth" id="placeOfBirth" cssStyle="width:97.6%;"/></td>
    </tr>
        <%--<tr>--%>
        <%--<td rowspan="4"><label>(2) උපන් ස්ථානය<br>பிறந்த இடம்<br> Place of Birth</label></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
        <%--<td><label>ස්ථානය பிறந்த இடம் Place</label></td>--%>
        <%--<td colspan="6"><s:textfield name="child.placeOfBirth" id="placeOfBirth" cssStyle="width:97.6%;"/></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
        <%--<td colspan="3"><label> රෝහලේදී /*in Tamil/In a Hospital</label></td>--%>
        <%--<td colspan="1"><label>ඔව් / *in Tamil / Yes </label></td>--%>
        <%--<td align="center"><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'true':''}"--%>
        <%--value="true"/></td>--%>
        <%--<td><label>නැත / *in Tamil / No</label></td>--%>
        <%--<td align="center"><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'false':''}"/></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
        <%--<td class="font-9" colspan="2"><label>(5) උප්පැන්න සහතිකය නිකුත් කල යුතු භාෂාව <br>பிறப்பு அத்தாட்சி ….. <br>Preferred--%>
        <%--Language for--%>
        <%--Birth Certificate </label></td>--%>
        <%--<td colspan="6"><s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'Tamil'}"--%>
        <%--name="register.preferredLanguage"--%>
        <%--cssStyle="width:190px; margin-left:5px;"></s:select></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
        <%--<td class="font-9"><label>(6)ස්ත්‍රී පුරුෂ භාවය<br> பால் <br>Gender of the child</label></td>--%>
        <%--<td colspan="3"><s:select--%>
        <%--list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"--%>
        <%--name="child.childGender" headerKey="0" headerValue="%{getText('select_gender.label')}"--%>
        <%--cssStyle="width:190px; margin-left:5px;"/></td>--%>
        <%--<td colspan="2"><label>(7) උපත් බර<br>பிறப்பு நிறை<br>Birth Weight (kg)</label></td>--%>
        <%--<td colspan="2"><s:textfield name="child.childBirthWeight" id="childBirthWeight" cssStyle="width:95%;"/></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
        <%--<td class="font-9"><label>(8)සජිවි උපත් අනුපිළි‍‍වල අනුව කීවෙනි ළමයා ද? <br>பிறப்பு ஒழுங்கு <br>According--%>
        <%--to Live Birth Order,--%>
        <%--rank of the child?</label></td>--%>
        <%--<td colspan="3" class="font-9"><s:textfield name="child.childRank" id="childRank"/></td>--%>
        <%--<td colspan="2" class="font-9"><label>(9)නිවුන් දරු උපතක් නම්, දරුවන් ගනන<br>பல்வகைத்தன்மை (இரட்டையர்கள்--%>
        <%--எனின்),<br> பிள்னளகளின் எண்ணிக்கை<br>If--%>
        <%--multiple births, number of children</label></td>--%>
        <%--<td colspan="2"><s:textfield name="child.numberOfChildrenBorn" cssStyle="width:95%;"/></td>--%>
        <%--</tr>--%>

    </tbody>
</table>

<s:hidden name="pageNo" value="1"/>
<%--

--%>

<%--<script type="text/javascript">
    function validate()
    {
        var errormsg = "";
        var element;
        var returnval;
        var flag = false;
        var lateOrbelate = false;
        var check = document.getElementById('skipjs');

        /*date related validations*/
        var datePicker = dojo.widget.byId('datePicker').inputNode.value;
        var submitDatePicker = dojo.widget.byId('submitDatePicker').inputNode.value;
        var birtdate = new Date(datePicker);
        var submit = new Date(submitDatePicker);

        //compare two days
        if (birtdate.getTime() > submit.getTime()) {
            errormsg = errormsg + "\n" + document.getElementById('error6').value;
            flag = true;
        }
        //comparing 90 days delay
        var one_day = 1000 * 60 * 60 * 24 ;
        var numDays = Math.ceil((submit.getTime() - birtdate.getTime()) / (one_day));

        if (numDays >= 90) {
            if (numDays >= 365) {
                errormsg = errormsg + "\n" + document.getElementById('error8').value;
            } else {
                errormsg = errormsg + "\n" + document.getElementById('error7').value;
            }
            lateOrbelate = true;
        }

        element = document.getElementById('bdfSerialNo');
        if (element.value == "") {
            errormsg = errormsg + "\n" + document.getElementById('error1').value;
            flag = true;
        }
        if (!(submit.getTime())) {
            errormsg = errormsg + "\n" + document.getElementById('error9').value;
            flag = true;
        }
        if (!birtdate.getTime()) {
            errormsg = errormsg + "\n" + document.getElementById('error10').value;
            flag = true;
        }
        element = document.getElementById('placeOfBirth');
        if (element.value == "") {
            errormsg = errormsg + "\n" + document.getElementById('error11').value;
            flag = true;
        }

        if (!check.checked) {

            element = document.getElementById('childFullNameOfficialLang');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('error2').value;
                flag = true;
            }

            element = document.getElementById('childFullNameEnglish');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('error3').value;
                flag = true;
            }

            element = document.getElementById('childBirthWeight');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('error4').value;
                flag = true;
            }

            element = document.getElementById('childRank');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('error5').value;
                flag = true;
            }
        }

        if (errormsg != "") {
            alert(errormsg);
            if (flag) {
                returnval = false;
            } else {
                if (lateOrbelate) {
                    returnval = true;
                }
            }
        }

        return returnval;
    }
</script>--%>

<%--0<div class="skip-validation">
    <s:checkbox name="skipjavaScript" id="skipjs" value="false">
        <s:label value="%{getText('skipvalidation.label')}"/>
    </s:checkbox>
</div>--%>
<div class="form-submit">
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
</div>
