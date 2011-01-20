<%@ taglib prefix="s" uri="/struts-tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>

<!--//todo not completed JSON -->
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="../js/validate.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<s:hidden id="error5" value="%{getText('p1.serial.text')}"/>
<s:hidden id="error13" value="%{getText('p1.invalide.inputType')}"/>

<script>
    $(document).ready(function() {
        $("#birth-confirmation-search").tabs();
    });
</script>

<script>
    $(document).ready(function() {
        $('#search-list-table').dataTable({
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
        })
    });
    var errormsg = "";
    var counter = 0;

    function validateForm() {
        var bdfSerialNumber = document.getElementById('bdfSerialNoId1').value;
        var conSerial = document.getElementById('bdfSerialNoId2').value;
        var valueArray = new Array(bdfSerialNumber, conSerial);

        for (var i = 0; i < valueArray.length; i++) {
            var c = valueArray[i];
            if (c != "") {
                counter++
            }
        }
        if (counter != 1) {
            errormsg = errormsg + document.getElementById('oneMethodErr').value;
        }

        var domObject = document.getElementById('bdfSerialNoId1');
        if (domObject.value != "") {
            validateSerialNo(domObject, 'invalideDateErr', 'bdfSerialNumber');
        }
        domObject = document.getElementById('bdfSerialNoId2');
        if (domObject.value != "") {
            validateSerialNo(domObject, 'invalideDateErr', 'confSerialNumber');
        }
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

    function initPage() {
    }
</script>
<div id="birth-confirmation-search" style="font-size:10pt;">
    <s:actionerror cssStyle="color:red;font-size:10pt"/>

    <ul>
        <li><a href="#fragment-1"><span> <s:label name="registrationSearch"
                                                  value="%{getText('registrationSerchTab.label')}"/></span></a>
        </li>
        <li><a href="#fragment-2"><span><s:label name="confirmationSearch"
                                                 value="%{getText('confirmationSearchTab.label')}"/></span></a></li>
    </ul>
    <s:form action="eprBDFSearchBySerialNo.do" name="birthConfirmationSearchForm" id="search-bdf-form"
            onsubmit="javascript:return validateForm()" method="post">


    <div id="fragment-1">

        <table class="search-option-table">
            <caption></caption>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td><s:label name="declarationSearialNumber"
                             value="%{getText('searchDeclarationSearial.label')}"/></td>
                <td><s:textfield name="serialNo" id="bdfSerialNoId1" maxLength="10" value=""/></td>
                <td><s:label name="district" value="%{getText('district.label')}"/></td>
                <td>
                    <s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                              value="birthDistrictId" cssStyle="width:240px;float:left;"/>
                </td>
            </tr>
            <tr>
                <td><s:label name="division" value="%{getText('select_DS_division.label')}"/></td>
                <td>
                    <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                              cssStyle="float:left;  width:240px;"/>
                </td>
                <td><s:label name="bdDivision" value="%{getText('select_BD_division.label')}"/></td>
                <td>
                    <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}"
                              list="bdDivisionList" cssStyle=" width:240px;float:left;"
                              headerValue="%{getText('all.divisions.label')}" headerKey="0"/>
                </td>
            </tr>
            <tr>
                <td colspan="3"></td>
                <td>
                    <div class="form-submit"><s:submit value="%{getText('bdfSearch.button')}" name="search"
                                                       cssStyle="float:left;margin :5px 63px 0 5px;"/></div>
                </td>
            </tr>
            </tbody>
        </table>


        </s:form>
    </div>
    <%--   <br/>--%>
    <div id="fragment-2">
        <s:form action="eprBDFSearchByIdUKey.do" method="post" onsubmit="javascript:return validateForm()">
            <table class="search-option-table">

                <tr>
                    <td width="250px"><s:label name="confirmationSearch"
                                               value="%{getText('searchConfirmationSerial.label')}"/></td>
                    <td width="250px"><s:textfield name="idUKey" id="bdfSerialNoId2" maxLength="10" value=""/></td>
                    <td width="250px">
                        <div class="form-submit"><s:submit value="%{getText('bdfSearch.button')}"
                                                           name="search"
                                                           cssStyle="float:left;margin :5px 30px 7px 5px;"/></div>
                    </td>
                    <td width="750px"></td>
                </tr>
            </table>

        </s:form>

    </div>
</div>
<br/>

