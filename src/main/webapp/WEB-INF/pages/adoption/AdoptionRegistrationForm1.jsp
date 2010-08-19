<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/popreg/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>

<script>
    $(function() {
        $("#receivedDatePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'

        });
    });
    $(function() {
        $("#bdayDatePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31',
            onSelect: function() {
                var bday = new Date(document.getElementById('bdayDatePicker').value);
                var today = new Date();
                var ageMonthBDay = bday.getMonth();
                var ageYearBDay = bday.getYear();
                var ageMonthTOday = today.getMonth();
                var ageYearTOday = today.getYear();
                var ageMonth,ageYear = 0;
                if (ageMonthTOday >= ageMonthBDay) {
                    ageMonth = ageMonthTOday - ageMonthBDay;
                    ageYear = ageYearTOday - ageYearBDay;
                }
                else    if (ageYearTOday > ageYearBDay) {
                    ageMonth = (ageMonthTOday + 12) - ageMonthBDay;
                    ageYear = (ageYearTOday - 1) - ageYearBDay;
                }

                if (confirm(document.getElementById("lable01").value + "   :" + ageYear
                        + "\n " + document.getElementById("lable02").value + "    :" + ageMonth)) {
                    document.getElementById("childAgeYears").value = ageYear;
                    document.getElementById("childAgeMonths").value = ageMonth;
                }
            }
        });
    });

    $(function() {
        $("#orderIssuedDatePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });
   /* $(function() {
        $('select#birthDistrictId').bind('change', function(evt1) {
            var id = $("select#birthDistrictId").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id,mode:3},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivisionId").html(options1);

                        var options2 = '';
                        var bd = data.bdDivisionList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#birthDivisionId").html(options2);
                    });
        });

        $('select#dsDivisionId').bind('change', function(evt2) {
            var id = $("select#dsDivisionId").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#birthDivisionId").html(options);
                    });
        })
    })*/

    $(function() {
    $('select#birthDistrictId').bind('change', function(evt1) {
        var id = $("select#birthDistrictId").attr("value");
        $.getJSON('/popreg/crs/DivisionLookupService', {id:id},
                function(data) {
                    var options1 = '';
                    var ds = data.dsDivisionList;
                    for (var i = 0; i < ds.length; i++) {
                        options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                    }
                    $("select#dsDivisionId").html(options1);

                    var options2 = '';
                    var bd = data.bdDivisionList;
                    for (var j = 0; j < bd.length; j++) {
                        options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                    }
                    $("select#birthDivisionId").html(options2);
                });
    });

    $('select#dsDivisionId').bind('change', function(evt2) {
        var id = $("select#dsDivisionId").attr("value");
        $.getJSON('/popreg/crs/DivisionLookupService', {id:id, mode:2},
                function(data) {
                    var options = '';
                    var bd = data.bdDivisionList;
                    for (var i = 0; i < bd.length; i++) {
                        options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                    }
                    $("select#birthDivisionId").html(options);
                });
    });

    $(function() {
        $('img#adoption_applicant_lookup').bind('click', function(evt3) {
            var id1 = $("input#applicantPin").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#applicantName").val(data1.fullNameInOfficialLanguage);
                        //$("textarea#deathPersonNameInEnglish").val(data1.fullNameInOfficialLanguage);
                        $("textarea#applicantAddress").val(data1.lastAddress);
                    });
        });
    });
    $(function() {
        $('img#mother_lookup').bind('click', function(evt3) {
            var id1 = $("input#wifePINorNIC").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#wifeName").val(data1.fullNameInOfficialLanguage);
                        //$("textarea#deathPersonNameInEnglish").val(data1.fullNameInOfficialLanguage);
                        $("textarea#applicantAddress").val(data1.lastAddress);
                    });
        });
    });
        })

