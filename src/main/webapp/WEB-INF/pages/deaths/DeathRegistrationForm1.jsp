<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    $(document).ready(function() {

        if ($('#deathOccurAthospitaltrue').is(':checked')) {
            $(".no_hospital").hide();
            $(".yes_hospital").show();

        } else if ($('#deathOccurAthospitalfalse').is(':checked')) {
            $(".yes_hospital").hide();
            $(".no_hospital").show();
        }

        $('input[type="radio"]').click(function() {
            if ($(this).attr("value") == "true") {
                $(".no_hospital").hide();
                $(".yes_hospital").show();
                $("#placeOfDeath").val('');
                $("#placeOfDeathInEnglish").val('');
            }
            if ($(this).attr("value") == "false") {
                $(".yes_hospital").hide();
                $(".no_hospital").show();
                $("#placeOfDeathInEnglish").val('');
            }
        });
    });
</script>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/timePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/agecalculator.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<s:if test="pageType == 0">
    <s:set name="row" value="1"/>
</s:if>
<s:elseif test="pageType == 1">
    <s:set name="row" value="2"/>
</s:elseif>
<s:elseif test="pageType == 2">
    <s:set name="row" value="1"/>
</s:elseif>
<s:elseif test="pageType == 3">
    <s:set name="row" value="1"/>
</s:elseif>
<s:hidden id="pageType" value="%{pageType}"/>
<script type="text/javascript">
$(function() {
    $('#clearWomanInfo').val($('#clear').val());
    $('#clearWomanInfo').bind('click', function() {
        $('#pregnantAtTimeOfDeathYestrue').attr('checked', false);
        $('#pregnantAtTimeOfDeathNofalse').attr('checked', false);
        $('#givenABirthWithInPreviouse6WeeksYestrue').attr('checked', false);
        $('#givenABirthWithInPreviouse6WeeksNofalse').attr('checked', false);
        $('#anAbortionTakenPlaceYestrue').attr('checked', false);
        $('#anAbortionTakenPlaceNofalse').attr('checked', false);
        $('#days_before_abortion_or_birth').val('');
    });
});

$(function () {
    $("#timePicker").cantipi({size:140, roundto:5});
});

