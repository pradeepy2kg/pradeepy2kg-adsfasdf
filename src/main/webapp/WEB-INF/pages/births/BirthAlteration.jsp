<%--@author Tharanga Punchihewa--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
    $(function() {
        $('#mother-info-min').click(function() {
            minimize("mother-info");
        });
        $('#mother-info-max').click(function() {
            maximize("mother-info");
        });
        $('#informant-info-min').click(function() {
            minimize("informant-info");
        });
        $('#informant-info-max').click(function() {
            maximize("informant-info");
        });
        $('#father-info-min').click(function() {
            minimize("father-info");
        });
        $('#father-info-max').click(function() {
            maximize("father-info");
        });
        $('#marriage-info-min').click(function() {
            minimize("marriage-info");
        });
        $('#marriage-info-max').click(function() {
            maximize("marriage-info");
        });
        $('#grandFather-info-min').click(function() {
            minimize("grandFather-info");
        });
        $('#grandFather-info-max').click(function() {
            maximize("grandFather-info");
        });
        $('#errors-info-min').click(function() {
            minimize("errors-info");
        });
        $('#errors-info-max').click(function() {
            maximize("errors-info");
        });
    });
    function minimize(id) {
        document.getElementById(id).style.display = 'none';
        document.getElementById(id + "-min").style.display = 'none';
        document.getElementById(id + "-max").style.display = 'block';

    }
    function maximize(id, click) {
        document.getElementById(id).style.display = 'block';
        document.getElementById(id + "-max").style.display = 'none';
        document.getElementById(id + "-min").style.display = 'block';
    }
    function initPage() {
        document.getElementById("errors-info-max").style.display = 'none';
        document.getElementById("mother-info-max").style.display = 'none';
        document.getElementById("informant-info-max").style.display = 'none';
        document.getElementById("father-info-max").style.display = 'none';
        document.getElementById("marriage-info-max").style.display = 'none';
        document.getElementById("grandFather-info-max").style.display = 'none';

    }

</script>


<div id="birth-alteration-outer">
<form action="eprLoadBirthAlteration.do" onsubmit="javascript:return validate()">
<table class="birth-alteration-table-style01" style="width:1030px;">
    <tr>
        <td width="30%"></td>
        <td width="35%" style="text-align:center;"><img src="<s:url value="/images/official-logo.png"/>"
                                                        alt=""/></td>
        <td width="35%">
            <table class="birth-alteration-table-style02" cellspacing="0" style="float:right;width:100%">
                <tr>
                    <td colspan="2" style="text-align:center;">කාර්යාල ප්‍රයෝජනය සඳහා පමණි / <br>
                        அலுவலக பாவனைக்காக மட்டும் / <br>
                        For office use only
                    </td>
                </tr>
                <tr>
                    <td width="40%">අනුක්‍රමික අංකය <br>
                        தொடர் இலக்கம் <br>
                        Serial Number
                    </td>
                    <td width="60%"><s:textfield/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><s:textfield/></td>
                </tr>
                <tr>
                    <td>පනතේ වගන්තිය <br>
                        பிறப்பைப் <br>
                        Section of the Act
                    </td>
                    <td><s:textfield name="sectionOfAct"/></td>
                </tr>
            </table>
            <div class="form-submit">
                <s:submit value="%{getText('submit.label')}"/>
            </div>
        </td>
    </tr>
    <tr>
        <td colspan="3" style="font-size:14pt;text-align:center;">උප්පැන්න ලියාපදිංචි කිරීමේ සටහනක විස්තර වෙනස්
            කිරීම / ඇතුලත් කිරීම හෝ අතහැරීම <br>
            ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள் <br>
            Alteration / insertion / omission of particulars from a Birth Registration entry
        </td>
    </tr>
    <tr>
        <td colspan="3" style="font-size:12pt;text-align:center;">උප්පැන්න හා මරණ ලියාපදිංචි කිරීමේ පනත යටතේ
            උප්පැන්න ලියාපදිංචි කිරීමේ සටහනක විස්තර වෙනස්
            කිරීම / ඇතුලත් කිරීම හෝ අතහැරීම සඳහා කරනු ලබන ප්‍රකාශය <br>
            in tamil <br>
            Declaration for the alteration / insertion / omission of particulars of a birth registration entry under
            the Births and Deaths Registration Act.
        </td>
    </tr>
    <tr>
        <td colspan="3" style="font-size:13pt;text-align:center;">වෙනස් කලයුතු උප්පැන්න සහතිකය පිලිබඳ විස්තර <br>
            பிள்ளை பற்றிய தகவல் <br>
            Particulars of the Birth Certificate to amend
        </td>
    </tr>
