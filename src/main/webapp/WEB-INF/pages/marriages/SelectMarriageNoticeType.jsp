<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:form action="eprMarriageNoticeInit.do" method="post">
    <table>
        <caption/>
        <col width="300px"/>
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
                <s:select
                        list="#@java.util.HashMap@{'BOTH_NOTICE':getText('both.submit.label'),
                        'MALE_NOTICE':getText('male.submit.label'),'FEMALE_NOTICE':getText('female.submit.label')}"
                        name="noticeType" cssStyle="width:190px;"/>
            </td>
        </tr>
        <%--<tr>
            <td>
                <s:label value="%{getText('label.who.request.license')}"/>
            </td>
            <td></td>
            <td align="left">
                <s:select
                        list="#@java.util.HashMap@{'HAND_COLLECT_MALE':getText('label.collect.by.male'),
                        'MAIL_TO_MALE':getText('label.mail.male'),'HAND_COLLECT_FEMALE':getText('label.collect.by.female'),'MAIL_TO_FEMALE':getText('label.mail.female')}"
                        name="licenseCollectType" cssStyle="width:190px;"/>
            </td>
        </tr>--%>
        </tbody>
    </table>
    <div class="form-submit">
        <s:submit value="%{button.go}"/>
    </div>
</s:form>