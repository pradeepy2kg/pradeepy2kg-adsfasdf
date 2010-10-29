<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css">
       #alteration-print-letter-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }
    }

    #alteration-print-letter-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<script type="text/javascript">
    function initPage() {
    }
</script>
<div class="alteration-print-letter-outer">
    <div class="form-submit">
        <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
        <s:hidden id="printMessage" value="%{getText('print.message')}"/>
    </div>
    <hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:20px;">
    <table border="0" cellspacing="0" width="100%">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td rowspan="8" width="200px" height="350px"></td>
            <td colspan="2" width="600px" height="100px"
                style="text-align:center;margin-left:auto;margin-right:auto;font-size:16pt">
                <label>රාජ්‍ය සේවය පිණිසයි / அரச பணி
                    On State Service</label></td>
            <td rowspan="8" width="200px"></td>
        </tr>
        <tr>
            <td><s:label name="deathAlteration.declarant.declarantFullName" cssStyle="width:600px;font-size:12pt;"
                         cssClass="disable"
                         disabled="true"/></td>
        </tr>
        <tr>
            <td><s:label name="deathAlteration.declarant.declarantAddress" cssStyle="width:600px;font-size:12pt;"
                         cssClass="disable"
                         disabled="true"/></td>
        </tr>
        <tr>
            <td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td>
                Printed On : <%= DateTimeUtils.getISO8601FormattedString(new Date()) %>
            </td>
            <td style="text-align:right;margin-left:auto;margin-right:0;">
            </td>
        </tr>
        </tbody>
    </table>
    <hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:20px;">
    <table class="table_reg_page_05" cellpadding="0" cellspacing="0" id="approvalTable" width="98%">
        <td>
            වෙනස්කම කල කොටුව<br>Cage name<br>cage name in tamil
        </td>
        <td>මරණ සහතිකයේ පැවති දත්තය<br> Before change <br> before change in tamil</td>
        <td>සිදුකල වෙනස්කම<br>Alteration<br>alteration in tamil</td>
        <td>අනුමැතිය<br> Approve <br> Approve in tamil</td>

        </tr>
        <tbody>
        <s:iterator value="approvalFieldList">
            <tr>
                <td align="left" width="125px"><s:property value="%{getText('death.alteration.field.'+key)}"/></td>
                <td align="left" width="400px"><s:property value="%{value.get(0)}"/></td>
                <td align="left" width="400px"><s:property value="%{value.get(1)}"/></td>
                <s:if test="%{value.get(2)==true}">
                    <td align="center"><s:label value="%{getText('yes.label')}"/></td>
                </s:if>
                <s:else>
                    <td align="center"><s:label value="%{getText(reject.label')}"/></td>
                </s:else>
            </tr>
        </s:iterator>
        </tbody>
    </table>
    <br>
    සටහන/note/***
    <br>
    අනුමතකල වෙනස්කම් සහිත නව මරණ සහතිකයක් සඳහා ඉල්ලුම් කල හැක.
    <br>
    You can apply for a new death certificate with altered values.
    <br>
    *****.
</div>
