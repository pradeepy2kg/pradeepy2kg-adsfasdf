<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="birth-confirmation-search">
    <script>
        function view_DSDivs() {
            dojo.event.topic.publish("view_DSDivs");
        }

        function view_BDDivs() {
            dojo.event.topic.publish("view_BDDivs");
        }
    </script>
    <s:actionerror/>
    <br/>
    <s:url id="loadDSDivList" action="ajaxSupport_loadDSDivList"/>
    <form action="eprBDFSearchBySerialNo.do" name="birthConfirmationSearchForm" id="search-bdf-form"
          method="post">
        <fieldset>
            <legend><s:label name="registrationSerchLegend"
                             value="%{getText('registrationSerchLegend.label')}"/></legend>
            <table>
                <col/>
                <col/>
                <col/>
                <col/>
                <tbody>
                <tr>
                    <td>
                        <s:label name="declarationSearialNumber" value="%{getText('searchDeclarationSearial.label')}"/>
                    </td>
                    <td>
                        <s:textfield name="serialNo"/></td>
                    <td><label>දිස්ත්‍රික්කය மாவட்டம் District</label></td>
                    <td>
                        <s:select name="birthDistrictId" list="districtList" value="birthDistrictId"
                                  onchange="javascript:view_DSDivs();return false;"/>
                    </td>
                </tr>
                <tr>

                    <td><label>D.S.කොට්ඨාශය பிரிவு D.S. Division</label></td>
                    <td colspan="3" class="table_reg_cell_01" id="table_reg_cell_01">
                        <sx:div id="dsDivisionId" value="dsDivisionId" href="%{loadDSDivList}" theme="ajax"
                            listenTopics="view_DSDivs" formId="search-bdf-form"></sx:div>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <s:submit value="%{getText('bdfSearch.button')}" name="search"/>
                    </td>
                    <td></td>
                </tr>
                </tbody>
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
        </s:form>
    </div>
    <br/>--%>
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
                    <td align="center">
                        <s:label value="%{getText(status)}"/></td>
                </tr>
            </table>
        </fieldset>
    </s:if>
</div>
