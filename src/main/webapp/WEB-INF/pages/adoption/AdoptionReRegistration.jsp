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
    <s:if test="#request.approved">
        <s:url id="markAsPrint" action="eprMarkDirectlyAdoptionNoticeAsPrinted.do">
            <s:param name="idUKey" value="%{#request.idUKey}"/>
        </s:url>
        <s:url id="cancel" action="cancelPrintAdoptionNotice.do">
            <s:param name="idUKey" value="%{#request.idUKey}"/>
            <s:param name="approved" value="#request.approved"/>
        </s:url>
        <s:url id="viewAdoptionOrderDetails" action="eprAdoptionOrderDetailsViewMode.do">
            <s:param name="idUKey" value="idUKey"/>
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
        <s:url id="viewAdoptionOrderDetails" action="eprAdoptionOrderDetailsViewMode.do">
            <s:param name="idUKey" value="idUKey"/>
            <s:param name="currentStatus" value="%{#request.currentStatus}"/>
            <s:param name="pageNo" value="%{#request.pageNo}"/>
            <s:param name="nextFlag" value="%{#request.nextFlag}"/>
            <s:param name="previousFlag" value="%{#request.previousFlag}"/>
        </s:url>
    </s:else>
</div>
<div id="adoption-page" class="form-submit">
    <s:if test="adoption.status.ordinal() >= 2">
        <s:a href="%{markAsPrint}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
        <s:a href="#" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
    </s:if>
    <s:if test="adoption.status.ordinal() < 4">
        <s:a href="%{viewAdoptionOrderDetails}"><s:label value="%{getText('order_details.label')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
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
    <tr style="text-align:right;font-size:10pt;">
        <td><s:label><%= DateTimeUtils.getISO8601FormattedString(new Date()) %>
        </s:label></td>
    </tr>
    <tr height="50px" style="text-align:left;font-size:10pt">
        <td><s:label value="%{adoption.applicantName}"/> මයා/ මිය</td>
    </tr>
    <tr height="30px" style="text-align:left;font-size:10pt">
        <td><s:textarea value="%{adoption.applicantAddress}"
                        cssStyle="resize: none; color: #000; background: #fff; border: none; width: 100%;" rows="5"
                        disabled="true"/></td>
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
            <s:if test="adoption.jointApplicant">
                දරුකමට හදාගත් ළමයෙකු සඳහා දරු සහතික නිකුත් කිරීම සහ උපත නැවත ලියාපදිංචි කිරීම.
                <br/>மகவேற்பு செய்யப்பட்ட பிள்ளைக்கான மகவேற்புச்சான்றிதழினை வழங்கல் மற்றும் பிறப்பினை மீள பதிவு செய்தல்
                <br/>Issuing Adoption Certificate and Re-Registration of Birth for Adopted Child.
            </s:if>
            <s:else>
                දරුකමට හදාගත් ළමයෙකු සඳහා දරු සහතික නිකුත් කිරීම.
                <br>﻿மகவேற்பு செய்யப்பட்ட பிள்ளைக்கான மகவேற்புச்சான்றிதழினை வழங்கல்
                <br>Issuing Adoption Certificate for an Adopted Child.
            </s:else>
        </td>
    </tr>
    <tr>
        <td align="left">
            <br>ළමයින් දරුකමට හදා ගැනීමේ ආඥා පනත (61 අධිකාරය) යටතේ පහත සඳහන් උසාවි නියෝගය මෙම කාර්යාලයට ලැබී ඇත.
            <br>மகவேற்புக் கட்டளைச்சட்டத்தின் ( 16ஆம் அதிகாரம்) கீழ் குறிப்பிடப்பட்ட நீதிமன்ற கட்டளை
            இக்காரியலயத்திற்கு கிடைக்கப்பெற்றது.
            <br>The following Adoption Order received by this office under Adoption Ordinance (Chapter 61).
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
            <br>Date of issue
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
            විනිසුරුගේ නම
            <br>நீதிபதியின் பெயர்
            <br>Name of the Judge
        </td>
        <td colspan="4"><s:label name="" value="%{adoption.judgeName}"/>
        </td>
    </tr>
</table>


<table class="adoption-reg-form-header-table" width="99%">
    <tr>
        <td align="left">
            මෙම නියෝගය පහත සඳහන් සහතික පත්‍ර අංකය යටතේ ලියාපදිංචි වී තිබේ:
            <br>இக்கட்டளை கீழ் குறிப்பிட்ட தொடர் இலக்கத்தின் கீழ் பதியப்பட்டுள்ளது.
            <br>This order is registered under the following certificate number:
        </td>
    </tr>
</table>

