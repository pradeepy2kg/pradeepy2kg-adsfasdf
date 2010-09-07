<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>


<s:if test="deathType.ordinal()==2 || deathType.ordinal() == 3">
    <s:set name="row" value="3"/>
</s:if>
<s:else>
    <s:set name="row" value="2"/>
</s:else>

<script type="text/javascript">
/* time picker */
(function($) {
    $.cantipi = function(el, options) {
        var base = this;
        var minutes;
        var hours;
        var ctx;
        var ampm = false;
        var size;

        base.$el = $(el);
        base.el = el;
        base.$el.data("cantipi", base);

        base.init = function() {
            base.options = $.extend({}, $.cantipi.defaultOptions, options);
            size = base.options.size;

            var canvas = document.createElement('canvas');
            canvas.height = size;
            canvas.width = size;
            // Without this canvas can't get focus and can't fire blur event
            canvas.tabIndex = 1;
            canvas.style.display = 'none';
            canvas.style.position = 'absolute';
            canvas.style.background = '#FFF';
            var offset = base.$el.offset();
            canvas.style.top = offset.top + el.offsetHeight;
            canvas.style.left = offset.left;

            canvas.onmousedown = clickclock;
            canvas.addEventListener('blur', function(e) {
                canvas.style.display = 'none'
            }, true);

            ctx = canvas.getContext('2d');

            var now = new Date();
            minutes = now.getMinutes();
            hours = now.getHours() % 12;

            el.onfocus = function() {
                canvas.style.display = 'block';
                canvas.focus();
            };

            base.$el.after(canvas);
            draw();
        };

        var clickclock = function(e) {
            point = getMouse(e);
            sethours(point);
            var hr24 = ampm ? hours : hours + 12;
            base.el.value = ('0' + hr24).substr(-2, 2) + ':' + ('0' + minutes).substr(-2, 2);
            draw();
        }

        function draw() {

            ctx.save();
            ctx.clearRect(0, 0, size, size);
            ctx.translate(size / 2, size / 2);
            ctx.scale(size / 100, size / 100);
            ctx.rotate(-Math.PI / 2);
            ctx.strokeStyle = "#20";
            ctx.fillStyle = "white";
            ctx.lineWidth = 2;
            ctx.lineCap = "round";

            ctx.beginPath();
            ctx.lineWidth = 2;
            ctx.fillStyle = '#BABA00';
            ctx.arc(0, 0, 35, 0, Math.PI * 2, true);
            ctx.fill();


            // Hour marks
            ctx.save();
            for (var i = 0; i < 12; i++) {
                ctx.beginPath();
                ctx.rotate(Math.PI / 6);
                ctx.moveTo(41, 0);
                ctx.lineTo(47, 0);
                ctx.stroke();
            }
            ctx.restore();

            // Minute marks
            ctx.save();
            ctx.lineWidth = 1;
            for (i = 0; i < 60; i++) {
                if (i % 5 != 0) {
                    ctx.beginPath();
                    ctx.moveTo(47, 0);
                    ctx.lineTo(44, 0);
                    ctx.stroke();
                }
                ctx.rotate(Math.PI / 30);
            }
            ctx.restore();

            // write Hours
            ctx.save();
            ctx.rotate(hours * (Math.PI / 6) + (Math.PI / 360) * minutes)
            ctx.lineWidth = 2;
            ctx.beginPath();
            ctx.moveTo(-12, 0);
            ctx.lineTo(38, 0);
            ctx.stroke();
            ctx.beginPath();

            ctx.beginPath();
            ctx.arc(28, 0, 4, 0, Math.PI * 2, true);
            ctx.stroke();

            ctx.restore();

            // write Minutes
            ctx.save();
            ctx.rotate((Math.PI / 30) * minutes)
            ctx.lineWidth = 1;
            ctx.beginPath();
            ctx.moveTo(-14, 0);
            ctx.lineTo(44, 0);
            ctx.stroke();
            ctx.beginPath();
            ctx.arc(34, 0, 5, 0, Math.PI * 2, true);
            ctx.stroke();
            ctx.restore();

            ctx.beginPath();
            ctx.lineWidth = 1;
            ctx.strokeStyle = '#BABA00';
            ctx.arc(0, 0, 49, 0, Math.PI * 2, true);
            ctx.stroke();

            ctx.save();
            ctx.beginPath();
            ctx.fillStyle = "white";
            ctx.arc(0, 0, 10, 0, Math.PI * 2, true);
            ctx.fill();
            ctx.rotate(Math.PI / 2);
            var i = ampm ? 'AM' : 'PM';
            ctx.font = 'normal 900 9px Lucida Grande';
            ctx.textAlign = 'center';
            ctx.textBaseline = 'middle';
            ctx.fillStyle = "black";
            ctx.fillText(i, 0, 0);
            ctx.restore();

            ctx.restore();

        }

        function getTrueOffsetLeft(ele)
        {
            var n = 0;
            while (ele)
            {
                n += ele.offsetLeft || 0;
                ele = ele.offsetParent;
            }
            var domobject = document.getElementById('bdfSerialNo');
            if (isFieldEmpty(domobject)) {
                domobject.value = new Date().getFullYear() + "0";
            }
            return n;
        }

        function getTrueOffsetTop(ele)
        {
            var n = 0;
            while (ele)
            {
                n += ele.offsetTop || 0;
                ele = ele.offsetParent;
            }
            return n;
        }


        function getMouse(e) {
            var x = e.clientX - getTrueOffsetLeft(e.target) + window.pageXOffset - size / 2;
            var y = e.clientY - getTrueOffsetTop(e.target) + window.pageYOffset - size / 2;
            return { x:x, y:y };
        }

        ;

        function sethours(point) {
            var tumbler = ctx.isPointInPath(point.x + size / 2, point.y + size / 2);
            ampm = tumbler ? !ampm : ampm;
            if (tumbler) return;

            var angle = Math.atan2(point.y, point.x) + Math.PI * 2;
            var distance = Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2));
            if (distance < size * 35 / 100) {
                hours = (Math.floor(angle / (Math.PI / 6)) + 3) % 12;
            } else {
                minutes = Math.floor((Math.floor((angle / (Math.PI / 30)) + 15) % 60) / base.options.roundto) * base.options.roundto;
            }
        }

        base.init();
    };

    $.cantipi.defaultOptions = {
        size:150,
        roundto: 1
    };

    $.fn.cantipi = function(options) {
        return this.each(function() {
            var clock = new $.cantipi(this, options);
        });
    };

})(jQuery);


