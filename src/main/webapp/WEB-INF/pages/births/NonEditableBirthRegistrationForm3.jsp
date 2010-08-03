<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="birth-registration-form-outer" id="birth-registration-form-3-outer">
<s:form action="eprViewBDFInNonEditableMode.do" name="nonEditableBirthRegistrationForm1" method="POST">

<s:if test="session.birthRegister.register.birthType.ordinal() != 0">
    <table class="table_reg_page_03" cellspacing="0" style="margin-top:5px">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td colspan="5" style="text-align:center;font-size:12pt">විවාහයේ විස්තර
                <br>திருமணத்தின் விபரங்கள்
                <br>Details of the Marriage
            </td>
        </tr>
        <tr>
            <td rowspan="2"><label>(25)මව්පියන් විවාහකද? <br>பெற்றோர்கள் மணம் முடித்தவர்களா? <br>Were Parent's
                Married?</label></td>
            <td rowspan="2">
                <table class="sub_table" width="100%">
                    <col width="60px"/>
                    <col width="20px" align="right"/>
                    <tbody>
                    <tr>
                        <td><label>නොදනී<br>*in tamil<br>Unknown</label></td>
                        <td><s:if test="session.birthRegister.marriage.parentsMarried==0"> <s:label
                                value="%{getText('yes.label')}"/> </s:if>
                        </td>
                    </tr>
                    <tr>
                        <td><label>ඔව්<br>*in tamil<br>Yes</label></td>
                        <td><s:if test="session.birthRegister.marriage.parentsMarried==1"> <s:label
                                value="%{getText('yes.label')}"/> </s:if></td>
                    </tr>
                    <tr>
                        <td><label>නැත<br>*in tamil<br>No</label></td>
                        <td><s:if test="session.birthRegister.marriage.parentsMarried==2"> <s:label
                                value="%{getText('yes.label')}"/> </s:if></td>
                    </tr>
                    <tr>
                        <td><label>නැත - පසුව විවාහවී ඇත<br>*in tamil<br>No but since married</label></td>
                        <td><s:if test="session.birthRegister.marriage.parentsMarried==3"> <s:label
                                value="%{getText('yes.label')}"/> </s:if></td>
                    </tr>
                    </tbody>
                </table>
            </td>
            <td><label>විවාහ වු ස්ථානය<br>விவாகம் இடம்பெற்ற இடம் <br>Place of Marriage</label></td>
            <td colspan="2"><s:label value="%{#session.birthRegister.marriage.placeOfMarriage}" /></td>
        </tr>
        <tr>
            <td><label>විවාහ වු දිනය<br>விவாகம் இடம்பெற்ற திகதி <br>Date of Marriage</label></td>
            <td colspan="2"><s:label value="%{#session.birthRegister.marriage.dateOfMarriage}"/></td>
        </tr>
        <tr>
            <td colspan="3" rowspan="2"><label>(26)මව්පියන් විවාහ වි නොමැති නම් පියාගේ තොරතුරු ඇතුලත් කර ගැනිම සදහා මව
                සහ
                පියාගේ අත්සන් <br>பெற்றோர்
                மணம் செய்யாதிருப்பின், தகப்பனின் தகவல்கள் பதிவு செய்ய வேண்டுமெனின் பெற்றோரின் கையொப்பம்<br>If
                parents are not married, signatures of mother and father to include father's particulars</label></td>
            <td><label>මවගේ අත්සන <br> தாயின் ஒப்பம் <br>Mother’s Signature</label></td>
            <td align="center"><s:if test="session.birthRegister.marriage.motherSigned==true"> <s:label value="%{getText('yes.label')}"/></s:if>
                <s:else><s:label value="%{getText('no.label')}"/></s:else></td>
        </tr>
        <tr>
            <td><label>පියාගේ අත්සන <br>தகப்பனின் ஒப்பம் <br>Father’s Signature</label></td>
            <td align="center"><s:if test="session.birthRegister.marriage.fatherSigned==true"> <s:label value="%{getText('yes.label')}"/></s:if>
             <s:else><s:label value="%{getText('no.label')}"/></s:else></td>
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
            <td colspan="5" style="text-align:center;font-size:12pt">සීයාගේ / මී මුත්තා ගේ විස්තර
                <br>தாத்தாவின் / பாட்டனின் விபரங்கள்
                <br>Details of the Grand Father / Great Grand Father
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
            <td colspan="3"><s:label value="%{#session.birthRegister.grandFather.grandFatherFullName}" cssStyle="width:98%;"/></td>
        </tr>
        <tr>
            <td><label>ඔහුගේ උපන් වර්ෂය <br>அவர் பிறந்த வருடம் <br>His Year of Birth</label></td>
            <td><s:label value="%{#session.birthRegister.grandFather.grandFatherBirthYear}"/></td>
            <td><label>උපන් ස්ථානය <br>அவர் பிறந்த இடம் <br>Place Of Birth</label></td>
            <td><s:label  value="%{#session.birthRegister.grandFather.grandFatherBirthPlace}"/></td>
        </tr>
        <tr>
            <td colspan="5"><label> (28)ළමයාගේ පියා ශ්‍රී ලංකාවේ නොඉපිද මීමුත්තා ලංකාවේ උපන්නේ නම් මී මුත්තාගේ <br>பிள்ளையின்
                தந்தை
                இலங்கையில் பிறக்காமல் பூட்டன் இலங்கையில் பிறந்திருந்தால் பூட்டனாரின் தகவல்கள
                ்<br>If the father was not
                born in Sri Lanka and if great grandfather born in Sri Lanka great grand father's</label></td>
        </tr>
        <tr>
            <td rowspan="2"></td>
            <td><label>සම්පුර්ණ නම <br>முழுப் பெயர் <br>Full Name</label></td>
            <td colspan="3"><s:label value="%{#session.birthRegister.grandFather.greatGrandFatherFullName}" cssStyle="width:98%;"/></td>
        </tr>
        <tr>
            <td><label>උපන් වර්ෂය <br>பிறந்த வருடம் <br>Year of Birth</label></td>
            <td><s:label value="%{#session.birthRegister.grandFather.greatGrandFatherBirthYear}" cssStyle="width:95%;"/></td>
            <td><label>උපන් ස්ථානය <br>அவர் பிறந்த இடம் <br>Place Of Birth</label></td>
            <td><s:label value="%{#session.birthRegister.grandFather.greatGrandFatherBirthPlace}" cssStyle="width:95%;"/></td>
        </tr>
        </tbody>
    </table>
