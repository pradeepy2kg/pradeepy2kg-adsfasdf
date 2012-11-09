
// Allow Numbers only.
function numbersOnly(e, decimal) {
    var key;
    var keychar;

    if (window.event)
        key = window.event.keyCode;
    else if (e)
        key = e.which;
    else
        return true;

    keychar = String.fromCharCode(key);
    if ((key == null) || (key == 0) || (key == 8) || (key == 9) || (key == 13) || (key == 27))
        return true;
    else if ((("0123456789").indexOf(keychar) > -1))
        return true;
    else
        return false;
}

function limitLines(obj, e) {
    var keynum;
    var address = getLineNumber(obj);
    if(e.which) {
        keynum = e.which;
    }

    if(keynum == 13) {
        if(address == obj.rows) {
            return false;
        }else{
            address++;
        }
    }
}

function getLineNumber(textarea) {
    return textarea.value.substr(0, textarea.selectionStart).split("\n").length;
}