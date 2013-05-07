<%@ page import="java.util.Date" %>
<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
<%@ page import="lk.rgd.crs.api.domain.AdoptionOrder" %>
<%@ page import="lk.rgd.common.util.GenderUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css">
    #adoption-registration-form-outer table tr td {
        padding: 0 5px;
    }

    @media print {
        .form-submit {
            display: none;
        }

        td {
            font-size: 10pt;
        }

        #zonal_office_list {
            display: none;
        }
    }

    #adoption-registration-form-outer .form-submit {
        margin: 5px 0 15px 0;
    }
</style>
<script type="text/javascript" src="<s:url value="/js/print.js"/>"></script>
<script type="text/javascript">
    function initPage() {
    }

    $(function () {
        var zonalOfficeId = $('#zonalOfficeId').val();
        populateZonalOfficeAddress(zonalOfficeId);
    });

    function populateZonalOfficeAddress(zonalOfficeId) {
        $.getJSON('/ecivil/crs/ZonalOfficeLookupService', {zonalOfficeId:zonalOfficeId}, function (data) {
            var siAddress = data.siAddress;
            var taAddress = data.taAddress;
            var enAddress = data.enAddress;
            var telephone = data.telephone;
            /* Set New Zonal Office Addresses. */
            $('textarea#siZonalOfficeAddress').html('සහකාර රෙජිස්ට්‍රාර් ජනරාල්,\n' + siAddress + '\nදුරකථන අංක: ' + telephone);
            $('textarea#taZonalOfficeAddress').html('உதவி பதிவாளர் நாயகம்,\n' + taAddress + '\nTelephone Numbers in ta: ' + telephone);
            $('textarea#enZonalOfficeAddress').html('Assistant Registrar General,\n' + enAddress + '\nTelephone Numbers: ' + telephone);
        });
    }
</script>

<div class="form-submit">

    <s:url id="markAsPrint" action="eprMarkAdoptionNoticeAsPrinted.do">
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        <s:param name="currentStatus" value="%{#request.currentStatus}"/>
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="idUKey" value="%{#request.idUKey}"/>
    </s:url>
    <s:url id="cancel" action="eprSearchAdoptionRecord.do">
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="currentStatus" value="%{#request.currentStatus}"/>
    </s:url>
    <s:url id="viewAdoptionOrderDetails" action="eprAdoptionOrderDetailsViewMode.do">
        <s:param name="idUKey" value="idUKey"/>
        <s:param name="currentStatus" value="%{#request.currentStatus}"/>
        <s:param name="pageNo" value="%{#request.pageNo}"/>
        <s:param name="nextFlag" value="%{#request.nextFlag}"/>
        <s:param name="previousFlag" value="%{#request.previousFlag}"/>
    </s:url>

</div>
<div id="adoption-page" class="form-submit">
    <s:if test="adoption.status.ordinal() >= 2">
        <s:a href="#" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
    </s:if>

    <s:a href="%{cancel}"><s:label value="%{getText('cancel.label')}"/></s:a>
</div>
<div id="adoption-registration-form-outer">
<div id="page1">
<br/>


<table class="adoption-reg-form-header-table" width="99%">
    <tr>
        <td align="center" style="font-size:15pt">
            <img src="<s:url value="/images/official-logo.png" />" alt=""><br>
            <label>
                රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව
                <br>பதிவாளர் நாயகம் திணைக்களம்
                <br>Registrar General's Department
            </label>

        </td>
    </tr>
    <tr>
        <td style="font-size:16px">
            <br><br> දරුකමට ගැනීම පිලිබඳ ලේඛනයේ සහතික පිටපත් ලබා ගැනීම සඳහා අයදුම්පත
            <br>மகவேற்புச் சம்பந்தமான சான்றிதழின் பிரதியினை பெற்றுக்கொள்வதற்கான விண்ணப்பப்படிவம்
            <br>Application to obtain a certified copy of the Certificate of Adoption
        </td>
    </tr>

</table>
<table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0" style="float:left;width:400px;">
    <tr>
        <td height="40px" width="215px">
            <label>
                සහතික පත්‍රයේ අංකය
                <br>சான்றிதழ் இல
                <br>Certificate Number
            </label>
        </td>
        <td width="185px">
            <s:label value="%{adoption.idUKey}"/>
        </td>
    </tr>
</table>
<table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0"
       style="float:right;width:450px; margin-right: 10px;">
    <tr>
        <td height="40px" colspan="10">
            කාර්යාල ප්‍රයෝජනය සඳහා පමණි.
            <br/>அலுவலக பாவனைக்காக மட்டும்.
            <br/>For office use only.
        </td>
    </tr>
    <tr>
        <td height="40px" width="200px">
            <label>
                අනුක්‍රමික අංකය
                <br>தொடர் இலக்கம்
                <br>Serial Number
            </label>
        </td>
        <td width="24px" height="40px">&nbsp;</td>
        <td width="24px" height="40px">&nbsp;</td>
        <td width="24px" height="40px">&nbsp;</td>
        <td width="24px" height="40px">&nbsp;</td>
        <td width="24px" height="40px">&nbsp;</td>
        <td width="24px" height="40px">&nbsp;</td>
        <td width="24px" height="40px">&nbsp;</td>
        <td width="24px" height="40px">&nbsp;</td>
        <td width="24px" height="40px">&nbsp;</td>
    </tr>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td style="font-size:16px">අයදුම්කරුගේ විස්තර / விண்ணப்பதாரியின் விபரங்கள்/ Applicants Details
        </td>
    </tr>
</table>


