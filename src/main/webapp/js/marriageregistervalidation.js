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