//    $('img#mother_lookup').bind('click', function(evt4) {
//        var id2 = $("input#wifePINorNIC").attr("value");
//        $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id2},
//                function(data) {
//                    $("textarea#wifeName").val(data.fullNameInOfficialLanguage);
//                });
//    });


    //these inpute can not be null

    function validate() {
        var errormsgOut = "";
        var element;
        var returnval;
        var flag = false;
        var inputs = new Array(9);

        //these inpute can not be null
        inputs[0] = new Date(document.getElementById("receivedDatePicker").value)
        inputs[1] = document.getElementById("court").value;
        inputs[2] = new Date(document.getElementById("orderIssuedDatePicker").value);
        inputs[3] = document.getElementById("courtOrderNumber").value;
        inputs[4] = document.getElementById("judgeName").value;
        inputs[5] = document.getElementById("applicantName").value;
        inputs[6] = document.getElementById("applicantAddress").value;
        inputs[7] = document.getElementById("childAgeYears").value;
        inputs[8] = document.getElementById("childAgeMonths").value;
        //these inputs may be null with conditions
        var childExistingName = document.getElementById("childExistingName").value;
        var childNewName = document.getElementById("childNewName").value;
        //var adoptionApplicantFather=document.getElementById("adoptionApplicantFather").value;
        //alert(adoptionApplicantFather);
        //check elements which are can not be null
        for (i = 0; i < inputs.length; i++)
        {
            if (inputs[i].length <= 0)
            {
                errormsgOut = errormsgOut + document.getElementById("error" + i).value + "\n";
            }
        }
        if (childExistingName.length <= 0 && childNewName.length <= 0) {

            errormsgOut = errormsgOut + document.getElementById("error9").value + "\n";
        }
        if (isNaN(inputs[7]) && inputs[7].length > 0)
        {
            errormsgOut = errormsgOut + document.getElementById("error10").value + "\n";
        }
        if ((isNaN(inputs[8]) && inputs[8].length > 0)) {

            errormsgOut = errormsgOut + document.getElementById("error11").value + "\n";
        }
        else if ((inputs[8] > 12 || inputs[8] < 0) && (inputs[8].length > 0)) {
            errormsgOut = errormsgOut + document.getElementById("error11").value + "\n";
        }
        if (errormsgOut.length > 0) {
            alert(errormsgOut);
            return false;
        }
        else
        {
            return true;
        }
    }
    function disable(mode) {
        document.getElementById('wifePINorNIC').disabled = mode;
        document.getElementById('wifeCountryId').disabled = mode;
        document.getElementById('wifePassport').disabled = mode;
        document.getElementById('wifeName').disabled = mode;
    }
</script>

<div id="adoption-registration-form-outer">
<s:form action="eprAdoptionAction.do" name="" id="" method="POST" onsubmit="javascript:return validate()">
<table class="adoption-reg-form-header-table" cellspacing="1" cellpadding="1">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="450px"></td>
        <td align="center" style="font-size:12pt; width:130px"><img
                src="<s:url value="/images/official-logo.png"/>"
                alt=""/></td>
        <td width="450px"></td>
    </tr>
    <tr>
        <td colspan="3" align="center">දරුකමට හදාගැනීමේ උසාවි නියෝගය (අංක 4 දරන ආකෘති පත්‍රය) <br/>
            Adoption Order Issued by Court
        </td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <tr>
        <td width="330px">නියෝගය ලැබුණු දිනය <br/>
            Received Date
        </td>
        <td style="text-align:center;" width="70"><s:textfield id="receivedDatePicker"
                                                               name="adoption.orderReceivedDate"></s:textfield>
        </td>
    </tr>
    <tr>
        <td>අධිකරණය<br/>
            Court
        </td>
        <td style="text-align:center;" width="70"><s:textfield name="adoption.court" id="court"/></td>
    </tr>
    <tr>
        <td>නියෝගය නිකුත් කල දිනය <br/>
            Issued Date
        </td>
        <td style="text-align:center;"><s:textfield id="orderIssuedDatePicker"
                                                    name="adoption.orderIssuedDate"></s:textfield>
        </td>
    </tr>
    <tr>
        <td>නියෝග අංකය<br/>
            Serial number
        </td>
        <td style="text-align:center;"><s:textfield name="adoption.courtOrderNumber" id="courtOrderNumber"/></td>
    </tr>
    <tr>
        <td>විනිසුරු නම <br/>
            Name of the Judge
        </td>
        <td style="text-align:center;"><s:textfield name="adoption.judgeName" id="judgeName"/></td>
    </tr>
    <tr>
        <td>******* සහතිකය නිකුත් කල යුතු භාෂාව <br>***in tamil***<br>Preferred
            Language for
            ******
        </td>
        <td style="text-align:left;" width="30px">
            <s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'Tamil'}"
                      name="adoption.languageToTransliterate"
                      cssStyle="width:190px; margin-left:5px;"></s:select>
        </td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>අයදුම් කරුගේ විස්තර <br/>
            Applicants Details
        </td>
    </tr>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col width="330px"/>
    <col width="175px"/>
    <col width="175px"/>
    <col width="175px"/>
    <col width="175px"/>
    <tbody>
    <tr>
        <td>අයදුම් කරු <br/>
            Applicant
        </td>
        <td>පියා <br/>
            Father
        </td>
        <td>
            <s:radio name="adoption.applicantMother" list="#@java.util.HashMap@{'false':''}" value="false"
                     id="adoptionApplicantFather"
                     onclick="disable(false)"/>
        </td>
        <td>මව <br/>
            Mother
        </td>
        <td>
            <s:radio name="adoption.applicantMother" list="#@java.util.HashMap@{'true':''}" onclick="disable(true);"/>
        </td>
    </tr>
    <tr>
        <td colspan="3">අයදුම් කරුගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br/>
            தாயின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம் <br/>
            Applicant's PIN / NIC Number
        </td>
        <td colspan="2" align="center" class="find-person"><s:textfield name="adoption.applicantPINorNIC"
                                                                                      id="applicantPin" cssStyle="float:left;width:250px;" />
            <img src="<s:url value="/images/search-father.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="adoption_applicant_lookup" ></td>
    </tr>
    <tr>
        <td>විදේශිකය‍කු නම් <br/>
            வெளிநாட்டவர் <br/>
            If a foreigner
        </td>
        <td>රට <br/>
            நாடு <br/>
            Country
        </td>
        <td align="center">
            <s:select name="adoption.applicantCountryId" list="countryList" headerKey="0"
                      headerValue="%{getText('adoption.select_country.label')}"/>
        </td>
        <td>ගමන් බලපත්‍ර අංකය <br/>
            கடவுச் சீட்டு <br/>
            Passport No.
        </td>
        <td align="left"><s:textfield name="adoption.applicantPassport" id="applcantPassportNumber"/></td>
    </tr>
    <tr>
        <td>නම <br/>
            Name of the Applicant
        </td>
        <td colspan="4" align="center"><s:textarea id="applicantName" name="adoption.applicantName"></s:textarea></td>
    </tr>

    <tr>
        <td>ලිපිනය <br/>
            Address
        </td>
        <td colspan="4" align="center"><s:textarea name="adoption.applicantAddress"
                                                   id="applicantAddress"></s:textarea></td>
    </tr>
    </tbody>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td><br/>
            අයදුම් කරු පියා නම් මවගේ විස්තර / If applicant is the father, Mother's details
        </td>
    </tr>
