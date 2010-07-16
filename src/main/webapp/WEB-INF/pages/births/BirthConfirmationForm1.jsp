<%@ page import="lk.rgd.crs.api.domain.BirthDeclaration" %>
<%@ page import="lk.rgd.common.api.domain.District" %>
<%@ page import="java.util.Map" %>
<%@ page import="lk.rgd.crs.web.WebConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<div id="birth-confirmation-form-outer">
<table class="table_con_header_01" width="100%" cellpadding="0" cellspacing="0">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="300px"></td>
        <td align="center"><img src="<s:url value="/images/official-logo.png" />" alt=""/><br> <label>
            ශ්‍රී ලංකා / ﻿இலங்கை / SRI LANKA<br><br>
            දෙමව්පියන් / භාරකරු විසින් උපත තහවුරු කිරීම
            ﻿﻿ <br>பெற்றோர் அல்லது பாதுகாப்பாளா் மூலம் பிறப்பை உறுதிப்படுத்தல்
            <br>Confirmation of Birth by Parents / Guardian
        </label></td>
        <td>
            <form action="eprBirthConfirmationInit.do" method="post">
                <table style=" border:1px solid #000000; width:300px">
                    <tr><s:actionerror/></tr>
                    <tr>
                        <td><label><span class="font-8">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</span></label>
                        </td>
                        <td><s:textfield name="bdId" id="SerialNo"/></td>
                    </tr>
                </table>
                <table style=" width:300px">
                    <tr>

                    <tr>
                        <td width="200px"></td>
                        <td align="right" class="button"><s:submit name="search"
                                                                   value="%{getText('searchButton.label')}"
                                                                   cssStyle="margin-right:10px;"/></td>
                    </tr>
                </table>
            </form>
            <form action="eprBirthConfirmationSkipChanges.do" onsubmit="javascript:return validateSkipChanges()">
                <table style=" border:1px solid #000000; width:300px">
                    <tr>
                        <td><s:label value="%{getText('noConfirmationChanges.label')}"/></td>
                        <td><s:checkbox name="skipConfirmationChages" id="skipChangesCBox"/></td>
                    </tr>
                    <tr><s:hidden name="pageNo" value="2"/>
                        <s:hidden name="bdId" value="%{#request.bdId}"/>
                        <td width="170px"></td>
                        <td align="left" class="button"><s:submit name="search"
                                                                  value="%{getText('skip.label')}"
                                                                  cssStyle="margin-right:8px;"/></td>
                    </tr>
                </table>
            </form>

        </td>
    </tr>
    </tbody>
</table>


<s:form action="eprBirthConfirmation" name="birthConfirmationForm1" id="birth-confirmation-form-1" method="POST"
        onsubmit="javascript:return validate()">


<table class="table_con_page_01" width="100%" cellpadding="0" cellspacing="0" style="margin-bottom:20px;">
    <caption></caption>
    <col align="center"/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td class="cell_01">1</td>
        <td>සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ අදාල “උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර” ප්‍රකාශනයේ අනුක්‍රමික අංකය
            හා දිනය
            <br>பிறப்பை பதிவு செய்வதற்கான விபரம்" எனும் படிவத்தின் தொடா் இலக்கமும் திகதியும்
            <br>Serial Number and the Date of the ‘Particulars for Registration of a Birth’ form
        </td>
        <td><s:textfield cssClass="disable" disabled="true" name="register.bdfSerialNo"/>
            <s:textfield cssClass="disable" disabled="true" name="register.dateOfRegistration"/></td>
    </tr>
    <tr>
        <td>2</td>
        <td><label>
            *in Sinhala
            <br>* in Tamil
            <br>Last date by which changes should be received by the registrar generals office.
        </label></td>
        <td><s:textfield cssClass="disable" disabled="true" value=""/></td>
    </tr>
    </tbody>
</table>


