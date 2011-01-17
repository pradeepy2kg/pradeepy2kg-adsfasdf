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

    function warning() {
        var ret = true;
        ret = confirm(document.getElementById('confirmation').value)
        return ret;
    }

</script>

<div id="alteration-print-letter-outer">
    <s:if test="%{!approvalPage}">
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
                    <label>රාජ්‍ය සේවය පිණිසයි / அரச பணி /
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
    </s:if>

    <table border="1"
           style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">

        <col width="200px"/>
        <col width="390px"/>
        <col width="390px"/>
        <col width="50px"/>
        <tbody>
        <tr>
            <td>
            </td>
            <td>මරණ සහතිකයේ පැවති දත්තය<br> இறப்புச் சான்றிதழில் உள்ள தரவு <br> Before change</td>
            <td>සිදුකල වෙනස්කම<br>செய்யப்பட்ட திருத்தம் <br>Alteration</td>
            <td>අනුමැතිය<br> அனுமதி <br> Approve</td>
        </tr>

        <s:form action="eprDeathAlterationSetBits" method="post" onsubmit="javascript:return warning()">
        <s:iterator value="changesList">
        <tr>
            <td><s:label value="%{getText('death.alteration.field.'+fieldConstant)}"/></td>
            <td>
                    <s:label value="%{existingValue}"/>
            <td>
                <s:label value="%{alterationValue}"/>
            </td>
            <s:if test="approvalPage==true">
                <td align="center">
                    <s:checkbox name="approvedIndex" fieldValue="%{fieldConstant}"/>
                </td>
            </s:if>
            <s:else>
                <td align="center">
                    <s:label value="%{approved}"/>
                </td>
            </s:else>
        </tr>
        </s:iterator>
    </table>
    <s:if test="%{!approvalPage}">
        <div>
            <br>
            සටහන/note/***
            <br>
            අනුමතකල වෙනස්කම් සහිත නව මරණ සහතිකයක් සඳහා ඉල්ලුම් කල හැක.
            <br>
            You can apply for a new death certificate with altered values.
            <br>
            *****
        </div>
        .
    </s:if>
    <s:else>
    <%--    <table>
            <caption/>
            <col>
            <col>
            <tbody>
            <tr>
                <td width="1000px" align="right"><s:label value="%{getText('label.apply.changes')}"/></td>
                <td align="right"><s:checkbox id="applyChanges" name="applyChanges"/></td>
            </tr>
            </tbody>
        </table>--%>
        <div class="form-submit">
            <s:submit name="submit" value="%{getText('lable.update')}"/>
            <s:hidden name="deathAlterationId" value="%{deathAlteration.idUKey}"/>
        </div>
    </s:else>
    </s:form>
</div>
<s:hidden id="confirmation" value="%{getText('confirm.apply.changes')}"/>