</table>
<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <tr>
        <td colspan="3" width="680px">මවගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br/>
            தாயின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம் <br/>
            Wife's PIN / NIC Number
        </td>
        <td colspan="2" align="left"><s:textfield name="adoption.wifePINorNIC" id="wifePINorNIC" cssStyle="float:left;width:250px;"/>
            <img src="<s:url value="/images/search-mother.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="mother_lookup"></td>
    </tr>
    <tr>
        <td width="330px">විදේශිකය‍කු නම් <br/>
            வெளிநாட்டவர் <br/>
            If a foreigner
        </td>
        <td width="175px">රට <br/>
            நாடு <br/>
            Country
        </td>
        <td width="175px" align="center">
            <s:select id="wifeCountryId" name="adoption.wifeCountryId" list="countryList" headerKey="0"
                      headerValue="%{getText('adoption.select_country.label')}"/>
        </td>
        <td width="175px">ගමන් බලපත්‍ර අංකය <br/>
            கடவுச் சீட்டு <br/>
            Passport No.
        </td>
        <td width="175px" align="center">
            <s:textfield name="adoption.wifePassport" id="wifePassport"> </s:textfield>
        </td>
    </tr>
    <tr>
        <td> මවගේ නම <br/>
            Name of Mother
        </td>
        <td colspan="4" align="center"><s:textarea name="adoption.wifeName" id="wifeName"></s:textarea></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>ළමයාගේ විස්තර <br/>
            Child's Information
        </td>
    </tr>
</table>

