<%--
  @author duminda
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="birth-registration-form-outer" id="birth-registration-form-3-outer">
<s:form action="eprBirthRegistration.do" name="birthRegistrationForm3" id="birth-registration-form-3" method="POST">


<table class="table_reg_page_03" cellspacing="0" style="margin-top:5px">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td colspan="5" style="text-align:center;font-size:12pt"> *in Sinhala<br>*in Tamil<br>Details of the Marriage
        </td>
    </tr>
    <tr>
        <td rowspan="2"><label>(25)මව්පියන් විවාහකද? <br>பெற்றோர்கள் மணம் முடித்தவர்களா? <br>Were Parent's
            Married?</label></td>
        <td rowspan="2">
            <table class="sub_table">
                <tr>
                    <td><label>*in sinhala<br>*in tamil<br>Yes</label></td>
                    <td><s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'0':''}"/></td>
                </tr>
                <tr>
                    <td><label>*in sinhala<br>*in tamil<br>No</label></td>
                    <td><s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'1':''}"/></td>
                </tr>
                <tr>
                    <td><label>*in sinhala<br>*in tamil<br>Since Married</label></td>
                    <td><s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'2':''}"/></td>
                </tr>
            </table>
        </td>
        <td><label>විවාහ වු ස්ථානය<br>விவாகம் இடம்பெற்ற இடம் <br>Place of Marriage</label></td>
        <td colspan="2"><s:textfield name="marriage.placeOfMarriage"/></td>
    </tr>
    <tr>
        <td><label>විවාහ වු දිනය<br>விவாகம் இடம்பெற்ற திகதி <br>Date of Marriage</label></td>
        <td colspan="2"><sx:datetimepicker id="marriageDatePicker" name="marriage.dateOfMarriage"
                                           displayFormat="yyyy-MM-dd"
                                           onmouseover="javascript:splitDate('marriageDatePicker')"/></td>
    </tr>
    <tr>
        <td colspan="3" rowspan="2"><label>(26)මව්පියන් විවාහ වි නොමැති නම් පියාගේ තොරතුරු ඇතුලත් කර ගැනිම සදහා මව සහ
            පියාගේ අත්සන් <br>பெற்றோர்
            மணம் செய்யாதிருப்பின், தகப்பனின் தகவல்கள் பதிவு செய்ய வேண்டுமெனின் பெற்றோரின் கையொப்பம்<br>If
            parents are not married, signatures of mother and father to include father's particulars</label></td>
        <td><label>මවගේ අත්සන <br> தாயின் ஒப்பம் <br>Mother’s Signature</label></td>
        <td><s:checkbox name="marriage.motherSigned"/></td>
    </tr>
    <tr>
        <td><label>පියාගේ අත්සන <br>தகப்பனின் ஒப்பம் <br>Father’s Signature</label></td>
        <td><s:checkbox name="marriage.fatherSigned"/></td>
    </tr>
    </tbody>
</table>


<table class="table_reg_page_03" cellspacing="0">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td colspan="5" style="text-align:center;font-size:12pt">*in Sinhala<br>*in Tamil<br>Details of the Grand Father
            / Great Grand Father
        </td>
    </tr>
    <tr>
        <td colspan="5"><label>(27)ළමයාගේ මුත්තා ශ්‍රී ලංකාවේ උපන්නේ නම් <br>பிள்ளையின் பாட்டனார் இலங்கையில்
            பிறந்திருந்தால் <br>If
            grandfather of the child born in Sri Lanka</label></td>
    </tr>
    <tr>
        <td rowspan="2" style="width:75px"></td>
        <td><label>ඔහුගේ සම්පුර්ණ නම<br>அவரின் முழுப் பேயர் <br>His Full Name</label></td>
        <td colspan="3"><s:textarea name="grandFather.grandFatherFullName"/></td>
    </tr>
    <tr>
        <td><label>ඔහුගේ උපන් වර්ෂය <br>அவர் பிறந்த வருடம் <br>His Year of Birth</label></td>
        <td><s:textfield name="grandFather.grandFatherBirthYear"/></td>
        <td><label>උපන් ස්ථානය <br>அவர் பிறந்த இடம் <br>Place Of Birth</label></td>
        <td><s:textfield name="grandFather.grandFatherBirthPlace"/></td>
    </tr>
    <tr>
        <td colspan="5"><label> (28)ළමයාගේ පියා ශ්‍රී ලංකාවේ නොඉපිද මීමුත්තා ලංකාවේ උපන්නේ නම් මී මුත්තාගේ <br>பிள்ளையின்
            தந்தை
            இலங்கையில் பிறக்காமல் பூட்டன் இலங்கையில் பிறந்திருந்தால் பூட்டனாரின் தகவல்கள்<br>If the father was not
            born in Sri Lanka and if great grandfather born in Sri Lanka great grand father's</label></td>
    </tr>
    <tr>
        <td rowspan="2"></td>
        <td><label>සම්පුර්ණ නම <br>முழுப் பெயர் <br>Full Name</label></td>
        <td colspan="3"><s:textarea name="grandFather.greatGrandFatherFullName"/></td>
    </tr>
    <tr>
        <td><label>උපන් වර්ෂය <br>பிறந்த வருடம் <br>Year of Birth</label></td>
        <td><s:textfield name="grandFather.greatGrandFatherBirthYear"/></td>
        <td><label>උපන් ස්ථානය <br>அவர் பிறந்த இடம் <br>Place Of Birth</label></td>
        <td><s:textfield name="grandFather.greatGrandFatherBirthPlace"/></td>
    </tr>
    </tbody>
