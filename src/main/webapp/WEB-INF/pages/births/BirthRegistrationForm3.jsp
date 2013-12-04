<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:set value="rowNumber" name="row"/>
<s:if test="birthType.ordinal()==0">
    <%--still birth--%>
    <s:set name="row" value="25"/>
</s:if>
<s:elseif test="birthType.ordinal()==1 || birthType.ordinal()==3">
    <%--live birth--%>
    <s:set name="row" value="27"/>
</s:elseif>
<s:elseif test="birthType.ordinal()==27">
    <%--adoption--%>
    <s:set name="row" value="1"/>
</s:elseif>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>

</HEAD>
<BODY onload="noBack();"
    onpageshow="if (event.persisted) noBack();" onunload="">
<%--back button disable--%>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<script type="text/javascript">
$(function() {
    $("#marriageDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});
$(function() {
    $("#informDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});
$(function() {
    $('img#informant_lookup').bind('click', function(evt1) {
        var id1 = $("input#informantNICorPIN").attr("value");

        $("textarea#informantName").val('');
        $("textarea#informantAddress").val('');
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                function(data1) {
                    if (data1 != null) {
                        $("textarea#informantName").val(data1.fullNameInOfficialLanguage);
                        $("textarea#informantAddress").val(data1.lastAddress);
                    }
                });
    });

    $('img#grandFather_lookup').bind('click', function(evt2) {
        var id2 = $("input#grandFatherNICorPIN").attr("value");

        $("textarea#grandFatherFullName").val('');
        $("input#grandFatherBirthPlace").val('');
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id2},
                function(data2) {
                    if (data2 != null) {
                        $("textarea#grandFatherFullName").val(data2.fullNameInOfficialLanguage);
                        $("input#grandFatherBirthPlace").val(data2.placeOfBirth);
                    }
                });
    });

    $('img#greatGrandFather_lookup').bind('click', function(evt3) {
        var id3 = $("input#greatGrandFatherNICorPIN").attr("value");

        $("textarea#greatGrandFatherFullName").val('');
        $("input#greatGrandFatherBirthPlace").val('');
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id3},
                function(data3) {
                    if (data3 != null) {
                        $("textarea#greatGrandFatherFullName").val(data3.fullNameInOfficialLanguage);
                        $("input#greatGrandFatherBirthPlace").val(data3.placeOfBirth);
                    }
                });
    });

    $('#grandFatherNICorPIN').change(function() {
        generateGrandFatherBirthYear('grandFatherNICorPIN', 'grandFatherBirthYear');
    });

    $('#greatGrandFatherNICorPIN').change(function() {
        generateGrandFatherBirthYear('greatGrandFatherNICorPIN', 'greatGrandFatherBirthYear');
        ;
    });
});

var informPerson;
function setInformPerson(id) {
    var name,nICorPIN,address,phonoNo,email;
    var informantName = document.getElementById("informantName");
    var informantNICorPIN = document.getElementById("informantNICorPIN");
    var informantAddress = document.getElementById("informantAddress");
    var informantPhoneNo = document.getElementById("informantPhoneNo");
    var informantEmail = document.getElementById("informantEmail");
    switch (id) {
        case 1:
            name = document.getElementById("motherFullNameLable").value;
            nICorPIN = document.getElementById("motherNICorPINLable").value;
            address = document.getElementById("motherAddressLable").value;
            phonoNo = document.getElementById("motherPhoneNoLable").value;
            email = document.getElementById("motherEmailLable").value;
            break;
        case 2:
            name = document.getElementById("fatherFullNameLable").value;
            nICorPIN = document.getElementById("fatherNICorPINLable").value;
            address = "";
            if (document.getElementsByName("marriage.parentsMarried")[0] != null) {
                if (document.getElementsByName("marriage.parentsMarried")[0].checked ||
                        document.getElementsByName("marriage.parentsMarried")[2].checked) {
                    address = document.getElementById("motherAddressLable").value;
                }
            }
            phonoNo = "";
            email = "";
            break;
        case 3:
            name = "";
            nICorPIN = "";
            address = "";
            phonoNo = "";
            email = "";
            break;
    }
    informantName.value = name;
    informantNICorPIN.value = nICorPIN;
    informantAddress.value = address;
    informantPhoneNo.value = phonoNo;
    informantEmail.value = email;


}

var errormsg = "";
// validations for BDF page 3
function validate() {
    // used to skip non trivial validation in the page
    var declarationType = document.getElementById('birthTypeId');
    var returnval = true;

    if (declarationType.value == 0) {
    }
    //validation for live birth
    if (declarationType.value == 1) {
        commonTags();
        validateParentsMaritalStatus();
    }
    if (declarationType.value == 2) {
        commonTags();
        validateParentsMaritalStatus();
    }

    validateInformant();
    if (declarationType.value != 0) {
        var check = document.getElementById('skipjs');
        if (!check.checked) {
            var reg = /^([1-9][0-9]{3})$/;
            if (document.getElementById('grandFatherBirthYear').value.search(reg) == 0) {
                validateBirthYearWithNIC('grandFatherNICorPIN', 'grandFatherBirthYear', 'p3error18');
            }
            if (document.getElementById('greatGrandFatherBirthYear').value.search(reg) == 0) {
                validateBirthYearWithNIC('greatGrandFatherNICorPIN', 'greatGrandFatherBirthYear', 'p3error19');
            }
        }
    }

    var out = checkActiveFieldsForSyntaxErrors('birth-registration-form-3');
    if(out != ""){
        errormsg = errormsg + out;
    }

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }
    errormsg = "";
    return returnval;
}

