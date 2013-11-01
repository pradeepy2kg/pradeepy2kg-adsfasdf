<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage(){}
</script>

<fieldset style="margin-bottom:10px;margin-top:5px;border:2px solid #c3dcee;">
        <legend><b><s:label value="%{getText('label.select.notice.type')}"/></b></legend>

<s:form action="eprMarriageNoticeInit.do" method="post">
    <table>
        <caption/>
        <col width="300px"/>
        <col width="30px"/>
        <col/>
        <tbody>
        <tr>
            <td>
            </td>
            <td align="left">
                <s:select
                        list="#@java.util.HashMap@{'BOTH_NOTICE':getText('both.submit.label'),
                        'MALE_NOTICE':getText('male.submit.label'),'FEMALE_NOTICE':getText('female.submit.label')}"
                        name="noticeType" cssStyle="width:190px;"/>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="form-submit">
        <s:submit value="%{getText('button.go')}"/>
    </div>
</s:form>

</fieldset>