$(function() {
    $("#timePicker").cantipi({size:140, roundto: 5});
});
<%--end of time picker--%>

$(function() {
    $("#deathDatePicker").datepicker({
        changeYear: true,
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2020-12-31'
    });
});

$(function() {
    $("#dateOfRegistrationDatePicker").datepicker({
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
    $('select#deathDistrictId').bind('change', function(evt1) {
        var id = $("select#deathDistrictId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id},
                function(data) {
                    var options1 = '';
                    var ds = data.dsDivisionList;
                    for (var i = 0; i < ds.length; i++) {
                        options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                    }
                    $("select#deathDsDivisionId").html(options1);

                    var options2 = '';
                    var bd = data.bdDivisionList;
                    for (var j = 0; j < bd.length; j++) {
                        options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                    }
                    $("select#deathDivisionId").html(options2);
                });
    });

    $('select#deathDsDivisionId').bind('change', function(evt2) {
        var id = $("select#deathDsDivisionId").attr("value");
        $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:2},
                function(data) {
                    var options = '';
                    var bd = data.bdDivisionList;
                    for (var i = 0; i < bd.length; i++) {
                        options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                    }
                    $("select#deathDivisionId").html(options);
                });
    });

    $('img#death_person_lookup').bind('click', function(evt3) {
        var id1 = $("input#deathPerson_PINorNIC").attr("value");
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                function(data1) {
                    $("textarea#deathPersonNameOfficialLang").val(data1.fullNameInOfficialLanguage);
                    //$("textarea#deathPersonNameInEnglish").val(data1.fullNameInOfficialLanguage);
                    $("textarea#deathPersonPermanentAddress").val(data2.lastAddress);
                });
    });
    $('img#death_person_father_lookup').bind('click', function(evt4) {
        var id2 = $("input#deathPersonFather_PINorNIC").attr("value");
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id2},
                function(data2) {
                    $("textarea#deathPersonFatherFullName").val(data2.fullNameInOfficialLanguage);
                });
    });
    $('img#death_person_mother_lookup').bind('click', function(evt5) {
        var id3 = $("input#deathPersonMother_PINorNIC").attr("value");
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id3},
                function(data3) {
                    $("textarea#deathPersonMotherFullName").val(data3.fullNameInOfficialLanguage);
                });
    });


});
var errormsg = "";
function validate() {
    var domObject;
    var returnval;
    var check = document.getElementById('skipjs');

    // validations that can skip
    if (!check.checked) {
        domObject = document.getElementById('deathPerson_PINorNIC');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error8').value;
        }
        domObject = document.getElementById('deathPersonNameOfficialLang');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error9').value;
        }
        domObject = document.getElementById('deathPersonNameInEnglish');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error10').value;
        }
        domObject = document.getElementById('deathPersonPermanentAddress');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('error11').value;
        }

    }


    /*date related validations*/
    domObject = document.getElementById('dateOfRegistrationDatePicker');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error1').value;
    } else {
        isDate(domObject.value, 'p1error1', 'p1errordate1');
    }

    domObject = document.getElementById('deathDatePicker');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error2').value;
    } else {
        isDate(domObject.value, 'p1error1', 'p1errordate2');
    }

    //validate death person NIC/PIN
    domObject = document.getElementById('deathPerson_PINorNIC');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'p1error1', 'p1errorPIN1');
    //validate Father NIC/PIN
    domObject = document.getElementById('deathPersonFather_PINorNIC');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'p1error1', 'p1errorPIN2');
    //validate mother NIC/PIN
    domObject = document.getElementById('deathPersonMother_PINorNIC');
    if (!isFieldEmpty(domObject))
        validatePINorNIC(domObject, 'p1error1', 'p1errorPIN3');

    //validation for emptry fields
    domObject = document.getElementById('deathSerialNo');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error0').value;
    } else {
        validateSerialNo(domObject, 'p1error1', 'p1errorSerial');

    }
    domObject = document.getElementById('placeOfDeath');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error3').value;
    }
    domObject = document.getElementById('placeOfBurial');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error4').value;
    }
    domObject = document.getElementById('deathPersonGender');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error5').value;
    }

    var dateOfReg = new Date(document.getElementById("dateOfRegistrationDatePicker").value);
    var dateOfDeath = new Date(document.getElementById("deathDatePicker").value);
    if (dateOfReg < dateOfDeath) {
        errormsg = errormsg + "\n" + document.getElementById("error7").value;
    }

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }

    errormsg = "";
    return returnval;
}