function commonTags() {
    var domObject;


    // validate date of marriage
    domObject = document.getElementById('marriageDatePicker');
    if (!isFieldEmpty(domObject))
        isDate(domObject.value, 'error11', 'error13');

    // validate grandfather PIN or NIC
    domObject = document.getElementById('grandFatherNICorPIN');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'error11', 'error14');


    // validate grandfather birth year
    domObject = document.getElementById('grandFatherBirthYear');
    if (!isFieldEmpty(domObject))
        validateBirthYear(domObject, 'error11', 'p3error11');

    // validate great grandfather PIN or NIC
    domObject = document.getElementById('greatGrandFatherNICorPIN');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'error11', 'error15');

    // validate great grandfather birth year
    domObject = document.getElementById('greatGrandFatherBirthYear');
    if (!isFieldEmpty(domObject))
        validateBirthYear(domObject, 'error11', 'p3error10');
}

//generate Grand Father birth year
function generateGrandFatherBirthYear(grandFatherNIC, grandFatherBirthYear) {
    var regNIC = /^([0-9]{9}[X|x|V|v])$/;
    var domObject = document.getElementById(grandFatherNIC);
    var domYear = document.getElementById(grandFatherBirthYear);
    if (domObject.value.search(regNIC) == 0) {
        domYear.value = 19 + domObject.value.substring(0, 2);
    }
}


// validation marriage related fields
function validateParentsMaritalStatus() {
    var domObject;

    // for parents married - Yes
    domObject = document.getElementsByName("marriage.parentsMarried")[0];
    validateMarriageDetails(domObject);

    // for parents married - No
    domObject = document.getElementsByName("marriage.parentsMarried")[1];
    validateParentSigns(domObject);

    // for parents married - No, but since married
    domObject = document.getElementsByName("marriage.parentsMarried")[2];
    validateMarriageDetails(domObject);

    // for parents married - unknown
    domObject = document.getElementsByName("marriage.parentsMarried")[3];
    validateUnknownStatus(domObject);
}

function validateUnknownStatus(domObject) {
    if (domObject.checked) {
        var element = document.getElementById('fatherName');

        if (!isFieldEmpty(element)) {
            errormsg = errormsg + "\n" + document.getElementById('p3error9').value;
        }
    }
}

function validateParentSigns(domObject) {
    if (domObject.checked) {
        var element = document.getElementById('fatherName');

        var element3 = document.getElementById('fatherSigned');
        if (!element3.checked && element.value.length > 0) {
            errormsg = errormsg + "\n" + document.getElementById('p3error7').value;
        }
        // validate mother signed
        var element4 = document.getElementById('motherSigned');
        if (!element4.checked && element.value.length > 0) {
            errormsg = errormsg + "\n" + document.getElementById('p3error8').value;
        }
    }
}

