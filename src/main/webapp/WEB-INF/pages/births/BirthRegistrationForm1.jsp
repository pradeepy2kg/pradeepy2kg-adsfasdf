<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:set value="3" name="row"/>

<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/popreg/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>

<div class="birth-registration-form-outer" id="birth-registration-form-1-outer">
<script>
    $(function() {
        $("#submitDatePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });

    $(function() {
        $("#birthDatePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });

    // mode 1 = passing District, will return DS list
    // mode 2 = passing DsDivision, will return BD list
    // any other = passing district, will return DS list and the BD list for the first DS
    $(function() {
        $('select#districtId').bind('change', function(evt1) {
            var id = $("select#districtId").attr("value");
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
        });

        $('img#childName').bind('click', function(evt3) {
            var id = $("textarea#childFullNameOfficialLang").attr("value");
            var wsMethod = "transliterate";
            var soapNs = "http://translitwebservice.transliteration.icta.com/";

            var soapBody = new SOAPObject("trans:" + wsMethod); //Create a new request object
            soapBody.attr("xmlns:trans", soapNs);
            soapBody.appendChild(new SOAPObject('InputName')).val(id);
            soapBody.appendChild(new SOAPObject('SourceLanguage')).val(0);
            soapBody.appendChild(new SOAPObject('TargetLanguage')).val(3);
            soapBody.appendChild(new SOAPObject('Gender')).val('U');

            //Create a new SOAP Request
            var sr = new SOAPRequest(soapNs + wsMethod, soapBody); //Request is ready to be sent

            //Lets send it
            SOAPClient.Proxy = "/TransliterationWebService/TransliterationService";
            SOAPClient.SendRequest(sr, processResponse1); //Send request to server and assign a callback
        });

        function processResponse1(respObj) {
            //respObj is a JSON equivalent of SOAP Response XML (all namespaces are dropped)
            $("textarea#childFullNameEnglish").val(respObj.Body[0].transliterateResponse[0].return[0].Text);
        };

        $('img#place').bind('click', function(evt4) {
            var id = $("input#placeOfBirth").attr("value");
            var wsMethod = "transliterate";
            var soapNs = "http://translitwebservice.transliteration.icta.com/";

            var soapBody = new SOAPObject("trans:" + wsMethod); //Create a new request object
            soapBody.attr("xmlns:trans", soapNs);
            soapBody.appendChild(new SOAPObject('InputName')).val(id);
            soapBody.appendChild(new SOAPObject('SourceLanguage')).val(0);
            soapBody.appendChild(new SOAPObject('TargetLanguage')).val(3);
            soapBody.appendChild(new SOAPObject('Gender')).val('U');

            //Create a new SOAP Request
            var sr = new SOAPRequest(soapNs + wsMethod, soapBody); //Request is ready to be sent

            //Lets send it
            SOAPClient.Proxy = "/TransliterationWebService/TransliterationService";
            SOAPClient.SendRequest(sr, processResponse2); //Send request to server and assign a callback
        });

        function processResponse2(respObj) {
            //respObj is a JSON equivalent of SOAP Response XML (all namespaces are dropped)
            $("input#placeOfBirthEnglish").val(respObj.Body[0].transliterateResponse[0].return[0].Text);
        }
    });

    function validate() {
        var errormsg = "";
        var element;
        var returnval;
        var flag = false;
        var lateOrbelate = false;
        var check = document.getElementById('skipjs');
        /*date related validations*/
        var birthdate = new Date(document.getElementById('birthDatePicker').value);
        var submit = new Date(document.getElementById('submitDatePicker').value);
        //compare two days
        if (birthdate.getTime() > submit.getTime()) {
            errormsg = errormsg + "\n" + document.getElementById('error6').value;
            flag = true;
        }

        var birthType = document.getElementById('birthTypeId').value;
        if (birthType != 2) {
            //comparing 90 days delay
            var one_day = 1000 * 60 * 60 * 24 ;
            var numDays = Math.ceil((submit.getTime() - birthdate.getTime()) / (one_day));
            if (numDays >= 90) {
                if (numDays >= 365) {
                    errormsg = errormsg + "\n" + document.getElementById('error8').value;
                } else {
                    errormsg = errormsg + "\n" + document.getElementById('error7').value;
                }
                lateOrbelate = true;
            }
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
        if (!birthdate.getTime()) {
            errormsg = errormsg + "\n" + document.getElementById('error10').value;
            flag = true;
        }
        element = document.getElementById('placeOfBirth');
        if (element.value == "") {
            errormsg = errormsg + "\n" + document.getElementById('error11').value;
            flag = true;
        }

        if (!check.checked) {
            element = document.getElementById('childFullNameOfficialLang');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('error2').value;
                flag = true;
            }

            element = document.getElementById('childFullNameEnglish');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('error3').value;
                flag = true;
            }

            element = document.getElementById('childBirthWeight');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('error4').value;
                flag = true;
            }
        else if (isNaN(element.value)) {
            errormsg = errormsg + "\n" + document.getElementById('error4').value;
            flag = true;
        }
            element = document.getElementById('childRank');
            if (element.value == "") {
                errormsg = errormsg + "\n" + document.getElementById('error5').value;
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


<s:form action="eprBirthRegistration.do" name="birthRegistrationForm1" id="birth-registration-form-1" method="POST"
        onsubmit="javascript:return validate()">

<table class="table_reg_header_01" style="font-size:9pt">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="300px"></td>
        <td align="center" style="font-size:12pt; width:430px"><img src="<s:url value="/images/official-logo.png"/>"
                                                                    alt=""/><br>
            <s:if test="birthType.ordinal() == 1">
                <label>
                    උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර
                    <br>ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள்
                    <br>Particulars for Registration of a Birth</label>
            </s:if>
            <s:elseif test="birthType.ordinal() == 0">
                <label>
                    මළ උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර
                    <br>* In Tamil
                    <br>Particulars for Registration of a Still Birth</label>
            </s:elseif>
            <s:elseif test="birthType.ordinal() == 2">
                දරුකමට හදාගත් ළමයකුගේ උප්පැන්නය නැවත ලියාපදිංචි කිරීම
                <br>* In Tamil
                <br>Re-registration of the Birth of an Adopted Child
            </s:elseif>
        </td>
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
                    <td colspan="2">කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>அலுவலக
                        பாவனைக்காக மட்டும்
                        <br>For office use only
                        <hr>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:if test="birthType.ordinal() == 1">
                            <label><span class="font-8">ලියාපදිංචි කල දිනය<br>in Tamil<br>Submitted Date</span></label>
                        </s:if>
                        <s:else>
                            <label><span class="font-8">ලියාපදිංචි කල දිනය<br>* In Tamil<br>Date of Registration</span></label>
                        </s:else>
                    </td>
                    <td>
                        <s:textfield name="register.dateOfRegistration" id="submitDatePicker"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <s:if test="birthType.ordinal() == 1">
                දැනුම් දෙන්නා (දෙමවිපියන් / භාරකරු) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරි වෙත භාර දිය
                යුතුය. මෙම
                තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ උපත ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம்
                சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the informant (Parent / Guardian) and the duly completed form should be
                forwarded
                to the Notifying Authority. The birth will be registered in the Civil Registration System based on the
                information provided in this form.
            </s:if>
            <s:elseif test="birthType.ordinal() == 0">
                දැනුම් දෙන්නා (දෙමවිපියන් / භාරකරු) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරි වෙත භාර දිය
                යුතුය. මෙම
                තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ මළ උපත ලියාපදිංචි කරනු ලැබේ.
                <br>* In Tamil
                <br>Should be perfected by the informant (Parent / Guardian) and the duly completed form should be
                forwarded
                to the Notifying Authority. The still birth will be registered in the Civil Registration System based on the
                information provided in this form.
            </s:elseif>
            <s:elseif test="birthType.ordinal() == 2">
                * In Sinhala
                <br>* In Tamil
                <br>Should be perfected by the adopting parents, and the duly completed form should be forwarded
                to the Assistant Registrar General in charge of the zone where the birth of the child occured; or to the
                head office of the Registrar Generals Department in Colombo. The birth will be re-registered in the
                Civil Registration System based on the information provided in this form.
            </s:elseif>
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
            <s:if test="birthType.ordinal() == 0">
                මළ උපත පිලිබඳ විස්තර
                <br>* In Tamil
                <br>Still-birth Information
            </s:if>
            <s:else>
                ළම‌යාගේ විස්තර
                <br>பிள்ளை பற்றிய தகவல்
                <br>Child's Information
            </s:else>
        </td>
    </tr>
        <%--TODO style not added--%>
    <s:if test="birthType.ordinal() == 2">
        <tr style="border-left:1px solid #000000;">
            <td width="150px" colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)දරුකමට
                ගැනීම පිළිබඳ සහතික පත්‍රයේ අංකය<br> * In Tamil<br>Serial
                Number of the Certificate of Adoption</label></td>
            <td colspan="7">
                <s:label value="%{#session.birthRegister.register.adoptionUKey}"/>
            </td>
        </tr>
        <tr>
            <td rowspan="5"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) ළමයාගේ උපත කලින්
                ලියාපදිංචි කර තිබුනේනමි<br>* In Tamil<br>If the birth was
                previously registered</label></td>
            <td><label>දිස්ත්‍රික්කය / மாவட்டம் / District</label></td>
            <td colspan="6" class="table_reg_cell_01">
                <s:label value="%{#session.oldBdfForAdoption.districtName}"/>
            </td>
        </tr>
        <tr>
            <td><label>ප්‍රාදේශීය ලේකමි කොටිඨාශය/<br>* In Tamil/<br>Divisional Secretariat</label></td>
            <td colspan="6" class="table_reg_cell_01">
                <s:label value="%{#session.oldBdfForAdoption.dsDivisionName}"/>
            </td>
        </tr>
        <tr>
            <td><label>ලියාපදිංචි කිරීමේ කොටිඨාශය/<br>* In Tamil/<br>Registration Division</label></td>
            <td colspan="6" class="table_reg_cell_01">
                <s:label value="%{#session.oldBdfForAdoption.bdDivisionName}"/>
            </td>
        </tr>
        <tr>
            <td><label>අනුක්‍රමික අංකය/ தொடர் இலக்கம்<br>Serial Number</label></td>
            <td colspan="6"><s:label value="%{#session.oldBdfForAdoption.serialNumber}"/></td>
        </tr>
    </s:if>
    <tr></tr>
    <tr style="border-left:1px solid #000000;">
        <td width="150px" align="left"><label>(1)උපන් දිනය<br> பிறந்த திகதி <br>Date of Birth</label></td>
        <td colspan="7">
            <s:textfield id="birthDatePicker" name="child.dateOfBirth"/>
        </td>
    </tr>
    <tr>
        <td rowspan="6"><label>(2) උපන් ස්ථානය<br>பிறந்த இடம்<br> Place of Birth</label></td>
        <td><label>දිස්ත්‍රික්කය மாவட்டம் District</label></td>
        <td colspan="6" class="table_reg_cell_01">
            <s:select id="districtId" name="birthDistrictId" list="districtList" value="birthDistrictId"
                      cssStyle="width:98.5%;"/>
        </td>
    </tr>

    <tr>
        <td><label>ප්‍රාදේශීය ලේකමි කොටිඨාශය/<br/>பிரிவு / <br/>Divisional Secretariat</label></td>
        <td colspan="6" class="table_reg_cell_01" id="table_reg_cell_01">
            <s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="%{dsDivisionId}"
                      cssStyle="float:left;  width:240px;"/>

        </td>
    </tr>
    <tr>
        <td><label>
            ලියාපදිංචි කිරීමේ කොට්ඨාශය /<br/>
            பிரிவு /<br/>
            Registration Division</label>
        </td>
        <td colspan="6">
            <s:select id="birthDivisionId" name="birthDivisionId" value="%{birthDivisionId}" list="bdDivisionList"
                      cssStyle=" width:240px;float:left;"/>
        </td>
    </tr>
    <tr>

        <td><label>සිංහල හෝ දෙමළ භාෂාවෙන් <br>சிங்களம் தமிழ் <br>In Sinhala or Tamil</label></td>
        <td colspan="6"><s:textfield name="child.placeOfBirth" id="placeOfBirth" cssStyle="width:97.6%;"/></td>
    </tr>
    <tr>
        <td><label>ඉංග්‍රීසි භාෂාවෙන් <br>இங்கிலீஷ் <br>In English</label></td>
        <td colspan="6">
            <s:textfield name="child.placeOfBirthEnglish" id="placeOfBirthEnglish" cssStyle="width:97.6%;"/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;" id="place">
        </td>
    </tr>
    <tr>
        <td colspan="3"><label>උපත සිදුවුයේ රෝහලකද? <br>In Tamil <br>Did the birth occur at a hospital?</label></td>
        <td colspan="1"><label>ඔව් / *in Tamil / Yes </label></td>
        <td align="center"><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'true':''}"
                                    value="true"/></td>
        <td><label>නැත / *in Tamil / No</label></td>
        <td align="center"><s:radio name="child.birthAtHospital" list="#@java.util.HashMap@{'false':''}"/></td>
    </tr>
    <s:if test="birthType.ordinal() != 0">
        <tr>
            <td class="font-9"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) නම රාජ්‍ය භාෂාවෙන්
                (සිංහල / දෙමළ)<br>பிறப்பு
                அத்தாட்சி
                பாத்த.... (சிங்களம்
                / தமிழ்) <br>Name in
                any of the official languages (Sinhala / Tamil)</label></td>
            <td colspan="7">
                <s:textarea name="child.childFullNameOfficialLang" id="childFullNameOfficialLang"
                            cssStyle="width:98.2%;"/>
            </td>
        </tr>

        <tr>
            <td class="font-9"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) නම ඉංග්‍රීසි
                භාෂාවෙන් <br>பிறப்பு அத்தாட்சி
                ….. <br>Name in English
            </label></td>
            <td colspan="7">
                <s:textarea name="child.childFullNameEnglish" id="childFullNameEnglish"
                            cssStyle="width:98.2%;text-transform: uppercase;"/>
                <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px;"
                     id="childName">
            </td>
        </tr>
    </s:if>
    <tr>
        <td class="font-9" colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) උප්පැන්න
            සහතිකය නිකුත් කල යුතු භාෂාව <br>பிறப்பு அத்தாட்சி ….. <br>Preferred
            Language for
            Birth Certificate </label></td>
        <td colspan="6"><s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'Tamil'}"
                                  name="register.preferredLanguage"
                                  cssStyle="width:190px; margin-left:5px;"></s:select></td>
    </tr>
    <tr>
        <td class="font-9"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)ස්ත්‍රී පුරුෂ භාවය<br>
            பால் <br>Gender of the child</label></td>
        <td colspan="3"><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="child.childGender" headerKey="0" headerValue="%{getText('select_gender.label')}"
                cssStyle="width:190px; margin-left:5px;"/></td>
        <s:if test="birthType.ordinal() == 1">
            <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) උපත් බර<br>பிறப்பு
                நிறை<br>Birth Weight (kg)</label></td>
            <td colspan="2"><s:textfield name="child.childBirthWeight" id="childBirthWeight"
                                         cssStyle="width:95%;"/></td>
        </s:if>
        <s:if test="birthType.ordinal() == 2">
            <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) උපත් බර (දන්නේ
                නමි)<br>பிறப்பு நிறை<br>Birth Weight, if known (kg)</label></td>
            <td colspan="2"><s:textfield name="child.childBirthWeight" id="childBirthWeight"
                                         cssStyle="width:95%;"/></td>
        </s:if>
        <s:elseif test="birthType.ordinal() == 0">
            <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) දරැවා මැරී උපදින විට
                ගර්භයට සති කීයක් වී තිබුනේද යන්න
                <br>* In Tamil
                <br>Number of weeks pregnant at the time of still-birth</label></td>
            <td colspan="2"><s:textfield name="child.weeksPregnant" id="weeksPregnant" cssStyle="width:95%;"/></td>
        </s:elseif>
    </tr>
    <tr>
        <td class="font-9"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)සජිවි උපත් අනුපිළි‍‍වල
            අනුව කීවෙනි ළමයා ද? <br>பிறப்பு ஒழுங்கு <br>According
            to Live Birth Order,
            rank of the child?</label></td>
        <td colspan="3" class="font-9"><s:textfield name="child.childRank" id="childRank"/></td>
        <td colspan="2" class="font-9"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)නිවුන් දරු
            උපතක් නම්, දරුවන් ගනන<br>பல்வகைத்தன்மை (இரட்டையர்கள்
            எனின்),<br> பிள்னளகளின் எண்ணிக்கை<br>If
            multiple births, number of children</label></td>
        <td colspan="2"><s:textfield name="child.numberOfChildrenBorn" cssStyle="width:95%;"/></td>
    </tr>

    </tbody>