$(function () {
    $("#deathDatePicker").datepicker({
        changeYear:true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});

$(function () {
    $("#dateOfRegistrationDatePicker").datepicker({
        changeYear:true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});


$(function () {
    $("#deathPersonDOB").datepicker({
        changeYear:true,
        dateFormat:'yy-mm-dd',
        startDate:'1920-01-01',
        endDate:'2020-12-31'
    });
});

// mode 1 = passing District, will return DS list
// mode 2 = passing DsDivision, will return BD list
// any other = passing district, will return DS list and the BD list for the first DS
$(function () {
    $('select#deathDistrictId').bind('change', function (evt1) {
        var id = $("select#deathDistrictId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
                function (data) {
                    var options1 = '';
                    var ds = data.dsDivisionList;
                    for (var i = 0; i < ds.length; i++) {
                        options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                    }
                    $("select#deathDsDivisionId").html(options1);

                    var options2 = '';
                    var bd = data.bdDivisionList;
                    for (var j = 0; j < bd.length; j++) {
                        options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                    }
                    $("select#deathDivisionId").html(options2);


                    var options3 = '';
                    var gn = data.gnDivisionList;
                    for (var k = 0; k < gn.length; k++) {
                        options3 += '<option value="' + gn[k].optionValue + '">' + gn[k].optionDisplay + '</option>';
                    }
                    $("select#gnDivisionId").html(options3);

                    var options4 = '';
                    var hos = data.hospitalList;
                    for (var l = 0; l < hos.length; l++) {
                        options4 += '<option value ="' + hos[l].optionValue + '">' + hos[l].optionDisplay + '</option>';
                    }
                    $("select#hospitalId").html(options4);


                });
    });


    /* $('select#deathDsDivisionId').bind('change', function(evt3) {
     var id = $("select#dsDivisionId").attr("value");
     $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:20},
     function(data) {
     var options = '';
     var hospital = data.hospitalList;
     for (var i = 0; i < hospital.length; i++) {
     options += '<option value="' + hospital[i].optionValue + '">' + hospital[i].optionDisplay + '</option>';
     }
     $("select#hospitalId").html(options);
     })
     });*/

    $('select#deathPersonPermenentAddressDistrictId').bind('change', function (evt1) {
        var id = $("select#deathPersonPermenentAddressDistrictId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:16},
                function (data) {
                    var options1 = '';
                    var ds = data.dsDivisionList;
                    var dsDivisionsSelect = document.getElementById('selectDSDivision').value;
                    options1 += '<option value="' + 0 + '">' + dsDivisionsSelect + '</option>';
                    for (var i = 0; i < ds.length; i++) {
                        options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                    }
                    $("select#deathPersonPermenentAddressDSDivisionId").html(options1);


                    var options3 = '';
                    var gn = data.gnDivisionList;
                    var gnDivisionsSelect = document.getElementById('selectGNDivision').value;
                    options3 += '<option value="' + 0 + '">' + gnDivisionsSelect + '</option>';
                    for (var k = 0; k < gn.length; k++) {
                        options3 += '<option value="' + gn[k].optionValue + '">' + gn[k].optionDisplay + '</option>';
                    }
                    $("select#deathPersonPermanentAddressGNDivision").html(options3);
                });
    });


    $('select#deathDsDivisionId').bind('change', function (evt2) {
        var id = $("select#deathDsDivisionId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                function (data) {
                    var options = '';
                    var bd = data.bdDivisionList;
                    for (var i = 0; i < bd.length; i++) {
                        options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                    }
                    $("select#deathDivisionId").html(options);

                    var options2 = '';
                    var hospital = data.hospitalList;
                    for (var j = 0; j < hospital.length; j++) {
                        options2 += '<option value="' + hospital[j].optionValue + '">' + hospital[j].optionDisplay + '</option>';
                    }
                    $("select#hospitalId").html(options2);


                });
    });

    $('select#deathPersonPermenentAddressDSDivisionId').bind('change', function (evt2) {
        var id = $("select#deathPersonPermenentAddressDSDivisionId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                function (data) {

                    var options4 = '';
                    var gn = data.gnDivisionList;
                    var dsDivisionsSelect = document.getElementById('selectGNDivision').value;
                    options4 += '<option value="' + 0 + '">' + dsDivisionsSelect + '</option>';
                    for (var k = 0; k < gn.length; k++) {
                        options4 += '<option value="' + gn[k].optionValue + '">' + gn[k].optionDisplay + '</option>';
                    }
                    $("select#deathPersonPermanentAddressGNDivision").html(options4);

                });
    });
    /**
     *
     fatherNameInOfficialLang fatherIdentificationNumber
     motherNameInOfficialLang motherIdentificationNumber
     */
    $('img#death_person_lookup').bind('click', function (evt3) {
        var id1 = $("input#deathPerson_PINorNIC").attr("value");
        var datePicker = $('#deathPersonDOB');
        var error = "error message for invalid date of birth";

        $("textarea#deathPersonNameOfficialLang").val('');
        $("textarea#deathPersonNameInEnglish").val('');
        $("input#deathPersonDOB").val('');
        $("select#deathPersonGender").val('');
        $("select#deathPersonRace").val('');
        $("textarea#deathPersonPermanentAddress").val('');
        $("input#deathPersonFather_PINorNIC").val('');
        $("textarea#deathPersonFatherFullName").val('');
        $("input#deathPersonMother_PINorNIC").val('');
        $("textarea#deathPersonMotherFullName").val('');
        datePicker.datepicker('setDate', calculateBirthDay(id1, error));
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                function (data1) {
                    if (data1 != null) {
                        $("textarea#deathPersonNameOfficialLang").val(data1.fullNameInOfficialLanguage);
                        $("textarea#deathPersonNameInEnglish").val(data1.fullNameInEnglishLanguage);
                        $("input#deathPersonDOB").val(data1.dateOfBirth);
                        $("select#deathPersonGender").val(data1.gender);
                        $("select#deathPersonRace").val(data1.race);
                        $("textarea#deathPersonPermanentAddress").val(data1.address);
                        $("input#deathPersonFather_PINorNIC").val(data1.fatherIdentificationNumber);
                        $("textarea#deathPersonFatherFullName").val(data1.fatherNameInOfficialLang);
                        $("input#deathPersonMother_PINorNIC").val(data1.motherIdentificationNumber);
                        $("textarea#deathPersonMotherFullName").val(data1.motherNameInOfficialLang);
                    }
                });
        personAgeDeath();
    });
    $('img#death_person_father_lookup').bind('click', function (evt4) {
        var id2 = $("input#deathPersonFather_PINorNIC").attr("value");

        $("textarea#deathPersonFatherFullName").val('');
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id2},
                function (data2) {
                    if (data2 != null) {
                        $("textarea#deathPersonFatherFullName").val(data2.fullNameInOfficialLanguage);
                    }
                });
    });
    $('img#death_person_mother_lookup').bind('click', function (evt5) {
        var id3 = $("input#deathPersonMother_PINorNIC").attr("value");

        $("textarea#deathPersonMotherFullName").val('');
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id3},
                function (data3) {
                    if (data3 != null) {
                        $("textarea#deathPersonMotherFullName").val(data3.fullNameInOfficialLanguage);
                    }
                });
    });
});
var errormsg = "";
function validate() {
    var domObject;
    var returnval;
    var check = document.getElementById('skipjs');
    var declarationType = document.getElementById('pageType');

    //validation for emptry fields
    domObject = document.getElementById('deathSerialNo');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error0').value;
    } else {
        validateSerialNo(domObject, 'p1error1', 'p1errorSerial');

    }

    /*date related validations*/
    domObject = document.getElementById('dateOfRegistrationDatePicker');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error1').value;
    } else {
        isDate(domObject.value, 'p1error1', 'p1errordate1');
    }

    if (declarationType.value == 1) {
        domObject = document.getElementById('resonForLateRegistration');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, '', 'error13');
        }
    }

    domObject = document.getElementById('deathDatePicker');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error2').value;
    } else {
        isDate(domObject.value, 'p1error1', 'p1errordate2');
    }

    //validate death person NIC/PIN
    domObject = document.getElementById('deathPerson_PINorNIC');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'p1error1', 'p1errorPIN1');
    //validate Father NIC/PIN
    domObject = document.getElementById('deathPersonFather_PINorNIC');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'p1error1', 'p1errorPIN2');
    //validate mother NIC/PIN
    domObject = document.getElementById('deathPersonMother_PINorNIC');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'p1error1', 'p1errorPIN3');

    /*domObject = document.getElementById('placeOfDeath');
     if (isFieldEmpty(domObject)) {
     errormsg = errormsg + "\n" + document.getElementById('error3').value;
     }*/
    domObject = document.getElementById('placeOfBurial');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error4').value;
    }
    domObject = document.getElementById('deathPersonGender');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error5').value;
    }

    var dateOfReg = new Date(document.getElementById("dateOfRegistrationDatePicker").value);
    var dateOfDeath = new Date(document.getElementById("deathDatePicker").value);
    if (dateOfReg < dateOfDeath) {
        errormsg = errormsg + "\n" + document.getElementById("error7").value;
    }
    domObject = document.getElementById('deathPersonAge');
    if (!isFieldEmpty(domObject)) {
        //validate age
        isNumeric(domObject.value, 'p1error1', 'invalidAgeAtDeath')

    }

    /*
     var pageType = document.getElementById("pageType").value;
     if (pageType == 0) {
     validateRadioButtons(new Array(document.getElementsByName('deathType')[0].checked,
     document.getElementsByName('deathType')[1].checked));
     }
     else {
     validateRadioButtons(new Array(document.getElementsByName('deathType')[0].checked,
     document.getElementsByName('deathType')[1].checked));
     }*/

    var person_bd = new Date(document.getElementById('deathPersonDOB').value);
    var date_of_death = new Date(document.getElementById('deathDatePicker').value);
    if (date_of_death.getTime() < person_bd.getTime()) {
        errormsg = errormsg + "\n" + document.getElementById('invalidDateRange').value;
    }
    // validations that can skip
    if (!check.checked) {
        domObject = document.getElementById('deathPerson_PINorNIC');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error8').value;
        }
        domObject = document.getElementById('deathPersonNameOfficialLang');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error9').value;
        }
        domObject = document.getElementById('deathPersonNameInEnglish');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error10').value;
        }
        domObject = document.getElementById('deathPersonPermanentAddress');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error11').value;
        }
    }
    domObject = document.getElementById('days_before_abortion_or_birth');
    isNumeric(domObject.value, 'p1error1', 'daysBeforeAbortionOrBirth')
    //  otherValidations();

    var out = checkActiveFieldsForSyntaxErrors('death-registration-form-1');
    if (out != "") {
        errormsg = errormsg + out;
    }

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }

    errormsg = "";
    return returnval;
}

function validateRadioButtons(array) {
    var atleastOneSelect = false;
    for (var i = 0; i < array.length; i++) {
        if (array[i] == true) {
            atleastOneSelect = true;
        }
    }
    if (!atleastOneSelect) {
        errormsg = errormsg + "\n" + document.getElementById('mustFillType').value;
    }
}

function otherValidations() {
    var bellow30Days = document.getElementsByName("death.infantLessThan30Days")[1].checked;
    //alert(bellow30Days)
    //  alert(document.getElementsByName("death.infantLessThan30Days")[0].checked)
    var age = document.getElementById("deathPersonAge").value;
    if (bellow30Days && (age > 0)) {
        errormsg = errormsg + "\n" + document.getElementById('invalidDataAge').value;
    }
}

