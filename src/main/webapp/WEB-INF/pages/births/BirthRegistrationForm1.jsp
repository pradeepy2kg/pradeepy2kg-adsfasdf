<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set value="1" name="row"/>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<s:if test="birthType.ordinal()==0">
    <%--still birth--%>
    <s:set name="row" value="1"/>
</s:if>
<s:elseif test="birthType.ordinal() == 1 || birthType.ordinal() == 3">
    <%--live birth--%>
    <s:set name="row" value="1"/>
</s:elseif>
<s:elseif test="birthType.ordinal()==2">
    <%--adoption--%>
    <s:set name="row" value="1"/>
</s:elseif>

<div class="birth-registration-form-outer" id="birth-registration-form-1-outer">
<script>
$(function() {
    $("#submitDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});

$(function() {
    $("#birthDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});

// mode 1 = passing District, will return DS list
// mode 2 = passing DsDivision, will return BD list
// any other = passing district, will return DS list and the BD list for the first DS
$(function() {
    $('select#districtId').bind('change', function(evt1) {
        var id = $("select#districtId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
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
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                 function(data) {
                     var options = '';
                     var bd = data.bdDivisionList;
                     for (var i = 0; i < bd.length; i++) {
                         options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                     }
                     $("select#birthDivisionId").html(options);
                 });
    });


    $('img#childName').bind('click', function(evt3) {
        var id = $("textarea#childFullNameOfficialLang").attr("value");
        var wsMethod = "transliterate";
        var soapNs = "http://translitwebservice.transliteration.icta.com/";

        var soapBody = new SOAPObject("trans:" + wsMethod); //Create a new request object
        soapBody.attr("xmlns:trans", soapNs);
        soapBody.appendChild(new SOAPObject('InputName')).val(id);
        soapBody.appendChild(new SOAPObject('SourceLanguage')).val(0);
        soapBody.appendChild(new SOAPObject('TargetLanguage')).val(3);
        soapBody.appendChild(new SOAPObject('Gender')).val('U');

        //Create a new SOAP Request
        var sr = new SOAPRequest(soapNs + wsMethod, soapBody); //Request is ready to be sent

        //Lets send it
        SOAPClient.Proxy = "/TransliterationWebService/TransliterationService";
        SOAPClient.SendRequest(sr, processResponse1); //Send request to server and assign a callback
    });

    function processResponse1(respObj) {
        //respObj is a JSON equivalent of SOAP Response XML (all namespaces are dropped)
        $("textarea#childFullNameEnglish").val(respObj.Body[0].transliterateResponse[0].return[0].Text);
    };

    $('img#place').bind('click', function(evt4) {
        var id = $("input#placeOfBirth").attr("value");
        var wsMethod = "transliterate";
        var soapNs = "http://translitwebservice.transliteration.icta.com/";

        var soapBody = new SOAPObject("trans:" + wsMethod); //Create a new request object
        soapBody.attr("xmlns:trans", soapNs);
        soapBody.appendChild(new SOAPObject('InputName')).val(id);
        soapBody.appendChild(new SOAPObject('SourceLanguage')).val(0);
        soapBody.appendChild(new SOAPObject('TargetLanguage')).val(3);
        //soapBody.appendChild(new SOAPObject('Gender')).val('U');

        //added by shan [ NOT Tested ] -> start
        var genderVal = $("select#genderList").attr("value");
        soapBody.appendChild(new SOAPObject('Gender')).val(genderVal);
        //-> end

        //Create a new SOAP Request
        var sr = new SOAPRequest(soapNs + wsMethod, soapBody); //Request is ready to be sent

        //Lets send it
        SOAPClient.Proxy = "/TransliterationWebService/TransliterationService";
        SOAPClient.SendRequest(sr, processResponse2); //Send request to server and assign a callback
    });

    function processResponse2(respObj) {
        //respObj is a JSON equivalent of SOAP Response XML (all namespaces are dropped)
        $("input#placeOfBirthEnglish").val(respObj.Body[0].transliterateResponse[0].
        return[0].Text
    )
        ;
    }
});

var errormsg = "";
function validate() {
    //skipping low priority data validations
    var check = document.getElementById('skipjs');
    var returnval = true;
    var declarationType = document.getElementById('birthTypeId');
    var birthdate = new Date(document.getElementById('birthDatePicker').value);
    var submit = new Date(document.getElementById('submitDatePicker').value);

    if (declarationType.value == 0) {
        commonTags();
        stillBirthCommonTags(check);
    }
    if (declarationType.value == 1 || declarationType.value == 3) {
        commonTags();
        liveBirthCommonTags(check)
        dateRange();
    }
    if (declarationType.value == 2) {
        commonTags();
        liveBirthCommonTags(check);
    }

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

//common tags
function commonTags() {
    var domObject;
    //serial number
    domObject = document.getElementById('bdfSerialNo');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, "", 'error1');
    else
        validateSerialNo(domObject, 'error13', 'error20');


    //date of register
    domObject = document.getElementById('submitDatePicker');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, "", 'error9')
    else
        isDate(domObject.value, "error13", "submitDate");

    //date of birth
    domObject = document.getElementById('birthDatePicker');
    if (isFieldEmpty(domObject))
        isEmpty(domObject, "", 'error10')
    else
        isDate(domObject.value, "error13", "dob");

    var submit = new Date(document.getElementById('submitDatePicker').value);
    var birthdate = new Date(document.getElementById('birthDatePicker').value);
    // compare birth date and date of registration
    if (birthdate.getTime() > submit.getTime()) {
        errormsg = errormsg + "\n" + document.getElementById('error6').value;
    }

    //place of birth
    domObject = document.getElementById('placeOfBirth');
    isEmpty(domObject, "", 'error11')
}

//still birth tags
function stillBirthCommonTags(check) {
    var domObject;
    //Number of weeks pregnant
    domObject = document.getElementById('weeksPregnant');
    if (!check.checked)
        isEmpty(domObject, "", 'error14');
    isNumeric(domObject.value, 'error13', 'error26')

    // child rank
    domObject = document.getElementById('childRank');
    if (!check.checked) {
        isEmpty(domObject, "", 'error5')
        if (domObject.value < 1 && !isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error13').value + " : " + document.getElementById('childRankGTZero').value;
        }
    }
    isNumeric(domObject.value, 'error13', 'error24')

    // number of child
    domObject = document.getElementById('numberOfChildrenBorn');
    isNumeric(domObject.value, 'error13', 'error25')
    if (!check.checked) {
        if (domObject.value < 2 && !isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error13').value + " : " + document.getElementById('numChildGTOne').value;
        }
    }
}

function liveBirthCommonTags(check) {
    var domObject;
    // child name
    domObject = document.getElementById('childFullNameOfficialLang');
    if (!check.checked)
        isEmpty(domObject, "", 'error2');

    domObject = document.getElementById('childFullNameEnglish');
    if (!check.checked)
        isEmpty(domObject, "", 'error3');

    //birth weight
    domObject = document.getElementById('childBirthWeight');
    if (!check.checked) {
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error4');
        } else {
            validateBirthWeight(domObject, 'error13', 'error23')
        }
    }

    // child rank
    domObject = document.getElementById('childRank');
    if (!check.checked) {
        isEmpty(domObject, "", 'error5')
        if (domObject.value < 1 && !isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error13').value + " : " + document.getElementById('childRankGTZero').value;
        }
    }
    isNumeric(domObject.value, 'error13', 'error24')

    // number of child
    domObject = document.getElementById('numberOfChildrenBorn');
    isNumeric(domObject.value, 'error13', 'error25')
    if (!check.checked) {
        if (domObject.value < 2 && !isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error13').value + " : " + document.getElementById('numChildGTOne').value;
        }
    }
}
// validate birth weight of the child
function validateBirthWeight(domElement, errorText, errorCode) {
    with (domElement) {
        var mode = false;
        var reg = /^([0-9]\.?([0-9]*))$/;
        if (reg.test(value.trim()) == false) {
            mode = true;
        } else if (value.trim() >= 10) {
            mode = true;
        }
        if (mode) {
            errormsg = errormsg + "\n" + document.getElementById(errorText).value + " : " + document.getElementById(errorCode).value;
        }
    }
}

//check live birth is a belated birth gives warnings
function dateRange() {
    var declarationType = document.getElementById('birthTypeId');
    if (declarationType.value != 0) {
        var birthdate = new Date(document.getElementById('birthDatePicker').value);
        var submit = new Date(document.getElementById('submitDatePicker').value);
        //comparing 90 days delay
        var one_day = 1000 * 60 * 60 * 24;
        var numDays = Math.ceil((submit.getTime() - birthdate.getTime()) / (one_day));
        if (numDays >= 90) {
            if (numDays >= 365) {
                document.getElementById('belatedError').innerHTML = document.getElementById('error8').value;
            } else {

                document.getElementById('belatedError').innerHTML = document.getElementById('error7').value;
            }
        }
        else {
            document.getElementById('belatedError').innerHTML = '';
        }
    }
}
function initSerialNumber() {
    var domobject = document.getElementById('bdfSerialNo');
    if (isFieldEmpty(domobject)) {
        domobject.value = new Date().getFullYear() + "0";
    }
}

function initPage() {
    dateRange();
    initSerialNumber();
}
</script>

<s:form action="eprBirthRegistration.do" name="birthRegistrationForm1" id="birth-registration-form-1" method="POST"
        onsubmit="javascript:return validate()">

<table class="table_reg_header_01" style="font-size:9pt">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="300px"></td>
        <td align="center" style="font-size:12pt; width:430px"><img src="<s:url value="/images/official-logo.png"/>"
                                                                    alt=""/><br>
            <s:if test="birthType.ordinal() == 1 || birthType.ordinal() == 3">
                <label>උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර
                    <br>ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள்
                    <br>Particulars for Registration of a Birth</label>
            </s:if>
            <s:elseif test="birthType.ordinal() == 0">
                <label>
                    මළ උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර
                    <br>ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள்
                    <br>Particulars for Registration of a Still Birth</label>
            </s:elseif>
            <s:elseif test="birthType.ordinal() == 2">
                දරුකමට හදාගත් ළමයකුගේ උප්පැන්නය නැවත ලියාපදිංචි කිරීම
                <br>ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள்
                <br>Re-registration of the Birth of an Adopted Child
            </s:elseif>
        </td>
        <td>
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <s:fielderror name="duplicateSerialNumberError" cssStyle="color:red;font-size:9pt;"/>
                </tr>
                <tr>
                    <td><label><span class="font-8">අනුක්‍රමික අංකය<s:label value="*"
                                                                            cssStyle="color:red;font-size:10pt;"/> <br>தொடர் இலக்கம்<br>Serial Number</span></label>
                    </td>
                    <td>
                            <%--                        <s:if test="editMode">
                                <s:textfield name="register.bdfSerialNo" id="bdfSerialNo" readonly="true"/>
                            </s:if>
                            <s:else>--%>
                        <s:textfield name="register.bdfSerialNo" id="bdfSerialNo" maxLength="10"/>
                            <%--<s:label value="*" cssStyle="color:red;font-size:15pt"/>         --%>
                            <%--    </s:else>--%>
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
                        <label>
                            <span class="font-8">භාරගත්  දිනය<s:label value="*"
                                                                      cssStyle="color:red;font-size:10pt;"/> <br>பிறப்பைப் பதிவு திகதி <br>Submitted Date</span>
                        </label>
                    </td>
                    <td><s:label value="YYYY-MM-DD" cssStyle="margin-left:20px;font-size:10px"/><br>
                        <s:textfield name="register.dateOfRegistration" id="submitDatePicker" maxLength="10"/>
                    </td>

                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <s:if test="birthType.ordinal() == 1 || birthType.ordinal() == 3">
                දැනුම් දෙන්නා (දෙමවිපියන් / භාරකරු) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරි වෙත භාර දිය
                යුතුය. මෙම
                තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ උපත ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம்
                சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the informant (Parent / Guardian) and the duly completed form should be
                forwarded
                to the Notifying Authority. The birth will be registered in the Civil Registration System based on the
                information provided in this form.
            </s:if>
            <s:elseif test="birthType.ordinal() == 0">
                දැනුම් දෙන්නා (දෙමවිපියන් / නෑයන්) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරි වෙත භාර දිය යුතුය.
                මෙම තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ මළ උපත ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம் சமா்ப்பித்தல் வேண்டும்.
                இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the informant (Parent / Relative) and the duly completed form should be
                forwarded to the Notifying Authority. The still birth will be registered in the Civil Registration
                System based on the information provided in this form.
            </s:elseif>
            <s:elseif test="birthType.ordinal() == 2">
                දරුකමට හදාගන්න දෙමව්පියන් විසින් සම්පුර්ණ කර, දරුවා උපත ලැබූ දිස්ත්‍රික්කය අයත් සහකාර රෙජිස්ට්‍රාර් ජනරාල් වෙත හෝ කොළඹ රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුවේ ප්‍රධාන කාර්යාලය වෙත භාර දිය යුතුය. මෙම තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ උපත නැවත ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம் சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the adopting parents, and the duly completed form should be forwarded to the Assistant Registrar General in charge of the zone where the birth of the child occurred; or to the head office of the Registrar Generals Department in Colombo. The birth will be re-registered in the
                Civil Registration System based on the information provided in this form.
            </s:elseif>
        </td>
    </tr>
    </tbody>
</table>


<table class="table_reg_page_01" cellspacing="0" cellpadding="0" style="margin-bottom:0;">
<caption></caption>
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
    <td class="font-9" colspan="8" style="text-align:center;">
        <s:if test="birthType.ordinal() == 0">
            මළ උපත පිලිබඳ විස්තර
            <br>பிள்ளை பற்றிய தகவல்
            <br>Still-birth Information
        </s:if>
        <s:else>
            ළම‌යාගේ විස්තර
            <br>பிள்ளை பற்றிய தகவல்
            <br>Child's Information
        </s:else>
    </td>
</tr>
    <%--TODO style not added--%>
<s:if test="birthType.ordinal() == 2">
    <tr style="border-left:1px solid #000000;">
        <td width="150px" colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)දරුකමට
            ගැනීම පිළිබඳ සහතික පත්‍රයේ අංකය<br> மகவேற்புச் செய்யப்பட்டது சம்பற்தமான சான்றிதழின் இலக்கம்<br>Serial
            Number of the Certificate of Adoption</label></td>
        <td colspan="7"><s:label value="%{#session.birthRegister.register.adoptionUKey}"/></td>
    </tr>
    <tr>
        <td rowspan="5"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            ළමයාගේ උපත කලින්
            ලියාපදිංචි කර තිබුනේනමි<br>பிள்ளையின் பிறப்பு பதிவு செய்யப்பட்டிருந்தால்<br>If the birth was
            previously registered</label></td>
        <td><label>දිස්ත්‍රික්කය / மாவட்டம் / District</label></td>
        <td colspan="6" class="table_reg_cell_01"><s:label value="%{#session.oldBdfForAdoption.districtName}"/></td>
    </tr>
    <tr>
        <td><label>ප්‍රාදේශීය ලේකමි කොටිඨාශය<br>பிரதேச செயளாளா் பிரிவு <br>Divisional Secretariat</label></td>
        <td colspan="6" class="table_reg_cell_01"><s:label
                value="%{#session.oldBdfForAdoption.dsDivisionName}"/></td>
    </tr>
    <tr>
        <td><label>ලියාපදිංචි කිරීමේ කොටිඨාශය<br>பதிவுப் பிரிவு<br>Registration Division</label></td>
        <td colspan="6" class="table_reg_cell_01"><s:label
                value="%{#session.oldBdfForAdoption.bdDivisionName}"/></td>
    </tr>
    <tr>
        <td><label>අනුක්‍රමික අංකය/ தொடர் இலக்கம்<br>Serial Number</label></td>
        <td colspan="6"><s:label value="%{#session.oldBdfForAdoption.serialNumber}"/></td>
    </tr>
</s:if>
<tr></tr>
<tr style="border-left:1px solid #000000;">
    <td width="150px" align="left"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)උපන් දිනය
        <s:label value="*" cssStyle="color:red;font-size:14pt;"/><br>
        பிறந்த திகதி<br>Date of Birth</label></td>
    <td colspan="3" style="border-right:none;">
        <s:label value="YYYY-MM-DD" cssStyle="margin-left:20px;font-size:10px"/><br>
        <s:textfield id="birthDatePicker" name="child.dateOfBirth" onchange="dateRange();" maxLength="10"/>
            <%--<s:label value="*" cssStyle="color:red;font-size:15pt"/>--%>
    </td>
    <td colspan="4" style="border-left:none;">
        <div id="belatedError" style="color:red; font-size:11pt"/>
    </td>
</tr>
<tr>
    <td rowspan="6"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) උපන්
        ස්ථානය <s:label value="*" cssStyle="color:red;font-size:14pt;"/>
        <br>பிறந்த இடம்
        <br> Place of Birth</label></td>
    <td><label>දිස්ත්‍රික්කය மாவட்டம் District</label></td>
    <td colspan="6" class="table_reg_cell_01">
        <s:select id="districtId" name="birthDistrictId" list="districtList" value="birthDistrictId"
                  cssStyle="width:98.5%; width:240px;"/>
    </td>
</tr>
<tr>
    <td><label>ප්‍රාදේශීය ලේකමි කොටිඨාශය/<br/>பிரதேச செயளாளா் பிரிவு/ <br/>Divisional Secretariat</label></td>
    <td colspan="6" class="table_reg_cell_01" id="table_reg_cell_01">
        <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                  cssStyle="float:left;  width:240px;"/>
    </td>
</tr>
<tr>
    <td><label>
        ලියාපදිංචි කිරීමේ කොට්ඨාශය /<br/>
        பதிவுப் பிரிவு /<br/>
        Registration Division</label>
    </td>
    <td colspan="6">
        <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}" list="bdDivisionList"
                  cssStyle="float:left;  width:240px; margin:2px 5px;"/>
    </td>
