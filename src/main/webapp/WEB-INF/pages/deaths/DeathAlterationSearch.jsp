<%--@author amith jayasekara--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>

<style type="text/css">
    #serialNumber {
        width: 232px
    }
</style>

<s:form method="post" action="eprCaptureDeathAlteration.do">
    <%--section select act--%>
    <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
        <legend align="right">
            <s:label value="%{getText('label.legend.choose.the.act')}"/>
        </legend>
        <table cellpadding="2px" cellspacing="0">
            <caption></caption>
            <col width="500px">
            <col>
            <tbody>
            <tr>
                <td align="left">
                    <s:label value="%{getText('label.selec.act')}"/>
                </td>
                <td align="left">
                    <s:select
                            list="#@java.util.HashMap@{'1':'52(1)','2':'53'}"
                            name="#" cssStyle="width:185px;" disabled="false"
                            id="sectionOfAct"/>
                </td>
            </tr>
            </tbody>
        </table>
    </fieldset>

    <%--section search by death certificate number--%>
    <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
        <legend align="right">
            <s:label value="%{getText('label.legend.search.by.certificate.number')}"/>
        </legend>
        <table cellpadding="2px" cellspacing="0">
            <caption></caption>
            <col width="500px">
            <col>
            <tbody>
            <tr>
                <td align="left">
                    <s:label value="%{getText('label.certificate.number')}"/>
                </td>
                <td align="left">
                    <s:textfield name="#" id="certificateNumber"/>
                </td>
            </tr>
            </tbody>
        </table>
    </fieldset>

    <%--section serch by identification number--%>
    <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
        <legend align="right">
            <s:label value="%{getText('label.legend.search.by.identification.number')}"/>
        </legend>
        <table cellpadding="2px" cellspacing="0">
            <caption></caption>
            <col width="500px">
            <col>
            <tbody>
            <tr>
                <td align="left">
                    <s:label value="%{getText('label.identification.number')}"/>
                </td>
                <td align="left">
                    <s:textfield name="#" id="identificationNumber" maxLength="10"/>
                </td>
            </tr>
            </tbody>
        </table>
    </fieldset>

    <%--secton search by serial number and bd/death division--%>
    <fieldset style="margin-bottom:10px;margin-top:20px;border:2px solid #c3dcee;">
        <legend align="right">
            <s:label value="%{getText('label.legend.search.by.serial.number')}"/>
        </legend>
        <table>
            <caption></caption>
            <col width="250px">
            <col width="250px">
            <col width="50px">
            <col width="250px">
            <col width="250px">
            <tr>
                <td>
                    <s:label value="%{getText('label.district')}"/>
                </td>
                <td>
                    <s:select id="districtId" name="birthDistrictId" list="districtList" value="birthDistrictId"
                              cssStyle="width:98.5%; width:240px;"/>
                </td>
                <td></td>
                <td>
                    <s:label value="%{getText('label.dsDivision')}"/>
                </td>
                <td>
                    <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                              cssStyle="float:left;  width:240px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label value="%{getText('label.bdDivision')}"/>
                </td>
                <td>
                    <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}"
                              list="bdDivisionList"
                              cssStyle="float:left;  width:240px;"/>
                </td>
                <td></td>
                <td>
                    <s:label value="%{getText('label.serialNumber')}"/>
                </td>
                <td>
                    <s:textfield name="#" id="serialNumber" maxLength="10"/>
                </td>
            </tr>
        </table>
    </fieldset>

    <%--section search button--%>
    <div class="form-submit">
        <s:submit type="submit" value="%{getText('button.search')}" id="searchButton"/>
    </div>

</s:form>