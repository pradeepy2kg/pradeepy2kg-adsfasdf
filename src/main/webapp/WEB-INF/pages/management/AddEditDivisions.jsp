<%-- @author Tharanga Punchihewa --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<script>
    $(document).ready(function() {
        $('#users-list-table').dataTable({
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
</script>


<script type="text/javascript">
    // mode 1 = passing District, will return DS list
    // mode 2 = passing DsDivision, will return BD list
    // any other = passing district, will return DS list and the BD list for the first DS

    $(function() {
        $('select#addDsDivisionDistrictId').bind('change', function(evt1) {
            var id = $("select#addDsDivisionDistrictId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id,mode:3},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#addDsDivisionDsDivisionId").html(options1);
                    });
        });
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

                        var options2 = '';
                        var bd = data.bdDivisionList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#adddivisionDivisionId").html(options2);
                    });
        });

        $('select#adddivisionDsDivisionId').bind('change', function(evt2) {
            var id = $("select#adddivisionDsDivisionId").attr("value");
            $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#adddivisionDivisionId").html(options);
                    });
        });
    });
    function validate() {
        var errormsg = "";
        var domObject;
        var pageNo ;
        var pageName;
        pageNo = document.getElementById("checkPage").value;
        if (pageNo == 1)  pageName = "District";
        if (pageNo == 2)  pageName = "Ds Division";
        if (pageNo == 3)  pageName = "Division";
        if (pageNo == 4)  pageName = "MR Division";
        domObject = document.getElementById("id");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Plese Enter The  Id of " + pageName + " \n";
        }
        else
        {
            var reg = /^([0-9]*)$/;
            if (reg.test(domObject.value.trim()) == false) {
                errormsg = errormsg + pageName + " Id Is Invalid \n";
            }
        }
        domObject = document.getElementById("enName");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Plese Enter The English Name of " + pageName + " \n";
        }
        domObject = document.getElementById("siName");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Plese Enter The Sinhala Name of " + pageName + " \n";
        }
        domObject = document.getElementById("taName");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Plese Enter The Tamil Name of " + pageName + " \n";
        }

        if (errormsg != "") {
            alert(errormsg);
            return false;
        }
        return true;
    }
</script>

