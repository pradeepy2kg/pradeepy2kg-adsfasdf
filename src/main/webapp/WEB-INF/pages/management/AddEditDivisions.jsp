<%-- @author Tharanga Punchihewa --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/media/css/TableTools.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/ZeroClipboard.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/TableTools.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script>
    $(document).ready(function() {
        $('#users-list-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "aaSorting": [
                [2,'asc']
            ],
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"

        });
    });
</script>

<style type="text/css">
    input {
        text-transform: none;
    }
</style>

<script type="text/javascript">
    // mode 1 = passing District, will return DS list
    // mode 2 = passing DsDivision, will return BD list
    // any other = passing district, will return DS list and the BD list for the first DS

    $(function() {
        $('select#adddivisionDistrictId').bind('change', function(evt1) {
            var id = $("select#adddivisionDistrictId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:3},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#adddivisionDsDivisionId").html(options1);
                    });
        });
        $('select#addMrDivisionDistrictId').bind('change', function(evt1) {
            var id = $("select#addMrDivisionDistrictId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:3},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#addMrDivisionDsDivisionId").html(options1);
                    });
        });
        $('select#addLocDivisionDistrictId').bind('change', function(evt1) {
            var id = $("select#addLocDivisionDistrictId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:3},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#addLocDivisionDsDivisionId").html(options1);
                    });
        });


        $('select#addGnDivisionDistrictId').bind('change', function(evt1) {
            var id = $("select#addGnDivisionDistrictId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:3},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#addGnDivisionDsDivisionId").html(options1);
                    });
        });
    });

    function validate() {
        var errormsg = "";
        var domObject;
        var pageType;
        var pageName;
        pageType = document.getElementById("checkPage").value;
        if (pageType == 1)  pageName = "District";
        if (pageType == 2)  pageName = "Divisional Secretary Division";
        if (pageType == 3)  pageName = "Division";
        if (pageType == 4)  pageName = "MR Division";
        if (pageType == 7)  pageName = "Grama Niladhari Division";
        domObject = document.getElementById("id");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Please Enter The  Id of " + pageName + " \n";
        }
        else {
            if (pageType != 6) {
                var reg = /^([0-9]*)$/;
                if (reg.test(domObject.value.trim()) == false) {
                    errormsg = errormsg + pageName + " Id Is Invalid \n";
                }
            }
        }
        domObject = document.getElementById("enName");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Please Enter The English Name of " + pageName + " \n";
        }
        domObject = document.getElementById("siName");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Please Enter The Sinhala Name of " + pageName + " \n";
        }
        domObject = document.getElementById("taName");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Please Enter The Tamil Name of " + pageName + " \n";
        }

        var out = checkActiveFieldsForSyntaxErrors('editDivisions');
        if(out != ""){
            errormsg = errormsg + out;
        }

        if (errormsg != "") {
            alert(errormsg);
            return false;
        }
        return true;
    }

    function initPage() {
    }
</script>

