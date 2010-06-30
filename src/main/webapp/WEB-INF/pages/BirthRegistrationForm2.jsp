<%--
  @author duminda
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-registration-form-outer" id="birth-registration-form-2-outer">
<s:form action="eprBirthRegistration.do" name="birthRegistrationForm2" id="birth-registration-form-2" method="POST"
        onsubmit="javascript:return validate()">

<script>
    function view_Info() {
        dojo.event.topic.publish("view_Info");
    }
</script>

<table class="table_reg_page_02" cellspacing="0">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td colspan="7" style="text-align:center;font-size:12pt">පි‍යාගේ විස්තර
            <br>தந்தை பற்றிய தகவல்
            <br>Details of the Father
        </td>
    </tr>
    <tr>
        <td rowspan="2"><label>(10)අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br>து தனிநபர் அடையாள எண் /தேசிய அடையாள அட்டை
            இலக்கம்<br>PIN / NIC Number</label></td>
        <td rowspan="2"><s:textfield name="parent.fatherNICorPIN"/>
            <label onclick="javascript:view_Info();return false;">>></label>
        </td>
        <td colspan="2" rowspan="2"><label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label></td>
        <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
        <td><s:select name="fatherCountry" list="countryList" headerKey="0"
                      headerValue="%{getText('select_country.label')}"/>
        </td>
    </tr>
    <tr>
        <s:url id="loadFatherInfo" action="ajaxSupport_loadFatherInfo"/>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
        <td><s:div id="fatherInfo"></s:div><sx:div id="fatherInfo" href="%{loadFatherInfo}" theme="ajax"
            listenTopics="view_Info" formId="birth-registration-form-2" preload="true"></sx:div>
        <td colspan="2"><label>(13)උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of Birth</label></td>
        <td colspan="2"><s:textfield name="parent.fatherPlaceOfBirth"/></td>
    </tr>
    <tr>
        <td><label>(14)පියාගේ ජාතිය<br>இனம்<br> Father's Race</label></td>
        <td colspan="6" class="table_reg_cell_02">
            <s:select list="raceList" name="fatherRace" headerKey="0" headerValue="%{getText('select_race.label')}"/>
        </td>
    </tr>
    </tbody>
</table>

<table style="border:none">
    <tr>
        <td></td>
    </tr>

</table>
<table style="border:none">
    <tr>
        <td></td>
    </tr>

</table>
<table style="border:none">
    <tr>
        <td></td>
    </tr>

</table>

