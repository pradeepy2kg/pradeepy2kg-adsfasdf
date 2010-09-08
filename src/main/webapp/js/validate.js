//check given element is empty
function isEmpty(domElement, errorMessage, errorCode) {
    with (domElement) {
        if (value == null || value.trim() == "") {
            errormsg = errormsg + "\n" + document.getElementById(errorCode).value + " " + errorMessage;
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

function validateSerialNo(domElement, errorText, errorCode) {
    with (domElement) {
        var reg = /^20([1-9][0-9])[0|1]([0-9]{5})$/;
        if (reg.test(value.trim()) == false) {
            printMessage(errorText, errorCode);
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
            var regNIC=/^([0-9]{9}[X|x|V|v])$/;
            if (domElement.value.search(regNIC) == 0) {
                var day = domElement.value.substring(2, 5);
                if ((day >= 367 && day <= 501) || (day >= 867)) {
                    printMessage(errorText, errorCode);
                }
            }
        }
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
                if (((c < "0") || (c > "9"))) return false;
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
                if (bag.indexOf(c) == -1) returnString += c;
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
                if (strDay.charAt(0) == "0" && strDay.length > 1) strDay = strDay.substring(1)
                if (strMonth.charAt(0) == "0" && strMonth.length > 1) strMonth = strMonth.substring(1)
                for (var i = 1; i <= 3; i++) {
                    if (strYr.charAt(0) == "0" && strYr.length > 1) strYr = strYr.substring(1)
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
                    //todo erro massage
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