<table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0" width="99%">
    <col width="330px"/>
    <col width="130px"/>
    <col width="330px"/>
    <col/>
    <tr>
        <td height="60px">
            <label>
                සහතික පත්‍රයේ අංකය
                <br>சான்றிதழ் இல
                <br>Certificate Number
            </label>
        </td>
        <td colspan="3">
            <s:label value="%{adoption.idUKey}"/>
        </td>
    </tr>
    <tr>
        <td>
            ලියාපදිංචි කිරීමේ අංකය<br>
            Registration No in ta<br>
            Registration Number
        </td>
        <td>
            <s:label value="%{adoption.adoptionEntryNo}"/>
        </td>
        <td>
            ලියාපදිංචි කිරීමේ දිනය
            <br>Date of Registration in ta
            <br>Date of Registration
        </td>
        <td>
            <s:label value="%{adoption.orderReceivedDate}"/>
        </td>
    </tr>
</table>

<ol>
    <li style="font-size:13px">
        <u style="font-size:15px">
            ලියාපදිංචි දරු ලේඛනයේ සහතික පිටපතක් (දරු සහතිකය) ලබාගැනීම
            <br>பதிவு செய்யப்பட்ட மகவேற்புப் பதிவேட்டின் பிரதியினை ( மகவேற்புச் சான்றிதழ்) பெற்றுக் கொள்ளல்
            <br>Obtaining a copy of the Certificate of Adoption </u>

        <br><br>මේ සමඟ එවා ඇති දරු සහතික ඉල්ලුම් පත්‍රය නිසි පරිදි සම්පූර්ණ කර පහත සඳහන් කාර්යාලයට ගොස් රු. 100 ක්
        ගෙවා දරු සහතිකය ලබා ගත හැක. තැපැල් මගින් යොමු කරන්නේ නම්, රෙජිස්ට්‍රාර් ජනරාල්ගේ ලංකා බැංකුවේ පිටකොටුව
        ශාඛාවේ අංක 7041650 දරණ ගිණුමට මුදල් බැර කර, ලදුපත සහ අයදුම්පත අමුණා එවිය යුතුය. තවද ලිපිනය සහිත කවරයක්
        අයදුම්පත සමඟ ඉදිරිපත් කළ යුතු අතර, ලියාපදිංචි/ සාමාන්‍ය තැපෑලට සරිලන මුද්දර, කවරයට අලවා එවිය යුතුය.
        <br/>
        <s:if test="!adoption.jointApplicant">
            <br/><strong> සැ. යු. - </strong>දරු නියෝගය ගරු අධිකරණයෙන් නිකුත් කර ඇත්තේ විවාහක දෙමාපියන් වෙත නොවන බැවින් මෙම
            දරු උපත නැවත ලියාපදිංචි කිරීමට 1977 අංක 06 දරණ දරුකමට හදා ගැනීමේ සංශෝධිත පනතින් ප්‍රතිපාදන සැලසී නොමැත.
            එබැවින් දරු සහතිකය උප්පැන්නයක් ලෙස භාවිතා කිරීමට සිදු වන බව දන්වා සිටිමි.<br/>
        </s:if>
        <br/>இத்துடன் அனுப்பப்பட்டுள்ள மகவேற்புச் சான்றிதழ் விண்ணப்பத்தினை உரிய விதத்தில் பூர்த்தி செய்து கீழ்
        குறிப்பிடப்படும் அலுவலகத்திற்குச் சென்று ரூ.100 பணத்தினை செலுத்தி மகவேற்புச் சான்றிதழினை பெற்றுக் கொள்ள
        முடியும். தபால் மூலம் சமர்ப்பிக்கப்படுமாயின், பதிவாளர் நாயகத்தின் இலங்கை வங்கியின் புறக்கோட்டை கிளையின் இலக்கம்
        7041650 உடைய கணக்கிற்கு வரவு வைத்து. பற்றுச்சீட்டு மற்றும் விண்ணப்பத்தினை இணைத்தனுப்பல் வேண்டும். மேலும்
        முகவரியிடப்பட்ட கடித உறையினை விண்ணப்பத்துடன் சமரப்பித்தல் வேண்டியதுடன். பதிவு/ சாதாரண தபாலுக்குரிய முத்திறையினை
        உறையில் ஒட்டி அனுப்பல் வேண்டும்.
        <br/>
        <s:if test="!adoption.jointApplicant">
            <br/><strong> கவனிக்குக:- </strong>
            மகவேற்புக் கட்டளை கௌரவ நீதிமன்றத்தினால் வழங்கப்பட்டது திருமணமாகிய திறத்தாருக்கல்லாது விடின் இப் பிறப்பினை மீள பதிவு செய்வதற்கு 1977 ஆம் ஆண்டின் 06 ஆம் இலக்க மகவேற்புக் (திருத்தற்) கட்டளைச் சட்டத்தில் ஏற்பாடுகள் இல்லை. ஆகையினால் மகவேற்புச் சான்றிதழினை பிறப்புச் சான்றிதழாக உபயோகிப்பதற்கு ஏற்படும் என அறிவிக்கின்றேன்.
            <br/>

            <div style="page-break-after:always;"></div> <br/><br/><br/>
        </s:if>
        <br>The adoption certificate can be obtained from the following office by paying Rs. 100/= along with the
        duly completed adoption application which attached herewith. If submit by post, payment should be made by
        crediting Registrar General's Account bearing No. 7041650 in Bank of Ceylon Pettah Branch and the
        application should be sent along with the recipt. A self addressed stamped envelope should be provided with
        the application and the relevant stamps should be affixed for registered/ normal postage.
        <br/><br/>
        <s:if test="!adoption.jointApplicant">
            <strong>N.B:-</strong>
            As the adoption order has not been issued to the married parties by the courts, there is no provisions to
            re-registration of birth in the amended <strong>Adoption act No.06 in 1977</strong>. Therefore this adoption
            certificate has to be used as a Birth Certificate.
        </s:if>
        <br/><br/>
