<%--@author Chathuranga Withana--%>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>

<%@ page import="lk.rgd.common.util.NameFormatUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script>
    // Basic form validation for PRS Person Registration
    var errormsg = "";
    function validate() {
        var returnVal = true;
        var domObject;

        domObject = document.getElementById("submitDatePicker");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error1');
        } else {
            isDate(domObject.value, 'error0', 'error1');
        }

        domObject = document.getElementById("person_NIC");
        if (!isFieldEmpty(domObject)) {
            validateNIC(domObject, 'error0', 'error11');
        }

        domObject = document.getElementById("temporaryPIN");
        if (!isFieldEmpty(domObject)) {
            validateTemPIN(domObject, 'error0', 'error2');
        }

        domObject = document.getElementById('personRaceId');
        if (domObject.value == 0) {
            errormsg = errormsg + "\n" + document.getElementById('error16').value;
        }

        // validate person passport number
        domObject = document.getElementById('personPassportNo');
        if (!isFieldEmpty(domObject)) {
            if (validatePassportNo(domObject, 'error0', 'error3')) {
                domObject = document.getElementById('personCountryId');
                if (domObject.value == 0) {
                    errormsg = errormsg + "\n" + document.getElementById('error13').value;
                }
            }
        }
        domObject = document.getElementById('personCountryId');
        if (domObject.value != 0) {
            domObject = document.getElementById('personPassportNo');
            if (isFieldEmpty(domObject)) {
                isEmpty(domObject, '', 'error3');
            }
        }

        domObject = document.getElementById("birthDatePicker");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error4');
        } else {
            isDate(domObject.value, 'error0', 'error4');
        }

        domObject = document.getElementById("placeOfBirth");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error5');
        }

        domObject = document.getElementById("personFullNameOfficialLang");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error6');
        }

        domObject = document.getElementById("personFullNameEnglish");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error7');
        }

        checkCivilStatus();

        domObject = document.getElementById("motherPINorNIC");
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error0', 'error14');
        }

        domObject = document.getElementById("fatherPINorNIC");
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error0', 'error15');
        }

        domObject = document.getElementById("permanentAddress");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error8');
        }

        domObject = document.getElementById("personPhoneNo");
        if (!isFieldEmpty(domObject)) {
            validatePhoneNo(domObject, "error0", 'error9');
        }

        domObject = document.getElementById("personEmail");
        if (!isFieldEmpty(domObject)) {
            validateEmail(domObject, "error0", 'error10');
        }

        if (errormsg != "") {
            alert(errormsg);
            returnVal = false;
        }
        errormsg = "";
        return returnVal;
    }

    // Check country and passport number given before adding it to the citizenship table
    function validateAddCitizenship() {
        var returnVal = true;
        var domObject;

        domObject = document.getElementById('citizenCountryId');
        if (domObject.value == 0) {
            errormsg = document.getElementById('error13').value;
            checkPassportNo();
        } else {
            checkPassportNo();
        }

        if (errormsg != "") {
            alert(errormsg);
            returnVal = false;
        }
        errormsg = "";
        return returnVal;
    }

    // checks passport number available and valid
    function checkPassportNo() {
        domObject = document.getElementById('citizenPassportNo');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, '', 'error3');
        } else {
            validatePassportNo(domObject, 'error0', 'error3');
        }
    }

    // TODO for dob calculation for person, how to add??
    /*function calculateBirthDay(id, datePicker, error) {
     var reg = /^([0-9]{9}[X|x|V|v])$/;
     var day = id.substring(2, 5);
     var year = 19 + id.substring(0, 2);
     var date = new Date(year);
     if ((id.search(reg) == 0) && (day >= 501 && day <= 866)) {
     if ((day > 559) && ((date.getFullYear() % 4) != 0 )) {
     day = id.substring(2, 5) - 2;
     date.setDate(date.getDate() + day - 500);
     } else {
     date.setDate(date.getDate() + day - 1500);
     }
     datePicker.datepicker('setDate', new Date(date.getYear(), date.getMonth(), date.getDate()));
     } else if ((id.search(reg) == 0) && (day > 0 && day <= 366)) {
     if ((day > 59) && ((date.getFullYear() % 4) != 0 )) {
     day = id.substring(2, 5) - 2;
     date.setDate(date.getDate() + day);
     } else {
     date.setDate(date.getDate() + day - 1000);
     }

     datePicker.datepicker('setDate', new Date(date.getYear(), date.getMonth(), date.getDate()));
     }
     }*/

    // check whether any civil status selected
    function checkCivilStatus() {
        var domObject,unChecked = true;
        for (i = 0; i < 6; i++) {
            domObject = document.getElementsByName("person.civilStatus")[i];
            if (domObject.checked) {
                unChecked = false;
                break;
            }
        }
        if (unChecked) {
            errormsg = errormsg + "\n" + document.getElementById('error17').value;
        }
    }
