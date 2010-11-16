<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>


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

    function initPage(){}
</script>


<s:form name="editDsDivisions" action="eprAddEditDivisions.do" method="POST">
    <s:if test="!(pageNo == 1 || pageNo==2)">
        <table border="1" style="width: 70%; border:1px solid #000; border-collapse:collapse;" class="font-9">
            <col width="400px"/>
            <col width="400px"/>
            <col/>
            <tbody>
            <tr>

                <td>දිස්ත්‍රික්කය / மாவட்டம் / District</td>
                <td><s:select id="districtId" name="districtId" list="districtList"/></td>
            </tr>
            <tr>
                <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
                <td><s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                              cssStyle="float:left; "/></td>
            </tr>

            </tbody>
        </table>
    </s:if>
    <table style="border:none; width:70%">
        <tr>
            <td>
                <div class="form-submit">
                    <s:submit value="ADD" cssStyle="margin-top:10px;" name="button"/>
                </div>
            </td>
            <td>
                <div class="form-submit">
                    <s:submit value="EDIT" cssStyle="margin-top:10px;" name="button"/>
                </div>
            </td>
            <td>
                <div class="form-submit">
                    <s:submit value="DELETE" cssStyle="margin-top:10px;"/>
                </div>
            </td>
        </tr>
    </table>
</s:form>

<s:if test="pageNo == 1 || pageNo==2">
    <s:form name="editDsDivisions" action="eprAddEditDivisions.do" method="POST">
        <table border="1" style="width: 70%; border:1px solid #000; border-collapse:collapse;margin-top:20px;"
               class="font-9">
            <tr>
                <s:if test="pageNo == 1">
                    <td colspan="3">දිස්ත්‍රික්කය / மாவட்டம் / District</td>
                </s:if>
                <s:if test="pageNo == 2">
                    <td colspan="2">දිස්ත්‍රික්කය / மாவட்டம் / District</td>
                </s:if>
                <td></td>
            </tr>
            <tr>
                <s:if test="pageNo == 1">
                    <td colspan="3">Number</td>
                </s:if>
                <s:if test="pageNo == 2">
                    <td colspan="2">Number</td>
                </s:if>
                <s:if test="pageNo == 1">
                    <td><s:label value=""/></td>
                </s:if>
                <s:if test="pageNo==2">
                    <td><s:textfield name=""/></td>
                </s:if>
            </tr>
            <tr>
                <td rowspan="3">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
                <td>Divisional Secretariat in English</td>
                <s:if test="pageNo == 1">
                    <td><s:label value=""/></td>
                </s:if>
                <td><s:textfield name=""/></td>
            </tr>
            <tr>
                <td>Divisional Secretariat in Sinhala</td>
                <s:if test="pageNo == 1">
                    <td><s:label value=""/></td>
                </s:if>
                <td><s:textfield name=""/></td>
            </tr>
            <tr>
                <td>Divisional Secretariat in Tamil</td>
                <s:if test="pageNo == 1">
                    <td><s:label value=""/></td>
                </s:if>
                <td><s:textfield name=""/></td>
            </tr>
        </table>
    </s:form>
</s:if>