<s:if test="adoption.jointApplicant">
    <div style="page-break-after:always;"></div> <br/><br/><br/>
</s:if>
        <table class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0" width="99%">
            <tr>
                <td height="90px">
                    <label>
                        රෙජිස්ට්‍රාර් ජනරාල්,
                        <br/>රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව ,
                        <br>234 / A 3, ඩෙන්සිල් කොබ්බැකඩුව මාවත,
                        <br>බත්තරමුල්ල
                        <br/>දුරකථන අංක: 0112889488/89
                    </label>
                </td>
                <td>
                    <label>
                        பதிவாளர் நாயகம்
                        <br/>பதிவாளர் நாயகம் திணைக்களம்
                        <br>234/A3, டென்சில் கொப்பேகடுவ மாவத்தை,
                        <br>பத்தரமுல்லை.
                        <br/>Telephone Numbers in ta: 0112889488/89
                    </label>
                </td>
                <td>
                    <label>
                        Registrar General,
                        <br/>Registrar Generals Department
                        <br>234 / A3, Denzil Kobbakaduwa Mawatha,
                        <br>Battaramulla
                        <br/>Telephone Numbers: 0112889488/89
                    </label>
                </td>
            </tr>
        </table>
    </li>
</ol>
</div>
<div id="page2">
    <br/>
    <s:if test="adoption.jointApplicant">
        <ol start="2">
            <li style="font-size:13px">
                <u style="font-size:15px">
                    දරුවා හදාගන්නා මව්පියන් දරුවාගේ ජනක මව්පියන් වශයෙන් ඇතුලත් කර දරුවාගේ උපත නැවත ලියාපදිංචි කිරීම
                    <br>மகவேற்பு செய்யும் பெற்றோர்களை பிள்ளையின் சொந்த பெற்றோராக உட்புகுத்துவதற்கு பிறப்பினை மீள பதிவு
                    செய்தல்
                    <br>Re-registration of the birth by including the particulars of adopted parents as the birth
                    parents
                    <br>
                </u><br>
                ඉහත සඳහන් ආකාරයෙන් ලබාගත් දරු සහතිකයේ සඳහන් විවාහක අඹු සැමියන්ගේ නම්, මෙම දරුවාගේ ජනක මව්පියන් වශයෙන්
                ඇතුලත්
                කර දරුවාගේ උපත නැවත ලියාපදිංචි කිරීම සඳහා මේ සමග ඒවා ඇති ලි. ප. අ. 149 දරණ ප්‍රකාශ පත්‍රය නිසි පරිදි
                සම්පූර්ණ කර රු. 5/- ක්
                වටිනා මුද්දරයක් මත, සමාදාන විනිශ්චයකාරවරයකු ඉදිරිපිට දී මව සහ පියා යන දෙදෙනාගේම අත්සන් තබා, පහත සඳහන්
                ලියවිලි සමග එවන්න
                <ol>
                    <li>ඉහත සඳහන් ආකාරයෙන් ලබාගත් දරු සහතිකය</li>
                    <li>ළමයා හදා වඩා ගන්නා මව්පියන්ගේ විවාහ සහතිකය</li>
                    <li>මව්පියන්ගේ උප්පැන්න සහතික</li>
                    <li>දරුවාගේ පැරණි උප්පැන්න සහතිකය (තිබේනම්)/ රෝහලේ උපත් විස්තරය</li>
                </ol>
                <br/>
                <br/>
                மேற்குறிப்பிடப்பட்ட வகையில் பெறப்பட்ட மகவேற்புச்சான்றிதழில் குறிப்பிடப்பட்ட கணவன் மனைவியினது பெயர்
                குறிப்பிட்ட
                பிள்ளையின் சொந்த பெற்றோர் எனும் வகையில் உட்புகுத்தி பிள்ளையின் பிறப்பினை மீள பதிவு செய்வதற்காக இத்துடன்
                அனுப்பப்பட்டுள்ள மகவேற்புச் சான்றிதழ் விண்ணப்பப்படிவத்தினை உரியவாறு பூர்த்தி செய்து 5/- ரூபா பெறுமதியான
                முத்திரையின் மேல் சமாதான நீதவான் முன்னிலையில் தாய் மற்றும் தந்தை ஆகிய இருவரும் கையொப்பமிட்டு கீழ்
                குறிப்பிடப்படும் ஆவணங்களின் பிரதிகளுடன் அனுப்பவும்.
                <ol>
                    <li>மேற்குறிப்பிடப்பட்ட விதத்தில் பெறப்பட்ட மகவேற்புச் சான்றிதழ்</li>
                    <li>மகவேற்புச் செய்யும் பெற்றோரின் விவாகச் சான்றிதழ்.</li>
                    <li>பெற்றோரின் பிறப்புச் சான்றிதழ்கள்.</li>
                    <li>பிள்ளையின் பழைய பிறப்புச் சான்றிதழ் (இருந்தால்)</li>
                </ol>
                <br/>
                <br/>
                Attach a copy of the Certificate of Adoption obtained as detailed above in Step #1, and fill the
                attached
                form
                for the re-registration of the birth, and send it along to the following address. Both parents must sign
                the
                application form in presence of a Justice of Peace, affixed a stamp of Rs. 5/=. Include copies of the
                following
                documents along with the application.
                <ol>
                    <li>Certificate of Adoption</li>
                    <li>Certificate of Marriage of parents</li>
                    <li>Certificates of Birth of parents</li>
                    <li>Previous Certificate of Birth of Child (if available)</li>
                </ol>
                <br/>
                <br/>
                <table id="zonal_office_address" class="adoption-reg-form-03-table01" cellspacing="0" cellpadding="0"
                       width="99%">
                    <tr>
                        <td height="90px" width="33%">
                            <s:textarea id="siZonalOfficeAddress" rows="7" disabled="true"
                                        cssStyle="color: #000; background: none; border: none; resize: none; font-size: 10pt; text-transform: capitalize;"/>
                        </td>
                        <td width="33%">
                            <s:textarea id="taZonalOfficeAddress" rows="7" disabled="true"
                                        cssStyle="color: #000; background: none; border: none; resize: none; font-size: 10pt; text-transform: capitalize;"/>
                        </td>
                        <td>
                            <s:textarea id="enZonalOfficeAddress" rows="7" disabled="true"
                                        cssStyle="color: #000; background: none; border: none; resize: none; font-size: 10pt; text-transform: capitalize;"/>
                        </td>
                    </tr>
                </table>
            </li>
        </ol>
    </s:if>
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
                <br> பதிவாளர் நாயகத்திற்கு பதிலாக
                <br>For, Registrar General
            </td>
        </tr>

    </table>