<table class="table_con_page_01" width="100%" cellpadding="0" cellspacing="0">
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td colspan="14" style="text-align:center;font-size:12pt"> සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ ඇතුළත් විස්තර
            <br>சிவில் பதிவு அமைப்பில் உள்ளடக்கப்பட்டுள்ள விபரம்
            <br>Information included in Civil Registration System
        </td>
    </tr>
    <tr>
        <td colspan="2"><label>විස්තර <br>விபரங்கள் <br>Particulars </label></td>
        <td colspan="6" width="350px"><label>සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ දැනට අඩංගු විස්තර <br>சிவில் பதிவு
            அமைப்பில்
            உள்ளடக்கப்பட்டுள்ள
            விபரம<br>Information included in Civil Registration System </label></td>
        <td class="cell_02" colspan="6"><label>
            වෙනස් විය යුතු විස්තර අතුලත් කරන්න.
            <br>புதியசிவில் பதிவ..ண்டிய விப...
            <br>Insert new details or modify existing details</label></td>
    </tr>
    <tr>
        <td class="cell_01">3</td>
        <td class="cell_04"><label>උපන් දිනය<br>பிறந்த திகதி<br>Date of birth</label></td>
        <td class="cell_03"><label>*in Sinhala<br>*in Tamil<br>Year</label></td>
        <td class="cell_03"><s:textfield value="%{child.dateOfBirth.year+1900}" cssClass="disable" disabled="true"
                                         size="4"/></td>
        <td class="cell_03"><label>*in Sinhala<br>*in Tamil<br>Month</label></td>
        <td class="cell_03"><s:textfield value="%{child.dateOfBirth.month+1}" cssClass="disable" disabled="true"
                                         size="4"/></td>
        <td class="cell_03"><label>*in Sinhala<br>*in Tamil<br>Day</label></td>
        <td class="cell_03"><s:textfield value="%{child.dateOfBirth.date}" cssClass="disable" disabled="true"
                                         size="4"/></td>
        <td colspan="6" width="350px"><sx:datetimepicker id="submitDatePicker" name="child.dateOfBirth"
                                                         displayFormat="yyyy-MM-dd"
                                                         value="child.dateOfBirth"
                                                         onmouseover="javascript:splitDate('submitDatePicker')"/></td>
    </tr>
    <tr>
        <td>4</td>
        <td><label>ස්ත්‍රී පුරුෂ භාවය <br>பால்பால்<br>Gender</label></td>
        <td colspan="6"><s:if test="child.childGender == 0">
            <s:textfield value="%{getText('male.label')}" cssClass="disable" disabled="true"/>
        </s:if>
            <s:elseif test="child.childGender == 1">
                <s:textfield value="%{getText('female.label')}" cssClass="disable" disabled="true"/>
            </s:elseif>
            <s:elseif test="child.childGender == 2">
                <s:textfield value="%{getText('unknown.label')}" cssClass="disable" disabled="true"/>
            </s:elseif></td>
        <td colspan="6"><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="child.childGender"/></td>
    </tr>
    <tr>
        <td>5</td>
        <td colspan="13"><label>උපන් ස්ථානය / பிறந்தபிறந்த இடம் / Place of birth</label></td>
    </tr>
    <tr>
        <td></td>
        <td><label>දිස්ත්‍රික්කය <br>மாவட்டம் <br>District</label></td>
        <td colspan="6"><s:textfield value="%{getDistrictList().get(birthDistrictId)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="6"><s:select list="districtList" name="birthDistrictId"/></td>
    </tr>
    <tr>
        <td></td>
        <td><label>D.S.කොට්ඨාශය<br>பிரிவு <br>D.S. Division</label></td>
        <td colspan="6"><s:textfield value="%{getDsDivisionList().get(dsDivisionId)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="6"><s:select list="dsDivisionList" name="dsDivisionId"/></td>
    </tr>
    <tr>
        <td></td>
        <td><label>කොට්ඨාශය<br>பிரிவு <br>Registration Division</label></td>
        <td colspan="6"><s:textfield value="%{getBdDivisionList().get(birthDivisionId)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="6"><s:select name="birthDivisionId" list="bdDivisionList"/></td>
    </tr>
    <tr>
        <td></td>
        <td><label>ස්ථානය <br>பிறந்த இடம் <br>Place</label></td>
        <td colspan="6"><s:textarea name="child.placeOfBirth" cssClass="disable" disabled="true" cols="38"/></td>
        <td colspan="6"><s:textfield name="child.placeOfBirth" size="35" id="placeOfBirth"/></td>
    </tr>
    <tr>
        <td></td>
        <td><label>*in sinhala<br>*in tamil<br>Place in English</label></td>
        <td colspan="6"><s:textarea name="child.placeOfBirthEnglish" cssClass="disable" disabled="true" cols="38"/></td>
        <td colspan="6"><s:textfield name="child.placeOfBirthEnglish" size="35" id="placeOfBirthEnglish"/></td>
    </tr>
    <tr>
        <td>6</td>
        <td><label>පියාගේ අනන්‍යතා අංකය <br>தந்நையின் தனிநபர் அடையாள எண்<br>Father's PIN</label></td>
        <td colspan="6"><s:textfield name="parent.fatherNICorPIN" cssClass="disable" disabled="true"/></td>
        <td colspan="6"><s:textfield name="parent.fatherNICorPIN" size="35"/></td>
    </tr>
    <tr>
        <td>7</td>
        <td><label>පියාගේ ජාතිය <br>தந்நையின் இனம்<br>Father's Race</label></td>
        <td colspan="6"><s:textfield value="%{getRaceList().get(fatherRace)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="6"><s:select list="raceList" name="fatherRace"/></td>
    </tr>
    <tr>

    <tr>
        <td>8</td>
        <td><label>ම‌වගේ අනන්‍යතා අංකය <br>தாயின் தனிநபர் அடையாள எண<br>Mother's PIN</label></td>
        <td colspan="6"><s:textfield name="parent.motherNICorPIN" cssClass="disable" disabled="true"/></td>
        <td colspan="6"><s:textfield name="parent.motherNICorPIN" size="35"/></td>
    </tr>
    <tr>
        <td>9</td>
        <td><label>මවගේ ජාතිය <br>தாயின் இனம்<br>Mother's Race</label></td>
        <td colspan="6"><s:textfield value="%{getRaceList().get(motherRace)}" cssClass="disable"
                                     disabled="true"/></td>
        <td colspan="6"><s:select list="raceList" name="motherRace"/></td>
    </tr>
    <tr>
        <td>10</td>
        <td><label>මව්පියන් විවාහකද? <br>பெற்றார் விவாகஞ் செய்தவர்களா? <br>Were Parents Married?</label></td>
        <td colspan="6"><s:textfield name="marriage.parentsMarried" cssClass="disable" disabled="true"
                                     value="%{getText('married.status.'+marriage.parentsMarried)}"/></td>
        <td><label id="yes" class="label">*in sinhala<br>*in tamil<br>Yes</label></td>
        <td><s:radio name="marriage.parentsMarried" id="parentsMarried" list="#@java.util.HashMap@{'1':''}"
                     value="1"/></td>
        <td><label class="label">*in sinhala<br>*in tamil<br>No</label></td>
        <td><s:radio name="marriage.parentsMarried" id="parentsMarried" list="#@java.util.HashMap@{'0':''}"/></td>
        <td><label class="label">*in sinhala<br>*in tamil<br>Since Married</label></td>
        <td><s:radio name="marriage.parentsMarried" id="parentsMarried" list="#@java.util.HashMap@{'2':''}"/></td>
    </tr>
    </tbody>
