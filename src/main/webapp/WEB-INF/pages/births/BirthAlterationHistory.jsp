<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">
    #alteration-approval-list-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }

        td {
            font-size: 10pt;
        }
    }

    #alteration-approval-list-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>

<table style="border:none;width:98.6%">

    <tr>
        <td colspan="3" style="text-align:center;font-size:11pt;">
            <s:label cssStyle="margin-top:30pt;"/><br>
            උප්පැන්න සහතිකය පිලිබඳ විස්තර <br>
            பிறப்புச் சான்றிதழ் பற்றிய விபரங்கள் <br>
            Particulars of the Birth Certificate
        </td>
    </tr>
</table>
<table class="alteration-approval-list-table" width="100%" cellpadding="0" cellspacing="0"
       style="margin-bottom:10px;">
    <tr>
        <td colspan="2">සහතිකයේ සඳහන් පුද්ගලයාගේ අනන්‍යතා අංකය <br>
            சான்றிதழில் குறிப்பிட்ட நபரின் அடையாள எண் <br>
            Identification Number of Person stated in the Certificate
        </td>
        <td colspan="2"><s:label name="nicOrPin"/></td>
    </tr>
    <tr>
        <td style="width:20%">දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td style="width:30%"><s:label name="districtName"/></td>
        <td style="width:20%">ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரதேச செயளாளார் பிரிவு <br>
            Divisional Secretariat
        </td>
        <td style="width:30%"><s:label name="dsDivisionName"/></td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
            பதிவுப் பிரிவு <br>
            Registration Division
        </td>
        <td><s:label name="bdDivisionName"/></td>
        <td>ලියාපදිංචි කිරීමේ අංකය <br>
            பதிவு இலக்கம் <br>
            Registration Number
        </td>
        <td><s:label name="birthDeclaration.register.bdfSerialNo"/></td>
    </tr>
</table>
<table class="alteration-approval-list-table" width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td style="text-align:center;width:15%;font-size:10pt;">Heading <br>
            தலைப்பு <br>
            ශීර්ෂය
        </td>
        <td style="width:10%;font-size:10pt; text-align:center;">
            Approved Date <br>
            Date in Tamil<br>
            අනුමත කල  දිනය
        </td>
        <%--<td style="width:30%;font-size:10pt; text-align:center;">
            Existing Entry <br>
            உள்ள பதிவுக் குறிப்பு <br>
            තිබෙන සටහන
        </td>--%>
        <td style="width:30%;font-size:10pt; text-align:center;">
            Approved  Entry <br>
            Approved  Entry in tamil<br>
            අනුමත කර ඇති සටහන
        </td>
        <%-- <td style="width:15%;font-size:10pt; text-align:center;">
            Approval Status <br>
            அனுமதி வழங்கல் <br>
            අනුමත කිරීම
        </td>--%>
    </tr>
    <%--<s:set id="count" name="count" value='0'/>--%>
    <s:iterator value=" historyChangesList">
        <s:set name="count" value='0'/>
        <s:set name="test" value="%{value.size()}"/>
        <s:iterator value="%{value}" status="status">
            <tr>
                    <%--<s:if test="#count==0">--%>

                <s:if test="#count==0">
                    <td rowspan="<s:property value='#test' />">
                        <s:if test="language=='si'">
                            <s:label value="%{getText('label.'+key+'.1')}"/>
                        </s:if>
                        <s:else>
                            <s:label value="%{getText('label.'+%{key}+'.2')}"/>
                        </s:else>
                        <br>
                        <s:label value="%{getText('label.'+%{key})}"/>
                    </td>
                </s:if>
                <s:set name="count" value="1"/>
                <td>
                    <s:label value="%{approveDate}"/>
                </td>
                    <%--<td>
                   <s:label value="%{existingValue}"/></td>--%>
                <td>
                    <s:label value="%{changeValue}"/>
                </td>
            </tr>
        </s:iterator>

    </s:iterator>

</table>

<hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:60px;">
<div class="form-submit" style="margin-bottom:20px;margin-right:10px;">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    <s:hidden id="printMessage" value="%{getText('print.message')}"/>
</div>
<s:form action="eprMarkBirthAlterationAsPrint.do" method="post">
    <div class="form-submit" style="margin-bottom:20px;margin-right:10px;">
        <s:submit type="button" value="%{getText('mark.as.print.button')}"/>
    </div>
    <s:hidden name="idUKey" value="%{#request.idUKey}"/>
</s:form>