</div>
<br/>

<div style="page-break-after:always;"></div>
<br/><br/><br/>
<table class="adoption-reg-form-header-table" width="99%">
    <tr>
        <td style="font-size:18px">
            රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව
            <br/>பதிவாளர் நாயகம் திணைக்களம்
            <br/>Registrar General's Department
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
       <%-- <td>
            මුද්දර ගාස්තු (එක පිටපතක් සඳහා රු. 25/- වටිනා මුද්දර අලවන්න)
            <br>முத்திரை தீர்வை ( ஒரு பிரதிக்கு ரூ. 25/- பெறுமதியான முத்திரையினை ஒட்டவும்)
            <br>Stamp fee (affix Rs 25/= stamps per copy).
        </td>--%>
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
<div id="adoption-page" class="form-submit">
    <s:if test="adoption.status.ordinal() >= 2">
        <s:a href="%{markAsPrint}"><s:label value="%{getText('mark_as_print.button')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
        <s:a href="#" onclick="printPage()"><s:label value="%{getText('print.button')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
    </s:if>
    <s:if test="adoption.status.ordinal() < 4">
        <s:a href="%{viewAdoptionOrderDetails}"><s:label value="%{getText('order_details.label')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
    </s:if>
    <s:a href="%{cancel}"><s:label value="%{getText('cancel.label')}"/></s:a>
</div>
<s:hidden id="zonalOfficeId" name="zonalOfficeId"/>
