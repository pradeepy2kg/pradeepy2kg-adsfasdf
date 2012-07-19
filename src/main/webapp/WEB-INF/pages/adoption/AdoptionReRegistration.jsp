<%@ page import="java.util.Date" %>
<%@ page import="lk.rgd.common.util.DateTimeUtils" %>
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
        $('#zonalOfficeId').bind('change', function () {
            var zonalOfficeId = $('#zonalOfficeId').val();
            populateZonalOfficeAddress(zonalOfficeId);
        });
    });

    function populateZonalOfficeAddress(zonalOfficeId) {
        /* Clear current Zonal Office Addresses. */
        $('#siZonalOfficeAddress').val('');
        $('#taZonalOfficeAddress').val('');
        $('#enZonalOfficeAddress').val('');
        $.getJSON('/ecivil/crs/ZonalOfficeLookupService', {zonalOfficeId:zonalOfficeId}, function (data) {
            var siAddress = data.siAddress;
            var taAddress = data.taAddress;
            var enAddress = data.enAddress;
            /* Set New Zonal Office Addresses. */
            $('#siZonalOfficeAddress').val(siAddress);
            $('#taZonalOfficeAddress').val(taAddress);
            $('#enZonalOfficeAddress').val(enAddress);
        });
    }
</script>

<div class="form-submit">
    <s:if test="#request.approved">
        <s:url id="markAsPrint" action="eprMarkDirectlyAdoptionNoticeAsPrinted.do">
            <s:param name="idUKey" value="%{#request.idUKey}"/>
        </s:url>
        <s:url id="cancel" action="cancelPrintAdoptionNotice.do">
            <s:param name="idUKey" value="%{#request.idUKey}"/>
            <s:param name="approved" value="#request.approved"/>
        </s:url>
    </s:if>
    <s:else>
        <s:url id="markAsPrint" action="eprMarkAdoptionNoticeAsPrinted.do">
            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
            <s:param name="currentStatus" value="%{#request.currentStatus}"/>
            <s:param name="pageNo" value="%{#request.pageNo}"/>
            <s:param name="idUKey" value="%{#request.idUKey}"/>
        </s:url>
        <s:url id="cancel" action="eprAdoptionBackToPreviousState.do">
            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
            <s:param name="pageNo" value="%{#request.pageNo}"/>
            <s:param name="currentStatus" value="%{#request.currentStatus}"/>
        </s:url>
    </s:else>
</div>
<div class="form-submit">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
</div>
<div id="adoption-page" class="form-submit">
    <s:a href="%{markAsPrint}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
    <s:a href="%{cancel}"><s:label value="%{getText('cancel.label')}"/></s:a>
