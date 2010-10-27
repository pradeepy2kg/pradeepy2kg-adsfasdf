<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>
<script>
    //these inpute can not be null
    var errormsg = "";
    function validate() {

        var returnval = true;
        var domObject;
        domObject = document.getElementById("reciveDatePicker");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error1');
        } else {
            isDate(domObject.value, 'error0', 'error1');
        }

        domObject = document.getElementById("deathDatePicker");
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, "", 'error2')
        } else {
            isDate(domObject.value, 'error0', 'error2');
        }

        domObject = document.getElementById("deathPerson_PINorNIC");
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error0', 'error3');
        }

        domObject = document.getElementById("passportNumber");
        if (!isFieldEmpty(domObject)) {
            validatePassportNo(domObject, 'error0', 'error4');
        }

        domObject = document.getElementById("deathAge");
        if (!isFieldEmpty(domObject)) {
            isNumeric(domObject.value, 'error0', 'error5');
        }

        domObject = document.getElementById("fatherPinNic");
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error0', 'error6');
        }

        domObject = document.getElementById("motherNIC");
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error0', 'error7');
        }

        domObject = document.getElementById("declarant_pinOrNic");
        if (!isFieldEmpty(domObject)) {
            validatePINorNIC(domObject, 'error0', 'error8');
        }

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }
</script>

<script type="text/javascript">
var act;
var informPerson;
function setInformPerson(nICorPIN, name, address, tp, email) {
    var informantName = document.getElementById("declarant_pinOrNic").value = nICorPIN;
    var informantNICorPIN = document.getElementById("declarantName").value = name;
    var informantAddress = document.getElementById("declarantAddress").value = address;
    var informantTP = document.getElementById("declarant_tp").value = tp;
    var informantEmail = document.getElementById("declarant_email").value = email;
}

function setAct(value) {
    if (value == 7) {
        act = 53;
    } else {
        act = 52;
    }
}

function validateAct(value) {
    if (value != act) {
        alert("add massage conflict with selected act and row number 10")
    }
}

