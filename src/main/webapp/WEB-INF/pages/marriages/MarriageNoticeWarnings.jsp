<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:if test="userWarnings.size()>0">
    <%--warnings from adding--%>
    <table>
        <caption/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td colspan="2" align="center">
                <s:iterator value="userWarnings">
                    <s:property value="message"/>
                </s:iterator>
            </td>
        </tr>
        </tbody>
    </table>
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
</s:if>
<%--warnings from approval--%>