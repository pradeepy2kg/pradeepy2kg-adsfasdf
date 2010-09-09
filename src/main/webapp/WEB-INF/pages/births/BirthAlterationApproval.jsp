<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:actionerror/>
<table cellpadding="5" cellspacing="0">
    <s:form action="" method="post">

        <tbody>
        <tr>
            <td><s:label name="district" value="%{getText('district.label')}"/></td>
            <td>
                <s:select id="birthDistrictId" name="birthDistrictId" list="districtList" value="birthDistrictId"
                          cssStyle="width:240px;"/>
            </td>
            <td></td>
            <td><s:label name="division" value="%{getText('select_DS_division.label')}"/></td>
            <td>
                <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                          cssStyle="float:left;  width:240px;"/>
            </td>
        </tr>
        <tr>
            <td><s:label name="bdDivision" value="%{getText('select_BD_division.label')}"/></td>
            <td>
                <s:select id="birthDivisionId" name="birthDivisionId" value="{birthDivisionId}" list="bdDivisionList"
                          headerValue="%{getText('all.divisions.label')}" headerKey="0"
                          cssStyle=" width:240px;float:left;"/>
            </td>
            <td class="button" align="left"><s:submit name="refresh" value="%{getText('refresh.label')}"/></td>
        </tr>
        </tbody>
    </s:form>
    </table>

    