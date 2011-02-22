//check given element is empty
function isEmpty(domElement, errorMessage, errorCode) {
    with (domElement) {
        if (value == null || value.trim() == "") {
            errormsg = errormsg + "\n" + document.getElementById(errorCode).value + " " + errorMessage;
        }
    }
}

function isMandatoryFieldsEmpty(domElement, errorElement, errorCode) {
    with (domElement) {
        if (value == null || value.trim() == "") {
            errormsg = errormsg + "\n" + errorElement + " " + document.getElementById(errorCode).value;
        }
    }
}

//check given element is empty and return true if empty else false
function isFieldEmpty(domElement) {
    with (domElement) {
        return (value == null || value.trim() == "") ? true : false;
    }
}

// check given text is an integer value
function isNumeric(text, errorText, message) {
    var reg = /^([0-9]*)$/;
    if (reg.test(text.trim()) == false) {
        printMessage(errorText, message);
    }
}

// check given text is a number(interger or floating point) 
function validateNumber(text, errorText, errorCode) {
    var reg = /^([0-9]+\.?[0-9]+|[1-9])$/;
    if (reg.test(text.trim()) == false) {
        printMessage(errorText, errorCode);
    }
}

// valdate an email address
function validateEmail(domElement, errorText, errorCode) {
    with (domElement) {
        var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
        if (reg.test(value.trim()) == false) {
            printMessage(errorText, errorCode);
        }
    }
}
/**
 *    validate serial number
 * @param domElement    dom object
 * @param errorText     error test (ex :invalid date)
 * @param errorCode     field name(ex :serial number)
 */
function validateSerialNo(domElement, errorText, errorCode) {
    with (domElement) {
        var reg = /^20([1-9][0-9])[0|1]([0-9]{5})$/;
        var notbe = /^20([1-9][0-9])[0|1]([0]{5})$/;

        if (notbe.test(value.trim()) == true) {
            printMessage(errorText, errorCode);
        } else {
            if (reg.test(value.trim()) == false) {
                printMessage(errorText, errorCode);
            }
        }
    }
}

function validatePassportNo(domElement, errorText, errorCode) {
    with (domElement) {
        var reg = /^([0-9a-zA-Z]{8,15})$/;
        if (reg.test(value.trim()) == false) {
            printMessage(errorText, errorCode);
            return false;
        } else {
            return true;
        }
    }
}

// validate telephone number
function validatePhoneNo(domElement, errorText, errorCode) {
    with (domElement) {
        var reg = /^[0-9]{7,15}$/;
        if (reg.test(value.trim()) == false) {
            printMessage(errorText, errorCode);
        }
    }
}

// validate PIN or NIC
function validatePINorNIC(domElement, errorText, errorCode) {
    with (domElement) {
        var reg = /^(([0-9]{10})|([0-9]{9}[X|x|V|v]))$/;
        if (reg.test(value.trim()) == false) {
            printMessage(errorText, errorCode);
        } else {
            var regNIC = /^([0-9]{9}[X|x|V|v])$/;
            if (domElement.value.search(regNIC) == 0) {
                var day = domElement.value.substring(2, 5);
                if ((day >= 367 && day <= 500) || (day >= 867)) {
                    printMessage(errorText, errorCode);
                }
            }
        }
    }
}

// validate NIC
function validateNIC(domElement, errorText, errorCode) {
    with (domElement) {
        var reg = /^([0-9]{9}[X|x|V|v])$/;
        if (reg.test(value.trim()) == false) {
            printMessage(errorText, errorCode);
        } else {
            var regNIC = /^([0-9]{9}[X|x|V|v])$/;
            if (domElement.value.search(regNIC) == 0) {
                var day = domElement.value.substring(2, 5);
                if ((day >= 367 && day <= 500) || (day >= 867)) {
                    printMessage(errorText, errorCode);
                }
            }
        }
    }
}

// validate Temporary PIN
function validateTemPIN(domElement, errorText, errorCode) {
    with (domElement) {
        var pin = value.trim();
        if (pin.length != 12 || checkInvalidPIN(pin, false)) {
            printMessage(errorText, errorCode);
        }
    }
}

// validate PIN
function validatePIN(domElement, errorText, errorCode) {
    with (domElement) {
        var pin = value.trim();
        if (pin.length != 12 || checkInvalidPIN(pin, true)) {
            printMessage(errorText, errorCode);
        }
    }
}

// used to validate temporary pin and pin
function checkInvalidPIN(pin, isPin) {
    var invalid = false;
    var year = pin.substring(0, 4);
    var date = pin.substring(4, 7);
    var serial = pin.substring(7, 11);
    var check = pin.substring(11, 12);

    if (isPin) {
        if ((year < 1700) || (year > 2200)) {
            return true;
        }
    } else {
        if ((year < 6700) || (year > 7200)) {
            return true;
        }
    }

    if ((date >= 367 && date <= 500) || (date >= 867)) {
        return true;
    }
    if (year >= 1994 && serial >= 2000) {
        return true;
    }
    // TODO validate check digit
    return false;
}

