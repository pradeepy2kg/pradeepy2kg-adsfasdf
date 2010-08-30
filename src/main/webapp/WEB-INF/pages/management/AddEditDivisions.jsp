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
        $('select#editDivisionDistrictId').bind('change', function(evt1) {
            alert("degvfrfg")
            var id = $("select#editDivisionDistrictId").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select# editdivisionDsDivisionId ").html(options1);

                        var options2 = '';
                        var bd = data.bdDivisionList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#editdivisionDivisionId").html(options2);
                    });
        });

        $('select# editdivisionDsDivisionId ').bind('change', function(evt2) {
            var id = $("select# editdivisionDsDivisionId ").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#editdivisionDivisionId").html(options);
                    });
        });
    });

</script>
<%-- --%>
<s:if test="!(pageNo == 1 || pageNo==2 || pageNo == 3 || pageNo==4)">
    <table style="border:none;margin-top:15px;text-align:center;" align="center">
        <tr>
            <td>Add And Edit District</td>
        </tr>
    </table>
    <s:form name="editDsDivisions" action="eprInitAddDistrict.do" method="POST">
        <table border="1" style="width: 70%; border:1px solid #000; border-collapse:collapse;" class="font-9"
               align="center">
            <col width="400px"/>
            <col width="400px"/>
            <col/>
            <tbody>
            <tr>

                <td>දිස්ත්‍රික්කය / மாவட்டம் / District</td>
                <td><s:select id="editDsDivisionDistrictId" name="UserDistrictId" list="districtList"/></td>
            </tr>
            <tr>
                <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
                <td><s:select id="editdivisionDsDivisionId" name="dsDivisionId" list="dsDivisionList"
                              cssStyle="float:left; "/></td>
            </tr>
            </tbody>
        </table>
        <table style="border:none; width:70%; text-align:center;" align="center">
            <tr>
                <td>
                    <div class="form-submit">
                        <s:submit value="ADD" cssStyle="margin-top:10px;" name="button"/>
                    </div>
                </td>
                <td>
                    <div class="form-submit">
                        <s:submit value="INACTIVE" cssStyle="margin-top:10px;" name="button"/>
                    </div>
                </td>
            </tr>
        </table>
    </s:form>
    <table style="border:none;margin-top:15px;text-align:center;" align="center">
        <tr>
            <td>Add And Edit Ds Division</td>
        </tr>
    </table>
    <s:form name="editDsDivisions" action="eprInitAddDsDivisions.do" method="POST">
        <table border="1" style="width: 70%; border:1px solid #000; border-collapse:collapse;" class="font-9"
               align="center">
            <col width="400px"/>
            <col width="400px"/>
            <col/>
            <tbody>
            <tr>

                <td>දිස්ත්‍රික්කය / மாவட்டம் / District</td>
                <td><s:select id="editDsDivisionDistrictId" name="UserDistrictId" list="districtList"/></td>
            </tr>
            <tr>
                <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
                <td><s:select id="editdivisionDsDivisionId" name="dsDivisionId" list="dsDivisionList"
                              cssStyle="float:left; "/></td>
            </tr>
            </tbody>
        </table>
        <table style="border:none; width:70%; text-align:center;" align="center">
            <tr>
                <td>
                    <div class="form-submit">
                        <s:submit value="ADD" cssStyle="margin-top:10px;" name="button"/>
                    </div>
                </td>
                <td>
                    <div class="form-submit">
                        <s:submit value="INACTIVE" cssStyle="margin-top:10px;" name="button"/>
                    </div>
                </td>
            </tr>
        </table>
    </s:form>
    <table style="border:none;margin-top:15px;text-align:center;" align="center">
        <tr>
            <td>Add And Edit Division</td>
        </tr>
    </table>
    <s:form name="editDivisions" action="eprInitAddDivisions.do" method="POST">
        <table border="1" style="width: 70%; border:1px solid #000; border-collapse:collapse;border-top:1px solid #000"
               class="font-9"
               align="center">
            <col width="400px"/>
            <col width="400px"/>
            <col/>
            <tbody>
            <tr>

                <td>දිස්ත්‍රික්කය / மாவட்டம் / District</td>
                <td><s:select id="editdivisionDistrictId" name="UserDistrictId" list="districtList"/></td>
            </tr>
            <tr>
                <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
                <td><s:select id="editdivisionDsDivisionId" name="dsDivisionId" list="dsDivisionList"
                              cssStyle="float:left; "/></td>
            </tr>
            <tr>
                <td><label>ලියාපදිංචි කිරීමේ කොටිඨාශය<br>* In Tamil<br>Registration Division</label></td>
                <td><s:select id="editdivisionDivisionId" name="divisionId" list="divisionList"
                              cssStyle="float:left; "/></td>
            </tr>
            </tbody>
        </table>
        <table style="border:none; width:70%; text-align:center;" align="center">
            <tr>
                <td>
                    <div class="form-submit">
                        <s:submit value="ADD" cssStyle="margin-top:10px;" name="button"/>
                    </div>
                </td>
                <td>
                </td>
                <td>
                    <div class="form-submit">
                        <s:submit value="INACTIVE" cssStyle="margin-top:10px;" name="button"/>
                    </div>
                </td>
            </tr>
        </table>
    </s:form>
