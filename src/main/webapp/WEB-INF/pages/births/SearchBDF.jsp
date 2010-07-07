<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css" title="currentStyle">
    @import "lib/datatables/media/css/demo_page.css";
    @import "lib/datatables/media/css/demo_table.css";
    @import "lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css";
</style>


<script type="text/javascript" language="javascript" src="lib/jquery/jquery.js"></script>
<script type="text/javascript" language="javascript" src="lib/datatables/media/js/jquery.dataTables.js"></script>
<script>
    $(document).ready(function() {
        $('#search-list-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "bSort": true,
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers"
        });
    });
</script>


<div id="birth-confirmation-search">
    <script>
        function view_DSDivs() {
            dojo.event.topic.publish("view_DSDivs");
        }

        function view_BDDivs() {
            dojo.event.topic.publish("view_BDDivs");
        }
    </script>
    <s:url id="loadDSDivList" action="../ajaxSupport_loadDSDivListSearch"/>
    <s:actionerror/>
    <br/>
    <s:form action="eprBDFSearchBySerialNo.do" name="birthConfirmationSearchForm" id="search-bdf-form"
            method="post">
        <fieldset>
            <legend><s:label name="registrationSerchLegend"
                             value="%{getText('registrationSerchLegend.label')}"/></legend>

            <table class="search-option-table">
                <caption></caption>
                <col/>
                <col/>
                <col/>
                <col/>
                <col/>
                <tbody>
                <tr>
                    <td><s:label name="declarationSearialNumber"
                                 value="%{getText('searchDeclarationSearial.label')}"/></td>
                    <td width="360px"><s:textfield name="serialNo"/></td>
                    <td width="200x"><label>දිස්ත්‍රික්කය மாவட்டம் District</label></td>
                    <td><s:select name="birthDistrictId" list="districtList" value="birthDistrictId"
                                  onchange="javascript:view_DSDivs();return false;"/></td>

                </tr>
                <tr>
                    <td><label>D.S.කොට්ඨාශය பிரிவு D.S. Division</label></td>
                    <td colspan="3"><sx:div id="dsDivisionId" value="dsDivisionId" href="%{loadDSDivList}" theme="ajax"
                                            listenTopics="view_DSDivs" formId="search-bdf-form"></sx:div></td>

                </tr>
                <tr>
                    <td colspan="3"></td>
                    <td>
                        <div class="form-submit"><s:submit value="%{getText('bdfSearch.button')}" name="search"/></div>
                    </td>
                </tr>
                </tbody>
            </table>
        </fieldset>
    </s:form>
    <br/>
    <s:form action="eprBDFSearchByIdUKey.do" method="post">
        <fieldset>
            <legend><s:label name="confirmatinSearchLegend"
                             value="%{getText('confirmationSearchLegend.label')}"/></legend>
            <table class="search-option-table">
                <tr>
                    <td width="300px"><s:label name="confirmationSearch"
                                               value="%{getText('searchConfirmationSerial.label')}"/></td>
                    <td width="250px"><s:textfield name="idUKey"/></td>
                    <td>
                        <div class="form-submit"><s:submit value="%{getText('bdfSearch.button')}" name="search"/></div>
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
    <s:if test="#request.bdf != null || #request.searchResultList.size>0">
        <fieldset>
            <legend>
                <s:label value="%{getText('searchResult.label')}"/>
            </legend>
            <table id="search-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
                <thead>
                <tr>
                    <s:if test="searchResultList.size>0">
                        <th></th>
                    </s:if>
                    <th><s:label name="childNamelbl" value="%{getText('childName.label')}"/></th>
                    <th><s:label name="childGenderlbl" value="%{getText('childGender.label')}"/></th>
                    <th><s:label name="districtlbl" value="%{getText('district.label')}"/></th>
                    <th><s:label name="divisionlbl" value="%{getText('division.label')}"/></th>
                    <th><s:label name="statuslbl" value="%{getText('status.label')}"/></th>
                </tr>
                </thead>
                <tbody>
                <s:if test="%{#request.bdf != null}">
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
                </s:if>
                <s:elseif test="searchResultList.size>0">
                    <s:iterator status="searchStatus" value="searchResultList" id="searchId">
                        <tr class="<s:if test="#searchStatus.odd == true">odd</s:if><s:else>even</s:else>">
                            <td class="table-row-index"><s:property value="%{#searchStatus.count}"/></td>
                            <td><s:property value="%{child.getChildFullNameOfficialLangToLength(50)}"/></td>
                            <td align="center">
                                <s:if test="child.childGender == 0">
                                    <s:label value="%{getText('male.label')}"/>
                                </s:if>
                                <s:elseif test="child.childGender == 1">
                                    <s:label value="%{getText('female.label')}"/>
                                </s:elseif>
                                <s:elseif test="child.childGender == 2">
                                    <s:label value="%{getText('unknown.label')}"/>
                                </s:elseif>
                            <td><s:property
                                    value="%{#request.districtList.get(register.getBirthDistrict().districtUKey)}"/></td>
                            <td><s:property
                                    value="%{#request.districtList.get(register.getBirthDivision().bdDivisionUKey)}"/></td>
                            <s:set value="getRegister().getStatus()" name="status"/>
                            <td><s:label value="%{getText(#status)}"/></td>
                        </tr>
                    </s:iterator>
                </s:elseif>
                </tbody>
            </table>
        </fieldset>
    </s:if>
</div>