// TODO
// validate check digit of pin
function calculateCheckDigit(number) {

}

// only allows to enter numbers to any field calls this method
function isNumberKey(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        // can enter only "V", this is to use Paste shortcut key (Ctrl+V)
        if (charCode != 118) {
            return false;
        }
    }
    return true;
}

//add X or V at the end of the NIC
function addXorV(domElement, letter, error) {
    var reg = /^([0-9]{9})$/;
    if ((document.getElementById(domElement).value.length == 9) && isInteger((document.getElementById(domElement).value))) {
        document.getElementById(domElement).value = document.getElementById(domElement).value + letter;
    } else {
        alert(document.getElementById(error).value);
    }
}

//validate birth dates with NIC numbert
function validateBirthYearWithNIC(yearNIC, yearDatePicker, errorText) {
    var reg = /^([0-9]{9}[X|x|V|v])$/;
    if (!(isFieldEmpty(document.getElementById(yearNIC))) && !(isFieldEmpty(document.getElementById(yearDatePicker)))) {
        if (document.getElementById(yearNIC).value.search(reg) == 0) {
            if (document.getElementById(yearDatePicker).value.substring(2, 4) != document.getElementById(yearNIC).value.substring(0, 2)) {
                errormsg = errormsg + "\n" + document.getElementById(errorText).value;
            }
        }
    }
}

// print error message
function printMessage(errorText, errorCode) {
    errormsg = errormsg + "\n" + document.getElementById(errorText).value + " : " + document.getElementById(errorCode).value;
    // errormsg = errormsg + "<br>" + document.getElementById(errorText).value + " " + document.getElementById(errorCode).value;
}

//todo amith
//date format validations
var dtCh = "-";
var minYear = 1900;
var maxYear = 2100;

function isInteger(s) {
    var i;
    for (i = 0; i < s.length; i++) {
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) {
            return false;
        }
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag) {
    var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++) {
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) {
            returnString += c;
        }
    }
    return returnString;
}

function daysInFebruary(year) {
    // February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}

function DaysArray(n) {
    for (var i = 1; i <= n; i++) {
        this[i] = 31
        if (i == 4 || i == 6 || i == 9 || i == 11) {
            this[i] = 30
        }
        if (i == 2) {
            this[i] = 29
        }
    }
    return this
}
/**
 * check date is valid date
 * @param dtStr
 * @param errorText
 * @param errorCode
 */
function isDate(dtStr, errorText, errorCode) {
    if (isFutureDate(dtStr)) {
        printMessage(errorText, errorCode);
    } else {
        var isValideDate = true;
        var daysInMonth = DaysArray(12)
        var pos1 = dtStr.indexOf(dtCh)
        var pos2 = dtStr.indexOf(dtCh, pos1 + 1)
        var strYear = dtStr.substring(0, pos1)
        var strMonth = dtStr.substring(pos1 + 1, pos2)
        var strDay = dtStr.substring(pos2 + 1)

        strYr = strYear
        if (strDay.charAt(0) == "0" && strDay.length > 1) {
            strDay = strDay.substring(1)
        }
        if (strMonth.charAt(0) == "0" && strMonth.length > 1) {
            strMonth = strMonth.substring(1)
        }
        for (var i = 1; i <= 3; i++) {
            if (strYr.charAt(0) == "0" && strYr.length > 1) {
                strYr = strYr.substring(1)
            }
        }

        month = parseInt(strMonth)
        day = parseInt(strDay)
        year = parseInt(strYr)

        // date format should be : yyyy-mm-dd
        if (pos1 == -1 || pos2 == -1) {
            isValideDate = false;
        }
        //enter a valid month
        if (strMonth.length < 1 || month < 1 || month > 12) {
            isValideDate = false;
        }
        //enter a valid day
        if (strDay.length < 1 || day < 1 || day > 31 || (month == 2 && day > daysInFebruary(year)) || day > daysInMonth[month]) {
            isValideDate = false;
        }
        //enter a valid 4 digit year between " + minYear + " and " + maxYear
        if (strYear.length != 4 || year == 0 || year < minYear || year > maxYear) {
            isValideDate = false;
        }
        //Please enter a valid date
        if (dtStr.indexOf(dtCh, pos2 + 1) != -1 || isInteger(stripCharsInBag(dtStr, dtCh)) == false) {
            isValideDate = false;
        }

        if (!isValideDate) {
            //todo error message
            printMessage(errorText, errorCode);
        }
    }
    return true
}

function isFutureDate(selectDate) {
    var selected = new Date(selectDate);
    var today = new Date();
    return (selected.getTime() > today.getTime()) ? true : false;
}

function customAlert(text) {
    //todo amith
    //find and replace
    var nInfo = document.createElement("div");
    nInfo.className = "custome_alert";
    //    nInfo.innerHTML = text + " <br><input type='submit' value='OK' onclick=exit;/>";
    nInfo.innerHTML = "<h6>Warnings</h6>" + text;
    window.document.body.appendChild(nInfo);
}
