<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lk.rgd.common.util.GenderUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function checkAll(field) {
        if (document.getElementById("selectAll").checked) {
            for (i = 0; i < field.length; i++) {
                field[i].checked = true;
            }
        }
        else {
            for (i = 0; i < field.length; i++)
                field[i].checked = false;
        }
    }
    function initPage() {
    }
</script>--%>
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
            <td>සහතිකයේ පැවති දත්තය<br>in tamilு <br> Before change</td>
            <td>සිදුකල වෙනස්කම<br>செய்யப்பட்ட திருத்தம் <br>Alteration</td>
            <td>අනුමැතිය<br> அனுமதி <br> Approve</td>
        </tr>

        <s:form action="eprApproveAndApplyBirthAlteration" method="post" onsubmit="javascript:return warning()">
        <s:iterator value="changesList">
        <tr>
            <td>
                <s:label value="%{getText('label.'+fieldConstant)}"/>
            </td>
            <td>
                    <s:label value="%{existingValue}"/>
            <td>
                <s:label value="%{alterationValue}"/>
            </td>
            <td align="center">
                <s:checkbox name="approvedIndex" fieldValue="%{fieldConstant}"/>
            </td>

        </tr>
        </s:iterator>
    </table>
<%--
    <table>
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
    <div class="form-submit-long">
        <s:submit name="submit" value="%{getText('lable.update')}"/>
        <s:hidden name="idUKey" value="%{birthAlteration.idUKey}"/>
    </div>
    </s:form>
</div>
<s:hidden id="confirmation" value="%{getText('confirm.apply.changes')}"/>