</table>

<table class="birth-alteration-table-style02" style="width:100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col width="15%"/>
    <col width="25%"/>
    <col width="20%"/>
    <col width="20%"/>
    <col width="20%"/>
    <tbody>
    <tr>
        <td colspan="2">සහතිකයේ සඳහන් පුද්ගලයාගේ අනන්‍යතා අංකය <br>
            தனிநபர்அடையாள எண் <br>
            Person Identification Number (PIN) stated in the Certificate
        </td>
        <td><s:textfield/></td>
        <td>සහතික පත්‍රයේ අංකය <br>
            சான்றிதழ் இல <br>
            Certificate Number
        </td>
        <td><s:textfield/></td>
    </tr>
    <tr>
        <td>දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td><s:select id="districtId" name="birthDistrictId" list="districtList" value="birthDistrictId"
                      cssStyle="width:98.5%; width:240px;"/></td>
        <td>ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரிவு <br>
            Divisional Secretariat
        </td>
        <td colspan="2"><s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  value="%{dsDivisionId}"
                                  cssStyle="float:left;  width:240px;"/></td>
    </tr>
    <tr>
        <td>ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
            பிரிவு <br>
            Registration Division
        </td>
        <td><s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}" list="bdDivisionList"
                      cssStyle="float:left;  width:240px; margin:2px 5px;"/></td>
        <td>ලියාපදිංචි කිරීමේ අංකය <br>
            சான்றிதழ் இல <br>
            Registration Number
        </td>
        <td colspan="2"><s:textfield/></td>
    </tr>
    </tbody>
</table>
<s:if test="pageNo == 1 || pageNo == 0">
<table class="birth-alteration-table-style01" style="width:100%;">
    <tr>
        <td style="text-align:center;font-size:13pt;">
            වෙනස් කිරීම / ඇතුලත් කිරීම හෝ අතහැරීම පිලිබඳ විස්තර <br>
            பிள்ளை பற்றிய தகவல் <br>
            Particulars about the alteration / insertion / omission
        </td>
    </tr>
    <tr>
        <td style="text-align:center;font-size:11pt;"> වෙනස් කිරීම / ඇතුලත් කිරීම සඳහා නිවැරදි විය යුතු ආකාරය අදාළ
            කොටුවේ සඳහන් කරන්න. ඉවත් කිරීමක් සඳහා "ඉවත්
            කරන්න" යන්න අදාළ කොටුවේ සඳහන් කරන්න <br>
            in Tamil <br>
            For alteration / insertion state the entry value as it should appear. For omission, state “omit” within
            the relavent cage.
        </td>
    </tr>
    <tr>
        <td style="text-align:center;font-size:13pt;">නම ඇතුලත් කිරීම හෝ වෙනස් කිරීම (27 වගන්තිය) <br>
            தந்தை பற்றிய தகவல் <br>
            Insertion or Alteration of the Name (Section 27)
        </td>
    </tr>
    <tr>
        <td style="text-align:center;font-size:11pt;">මෙම වගන්තිය යටතේ මව්පියන් හෝ භාරකරු හට අවුරුදු 21 කට අඩු
            දරුවකුගේ නම වෙනස් කිරීමට ඉල්ලීම් කල හැක. අවු
            රුදු 21 කට වැඩි පුද්ගලයෙකුගේ නම වෙනස් කිරීමට ඔහු විසින් ඉල්ලුම් පත්‍රයක් ඉදිරිපත් කල හැක. <br>
            தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை பற்றிய தகவல் தந்தை
            பற்றிய தகவல் தந்தை பற்றிய தகவல் <br>
            Under this section the name change of a child under 21 years could be requested by his parents or the
            guardian. A person over 21 years in age could request for a name change on his own.
        </td>
    </tr>
