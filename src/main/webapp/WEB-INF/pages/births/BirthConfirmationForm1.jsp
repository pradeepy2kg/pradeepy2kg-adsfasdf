<%@ page import="lk.rgd.crs.api.domain.BirthDeclaration" %>
<%@ page import="lk.rgd.common.api.domain.District" %>
<%@ page import="java.util.Map" %>
<%@ page import="lk.rgd.crs.web.WebConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/popreg/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="../js/validate.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>
<script>

    function initPage() {
        disableOk(true);
    }

    function disableNext(mode) {
        document.getElementById('next').disabled = mode;
    }

    function disableOk(mode) {
        document.getElementById('searchOk').disabled = mode;
    }

    function okCheck() {
        var ok = document.getElementById('skipChangesCBox');
        if (ok.checked) {
            disableNext(true)
            disableOk(false)
        } else {
            disableOk(true)
            disableNext(false)
        }
    }

    $(function() {
        $("#submitDatePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });
    $(function() {
        $('img#place').bind('click', function(evt) {
            var id = $("input#placeOfBirth").attr("value");
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
            SOAPClient.SendRequest(sr, processResponse); //Send request to server and assign a callback
        });

        function processResponse(respObj) {
            //respObj is a JSON equivalent of SOAP Response XML (all namespaces are dropped)
            $("input#placeOfBirthEnglish").val(respObj.Body[0].transliterateResponse[0].
            return[0].Text
        )
            ;
        }
    })
    var errormsg = "";
    function validate()
    {
        var errormsg = "";
        var element;
        var returnval;

        /*date related validations*/
        var submit = new Date(document.getElementById("submitDatePicker").value);
        if (!(submit.getTime())) {
            errormsg = errormsg + "\n" + document.getElementById('p1error3').value;
            flag = true;
        }
        element = document.getElementById('SerialNo');
        if (element.value == "") {
            errormsg = errormsg + "\n" + document.getElementById('p1error1').value;
        }
        element = document.getElementById('placeOfBirth');
        if (element.value == "") {
            errormsg = errormsg + "\n" + document.getElementById('p1error2').value;
            flag = true;
        }


        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        return returnval;
    }

    function validateSkipChanges() {
        var noSerialEntered = document.getElementById('p1error1').value;
        var notChecked = document.getElementById('p1errorckbx').value;
        var serial = document.getElementById('SerialNo');
        var skipChanges = document.getElementById('skipChangesCBox');
        if (serial.value == 0) {
            alert(noSerialEntered);
            return false;
        }
        if (!skipChanges.checked) {
            alert(notChecked);
            return false;
        }
    }

    function validateSerialNum() {
        var domObject;
        var returnval = true;
        var serial = document.getElementById('SerialNo');
        isNumeric(serial.value, 'error5');
        isEmpty(serial, "p1error1") ;
        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }
</script>

<div id="birth-confirmation-form-outer">
<table class="table_con_header_01" width="100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="300px"></td>
        <td align="center"><img src="<s:url value="/images/official-logo.png" />" alt=""/><br> <label>
            ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA<br><br>
            දෙමව්පියන් / භාරකරු විසින් උපත තහවුරු කිරීම
            ﻿﻿ <br>பெற்றோர் அல்லது பாதுகாப்பாளா் மூலம் பிறப்பை உறுதிப்படுத்தல்
            <br>Confirmation of Birth by Parents / Guardian
        </label></td>
        <td>
            <form action="eprBirthConfirmationInit.do" method="post" onsubmit="javascript:return validateSerialNum()">
                <table style=" border:1px solid #000000; width:300px">
                    <tr><s:actionerror/></tr>
                    <tr>
                        <td><label><span class="font-8">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</span></label>
                        </td>
                        <td><s:textfield name="bdId" id="SerialNo"/></td>
                    </tr>
                </table>
                <table style=" width:300px">
                    <tr>

                    <tr>
                        <td width="200px"></td>
                        <td align="right" class="button"><s:submit name="search"
                                                                   value="%{getText('searchButton.label')}"
                                                                   cssStyle="margin-right:10px;"/></td>
                    </tr>
                </table>
            </form>
            <form action="eprBirthConfirmationSkipChanges.do" onsubmit="javascript:return validateSkipChanges()">
                <table style=" border:1px solid #000000; width:300px">
                    <tr>
                        <td><s:label value="%{getText('noConfirmationChanges.label')}"/></td>
                        <td><s:checkbox name="skipConfirmationChages" id="skipChangesCBox"
                                        onclick="okCheck()"/></td>
                    </tr>
                    <tr><s:hidden name="pageNo" value="2"/>
                        <s:hidden name="bdId" value="%{#request.bdId}"/>
                        <td width="170px"></td>
                        <td align="left" class="button"><s:submit id="searchOk" name="searchOk"
                                                                  value="%{getText('skip.label')}"
                                                                  cssStyle="margin-right:8px;"/></td>
                    </tr>
                </table>
            </form>

        </td>
    </tr>
    </tbody>
</table>


<s:form action="eprBirthConfirmation" name="birthConfirmationForm1" id="birth-confirmation-form-1" method="POST"
        onsubmit="javascript:return validate()">


<table class="table_con_page_01" width="100%" cellpadding="0" cellspacing="0" style="margin-bottom:20px;">
    <caption></caption>
    <col align="center"/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td class="cell_01">1</td>
        <td>සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ අදාල “උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර” ප්‍රකාශනයේ අනුක්‍රමික අංකය
            හා දිනය
            <br>பிறப்பை பதிவு செய்வதற்கான விபரம்" எனும் படிவத்தின் தொடா் இலக்கமும் திகதியும்
            <br>Serial Number and the Date of the ‘Particulars for Registration of a Birth’ form
        </td>
        <td><s:textfield cssClass="disable" disabled="true"
                         name="#session.birthConfirmation_db.register.bdfSerialNo"/>
            <s:textfield cssClass="disable" disabled="true"
                         name="#session.birthConfirmation_db.register.dateOfRegistration"/></td>
    </tr>
    <tr>
        <td>2</td>
        <td><label>
            යම් වෙනසක් සිදු කලයුතු නම් රෙජිස්ට්‍රාර් ජනරාල් වෙත දැනුම් දිය යුතු අවසන් දිනය <br>
            மாற்றங்கள் பதிவாளர் அதிகாரியின் அலுவலகத்தை அடைய வேண்டிய இறுதித் திகதி
            <br>Last date by which changes should be received by the registrar generals office.
        </label></td>
        <td><s:textfield cssClass="disable" disabled="true" value=""/></td>
    </tr>
    </tbody>
</table>


<table class="table_con_page_01" width="100%" cellpadding="0" cellspacing="0">
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
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
        <td colspan="14" style="text-align:center;font-size:12pt"> සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ ඇතුළත් විස්තර
            <br>சிவில் பதிவு அமைப்பில் உள்ளடக்கப்பட்டுள்ள விபரம்
            <br>Information included in Civil Registration System
        </td>
    </tr>
    <tr>
        <td colspan="2"><label>විස්තර <br>விபரங்கள் <br>Particulars </label></td>
        <td colspan="6" width="350px"><label>සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ දැනට අඩංගු විස්තර <br>சிவில் பதிவு
            அமைப்பில்
            உள்ளடக்கப்பட்டுள்ள
            விபரம<br>Information included in Civil Registration System </label></td>
        <td class="cell_02" colspan="6"><label>
            වෙනස් විය යුතු විස්තර අතුලත් කරන්න.
            <br>புதியசிவில் பதிவ..ண்டிய விப...
            <br>Insert new details or modify existing details</label></td>
    </tr>
    <tr>
        <td class="cell_01">3</td>
        <td class="cell_04"><label>උපන් දිනය<br>பிறந்த திகதி<br>Date of birth</label></td>
        <td class="cell_03"><label>අව්රුද්ද <br>இயர் <br>Year</label></td>
        <td class="cell_03"><s:textfield value="%{#session.birthConfirmation_db.child.dateOfBirth.year+1900}"
                                         cssClass="disable" disabled="true"
                                         size="4"/></td>
        <td class="cell_03"><label>මාසය<br>மாதம்<br>Month</label></td>
        <td class="cell_03"><s:textfield value="%{#session.birthConfirmation_db.child.dateOfBirth.month+1}"
                                         cssClass="disable" disabled="true"
                                         size="4"/></td>
        <td class="cell_03"><label>දිනය<br>திகதி<br>Day</label></td>
        <td class="cell_03"><s:textfield value="%{#session.birthConfirmation_db.child.dateOfBirth.date}"
                                         cssClass="disable" disabled="true"
                                         size="4"/></td>
        <td colspan="6" width="350px"><s:textfield name="child.dateOfBirth" id="submitDatePicker"/>
        </td>
    </tr>
    <tr>
        <td>4</td>
        <td><label>ස්ත්‍රී පුරුෂ භාවය <br>பால்பால்<br>Gender</label></td>
        <td colspan="6"><s:if test="#session.birthConfirmation_db.child.childGender == 0">
            <s:textfield value="%{getText('male.label')}" cssClass="disable" disabled="true"/>
        </s:if>
            <s:elseif test="#session.birthConfirmation_db.child.childGender == 1">
                <s:textfield value="%{getText('female.label')}" cssClass="disable" disabled="true"/>
            </s:elseif>
            <s:elseif test="#session.birthConfirmation_db.child.childGender == 2">
                <s:textfield value="%{getText('unknown.label')}" cssClass="disable" disabled="true"/>
            </s:elseif></td>
        <td colspan="6"><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="child.childGender"/></td>
    </tr>
    <tr>
        <td>5</td>
        <td colspan="13"><label>උපන් ස්ථානය / பிறந்தபிறந்த இடம் / Place of birth</label></td>
    </tr>
    <tr>
        <td rowspan="5"></td>
        <td><label>දිස්ත්‍රික්කය <br>மாவட்டம் <br>District</label></td>
        <td colspan="6"><s:textfield value="%{getDistrictList().get(birthDistrictId)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="6"><s:select list="districtList" name="birthDistrictId"/></td>
    </tr>
    <tr>
        <td><label>D.S.කොට්ඨාශය<br>பிரிவு <br>D.S. Division</label></td>
        <td colspan="6"><s:textfield value="%{getDsDivisionList().get(dsDivisionId)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="6"><s:select list="dsDivisionList" name="dsDivisionId"/></td>
    </tr>
    <tr>
        <td><label>කොට්ඨාශය<br>பிரிவு <br>Registration Division</label></td>
        <td colspan="6"><s:textfield value="%{getBdDivisionList().get(birthDivisionId)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="6"><s:select name="birthDivisionId" list="bdDivisionList"/></td>
    </tr>
    <tr>
        <td><label>ස්ථානය <br>பிறந்த இடம் <br>Place</label></td>
        <td colspan="6"><s:textarea name="#session.birthConfirmation_db.child.placeOfBirth" cssClass="disable"
                                    disabled="true" cols="38"/></td>
        <td colspan="6"><s:textfield name="child.placeOfBirth" size="35" id="placeOfBirth"/></td>
    </tr>
    <tr>
        <td><label>ඉංග්‍රීසි භාෂාවෙන් <br>இங்கிலீஷ் <br>In English</label></td>
        <td colspan="6"><s:textarea name="#session.birthConfirmation_db.child.placeOfBirthEnglish"
                                    cssClass="disable"
                                    disabled="true" cols="38"/></td>
        <td colspan="6">
            <s:textfield name="child.placeOfBirthEnglish" size="35" id="placeOfBirthEnglish"/>
            <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;" id="place">
        </td>
    </tr>
    <tr>
        <td>6</td>
        <td><label>පියාගේ අනන්‍යතා අංකය <br>தந்நையின் தனிநபர் அடையாள எண்<br>Father's PIN</label></td>
        <td colspan="6"><s:textfield name="#session.birthConfirmation_db.parent.fatherNICorPIN" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="6"><s:textfield name="parent.fatherNICorPIN" size="35"/></td>
    </tr>
    <tr>
        <td>7</td>
        <td><label>පියාගේ ජාතිය <br>தந்நையின் இனம்<br>Father's Race</label></td>
        <td colspan="6">
            <s:textfield value="%{getRaceList().get(fatherRace)}" cssClass="disable" disabled="true"/>
        </td>
        <td colspan="6">
            <s:select list="raceList" name="fatherRace" headerKey="0" headerValue="%{getText('select_race.label')}"/>
        </td>
    </tr>
    <tr>

    <tr>
        <td>8</td>
        <td><label>ම‌වගේ අනන්‍යතා අංකය <br>தாயின் தனிநபர் அடையாள எண<br>Mother's PIN</label></td>
        <td colspan="6"><s:textfield name="#session.birthConfirmation_db.parent.motherNICorPIN" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="6"><s:textfield name="parent.motherNICorPIN" size="35"/></td>
    </tr>
    <tr>
        <td>9</td>
        <td><label>මවගේ ජාතිය <br>தாயின் இனம்<br>Mother's Race</label></td>
        <td colspan="6"><s:textfield value="%{getRaceList().get(motherRace)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="6">
            <s:select list="raceList" name="motherRace" headerKey="0" headerValue="%{getText('select_race.label')}"/>
        </td>
    </tr>
    <tr>
        <td>10</td>
        <td><label>මව්පියන් විවාහකද? <br>பெற்றார் விவாகஞ் செய்தவர்களா? <br>Were Parents Married?</label></td>
        <td colspan="6"><s:textfield name="#session.birthConfirmation_db.marriage.parentsMarried" cssClass="disable"
                                     disabled="true"
                                     value="%{getText('married.status.'+marriage.parentsMarried)}"/></td>
        <td><label id="yes" class="label">ඔව්<br>*in tamil<br>Yes</label></td>
        <td><s:radio name="marriage.parentsMarried" id="parentsMarried" list="#@java.util.HashMap@{'1':''}"
                     value="1"/></td>
        <td><label class="label">නැත<br>*in tamil<br>No</label></td>
        <td><s:radio name="marriage.parentsMarried" id="parentsMarried" list="#@java.util.HashMap@{'0':''}"/></td>
        <td><label class="label">නැත - පසුව විවාහවී ඇත<br>*in tamil<br>No but since married</label></td>
        <td><s:radio name="marriage.parentsMarried" id="parentsMarried" list="#@java.util.HashMap@{'2':''}"/></td>
    </tr>
    </tbody>
</table>

<s:hidden name="pageNo" value="1"/>

<s:hidden id="p1error1" value="%{getText('cp1.error.serialNum.value')}"/>
<s:hidden id="p1error2" value="%{getText('cp1.placeOfBirth.error.value')}"/>
<s:hidden id="p1error3" value="%{getText('cp1.date.error.value')}"/>
<s:hidden id="p1error4" value="%{getText('cp1.parents.marriage.error.value')}"/>
<s:hidden id="p1errorckbx" value="%{getText('cp1.skipChanges.checked.error.value')}"/>
<s:hidden id="error5" value="%{getText('p1.serial.text')}"/>
<s:hidden id="error13" value="%{getText('p1.invalide.inputType')}"/>
<div class="form-submit">
    <s:submit value="%{getText('next.label')}" id="next"/>
</div>
</s:form>
</div>
<%-- Styling Completed --%>