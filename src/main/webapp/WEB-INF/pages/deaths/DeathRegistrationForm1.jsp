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
    var declarationType = document.getElementById('deathTypeId');

    //validation for emptry fields
    domObject = document.getElementById('deathSerialNo');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error0').value;
    } else {
        validateSerialNo(domObject, 'p1error1', 'p1errorSerial');

    }

    /*date related validations*/
    domObject = document.getElementById('dateOfRegistrationDatePicker');
    if (isFieldEmpty(domObject)) {
        errormsg = errormsg + "\n" + document.getElementById('error1').value;
    } else {
        isDate(domObject.value, 'p1error1', 'p1errordate1');
    }

    if (declarationType.value == 2) {
        domObject = document.getElementById('resonForLateRegistration');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, '', 'error13');
        }
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

    if (errormsg != "") {
        alert(errormsg);
        returnval = false;
    }

    errormsg = "";
    return returnval;
}

$(function() {
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

    function processResponse2(respObj) {
        //respObj is a JSON equivalent of SOAP Response XML (all namespaces are dropped)
        $("input#placeOfDeathInEnglish").val(respObj.Body[0].transliterateResponse[0].
        return[0].Text
    )
        ;
    }

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
});

function initSerialNumber() {
    var domObject = document.getElementById('deathSerialNo');
    if (domObject.value.trim() == 0) {
        domObject.value = null;
    }
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

<table class="table_reg_header_01" style="font-size:9pt">
    <caption></caption>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td width="300px"></td>
        <td align="center" style="font-size:12pt; width:430px"><img src="<s:url value="/images/official-logo.png"/>"
                                                                    alt=""/><br>
            <s:if test="deathType.ordinal() == 0 || deathType.ordinal() == 1">
                මරණ ප්‍රකාශයක් [30, 39(1), 41(1) (උ) වගන්ති] - සාමාන්‍ය මරණ හා හදිසි මරණ
                <br>இறப்பு பிரதிக்கினை [30, 39(1), 41(1) (உ) பிரிவு ]- சாதாரண மரணம் மற்றும் திடீா் மரணம்
                <br>Declaration of Death [Sections 30, 39(1) and 41(1)(e)] – Normal Death or Sudden Death
            </s:if>
            <s:elseif test="deathType.ordinal() == 2 || deathType.ordinal() == 3">
                මරණ ප්‍රකාශයක් [36වෙනි වගන්තිය] - කාලය ඉකුත් වූ මරණ ලියාපදිංචි කිරීම හෝ නැතිවුණු පුද්ගලයෙකුගේ මරණ
                <br>மரண பிரதிக்கினை [36வது பிரிவு ] - காலந் தாழ்த்திய இறப்பினை பதிவு செய்தல் அல்லது காணாமற் போன நபரின் மரணம்
                <br>Declaration of Death [Section 36] – Late registration or Death of missing person
            </s:elseif>
        </td>
        <td>
            <table class="table_reg_datePicker_page_01">
                <tr>
                    <s:fielderror name="duplicateSerialNumberError" cssStyle="color:red;font-size:9pt;"/>
                </tr>
                <tr>
                    <td><label><span class="font-8">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</span></label>
                    </td>
                    <td>
                        <s:if test="editMode">
                            <s:textfield name="death.deathSerialNo" id="deathSerialNo" readonly="true"/>
                        </s:if>
                        <s:else>
                            <s:textfield name="death.deathSerialNo" id="deathSerialNo" maxLength="10"/>
                        </s:else>
                    </td>
                </tr>
            </table>

            <table class="table_reg_datePicker_page_01">
                <tr>
                    <td colspan="2">කාර්යාල ප්‍රයෝජනය සඳහා පමණි <br>அலுவலக பாவனைக்காக மட்டும்
                        <br>For office use only
                        <hr>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label><span
                                class="font-8">භාරගත්  දිනය<br>பிறப்பைப் பதிவு திகதி <br>Submitted Date</span></label>
                    </td>
                    <td><s:label value="YYYY-MM-DD" cssStyle="margin-left:20px;font-size:10px"/><br>
                        <s:textfield name="death.dateOfRegistration" id="dateOfRegistrationDatePicker" maxLength="10"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <s:if test="deathType.ordinal() == 0 || deathType.ordinal() == 1">
                ප්‍රකාශකයා විසින් මරණය සිදු වූ කොට්ටාශයේ මරණ රෙජිස්ට්‍රාර් තැන වෙත ලබා දිය යුතුය. මෙම තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ මරණය ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் இறப்பு நிகழ்ந்த பிரிவின் இறப்பு பதிவாளாரிடம் சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the declarant and the duly completed form should be forwarded to the Registrar of Deaths of the division where the death has occurred. The death will be registered in the Civil Registration System based on the information provided in this form.
            </s:if>
            <s:elseif test="deathType.ordinal() == 2 || deathType.ordinal() == 3 || deathType.ordinal() == 4">
                ලියාපදිංචි නොකරන ලද මරණයක් සම්බන්ධයෙන් මෙහි පහත ප්‍රකාශ කරනු ලබන විස්තර මගේ දැනීමේ හා විශ්වාසයේ ප්‍රකාර සැබෑ බව හා නිවැරදි බවද, මරණය සිදුවී, නැතහොත් ගෘහයක් හෝ ගොඩනැගිල්ලක් නොවන ස්ථානයක තිබී මෘතශරීරය සම්බවී, මාස තුඅනක් ඇතුලත දී මරණය ලියාපදිංචි කිරීමට නොහැකි වුයේ මෙහි පහත සඳහන් කාරණය හේතු කොටගෙන බවද, ..... පදිංචි .... වන මම ගාම්භීරතා පුර්වකාවද, අවංක ලෙසද, සැබෑ ලෙසද, මෙයින් ප්‍රකාශ කරමි.
                <br>பதியப்படாத மரணம் சம்பந்தமாக இங்கு கீழ் பிரதிக்கினை செய்யப்படும் விபரங்கள் எனது அறிவிக்கும் நம்பிக்கைக்கும் உரியவகையில் உண்மையானதும் சரியானதும் எனவும் இறப்பு நிகழ்ந்து அல்லது வீடு அல்லது கட்டிடம் அல்லாத இடத்திலிருந்து அப்பிரேதத்தைக் கண்டு மூன்று மாதங்களுக்குள் இறப்பினை பதிவதற்கு இயலாது போனது கீழ் குறிப்பிடப்படும் காரணத்தினால் ஆகும் என ….......................................................................வதியும் ….........................................................ஆகிய நான் நோ்மையாகவும் உண்மையாகவும் பயபக்தியுடனும் பிரதிக்கினை செய்கின்றேன்.
                <br>I …. of …. solemnly, sincerely, and truly declare that the particulars stated below relating to an unregistered death, are true and correct to the best of my knowledge and belief, and that the death has not been registered within three months from its occurrence or from the finding of the corpse in a place other than a house or a building, for this reason.
            </s:elseif>
        </td>
    </tr>
    </tbody>
</table>

<s:if test="deathType.ordinal() == 2 || deathType.ordinal() == 3">
    <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;" class="font-9">
        <tr>
            <td width="150px">(1)මරණය ලියාපදිංචි කිරීම ප්‍රමාද වීමට කාරණය
                <br>இறப்பினை பதிவதற்கு தாமதித்ததற்கான காரணம்
                <br>Reason for the late registration of the death
            </td>
            <td><s:textarea id="resonForLateRegistration" name="death.reasonForLateRegistration"
                            cssStyle="width:880px;"/></td>
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
            <br>இறப்பு பற்றிய தகவல்
            <br>Information about the Death
        </td>
    </tr>
    <s:if test="deathType.ordinal() == 0 || deathType.ordinal() == 1">
        <tr>
            <td>
                (1)හදිසි මරණයක්ද ?
                <br>திடீா் மரணமா?
                <br>Sudden death?
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
                (2)නැතිවුණු පුද්ගලයෙකුගේ මරණයක්ද ?
                <br>காணாமற்போன நபரது மரணமா?
                <br>Is the death of a missing person?
            </td>
            <td colspan="3">
                ඔව්
                <br>ஆம்
                <br>Yes
            </td>
            <td><s:radio name="deathType" list="#@java.util.HashMap@{'MISSING':''}" value="false"/></td>
            <td colspan="3">
                නැත
                <br>இல்லை
                <br>No
            </td>
            <td><s:radio name="deathType" list="#@java.util.HashMap@{'LATE_NORMAL':''}" value="false"/></td>
        </tr>
    </s:elseif>
    <tr>
        <td>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            මරණය සිදු වූ දිනය
            <br>இறந்த திகதி
            <br>Date of Death
        </td>
        <td colspan="4">
            <s:label value="YYYY-MM-DD" cssStyle="font-size:10px"/><br>
            <s:textfield id="deathDatePicker" name="death.dateOfDeath"/>
        </td>
        <td>
            වෙලාව
            <br>நேரம்
            <br>Time
        </td>
        <td colspan="3">
            <s:textfield name="death.timeOfDeath" id="timePicker"/>
        </td>
    </tr>
    <tr>
        <td rowspan="5">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            මරණය සිදු වූ ස්ථානය
            <br>இறப்பு நிகழந்த இடம்
            <br>Place of Death
        </td>
        <td colspan="3">දිස්ත්‍රික්කය / மாவட்டம் / District</td>
        <td colspan="5"><s:select id="deathDistrictId" name="deathDistrictId" list="districtList"/></td>
    </tr>
    <tr>
        <td colspan="3">
            ප්‍රාදේශීය ලේකම් කොට්ඨාශය /
            <br>பிரதேச செயளாளா் பிரிவு /
            <br>Divisional Secretariat
        </td>
        <td colspan="5"><s:select id="deathDsDivisionId" name="dsDivisionId" list="dsDivisionList"
                                  cssStyle="float:left; "/></td>
    </tr>
    <tr>
        <td colspan="3">
            ලියාපදිංචි කිරීමේ කොට්ඨාශය /
            <br>பதிவுப் பிரிவு /
            <br>Registration Division
        </td>
        <td colspan="5"><s:select id="deathDivisionId" name="deathDivisionId" list="bdDivisionList"
                                  cssStyle="float:left;"/></td>
    </tr>
    <tr>
        <td rowspan="2" colspan="1">
            ස්ථානය
            <br>இடம்
            <br>Place
        </td>
        <td colspan="2">
            සිංහල හෝ දෙමළ භාෂාවෙන්
            <br>சிங்களம்அல்லது தமிழ் மொழியில்
            <br>In Sinhala or Tamil
        </td>
        <td colspan="5"><s:textfield name="death.placeOfDeath" cssStyle="width:555px;" id="placeOfDeath"/></td>
    </tr>
    <tr>
        <td colspan="2">
            ඉංග්‍රීසි භාෂාවෙන්
            <br>ஆங்கில மொழியில்
            <br>In English
        </td>
        <td colspan="5">
            <s:textfield name="death.placeOfDeathInEnglish" id="placeOfDeathInEnglish" cssStyle="width:555px;"/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px 0;"
                 id="place">
        </td>
    </tr>
    <tr>
        <td rowspan="2" colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            මරණයට හේතුව තහවුරුද?
            <br>இறப்பிற்கான காரணம் உறுதியானதா?
            <br>Is the cause of death established?
        </td>
        <td colspan="1">නැත / இல்லை / No</td>
        <td colspan="2" align="center"><s:radio name="death.causeOfDeathEstablished"
                                                list="#@java.util.HashMap@{'false':''}"
                                                id=""/></td>
        <td rowspan="2" colspan="3">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            (6)මරණය දින 30 කට අඩු ළදරුවෙකුගේද?
            <br>இறப்பு 30 நாட்களுக்கு குறைவான சிசுவினதா?
            <br>Is the death of an infant less than 30 days?
        </td>
        <td colspan="1">නැත / இல்லை / No</td>
        <td colspan="1" align="center"><s:radio name="death.infantLessThan30Days"
                                                list="#@java.util.HashMap@{'false':''}"/></td>
    </tr>
    <tr>
        <td colspan="1">ඔව් / ஆம் /Yes</td>
        <td colspan="2" align="center"><s:radio name="death.causeOfDeathEstablished"
                                                list="#@java.util.HashMap@{'true':''}"/></td>
        <td colspan="1">ඔව් / ஆம் /Yes</td>
        <td colspan="1" align="center"><s:radio name="death.infantLessThan30Days"
                                                list="#@java.util.HashMap@{'true':''}"/></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            මරණයට හේතුව
            <br>இறப்பிற்கான காரணம்
            <br>Cause of death
        </td>
        <td colspan="4"><s:textarea name="death.causeOfDeath" cssStyle="width:420px; "/></td>
        <td colspan="2">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            ේතුවේ ICD කේත අංකය
            <br>காரணத்திற்கான ICD குறியீட்டு இலக்கம்
            <br>ICD Code of cause
        </td>
        <td colspan="2"><s:textfield name="death.icdCodeOfCause" cssStyle="width:225px;"/></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            ආදාහන හෝ භූමදාන ස්ථානය
            <br>அடக்கம் செய்த அல்லது தகனஞ் செய்த இடம்
            <br>Place of burial or cremation
        </td>
        <td colspan="8"><s:textarea name="death.placeOfBurial" id="placeOfBurial" cssStyle="width:880px;"/></td>
    </tr>
    <s:if test="deathType.ordinal() == 2 || deathType.ordinal() == 3">
        <tr>
            <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                වෙනත් තොරතුරු
                <br>வேறுத்தகவல்கள்
                <br>Any other information
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
        <td colspan="7">
            මරණයට පත් වූ පුද්ගලයාගේ විස්තර
            <br>இறந்த நபரைப் பற்றிய தகவல்
            <br>Information about the person Departed
        </td>
    </tr>
    <tr>
        <td rowspan="2" colspan="2">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            අනන්‍යතා අංකය
            <br>அடையாள எண்
            <br>Identification No.
        </td>
        <td rowspan="2" colspan="2" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="death_person_NIC_V" onclick="javascript:addXorV('deathPerson_PINorNIC','V','error12')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="death_person_NIC_X" onclick="javascript:addXorV('deathPerson_PINorNIC','X','error12')">
            <br>
            <s:textfield name="deathPerson.deathPersonPINorNIC" id="deathPerson_PINorNIC"
                         cssStyle="float:left;"/>
            <img src="<s:url value="/images/search-father.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="death_person_lookup">

        </td>
        <td rowspan="2">
            විදේශිකය‍කු නම්
            <br>வெளிநாட்டவர் எனின்
            <br>If a foreigner
        </td>
        <td>
            රට
            <br>நாடு
            <br>Country
        </td>
        <td><s:select id="deathPersonCountryId" name="deathPersonCountry" list="countryList" headerKey="0"
                      headerValue="%{getText('select_country.label')}" cssStyle="width:185px;"/></td>
    </tr>
    <tr>
        <td>
            ගමන් බලපත්‍ර අංකය
            <br>கடவுச் சீட்டு
            <br>Passport No.
        </td>
        <td><s:textfield name="deathPerson.deathPersonPassportNo" cssStyle="width:180px;"/></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            වයස හෝ අනුමාන වයස
            <br>வயது அல்லது அனுமான வயது
            <br>Age or probable Age
        </td>
        <td colspan="1"><s:textfield name="deathPerson.deathPersonAge" id="deathPersonAge"/></td>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            ස්ත්‍රී පුරුෂ භාවය
            <br>பால்
            <br>Gender
        </td>
        <td colspan="1"><s:select
                list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                name="deathPerson.deathPersonGender" headerKey="0" headerValue="%{getText('select_gender.label')}"
                id="deathPersonGender" cssStyle="width:190px; margin-left:5px;"/></td>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            ජාතිය
            <br>இனம்
            <br>Race
        </td>
        <td colspan="2">
            <s:select list="raceList" name="deathPersonRace" headerKey="0" headerValue="%{getText('select_race.label')}"
                      cssStyle="width:300px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)
            <br>பெயா் அரச கரும மொழியில் (சிங்களம் / தமிழ்)
            <br>Name in either of the official languages (Sinhala / Tamil)
        </td>
        <td colspan="6"><s:textarea name="deathPerson.deathPersonNameOfficialLang" id="deathPersonNameOfficialLang"
                                    cssStyle="width:880px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            නම ඉංග්‍රීසි භාෂාවෙන්
            <br>பெயா் ஆங்கில மொழியில்
            <br>Name in English
        </td>
        <td colspan="6">
            <s:textarea name="deathPerson.deathPersonNameInEnglish" id="deathPersonNameInEnglish"
                        cssStyle="width:880px;"/>
            <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px 0;"
                 id="deathName">
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            ස්ථිර ලිපිනය
            <br>நிரந்தர வதிவிட முகவரி
            <br>Permanent Address
        </td>
        <td colspan="6">
            <s:textarea name="deathPerson.deathPersonPermanentAddress" id="deathPersonPermanentAddress"
                        cssStyle="width:880px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            පියාගේ අනන්‍යතා අංකය
            <br>தந்தையின் அடையாள எண்
            <br>Fathers Identification No.
        </td>
        <td colspan="6" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="death_person_father_NIC_V"
                 onclick="javascript:addXorV('deathPersonFather_PINorNIC','V','error12')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="death_person_father_NIC_X"
                 onclick="javascript:addXorV('deathPersonFather_PINorNIC','X','error12')">
            <br>
            <s:textfield name="deathPerson.deathPersonFatherPINorNIC" id="deathPersonFather_PINorNIC"
                         cssStyle="float:left;"/>

            <img src="<s:url value="/images/search-father.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="death_person_father_lookup">

        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            පියාගේ සම්පුර්ණ නම
            <br>தந்தையின் முழுப் பெயர்
            <br>Fathers full name
        </td>
        <td colspan="6">
            <s:textarea name="deathPerson.deathPersonFatherFullName" id="deathPersonFatherFullName"
                        cssStyle="width:880px;"/>
        </td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            මවගේ අනන්‍යතා අංකය
            <br>தாயின் அடையாள எண்
            <br>Mothers Identification No.
        </td>
        <td colspan="6" class="find-person">
            <img src="<s:url value="/images/alphabet-V.gif" />"
                 id="death_person_mother_NIC_V"
                 onclick="javascript:addXorV('deathPersonMother_PINorNIC','V','error12')">
            <img src="<s:url value="/images/alphabet-X.gif" />"
                 id="death_person_mother_NIC_X"
                 onclick="javascript:addXorV('deathPersonMother_PINorNIC','X','error12')">
            <br>
            <s:textfield name="deathPerson.deathPersonMotherPINorNIC" id="deathPersonMother_PINorNIC"
                         cssStyle="float:left;"/>
            <img src="<s:url value="/images/search-mother.png" />"
                 style="vertical-align:middle; margin-left:20px;" id="death_person_mother_lookup"></td>
    </tr>
    <tr>
        <td colspan="1">(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
            මවගේ සම්පුර්ණ නම
            <br>தாயின் முழுப் பெயர்
            <br>Mothers full name
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
<s:hidden id="error12" value="%{getText('NIC.error.add.VX')}"/>

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

<s:hidden id="deathTypeId" value="%{deathType.ordinal()}"/>
<s:hidden id="error13" value="%{getText('enter.reasonForLate.label')}"/>

</div>