</script>
<script type="text/javascript">

    $('img#personName').bind('click', function(evt3) {
        var id = $("textarea#personFullNameOfficialLang").attr("value");
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
        $("textarea#personFullNameEnglish").val(respObj.Body[0].transliterateResponse[0].return[0].Text);
    };

    $(function() {
        $("#submitDatePicker").datepicker({
            changeYear:true,
            yearRange:'1960:2020',
            dateFormat:'yy-mm-dd'
        });
    });

    $(function() {
        $("#birthDatePicker").datepicker({
            changeYear:true,
            yearRange:'1960:2020',
            dateFormat:'yy-mm-dd'
        });
    });

    // Enable citizen list minimized in page load 
    function initPage() {
        document.getElementById('citizen-info-min').style.display = 'none';
        document.getElementById('citizenDetails').style.display = 'none';
    }

    // Minimize and maximize citizen list
    $(function() {
        $('#citizen-info-max').click(function() {
            document.getElementById('citizen-info-min').style.display = 'block';
            document.getElementById('citizen-info-max').style.display = 'none';
            document.getElementById('citizenDetails').style.display = 'block';
        });
        $('#citizen-info-min').click(function() {
            document.getElementById('citizen-info-min').style.display = 'none';
            document.getElementById('citizen-info-max').style.display = 'block';
            document.getElementById('citizenDetails').style.display = 'none';
        });
    });

    // Citizen list dataTable
    $(document).ready(function() {
        $('#citizenship-table').dataTable({
            "bPaginate": false,
            "bLengthChange": false,
            "bFilter": false,
            "aaSorting": [
                [0,'desc']
            ],
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers",
            "aoColumns": [
                /* Country Id */   {"bVisible":false },
                /* Country */  null,
                /* Passport No. */ null
            ]
        });

        $('#persons-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "bSort": true,
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "header":false,
            "sPaginationType": "full_numbers"
        });
    });

    // Add new citizen to the citizen list 
    function fnClickAddRow() {
        if (validateAddCitizenship()) {
            $('#citizenship-table').dataTable().fnAddData([
                $('select#citizenCountryId').attr('value'),
                $('select#citizenCountryId option:selected').text(),
                $('input#citizenPassportNo').val().toUpperCase()
            ]);
        }
    }

    var oTable;
    // Delete selected citizen row
    $(document).ready(function() {
        /* Add a click handler to the rows - this could be used as a callback */
        $("#citizenship-table tbody").click(function(event) {
            var i = 1;
            $(oTable.fnSettings().aoData).each(function () {
                // TODO fix color changing defect
                /*var y = i % 2;
                 if (y == 1) {
                 $(this.nTr).addClass("odd");
                 }
                 else {
                 $(this.nTr).addClass("even");
                 }
                 i++;*/
                $(this.nTr).removeClass('row_selected');
            });
            $(event.target.parentNode).addClass('row_selected');
            $(event.target.parentNode).removeClass('odd');
            $(event.target.parentNode).removeClass('even');
        });

        /* Add a click handler for the delete row */
        $('#delete').click(function() {
            var anSelected = fnGetSelected(oTable);
            oTable.fnDeleteRow(anSelected[0]);
        });

        /* Init the table */
        oTable = $('#citizenship-table').dataTable();
    });

    /* Get the rows which are currently selected */
    function fnGetSelected(oTableLocal) {
        var aReturn = new Array();
        var aTrs = oTableLocal.fnGetNodes();

        for (var i = 0; i < aTrs.length; i++) {
            if ($(aTrs[i]).hasClass('row_selected')) {
                aReturn.push(aTrs[i]);
            }
        }
        return aReturn;
    }

    // Set citizen list to a huddersfield
    $(document).ready(function() {
        $('#submitButton').click(function() {

            var nNodes = oTable.fnGetNodes();
            var arr = new Array();
            for (var i = 0; i < nNodes.length; i++) {
                arr[i] = oTable.fnGetData(i)[0] + ':' + oTable.fnGetData(i)[2];
            }

            var citizenHidden = document.getElementById('citizenship');
            citizenHidden.value = arr;
        });
    });
