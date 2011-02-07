/* @author Mahesha Kalpanie */
function validateEmptyField(fieldId, messageId, errormsg) {
    var domObject = document.getElementById(fieldId);
    if (isFieldEmpty(domObject)) {
        return printValidationMessage(messageId, errormsg);
    } else {
        return errormsg;
    }
}

function printValidationMessage(messageId, errormsg) {
    return errormsg + "\n" + document.getElementById(messageId).value;
}

function printErrorMessages(errormsg) {
    if (errormsg != "") {
        alert(errormsg);
        errormsg = "";
        return false;
    }
    errormsg = "";
    return true;
}

function validateRadio(obj, messageId, errormsg) {
    alert(obj.length);
    for (var i = 0; i < obj.length; i++) {
        if (obj[i].checked) {
            return errormsg;
        }
    }
    return printValidationMessage(messageId, errormsg);
}

function validateRadioSet(fieldName, messageId, errormsg) {
    for (var i = 0; i < fieldName.length; i++) {
        if (document.getElementsByName(fieldName)[i].checked) {
            return errormsg;
        }
    }
    return printValidationMessage(messageId, errormsg);
}

function validatePin(fieldId, messageId, invalidmsg, errormsg) {
    var domObject = document.getElementById(fieldId);

    if (isFieldEmpty(domObject)) {
        return printValidationMessage(messageId, errormsg);
    }
    //if (!isFieldEmpty(domObject)) {
    //validatePINNumber(domObject, messageId, errormsg);
    if (isValidPIN(domObject)) {
        return errormsg;
    }
    // }
    return printValidationMessage(invalidmsg, errormsg);

}

function validateIdUkey(fieldId, messageId, invalidmsg, errormsg) {
    var domObject = document.getElementById(fieldId);

    if (isFieldEmpty(domObject)) {
        return printValidationMessage(messageId, errormsg);
    }
    if (!isNumeric(domObject.value)) {
        return printValidationMessage(invalidmsg, errormsg);
    }
    return errormsg;
}

//  check for valid numeric strings
function isNumeric(strString) {
    var strValidChars = "123456789";
    var strChar;
    var blnResult = true;

    if (strString.length == 0) {
        return false;
    }

    //  test strString consists of valid characters listed above
    for (i = 0; i < strString.length && blnResult == true; i++) {
        strChar = strString.charAt(i);
        if (strValidChars.indexOf(strChar) == -1) {
            blnResult = false;
        }
    }
    return blnResult;
}


function validateSerialNo(fieldId, emptymsg, invalidmsg, errormsg) {
    var domObject = document.getElementById(fieldId);

    if (isFieldEmpty(domObject)) {
        return printValidationMessage(emptymsg, errormsg);
    }
    var reg = /^20([1-9][0-9])[0|1]([0-9]{5})$/;
    var notbe = /^20([1-9][0-9])[0|1]([0]{5})$/;

    if (notbe.test(domObject.value.trim()) == true) {
        return printValidationMessage(invalidmsg, errormsg);
    } else {
        if (reg.test(domObject.value.trim()) == false) {
            return printValidationMessage(invalidmsg, errormsg);
        }
    }
    return errormsg;
}

function isValidPIN(domElement) {
    with (domElement) {
        var reg = /^(([0-9]{10})|([0-9]{9}[X|x|V|v]))$/;
        if (reg.test(value.trim()) == false) {
            return false;
        } else {
            var regNIC = /^([0-9]{9}[X|x|V|v])$/;
            if (domElement.value.search(regNIC) == 0) {
                var day = domElement.value.substring(2, 5);
                if ((day >= 367 && day <= 500) || (day >= 867)) {
                    return false;
                }
            }
        }
    }
    return true;
}

function isFieldEmpty(domElement) {
    with (domElement) {
        return (value == null || value.trim() == "") ? true : false;
    }
}

function validateSelectOption(fieldId, messageId, errormsg) {
    var domObject = document.getElementById(fieldId);
    if (domObject.value == 0) {
        return printValidationMessage(messageId, errormsg);
    } else {
        return errormsg;
    }
}

function calculateAge(birthDay, age) {
    var dateOdBirthSubmitted = true;
    var person_bd = new Date();
    var dom = null;
    dom = document.getElementById(birthDay);
    person_bd = new Date(document.getElementById(birthDay).value);
    if (isFieldEmpty(dom)) {
        dateOdBirthSubmitted = false;
    }

    var today = new Date();
    var person_age = today.getYear() - person_bd.getYear();
    if (!(dateOdBirthSubmitted)) {
        document.getElementById(age).value = 0;
    }
    else {
        document.getElementById(age).value = person_age;
    }
}

function isFutureDate(selectDate) {
    var selected = new Date(selectDate);
    var today = new Date();
    return (selected.getTime() > today.getTime()) ? true : false;
}

// shan
function isDate(fieldId, emptymsg, messageId, errormsg) {
    var domObject = document.getElementById(fieldId);
    if (isFieldEmpty(domObject)) {
        return printValidationMessage(emptymsg, errormsg);
    } else {
        var txtDate = document.getElementById(fieldId).value;

        if (isFutureDate(txtDate)) {
            return printValidationMessage(messageId, errormsg);
        } else {

            var day,      // day
                    month,    // month
                    year;     // year

            if (txtDate.length !== 10) {
                return printValidationMessage(messageId, errormsg);
            }

            if (txtDate.substring(4, 5) !== '-' || txtDate.substring(7, 8) !== '-') {
                return printValidationMessage(messageId, errormsg);
            }

            month = txtDate.substring(5, 7);
            day = txtDate.substring(8, 10) - 0;
            year = txtDate.substring(0, 4) - 0;

            if (year < 1000 || year > 3000) {
                return printValidationMessage(messageId, errormsg);
            }

            if (month > 0 || month < 13) {
                if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    if (day < 0 || day > 31) {
                        return printValidationMessage(messageId, errormsg);
                    }
                } else {
                    if (month == 2) {
                        if (day < 0 || day > 29) {
                            return printValidationMessage(messageId, errormsg);
                        }
                    } else {
                        if (day < 0 || day > 30) {
                            return printValidationMessage(messageId, errormsg);
                        }
                    }
                }
            } else {
                return printValidationMessage(messageId, errormsg);
            }

            return errormsg;
        }
    }
}

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

//add X or V at the end of the NIC
//todo : to be modified
function addXorV(domElement, letter, error) {
    var reg = /^([0-9]{9})$/;
    if ((document.getElementById(domElement).value.length == 9) && isInteger((document.getElementById(domElement).value))) {
        document.getElementById(domElement).value = document.getElementById(domElement).value + letter;
    } else {
        alert(document.getElementById(error).value);
    }
}