</table>
<table class="birth-alteration-table-style02" style="width:100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col width="250px"/>
    <col width="760px"/>
    <tbody>
    <tr>
        <td>නම රාජ්‍ය භාෂාවෙන්
            (සිංහල / දෙමළ) <br>
            பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்) <br>
            Name in any of the official languages (Sinhala / Tamil)
        </td>
        <td><s:textarea/></td>
    </tr>
    <tr>
        <td>නම ඉංග්‍රීසි භාෂාවෙන් <br>
            பிறப்பு அத்தாட்சி ….. <br>
            Name in English
        </td>
        <td><s:textarea/></td>
    </tr>
    </tbody>
</table>
</s:if>
<s:if test="pageNo == 2 || pageNo == 0">

<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td style="font-size:13pt;text-align:center;" colspan="7">උප්පැන සහතිකයක දෝෂ නිවැරදි කිරීම (52 (1) වගන්තිය) <br>
            தந்தை பற்றிய தகவல் <br>
            Correction of Errors of a Birth Certificate (Section 52 (1))
            <div class="birth-alteration-minimize-icon" id="errors-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="errors-info-max"></div>
        </td>
    </tr>
</table>
<div id="errors-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;border-top:none;" cellpadding="0"
           cellspacing="0">
        <caption></caption>
        <col width="250px;"/>
        <col width="125px;"/>
        <col width="125px;"/>
        <col width="125px;"/>
        <col width="125px;"/>
        <col width="125px;"/>
        <tbody>
        <tr>
            <td>උපන් දිනය <br>
                பிறந்த திகதி <br>
                Date of Birth
            </td>
            <td>වර්ෂය<br>
                வருடம்<br>
                Year
            </td>
            <td></td>
            <td>මාසය<br>
                மாதம்<br>
                Month
            </td>
            <td></td>
            <td>දිනය<br>
                கிழமை<br>
                Day
            </td>
            <td></td>
        </tr>
        <tr>
            <td rowspan="5">උපන් ස්ථානය<br>
                பிறந்த இடம்<br>
                Place of Birth
            </td>
            <td colspan="2">දිස්ත්‍රික්කය / <br>
                மாவட்டம் /<br>
                District
            </td>
            <td colspan="4"></td>
        </tr>
        <tr>
            <td colspan="2">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>
                பிரிவு / <br>
                Divisional Secretariat
            </td>
            <td colspan="4"></td>
        </tr>
        <tr>
            <td colspan="2">ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br>
                பிரிவு / <br>
                Registration Division
            </td>
            <td colspan="4"></td>
        </tr>
        <tr>
            <td rowspan="2">ස්ථානය <br>
                பிறந்த <br>
                Place
            </td>
            <td>සිංහල හෝ දෙමළ භාෂාවෙන්<br>
                சிங்களம் தமிழ்<br>
                In Sinhala or Tamil
            </td>
            <td colspan="4"></td>
        </tr>
        <tr>
            <td>ඉංග්‍රීසි භාෂාවෙන්<br>
                in tamil<br>
                In English
            </td>
            <td colspan="4"></td>
        </tr>
        <tr>
            <td>
                ස්ත්‍රී පුරුෂ භාවය<br>
                பால் <br>
                Gender of the child
            </td>
            <td colspan="6"></td>
        </tr>
        </tbody>
    </table>
</div>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="9" style="text-align:center;font-size:12pt"> මවගේ විස්තර <br>தாய் பற்றிய தகவல் <br>Details of the
            Mother
            <div class="birth-alteration-minimize-icon" id="mother-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="mother-info-max"></div>
        </td>
    </tr>