</script>

<div class="prs-existing-person-register-outer">
<s:form action="eprExistingPersonRegistration.do" method="POST" onsubmit="javascript:return validate()">

<table class="table_reg_header_01" style="font-size:9pt">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="250px"></td>
        <td align="center" style="font-size:12pt; width:430px">
            <img src="<s:url value="/images/official-logo.png"/>"/><br>
            <label>ජනගහන ලේඛනයේ පුද්ගලයකු ලියාපදිංචි කිරීම
                <br>குடிமதிப்பீட்டு ஆவணத்தில் ஆட்களை பதிவு செய்தல்
                <br>Registration of a person in the Population Registry</label>
        </td>
        <td width="350px" align="right" style="margin-right:0;">
            <table class="table_reg_datePicker_page_01" style="width:90%;margin-right:0;">
                <tr>
                    <td colspan="2">කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>அலுவலக பாவனைக்காக மட்டும்
                        <br>For office use only
                        <hr>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label><span
                                class="font-8">භාරගත්  දිනය<br>பெறப்பட்ட திகதி <br>Date of Acceptance </span></label>
                    </td>
                    <td>
                        <s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
                        <s:textfield name="person.dateOfRegistration" id="submitDatePicker" maxLength="10"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    </tbody>
</table>

<s:if test="personList.size > 0">
    <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
        <legend><b><s:label value="%{getText('label.possibleDuplicates')}"/> </b></legend>
        <table id="persons-table" width="100%" cellpadding="0" cellspacing="0" class="display">
            <thead>
            <tr>
                <th width="150px;">NIC</th>
                <th width="480px;"><s:label name="name" value="%{getText('label.personName')}"/></th>
                <th width="200px;">Status</th>
                <th width="20px;"></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator status="searchStatus" value="personList">
                <tr class="<s:if test="#searchStatus.odd == true">odd</s:if><s:else>even</s:else>">
                    <td align="center"><s:property value="nic"/></td>
                    <td>
                        <s:if test="fullNameInOfficialLanguage != null">
                            <%= NameFormatUtil.getDisplayName((String) request.getAttribute("fullNameInOfficialLanguage"), 50)%>
                        </s:if>
                    </td>
                    <td align="center"><s:property value="status"/></td>
                    <td align="center">
                        <s:if test="status.ordinal() != 2">
                            <s:url id="editPerson" action="eprEditPerson.do">
                                <s:param name="personUKey" value="personUKey"/>
                            </s:url>
                            <s:a href="%{editPerson}" title="%{getText('editTooltip.label')}">
                                <img src="<s:url value='/images/edit.png'/>" width="25" height="25" border="none"/>
                            </s:a>
                        </s:if>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
        <table width="60%" align="center" style="padding:0;cellspacing:0;cellpadding:0;">
            <tr>
                <td><s:checkbox name="ignoreDuplicate"/></td>
                <td><s:label value="Add Person Ignore Duplicates" name="ignoreDuplicate"/></td>
                <td>
                    <div class="form-submit" style="padding:0;margin-top:0px;">
                        <s:submit name="approve" value="Register"/></div>
                </td>
            </tr>
        </table>
    </fieldset>
</s:if>