</s:if>
<s:if test="pageNo == 1">
    <s:form name="editDivisions" action="eprAddDivisionsAndDsDivisions.do" method="POST">
        <table border="1" style="width: 70%; border:1px solid #000; border-collapse:collapse;margin-top:20px;"
               class="font-9" align="center">
            <tr>
                <td colspan="2">Number</td>
                <td><s:textfield name="district.districtId"/></td>
            </tr>
            <tr>
                <td rowspan="3">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
                <td>Divisional Secretariat in English</td>
                <td><s:textfield name="district.enDistrictName"/></td>
            </tr>
            <tr>
                <td>Divisional Secretariat in Sinhala</td>
                <td><s:textfield name="district.siDistrictName"/></td>
            </tr>
            <tr>
                <td>Divisional Secretariat in Tamil</td>
                <td><s:textfield name="district.taDistrictName"/></td>
            </tr>
        </table>
        <s:hidden name="pageNo" value="1"/>
        <div class="form-submit">
            <s:submit value="ADD" cssStyle="margin-top:10px;" name="button"/>
        </div>
    </s:form>
</s:if>
<s:if test="pageNo == 2">
    <s:form name="editDsDivisions" action="eprAddDivisionsAndDsDivisions.do" method="POST">
        <table border="1" style="width: 70%; border-collapse:collapse;margin-top:20px;"
               class="font-9" align="center">
            <tr>
                <td colspan="2">දිස්ත්‍රික්කය / மாவட்டம் / District</td>
                <td><s:textfield name="UserDistrictId" cssStyle="visibility:hidden;"/></td>
            </tr>
            <tr>
                <td colspan="2">Number</td>
                <td><s:textfield name="dsDivision.divisionId" cssStyle="visibility:hidden;"/></td>
            </tr>
            <tr>
                <td rowspan="3">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
                <td>Divisional Secretariat in English</td>
                <td><s:textfield name="dsDivision.enDivisionName"/></td>
            </tr>
            <tr>
                <td>Divisional Secretariat in Sinhala</td>
                <td><s:textfield name="dsDivision.siDivisionName"/></td>
            </tr>
            <tr>
                <td>Divisional Secretariat in Tamil</td>
                <td><s:textfield name="dsDivision.taDivisionName"/></td>
            </tr>
        </table>
        <div class="form-submit">
            <s:submit value="ADD" cssStyle="margin-top:10px;" name="button"/>
        </div>
        <s:hidden name="pageNo" value="2"/>
    </s:form>
</s:if>
<s:if test="pageNo == 3">
    <s:form name="editDivisions" action="eprAddDivisionsAndDsDivisions.do" method="POST">
        <table border="1" style="width: 70%; border:1px solid #000; border-collapse:collapse;margin-top:20px;"
               class="font-9" align="center">
            <tr>
                <td colspan="2">දිස්ත්‍රික්කය / மாவட்டம் / District</td>
                <td><s:label name="dsDivisionId" cssStyle="visibility:hidden;"/></td>
            </tr>
            <tr>
                <td colspan="2">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / பிரிவு / Divisional Secretariat</td>
                <td><s:textfield name="dsDivisionId" cssStyle="visibility:hidden;"/></td>
            </tr>
            <tr>
                <td colspan="2">Number</td>
                <td><s:textfield name="bdDivision.divisionId"/></td>
            </tr>
            <tr>
                <td rowspan="3">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
                <td>Divisional Secretariat in English</td>
                <td><s:textfield name="bdDivision.enDivisionName"/></td>
            </tr>
            <tr>
                <td>Divisional Secretariat in Sinhala</td>
                <td><s:textfield name="bdDivision.siDivisionName"/></td>
            </tr>
            <tr>
                <td>Divisional Secretariat in Tamil</td>
                <td><s:textfield name="bdDivision.taDivisionName"/></td>
            </tr>
        </table>
        <s:hidden name="pageNo" value="3"/>
        <div class="form-submit">
            <s:submit value="ADD" cssStyle="margin-top:10px;" name="button"/>
        </div>
    </s:form>
</s:if>