</div>
<div id="adoption-registration-form-outer">
<div id="page1" style="page-break-after:always;">
    <div id="zonal_office_list">
        <fieldset style="border:2px solid #c3dcee;">
            <table>
                <tr>
                    <td><s:label value="%{getText('select_zonal_office.label')}"/></td>
                    <td>
                        <s:select list="zonalOfficeList" id="zonalOfficeId" name="zonalOfficeId"/>
                    </td>
                </tr>
            </table>
        </fieldset>
    </div>
    <table class="adoption-reg-form-header-table" width="99%">
        <tr>
            <td align="center" style="font-size:15pt">
                <img src="<s:url value="/images/official-logo.png" />" alt=""><br>
                <label>
                    රෙජිස්ට්‍රාර් ජෙනරාල් දෙපාර්තමේන්තුව
                    <br>பதிவாளா் நாயகம் திணைக்களம்
                    <br>Registrar General's Department
                </label>

            </td>
        </tr>
        <tr style="text-align:right;font-size:10pt;">
            <td><s:label><%= DateTimeUtils.getISO8601FormattedString(new Date()) %>
            </s:label></td>
        </tr>
        <tr height="50px" style="text-align:left;font-size:10pt">
            <td><s:label value="%{adoption.applicantName}"/></td>
        </tr>
        <tr height="30px" style="text-align:left;font-size:10pt">
            <td><s:label value="%{adoption.applicantAddress}"/></td>
        </tr>
        <tr>
            <td align="left">
                <br>මහත්මයාණෙනි / මහත්මියනි
                <br>கனம் ஐயா/ அம்மணி
                <br>Dear Sir / Madam
            </td>
        </tr>
        <tr>
            <td style="font-size:13pt;">
                දරුකමට හදාගත් ළමයෙකුගේ උපත නැවත ලියාපදිංචි කිරීම
                <br> மகவேற்புச் செய்யப்பட்ட பிள்ளையின் பிறப்பினை மீண்டும் பதிவு செய்தல்
                <br>Re-registration of the birth of an Adopted Child
            </td>
        </tr>
        <tr>
            <td align="left">
                <br>ළමයින් දරුකමට හදා ගැනීමේ ආඥා පනත (අධිකාරය 61) යටතේ පහත සඳහන් උසාවි නියෝගය මෙම කාර්යාලයට ලැබී ඇත.
                <br>மகவேற்புக் கட்டளைச்சட்டத்தின் ( 16ஆம் அதிகாரம்) கீழ் குறிப்பிடப்பட்ட நீதிமன்ற கட்டளை
                இக்காரியலயத்திற்கு கிடைக்கப்பெற்றது.
                <br>Under section 61 of the Adoption of Children Ordinance, we have received the following Adoption
                order.
            </td>
        </tr>
    </table>

    <table cellspacing="0" cellpadding="0" border="1" width="99%"
           style="margin-top:10px;float:left;border:1px solid #000; border-collapse:collapse;">
        <tr>
            <td width="330px" height="60px">
                අධිකරණය
                <br>நீதிமன்றம்
                <br>Court
            </td>
            <td><s:label name="courtName"/></td>
        </tr>
        <tr>
            <td height="60px">
                නියෝගය නිකුත් කල දිනය
                <br>கட்டளை வழங்கப்பட்ட திகதி
                <br>Issued Date
            </td>
            <td><s:label name="" value="%{adoption.orderIssuedDate}"/>
            </td>
        </tr>
        <tr>
            <td height="60px">
                නියෝග අංකය
                <br>கட்டளை இலக்கம்
                <br>Serial number
            </td>
            <td><s:label name="" value="%{adoption.courtOrderNumber}"/>
        </tr>
        <tr>
            <td height="60px">
                විනිසුරු නම
                <br>நீதிபதியின் பெயா்
                <br>Name of the Judge
            </td>
            <td colspan="4"><s:label name="" value="%{adoption.judgeName}"/>
            </td>
        </tr>
    </table>


    <table class="adoption-reg-form-header-table" width="99%">
        <tr>
            <td align="left">
                මෙම නියෝගය පහත සඳහන් අනුක්‍රමික අංකය යටතේ ලියාපදිංචි වී තිබේ:
                <br>இக்கட்டளை கீழ் குறிப்பிட்ட தொடா் இலக்கத்தின் கீழ் பதியப்பட்டுள்ளது.
                <br>This has been registered with the following serial number:
            </td>
        </tr>
    </table>

    <table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0" width="99%">
        <tr>
            <td height="60px" width="330px">
                <label>
                    ලියාපදිංචි කිරීමේ අනුක්‍රමික අංකය
                    <br>பதிவு செய்தலின் தொடா் இலக்கம்
                    <br>Serial number of the registration
                </label>
            </td>
            <td>
                <s:label value="%{adoption.idUKey}"/>
            </td>
        </tr>
    </table>

    <ol>
        <li style="font-size:13px">
            <u style="font-size:15px">
                ලියාපදිංචි දරු ලේඛනයේ සහතික පිටපතක් (දරු සහතිකය) ලබාගැනීම
                <br>பதிவு செய்யப்பட்ட மகவேற்புப் பதிவேட்டின் பிரதியினை ( மகவேற்புச் சான்றிதழ்) பெற்றுக் கொள்ளல்
                <br>Obtaining a copy of the Certificate of Adoption </u>

            <br><br>මේ සමග ඒවා ඇති දරු සහතික ඉල්ලුම් පත්‍රය නිසි පරිදි සම්පුර්ණ කර රු 25/- ක් වටිනා වලංගු මුද්දර එහි
            අලවා
            පහත සඳහන් ලිපිනයට එවන්න. ඔබගේ අයදුම්පත්‍රය සමග ඔබගේ නිවැරදි ලිපිනය සඳහන්, නියමිත තැපැල් ගාස්තු සඳහා
            මුද්දර අලවන ලද දිග ලියුම් කවරයක් ද අමුණ එවන්න.
            <br>இத்துடன் அனுப்பப்பட்டுள்ள மகவேற்புச் சான்றிதழ் விண்ணப்பப்படிவத்தினை உரியவாறு பூா்த்தி செய்து 25/-
            ரூபா பெறுமதியான செல்லுப்படியாகும் முத்திரையினை அதில் ஒட்டி கீழ் குறிப்பிடப்பட்டுள்ள முகவரிக்கு
            அனுப்பவும். தங்களது விண்ணப்பப்படிவத்துடன் தங்களது சரியான முகவரியினை குறிப்பிட்டு உரிய தபாற் கட்டணம்
            சம்பந்தமான முத்திரையினை ஒட்டி நீண்ட தபாலுறையினை யும் இணைத்து அனுப்பவும்.
            <br>Fill the attached application form including stamps to the value of Rs. 25/- to obtain a certified
            copy of the Certificate of Adoption. Send the completed form to the following address, along with a self
            addressed stamped envelope.
            <br><br>
            <table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0" width="99%">
                <tr>
                    <td height="90px">
                        <label>
                            රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව ,
                            <br>234 / A 3, ඩෙන්සිල් කොබ්බැකඩුව මාවත,
                            <br>බත්තරමුල්ල
                        </label>
                    </td>
                    <td>
                        <label>
                            பதிவாளா் நாயகம் திணைக்களம்,
                            <br>234/A3, டென்சில் கொப்பேகடுவ மாவத்தை,
                            <br>பத்தரமுல்லை.
                        </label>
                    </td>
                    <td>
                        <label>
                            Registrar Generals Department
                            <br>234 / A3, Denzil Kobbakaduwa Mawatha,
                            <br>Battaramulla
                        </label>
                    </td>
                </tr>
            </table>
        </li>
    </ol>