<table class="table_reg_page_05" cellspacing="0" cellpadding="0" style="margin-bottom:0;margin-top:20px;">
    <caption></caption>
    <col width="230px"/>
    <col width="120px"/>
    <col width="130px"/>
    <col width="100px"/>
    <col width="80px"/>
    <col width="120px"/>
    <col width="120px"/>
    <col width="130px"/>
    <tbody>

    <tr>
        <td>
            (1) ජාතික හැඳුනුම්පත් අංකය
            <br>தேசிய அடையாள அட்டை இலக்கம்
            <br>National Identity Card (NIC) Number
        </td>
        <td colspan="2">
            <s:textfield name="person.nic" id="person_NIC" maxLength="10"/>
        </td>
        <td rowspan="3" colspan="2">
            (4) විදේශිකයකු නම්<br>வெளிநாட்டவர் எனின் <br>If a foreigner
        </td>
        <td rowspan="2">රට<br>நாடு<br>Country</td>
        <td colspan="2" rowspan="2">
            <s:select id="personCountryId" name="personCountryId" list="countryList" headerKey="0"
                      headerValue="%{getText('select_country.label')}" cssStyle="width:75%;margin-left:5px;"/>
        </td>
    </tr>
    <tr>
        <td>
            (2) තාවකාලික අනන්‍යතා අංකය
            <br>தற்காலிக அடையாள எண்
            <br>Temporary Identification number
        </td>
        <td colspan="2">
            <s:textfield name="person.temporaryPin" id="temporaryPIN" maxLength="10" onkeypress="return isNumberKey(event)"/>
        </td>
    </tr>
    <tr>
        <td>
            (3) ජාතිය<br>இனம்<br>Race
        </td>
        <td colspan="2">
            <s:select id="personRaceId" name="personRaceId" list="raceList" headerKey="0"
                      headerValue="%{getText('select_race.label')}" cssStyle="width:72%;margin-left:5px;"/>
        </td>
        <td>ගමන් බලපත්‍ර අංකය<br>கடவுச் சீட்டு இல.<br>Passport No.</td>
        <td colspan="2">
            <s:textfield name="personPassportNo" id="personPassportNo" maxLength="15"/>
        </td>
    </tr>
    <tr>
        <td>(5) උපන් දිනය<br>பிறந்த திகதி<br>Date of Birth</td>
        <td colspan="7">
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
            <s:textfield id="birthDatePicker" name="person.dateOfBirth" maxLength="10"/>
        </td>
    </tr>
    <tr>
        <td>(6) උපන් ස්ථානය<br>பிறந்த இடம்<br>Place of Birth</td>
        <td colspan="7">
            <s:textfield name="person.placeOfBirth" id="placeOfBirth" cssStyle="width:97.6%;" maxLength="255"/>
        </td>
    </tr>
    <tr>
        <td class="font-9">
            (7) නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
            <br>பெயர் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
            <br>Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="7">
            <s:textarea rows="3" name="person.fullNameInOfficialLanguage" id="personFullNameOfficialLang"
                        cssStyle="width:98.2%;"/>
        </td>
    </tr>
    <tr>
        <td class="font-9">(8) නම ඉංග්‍රීසි භාෂාවෙන් <br>பெயர் ஆங்கில மொழியில்<br>Name in English</td>
        <td colspan="7">
            <s:textarea name="person.fullNameInEnglishLanguage" id="personFullNameEnglish"
                        cssStyle="width:98.2%;"/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;"
                 id="personName">
        </td>
    </tr>
    <tr>
        <td class="font-9" colspan="3">
            පුද්ගලයකු ලියාපදිංචි කිරීමේ සහතිකය නිකුත් කල යුතු භාෂාව
            <br>நபரின் பதிவிற்கான சான்றிதழினை வழங்கப்படவேண்டிய மொழி 
            <br>Preferred Language for Person Registration Certificate
        </td>
        <td colspan="5">
            <s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'தமிழ்'}" name="person.preferredLanguage"
                      cssStyle="width:190px; margin-left:5px;"/>
        </td>
    </tr>

    <tr>
        <td class="font-9">
            (9) ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender of the child
        </td>
        <td colspan="2">
            <s:select name="person.gender" cssStyle="width:190px; margin-left:5px;"
                      list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"/>
        </td>
        <td>
            (10) සිවිල් තත්ත්වය<br>சிவில் நிலைமை <br>Civil Status
        </td>
        <td colspan="5" style="border:none;padding:0">
            <table style="border:none;" cellspacing="0;" width="100%">
                <tr>
                    <td align="right">අවිවාහක<br>திருமணமாகாதவர் <br>Never Married</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"/></td>
                    <td align="right">විවාහක<br>திருமணமாணவர் <br>Married</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'MARRIED':''}"/></td>
                    <td align="right">දික්කසාද<br>திருமணம் தள்ளுபடி செய்தவர் <br>Divorced</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'DIVORCED':''}"/></td>
                </tr>
                <tr>
                    <td align="right">වැන්දඹු<br>விதவை <br>Widowed</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'WIDOWED':''}"/></td>
                    <td align="right">නිෂ්ප්‍රභාකර ඇත <br>தள்ளிவைத்தல் <br>Annulled</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'ANNULLED':''}"/></td>
                    <td align="right">වෙන් වී ඇත<br>பிரிந்திருத்தல் <br>Separated</td>
                    <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'SEPARATED':''}"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            (11) මවගේ අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය
            <br>தாயின் அடையாள எண் அல்லது தேசிய அடையாள அட்டை இலக்கம்
            <br>Mothers Identification Number (PIN) or NIC
        </td>
        <td colspan="2">
            <s:textfield name="person.motherPINorNIC" id="motherPINorNIC" maxLength="10"/>
        </td>
        <td colspan="3">
            (12) පියාගේ අනන්‍යතා අංකය හෝ ජාතික හැඳුනුම්පත් අංකය
            <br>தந்தையின் அடையாள எண் அல்லது தேசிய அடையாள அட்டை இலக்கம்
            <br>Fathers Identification Number (PIN) or NIC
        </td>
        <td colspan="2">
            <s:textfield name="person.fatherPINorNIC" id="fatherPINorNIC" maxLength="10"/>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_05" cellspacing="0" cellpadding="0" style="margin-top:20px;margin-bottom:10px;">
    <caption></caption>
    <col width="230px"/>
    <col width="120px"/>
    <col width="130px"/>
    <col width="100px"/>
    <col width="100px"/>
    <col width="110px"/>
    <col width="110px"/>
    <col width="130px"/>
    <tbody>

    <tr>
        <td>(13) ස්ථිර ලිපිනය<br>நிரந்தர வதிவிட முகவரி<br>Permanent Address</td>
        <td colspan="7">
            <s:textarea name="permanentAddress" id="permanentAddress" cssStyle="width:98.2%;text-transform:uppercase;"/>
        </td>
    </tr>
    <tr>
        <td colspan="8" style="font-size:10pt;">
            වර්තමාන පදිංචිය වෙනත් ස්ථානයක නම් පමණක්, தற்போதைய முகவரி வேறு இடமாயின் மட்டும், Only if residing at a
            different location,
        </td>
    </tr>
    <tr>
        <td>(14) වර්තමාන ලිපිනය<br>தற்போதைய வதிவிட முகவரி<br>Current Address</td>
        <td colspan="7">
            <s:textarea name="currentAddress" cssStyle="width:98.2%;text-transform:uppercase;"/>
        </td>
    </tr>
    <tr>
        <td>(15) දුරකථන අංක<br>தொலைபேசி இலக்கம்<br>Telephone Numbers</td>
        <td colspan="2">
            <s:textfield name="person.personPhoneNo" id="personPhoneNo" maxLength="15"
                         onkeypress="return isNumberKey(event)"/>
        </td>
        <td>(16) ඉ – තැපැල් <br>மின்னஞ்சல்<br>Email</td>
        <td colspan="4">
            <s:textfield name="person.personEmail" id="personEmail" cssStyle="text-transform:none;" maxLength="30"/>
        </td>
    </tr>
    </tbody>
