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
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<script type="text/javascript">
    function initPage() {
    }
</script>

<s:form action="" method="post">

<div id="alteration-approval-list-outer">

<table style="border:none;width:98.6%">
    <tr>
        <td style="width:33%;height:50px;"></td>
        <td style="width:33%"></td>
        <td rowspan="2" style="width:33%">
            <table class="alteration-approval-list-table" width="100%" cellpadding="0"
                   style="float:right;"
                   cellspacing="0">
                <tr>
                    <td>පනතේ වගන්තිය <br>
                        சட்டத்தின் பிரிவு <br>
                        Section of the Act
                    </td>
                    <td align="center">
                        <s:if test="birthAlteration.type.ordinal()==0">
                            <s:label value="27" cssStyle="margin-left:10px;"/>
                        </s:if>
                        <s:elseif test="birthAlteration.type.ordinal()==1">
                            <s:label value="27A" cssStyle="margin-left:10px;"/>
                        </s:elseif>
                        <s:else>
                            <s:label value="52_1" cssStyle="margin-left:10px;"/>
                        </s:else>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td></td>
        <td align="center" style="font-size:12pt; width:430px"><img
                src="<s:url value="/images/official-logo.png"/>"
                alt=""/></td>
    </tr>
    <tr>
        <td colspan="3" style="text-align:center;font-size:12pt;">
            උප්පැන්න හා මරණ ලියාපදිංචි කිරීමේ පනත
            27 සහ 27අ (4) වගන්තිය යටතේ වූ නියමය ප්‍රසිද්ධ කිරීම <br>
            பிறப்பு மற்றும் இறப்பு பதிவு செய்தல் பிரிவின் 27 மற்றும் 27 அ (4) பிரிவின் கீழ்
            இடப்பட்ட கட்டளையினை பிரசித்தபபடுத்தல் <br>
            The Birth and Death Registration Act
            Publication of Order under section 27 and 27A (4)
        </td>
    </tr>
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
        <td style="width:35%;font-size:10pt; text-align:center;">
            Existing Entry <br>
            உள்ள பதிவுக் குறிப்பு <br>
            තිබෙන සටහන
        </td>
        <td style="width:35%;font-size:10pt; text-align:center;">
            Requested Entry <br>
            வேண்டப்பட்ட பதிவுக் குறிப்பு <br>
            ඉල්ලුම් කර ඇති සටහන
        </td>
        <td style="width:15%;font-size:10pt; text-align:center;">
            Approval Status <br>
            அனுமதி வழங்கல் <br>
            අනුමත කිරීම
        </td>
    </tr>
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
                <s:label value="%{approved}"/>
            </td>

        </tr>
    </s:iterator>

</table>

<hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:60px;">
<div style="page-break-after:always;margin-bottom:350px;"></div>

    <%--Latter for declarant   --%>
<table border="0" cellspacing="0" width="100%" style="margin-top:0;">
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
        <td><s:label cssStyle="width:600px;font-size:10pt;" name="declarant.declarantFullName"
                     disabled="true"/></td>
    </tr>
    <tr>
        <td><s:label cssStyle="width:600px;font-size:10pt;" name="declarant.declarantAddress"
                     cssClass="disable"
                     disabled="true"/></td>
    </tr>

    </tbody>
</table>

<hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:30px;">
<table style="border:none;width:98.6%">
    <tr>
        <td style="width:33%;height:50px;"></td>
        <td style="width:33%"></td>
        <td rowspan="2" style="width:33%">
            <table class="alteration-approval-list-table" width="100%" cellpadding="0"
                   style="float:right;"
                   cellspacing="0">
                <tr>
                    <td>පනතේ වගන්තිය <br>
                        சட்டத்தின் பிரிவு <br>
                        Section of the Act
                    </td>
                    <td>
                        <s:if test="birthAlteration.type.ordinal()==0">
                            <s:label value="27" cssStyle="margin-left:10px;"/>
                        </s:if>
                        <s:elseif test="birthAlteration.type.ordinal()==1">
                            <s:label value="27A" cssStyle="margin-left:10px;"/>
                        </s:elseif>
                        <s:else>
                            <s:label value="52_1" cssStyle="margin-left:10px;"/>
                        </s:else>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td></td>
        <td align="center" style="font-size:12pt; width:430px"><img
                src="<s:url value="/images/official-logo.png"/>"
                alt=""/></td>
    </tr>
    <tr>
        <td colspan="3" style="text-align:center;font-size:11pt;">
            <s:label cssStyle="margin-top:30pt;"/><br>
            උප්පැන්න සහතිකය පිලිබඳ විස්තර <br>
            பிறப்புச் சான்றிதழ் பற்றிய விபரங்கள் <br>
            Particulars of the Birth Certificate
        </td>
    </tr>
    <tr>
        <td colspan="3">

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
        <td style="width:35%;font-size:10pt; text-align:center;">
            Existing Entry <br>
            உள்ள பதிவுக் குறிப்பு <br>
            තිබෙන සටහන
        </td>
        <td style="width:35%;font-size:10pt; text-align:center;">
            Requested Entry <br>
            வேண்டப்பட்ட பதிவுக் குறிப்பு <br>
            ඉල්ලුම් කර ඇති සටහන
        </td>
        <td style="width:15%;font-size:10pt; text-align:center;">
            Approval Status <br>
            அனுமதி வழங்கல் <br>
            අනුමත කිරීම
        </td>
    </tr>
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
                <s:label value="%{approved}"/>
            </td>

        </tr>
    </s:iterator>
    <s:iterator status="approvalStatus" value="birthAlterationApprovalList" id="approvalList">
        <tr>

            <td style="padding-left:5px;">
                <s:property
                        value="%{getText(sectionOfAct+'.'+birthAlterationApprovalList[#approvalStatus.index][0]+'.label')}"/></td>
            <td style="padding-left:15px;"><s:property
                    value="birthAlterationApprovalList[#approvalStatus.index][1]"/><s:label/></td>
            <td style="padding-left:15px;"><s:property
                    value="birthAlterationApprovalList[#approvalStatus.index][2]"/><s:label/></td>
            <td style="text-align:center;">
                ප්‍රතික්ෂේපිතයි <br>
                இரத்து செய்தல் <br>
                Rejected
            </td>
        </tr>
    </s:iterator>

</table>
</div>
</s:form>
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