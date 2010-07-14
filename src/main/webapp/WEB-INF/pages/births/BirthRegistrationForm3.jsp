<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="birth-registration-form-outer" id="birth-registration-form-3-outer">
<s:form action="eprBirthRegistration.do" name="birthRegistrationForm3" id="birth-registration-form-3" method="POST"
        onsubmit="javascript:return validate()">

<s:if test="liveBirth">
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
                        <td><s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'0':''}"/></td>
                    </tr>
                    <tr>
                        <td><label>ඔව්<br>*in tamil<br>Yes</label></td>
                        <td><s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'1':''}" value="4"/></td>
                    </tr>
                    <tr>
                        <td><label>නැත<br>*in tamil<br>No</label></td>
                        <td><s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'2':''}"/></td>
                    </tr>
                    <tr>
                        <td><label>නැත - පසුව විවාහවී ඇත<br>*in tamil<br>No but since married</label></td>
                        <td><s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'3':''}"/></td>
                    </tr>
                    </tbody>
                </table>
            </td>
            <td><label>විවාහ වු ස්ථානය<br>விவாகம் இடம்பெற்ற இடம் <br>Place of Marriage</label></td>
            <td colspan="2"><s:textfield name="marriage.placeOfMarriage" id="placeOfMarriage"/></td>
        </tr>
        <tr>
            <td><label>විවාහ වු දිනය<br>விவாகம் இடம்பெற்ற திகதி <br>Date of Marriage</label></td>
            <td colspan="2"><sx:datetimepicker id="marriageDatePicker" name="marriage.dateOfMarriage"
                                               displayFormat="yyyy-MM-dd"
                                               onmouseover="javascript:splitDate('marriageDatePicker')"/></td>
        </tr>
        <tr>
            <td colspan="3" rowspan="2"><label>(26)මව්පියන් විවාහ වි නොමැති නම් පියාගේ තොරතුරු ඇතුලත් කර ගැනිම සදහා මව
                සහ
                පියාගේ අත්සන් <br>பெற்றோர்
                மணம் செய்யாதிருப்பின், தகப்பனின் தகவல்கள் பதிவு செய்ய வேண்டுமெனின் பெற்றோரின் கையொப்பம்<br>If
                parents are not married, signatures of mother and father to include father's particulars</label></td>
            <td><label>මවගේ අත්සන <br> தாயின் ஒப்பம் <br>Mother’s Signature</label></td>
            <td align="center"><s:checkbox name="marriage.motherSigned" id="motherSigned"/></td>
        </tr>
        <tr>
            <td><label>පියාගේ අත්සන <br>தகப்பனின் ஒப்பம் <br>Father’s Signature</label></td>
            <td align="center"><s:checkbox name="marriage.fatherSigned" id="fatherSigned"/></td>
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
            <td colspan="3"><s:textarea name="grandFather.grandFatherFullName" cssStyle="width:98%;"/></td>
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
                இலங்கையில் பிறக்காமல் பூட்டன் இலங்கையில் பிறந்திருந்தால் பூட்டனாரின் தகவல்கள
                ்<br>If the father was not
                born in Sri Lanka and if great grandfather born in Sri Lanka great grand father's</label></td>
        </tr>
        <tr>
            <td rowspan="2"></td>
            <td><label>සම්පුර්ණ නම <br>முழுப் பெயர் <br>Full Name</label></td>
            <td colspan="3"><s:textarea name="grandFather.greatGrandFatherFullName" cssStyle="width:98%;"/></td>
        </tr>
        <tr>
            <td><label>උපන් වර්ෂය <br>பிறந்த வருடம் <br>Year of Birth</label></td>
            <td><s:textfield name="grandFather.greatGrandFatherBirthYear" cssStyle="width:95%;"/></td>
            <td><label>උපන් ස්ථානය <br>அவர் பிறந்த இடம் <br>Place Of Birth</label></td>
            <td><s:textfield name="grandFather.greatGrandFatherBirthPlace" cssStyle="width:95%;"/></td>
        </tr>
        </tbody>
    </table>
