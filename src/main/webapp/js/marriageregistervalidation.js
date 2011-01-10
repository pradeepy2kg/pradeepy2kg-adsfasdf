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

function validatePin(fieldId, messageId, errormsg) {
    var domObject = document.getElementById(fieldId);
    if (!isFieldEmpty(domObject)) {
        //validatePINNumber(domObject, messageId, errormsg);
        if (isValidPIN(domObject)) {
            return errormsg;
        }
    }
    return printValidationMessage(messageId, errormsg);

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

// shan

function isDate(fieldId, messageId, errormsg) {
    var domObject = document.getElementById(fieldId);
    if (isFieldEmpty(domObject)) {
        return printValidationMessage(messageId, errormsg);
    } else {

        var txtDate = document.getElementById(fieldId).value;

        var day,      // day
                month,    // month
                year;     // year

        if (txtDate.length !== 10) {
            return printValidationMessage(messageId, errormsg);
        }

        if (txtDate.substring(4, 5) !== '-' || txtDate.substring(7, 8) !== '-') {
            return printValidationMessage(messageId, errormsg);
        }

        month = txtDate.substring(5, 7) - 1; // because months in JS start from 0
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
