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
        var pinOrNic = value.trim();

        if (pinOrNic.length == 12) {
            if (checkInvalidPIN(pinOrNic, true, false)) {
                printMessage(errorText, errorCode);
            }
        } else if (pinOrNic.length == 10) {
            var regNIC = /^([0-9]{9}[X|x|V|v])$/;

            if (regNIC.test(pinOrNic)) {
                var day = domElement.value.substring(2, 5);
                if ((day >= 367 && day <= 500) || (day >= 867)) {
                    printMessage(errorText, errorCode);
                }
            } else {
                printMessage(errorText, errorCode);
            }
        } else {
            printMessage(errorText, errorCode);
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
        if (pin.length != 12 || checkInvalidPIN(pin, false, false)) {
            printMessage(errorText, errorCode);
        }
    }
}

// validate PIN
function validatePIN(domElement, errorText, errorCode) {
    with (domElement) {
        var pin = value.trim();
        if (pin.length != 12 || checkInvalidPIN(pin, false, true)) {
            printMessage(errorText, errorCode);
        }
    }
}

// used to validate temporary pin and pin
// * both = true for fields enable to enter PIN and Temporary PIN both, * both = false for only PIN or Temporary PIN
// * isPin=true for PIN, * isPin=false for Temporary PIN
// both = true                  - to check both PIN or Temporary PIN invalid
// both = false, isPin = true   - to check PIN invalid
// both = false, isPin = false  - to check Temporary PIN invalid
function checkInvalidPIN(pin, both, isPin) {
    var invalid = false;
    var year = pin.substring(0, 4);
    var date = pin.substring(4, 7);
    var serial = pin.substring(7, 11);
    var check = pin.substring(11, 12);
    var number = pin.substring(0, 11);

    // validate year range
    if (both) {
        if ((year < 1700) || (year > 2200 && year < 6700) || (year > 7200)) {
            return true;
        }
    } else {
        if (isPin) {
            if ((year < 1700) || (year > 2200)) {
                return true;
            }
        } else {
            if ((year < 6700) || (year > 7200)) {
                return true;
            }
        }
    }
    // validate date range
    if ((date >= 367 && date <= 500) || (date >= 867)) {
        return true;
    }
    // validate serial number range
    if (serial < 1 || serial > 9999) {
        return true;
    }
    // validate check digit
    if (check != calculateCheckDigit(number)) {
        return true;
    }
    return false;
}

// calculate check digit of given pin
function calculateCheckDigit(number) {
    var N = new Array(11);
    for (var i = 0; i < N.length; i++) {
        N[i] = number.substring(i, i + 1);
    }

    var check = 11 - ((N[0] * 8 + N[1] * 4 + N[2] * 3 + N[3] * 2 + N[4] * 7 + N[5] * 6 + N[6] * 5 + N[7] * 7 + N[8] * 4 + N[9] * 3 + N[10] * 2) % 11);

    if (check > 9) {
        return check - 10;
    } else {
        return check;
    }
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

function calculateBirthDay(id, error) {
    var regNIC = /^([0-9]{9}[X|x|V|v])$/;
    var regPIN = /^([0-9]{12})$/;
    var day;
    var BirthYear;
    if(id.length == 12){
        day = id.substring(4, 7);
        BirthYear = id.substring(0, 4);
    }else if( id.length == 10){
        day = id.substring(2, 5);
        BirthYear = 19 + id.substring(0, 2);
    }
    var D = new Date(BirthYear);
    if (((id.search(regNIC) == 0) || id.search(regPIN) == 0) && (day >= 501 && day <= 866)) {
        if ((day > 559) && ((D.getFullYear() % 4) != 0 )) {
            day = day - 2;
            D.setDate(D.getDate() + day - 500);
        } else {
            D.setDate(D.getDate() + day - 1500);
        }
        return   new Date(D.getFullYear(), D.getMonth(), D.getDate());

    } else if (((id.search(regNIC) == 0) || id.search(regPIN) == 0) && (day > 0 && day <= 366)) {
        if ((day > 59) && ((D.getFullYear() % 4) != 0 )) {
            day = day - 2;
            D.setDate(D.getDate() + day);
        } else {
            D.setDate(D.getDate() + day - 1000);
        }
        return new Date(D.getFullYear(), D.getMonth(), D.getDate());

    } else if (((id.search(regNIC) == 0) || id.search(regPIN) == 0) && ((day >= 367 && day <= 501)) | (day > 867)) {
        alert(error);
    }
}

function deleteWarning(message) {
    var ret = true;
    ret = confirm(document.getElementById(message).value);
    return ret;
}

// Display popup window to search District and DSDivision of any given GNDivision
function displayGNSearch() {
    var url = 'http://www.life.gov.lk/LIFe/navigate/';
    displayPopup(url);
}

// Display popup by using specified url
function displayPopup(url) {
    var w = 850;
    var h = 750;
    var left = (w / 2 - 150);
    var top = (h / 2 - 300);
    var features = "width=" + w + ",height=" + h + ",top=" + top + ",left=" + left;
    features += ",scrollbars=1,resizable=0,status=0,directories=no,menubar=0,toolbar=1";
    window.open(url, '', features);
}