<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<div id="birth-confirmation-search">
    <s:actionerror/>
    <form action="eprBirthConfirmation.do" name="birthConfirmationSearchForm" method="post">
        <table>
            <tr>
                <td>
                    <s:label name="confirmationSerch" value="%{getText('confirmationSerch.search.label')}"/>
                </td>
                <td>
                    <s:textfield name="serialNo"/>
                </td>
                <td>
                    <s:submit value="%{getText('confirmationSearch.button')}" name="search"/>
                </td>
            </tr>
        </table>
    </form>
</div>