<table border="1" width="99%" style="float:left;border:1px solid #000; border-collapse:collapse;">
    <caption></caption>
    <col width="285px"/>
    <col width="125px"/>
    <col width="110px"/>
    <col width="50px"/>
    <col width="110px"/>
    <col width="50px"/>
    <col width="110px"/>
    <col width="50px"/>
    <tbody>
    <tr>
        <td>
            ලියාපදිංචි කිරීමේ අංකය<br>
            Registration No in ta<br>
            Registration Number
        </td>
        <td colspan="2">
            <s:label value="%{adoption.adoptionEntryNo}"/>
        </td>
        <td colspan="3">
            ලියාපදිංචි කිරීමේ දිනය
            <br>Date of Registration in ta
            <br>Date of Registration
        </td>
        <td colspan="2">
            <s:label value="%{adoption.orderReceivedDate}"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            අයදුම් කරන්නේ කවුරුන් විසින්ද? ('X' ලකුණක් යොදා සටහන් කරන්න)
            <br/>யாரால் விண்ணப்பிக்கப்படுவது? ('X'அடையாளம் இடவும்)
            <br/>Who is applying? (mark with a tick 'X')
        </td>
        <td height="40px">
            පියා
            <br>தந்தை
            <br>Father
        </td>
        <td></td>
        <td>
            මව
            <br>தாய்
            <br>Mother
        </td>
        <td></td>
        <td>
            වෙනත්
            <br>வேறு
            <br>Other
        </td>
        <td></td>
    </tr>
    <tr>
        <td colspan="1" height="40px">
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification Number
        </td>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td rowspan="3" colspan="1" height="120px">
            අයදුම්කරුගේ නම
            <br>விண்ணப்பதாரரின் பெயர்
            <br>Name of the Applicant
        </td>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="7"></td>
    </tr>

    <tr>
        <td colspan="1" rowspan="4" height="160px">
            ලිපිනය
            <br>முகவரி
            <br>Address
        </td>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="7"></td>
    </tr>
    <tr>
        <td colspan="7"></td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td style="font-size:16px">ළමයාගේ විස්තර / பிள்ளை பற்றிய தகவல்/ Child's Information
        </td>
    </tr>
</table>


<table border="1" width="99%" style="margin-top:10px;float:left;border:1px solid #000; border-collapse:collapse;">
    <caption></caption>
    <col width="327px"/>
    <col/>
    <tbody>
    <tr>
        <td height="60px">
            නම
            <br>பெயர்
            <br>Name
        </td>
        <s:if test="#request.adoption.childNewName != null">
            <td height="40px" colspan="3"><s:label name="" value="%{adoption.childNewName}"/></td>
        </s:if>
        <s:else>
            <td height="40px" colspan="3"><s:label name="" value="%{adoption.childExistingName}"/></td>
        </s:else>
    </tr>

    <tr>
        <td>
            උපන් දිනය
            <br>பிறந்த திகதி
            <br>Date of Birth
        </td>
        <td height="40px"><s:label name="" value="%{adoption.childBirthDate}"/></td>
        <td height="40px" width="250px">
            ස්ත්‍රී පුරුෂ භාවය
            <br>பால்
            <br>Gender
        </td>
        <td width="250px">
            <label>
                <% AdoptionOrder a = (AdoptionOrder) request.getAttribute("adoption");
                    out.print(GenderUtil.getGender(a.getChildGender(), a.getLanguageToTransliterate()));%>
            </label>
        </td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td style="font-size:16px">
            අධිකරණ නියෝගය පිලිබඳ විස්තර<br/>
            நீதிமன்ற கட்டளை சம்பந்தமான விபரங்கள்
            Information about the Adoption Order
        </td>
    </tr>
</table>

<table border="1" width="99%" style="margin-top:10px;float:left;border:1px solid #000; border-collapse:collapse;">
    <caption></caption>
    <col width="327"/>
    <col/>
    <tbody>
    <tr>
        <td height="40px">
            අධිකරණය
            <br>நீதிமன்றம்
            <br>Court
        </td>
        <td colspan="3"><s:label name="courtName"/></td>
    </tr>
    <tr>
        <td height="40px">
            නියෝගය නිකුත් කල දිනය
            <br>கட்டளை வழங்கப்பட்ட திகதி
            <br>Issued Date
        </td>
        <td><s:label name="" value="%{adoption.orderIssuedDate}"/></td>
        <td width="250px">
            නියෝග අංකය
            <br>கட்டளை இலக்கம்
            <br>Serial number
        </td>
        <td width="250px"><s:label name="" value="%{adoption.courtOrderNumber}"/></td>
    </tr>
    </tbody>
</table>


<table style="width:1000px; text-align:left;border:none; margin-top:15px;margin-bottom:125px;">
    <tr>
        <td>
            මුද්දර ගාස්තු (එක පිටපතක් සඳහා රු. 25/- වටිනා මුද්දර අලවන්න)
            <br>முத்திரை தீர்வை ( ஒரு பிரதிக்கு ரூ. 25/- பெறுமதியான முத்திரையினை ஒட்டவும்)
            <br>Stamp fee (affix Rs 25/= stamps per copy).
        </td>
    </tr>
</table>

<table border="1" width="99%" style="margin-top:10px;float:left;border:1px solid #000; border-collapse:collapse;">
    <caption></caption>
    <col width="327"/>
    <col/>
    <tbody>
    <tr>
        <td height="40px">
            දිනය
            <br>திகதி
            <br>Date
        </td>
        <td></td>
        <td height="40px" width="250px">
            අයදුම්කරුගේ අත්සන
            <br>விண்ணப்பதாரரது கையொப்பம்
            <br>Signature of Applicant
        </td>
        <td width="250px"></td>
    </tr>
    </tbody>
</table>
<br><br><br><br><br>
</div>
<s:hidden id="zonalOfficeId" name="zonalOfficeId"/>