// validate place of marriage
function validateMarriageDetails(domObject) {
    var check=document.getElementById('skipjs');

    if (domObject.checked && !check.checked) {
        domObject = document.getElementById('placeOfMarriage');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('p3error6').value;
        }

        domObject = document.getElementById('marriageDatePicker');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('p3error5').value;
        }
    }
}

// validate informant fields
function validateInformant() {
    var domObject;

    checkInformantType();

    // check informant PIN or NIC
    domObject = document.getElementById('informantNICorPIN');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'error11', 'error12')

    // check informant name
    domObject = document.getElementById('informantName');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('p3error2').value;
    }

    // check informant address
    domObject = document.getElementById('informantAddress');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('p3error3').value;
    }

    domObject = document.getElementById('informantPhoneNo');
    if (!isFieldEmpty(domObject))
        validatePhoneNo(domObject, 'error11', 'error16');

    // validating informant email
    domObject = document.getElementById('informantEmail');
    if (!isFieldEmpty(domObject))
        validateEmail(domObject, 'error11', 'error10')

    // check informant sign date
    // validate infomant date
    domObject = document.getElementById('informDatePicker');
    if (isFieldEmpty(domObject))
        errormsg = errormsg + "\n" + document.getElementById('p3error4').value;
    else
        isDate(domObject.value, "error11", "infomantDate");
    var submit = new Date(document.getElementById('submitDatePicker').value);
    domObject = new Date(domObject.value);
    if (domObject.getTime() > submit.getTime()) {
        errormsg = errormsg + "\n" + document.getElementById('error17').value;
    }
    // informant date after child date of birth
    domObject = new Date(document.getElementById('informDatePicker').value);
    var dob = new Date(document.getElementById('dateOfBirth').value);
    if (domObject.getTime() < dob.getTime()) {
        errormsg = errormsg + "\n" + document.getElementById('error18').value;
    }
}

// check informant type selected
function checkInformantType() {
    var declarationType = document.getElementById('birthTypeId');

    var mother = document.getElementById('informantTypeMOTHER').checked;
    var father = document.getElementById('informantTypeFATHER').checked;

    if (declarationType.value == 0) {
        var relative = document.getElementById('informantTypeRELATIVE').checked;
        informantAvailable(mother, father, relative);
    } else {
        var guardian = document.getElementById('informantTypeGUARDIAN').checked;
        informantAvailable(mother, father, guardian);
    }

}

function validateBirthYear(domElement, errorText, errorCode) {
    with (domElement) {
        var reg = /^([1-9][0-9]{3})$/;
        if (reg.test(value) == false) {
            printMessage(errorText, errorCode);
        }
    }
}

function informantAvailable(inf1, inf2, inf3) {
    if (!(inf1 || inf2 || inf3))
        errormsg = errormsg + "\n" + document.getElementById('p3error1').value;
}

function disableMarriage(mode) {
    if (mode) {
        document.getElementById('placeOfMarriage').value = null;
        document.getElementById('marriageDatePicker').value = null;
    }
    document.getElementById('placeOfMarriage').disabled = mode;
    document.getElementById('marriageDatePicker').disabled = mode;
}


function disableSigns(mode) {
    if (!mode) {
        var name = document.getElementById('fatherName');
        if (name.value.length == 0) {
            mode = true;
        }
    }
    showSignRequired(!mode);
    document.getElementById('motherSigned').disabled = mode;
    document.getElementById('fatherSigned').disabled = mode;
    if (mode) {
        document.getElementById('fatherSigned').checked = false;
        document.getElementById('motherSigned').checked = false;
    }
}

function disableSignature() {
    var mode;
    var name = document.getElementById('fatherName');
    if (name.value.length == 0) {
        mode = true;
    }
    showSignRequired(!mode);
    if (mode) {
        document.getElementById('fatherSigned').checked = false;
        document.getElementById('motherSigned').checked = false;
        document.getElementById('motherSigned').disabled = true;
        document.getElementById('fatherSigned').disabled = true;
    }
}