</table>
<div id="mother-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;border-top:none;" cellpadding="0"
           cellspacing="0">
        <caption></caption>
        <col width="250px;"/>
        <col width="100px;"/>
        <col width="150px;"/>
        <col width="10px;"/>
        <col width="50px;"/>
        <col width="100px;"/>
        <col width="50px;"/>
        <col width="50px;"/>
        <col width="100px;"/>

        <tbody>
        <tr>
            <td rowspan="2">අනන්‍යතා අංකය
                / ජාතික හැදුනුම්පත් අංකය<br>து தனிநபர் அடையாள எண் /தேசிய
                அடையாள அட்டை
                இலக்கம்<br>PIN / NIC Number
            </td>
            <td colspan="2" rowspan="2" class="find-person">
                <s:textfield id="mother_pinOrNic" name="parent.motherNICorPIN" />
                <img src="<s:url value="/images/search-mother.png"/>" style="vertical-align:middle;" id="mother_lookup">
            </td>
            <td colspan="2" rowspan="2"><label>විදේශිකය‍කු නම්<br>வெளிநாட்டவர் எனின் <br>If foreigner</label>
            </td>
            <td colspan="2"><label>රට<br>நாடு <br>Country</label></td>
            <td colspan="2"><s:select name="motherCountry" list="countryList" headerKey="0"
                                      headerValue="%{getText('select_country.label')}" cssStyle="width:80%;"/></td>
        </tr>
        <tr>
            <td colspan="2"><label>ගමන් බලපත්‍ර අංකය <br>கடவுச் சீட்டு <br>Passport No.</label></td>
            <td colspan="2" class="passport"><s:textfield name="parent.motherPassportNo" /></td>
        </tr>
        <tr>
            <td>උපන් දිනය<br>
                பிறந்த திகதி<br>
                Date of Birth
            </td>
            <td><s:textfield cssStyle="width:125px;"/></td>
            <td>ළමයාගේ උපන් දිනට වයස <br>
                பிள்ளை பிறந்த திகதியில் மாதாவின் வயது <br>
                Age as at the date of birth of child
            </td>
            <td colspan="2"><s:textfield cssStyle="margin-right:10px;width:80%"/></td>
            <td colspan="2">ජාතිය<br>
                இனம்<br>
                Race
            </td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td>උපන් ස්ථානය<br>
                பிறந்த இடம் <br>
                Place of Birth
            </td>
            <td colspan="8"><s:textarea/></td>
        </tr>
        <tr>
            <td>ස්ථිර ලිපිනය<br>
                தாயின் நிரந்தர வதிவிட முகவரி<br>
                Permanent Address
            </td>
            <td colspan="8"><s:textarea/></td>
        </tr>
        </tbody>
    </table>
</div>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col width="250px;"/>
    <col width="250px;"/>
    <col width="250px;"/>
    <col width="250px;"/>
    <tbody>
    <tr>
        <td colspan="4" style="text-align:center;font-size:12pt">දැනුම් දෙන්නාගේ විස්තර<br>
            அறிவிப்பு கொடுப்பவரின் தகவல்கள்<br>
            Details of the Informant
            <div class="birth-alteration-minimize-icon" id="informant-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="informant-info-max"></div>
        </td>
    </tr>
    </tbody>
</table>
<div id="informant-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:1030px;border-top:none;" cellpadding="0"
           cellspacing="0">
        <caption></caption>
        <col width="250px;"/>
        <col width="250px;"/>
        <col width="250px;"/>
        <col width="250px;"/>
        <tbody>
        <tr>
            <td>දැනුම් දෙන්නේ කවුරුන් විසින් ද?<br>
                தகவல் வழங்குபவா் <br>
                Person Giving Information
            </td>
            <td>මව <br>மாதா<br>
                Mother
            </td>
            <td>පියා<br> பிதா<br>
                Father
            </td>
            <td>භාරකරු<br> பாதுகாவலர் <br>
                Guardian
            </td>
        </tr>
        <tr>
            <td colspan="2">දැනුම් දෙන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>
                தகவல் கொடுப்பவரின் தனிநபர் அடையாள எண் / அடையாள அட்டை இல. <br>
                PIN / NIC of the Informant
            </td>
            <td colspan="2"><s:textfield/></td>
        </tr>
        <tr>
            <td>නම<br>
                கொடுப்பவரின் பெயர்<br>
                Name
            </td>
            <td colspan="3"><s:textarea/></td>
        </tr>
        <tr>
            <td>තැපැල් ලිපිනය<br>
                தபால் முகவரி<br>
                Postal Address
            </td>
            <td colspan="3"><s:textarea/></td>
        </tr>
        </tbody>
    </table>