</tr>
<tr>
    <td><label>සිංහල හෝ දෙමළ භාෂාවෙන් <br>சிங்களம்அல்லது தமிழ் மொழியில்<br>In Sinhala or Tamil</label></td>
    <td colspan="6"><s:textfield name="child.placeOfBirth" id="placeOfBirth" cssStyle="width:95%;"
                                 maxLength="255"/>
            <%--<s:label value="*" cssStyle="color:red;font-size:15pt"/>          --%>
    </td>
</tr>
<tr>
    <td><label>ඉංග්‍රීසි භාෂාවෙන් <br>ஆங்கில மொழியில்<br>In English</label></td>
    <td colspan="6">
        <s:textfield name="child.placeOfBirthEnglish" id="placeOfBirthEnglish"
                     cssStyle="width:95%; text-transform:uppercase;"/>
        <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;" id="place">
    </td>
</tr>
<tr>
    <td colspan="3"><label>උපත සිදුවුයේ රෝහලකද? <br>பிறப்பு நிகழ்ந்தது வைத்திய சாலையிலா?<br>Did the birth occur at a
        hospital?</label></td>
    <td colspan="1"><label>ඔව් / ஆம் / Yes </label></td>
    <td align="center"><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'true':''}"
                                value="true"/></td>
    <td><label>නැත / இல்லை / No</label></td>
    <td align="center"><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'false':''}"/></td>