<div id="add-inactive-divisions-outer">
<s:if test="!(pageType == 1 || pageType==2 || pageType == 3 || pageType==4 || pageType==5 || pageType==6||pageType==7)">
<table style="border:1px;width:100%">
<tr>
    <td style="width:50%">
        <fieldset style="border:2px solid #c3dcee;width:96%;margin-top:8px;">
            <s:form name="editDsDivisions" action="eprInitDivisionList.do" method="POST">
                <table style="border:none;margin-top:15px;text-align:center;" align="center">
                    <tr style="height:15px;">
                        <td style="font-size:13pt;" colspan="4">District List</td>
                    </tr>
                    <tr style="height:50px;">
                        <td colspan="4"></td>
                    </tr>
                    <tr>
                        <td style="font-size:10pt;text-align:left;padding-left:25px;" colspan="3">
                            * Add New District <br>
                            * Active District <br>
                            * Inactive District
                        </td>
                        <td style="width:25%">
                            <div class="form-submit">
                                <s:hidden name="pageType" value="1"/>
                                <s:submit value="District List" cssStyle="margin-top:10px;font-size:10pt;"/>
                            </div>
                        </td>
                    </tr>
                </table>
            </s:form>
        </fieldset>
    </td>
    <td style="width:50%">
        <fieldset style="border:2px solid #c3dcee;width:96%;margin-top:8px;">
            <table style="border:none;margin-top:15px;text-align:center;" align="center">
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col/>
                <tbody>
                <tr style="height:15px;">
                    <td style="font-size:13pt;" colspan="4">Divisional Secretary Division List</td>

                </tr>
                <s:form name="editDsDivisions" action="eprInitDivisionList.do" method="POST">
                    <tr style="height:50px;">
                        <td colspan="4">
                            <table class="add-inactive-divisions-outer-table" align="center" cellspacing="0">
                                <col style="width:30%"/>
                                <col style="width:70%"/>
                                <col/>
                                <tbody>
                                <tr>

                                    <td>District</td>
                                    <td><s:select id="addDsDivisionDistrictId" name="userDistrictId"
                                                  list="districtList"/></td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td style="font-size:10pt;text-align:left;padding-left:25px;" colspan="3">
                            * Add New Divisional Secretariat <br>
                            * Active Divisional Secretariat <br>
                            * Inactive Divisional Secretariat
                        </td>
                        <td>
                            <div class="form-submit">
                                <s:hidden name="pageType" value="2"/>
                                <s:submit value="DsDivision List" cssStyle="margin-top:10px;"/>
                            </div>
                        </td>
                    </tr>
                </s:form>
                </tbody>
            </table>
        </fieldset>
    </td>
</tr>
<tr>
    <td>
        <fieldset style="border:2px solid #c3dcee;float:left;width:96%;">
            <table style="border:none;margin-top:15px;text-align:center;margin-bottom:10px;" align="center">
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col/>
                <tbody>
                <tr>
                    <td style="font-size:13pt;" colspan="4">Birth Death Registration Division List</td>
                </tr>
                <tr>
                    <td colspan="4">
                        <s:form name="editDivisions" action="eprInitDivisionList.do" method="POST">
                        <table class="add-inactive-divisions-outer-table" align="center" cellspacing="0">
                            <col style="width:30%"/>
                            <col style="width:70%"/>
                            <col/>
                            <tbody>
                            <tr>

                                <td>District</td>
                                <td><s:select id="adddivisionDistrictId" name="userDistrictId"
                                              list="districtList"/></td>
                            </tr>
                            <tr>
                                <td>Divisional Secretary Division</td>
                                <td><s:select id="adddivisionDsDivisionId" name="dsDivisionId"
                                              list="dsDivisionList"/></td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="font-size:10pt;text-align:left;padding-left:25px;" colspan="3">
                        * Add New Division <br>
                        * Active Division <br>
                        * Inactive Division
                    </td>
                    <td>
                        <div class="form-submit">
                            <s:hidden name="pageType" value="3"/>
                            <s:submit value="Division List" cssStyle="margin-top:10px;" name="button"/>
                        </div>
                    </td>
                </tr>
                </s:form>
                </tbody>
            </table>
        </fieldset>
    </td>
    <td>
        <fieldset style="border:2px solid #c3dcee;width:96%">
            <table style="border:none;margin-top:15px;text-align:center;margin-bottom:10px;" align="center">
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col/>
                <tbody>
                <tr>
                    <td style="font-size:13pt;" colspan="4">Marriage Registration Division List</td>
                </tr>
                <tr>
                    <td colspan="4">
                        <s:form name="editDivisions" action="eprInitDivisionList.do" method="POST">
                        <table class="add-inactive-divisions-outer-table" align="center" cellspacing="0">
                            <col style="width:30%"/>
                            <col style="width:70%"/>
                            <col/>
                            <tbody>
                            <tr>

                                <td>District</td>
                                <td><s:select id="addMrDivisionDistrictId" name="userDistrictId"
                                              list="districtList"/></td>
                            </tr>
                            <tr>
                                <td>Divisional Secretary Division</td>
                                <td><s:select id="addMrDivisionDsDivisionId" name="dsDivisionId"
                                              list="dsDivisionList"/></td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="font-size:10pt;text-align:left;padding-left:25px;" colspan="3">
                        * Add New MRDivision <br>
                        * Active MRDivision <br>
                        * Inactive MRDivision
                    </td>
                    <td>
                        <div class="form-submit">
                            <s:hidden name="pageType" value="4"/>
                            <s:submit value="MRDivision List" cssStyle="margin-top:10px;" name="button"/>
                        </div>
                    </td>
                </tr>
                </s:form>
                </tbody>
            </table>
        </fieldset>
    </td>
