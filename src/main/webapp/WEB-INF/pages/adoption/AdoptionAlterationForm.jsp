<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript">
    $(function () {
        $('#child-info-min').click(function () {
            minimize("child-info");
        });
        $('#child-info-max').click(function () {
            maximize("child-info");
        });
        $('#child-info-check').click(function () {
            document.getElementById('child-info-check').disabled = true;
            enableFields(new Array('childName', 'childBirthDate', 'childGender'));
            disableFields(new Array('childNameHidden', 'childBirthDateHidden', 'childGenderHidden'));
        });
        $('#applicant-info-min').click(function () {
            minimize("applicant-info");
        });
        $('#applicant-info-max').click(function () {
            maximize("applicant-info");
        });
        $('#applicant-info-check').click(function () {
            document.getElementById('applicant-info-check').disabled = true;
            enableFields(new Array('applicantName', 'applicantAddress', 'applicantSecondAddress', 'applicantOccupation'));
            disableFields(new Array('applicantNameHidden', 'applicantAddressHidden', 'applicantSecondAddressHidden', 'applicantOccupationHidden'));
        });
        if ($('#jointApplicant').val() == 'true') {
            $('#spouse-info-min').click(function () {
                minimize("spouse-info");
            });
            $('#spouse-info-max').click(function () {
                maximize("spouse-info");
            });
            $('#spouse-info-check').click(function () {
                document.getElementById('spouse-info-check').disabled = true;
                enableFields(new Array('spouseName', 'spouseOccupation'));
                disableFields(new Array('spouseNameHidden', 'spouseOccupationHidden'));
            });
        }

        $(".datePicker").datepicker({
            changeYear:true,
            yearRange:'1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });

        init();
    });

    function init() {
        var fields;
        if ($('#jointApplicant').val() == 'true') {
            fields = new Array('childName', 'childBirthDate', 'childGender', 'applicantName', 'applicantAddress', 'applicantSecondAddress', 'applicantOccupation', 'spouseName', 'spouseOccupation');
        } else {
            fields = new Array('childName', 'childBirthDate', 'childGender', 'applicantName', 'applicantAddress', 'applicantSecondAddress', 'applicantOccupation');
        }
        disableFields(fields);
        minimize("child-info");
        minimize("applicant-info");
        if ($('#jointApplicant').val() == 'true') {
            minimize("spouse-info");
        }
    }

    function minimize(id) {
        document.getElementById(id).style.display = 'none';
        document.getElementById(id + "-min").style.display = 'none';
        document.getElementById(id + "-max").style.display = 'block';
        document.getElementById(id + "-check").style.display = 'none';
        document.getElementById(id + "-check-label").style.display = 'none';
    }

    function maximize(id) {
        document.getElementById(id).style.display = 'block';
        document.getElementById(id + "-max").style.display = 'none';
        document.getElementById(id + "-min").style.display = 'block';
        if (!document.getElementById(id + "-check").checked) {
            document.getElementById(id + "-check").style.display = 'block';
            document.getElementById(id + "-check-label").style.display = 'block';
        }
    }

    function enableFields(fieldIds) {
        for (var i = 0; i < fieldIds.length; i++) {
            document.getElementById(fieldIds[i]).disabled = false;
        }
    }

    function disableFields(fields) {
        for (i = 0; i < fields.length; i++) {
            document.getElementById(fields[i]).disabled = true;
        }
    }

    var errormsg = '';
    function validate() {
        var returnval = true;
        var domObject;

        domObject = document.getElementById('dateReceived');
        if (isFieldEmpty(domObject)) {
            errormsg += $('#emptyDateReceived').val();
        }

        domObject = document.getElementById('placeReceived');
        if (isFieldEmpty(domObject)) {
            errormsg += "\n" + $('#emptyPlace').val();
        }

        if ($('#alterationMethod').val() == 'BY_APPLICATION') {
            if (!$('input:radio[name="adoptionAlteration.declarant.declarantType"]').is(':checked')) {
                errormsg += "\n" + $('#emptyDeclarantType').val();
            }

            domObject = document.getElementById('declarantName');
            if (isFieldEmpty(domObject)) {
                errormsg += "\n" + $('#emptyDeclarantName').val();
            }

            domObject = document.getElementById('declarantAddress');
            if (isFieldEmpty(domObject)) {
                errormsg += "\n" + $('#emptyDeclarantAddress').val();
            }
        } else if ($('#alterationMethod').val() == 'BY_COURT_ORDER') {
            domObject = document.getElementById('orderNo');
            if (isFieldEmpty(domObject)) {
                errormsg += "\n" + $('#emptyOrderNo').val();
            }

            domObject = document.getElementById('orderDate');
            if (isFieldEmpty(domObject)) {
                errormsg += "\n" + $('#emptyOrderDate').val();
            }
        }
        if (errormsg != '') {
            alert(errormsg);
            returnval = false;
            errormsg = '';
        }
        return returnval;
    }
</script>
<div id="adoption-alteration-outer">
<s:actionerror cssStyle="color: red; font-size: 10pt;"/>
<s:actionmessage cssStyle="color: blue; font-size: 10pt;"/>
<s:form method="POST" name="adoptionAlteration" onsubmit="return validate();">
<table class="adoption-alteration-table-style01" style="width:1030px; font-size: 9pt;">
    <col width="32%"/>
    <col/>
    <col width="32%"/>
    <tr>
        <td></td>
        <td align="center">
            <img src="<s:url value="/images/official-logo.png"/>" alt=""/>
        </td>
        <td>
            <table class="alteration-table-style02" cellspacing="0" style="float:right;width:100%">
                <col width="120px">
                <col>
                <tr>
                    <td colspan="2" style="text-align:center;">
                        කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>
                        அலுவலக பாவனைக்காக மட்டும் <br>
                        For office use only
                    </td>
                </tr>
                <tr>
                    <td>
                        භාරගත් දිනය <s:label cssStyle="color: red;" value="*"/><br/>
                        பெறப்பட்ட திகதி<br/>
                        Recieved Date
                    </td>
                    <td><s:textfield id="dateReceived" cssClass="datePicker"
                                     name="adoptionAlteration.dateReceived"/></td>
                </tr>
                <tr>
                    <td>
                        ස්ථානය <s:label cssStyle="color: red;" value="*"/><br/>
                        Place in ta<br/>
                        Place
                    </td>
                    <td><s:textfield id="placeReceived" name="adoptionAlteration.placeReceived"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3" align="center">
            <p style="font-size: 14pt; font-weight: bold;">ළමයින් දරුකමට හදා ගැනීමේ ආඥා පනතේ 10 (5) (ඇ) වන වගන්තිය</p>

            <p style="font-size: 12pt;">දරුකමට හදා ගැනීමේ ලේඛණයෙහි සටහනක යම් විස්තරවල ඇති යම් දෝෂයක් නිවරද කිරීම පිණිස
                වූ ප්‍රකාශය</p>
        </td>
    </tr>
</table>
<table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
    <col width="350px">
    <col width="150px">
    <col width="300px">
    <col>
    <tr>
        <td>
            ලියාපදිංචි කිරීමේ අංකය<br>
            Registration No in ta<br>
            Registration Number
        </td>
        <td>
            <s:label value="%{adoption.adoptionEntryNo}"/>
        </td>
        <td>
            ලියාපදිංචි කිරීමේ දිනය
            <br>Date of Registration in ta
            <br>Date of Registration
        </td>
        <td>
            <s:label value="%{adoption.orderReceivedDate}"/><br>
            <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/>
        </td>
    </tr>
    <tr>
        <td>අධිකරණය<br/>
            நீதிமன்றம் <br/>
            Court
        </td>
        <td colspan="3"><s:label name="courtName" id="courtName"/></td>
    </tr>
    <tr>
        <td>නියෝග අංකය<br/>
            கட்டளை இலக்கம்<br/>
            Serial number
        </td>
        <td><s:label value="%{#request.adoption.courtOrderNumber}"/></td>
        <td>නියෝගය නිකුත් කල දිනය <br/>
            கட்டளை வழங்கப்பட்ட திகதி<br/>
            Issued Date
        </td>
        <td style="text-align:left;"><s:label value="%{#request.adoption.orderIssuedDate}"/></td>
    </tr>
</table>
<table class="adoption-reg-form-header-table" style="border: 1px solid #000; width: 100%;">
    <tr>
        <td>
            ළමයාගේ විස්තර
            <br>பிள்ளையின் விபரங்கள்
            <br>Child's Information
        </td>
        <td style="width:20%;text-align:right;border-right:none">
            <div id="child-info-check-label">
                <s:label value="%{getText('edit.label')}"/></div>
        </td>
        <td style="border-right:none;width:3%">
            <s:checkbox id="child-info-check" name="editChildInfo" cssStyle="float:right;"/>
        </td>
        <td style="width:2%">
            <div class="minimize-icon" id="child-info-min">[-]</div>
            <div class="maximize-icon" id="child-info-max">[+]</div>
        </td>
    </tr>
</table>
<div id="child-info">
    <table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
        <caption></caption>
        <col style="width:320px;"/>
        <col style="width:160px;"/>
        <col style="width:190px;"/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td>
                ලබා දෙන නම රාජ්‍ය භාෂාවෙන්
                <br>பெற்றுக்கொடுக்கப்படும் பெயர் அரச கரும மொழியில்
                <br>New name given in Official Language
            </td>
            <td colspan="4"><s:textarea name="adoptionAlteration.childName" id="childName"
                                        cssStyle="margin-left:5px"/>
            </td>
        </tr>
        <tr>
            <td>
                උපන් දිනය
                <br>பிறந்த திகதி
                <br>Date of Birth
            </td>
            <td colspan="2">
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
                <s:textfield id="childBirthDate" cssClass="datePicker" name="adoptionAlteration.childBirthDate"
                             cssStyle="margin-left:5px;width:200px" maxLength="10"/>
            </td>
            <td>
                ස්ත්‍රී පුරුෂ භාවය
                <br>பால்
                <br>Gender
            </td>
            <td>
                <s:select
                        list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                        name="adoptionAlteration.childGender" id="childGender"
                        cssStyle="width:150px; margin-left:5px;"/>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<%-- Hidden fields for child details. Will disable when the child info is editing --%>
<s:hidden id="childNameHidden" name="adoptionAlteration.childName"/>
<s:hidden id="childBirthDateHidden" name="adoptionAlteration.childBirthDate"/>
<s:hidden id="childGenderHidden" name="adoptionAlteration.childGender"/>
<table class="adoption-reg-form-header-table" style="border: 1px solid #000; width: 100%;">
    <tr>
        <td>අයදුම්කරුගේ විස්තර <br/>
            விண்ணப்பதாரரின் விபரங்கள்<br/>
            Applicants Details
        </td>
        <td style="width:20%;text-align:right;border-right:none">
            <div id="applicant-info-check-label">
                <s:label value="%{getText('edit.label')}"/></div>
        </td>
        <td style="border-right:none;width:3%">
            <s:checkbox id="applicant-info-check" name="editApplicantInfo" cssStyle="float:right;"/>
        </td>
        <td style="width:2%">
            <div class="minimize-icon" id="applicant-info-min">[-]</div>
            <div class="maximize-icon" id="applicant-info-max">[+]</div>
        </td>
    </tr>
</table>
<div id="applicant-info">
    <table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
        <caption></caption>
        <col width="320px"/>
        <col width="175px"/>
        <col width="175px"/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td>
                අයදුම්කරුගේ නම
                <br>விண்ணப்பதாரரின் பெயர்
                <br>Name of the Applicant
            </td>
            <td colspan="4"><s:textarea id="applicantName" name="adoptionAlteration.applicantName"/></td>
        </tr>
        <tr>
            <td>
                ලිපිනය 1
                <br>முகவரி 1
                <br>Address 1
            </td>
            <td colspan="4"><s:textarea id="applicantAddress" name="adoptionAlteration.applicantAddress"/></td>
        </tr>
        <tr>
            <td>
                ලිපිනය 2
                <br>முகவரி 2
                <br>Address 2
            </td>
            <td colspan="4"><s:textarea id="applicantSecondAddress"
                                        name="adoptionAlteration.applicantSecondAddress"/></td>
        </tr>
        <tr>
            <td>
                රැකියාව
                <br>தொழில்
                <br>Occupation
            </td>
            <td colspan="4"><s:textarea id="applicantOccupation" name="adoptionAlteration.applicantOccupation"/></td>
        </tr>
        </tbody>
    </table>
</div>
<%-- Hidden fields for applicant details. Will disable when the applicant info is editing --%>
<s:hidden id="applicantNameHidden" name="adoptionAlteration.applicantName"/>
<s:hidden id="applicantAddressHidden" name="adoptionAlteration.applicantAddress"/>
<s:hidden id="applicantSecondAddressHidden" name="adoptionAlteration.applicantSecondAddress"/>
<s:hidden id="applicantOccupationHidden" name="adoptionAlteration.applicantOccupation"/>
<s:if test="adoption.jointApplicant">
    <table class="adoption-reg-form-header-table" style="border: 1px solid #000; width: 100%;">
        <tr>
            <td>
                සහකරුගේ විස්තර
                <br>Spouse's details in ta
                <br>Spouse's details
            </td>
            <td style="width:20%;text-align:right;border-right:none">
                <div id="spouse-info-check-label">
                    <s:label value="%{getText('edit.label')}"/></div>
            </td>
            <td style="border-right:none;width:3%">
                <s:checkbox id="spouse-info-check" name="editSpouseInfo" cssStyle="float:right;"/>
            </td>
            <td style="width:2%">
                <div class="minimize-icon" id="spouse-info-min">[-]</div>
                <div class="maximize-icon" id="spouse-info-max">[+]</div>
            </td>
        </tr>
    </table>
    <div id="spouse-info">
        <table class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
            <caption></caption>
            <col width="320px"/>
            <col width="175px"/>
            <col width="175px"/>
            <col/>
            <col/>
            <tr>
                <td>
                    සහකරුගේ නම
                    <br>தாயின் பெயர்
                    <br>Name of Spouse
                </td>
                <td colspan="4"><s:textarea name="adoptionAlteration.spouseName" id="spouseName"/></td>
            </tr>
            <tr>
                <td>
                    සහකරුගේ රැකියාව
                    <br>தாயின் தொழில்
                    <br>Occupation of Spouse
                </td>
                <td colspan="4"><s:textarea name="adoptionAlteration.spouseOccupation" id="spouseOccupation"/></td>
            </tr>
        </table>
    </div>
    <%-- Hidden fields for spouse details. Will disable when the spouse info is editing --%>
    <s:hidden id="spouseNameHidden" name="adoptionAlteration.spouseName"/>
    <s:hidden id="spouseOccupationHidden" name="adoptionAlteration.spouseOccupation"/>
</s:if>
<s:if test="adoptionAlteration.method.ordinal() == 0">
    <table class="adoption-reg-form-01-table01" style=" margin-top:20px;width:100%;" cellpadding="0" cellspacing="0">
        <caption></caption>
        <col style="width:260px;"/>
        <col style="width:190px;"/>
        <col style="width:190px;"/>
        <col style="width:190px;"/>
        <col style="width:190px;"/>
        <tbody>
        <tr>
            <td colspan="5" style="text-align:center;font-size:11pt">ප්‍රකාශය කරන්නාගේ විස්තර<br>
                பிரதிக்கினை செய்பவரின் விபரங்கள்<br>
                Details of the Declarant
            </td>
        </tr>
        <tr>
            <td>ප්‍රකාශය කරන්නේ කවුරුන් විසින් ද? <s:label cssStyle="color: red;" value="*"/><br>
                தகவல் வழங்குபவர் <br>
                Person Giving declaration
            <td>
                <table style="border:none;width:100%">
                    <tr>
                        <td style="width:75%;border:none">
                            මව <br>மாதா<br>Mother
                        </td>
                        <td style="width:25%;border:none;">
                            <s:radio id="declarantType" name="adoptionAlteration.declarant.declarantType"
                                     list="#@java.util.HashMap@{'MOTHER':''}"
                                     onchange="javascript:setDeclarantPerson(1)"/></td>
                </table>
            </td>
            <td>
                <table style="border:none;width:100%">
                    <tr>
                        <td style="width:75%;border:none"> පියා<br> பிதா<br> Father
                        </td>
                        <td style="width:25%;border:none;">
                            <s:radio id="declarantType" name="adoptionAlteration.declarant.declarantType"
                                     list="#@java.util.HashMap@{'FATHER':''}"
                                     onchange="javascript:setDeclarantPerson(2)"/></td>
                </table>

            </td>
            <td>
                <table style="border:none;width:100%">
                    <tr>
                        <td style="width:75%;border:none"> භාරකරු<br> பாதுகாவலர் <br> Guardian
                        </td>
                        <td style="width:25%;border:none;">
                            <s:radio id="declarantType" name="adoptionAlteration.declarant.declarantType"
                                     list="#@java.util.HashMap@{'OTHER':''}"
                                     onchange="javascript:setDeclarantPerson(3) "/></td>
                </table>
            </td>
            <td>
                <table style="border:none;width:100%">
                    <tr>
                        <td style="width:75%;border:none"> තමුන්<br> தன்னால்<br> Self
                        </td>
                        <td style="width:25%;border:none;">
                            <s:radio id="declarantType" name="adoptionAlteration.declarant.declarantType"
                                     list="#@java.util.HashMap@{'RELATIVE':''}"
                                     onchange="javascript:setDeclarantPerson(4)"/></td>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                අනන්‍යතා අංකය
                <br>அடையாள எண்
                <br>Identification Number
            </td>
            <td colspan="2"><s:textfield id="Declarant_pinOrNic" name="adoptionAlteration.declarant.declarantNICorPIN"
                                         maxLength="12"/>
            </td>
        </tr>
        <tr>
            <td>නම <s:label cssStyle="color: red;" value="*"/><br>
                பெயர்<br>
                Name<br></td>
            <td colspan="4"><s:textarea id="declarantName" name="adoptionAlteration.declarant.declarantFullName"/></td>
        </tr>
        <tr>
            <td>තැපැල් ලිපිනය <s:label cssStyle="color: red;" value="*"/><br>
                தபால் முகவரி<br>
                Postal Address
            </td>
            <td colspan="4"><s:textarea id="declarantAddress"
                                        name="adoptionAlteration.declarant.declarantAddress"/></td>
        </tr>
        </tbody>
    </table>
</s:if>
<s:elseif test="adoptionAlteration.method.ordinal() == 1">
    <s:hidden name="adoptionAlteration.declarant.declarantType" value="OTHER"/>
    <table class="adoption-reg-form-01-table01" style=" margin-top:20px;width:100%;" cellpadding="0" cellspacing="0">
        <col width="180px"/>
        <col width="350px"/>
        <col width="180px"/>
        <col/>
        <tr>
            <td>උසාවිය <s:label cssStyle="color: red;" value="*"/><br/>Court in ta<br/>Court</td>
            <td colspan="3"><s:select list="courtList" name="courtUKey" id="courtUKey"/></td>
        </tr>
        <tr>
            <td>නියෝග අංකය <s:label cssStyle="color: red;" value="*"/><br/>Order No in ta<br/>Order Number</td>
            <td><s:textfield id="orderNo" name="adoptionAlteration.courtOrderNumber"/></td>
            <td>නියෝග දිනය <s:label cssStyle="color: red;" value="*"/><br/>Order Date in ta<br/>Order Date</td>
            <td><s:textfield id="orderDate" name="adoptionAlteration.orderDate" cssClass="datePicker"/></td>
        </tr>
    </table>
</s:elseif>
<s:hidden id="jointApplicant" value="%{adoption.jointApplicant}"/>
<s:hidden name="adoptionAlteration.idUKey"/>
<s:hidden name="adoptionAlteration.aoUKey"/>
<s:hidden id="alterationMethod" name="adoptionAlteration.method"/>

<%-- Error message labels --%>
<s:hidden id="emptyDateReceived" value="%{getText('enter.date.received.label')}"/>
<s:hidden id="emptyPlace" value="%{getText('enter.place.label')}"/>
<s:hidden id="emptyDeclarantType" value="%{getText('enter.declarant.type.label')}"/>
<s:hidden id="emptyDeclarantName" value="%{getText('enter.declarant.name.label')}"/>
<s:hidden id="emptyDeclarantAddress" value="%{getText('enter.declarant.address.label')}"/>
<s:hidden id="emptyOrderNo" value="%{getText('enter.court.order.number.label')}"/>
<s:hidden id="emptyOrderDate" value="%{getText('enter.court.order.date.label')}"/>

<div class="form-submit">
    <s:if test="editMode">
        <s:submit action="eprUpdateAdoptionAlteration" value="%{getText('update.label')}"/>
    </s:if>
    <s:else>
        <s:submit action="eprAddAdoptionAlteration" value="%{getText('save.label')}"/>
    </s:else>
</div>
</s:form>
</div>