</tr>
<s:if test="birthType.ordinal() != 0">
    <tr>
        <td class="font-9"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
            <br>பெயா் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
            <br>Name in any of the official languages (Sinhala / Tamil)</label>
        </td>
        <td colspan="7">
            <s:textarea name="child.childFullNameOfficialLang" id="childFullNameOfficialLang"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td class="font-9"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            නම ඉංග්‍රීසි භාෂාවෙන් <br>பெயா் ஆங்கில மொழியில்<br>Name in English
        </label></td>
        <td colspan="7">
            <s:textarea name="child.childFullNameEnglish" id="childFullNameEnglish"
                        cssStyle="width:98.2%;text-transform: uppercase;"/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;"
                 id="childName">
        </td>
    </tr>
</s:if>
<tr>
    <td class="font-9" colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) උප්පැන්න
        සහතිකය නිකුත් කල යුතු භාෂාව <br>பிறப்பு அத்தாட்சி ….. <br>Preferred Language for Birth Certificate </label>
    </td>
    <td colspan="6">
        <s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'தமிழ்'}" name="register.preferredLanguage"
                  cssStyle="width:190px; margin-left:5px;"/>
    </td>
</tr>
<tr>
    <td class="font-9">
        <label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)ස්ත්‍රී
            පුරුෂ භාවය<br>பால் <br>Gender
            of the child</label>
    </td>
    <td colspan="3">
        <s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="child.childGender" cssStyle="width:190px; margin-left:5px;" id="genderList"/>
    </td>
    <s:if test="birthType.ordinal() != 0">
        <s:if test="birthType.ordinal() == 1 || birthType.ordinal() == 3">
            <td colspan="2">
                <label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) උපත්
                    බර<br>பிறப்பு நிறை<br>Birth
                    Weight (kg)</label>
            </td>
        </s:if>
        <s:if test="birthType.ordinal() == 2">
            <td colspan="2">
                <label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) උපත්
                    බර (දන්නේ නමි)<br>பிறப்பு
                    நிறை<br>Birth Weight, if known (kg)</label>
            </td>
        </s:if>
        <td colspan="2"><s:textfield name="child.childBirthWeight" id="childBirthWeight"
                                     cssStyle="width:95%;" maxLength="5"/></td>
    </s:if>
    <s:elseif test="birthType.ordinal() == 0">
        <td colspan="2">
            <label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                දරුවා මැරී උපදින විට ගර්භයට සති කීයක් වී තිබුනේද යන්න
                <br>பிள்ளை இறந்து பிறந்த பொழுது கா்ப்பத்திற்கு எத்தனை கிழமை
                <br>Number of weeks pregnant at the time of still-birth</label>
        </td>
        <td colspan="2"><s:textfield name="child.weeksPregnant" id="weeksPregnant" cssStyle="width:95%;"
                                     maxLength="2"/></td>
    </s:elseif>
