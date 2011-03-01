<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/media/css/ColVis.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>


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

        if (!check.checked) {
            /*     if (isFieldEmpty(pin)) {
             isEmpty(pin, document.getElementById('emptry').value, "pin")
             }*/
            if (isFieldEmpty(nic)) {
                isEmpty(nic, document.getElementById('nic').value, 'emptry')
            }
            if (isFieldEmpty(phone)) {
                isEmpty(phone, document.getElementById('phone').value, 'emptry')
            }
            if (isFieldEmpty(email)) {
                isEmpty(email, document.getElementById('email').value, 'emptry')
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
        /*        //validate date of birth
         isDate(dob, "invalideData", "email")*/
        /*todo validate compulsory fields*/
        isMandatoryFieldsEmpty(nameOfficialLang, document.getElementById('nameOfficialError').value, "cannotNull")
        isMandatoryFieldsEmpty(nameEnglish, document.getElementById('nameEnglishError').value, "cannotNull")
        isMandatoryFieldsEmpty(address, document.getElementById('addressError').value, "cannotNull")
        isMandatoryFieldsEmpty(pin, document.getElementById('pin').value, "cannotNull")

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

    function initPage() {
    }
   
    $(function() {
        $('img#registrar_lookup').bind('click', function(evt1) {
            var id1 = $("input#registrarPin").attr("value");
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("input#registrarNameInOfficelaLang").val(data1.fullNameInOfficialLanguage);
                        $("input#registrarNameInEnglish").val(data1.fullNameInEnglishLanguage);
                        $("input#registrarNIC").val(data1.nic);
                        $("select#registrarGender").val(data1.gender);
                        $("input#dateOfBirthDatePicker").val(data1.dateOfBirth);
                        $("textarea#registrarAddress").val(data1.address);
                        $("input#registrarPhone").val(data1.phoneNumber);
                        $("input#registrarEmail").val(data1.email);
                    });
        });
    });
</script>
<style type="text/css">
    .add-registrar-body {
        margin: 10px;
        padding: 5px;
        border: #c3dcee 2px solid;
    }
</style>
<s:actionerror cssStyle="color:red;"/>
<s:actionmessage cssStyle="color:blue;;font-size:10pt"/>

<div class="add-registrar-body">
    <div class="block">
        <s:form action="eprRegistrarsAdd.do" method="post" onsubmit="javascript:return validate()">
        <table width="100%" cellpadding="5" cellspacing="5">
            <caption></caption>
            <col width="400px"/>
            <col width="1000px"/>
            <tbody>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.full.name.officelaLang')}"/><s:label value="*"
                                                                                                              cssStyle="color:red;font-size:14pt;"/></td>
                <td align="left"><s:textfield id="registrarNameInOfficelaLang" cssStyle="width:100%"
                                              name="registrar.fullNameInOfficialLanguage"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.full.name.english')}"/><s:label value="*"
                                                                                                         cssStyle="color:red;font-size:14pt;"/></td>
                <td align="left"><s:textfield id="registrarNameInEnglish" cssStyle="width:100%"
                                              name="registrar.fullNameInEnglishLanguage"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.pin')}"/><s:label value="*"
                                                                                           cssStyle="color:red;font-size:14pt;"/></td>
                <td align="left"><s:textfield id="registrarPin" name="registrar.pin" maxLength="12"/>
                    <img src="<s:url value='/images/search-father.png' />"
                         style="vertical-align:middle;" id="registrar_lookup"/>
                </td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.nic')}"/></td>
                <td align="left"><s:textfield id="registrarNIC" name="registrar.nic" maxLength="10"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.gender')}"/></td>
                <td align="left"><s:select
                        list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                        name="registrar.gender" cssStyle="width:190px;" id="registrarGender"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.dateofbirth')}"/></td>
                <td align="left"><s:textfield name="registrar.dateOfBirth" id="dateOfBirthDatePicker"
                                              maxLength="10"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.address')}"/><s:label value="*"
                                                                                               cssStyle="color:red;font-size:14pt;"/></td>
                <td align="left"><s:textarea id="registrarAddress" cssStyle="width:100%"
                                             name="registrar.currentAddress"/></td>
            </tr>
            <tr>
                <td align="left"><s:property value="%{getText('registrar.phone')}"/></td>
                <td align="left"><s:textfield id="registrarPhone" name="registrar.phoneNo" maxLength="10"/></td>
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
            <td><s:checkbox id="skipValidationId" name="skipValidationName"/><s:property
                    value="%{getText('label.skip.validation')}"/></td>
            <td align="right">
                <div id="addNew_button" class="button">
                    <s:submit name="refresh" value="%{getText('label.add.registrar')}"/>
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
<s:hidden id="pin" value="%{getText('registrar.pin')}"/>
<s:hidden id="nic" value="%{getText('registrar.nic')}"/>
<s:hidden id="dob" value="%{getText('registrar.dateofbirth')}"/>
<s:hidden id="addressError" value="%{getText('registrar.address')}"/>
<s:hidden id="phone" value="%{getText('registrar.phone')}"/>
<s:hidden id="email" value="%{getText('registrar.email')}"/>
<s:hidden id="nameOfficialError" value="%{getText('registrar.full.name.officelaLang')}"/>
<s:hidden id="nameEnglishError" value="%{getText('registrar.full.name.english')}"/>
<s:hidden id="emptry" value="%{getText('field.emptry')}"/>
