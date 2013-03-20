<%--@author Duminda Dharmakeerthi--%>
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
        $('#zonalOfficeTable').dataTable({
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

    $(function(){

    });

    function validateZonalOffice(){
        var errormsg = "";
        var domObject;

        domObject = document.getElementById("siZonalOfficeName");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Please Enter The Zonal Office Name in Sinhala\n";
        }
        domObject = document.getElementById("taZonalOfficeName");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Please Enter The Zonal Office Name in Tamil\n";
        }
        domObject = document.getElementById("enZonalOfficeName");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Please Enter The Zonal Office Name in English\n";
        }
        domObject = document.getElementById("siZonalOfficeMailAddress");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Please Enter The Zonal Office Mail Address in Sinhala\n";
        }
        domObject = document.getElementById("taZonalOfficeMailAddress");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Please Enter The Zonal Office Mail Address in Tamil\n";
        }
        domObject = document.getElementById("enZonalOfficeMailAddress");
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "Please Enter The Zonal Office Mail Address in English\n";
        }

        var out = checkActiveFieldsForSyntaxErrors('add_edit_zonal_office');
        if(out != ""){
            errormsg = errormsg + out;
        }

        if (errormsg != "") {
            alert(errormsg);
            return false;
        }
        return true;
    }
</script>
<style type="text/css">
    #manageZonalOfficeOuter{
        padding: 10px;
    }
    #addZonalOffice{
        margin-top: 10px;
        border-right: 1px solid #000;
        border-bottom: 1px solid #000;
    }
    #addZonalOffice tr td{
        border-left: 1px solid #000000;
        border-top: 1px solid #000000;
    }
    #addZonalOffice tr td textarea, #addZonalOffice tr td input{
        width: 95%;
        margin: 0 10px;
    }