<div>
    <s:actionmessage cssStyle="color:blue;font-size:10pt"/>
    <s:if test="#request.bdf != null || #request.searchResultList.size>0">
        <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
            <legend>
                <s:label value="%{getText('searchResult.label')}"/>
            </legend>
            <table id="search-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
                <thead>
                <tr>
                    <s:if test="searchResultList.size>0">
                        <%--<th></th>--%>
                    </s:if>
                    <th width="20px"><s:label value="%{getText('division.label')}"/></th>
                    <th width="100px"><s:label name="serial" value="%{getText('serial.label')}"/></th>
                    <th><s:label name="childNamelbl" value="%{getText('childName.label')}"/></th>
                    <th><s:label name="childGenderlbl" value="%{getText('childGender.label')}"/></th>
                    <th width="40px"><s:label name="live" value="%{getText('live.label')}"/></th>
                    <th><s:label name="statuslbl" value="%{getText('status.label')}"/></th>
                    <th width="20px"></th>
                    <th width="20px"></th>
                </tr>
                </thead>
                <tbody>
                <s:if test="%{#request.bdf != null}">
                    <tr>
                        <td><s:property value="register.birthDivision.bdDivisionUKey"/></td>
                        <td><s:property value="%{#request.bdf.register.bdfSerialNo}"/></td>
                        <td align="center">
                            <s:label name="childName"
                                     value="%{#request.bdf.child.getChildFullNameOfficialLangToLength(50)}"/>
                        </td>
                        <td align="center">
                            <s:if test="%{#request.bdf.child.childGender == 0}">
                                <s:label value="%{getText('male.label')}"/>
                            </s:if>
                            <s:elseif test="%{#request.bdf.child.childGender == 1}">
                                <s:label value="%{getText('female.label')}"/>
                            </s:elseif>
                            <s:elseif test="%{#request.bdf.child.childGender == 2}">
                                <s:label value="%{getText('unknown.label')}"/>
                            </s:elseif>
                        </td>
                        <td align="center">
                            <s:if test="%{#request.bdf.register.birthType.ordinal() != 0}">
                                <s:label value="%{getText('yes.label')}"/>
                            </s:if>
                            <s:elseif test="%{#request.bdf.register.birthType.ordinal() == 0}">
                                <s:label value="%{getText('no.label')}"/>
                            </s:elseif>
                        </td>
                        <td align="center">
                            <s:label value="%{getText(status)}"/></td>
                        <s:url id="viewSelected" action="eprViewBDFInNonEditableMode.do">
                            <s:param name="bdId" value="%{#request.bdf.idUKey}"/>
                        </s:url>
                        <td align="center">
                            <s:if test="bdf.register.status.ordinal() !=4">
                                <s:a href="%{viewSelected}" title="%{getText('view.label')}">
                                    <img src="<s:url value='/images/view.gif'/>" width="25" height="25" border="none"/>
                                </s:a>
                            </s:if>
                        </td>
                            <%--TODO still implementing--%>
                        <s:url id="editSelected" action="eprBirthRegistrationInit.do">
                            <s:param name="bdId" value="%{#request.bdf.idUKey}"/>
                        </s:url>
                        <td align="center">
                            <s:if test="%{#request.bdf.register.status.ordinal() == 0}">
                                <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                                    <img src="<s:url value='/images/edit.png'/>" width="25" height="25" border="none"/></s:a>
                            </s:if>
                        </td>
                    </tr>
                </s:if>
                <s:elseif test="searchResultList.size>0">
                    <s:iterator status="searchStatus" value="searchResultList" id="searchId">
                        <tr class="<s:if test="#searchStatus.odd == true">odd</s:if><s:else>even</s:else>">
                                <%--<td class="table-row-index"><s:property value="%{#searchStatus.count}"/></td>--%>
                            <td><s:property value="register.birthDivision.bdDivisionUKey"/></td>
                            <td><s:property value="register.bdfSerialNo"/></td>
                            <td><s:property value="%{child.getChildFullNameOfficialLangToLength(50)}"/></td>
                            <td align="center">
                                <s:if test="child.childGender == 0">
                                    <s:label value="%{getText('male.label')}"/>
                                </s:if>
                                <s:elseif test="child.childGender == 1">
                                    <s:label value="%{getText('female.label')}"/>
                                </s:elseif>
                                <s:elseif test="child.childGender == 2">
                                    <s:label value="%{getText('unknown.label')}"/>
                                </s:elseif>

                            <td align="center">
                                <s:if test="register.birthType.ordinal() != 0">
                                    <s:label value="%{getText('yes.label')}"/>
                                </s:if>
                                <s:elseif test="register.birthType.ordinal() == 0">
                                    <s:label value="%{getText('no.label')}"/>
                                </s:elseif>
                            </td>
                            <s:set value="getRegister().getStatus()" name="status"/>
                            <td><s:label value="%{getText(#status)}"/></td>
                            <s:url id="viewSelected" action="eprViewBDFInNonEditableMode.do">
                                <s:param name="bdId" value="idUKey"/>
                            </s:url>
                            <td align="center">
                                <s:if test="register.status.ordinal() !=4">
                                    <s:a href="%{viewSelected}" title="%{getText('view.label')}">
                                        <img src="<s:url value='/images/view_1.gif'/>" width="25" height="25"
                                             border="none"/>
                                    </s:a>
                                </s:if>
                            </td>

                            <td align="center">
                                <s:if test="register.status.ordinal() ==0">
                                    <s:url id="editSelected" action="eprBirthRegistrationInit.do">
                                        <s:param name="bdId" value="idUKey"/>
                                    </s:url>
                                    <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                                        <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                             border="none"/></s:a>
                                </s:if>
                                <s:elseif test="register.status.ordinal() ==2 || register.status.ordinal()==5">
                                    <s:url id="editSelected" action="eprBirthConfirmationInit.do">
                                        <s:param name="bdId" value="idUKey"/>
                                    </s:url>
                                    <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                                        <img src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                             border="none"/></s:a>
                                </s:elseif>

                            </td>
                        </tr>
                    </s:iterator>
                </s:elseif>
                </tbody>
            </table>
        </fieldset>
    </s:if>
    
    <s:hidden id="oneMethodErr" value="%{getText('err.use.one,method.to.search')}"/>
    <s:hidden id="invalideDateErr" value="%{getText('err.invalide.data')}"/>
    <s:hidden id="bdfSerialNumber" value="%{getText('field.bdf.serial')}"/>
    <s:hidden id="confSerialNumber" value="%{getText('conf.serial.number')}"/>

</div>