</tr>
<tr>
    <td>
        <fieldset style="border:2px solid #c3dcee;width:96%">
            <s:form name="editDivisions" action="eprInitDivisionList.do" method="POST">
                <table style="border:none;text-align:center;margin-top:0;margin-bottom:109px;" align="center">
                    <col style="width:25%"/>
                    <col style="width:25%"/>
                    <col style="width:25%"/>
                    <col style="width:25%"/>
                    <col/>
                    <tbody>
                    <tr>
                        <td style="font-size:13pt;" colspan="4">Court List</td>
                    </tr>
                    <tr>
                        <td style="font-size:10pt;text-align:left;padding-left:25px;" colspan="3">
                            * Add New Court <br>
                            * Active Court <br>
                            * Inactive Court
                        </td>
                        <td>
                            <div class="form-submit">
                                <s:hidden name="pageType" value="5"/>
                                <s:submit value="Court List" cssStyle="margin-top:10px;font-size:10pt;"/>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </s:form>
        </fieldset>
    </td>
    <td>
        <fieldset style="border:2px solid #c3dcee;width:96%;">
                <%--<s:form name="editDivisions" action="eprInitDivisionList.do" method="POST">--%>
            <table style="border:none;margin-top:15px;text-align:center;margin-bottom:10px;" align="center">
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col/>
                <tbody>
                <tr>
                    <td style="font-size:13pt;" colspan="4">Location List</td>
                </tr>
                <tr>
                    <td colspan="4">
                        <s:form name="editDivisions" action="eprInitDivisionList.do" method="POST">
                        <table class="add-inactive-divisions-outer-table" align="center" cellspacing="0">
                            <col style="width:30%"/>
                            <col style="width:70%"/>
                            <col/>
                            <tbody>
                            <tr>

                                <td>District</td>
                                <td><s:select id="addLocDivisionDistrictId" name="userDistrictId"
                                              list="districtList"/></td>
                            </tr>
                            <tr>
                                <td>Divisional Secretary Division</td>
                                <td><s:select id="addLocDivisionDsDivisionId" name="dsDivisionId"
                                              list="dsDivisionList"/></td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="font-size:10pt;text-align:left;padding-left:25px;" colspan="3">
                        * Add New Location <br>
                        * Active Location <br>
                        * Inactive Location
                    </td>
                    <td>
                        <div class="form-submit">
                            <s:hidden name="pageType" value="6"/>
                            <s:submit value="Location List" cssStyle="margin-top:10px;font-size:10pt;"/>
                        </div>
                    </td>
                </tr>
                </s:form>
            </table>
        </fieldset>
    </td>

</tr>


