<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage(){}
</script>

<div id="Debug-details">
    <fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <legend><b><s:label value="Details"/></b></legend>
        <table style=" border:none; border-collapse:collapse;">
            <col width="150px">
            <col>
            <tr>
                <td>
                    <s:label>IdUKey </s:label>
                </td>
                <td>
                    <s:label value="%{idUKey}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Time Stamp </s:label>
                </td>
                <td>
                    <s:label value="%{timestamp.toString()}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Event Type </s:label>
                </td>
                <td>
                    <s:label value="%{eventType}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Event Code </s:label>
                </td>
                <td>
                    <s:label value="%{eventCode}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Class Name </s:label>
                </td>
                <td>
                    <s:label value="%{className}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Method Name </s:label>
                </td>
                <td>
                    <s:label value="%{methodName}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <s:label>Record Id </s:label>
                </td>
                <td>
                    <s:label value="%{recordId}"/>
                </td>
            </tr>

            <s:if test="debug!=null">
                <tr>
                    <td>
                        <s:label value="Debug Details"/>
                    </td>

                    <td>
                        <s:textarea cols="120" rows="20" name="debug" value="%{debug}" cssStyle="text-transform:none;"/>
                    </td>
                </tr>
            </s:if>
            <s:if test="stackTrace!=null">
                <tr>
                    <td>
                        <s:label value="Stack Trace"/>
                    </td>
                    <td>
                        <s:textarea cols="120" rows="20" name="stackTrace" value="%{stackTrace}"
                                    cssStyle="text-transform:none;"/>
                    </td>
                </tr>
            </s:if>
        </table>
    </fieldset>

    <s:url id="back" action="eprInitEventsManagement.do">
        <s:param name="idUKey" value="idUKey"/>
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        <s:param name="pageNumber" value="%{#request.pageNumber}"/>
        <s:param name="recordCounter" value="#request.recordCounter"/>
        <s:param name="startDate" value="#request.startDate"/>
        <s:param name="endDate" value="#request.endDate"/>
        <s:param name="startTime" value="#request.startTime"/>
        <s:param name="endTime" value="#request.endTime"/>
        <s:param name="eventType" value="#request.eventType"/>
        <s:param name="goBackFlag" value="#request.goBackFlag"/>
        <s:param name="filterFlag" value="#request.filterFlag"/>
    </s:url>

    <div class="form-submit">
        <s:a href="%{back}"><s:submit value="%{getText('back.label')}"/></s:a>
    </div>

</div>