</table>

<s:hidden name="pageNo" value="1"/>

<s:hidden id="p1error1" value="%{getText('cp1.error.serialNum.value')}"/>
<s:hidden id="p1error2" value="%{getText('cp1.placeOfBirth.error.value')}"/>
<s:hidden id="p1error3" value="%{getText('cp1.date.error.value')}"/>
<s:hidden id="p1error4" value="%{getText('cp1.parents.marriage.error.value')}"/>
<s:hidden id="p1errorckbx" value="%{getText('cp1.skipChanges.checked.error.value')}"/>

<script type="text/javascript">
    function validate()
    {
        var errormsg = "";
        var element;
        var returnval;

        /*date related validations*/
        var submitDatePicker = dojo.widget.byId('submitDatePicker').inputNode.value;
        var submit = new Date(submitDatePicker);
        if (!(submit.getTime())) {
            errormsg = errormsg + "\n" + document.getElementById('p1error3').value;
            flag = true;
        }
        element = document.getElementById('SerialNo');
        if (element.value == "") {
            errormsg = errormsg + "\n" + document.getElementById('p1error1').value;
        }
        element = document.getElementById('placeOfBirth');
        if (element.value == "") {
            errormsg = errormsg + "\n" + document.getElementById('p1error2').value;
            flag = true;
        }


        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        return returnval;
    }

    function validateSkipChanges() {
        var noSerialEntered=document.getElementById('p1error1').value;
        var notChecked=document.getElementById('p1errorckbx').value;
        var serial=document.getElementById('SerialNo');
        var skipChanges=document.getElementById('skipChangesCBox');
        if(serial.value==0){
            alert(noSerialEntered);
            return false;
        }
        if(!skipChanges.checked ){
           alert(notChecked);
            return false;
        }
    }
</script>

<div class="form-submit">
    <s:submit value="%{getText('next.label')}"/>
</div>
</s:form>
</div>
<%-- Styling Completed --%>