<table class="table_reg_page_02" cellspacing="0">
    <caption></caption>
    <col/>
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
        <td colspan="9" style="text-align:center;font-size:12pt"> මවගේ විස්තර <br>தாய் பற்றிய தகவல் <br>Details of the
            Mother
        </td>
    </tr>
    <tr>
        <td rowspan="2"><label>(15)අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>து தனிநபர் அடையாள எண் /தேசிய அடையாள அட்டை
            இலக்கம்<br>PIN / NIC Number</label></td>
        <td colspan="2" rowspan="2"><s:textfield name="parent.motherNICorPIN"/></td>
        <td colspan="2" rowspan="2"><label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label></td>
        <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
        <td colspan="2"><s:select name="motherCountry" list="countryList" headerKey="0"
                                  headerValue="%{getText('select_country.label')}"/></td>
    </tr>
    <tr>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
        <td colspan="2"><s:textfield name="parent.motherPlaceOfBirth"/></td>
    </tr>
    <tr>
        <td><label>(16)සම්පුර්ණ නම<br>தந்தையின் முழு பெயர்<br>Full Name</label></td>
        <td colspan="8"><s:textarea name="parent.motherFullName" id="motherFullName"/></td>
    </tr>
    <tr>
        <td><label>(17)උපන් දිනය <br>பிறந்த திகதி <br>Date of Birth</label></td>
        <td colspan="3"><sx:datetimepicker id="motherDatePicker" name="parent.motherDOB" displayFormat="yyyy-MM-dd"
                                           onmouseover="javascript:splitDate('motherDatePicker')"/></td>
        <td colspan="2"><label>(18)උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of Birth</label></td>
        <td colspan="3"><s:textfield name="parent.motherPlaceOfBirth"/></td>
    </tr>
    <tr>
        <td><label>(19)ම‌වගේ ජාතිය<br>இனம்<br> Mother's Race</label></td>
        <td colspan="3"><s:select list="raceList" name="motherRace" headerKey="0"
                                  headerValue="%{getText('select_race.label')}"/></td>
        <td colspan="4"><label>(20) ළමයාගේ උපන් දිනට මවගේ වයස<br> பிள்ளை பிறந்த திகதியில் மாதாவின் வயது<br>Mother's Age
            as at
            the date of birth of child </label></td>
        <td><s:textfield name="parent.motherAgeAtBirth"/></td>
    </tr>
    <tr>
        <td rowspan="3"><label>(21)මවගේ ස්ථිර ලිපිනය<br>தாயின் நிரந்தர வதிவிட முகவரி<br>Permanent Address of the Mother</label>
        </td>
        <td colspan="2" class="table_reg_cell_02"><label>*in Sinhala/*in English/District</label></td>
        <td colspan="6" class="table_reg_cell_02"><s:select name="motherDistrictId" list="allDistrictList" headerKey="0"
                                                            headerValue="%{getText('select_district.label')}"/></td>
    </tr>
    <tr>

        <td colspan="2"><label>*in Sinhala/*in English/D.S Division</label></td>
        <td colspan="7" class="table_reg_cell_02"><s:select name="motherDSDivisionId" list="allDSDivisionList"
                                                            headerKey="0"
                                                            headerValue="%{getText('select_ds_division.label')}"/></td>
    </tr>
    <tr>

        <td colspan="8"><s:textarea name="parent.motherAddress"/></td>
    </tr>
    <tr>
        <td><label>(22)රෝහලට ඇතුලත් කිරිමේ අංකය<br>*in tamil<br>Hospital Admission Number</label></td>
        <td colspan="3"><s:textfield name="parent.motherAdmissionNo"/></td>
        <td colspan="2"><label>(23)රෝහලට ඇතුලත් කිරිමේ දිනය<br>*in tamil<br>Hospital Admission Date</label></td>
        <td colspan="3"><sx:datetimepicker id="admitDatePicker" name="parent.motherAdmissionDate"
                                           displayFormat="yyyy-MM-dd"
                                           onmouseover="javascript:splitDate('admitDatePicker')"/></td>
    </tr>
    <tr>
        <td><label>(24)ම‌ව සම්බන්ධ කල හැකි තොරතුරු <br>தாயின் தொடர்பு இலக்க தகவல் <br>Contact Details of the
            Mother</label></td>
        <td><label>දුරකතනය <br> தொலைபேசி இலக்கம் <br> Telephone</label></td>
        <td colspan="3"><s:textfield name="parent.motherPhoneNo"/></td>
        <td colspan="2"><label>ඉ – තැපැල් <br> மின்னஞ்சல்<br>Email</label></td>
        <td colspan="2"><s:textfield name="parent.motherEmail"/></td>
    </tr>
    </tbody>
</table>

<s:hidden name="pageNo" value="2"/>

<s:hidden id="p2error1" value="%{getText('p2.fatherName.error.value')}"/>
<s:hidden id="p2error2" value="%{getText('p2.motherName.error.value')}"/>

<script type="text/javascript">
    function validate()
    {
        var errormsg = "";
        var element;
        var returnval;
        var check = document.getElementById('skipjs');
        if (!check.checked) {

            element = document.getElementById('fatherFullName');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('p2error1').value;
            }
            element = document.getElementById('motherFullName');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('p2error2').value;
            }
        }
        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        return returnval;
    }
</script>
</div>

<div class="form-submit">
    <table style="border:none; margin-bottom:20px;" align="center" class="form-submit">
        <s:url id="backUrl" action="eprBirthRegistration">
            <s:param name="back" value="true"/>
            <s:param name="pageNo" value="{pageNo - 1}"/>
        </s:url>
        <s:a href="%{backUrl}"> << </s:a>
        <s:checkbox name="skipjavaScript" id="skipjs" value="false">
            <s:label value="%{getText('skipvalidation.label')}"/>
        </s:checkbox>
        <s:submit value="%{getText('next.label')}"/>
    </table>
</div>
</s:form>