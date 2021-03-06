<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
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

        domObject = document.getElementById("pin");
        if (!isFieldEmpty(domObject)) {
            validatePIN(domObject, 'error0', 'error2');
        }

        domObject = document.getElementById("nic");
        if (!isFieldEmpty(domObject)) {
            validateNIC(domObject, 'error0', 'error11');
        }


        checkCivilStatus();

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

        var out = checkActiveFieldsForSyntaxErrors('person-details-update-form');
        if (out != "") {
            errormsg = errormsg + out;
        }

        if (errormsg != "") {
            alert(errormsg);
            returnVal = false;
        }
        errormsg = "";
        return returnVal;
        alert("AAA")
    }

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

    $(function() {
        $('img#personName').bind('click', function(evt3) {
            var text = $("textarea#personFullNameOfficialLang").attr("value");

            $.post('/ecivil/TransliterationService', {text:text,gender:'M'},
                    function(data) {
                        if (data != null) {
                            var s = data.translated;
                            $("textarea#personFullNameEnglish").val(s);
                        }
                    });
        });
    });

    $(function() {
        $("#submitDatePicker").datepicker({
            changeYear:true,
            yearRange:'1960:2020',
            dateFormat:'yy-mm-dd'
        });
    });

    $(function() {
        $("#permenantAddressDate").datepicker({
            changeYear:true,
            yearRange:'1960:2020',
            dateFormat:'yy-mm-dd'
        });
    });

    $(function() {
        $("#changeAddressDate").datepicker({
            changeYear:true,
            yearRange:'1960:2020',
            dateFormat:'yy-mm-dd'
        });
    });

    // Enable citizen list minimized in page load and disable save buttons
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
        var oTable = $('#citizenship-table').dataTable({
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
                /* Passport No. */ null,
                /* Delete Button */ null
            ]
        });

        // delete selected row from citizenship table
        $('#citizenship-table tbody tr a.delete img').live('click', function () {
            var row = $(this).closest("tr").get(0);
            oTable.fnDeleteRow(oTable.fnGetPosition(row));
        });

        // Set citizen list string to "citizenship" hidden field
        $('#submitButton').click(function() {
            var nNodes = oTable.fnGetNodes();
            var arr = new Array();
            for (var i = 0; i < nNodes.length; i++) {
                arr[i] = oTable.fnGetData(i)[0] + ':' + oTable.fnGetData(i)[2];
            }

            var citizenHidden = document.getElementById('citizenship');
            citizenHidden.value = arr;
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

    // Add new citizen to the citizen list
    function fnClickAddRow() {
        if (validateAddCitizenship()) {
            $('#citizenship-table').dataTable().fnAddData([
                $('select#citizenCountryId').attr('value'),
                $('select#citizenCountryId option:selected').text(),
                $('input#citizenPassportNo').val().toUpperCase(),
                "<a href='javascript:void(0)' class='delete'><img src='<s:url value='/images/reject.gif'/>' width='25' height='25' border='none' style='float:right;margin-right:15px;'></a>"
            ]);
            $('input#citizenPassportNo').val('');
            $('select#citizenCountryId').val('');
        }
    }

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
</script>

<div class="prs-existing-person-register-outer">

<s:form id="person-details-update-form" action="eprUpdatePersonDetails.do" method="POST" onsubmit="javascript:return validate()">

<table class="table_reg_header_01" style="font-size:9pt">
<caption></caption>
<col/>
<col/>
<tbody>
<tr>
    <td>

    </td>

    <td align="center" style="font-size:12pt; width:400px">
        <img src="<s:url value="/images/official-logo.png"/>"/> <br/>

        <div id="main_title">
            ජනගහන ලේඛනයේ විස්තර වෙනස් කිරීම<br/>
            குடிமதிப்பீட்டு ஆவணத்தில் ஆட்களை பதிவு செய்தல் <br/>
            Updating details in the Population Registry
        </div>
    </td>

    <td width="350px" align="right" style="margin-right:0;">
        <table class="table_reg_datePicker_page_01" style="width:90%;margin-right:0;margin-bottom:0;border-bottom:none">
            <tr>
                <td colspan="2">
                    කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br/>
                    அலுவலக பாவனைக்காக மட்டும் <br/>
                    For office use only
                </td>
            </tr>
        </table>
        <table class="table_reg_datePicker_page_01" style="width:90%;margin-right:0;margin-top:0px;">
            <tr>
                <td>භාරගත් දිනය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br/>
                    பெறப்பட்ட திகதி <br/>
                    Date of Acceptance
                </td>
                <td>
                    <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
                    <s:textfield id="submitDatePicker" name="person.dateOfRegistration" maxLength="10" disabled="true"/>
                </td>
            </tr>
        </table>
    </td>
</tr>

<tr>
    <table class="table_reg_page_05" cellspacing="0" cellpadding="0" style="margin-bottom:0;margin-top:20px;">
        <tr>
            <td width="25%" colspan="2">
                (1) අනන්‍යතා අංකය <br/>
                அடையாள எண் <br/>
                Identification number
            </td>
            <td width="25%" colspan="2">
                <s:textfield id="pin" name="person.pin" maxLength="10" readonly="true"/>
            </td>
            <td width="25%" colspan="2">
                (2) ජාතික හැඳුනුම්පත් අංකය <br/>
                தேசிய அடையாள அட்டை இலக்கம் <br/>
                National Identity Card (NIC) Number
            </td>
            <td width="25%" colspan="2">
                <s:textfield name="person.nic" id="nic" readonly="true"/>
            </td>
        </tr>
        <tr>
            <td rowspan="2" colspan="2">
                (3) සිවිල් තත්වය <s:label value="*" cssStyle="color:red;font-size:10pt"/><br/>
                சிவில் நிலைமை <br/>
                Civil Status
            </td>
            <td align="right" class="right_align">අවිවාහක<br>திருமணமாகாதவர் <br>Never Married</td>
            <td width="5%"><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'NEVER_MARRIED':''}"/></td>
            <td align="right" class="right_align">විවාහක<br>திருமணமாணவர் <br>Married</td>
            <td width="5%"><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'MARRIED':''}"/></td>
            <td align="right" class="right_align">දික්කසාද<br>திருமணம் தள்ளுபடி செய்தவர் <br>Divorced</td>
            <td width="5%"><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'DIVORCED':''}"/></td>
        </tr>
        <tr>
            <td align="right" class="right_align">වැන්දඹු<br>விதவை <br>Widowed</td>
            <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'WIDOWED':''}"/></td>
            <td align="right" class="right_align">ශුන්‍ය කර ඇත<br>தள்ளிவைத்தல் <br>Annulled</td>
            <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'ANNULLED':''}"/></td>
            <td align="right" class="right_align">වෙන් වී ඇත<br>பிரிந்திருத்தல் <br>Separated</td>
            <td><s:radio name="person.civilStatus" list="#@java.util.HashMap@{'SEPARATED':''}"/></td>
        </tr>
    </table>