</table>

<table class="table_reg_page_05" style="border-right:#000 solid 1px;border-bottom:#000 solid 1px;">
    <tr>
        <td align="center" style="font-size:11pt;border:none">
            වෙනත් රටවල පුරවැසිභාවය ඇතිනම් ඒ පිලිබඳ විස්තර
            <br>வேறு நாடுகளில் பிரஜாவுரிமை இருந்தால் அது பற்றிய தகவல்கள்
            <br>If a citizen of any other countries, such details
        </td>
        <td width="2%" style="border:none">
            <div id="citizen-info-min">[-]</div>
            <div id="citizen-info-max">[+]</div>
        </td>
    </tr>
</table>

<div id="citizenDetails">
    <table id="citizen" class="table_reg_page_05" cellspacing="0" cellpadding="0"
           style="margin-bottom:10px;border-top:none;">
        <col width="230px"/>
        <col width="250px"/>
        <col width="220px"/>
        <col width="330px"/>
        <tr>
            <td>රට<br>நாடு <br>Country</td>
            <td>
                <s:select id="citizenCountryId" list="countryList" headerKey="0"
                          headerValue="%{getText('select_country.label')}" cssStyle="width:75%;margin-left:5px;"/>
            </td>
            <td>ගමන් බලපත්‍ර අංකය<br>கடவுச் சீட்டு இல.<br>Passport No.</td>
            <td>
                <s:textfield id="citizenPassportNo" maxLength="15"/>
            </td>
        </tr>
    </table>
    <div align="center">
        <a href="javascript:void(0)" onclick="fnClickAddRow();"><s:label value="%{getText('label.add')}"/></a>&nbsp;&nbsp;
        <a href="javascript:void(0)" id="delete"><s:label value="%{getText('label.delete')}"/></a>
    </div>
    <table id="citizenship-table" width="100%" cellpadding="0" cellspacing="0" class="display">
        <thead>
        <tr style="font-size:10pt;">
            <th>Country Id</th>
            <th>රට/நாடு /Country</th>
            <th>ගමන් බලපත්‍ර අංකය/கடவுச் சீட்டு இல./Passport No.</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="citizenshipList">
            <tr>
                <td><s:property value="countryId"/></td>
                <td><s:property value="country.siCountryName"/></td>
                <td><s:property value="passportNo"/></td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</div>