</tr>
<tr>
    <td class="font-9">
        <label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            <s:if test="birthType.ordinal() != 0">
                සජිවි උපත් අනුපිළි‍‍වල අනුව කීවෙනි ළමයා
                ද?
                <br>பிறப்பு ஒழுங்கு
                <br>According to Live Birth Order, rank of the child?
            </s:if>
            <s:else>
                උපත් අනුපිළිවල අනුව කීවෙනි උපතද?
                <br>பிறப்பு ஒழுங்கு
                <br>Birth rank
            </s:else>
        </label>
    </td>
    <td colspan="3" class="font-9"><s:textfield name="child.childRank" id="childRank" maxLength="2"/></td>
    <td colspan="2" class="font-9">
        <label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)නිවුන්
            දරු උපතක් නම්, දරුවන් ගණන
            <br>பல்வகைத்தன்மை (இரட்டையர்கள் எனின்),
            <br> பிள்னளகளின் எண்ணிக்கை
            <br>If multiple births, number of children</label>
    </td>
    <td colspan="2"><s:textfield name="child.numberOfChildrenBorn" id="numberOfChildrenBorn"
                                 cssStyle="width:95%;" maxLength="2"/></td>
</tr>
</tbody>
</table>

<s:hidden name="pageNo" value="1"/>
<s:hidden name="rowNumber" value="%{row}"/>
<s:hidden name="counter" value="%{i}"/>

