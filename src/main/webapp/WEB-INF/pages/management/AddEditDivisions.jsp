<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript">
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
                       $("select#birthDivisionId").html(options2);
                   });
       });

       $('select#dsDivisionId').bind('change', function(evt2) {
           var id = $("select#dsDivisionId").attr("value");
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
    function Add()
    {
        try
        {
            xmlhttp = new XMLHttpRequest();
            document.getElementById("Content").innerHTML = document.getElementById("selectAddFields").innerHTML;
        }
        catch (e)
        {
            document.getElementById("Content").innerHTML = "<h1>XMLHttp cannot be created!</h1>";
        }
    }
    function AddDsDivisions()
    {
        try
        {
            xmlhttp = new XMLHttpRequest();
            document.getElementById("Content1").innerHTML = document.getElementById("addDsDivisionFields").innerHTML;
        }
        catch (e)
        {
            document.getElementById("Content1").innerHTML = "<h1>XMLHttp cannot be created!</h1>";
        }
    }
    function AddDivisions()
    {
        try
        {
            xmlhttp = new XMLHttpRequest();
            document.getElementById("Content1").innerHTML = document.getElementById("addDivisionFields").innerHTML;
        }
        catch (e)
        {
            document.getElementById("Content1").innerHTML = "<h1>XMLHttp cannot be created!</h1>";
        }
    }
    function Edit()
    {
        try
        {
            xmlhttp = new XMLHttpRequest();
            document.getElementById("Content").innerHTML = document.getElementById("selectEditFields").innerHTML;
        }
        catch (e)
        {
            document.getElementById("Content").innerHTML = "<h1>XMLHttp cannot be created!</h1>";
        }
    }
    function EditDsDivisions()
    {
        try
        {
            xmlhttp = new XMLHttpRequest();
            document.getElementById("Content1").innerHTML = document.getElementById("editDsDivisionFields").innerHTML;
        }
        catch (e)
        {
            document.getElementById("Content1").innerHTML = "<h1>XMLHttp cannot be created!</h1>";
        }
    }
    function EditDivisions()
    {
        try
        {
            xmlhttp = new XMLHttpRequest();
            document.getElementById("Content1").innerHTML = document.getElementById("editDivisionFields").innerHTML;
        }
        catch (e)
        {
            document.getElementById("Content1").innerHTML = "<h1>XMLHttp cannot be created!</h1>";
        }
    }
</script>
<div>
    <s:form action="eprAddEditDivisions" name="addEditDivision_Form" method="POST">
        <table>
            <tr>
                <td>
                    <s:label>Select a District</s:label>
                </td>
                <td>
                    <s:select id="districtId" name="birthDistrictId" list="districtList" value="birthDistrictId"/>
                </td>
            </tr>
        </table>
        <table id="selectEditFields" style="display:none;">
            <tr>
                <td>
                <td colspan="1"><s:radio id="editDsDivision" name="editDivision"
                                         list="#@java.util.HashMap@{'EditDSDivision':'Edit Divisional Secretariat'}"
                                         onclick="javascript:EditDsDivisions()"/>
                </td>
                <td>
                <td colspan="1"><s:radio id="editDivision" name="editDivision"
                                         list="#@java.util.HashMap@{'EditDivision':'Edit Registration Division'}"
                                         onclick="javascript:EditDivisions()"/>
                </td>
            </tr>
        </table>
        <table id="selectAddFields" style="display:none;">
            <tr>
                <td colspan="1"><s:radio id="addDsDivision" name="addDivision"
                                         list="#@java.util.HashMap@{'AddDsDivision':'Add Divisional Secretariat'}"
                                         onclick="javascript:AddDsDivisions()"/>

                </td>
                <td colspan="1"><s:radio id="addDivision" name="addDivision"
                                         list="#@java.util.HashMap@{'AddDivision':'Add Registration Division'}"
                                         onclick="javascript:AddDivisions()"/>
                </td>
            </tr>
        </table>
        <table id="addDsDivisionFields" style="display:none;">
            <tr>
                <td>
                    <s:label>Enter English Name</s:label>
                </td>
                <td>
                    <s:textfield name="englishName"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Enter Sinhala Name</s:label>
                </td>
                <td>
                    <s:textfield name="sinhalaName"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Enter Tamil Name</s:label>
                </td>
                <td>
                    <s:textfield name="tamilName"/>
                </td>
            </tr>

        </table>
        <table id="addDivisionFields" style="display:none;">
            <tr>
            <td>
                <s:label>Select Ds Division</s:label>
            </td>
            <td>
                <s:select id="districtId" name="birthDistrictId" list="districtList" value="birthDistrictId"/>
            </td>
            </tr>
            <tr>
                <td>
                    <s:label>Enter English Name</s:label>
                </td>
                <td>
                    <s:textfield name="englishName"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Enter Sinhala Name</s:label>
                </td>
                <td>
                    <s:textfield name="sinhalaName"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Enter Tamil Name</s:label>
                </td>
                <td>
                    <s:textfield name="tamilName"/>
                </td>
            </tr>
        </table>
        <div class="form-submit">
            <s:submit value="Submit" cssStyle="margin-top:10px;"/>
        </div>
    </s:form>
</div>