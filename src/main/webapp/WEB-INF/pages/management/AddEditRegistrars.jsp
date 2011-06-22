<%@ page import="java.util.Locale" %>
<%@ page import="lk.rgd.common.util.AssignmentUtill" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<script>
$(document).ready(function() {
    //Hide (Collapse) the toggle containers on load
    $(".toggle_container").hide();

    //Switch the "Open" and "Close" state per click then slide up/down (depending on open/close state)
    $("h2.trigger").click(function() {
        $(this).toggleClass("active").next().slideToggle("slow");
    });
});

$(document).ready(function() {
    //hide the all of the element with class msg_body
    $(".msg_body").hide();
    //toggle the componenet with class msg_body
    $(".msg_head").click(function() {
        $(this).next(".msg_body").slideToggle(600);
    });
});

$(function() {
    $("#dateOfBirthDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});

$(function() {
    $("#dateOfAppoinmentDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});

$(function() {
    $("#dateOfPermenentDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});

$(function() {
    $("#dateOfTerminationDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});


$(document).ready(function() {
    $('#registrars-list-table').dataTable({
        "bPaginate": true,
        "bLengthChange": false,
        "bFilter": true,
        "bSort": true,
        "bInfo": false,
        "bAutoWidth": false,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers"
    });
});

// mode 1 = passing District, will return DS list
// mode 2 = passing DsDivision, will return BD list
// any other = passing district, will return DS list and the BD list for the first DS
$(function() {
    $('select#districtId').bind('change', function(evt1) {
        var id = $("select#districtId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:3},
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
});

var nameOfficialLang;
var nameEnglish;
var pin;
var nic;
var gender;
var dob;
var address;
var phone;
var email;
var lang;
var errormsg = "";

function disableFields(mode) {
    nameOfficialLang.disabled = mode;
    nameEnglish.disabled = mode;
    pin.disabled = mode;
    nic.disabled = mode;
    gender.disabled = mode;
    dob.disabled = mode;
    address.disabled = mode;
    phone.disabled = mode;
    email.disabled = mode;
    lang.disabled = mode;
    document.getElementById('saveUpdate').disabled = mode;

}

function initPage() {
    nameOfficialLang = document.getElementById('registrarNameInOfficelaLang');
    nameEnglish = document.getElementById('registrarNameInEnglish');
    pin = document.getElementById('registrarPin');
    nic = document.getElementById('registrarNIC');
    gender = document.getElementById('registrarGender');
    dob = document.getElementById('dateOfBirthDatePicker');
    address = document.getElementById('registrarAddress');
    phone = document.getElementById('registrarPhone');
    email = document.getElementById('registrarEmail');
    lang = document.getElementById('prefLanguage');
    disableFields(true)
}

function validateForm() {
    var check = document.getElementById('skipValidationId');
    var returnval = true;

    isMandatoryFieldsEmpty(nameOfficialLang, document.getElementById('nameOfficialError').value, "cannotNull")
    isMandatoryFieldsEmpty(nameEnglish, document.getElementById('nameEnglishError').value, "cannotNull")

    if (!check.checked) {
        if (isFieldEmpty(pin)) {
            isEmpty(pin, document.getElementById('pin').value, "empty")
        }
    }

    isMandatoryFieldsEmpty(nic, document.getElementById('nic').value, "cannotNull")
    isMandatoryFieldsEmpty(dob, document.getElementById('dob').value, "cannotNull")
    isMandatoryFieldsEmpty(address, document.getElementById('addressError').value, "cannotNull")

    if (!check.checked) {
        if (isFieldEmpty(phone)) {
            isEmpty(phone, document.getElementById('phone').value, 'empty')
        }
        if (isFieldEmpty(email)) {
            isEmpty(email, document.getElementById('email').value, 'empty')
        }
    }
    if (!isFieldEmpty(pin) && isInteger(pin)) {
        //validate PIN or NIC
        validatePINorNIC(pin, "pin", "invalideData")
    }
    if (!isFieldEmpty(nic)) {
        validatePINorNIC(nic, "nic", "invalideData")
    }
    if (!isFieldEmpty(phone)) {
        //validate phone number
        validatePhoneNo(phone, "phone", "invalideData")
    }
    if (!isFieldEmpty(email)) {
        //validate email
        validateEmail(email, "email", "invalideData")
    }
    //validate date of birth
    if (!isFieldEmpty(dob)) {
        isDate(dob.value, "Invalid ", "invalideData")
    }

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}
</script>

<style type="text/css">

    #assignments {
        margin-top: 30px;
        margin-bottom: 10px;
        font-size: 15px;
        cursor: default;
        height: 40px;
        width: 45%;
        float: left;
        text-align: center;
    }

    #assignments2 {
        margin-top: 25px;
        font-size: 15px;
        cursor: default;
        height: 40px;
        width: 45%;
        float: right;
        text-align: center;
        margin-bottom: 10px;
    }

    #addText {
        text-decoration: none;
        a: hover(black);
    }
</style>
<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
    <legend align="right"><s:property value="%{getText('registrar.basic.info')}"/></legend>
    <form action="eprUpdateRegistrar.do" method="post" onsubmit="javascript:return validateForm()">
        <table border="0" style="width: 100%" cellpadding="5" cellspacing="5">
            <caption></caption>
            <col width="400px"/>
            <col width="1000px"/>
            <tbody>
            <tr>
                <td align="left">
                    <s:property value="%{getText('registrar.full.name.officelaLang')}"/> <s:label value="*"
                                                                                                  cssStyle="color:red;font-size:14pt;"/>
                </td>
                <td align="left"><s:textfield id="registrarNameInOfficelaLang" cssStyle="width:100%"
                                              name="registrar.fullNameInOfficialLanguage"/></td>
            </tr>
            <tr>
                <td align="left">
                    <s:property value="%{getText('registrar.full.name.english')}"/> <s:label value="*"
                                                                                             cssStyle="color:red;font-size:14pt;"/>
                </td>
                <td align="left"><s:textfield id="registrarNameInEnglish" cssStyle="width:100%"
                                              name="registrar.fullNameInEnglishLanguage"/></td>
            </tr>
            <tr>
                <td align="left">
                    <s:property value="%{getText('registrar.pin')}"/>
                </td>
                <td align="left"><s:textfield id="registrarPin" name="registrar.pin" maxLength="12"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.nic')}"/>
                    <s:label value="*" cssStyle="color:red;font-size:14pt;"/>
                </td>
                <td align="left"><s:textfield id="registrarNIC" name="registrar.nic" maxLength="10"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.gender')}"/></td>
                <td align="left"><s:select
                        list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                        name="registrar.gender" cssStyle="width:190px;" id="registrarGender"/></td>
            </tr>
            <tr>
                <td align="left">
                    <s:property value="%{getText('registrar.dateofbirth')}"/> <s:label value="*"
                                                                                       cssStyle="color:red;font-size:14pt;"/>
                </td>
                <td align="left"><s:textfield name="registrar.dateOfBirth" id="dateOfBirthDatePicker"/></td>
            </tr>
            <tr>
                <td align="left">
                    <s:property value="%{getText('registrar.address')}"/> <s:label value="*"
                                                                                   cssStyle="color:red;font-size:14pt;"/>
                </td>
                <td align="left"><s:textarea id="registrarAddress" cssStyle="width:100%"
                                             name="registrar.currentAddress"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.phone')}"/></td>
                <td align="left"><s:textfield id="registrarPhone" name="registrar.phoneNo"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.email')}"/></td>
                <td align="left"><s:textfield id="registrarEmail" name="registrar.emailAddress"
                                              cssStyle="text-transform:none;"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.prefLang')}"/></td>
                <td align="left"><s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'Tamil'}"
                                           name="registrar.preferredLanguage"
                                           cssStyle="width:190px;" id="prefLanguage"/></td>
            </tr>
            </tbody>
        </table>
        <table>
            <col width="500px">
            <col width="250px">
            <col width="250px">
            <col width="250px">
            <tbody>
            <tr>
                <td colspan="1"></td>
                <td><s:property value="%{getText('label.skip.validation')}"/><s:checkbox id="skipValidationId"
                                                                                         name="skipValidationName"/></td>
                <td><s:property value="%{getText('label.enable.edit.mode')}"/> <s:checkbox name="enableEditMode"
                                                                                           id="enableEditMode"
                                                                                           onclick="javascript:disableFields(false);"/></td>
                <td><s:submit value="%{getText('label.submit')}" id="saveUpdate"/></td>
            </tr>
            </tbody>
        </table>
    </form>
</fieldset>

<%--current assignments--%>
<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
    <legend align="right"><s:property value="%{getText('registrar.current.assignments')}"/></legend>
    <s:actionerror cssStyle="color:red;"/><s:actionmessage cssStyle="color:blue;"/>
    <table id="registrars-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
        <thead>
        <tr class="table-title">
            <th><s:label value="%{getText('label.bdDivision')}"/></th>
            <th width="17%"><s:label value="%{getText('label.type')}"/></th>
            <th width="16%"><s:label value="%{getText('label.startDate')}"/></th>
            <th width="17%"><s:label value="%{getText('label.endDate')}"/></th>
            <th width="9%"><s:label value="%{getText('label.active')}"/></th>
            <th width="5%"></th>
            <th width="5%"></th>
        </tr>
        </thead>
        <s:if test="assignmentList.size>0">
            <tbody>
            <s:iterator status="assignmentStatus" value="assignmentList" id="assignmentList">
                <s:url action="eprAssignmentEdit.do" id="editSelected">
                    <s:param name="assignmentUKey" value="assignmentUKey"/>
                    <s:param name="editableAssignment" value="true"/>
                    <s:param name="assignmentType" value="%{type.ordinal()}"/>
                </s:url>
                <tr>
                    <s:if test="birthDivision != null">
                        <td><s:property value="birthDivision.enDivisionName"/></td>
                    </s:if>
                    <s:if test="deathDivision != null">
                        <td><s:property value="deathDivision.enDivisionName"/></td>
                    </s:if>
                    <s:if test="marriageDivision != null">
                        <td><s:property value="marriageDivision.enDivisionName"/></td>
                    </s:if>
                    <td>
                        <%= AssignmentUtill.getAssignmentType((Integer) request.getAttribute("type.ordinal()"),
                                ((Locale) session.getAttribute("WW_TRANS_I18N_LOCALE")).getLanguage())
                        %>
                    </td>
                    <td><s:property value="permanentDate"/></td>
                    <td><s:property value="terminationDate"/></td>
                    <td align="center">
                        <s:if test="lifeCycleInfo.active ==true">
                            <img src="<s:url value='/images/approve.gif'/>" width="25" height="25" border="none"/>
                        </s:if>
                        <s:else>
                            <img src="<s:url value='/images/reject.gif'/>" width="25" height="25" border="none"/>
                        </s:else>
                    </td>
                    <td align="center">
                        <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                            <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                 border="none"/></s:a>
                    </td>
                    <td align="center">
                        <s:url action="eprAssignmentDelete.do" id="deleteSelected">
                            <s:param name="assignmentUKey" value="assignmentUKey"/>
                        </s:url>
                        <s:a href="%{deleteSelected}" title="%{getText('deleteToolTip.label')}"><img
                                src="<s:url value='/images/delete.gif'/>" width="25" height="25"
                                border="none" onclick="javascript:return deleteWarning('warning')"/></s:a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </s:if>
    </table>
    <div id="assignments" align="right">
        <s:url action="eprAssignmentAddDirect.do" id="x">
            <s:param value="%{registrar.pin}" name="registrarPin"/>
            <s:param value="3" name="directAssignment"/>
            <s:param value="1" name="assignmentType"/>
        </s:url>
        <s:a href="%{x}" id="addText"><s:property
                value="%{getText('assignment.add.new.assignment.birth.death')}"/></s:a>
    </div>
    <div id="assignments2" align="right">
        <s:url action="eprAssignmentAddDirect.do" id="x">
            <s:param value="%{registrar.pin}" name="registrarPin"/>
            <s:param value="3" name="directAssignment"/>
            <s:param value="2" name="assignmentType"/>
        </s:url>
        <s:a href="%{x}" id="addText"><s:property value="%{getText('assignment.add.new.assignment.marriage')}"/></s:a>
    </div>

</fieldset>

<s:hidden id="invalideData" value="%{getText('invalide.data')}"/>
<s:hidden id="cannotNull" value="%{getText('cannot.null')}"/>
<s:hidden id="pin" value="%{getText('registrar.pin')}"/>
<s:hidden id="nic" value="%{getText('registrar.nic')}"/>
<s:hidden id="dob" value="%{getText('registrar.dateofbirth')}"/>
<s:hidden id="addressError" value="%{getText('registrar.address')}"/>
<s:hidden id="phone" value="%{getText('registrar.phone')}"/>
<s:hidden id="email" value="%{getText('registrar.email')}"/>
<s:hidden id="nameOfficialError" value="%{getText('registrar.full.name.officelaLang')}"/>
<s:hidden id="nameEnglishError" value="%{getText('registrar.full.name.english')}"/>
<s:hidden id="empty" value="%{getText('field.emptry')}"/>
<s:hidden id="warning" value="Do you really want to delete this assignment ?"/>


