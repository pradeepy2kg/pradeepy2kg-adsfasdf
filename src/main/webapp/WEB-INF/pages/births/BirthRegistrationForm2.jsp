<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:set value="rowNumber" name="row"/>

<s:if test="birthType.ordinal()==0">
    <%--still birth--%>
    <s:set name="row" value="8"/>
</s:if>
<s:elseif test="birthType.ordinal()==1">
    <%--live birth--%>
    <s:set name="row" value="10"/>
</s:elseif>
<s:elseif test="birthType.ordinal()==2">
    <%--adoption--%>
    <s:set name="row" value="12"/>
</s:elseif>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>

<s:hidden id="p2error1" value="%{getText('p2.fatherName.error.value')}"/>
<s:hidden id="p2error2" value="%{getText('p2.motherName.error.value')}"/>
<s:hidden id="mother_age" value="%{getText('p2.motherAge.error.value')}"/>
<s:hidden id="mother_birth_day_empty" value="%{getText('p2.motherAge.empty.error.value')}"/>
<s:hidden id="childDateOfBirth" value="%{child.dateOfBirth}"/>


<script>

$(function() {
    var currentYear = new Date().getFullYear();
    var id1 = $("input#father_pinOrNic").attr("value");


    $("#fatherDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31',
        defaultDate:-365 * 18

    });
})
        ;

$(function() {
    $("#motherDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
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
        },
        defaultDate:-365 * 18
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
    var currentYear = new Date().getFullYear();

    $('img#father_lookup').bind('click', function(evt1) {
        var id1 = $("input#father_pinOrNic").attr("value");
        var regNIC = /^([0-9]{9}[X|x|V|v])$/;
        var day = id1.substring(2, 5);
        if ((id1.search(regNIC) == 0 && (day >= 0 && day <= 367))) {
            var fatherBirthYear = 19 + id1.substring(0, 2);
            var D = new Date(fatherBirthYear, 01, 01) ;
            D.setDate(D.getDate() + id1.substring(2, 5) - 1000);
            $('#fatherDatePicker').datepicker('setDate', new Date(D.getYear(), D.getMonth() - 1, D.getDate() - 1));
        } else {
            alert("enter valid NIC for father")
        }
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                function(data1) {
                    $("textarea#fatherFullName").val(data1.fullNameInOfficialLanguage);
                    $("input#fatherPlaceOfBirth").val(data1.placeOfBirth);
                    $("input#fatherDatePicker").val(data1.dateOfBirth);

                });
    });

    $('img#mother_lookup').bind('click', function(evt2) {
        var id2 = $("input#mother_pinOrNic").attr("value");
        var regNIC = /^([0-9]{9}[X|x|V|v])$/;
        var day = id2.substring(2, 5);
        if ((id2.search(regNIC) == 0) && (day >= 501 && day <= 867)) {
            var motherBirthYear = 19 + id2.substring(0, 2);
            var D = new Date(motherBirthYear, 01, 01) ;
            D.setDate(D.getDate() + id2.substring(2, 5) - 1500);
            $('#motherDatePicker').datepicker('setDate', new Date(D.getYear(), D.getMonth() - 1, D.getDate() - 1));
        } else {
            alert("enter valid NIC for Mother")
        }
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id2},
                function(data2) {
                    $("textarea#motherFullName").val(data2.fullNameInOfficialLanguage);
                    $("input#motherPlaceOfBirth").val(data2.placeOfBirth);
                    $("textarea#motherAddress").val(data2.lastAddress);
                    $("input#motherDatePicker").val(data2.dateOfBirth);


                });
    });

    $('#mother_lookup').click(function() {
        var child_bday = new Date(document.getElementById('childDateOfBirth').value);
        var mother_bday = document.getElementById('motherDatePicker').value;
        if (mother_bday != "") {
            var motherbday = new Date(mother_bday);
            var mother_age = child_bday.getYear() - motherbday.getYear();
            $("input#motherAgeAtBirth").val(mother_age);
        }
    });

    $('select#motherDistrictId').bind('change', function(evt3) {
        var id = $("select#motherDistrictId").attr("value");
        var label = $("input#dsDivisionLabel").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:3},
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
function validate() {
    var domObject;
    var returnval;
    var check = document.getElementById('skipjs');

    commonTags();

    if (!check.checked) {
        // validate father full name
        domObject = document.getElementById('fatherFullName');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'p2error1');
        }

        // validate mother full name
        domObject = document.getElementById('motherFullName');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'p2error2');
        }

        // validate mother date of birth
        domObject = document.getElementById('motherDatePicker');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'mother_birth_day_empty');
        }

        // validate mother age at birth
        domObject = document.getElementById('motherAgeAtBirth');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'mother_age');
        }

        validateBirthYearWithNIC("father_pinOrNic", "fatherDatePicker", "error7");
        validateBirthYearWithNIC("mother_pinOrNic", "motherDatePicker", "error8");
    }

    // validate mother phone number
    domObject = document.getElementById('motherPhoneNo');
    if (!isFieldEmpty(domObject))
        validatePhoneNo(domObject, 'error2', 'error6')

    // validate mother email address
    domObject = document.getElementById('motherEmail');
    if (!isFieldEmpty(domObject))
        validateEmail(domObject, 'error2', 'error1')

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