<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col style="width:330px;"/>
    <col style="width:160px;"/>
    <col style="width:190px;"/>
    <col style="width:160px;"/>
    <col style="width:190px;"/>
    <tbody>
    <tr>
        <td>උපන් දිනය<br/>
            Date of birth
        </td>
        <td colspan="2" style="text-align:center;"><s:textfield id="bdayDatePicker"
                                                                name="adoption.childBirthDate"
                                                                onchange="calYearAndMonth()"></s:textfield>
        </td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br/>
            Gender
        </td>
        <td align="center"><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="adoption.childGender" headerKey="0" headerValue="%{getText('select_gender.label')}"
                cssStyle="width:190px; margin-left:5px;"/></td>
    </tr>
    <tr>
        <td>වයස <br/>
            Age
        </td>
        <td>අවුරුදු <br/>
            Years
        </td>
        <td align="center"><s:textfield name="adoption.childAgeYears" id="childAgeYears"
                                        onchange="validateNum(document.getElementById('childAgeYears').value)"/></td>
        <td>මාස <br/>
            Months
        </td>
        <td><s:textfield name="adoption.childAgeMonths" id="childAgeMonths" onclick="calYearAndMonth()"
                         onchange="validateNum(document.getElementById('childAgeMonths').value)"/></td>
    </tr>
    <tr>
        <td>දැනට පවතින නම <br/>
            (නමක් දී ඇති නම්) <br/>
            Existing Name <br/>
            (if already given)
        </td>
        <td colspan=" 4" align="center"><s:textarea name="adoption.childExistingName"
                                                    id="childExistingName"></s:textarea></td>
    </tr>
    <tr>
        <td>ලබා දෙන නම <br/>
            New name given
        </td>
        <td colspan="4" align="center"><s:textarea name="adoption.childNewName" id="childNewName"></s:textarea></td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td>උපත දැනටත් ලියාපදිංචි කර උප්පැන්න සහතිකයක් නිකුත් කර ඇතිනම් <br/>
            If the birth is already registered, and a Birth Certificate issued
        </td>
    </tr>
</table>


<table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
    <tr>
        <td width="60%">උප්පැන්න සහතිකයේ අනුක්‍රමික අංකය <br/>
            The serial number of the Birth Certificate
        </td>
        <td width="40%" align="center"><s:textfield name="adoption.birthCertificateNumber" id="birthCertificateNumber"
                                                    cssStyle="width:85%;"/></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>හෝ<br/>OR
        </td>
    </tr>
</table>
<table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="2">
    <caption></caption>
    <col width="400px"/>
    <col width="400px"/>
    <tbody>
    <tr>
        <td colspan="2" style="text-align:center;">උපත ලියපදින්ච්චි කිරීමේ රිසීට් පතේ සටහන් <br/>
            Birth Registration acknowledgement slip
        </td>
    </tr>
    <tr>
        <td colspan="1">දිස්ත්‍රික්කය <br/>
            District
        </td>
        <td colspan="1"><s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                                  value="birthDistrictId" cssStyle="width:280px;"/>
        </td>
    </tr>
    <tr>
        <td>ප්‍රාදේශීය ලේකම් කොට්ටාශය <br/>
            Divisional Secretariat
        </td>
        <td>
            <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                      cssStyle="float:left;  width:280px;"/>
        </td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ටාශය <br/>
            Registration Division
        </td>
        <td>
            <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}"
                      list="bdDivisionList"
                      cssStyle=" width:280px;float:left;"/>
        </td>
    </tr>
    <tr>
        <td>අනුක්‍රමික අංකය <br/>
            Serial Number
        </td>
        <td>
            <s:textfield name="adoption.birthRegistrationSerial" id="birthRegistrationSrialNum"
                         cssStyle="width:280px;"> </s:textfield>
        </td>
    </tr>
    </tbody>
</table>

<s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
<s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
<s:hidden name="currentStatus" value="%{#request.currentStatus}"/>
<s:hidden name="pageNo" value="%{#request.pageNo}"/>
<s:hidden name="idUKey" value="%{#request.idUKey}"/>
<%--<s:hidden name="pageNo" value="1"/>--%>
<div class="button" align="right">
    <s:submit value="%{getText('submit.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
<s:hidden id="error0" value="%{getText(' er.lable.receivedDate')}"/>
<s:hidden id="error1" value="%{getText('er.lable.court')}"/>
<s:hidden id="error2" value="%{getText('er.lable.orderIssuedDate')}"/>
<s:hidden id="error3" value="%{getText('er.lable.courtOrderNumber')}"/>
<s:hidden id="error4" value="%{getText('er.lable.judgeName')}"/>
<s:hidden id="error5" value="%{getText('er.lable.applicantName')}"/>
<s:hidden id="error6" value="%{getText('er.lable.applicantAddress')}"/>
<s:hidden id="error7" value="%{getText('er.lable.childAgeYears')}"/>
<s:hidden id="error8" value="%{getText('er.lable.childAgeMonths')}"/>
<s:hidden id="error9" value="%{getText('er.lablechildName')}"/>
<s:hidden id="error10" value="%{getText('er.lable.childAgeYearsValid')}"/>
<s:hidden id="error11" value="%{getText('er.lable.childAgeMonthsValid')}"/>
<s:hidden id="lable01" value="%{getText('lable.childAgeYear')}"/>
<s:hidden id="lable02" value="%{getText('lable.childAgeMonth')}"/>

</div>