$('img#place').bind('click', function(evt6) {
    var id = $("input#placeOfDeath").attr("value");
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
    $("input#placeOfDeathInEnglish").val(respObj.Body[0].transliterateResponse[0].
    return[0].Text
)
    ;
}
;

$('img#deathName').bind('click', function(evt7) {
    var id = $("textarea#deathPersonNameOfficialLang").attr("value");
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
    $("textarea#deathPersonNameInEnglish").val(respObj.Body[0].transliterateResponse[0].
    return[0].Text
)
    ;
}

function initSerialNumber() {
    var domObject = document.getElementById('deathSerialNo');
    if (isFieldEmpty(domObject)) {
        domObject.value = new Date().getFullYear() + "0";
    }
}

function initPage() {
    initSerialNumber();
}
</script>


<div id="death-declaration-form-1-outer">
<s:form name="deathRegistrationForm1" id="death-registration-form-1" action="eprDeathDeclaration.do" method="POST"
        onsubmit="javascript:return validate()">
<table>
    <col style="width:60%"/>
    <col style="width:40%"/>
    <tr>
        <td></td>
        <td>
            <s:fielderror name="duplicateSerialNumberError" cssStyle="color:red;font-size:10pt"/>
        </td>
    </tr>
</table>
<table style="width: 100%; border:none; border-collapse:collapse;" class="font-9">
    <col width="180px"/>
    <col width="350px"/>
    <col width="120px"/>
    <col width="160px"/>
    <tbody>

    <tr>
        <td rowspan="3"></td>
        <td rowspan="2" align="center">
            <img src="<s:url value="/images/official-logo.png" />" style="display: block; text-align: center;"
                 width="80" height="100">
        </td>

        <td style="border:1px solid #000;">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</td>
        <td style="border:1px solid #000;">
            <s:if test="editMode">
                <s:textfield name="register.bdfSerialNo" id="deathSerialNo" readonly="true"/>
            </s:if>
            <s:else>
                <s:textfield name="register.bdfSerialNo" id="deathSerialNo"/>
            </s:else>
        </td>
    </tr>
    <tr>
        <td colspan="2" style="border:1px solid #000;text-align:center;">කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>அலுவலக
            பாவனைக்காக மட்டும்
            <br>For office use only
        </td>
    </tr>
    <tr>
        <td align="center" class="font-12">
            <s:if test="deathType.ordinal() == 0 || deathType.ordinal() == 1">
                ප්‍රකාශයක් [30, 39(1), 41(1) (උ) වගන්ති] - සාමාන්‍ය මරණ හා හදිසි මරණ <br/>
                ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள் <br/>
                Declaration of Death [Sections 30, 39(1) and 41(1)(e)] – Normal Death or Sudden Death
            </s:if>
            <s:elseif test="deathType.ordinal() == 2 || deathType.ordinal() == 3">
                මරණ ප්‍රකාශයක් [36වෙනි වගන්තිය] - කාලය ඉකුත් වූ මරණ ලියාපදිංචි කිරීම හෝ නැතිවුණු පුද්ගලයෙකුගේ මරණ <br/>
                ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள் <br/>
                Declaration of Death [Section 36] – Late registration or Death of missing person
            </s:elseif>
        </td>
        <td style="border:1px solid #000;">ලියාපදිංචි කල දිනය<br>பிறப்பைப் பதிவு திகதி <br>Date of Registration
        </td>
        <td style="border:1px solid #000;">
            <s:label value="YYYY-MM-DD" cssStyle="font-size:10px"/><br>
            <s:textfield id="dateOfRegistrationDatePicker" name="death.dateOfRegistration"/>
        </td>
    </tr>
    <tr>
        <td colspan="4" height="15px"></td>
    </tr>
    <tr>
        <td colspan="4" class="font-9" style="text-align:justify;">
            <s:if test="deathType.ordinal() == 0 || deathType.ordinal() == 1">
                ප්‍රකාශකයා විසින් මරණය සිදු වූ කොට්ටාශයේ මරණ රෙජිස්ට්‍රාර් තැන වෙත ලබා දිය යුතුය. මෙම තොරතුරු මත
                සිවිල්
                ලියාපදිංචි කිරිමේ පද්ධතියේ මරණය ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம்
                சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the declarant and the duly completed form should be forwarded to the
                Registrar of Deaths of the division where the death has occurred. The death will be registered in
                the
                Civil Registration System based on the information provided in this form.
            </s:if>
            <s:elseif test="deathType.ordinal() == 2 || deathType.ordinal() == 3">
                ලියාපදිංචි නොකරන ලද මරණයක් සම්බන්ධයෙන් මෙහි පහත ප්‍රකාශ කරනු ලබන විස්තර මගේ දැනීමේ හා විශ්වාසයේ ප
                ්‍රකාර සැබෑ බව හා නිවැරදි බවද, මරණය සිදුවී, නැතහොත් ගෘහයක් හෝ ගොඩනැගිල්ලක් නොවන ස්ථානයක තිබී මෘතශරීරය
                සම්බවී,
                මාස තුඅනක් ඇතුලත දී මරණය ලියාපදිංචි කිරීමට නොහැකි වුයේ මෙහි පහත සඳහන් කාරණය හේතු කොටගෙන බවද
                , ..... පදිංචි .... වන මම ගාම්භීරතා පුර්වකාවද, අවංක ලෙසද, සැබෑ ලෙසද, මෙයින් ප්‍රකාශ කරමි. <br/>

                in tamil line 1
                in tamil line 2
                in tamil line 3 <br/>

                I …. of …. solemnly, sincerely, and truly declare that the particulars stated below relating to an
                unregistered death, are true and correct to the best of my knowledge and belief, and that the death has not
                been registered within three months from its occurrence or from the finding of the corpse in a place other
                than a house or a building, for this reason.
            </s:elseif>
        </td>
    </tr>
    </tbody>
</table>
<s:if test="deathType.ordinal() == 2 || deathType.ordinal() == 3">
    <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;" class="font-9">
        <tr>
            <td width="150px">(1)මරණය ලියාපදිංචි කිරීම ප්‍රමාද වීමට කාරණය <br/>
                in tamil <br/>
                Reason for the late registration of the death
            </td>
            <td><s:textarea name="death.reasonForLateRegistration" cssStyle="width:880px;"/></td>
        </tr>
    </table>
</s:if>
<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;" class="font-9">
    <col width="150px"/>
    <col width="120px"/>
    <col width="120px"/>
    <col width="90px"/>
    <col width="120px"/>
    <col width="90px"/>
    <col width="120px"/>
    <col width="120px"/>
    <col/>
    <tbody>

    <tr>
        <td colspan="9" class="form-sub-title">
            මරණය පිලිබඳ විස්තර
            <br>பிள்ளை பற்றிய தகவல்
            <br>Information about the Death
        </td>
    </tr>
    <s:if test="deathType.ordinal() == 0 || deathType.ordinal() == 1">
        <tr>
            <td>
                (1)හදිසි මරණයක්ද ? <br/>
                in tamil <br/>
                Sudden death?
            </td>
            <td colspan="3">ඔව් xx Yes</td>
            <td align="center"><s:radio name="deathType" list="#@java.util.HashMap@{'SUDDEN':''}" value="false"/></td>
            <td colspan="3">නැත xx No</td>
            <td align="center"><s:radio name="deathType" list="#@java.util.HashMap@{'NORMAL':''}" value="false"/></td>
        </tr>
    </s:if>
    <s:elseif test="deathType.ordinal() == 2 || deathType.ordinal() == 3">
        <tr>
            <td>
                (2)නැතිවුණු පුද්ගලයෙකුගේ මරණයක්ද ? <br/>
                in tamil <br/>
                Is the death of a missing person?
            </td>
            <td colspan="3">ඔව් xx Yes</td>
            <td><s:radio name="deathType" list="#@java.util.HashMap@{'MISSING':''}" value="false"/></td>
            <td colspan="3">නැත xx No</td>
            <td><s:radio name="deathType" list="#@java.util.HashMap@{'LATE':''}" value="false"/></td>
        </tr>
    </s:elseif>
    <tr>
        <td>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)මරණය සිදුවූ දිනය<br>பிறந்த திகதி<br>Date of
            death
        </td>
        <td colspan="4">
            <s:label value="YYYY-MM-DD" cssStyle="font-size:10px"/><br>
            <s:textfield id="deathDatePicker" name="death.dateOfDeath"/>
        </td>
        <td>වෙලාව<br>*in tamil<br>Time</td>
        <td colspan="3">
            <s:textfield name="death.timeOfDeath" id="timePicker"/>
        </td>
    </tr>
    <tr>
        <td rowspan="5">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)මරණය සිදු වූ ස්ථානය<br>பிறந்த இடம்<br>Place
            of Death
        </td>
        <td colspan="3">දිස්ත්‍රික්කය / மாவட்டம் / District</td>
        <td colspan="5"><s:select id="deathDistrictId" name="deathDistrictId" list="districtList"/></td>
    </tr>
    <tr>
        <td colspan="3">ප්‍රාදේශීය ලේකම් කොට්ඨාශය / <br>பிரிவு / <br>Divisional Secretariat</td>
        <td colspan="5"><s:select id="deathDsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  cssStyle="float:left; "/></td>
    </tr>
    <tr>
        <td colspan="3">ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br>பிரிவு / <br>Registration Division</td>
        <td colspan="5"><s:select id="deathDivisionId" name="deathDivisionId" list="bdDivisionList"
                                  cssStyle="float:left;"/></td>
    </tr>
    <tr>
        <td rowspan="2" colspan="1">ස්ථානය <br>பிறந்த <br>Place</td>
        <td colspan="2">සිංහල හෝ දෙමළ භාෂාවෙන්<br>சிங்களம் தமிழ்<br>In Sinhala or Tamil</td>
        <td colspan="5"><s:textfield name="death.placeOfDeath" cssStyle="width:555px;" id="placeOfDeath"/></td>
    </tr>
    <tr>
        <td colspan="2">ඉංග්‍රීසි භාෂාවෙන්<br>*in tamil<br>In English</td>
        <td colspan="5">
            <s:textfield name="death.placeOfDeathInEnglish" id="placeOfDeathInEnglish" cssStyle="width:555px;"/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px 0;"
                 id="place">
        </td>
    </tr>
    <tr>
        <td rowspan="2" colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)මරණයට හේතුව
            තහවුරුද?<br>*in tamil<br>Is the cause of death established?
        </td>
        <td colspan="1">නැත / xx / No</td>
        <td colspan="2" align="center"><s:radio name="death.causeOfDeathEstablished"
                                                list="#@java.util.HashMap@{'false':''}"
                                                id=""/></td>
        <td rowspan="2" colspan="3">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)මරණය දින 30 කට අඩු
            ළදරුවෙකුගේද?<br>*in tamil<br>Is the death of an infant
            less
            than 30 days?
        </td>
        <td colspan="1">නැත / xx / No</td>
        <td colspan="1" align="center"><s:radio name="death.infantLessThan30Days"
                                                list="#@java.util.HashMap@{'false':''}"/></td>
    </tr>
    <tr>
        <td colspan="1">ඔව් / xx /Yes</td>
        <td colspan="2" align="center"><s:radio name="death.causeOfDeathEstablished"
                                                list="#@java.util.HashMap@{'true':''}"/></td>
        <td colspan="1">ඔව් / xx /Yes</td>
        <td colspan="1" align="center"><s:radio name="death.infantLessThan30Days"
                                                list="#@java.util.HashMap@{'true':''}"/></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)මරණයට හේතුව<br>*in tamil<br>Cause
            of death
        </td>
        <td colspan="4"><s:textarea name="death.causeOfDeath" cssStyle="width:420px; "/></td>
        <td colspan="2">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)හේතුවේ ICD කේත අංකය<br>*in
            tamil<br>ICD Code of cause
        </td>
        <td colspan="2"><s:textfield name="death.icdCodeOfCause" cssStyle="width:225px;"/></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)ආදාහන හෝ භූමදාන ස්ථානය<br>*in
            tamil<br>Place of burial or cremation
        </td>
        <td colspan="8"><s:textarea name="death.placeOfBurial" id="placeOfBurial" cssStyle="width:880px;"/></td>
    </tr>
    <s:if test="deathType.ordinal() == 2 || deathType.ordinal() == 3">
        <tr>
            <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)වෙනත් තොරතුරු <br/>
                in tamil <br/>
                Any other information
            </td>
            <td colspan="8"><s:textarea name="death.anyOtherInformation" id="anyOtherInfo"
                                        cssStyle="width:880px;"/></td>
        </tr>
    </s:if>
    <tr>
        <td colspan="2"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            මරණ
            සහතිකය නිකුත් කල යුතු භාෂාව <br>*in tamil<br>Preferred
            Language for
            Death Certificate </label></td>
        <td colspan="7"><s:select list="#@java.util.HashMap@{'si':'සිංහල','ta':'Tamil'}"
                                  name="death.preferredLanguage"
                                  cssStyle="width:190px; margin-left:5px;"></s:select></td>
    </tr>
    </tbody>