// check parent age is between 10 to 89
function checkParentAge(domValue, errorText, errorCode) {
    var reg = /^[1-8][0-9]$/;
    if (reg.test(domValue) == false) {
        printMessage(errorText, errorCode);
    }
}

function checkFatherAge(domElement, errorText, errorCode) {
    with (domElement) {
        var father_bday = new Date(value);
        var today = new Date();
        var father_age = today.getYear() - father_bday.getYear();
        checkParentAge(father_age, errorText, errorCode);
    }
}

function motherAgeBirth() {
    var child_bday = new Date(document.getElementById('childDateOfBirth').value);
    var mother_bday = new Date(document.getElementById('motherDatePicker').value);
    var mother_age = child_bday.getYear() - mother_bday.getYear();
    document.getElementById("motherAgeAtBirth").value = mother_age;
    if (mother_age <= 0) {
        document.getElementById("motherAgeAtBirth").value = 0;
    }
}

function initPage() {

}

function commonTags() {
    var domObject;

    // validate father PIN or NIC
    domObject = document.getElementById('father_pinOrNic');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'error2', 'error4');

    // validate father date of birth
    domObject = document.getElementById('fatherDatePicker');
    if (!isFieldEmpty(domObject))
        checkFatherAge(domObject, 'error2', 'fatherDOB');

    // validate mother PIN or NIC
    domObject = document.getElementById('mother_pinOrNic');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'error2', 'error5');

    // validate mother date of birth
    domObject = document.getElementById('motherDatePicker');
    if (!isFieldEmpty(domObject)) {
        isDate(domObject.value, "error2", "motherDOB");
    }

    // validate mother age at birth
    domObject = document.getElementById('motherAgeAtBirth');
    if (!isFieldEmpty(domObject))
        checkParentAge(domObject.value, 'error2', 'mother_age');

    // validate hospital addmission date
    domObject = document.getElementById('admitDatePicker');
    if (!isFieldEmpty(domObject)) {
        isDate(domObject.value, "error2", "dateOfAddmission");
        var submit = new Date(document.getElementById('birthDatePicker').value);
        domObject = new Date(domObject.value);
        if (domObject.getTime() > submit.getTime()) {
            errormsg = errormsg + "\n" + document.getElementById('dateOfAddmissionWrong').value;
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
        <td colspan="9" style="text-align:center;font-size:12pt;">පියාගේ විස්තර
            <br>தந்தை பற்றிய தகவல்
            <br>Details of the Father
        </td>
    </tr>
    <tr>
        <td rowspan="2" width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set
                name="i" value="#i+1"/>)අනන්‍යතා අංකය
            / ජාතික හැඳුනුම්පත් අංකය <br>து தனிநபர் அடையாள எண் /தேசிய
            அடையாள அட்டை
            இலக்கம்<br>PIN / NIC Number</label></td>
        <td rowspan="2" width="230px" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="father_NIC_V" onclick="javascript:addXorV('father_pinOrNic','V','error9')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="father_NIC_X" onclick="javascript:addXorV('father_pinOrNic','X','error9')">
            <br>
            <s:textfield id="father_pinOrNic" name="parent.fatherNICorPIN"/>
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
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                     value="#i+1"/>)සම්පුර්ණ
            නම<br>தந்தையின்
            முழு பெயர்<br>Full Name</label></td>
        <td colspan="8">
            <s:textarea name="parent.fatherFullName" id="fatherFullName" cssStyle="width:98%;"/>
        </td>
    </tr>
    <tr>
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                     value="#i+1"/>)උපන්
            දිනය <br>பிறந்த
            திகதி <br>Date of Birth</label></td>
        <td colspan="2">
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:180px;font-size:10px"/><br>
            <s:textfield name="parent.fatherDOB" id="fatherDatePicker"/>
        </td>
        <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                   value="#i+1"/>)උපන්
            ස්ථානය <br>பிறந்த இடம்
            <br>Place of Birth</label></td>
        <td colspan="2"><s:textfield id="fatherPlaceOfBirth" name="parent.fatherPlaceOfBirth"
                                     cssStyle="width:95%;"/></td>

    </tr>