</div>

<div id="page2" style="page-break-after:always;">
    <ol start="2">
        <li style="font-size:13px">
            <u style="font-size:15px">
                දරුවා හදාගන්නා මව්පියන් දරුවාගේ ජනක මව්පියන් වශයෙන් ඇතුලත් කර දරුවාගේ උපත නැවත ලියාපදිංචි කිරීම
                <br>மகவேற்பு செய்யும் பெற்றோர்களை பிள்ளையின் சொந்த பெற்றோராக உட்புகுத்துவதற்கு பிறப்பினை மீள பதிவு
                செய்தல்
                <br>Re-registration of the birth by including the particulars of adopted parents as the birth parents
                <br>
            </u><br>
            ඉහත සඳහන් ආකාරයෙන් ලබාගත් දරු සහතිකයේ සඳහන් විවාහක අඹු සැමියන්ගේ නම්, මෙම දරුවාගේ ජනක මව්පියන් වශයෙන් ඇතුලත්
            කර දරුවාගේ උපත නැවත ලියාපදිංචි කිරීම සඳහා මේ සමග ඒවා ඇති ප්‍රකාශ පත්‍රය නිසි පරිදි සම්පුඋර්ණ කර රු. 5/- ක්
            වටින මුද්දරයක් මත, සමාදාන විනිශ්‍යකරවරයකු ඉදිරිපිට දී මව සහ පියාගේ යන දෙදෙනාගේම අත්සන් තබා, පහත සඳහන්
            ලියවිලි වල පිටපත් සමග එවන්න.
            <ol>
                <li>ඉහත සඳහන් ආකාරයෙන් ලබාගත් දරු සහතිකය</li>
                <li>ළමයා හදා වඩා ගන්නා මව්පියන්ගේ විවාහ සහතිකය</li>
                <li>මව්පියන්ගේ උප්පැන්න සහතික</li>
                <li>දරුවාගේ පැරණි උප්පැන සහතිකය (තිබේනම්)</li>
            </ol>
            <br>
            மேற்குறிப்பிடப்பட்ட வகையில் பெறப்பட்ட மகவேற்புச்சான்றிதழில் குறிப்பிடப்பட்ட கணவன் மனைவியினது பெயா்
            குறிப்பிட்ட
            பிள்ளையின் சொந்த பெற்றோர் எனும் வகையில் உட்புகுத்தி பிள்ளையின் பிறப்பினை மீள பதிவு செய்வதற்காக இத்துடன்
            அனுப்பப்பட்டுள்ள மகவேற்புச் சான்றிதழ் விண்ணப்பப்படிவத்தினை உரியவாறு பூா்த்தி செய்து 5/- ரூபா பெறுமதியான
            முத்திரையின் மேல் சமாதான நீதவான் முன்னிலையில் தாய் மற்றும் தந்தை ஆகிய இருவரும் கையொப்பமிட்டு கீழ்
            குறிப்பிடப்படும் ஆவணங்களின் பிரதிகளுடன் அனுப்பவும்.
            <ol>
                <li>மேற்குறிப்பிடப்பட்ட விதத்தில் பெறப்பட்ட மகவேற்புச் சான்றிதழ்</li>
                <li>மகவேற்புச் செய்யும் பெற்றோரின் விவாகச் சான்றிதழ்.</li>
                <li>பெற்றோரின் பிறப்புச் சான்றிதழ்.கள்.</li>
                <li>பிள்ளையின் பழைய பிறப்புச் சான்றிதழ் (இருந்தால்)</li>
            </ol>
            <br>
            Attach a copy of the Certificate of Adoption obtained as detailed above in Step #1, and fill the attached
            form
            for the re-registration of the birth, and send it along to the following address. Both parents must sign the
            application form in front of a Justice of Peace, attaching a stamp of Rs. 5/=. Include copies of the
            following
            documents along with the application.
            <ol>
                <li>Certificate of Adoption</li>
                <li>Certificate of Marriage of parents</li>
                <li>Certificates of Birth of parents</li>
                <li>Original Certificate of Birth of Child (if available)</li>
            </ol>
            <br>
            <table id="zonal_office_address" class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0" width="99%">
                <tr>
                    <td height="90px" width="33%">
                        <s:textarea id="siZonalOfficeAddress" value="%{siZonalOfficeAddress}" rows="4" disabled="true"
                                    cssStyle="color: #000; background: none; border: none; resize: none; font-size: 10pt;"/>
                    </td>
                    <td width="33%">
                        <s:textarea id="taZonalOfficeAddress" value="%{taZonalOfficeAddress}" rows="4" disabled="true"
                                    cssStyle="color: #000; background: none; border: none; resize: none; font-size: 10pt;"/>
                    </td>
                    <td>
                        <s:textarea id="enZonalOfficeAddress" value="%{enZonalOfficeAddress}" rows="4" disabled="true"
                                    cssStyle="color: #000; background: none; border: none; resize: none; font-size: 10pt;"/>
                    </td>
                </tr>
            </table>
        </li>
    </ol>
    <table style="float:right;" width="99%">
        <tr>
            <td align="right">
                <br><br><br> මෙයට විශ්වාසී
                <br>தங்கள் விசுவாசமுள்ள
                <br>Yours Faithfully
            </td>
        </tr>
        <tr>
            <td align="right">
                <br><br><br>රෙජිස්ට්‍රාර් ජනරාල් වෙනුවට
                <br> பதிவாளா் நாயகத்திற்கு பதிலாக
                <br>For, Registrar General
            </td>
        </tr>

    </table>
