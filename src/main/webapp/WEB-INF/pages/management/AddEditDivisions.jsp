<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/popreg/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>


<script type="text/javascript">


// mode 1 = passing District, will return DS list
// mode 2 = passing DsDivision, will return BD list
// any other = passing district, will return DS list and the BD list for the first DS
$(function() {
    $('select#districtId').bind('change', function(evt1) {
        var id = $("select#districtId").attr("value");
        $.getJSON('/popreg/crs/DivisionLookupService', {id:id},
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
        var dsDivision=document.getElementById("dsDivisionId").list;
        document.getElementById("editDsDivisionNameId").value=dsDivision;
        $.getJSON('/popreg/crs/DivisionLookupService', {id:id, mode:2},
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

</script>


<div id="death-declaration-form-1-outer">
<s:form name="deathRegistrationForm1" id="death-registration-form-1" action="eprDeathDeclaration.do" method="POST"
        >

<table border="1" style="width: 70%; border:1px solid #000; border-collapse:collapse;" class="font-9">
    <col width="400px"/>
    <col width="400px"/>
    <col/>
    <tbody>
    <tr>
       
        <td>දිස්ත්‍රික්කය / மாவட்டம் / District</td>
        <td><s:select id="deathDistrictId" name="districtId" list="districtList"/></td>
    </tr>
    <tr>
        <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
        <td><s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  cssStyle="float:left; "/></td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br>பிரிவு / <br>Registration Division</td>
        <td><s:select id="divisionId"  list="divisionList"
                                  cssStyle="float:left;"/></td>
    </tr>

    </tbody>
</table>
<table border="1" style="width: 70%; border:1px solid #000; border-collapse:collapse;margin-top:20px;" class="font-9">
    <col width="400px"/>
    <col width="400px"/>
    <col/>
    <tbody>
    <tr>

        <td>දිස්ත්‍රික්කය / மாவட்டம் / District</td>
        <td><s:textfield name="" id="editDistricNameId"/></td>
    </tr>
    <tr>
        <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
        <td><s:textfield name="" id="editDsDivisionNameId"/></td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br>பிரிவு / <br>Registration Division</td>
        <td><s:textfield name="" id="editDivisionNameId"/></td>
    </tr>

    </tbody>
</table>

<div class="form-submit">
    <s:hidden name="pageNo" value="1"/>
    <s:hidden name="rowNumber" value="%{row}"/>
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
</div>