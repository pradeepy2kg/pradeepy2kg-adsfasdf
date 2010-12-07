<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:form action="eprMarriageNoticeInit.do" method="post">
    <table>
        <caption/>
        <col width="150px"/>
        <col width="30px"/>
        <col/>
        <tbody>
        <tr>
            <td align="left">
                <s:label value="%{getText('label.select.notice.type')}"/>
            </td>
            <td>

            </td>
            <td align="left">
                <s:select list="#@java.util.HashMap@{'BOTH_NOTICE':getText('both.submit.label'),'MALE_NOTICE':getText('male.submit.label'),'FEMALE_NOTICE':getText('female.submit.label')}"
                        name="noticeType" cssStyle="width:190px;"/>
            </td>
        </tr>
        </tbody>
    </table>
<div class="form-submit">
    <s:submit value="go.notice"/>
</div>
</s:form>