function initPage() {
    var declarationType = document.getElementById('birthTypeId');
    if (declarationType.value != 0) {
        if (document.getElementsByName("marriage.parentsMarried")[0].checked) {
            disableSigns(true);
            showMarriageRequired(true);
            showSignRequired(false);
        } else if (document.getElementsByName("marriage.parentsMarried")[1].checked) {
            disableMarriage(true);
            disableSignature();
            showMarriageRequired(false);
        } else if (document.getElementsByName("marriage.parentsMarried")[2].checked) {
            //disableSigns(true);
            disableSignature();
            showMarriageRequired(true);

        } else if (document.getElementsByName("marriage.parentsMarried")[3].checked) {
            disableMarriage(true);
            disableSigns(true);
            showMarriageRequired(false);
            showSignRequired(false);
        }
    }
}

function showMarriageRequired(mode) {
    if (mode) {
        document.getElementById('placeStar').style.display = '';
        document.getElementById('dateStar').style.display = '';
    }
    else {
        document.getElementById('placeStar').style.display = 'none';
        document.getElementById('dateStar').style.display = 'none';
    }
}

function showSignRequired(mode) {
    if (mode) {
        document.getElementById('signatureStars').style.display = '';
    }
    else {
        document.getElementById('signatureStars').style.display = 'none';
    }
}

function maxLengthCalculate(id, max, divId) {
    var dom = document.getElementById(id).value;
    if (dom.length > max) {
        document.getElementById(divId).innerHTML = document.getElementById('maxLengthError').value + " : " + max
    }
    else {
        document.getElementById(divId).innerHTML = "";
    }
}

</script>

<div class="birth-registration-form-outer" id="birth-registration-form-3-outer">
<s:form action="eprBirthRegistration.do" name="birthRegistrationForm3" id="birth-registration-form-3" method="POST"
        onsubmit="javascript:return validate()">

