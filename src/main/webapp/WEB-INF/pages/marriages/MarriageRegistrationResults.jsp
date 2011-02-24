<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
<br>
<s:actionerror cssErrorStyle="color:red;"/>

<%--<div class="form-submit">
    <s:form action="eprMarriageRegistrationInit.do" method="post">
        <s:submit value="%{getText('button.addNew')}"/>
    </s:form>
</div>
<s:if test="(!(#session.user_bean.role.roleId.equals('DEO')))">
    <div class="form-submit">
        <s:form action="eprApproveMarriageNotice.do" method="post">
            <s:submit value="%{getText('button.approve')}"/>
            <s:hidden name="ignoreWarnings" value="false"/>
            <s:hidden name="idUKey" value="%{#request.idUKey}"/>
            <s:hidden name="pageNo" value="1"/>
            <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
            <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
            <s:hidden name="noticeType" value="%{#request.noticeType}"/>
            <s:hidden name="mrDivisionId" value="%{#request.mrDivisionId}"/>
            <s:hidden name="noticeSerialNo" value="%{#request.noticeSerialNo}"/>
            <s:hidden name="dsDivisionId" value="%{#request.mrDivisionId}"/>
            <s:hidden name="districtId" value="%{#request.districtId}"/>
            <s:hidden name="searchStartDate" value="%{#request.searchStartDate}"/>
            <s:hidden name="searchEndDate" value="%{#request.searchEndDate}"/>
            <s:hidden name="pinOrNic" value="%{#request.pinOrNic}"/>
        </s:form>
    </div>
</s:if>--%>