<tr>
    <td>
        <fieldset style="border:2px solid #c3dcee;float:left;width:96%;">
            <table style="border:none;margin-top:15px;text-align:center;margin-bottom:10px;" align="center">
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col/>
                <tbody>
                <tr>
                    <td style="font-size:13pt;" colspan="4">Grama Niladhari Division List</td>
                </tr>
                <tr>
                    <td colspan="4">
                        <s:form name="editDivisions" action="eprInitDivisionList.do" method="POST">
                        <table class="add-inactive-divisions-outer-table" align="center" cellspacing="0">
                            <col style="width:30%"/>
                            <col style="width:70%"/>
                            <col/>
                            <tbody>
                            <tr>

                                <td>District</td>
                                <td><s:select id="addGnDivisionDistrictId" name="userDistrictId"
                                              list="districtList"/></td>
                            </tr>
                            <tr>
                                <td>Divisional Secretary Division</td>
                                <td><s:select id="addGnDivisionDsDivisionId" name="dsDivisionId"
                                              list="dsDivisionList"/></td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="font-size:10pt;text-align:left;padding-left:25px;" colspan="3">
                        * Add New Grama Niladhari Division<br>
                        * Active Grama Niladhari Division<br>
                        * Inactive Grama Niladhari Division
                    </td>
                    <td>
                        <div class="form-submit">
                            <s:hidden name="pageType" value="7"/>
                            <s:submit value="GN Division List" cssStyle="margin-top:10px;" name="button"/>
                        </div>
                    </td>
                </tr>
                </s:form>
                </tbody>
            </table>
        </fieldset>
    </td>
    <td>
        <fieldset style="border:2px solid #c3dcee;width:96%;">
            <s:form name="reArrangeDivisions" action="eprInitRearrangeDivision.do" method="POST">
            <table style="border:none;margin-top:15px;text-align:center;margin-bottom:10px;" align="center">
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col/>
                <tbody>
                <tr>
                    <td style="font-size:13pt;" colspan="4">Re-arrange Grama Niladhari Divisions</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td style="font-size:10pt;text-align:left;padding-left:25px;" colspan="3">
                        Re-arrange Grama Niladhari Divisions
                    </td>
                    <td>
                        <div class="form-submit">
                            <s:submit value="Re-arrange" cssStyle="margin-top:10px;font-size:10pt;"/>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td height="35px">&nbsp;</td>
                </tr>
            </table>
            </s:form>
        </fieldset>
    </td>
</tr>