</tr>
<tr>
    <td colspan="4">
        &nbsp;
    </td>
</tr>
<tr>  <!-- Addresses and TP Details  [ table ]   -->
    <table class="table_reg_page_05" cellspacing="0" cellpadding="0" style="margin-bottom:10px;margin-top:20px;">
        <tr>
            <td width="15%">
                (4) ස්ථිර ලිපිනය<s:label value="*" cssStyle="color:red;font-size:10pt"/><br/>
                நிரந்தர வதிவிட முகவரி <br/>
                Permanent Address
            </td>
            <td colspan="3">
                <s:textarea name="person.permanentAddress" id="permanentAddress"
                            cssStyle="width:98.2%;text-transform:uppercase;"/>
            </td>
        </tr>
        <tr>
            <td colspan="4">
                වර්තමාන පදිංචිය වෙනත් ස්ථානයක නම් පමණක්, தற்போதைய முகவரி வேறு இடமாயின் மட்டும் , Only if residing at a
                different location
            </td>
        </tr>
        <tr>
            <td>
                (5) වර්තමාන ලිපිනය <br/>
                தற்போதைய வதிவிட முகவரி <br/>
                Current Address
            </td>
            <td colspan="3">
                <s:textarea name="person.currentAddress" cssStyle="width:98.2%;text-transform:uppercase;"/>
            </td>
        </tr>
        <tr>
            <td width="20%">
                (6) දුරකථන අංක <br/>
                தொலைபேசி இலக்கம் <br/>
                Telephone Numbers
            </td>

            <td width="30%">
                <s:textfield name="person.personPhoneNo" id="personPhoneNo" maxLength="15"/>
            </td>
            <td width="20%">
                (7) ඉ – තැපැල <br/>
                மின்னஞ்சல் <br/>
                Email
            </td>
            <td width="30%">
                <s:textfield name="person.personEmail" id="personEmail" cssStyle="text-transform:lowercase;"/>
            </td>
        </tr>
    </table>
