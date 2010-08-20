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
//function isEmpty(domElement) {
//    with (domElement) {
//        if (value == null || value == "") {
//            return true;
//        } else {
//            return false;
//        }
//    }
//}


//check given text is numeric value
function isNumeric(text, message) {
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
        errormsg = errormsg + "\n" + document.getElementById('error13').value + " : " + document.getElementById(message).value;
    }
}


//valdating email address
function validateEmail(domElement, errorCode) {
    with (domElement) {
        var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
        if (reg.test(value) == false) {
            errormsg = errormsg + "\n" + document.getElementById('error2').value + " : " + document.getElementById(errorCode).value;
            return false;
        }
    }
}