$(function () {
    $('img#place').bind('click', function (evt6) {
        /*    var text = $("textarea#placeOfDeath").attr("value");

         $.post('/ecivil/TransliterationService', {text:text, gender:'U'},
         function (data) {
         if (data != null) {
         var s = data.translated;
         $("input#placeOfDeathInEnglish").val(s);
         }
         });*/


        if ($('#deathOccurAthospitaltrue').is(':checked')) {
            var e = document.getElementById("hospitalId");
            var text = e.options[e.selectedIndex].text;
            $.post('/ecivil/TransliterationService', {text:text, gender:'U'},
                    function(data) {
                        if (data != null) {
                            var s = data.translated;
                            $("input#placeOfDeathInEnglish").val(s);
                            $("textarea#placeOfDeath").val(text);     // to do edit
                        }
                    });

        } else if ($('#deathOccurAthospitalfalse').is(':checked')) {
            var text = $("textarea#placeOfDeath").attr("value");
            $.post('/ecivil/TransliterationService', {text:text, gender:'U'},
                    function (data) {
                        if (data != null) {
                            var s = data.translated;
                            $("input#placeOfDeathInEnglish").val(s);
                        }
                    });

        }
    });

    $('img#deathName').bind('click', function (evt7) {
        var text = $("textarea#deathPersonNameOfficialLang").attr("value");
        var genderVal = $("select#deathPersonGender").attr("value");
        var gender = genderVal == 0 ? 'M' : 'F';

        $.post('/ecivil/TransliterationService', {text:text, gender:gender},
                function (data) {
                    if (data != null) {
                        var s = data.translated;
                        $("textarea#deathPersonNameInEnglish").val(s);
                    }
                });
    });
});

function initSerialNumber() {
    var domObject = document.getElementById('deathSerialNo');
    if (domObject.value.trim() == 0) {
        domObject.value = null;
    }
    if (isFieldEmpty(domObject)) {
        domObject.value = new Date().getFullYear() + "1";
    }
}

function initPage() {
    initSerialNumber();
<s:if test="pageType==1">
    document.getElementById('resonForLateRegistration').value = document.getElementById('reasonForLateDefaultText').value;
</s:if>
}

function personAgeDeath() {
    var dateOfBirthSubmitted = true;
    var dateOfDeathSubmitted = true;

    var dom = document.getElementById('deathPersonDOB');
    if (isFieldEmpty(dom)) {
        dateOfBirthSubmitted = false;
    }
    dom = document.getElementById('deathDatePicker');
    if (isFieldEmpty(dom)) {
        dateOfDeathSubmitted = false;
    }
    var person_bd = new Date(document.getElementById('deathPersonDOB').value);
    var date_of_death = new Date(document.getElementById('deathDatePicker').value);
    var age = calculateAge(person_bd, date_of_death);

    if (!(dateOfBirthSubmitted && dateOfDeathSubmitted)) {
        if (isFieldEmpty(document.getElementById("deathPersonAge"))) {
            document.getElementById("deathPersonAge").value = 0;
        }
    }
    else {
        $('#deathPersonAge').val(age[0]);
        $('#deathPersonAgeMonth').val(age[1]);
        $('#deathPersonAgeDate').val(age[2]);
    }

}

function maxLengthCalculate(id, max, divId) {
    var dom = document.getElementById(id).value;
    if (dom.length > max) {
        document.getElementById(divId).innerHTML = document.getElementById('maxLengthError').value + " : " + max
    }
    else {
        document.getElementById(divId).innerHTML = "";
    }
}
</script>


<div id="death-declaration-form-1-outer">
<s:form name="deathRegistrationForm1" id="death-registration-form-1" action="eprDeathDeclaration.do" method="POST"
        onsubmit="javascript:return validate()">
