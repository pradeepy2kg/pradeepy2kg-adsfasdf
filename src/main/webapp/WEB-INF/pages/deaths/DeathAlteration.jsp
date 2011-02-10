<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/timePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/transliteration.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script>
    //these inpute can not be null
    var errormsg = "";
    function validate() {
        var bothEmpty;
        var returnval = true;
        var domObject;
        domObject = document.getElementById("reciveDatePicker");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error1');
        } else {
            isDate(domObject.value, 'error0', 'error1');
        }

        if (document.getElementById('death-info-check').checked) {
            domObject = document.getElementById("deathDatePicker");
            if (isFieldEmpty(domObject)) {
                isEmpty(domObject, "", 'error23')
            }

            domObject = document.getElementById("placeOfDeath");
            if (isFieldEmpty(domObject)) {
                isEmpty(domObject, "", 'error24')
            }

            domObject = document.getElementById("placeOfBurial");
            if (isFieldEmpty(domObject)) {
                isEmpty(domObject, "", 'error2')
            }
        }


        domObject = document.getElementById("deathPerson_PINorNIC");
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error0', 'error3');
        }

        domObject = document.getElementById("passportNumber");
        if (!isFieldEmpty(domObject)) {
            validatePassportNo(domObject, 'error0', 'error4');
        }

        domObject = document.getElementById("deathAge");
        if (!isFieldEmpty(domObject)) {
            isNumeric(domObject.value, 'error0', 'error5');
        }

        domObject = document.getElementById("fatherPinNic");
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error0', 'error6');
        }

        domObject = document.getElementById("motherNIC");
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error0', 'error7');
        }

        validateRadioButtons(new Array(document.getElementsByName('deathAlteration.declarant.declarantType')[0].checked,
                document.getElementsByName('deathAlteration.declarant.declarantType')[1].checked,
                document.getElementsByName('deathAlteration.declarant.declarantType')[2].checked,
                document.getElementsByName('deathAlteration.declarant.declarantType')[3].checked,
                document.getElementsByName('deathAlteration.declarant.declarantType')[4].checked,
                document.getElementsByName('deathAlteration.declarant.declarantType')[5].checked), "declerent_type");

        domObject = document.getElementById("declarant_pinOrNic");
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error0', 'error8');
        }
        domObject = document.getElementById("declarantName");
        if (isFieldEmpty(domObject)) {
            printMessage('text_must_fill', "declerent_name")
        }
        domObject = document.getElementById("declarantAddress");
        if (isFieldEmpty(domObject)) {
            printMessage('text_must_fill', "declerent_address")
        }
        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";

        return returnval;
    }

    function validateRadioButtons(array, errorText) {
        var atleastOneSelect = false;
        for (var i = 0; i < array.length; i++) {
            if (array[i] == true) {
                atleastOneSelect = true;
            }
        }
        if (!atleastOneSelect) {
            printMessage("text_must_select", errorText)
        }
    }
</script>

