<%@ page import="lk.rgd.common.util.NameFormatUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>

<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
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

    function initPage() {
    }
</script>
<div>
    <s:actionerror cssStyle="color:red;font-size:10pt"/>
</div>
<div>
    <s:if test="searchResultList.size > 0">
        <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
            <legend><b><s:label value="%{getText('searchResult.label')}"/></b></legend>
            <table id="search-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
                <thead>
                <tr>
                    <th width="80px"><s:label name="certificate" value="%{getText('certificate.label')}"/></th>
                    <th width="80px"><s:label name="serial" value="%{getText('serial.label')}"/></th>
                    <th><s:label name="childNamelbl" value="%{getText('childName.label')}"/></th>
                    <th width="50px"><s:label name="childGenderlbl" value="%{getText('childGender.label')}"/></th>
                    <th width="40px">
                        <s:if test="#request.certificateType.ordinal() == 0">
                            <s:label name="live" value="%{getText('live.label')}"/>
                        </s:if>
                        <s:elseif test="#request.certificateType.ordinal() == 1">
                            <s:label name="deathType" value="%{getText('death.label')}"/>
                        </s:elseif>
                    </th>
                    <th width="20px"></th>
                </tr>
                </thead>
                <tbody>
                <s:if test="#request.certificateType.ordinal() == 0">
                    <s:iterator status="searchStatus" value="searchResultList" id="searchId">
                        <tr class="<s:if test="#searchStatus.odd == true">odd</s:if><s:else>even</s:else>">
                            <td><s:property value="idUKey"/></td>
                            <td><s:property value="register.bdfSerialNo"/></td>
                            <td>
                                <s:if test="child.childFullNameOfficialLang != null">
                                    <%= NameFormatUtil.getDisplayName((String) request.getAttribute("child.childFullNameOfficialLang"), 50)%>
                                </s:if>
                            </td>
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
                            </td>
                            <td align="center">
                                <s:if test="register.birthType.ordinal() != 0">
                                    <s:label value="%{getText('yes.label')}"/>
                                </s:if>
                                <s:elseif test="register.birthType.ordinal() == 0">
                                    <s:label value="%{getText('no.label')}"/>
                                </s:elseif>
                            </td>
                            <td align="center">
                                <s:url id="cetificatePrintUrl" action="eprBirthCertificate.do">
                                    <s:param name="bdId" value="idUKey"/>
                                    <s:param name="certificateSearch" value="true"/>
                                </s:url>
                                <s:a href="%{cetificatePrintUrl}">
                                    <img src="<s:url value='/images/print_icon.gif'/>" border="none" height="25"/>
                                </s:a>
                            </td>
                        </tr>
                    </s:iterator>
                </s:if>
                <s:elseif test="#request.certificateType.ordinal() == 1">
                    <s:iterator status="searchStatus" value="searchResultList" id="searchId">
                        <tr class="<s:if test="#searchStatus.odd == true">odd</s:if><s:else>even</s:else>">
                            <td><s:property value="idUKey"/></td>
                            <td><s:property value="death.deathSerialNo"/></td>
                            <td><s:property value="%{deathPerson.getDeathPersonNameOfficialLangToLength(50)}"/></td>
                            <td align="center">
                                <s:if test="deathPerson.deathPersonGender == 0">
                                    <s:label value="%{getText('male.label')}"/>
                                </s:if>
                                <s:elseif test="deathPerson.deathPersonGender == 1">
                                    <s:label value="%{getText('female.label')}"/>
                                </s:elseif>
                                <s:elseif test="deathPerson.deathPersonGender == 2">
                                    <s:label value="%{getText('unknown.label')}"/>
                                </s:elseif>
                            </td>
                            <td align="center">
                                <s:if test="deathType.ordinal() == 0">
                                    <s:label value="%{getText('normalDeath.label')}"/>
                                </s:if>
                                <s:elseif test="deathType.ordinal() == 1">
                                    <s:label value="%{getText('suddenDeath.label')}"/>
                                </s:elseif>
                                <s:elseif test="deathType.ordinal() == 2">
                                    <s:label value="%{getText('lateDeath.label')}"/>
                                </s:elseif>
                                <s:elseif test="deathType.ordinal() == 3">
                                    <s:label value="%{getText('missingDeath.label')}"/>
                                </s:elseif>
                            </td>
                            <td align="center">
                                <s:url id="cetificatePrintUrl" action="eprDeathCertificate.do">
                                    <s:param name="idUKey" value="idUKey"/>
                                    <s:param name="certificateSearch" value="true"/>
                                </s:url>
                                <s:a href="%{cetificatePrintUrl}">
                                    <img src="<s:url value='/images/print_icon.gif'/>" border="none" height="25"/>
                                </s:a>
                            </td>
                        </tr>
                    </s:iterator>
                </s:elseif>
                </tbody>
            </table>
        </fieldset>
    </s:if>
    <s:actionmessage cssClass="alreadyPrinted" cssStyle="color:blue;;font-size:10pt"/>
</div>