<s:actionerror cssStyle="color:red;"/>
<table class="table_reg_header_01" style="font-size:9pt">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="300px"></td>
        <td align="center" style="font-size:12pt; width:430px"><img src="<s:url value="/images/official-logo.png"/>"
                                                                    alt=""/><br>
            <s:if test="pageType == 0">
                මරණ ප්‍රකාශය - සාමාන්‍ය මරණ
                <br>இறப்பு பிரதிக்கினை - சாதாரண மரணம்
                <br>Declaration of Death – Normal Death
            </s:if>
            <s:elseif test="pageType == 1">
                මරණ ප්‍රකාශයක් [36වෙනි වගන්තිය] - කාලය ඉකුත් වූ මරණ ලියාපදිංචි කිරීම හෝ නැතිවුණු පුද්ගලයෙකුගේ මරණ
                <br>மரண பிரதிக்கினை [36வது பிரிவு ] - காலந் தாழ்த்திய இறப்பினை பதிவு செய்தல் அல்லது காணாமற் போன நபரின் மரணம்
                <br>Declaration of Death [Section 36] – Late registration or Death of missing person
            </s:elseif>
            <s:elseif test="pageType == 2">
                මරණ ප්‍රකාශය - හදිසි මරණ
                <br>இறப்பு பிரதிக்கினை - திடீர் மரணம்
                <br>Declaration of Death – Sudden Death
            </s:elseif>
            <s:elseif test="pageType == 3">
                missing person <br>
                මරණ ප්‍රකාශයක් [36වෙනි වගන්තිය] - කාලය ඉකුත් වූ මරණ ලියාපදිංචි කිරීම හෝ නැතිවුණු පුද්ගලයෙකුගේ මරණ
                <br>மரண பிரதிக்கினை [36வது பிரிவு ] - காலந் தாழ்த்திய இறப்பினை பதிவு செய்தல் அல்லது காணாமற் போன நபரின் மரணம்
                <br>Declaration of Death [Section 36] – Late registration or Death of missing person
            </s:elseif>
        </td>
        <td>
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <s:fielderror name="duplicateSerialNumberError" cssStyle="color:red;font-size:9pt;"/>
                </tr>
                <tr>
                    <td><label><span class="font-8">අනුක්‍රමික අංකය<s:label value="*"
                                                                            cssStyle="color:red;font-size:10pt"/><br>தொடர் இலக்கம்<br>Serial Number</span></label>
                    </td>
                    <td>
                        <s:textfield name="death.deathSerialNo" id="deathSerialNo" maxLength="10"/>
                    </td>
                </tr>
            </table>

            <table class="table_reg_datePicker_page_01">
                <tr>
                    <td colspan="2">කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>அலுவலக பாவனைக்காக மட்டும்
                        <br>For office use only
                        <hr>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label><span
                                class="font-8">භාරගත්  දිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/><br>பிறப்பைப் பதிவு திகதி <br>Submitted Date</span></label>
                    </td>
                    <td><s:label value="YYYY-MM-DD" cssStyle="margin-left:20px;font-size:10px"/><br>
                        <s:textfield name="death.dateOfRegistration" id="dateOfRegistrationDatePicker" maxLength="10"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <s:if test="pageType == 0 ">
                දැනුම් දෙන්නා විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරියා / රෙජිස්ට්‍රාර් වෙත භාර දිය යුතුය. මෙම තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ මරණය ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் இறப்பு நிகழ்ந்த பிரிவின் அறிக்கையிடும் அதிகாரியிடம் /இறப்பு பதிவாளாரிடம் சமர்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு முறையில் இறப்பு பதிவு செய்யப்படும்.
                <br>Should be perfected by the informant and the duly completed form should be forwarded to the Officer / Registrar.
                The death will be registered in the Civil Registration System based on the information provided in this form.
            </s:if>
            <s:elseif test="pageType== 1">
                ලියාපදිංචි නොකරන ලද මරණයක් සම්බන්ධයෙන් මෙහි පහත ප්‍රකාශ කරනු ලබන විස්තර මගේ දැනීමේ හා විශ්වාසයේ ප්‍රකාර සැබෑ බව හා නිවැරදි බවද, මරණය සිදුවී, නැතහොත් ගෘහයක් හෝ ගොඩනැගිල්ලක් නොවන ස්ථානයක තිබී මෘතශරීරය සම්බවී, මාස තුඅනක් ඇතුලත දී මරණය ලියාපදිංචි කිරීමට නොහැකි වුයේ මෙහි පහත සඳහන් කාරණය හේතු කොටගෙන බවද, ..... පදිංචි .... වන මම ගාම්භීරතා පුර්වකාවද, අවංක ලෙසද, සැබෑ ලෙසද, මෙයින් ප්‍රකාශ කරමි.
                <br>பதியப்படாத மரணம் சம்பந்தமாக இங்கு கீழ் பிரதிக்கினை செய்யப்படும் விபரங்கள் எனது அறிவிக்கும் நம்பிக்கைக்கும் உரியவகையில் உண்மையானதும் சரியானதும் எனவும் இறப்பு நிகழ்ந்து அல்லது வீடு அல்லது கட்டிடம் அல்லாத இடத்திலிருந்து அப்பிரேதத்தைக் கண்டு மூன்று மாதங்களுக்குள் இறப்பினை பதிவதற்கு இயலாது போனது கீழ் குறிப்பிடப்படும் காரணத்தினால் ஆகும் என ….......................................................................வதியும் ….........................................................ஆகிய நான் நோ்மையாகவும் உண்மையாகவும் பயபக்தியுடனும் பிரதிக்கினை செய்கின்றேன்.
                <br>I …. of …. solemnly, sincerely, and truly declare that the particulars stated below relating to an unregistered death, are true and correct to the best of my knowledge and belief, and that the death has not been registered within three months from its occurrence or from the finding of the corpse in a place other than a house or a building, for this reason.
            </s:elseif>
            <s:elseif test="pageType== 2">
                දැනුම් දෙන්නා විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරියා / රෙජිස්ට්‍රාර් වෙත භාර දිය යුතුය. මෙම තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ මරණය ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் இறப்பு நிகழ்ந்த பிரிவின் அறிக்கையிடும் அதிகாரியிடம் /இறப்பு பதிவாளாரிடம் சமர்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு முறையில் இறப்பு பதிவு செய்யப்படும்.
                <br>Should be perfected by the informant and the duly completed form should be forwarded to the Officer / Registrar.
                The death will be registered in the Civil Registration System based on the information provided in this form.
            </s:elseif>
            <s:elseif test="pageType== 3">
                missing person <br>
                ලියාපදිංචි නොකරන ලද මරණයක් සම්බන්ධයෙන් මෙහි පහත ප්‍රකාශ කරනු ලබන විස්තර මගේ දැනීමේ හා විශ්වාසයේ ප්‍රකාර සැබෑ බව හා නිවැරදි බවද, මරණය සිදුවී, නැතහොත් ගෘහයක් හෝ ගොඩනැගිල්ලක් නොවන ස්ථානයක තිබී මෘතශරීරය සම්බවී, මාස තුඅනක් ඇතුලත දී මරණය ලියාපදිංචි කිරීමට නොහැකි වුයේ මෙහි පහත සඳහන් කාරණය හේතු කොටගෙන බවද, ..... පදිංචි .... වන මම ගාම්භීරතා පුර්වකාවද, අවංක ලෙසද, සැබෑ ලෙසද, මෙයින් ප්‍රකාශ කරමි.
                <br>பதியப்படாத மரணம் சம்பந்தமாக இங்கு கீழ் பிரதிக்கினை செய்யப்படும் விபரங்கள் எனது அறிவிக்கும் நம்பிக்கைக்கும் உரியவகையில் உண்மையானதும் சரியானதும் எனவும் இறப்பு நிகழ்ந்து அல்லது வீடு அல்லது கட்டிடம் அல்லாத இடத்திலிருந்து அப்பிரேதத்தைக் கண்டு மூன்று மாதங்களுக்குள் இறப்பினை பதிவதற்கு இயலாது போனது கீழ் குறிப்பிடப்படும் காரணத்தினால் ஆகும் என ….......................................................................வதியும் ….........................................................ஆகிய நான் நோ்மையாகவும் உண்மையாகவும் பயபக்தியுடனும் பிரதிக்கினை செய்கின்றேன்.
                <br>I …. of …. solemnly, sincerely, and truly declare that the particulars stated below relating to an unregistered death, are true and correct to the best of my knowledge and belief, and that the death has not been registered within three months from its occurrence or from the finding of the corpse in a place other than a house or a building, for this reason.
            </s:elseif>
        </td>
    </tr>
    </tbody>
</table>

<s:if test="pageType==1">
    <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;" class="font-9">
        <tr>
            <td width="150px">(1)මරණය ලියාපදිංචි කිරීම ප්‍රමාද වීමට කාරණය <s:label value="*"
                                                                                   cssStyle="color:red;font-size:10pt;"/>
                <br>இறப்பினை பதிவதற்கு தாமதித்ததற்கான காரணம்
                <br>Reason for the late registration of the death
            </td>
            <td>
                <s:textarea name="death.reasonForLateRegistration" id="resonForLateRegistration"
                            onchange="checkSyntax('resonForLateRegistration')" cssStyle="width:880px;"
                            onblur="maxLengthCalculate('resonForLateRegistration','255','resonForLateRegistration_div');"/>
                <div id="resonForLateRegistration_div" style="color:red;font-size:8pt"></div>

            </td>
        </tr>
    </table>
</s:if>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;margin-bottom:15px;"
       class="font-9">
<col width="155px"/>
<col width="120px"/>
<col width="120px"/>
<col width="90px"/>
<col width="120px"/>
<col width="100 0px"/>
<col width="120px"/>
<col width="130px"/>
<col/>
<tbody>

<tr>
    <td colspan="9" class="form-sub-title">
        මරණය පිලිබඳ විස්තර
        <br>இறப்பு பற்றிய தகவல்
        <br>Information about the Death
    </td>
</tr>
<s:if test="pageType == 0">
    <tr>
        <td colspan="4">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            මරණයේ ස්වභාවය?
            <br/>மரணத்தின் வகை? <br/>
            Type of death?
        </td>
        <td colspan="5">
            සාමාන්‍ය මරණයකි / சாதாரண மரணம் / Normal Death
        </td>
    </tr>
</s:if>
<s:elseif test="pageType == 2">
    <tr>
        <td colspan="4">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            මරණයේ ස්වභාවය?
            <br/>மரணத்தின் வகை? <br/>
            Type of death?
        </td>
        <td colspan="5">
            හදිසි මරණයකි / திடீர் மரணம் / Sudden Death
        </td>
    </tr>
</s:elseif>
<tr>
    <td>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        මරණය සිදු වූ දිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/>
        <br>இறந்த திகதி
        <br>Date of Death
    </td>
    <td colspan="3">
        <s:label value="YYYY-MM-DD" cssStyle="font-size:10px"/><br>
        <s:textfield id="deathDatePicker" name="death.dateOfDeath" maxLength="10"/>
    </td>
    <td>
        වෙලාව
        <br>நேரம்
        <br>Time
    </td>
    <td colspan="4">
        <s:textfield name="death.timeOfDeath" id="timePicker"/>
    </td>
</tr>
<tr>
    <td rowspan="6">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        මරණය සිදු වූ ස්ථානය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
        <br>இறப்பு நிகழந்த இடம்
        <br>Place of Death
    </td>
    <td colspan="3">දිස්ත්‍රික්කය / மாவட்டம் / District</td>
    <td colspan="5"><s:select id="deathDistrictId" name="deathDistrictId" list="districtList"/></td>