</style>
<div id="manageZonalOfficeOuter">
    <table width="100%">
        <tr>
            <td>
                <s:actionerror cssStyle="color:red;font-size:10pt"/>
                <s:actionmessage cssStyle="color:blue;font-size:10pt"/>
            </td>
        </tr>
    </table>
    <fieldset style="border:2px solid #c3dcee;width:96%;margin-top:8px;">
        <s:form id="add_edit_zonal_office" name="add_edit_zonal_office" method="POST" onsubmit="javascript:return validateZonalOffice()">
            <table cellspacing="0" cellpadding="5px" width="95%" align="center" id="addZonalOffice">
                <col width="180px"/>
                <col width="200px"/>
                <col/>
                <tr>
                    <td rowspan="3">Zonal Office </td>
                    <td>Name in Sinhala <s:label value="*" cssStyle="color:red;font-size:10pt;"/></td>
                    <td>
                        <s:textarea id="siZonalOfficeName" name="zonalOffice.siZonalOfficeName"/>
                    </td>
                </tr>
                <tr>
                    <td>Name in Tamil <s:label value="*" cssStyle="color:red;font-size:10pt;"/></td>
                    <td>
                        <s:textarea id="taZonalOfficeName" name="zonalOffice.taZonalOfficeName"/>
                    </td>
                </tr>
                <tr>
                    <td>Name in English <s:label value="*" cssStyle="color:red;font-size:10pt;"/></td>
                    <td>
                        <s:textarea id="enZonalOfficeName" name="zonalOffice.enZonalOfficeName"/>
                    </td>
                </tr>
                <tr>
                    <td rowspan="3">Zonal Office Mail</td>
                    <td>Address in Sinhala <s:label value="*" cssStyle="color:red;font-size:10pt;"/></td>
                    <td>
                        <s:textarea id="siZonalOfficeMailAddress" name="zonalOffice.siZonalOfficeMailAddress"/>
                    </td>
                </tr>
                <tr>
                    <td>Address in Tamil <s:label value="*" cssStyle="color:red;font-size:10pt;"/></td>
                    <td>
                        <s:textarea id="taZonalOfficeMailAddress" name="zonalOffice.taZonalOfficeMailAddress"/>
                    </td>
                </tr>
                <tr>
                    <td>Address in English <s:label value="*" cssStyle="color:red;font-size:10pt;"/></td>
                    <td>
                        <s:textarea id="enZonalOfficeMailAddress" name="zonalOffice.enZonalOfficeMailAddress"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">Zonal Office E-Mail</td>
                    <td><s:textfield id="zonalOfficeEmail" name="zonalOffice.zonalOfficeEmail"/></td>
                </tr>
                <tr>
                    <td colspan="2">Zonal Office Fax</td>
                    <td><s:textfield id="zonalOfficeFax" name="zonalOffice.zonalOfficeFax"/></td>
                </tr>
                <tr>
                    <td colspan="2">Zonal Office Land Phone</td>
                    <td><s:textfield id="zonalOfficeLandPhone" name="zonalOffice.zonalOfficeLandPhone"/></td>
                </tr>
                <tr>
                    <td colspan="3">
                        <div class="form-submit">
                            <s:submit action="eprInitZonalOffices" value="BACK" cssStyle="margin-top:10px; font-size:10pt; width: 115px;" name="button"/>
                            <s:if test="zonalOfficeUKey > 0">
                                <s:hidden name="page" value="4"/>
                                <s:hidden name="zonalOffice.zonalOfficeUKey"/>
                                <s:submit action="eprManageZonalOffices" value="UPDATE" cssStyle="margin-top:10px;font-size:10pt; width: 115px; "/>
                            </s:if>
                            <s:elseif test="zonalOfficeUKey == 0">
                                <s:hidden name="page" value="4"/>
                                <s:hidden name="zonalOffice.zonalOfficeUKey" value="0"/>
                                <s:submit action="eprManageZonalOffices" value="ADD" cssStyle="margin-top:10px;font-size:10pt; width: 115px; "/>
                            </s:elseif>
                        </div>
                    </td>
                </tr>
            </table>
        </s:form>
    </fieldset>
    <br/>
    <table id="zonalOfficeTable" cellpadding="0" cellspacing="0" class="display">
        <col>
        <col>
        <col width="80px">
        <col width="80px">
        <thead>
            <tr>
                <th>#</th>
                <th>Zonal Office Name</th>
                <th>Active</th>
                <th>Edit</th>
            </tr>
        </thead>
        <tbody>
             <s:iterator status="status" value="zonalOfficeList">
                 <tr>
                     <td><s:property value="zonalOfficeUKey"/></td>
                     <td><s:property value="enZonalOfficeName"/></td>
                     <td align="center">
                         <s:if test="active">
                             <s:url id="deactivate" action="eprManageZonalOffices.do">
                                 <s:param name="page" value="1"/>
                                 <s:param name="zonalOfficeUKey" value="zonalOfficeUKey"/>
                             </s:url>
                             <s:a href="%{deactivate}" title="Deactivate">
                                 <img src="<s:url value='/images/approve.gif'/>" width="25" height="25" border="none"/>
                             </s:a>
                         </s:if>
                         <s:else>
                             <s:url id="activate" action="eprManageZonalOffices.do">
                                 <s:param name="page" value="2"/>
                                 <s:param name="zonalOfficeUKey" value="zonalOfficeUKey"/>
                             </s:url>
                             <s:a href="%{activate}" title="Activate">
                                 <img src="<s:url value='/images/reject.gif'/>" width="25" height="25" border="none"/>
                             </s:a>
                         </s:else>
                     </td>
                     <td align="center">
                         <s:url id="edit" action="eprManageZonalOffices.do">
                             <s:param name="page" value="3"/>
                             <s:param name="zonalOfficeUKey" value="zonalOfficeUKey"/>
                         </s:url>
                         <s:a href="%{edit}" title="Edit">
                             <img src="<s:url value='/images/edit.png'/>" width="25" height="25" border="none"/>
                         </s:a>
                     </td>
                 </tr>
             </s:iterator>
        </tbody>
    </table>
</div>