</s:if>
<s:else>
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
                <s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'1':''}"/>
                <label> ඔවි/*in tamil / Yes</label>
            </td>
            <td class="font-9" colspan="1">
                <s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'2':''}"/>
                <label> නැත / *in tamil / No</label>
            </td>
        </tr>
        </tbody>
    </table>
</s:else>

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
                    <td align="center" width="150px"><s:radio name="informant.informantType" list="#{'MOTHER':''}"
                                                              onchange="javascript:setInformPerson('MOTHER',
            '%{parent.motherNICorPIN}', '%{parent.motherFullName}', '%{parent.motherAddress}',
            '%{parent.motherPhoneNo}','%{parent.motherEmail}')"/></td>
                </tr>
            </table>
        </td>
        <td colspan="2" style="width:180px">
            <table class="sub_table">
                <tr>
                    <td><label>පියා<br> பிதா <br>Father</label></td>
                    <td align="center" width="150px"><s:radio name="informant.informantType" list="#{'FATHER':''}"
                                                              onchange="javascript:setInformPerson('FATHER',
            '%{parent.fatherNICorPIN}',
            '%{parent.fatherFullName}','','','')"/></td>
                </tr>
            </table>
        </td>
        <td>
            <table class="sub_table">
                <tr>
                    <s:if test="liveBirth">
                        <td><label>භාරකරු<br> பாதுகாவலர் <br>Guardian</label></td>
                        <td align="center" width="150px">
                            <s:radio name="informant.informantType" list="#{'GUARDIAN':''}"
                                     onchange="javascript:setInformPerson('GUARDIAN','','','','','','')"/></td>
                    </s:if>
                    <s:else>
                        <td><label>නෑයන් <br> * In Tamil <br>Relative</label></td>
                        <td align="center" width="150px">
                            <s:radio name="informant.informantType" list="#{'RELATIVE':''}"
                                     onchange="javascript:setInformPerson('RELATIVE','','','','','','')"/></td>
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
        <td colspan="3"><s:textfield name="informant.informantNICorPIN" id="informantNICorPIN"/></td>
    </tr>
    <tr>
        <td colspan="2"><label>(30) නම <br>கொடுப்பவரின் பெயர் <br>Name</label></td>
        <td colspan="4"><s:textarea name="informant.informantName" id="informantName" cssStyle="width:98%;"/></td>
    </tr>
    <tr>
        <td colspan="2"><label>(32)තැපැල් ලිපිනය<br>தபால் முகவரி <br>Postal Address</label></td>
        <td colspan="4"><s:textarea name="informant.informantAddress" id="informantAddress"
                                    cssStyle="width:98%;"/></td>
    </tr>
    <tr>
        <td><label>දුරකතනය<br>தொலைபேசி இலக்கம் <br>Telephone</label></td>
        <td colspan="2"><s:textfield name="informant.informantPhoneNo" id="informantPhoneNo"
                                     cssStyle="width:95%;"/></td>
        <td><label>ඉ -තැපැල <br>மின்னஞ்சல் <br>Email</label></td>
        <td colspan="2"><s:textfield name="informant.informantEmail" id="informantEmail"
                                     cssStyle="width:95%;"/></td>

    </tr>
    <tr>
        <td colspan="2"><label>දිනය <br>*in tamil<br>Date</label></td>
        <td colspan="4"><sx:datetimepicker id="informDatePicker" name="informant.informantSignDate"
                                           displayFormat="yyyy-MM-dd"
                                           onmouseover="javascript:splitDate('secondDatePicker')"/></td>
    </tr>
    </tbody>
</table>


<s:hidden id="p3error1" value="%{getText('p3.person.error.value')}"/>
<s:hidden id="p3error2" value="%{getText('p3.Informent.Name.error.value')}"/>
<s:hidden id="p3error3" value="%{getText('p3.Informent.Address.error.value')}"/>
<s:hidden id="p3error4" value="%{getText('p3.Inform.Date.error.value')}"/>
<s:hidden id="p3error5" value="%{getText('p3.Marriage.Date.value')}"/>
<s:hidden id="p3error6" value="%{getText('p3.Marriage.place.value')}"/>
<s:hidden id="p3error7" value="%{getText('p3.father.Signature')}"/>
<s:hidden id="p3error8" value="%{getText('p3.mother.Signature')}"/>
<s:hidden id="fatherName" value="%{parent.fatherFullName}"/>
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
    }

    /**
     * validate for informent details
     */
    function validate()
    {
        var errormsg = "";
        var element;
        var returnval;
        /*date related validations*/
        var submitDatePicker = dojo.widget.byId('informDatePicker').inputNode.value;
        var submit = new Date(submitDatePicker);
        if (!(submit.getTime())) {
            errormsg = errormsg + "\n" + document.getElementById('p3error4').value;
            flag = true;
        }

        element = document.getElementsByName("informant.informantType");
        if (element.checked) {
            errormsg = errormsg + "\n" + document.getElementById('p3error1').value;
        }
        element = document.getElementById('informantName');
        if (element.value == "") {
            errormsg = errormsg + "\n" + document.getElementById('p3error2').value;
        }
        element = document.getElementById('informantAddress');
        if (element.value == "") {
            errormsg = errormsg + "\n" + document.getElementById('p3error3').value;
        }
        element = document.getElementsByName("marriage.parentsMarried")[1];
        if (element.checked)
        {
            element = document.getElementById('placeOfMarriage');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('p3error6').value;
            }

            submitDatePicker = dojo.widget.byId('marriageDatePicker').inputNode.value;
            submit = new Date(submitDatePicker);
            if (!(submit.getTime())) {
                errormsg = errormsg + "\n" + document.getElementById('p3error5').value;
                flag = true;
            }
           /* element = document.getElementById('motherSigned');
            if(!element.checked)
            {
               errormsg = errormsg + "\n" + document.getElementById('p3error8').value;
            }
            element = document.getElementById('fatherSigned');
            if(!element.checked)
            {
               errormsg = errormsg + "\n" + document.getElementById('p3error7').value;
            }*/

        }
        element = document.getElementsByName("marriage.parentsMarried")[2];
        if(element.checked ){
            element = document.getElementById('fatherName');
            if(!element.checked && element.value.length>0)
            {
               errormsg = errormsg + "\n" + document.getElementById('p3error7').value;
            }
        }

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        return returnval;
    }
</script>
<s:hidden name="pageNo" value="3"/>
<div class="form-submit">
    <s:submit value="%{getText('next.label')}"/>
</div>
<div class="next-previous">
    <s:url id="backUrl" action="eprBirthRegistration">
        <s:param name="back" value="true"/>
        <s:param name="pageNo" value="{pageNo - 1}"/>
    </s:url>
    <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
</div>
</s:form>
</div>
<%-- Styling Completed --%>