</table>

<table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin-bottom:0;" class="font-9">
    <col width="220px"/>
    <col width="100px"/>
    <col width="100px"/>
    <col width="100px"/>
    <col width="150px"/>
    <col width="130px"/>
    <col/>
    <tbody>
    <tr class="form-sub-title">
        <td colspan="7">මරණයට පත් වූ පුද්ගලයාගේ විස්තර<br>பிள்ளை பற்றிய தகவல்<br>Information about the person
            Departed
        </td>
    </tr>
    <tr>
        <td rowspan="2" colspan="2">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)පුද්ගල අනන්‍යතා අංකය /
            ජාතික
            හැදුනුම්පත් අංකය<br>தனிநபர் அடையாள எண் / அடையாள அட்டை இல.
            <br>PIN / NIC
        </td>
        <td rowspan="2" colspan="2" class="find-person"><s:textfield name="deathPerson.deathPersonPINorNIC"
                                                                     id="deathPerson_PINorNIC"
                                                                     cssStyle="float:left;"/>
            <img src="<s:url value="/images/search-father.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="death_person_lookup"></td>
        <td rowspan="2">විදේශිකය‍කු නම්<br>வெளிநாட்டவர் <br>If a foreigner</td>
        <td>රට<br>நாடு<br>Country</td>
        <td><s:select id="deathPersonCountryId" name="deathPersonCountry" list="countryList" headerKey="0"
                      headerValue="%{getText('select_country.label')}" cssStyle="width:185px;"/></td>
    </tr>
    <tr>
        <td>ගමන් බලපත්‍ර අංකය<br>கடவுச் சீட்டு<br>Passport No.</td>
        <td><s:textfield name="deathPerson.deathPersonPassportNo" cssStyle="width:180px;"/></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)වයස හෝ අනුමාන වයස<br>பிறப்ப<br>Age
            or probable Age
        </td>
        <td colspan="1"><s:textfield name="deathPerson.deathPersonAge" id="deathPersonAge"/></td>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)ස්ත්‍රී පුරුෂ භාවය<br>பால் <br>Gender
        </td>
        <td colspan="1"><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="deathPerson.deathPersonGender" headerKey="0" headerValue="%{getText('select_gender.label')}"
                id="deathPersonGender" cssStyle="width:190px; margin-left:5px;"/></td>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)ජාතිය<br>பிறப்<br>Race</td>
        <td colspan="2">
            <s:select list="raceList" name="deathPersonRace" headerKey="0" headerValue="%{getText('select_race.label')}"
                      cssStyle="width:300px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)නම රාජ්‍ය භාෂාවෙන් (සිංහල /
            දෙමළ)<br>பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்)<br>Name
            in either of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="6"><s:textarea name="deathPerson.deathPersonNameOfficialLang" id="deathPersonNameOfficialLang"
                                    cssStyle="width:880px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)නම ඉංග්‍රීසි භාෂාවෙන්<br>பிறப்பு
            அத்தாட்சி …..<br>Name in English
        </td>
        <td colspan="6">
            <s:textarea name="deathPerson.deathPersonNameInEnglish" id="deathPersonNameInEnglish"
                        cssStyle="width:880px;"/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px 0;"
                 id="deathName">
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)ස්ථිර ලිපිනය<br>தாயின் நிரந்தர
            வதிவிட முகவரி<br>Permanent Address
        </td>
        <td colspan="6">
            <s:textarea name="deathPerson.deathPersonPermanentAddress" id="deathPersonPermanentAddress"
                        cssStyle="width:880px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)පියාගේ පු.අ.අ. / ජා.හැ.අ.<br>*in
            tamil<br>Fathers PIN / NIC
        </td>
        <td colspan="6" class="find-person">
            <s:textfield name="deathPerson.deathPersonFatherPINorNIC" id="deathPersonFather_PINorNIC"
                         cssStyle="float:left;"/>
            <img src="<s:url value="/images/search-father.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="death_person_father_lookup"></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)පියාගේ සම්පුර්ණ නම<br>*in tamil
            <br>Fathers full name
        </td>
        <td colspan="6">
            <s:textarea name="deathPerson.deathPersonFatherFullName" id="deathPersonFatherFullName"
                        cssStyle="width:880px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)මවගේ පු.අ.අ. / ජා.හැ.අ.<br>*in
            tamil<br>Mothers PIN / NIC
        </td>
        <td colspan="6" class="find-person">
            <s:textfield name="deathPerson.deathPersonMotherPINorNIC" id="deathPersonMother_PINorNIC"
                         cssStyle="float:left;"/>
            <img src="<s:url value="/images/search-mother.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="death_person_mother_lookup"></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)මවගේ සම්පුර්ණ නම<br>*in tamil <br>Mothers
            full name
        </td>
        <td colspan="6">
            <s:textarea name="deathPerson.deathPersonMotherFullName" id="deathPersonMotherFullName"
                        cssStyle="width:880px;"/>
        </td>
    </tr>
    </tbody>