</table>
</s:if>
<s:if test="!(pageType == 0)">
<fieldset style="border:2px solid #c3dcee;margin-left:6em;margin-right:20.5em;margin-top:1.5em;width:80%">
    <table style="border:none;font:12pt bold;" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td><s:label cssStyle="color:blue;" name="msg"/>
                <s:fielderror name="duplicateIdNumberError" cssStyle="color:red;font-size:10pt"/></td>
        </tr>
    </table>
    <s:if test="editMode">
        <s:url id="manageDivision" action="eprEditDivisionDetails.do" namespace="../management"/>
    </s:if>
    <s:else>
        <s:url id="manageDivision" action="eprAddDivisionsAndDsDivisions.do" namespace="../management"/>
        <s:url id="print" action="eprAddDivisionsAndDsDivisions.do" namespace="../management"/>
    </s:else>
    <s:form name="editDivisions" id="editDivisions" method="POST" action="%{manageDivision}"
            onsubmit="javascript:return validate()">
        <table class="add-inactive-divisions-outer-table" cellspacing="0" align="center" style="margin-top:2px;">
            <col width="27%"/>
            <col width="18%"/>
            <col width="55%"/>
            <s:if test="!((pageType==1) ||(pageType==5) )">   <%--||(pageType==6)--%>
                <tr style="height:35px;">
                    <td colspan="2">District</td>
                    <s:textfield name="userDistrictId" cssStyle="visibility:hidden;"/>
                    <td><s:label name="" value="%{districtEn}" cssStyle=" margin-left:15px;"/></td>
                </tr>
            </s:if>
            <s:if test="pageType==3 ||pageType==4 || pageType==6|| pageType==7">   <%-- Shan added page 6--%>
                <tr style="height:35px;">
                    <s:textfield name="dsDivisionId" cssStyle="visibility:hidden;"/>
                    <td colspan="2">Divisional Secretary Division</td>
                    <td><s:label name="" value="%{dsDivisionEn}" cssStyle=" margin-left:15px;"/></td>
                </tr>
            </s:if>
            <s:if test="pageType==2"><s:hidden name="dsDivision.dsDivisionUKey" value="%{dsDivisionId}"/></s:if>
            <s:if test="pageType==3"><s:hidden name="divisionId" value="%{divisionId}"/></s:if>
            <s:if test="pageType==4"><s:hidden name="mrdivisionId" value="%{mrdivisionId}"/></s:if>
            <s:if test="pageType==5"><s:hidden name="courtId" value="%{courtId}"/></s:if>
            <s:if test="pageType==6"><s:hidden name="locationId" value="%{locationId}"/></s:if>
            <s:if test="pageType==7"><s:hidden name="gnDivisionId" value="%{gnDivisionId}"/></s:if>

            <s:if test="pageType==1">
                <tr>
                    <td colspan="2">Province</td>
                    <td>
                        <s:select list="provinceList" name="provinceId" />
                    </td>
                </tr>
            </s:if>
            <s:if test="pageType!=7">
                <tr>
                    <td colspan="2">
                        <s:if test="pageType==1">District Id</s:if>
                        <s:if test="pageType==2">Divisional Secretary Division Id</s:if>
                        <s:if test="pageType==3">Registration Division Id</s:if>
                        <s:if test="pageType==4">Marriage Registration Division Id</s:if>
                        <s:if test="pageType==5">Court Id</s:if>
                        <s:if test="pageType==6">Location Code</s:if> <s:label value="*"
                                                                               cssStyle="color:red;font-size:14pt;"/>
                    </td>
                    <td>
                        <s:if test="pageType==1"><s:textfield name="district.districtId" id="id"
                                                              onkeypress="return isNumberKey(event)"/></s:if>
                        <s:if test="pageType==2"><s:textfield name="dsDivision.divisionId" id="id"
                                                              onkeypress="return isNumberKey(event)"/> </s:if>
                        <s:if test="pageType==3"><s:textfield name="bdDivision.divisionId" id="id"
                                                              onkeypress="return isNumberKey(event)"/></s:if>
                        <s:if test="pageType==4"><s:textfield name="mrDivision.divisionId" id="id"
                                                              onkeypress="return isNumberKey(event)"/></s:if>
                        <s:if test="pageType==5"><s:textfield name="court.courtId" id="id"
                                                              onkeypress="return isNumberKey(event)"/></s:if>
                        <s:if test="pageType==6"><s:textfield name="location.locationCode" id="id"
                                                              readonly="true"/></s:if>
                    </td>
                </tr>
            </s:if>
            <s:if test="pageType==7">
                <tr>
                    <td colspan="2">Grama Niladhari Division Id <s:label value="*"
                                                                         cssStyle="color:red;font-size:14pt;"/></td>
                    <td>
                        <s:textfield name="gnDivision.gnDivisionId" id="id" onkeypress="return isNumberKey(event)"/>
                    </td>
                </tr>
            </s:if>
            <tr>
                <td rowspan="3">
                    <s:if test="pageType==1">District</s:if>
                    <s:if test="pageType==2">Divisional Secretary Division</s:if>
                    <s:if test="pageType==3">Registration Division</s:if>
                    <s:if test="pageType==4">Marriage Registration Division</s:if>
                    <s:if test="pageType==5">Court</s:if>
                    <s:if test="pageType==6">Office Name</s:if>
                    <s:if test="pageType==7">Grama Niladhari Division</s:if>
                    <s:label value="*" cssStyle="color:red;font-size:14pt;"/>
                </td>
                <td>Name in English</td>
                <td>
                    <s:if test="pageType==1"><s:textfield name="district.enDistrictName" id="enName"/></s:if>
                    <s:if test="pageType==2"><s:textfield name="dsDivision.enDivisionName" id="enName"/></s:if>
                    <s:if test="pageType==3"><s:textfield name="bdDivision.enDivisionName" id="enName"/></s:if>
                    <s:if test="pageType==4"><s:textfield name="mrDivision.enDivisionName" id="enName"/></s:if>
                    <s:if test="pageType==5"><s:textfield name="court.enCourtName" id="enName"/></s:if>
                    <s:if test="pageType==6"><s:textfield name="location.enLocationName" id="enName"/></s:if>
                    <s:if test="pageType==7"><s:textfield name="gnDivision.enGNDivisionName" id="enName"/></s:if>
                </td>
            </tr>
            <tr>
                <td>Name in Sinhala</td>
                <td>
                    <s:if test="pageType==1"><s:textfield name="district.siDistrictName" id="siName"/></s:if>
                    <s:if test="pageType==2"><s:textfield name="dsDivision.siDivisionName" id="siName"/></s:if>
                    <s:if test="pageType==3"><s:textfield name="bdDivision.siDivisionName" id="siName"/></s:if>
                    <s:if test="pageType==4"><s:textfield name="mrDivision.siDivisionName" id="siName"/></s:if>
                    <s:if test="pageType==5"><s:textfield name="court.siCourtName" id="siName"/></s:if>
                    <s:if test="pageType==6"><s:textfield name="location.siLocationName" id="siName"/></s:if>
                    <s:if test="pageType==7"><s:textfield name="gnDivision.siGNDivisionName" id="siName"/></s:if>
                </td>
            </tr>
            <tr>
                <td>Name in Tamil</td>
                <td>
                    <s:if test="pageType==1"><s:textfield name="district.taDistrictName" id="taName"/></s:if>
                    <s:if test="pageType==2"><s:textfield name="dsDivision.taDivisionName" id="taName"/></s:if>
                    <s:if test="pageType==3"><s:textfield name="bdDivision.taDivisionName" id="taName"/></s:if>
                    <s:if test="pageType==4"><s:textfield name="mrDivision.taDivisionName" id="taName"/></s:if>
                    <s:if test="pageType==5"><s:textfield name="court.taCourtName" id="taName"/></s:if>
                    <s:if test="pageType==6"><s:textfield name="location.taLocationName" id="taName"/></s:if>
                    <s:if test="pageType==7"><s:textfield name="gnDivision.taGNDivisionName" id="taName"/></s:if>
                </td>
            </tr>
            <s:if test="pageType==6">
                <tr>
                    <td colspan="2">Office Type</td>
                    <td>
                        <s:select
                                list="#@java.util.HashMap@{'2':'Divisional Secretary Office'}"
                                cssStyle="width:82%; margin-left:10px;"/>
                    </td>
                </tr>
                <tr>
                    <td rowspan="2">Office Signature</td>
                    <td>Signature in<br>Sinhala/English</td>
                    <td><s:textarea name="location.sienLocationSignature"
                                    cssStyle="text-transform:capitalize;width:95%;"
                                    readonly="true" rows="2"/></td>
                </tr>
                <tr>
                    <td>Signature in<br>Tamil/English</td>
                    <td><s:textarea name="location.taenLocationSignature"
                                    cssStyle="text-transform:capitalize;width:95%;"
                                    readonly="true" rows="2"/></td>
                </tr>
                <tr>
                    <td rowspan="3">Office Mailing Address</td>
                    <td>Address in English</td>
                    <td><s:textarea name="location.enLocationMailingAddress"
                                    cssStyle="text-transform:capitalize;width:95%;"
                                    readonly="true" rows="3"/></td>
                </tr>
                <tr>
                    <td>Address in Sinhala</td>
                    <td><s:textarea name="location.siLocationMailingAddress"
                                    cssStyle="text-transform:capitalize;width:95%;"
                                    readonly="true" rows="3"/></td>
                </tr>
                <tr>
                    <td>Address in Tamil</td>
                    <td><s:textarea name="location.taLocationMailingAddress"
                                    cssStyle="text-transform:capitalize;width:95%;"
                                    readonly="true" rows="3"/></td>
                </tr>
            </s:if>
        </table>
        <%--* pageType is used to load jsp page
