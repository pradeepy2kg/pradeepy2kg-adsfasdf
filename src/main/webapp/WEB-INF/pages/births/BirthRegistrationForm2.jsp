<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:set value="rowNumber" name="row"/>


<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/popreg/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>

<s:hidden id="p2error1" value="%{getText('p2.fatherName.error.value')}"/>
<s:hidden id="p2error2" value="%{getText('p2.motherName.error.value')}"/>
<s:hidden id="mother_age" value="%{getText('p2.motherAge.error.value')}"/>
<s:hidden id="mother_birth_day_empty" value="%{getText('p2.motherAge.empty.error.value')}"/>
<s:hidden id="childDateOfBirth" value="%{child.dateOfBirth}"/>

<script>

$(function() {
    $("#fatherDatePicker").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});

$(function() {
    $("#motherDatePicker").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31',
        onSelect: function(dateText, inst) {
            var child_bday = new Date(document.getElementById('childDateOfBirth').value);
            var mother_bday = document.getElementById('motherDatePicker').value;
            if (mother_bday != "") {
                var motherbday = new Date(mother_bday);
                var mother_age = child_bday.getYear() - motherbday.getYear();
                $("input#motherAgeAtBirth").val(mother_age);
            }
        }
    });
    var child_bday = new Date(document.getElementById('childDateOfBirth').value);
    var mother_bday = document.getElementById('motherDatePicker').value;
    if (mother_bday != "") {
        var motherbday = new Date(mother_bday);
        var mother_age = child_bday.getYear() - motherbday.getYear();
        $("input#motherAgeAtBirth").val(mother_age);
    }
});

$(function() {
    $("#admitDatePicker").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});

$(function() {
    $('img#father_lookup').bind('click', function(evt1) {
        var id1 = $("input#father_pinOrNic").attr("value");
        $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id1},
                function(data1) {
                    $("textarea#fatherFullName").val(data1.fullNameInOfficialLanguage);
                    $("input#fatherPlaceOfBirth").val(data1.placeOfBirth);
                    $("input#fatherDatePicker").val(data1.dateOfBirth);
                });
    });

    $('img#mother_lookup').bind('click', function(evt2) {
        var id2 = $("input#mother_pinOrNic").attr("value");
        $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id2},
                function(data2) {
                    $("textarea#motherFullName").val(data2.fullNameInOfficialLanguage);
                    $("input#motherPlaceOfBirth").val(data2.placeOfBirth);
                    $("textarea#motherAddress").val(data2.lastAddress);
                    $("input#motherDatePicker").val(data2.dateOfBirth);

                    var child_bday = new Date(document.getElementById('childDateOfBirth').value);
                    var mother_bday = document.getElementById('motherDatePicker').value;
                    if (mother_bday != "") {
                        var motherbday = new Date(mother_bday);
                        var mother_age = child_bday.getYear() - motherbday.getYear();
                        $("input#motherAgeAtBirth").val(mother_age);
                    }
                });
    });

    $('select#motherDistrictId').bind('change', function(evt3) {
        var id = $("select#motherDistrictId").attr("value");
        var label = $("input#dsDivisionLabel").attr("value");
        $.getJSON('/popreg/crs/DivisionLookupService', {id:id, mode:3},
                function(data) {
                    var options = '';
                    var ds = data.dsDivisionList;
                    options += '<option value="-1">' + label + '</option>';
                    for (var i = 0; i < ds.length; i++) {
                        options += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                    }
                    $("select#motherDSDivisionId").html(options);
                });
    });
})