</div>

<table class="adoption-reg-form-header-table" width="99%">
    <tr>
        <td style="font-size:18px">
            රෙජිස්ට්‍රාර් ජෙනරාල් දෙපාර්තමේන්තුව / பதிவாளா் நாயகம் திணைக்களம்/Registrar General's Department
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
<table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0" style="float:right;width:450px; margin-right: 10px;">
    <tr>
        <td height="40px" width="250px">
            <label>
                ලියාපදිංචි කිරීමේ අනුක්‍රමික අංකය
                <br>பதிவு செய்தல் தொடா் இலக்கம்
                <br>Serial number of the registration
            </label>
        </td>
        <td width="200px">

            <s:label value="%{adoption.idUKey}"/>

        </td>
    </tr>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td style="font-size:16px">අයදුම් කරුගේ විස්තර / விண்ணப்பதாரியின் விபரங்கள்/ Applicants Details
        </td>
    </tr>
</table>


<table border="1" width="99%" style="float:left;border:1px solid #000; border-collapse:collapse;">
    <caption></caption>
    <col width="110px"/>
    <col width="110px"/>
    <col width="110px"/>
    <col width="110px"/>
    <col width="310px"/>
    <col width="310px"/>
    <tbody>
    <tr>
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
            වෙනත් (කවුරුන්දැයි සටහන් කරන්න)
            <br>வேறு (யார் என குறிப்பிடவும்)
            <br>Other (Specify whom)
        </td>
        <td></td>
    </tr>
    <tr>
        <td colspan="3" height="40px">
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification Number
        </td>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td rowspan="3" colspan="3" height="120px">
            නම
            <br>விண்ணப்பதாரியின் பெயா்
            <br>Name of the Applicant
        </td>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td colspan="3"></td>
    </tr>

    <tr>
        <td colspan="3" rowspan="4" height="160px">
            ලිපිනය
            <br>முகவரி
            <br>Address
        </td>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td colspan="3"></td>
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
        <td width="250px"><s:label name="" value="%{genderSi}"/></td>
    </tr>
    </tbody>