* checkPage is used to get value of page number in javascript--%>
        <s:if test="pageType==1"><s:hidden name="pageType" value="1"/><s:hidden id="checkPage" value="1"/></s:if>
        <s:if test="pageType==2"><s:hidden name="pageType" value="2"/><s:hidden id="checkPage" value="2"/></s:if>
        <s:if test="pageType==3"><s:hidden name="pageType" value="3"/><s:hidden id="checkPage" value="3"/></s:if>
        <s:if test="pageType==4"><s:hidden name="pageType" value="4"/><s:hidden id="checkPage" value="4"/></s:if>
        <s:if test="pageType==5"><s:hidden name="pageType" value="5"/><s:hidden id="checkPage" value="5"/></s:if>
        <s:if test="pageType==6"><s:hidden name="pageType" value="6"/><s:hidden id="checkPage" value="6"/></s:if>
        <s:if test="pageType==7"><s:hidden name="pageType" value="7"/><s:hidden id="checkPage" value="7"/></s:if>
        <div class="form-submit">
            <s:if test="editMode"><s:submit value="EDIT" cssStyle="margin-top:10px;" name="button"/></s:if>
            <s:else><s:submit value="ADD" cssStyle="margin-top:10px;" name="button"/></s:else>
        </div>
    </s:form>
    <s:form name="editDivisions" action="eprInitAddDivisionsAndDsDivisions.do" method="POST">
        <s:hidden name="pageType" value="0"/>
        <div class="form-submit">
            <s:submit value="BACK" cssStyle="margin-top:10px;" name="button"/>
        </div>
    </s:form>
