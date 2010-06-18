<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<div id="birth-confirmation-search">
    <s:actionerror/>
    <form action="eprBDFSearch.do" name="birthConfirmationSearchForm" method="post">
        <table>
            <tr>
                <td>
                    <s:label name="declarationSearialNumber" value="%{getText('searchDeclarationSearial.label')}"/>
                </td>
                <td></td>
                <td>
                    <s:textfield name="serialNo"/>
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
    </form>
</div>

<div>
</div>