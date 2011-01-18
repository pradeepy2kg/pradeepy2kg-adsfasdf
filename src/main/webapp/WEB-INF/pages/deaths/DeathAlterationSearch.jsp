<%--@author amith jayasekara--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script>
    $(document).ready(function() {
        $("#tabs").tabs();
    });
</script>
<style type="text/css">
    #serialNumber {
        width: 232px
    }
</style>


<script type="text/javascript">

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
                        $("select#divisionId").html(options2);
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
                        $("select#divisionId").html(options);
                    });
        });
    });

    var errormsg = "";
    var counter = 0;

    function validateForm() {

        var pin = document.getElementById('identificationNumber').value;
        var serial = document.getElementById('serialNumber').value;
        var certifcateNumber = document.getElementById('idUKey').value;
        var valueArray = new Array(pin, serial, certifcateNumber);

        for (var i = 0; i < valueArray.length; i++) {
            var c = valueArray[i];
            if (c != "") {
                counter++
            }
        }
        if (counter != 1) {
            errormsg = errormsg + document.getElementById('oneMethodErr').value;
        }

        //validate   number fields
        isNumeric(certifcateNumber, 'invalideDateErr', 'certificateNumberFi')
        validateSerialNo(serial, 'invalideDateErr', 'serialNumnerFi')
        validatePINorNIC(pin, 'invalideDateErr', 'pinNumberFi')

        if (errormsg != "") {
            alert(errormsg)
            errormsg = "";
            counter = 0;
            return false;
        }
        else {
            return true;
        }
        errormsg = "";
        counter = 0;
        return false;
    }

    function initPage() {
    }

</script>

<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage/>
<s:form method="post" action="eprDeathAlterationPageLoad.do" onsubmit="javascript:return validateForm()">
    <%--section search by death certificate number--%>
    <div id="tabs" style="font-size:10pt;">
        <ul>
            <li><a href="#fragment-1"><span> <s:label
                    value="%{getText('label.tab.search.by.certificate.number')}"/></span></a></li>
            <li><a href="#fragment-2"><span><s:label
                    value="%{getText('label.search.by.death.person.pin')}"/></span></a></li>
            <li><a href="#fragment-3"><span><s:label
                    value="%{getText('label.tab.search.by.serial.number')}"/></span></a></li>
        </ul>

        <div id="fragment-1">

            <table cellpadding="2px" cellspacing="0">
                <caption></caption>
                <col width="265px">
                <col width="500px">
                <tbody>
                <tr>
                    <td align="left">
                        <s:label value="%{getText('label.certificate.number')}"/>
                    </td>
                    <td align="left">
                        <s:textfield name="idUKey" id="idUKey" value=""/>
                    </td>
                </tr>
                </tbody>
            </table>

        </div>
        <div id="fragment-2">
                <%--section search by identification number--%>

            <table cellpadding="2px" cellspacing="0">
                <caption></caption>
                <col width="265px">
                <col width="500px">
                <tbody>
                <tr>
                    <td align="left">
                        <s:label value="%{getText('label.death.person.pin')}"/>
                    </td>
                    <td align="left">
                        <s:textfield name="pin" id="identificationNumber" maxLength="10" value=""/>
                    </td>
                </tr>
                </tbody>
            </table>

        </div>
        <div id="fragment-3">
                <%--section search by serial number and bd/death division--%>

            <table>
                <caption></caption>
                <col width="250px">
                <col width="250px">
                <col width="50px">
                <col width="250px">
                <col width="250px">
                <tr>
                    <td>
                        <s:label value="%{getText('label.district')}"/>
                    </td>
                    <td>
                        <s:select id="districtId" name="districtUKey" list="districtList" value="%{districtUKey}"
                                  cssStyle="width:98.5%; width:240px;"/>
                    </td>
                    <td></td>
                    <td>
                        <s:label value="%{getText('label.dsDivision')}"/>
                    </td>
                    <td>
                        <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{divisionUKey}"
                                  cssStyle="float:left;  width:240px;"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:label value="%{getText('label.bdDivision')}"/>
                    </td>
                    <td>
                        <s:select id="divisionId" name="divisionUKey" value="%{divisionUKey}"
                                  list="bdDivisionList"
                                  cssStyle="float:left;  width:240px;"/>
                    </td>
                    <td></td>
                    <td>
                        <s:label value="%{getText('label.serialNumber')}"/>
                    </td>
                    <td>
                        <s:textfield name="serialNumber" id="serialNumber" maxLength="10" value=""/>
                    </td>
                </tr>
            </table>

        </div>
    </div>
    <%--section search button--%>
    <div class="form-submit">
        <s:submit type="submit" value="%{getText('button.search')}" id="searchButton"/>
    </div>
    <s:hidden id="oneMethodErr" value="%{getText('err.use.one,method.to.search')}"/>
    <s:hidden id="invalideDateErr" value="%{getText('err.invalide.data')}"/>
    <s:hidden id="serialNumnerFi" value="%{getText('field.serial.number')}"/>
    <s:hidden id="pinNumberFi" value="%{getText('field.pin.number')}"/>
    <s:hidden id="certificateNumberFi" value="%{getText('field.certificate.number')}"/>
</s:form>