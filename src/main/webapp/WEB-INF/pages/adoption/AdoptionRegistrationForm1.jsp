<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script>
    $(function() {
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
    })


</script>

<div id="adoption-registration-form-outer">
<s:form action="eprAdoptionAction.do" name="" id="" method="POST" onsubmit="javascript:return validate()">
<table class="adoption-reg-form-header-table">
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
        <td style="text-align:right;"><sx:datetimepicker id="receivedDatePicker" name="adoption.orderReceivedDate"
                                                         displayFormat="yyyy-MM-dd"
                                                         onchange="javascript:splitDate('receivedDatePicker')"/></td>
    </tr>
    <tr>
        <td>අධිකරණය<br/>
            Court
        </td>
        <td><s:textfield name="adoption.court" id="court"/></td>
    </tr>
    <tr>
        <td>නියෝගය නිකුත් කල දිනය <br/>
            Issued Date
        </td>
        <td style="text-align:right;"><sx:datetimepicker id="orderIssuedDatePicker" name="adoption.orderIssuedDate"
                                                         displayFormat="yyyy-MM-dd"
                                                         onchange="javascript:splitDate('issueDatePicker')"/></td>
    </tr>
    <tr>
        <td>නියෝග අංකය<br/>
            Serial number
        </td>
        <td><s:textfield name="adoption.courtOrderNumber" id="courtOrderNumber"/></td>
    </tr>
    <tr>
        <td>විනිසුරු නම <br/>
            Name of the Judge
        </td>
        <td><s:textfield name="adoption.judgeName" id="judgeName"/></td>
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
        <td>පියා   </br>
            Father
        </td>
        <td>
            <s:radio name="adoption.applicantMother" list="#@java.util.HashMap@{'false':''}" value="false"/>
        </td>
        <td>මව <br/>
            Mother
        </td>
        <td>
            <s:radio name="adoption.applicantMother" list="#@java.util.HashMap@{'true':''}"/>
        </td>
    </tr>
    <tr>
        <td colspan="3">අයදුම් කරුගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br/>
            தாயின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம் <br/>
            Applicant's PIN / NIC Number
        </td>
        <td colspan="2">
            <s:textfield id="applicantPINorNIC" name="adoption.applicantPINorNIC"/>
        </td>
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
        <td><s:textfield id="applicantCountryId" name="adoption.applicantCountryId"/></td>
        <td>ගමන් බලපත්‍ර අංකය <br/>
            கடவுச் சீட்டு <br/>
            Passport No.
        </td>
        <td><s:textfield id="applicantPassport" name="adoption.applicantPassport"/></td>
    </tr>
    <tr>
        <td>නම <br/>
            Name of the Applicant
        </td>
        <td colspan="4"><s:textarea id="applicantName" name="adoption.applicantName"/></td>
    </tr>

    <tr>
        <td>ලිපිනය <br/>
            Address
        </td>
        <td colspan="4"><s:textarea name="adoption.applicantAddress" id="adoption.applicantAddress"/></td>
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
        <td colspan="2"><s:textfield name="adoption.wifePINorNIC" id="wifePINorNIC"/></td>
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
        <td width="175px"><s:textfield name="adoption.wifeCountryId" id="wifeCountryId"/> </td>
        <td width="175px">ගමන් බලපත්‍ර අංකය <br/>
            கடவுச் சீட்டு <br/>
            Passport No.
        </td>
        <td width="175px"><s:textfield name="adoption.wifePassport" id="wifePassport"/> </td>
    </tr>
    <tr>
        <td> මවගේ නම <br/>
            Name of Mother
        </td>
        <td colspan="4"><s:textarea name="adoption.wifeName" id="wifeName"/></td>
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
        <td colspan="2" style="text-align:right;"><sx:datetimepicker id="bdayDatePicker" name="adoption.childBirthDate"
                                                                     displayFormat="yyyy-MM-dd"
                                                                     onchange="javascript:splitDate('bdayDatePicker')"/></td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br/>
            Gender
        </td>
        <td><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="" headerKey="0" headerValue="%{getText('select_gender.label')}"
                cssStyle="width:190px; margin-left:5px;"/></td>
    </tr>
    <tr>
        <td>වයස <br/>
            Age
        </td>
        <td>අවුරුදු <br/>
            Years
        </td>
        <td><s:textfield name="adoption.childAgeYears" id="childAgeYears"/></td>
        <td>මාස <br/>
            Months
        </td>
        <td><s:textfield name="adoption.childAgeMonths" id="childAgeMonths"/></td>
    </tr>
    <tr>
        <td>දැනට පවතින නම <br/>
            (නමක් දී ඇති නම්) <br/>
            Existing Name <br/>
            (if already given)
        </td>
        <td colspan="4"><s:textarea name="adoption.childExistingName" id="childExistingName"/></td>
    </tr>
    <tr>
        <td>ලබා දෙන නම <br/>
            New name given
        </td>
        <td colspan="4"><s:textarea name="adoption.childNewName" id="childNewName"/></td>
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
        <td width="70%">උප්පැන්න සහතිකයේ අනුක්‍රමික අංකය <br/>
            The serial number of the Birth Certificate
        </td>
        <td width="30%"><s:textfield name="adoption.birthCertificateSerial" id="" cssStyle="width:85%;"/></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table">
    <tr>
        <td>හෝ<br/>OR
        </td>
    </tr>
</table>
<table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col width="330px"/>
    <col width="700px"/>
    <tbody>
    <tr>
        <td colspan="2" style="text-align:center;">උපත ලියපදින්ච්චි කිරීමේ රිසීට් පතේ සටහන් <br/>
            Birth Registration acknowledgement slip
        </td>
    </tr>
    <tr>
        <td>දිස්ත්‍රික්කය <br/>
            District
        </td>
        <td><s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                      value="birthDistrictId" cssStyle="width:240px;"/>
        </td>
    </tr>
    <tr>
        <td>ප්‍රාදේශීය ලේකම් කොට්ටාශය <br/>
            Divisional Secretariat
        </td>
        <td>
            <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                      cssStyle="float:left;  width:240px;"/>
        </td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ටාශය <br/>
            Registration Division
        </td>
        <td>
            <s:select id="birthDivisionId" name="adoption.birthDivisionId" value="%{birthDivisionId}"
                      list="bdDivisionList"
                      cssStyle=" width:240px;float:right;"/>
        </td>
    </tr>
    <tr>
        <td>අනුක්‍රමික අංකය <br/>
            Serial Number
        </td>
        <td><s:textfield name="adoption.birthCertificateSerial" id="birthCertificateSerial"/> </td>
    </tr>
    </tbody>
</table>
<s:hidden name="pageNo" value="1"/>
<div class="adoption-form-submit">
    <s:submit value="%{getText('submit.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
<script type="text/javascript">
    function validate()
    {
        var errormsgOut = "";
        var element;
        var returnval;
        var flag = false;
        var inputs = new Array(9);
        var errormsg = new Array("receivedDate", "court", "orderIssuedDate", "courtOrderNumber", "judgeName",
                "applicantName", "applicantAddress", "childAgeYears", "childAgeMonths");

        //these inpute can not be null
        inputs[0] = new Date(document.getElementById("receivedDatePicker").value);
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
        //check elements which are can not be null
        for (i = 0; i < inputs.length; i++)
        {
            if (inputs[i].length <= 0)
            {
                errormsgOut = errormsgOut + "plese Enter  " + errormsg[i] + "\n";
            }
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
</script>
</div>