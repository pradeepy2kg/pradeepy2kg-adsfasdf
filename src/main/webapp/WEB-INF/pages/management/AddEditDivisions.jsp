<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

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
            <tr>
                <td>
                <td colspan="1"><s:radio id="enteringType" name="enteringType"
                                         list="#@java.util.HashMap@{'DSdivision':'Add Divisional Secretariat'}"/>
                </td>
                <td>
                <td colspan="1"><s:radio id="enteringType" name="enteringType"
                                         list="#@java.util.HashMap@{'Division':'Add Registration Division'}"/>
                </td>
                <td colspan="1"><s:radio id="addDivisions" name="addDivisions"
                                         list="#@java.util.HashMap@{'DSdivision':'Add'}"/>
                </td>
                <td>
                <td colspan="1"><s:radio id="editDivisions" name="addDivisions"
                                         list="#@java.util.HashMap@{'Division':'Edit'}"/>
                </td>
            </tr>
            <tr>
                <td>
                <td colspan="1"><s:radio id="enteringType" name="enteringType"
                                         list="#@java.util.HashMap@{'DSdivision':'Edit Divisional Secretariat'}"/>
                </td>
                <td>
                <td colspan="1"><s:radio id="enteringType" name="denteringType"
                                         list="#@java.util.HashMap@{'Division':'Edit Registration Division'}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Select Ds Division</s:label>
                </td>
                <td>
                    <s:select id="districtId" name="birthDistrictId" list="districtList" value="birthDistrictId"/>
                </td>
                <td>
                    <s:label>Select Division</s:label>
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