</table>

<table class="adoption-reg-form-header-table">
    <tr>
        <td style="font-size:16px">
            අධිකරණ නියෝගය පිලිබඳ විස්තර<br/>
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


<table style="width:1000px; text-align:left;border:none; margin-top:15px;margin-bottom:15px;">
    <tr>
        <td>
            මුද්දර ගාස්තු (එක පිටපතක් සඳහා රු. 25/- වටිනා මුද්දර අලවන්න)
            <br>முத்திரை தீா்வை ( ஒரு பிரதிக்கு ரூ. 25/- பெறுமதியான முத்திரையினை ஒட்டவும்)
            <br>Paste Stamps (Rs. 25/- per copy)
        </td>
    </tr>
</table>

<table border="1" width="99%" style="margin-top:10px;float:left;border:1px solid #000; border-collapse:collapse;">
    <caption></caption>
    <col width="327"/>
    <col/>
    <tbody>
    <tr>
        <td height="40px" width="330px">
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
<div class="form-submit">
    <s:submit type="button" value="%{getText('print.button')}" onclick="printPage()"/>
</div>
<div id="adoption-page" class="form-submit">
    <s:a href="%{markAsPrint}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
    <s:a href="%{cancel}"><s:label value="%{getText('cancel.label')}"/></s:a>
</div>
