<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:if test="warningsAtApproval">
    <%--warning at approval--%>
    <s:form action="eprApproveMarriageNotice.do">
        <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
            <table>
                <caption/>
                <col/>
                <col/>
                <tbody>
                <tr>
                    <td colspan="2" align="left">
                        <s:iterator value="warnings">
                            <s:label cssStyle="color:red;" value="%{message}"/>
                            <br>
                        </s:iterator>
                    </td>
                </tr>
                <tr>
                    <td align="right"><s:label value="%{getText('label.apply.ignore.warnings')}"/></td>
                    <td align="center">
                    <td><s:checkbox name="ignoreWarnings"/></td>
                    </td>
                </tr>
                </tbody>
            </table>
        </fieldset>
        <table>
        <caption/>
        <col width="400px"/>
        <col/>
        <col/>
        <tr>
        <td></td>
        <td colspan="2" align="right">
        <div class="form-submit">

        <s:submit cssStyle="width:150px" value="%{getText('button.ignoreWarnings')}"/>
        <s:hidden name="idUKey" value="%{#request.idUKey}"/>
        <s:hidden name="pageNo" value="%{#request.pageNo}"/>
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

    <div class="form-submit">
        <s:form action="eprMarriageNoticeSearchInit.do">
            <s:submit cssStyle="width:150px" value="%{getText('button.cancel')}"/>
        </s:form>
    </div>
    </td>
    </tr>
    </table>
</s:if>
<s:else>
    <fieldset style="margin-bottom:10px;border:2px solid #c3dcee;">
        <table>
            <caption/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td colspan="2" align="left">
                    <s:iterator value="userWarnings">
                        <s:label cssStyle="color:red;" value="%{message}"/>
                        <br>
                    </s:iterator>
                </td>
            </tr>
            </tbody>
        </table>
    </fieldset>
    <s:if test="%{#request.editMode}">
        <table>
            <caption/>
            <col width="400px"/>
            <col/>
            <col/>
            <tr>
                <td></td>
                <td colspan="2">
                    <div class="form-submit">
                        <s:form action="eprRollBackNoticeToPreviousAndEditNotice.do">
                            <s:submit cssStyle="width:150px" value="%{getText('button.roll.back')}"/>
                            <s:hidden name="ignoreWarnings" value="true"/>
                            <s:submit cssStyle="width:150px" value="%{getText('button.ignoreWarnings')}"/>
                            <s:hidden name="idUKey" value="%{#request.idUKey}"/>
                            <s:hidden name="pageNo" value="%{#request.pageNo}"/>
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

                    <div class="form-submit">
                        <s:form action="eprRollBackNoticeToPreviousAndEditNotice.do">
                            <s:submit cssStyle="width:150px" value="%{getText('button.proceed')}"/>
                            <s:hidden name="ignoreWarnings" value="true"/>
                            <s:submit cssStyle="width:150px" value="%{getText('button.ignoreWarnings')}"/>
                            <s:hidden name="idUKey" value="%{#request.idUKey}"/>
                            <s:hidden name="pageNo" value="%{#request.pageNo}"/>
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
                </td>
            </tr>
        </table>
    </s:if>
    <s:else>
        <%--warnings at add second notice--%>
        <table>
            <caption/>
            <col width="400px"/>
            <col/>
            <col/>
            <tr>
                <td></td>
                <td colspan="2">
                    <div class="form-submit">
                        <s:form action="eprRollBackNoticeToPrevious.do">
                            <s:submit cssStyle="width:150px" value="%{getText('button.roll.back')}"/>
                            <s:hidden name="ignoreWarnings" value="true"/>
                            <s:hidden value="%{#request.idUKey}" name="idUKey"/>
                            <s:hidden value="%{#request.noticeType}" name="noticeType"/>
                        </s:form>
                    </div>

                    <div class="form-submit">
                        <s:form action="eprMarriageSecondNoticeAdd.do">
                            <s:submit cssStyle="width:150px" value="%{getText('button.proceed')}"/>
                            <s:hidden name="ignoreWarnings" value="true"/>
                            <s:hidden value="%{#request.idUKey}" name="idUKey"/>
                            <s:hidden value="%{#request.noticeType}" name="noticeType"/>
                        </s:form>
                    </div>
                </td>
            </tr>
        </table>
    </s:else>
</s:else>
<%--warnings from approval--%>