//javascript for form validation
var errormsg = "";
function validate()
{
    var domObject;
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
    //validating mothers email
    domObject = document.getElementById('motherEmail');
    if (!isEmpty(domObject))
        validateEmail(domObject, 'error1')

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

function motherage() {
    var child_bday = new Date(document.getElementById('childDateOfBirth').value);
    var mother_bday = new Date(document.getElementById('motherDatePicker').value);
<%--var mother_dob = new Date(mother_bday);--%>
<%--var child_dob = new Date(child_bday);--%>
    var mother_age_at_birth = document.getElementById("motherAgeAtBirth");
    var mother_age = child_dob.getYear() - mother_dob.getYear();
    if (mother_age <= 10 || mother_bday.length == 0) {
        if (mother_bday.length == 0)
        {
            alert(document.getElementById('mother_birth_day_empty').value);

        }
        else if (confirm(document.getElementById('mother_age').value + mother_age)) {
            mother_age_at_birth.value = mother_age;
        }
        return false;
    }
    else {
        mother_age_at_birth.value = mother_age;
        return true;
    }
}

//check given element is empty
function isEmpty(domElement, errorMessage, errorCode) {
    with (domElement) {
        if (value == null || value == "") {
            errormsg = errormsg + "\n" + document.getElementById(errorCode).value + " " + errorMessage;
        }
    }
}

//check given element is empty and return true if empty else false
function isEmpty(domElement) {
    with (domElement) {
        if (value == null || value == "") {
            return true;
        } else {
            return false;
        }
    }
}

function validateEmail(domElement, errorCode) {
    with (domElement) {
        var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
        if (reg.test(value) == false) {
            errormsg = errormsg + "\n" + document.getElementById('error2').value + " : " + document.getElementById(errorCode).value;
            return false;
        }
    }
}
</script>

<div class="birth-registration-form-outer" id="birth-registration-form-2-outer">
<s:form action="eprBirthRegistration.do" name="birthRegistrationForm2" id="birth-registration-form-2" method="POST"
        onsubmit="javascript:return validate()">

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
        <td rowspan="2" width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)අනන්‍යතා අංකය
            / ජාතික හැදුනුම්පත් අංකය <br>து தனிநபர் அடையாள எண் /தேசிய
            அடையாள அட்டை
            இலக்கம்<br>PIN / NIC Number</label></td>
        <td rowspan="2" width="230px" class="find-person"><s:textfield id="father_pinOrNic"
                                                                       name="parent.fatherNICorPIN"/>
            <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;" id="father_lookup">
        </td>
        <td colspan="2" rowspan="2" width="120px"><label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
        </td>
        <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
        <td colspan="2"><s:select name="fatherCountry" list="countryList" headerKey="0"
                                  headerValue="%{getText('select_country.label')}" cssStyle="width:97%;"/>
        </td>
    </tr>
    <tr>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
        <td class="passport"><s:textfield name="parent.fatherPassportNo"/></td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_02" cellspacing="0" style="margin:-10px auto; border-top:none;">
    <tr>
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)සම්පුර්ණ නම<br>தந்தையின்
            முழு பெயர்<br>Full Name</label></td>
        <td colspan="8">
            <s:textarea name="parent.fatherFullName" id="fatherFullName" cssStyle="width:98%;"/>
        </td>
    </tr>
    <tr>
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)උපන් දිනය <br>பிறந்த
            திகதி <br>Date of Birth</label></td>
        <td colspan="2">
            <s:textfield name="parent.fatherDOB" id="fatherDatePicker"/>
        </td>
        <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)උපන් ස්ථානය <br>பிறந்த இடம்
            <br>Place of Birth</label></td>
        <td colspan="2"><s:textfield id="fatherPlaceOfBirth" name="parent.fatherPlaceOfBirth"
                                     cssStyle="width:95%;"/></td>

    </tr>
</table>

<table class="table_reg_page_02" cellspacing="0" style="border-top:none;">
    <tbody>
    <tr>
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)පියාගේ ජාතිය<br>இனம்<br>
            Father's Race</label></td>
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
        <td rowspan="2" width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)අනන්‍යතා අංකය
            / ජාතික හැදුනුම්පත් අංකය<br>து தனிநபர் அடையாள எண் /தேசிய
            அடையாள அட்டை
            இலக்கம்<br>PIN / NIC Number</label></td>
        <td colspan="2" rowspan="2" width="230px" class="find-person">
            <s:textfield id="mother_pinOrNic" name="parent.motherNICorPIN"/>
            <img src="<s:url value="/images/search-mother.png"/>" style="vertical-align:middle;" id="mother_lookup">
        </td>
        <td colspan="2" rowspan="2" width="120px"><label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
        </td>
        <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
        <td colspan="2"><s:select name="motherCountry" list="countryList" headerKey="0"
                                  headerValue="%{getText('select_country.label')}"/></td>
    </tr>
    <tr>
        <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
        <td colspan="2" class="passport"><s:textfield name="parent.motherPassportNo"/></td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_02" cellspacing="0" style="margin:0; border-top:none; border-bottom:none;">
    <tbody>
    <tr>
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)සම්පුර්ණ නම<br>தந்தையின்
            முழு பெயர்<br>Full Name</label></td>
        <td colspan="8">
            <s:textarea name="parent.motherFullName" id="motherFullName" cssStyle="width:98%;"/>
        </td>
    </tr>
    <tr>
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)උපන් දිනය <br>பிறந்த
            திகதி <br>Date of Birth</label></td>
        <td colspan="3">
                <%--<s:textfield name="parent.motherDOB" id="motherDatePicker" onchange="javascript:motherage()"/>--%>
                <s:textfield name="parent.motherDOB" id="motherDatePicker"/>
        <td colspan="3" width="100px"><label>
            <s:if test="%{#session.birthRegister.register.birthType.ordinal() != 0}">
                (<s:property value="#row"/><s:set name="row"
                                                  value="#row+1"/>) ළමයාගේ උපන් දිනට මවගේ වයස<br> பிள்ளை பிறந்த திகதியில் மாதாவின் வயது<br>Mother's Age
                as at
                the date of birth of child
            </s:if>
            <s:else>
                (<s:property value="#row"/><s:set name="row"
                                                  value="#row+1"/>) ළමයාගේ මළ උපන් දිනට මවගේ වයස<br> * Tamil<br>Mother's Age
                as at the date of still-birth of child
            </s:else>
        </label>
        </td>
        <td class="passport">
            <s:textfield name="parent.motherAgeAtBirth" id="motherAgeAtBirth"/></td>
    </tr>
    <tr style="border-bottom:none;">
        <td style="border-bottom:none;"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)මවගේ ස්ථිර
            ලිපිනය<br>தாயின் நிரந்தர வதிவிட முகவரி<br>Permanent
            Address of the Mother</label>
        </td>
        <td colspan="8" style="border-bottom:none;">
            <s:textarea id="motherAddress" name="parent.motherAddress" cssStyle="width:98%;"/>
        </td>
    </tr>

    </tbody>