</div>
</s:if>
<s:if test="pageNo == 3 || pageNo == 0">
<table class="birth-alteration-table-style01" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td style="text-align:center;font-size:12pt"> උප්පැන්න සහතිකයක තොරතුරු සංශෝදනය කිරීම (27 A වගන්තිය)<br>
            தந்தை பற்றிய தகவல்<br>
            Amendment of Birth Registration Entry (Section 27 A)
        </td>
    </tr>
</table>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="5" style="text-align:center;font-size:12pt">පියාගේ විස්තර<br>
            தந்தை பற்றிய தகவல்<br>
            Details of the Father
            <div class="birth-alteration-minimize-icon" id="father-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="father-info-max"></div>
        </td>
    </tr>
</table>
<div id="father-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;border-top:none;" cellpadding="0"
           cellspacing="0">

        <caption></caption>
        <col width="250px;"/>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td rowspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>
                து தனிநபர் அடையாள எண் /தேசிய அடையாள அட்டை இலக்கம்<br>
                PIN / NIC Number
            </td>
            <td rowspan="2"><s:textfield/></td>
            <td rowspan="2">විදේශිකය‍කු නම්<br>
                வெளிநாட்டவர் எனின் <br>
                If a foreigner
            </td>
            <td>රට<br>
                நாடு<br>
                Country
            </td>
            <td></td>
        </tr>
        <tr>
            <td>ගමන් බලපත්‍ර අංකය<br>
                கடவுச் சீட்டு<br>
                Passport No.
            </td>
            <td></td>
        </tr>
        <tr>
            <td>සම්පුර්ණ නම<br>
                தந்தையின் முழு பெயர்<br>
                Full Name
            </td>
            <td colspan="4"><s:textarea/></td>
        </tr>
        <tr>
            <td>උපන් දිනය<br>
                பிறந்த திகதி <br>
                Date of Birth
            </td>
            <td><s:textfield/></td>
            <td colspan="2">ජාතිය<br>
                இனம்<br>
                Race
            </td>
            <td><s:textfield/></td>
        </tr>
        <tr>
            <td>උපන් ස්ථානය<br>
                பிறந்த இடம்<br>
                Place of Birth
            </td>
            <td colspan="4"></td>
        </tr>
        </tbody>
    </table>
</div>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="4" style="text-align:center;font-size:12pt">විවාහයේ විස්තර වෙනස් කිරීම<br>
            திருமணத்தின் விபரங்கள் <br>
            Changing of Details of the Marriage
            <div class="birth-alteration-minimize-icon" id="marriage-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="marriage-info-max"></div>
        </td>
    </tr>
</table>
<div id="marriage-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;border-top:none;" cellpadding="0"
           cellspacing="0">
        <tr>
            <td rowspan="2">මව්පියන් විවාහකද?</td>
            <td rowspan="2">
                பெற்றோர்கள் மணம் முடித்தவர்களா? <br>
                Were Parent's Married?
                ඔව්<br>
                Yes<br>

                නැත<br>
                No

                නැත, නමුත් පසුව විවාහවී ඇත<br>
                No, but since married<br></td>
            <td>විවාහ වු ස්ථානය<br>
                விவாகம் இடம்பெற்ற இடம்<br>
                Place of Marriage
            </td>
            <td><s:textfield/></td>
        </tr>
        <tr>
            <td>
                විවාහ වු දිනය<br>
                விவாகம் இடம்பெற்ற திகதி<br>
                Date of Marriage
            </td>
            <td><s:textfield/></td>
        </tr>
    </table>
