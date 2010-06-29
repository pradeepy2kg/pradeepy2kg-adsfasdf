<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<div id="birth-confirmation-search">
    <s:actionerror/>
    <br/>

    <form action="eprBDFSearchBySerialNo.do" name="birthConfirmationSearchForm" method="post">
        <fieldset>
            <legend><s:label name="registrationSerchLegend"
                             value="%{getText('registrationSerchLegend.label')}"/></legend>
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
                    <td></td>
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
    <br/>
    <s:form action="eprBDFSearchByIdUKey.do" method="post">
        <fieldset>
            <legend><s:label name="confirmatinSearchLegend"
                             value="%{getText('confirmationSearchLegend.label')}"/></legend>
            <table>
                <tr>
                    <td>
                        <s:label name="confirmationSearch" value="%{getText('searchConfirmationSerial.label')}"/>
                    </td>

                    <td>
                        <s:textfield name="idUKey"/>
                    </td>
                </tr>
                <tr>

                    <td></td>
                    <td>
                        <s:submit value="%{getText('bdfSearch.button')}" name="search"/>
                    </td>
                </tr>
            </table>
        </fieldset>
    </s:form>
    <%--<br/>
    <s:form action="eprBDFSearchByChildName.do" method="post">
        <fieldset>
            <legend><s:label name="nameSearchLegend" value="%{getText('nameSearchLegend.label')}"/></legend>
            <table>
                <tr>
                    <td>
                        <s:label name="childName" value="%{getText('childName.label')}"/>
                    </td>

                    <td>
                        <s:textfield name="childName"/>
                    </td>
                </tr>
                <tr>

                    <td></td>
                    <td>
                        <s:submit value="%{getText('bdfSearch.button')}" name="search"/>
                    </td>
                </tr>
            </table>
        </fieldset>
    </s:form>--%>
</div>
<br/>

<div>
    <s:if test="%{#request.bdf != null}">
        <fieldset>
            <legend>
                <s:label value="%{getText('searchResult.label')}"/>
            </legend>
            <table>
                <th><s:label name="childNamelbl" value="%{getText('childName.label')}"/></th>
                <th><s:label name="childGenderlbl" value="%{getText('childGender.label')}"/></th>
                <th><s:label name="districtlbl" value="%{getText('district.label')}"/></th>
                <th><s:label name="divisionlbl" value="%{getText('division.label')}"/></th>
                <th><s:label name="statuslbl" value="%{getText('status.label')}"/></th>
                <tr>
                    <td align="center"><s:label name="childName"
                                                value="%{#request.bdf.child.getChildFullNameOfficialLangToLength(50)}"/></td>
                    <td align="center"><s:if test="%{#request.bdf.child.childGender == 0}">
                        <s:label value="%{getText('male.label')}"/>
                    </s:if>
                        <s:elseif test="%{#request.bdf.child.childGender == 1}">
                            <s:label value="%{getText('female.label')}"/>
                        </s:elseif>
                        <s:elseif test="%{#request.bdf.child.childGender == 2}">
                            <s:label value="%{getText('unknown.label')}"/>
                        </s:elseif>
                    </td>
                    <td align="center"><s:label name="district"
                                                value="%{#request.districtList.get(#request.bdf.register.getBirthDistrict().districtUKey)}"/></td>
                    <td align="center"><s:label name="division"
                                                value="%{#request.divisionList.get(#request.bdf.register.getBirthDivision().bdDivisionUKey)}"/></td>
                    <td align="center"><s:label name="" value="%{#request.bdf.register.getStatus()}"/></td>
                </tr>
            </table>
        </fieldset>
    </s:if>
</div>