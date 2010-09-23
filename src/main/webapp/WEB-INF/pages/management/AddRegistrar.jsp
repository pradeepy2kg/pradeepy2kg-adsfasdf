<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>

<script type="text/javascript">

    $(function() {
        $("#dateOfBirthDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2040-12-31'
        });
    });


    var errormsg = "";

    function validate() {

        var nameOfficialLang = document.getElementById('registrarNameInOfficelaLang');
        var nameEnglish = document.getElementById('registrarNameInEnglish');
        var pin = document.getElementById('registrarPin');
        var nic = document.getElementById('registrarNIC');
        var gender = document.getElementById('registrarGender');
        var dob = document.getElementById('dateOfBirthDatePicker');
        var address = document.getElementById('registrarAddress');
        var phone = document.getElementById('registrarPhone');
        var email = document.getElementById('registrarEmail');
        var lang = document.getElementById('prefLanguage');

        var check = document.getElementById('skipValidationId');
        var returnval = true;

        //valdiate numbers
        if ((check.checked && !isFieldEmpty(pin)) || (!check.checked)) {
            isNumeric(pin.value, "invalideData", "pin")
            //validate PIN or NIC
            validatePINorNIC(pin, "invalideData", "pin")
        }
        if ((check.checked && !isFieldEmpty(nic)) || (!check.checked)) {
            validatePINorNIC(nic, "invalideData", "nic")
        }
        if ((check.checked && !isFieldEmpty(phone)) || (!check.checked)) {
            //validate phone number
            validatePhoneNo(phone, "invalideData", "phone")
        }
        if ((check.checked && !isFieldEmpty(email)) || (!check.checked)) {
            //validate email
            validateEmail(email, "invalideData", "email")
        }
        /*        //validate date of birth
         isDate(dob, "invalideData", "email")*/
        /*todo validate compulsory fields*/
        isEmpty(nameOfficialLang, "nameOfficial", "cannotNull")

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }
</script>
<style type="text/css">
    .add-registrar-body {
        background: #ffffff;
    }

</style>
<s:actionerror cssStyle="color:red;"/>
<s:actionmessage cssStyle="color:blue;"/>

<div class="add-registrar-body">
    <div class="block">
        <s:form action="eprRegistrarsAdd.do" method="post" onsubmit="javascript:return validate()">
        <table border="0" style="width: 100%" cellpadding="5" cellspacing="5">
            <caption></caption>
            <col width="400px"/>
            <col width="1000px"/>
            <tbody>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.full.name.officelaLang')}"/></td>
                <td align="left"><s:textfield id="registrarNameInOfficelaLang" cssStyle="width:100%"
                                              name="registrar.fullNameInOfficialLanguage"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.full.name.english')}"/></td>
                <td align="left"><s:textfield id="registrarNameInEnglish" cssStyle="width:100%"
                                              name="registrar.fullNameInEnglishLanguage"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.pin')}"/></td>
                <td align="left"><s:textfield id="registrarPin" name="registrar.pin"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.nic')}"/></td>
                <td align="left"><s:textfield id="registrarNIC" name="registrar.nic"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.gender')}"/></td>
                <td align="left"><s:select
                        list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                        name="registrar.gender" cssStyle="width:190px;" id="registrarGender"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.dateofbirth')}"/></td>
                <td align="left"><s:textfield name="registrar.dateOfBirth" id="dateOfBirthDatePicker"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.address')}"/></td>
                <td align="left"><s:textarea id="registrarAddress" cssStyle="width:100%"
                                             name="registrar.currentAddress"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.phone')}"/></td>
                <td align="left"><s:textfield id="registrarPhone" name="registrar.phoneNo"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.email')}"/></td>
                <td align="left"><s:textfield id="registrarEmail" name="registrar.emailAddress"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.prefLang')}"/></td>
                <td align="left"><s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'Tamil'}"
                                           name="registrar.preferredLanguage"
                                           cssStyle="width:190px;"/></td>
            </tr>
            </tbody>
        </table>
    </div>

    <table border="0" style="width: 100%" cellpadding="5" cellspacing="5">
        <caption></caption>
        <col width="400px"/>
        <col width="1000px"/>
        <tbody>
        <tr>
            <td><s:property value="%{getText('label.skip.validation')}"/><s:checkbox id="skipValidationId"
                                                                                     name="skipValidationName"/></td>
            <td align="right">
                <div id="addNew_button" class="button">
                    <s:submit name="refresh" value="%{getText(label.add.registrar)}"/>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
    <s:hidden name="page" value="1"/>
    </s:form>
</div>

<s:hidden id="invalideData" value="%{getText('invalide.data')}"/>
<s:hidden id="cannotNull" value="%{getText('cannot.null')}"/>
<s:hidden id="pin" value="%{getText('filed.pin')}"/>
<s:hidden id="nic" value="%{getText('field.nic')}"/>
<s:hidden id="dob" value="%{getText('field.dob')}"/>
<s:hidden id="address" value="%{getText('field.address')}"/>
<s:hidden id="phone" value="%{getText('field.phone')}"/>
<s:hidden id="email" value="%{getText('field.email')}"/>
<s:hidden id="nameOfficial" value="%{getText('field.nameOfficial')}"/>