<div id="add-inactive-divisions-outer">
<s:if test="!(pageNo == 1 || pageNo==2 || pageNo == 3 || pageNo==4 || pageNo==5)">
    <fieldset style="border:3px solid #c3dcee;margin-left:2em;float:left;width:46%;height:15.4em">

        <s:form name="editDsDivisions" action="eprInitDivisionList.do" method="POST">
            <%--  <table class="add-inactive-divisions-outer-table" align="center" cellspacing="0">
                <col style="width:30%"/>
                <col style="width:70%"/>
                <col/>
                <tbody>

                </tbody>
            </table>--%>
            <table style="border:none;margin-top:15px;text-align:center;margin-bottom:10px;" align="center">
                <tr>
                    <td style="font-size:13pt;">Add And Inactive District</td>
                </tr>
                <tr>
                    <td style="font-size:10pt;text-align:left;">
                        * Add New District <br>
                        * Active District <br>
                        * Inactive District
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="form-submit">
                            <s:hidden name="pageNo" value="1"/>
                            <s:submit value="District List" cssStyle="margin-top:10px;font-size:10pt;"/>
                        </div>
                    </td>
                </tr>
            </table>
        </s:form>
    </fieldset>
    <fieldset style="border:3px solid #c3dcee;margin-top:2.5em;width:46%">
        <table style="border:none;margin-top:15px;text-align:center;margin-bottom:10px;" align="center">
            <tr>
                <td style="font-size:13pt;">Add And Inactive Ds Division</td>
            </tr>
        </table>
        <s:form name="editDsDivisions" action="eprInitDivisionList.do" method="POST">
            <table class="add-inactive-divisions-outer-table" align="center" cellspacing="0">
                <col style="width:30%"/>
                <col style="width:70%"/>
                <col/>
                <tbody>
                <tr>

                    <td>District</td>
                    <td><s:select id="addDsDivisionDistrictId" name="UserDistrictId" list="districtList"/></td>
                </tr>
                </tbody>
            </table>
            <table style="border:none; width:70%; text-align:center;margin-top:10px;" align="center">
                <tr>
                    <td style="font-size:10pt;text-align:left;">
                        * Add New Divisional Secretariat <br>
                        * Active Divisional Secretariat <br>
                        * Inactive Divisional Secretariat
                    </td>

                </tr>
                <tr>
                    <td>
                        <div class="form-submit">
                            <s:hidden name="pageNo" value="2"/>
                            <s:submit value="DsDivision List" cssStyle="margin-top:10px;"/>
                        </div>
                    </td>

                </tr>
            </table>
        </s:form>
    </fieldset>
    <fieldset style="border:3px solid #c3dcee;margin-left:2em; margin-top:2.5em;float:left;width:46%;">
        <table style="border:none;margin-top:15px;text-align:center;margin-bottom:10px;" align="center">
            <tr>
                <td style="font-size:13pt;">Add And Inactive Division</td>
            </tr>
        </table>
        <s:form name="editDivisions" action="eprInitDivisionList.do" method="POST">
            <table class="add-inactive-divisions-outer-table" align="center" cellspacing="0">
                <col style="width:30%"/>
                <col style="width:70%"/>
                <col/>
                <tbody>
                <tr>

                    <td>District</td>
                    <td><s:select id="adddivisionDistrictId" name="UserDistrictId" list="districtList"/></td>
                </tr>
                <tr>
                    <td>Divisional Secretariat</td>
                    <td><s:select id="adddivisionDsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  cssStyle="float:left; "/></td>
                </tr>
                </tbody>
            </table>
            <table style="border:none; width:70%; text-align:center;margin-top:10px;" align="center">
                <tr>
                <tr>
                    <td style="font-size:10pt;text-align:left;">
                        * Add New Division <br>
                        * Active Division <br>
                        * Inactive Division
                    </td>

                </tr>
                <td>
                    <div class="form-submit">
                        <s:hidden name="pageNo" value="3"/>
                        <s:submit value="Division List" cssStyle="margin-top:10px;" name="button"/>
                    </div>
                </td>
                <td>
                </td>
                <td></td>
                </tr>
            </table>
        </s:form>
    </fieldset>
    <fieldset style="border:3px solid #c3dcee;margin-top:2.5em;width:46%">
        <table style="border:none;margin-top:15px;text-align:center;margin-bottom:10px;" align="center">
            <tr>
                <td style="font-size:13pt;">Add And Inactive MRDivision</td>
            </tr>
        </table>
        <s:form name="editDivisions" action="eprInitDivisionList.do" method="POST">
            <table class="add-inactive-divisions-outer-table" align="center" cellspacing="0">
                <col style="width:30%"/>
                <col style="width:70%"/>
                <col/>
                <tbody>
                <tr>

                    <td>District</td>
                    <td><s:select id="adddivisionDistrictId" name="UserDistrictId" list="districtList"/></td>
                </tr>
                <tr>
                    <td>Divisional Secretariat</td>
                    <td><s:select id="adddivisionDsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  cssStyle="float:left; "/></td>
                </tr>
                </tbody>
            </table>
            <table style="border:none; width:70%; text-align:center;margin-top:10px;" align="center">
                <tr>
                    <td style="font-size:10pt;text-align:left;">
                        * Add New MRDivision <br>
                        * Active MRDivision <br>
                        * Inactive MRDivision
                    </td>

                </tr>
                <tr>
                    <td>
                        <div class="form-submit">
                            <s:hidden name="pageNo" value="4"/>
                            <s:submit value="MRDivision List" cssStyle="margin-top:10px;" name="button"/>
                        </div>
                    </td>
                    <td>
                    </td>
                    <td></td>
                </tr>
            </table>
        </s:form>
    </fieldset>
     <s:form name="editDivisions" action="eprInitCourtList.do" method="POST">
    <fieldset style="border:3px solid #c3dcee;margin-top:2.5em;width:46%;margin-left:2em;">
        <table style="border:none;margin-top:15px;text-align:center;margin-bottom:10px;" align="center">
            <tr>
                <td style="font-size:13pt;">Add And Inactive Court</td>
            </tr>
            <tr>
                <td style="font-size:10pt;text-align:left;">
                    * Add New Court <br>
                    * Active Court <br>
                    * Inactive Court
                </td>
            </tr>
            <tr>
                <td>
                    <div class="form-submit">
                        <s:hidden name="pageNo" value="5"/>
                        <s:submit value="District List" cssStyle="margin-top:10px;font-size:10pt;"/>
                    </div>
                </td>
            </tr>
        </table>
    </fieldset>
    </s:form>