$(function() {
    $("#deathDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});

$(function() {
    $("#reciveDatePicker").datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
});

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

        function getTrueOffsetLeft(ele) {
            var n = 0;
            while (ele) {
                n += ele.offsetLeft || 0;
                ele = ele.offsetParent;
            }
            return n;
        }

        function getTrueOffsetTop(ele) {
            var n = 0;
            while (ele) {
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
    $("#deathTimePicker").cantipi({size:140, roundto: 5});
});
<%--end of time picker--%>

/*show hide*/
$(function() {
    $('#death-info-min').click(function() {
        minimize("death-info");
    });
    $('#death-info-max').click(function() {
        maximize("death-info");
    });


    $('#death-person-info-min').click(function() {
        minimize("death-person-info");
    });
    $('#death-person-info-max').click(function() {
        maximize("death-person-info");
    });
    $('#death-info-check').click(function() {


        document.getElementById("death-info-check").disabled = true;


        var fieldIds = new Array('deathDatePicker', 'deathTimePicker', 'placeOfDeath', 'placeOfDeathInEnglish', 'cause_of_death',
                'ICD_code', 'placeOfBurial', 'act5353', 'act5252', 'cause_of_death_yesfalse', 'cause_of_death_notrue');
        enableFields(fieldIds);
    });

    $('#death-person-info-check').click(function() {


        document.getElementById("death-person-info-check").disabled = true;


        var fieldIds = new Array('deathPerson_PINorNIC', 'deathPersonCountryList', 'passportNumber', 'deathAge', 'deathPersonGender',
                'deathPersonRaceList', 'nameOfficialLang', 'nameEnglish', 'address', 'pinNic', 'fatherName', 'fatherNIC', 'motherName');
        enableFields(fieldIds);
    });
});


function enableFields(fieldIds) {
    for (var i = 0; i < fieldIds.length; i++) {
        document.getElementById(fieldIds[i]).disabled = false;
    }
}

function minimize(id) {
    document.getElementById(id).style.display = 'none';
    document.getElementById(id + "-min").style.display = 'none';
    document.getElementById(id + "-max").style.display = 'block';
    document.getElementById(id + "-check").style.display = 'none';
    document.getElementById(id + "-check-lable").style.display = 'none';

}

function maximize(id, click) {
    document.getElementById(id).style.display = 'block';
    document.getElementById(id + "-max").style.display = 'none';
    document.getElementById(id + "-min").style.display = 'block';
    document.getElementById(id + "-check").style.display = 'block';
    document.getElementById(id + "-check-lable").style.display = 'block';

}


function initPage() {
    var idNames;
    var checkIdNames;
    var fieldIds;
    idNames = new Array('death-info', 'death-person-info');
    checkIdNames = new Array('death-person-info-check', 'death-info-check');
    fieldIds = new Array(
            'deathDatePicker', 'deathTimePicker', 'placeOfDeath', 'placeOfDeathInEnglish',
            'cause_of_death', 'ICD_code', 'placeOfBurial', 'deathPerson_PINorNIC', 'deathPersonCountryList',
            'passportNumber', 'deathAge', 'deathPersonGender', 'deathPersonRaceList', 'nameOfficialLang', 'nameEnglish', 'address', 'pinNic',
            'fatherName', 'fatherNIC', 'motherName', 'act5353', 'act5252', 'cause_of_death_yesfalse', 'cause_of_death_notrue'
            );

    //set serial number

    for (var i = 0; i < idNames.length; i++) {
        document.getElementById(idNames[i]).style.display = 'none';
        document.getElementById(idNames[i] + "-min").style.display = 'none';
    }
    for (var i = 0; i < checkIdNames.length; i++) {
        document.getElementById(checkIdNames[i]).style.display = 'none';
        document.getElementById(checkIdNames[i] + "-lable").style.display = 'none';
    }
    for (var i = 0; i < fieldIds.length; i++) {
        document.getElementById(fieldIds[i]).disabled = true;
    }
}
</script>
<div id="death-alteration-outer">
<s:form method="post" action="eprCaptureDeathAlteration.do" onsubmit="javascript:return validate()">
<table class="death-alteration-table-style01" style="width:1030px;" cellpadding="2px">
    <tr>
        <td width="30%"></td>
        <td width="35%" style="text-align:center;"></td>
        <td width="35%">
            <table class="birth-alteration-table-style02" cellspacing="0" style="float:right;width:100%">
                <tr>
                    <td colspan="2" style="text-align:center;">කාර්යාල ප්‍රයෝජනය සඳහා පමණි /<br>
                        அலுவலக பாவனைக்காக மட்டும் / <br>
                        For office use only
                    </td>
                </tr>
                <s:if test="alterationSerialNo>0">
                    <tr>
                        <td width="40%"><s:label value="අනුක්‍රමික අංකය"/><br>
                            <s:label value=" தொடர் இலக்கம் "/><br>
                            <s:label value=" Serial Number"/>
                        </td>

                        <td width="60%"><s:textfield id="bdfSerialNo" name="alterationSerialNo" maxLength="10"
                                                     value="%{alterationSerialNo}" readonly="true"/></td>

                    </tr>
                </s:if>
                <tr>
                    <s:fielderror name="duplicateSerialNumberError" cssStyle="color:red;font-size:9pt;"/>
                </tr>
                <tr>
                    <td><s:label value="භාරගත් දිනය"/><br>
                        <s:label value="பிறப்பைப் பதிவு திி"/> <br>
                        <s:label value="Date of Acceptance"/>
                    </td>
                    <td><s:textfield id="reciveDatePicker" name="deathAlteration.dateReceived" value="%{toDay}"/></td>
                <tr>
                    <td><s:label value="පනතේ වගන්තිය "/><br>
                        <s:label value="பிறப்பைப்"/> <br>
                        <s:label value="Section of the Act"/>
                    </td>
                    <td>
                        <s:select
                                list="#@java.util.HashMap@{'TYPE_52_1_A':'52(1) A ','TYPE_52_1_B':'52(1) B','TYPE_52_1_D':'52(1) D','TYPE_52_1_E':'52(1) E','TYPE_52_1_I':'52(1) I','TYPE_52_1_H':'52(1) H','TYPE_53':'53'}"
                                name="deathAlteration.type"
                                cssStyle="width:190px; margin-left:5px;" onchange="setAct(value)"/>
                    </td>

                </tr>
            </table>
        </td>
    </tr>
</table>

<table class="death-alteration-table-style01" style="width:1030px;">
    <tr>
        <td colspan="3" style="font-size:12pt;text-align:center;">
            <s:label
                    value="මරණ සහතිකයක දෝෂ නිවැරදි කිරීම (උප්පැන්න හා මරණ ලියාපදිංචි කිරීමේ පනතේ 52 (1) සහ 53 වගන්ති)"/>
            <br>
            <s:label value="தந்தை பற்றிய தகவல்"/> <br>
            <s:label
                    value="Correction of Errors of a Death Certificate (under the Births and Deaths Registration Act. Sections 52 (1) and 53)"/>
        </td>
    </tr>
    <tr>
        <td>
            <s:label
                    value="සැ.යු. හදිසි මරණ සහතිකයක වෙනසක් කිරීමට ඉල්ලුම් කල හැක්කේ මරණ පරීක්ෂක හට පමණකි. in Tamil. Note: An alteration on a certificate of death for a sudden death can be requested only by an inquirer into deaths"/>
        </td>
    </tr>
    <tr>
        <td></td>
    </tr>
    <tr>
        <td></td>
    </tr>
    <tr>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;border-top:2px">
            <s:label value="වෙනස් කලයුතු මරණ සහතිකය පිලිබඳ විස්තර"/> <br>
            <s:label value="பிள்ளை பற்றிய தகவல்"/> <br>
            <s:label value="Particulars of the Death Certificate to amend"/>
        </td>
    </tr>
</table>
<br>
<table class="death-alteration-table-style02" style=" margin-top:0px;width:100%;" cellpadding="0" cellpadding="2px"
       cellspacing="0">
    <caption></caption>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <col style="width:20%"/>
    <tbody>
    <tr>
        <td colspan="2">(1)සහතිකයේ සඳහන් පුද්ගලයාගේ අනන්‍යතා අංකය <br>
            தனிநபர்அடையாள எண் <br>
            Person Identification Number (PIN) stated in the Certificate
        </td>
        <td>
            <s:property value="deathRegister.deathPerson.deathPersonPINorNIC"/>
        </td>
        <td>(2)සහතික පත්‍රයේ අංකය <br>
            சான்றிதழ் இல <br>
            Certificate Number
        </td>
        <td>
            <s:property value="deathRegister.idUKey"/>
        </td>
    </tr>
    <tr>
        <td>(3)දිස්ත්‍රික්කය <br>
            மாவட்டம் <br>
            District
        </td>
        <td>
            <s:property value="district"/>
        </td>
        <td>(4)ප්‍රාදේශීය ලේකම් කොට්ඨාශය <br>
            பிரிவு <br>
            Divisional Secretariat
        </td>
        <td colspan="2"><s:property value="dsDivision"/></td>
    </tr>
    <tr>
        <td>(5)ලියාපදිංචි කිරීමේ කොට්ඨාශය <br>
            பிரிவு <br>
            Registration Division
        </td>
        <td>
            <s:property value="deathDivision"/>
        </td>
        <td>(6)ලියාපදිංචි කිරීමේ අංකය <br>
            சான்றிதழ் இல <br>
            Registration Number
        </td>
        <td colspan="2">
            <s:property value="deathRegister.death.deathSerialNo"/>
        </td>
    </tr>
    </tbody>
</table>
<br>
<%--
<table class="death-alteration-table-style01" style="width:1030px;border-top:50px">
    <tr>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;">
            <s:label value="ඇතුලත් කිරීම්, වෙනස් කිරීම් සහ ඉවත් කිරීම් තිබෙන තීරු අංක පහත සඳහන් කරන්න "/> <br>
            <s:label value="in Tamil"/> <br>
            <s:label value="Specify cage numbers for Insertions, Alterations and Omissions below"/>
        </td>
    </tr>
</table>
<br>

<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;" cellpadding="2px">
    <caption></caption>
    <col width="350px">
    <col>
    <tr>
        <td>
            (7)ඇතුලත් කිරීම් සහිත තීරු අංක / in tamil
            <br>
            Cage numbers for insertions
        </td>
        <td></td>
    </tr>
    <tr>
        <td>
            (8)වෙනස් කිරීම් සහිත තීරු අංක / in tamil
            <br>
            Cage numbers for Alterations
        </td>
        <td></td>
    </tr>
    <tr>
        <td>
            (9)ඉවත් කල යුතු තීරු අංක / in tamil
            <br>
            Cage numbers to be Deleted
        </td>
        <td></td>
    </tr>
</table>
<br>
--%>

<table class="death-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <caption/>
    <col width="300px"/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tr>
        <td style="border-right:none"></td>
        <td style="font-size:11pt;text-align:center;margin-top:20px;border-right:none">
            <s:label value="මරණය පිලිබඳ විස්තර"/> <br>
            <s:label value="பிள்ளை பற்றிய தகவல்"/> <br>
            <s:label value="Information about the Death"/>
        </td>
        <td style="width:20%;text-align:right;border-right:none">
                <%--            <div id="death-info-check-lable">
               <s:label value="%{getText('edit.lable')}"/></div>--%>
        </td>
        <td style="border-right:none;width:3%">
                <%--
                            <s:checkbox id="death-info-check" name="editDeathInfo" cssStyle="float:right;"/>
                --%>
        </td>
        <td style="width:2%;border-left:none">
            <div class="birth-alteration-minimize-icon" id="death-info-min">[-]</div>
            <div class="birth-alteration-maximize-icon" id="death-info-max">[+]</div>
        </td>
    </tr>
</table>
<br>

<div id="death-info">
    <table border="1" style="margin-top:1px;width:100%;border:1px solid #000;border-collapse:collapse;"
           cellpadding="2px">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr style="border-bottom:2px">
            <td>
                (10)හදිසි මරණයක්ද ? <br>
                in tamil <br>
                Sudden death?
            </td>
            <td colspan="2">
                ඔව් (53 වගන්තිය) <br>
                xx <br>
                Yes (Section 53)
            </td>
            <td align="center">
                <s:radio name=""
                         list="#@java.util.HashMap@{'53':''}"
                         id="act53" onclick="validateAct(value)"/>
            </td>
            <td>
                නැත (52 (1) වගන්තිය) <br>
                xx <br>
                No (Section 52 (1))
            </td>
            <td align="center">
                <s:radio name=""
                         list="#@java.util.HashMap@{'52':''}"
                         id="act52" onclick="validateAct(value)"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" style="border-top:2px">
                (11)මරණය සිදු වූ දිනය <br>
                பிறந்த திகதி <br>
                Date of Death
            </td>
            <td colspan="2" align="center">
                <s:textfield name="deathAlteration.deathInfo.dateOfDeath" id="deathDatePicker"
                             value="%{deathRegister.death.dateOfDeath}"/>
            </td>
            <td>
                වෙලාව <br>
                in tamil<br>
                Time
            </td>
            <td align="center">
                <s:textfield name="deathAlteration.deathInfo.timeOfDeath" id="deathTimePicker"
                             value="%{deathRegister.death.timeOfDeath}"/>
            </td>
        </tr>
        <tr>
            <td rowspan="2">
                (12)මරණය සිදු වූ ස්ථානය <br>
                பிறந்த இடம் <br>
                Place of Death
            </td>
            <td colspan="2">
                සිංහල හෝ දෙමළ භාෂාවෙන් <br>
                சிங்களம் தமிழ் <br>
                In Sinhala or Tamil
            </td>
            <td colspan="3">
                <s:textfield name="deathAlteration.deathInfo.placeOfDeath" cssStyle="width:99%;" id="placeOfDeath"
                             value="%{deathRegister.death.placeOfDeath}"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                ඉංග්‍රීසි භාෂාවෙන් <br>
                in tamil <br>
                In English
            </td>
            <td colspan="3">
                <s:textfield name="deathAlteration.deathInfo.placeOfDeathInEnglish" id="placeOfDeathInEnglish"
                             cssStyle="width:99%;" value="%{deathRegister.death.placeOfDeathInEnglish}"/>
                <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;margin:5px 0;"
                     id="place">
            </td>
        </tr>
        <tr>
            <td>(13)මරණයට හේතුව තහවුරුද? <br>
                in tamil <br>
                Cause of death established?
            </td>
            <td colspan="2">
                නැත / xx / No
            </td>
            <td align="center">
                <s:radio name="deathAlteration.deathInfo.causeOfDeathEstablished"
                         list="#@java.util.HashMap@{'false':''}"
                         id="cause_of_death_yes" value="%{deathRegister.death.causeOfDeathEstablished}"/>
            </td>
            <td>ඔව් / xx /Yes</td>
            <td align="center">
                <s:radio name="deathAlteration.deathInfo.causeOfDeathEstablished"
                         list="#@java.util.HashMap@{'true':''}"
                         id="cause_of_death_no" value="%{deathRegister.death.causeOfDeathEstablished}"/>
            </td>
        </tr>
        <tr>
            <td>(14)මරණයට හේතුව <br>
                in tamil <br>
                Cause of death
            </td>
            <td colspan="3">
                <s:textarea name="deathAlteration.deathInfo.causeOfDeath" value="%{deathRegister.death.causeOfDeath}"
                            cssStyle="width:420px;" id="cause_of_death"/>
            </td>
            <td>
                (15)හේතුවේ ICD කේත අංකය <br>
                in tamil <br>
                ICD Code of cause
            </td>
            <td>
                <s:textfield name="deathAlteration.deathInfo.icdCodeOfCause"
                             value="%{deathRegister.death.icdCodeOfCause}"
                             cssStyle="width:225px;" id="ICD_code"/>
            </td>
        </tr>
        <tr>
            <td>(16)ආදාහන හෝ භූමදාන ස්ථානය <br>
                in tamil <br>
                Place of burial or cremation
            </td>
            <td colspan="5">
                <s:textarea name="deathAlteration.deathInfo.placeOfBurial" value="%{deathRegister.death.placeOfBurial}"
                            id="placeOfBurial" cssStyle="width:99%;"/>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>

<table class="death-alteration-table-style02" style=" margin-top:20px;width:100%" cellpadding="0" cellspacing="0">
    <caption/>
    <col width="300px"/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tr>
        <td style="border-right:none"></td>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;border-right:none">
            <s:label value="මරණයට පත් වූ පුද්ගලයාගේ විස්තර"/> <br>
            <s:label value="பிள்ளை பற்றிய தகவல்"/> <br>
            <s:label value="Information about the person Departed"/>
        </td>
        <td style="width:20%;text-align:right;border-right:none">
                <%--            <div id="death-person-info-check-lable">
               <s:label value="%{getText('edit.lable')}"/></div>--%> 
        </td>
        <td style="border-right:none;width:3%">

                <%--
                            <s:checkbox id="death-person-info-check" name="editDeathPerson" cssStyle="float:right;"/>
                --%>

        </td>
        <td style="width:2%">
            <div class="birth-alteration-minimize-icon" id="death-person-info-min">[-]</div>
            <div class="birth-alteration-maximize-icon" id="death-person-info-max">[+]</div>
        </td>
    </tr>
</table>
<br>

<div id="death-person-info">
    <table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;"
           cellpadding="2px">
        <caption></caption>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <col/>
        <tbody>
        <tr>
            <td rowspan="2">
                (17)අනන්‍යතා අංකය <br>
                தனிநபர் அடையாள எண் <br>
                Identification Number
            </td>
            <td colspan="3" rowspan="2">
                <s:textfield name="deathRegister.deathPerson.deathPersonPINorNIC" id="deathPerson_PINorNIC"
                             cssStyle="float:left;"/>
                <img src="<s:url value="/images/search-father.png" />"
                     style="vertical-align:middle; margin-left:20px;" id="death_person_lookup">
            </td>

            <td rowspan="2">
                (18)විදේශිකය‍කු නම් <br>
                வெளிநாட்டவர் <br>
                If a foreigner
            </td>
            <td>
                රට <br>
                நாடு <br>
                Country
            </td>
            <td align="center">
                <s:select id="deathPersonCountryList" name="deathPersonCountry" value="%{deathCountryId}"
                          list="countryList"
                          headerKey="0"
                          headerValue="%{getText('select_country.label')}"/>
            </td>
        </tr>
        <tr>
            <td style="border-top:20px">
                ගමන් බලපත්‍ර අංකය <br>
                கடவுச் சீட்டு <br>
                Passport No.
            </td>
            <td>
                <s:textfield name="deathRegister.deathPerson.deathPersonPassportNo" cssStyle="width:180px;"
                             id="passportNumber"/>
            </td>
        </tr>
        <tr>
            <td>
                (19)වයස හෝ අනුමාන වයස <br>
                பிறப்ப <br>
                Age or probable Age
            </td>
            <td><s:textfield name="deathRegister.deathPerson.deathPersonAge" cssStyle="width:180px;"
                             id="deathAge"/></td>
            <td>
                (20)ස්ත්‍රී පුරුෂ භාවය <br>
                பால் <br>
                Gender
            </td>
            <td>
                <s:select
                        list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                        name="deathRegister.deathPerson.deathPersonGender" headerKey="0"
                        headerValue="%{getText('select_gender.label')}"
                        id="deathPersonGender" cssStyle="width:190px; margin-left:5px;"/>
            </td>
            <td>
                (21)ජාතිය <br>
                பிறப் <br>
                Race
            </td>
            <td colspan="2" align="center">
                <s:select list="raceList" name="deathPersonRace" headerKey="0" value="%{deathRaceId}"
                          headerValue="%{getText('select_race.label')}"
                          cssStyle="width:200px;" id="deathPersonRaceList"/>
            </td>
        </tr>
        <tr>
            <td>
                (22)නම රාජ්‍ය භාෂාවෙන් <br>
                (සිංහල / දෙමළ)
                பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்) <br>
                Name in either of the official languages (Sinhala / Tamil)
            </td>
            <td colspan="6">
                <s:textarea name="deathRegister.deathPerson.deathPersonNameOfficialLang" cssStyle="width:99%;"
                            id="nameOfficialLang"/>
            </td>
        </tr>
        <tr>
            <td>
                (23)නම ඉංග්‍රීසි භාෂාවෙන් <br>
                பிறப்பு அத்தாட்சி ….. <br>
                Name in English
            </td>
            <td colspan="6">
                <s:textarea name="deathRegister.deathPerson.deathPersonNameInEnglish" cssStyle="width:99%;"
                            id="nameEnglish"/>
            </td>
        </tr>
        <tr>
            <td>
                (24)ස්ථිර ලිපිනය <br>
                தாயின் நிரந்தர வதிவிட முகவரி <br>
                Permanent Address
            </td>
            <td colspan="6">
                <s:textarea name="deathRegister.deathPerson.deathPersonPermanentAddress" cssStyle="width:99%;"
                            id="address"/>
            </td>
        </tr>
        <tr>
            <td>
                (25)පියාගේ අනන්‍යතා අංකය <br>
                தனிநபர் அடையாள எண் <br>
                Fathers Identification Number
            </td>
            <td colspan="6">
                <s:textfield name="deathRegister.deathPerson.deathPersonFatherPINorNIC" cssStyle="width:180px;"
                             id="fatherPinNic"/>
            </td>
        </tr>
        <tr>
            <td>
                (26)පියාගේ සම්පුර්ණ නම <br>
                in tamil <br>
                Fathers full name
            </td>
            <td colspan="6">
                <s:textarea name="deathRegister.deathPerson.deathPersonFatherFullName" cssStyle="width:99%;"
                            id="fatherName"/>
            </td>
        </tr>
        <tr>
            <td>
                (27)මවගේ අනන්‍යතා අංකය <br>
                தனிநபர் அடையாள எண் <br>
                Mothers Identification Number
            </td>
            <td colspan="6">
                <s:textfield name="deathRegister.deathPerson.deathPersonMotherPINorNIC" cssStyle="width:180px;"
                             id="motherNIC"/>
            </td>

        </tr>
        <tr>
            <td>
                (28)මවගේ සම්පුර්ණ නම <br>
                in tamil <br>
                Mothers full name
            </td>
            <td colspan="6">
                <s:textarea name="deathRegister.deathPerson.deathPersonMotherFullName" cssStyle="width:99%;"
                            id="motherName"/>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>
<table class="death-alteration-table-style01" style=" margin-top:20px;width:100%;" cellpadding="0"
       cellspacing="0">
    <tr>
        <td colspan="8" style="text-align:center;font-size:12pt;width:90%;border-right:none">
            දෝෂය හා එය සිදුවූ අන්දම පිලිබඳ ලුහුඬු විස්තර<br>
            தாத்தாவின் / பாட்டனின் விபரங்கள்<br>
            Nature of the error and a brief explanation of how the error occurred
        </td>
        <td style="border-right:none"></td>
        <td></td>
    </tr>
</table>
<br>
<table class="death-alteration-table-style02" style=" margin-top:20px;width:100%;" cellpadding="0" cellpadding="2px"
       cellspacing="0">
    <tr>
        <td colspan="2" style="text-align:center;font-size:12pt">
            ප්‍රකාශය සනාත කිරීමට ඇති ලේඛනගත හෝ වෙනත් සාක්ෂිවල ස්වභාවය<br>
            தாத்தாவின் / பாட்டனின் விபரங்கள் <br>
            Nature of documentary or other evidence in support of the declaration
        </td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="deathAlteration.bcOfFather"/></td>
        <td style="width:90%"><s:label
                value=" පියාගේ උප්පැන්න සහතිකය  / in Tamil / Fathers Birth Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="deathAlteration.bcOfMother"/></td>
        <td style="width:90%"><s:label
                value="මවගේ උප්පැන්න සහතිකය / in Tamil / Mothers Birth Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td style="width:5%"><s:checkbox name="deathAlteration.mcOfParents"/></td>
        <td style="width:90%"><s:label
                value=" මව්පියන්ගේ විවාහ සහතිකය / in Tamil / Parents Marriage Certificate"
                cssStyle="margin-left:5px;"/></td>
    </tr>
    <tr>
        <td colspan="2" style="width:98%"><s:textarea name="deathAlteration.otherDocuments" cssStyle="width:98%"/></td>
    </tr>
</table>
<br>
<table class="death-alteration-table-style01" style="width:1030px;border-top:50px">
    <tr>
        <td colspan="3" style="font-size:11pt;text-align:center;margin-top:20px;">
            <s:label value="ප්‍රකාශය කරන්නාගේ විස්තර"/> <br>
            <s:label value="அறிவிப்பு கொடுப்பவரின் தகவல்கள்"/> <br>
            <s:label value="Details of the Declarant"/>
        </td>
    </tr>
</table>
<br>
<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;" cellpadding="2px">
    <caption></caption>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <col/>
    <tbody>
    <tr>
        <td rowspan="2" width="200px">
            (30)දැනුම් දෙන්නේ කවරකු වශයෙන්ද <br>
            in tamil <br>
            Capacity for giving information
        </td>
        <td>
            පියා / මව <br>
            in tamil <br>
            Father / Mother
        </td>
        <td align="center"><s:radio id="declarantType" name="deathRegister.declarant.declarantType"
                                    list="#@java.util.HashMap@{'FATHER':''}"
                                    onchange="setInformPerson('','','','','');"
                                    value="%{deathAlteration.declarant.declarantType}"/>
        </td>
        <td align="center">
            ස්වාමිපුරුෂයා / භාර්යාව <br>
            in tamil <br>
            Husband / Wife
        </td>
        <td width="45px" align="center">
                <%--todo change--%>
            <s:radio id="declarantType" name="deathRegister.declarant.declarantType"
                     list="#@java.util.HashMap@{'SPOUSE':''}"
                     onchange="setInformPerson('','','','','');" value="%{deathAlteration.declarant.declarantType}"/>
        </td>
        <td>
            සහෝදරයා සහෝදරිය <br>
            in tamil <br>
            Brother / Sister
        </td>
        <td align="center">
            <s:radio id="declarantType" name="deathRegister.declarant.declarantType"
                     list="#@java.util.HashMap@{'BORTHER_OR_SISTER':''}"
                     onchange="setInformPerson('','','','','');" value="%{deathAlteration.declarant.declarantType}"/>
        </td>
    </tr>
    <tr>
        <td>
            පුත්‍රයා / දියණිය <br>
            in tamil <br>
            Son / Daughter
        </td>
        <td align="center">
            <s:radio id="declarantType" name="deathRegister.declarant.declarantType"
                     list="#@java.util.HashMap@{'SON_OR_DAUGHTER':''}"
                     onchange="setInformPerson('','','','','');" value="%{deathAlteration.declarant.declarantType}"/>
        </td>
        <td>
            නෑයන් <br>
            பாதுகாவலர் <br>
            Relative
        </td>
        <td align="center">
            <s:radio id="declarantType" name="deathRegister.declarant.declarantType"
                     list="#@java.util.HashMap@{'RELATIVE':''}"
                     onchange="setInformPerson('','','','','');" value="%{deathAlteration.declarant.declarantType}"/>
        </td>
        <td>
            වෙනත් <br>
            in tamil <br>
            Other
        </td>
        <td align="center">
            <s:radio id="declarantType" name="deathRegister.declarant.declarantType"
                     list="#@java.util.HashMap@{'OTHER':''}"
                     onchange="setInformPerson('','','','','');" value="%{deathAlteration.declarant.declarantType}"/>
        </td>
    </tr>
    <tr style="border-top:20px">
        <td colspan="1">
            (31)අනන්‍යතා අංකය <br>
            தனிநபர் அடையாள எண் <br>
            Identification Number
        </td>
        <td colspan="6" align="left">
            <s:textfield id="declarant_pinOrNic" name="deathRegister.declarant.declarantNICorPIN" maxLength="10"
                         value="%{deathAlteration.declarant.declarantNICorPIN}"/>
        </td>
    </tr>
    <tr>
        <td>
            (32)නම <br>
            கொடுப்பவரின் பெயர் <br>
            Name
        </td>
        <td colspan="6">
            <s:textarea id="declarantName" name="deathRegister.declarant.declarantFullName" cssStyle="width:99%"
                        value="%{deathAlteration.declarant.declarantFullName}"/>
        </td>
    </tr>
    <tr>
        <td>
            (33)තැපැල් ලිපිනය <br>
            தபால் முகவரி <br>
            Postal Address
        </td>
        <td colspan="6">
            <s:textarea id="declarantAddress" name="deathRegister.declarant.declarantAddress" cssStyle="width:99%"
                        value="%{deathAlteration.declarant.declarantAddress}"/>
        </td>
    </tr>

    </tbody>
</table>
<%--<table border="1" style="margin-top:0px;width:100%;border:1px solid #000;border-collapse:collapse;border-top:none"
       cellpadding="5px">
    <tbody>
    <tr>
        <td>(34)ඇමතුම් විස්තර <br>
            இலக்க வகை <br>
            Contact Details
        </td>
        <td>
            දුරකතනය <br>
            தொலைபேசி இலக்கம் <br>
            Telephone
        </td>
        <td colspan="2" align="center">
            <s:textfield id="declarant_tp" name="deathRegister.declarant.declarantPhone" maxLength="10"
                         cssStyle="width:98%"/>
        </td>
        <td>
            ඉ -තැපැල <br>
            மின்னஞ்சல் <br>
            Email
        </td>
        <td colspan="2">
            <s:textfield cssStyle="width:98%" id="declarant_email" name="deathRegister.declarant.declarantEMail"
                         maxLength="10"/>
        </td>
    </tr>
    </tbody>
</table>--%>
<div class="form-submit">
    <s:submit value="%{getText('save.label')}"/>
</div>
<s:hidden name="pageNumber" value="1"/>
<s:hidden name="deathId" value="%{deathRegister.idUKey}"/>
<s:hidden name="editMode" value="%{editMode}"/>
<s:hidden name="deathAlterationId" value="%{deathAlteration.idUKey}"/>
<s:hidden id="error0" value="%{getText('er.invalid.inputType')}"/>
<s:hidden id="error1" value="%{getText('er.label.reciveDatePicker')}"/>
<s:hidden id="error2" value="%{getText('er.label.dateOfDeath')}"/>
<s:hidden id="error3" value="%{getText('er.label.deathPerson_PINorNIC')}"/>
<s:hidden id="error4" value="%{getText('er.label.passportNumber')}"/>
<s:hidden id="error5" value="%{getText('er.label.deathAge')}"/>
<s:hidden id="error6" value="%{getText('er.label.fatherPinNic')}"/>
<s:hidden id="error7" value="%{getText('er.label.motherNIC')}"/>
<s:hidden id="error8" value="%{getText('er.label.declarant_pinOrNic')}"/>
</s:form>
</div>