</fieldset>


<fieldset style="border:none;width:83%;margin-left:75px;">
<s:form name="users_print" action="" method="POST">
<s:if test="!editMode">
    <table id="users-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
        <thead>
        <tr>
            <th style="width:12%"><s:label name="name" value="Id/ Code"/></th>
            <th style="width:63%"><s:label name="name" value="Name"/></th>
            <th style="width:10%"><s:label name="inactive" value="Active"/></th>
            <s:if test="pageType != 1">
                <th style="width:9%">Edit</th>
            </s:if>
            <s:set name="allowEdit" value="true"/>
        </tr>
        </thead>
        <tbody>
        <s:if test="pageType==1"> <s:set name="List" value="districtNameList"/></s:if>
        <s:if test="pageType==2"> <s:set name="List" value="dsDivisionNameList"/></s:if>
        <s:if test="pageType==3"> <s:set name="List" value="bdDivisionNameList"/></s:if>
        <s:if test="pageType==4"> <s:set name="List" value="mrDivisionNameList"/></s:if>
        <s:if test="pageType==5"> <s:set name="List" value="courtNameList"/></s:if>
        <s:if test="pageType==6"> <s:set name="List" value="locationNameList"/></s:if>
        <s:if test="pageType==7"> <s:set name="List" value="gnDivisionNameList"/></s:if>
        <s:iterator status="divisionListStatus" value="List">
            <tr>
                <td align="center">
                    <s:if test="pageType==1"><s:property value="districtId"/></s:if>
                    <s:if test="pageType==2"><s:property value="divisionId"/></s:if>
                    <s:if test="pageType==3"><s:property value="divisionId"/></s:if>
                    <s:if test="pageType==4"><s:property value="divisionId"/></s:if>
                    <s:if test="pageType==5"><s:property value="courtId"/></s:if>
                    <s:if test="pageType==6"><s:property value="locationCode"/></s:if>
                    <s:if test="pageType==7"><s:property value="gnDivisionId"/></s:if>
                </td>
                <td>
                    <s:if test="pageType==1"><s:property value="enDistrictName"/></s:if>
                    <s:if test="(pageType==2 || pageType==3 || pageType==4)"><s:property
                            value="enDivisionName"/></s:if>
                    <s:if test="(pageType==5)"><s:property value="enCourtName"/></s:if>
                    <s:if test="(pageType==6)"><s:property value="enLocationName"/> / <s:property
                            value="siLocationName"/></s:if>
                    <s:if test="(pageType==7)"><s:property value="enGNDivisionName"/></s:if>
                </td>
                <s:if test="pageType==1">
                    <s:url id="inactiveSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="userDistrictId" value="districtUKey"/>
                        <s:param name="activate" value="false"/>
                    </s:url>
                    <s:url id="activeSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="userDistrictId" value="districtUKey"/>
                        <s:param name="activate" value="true"/>
                    </s:url>
                </s:if>
                <s:if test="pageType==2">
                    <s:url id="inactiveSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionUKey"/>
                        <s:param name="activate" value="false"/>
                    </s:url>
                    <s:url id="activeSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionUKey"/>
                        <s:param name="activate" value="true"/>
                    </s:url>
                    <s:url id="editSelected" action="eprInitEditDivisionDetails.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionUKey"/>
                    </s:url>
                </s:if>
                <s:if test="pageType==3">
                    <s:url id="inactiveSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="divisionId" value="bdDivisionUKey"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                        <s:param name="activate" value="false"/>
                    </s:url>
                    <s:url id="activeSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="divisionId" value="bdDivisionUKey"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                        <s:param name="activate" value="true"/>
                    </s:url>
                    <s:url id="editSelected" action="eprInitEditDivisionDetails.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                        <s:param name="divisionId" value="bdDivisionUKey"/>
                    </s:url>
                </s:if>
                <s:if test="pageType==4">
                    <s:url id="inactiveSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="mrdivisionId" value="mrDivisionUKey"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                        <s:param name="activate" value="false"/>
                    </s:url>
                    <s:url id="activeSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="mrdivisionId" value="mrDivisionUKey"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                        <s:param name="activate" value="true"/>
                    </s:url>
                    <s:url id="editSelected" action="eprInitEditDivisionDetails.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                        <s:param name="mrdivisionId" value="mrDivisionUKey"/>
                    </s:url>
                </s:if>
                <s:if test="pageType==5">
                    <s:url id="inactiveSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="courtId" value="courtUKey"/>
                        <s:param name="activate" value="false"/>
                    </s:url>
                    <s:url id="activeSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="courtId" value="courtUKey"/>
                        <s:param name="activate" value="true"/>
                    </s:url>
                    <s:url id="editSelected" action="eprInitEditDivisionDetails.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="courtId" value="courtUKey"/>
                    </s:url>
                </s:if>
                <s:if test="pageType==6">
                    <s:url id="inactiveSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="locationId" value="locationUKey"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                        <s:param name="activate" value="false"/>
                    </s:url>
                    <s:url id="activeSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="locationId" value="locationUKey"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                        <s:param name="activate" value="true"/>
                    </s:url>
                    <s:url id="editSelected" action="eprInitEditDivisionDetails.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="locationId" value="locationUKey"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                    </s:url>
                </s:if>
                <s:if test="pageType==7">
                    <s:url id="inactiveSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="gnDivisionId" value="gnDivisionUKey"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                        <s:param name="activate" value="false"/>
                    </s:url>
                    <s:url id="activeSelected" action="eprActivateOrInactivateDivisionsAndDsDivisions.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="gnDivisionId" value="gnDivisionUKey"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                        <s:param name="activate" value="true"/>
                    </s:url>
                    <s:url id="editSelected" action="eprInitEditDivisionDetails.do">
                        <s:param name="pageType" value="pageType"/>
                        <s:param name="userDistrictId" value="userDistrictId"/>
                        <s:param name="dsDivisionId" value="dsDivisionId"/>
                        <s:param name="gnDivisionId" value="gnDivisionUKey"/>
                    </s:url>
                </s:if>
                <td align="center">
                    <s:if test="active || lifeCycleInfo.active">
                        <s:a href="%{inactiveSelected}" title="Inactivate"><img
                                src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                border="none"/></s:a>
                    </s:if>
                    <s:else>
                        <s:a href="%{activeSelected}" title="Activate"><img
                                src="<s:url value='/images/reject.gif'/>" width="25" height="25"
                                border="none"/></s:a>
                    </s:else>
                </td>
                <s:if test="pageType != 1">
                    <td align="center">
                        <s:a href="%{editSelected}" title="%{getText('editTooltip.label')}">
                            <img src="<s:url value='/images/edit.png'/>" width="25" height="25" border="none"/>
                        </s:a>
                    </td>
                </s:if>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</s:if>
</s:form>
</fieldset>
<table>
    <tr>
        <td>
        </td>
    </tr>
</table>
</s:if>
</div>