</s:if>
<s:if test="!(pageNo == 0)">
    <fieldset style="border:3px solid #c3dcee;margin-left:6em;margin-right:20.5em;margin-top:2.5em;width:80%">
        <table style="border:none;font:12pt bold;" align="center">
            <tr>
                <td><s:label name="msg"/></td>
            </tr>
        </table>
        <s:form name="editDivisions" action="eprAddDivisionsAndDsDivisions.do" method="POST"
                onsubmit="javascript:return validate()">
            <table class="add-inactive-divisions-outer-table" cellspacing="0" align="center" style="margin-top:15px">
                <s:if test="!((pageNo==1) ||(pageNo==5))">
                    <tr>
                        <td colspan="2">District</td>
                        <s:textfield name="UserDistrictId" cssStyle="visibility:hidden;"/>
                        <td><s:label name="" value="%{districtEn}" cssStyle=" margin-left:15px;"/></td>
                    </tr>
                </s:if>
                <s:if test="pageNo==3 ||pageNo==4">
                    <tr>
                        <s:textfield name="dsDivisionId" cssStyle="visibility:hidden;"/>
                        <td colspan="2">Divisional Secretariat</td>
                        <td><s:label name="" value="%{dsDivisionEn}" cssStyle=" margin-left:15px;"/></td>
                    </tr>
                </s:if>
                <tr>
                    <td colspan="2">Id</td>
                    <td>
                        <s:if test="pageNo==1"><s:textfield name="district.districtId" id="id"/></s:if>
                        <s:if test="pageNo==2"><s:textfield name="dsDivision.divisionId" id="id"/> </s:if>
                        <s:if test="pageNo==3"><s:textfield name="bdDivision.divisionId" id="id"/></s:if>
                        <s:if test="pageNo==4"><s:textfield name="mrDivision.divisionId" id="id"/></s:if>
                        <s:if test="pageNo==5"><s:textfield name="mrDivision.divisionId" id="id"/></s:if>
                    </td>
                </tr>
                <tr>
                    <td rowspan="3">
                        <s:if test="pageNo==1">District</s:if>
                        <s:if test="pageNo==2">Divisional Secretariat</s:if>
                        <s:if test="pageNo==3">Registration Division</s:if>
                        <s:if test="pageNo==4">Marriage Division</s:if>
                        <s:if test="pageNo==5">Marriage Division</s:if>
                    </td>
                    <td>Name in English</td>
                    <td>
                        <s:if test="pageNo==1"><s:textfield name="district.enDistrictName" id="enName"/></s:if>
                        <s:if test="pageNo==2"><s:textfield name="dsDivision.enDivisionName" id="enName"/></s:if>
                        <s:if test="pageNo==3"><s:textfield name="bdDivision.enDivisionName" id="enName"/></s:if>
                        <s:if test="pageNo==4"><s:textfield name="mrDivision.enDivisionName" id="enName"/></s:if>
                        <s:if test="pageNo==5"><s:textfield name="mrDivision.enDivisionName" id="enName"/></s:if>
                    </td>
                </tr>
                <tr>
                    <td>Name in Sinhala</td>
                    <td>
                        <s:if test="pageNo==1"><s:textfield name="district.siDistrictName" id="siName"/></s:if>
                        <s:if test="pageNo==2"><s:textfield name="dsDivision.siDivisionName" id="siName"/></s:if>
                        <s:if test="pageNo==3"><s:textfield name="bdDivision.siDivisionName" id="siName"/></s:if>
                        <s:if test="pageNo==4"><s:textfield name="mrDivision.siDivisionName" id="siName"/></s:if>
                        <s:if test="pageNo==5"><s:textfield name="mrDivision.siDivisionName" id="siName"/></s:if>
                    </td>
                </tr>
                <tr>
                    <td>Name in Tamil</td>
                    <td>
                        <s:if test="pageNo==1"><s:textfield name="district.taDistrictName" id="taName"/></s:if>
                        <s:if test="pageNo==2"><s:textfield name="dsDivision.taDivisionName" id="taName"/></s:if>
                        <s:if test="pageNo==3"><s:textfield name="bdDivision.taDivisionName" id="taName"/></s:if>
                        <s:if test="pageNo==4"><s:textfield name="mrDivision.taDivisionName" id="taName"/></s:if>
                        <s:if test="pageNo==5"><s:textfield name="mrDivision.taDivisionName" id="taName"/></s:if>
                    </td>
                </tr>
            </table>
            <%--* pageNo is used to load jsp page
                * checkPage is used to get value of page number in javascript--%>
            <s:if test="pageNo==1"><s:hidden name="pageNo" value="1"/><s:hidden id="checkPage" value="1"/></s:if>
            <s:if test="pageNo==2"><s:hidden name="pageNo" value="2"/><s:hidden id="checkPage" value="2"/></s:if>
            <s:if test="pageNo==3"><s:hidden name="pageNo" value="3"/><s:hidden id="checkPage" value="3"/></s:if>
            <s:if test="pageNo==4"><s:hidden name="pageNo" value="4"/><s:hidden id="checkPage" value="4"/></s:if>
            <s:if test="pageNo==5"><s:hidden name="pageNo" value="5"/><s:hidden id="checkPage" value="5"/></s:if>
            <div class="form-submit">
                <s:submit value="ADD" cssStyle="margin-top:10px;" name="button"/>
            </div>
        </s:form>
        <s:form name="editDivisions" action="eprInitAddDivisionsAndDsDivisions.do" method="POST">
            <s:hidden name="pageNo" value="0"/>
            <div class="form-submit">
                <s:submit value="BACK" cssStyle="margin-top:10px;" name="button"/>
            </div>
        </s:form>
    </fieldset>


    <fieldset style="border:none">
        <s:form name="users_print" action="" method="POST">
            <table id="users-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
                <thead>
                <tr>
                    <th><s:label name="name" value="    "/></th>
                    <th><s:label name="name" value="Name"/></th>
                    <th><s:label name="edit" value="Inactive"/></th>
                    <th><s:label name="edit" value="Active"/></th>
                    <s:set name="allowEdit" value="true"/>
                </tr>
                </thead>
                <tbody>
                <s:if test="pageNo==1"> <s:set name="List" value="districtNameList"/></s:if>
                <s:if test="pageNo==2"> <s:set name="List" value="dsDivisionNameList"/></s:if>
                <s:if test="pageNo==3"> <s:set name="List" value="bdDivisionNameList"/></s:if>
                <s:if test="pageNo==4"> <s:set name="List" value="mrDivisionNameList"/></s:if>
                <s:if test="pageNo==5"> <s:set name="List" value="mrDivisionNameList"/></s:if>
                <s:iterator status="divisionListStatus" value="List">
                    <tr>
                        <td><s:property value="%{#divisionListStatus.count}"/></td>
                        <td>
                            <s:if test="pageNo==1"><s:property value="enDistrictName"/></s:if>
                            <s:if test="!(pageNo==1)"><s:property value="enDivisionName"/></s:if>
                        </td>
                        <s:if test="pageNo==1">
                            <s:url id="inactiveSelected" action="eprInactiveDivisionsAndDsDivisions.do">
                                <s:param name="pageNo" value="pageNo"/>
                                <s:param name="UserDistrictId" value="districtUKey"/>
                            </s:url>
                            <s:url id="activeSelected" action="eprActiveDivisionsAndDsDivisions.do">
                                <s:param name="pageNo" value="pageNo"/>
                                <s:param name="UserDistrictId" value="districtUKey"/>
                            </s:url>
                        </s:if>
                        <s:if test="pageNo==2">
                            <s:url id="inactiveSelected" action="eprInactiveDivisionsAndDsDivisions.do">
                                <s:param name="pageNo" value="pageNo"/>
                                <s:param name="UserDistrictId" value="UserDistrictId"/>
                                <s:param name="dsDivisionId" value="dsDivisionUKey"/>
                            </s:url>
                            <s:url id="activeSelected" action="eprActiveDivisionsAndDsDivisions.do">
                                <s:param name="pageNo" value="pageNo"/>
                                <s:param name="UserDistrictId" value="UserDistrictId"/>
                                <s:param name="dsDivisionId" value="dsDivisionUKey"/>
                            </s:url>
                        </s:if>
                        <s:if test="pageNo==3">
                            <s:url id="inactiveSelected" action="eprInactiveDivisionsAndDsDivisions.do">
                                <s:param name="pageNo" value="pageNo"/>
                                <s:param name="divisionId" value="bdDivisionUKey"/>
                                <s:param name="UserDistrictId" value="UserDistrictId"/>
                                <s:param name="dsDivisionId" value="dsDivisionId"/>
                            </s:url>
                            <s:url id="activeSelected" action="eprActiveDivisionsAndDsDivisions.do">
                                <s:param name="pageNo" value="pageNo"/>
                                <s:param name="divisionId" value="bdDivisionUKey"/>
                                <s:param name="UserDistrictId" value="UserDistrictId"/>
                                <s:param name="dsDivisionId" value="dsDivisionId"/>
                            </s:url>
                        </s:if>
                        <s:if test="pageNo==4">
                            <s:url id="inactiveSelected" action="eprInactiveDivisionsAndDsDivisions.do">
                                <s:param name="pageNo" value="pageNo"/>
                                <s:param name="UserDistrictId" value="dsDivisionUKey"/>
                            </s:url>
                            <s:url id="activeSelected" action="eprActiveDivisionsAndDsDivisions.do">
                                <s:param name="pageNo" value="pageNo"/>
                                <s:param name="dsDivisionId" value="dsDivisionUKey"/>
                            </s:url>
                        </s:if>
                        <s:if test="pageNo==5">
                            <s:url id="inactiveSelected" action="eprInactiveDivisionsAndDsDivisions.do">
                                <s:param name="pageNo" value="pageNo"/>
                                <s:param name="UserDistrictId" value="dsDivisionUKey"/>
                            </s:url>
                            <s:url id="activeSelected" action="eprActiveDivisionsAndDsDivisions.do">
                                <s:param name="pageNo" value="pageNo"/>
                                <s:param name="dsDivisionId" value="dsDivisionUKey"/>
                            </s:url>
                        </s:if>
                        <td align="center">
                            <s:if test="active">
                                <s:a href="%{inactiveSelected}"><img
                                        src="<s:url value='/images/reject.gif'/>" width="25" height="25"
                                        border="none"/></s:a>
                            </s:if>
                        </td>
                        <td align="center"><s:else>
                            <s:a href="%{activeSelected}"><img
                                    src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                    border="none"/></s:a> </s:else>
                        </td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
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