</table>

<s:hidden name="pageNo" value="1"/>
<s:hidden name="rowNumber" value="%{row}"/>
<s:hidden id="birthTypeId" value="%{birthType.ordinal()}"/>

<s:hidden id="error1" value="%{getText('p1.SerialNum.error.value')}"/>
<s:hidden id="error2" value="%{getText('p1.childName.error.value')}"/>
<s:hidden id="error3" value="%{getText('p1.NameEnglish.error.value')}"/>
<s:hidden id="error4" value="%{getText('p1.Weigth.error.value')}"/>
<s:hidden id="error5" value="%{getText('p1.Rank.error.value')}"/>
<s:hidden id="error6" value="%{getText('p1.dob.after.submit.value')}"/>
<s:hidden id="error7" value="%{getText('p1.submit.after.90.value')}"/>
<s:hidden id="error8" value="%{getText('p1.submit.after.365.value')}"/>
<s:hidden id="error9" value="%{getText('p1.submitDate.error.value')}"/>
<s:hidden id="error10" value="%{getText('p1.dob.error.value')}"/>
<s:hidden id="error11" value="%{getText('p1.placeOfBirth.error.value')}"/>


<script type="text/javascript">
</script>

<div class="skip-validation">
    <s:checkbox name="skipjavaScript" id="skipjs" value="false">
        <s:label value="%{getText('skipvalidation.label')}"/>
    </s:checkbox>
</div>
<div class="form-submit">
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
</div>
