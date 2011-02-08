<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
    }
</script>

<div align="center">
    <fieldset style="margin-bottom:10px;margin-top:10px;border:2px solid #c3dcee;width:50%">
        <legend><b>Indexing Options</b></legend>
        <table width="70%" align="left">
            <caption></caption>
            <col width="10%">
            <col width="50%">
            <col width="5%">
            <col width="20%">
            <tbody>
            <tr>
                <td colspan="4">
                    <s:actionmessage name="recordIndexOK" cssStyle="color:blue;font-size:10pt"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td align="left">Birth Records</td>
                <td></td>
                <td>
                    <s:form action="eprIndexRecords.do" method="POST">
                        <s:hidden name="indexRecord" value="1"/>
                        <s:submit value="Re-Index" cssStyle="text-transform:none;"/>
                    </s:form>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>Death Records</td>
                <td></td>
                <td>
                    <s:form action="eprIndexRecords.do" method="POST">
                        <s:hidden name="indexRecord" value="2"/>
                        <s:submit value="Re-Index" cssStyle="text-transform:none;"/>
                    </s:form>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>PRS Records</td>
                <td></td>
                <td>
                    <s:form action="eprIndexRecords.do" method="POST">
                        <s:hidden name="indexRecord" value="3"/>
                        <s:submit value="Re-Index" cssStyle="text-transform:none;"/>
                    </s:form>
                </td>
            </tr>
            <tr height="100px">
                <td></td>
                <td style="color:deeppink;">All Records</td>
                <td></td>
                <td>
                    <s:form action="eprIndexRecords.do" method="POST">
                        <s:hidden name="indexRecord" value="10"/>
                        <s:submit value="Re-Index" cssStyle="text-transform:none;"/>
                    </s:form>
                </td>
            </tr>
            </tbody>
        </table>
    </fieldset>
</div>