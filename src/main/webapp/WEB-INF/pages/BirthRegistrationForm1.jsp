<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-registration-form-outer" id="birth-registration-form-1-outer">
<script>
    function view_DSDivs() {
        dojo.event.topic.publish("view_DSDivs");
    }

    function view_BDDivs() {
        dojo.event.topic.publish("view_BDDivs");
    }
</script>
<s:form action="eprBirthRegistration.do" name="birthRegistrationForm1" id="birth-registration-form-1" method="POST"
        onsubmit="javascript:return validate()">
<div id="birth-registration-form-header">
    <div id="birth-registration-form-header-logo">
        <img src="<s:url value="/images/official-logo.png"/>" alt=""/>
    </div>
    <div id="birth-registration-form-header-title" class="font-12">
        <label>උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර
            <br>ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள்
            <br>Particulars for Registration of a Birth</label>
    </div>
    <div id="serial-no">
        <label><span class="font-8">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</span>
            <s:textfield name="register.bdfSerialNo" id="bdfSerialNo"/>
        </label>
    </div>
    <div id="submit-date">
        <label><span class="font-8">යොමුකළ දිනය<br>----------<br>Submitted Date</span>
            <sx:datetimepicker id="submitDatePicker" name="register.dateOfRegistration"
                               displayFormat="yyyy-MM-dd"
                               onmouseover="javascript:splitDate()"/>
        </label>
    </div>
    <div id="birth-registration-form-header-info" class="font-9">
        දැනුම් දෙන්නා (දෙමවිපියන් / භාරකරු) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරි වෙත භාර දිය යුතුය. මෙම
        තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ උපත ලියාපදිංචි කරනු ලැබේ.
        <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம்
        சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
        <br>Should be perfected by the informant (Parent / Guardian) and the duly completed form should be
        forwarded
        to the Notifying Authority. The birth will be registered in the Civil Registration System based on the
        information provided in this form.
    </div>


    <table class="table_reg_page_01" cellspacing="0">

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
                ළම‌යාගේ විස්තර
                <br>பிள்ளை பற்றிய தகவல்
                <br>Child's Information
            </td>
        </tr>
        <tr style="border-left:1px solid #000000;">
            <td class="font-9"><label>(1)උපන් දිනය<br> பிறந்த திகதி <br>Date of Birth</label></td>
            <td colspan="7">
                <sx:datetimepicker id="datePicker" name="child.dateOfBirth" displayFormat="yyyy-MM-dd"
                                   onchange="javascript:splitDate('datePicker')"/>
            </td>
        </tr>
        <tr>
            <td rowspan="5"><label>(2) උපන් ස්ථානය<br>பிறந்த இடம்<br> Place of Birth</label></td>
            <td><label>දිස්ත්‍රික්කය மாவட்டம் District</label></td>
            <td colspan="6" class="table_reg_cell_01">
                <s:select name="birthDistrictId" list="districtList" value="birthDistrictId"
                          onchange="javascript:view_BDDivs;view_DSDivs();return false;"/></td>
        </tr>
        <tr>
            <td><label>D.S.කොට්ඨාශය பிரிவு D.S. Division</label></td>
            <td colspan="6" class="table_reg_cell_01"><s:url id="loadDSDivList" action="ajaxSupport_loadDSDivList"/>
                <sx:div id="dsDivisionId" href="%{loadDSDivList}" theme="ajax" listenTopics="view_DSDivs"
                        formId="birth-registration-form-1" showLoadingText="false">
                </sx:div></td>
        </tr>
        <tr>
            <td><label>කොට්ඨාශය பிரிவு Division</label></td>
            <td colspan="6" class="table_reg_cell_01"><s:url id="loadBDDivList" action="ajaxSupport_loadBDDivList"/>
                <sx:div id="birthDivisionId" href="%{loadBDDivList}" theme="ajax" listenTopics="view_BDDivs"
                        formId="birth-registration-form-1" showLoadingText="false">
                </sx:div></td>
        </tr>
        <tr>
            <td><label>ස්ථානය பிறந்த இடம் Place</label></td>
            <td colspan="6"><s:textfield name="child.placeOfBirth"/></td>
        </tr>
        <tr>
            <td colspan="3"><label> *in Sinhala/*in Tamil/In a Hospital</label></td>
            <td colspan="1"><label>ඔව් / *in Tamil / Yes </label></td>
            <td><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'0':''}"/></td>
            <td><label style="border-left:1px solid #000;">නැත / *in Tamil / No</label></td>
            <td><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'1':''}"/></td>
        </tr>
        <tr>
            <td class="font-9"><label>(3) නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)<br>பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம்
                / தமிழ்) <br>Name in
                any of the official languages (Sinhala / Tamil)</label></td>
            <td colspan="7"><s:textarea name="child.childFullNameOfficialLang" id="childFullNameOfficialLang"/></td>
        </tr>
        <tr>
            <td class="font-9"><label>(4) නම ඉංග්‍රීසි භාෂාවෙන් <br>பிறப்பு அத்தாட்சி ….. <br>Name in English
            </label></td>
            <td colspan="7">
                <s:textarea name="child.childFullNameEnglish" id="childFullNameEnglish"/></td>
        </tr>
        <tr>
            <td class="font-9"><label>(5) නම ඉංග්‍රීසි භාෂාවෙන් <br>பிறப்பு அத்தாட்சி ….. <br>Preferred Language for
                Birth Certificate </label></td>
            <td colspan="7"><s:select list="#@java.util.HashMap@{'en':'English','si':'සිංහල','ta':'Tamil'}"
                                      name="register.preferredLanguage"></s:select></td>
        </tr>
        <tr>
            <td class="font-9"><label>(6)ස්ත්‍රී පුරුෂ භාවය<br> பால் <br>Gender of the child</label></td>
            <td colspan="3"><s:select
                    list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                    name="child.childGender" headerKey="0" headerValue="%{getText('select_gender.label')}"/></td>
            <td colspan="2"><label>(7) උපත් බර<br>பிறப்பு நிறை<br>Birth Weight (kg)</label></td>
            <td colspan="2"><s:textfield name="child.childBirthWeight" id="childBirthWeight"/></td>
        </tr>
        <tr>
            <td class="font-9"><label>(8)සජිවි උපත් අනුපිළි‍‍වල අනුව කීවෙනි ළමයා ද? <br>பிறப்பு ஒழுங்கு <br>According
                to Live Birth Order,
                number of children?</label></td>
            <td colspan="3" class="font-9"><s:textfield name="child.childRank" id="childRank"/></td>
            <td colspan="2" class="font-9"><label>(9)නිවුන් දරු උපතක් නම්, දරුවන් ගනන<br>பல்வகைத்தன்மை (இரட்டையர்கள்
                எனின்),<br> பிள்னளகளின் எண்ணிக்கை<br>If
                multiple births, number of children</label></td>
            <td colspan="2"><s:textfield name="child.numberOfChildrenBorn"/></td>
        </tr>

        </tbody>
    </table>

    <s:hidden name="pageNo" value="1"/>
    <s:hidden name="" value="register.comments"/>

    <s:hidden id="error1" value="%{getText('p1.SerialNum.error.value')}"/>
    <s:hidden id="error2" value="%{getText('p1.childName.error.value')}"/>
    <s:hidden id="error3" value="%{getText('p1.NameEnglish.error.value')}"/>
    <s:hidden id="error4" value="%{getText('p1.Weigth.error.value')}"/>
    <s:hidden id="error5" value="%{getText('p1.Rank.error.value')}"/>

    <script type="text/javascript">
        function validate()
        {
            var errormsg = "";
            var element;
            var returnval;
            var check = document.getElementById('skipjs');
            if (!check.checked) {

                element = document.getElementById('bdfSerialNo');
                if (element.value == "") {

                    errormsg = errormsg + "\n" + document.getElementById('error1').value;
                }
                element = document.getElementById('childFullNameOfficialLang');
                if (element.value == "") {
                    errormsg = errormsg + "\n" + document.getElementById('error2').value;
                }

                element = document.getElementById('childFullNameEnglish');
                if (element.value == "") {
                    errormsg = errormsg + "\n" + document.getElementById('error3').value;
                }

                element = document.getElementById('childBirthWeight');
                if (element.value == "") {
                    errormsg = errormsg + "\n" + document.getElementById('error4').value;
                }

                element = document.getElementById('childRank');
                if (element.value == "") {
                    errormsg = errormsg + "\n" + document.getElementById('error5').value;
                }
            }
            if (errormsg != "") {
                alert(errormsg);
                returnval = false;
            }
            return returnval;
        }
    </script>

    <table style="border:none; margin-bottom:20px;" align="center" class="form-submit">
        <tr>
            <td><s:checkbox name="skipjavaScript" id="skipjs" value="false">
                <s:label value="%{getText('skipvalidation.label')}"/>
            </s:checkbox>
                <s:submit value="%{getText('next.label')}"/></td>
        </tr>
    </table>

    </s:form>
</div>