</table>

<table class="table_reg_page_02" cellspacing="0" style="margin:0; border-top:none;">
    <tbody>
    <tr>
        <td width="200px" style="border-top:none; border-bottom:none;"></td>
        <td colspan="2" class="table_reg_cell_02" style="border-top:1px solid #000;"><label>දිස්ත්‍රික්කය /மாவட்டம்
            /District</label></td>
        <td colspan="6" class="table_reg_cell_02" style="border-top:1px solid #000;">
            <s:if test="#parent.motherDSDivision.district.districtUKey >0">
            <s:select id="motherDistrictId" name="motherDistrictId" list="allDistrictList" cssStyle="width:99%;"/></td>
        </s:if>
        <s:else>
            <s:select id="motherDistrictId" name="motherDistrictId" list="allDistrictList" headerKey="0"
                      headerValue="%{getText('select_district.label')}" cssStyle="width:99%;"/></td>
        </s:else>
    </tr>
    <tr>
        <td width="200px" style="border-top:none;"></td>
        <td colspan="2"><label>ප්‍රාදේශීය ලේකම් කොට්ඨාශය /பிரிவு /Divisional Secretariat</label></td>
        <td colspan="6" class="table_reg_cell_02">
            <s:if test="#parent.motherDSDivision.dsDivisionUKey > 0">
                <s:select id="motherDSDivisionId" name="motherDSDivisionId" list="allDSDivisionList"
                          headerKey="#parent.motherDSDivision.dsDivisionUKey" cssStyle="width:99%;"/>
            </s:if>
            <s:else>
                <s:select id="motherDSDivisionId" name="motherDSDivisionId" list="allDSDivisionList"
                          headerKey="0" headerValue="%{getText('select_ds_division.label')}" cssStyle="width:99%;"/>
            </s:else>
            <s:textfield id="dsDivisionLabel" value="%{getText('select_ds_division.label')}" cssStyle="display:none;"/>
        </td>
    </tr>
    <tr>
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)ම‌වගේ ජාතිය<br>இனம்<br>
            Mother's Race</label></td>
        <td colspan="2"><s:select list="raceList" name="motherRace" headerKey="0"
                                  headerValue="%{getText('select_race.label')}"/></td>

        <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)උපන් ස්ථානය <br>பிறந்த இடம்
            <br>Place of Birth</label></td>
        <td colspan="3" class="passport"><s:textfield id="motherPlaceOfBirth" name="parent.motherPlaceOfBirth"/></td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)රෝහලට ඇතුලත් කිරිමේ අංකය<br>*in
            tamil<br>Hospital Admission Number</label></td>
        <td colspan="2" class="passport"><s:textfield name="parent.motherAdmissionNo"/></td>
        <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)රෝහලට ඇතුලත් කිරිමේ
            දිනය<br>*in tamil<br>Hospital Admission Date</label></td>
        <td colspan="3"><s:textfield name="parent.motherAdmissionDate" id="admitDatePicker"/></td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)ම‌ව සම්බන්ධ කල හැකි තොරතුරු <br>தாயின்
            தொடர்பு இலக்க தகவல் <br>Contact Details of the
            Mother</label></td>
        <td colspan="1"><label>දුරකතනය <br> தொலைபேசி இலக்கம் <br> Telephone</label></td>
        <td colspan="1"><s:textfield name="parent.motherPhoneNo"/></td>
        <td colspan="2"><label>ඉ – තැපැල් <br> மின்னஞ்சல்<br>Email</label></td>
        <td colspan="3" class="passport"><s:textfield name="parent.motherEmail" id="motherEmail"/></td>
    </tr>
    </tbody>
</table>

<s:hidden id="error1" value="%{getText('p1.invalid.emailMother.text')}"/>
<s:hidden id="error2" value="%{getText('p1.invalide.inputType')}"/>

<s:hidden name="pageNo" value="2"/>
<s:hidden name="rowNumber" value="%{row}"/>
<div class="skip-validation">
    <s:checkbox name="skipjavaScript" id="skipjs" value="false">
        <s:label value="%{getText('skipvalidation.label')}"/>
    </s:checkbox>
</div>

<div class="form-submit">
    <s:submit value="%{getText('next.label')}"/>
</div>
<div class="next-previous">
    <s:url id="backUrl" action="eprBirthRegistration">
        <s:param name="back" value="true"/>
        <s:param name="pageNo" value="{pageNo - 1}"/>
    </s:url>
    <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</s:form>
</div>
<%-- Styling Completed --%>