<s:if test="birthType.ordinal() != 0">
    <table class="table_reg_page_03" cellspacing="0" style="margin-top:5px">
        <caption></caption>
        <col width="450px"/>
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
            <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) මව්පියන් විවාහකද?
                <br>பெற்றோர்கள் திருமணம் முடித்தவர்களா?
                <br>Were Parent's Married?</label></td>
            <td colspan="4">
                <table class="sub_table" width="100%" cellspacing="0" cellpadding="0">
                    <col width="40%"/>
                    <col width="10%" align="right"/>
                    <col width="40%"/>
                    <col width="10%" align="right"/>
                    <tbody>
                    <tr>
                        <td style="border-bottom: solid 1px black;"><label>ඔව්<br>ஆம்<br>Yes</label></td>
                        <td style="border-bottom: solid 1px black;border-right:solid 1px black"><s:radio
                                name="marriage.parentsMarried" list="#@java.util.HashMap@{'MARRIED':''}"
                                onclick="disableMarriage(false);disableSigns(true);showMarriageRequired(true);showSignRequired(false);"
                                id="marriedId1"/>
                        </td>
                        <td style="border-bottom: solid 1px black;"><label>නැත<br>இல்லை<br>No</label></td>
                        <td style="border-bottom: solid 1px black;"><s:radio name="marriage.parentsMarried"
                                                                             list="#@java.util.HashMap@{'UNMARRIED':''}"
                                                                             onclick="disableMarriage(true);disableSigns(false);showMarriageRequired(false);"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label>නැත, නමුත් පසුව විවාහවී ඇත<br>இல்லை, பின் விவாகமாணவா்கள்<br>No, but since
                            married</label>
                        </td>
                        <td style="border-right:solid 1px black"><s:radio name="marriage.parentsMarried"
                                                                          list="#@java.util.HashMap@{'NO_SINCE_MARRIED':''}"
                                                                          id="marriedId3"
                                                                          onclick="disableMarriage(false);disableSigns(true);showMarriageRequired(true);showSignRequired(false);"/>
                        </td>
                        <td><label>නොදනී<br>தெரியாது<br>Unknown</label></td>
                        <td><s:radio name="marriage.parentsMarried" list="#@java.util.HashMap@{'UNKNOWN':''}"
                                     onclick="disableMarriage(true);disableSigns(true);showMarriageRequired(false);showSignRequired(false);"
                                     id="unknownMarriage"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        <tr>
            <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                විවාහ වු ස්ථානය / விவாகம் இடம்பெற்ற இடம் / Place of Marriage <s:label id="placeStar" value="*"
                                                                                      cssStyle="width:10px;color:red;font-size:14pt;"/>
            </label></td>
            <td colspan="4"><s:textfield name="marriage.placeOfMarriage" id="placeOfMarriage"
                                         cssStyle="float:left;"/></td>
        </tr>
        <tr>
            <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                විවාහ වු දිනය / விவாகம் இடம்பெற்ற திகதி / Date of Marriage <s:label id="dateStar" value="*"
                                                                                    cssStyle="color:red;font-size:14pt;"/>
            </label></td>
            <td colspan="4">
                    <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
                    <s:textfield name="marriage.dateOfMarriage" id="marriageDatePicker"
                                 cssStyle="float:left;margin-left:5px;" maxLength="10"/>
        </tr>
        <tr id="motherFatherSign">
            <td colspan="5"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                මව්පියන් විවාහ වි නොමැති නම් පියාගේ තොරතුරු ඇතුලත් කර ගැනිම සදහා මව සහ පියාගේ අත්සන්
                <s:label id="signatureStars" value="*" cssStyle="color:red;font-size:14pt;"/>
                <br>பெற்றோர் விவாகம் செய்யாதிருப்பின், தகப்பனின் தகவல்கள் பதிவு செய்ய வேண்டுமெனின் பெற்றோரின் கையொப்பம்
                <br>If parents are not married, signatures of mother and father to include father's particulars</label>
            </td>
        </tr>
        <tr>
            <td><label>මවගේ අත්සන <br> தாயின் ஒப்பம் <br>Mother’s Signature</label></td>
            <td align="center"><s:checkbox name="marriage.motherSigned" id="motherSigned"/></td>
            <td><label>පියාගේ අත්සන <br>தகப்பனின் ஒப்பம் <br>Father’s Signature</label></td>
            <td align="center"><s:checkbox name="marriage.fatherSigned" id="fatherSigned"/></td>
        </tr>
        </tbody>
    </table>

    <table class="table_reg_page_03" cellspacing="0">
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td colspan="7" style="text-align:center;font-size:12pt">
                මුත්තා / මී මුත්තා ගේ විස්තර
                <br>பாட்டன் /பூட்டனின் விபரங்கள்
                <br>Details of the Grand Father / Great Grand Father
            </td>
        </tr>
        <tr>
            <td colspan="7"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) ළමයාගේ මුත්තා ශ්‍රී
                ලංකාවේ උපන්නේ නම්
                / பிள்ளையின் பாட்டனார் இலங்கையில் பிறந்திருந்தால்
                / If grandfather of the child born in Sri Lanka</label>
            </td>
        </tr>
        <tr>
            <td rowspan="3" style="width:75px" colspan="1"></td>
            <td>අනන්‍යතා අංකය (තිබේ නම්)
                <br>அடையாள எண் (இருக்குமாயின்)
                <br>Identification Number (if available)
            </td>
            <td colspan="5" class="find-person">
                <img src="<s:url value="/images/alphabet-V.gif" />"
                     id="grandFather_NIC_V" onclick="javascript:addXorV('grandFatherNICorPIN','V','error20')">
                <img src="<s:url value="/images/alphabet-X.gif" />"
                     id="grandFather_NIC_X" onclick="javascript:addXorV('grandFatherNICorPIN','X','error20')">
                <br>
                <s:textfield id="grandFatherNICorPIN" name="grandFather.grandFatherNICorPIN" maxLength="12"/>
                <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;"
                     id="grandFather_lookup"/>
            </td>
        </tr>
        <tr>
            <td colspan="1"><label>ඔහුගේ සම්පුර්ණ නම<br>அவரின் முழுப் பேயர் <br>His Full Name</label></td>
            <td colspan="5">
                <s:textarea name="grandFather.grandFatherFullName" id="grandFatherFullName"  cssStyle="width:97%;"
                            onblur="maxLengthCalculate('grandFatherFullName','600','grandFatherFullName_div');"/>
                <div id="grandFatherFullName_div" style="color:red;font-size:8pt"></div>
            </td>
        </tr>
        <tr>
            <td><label>ඔහුගේ උපන් වර්ෂය
                <br>அவர் பிறந்த வருடம்
                <br>His Year of Birth</label></td>
            <td><s:label value="YYYY" cssStyle="margin-left:10px"/><br>
                <s:textfield id="grandFatherBirthYear" name="grandFather.grandFatherBirthYear"
                             onclick="javascript:generateGrandFatherBirthYear('grandFatherNICorPIN','grandFatherBirthYear');"
                             maxLength="4"/>
            </td>
            <td><label>උපන් ස්ථානය <br>அவர் பிறந்த இடம் <br>Place Of Birth</label></td>
            <td colspan="3"><s:textfield name="grandFather.grandFatherBirthPlace"
                                         id="grandFatherBirthPlace"  cssStyle="width:93%;"/></td>
        </tr>
        <tr>
            <td colspan="7"><label> (<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                ළමයාගේ පියා ශ්‍රී ලංකාවේ නොඉපිද මීමුත්තා ලංකාවේ උපන්නේ නම් මී මුත්තාගේ
                <br>பிள்ளையின் தந்தை இலங்கையில் பிறக்காமல் பூட்டன் இலங்கையில் பிறந்திருந்தால் பூட்டனாரின் தகவல்கள்
                <br>If the father was not born in Sri Lanka and if great grand father born in Sri Lanka, great grand
                father's</label>
            </td>
        </tr>
        <tr>
            <td rowspan="3" colspan="1"></td>
            <td>අනන්‍යතා අංකය (තිබේ නම්)
                <br>அடையாள எண் (இருக்குமாயின்)
                <br>Identification Number (if available)
            </td>
            <td colspan="5" class="find-person">
                <img src="<s:url value="/images/alphabet-V.gif" />"
                     id="greatGrandFather_NIC_V" onclick="javascript:addXorV('greatGrandFatherNICorPIN','V','error20')">
                <img src="<s:url value="/images/alphabet-X.gif" />"
                     id="greatGrandFather_NIC_X" onclick="javascript:addXorV('greatGrandFatherNICorPIN','X','error20')">
                <br>
                <s:textfield id="greatGrandFatherNICorPIN" name="grandFather.greatGrandFatherNICorPIN" maxLength="12"/>
                <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;"
                     id="greatGrandFather_lookup"/>
            </td>
        </tr>
        <tr>
            <td colspan="1"><label>සම්පුර්ණ නම <br>முழுப் பெயர் <br>Full Name</label></td>
            <td colspan="5">
                <s:textarea name="grandFather.greatGrandFatherFullName" id="greatGrandFatherFullName"
                            cssStyle="width:97%;"
                            onblur="maxLengthCalculate('greatGrandFatherFullName','600','greatGrandFatherFullName_div');"/>
                <div id="greatGrandFatherFullName_div" style="color:red;font-size:8pt"></div>
            </td>
        </tr>
        <tr>
            <td><label>ඔහුගේ උපන් වර්ෂය
                <br>அவர் பிறந்த வருடம்
                <br>His Year of Birth</label></td>
            <td><s:label value="YYYY" cssStyle="margin-left:10px"/><br><s:textfield
                    name="grandFather.greatGrandFatherBirthYear" id="greatGrandFatherBirthYear" maxLength="4"/></td>
            <td><label>උපන් ස්ථානය <br>அவர் பிறந்த இடம் <br>Place Of Birth</label></td>
            <td><s:textfield name="grandFather.greatGrandFatherBirthPlace"
                             id="greatGrandFatherBirthPlace"  cssStyle="width:93%;"/></td>
        </tr>
        </tbody>
    </table>
</s:if>

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
        <td colspan="6" style="text-align:center;font-size:12pt">දැනුම් දෙන්නාගේ විස්තර
            <br>தகவல் தருபவரின் விபரங்கள்
            <br>Details of the Informant
        </td>
    </tr>

    <tr>
        <td colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) දැනුම් දෙන්නේ කවුරුන්
            විසින් ද? <s:label value="*" cssStyle="color:red;font-size:14pt;"/><br>யாரால் தகவல் தரப்படுகின்றது?<br>Person
            Giving
            Information</label></td>
        <td>
            <table class="sub_table">
                <tr>
                    <td><label>මව <br>தாய் <br>Mother</label></td>
                    <td align="center" width="150px"><s:radio id="informantType" name="informant.informantType"
                                                              list="#@java.util.HashMap@{'MOTHER':''}"
                                                              onchange="javascript:setInformPerson(1)"/></td>
                </tr>
            </table>
        </td>
        <td colspan="2" style="width:180px">
            <table class="sub_table">
                <tr>
                    <td><label>පියා<br> தந்தை <br>Father</label></td>
                    <td align="center" width="150px"><s:radio id="informantType" name="informant.informantType"
                                                              list="#@java.util.HashMap@{'FATHER':''}"
                                                              onchange="javascript:setInformPerson(2)"/></td>
                </tr>
            </table>
        </td>
        <td>
            <table class="sub_table">
                <tr>
                    <s:if test="birthType.ordinal() != 0">
                        <td><label>භාරකරු<br> பாதுகாவலர் <br>Guardian</label></td>
                        <td align="center" width="150px">
                            <s:radio id="informantType" name="informant.informantType"
                                     list="#@java.util.HashMap@{'GUARDIAN':''}"
                                     onchange="javascript:setInformPerson(3)"/></td>
                    </s:if>
                    <s:else>
                        <td><label>නෑයන් <br> பாதுகாவலர்<br>Relative</label></td>
                        <td align="center" width="150px">
                            <s:radio id="informantType" name="informant.informantType"
                                     list="#@java.util.HashMap@{'RELATIVE':''}"
                                     onchange="javascript:setInformPerson(3)"/></td>
                    </s:else>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification Number</label></td>
        <td colspan="6" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="informant_NIC_V" onclick="javascript:addXorV('informantNICorPIN','V','error20')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="informant_NIC_X" onclick="javascript:addXorV('informantNICorPIN','X','error20')">
            <br>
            <s:textfield name="informant.informantNICorPIN" id="informantNICorPIN" maxLength="12"/>
            <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;"
                 id="informant_lookup"/>
        </td>
    </tr>
    <tr>
        <td colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) නම <s:label value="*"
                                                                                                          cssStyle="color:red;font-size:14pt;"/><br>கொடுப்பவரின்
            பெயர்
            <br>Name</label></td>
        <td colspan="4">
            <s:textarea name="informant.informantName" id="informantName"  cssStyle="width:95%;"
                        onblur="maxLengthCalculate('informantName','600','informantName_div');"/>
            <div id="informantName_div" style="color:red;font-size:8pt"></div>
        </td>
    </tr>
    <tr>
        <td colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) තැපැල් ලිපිනය<s:label
                value="*" cssStyle="color:red;font-size:14pt;"/>
            <br>அஞ்சல் முகவரி
            <br>Postal Address</label></td>
        <td colspan="4">
            <s:textarea name="informant.informantAddress" id="informantAddress" cssStyle="width:95%;"
                        onblur="maxLengthCalculate('informantAddress','255','informantAddress_div');"/>
            <div id="informantAddress_div" style="color:red;font-size:8pt"></div>
        </td>
    </tr>
    <tr>
        <td colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) ඇමතුම් විස්තර
            <br>தொடர்பு விபரம்<br>Contact Details</label>
        </td>
        <td><label>දුරකතනය<br>தொலைபேசி இலக்கம் <br>Telephone</label></td>
        <td><s:textfield name="informant.informantPhoneNo" id="informantPhoneNo"
                         cssStyle="width:95%;" maxLength="10"/></td>
        <td><label>ඉ -තැපැල <br>மின்னஞ்சல் <br>Email</label></td>
        <td><s:textfield name="informant.informantEmail" id="informantEmail"
                         cssStyle="width:95%;text-transform:none;"/></td>
    </tr>
    <tr>
        <td colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) දිනය <s:label value="*"
                                                                                                            cssStyle="color:red;font-size:14pt;"/><br>திகதி<br>Date</label>
        </td>
        <td colspan="4">
                <s:label value="YYYY-MM-DD" cssStyle="margin-left:5px;font-size:10px"/><br>
                <s:textfield name="informant.informantSignDate" id="informDatePicker"
                             cssStyle="float:left;margin-left:5px;"
                             maxLength="10"/>
    </tr>
    </tbody>
