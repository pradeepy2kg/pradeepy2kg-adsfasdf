<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<div id="birth-confirmation-search">
    <s:actionerror/>
    <form action="eprBDFSearch.do" name="birthConfirmationSearchForm" method="post">
        <fieldset>
            <legend><s:label name="registrationSerchLegend" value="%{getText('registrationSerchLegend.label')}"/> </legend>
            <table>
                <tr>
                    <td>
                        <s:label name="declarationSearialNumber" value="%{getText('searchDeclarationSearial.label')}"/>
                    </td>
                    <td>
                        <s:textfield name="serialNo"/>
                    </td>
                    <td>
                        <s:label><span><s:label name="district" value="%{getText('district.label')}"/></span><s:select
                                list="districtList" name="district" value="#request.district"/></s:label>
                    </td>
                    <td>
                        <s:label><span><s:label name="division" value="%{getText('division.label')}"/></span><s:select
                                list="divisionList" value="division"
                                name="division" headerKey="0"/></s:label>
                    </td>
                </tr>

                <tr>
                    <td></td>
                    <td><s:label name="searchOr" value="%{getText('searchOr.label')}"/></td>
                    <td></td>
                </tr>
                <tr>
                    <td>
                        <s:label name="confirmationSerch" value="%{getText('searchConfirmationSerial.label')}"/>
                    </td>
                    <td></td>
                    <td>
                        <s:textfield name="idUKey"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <s:submit value="%{getText('bdfSearch.button')}" name="search"/>
                    </td>
                    <td></td>
                </tr>
            </table>
        </fieldset>
    </form>
    <s:form>
        <fieldset>
            <legend><s:label name="confirmatinSearchLegend" /></legend>
            <table>
                
            </table>
        </fieldset>
    </s:form>
</div>

<div>
</div>