</div>
<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="2" style="text-align:center;font-size:12pt">
            විවාහයෙන් පසු මවගේ නම වෙනස් කිරීම<br>
            தாத்தாவின பாட்டனின் விபரங்கள் <br>
            Change of Mothers name after marriage
        </td>
    </tr>
    <tr>
        <td style="width:250px;">විවාහයට පසුව මවගේ සම්පුර්ණ නම<br>
            முழுப் பெயர்<br>
            Full Name of Mother after Marriage
        </td>
        <td style="width:760px;"><s:textarea/></td>
    </tr>
</table>


<table class="birth-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="8" style="text-align:center;font-size:12pt">මුත්තා / මී මුත්තා ගේ විස්තර වෙනස් කිරීම<br>
            தாத்தாவின் / பாட்டனின் விபரங்கள் <br>
            Changing of the Details of the Grand Father / Great Grand Father
            <div class="birth-alteration-minimize-icon" id="grandFather-info-min"></div>
            <div class="birth-alteration-maximize-icon" id="grandFather-info-max"></div>
        </td>
    </tr>
</table>
<div id="grandFather-info">
    <table class="birth-alteration-table-style02" style=" margin-top:0px;width:100%;border-top:none;" cellpadding="0"
           cellspacing="0">
        <caption></caption>
        <col width="15px"/>
        <col width="230px"/>
        <col width="50px"/>
        <col width="150px"/>
        <col width="100px"/>
        <col width="150px"/>
        <col width="100px"/>
        <tbody>
        <tr>
            <td colspan="8">ළමයාගේ මුත්තා ශ්‍රී ලංකාවේ උපන්නේ නම්<br>
                பிள்ளையின் பாட்டனார் இலங்கையில் பிறந்திருந்தால் <br>
                If grandfather of the child born in Sri Lanka
            </td>
        </tr>
        <tr>
            <td rowspan="2" style="background-color:darkgray;"></td>
            <td>ඔහුගේ සම්පුර්ණ නම<br>
                அவரின் முழுப் பேயர்<br>
                His Full Name
            </td>
            <td colspan="6"><s:textarea/></td>
        </tr>
        <tr>
            <td colspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය (තිබේ නම්)<br>
                தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம்<br>
                PIN / NIC Number (if available)
            </td>
            <td><s:textfield/></td>
            <td>ඔහුගේ උපන් වර්ෂය<br>
                அவர் பிறந்த வருடம்<br>
                His Year of Birth
            </td>
            <td><s:textfield/></td>
            <td>උපන් ස්ථානය<br>
                அவர் பிறந்த இடம்<br>
                Place Of Birth
            </td>
            <td><s:textfield/></td>
        </tr>
        <tr>
            <td colspan="8">ළමයාගේ පියා ශ්‍රී ලංකාවේ නොඉපිද මීමුත්තා ලංකාවේ උපන්නේ නම් මී මුත්තාගේ<br>
                பிள்ளையின் தந்தை இலங்கையில் பிறக்காமல் பூட்டன் இலங்கையில் பிறந்திருந்தால் பூட்டனாரின் தகவல்கள் <br>
                If the father was not born in Sri Lanka and if great grand father born in Sri Lanka, great grand
                father's
            </td>
        </tr>
        <tr>
            <td rowspan="2" style="background-color:darkgray;"></td>
            <td>ඔහුගේ සම්පුර්ණ නම<br>
                முழுப் பெயர்<br>
                His Full Name
            </td>
            <td colspan="6"><s:textarea/></td>
        </tr>
        <tr>
            <td colspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය (තිබේ නම්)<br>
                தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம்<br>
                PIN / NIC Number (if available)
            </td>
            <td><s:textfield/></td>
            <td>ඔහුගේ උපන් වර්ෂය<br>
                அவர் பிறந்த வருடம்<br>
                His Year of Birth
            </td>
            <td><s:textfield/></td>
            <td>උපන් ස්ථානය<br>
                அவர் பிறந்த இடம்<br>
                Place Of Birth
            </td>
            <td><s:textfield/></td>
        </tr>
        </tbody>
    </table>
</div>
</s:if>
</div>
<div class="form-submit">
    <s:submit value="%{getText('submit.label')}"/>
</div>