<div class="skip-validation">
    <s:checkbox name="skipjavaScript" id="skipjs" value="false">
        <s:label value="%{getText('skipvalidation.label')}"/>
    </s:checkbox>
</div>
<div class="form-submit">
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>

<s:hidden id="birthTypeId" value="%{birthType.ordinal()}"/>
<s:hidden id="error1" value="%{getText('p1.SerialNum.error.value')}"/>
<s:hidden id="error2" value="%{getText('p1.childName.error.value')}"/>
<s:hidden id="error3" value="%{getText('p1.NameEnglish.error.value')}"/>
<s:hidden id="error4" value="%{getText('p1.Weigth.error.value')}"/>
<s:hidden id="error5" value="%{getText('p1.Rank.error.value')}"/>
<s:hidden id="error6" value="%{getText('p1.dob.after.submit.value')}"/>
<s:hidden id="error7" value="%{getText('p1.submit.after.90.value')}"/>
<s:hidden id="error8" value="%{getText('p1.submit.after.365.value')}"/>
<s:hidden id="error9" value="%{getText('p1.submitDate.error.value')}"/>
<s:hidden id="error10" value="%{getText('p1.dob.error.value')}"/>
<s:hidden id="error11" value="%{getText('p1.placeOfBirth.error.value')}"/>
<s:hidden id="error12" value="%{getText('p1.numbeOfChildren')}"/>
<s:hidden id="error14" value="%{getText('p1.numbeOfWeeksPregnant')}"/>

<s:hidden id="error13" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error16" value="%{getText('p1.child.rank')}"/>

<%--hidden fields for input fields--%> .
<s:hidden id="error20" value="%{getText('p1.serial.text')}"/>
<s:hidden id="error21" value="%{getText('p1.dob.text')}"/>
<s:hidden id="error22" value="%{getText('p1.dateOfRegistration.text')}"/>
<s:hidden id="error23" value="%{getText('p1.birthWeight.text')}"/>
<s:hidden id="error24" value="%{getText('p1.child.rank.text')}"/>
<s:hidden id="error25" value="%{getText('p1.numOfChildren.text')}"/>
<s:hidden id="error26" value="%{getText('p1.weeksPregnant.text')}"/>
<s:hidden id="dob" value="%{getText('p1.dob')}"/>
<s:hidden id="submitDate" value="%{getText('p1.submit.date')}"/>
<s:hidden id="numChildGTOne" value="%{getText('p1.num.child.gt.one')}"/>
<s:hidden id="childRankGTZero" value="%{getText('p1.child.rank.gt.zero')}"/>

</div>