</table>
<s:hidden name="rowNumber" value="%{row}"/>
<s:hidden name="pageNo" value="3"/>

<div class="skip-validation">
    <s:checkbox name="skipjavaScript" id="skipjs" value="false">
        <s:label value="%{getText('skipvalidation.label')}"/>
    </s:checkbox>
</div>

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

<s:hidden id="birthTypeId" value="%{birthType.ordinal()}"/>
<s:hidden id="p3error1" value="%{getText('p3.person.error.value')}"/>
<s:hidden id="p3error2" value="%{getText('p3.Informent.Name.error.value')}"/>
<s:hidden id="p3error3" value="%{getText('p3.Informent.Address.error.value')}"/>
<s:hidden id="p3error4" value="%{getText('p3.Inform.Date.error.value')}"/>
<s:hidden id="p3error5" value="%{getText('p3.Marriage.Date.value')}"/>
<s:hidden id="p3error6" value="%{getText('p3.Marriage.place.value')}"/>
<s:hidden id="p3error7" value="%{getText('p3.father.Signature')}"/>
<s:hidden id="p3error8" value="%{getText('p3.mother.Signature')}"/>
<s:hidden id="p3error9" value="%{getText('p3.father.info.captured.before')}"/>
<s:hidden id="p3error18" value="%{getText('p3.GrandFather.birthYear.mismatch')}"/>
<s:hidden id="p3error19" value="%{getText('p3.GreatGrandFather.birthYear.mismatch')}"/>
<s:hidden id="error20" value="%{getText('NIC.error.add.VX')}"/>

