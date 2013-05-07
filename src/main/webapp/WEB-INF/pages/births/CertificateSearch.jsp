<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" src="../js/validate.js"></script>

<s:hidden id="error1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error2" value="%{getText('enter.submitDate.label')}"/>
<s:hidden id="error3" value="%{getText('enter.serial.label')}"/>
<s:hidden id="error4" value="%{getText('submitDate.label')}"/>
<s:hidden id="error5" value="%{getText('enter.applicantName.label')}"/>
<s:hidden id="error6" value="%{getText('enter.applicantAddress.label')}"/>
<s:hidden id="error7" value="%{getText('enter.stampCharges.label')}"/>
<s:hidden id="error8" value="%{getText('stampCharges.label')}"/>
<s:hidden id="error9" value="%{getText('noOfCopies.label')}"/>
<s:hidden id="error10" value="%{getText('certificateIssuDate.label')}"/>
<s:hidden id="error11" value="%{getText('enter.noOfCopies.label')}"/>
<s:hidden id="error12" value="%{getText('enter.searchFullNameOfficialLang.label')}"/>
<s:hidden id="error13" value="%{getText('serial.label')}"/>
<s:hidden id="error14" value="%{getText('enter.registrationDivision.label')}"/>
<s:hidden id="error15" value="%{getText('enter.serialNo.label')}"/>
<s:hidden id="error16" value="%{getText('enter.certificate.pin.no.label')}"/>
<s:hidden id="select" value="%{getText('select.label')}"/>
<div class="birth-certificate-search-form-outer" id="birth-certificate-search-form-outer">
<script>

$(function() {
    $("#datePicker").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});
$(function() {
    $("#dateOdEvent").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});
$(function() {
    $("#dateOfSubmission").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});

$(function() {
    var id = $("select#districtId").attr("value");

    if (id == 0) {
        $('select#dsDivisionId').attr('disabled', 'disabled');
    }
    $('select#districtId').bind('change', function(evt1) {
        var id = $("select#districtId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:16},
                function(data) {
                    var options1 = '';
                    options1 = '<option value="0">' + $('#select').val() + '</option>';
                    if (id > 0) {
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                    }
                    $("select#dsDivisionId").html(options1);

                });
        if (id == 0) {
            $('select#dsDivisionId').attr('disabled', 'disabled');
        } else {
            $('select#dsDivisionId').removeAttr('disabled');
        }
    });


    $('img#personName').bind('click', function(evt4) {
        var text = $("textarea#searchFullNameOfficialLang").attr("value");

        $.post('/ecivil/TransliterationService', {text:text,gender:'U'},
                function(data) {
                    if (data != null) {
                        var s = data.translated;
                        $("textarea#searchFullNameEnglish").val(s);
                    }
                });
    });

    $('#noOfCopies').bind('change', function(evt4) {
        var stampCharge = '<s:property value="@lk.rgd.crs.web.WebConstants@STAMP_CHARGES"/>';
        var noOfCopies = $('#noOfCopies').attr("value");
        $('#stampCharges').val(stampCharge * noOfCopies);
    });

});

var errormsg = "";

function validate() {
    var domObject;
    var returnval = true;

    commonTags();

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

function commonTags() {
    var domObject;

    // validate application number field
    domObject = document.getElementById('applicationNo');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, '', 'error3');

    // validate submitted date
    domObject = document.getElementById('dateOfSubmission');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, '', 'error2');
    else
        isDate(domObject.value, 'error1', 'error4');

    // validate applicant data
    domObject = document.getElementById('applicantFullName');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, '', 'error5')

    domObject = document.getElementById('applicantAddress');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, '', 'error6');

    domObject = document.getElementById('searchFullNameOfficialLang');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, '', 'error12');

    // validate number of copies needed
    domObject = document.getElementById('noOfCopies');
    if (isFieldEmpty(domObject)) {
        isEmpty(domObject, '', 'error11');
    } else {
        isNumeric(domObject.value, 'error1', 'error9');
    }

    // validate stamp charges field
    domObject = document.getElementById('stampCharges');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, '', 'error7');
    else
        validateNumber(domObject.value, 'error1', 'error8');
}

function initPage() {
}

