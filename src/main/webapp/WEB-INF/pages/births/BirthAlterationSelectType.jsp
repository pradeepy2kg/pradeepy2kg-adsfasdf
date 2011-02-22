<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript">
    $(function() {
        $('select#birthDistrictId').bind('change', function(evt1) {
            var id = $("select#birthDistrictId").attr("value");
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
        $('select#childBirthDistrictId').bind('change', function(evt1) {
            var id = $("select#childBirthDistrictId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#childDsDivisionId").html(options1);

                        var options2 = '';
                        var bd = data.bdDivisionList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#childBirthDivisionId").html(options2);
                    });
        });

        $('select#childDsDivisionId').bind('change', function(evt2) {
            var id = $("select#childDsDivisionId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#childBirthDivisionId").html(options);
                    });
        });
    });

    $(document).ready(function() {
        $("#tabs").tabs();
    });

    var errormsg = "";
    var counter = 0;

    function validateForm() {

        var pin = document.getElementById('idNumberSearch').value;
        var serial = document.getElementById('bdfSerialNoIdSearch').value;
        var certifcateNumber = document.getElementById('bdfSearchSerialNoId').value;
        var valueArray = new Array(pin, serial, certifcateNumber);

        for (var i = 0; i < valueArray.length; i++) {
            var c = valueArray[i];
            if (c != "") {
                counter++
            }
        }
        if (counter > 1) {
            errormsg = errormsg + document.getElementById('oneMethodErr').value;
        }
        if (counter == 0) {
            return false;
        }

        //validate   number fields
        isNumeric(certifcateNumber, 'invalideDateErr', 'certificateNumberFi')
        isNumeric(serial, 'invalideDateErr', 'serialNumnerFi')
        isNumeric(pin, 'invalideDateErr', 'pinNumberFi')

        if (errormsg != "") {
            alert(errormsg)
            errormsg = "";
            counter = 0;
            return false;
        }
        else {
            return true;
        }
        return false;
    }

</script>
<div id="birth-confirmation-search">
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage cssStyle="color:blue;font-size:10pt"/>
<s:form action="eprBirthAlterationSearch.do" onsubmit="javascript:return validateForm()">
    <div id="tabs" style="font-size:10pt;">
        <ul>
            <li><a href="#fragment-1"><span> <s:label
                    value="%{getText('registrationSerchTab3.label')}"/></span></a></li>
            <li><a href="#fragment-2"><span><s:label
                    value="%{getText('registrationSerchTab2.label')}"/></span></a></li>
            <li><a href="#fragment-3"><span><s:label
                    value="%{getText('registrationSerchTab1.label')}"/></span></a></li>
        </ul>
        <table class="search-alteration-option-table">
            <tr>
                <td width="135px"><s:label value="%{getText('sectionOfTheAct.lable')}"
                                           cssStyle="margin-left:20px;"/></td>
                <td width="500px"><s:select
                        list="#@java.util.HashMap@{'TYPE_27':'27','TYPE_52_1_A':'52(1)','TYPE_27A':'27 (A)'}"
                        name="alterationType" cssStyle="width:230px;margin-left:175px;"/></td>
            </tr>
        </table>
        <div id="fragment-1">
            <table class="search-alteration-option-table">
                <tr>
                    <td width="135px"><s:label name="confirmationSearch"
                                               value="%{getText('certificateNumber.lable')}"/></td>
                    <td width="500px"><s:textfield name="birthCertificateNumber" id="bdfSerialNoIdSearch" maxLength="10"
                                                   onkeypress="return isNumberKey(event)"
                                                   cssStyle="margin-left:154px;" value=""/></td>
                </tr>
            </table>
        </div>
        <div id="fragment-2">
            <table class="search-alteration-option-table">
                <tr>
                    <td width="135px"><s:label name="confirmationSearch"
                                               value="%{getText('idNumber.lable')}"/></td>
                    <td width="200px"><s:textfield name="nicOrPin" id="idNumberSearch" maxLength="12" value=""/></td>

                </tr>
            </table>
        </div>
        <div id="fragment-3">
            <table class="search-option-table">
                <caption></caption>
                <col/>
                <col/>
                <col/>
                <col/>
                <col/>
                <tbody>
                <tr>
                    <td><s:label value="%{getText('searchDeclarationSearial.label')}"/></td>
                    <td><s:textfield name="serialNo" id="bdfSearchSerialNoId" value="" maxLength="10"
                                     onkeypress="return isNumberKey(event)"/></td>
                    <td><s:label value="%{getText('district.label')}"/></td>
                    <td>
                        <s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                                  value="birthDistrictId" cssStyle="width:240px;float:left;"/>
                    </td>
                </tr>
                <tr>
                    <td><s:label name="division" value="%{getText('select_DS_division.label')}"/></td>
                    <td>
                        <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  value="%{dsDivisionId}"
                                  cssStyle="float:left;  width:240px;"/>
                    </td>
                    <td><s:label value="%{getText('select_BD_division.label')}"/></td>
                    <td>
                        <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}"
                                  list="bdDivisionList" cssStyle=" width:240px;float:left;"
                                  headerValue="%{getText('all.divisions.label')}" headerKey="0"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br/>

    <div class="form-submit"><s:submit value="%{getText('bdfSearch.button')}"
                                       name="search"/></div>
    </div>
</s:form>
<s:hidden id="oneMethodErr" value="%{getText('err.use.one,method.to.search')}"/>
<s:hidden id="invalideDateErr" value="%{getText('err.invalide.data')}"/>
<s:hidden id="serialNumnerFi" value="%{getText('field.serial.number')}"/>
<s:hidden id="pinNumberFi" value="%{getText('field.pin.number')}"/>
<s:hidden id="certificateNumberFi" value="%{getText('field.certificate.number')}"/>
