<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="death-declaration-form-1-outer">
    <s:form name="deathRegistrationForm1" id="death-registration-form-1" action="eprDeathDeclaration.do" method="POST">
        <table style="width: 100%; border:none; border-collapse:collapse;" class="font-9">
            <col width="180px"/>
            <col width="350px"/>
            <col width="120px"/>
            <col width="160px"/>
            <tbody>
            <tr>
                <td rowspan="3"></td>
                <td rowspan="2" align="center">
                    <img src="<s:url value="/images/official-logo.png" />" style="display: block; text-align: center;"
                         width="80" height="100">
                </td>
                <td style="border:1px solid #000;">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</td>
                <td style="border:1px solid #000;"></td>
            </tr>
            <tr>
                <td colspan="2" style="border:1px solid #000;">කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>அலுவலக பாவனைக்காக மட்டும்
                    <br>For office use only
                </td>
            </tr>
            <tr>
                <td align="center" class="font-12">මරණ ප්‍රකාශයක් (30 වෙනි වගන්තිය)<br>ஒரு பிறப்பைப் பதிவு செய்வதற்கான
                    விபரங்கள் <br>Declaration
                    of Death (Under Section 30)
                </td>
                <td style="border:1px solid #000;">ලියාපදිංචි කල දිනය<br>பிறப்பைப் பதிவு திகதி <br>Date of Registration
                </td>
                <td style="border:1px solid #000;"></td>
            </tr>
            <tr>
                <td colspan="4" height="15px"></td>
            </tr>
            <tr>
                <td colspan="4" class="font-9" style="text-align:justify;">
                    ප්‍රකාශකයා විසින් මරණය සිදු වූ කොට්ටාශයේ මරණ රෙජිස්ට්‍රාර් තැන වෙත ලබා දිය යුතුය. මෙම තොරතුරු මත
                    සිවිල්
                    ලියාපදිංචි කිරිමේ පද්ධතියේ මරණය ලියාපදිංචි කරනු ලැබේ.
                    <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம்
                    சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                    <br>Should be perfected by the declarant and the duly completed form should be forwarded to the
                    Registrar of Deaths of the division where the death has occurred. The death will be registered in
                    the
                    Civil Registration System based on the information provided in this form.
                </td>
            </tr>
            </tbody>
        </table>

        <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;" class="font-9">
            <col width="150px"/>
            <col width="100px"/>
            <col width="120px"/>
            <col width="90px"/>
            <col width="120px"/>
            <col width="90px"/>
            <col width="120px"/>
            <col width="100px"/>
            <col/>
            <tbody>
            <tr>
                <td colspan="9" class="form-sub-title">
                    මරණය පිලිබඳ විස්තර
                    <br>பிள்ளை பற்றிய தகவல்
                    <br>Information about the Death
                </td>
            </tr>
            <tr>
                <td>මරණය සිදුවූ දිනය<br>பிறந்த திகதி<br>Date of death</td>
                <td>වර්ෂය<br>வருடம்<br>Year</td>
                <td></td>
                <td>මාසය<br>மாதம்<br>Month</td>
                <td></td>
                <td>දිනය<br>கிழமை<br>Day</td>
                <td></td>
                <td>වෙලාව<br>*in tamil<br>Time</td>
                <td></td>
            </tr>
            <tr>
                <td rowspan="5">මරණය සිදු වූ ස්ථානය<br>பிறந்த இடம்<br>Place of Death</td>
                <td colspan="3">දිස්ත්‍රික්කය / மாவட்டம் / District</td>
                <td colspan="5"></td>
            </tr>
            <tr>
                <td colspan="3">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
                <td colspan="5"></td>
            </tr>
            <tr>
                <td colspan="3">ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br>பிரிவு / <br>Registration Division</td>
                <td colspan="5"></td>
            </tr>
            <tr>
                <td rowspan="2" colspan="1">ස්ථානය <br>பிறந்த <br>Place</td>
                <td colspan="2">සිංහල හෝ දෙමළ භාෂාවෙන්<br>சிங்களம் தமிழ்<br>In Sinhala or Tamil</td>
                <td colspan="5"></td>
            </tr>
            <tr>
                <td colspan="2">ඉංග්‍රීසි භාෂාවෙන්<br>*in tamil<br>In English</td>
                <td colspan="5"></td>
            </tr>
            <tr>
                <td rowspan="2" colspan="1">මරණයට හේතුව තහවුරුද?<br>*in tamil<br>Is the cause of death established?</td>
                <td colspan="1">නැත / xx / No</td>
                <td colspan="2"></td>
                <td rowspan="2" colspan="3">මරණය දින 30 කට අඩු ළදරුවෙකුගේද?<br>*in tamil<br>Is the death of an infant
                    less
                    than 30 days?
                </td>
                <td colspan="1">නැත / xx / No</td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td colspan="1">ඔව් / xx /Yes</td>
                <td colspan="2"></td>
                <td colspan="1">ඔව් / xx /Yes</td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td colspan="1">මරණයට හේතුව<br>*in tamil<br>Cause of death</td>
                <td colspan="4"></td>
                <td colspan="2">හේතුවේ ICD කේත අංකය<br>*in tamil<br>ICD Code of cause</td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td colspan="1">ආදාහන හෝ භූමදාන ස්ථානය<br>*in tamil<br>Place of burial or cremation</td>
                <td colspan="8"></td>
            </tr>
            </tbody>
        </table>

        <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin-bottom:0;" class="font-9">
            <col width="150px"/>
            <col width="100px"/>
            <col width="100px"/>
            <col width="100px"/>
            <col width="150px"/>
            <col width="130px"/>
            <col/>
            <tbody>
            <tr class="form-sub-title">
                <td colspan="7">මරණයට පත් වූ පුද්ගලයාගේ විස්තර<br>பிள்ளை பற்றிய தகவல்<br>Information about the person
                    Departed
                </td>
            </tr>
            <tr>
                <td rowspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>தனிநபர் அடையாள எண் / அடையாள அட்டை இல.
                    <br>PIN / NIC
                </td>
                <td rowspan="2" colspan="3"></td>
                <td rowspan="2">විදේශිකය‍කු නම්<br>வெளிநாட்டவர் <br>If a foreigner</td>
                <td>රට<br>நாடு<br>Country</td>
                <td></td>
            </tr>
            <tr>
                <td>ගමන් බලපත්‍ර අංකය<br>கடவுச் சீட்டு<br>Passport No.</td>
                <td></td>
            </tr>
            <tr>
                <td colspan="1">වයස හෝ අනුමාන වයස<br>பிறப்ப<br>Age or probable Age</td>
                <td colspan="1"></td>
                <td colspan="1">ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender</td>
                <td colspan="1"></td>
                <td colspan="1">ජාතිය<br>பிறப்<br>Race</td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td colspan="1">නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)<br>பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்)<br>Name
                    in either of the official languages (Sinhala / Tamil)
                </td>
                <td colspan="6"></td>
            </tr>
            <tr>
                <td colspan="1">නම ඉංග්‍රීසි භාෂාවෙන්<br>பிறப்பு அத்தாட்சி …..<br>Name in English</td>
                <td colspan="6"></td>
            </tr>
            <tr>
                <td colspan="1">ස්ථිර ලිපිනය<br>தாயின் நிரந்தர வதிவிட முகவரி<br>Permanent Address</td>
                <td colspan="6"></td>
            </tr>
            <tr>
                <td colspan="1">පියාගේ පු.අ.අ. / ජා.හැ.අ.<br>*in tamil<br>Fathers PIN / NIC</td>
                <td colspan="6"></td>
            </tr>
            <tr>
                <td colspan="1">පියාගේ සම්පුර්ණ නම<br>*in tamil <br>Fathers full name</td>
                <td colspan="6"></td>
            </tr>
            <tr>
                <td colspan="1">මවගේ පු.අ.අ. / ජා.හැ.අ.<br>*in tamil<br>Mothers PIN / NIC</td>
                <td colspan="6"></td>
            </tr>
            <tr>
                <td colspan="1">මවගේ සම්පුර්ණ නම<br>*in tamil <br>Mothers full name</td>
                <td colspan="6"></td>
            </tr>
            </tbody>
        </table>

        <div class="form-submit">
            <s:hidden name="pageNo" value="1"/>
            <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
        </div>
    </s:form>
</div>