function validateMandatory() {
    var domObject;
    var returnval = true;

    mandatoryFields();

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

function mandatoryFields() {
    var domObject;

    // validate application number field
    domObject = document.getElementById('applicationNo');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, '', 'error3');

    // validate submitted date
    domObject = document.getElementById('dateOfSubmission');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, '', 'error2');
    else
        isDate(domObject.value, 'error1', 'error4');

    // validate applicant data
    domObject = document.getElementById('applicantFullName');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, '', 'error5')

    domObject = document.getElementById('applicantAddress');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, '', 'error6');

    if (isFieldEmpty(document.getElementById('certificateNo')) && isFieldEmpty(document.getElementById('searchPIN')))
        isEmpty(document.getElementById('certificateNo'), '', 'error16')

}

function numbersonly(e, decimal) {
    var key;
    var keychar;

    if (window.event)
        key = window.event.keyCode;
    else if (e)
        key = e.which;
    else
        return true;

    keychar = String.fromCharCode(key);
    if ((key == null) || (key == 0) || (key == 8) || (key == 9) || (key == 13) || (key == 27))
        return true;
    else if ((("0123456789").indexOf(keychar) > -1))
        return true;
    else if (decimal && (keychar == "."))
        return true;
    else
        return false;
}
</script>
<s:if test="certificateType.ordinal() == 0">
    <s:url id="certificateSearch" value="eprBirthCertificateSearch.do"/>
</s:if>
<s:elseif test="certificateType.ordinal() == 1">
    <s:url id="certificateSearch" value="eprDeathCertificateSearch.do"/>
</s:elseif>

<s:form action="%{certificateSearch}" name="birthCertificateSearchForm" id="birth-certificate-search-form-1"
        method="POST">