<s:hidden id="fatherName" value="%{parent.fatherFullName}"/>
<s:hidden id="submitDatePicker" value="%{register.dateOfRegistration}"/>
<s:hidden id="dateOfBirth" value="%{child.dateOfBirth}"/>
<s:hidden id="p3error11" value="%{getText('p1.YearofBirthOfGrandFather')}"/>
<s:hidden id="p3error10" value="%{getText('p1.YearofBirthOfGreatGrandFather')}"/>
<s:hidden id="error10" value="%{getText('p1.invalid.emailInformant.text')}"/>
<s:hidden id="error11" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="error12" value="%{getText('informantNIC.text')}"/>
<s:hidden id="error13" value="%{getText('marriageDate.text')}"/>
<s:hidden id="error14" value="%{getText('grandFatherNICorPIN.text')}"/>
<s:hidden id="error15" value="%{getText('greatGrandFatherNICorPIN.text')}"/>
<s:hidden id="error16" value="%{getText('p3.Informant.telephone.error.value')}"/>
<s:hidden id="error17" value="%{getText('p3.informdate.with.reg.date')}"/>
<s:hidden id="error18" value="%{getText('informDate.and.birthDate')}"/>
<s:hidden id="infomantDate" value="%{getText('p3.informant.date')}"/>

<%--Father information --%>
<s:hidden id="fatherNICorPINLable" value="%{parent.fatherNICorPIN}"/>
<s:hidden id="fatherFullNameLable" value="%{parent.fatherFullName}"/>
<%--mother information --%>
<s:hidden id="motherNICorPINLable" value="%{parent.motherNICorPIN}"/>
<s:hidden id="motherFullNameLable" value="%{parent.motherFullName}"/>
<s:hidden id="motherAddressLable" value="%{parent.motherAddress}"/>
<s:hidden id="motherPhoneNoLable" value="%{parent.motherPhoneNo}"/>
<s:hidden id="motherEmailLable" value="%{parent.motherEmail}"/>
<s:hidden id="maxLengthError" value="%{getText('error.max.length')}"/>

</div>