</table>


<table class="table_reg_page_03" cellspacing="0">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td colspan="6" style="text-align:center;font-size:12pt">දැනුම් දෙන්නාගේ විස්තර<br>அறிவிப்பு கொடுப்பவரின்
            தகவல்கள் <br>Details of the Informant
        </td>
    </tr>
    <tr>
        <td colspan="2"><label>(29)දැනුම් දෙන්නේ කවුරුන් විසින් ද? <br>தகவல் வழங்குபவா் <br>Person Giving
            Information</label></td>
        <td>
            <table class="sub_table">
                <tr>
                    <td><label>මව <br>மாதா <br>Mother</label></td>
                    <td align="justify"><s:radio name="informant.informantType" list="#{'MOTHER':''}" onchange="javascript:setInformPerson('MOTHER',
            '%{parent.motherNICorPIN}', '%{parent.motherFullName}', '%{parent.motherAddress}',
            '%{parent.motherPhoneNo}','%{parent.motherEmail}')"/></td>
                </tr>
            </table>
        </td>
        <td colspan="2" style="width:180px">
            <table class="sub_table">
                <tr>
                    <td><label>පියා<br> பிதா <br>Father</label></td>
                    <td align="justify"><s:radio name="informant.informantType" list="#{'FATHER':''}" onchange="javascript:setInformPerson('FATHER',
            '%{parent.fatherNICorPIN}',
            '%{parent.fatherFullName}','','','')"/></td>
                </tr>
            </table>
        </td>
        <td>
            <table class="sub_table">
                <tr>
                    <td><label>භාරකරු<br> பாதுகாவலர் <br>Guardian</label></td>
                    <td align="justify">
                        <s:radio name="informant.informantType" list="#{'GUARDIAN':''}"
                                 onchange="javascript:setInformPerson('GUARDIAN','','','','','','')"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="2"><label>(30) නම <br>கொடுப்பவரின் பெயர் <br>Name</label></td>
        <td colspan="4"><s:textarea name="informant.informantName" id="informantName"/></td>
    </tr>
    <tr>
        <td colspan="3"><label>(31)දැනුම් දෙන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>தகவல் கொடுப்பவரின்
            தனிநபர்
            அடையாள எண் / அடையாள அட்டை இல. <br>PIN / NIC of the Informant</label></td>
        <td colspan="3"><s:textfield name="informant.informantNICorPIN" id="informantNICorPIN"/></td>
    </tr>
    <tr>
        <td colspan="2"><label>(32)තැපැල් ලිපිනය<br>தபால் முகவரி <br>Postal Address</label></td>
        <td colspan="4"><s:textarea name="informant.informantAddress" id="informantAddress"/></td>
    </tr>
    <tr>
        <td><label>දුරකතනය<br>தொலைபேசி இலக்கம் <br>Telephone</label></td>
        <td colspan="2"><s:textfield name="informant.informantPhoneNo" id="informantPhoneNo"/></td>
        <td><label>ඉ -තැපැල <br>மின்னஞ்சல் <br>Email</label></td>
        <td colspan="2"><s:textfield name="informant.informantEmail" id="informantEmail"/></td>
    </tr>
    <tr>
        <td><label>(32) අත්සන<br>தகவல் ... <br>Signature</label></td>
        <td colspan="2"><s:checkbox name="informant.informantSigned"/></td>
        <td><label>දිනය <br>*in tamil<br>Date</label></td>
        <td colspan="2"><sx:datetimepicker id="informDatePicker" name="informant.informantSignDate"
                                           displayFormat="yyyy-MM-dd"
                                           onmouseover="javascript:splitDate('secondDatePicker')"/></td>
    </tr>
    </tbody>
</table>


<script type="text/javascript">
    var informPerson;
    function setInformPerson(id, nICorPIN, name, address, phonoNo, email)
    {

        var informantName = document.getElementById("informantName");
        var informantNICorPIN = document.getElementById("informantNICorPIN");
        var informantAddress = document.getElementById("informantAddress");
        var informantPhoneNo = document.getElementById("informantPhoneNo");
        var informantEmail = document.getElementById("informantEmail");

        informantName.value = name;
        informantNICorPIN.value = nICorPIN;
        informantAddress.value = address;
        informantPhoneNo.value = phonoNo;
        informantEmail.value = email;


        informantName.readOnly = false;
        informantNICorPIN.readOnly = false;
        informantAddress.readOnly = false;
        informantPhoneNo.readOnly = false;
        informantEmail.readOnly = false;


        if (id == 0)
        {
            informantName.readOnly = true;
            informantNICorPIN.readOnly = true;
            informantAddress.readOnly = true;


            informantPhoneNo.readOnly = true;
            informantEmail.readOnly = true;

        }
        else if (id == 1)
        {
            informantName.readOnly = true;
            informantNICorPIN.readOnly = true;


        }


    }

</script>


<s:hidden name="pageNo" value="3"/>

<table style="border:none; margin:12px;" align="center" class="form-submit">
    <s:url id="backUrl" action="eprBirthRegistration">
        <s:param name="back" value="true"/>
        <s:param name="pageNo" value="{pageNo - 1}"/>
    </s:url>
    <s:a href="%{backUrl}"> << </s:a>
    <s:submit value="%{getText('next.label')}"/>
</table>
</s:form>
</div>