</s:if>
<s:elseif test="session.birthRegister.register.birthType.ordinal() == 0">
    <table class="table_reg_page_03" cellspacing="0" style="margin-top:5px">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td colspan="5" style="text-align:center;font-size:12pt"> *in Sinhala<br>*in Tamil<br>Details of the
                Marriage
            </td>
        </tr>
        <tr>
            <td class="font-9" colspan="3">
                <label>(25) මවි පියන් විවාහකද?<br>* Tamil <br>Were Parents Married ?</label>
            </td>
            <td class="font-9" colspan="1">
                <s:if test="session.birthRegister.marriage.parentsMarried== 1"> <s:label
                                value="%{getText('yes.label')}"/> </s:if>
                <label> ඔවි/*in tamil / Yes</label>
            </td>
            <td class="font-9" colspan="1">
               <s:if test="session.birthRegister.marriage.parentsMarried==2"> <s:label
                                value="%{getText('yes.label')}"/> </s:if>
                <label> නැත / *in tamil / No</label>
            </td>
        </tr>
        </tbody>
    </table>
</s:elseif>

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
                    <td align="center" width="150px"><s:if test="session.birthRegister.informant.informantType.ordinal() == 1"><s:label
                                value="%{getText('yes.label')}"/></s:if></td>
                </tr>
            </table>
        </td>
        <td colspan="2" style="width:180px">
            <table class="sub_table">
                <tr>
                    <td><label>පියා<br> பிதா <br>Father</label></td>
                    <td align="center" width="150px"><s:if test="session.birthRegister.informant.informantType.ordinal() == 0"> <s:label
                                value="%{getText('yes.label')}"/> </s:if></td>
                </tr>
            </table>
        </td>
        <td>
            <table class="sub_table">
                <tr>
                    <s:if test="session.birthRegister.register.birthType.ordinal() != 0">
                        <td><label>භාරකරු<br> பாதுகாவலர் <br>Guardian</label></td>
                        <td align="center" width="150px">
                            <s:if test="session.birthRegister.informant.informantType.ordinal() == 2"> <s:label
                                value="%{getText('yes.label')}"/> </s:if></td>
                    </s:if>
                    <s:else>
                        <td><label>නෑයන් <br> * In Tamil <br>Relative</label></td>
                        <td align="center" width="150px">
                            <s:if test="session.birthRegister.informant.informantType.ordinal() == 3"> <s:label
                                value="%{getText('yes.label')}"/> </s:if></td>
                    </s:else>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3"><label>(31)දැනුම් දෙන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>தகவல்
            கொடுப்பவரின்
            தனிநபர்
            அடையாள எண் / அடையாள அட்டை இல. <br>PIN / NIC of the Informant</label></td>
        <td colspan="3"><s:label value="%{#session.birthRegister.informant.informantNICorPIN}" /></td>
    </tr>
    <tr>
        <td colspan="2"><label>(30) නම <br>கொடுப்பவரின் பெயர் <br>Name</label></td>
        <td colspan="4"><s:label value="%{#session.birthRegister.informant.informantName}" cssStyle="width:98%;"/></td>
    </tr>
    <tr>
        <td colspan="2"><label>(32)තැපැල් ලිපිනය<br>தபால் முகவரி <br>Postal Address</label></td>
        <td colspan="4"><s:label value="%{#session.birthRegister.informant.informantAddress}" cssStyle="width:98%;"/></td>
    </tr>
    <tr>
        <td><label>දුරකතනය<br>தொலைபேசி இலக்கம் <br>Telephone</label></td>
        <td colspan="2"><s:label value="%{#session.birthRegister.informant.informantPhoneNo}"  cssStyle="width:95%;"/></td>
        <td><label>ඉ -තැපැල <br>மின்னஞ்சல் <br>Email</label></td>
        <td colspan="2"><s:label value="%{#session.birthRegister.informant.informantEmail}" id="informantEmail"
                                     cssStyle="width:95%;"/></td>

    </tr>
    <tr>
        <td colspan="2"><label>දිනය <br>*in tamil<br>Date</label></td>
        <td colspan="4"><s:label value="%{#session.birthRegister.informant.informantSignDate}"/></td>
    </tr>
    </tbody>
</table>
<s:hidden name="pageNo" value="3"/>
<div class="form-submit">
    <s:submit value="%{getText('next.label')}"/>
</div>
<div class="next-previous">
    <s:url id="backUrl" action="eprViewBDFInNonEditableMode">
        <s:param name="back" value="true"/>
        <s:param name="pageNo" value="{pageNo - 1}"/>
    </s:url>
    <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</s:form>
</div>

