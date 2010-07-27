<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="still-birth-registration-form-outer" id="still-birth-registration-form-1-outer">
<script>
    $(function() {
        $('select#birthDistrictId').bind('change', function(evt1) {
            var id = $("select#birthDistrictId").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id},
                    function(data) {
                        var options1 = '';
                        var ds = data.dsDivisionList;
                        for (var i = 0; i < ds.length; i++) {
                            options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                        }
                        $("select#dsDivisionId").html(options1);

                        var options2 = '';
                        var bd = data.bdDivisionList;
                        for (var j = 0; j < bd.length; j++) {
                            options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                        }
                        $("select#birthDivisionId").html(options2);
                    });
        });

        $('select#dsDivisionId').bind('change', function(evt2) {
            var id = $("select#dsDivisionId").attr("value");
            $.getJSON('/popreg/crs/DivisionLookupService', {id:id, mode:2},
                    function(data) {
                        var options = '';
                        var bd = data.bdDivisionList;
                        for (var i = 0; i < bd.length; i++) {
                            options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                        }
                        $("select#birthDivisionId").html(options);
                    });
        })
    });
</script>

<s:form action="eprBirthRegistration.do" name="birthRegistrationForm1" id="birth-registration-form-1" method="POST"
        onsubmit="javascript:return validate()">

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
                    <td><label><span class="font-8">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</span></label>
                    </td>
                    <td><s:textfield name="register.bdfSerialNo" id="bdfSerialNo"/></td>
                </tr>
            </table>
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <td><label><span
                            class="font-8">ලියාපදිංචි කල දිනය<br>* In Tamil<br>Date of Registration</span></label>
                    </td>
                    <td><sx:datetimepicker id="submitDatePicker" name="register.dateOfRegistration"
                                           displayFormat="yyyy-MM-dd"
                                           onmouseover="javascript:splitDate()"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3">දැනුම් දෙන්නා (දෙමවිපියන් / භාරකරු) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරි වෙත භාර දිය
            යුතුය. මෙම
            තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ මළ උපත ලියාපදිංචි කරනු ලැබේ.
            <br>* In Tamil
            <br>Should be perfected by the informant (Parent / Guardian) and the duly completed form should be
            forwarded
            to the Notifying Authority. The still birth will be registered in the Civil Registration System based on the
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
            <sx:datetimepicker id="datePicker" name="child.dateOfBirth" displayFormat="yyyy-MM-dd"
                               onchange="javascript:splitDate('datePicker')"/>
        </td>
    </tr>
    <tr>
        <td rowspan="5"><label>(2) උපන් ස්ථානය<br>பிறந்த இடம்<br> Place of Birth</label></td>
        <td width="220px"><label>දිස්ත්‍රික්කය மாவட்டம் District</label></td>
        <td colspan="6" class="table_reg_cell_01">
            <s:select id="birthDistrictId" name="birthDistrictId" list="districtList"
                      value="birthDistrictId" cssStyle="width:240px;"/>
                <%--            <s:select name="birthDistrictId" list="districtList" value="birthDistrictId"
               onchange="javascript:view_DSDivs();return false;"/>--%>
        </td>
    </tr>
    <tr>
            <s:url id="loadDSDivList" action="../ajaxSupport_loadDSDivList"/>
        <td><label>D.S.කොට්ඨාශය பிரிவு D.S. Division</label></td>
        <td colspan="6" class="table_reg_cell_01" id="table_reg_cell_01">

                <%--            <sx:div name="dsDivisionId" id="dsDivisionId" value="dsDivisionId" href="%{loadDSDivList}" theme="ajax"
               listenTopics="view_DSDivs" formId="birth-registration-form-1"></sx:div>--%>
            <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                      cssStyle="float:left;  width:240px;"/>
            <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}"
                      list="bdDivisionList"
                      cssStyle=" width:240px;float:right;"/>
        </td>
    <tr>
        <td><label>ස්ථානය பிறந்த இடம் Place</label></td>
        <td colspan="6"><s:textfield name="child.placeOfBirth" id="placeOfBirth" size="75"/></td>
    </tr>
    <tr>
        <td><label>*in sinhala/*in tamil/ Place in English</label></td>
        <td colspan="6"><s:textfield name="child.placeOfBirthEnglish" id="placeOfBirthEnglish"
                                     cssStyle="width:97.6%;"/></td>
    </tr>
    <tr>
        <td colspan="3"><label> රෝහලේදී /*in Tamil/In a Hospital</label></td>
        <td colspan="1" width="260px"><label>ඔව් / *in Tamil / Yes </label></td>
        <td style="width:75px;text-align:center;"><s:radio name="child.birthAtHospital"
                                                           list="#@java.util.HashMap@{'true':''}"/></td>
        <td><label>නැත / *in Tamil / No</label></td>
        <td style="width:75px;text-align:center;"><s:radio name="child.birthAtHospital"
                                                           list="#@java.util.HashMap@{'false':''}"/></td>
    </tr>
    <tr></tr>
    <tr>
        <td class="font-9"><label>(3) ස්ත්‍රී පුරුෂ භාවය<br> பால் <br>Gender of the child</label></td>
        <td colspan="3"><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="child.childGender" headerKey="0" headerValue="%{getText('select_gender.label')}"/></td>
        <td colspan="2">
            <label>(4) දරැවා මැරී උපදින විට ගර්භයට සති කීයක් වී තිබුනේද යන්න
                <br>* In Tamil
                <br>Number of weeks pregnant at the time of still-birth</label></td>
            <%--TODO attribute ?--%>
        <td colspan="2"><s:textfield name="child.weeksPregnant" id="weeksPregnant" size="35"/></td>
    </tr>
    <tr>
        <td class="font-9"><label>(5) සජිවි උපත් අනුපිළි‍‍වල අනුව කීවෙනි උපත ද? <br>பிறப்பு ஒழுங்கு <br>According
            to Live Birth Order,
            number of children?</label></td>
        <td colspan="3" class="font-9"><s:textfield name="child.childRank" id="childRank"/></td>
        <td colspan="2" class="font-9"><label>(6) නිවුන් දරු උපතක් නම්, දරුවන් ගනන<br>பல்வகைத்தன்மை (இரட்டையர்கள்
            எனின்),<br> பிள்னளகளின் எண்ணிக்கை<br>If
            multiple births, number of children</label></td>
        <td colspan="2"><s:textfield name="child.numberOfChildrenBorn" size="35"/></td>
    </tr>
    </tbody>
</table>

<s:hidden name="pageNo" value="1"/>

<s:hidden id="error1" value="%{getText('p1.SerialNum.error.value')}"/>
<s:hidden id="error6" value="%{getText('p1.dob.after.submit.value')}"/>
<s:hidden id="error7" value="%{getText('p1.submit.after.90.value')}"/>
<s:hidden id="error8" value="%{getText('p1.submit.after.365.value')}"/>
<s:hidden id="error9" value="%{getText('p1.submitDate.error.value')}"/>
<s:hidden id="error10" value="%{getText('p1.dob.error.value')}"/>
<s:hidden id="error11" value="%{getText('p1.placeOfBirth.error.value')}"/>

<script type="text/javascript">
    function validate()
    {
        var errormsg = "";
        var element;
        var returnval;
        var flag = false;
        var lateOrbelate = false;
        var check = document.getElementById('skipjs');

        /*date related validations*/
        var datePicker = dojo.widget.byId('datePicker').inputNode.value;
        var submitDatePicker = dojo.widget.byId('submitDatePicker').inputNode.value;
        var birtdate = new Date(datePicker);
        var submit = new Date(submitDatePicker);

        //compare two days
        if (birtdate.getTime() > submit.getTime()) {
            errormsg = errormsg + "\n" + document.getElementById('error6').value;
            flag = true;
        }

        element = document.getElementById('bdfSerialNo');
        if (element.value == "") {
            errormsg = errormsg + "\n" + document.getElementById('error1').value;
            flag = true;
        }
        if (!(submit.getTime())) {
            errormsg = errormsg + "\n" + document.getElementById('error9').value;
            flag = true;
        }
        if (!birtdate.getTime()) {
            errormsg = errormsg + "\n" + document.getElementById('error10').value;
            flag = true;
        }
        element = document.getElementById('placeOfBirth');
        if (element.value == "") {
            errormsg = errormsg + "\n" + document.getElementById('error11').value;
            flag = true;
        }

        if (!check.checked) {
            element = document.getElementById('bdfSerialNo');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('error1').value;
                flag = true;
            }
        }

        if (errormsg != "") {
            alert(errormsg);
            if (flag) {
                returnval = false;
            } else {
                if (lateOrbelate) {
                    returnval = true;
                }
            }
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
