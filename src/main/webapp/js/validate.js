/*//validate date format to iso date format
 function validateDateFormat() {
 //todo
 alert('called');

 // var dateToValidate = new Date(document.getElementById('birthDatePicker').value);
 var dateToValidate = new Date();

 alert(dateFormat(dateToValidate.format("m/dd/yy")));
 }*/


//check given element is empty
function isEmpty(domElement, errorMessage, errorCode) {
    with (domElement) {
        if (value == null || value == "") {
            errormsg = errormsg + "\n" + document.getElementById(errorCode).value + " " + errorMessage;
        }
    }
}

//check given element is empty and return true if empty else false
function isFieldEmpty(domElement) {
    with (domElement) {
        if (value == null || value == "") {
            return true;
        } else {
            return false;
        }
    }
}

//check given text is numeric value
function isNumeric(text, errorText, message) {
    var validChars = "0123456789.";
    var isNumber = true;
    var characters;
    for (i = 0; i < text.length && isNumber == true; i++) {
        characters = text.charAt(i);
        if (validChars.indexOf(characters) == -1) {
            isNumber = false;
        }
    }
    if (!isNumber) {
        errormsg = errormsg + "\n" + document.getElementById(errorText).value + " : " + document.getElementById(message).value;
    }
}

//valdating email address
function validateEmail(domElement, errorText, errorCode) {
    with (domElement) {
        var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
        if (reg.test(value) == false) {
            errormsg = errormsg + "\n" + document.getElementById(errorText).value + " : " + document.getElementById(errorCode).value;
            return false;
        }
    }
}

// validate PIN or NIC
//function validatePINorNIC(message, errorText, errorCode) {
//    with (message) {
//        // TODO still implementing chathuranga
//        if (value.length != 10) {
//            alert(value.length + 'aaa')
//            errormsg = errormsg + "\n" + document.getElementById(errorText).value + " : " + document.getElementById(errorCode).value;
//            return false;
//        }
//    }
//}