<table style="font-size:9pt;">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td align="center" style="font-size:12pt; width:180px">
            <img src="<s:url value="/images/official-logo.png"></s:url>" alt=""/><br>
        </td>
        <td align="center" style="font-size:12pt;">
            <s:if test="certificateType.ordinal() == 0">
                <label>උප්පැන්න සහතිකයක් ගැනීමේ සහ/නොහොත් ලේකම් පොත් සොයා බැලීමේ ඉල්ලුම් පත්‍රය
                    <br>பிறப்புச் சான்றிதழ் பெறல் மற்றும் /அல்லது பதிவினை தேடுவதற்கான விண்ணப்ப படிவம்
                    <br>APPLICATION FOR BIRTH CERTIFICATE AND/OR SEARCH OF REGISTERS</label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label>මරණ සහතිකයක් ගැනීමේ සහ/ නොහොත් ලේකම් පොත් සොයා බැලීමේ ඉල්ලුම් පත්‍රය
                    <br>இறப்புச் சான்றிதழ் பெறல் மற்றும் /அல்லது பதிவினை தேடுவதற்கான விண்ணப்ப படிவம்
                    <br>APPLICATION FOR DEATH CERTIFICATE AND/OR SEARCH OF REGISTERS</label>
            </s:elseif>
        </td>
        <td width="300px">
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <s:fielderror name="duplicateApplicationNoError" cssStyle="color:red;font-size:10pt"/>
                </tr>
                <tr>
                    <td><label><span
                            class="font-8">ඉල්ලුම් පත්‍ර අංකය <s:label value="*"
                                                                       cssStyle="color:red;font-size:10pt"/><br>விண்ணப்ப இலக்கம்<br>Application No</span></label>
                    </td>
                    <td><s:textfield name="certSearch.certificate.applicationNo" id="applicationNo"
                                     cssStyle="text-transform:uppercase;"/></td>
                </tr>
            </table>
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <td align="center" colspan="2">කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>அலுவலக பாவனைக்காக மட்டும்
                        <br>For office use only
                        <hr>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label><span
                                class="font-8">භාරගත්  දිනය<s:label value="*"
                                                                    cssStyle="color:red;font-size:10pt"/><br>பிறப்பைப் பதிவு திகதி <br>Submitted Date</span></label>
                    </td>
                    <td>
                            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
                            <s:textfield id="dateOfSubmission"
                                         name="certSearch.certificate.dateOfSubmission" maxLength="10"/>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="8">
            <s:if test="certificateType.ordinal() == 0">
                <label>උපත සිදුවූ දිස්ත්‍රික්කයේ දිස්ත්‍රික් රෙජිස්ට්‍රාර් වෙත යැවිය යුතුය.
                    <br>பிறப்பு நிகழ்ந்த மாவட்டத்தின் மாவட்ட பதிவாளருக்கு அனுப்பப்பட வேண்டும்
                    <br>To be sent to the Office of the District Registrar of the District in which the birth
                    occurred.</label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label>මරණය සිදුවූ දිස්ත්‍රික්කයේ දිස්ත්‍රික් රෙජිස්ට්‍රාර් වෙත යැවිය යුතුය.
                    <br>இறப்பு நிகழ்ந்த மாவட்டத்தின் மாவட்ட பதிவாளருக்கு அனுப்பப்பட வேண்டும்
                    <br>To be sent to the Office of the District Registrar of the District in which the death
                    occurred.</label>
            </s:elseif>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_01" cellspacing="0" cellpadding="2px">
    <tr>
        <td class="font-9" width="400px"><label>(1) ඉල්ලුම්කරුගේ සම්පූර්ණ නම<s:label value="*"
                                                                                     cssStyle="color:red;font-size:10pt"/><br>விண்ணப்பதாரரின்
            முழுப் பெயர் <br>
            Full Name of the Applicant</label></td>
        <td colspan="6"><s:textarea name="certSearch.certificate.applicantFullName" id="applicantFullName"
                                    cssStyle="text-transform:uppercase;"/></td>
    </tr>
    <tr>
        <td class="font-9"><label>ඉල්ලුම්කරුගේ ලිපිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/><br>விண்ணப்பதாரரின்
            முகவரி <br>
            Address of the Applicant</label></td>
        <td colspan="6"><s:textarea name="certSearch.certificate.applicantAddress" id="applicantAddress"
                                    cssStyle="text-transform:uppercase;"/></td>
    </tr>
    <tr>
        <td class="font-9">
            <s:if test="certificateType.ordinal() == 0">
                <label>(2) ඉල්ලුම්කරන්නේ කාගේ උප්පැන්නය ගැනද? <br>එම අයගේ සම්පූර්ණ නම රාජ්‍ය භාෂාවෙන්
                    (සිංහල / දෙමළ)<s:label value="*" cssStyle="color:red;font-size:10pt"/><br>விண்ணப்பிப்பது யாருடைய
                    பிறப்பினை? <br>
                    அவரின் முழுப்பெயர் அரச கரும மொழியில்(சிங்களம்/ தழிழ்) <br>
                    Full Name of the person respecting whose birth application is made (Sinhala/Tamil) ?</label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label>(2) ඉල්ලුම් කරන්නේ කාගේ මරණය ගැනද? <br>එම අයගේ සම්පූර්ණ නම රාජ්‍ය භාෂාවෙන්
                    (සිංහල / දෙමළ)<s:label value="*" cssStyle="color:red;font-size:10pt"/><br>விண்ணப்பிப்பது யாருடைய
                    இறப்பினை? <br>
                    அவரின் முழுப்பெயர் அரச கரும மொழியில்(சிங்களம்/ தழிழ்) <br>
                    Full Name of the person respecting whose death application is made (Sinhala/Tamil) ?</label>
            </s:elseif>
        </td>
        <td colspan="6">
            <s:textarea name="certSearch.search.searchFullNameOfficialLang" id="searchFullNameOfficialLang"
                        cssStyle="text-transform:uppercase;"/>
        </td>
    </tr>
    <tr>
        <td class="font-9">
            <s:if test="certificateType.ordinal() == 0">
                <label> ඉල්ලුම්කරන්නේ කාගේ උප්පැන්නය ගැනද? <br>එම අයගේ සම්පූර්ණ නම ඉංග්‍රීසි භාෂාවෙන්
                    <s:label value="*" cssStyle="color:red;font-size:10pt"/>
                    <br>விண்ணப்பிப்பது யாருடைய பிறப்பினை? <br>
                    அவரின் முழுப்பெயர் ஆங்கில மொழியில் <br>
                    Full Name of the person respecting whose birth application is made in English ?</label>
            </s:if>
            <s:if test="certificateType.ordinal() == 1">
                <label> ඉල්ලුම් කරන්නේ කාගේ මරණය ගැනද? <br>එම අයගේ සම්පූර්ණ නම ඉංග්‍රීසි භාෂාවෙන් <s:label value="*"
                                                                                                           cssStyle="color:red;font-size:10pt"/>
                    <br>விண்ணப்பிப்பது யாருடைய இறப்பினை?
                    அவரின் முழுப்பெயர் ஆங்கில மொழியில்<br>
                    Full Name of the person respecting whose death application is made in English ?</label>
            </s:if>
        </td>
        <td colspan="6"><s:textarea name="certSearch.search.searchFullNameEnglish" id="searchFullNameEnglish"
                                    cssStyle="text-transform:uppercase;"/><br/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px"
                 id="personName">
        </td>
    </tr>
    <tr>
        <td class="font-9"><label>ස්ත්‍රී පුරුෂ භාවය<br> பால் <br>Gender of the child</label></td>
        <td><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="certSearch.search.gender" headerKey="0" headerValue="%{getText('select_gender.label')}"
                cssStyle="width:190px; margin-left:5px;"/></td>
        <td><label>අවශ‍ය පිටපත් ගණන<s:label value="*" cssStyle="color:red;font-size:14pt"/><br>தேவையான பிரதிகளின்
            எண்ணிக்கை<br>No. of Copies required</label></td>
        <td><s:textfield name="certSearch.certificate.noOfCopies" id="noOfCopies" maxLength="2"
                         onKeyPress="return numbersonly(event,true)"/></td>
    </tr>
    <tr>
        <td class="font-9" rowspan="2"><label>(3) රෙජිසිට්‍රර්ගේ කොට්ඨාශය
            <s:label/><br>பதிவாளர் பிரிவு <br>Registrar's
            Division</label></td>
        <td><label>දිස්ත්‍රික්කය மாவட்டம் District</label></td>
        <td colspan="3" class="table_reg_cell_01">
            <s:select id="districtId" name="birthDistrictId" list="allDistrictList" value="birthDistrictId"
                      headerKey="0"
                      headerValue="%{getText('select.label')}"
                      cssStyle="width:98.5%;"/>
        </td>
    </tr>
    <tr>
        <td><label>
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය
            <br/>பிரதேச செயளாளர் பிரிவு
            <br/>Divisional Secretary Division
        </label></td>
        <td colspan="3" class="table_reg_cell_01" id="table_reg_cell_01">
            <s:select id="dsDivisionId" name="dsDivisionId" list="allDSDivisionList" value="%{dsDivisionId}"
                      headerKey="0"
                      headerValue="%{getText('select.label')}"
                      cssStyle="width:98.5%;"/>
        </td>
    <tr>
    <tr>
        <td class="font-9"><label>
            (4) මෙහි ඇලවූ මුද්දරවල වටිනාකම<s:label value="*" cssStyle="color:red;font-size:10pt"/><br>இங்கு
            ஒட்டப்பட்ட
            முத்திரைகளின் பெறுமதி <br>Value of stamps affixed</label>
        </td>
        <td colspan="6"><s:textfield name="certSearch.certificate.stampCharges" id="stampCharges"
                                     maxLength="5" onKeyPress="return numbersonly(event,true)"/></td>
    </tr>
    </tbody>