</tr>
<tr>
    <td colspan="3">
        ප්‍රාදේශීය ලේකම් කොට්ඨාශය /
        <br/>பிரதேச செயளாளர் பிரிவு
        <br/>Divisional Secretary Division
    </td>
    <td colspan="5"><s:select id="deathDsDivisionId" name="dsDivisionId" list="dsDivisionList"
                              cssStyle="float:left; "/></td>
</tr>
<tr>
    <td colspan="3">
        ලියාපදිංචි කිරීමේ කොට්ඨාශය /
        <br>பதிவுப் பிரிவு /
        <br>Registration Division
    </td>
    <td colspan="5"><s:select id="deathDivisionId" name="deathDivisionId" list="bdDivisionList"
                              cssStyle="float:left;"/></td>
</tr>
<tr>
    <td colspan="4">
        මරණය සිදුවුයේ රෝහලක් තුලදී ද?
        <br>மரணம் வைத்தியசாலையில் இடம்பெற்றதா?
        <br>Did the death occur at a Hospital?
    </td>
    <td colspan="1">ඔව්
        <br> ஆம்
        <br>Yes
    </td>
    <td colspan="1" align="center">
        <s:radio name="death.deathOccurAtaHospital" id="deathOccurAthospital"
                 list="#@java.util.HashMap@{'true':''}"/>
    </td>
    <td colspan="1">
        නැත <br>
        இல்லை <br> No
    </td>
    <td colspan="1" align="center">
        <s:radio name="death.deathOccurAtaHospital" id="deathOccurAthospital"
                 list="#@java.util.HashMap@{'false':''}"/>
    </td>
</tr>
<tr>
    <td rowspan="2" colspan="1">
        ස්ථානය
        <br>இடம்
        <br>Place
    </td>
    <td colspan="2">
        සිංහල හෝ දෙමළ භාෂාවෙන්
        <br>சிங்களம்அல்லது தமிழ் மொழியில்
        <br>In Sinhala or Tamil
    </td>
    <td colspan="5">
            <%--   <s:textarea name="death.placeOfDeath" id="placeOfDeath" onchange="checkSyntax('placeOfDeath')" cssStyle="width:555px;"
            onblur="maxLengthCalculate('placeOfDeath','255','placeOfDeath_div');"/>--%>


        <div class="no_hospital">
            <s:textarea name="death.placeOfDeath" id="placeOfDeath" onchange="checkSyntax('placeOfDeath')"
                        cssStyle="width:555px;"
                        onblur="maxLengthCalculate('placeOfDeath','255','placeOfDeath_div');"/>
        </div>
        <div class="yes_hospital">
            <s:select id="hospitalId" name="death.deathHospital.hospitalUKey" list="hospitalList"
                      headerKey="0" headerValue="%{getText('select_hospital.label')}"
                      cssStyle="float:left;  width:285px; margin:2px 5px;"/>
        </div>

        <div id="placeOfDeath_div" style="color:red;font-size:8pt"></div>
    </td>
</tr>
<tr>
    <td colspan="2">
        ඉංග්‍රීසි භාෂාවෙන්
        <br>ஆங்கில மொழியில்
        <br>In English
    </td>
    <td colspan="5">

        <s:textfield name="death.placeOfDeathInEnglish" id="placeOfDeathInEnglish" cssStyle="width:555px;"
                     maxLength="240"/>

        <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px 0;"
             id="place">
    </td>
</tr>
<tr>
    <td colspan="5">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        මරණයට හේතුව තහවුරුද?
        <br>இறப்பிற்கான காரணம் உறுதியானதா?
        <br>Is the cause of death established?
    </td>
    <td colspan="1">
        ඔව්
        <br> ஆம்
        <br>Yes
    </td>
    <td colspan="1" align="center">
        <s:radio name="death.causeOfDeathEstablished"
                 list="#@java.util.HashMap@{'true':''}"/>
    </td>
    <td colspan="1">
        නැත <br>
        இல்லை <br> No
    </td>
    <td colspan="1" align="center">
        <s:radio name="death.causeOfDeathEstablished"
                 list="#@java.util.HashMap@{'false':''}"/>
    </td>
</tr>
<tr>
    <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        මරණයට හේතුව
        <br>இறப்பிற்கான காரணம்
        <br>Cause of death
    </td>
    <td colspan="4">
        <s:textarea name="death.causeOfDeath" id="causeOfDeath" onchange="checkSyntax('causeOfDeath')"
                    cssStyle="width:420px;"
                    onblur="maxLengthCalculate('causeOfDeath','600','causeOfDeath_div');" rows="6"/>
        <div id="causeOfDeath_div" style="color:red;font-size:8pt"></div>
    </td>
    <td colspan="2">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        හේතුවේ ICD කේත අංකය
        <br>காரணத்திற்கான ICD குறியீட்டு இலக்கம்
        <br>ICD Code of cause
    </td>
    <td colspan="2"><s:textfield name="death.icdCodeOfCause" cssStyle="width:225px;" maxLength="240"/></td>
</tr>
<tr>
    <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        ආදාහන හෝ භූමදාන ස්ථානය<s:label value="*" cssStyle="color:red;font-size:10pt"/>
        <br>அடக்கம் செய்த அல்லது தகனஞ் செய்த இடம்
        <br>Place of burial or cremation
    </td>
    <td colspan="8">
        <s:textarea name="death.placeOfBurial" id="placeOfBurial" onchange="checkSyntax('placeOfBurial')"
                    cssStyle="width:880px;"
                    onblur="maxLengthCalculate('placeOfBurial','255','placeOfBurial_div');"/>
        <div id="placeOfBurial_div" style="color:red;font-size:8pt"></div>
    </td>
</tr>
<tr>
    <td colspan="4"><label>
        මරණ
        සහතිකය නිකුත් කල යුතු භාෂාව
        <br>சான்றிதழ் வழங்கப்பட வேண்டிய மொழி
        <br>Preferred
        Language for
        Death Certificate </label></td>
    <td colspan="5"><s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'தமிழ்'}"
                              name="death.preferredLanguage"
                              cssStyle="width:190px; margin-left:5px;"></s:select></td>
</tr>
</tbody>
</table>
<table border="1"
       style="width:100%;border-bottom:none; border:1px solid #000; border-collapse:collapse; margin-bottom:0;"
       class="font-9">
    <caption/>
    <col width="520px"/>
    <col width="150px"/>
    <col width="130px"/>
    <col/>
    <col/>
    <tbody>
    <tr class="form-sub-title">
        <td colspan="5">
            මරණයට පත් වූ පුද්ගලයාගේ විස්තර
            <br>இறந்த நபரைப் பற்றிய தகவல்
            <br>Information about the person Departed
        </td>
    </tr>
    </tbody>
</table>

<table border="1" style="width: 100%;border-top:none;border-collapse:collapse; margin-bottom:15px;"
       class="font-9">
<col width="200px"/>
<col width="100px"/>
<col width="100px"/>
<col width="100px"/>
<col width="100px"/>
<col width="75px"/>
<col width="70px"/>
<col width="73px"/>
<col width="72px"/>
<col width="70px"/>
<col width="70px"/>
<tbody>

