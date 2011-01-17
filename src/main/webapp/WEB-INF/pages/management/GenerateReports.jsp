<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">
    #page-body {
        width: 100%;
    }

    #selection {
        border: #444 1px solid;
        margin:20px;
    }
</style>

<script type="text/javascript">
    function initPage() {
    }
</script>

<html>
<body>
<s:actionerror cssStyle="color:red;"/>
<div id="page-body">
    <div id="selection">
        <s:form action="eprCreateReports.do" method="POST">
        <table border="0" cellpadding="5" cellspacing="5" width="100%">
            <tr>
                <th colspan="3" align="left" style="background-color:#a9a9a9;">Choose Options</th>
            </tr>
            <tr>
                <td width="20%" align="right"><s:label value="%{getText('select.year.label')}"/></td>
                <td width="50%">
                    <s:select list="yearList" name="year" id="selectYear" cssStyle="width:300px;"/>
                </td>
                <td width="30%">&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td colspan="2" align="left">
                    <s:submit/>
                </td>
            </tr>
        </table>
        </s:form>
    </div>
</div>
<div id="message">
     <s:actionmessage cssStyle="color:blue;"/>
</div>
</body>
</html>