</tr>

<tr>
    <td>
        <table class="table_reg_page_05" cellspacing="0" cellpadding="0" style="margin-bottom:0;margin-top:20px;">

        </table>
    </td>
</tr>

<fieldset style="margin-bottom:5px;border:1px solid #000;width:97.9%;">
    <table class="table_reg_page_05" style="border:none;margin-bottom:5px;margin-top:5px;width:100%;">
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
        <table id="citizenship-table" cellpadding="0" cellspacing="0" class="display" style="width:100%;">
            <thead>
            <tr style="font-size:10pt;">
                <th>Country Id</th>
                <th>රට/நாடு /Country</th>
                <th>ගමන් බලපත්‍ර අංකය/கடவுச் சீட்டு இல./Passport No.</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="citizenshipList">
                <tr>
                    <td><s:property value="countryId"/></td>
                    <td><s:property value="country.countryCode"/> : <s:property value="country.siCountryName"/></td>
                    <td><s:property value="passportNo"/></td>
                    <td>
                        <a href='javascript:void(0)' class='delete'>
                            <img src="<s:url value='/images/reject.gif'/>" width="25" height="25" border="none"
                                 style="float:right;margin-right:15px;">
                        </a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>

        <table id="citizen" class="table_reg_page_05" cellspacing="0" cellpadding="0"
               style="margin-top:10px;width:100%;">
            <col width="230px"/>
            <col width="250px"/>
            <col width="220px"/>
            <col width="270px"/>
            <col width="60px"/>
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
                <td>
                    <a href="javascript:void(0)" onclick="fnClickAddRow();">
                        <img src="<s:url value="/images/add.png"/>" width="20" height="20" border="none"
                             style="float:right;margin-right:15px;">
                    </a>
                </td>
            </tr>
        </table>
    </div>
</fieldset>
    <%--<s:form action="eprUpdatePersonDetails.do" method="POST" onsubmit="javascript:return validate()">--%>
    <%--<s:hidden name="personUKey" value="%{#request.personUKey}"/>--%>
    <%--<div class="form-submit">--%>
    <%--<s:submit value="%{getText('save.label')}" cssStyle="margin-top:-15px;"/>--%>
    <%--</div>--%>
    <%--</s:form>--%>

    <%--<s:form action="eprPersonDetails.do" method="get">--%>
    <%--<s:hidden name="personUKey" value="%{#request.personUKey}"/>--%>
    <%--<div class="form-submit">--%>
    <%--<s:submit value="%{getText('previous.label')}" cssStyle="margin-top:-15px;"/>--%>
    <%--</div>--%>
</tbody>
</table>

<div class="form-submit">
    <s:submit id="submitButton" value="%{getText('save.label')}" cssStyle="margin-top:5px;margin-bottom:5px;"/>
</div>

<s:hidden id="citizenship" name="citizenship"/>
<s:hidden name="person.personUKey" value="%{#request.personUKey}"/>
<s:hidden name="person.status" value="%{#request.person.status}"/>
<s:hidden name="personUKey" value="%{#request.personUKey}"/>
</s:form>

<s:form action="eprPersonDetails.do" method="POST">
    <s:hidden name="personUKey" value="%{#request.personUKey}"/>
    <div class="form-submit">
        <s:submit id="submitButton" value="%{getText('previous.label')}" cssStyle="margin-top:5px;margin-bottom:5px;"/>
    </div>
</s:form>
<br/>
<br/>

<s:hidden id="error0" value="%{getText('er.invalid.inputType')}"/>
<s:hidden id="error1" value="%{getText('er.label.submitDatePicker')}"/>
<s:hidden id="error2" value="%{getText('er.label.temporaryPIN')}"/>
<s:hidden id="error3" value="%{getText('er.label.personPassportNo')}"/>
<s:hidden id="error4" value="%{getText('er.label.birthDatePicker')}"/>
<s:hidden id="error8" value="%{getText('er.label.permanentAddress')}"/>
<s:hidden id="error9" value="%{getText('er.label.personPhoneNo')}"/>
<s:hidden id="error10" value="%{getText('er.label.personEmail')}"/>
<s:hidden id="error11" value="%{getText('er.label.personNIC')}"/>
<s:hidden id="error12" value="%{getText('er.label.personNIC')}"/>
<s:hidden id="error13" value="%{getText('er.label.personCountry')}"/>
<s:hidden id="error17" value="%{getText('er.label.civilStatus')}"/>

</div>