</table>

<table class="table_reg_page_02" cellspacing="0" style="border-top:none;">
    <tbody>
    <tr>
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                     value="#i+1"/>)පියාගේ
            ජාතිය<br>இனம்<br>
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
        <td rowspan="2" width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set
                name="i" value="#i+1"/>)අනන්‍යතා අංකය
            / ජාතික හැඳුනුම්පත් අංකය<br>து தனிநபர் அடையாள எண் /தேசிய
            அடையாள அட்டை
            இலக்கம்<br>PIN / NIC Number</label></td>
        <td colspan="2" rowspan="2" width="230px" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="mother_NIC_V" onclick="javascript:addXorV('mother_pinOrNic','V','error9')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="mother_NIC_X" onclick="javascript:addXorV('mother_pinOrNic','X','error9')">
            <br>
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
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                     value="#i+1"/>)සම්පුර්ණ
            නම<br>தந்தையின்
            முழு பெயர்<br>Full Name</label></td>
        <td colspan="8">
            <s:textarea name="parent.motherFullName" id="motherFullName" cssStyle="width:98%;"/>
        </td>
    </tr>
    <tr>
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                     value="#i+1"/>)උපන්
            දිනය <br>பிறந்த
            திகதி <br>Date of Birth</label></td>
        <td colspan="3">
                <%--<s:textfield name="parent.motherDOB" id="motherDatePicker" onchange="javascript:motherage()"/>--%>
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:140px;font-size:10px"/><br>
                <s:textfield name="parent.motherDOB" id="motherDatePicker"/>
        <td colspan="3" width="100px"><label>
            <s:if test="%{#session.birthRegister.register.birthType.ordinal() != 0}">
                (<s:property value="#row"/><s:set name="row"
                                                  value="#row+1"/><s:set name="i"
                                                                         value="#i+1"/>) ළමයාගේ උපන් දිනට මවගේ වයස<br> பிள்ளை பிறந்த திகதியில் மாதாவின் வயது<br>Mother's Age
                as at
                the date of birth of child
            </s:if>
            <s:else>
                (<s:property value="#row"/><s:set name="row"
                                                  value="#row+1"/><s:set name="i"
                                                                         value="#i+1"/>) ළමයාගේ මළ උපන් දිනට මවගේ වයස<br> * Tamil<br>Mother's Age
                as at the date of still-birth of child
            </s:else>
        </label>
        </td>
        <td class="passport">
            <s:textfield name="parent.motherAgeAtBirth" id="motherAgeAtBirth" onfocus="motherAgeBirth();"/>
            <div id="motherAgeAtChildBirth" style="color:red;"/>
        </td>

    </tr>
    <tr style="border-bottom:none;">
        <td style="border-bottom:none;"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set
                name="i" value="#i+1"/>)මවගේ ස්ථිර
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
        <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                     value="#i+1"/>
            )ම‌වගේ ජාතිය<br>இனம்<br>
            Mother's Race</label></td>
        <td colspan="2"><s:select list="raceList" name="motherRace" headerKey="0"
                                  headerValue="%{getText('select_race.label')}"/></td>

        <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                   value="#i+1"/>
            )උපන් ස්ථානය <br>பிறந்த இடம்
            <br>Place of Birth</label></td>
        <td colspan="3" class="passport"><s:textfield id="motherPlaceOfBirth" name="parent.motherPlaceOfBirth"/></td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/> <s:set name="i" value="#i+1"/>)රෝහලට
            ඇතුලත් කිරිමේ අංකය<br>*in
            tamil<br>Hospital Admission Number</label></td>
        <td colspan="2" class="passport"><s:textfield name="parent.motherAdmissionNo"/></td>
        <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/><s:set name="i"
                                                                                                   value="#i+1"/>
            )රෝහලට ඇතුලත් කිරිමේ
            දිනය<br>*in tamil<br>Hospital Admission Date</label></td>
        <td colspan="3">
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
            <s:textfield name="parent.motherAdmissionDate" id="admitDatePicker" cssStyle="float:left;margin-left:5px;"/>
        </td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/> <s:set name="i" value="#i+1"/>)ම‌ව
            සම්බන්ධ
            කල හැකි තොරතුරු <br>தாயின்
            தொடர்பு இலக்க தகவல் <br>Contact Details of the
            Mother</label></td>
        <td colspan="1"><label>දුරකතනය <br> தொலைபேசி இலக்கம் <br> Telephone</label></td>
        <td colspan="1"><s:textfield id="motherPhoneNo" name="parent.motherPhoneNo"/></td>
        <td colspan="2"><label>ඉ – තැපැල් <br> மின்னஞ்சல்<br>Email</label></td>
        <td colspan="3" class="passport"><s:textfield name="parent.motherEmail" id="motherEmail"
                                                      cssStyle="text-transform:none;"/></td>
    </tr>
    </tbody>