<tr>
    <td colspan="4">
        (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        මැරුණු පුද්ගලයා හඳුනාගෙන තිබේද? <br>
        இறந்த நபர் அடையாளம் காணப்பட்டுள்ளாரா?<br>
        Is the departed person identified?
    </td>
    <td colspan="2">
        ඔව්
        <br> ஆம்
        <br>Yes
    </td>
    <td colspan="2">
        <s:radio name="deathPerson.personIdentified"
                 list="#@java.util.HashMap@{'true':''}"/></td>
    <td colspan="2">
        නැත <br>
        இல்லை <br> No
    </td>
    <td><s:radio name="deathPerson.personIdentified"
                 list="#@java.util.HashMap@{'false':''}"/></td>
</tr>
<tr>
    <td>
        (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        ශ්‍රී ලාංකිකයෙකු නම්<br/>இலங்கையராயின் <br/>If Sri Lankan
    </td>
    <td colspan="3">
        අනන්‍යතා අංකය
        <br>அடையாள எண்
        <br>Identification Number
    </td>
    <td colspan="7" class="find-person">
        <img src="<s:url value="/images/alphabet-V.gif" />"
             id="death_person_NIC_V" onclick="javascript:addXorV('deathPerson_PINorNIC','V','error12')">
        <img src="<s:url value="/images/alphabet-X.gif" />"
             id="death_person_NIC_X" onclick="javascript:addXorV('deathPerson_PINorNIC','X','error12')">
        <br>
        <s:textfield name="deathPerson.deathPersonPINorNIC" id="deathPerson_PINorNIC"
                     cssStyle="float:left;" maxLength="12"/>
        <img src="<s:url value="/images/search-father.png" />"
             style="vertical-align:middle; margin-left:20px;" id="death_person_lookup">

    </td>
</tr>
<tr>
    <td rowspan="2">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        විදේශිකය‍කු නම්
        <br>வெளிநாட்டவர் எனின்
        <br>If a foreigner
    </td>
    <td colspan="3">
        රට / நாடு / Country
    </td>
    <td colspan="7"><s:select id="deathPersonCountryId" name="deathPersonCountry" list="countryList" headerKey="0"
                              headerValue="%{getText('select_country.label')}" cssStyle="width:185px;"/></td>
</tr>
<tr>
    <td colspan="3">
        ගමන් බලපත්‍ර අංකය / கடவுச் சீட்டு இல. / <br/>Passport Number
    </td>
    <td colspan="7"><s:textfield name="deathPerson.deathPersonPassportNo" cssStyle="width:180px;" maxLength="15"/></td>
</tr>

<tr>
    <td>
        (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        උපන් දිනය
        <br>பிறந்த திகதி
        <br>Date of Birth
    </td>
    <td colspan="2"><s:textfield maxLength="10" name="deathPerson.deathPersonDOB" id="deathPersonDOB"
                                 value="%{deathPerson.deathPersonDOB}" onchange="personAgeDeath();"/></td>
    <td colspan="2">
        (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        වයස
        <br>வயது
        <br>Age
    </td>
    <td>අවුරුදු<br/>வருடங்கள் <br/>Years</td>
    <td><s:textfield id="deathPersonAge" name="deathPerson.deathPersonAge" cssStyle="width:50px;"
                     maxlength="3"/></td>
    <td>මාස<br/>மாதங்கள்<br/>Months</td>
    <td>
        <s:select list="months" id="deathPersonAgeMonth"
                  name="deathPerson.deathPersonAgeMonth" headerKey="0" headerValue=" --- " cssStyle="width:60px;"/>
    </td>
    <td>දින<br/>திகதி<br/>Days</td>
    <td>
        <s:select list="days" id="deathPersonAgeDate" name="deathPerson.deathPersonAgeDate" headerKey="0"
                  headerValue=" --- " cssStyle="width:60px;"/>
    </td>
</tr>
<tr>
    <td>
        (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        ස්ත්‍රී පුරුෂ භාවය
        <br>பால்
        <br>Gender
    </td>
    <td colspan="2">
        <s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="deathPerson.deathPersonGender" headerKey="0" headerValue="%{getText('select_gender.label')}"
                id="deathPersonGender" cssStyle="width:185px;"/>
    </td>
    <td colspan="2">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        ජන වර්ගය<br/>
        இனம்<br/>
        Ethnic Group
    </td>
    <td colspan="6">
        <s:select list="raceList" name="deathPersonRace" headerKey="0" headerValue="%{getText('select_race.label')}"
                  cssStyle="width:190px;" id="deathPersonRace"/>
    </td>
</tr>
<tr>
    <td>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
        <br>பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
        <br>Name in either of the official languages (Sinhala / Tamil)
    </td>
    <td colspan="10">

        <s:textarea name="deathPerson.deathPersonNameOfficialLang" id="deathPersonNameOfficialLang"
                    onchange="checkSyntax('deathPersonNameOfficialLang')"
                    onblur="maxLengthCalculate('deathPersonNameOfficialLang','600','deathPersonNameOfficialLang_div');"
                    cssStyle="width:99%"/>
        <div id="deathPersonNameOfficialLang_div" style="color:red;font-size:8pt"></div>
    </td>
</tr>
<tr>
    <td>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        නම ඉංග්‍රීසි භාෂාවෙන්
        <br>பெயர் ஆங்கில மொழியில்
        <br>Name in English
    </td>
    <td colspan="10">
        <s:textarea name="deathPerson.deathPersonNameInEnglish" id="deathPersonNameInEnglish"
                    onblur="maxLengthCalculate('deathPersonNameInEnglish','600','deathPersonNameInEnglish_div');"
                    cssStyle="width:99%"/>
        <div id="deathPersonNameInEnglish_div" style="color:red;font-size:8pt;"></div>
        <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px 0;"
             id="deathName">
    </td>
</tr>
<tr>
    <td rowspan="4">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        ස්ථිර ලිපිනය
        <br>நிரந்தர வதிவிட முகவரி
        <br>Permanent Address
    </td>
    <td colspan="10">
        <s:textarea name="deathPerson.deathPersonPermanentAddress" id="deathPersonPermanentAddress"
                    onchange="checkSyntax('deathPersonPermanentAddress')"
                    cssStyle="width:99%;"
                    onblur="maxLengthCalculate('deathPersonPermanentAddress','255','deathPersonPermanentAddress_div');"/>
        <div id="deathPersonPermanentAddress_div" style="color:red;font-size:8pt"></div>
    </td>
</tr>
<tr>
    <td colspan="4">
        දිස්ත්‍රික්කය <br>
        மாவட்டம் <br>
        District
    </td>
    <td colspan="7">
        <s:select id="deathPersonPermenentAddressDistrictId" name="deathPersonPermenentAddressDistrictId"
                  list="allDistrictList" value="%{deathPersonPermenentAddressDistrictId}"
                  cssStyle="float:left;  width:98%;" headerValue="%{getText('district.label')}" headerKey="0"/>
    </td>
</tr>
<tr>
    <td colspan="4">
        ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
        பிரதேச செயளாளர் பிரிவு <br/>
        Divisional Secretary Division
    </td>
    <td colspan="7">
        <s:select id="deathPersonPermenentAddressDSDivisionId" name="deathPersonPermenentAddressDSDivisionId"
                  list="allDSDivisionList" value="%{deathPersonPermenentAddressDSDivisionId}"
                  cssStyle="float:left;  width:98%;" headerValue="%{getText('dsDivision.label')}" headerKey="0"/>
    </td>
</tr>
<tr>
    <td colspan="4"><label>
        ග්‍රාම නිළධාරී කොටිඨාශය / கிராம சேவையாளர் பிரிவு/ Grama Niladhari Division</label>
    </td>
    <td colspan="7" align="center">
        <s:select id="deathPersonPermanentAddressGNDivision" name="gnDivisionId" value="%{gnDivisionId}"
                  list="gnDivisionList"
                  cssStyle="float:left; width:99%" headerKey="0" headerValue="%{getText('gnDivisions.label')}"/><br/>
        <a href="javascript:displayGNSearch()">
            <span><s:label value="%{getText('searchGNDivision.label')}"/></span>
        </a>
    </td>
</tr>

<s:if test="pageType==3">
    <tr>
        <td colspan="11" height="35px">
            (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            නැතිවූ පුද්ගලයෙකුගේ මරණයක් නම් / காணாமற் போன நபரின் இறப்பாயின் / If the death is for a Missing Person
        </td>
    </tr>
    <tr>
        <td>
            අවසන් වරට පදිංචි ලිපිනය <br>
            கடைசியாக குடியிருந்த இடம் <br>
            Last address
        </td>
        <td colspan="10">
                <%--<s:textarea name="deathPerson.lastAddressOfMissingPerson" id="lastAddressOfMissingPerson"
                cssStyle="width:98%;"/>--%>
            <s:textarea name="deathPerson.lastAddressOfMissingPerson" id="lastAddressOfMissingPerson"
                        onchange="checkSyntax('lastAddressOfMissingPerson')"
                        cssStyle="width:99%;"
                        onblur="maxLengthCalculate('lastAddressOfMissingPerson','255','lastAddressOfMissingPerson_div');"/>
            <div id="lastAddressOfMissingPerson_div" style="color:red;font-size:8pt"></div>
        </td>
    </tr>
</s:if>
<tr>
    <td>
        (<s:property value="#row"/><s:set name="row" value="#row+1"/>) තත්වය නොහොත් වෘත්තීය <br>
        நிலவரம் அல்லது தொழில் <br>
        Rank or Profession
    </td>
    <td colspan="4">

        <s:textarea name="deathPerson.rankOrProfession" id="deathPersonRank" onchange="checkSyntax('deathPersonRank')"
                    cssStyle="width:98%;"
                    onblur="maxLengthCalculate('deathPersonRank','255','deathPersonRank_div');"/>
        <div id="deathPersonRank_div" style="color:red;font-size:8pt"></div>
    </td>
    <td colspan="2">විශ්‍රාම වැටුප් ලාභියෙකුද? <br>
        இளைப்பாற்று ஊதியம் பெறுபவரா? <br>
        Was a Pensioner?
    </td>
    <td>
        ඔව් <br>
        ஆம் <br>
        Yes
    </td>
    <td align="center">
        <s:radio name="deathPerson.pensioner"
                 list="#@java.util.HashMap@{'true':''}"/>
    </td>
    <td>
        නැත <br>
        இல்லை <br>
        No
    </td>
    <td align="center">
        <s:radio name="deathPerson.pensioner"
                 list="#@java.util.HashMap@{'false':''}"/>
    </td>
</tr>

<tr>
    <td colspan="5">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        පියාගේ අනන්‍යතා අංකය / தந்தையின் அடையாள எண் / Fathers Identification No.
    </td>
    <td colspan="6" class="find-person">
        <img src="<s:url value="/images/alphabet-V.gif" />"
             id="death_person_father_NIC_V"
             onclick="javascript:addXorV('deathPersonFather_PINorNIC','V','error12')">
        <img src="<s:url value="/images/alphabet-X.gif" />"
             id="death_person_father_NIC_X"
             onclick="javascript:addXorV('deathPersonFather_PINorNIC','X','error12')">
        <br>
        <s:textfield name="deathPerson.deathPersonFatherPINorNIC" id="deathPersonFather_PINorNIC"
                     cssStyle="float:left;" maxLength="12"/>

        <img src="<s:url value="/images/search-father.png" />"
             style="vertical-align:middle; margin-left:20px;" id="death_person_father_lookup">

    </td>
</tr>
<tr>
    <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        පියාගේ සම්පුර්ණ නම
        <br>தந்தையின் முழுப் பெயர்
        <br>Fathers full name
    </td>
    <td colspan="10">

        <s:textarea name="deathPerson.deathPersonFatherFullName" id="deathPersonFatherFullName"
                    onchange="checkSyntax('deathPersonFatherFullName')"
                    cssStyle="width:99%;"
                    onblur="maxLengthCalculate('deathPersonFatherFullName','255','deathPersonFatherFullName_div');"/>
        <div id="deathPersonFatherFullName_div" style="color:red;font-size:8pt"></div>
    </td>
</tr>
<tr>
    <td colspan="5">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        මවගේ අනන්‍යතා අංකය / தாயின் அடையாள எண் / Mothers Identification No.
    </td>
    <td colspan="6" class="find-person">
        <img src="<s:url value="/images/alphabet-V.gif" />"
             id="death_person_mother_NIC_V"
             onclick="javascript:addXorV('deathPersonMother_PINorNIC','V','error12')">
        <img src="<s:url value="/images/alphabet-X.gif" />"
             id="death_person_mother_NIC_X"
             onclick="javascript:addXorV('deathPersonMother_PINorNIC','X','error12')">
        <br>
        <s:textfield name="deathPerson.deathPersonMotherPINorNIC" id="deathPersonMother_PINorNIC"
                     cssStyle="float:left;" maxLength="12"/>
        <img src="<s:url value="/images/search-mother.png" />"
             style="vertical-align:middle; margin-left:20px;" id="death_person_mother_lookup"></td>
</tr>
<tr>
    <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
        මවගේ සම්පුර්ණ නම
        <br>தாயின் முழுப் பெயர்
        <br>Mothers full name
    </td>
    <td colspan="10">

        <s:textarea name="deathPerson.deathPersonMotherFullName" id="deathPersonMotherFullName"
                    onchange="checkSyntax('deathPersonMotherFullName')"
                    cssStyle="width:99%;"
                    onblur="maxLengthCalculate('deathPersonMotherFullName','255','deathPersonMotherFullName_div');"/>
        <div id="deathPersonMotherFullName_div" style="color:red;font-size:8pt"></div>
    </td>
</tr>
</tbody>
</table>

<div class="font-10">
    (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
    මියගියේ වයස අවු. 49ට අඩු කාන්තාවක් නම් පමණක් මෙම කොටස සම්පුර්ණ කල යුතුය
    <br>இறந்த நபர் 49வயதிற்கு குறைந்த பெண்ணாயிருந்தால் மடடும் இப்பகுதி பூரணப்படுத்தப்படல்வேண்டும்
    <br>Fill this section only If the departed is a woman below 49 years
    <input type="button" id="clearWomanInfo" value="clear" style="float: right; margin-bottom: 5px;"/>
</div>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin-bottom:10; margin-top:10;"
       class="font-9">
    <caption/>
    <col width="4%"/>
    <col width="66%"/>
    <col width="15%"/>
    <col width="15%"/>
    <tbody>
    <tr>
        <td colspan="2"></td>
        <td colspan="1" align="center">ඔව් / ஆம் / Yes</td>
        <td colspan="1" align="center">නැත / இல்லை / No</td>
    </tr>
    <tr>
        <td colspan="2">
            මරණය සිදුවනවිට ඇය දරුවකු ලැබීමට (ගර්භනීව) සිටියේද? <br>
            இறப்பு நிகழ்த பொழுது அவர் பிள்ளை பிறசவிக்க (கர்ப்பிணி) இருந்தாரா? <br>
            Was she pregnant at time of death?
        </td>
        <td align="center">
            <s:radio id="pregnantAtTimeOfDeathYes" name="deathPerson.pregnantAtTimeOfDeath"
                     list="#@java.util.HashMap@{'true':''}"/>
        </td>
        <td align="center">
            <s:radio id="pregnantAtTimeOfDeathNo" name="deathPerson.pregnantAtTimeOfDeath"
                     list="#@java.util.HashMap@{'false':''}"/>
        </td>
    </tr>
    <tr>
        <td style="background:#cccccc;"></td>
        <td>
            මරණයට පෙර සති 6ක් (දින 42ක්) ඇතුලත දී ඇය විසින් දරුවකු ප්‍රසුත කරනු ලැබුවාද? <br>
            இறப்பிற்கு முன் 6 கிழமைகளுக்குள் (42 நாட்களுக்கிடையில் ) அவர் மூலம் பிள்ளை பிரசவிக்கப்பட்டதா?<br>
            Has she given birth in the previous 6 weeks (42 days) ?
        </td>
        <td align="center">
            <s:radio id="givenABirthWithInPreviouse6WeeksYes" name="deathPerson.givenABirthWithInPreviouse6Weeks"
                     list="#@java.util.HashMap@{'true':''}"/>
        </td>
        <td align="center">
            <s:radio id="givenABirthWithInPreviouse6WeeksNo" name="deathPerson.givenABirthWithInPreviouse6Weeks"
                     list="#@java.util.HashMap@{'false':''}"/>
        </td>
    </tr>
    <tr>
        <td style="background:#cccccc;"></td>
        <td>
            නැතහොත් ගබ්සාවක් සිදුවී තිබේද? <br>
            அல்லது கருக்கலைப்பு நடைப்பெற்றிருந்ததா?<br>
            Has an abortion taken place?
        </td>
        <td align="center">
            <s:radio id="anAbortionTakenPlaceYes" name="deathPerson.anAbortionTakenPlace"
                     list="#@java.util.HashMap@{'true':''}"/>
        </td>
        <td align="center">
            <s:radio id="anAbortionTakenPlaceNo" name="deathPerson.anAbortionTakenPlace"
                     list="#@java.util.HashMap@{'false':''}"/>
        </td>
    </tr>
    <tr>
        <td style="background:#cccccc;"></td>
        <td>
            දරු ප්‍රසුතිය හෝ ගබ්සාව සිදුවුයේ මරණය සිදුවීමට දින කීයකට පෙරද?
            <br>பிரசவம் அல்லது கருக்கலைப்பு நடைப்பெற்றது இறப்பு நடைப்பெறுவதற்கு எத்தனை நாட்களுக்கு முன்?
            <br>If a birth or abortion took place, how many days before the death has it occurred?
        </td>
        <td colspan="2" align="left">
            <s:textfield name="deathPerson.daysBeforeAbortionOrBirth" id="days_before_abortion_or_birth"
                         cssStyle="float:left;" maxLength="3"/>
        </td>
    </tr>
    </tbody>
</table>

<div class="skip-validation">
    <s:checkbox name="skipjavaScript" id="skipjs" value="false">
        <s:label value="%{getText('skipvalidation.label')}"/>
    </s:checkbox>
</div>
<div class="form-submit">
    <s:hidden name="pageNo" value="1"/>
    <s:hidden name="rowNumber" value="%{row}"/>
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
<s:hidden name="pageType" value="%{pageType}"/>
<s:hidden name="idUKey" value="%{#request.idUKey}"/>
</s:form>
<br/><br/>
<s:hidden id="error0" value="%{getText('p1.errorlable.serialNumber')}"/>
<s:hidden id="error1" value="%{getText('p1.errorlable.dateofReg')}"/>
<s:hidden id="error2" value="%{getText('p1.errorlable.dateofDeath')}"/>
<s:hidden id="error3" value="%{getText('p1.errorlable.placeofDeath')}"/>
<s:hidden id="error4" value="%{getText('p1.errorlable.placeofBurial')}"/>
<s:hidden id="error5" value="%{getText('p1.errorlable.gerder')}"/>
<s:hidden id="error6" value="%{getText('p1.errorlable.serialNumberIsNum')}"/>
<s:hidden id="error7" value="%{getText('p1.errorlable.dateofRegAndDateofDeath')}"/>
<s:hidden id="error8" value="%{getText('p1.errorlable.deathPerson.PINorNIC')}"/>
<s:hidden id="error9" value="%{getText('p1.errorlable.deathPerson.NameOfficialLang')}"/>
<s:hidden id="error10" value="%{getText('p1.errorlable.deathPerson.NameInEnglish')}"/>
<s:hidden id="error11" value="%{getText('p1.errorlable.deathPerson.PermanentAddress')}"/>
<s:hidden id="error12" value="%{getText('NIC.error.add.VX')}"/>

<s:hidden id="p1error1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="p1errorPIN1" value="%{getText('p1.deathPerson_PIN')}"/>
<s:hidden id="p1errorPIN2" value="%{getText('p1.father_PIN')}"/>
<s:hidden id="p1errorPIN3" value="%{getText('p1.mother_PIN')}"/>
<s:hidden id="p1errorAge" value="%{getText('p1.deathAge')}"/>
<s:hidden id="p1errordate1" value="%{getText('p1.dateOfRegistrationDate')}"/>
<s:hidden id="p1errordate2" value="%{getText('p1.deathDate')}"/>
<s:hidden id="p1errorSerial" value="%{getText('p1.serialNumber.format')}"/>
<s:hidden id="invalidAgeAtDeath" value="%{getText('error.invalid.age.at.death')}"/>
<s:hidden id="invalidDateRange" value="%{getText('error.dod.mst.lt.dob')}"/>
<s:hidden id="invalidDataAge" value="%{getText('error.if.bellow30.age.must.0')}"/>
<s:hidden id="mustFillType" value="%{getText('error.must.fill.type')}"/>
<s:hidden id="daysBeforeAbortionOrBirth" value="%{getText('error.days.before.abortion.or.birth')}"/>


<s:hidden id="error13" value="%{getText('enter.reasonForLate.label')}"/>
<s:hidden id="reasonForLateDefaultText" value="%{getText('text.default.reason')}"/>
<s:hidden id="selectDSDivision" value="%{getText('dsDivision.label')}"/>
<s:hidden id="selectGNDivision" value="%{getText('gnDivisions.label')}"/>
<s:hidden id="maxLengthError" value="%{getText('error.max.length')}"/>

<s:hidden id="clear" value="%{getText('clear.label')}"/>
</div>