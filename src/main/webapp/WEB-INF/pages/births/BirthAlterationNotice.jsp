<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">
    #alteration-print-letter-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }

        td {
            font-size: 10pt;
        }

        #page-separator {
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

<div id="alteration-print-letter-outer">
<s:form action="eprMarkBirthAlterationAsPrint.do" method="post">
    <div class="form-submit" style="margin-bottom:20px;margin-right:10px;">
        <s:submit type="button" value="%{getText('mark.as.print.button')}"/>
    </div>
    <s:hidden name="idUKey" value="%{#request.idUKey}"/>
</s:form>
<div class="form-submit" style="margin-bottom:20px;margin-right:10px;">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    <s:hidden id="printMessage" value="%{getText('print.message')}"/>
</div>

<table style="border:none;width:98.6%">
    <tr>
        <td style="width:33%;height:50px;"></td>
        <td style="width:33%"></td>
        <td rowspan="2" style="width:33%">
            <table class="table_print" width="100%" cellpadding="0"
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
<table class="table_print" width="100%" cellpadding="0" cellspacing="0"
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
            பிரதேச செயளாளர் பிரிவு <br/>
            Divisional Secretary Division
        </td>
        <td style="width:30%"><s:label name="dsDivisionName"/></td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
            பதிவுப் பிரிவு<br>
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
<table class="table_print" width="100%" cellpadding="0" cellspacing="0">
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
                <s:if test="language=='si'">
                    <s:label value="%{getText('label.'+fieldConstant+'.1')}"/>
                </s:if>
                <s:else>
                    <s:label value="%{getText('label.'+fieldConstant+'.2')}"/>
                </s:else>
                <br>
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

<hr id="page-separator" style="border-style:dashed;float:left;width:100% ;margin-bottom:30px;margin-top:60px;">
<div style="page-break-after:always;"></div>

<table style="border:none;width:98.6%">
    <tr>
        <td style="width:33%;height:50px;"></td>
        <td style="width:33%"></td>
        <td rowspan="2" style="width:33%">
            <table class="table_print" width="100%" cellpadding="0"
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
<table class="table_print" width="100%" cellpadding="0" cellspacing="0"
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
            பிரதேச செயளாளர் பிரிவு <br/>
            Divisional Secretary Division
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
<table class="table_print" width="100%" cellpadding="0" cellspacing="0">
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
                <s:if test="language=='si'">
                    <s:label value="%{getText('label.'+fieldConstant+'.1')}"/>
                </s:if>
                <s:else>
                    <s:label value="%{getText('label.'+fieldConstant+'.2')}"/>
                </s:else>
                <br>
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

<div style="page-break-after:always;"></div>
<hr style="border-style:dashed ; float:left;width:100% ;margin-bottom:30px;margin-top:60px;">
<%--Latter for declarant   --%>
<table border="0" cellspacing="0" width="100%">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td rowspan="7" width="10%" height="350px"></td>
        <td colspan="3" width="80%" height="100px"
            style="text-align:center;margin-left:auto;margin-right:auto;font-size:22pt">
            <label>රාජ්‍ය සේවය පිණිසයි &nbsp;&nbsp;அரச பணி &nbsp;&nbsp;ON STATE SERVICE</label><br/>
            <label style="font-size:11pt;">රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව &nbsp;&nbsp;பதிவாளர் நாயகம் திணைக்களம்
                &nbsp;&nbsp;REGISTRAR GENERAL'S DEPARTMENT</label>
        </td>
        <td rowspan="7" width="10%"></td>
    </tr>
    <tr>
        <td></td>
        <td width="10%">&nbsp;</td>
        <td width="30%">
            <s:label name="birthAlteration.declarant.declarantFullName" cssStyle="width:600px;font-size:14pt;"/><br/>
            <s:label name="birthAlteration.declarant.declarantAddress" cssStyle="width:600px;font-size:14pt;"/>
        </td>
    </tr>
    <tr>
        <td height="50px" width="30%">
            <s:textarea id="retAddress" value="%{returnAddress}" disabled="true" rows="5"
                        cssStyle="margin-top:10px;text-transform:none;width:100%;font-size:14pt;background:transparent;border:none;"/>
        </td>
        <td width="10%">&nbsp;</td>
        <td width="30%">
        </td>
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
    </tbody>
</table>
<hr style="border-style:dashed;float:left;width:100%;margin-top:30px;">
<br/><br/>

</div>
<s:form action="eprMarkBirthAlterationAsPrint.do" method="post">
    <div class="form-submit" style="margin-bottom:20px;margin-right:10px;">
        <s:submit type="button" value="%{getText('mark.as.print.button')}"/>
    </div>
    <s:hidden name="idUKey" value="%{#request.idUKey}"/>
</s:form>
<div class="form-submit" style="margin-bottom:20px;">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
    <s:hidden id="printMessage" value="%{getText('print.message')}"/>
</div>