</table>

<s:hidden name="pageNo" value="2"/>
<s:hidden name="rowNumber" value="%{row}"/>
<s:hidden name="counter" value="%{i}"/>
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
<s:set value="0" name="counter"/>
</s:form>

<s:hidden id="error1" value="%{getText('p1.invalid.emailMother.text')}"/>
<s:hidden id="error2" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error3" value="%{getText('p2.motherAgeAthBirthBelowZero.error')}"/>
<s:hidden id="error4" value="%{getText('fatherPINorNIC.label')}"/>
<s:hidden id="error5" value="%{getText('motherPINorNIC.label')}"/>
<s:hidden id="error6" value="%{getText('motherPhoneNo.label')}"/>
<s:hidden id="error7" value="%{getText('p2.father.NIC.DOB.notMatch')}"/>
<s:hidden id="error8" value="%{getText('p2.mother.NIC.DOB.notMatch')}"/>
<s:hidden id="fatherDOB" value="%{getText('p2.father.dob')}"/>
<s:hidden id="motherDOB" value="%{getText('p2.mother.dob')}"/>
<s:hidden id="dateOfAddmission" value="%{getText('p2.hospital.addmission.date')}"/>
<s:hidden id="dateOfAddmissionWrong" value="%{getText('p2.hospital.addmission.date.wrong')}"/>
<s:hidden id="birthDatePicker" value="%{child.dateOfBirth}"/>
<s:hidden id="error9" value="%{getText('NIC.error.add.VX')}"/>
<s:hidden id="error10" value="%{getText('p2.NIC.error.format.father')}"/>
<s:hidden id="error11" value="%{getText('p2.NIC.error.format.mother')}"/>


</div>
<%-- Styling Completed --%>