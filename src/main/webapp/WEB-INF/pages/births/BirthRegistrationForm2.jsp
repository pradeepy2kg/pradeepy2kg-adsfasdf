<%--
  @author duminda
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script>
    function view_DSDivs() {
        dojo.event.topic.publish("view_DSDivs");
    }

    function view_BDDivs() {
        dojo.event.topic.publish("view_BDDivs");
    }
</script>
<s:url id="loadDSDivList" action="../ajaxSupport_loadMotherDSDivList"/>

<div class="birth-registration-form-outer" id="birth-registration-form-2-outer">
<s:form action="eprBirthRegistration.do" name="birthRegistrationForm2" id="birth-registration-form-2" method="POST"
        onsubmit="javascript:return validate()">

<script>
    function view_FatherInfo() {
        dojo.event.topic.publish("view_FatherInfo");
    }

    function view_MotherInfo() {
        dojo.event.topic.publish("view_MotherInfo");
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
    <col/>
    <col/>
    <tbody>
    <tr>
        <td colspan="9" style="text-align:center;font-size:12pt">පියාගේ විස්තර
            <br>தந்தை பற்றிய தகவல்
            <br>Details of the Father
        </td>
    </tr>
    <tr>
        <td rowspan="2" width="200px"><label>(10)අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br>து தனிநபர் அடையாள எண் /தேசிய
            அடையாள அட்டை
            இலக்கம்<br>PIN / NIC Number</label></td>
        <td rowspan="2" width="230px" class="find-person"><s:textfield name="parent.fatherNICorPIN"/>
            <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;"
                 onclick="javascript:view_FatherInfo();return false;">
        </td>
        <td colspan="2" rowspan="2" width="120px"><label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
        </td>
        <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
        <td colspan="2"><s:select name="fatherCountry" list="countryList" headerKey="0"
                                  headerValue="%{getText('select_country.label')}" cssStyle="width:97%;"/>
        </td>
    </tr>
    <s:url id="loadFatherInfo" action="../ajaxSupport_loadFatherInfo"/>
    <tr>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
        <td class="passport"><s:textfield name="parent.fatherPassportNo"/></td>
    </tr>
    </tbody>
    </table>

    <sx:div id="test" href="%{loadFatherInfo}" listenTopics="view_FatherInfo" formId="birth-registration-form-2"
            theme="ajax"/>

    <table  class="table_reg_page_02" cellspacing="0" style="border-top:none;">
    <tbody>
    <tr>
        <td width="200px"><label>(14)පියාගේ ජාතිය<br>இனம்<br> Father's Race</label></td>
        <td colspan="6" class="table_reg_cell_02">
            <s:select list="raceList" name="fatherRace" headerKey="0" headerValue="%{getText('select_race.label')}"
                      cssStyle="width:200px;"/>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_02" cellspacing="0" style="margin:0;">
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
        <td rowspan="2" width="200px"><label>(15)අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>து தனிநபர் அடையாள எண் /தேசிய
            அடையாள அட்டை
            இலக்கம்<br>PIN / NIC Number</label></td>
        <td colspan="2" rowspan="2" width="230px" class="find-person"><s:textfield name="parent.motherNICorPIN"/>
            <img src="<s:url value="/images/search-mother.png"/>" style="vertical-align:middle;"
                 onclick="javascript:view_MotherInfo();return false;">
        </td>
        <td colspan="2" rowspan="2" width="120px"><label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
        </td>
        <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
        <td colspan="2"><s:select name="motherCountry" list="countryList" headerKey="0"
                                  headerValue="%{getText('select_country.label')}" /></td>
    </tr>
    <tr>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
        <td colspan="2" class="passport"><s:textfield name="parent.motherPassportNo"/></td>
    </tr>
    <tr>
        <td><label>(19)ම‌වගේ ජාතිය<br>இனம்<br> Mother's Race</label></td>
        <td colspan="3"><s:select list="raceList" name="motherRace" headerKey="0"
                                  headerValue="%{getText('select_race.label')}"/></td>

        <td colspan="3"><label>(20)උපන් ස්ථානය <br>பிறந்த இடம் <br>Place of Birth</label></td>
        <td colspan="3" class="passport"><s:textfield name="parent.motherPlaceOfBirth"/></td>
    </tr>
    <tr>
        <td rowspan="3"><label>(21)මවගේ ස්ථිර ලිපිනය<br>தாயின் நிரந்தர வதிவிட முகவரி<br>Permanent Address of the Mother</label>
        </td>
        <td colspan="2" class="table_reg_cell_02"><label>*in Sinhala/*in English/District</label></td>
        <td colspan="6" class="table_reg_cell_02">
            <s:select name="motherDistrictId" list="allDistrictList" headerKey="0"
                      headerValue="%{getText('select_district.label')}"
                      onchange="javascript:view_DSDivs();return false;" cssStyle="width:99%;"/></td>
    </tr>
    <tr>

        <td colspan="2"><label>*in Sinhala/*in English/D.S Division</label></td>
        <td colspan="7" class="table_reg_cell_02">
            <sx:div id="motherDSDivisionId" value="motherDSDivisionId" href="%{loadDSDivList}" theme="ajax"
                    listenTopics="view_DSDivs" formId="birth-registration-form-2"></sx:div>
        </td>
    </tr>
    </tbody>
    </table>

    <s:url id="loadMotherInfo" action="../ajaxSupport_loadMotherInfo"/>
    <sx:div id="parent.motherNICorPIN" href="%{loadMotherInfo}"
            listenTopics="view_MotherInfo" formId="birth-registration-form-2" theme="ajax"/>

  <table class="table_reg_page_02" cellspacing="0" style="margin:0;">
    <tbody>
    <tr>
        <td><label>(22)රෝහලට ඇතුලත් කිරිමේ අංකය<br>*in tamil<br>Hospital Admission Number</label></td>
        <td colspan="3" class="passport"><s:textfield name="parent.motherAdmissionNo"/></td>
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
        <td colspan="2" class="passport"><s:textfield name="parent.motherEmail"/></td>
    </tr>
    </tbody>
  </table>

<s:hidden name="pageNo" value="2"/>

<s:hidden id="p2error1" value="%{getText('p2.fatherName.error.value')}"/>
<s:hidden id="p2error2" value="%{getText('p2.motherName.error.value')}"/>
<s:hidden id="mother_age" value="%{getText('p2.motherAge.error.value')}"/>
<s:hidden id="mother_birth_day_empty" value="%{getText('p2.motherAge.empty.error.value')}"/>

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
    function motherage(child_bday) {
        var mother_dob = dojo.widget.byId('motherDatePicker').inputNode.value;
        var check_mother_dob = mother_dob;
        mother_dob = new Date(mother_dob);
        var child_dob = new Date(child_bday);
        var mother_age_at_birth = document.getElementById("motherAgeAtBirth");
        var mother_age = 100 - (mother_dob.getYear() - child_dob.getYear());
        if (mother_age <= 10 || check_mother_dob.length == 0) {
            if(check_mother_dob.length == 0)
            {
                alert(document.getElementById('mother_birth_day_empty').value);
                return false;
            }
           else if (confirm(document.getElementById('mother_age').value + mother_age)) {
                mother_age_at_birth.value = mother_age;
                return false;
            }

        }
        else{
             mother_age_at_birth.value = mother_age;
            return true;
        }
    }
</script>

<div class="skip-validation">
    <s:checkbox name="skipjavaScript" id="skipjs" value="false">
        <s:label value="%{getText('skipvalidation.label')}"/>
    </s:checkbox>
</div>

<div class="form-submit">
    <s:submit value="%{getText('next.label')}"/>
</div>
<div class="next-previous">
    <s:if test="liveBirth">
        <s:set name="pageNo" value="0"/>
    </s:if>
    <s:else>
        <s:set name="pageNo" value="5"/>
    </s:else>

    <s:url id="backUrl" action="eprBirthRegistration">
        <s:param name="back" value="true"/>
        <s:param name="pageNo" value="#pageNo"/>
    </s:url>
    <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</s:form>
</div>
<%-- Styling Completed --%>