</table>
<s:hidden id="error0" value="%{getText('p1.errorlable.serialNumber')}"/>
<s:hidden id="error1" value="%{getText('p1.errorlable.dateofReg')}"/>
<s:hidden id="error2" value="%{getText('p1.errorlable.dateofDeath')}"/>
<s:hidden id="error3" value="%{getText('p1.errorlable.placeofDeath')}"/>
<s:hidden id="error4" value="%{getText('p1.errorlable.placeofBurial')}"/>
<s:hidden id="error5" value="%{getText('p1.errorlable.gerder')}"/>
<s:hidden id="error6" value="%{getText('p1.errorlable.serialNumberIsNum')}"/>
<s:hidden id="error7" value="%{getText('p1.errorlable.dateofRegAndDateofDeath')}"/>
<s:hidden id="error8" value="%{getText('p1.errorlable.deathPerson.PINorNIC')}"/>
<s:hidden id="error9" value="%{getText('p1.errorlable.deathPerson.NameOfficialLang')}"/>
<s:hidden id="error10" value="%{getText('p1.errorlable.deathPerson.NameInEnglish')}"/>
<s:hidden id="error11" value="%{getText('p1.errorlable.deathPerson.PermanentAddress')}"/>

<s:hidden id="p1error1" value="%{getText('p1.invalide.inputType')}"/>
<s:hidden id="p1errorPIN1" value="%{getText('p1.deathPerson_PIN')}"/>
<s:hidden id="p1errorPIN2" value="%{getText('p1.father_PIN')}"/>
<s:hidden id="p1errorPIN3" value="%{getText('p1.mother_PIN')}"/>
<s:hidden id="p1errorAge" value="%{getText('p1.deathAge')}"/>
<s:hidden id="p1errordate1" value="%{getText('p1.dateOfRegistrationDate')}"/>
<s:hidden id="p1errordate2" value="%{getText('p1.deathDate')}"/>
<s:hidden id="p1errorSerial" value="%{getText('p1.serialNumber.format')}"/>


<div class="skip-validation">
    <s:checkbox name="skipjavaScript" id="skipjs" value="false">
        <s:label value="%{getText('skipvalidation.label')}"/>
    </s:checkbox>
</div>
<div class="form-submit">
    <s:hidden name="pageNo" value="1"/>
    <s:hidden name="rowNumber" value="%{row}"/>
    <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
</div>
</s:form>
</div>