</table>
<s:hidden name="pageNo" value="1"/>

<div class="form-submit">
    <s:submit value="%{getText('bdfSearch.button')}" cssStyle="margin-top:10px;"
              onclick="javascript:return validate()"/>
</div>

<table class="table_reg_page_01" cellspacing="0" cellpadding="2px">
    <tr>
        <td class="font-9">
            <s:if test="certificateType.ordinal() == 0">
                <label> උප්පැන්න සහතිකයේ අංකය<br>பிறப்புச் சான்றிதழின் இலக்கம் <br>Birth Certificate
                    Number</label>
            </s:if>
            <s:elseif test="certificateType.ordinal() == 1">
                <label> මරණ සහතිකයේ අංකය<br>இறப்புச் சான்றிதழின் இலக்கம் <br>Death Certificate Number</label>
            </s:elseif>
        </td>
        <td><s:textfield name="certSearch.search.certificateNo" id="certificateNo"
                         onKeyPress="return numbersonly(event,true)"/></td>
    </tr>
    <tr>
        <td class="font-9"><label>
            අනන්‍යතා අංකය <br>அடையாள எண் <br>Identification Number</label>
        </td>
        <td colspan="3"><s:textfield name="certSearch.search.searchPIN" maxLength="12" id="searchPIN"
                                     onKeyPress="return numbersonly(event,true)"/></td>
    </tr>
</table>
<div class="form-submit">
    <s:submit value="%{getText('bdfSearch.button')}" cssStyle="margin-top:10px;"
              onclick="javascript:return validateMandatory()"/>
</div>

</s:form>
</div>
