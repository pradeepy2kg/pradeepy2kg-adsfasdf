<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="still-birth-registration-form-outer" id="still-birth-registration-form-1-outer">
    <s:form action="eprViewBDFInNonEditableMode.do" name="nonEditableStillBirthRegistrationForm1"
            id="birth-registration-form-1" method="POST">
        <s:set value="%{#session.WW_TRANS_I18N_LOCALE.language}" name="userPreferedLang"/>
        <table class="table_reg_header_01" style="font-size:9pt">
            <caption></caption>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td width="300px"></td>
                <td align="center" style="font-size:12pt; width:430px">
                    <img src="<s:url value="/images/official-logo.png"/>" alt=""/><br><label>
                    මළ උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර
                    <br>* In Tamil
                    <br>Particulars for Registration of a Still Birth</label></td>
                <td>
                    <table class="table_reg_datePicker_page_01">
                        <tr>
                            <td><label><span
                                    class="font-8">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</span></label>
                            </td>
                            <td><s:label value="%{#session.birthRegister.register.bdfSerialNo}" id="bdfSerialNo"/></td>
                        </tr>
                    </table>
                    <table class="table_reg_datePicker_page_01">
                        <tr>
                            <td><label><span
                                    class="font-8">ලියාපදිංචි කල දිනය<br>* In Tamil<br>Date of Registration</span></label>
                            </td>
                            <td><s:label value="%{#session.birthRegister.register.dateOfRegistration}"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="3">දැනුම් දෙන්නා (දෙමවිපියන් / භාරකරු) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරි වෙත
                    භාර දිය
                    යුතුය. මෙම
                    තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ මළ උපත ලියාපදිංචි කරනු ලැබේ.
                    <br>* In Tamil
                    <br>Should be perfected by the informant (Parent / Guardian) and the duly completed form should be
                    forwarded
                    to the Notifying Authority. The still birth will be registered in the Civil Registration System
                    based on the
                    information provided in this form.
                </td>
            </tr>
            </tbody>
        </table>

        <table class="table_reg_page_01" cellspacing="0" cellpadding="0">

            <caption></caption>
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
                <td class="font-9" colspan="8" style="text-align:center;">
                    මළ උපත පිලිබඳ විස්තර
                    <br>* In Tamil
                    <br>Still-birth Information
                </td>
            </tr>
            <tr style="border-left:1px solid #000000;">
                <td width="150px"><label>(1) උපන් දිනය<br> பிறந்த திகதி <br>Date of Birth</label></td>
                <td colspan="7">
                    <s:label value="%{#session.birthRegister.child.dateOfBirth}"/>
                </td>
            </tr>
            <tr>
                <td rowspan="5"><label>(2) උපන් ස්ථානය<br>பிறந்த இடம்<br> Place of Birth</label></td>
                <td width="220px"><label>දිස්ත්‍රික්කය மாவட்டம் District</label></td>
                <td colspan="6" class="table_reg_cell_01">
                    <s:if test="#userPreferedLang == 'si'">
                        <s:label
                                value="%{#session.birthRegister.register.birthDivision.dsDivision.district.siDistrictName}"/></s:if>
                    <s:elseif test="#userPreferedLang == 'en'">
                        <s:label
                                value="%{#session.birthRegister.register.birthDivision.dsDivision.district.enDistrictName}"/>
                    </s:elseif>
                    <s:else>
                        <s:label
                                value="%{#session.birthRegister.register.birthDivision.dsDivision.district.taDistrictName}"/>
                    </s:else></td>
            </tr>
            <tr>

                <td><label>D.S.කොට්ඨාශය பிரிவு D.S. Division</label></td>
                <td colspan="6" class="table_reg_cell_01" id="table_reg_cell_01">
                    <s:if
                            test="#userPreferedLang == 'si'">
                        <s:label
                                value="%{#session.birthRegister.register.birthDivision.dsDivision.siDivisionName}"
                                cssStyle="float:left;  width:240px;"/></s:if>
                    <s:elseif test="#userPreferedLang == 'en'">
                        <s:label
                                value="%{#session.birthRegister.register.birthDivision.dsDivision.enDivisionName}"
                                cssStyle="float:left;  width:240px;"/>
                    </s:elseif>
                    <s:else>
                        <s:label
                                value="%{#session.birthRegister.register.birthDivision.dsDivision.taDivisionName}"
                                cssStyle="float:left;  width:240px;"/>
                    </s:else>
                </td>
            <tr>
                <td><label>ස්ථානය பிறந்த இடம் Place</label></td>
                <td colspan="6"><s:label value="%{#session.birthRegister.child.placeOfBirth}"/></td>
            </tr>
                <%--<tr>
                    <td colspan="3"><label> රෝහලේදී /*in Tamil/In a Hospital</label></td>
                    <td colspan="1" width="260px"><label>ඔව් / *in Tamil / Yes </label></td>
                    <td style="width:75px;text-align:center;"><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'true':''}"/></td>
                    <td><label>නැත / *in Tamil / No</label></td>
                    <td style="width:75px;text-align:center;"><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'false':''}"/></td>
                </tr>--%>
            <tr>
                <td colspan="3"><label> *in Sinhala/*in Tamil/In a Hospital</label></td>
                <td colspan="1"><label>ඔව් / *in Tamil / Yes </label></td>
                <td>
                    <s:if test="session.birthRegister.child.birthAtHospital ==true">
                        <s:label value="%{getText('yes.label')}"/>
                    </s:if>
                    <s:else>
                        <s:label value="%{getText('no.label')}"/>
                    </s:else>

                </td>

                    <%--<td align="center"><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'true':''}" value="true"/></td>
                 <td><label>නැත / *in Tamil / No</label></td>
                 <td align="center"><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'false':''}"/></td>--%>
            </tr>

            <tr></tr>
            <tr>
                <td class="font-9"><label>(3) ස්ත්‍රී පුරුෂ භාවය<br> பால் <br>Gender of the child</label></td>
                <td colspan="3"><s:if test="session.birthRegister.child.childGender == 0">
                    <s:label name="" value="%{getText('male.label')}"/>
                </s:if>
                    <s:elseif test="session.birthRegister.child.childGender == 1">
                        <s:label name="" value="%{getText('female.label')}"/>
                    </s:elseif>
                    <s:elseif test="session.birthRegister.child.childGender == 2">
                        <s:label name="" value="%{getText('unknown.label')}"/>
                    </s:elseif></td>
                <td colspan="2">
                    <label>(4) දරැවා මැරී උපදින විට ගර්භයට සති කීයක් වී තිබුනේද යන්න
                        <br>* In Tamil
                        <br>Number of weeks pregnant at the time of still-birth</label></td>
                    <%--TODO attribute ?--%>
                <td colspan="2"><s:label value="%{#session.birthRegister.child.weeksPregnant}"/></td>
            </tr>
            <tr>
                <td class="font-9"><label>(5) සජිවි උපත් අනුපිළි‍‍වල අනුව කීවෙනි උපත ද? <br>பிறப்பு ஒழுங்கு <br>According
                    to Live Birth Order,
                    number of children?</label></td>
                <td colspan="3" class="font-9"><s:label value="%{#session.birthRegister.child.childRank}"/></td>
                <td colspan="2" class="font-9"><label>(6) නිවුන් දරු උපතක් නම්, දරුවන් ගනන<br>பல்வகைத்தன்மை
                    (இரட்டையர்கள்
                    எனின்),<br> பிள்னளகளின் எண்ணிக்கை<br>If
                    multiple births, number of children</label></td>
                <td colspan="2"><s:label value="%{#session.birthRegister.child.numberOfChildrenBorn}"
                                         cssStyle="width:95%;"/></td>
            </tr>
            </tbody>
        </table>

        <s:hidden name="pageNo" value="1"/>

        <table style="border:none; margin-bottom:20px;" align="center" class="form-submit">
            <tr>
                <td><s:submit value="%{getText('next.label')}"/></td>
            </tr>
        </table>

    </s:form>
</div>