<script type="text/javascript">
    var act;
    var informPerson;
    function setInformPerson(nICorPIN, name, address, tp, email) {
        var informantName = document.getElementById("declarant_pinOrNic").value = nICorPIN;
        var informantNICorPIN = document.getElementById("declarantName").value = name;
        var informantAddress = document.getElementById("declarantAddress").value = address;
        var informantTP = document.getElementById("declarant_tp").value = tp;
        var informantEmail = document.getElementById("declarant_email").value = email;
    }


    $(function() {
        $("#deathDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    $(function() {
        $("#reciveDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });

    //time picker
    $(function() {
        $("#deathTimePicker").cantipi({size:140, roundto: 5});
    });


    /*show hide*/
    $(function() {
        $('#death-info-min').click(function() {
            minimize("death-info");
        });
        $('#death-info-max').click(function() {
            maximize("death-info");
        });


        $('#death-person-info-min').click(function() {
            minimize("death-person-info");
        });
        $('#death-person-info-max').click(function() {
            maximize("death-person-info");
        });
        $('#death-info-check').click(function() {
            document.getElementById("death-info-check").disabled = true;
            document.getElementById('death').value = true;
            var fieldIds = new Array('deathDatePicker', 'deathTimePicker', 'placeOfDeath', 'placeOfDeathInEnglish', 'cause_of_death',
                    'ICD_code', 'placeOfBurial', 'act53true', 'act52false', 'cause_of_death_yesfalse', 'cause_of_death_notrue');
            enableFields(fieldIds);
        });

        $('#death-person-info-check').click(function() {
            document.getElementById("death-person-info-check").disabled = true;
            document.getElementById('deathPerson').value = true;
            var fieldIds = new Array('deathPerson_PINorNIC', 'deathPersonCountryList', 'passportNumber', 'deathAge', 'deathPersonGender'
                    , 'deathPersonRaceList', 'fatherPinNic', 'nameEnglish', 'address', 'nameOfficialLang', 'motherNIC', 'motherName', 'fatherName');
            enableFields(fieldIds);
        });
    });


    function enableFields(fieldIds) {
        for (var i = 0; i < fieldIds.length; i++) {
            document.getElementById(fieldIds[i]).disabled = false;
        }
    }

    function minimize(id) {
        document.getElementById(id).style.display = 'none';
        document.getElementById(id + "-min").style.display = 'none';
        document.getElementById(id + "-max").style.display = 'block';
        document.getElementById(id + "-check").style.display = 'none';
        document.getElementById(id + "-check-lable").style.display = 'none';

    }

    function maximize(id, click) {
        document.getElementById(id).style.display = 'block';
        document.getElementById(id + "-max").style.display = 'none';
        document.getElementById(id + "-min").style.display = 'block';
        document.getElementById(id + "-check").style.display = 'block';
        document.getElementById(id + "-check-lable").style.display = 'block';

    }


    function initPage() {
        var idNames;                                                //    cause_of_death_yesfalse ,cause_of_death_notrue , act52false ,act53true
        var checkIdNames;
        var fieldIds;
        idNames = new Array('death-info', 'death-person-info');
        checkIdNames = new Array('death-person-info-check', 'death-info-check');
        fieldIds = new Array('deathDatePicker', 'cause_of_death_yesfalse', 'motherNIC', 'motherName', 'fatherName',
                'fatherPinNic', 'nameEnglish', 'address', 'nameOfficialLang', 'deathAge', 'deathPersonGender', 'deathPersonRaceList'
                , 'deathPerson_PINorNIC', 'deathPersonCountryList', 'passportNumber', 'placeOfBurial', 'cause_of_death', 'ICD_code'
                , 'cause_of_death_notrue', 'placeOfDeath', 'placeOfDeathInEnglish', 'deathTimePicker', 'act53true', 'act52false'
                );

        //set serial number


        for (var i = 0; i < idNames.length; i++) {
            document.getElementById(idNames[i]).style.display = 'none';
            document.getElementById(idNames[i] + "-min").style.display = 'none';
        }
        for (var i = 0; i < checkIdNames.length; i++) {
            document.getElementById(checkIdNames[i]).style.display = 'none';
            document.getElementById(checkIdNames[i] + "-lable").style.display = 'none';
        }

        for (var i = 0; i < fieldIds.length; i++) {
            document.getElementById(fieldIds[i]).disabled = true;
        }
    }
</script>
<div id="death-alteration-outer">
<s:if test="%{editMode==true}">
    <s:url action="eprEditDeathAlteration" id="urlEditOrAdd" namespace="../alteration"/>
    <%--TODO change other pages accourding to this S URL format --%>
</s:if>
<s:else>
    <s:url action="eprCaptureDeathAlteration" id="urlEditOrAdd" namespace="../alteration"/>
</s:else>
<s:form method="post" action="%{urlEditOrAdd}" onsubmit="javascript:return validate()">
<table class="death-alteration-table-style01" style="width:1030px;" cellpadding="2px">
    <tr>
        <td width="30%"></td>
        <td width="35%" style="text-align:center;"></td>
        <td width="35%">
            <table class="birth-alteration-table-style02" cellspacing="0" style="float:right;width:100%">
                <tr>
                    <td colspan="2" style="text-align:center;">කාර්යාල ප්‍රයෝජනය සඳහා පමණි /<br>
                        அலுவலக பாவனைக்காக மட்டும் / <br>
                        For office use only
                    </td>
                </tr>
                <s:if test="alterationSerialNo>0">
                    <tr>
                        <td width="40%"><s:label value="අනුක්‍රමික අංකය"/><br>
                            <s:label value=" தொடர் இலக்கம் "/><br>
                            <s:label value=" Serial Number"/>
                        </td>

                        <td width="60%"><s:textfield id="bdfSerialNo" name="alterationSerialNo" maxLength="10"
                                                     value="%{alterationSerialNo}" readonly="true"/></td>

                    </tr>
                </s:if>
                <tr>
                    <s:fielderror name="duplicateSerialNumberError" cssStyle="color:red;font-size:9pt;"/>
                </tr>
                <tr>
                    <td><s:label value="භාරගත් දිනය"/><br>
                        <s:label value="பிறப்பைப் பதிவு திி"/> <br>
                        <s:label value="Date of Acceptance"/>
                    </td>
                    <td><s:textfield id="reciveDatePicker" name="deathAlteration.dateReceived" value="%{toDay}"/></td>
                <tr>
                    <td><s:label value="පනතේ වගන්තිය "/><br>
                        <s:label value="பிறப்பைப்"/> <br>
                        <s:label value="Section of the Act"/>
                    </td>
                    <td>
                        <s:select
                                list="#@java.util.HashMap@{'TYPE_52_1_A':'52(1) A ','TYPE_52_1_B':'52(1) B','TYPE_52_1_D':'52(1) D','TYPE_52_1_E':'52(1) E','TYPE_52_1_I':'52(1) I','TYPE_52_1_H':'52(1) H','TYPE_53':'53'}"
                                name="deathAlteration.type"
                                cssStyle="width:190px; margin-left:5px;" onchange="setAct(value)"/>
                    </td>

                </tr>
            </table>
        </td>
    </tr>
</table>

<table class="death-alteration-table-style01" style="width:1030px;">
    <tr>
        <td colspan="3" style="font-size:12pt;text-align:center;">
            <s:label
                    value="මරණ සහතිකයක දෝෂ නිවැරදි කිරීම (උප්පැන්න හා මරණ ලියාපදිංචි කිරීමේ පනතේ 52 (1) සහ 53 වගන්ති)"/>
            <br>
            <s:label value="தந்தை பற்றிய தகவல்"/> <br>
            <s:label
                    value="Correction of Errors of a Death Certificate (under the Births and Deaths Registration Act. Sections 52 (1) and 53)"/>
        </td>
    </tr>
    <tr>
        <td>
            <s:label
                    value="සැ.යු. හදිසි මරණ සහතිකයක වෙනසක් කිරීමට ඉල්ලුම් කල හැක්කේ මරණ පරීක්ෂක හට පමණකි. in Tamil. Note: An alteration on a certificate of death for a sudden death can be requested only by an inquirer into deaths"/>
        </td>
    </tr>
    <tr>
        <td></td>
    </tr>
    <tr>
        <td></td>
    </tr>
    <tr>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;border-top:2px">
            <s:label value="වෙනස් කලයුතු මරණ සහතිකය පිලිබඳ විස්තර"/> <br>
            <s:label value="பிள்ளை பற்றிய தகவல்"/> <br>
            <s:label value="Particulars of the Death Certificate to amend"/>
        </td>
    </tr>
</table>
<br>
<table class="death-alteration-table-style02" style=" margin-top:0px;width:100%;" cellpadding="0" cellpadding="2px"
       cellspacing="0">
    <caption></caption>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <tbody>
    <tr>
        <td colspan="2">(1)සහතිකයේ සඳහන් පුද්ගලයාගේ අනන්‍යතා අංකය <br>
            தனிநபர்அடையாள எண் <br>
            Person Identification Number (PIN) stated in the Certificate
        </td>
        <td>
            <s:property value="%{deathAlteration.deathPersonPin}"/>
        </td>
        <td>(2)සහතික පත්‍රයේ අංකය <br>
            சான்றிதழ் இல <br>
            Certificate Number
        </td>
        <td>
            <s:property value="deathAlteration.deathRegisterIDUkey"/>
        </td>
    </tr>
    <tr>
        <td>(3)දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td>
            <s:property value="district"/>
        </td>
        <td>(4)ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரிவு <br>
            Divisional Secretariat
        </td>
        <td colspan="2"><s:property value="dsDivision"/></td>
    </tr>
    <tr>
        <td>(5)ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
            பிரிவு <br>
            Registration Division
        </td>
        <td>
            <s:property value="deathDivision"/>
        </td>
        <td>(6)ලියාපදිංචි කිරීමේ අංකය <br>
            சான்றிதழ் இல <br>
            Registration Number
        </td>
        <td colspan="2">
            <s:property value="serialNumber"/>
        </td>
    </tr>
    </tbody>
</table>
<br>

<table class="death-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <caption/>
    <col width="300px"/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tr>
        <td style="border-right:none"></td>
        <td style="font-size:11pt;text-align:center;margin-top:20px;border-right:none">
            <s:label value="මරණය පිලිබඳ විස්තර"/> <br>
            <s:label value="பிள்ளை பற்றிய தகவல்"/> <br>
            <s:label value="Information about the Death"/>
        </td>
        <td style="width:20%;text-align:right;border-right:none">
            <div id="death-info-check-lable">
                <s:label value="%{getText('edit.lable')}"/></div>
        </td>
        <td style="border-right:none;width:3%">
            <s:checkbox id="death-info-check" name="#" cssStyle="float:right;"/>
        </td>
        <td style="width:2%;border-left:none">
            <div class="birth-alteration-minimize-icon" id="death-info-min">[-]</div>
            <div class="birth-alteration-maximize-icon" id="death-info-max">[+]</div>
        </td>
    </tr>
</table>
<br>

<div id="death-info">
    <table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;"
           cellpadding="2px">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr style="border-bottom:2px">
            <td>
                (10)හදිසි මරණයක්ද ? <br>
                திடீர் மரணமா?<br>
                Sudden death?
            </td>
            <td colspan="2">
                ඔව් (53 වගන්තිය) <br>
                ஆம்( 53 ஆம் பிரிவு)<br>
                Yes (Section 53)
            </td>
            <td align="center">
                <s:radio name="deathAlteration.deathInfo.suddenDeath"
                         list="#@java.util.HashMap@{'true':''}"
                         id="act53" onclick="validateAct(value)"/>
            </td>
            <td>
                නැත (52 (1) වගන්තිය) <br>
                இல்லை( 52(1) ஆம் பிரிவு)<br>
                No (Section 52 (1))
            </td>
            <td align="center">
                <s:radio name="deathAlteration.deathInfo.suddenDeath"
                         list="#@java.util.HashMap@{'false':''}"
                         id="act52" onclick="validateAct(value)"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" style="border-top:2px">
                (11)මරණය සිදු වූ දිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
                பிறந்த திகதி <br>
                Date of Death
            </td>
            <td colspan="2" align="center">
                <s:textfield name="deathAlteration.deathInfo.dateOfDeath" id="deathDatePicker"
                             value="%{deathAlteration.deathInfo.dateOfDeath}"/>
            </td>
            <td>
                වෙලාව <br>
                நேரம்<br>
                Time
            </td>
            <td align="center">
                <s:textfield name="deathAlteration.deathInfo.timeOfDeath" id="deathTimePicker"
                             value="%{deathAlteration.deathInfo.timeOfDeath}"/>
            </td>
        </tr>
        <tr>
            <td rowspan="2">
                (12)මරණය සිදු වූ ස්ථානය <br>
                பிறந்த இடம் <br>
                Place of Death
            </td>
            <td colspan="2">
                සිංහල හෝ දෙමළ භාෂාවෙන් <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
                சிங்களம் தமிழ் <br>
                In Sinhala or Tamil
            </td>
            <td colspan="3">
                <s:textfield name="deathAlteration.deathInfo.placeOfDeath" cssStyle="width:99%;" id="placeOfDeath"
                             value="%{deathAlteration.deathInfo.placeOfDeath}"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                ඉංග්‍රීසි භාෂාවෙන් <br>
                ஆங்கில மொழியில்<br>
                In English
            </td>
            <td colspan="3">
                <s:textfield name="deathAlteration.deathInfo.placeOfDeathInEnglish" id="placeOfDeathInEnglish"
                             cssStyle="width:99%;" value="%{deathAlteration.deathInfo.placeOfDeathInEnglish}"/>
                <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px 0;"
                     id="place" onclick="transliterateTextField('placeOfDeath', 'placeOfDeathInEnglish')">
            </td>
        </tr>
        <tr>
            <td>(13)මරණයට හේතුව තහවුරුද? <br>
                இறப்பிற்கான காரணம் உறுதியானதா?<br>
                Cause of death established?
            </td>
            <td colspan="2">
                නැත / இல்வல / No
            </td>
            <td align="center">
                <s:radio name="deathAlteration.deathInfo.causeOfDeathEstablished"
                         list="#@java.util.HashMap@{'false':''}"
                         id="cause_of_death_yes" value="%{deathAlteration.deathInfo.causeOfDeathEstablished}"/>
            </td>
            <td>ඔව් / ஆம் /Yes</td>
            <td align="center">
                <s:radio name="deathAlteration.deathInfo.causeOfDeathEstablished"
                         list="#@java.util.HashMap@{'true':''}"
                         id="cause_of_death_no" value="%{deathAlteration.deathInfo.causeOfDeathEstablished}"/>
            </td>
        </tr>
        <tr>
            <td>(14)මරණයට හේතුව <br>
                இறப்பிற்கான காரணம்<br>
                Cause of death
            </td>
            <td colspan="3">
                <s:textarea name="deathAlteration.deathInfo.causeOfDeath"
                            value="%{deathAlteration.deathInfo.causeOfDeath}"
                            cssStyle="width:420px;" id="cause_of_death"/>
            </td>
            <td>
                (15)හේතුවේ ICD කේත අංකය <br>
                காரணத்திற்கான ICD குறியீட்டு இலக்கம்<br>
                ICD Code of cause
            </td>
            <td>
                <s:textfield name="deathAlteration.deathInfo.icdCodeOfCause"
                             value="%{deathAlteration.deathInfo.icdCodeOfCause}"
                             cssStyle="width:225px;" id="ICD_code"/>
            </td>
        </tr>
        <tr>
            <td>(16)ආදාහන හෝ භූමදාන ස්ථානය<s:label value="*" cssStyle="color:red;font-size:10pt"/> <br>
                அடக்கம் செய்த அல்லது தகனஞ் செய்த இடம் <br>
                Place of burial or cremation
            </td>
            <td colspan="5">
                <s:textarea name="deathAlteration.deathInfo.placeOfBurial"
                            value="%{deathAlteration.deathInfo.placeOfBurial}"
                            id="placeOfBurial" cssStyle="width:99%;"/>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>

<table class="death-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <caption/>
    <col width="300px"/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tr>
        <td style="border-right:none"></td>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;border-right:none">
            <s:label value="මරණයට පත් වූ පුද්ගලයාගේ විස්තර"/> <br>
            <s:label value="பிள்ளை பற்றிய தகவல்"/> <br>
            <s:label value="Information about the person Departed"/>
        </td>
        <td style="width:20%;text-align:right;border-right:none">
            <div id="death-person-info-check-lable">
                <s:label value="%{getText('edit.lable')}"/></div>
        </td>
        <td style="border-right:none;width:3%">
            <s:checkbox id="death-person-info-check" name="" cssStyle="float:right;"/>
        </td>
        <td style="width:2%">
            <div class="birth-alteration-minimize-icon" id="death-person-info-min">[-]</div>
            <div class="birth-alteration-maximize-icon" id="death-person-info-max">[+]</div>
        </td>
    </tr>
</table>
<br>

<div id="death-person-info">
    <table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;"
           cellpadding="2px">
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
            <td rowspan="2">
                (17)අනන්‍යතා අංකය <br>
                தனிநபர் அடையாள எண் <br>
                Identification Number
            </td>
            <td colspan="3" rowspan="2">
                <s:textfield name="deathAlteration.deathPerson.deathPersonPINorNIC" id="deathPerson_PINorNIC"
                             cssStyle="float:left;" value="%{deathAlteration.deathPerson.deathPersonPINorNIC}"/>
                <img src="<s:url value="/images/search-father.png" />"
                     style="vertical-align:middle; margin-left:20px;" id="death_person_lookup">
            </td>

            <td rowspan="2">
                (18)විදේශිකය‍කු නම් <br>
                வெளிநாட்டவர் <br>
                If a foreigner
            </td>
            <td>
                රට <br>
                நாடு <br>
                Country
            </td>
            <td align="center">
                <s:select id="deathPersonCountryList" name="deathPersonCountry" value="%{deathCountryId}"
                          list="countryList"
                          headerKey="0"
                          headerValue="%{getText('select_country.label')}"/>
            </td>
        </tr>
        <tr>
            <td style="border-top:20px">
                ගමන් බලපත්‍ර අංකය <br>
                கடவுச் சீட்டு <br>
                Passport No.
            </td>
            <td>
                <s:textfield name="deathAlteration.deathPerson.deathPersonPassportNo" cssStyle="width:180px;"
                             id="passportNumber" value="%{deathAlteration.deathPerson.deathPersonPassportNo}"/>
            </td>
        </tr>
        <tr>
            <td>
                (19)වයස හෝ අනුමාන වයස <br>
                பிறப்ப <br>
                Age or probable Age
            </td>
            <td><s:textfield name="deathAlteration.deathPerson.deathPersonAge" cssStyle="width:180px;"
                             id="deathAge" value="%{deathAlteration.deathPerson.deathPersonAge}"/></td>
            <td>
                (20)ස්ත්‍රී පුරුෂ භාවය <br>
                பால் <br>
                Gender
            </td>
            <td>
                <s:select
                        list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                        name="deathAlteration.deathPerson.deathPersonGender" headerKey="0"
                        headerValue="%{getText('select_gender.label')}"
                        id="deathPersonGender" cssStyle="width:190px; margin-left:5px;"/>
            </td>
            <td>
                (21)ජාතිය <br>
                பிறப் <br>
                Race
            </td>
            <td colspan="2" align="center">
                <s:select list="raceList" name="deathPersonRace" headerKey="0" value="%{deathRaceId}"
                          headerValue="%{getText('select_race.label')}"
                          cssStyle="width:200px;" id="deathPersonRaceList"/>
            </td>
        </tr>
        <tr>
            <td>
                (22)නම රාජ්‍ය භාෂාවෙන් <br>
                (සිංහල / දෙමළ)
                பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்) <br>
                Name in either of the official languages (Sinhala / Tamil)
            </td>
            <td colspan="6">
                <s:textarea name="deathAlteration.deathPerson.deathPersonNameOfficialLang" cssStyle="width:99%;"
                            id="nameOfficialLang" value="%{deathAlteration.deathPerson.deathPersonNameOfficialLang}"/>
            </td>
        </tr>
        <tr>
            <td>
                (23)නම ඉංග්‍රීසි භාෂාවෙන් <br>
                பிறப்பு அத்தாட்சி ….. <br>
                Name in English
            </td>
            <td colspan="6">
                <s:textarea name="deathAlteration.deathPerson.deathPersonNameInEnglish" cssStyle="width:99%;"
                            id="nameEnglish" value="%{deathAlteration.deathPerson.deathPersonNameInEnglish}"/>
            </td>
        </tr>
        <tr>
            <td>
                (24)ස්ථිර ලිපිනය <br>
                தாயின் நிரந்தர வதிவிட முகவரி <br>
                Permanent Address
            </td>
            <td colspan="6">
                <s:textarea name="deathAlteration.deathPerson.deathPersonPermanentAddress" cssStyle="width:99%;"
                            id="address" value="%{deathAlteration.deathPerson.deathPersonPermanentAddress}"/>
            </td>
        </tr>
        <tr>
            <td>
                (25)පියාගේ අනන්‍යතා අංකය <br>
                தனிநபர் அடையாள எண் <br>
                Fathers Identification Number
            </td>
            <td colspan="6">
                <s:textfield name="deathAlteration.deathPerson.deathPersonFatherPINorNIC" cssStyle="width:180px;"
                             id="fatherPinNic" value="%{deathAlteration.deathPerson.deathPersonFatherPINorNIC}"/>
            </td>
        </tr>
        <tr>
            <td>
                (26)පියාගේ සම්පුර්ණ නම <br>
                தந்தையின் முழுப் பெயர்<br>
                Fathers full name
            </td>
            <td colspan="6">
                <s:textarea name="deathAlteration.deathPerson.deathPersonFatherFullName" cssStyle="width:99%;"
                            id="fatherName" value="%{deathAlteration.deathPerson.deathPersonFatherFullName}"/>
            </td>
        </tr>
        <tr>
            <td>
                (27)මවගේ අනන්‍යතා අංකය <br>
                தனிநபர் அடையாள எண் <br>
                Mothers Identification Number
            </td>
            <td colspan="6">
                <s:textfield name="deathAlteration.deathPerson.deathPersonMotherPINorNIC" cssStyle="width:180px;"
                             id="motherNIC" value="%{deathAlteration.deathPerson.deathPersonMotherPINorNIC}"/>
            </td>

        </tr>
        <tr>
            <td>
                (28)මවගේ සම්පුර්ණ නම <br>
                தாயின் முழுப் பெயர்<br>
                Mothers full name
            </td>
            <td colspan="6">
                <s:textarea name="deathAlteration.deathPerson.deathPersonMotherFullName" cssStyle="width:99%;"
                            id="motherName" value="%{deathAlteration.deathPerson.deathPersonMotherFullName}"/>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>
<table class="death-alteration-table-style02" style=" margin-top:20px;width:100%;" cellpadding="0" cellpadding="2px"
       cellspacing="0">
    <tr>
        <td align="center" style="text-align:center;font-size:12pt">
            දෝෂය හා එය සිදුවූ අන්දම පිලිබඳ ලුහුඬු විස්තර<br>
            தாத்தாவின் / பாட்டனின் விபரங்கள்<br>
            Nature of the error and a brief explanation of how the error occurred
        </td>
    </tr>
    <tr>
        <td align="center">
            <s:textarea name="deathAlteration.howErrorHappen" cssStyle="width:98%"
                        value="%{deathAlteration.howErrorHappen}"/>
        </td>
    </tr>
</table>
<br>
<table class="death-alteration-table-style02" style=" margin-top:20px;width:100%;" cellpadding="0" cellpadding="2px"
       cellspacing="0">
    <tr>
        <td colspan="2" style="text-align:center;font-size:12pt">
            ප්‍රකාශය සනාත කිරීමට ඇති ලේඛනගත හෝ වෙනත් සාක්ෂිවල ස්වභාවය<br>
            தாத்தாவின் / பாட்டனின் விபரங்கள் <br>
            Nature of documentary or other evidence in support of the declaration
        </td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="deathAlteration.bcOfFather"/></td>
        <td style="width:90%"><s:label
                value=" පියාගේ උප්පැන්න සහතිකය  / தகப்பனின் பிறப்புச் சான்றிதழ்  / Fathers Birth Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="deathAlteration.bcOfMother"/></td>
        <td style="width:90%"><s:label
                value="මවගේ උප්පැන්න සහතිකය / தாயின் பிறப்புச் சான்றிதழ் / Mothers Birth Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="deathAlteration.mcOfParents"/></td>
        <td style="width:90%"><s:label
                value=" මව්පියන්ගේ විවාහ සහතිකය / பொற்றோரின் திருமணச் சான்றிதழ் / Parents Marriage Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td colspan="2" style="width:98%"><s:textarea name="deathAlteration.otherDocuments" cssStyle="width:98%"/></td>
    </tr>
</table>
<br>
<table class="death-alteration-table-style01" style="width:1030px;border-top:50px">
    <tr>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;">
            <s:label value="ප්‍රකාශය කරන්නාගේ විස්තර"/> <br>
            <s:label value="அறிவிப்பு கொடுப்பவரின் தகவல்கள்"/> <br>
            <s:label value="Details of the Declarant"/>
        </td>
    </tr>
</table>
<br>
<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;" cellpadding="2px">
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
        <td rowspan="2" width="200px">
            (30)දැනුම් දෙන්නේ කවරකු වශයෙන්ද <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            தகவல் வழங்குபவர் <br>
            Capacity for giving information
        </td>
        <td>
            පියා / මව <br>
            தந்தை/ தாய்<br>
            Father / Mother
        </td>
        <td align="center"><s:radio id="declarantType" name="deathAlteration.declarant.declarantType"
                                    list="#@java.util.HashMap@{'FATHER':''}"
                                    onchange="setInformPerson('','','','','');"
                                    value="%{deathAlteration.declarant.declarantType}"/>
        </td>
        <td align="center">
            ස්වාමිපුරුෂයා / භාර්යාව <br>
            கணவன்/ மனைவி<br>
            Husband / Wife
        </td>
        <td width="45px" align="center">
                <%--todo change--%>
            <s:radio id="declarantType" name="deathAlteration.declarant.declarantType"
                     list="#@java.util.HashMap@{'SPOUSE':''}"
                     onchange="setInformPerson('','','','','');" value="%{deathAlteration.declarant.declarantType}"/>
        </td>
        <td>
            සහෝදරයා සහෝදරිය <br>
            சகோதரான்/ சகோதரி<br>
            Brother / Sister
        </td>
        <td align="center">
            <s:radio id="declarantType" name="deathAlteration.declarant.declarantType"
                     list="#@java.util.HashMap@{'BORTHER_OR_SISTER':''}"
                     onchange="setInformPerson('','','','','');" value="%{deathAlteration.declarant.declarantType}"/>
        </td>
    </tr>
    <tr>
        <td>
            පුත්‍රයා / දියණිය <br>
            மகன்/ மகள்<br>
            Son / Daughter
        </td>
        <td align="center">
            <s:radio id="declarantType" name="deathAlteration.declarant.declarantType"
                     list="#@java.util.HashMap@{'SON_OR_DAUGHTER':''}"
                     onchange="setInformPerson('','','','','');" value="%{deathAlteration.declarant.declarantType}"/>
        </td>
        <td>
            නෑයන් <br>
            பாதுகாவலர் <br>
            Relative
        </td>
        <td align="center">
            <s:radio id="declarantType" name="deathAlteration.declarant.declarantType"
                     list="#@java.util.HashMap@{'RELATIVE':''}"
                     onchange="setInformPerson('','','','','');" value="%{deathAlteration.declarant.declarantType}"/>
        </td>
        <td>
            වෙනත් <br>
            வேறு<br>
            Other
        </td>
        <td align="center">
            <s:radio id="declarantType" name="deathAlteration.declarant.declarantType"
                     list="#@java.util.HashMap@{'OTHER':''}"
                     onchange="setInformPerson('','','','','');" value="%{deathAlteration.declarant.declarantType}"/>
        </td>
    </tr>
    <tr style="border-top:20px">
        <td colspan="1">
            (31)අනන්‍යතා අංකය <br>
            தனிநபர் அடையாள எண் <br>
            Identification Number
        </td>
        <td colspan="6" align="left">
            <s:textfield id="declarant_pinOrNic" name="deathAlteration.declarant.declarantNICorPIN" maxLength="10"
                         value="%{deathAlteration.declarant.declarantNICorPIN}"/>
        </td>
    </tr>
    <tr>
        <td>
            (32)නම <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            கொடுப்பவரின் பெயர் <br>
            Name
        </td>
        <td colspan="6">
            <s:textarea id="declarantName" name="deathAlteration.declarant.declarantFullName" cssStyle="width:99%"
                        value="%{deathAlteration.declarant.declarantFullName}"/>
        </td>
    </tr>
    <tr>
        <td>
            (33)තැපැල් ලිපිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br>
            தபால் முகவரி <br>
            Postal Address
        </td>
        <td colspan="6">
            <s:textarea id="declarantAddress" name="deathAlteration.declarant.declarantAddress" cssStyle="width:99%"
                        value="%{deathAlteration.declarant.declarantAddress}"/>
        </td>
    </tr>

    </tbody>
</table>
<%--<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;border-top:none"
       cellpadding="5px">
    <tbody>
    <tr>
        <td>(34)ඇමතුම් විස්තර <br>
            இலக்க வகை <br>
            Contact Details
        </td>
        <td>
            දුරකතනය <br>
            தொலைபேசி இலக்கம் <br>
            Telephone
        </td>
        <td colspan="2" align="center">
            <s:textfield id="declarant_tp" name="deathRegister.declarant.declarantPhone" maxLength="10"
                         cssStyle="width:98%"/>
        </td>
        <td>
            ඉ -තැපැල <br>
            மின்னஞ்சல் <br>
            Email
        </td>
        <td colspan="2">
            <s:textfield cssStyle="width:98%" id="declarant_email" name="deathRegister.declarant.declarantEMail"
                         maxLength="10"/>
        </td>
    </tr>
    </tbody>
</table>--%>
<div class="form-submit">
    <s:submit value="%{getText('save.label')}"/>
</div>
<s:hidden name="deathId" value="%{deathAlteration.deathRegisterIDUkey}"/>
<s:hidden name="editMode" value="%{editMode}"/>
<s:hidden name="deathAlterationId" value="%{deathAlteration.idUKey}"/>
<s:hidden id="deathPerson" name="editDeathPerson" value=""/>
<s:hidden id="death" name="editDeathInfo" value=""/>
</s:form>
</div>
<s:hidden id="error0" value="%{getText('er.invalid.inputType')}"/>
<s:hidden id="error1" value="%{getText('er.label.reciveDatePicker')}"/>
<s:hidden id="error2" value="%{getText('er.label.dateOfDeath')}"/>
<s:hidden id="error23" value="%{getText('er.label.placeOfdeath')}"/>
<s:hidden id="error24" value="%{getText('er.label.placeOfBurial')}"/>
<s:hidden id="error3" value="%{getText('er.label.deathPerson_PINorNIC')}"/>
<s:hidden id="error4" value="%{getText('er.label.passportNumber')}"/>
<s:hidden id="error5" value="%{getText('er.label.deathAge')}"/>
<s:hidden id="error6" value="%{getText('er.label.fatherPinNic')}"/>
<s:hidden id="error7" value="%{getText('er.label.motherNIC')}"/>
<s:hidden id="error8" value="%{getText('er.label.declarant_pinOrNic')}"/>
<s:hidden id="error9" value="%{getText('er.label.cannot.empty')}"/>
<s:hidden id="error10" value="%{getText('er.label.declarent.type')}"/>
<s:hidden id="declerent_type" value="%{getText('label.declarent.type.must.fill')}"/>
<s:hidden id="text_must_select" value="%{getText('label.must.select')}"/>
<s:hidden id="text_must_fill" value="%{getText('label.must.fill')}"/>
<s:hidden id="declerent_name" value="%{getText('label.declerent.name')}"/>
<s:hidden id="declerent_address" value="%{getText('label.declerent.address')}"/>