<div class="form-submit">
    <s:submit id="submitButton" value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>

<s:hidden id="citizenship" name="citizenship"/>
<s:hidden id="personUKey" name="personUKey" value="%{personUKey}"/>

</s:form>

<s:hidden id="error0" value="%{getText('er.invalid.inputType')}"/>
<s:hidden id="error1" value="%{getText('er.label.submitDatePicker')}"/>
<s:hidden id="error2" value="%{getText('er.label.temporaryPIN')}"/>
<s:hidden id="error3" value="%{getText('er.label.personPassportNo')}"/>
<s:hidden id="error4" value="%{getText('er.label.birthDatePicker')}"/>
<s:hidden id="error5" value="%{getText('er.label.placeOfBirth')}"/>
<s:hidden id="error6" value="%{getText('er.label.personFullNameOfficialLang')}"/>
<s:hidden id="error7" value="%{getText('er.label.personFullNameEnglish')}"/>
<s:hidden id="error8" value="%{getText('er.label.permanentAddress')}"/>
<s:hidden id="error9" value="%{getText('er.label.personPhoneNo')}"/>
<s:hidden id="error10" value="%{getText('er.label.personEmail')}"/>
<s:hidden id="error11" value="%{getText('er.label.personNIC')}"/>
<s:hidden id="error12" value="%{getText('er.label.personNIC')}"/>
<s:hidden id="error13" value="%{getText('er.label.personCountry')}"/>
<s:hidden id="error14" value="%{getText('er.label.motherPINorNIC')}"/>
<s:hidden id="error15" value="%{getText('er.label.fatherPINorNIC')}"/>
<s:hidden id="error16" value="%{getText('er.label.personRace')}"/>
<s:hidden id="error17" value="%{getText('er.label.civilStatus')}"/>
</div>