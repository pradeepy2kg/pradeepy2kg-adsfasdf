<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="adoption-applicant-info-form-outer">
<form action="eprAdoptionFind.do" method="post">
    <table style=" border:1px solid #000000; width:300px">
        <tr><s:actionerror/></tr>
        <tr>
            <td>
                <s:label value="%{getText('adoption_court_order_serial.label')}"/>
            </td>
            <td><s:textfield name="courtOrderNo" id="courtOrderNo"/></td>
        </tr>
    </table>
    <table style=" width:300px">
        <tr>

        <tr>
            <td width="200px"></td>
            <td align="right" class="button"><s:submit name="search"
                                                       value="%{getText('adoption_search_button.label')}"
                                                       cssStyle="margin-right:10px;"/></td>
        </tr>
        <tr>
        </tr>
    </table>
</form>
<table border="1" class="adoption-applicant" cellspacing="0" cellpadding="0"
       style="border:1px solid #000; border-collapse:collapse;">
    <tr>
        <td width="662px">නියෝගය ලැබුණු දිනය <br/>
            Received Date
        </td>
        <td style="text-align:center;" width="350px"><s:textfield name="adoption.orderReceivedDate"
                                                                  disabled="true"/></td>
    </tr>
    <tr>
        <td>අධිකරණය<br/>
            Court
        </td>
        <td style="text-align:center;"><s:textfield name="adoption.court" id="court" disabled="true"/></td>
    </tr>
    <tr>
        <td>නියෝගය නිකුත් කල දිනය <br/>
            Issued Date
        </td>
        <td style="text-align:center;"><s:textfield name="adoption.orderIssuedDate" disabled="true"/></td>
    </tr>
    <tr>
        <td>නියෝග අංකය<br/>
            Serial number
        </td>
        <td style="text-align:center;"><s:textfield name="adoption.courtOrderNumber" id="courtOrderNumber"
                                                    disabled="true"/></td>
    </tr>
    <tr>
        <td>විනිසුරු නම <br/>
            Name of the Judge
        </td>
        <td style="text-align: center;"><s:textfield name="adoption.judgeName" id="judgeName" disabled="true"/></td>
    </tr>

</table>
<br>
<br>
<br>
<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <caption></caption>
    <col style="width:330px;"/>
    <col style="width:160px;"/>
    <col style="width:190px;"/>
    <col style="width:160px;"/>
    <col style="width:190px;"/>
    <tbody>
    <tr>
        <td>උපන් දිනය<br/>
            Date of birth
        </td>
        <td colspan="2" style="text-align:center;">
            <s:textfield disabled="true" name="adoption.childBirthDate"/>
        </td>
        <td>ස්ත්‍රී පුරුෂ භාවය<br/>
            Gender
        </td>
        <td>
            <s:textfield disabled="true" name="adoption.childGender"/>
        </td>
    </tr>
    <tr>
        <td>වයස <br/>
            Age
        </td>
        <td>අවුරුදු <br/>
            Years
        </td>
        <td><s:textfield name="adoption.childAgeYears" id="childAgeYears" disabled="true"/></td>
        <td>මාස <br/>
            Months
        </td>
        <td><s:textfield name="adoption.childAgeMonths" id="childAgeMonths" disabled="true"/></td>
    </tr>
    <tr>
        <td>දැනට පවතින නම <br/>
            (නමක් දී ඇති නම්) <br/>
            Existing Name <br/>
            (if already given)
        </td>
        <td colspan="4"><s:textarea name="adoption.childExistingName" id="childExistingName"
                                    disabled="true"></s:textarea></td>
    </tr>
    <tr>
        <td>ලබා දෙන නම <br/>
            New name given
        </td>
        <td colspan="4"><s:textarea name="adoption.childNewName" id="childNewName"
                                    disabled="true"></s:textarea></td>
    </tr>
    </tbody>
</table>
<br>
<br>
<br>

<s:form action="eprAdoptionApplicantInfo.do" >
    <s:hidden name="pageNo" value="1"/>
    <table border="1" class="adoption-applicant" cellspacing="0" cellpadding="0"
           style="border:1px solid #000; border-collapse:collapse;">
        <caption></caption>
        <col/>
        <col width="330px"/>
        <col width="175px"/>
        <col width="175px"/>
        <col width="175px"/>
        <col width="175px"/>
        <col width="175px"/>
        <col width="175px"/>
        <tbody>

        <tr>
            <td colspan="2">අයදුම් කරු <br/>
                Applicant
            </td>
            <td>පියා   </br>
                Father
            </td>
            <td>
                <s:radio name="certificateApplicantType"
                         list="#@java.util.HashMap@{'FATHER':''}"/>
            </td>
            <td>මව <br/>
                Mother
            </td>
            <td>
                <s:radio name="certificateApplicantType"
                         list="#@java.util.HashMap@{'MOTHER':''}"/>
            </td>
            <td>වෙනත් <br/>
                Other
            </td>
            <td>
                <s:radio name="certificateApplicantType"
                         list="#@java.util.HashMap@{'OTHER':''}"/>
            </td>
        </tr>
        <tr>
            <td colspan="6">
                අයදුම් කරුගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
                <br>
                தாயின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம்
                <br>
                Applicant's PIN / NIC Number
                <br>
            </td>
            <td colspan="4">
                <s:textfield id="certifcateApplicantPin" name="certificateApplicantPINorNIC"/>
            </td>
        </tr>
        <td colspan="2">
            අයදුම් කරුගේ නම
            <br>
            Name of the Applicant
        </td>
        <td colspan="6">
            <s:textarea id="certificateApplicantName" name="certificateApplicantName"
                        cssStyle="width:98.2%;"/>
        </td>
        </tr>
        <tr>
            <td colspan="2">
                ලිපිනය
                <br>
                Address
            </td>
            <td colspan="6">
                <s:textarea id="certificateApplicantAddress" name="certificateApplicantAddress"
                            cssStyle="width:98.2%;"/>
            </td>
        </tr>
        </tbody>
    </table>

    <div class="adoption-form-submit">
        <s:submit value="%{getText('adoption.submit')}" cssStyle="margin-top:10px;"/>
    </div>

</s:form>
</div>