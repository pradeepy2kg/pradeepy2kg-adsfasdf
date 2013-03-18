/**
 * @author Duminda Dharmakeerthi
 *
 * To detect text input fields of the form.
 * Will use jQuery methods. Therefore, jQuery library should be available for use.
 */

/**
 * Return the IDs of the text fields and text areas as an array.
 * @param formId ID of the web form
 */
function getActiveTextFields(formId) {
    $('#' + formId + ' :input').each(function(index, element) {
        var fieldValue = element.value;
        var ok = true;
        if (fieldValue.length > 0) {
            if(!checkSyntax(element.id, fieldValue)){
                ok = false;
            }
        }
        alert("Status: "+ ok);
        return ok;
    });
}

/**
 * Syntax Checker was originally written by Shanmugarajah Sinnathamby.
 * Modified to use in the ePopulation Registry and Converted to JavaScript by Duminda Dharmakeerthi.
 */

/**
 * Check the input TEXT for syntax errors. (in Sinhala and Tamil)
 * This function will look for syntax errors in Sinhala and Tamil text and provide an option to the user to correct.
 * It will suggest the corrected text.
 *
 * @param id    ID of the element
 * @param text  Content of the element.
 */
function checkSyntax(id, text) {
    var curChar;
    var charCode;
    var output = "";
    var arrChars;
    var prevConsonant = 0;
    var outputDone;
    var prevCharCode = 0;
    var nextChar;
    var nextCharCode;
    var prevprevChar;
    var prevprevCharCode;
    var nextnextChar;
    var nextnextCharCode;

    text = text.replace("අා", "ආ");
    text = text.replace("අැ", "ඇ");
    text = text.replace("අෑ", "ඈ");
    text = text.replace("උෟ", "ඌ");
    text = text.replace("එ්", "ඒ");
    text = text.replace("ෙඑ", "ඓ");
    text = text.replace("ඔ්", "ඕ");
    text = text.replace("ෙඑ", "ඓ");
    text = text.replace("ඔෟ", "ඖ");
    text = text.replace("ේ", "ේ");
    text = text.replace("ො", "ො");
    text = text.replace("ෝ", "ෝ");
    text = text.replace("ෝ", "ෝ");
    text = text.replace("ෞ", "ෞ");
    text = text.replace("ொ", "ொ");
    text = text.replace("ோ", " ோ");
    text = text.replace("ௌ", "ௌ");
    text = text.replace("ා්", "ෳ");

    arrChars = text.split('');
    for (i = 0; i < arrChars.length; i++) {
        outputDone = 0;
        curChar = arrChars[i];
        charCode = curChar.charCodeAt(0);

        if (charCode == 8204) {
            var isKsha = 0;
            // check for ksha kuththu character
            if (prevCharCode == 3021) {
                // check for next char as sha
                if (i + 1 < arrChars.length) {
                    nextChar = arrChars[i + 1];
                    nextCharCode = nextChar.charCodeAt(0);
                    // next char is a sha
                    if (nextCharCode == 2999) {
                        isKsha = 1;
                    }
                }
            }
            if (isKsha == 0) {
                // output = output + '<' + curChar + '>';
                outputDone = 1;
            }
            prevConsonant = 0;
        } else if (charCode == 8205) {
            // check for tamil ksha kuththu character
            var isHanging = 0;
            if (prevCharCode == 3021) {
                // check for next char as sha
                if (i + 1 < arrChars.length) {
                    nextChar = arrChars[i + 1];
                    nextCharCode = nextChar.charCodeAt(0);
                    // next char is a sha
                    if (nextCharCode == 2999) {
                        isHanging = 1;
                    }
                }
            }
            // check for bandhi and rakar and yanse and reph
            else if (prevCharCode == 3530) {
                // check for next char as sha
                if (i + 1 < arrChars.length) {
                    nextChar = arrChars[i + 1];
                    nextCharCode = nextChar.charCodeAt(0);
                    // next char is a sinhala consonant then allow
                    if (nextCharCode > 3481 & nextCharCode < 3528) {
                        if (i - 2 > -1) {
                            prevprevChar = arrChars[i - 2];
                            prevprevCharCode = prevprevChar.charCodeAt(0);
                            // prev prev char is a sinhala consonant then allow
                            if (prevprevCharCode > 3481 & prevprevCharCode < 3528) {
                                isHanging = 1;
                            }
                        }
                    }
                }
                prevConsonant = 0;
            }
            // check for touch character
            else if (prevCharCode > 3481 & prevCharCode < 3528) {
                // check for next char as sha
                if (i + 1 < arrChars.length) {
                    nextChar = arrChars[i + 1];
                    nextCharCode = nextChar.charCodeAt(0);
                    // next char is a sinhala alkirima then allow
                    if (nextCharCode == 3530) {
                        if (i + 2 < arrChars.length) {
                            nextChar = arrChars[i + 2];
                            nextCharCode = nextChar.charCodeAt(0);
                            // next char is a sinhala consonant then allow
                            if (nextCharCode > 3481 & nextCharCode < 3528) {
                                isHanging = 1;
                            }
                        }
                    }
                }
            }

            if (isHanging == 0) {
                // output = output + '<' + curChar + '>';
                outputDone = 1;
            }
            prevConsonant = 0;
        } else if (charCode > 3529 & charCode < 3572) {
            // if sinhala vowel modifier not followed by sinhala consonant

            if (prevConsonant != 1) {
                // if sinhala kombu is accompanying a consonant with al,o modifier or o al modifier or au modifier
                if (charCode == 3545) {
                    if (i + 1 < arrChars.length) {
                        nextChar = arrChars[i + 1];
                        nextCharCode = nextChar.charCodeAt(0);
                        // next char is a sinhala consonant then allow
                        if (nextCharCode > 3481 & nextCharCode < 3528) {
                            if (i + 2 < arrChars.length) {
                                nextnextChar = arrChars[i + 2];
                                nextnextCharCode = nextnextChar.charCodeAt(0);

                                if (nextnextCharCode == 3530) {
                                    output = output + nextChar + "ේ";
                                    outputDone = 1;
                                    i = i + 2;
                                } else if (nextnextCharCode == 3535) {
                                    output = output + nextChar + "ො";
                                    outputDone = 1;
                                    i = i + 2;
                                } else if (nextnextCharCode == 3551) {
                                    output = output + nextChar + "ෞ";
                                    outputDone = 1;
                                    i = i + 2;
                                } else if (nextnextCharCode == 3571) {
                                    output = output + nextChar + "ෝ";
                                    outputDone = 1;
                                    i = i + 2;
                                } else {
                                    output = output + nextChar + curChar;
                                    outputDone = 1;
                                    i = i + 1;
                                }
                            } else {
                                // exchange the consonant and kombu
                                output = output + nextChar + curChar;
                                outputDone = 1;
                                i = i + 1;
                            }
                        }
                    }
                }

                if (charCode == 3571) {
                    // output = output + "<ා><්>";
                    outputDone = 1;
                } else if (outputDone != 1) {
                    // output = output + '<' + curChar + '>';
                    outputDone = 1;
                }
            }
            prevConsonant = 0;
        } else if (charCode > 3006 & charCode < 3045) {
            // if tamil vowel modifier not followed by tamil consonant
            if (prevConsonant != 2) {
                if (charCode == 3014) {
                    if (i + 1 < arrChars.length) {
                        nextChar = arrChars[i + 1];
                        nextCharCode = nextChar.charCodeAt(0);
                        // next char is a Tamil consonant then allow
                        if (nextCharCode > 2964 & nextCharCode < 3006) {
                            if (i + 2 < arrChars.length) {
                                nextnextChar = arrChars[i + 2];
                                nextnextCharCode = nextnextChar.charCodeAt(0);

                                if (nextnextCharCode == 3006) {
                                    output = output + nextChar + "ொ";
                                    outputDone = 1;
                                    i = i + 2;
                                } else if (nextnextCharCode == 2995) {
                                    output = output + nextChar + "ௌ";
                                    outputDone = 1;
                                    i = i + 2;
                                } else {
                                    output = output + nextChar + curChar;
                                    outputDone = 1;
                                    i = i + 1;
                                }
                            } else {
                                // exchange the consonant and kombu
                                output = output + nextChar + curChar;
                                outputDone = 1;
                                i = i + 1;
                            }
                        }
                    }
                } else if (charCode == 3015) {
                    if (i + 1 < arrChars.length) {
                        nextChar = arrChars[i + 1];
                        nextCharCode = nextChar.charCodeAt(0);
                        // next char is a Tamil consonant then allow
                        if (nextCharCode > 2964 & nextCharCode < 3006) {
                            if (i + 2 < arrChars.length) {
                                nextnextChar = arrChars[i + 2];
                                nextnextCharCode = nextnextChar.charCodeAt(0);

                                if (nextnextCharCode == 3006) {
                                    output = output + nextChar + "ோ";
                                    outputDone = 1;
                                    i = i + 2;
                                } else {
                                    output = output + nextChar + curChar;
                                    outputDone = 1;
                                    i = i + 1;
                                }
                            } else {
                                // exchange the consonant and kombu
                                output = output + nextChar + curChar;
                                outputDone = 1;
                                i = i + 1;
                            }
                        }
                    }
                }
                if (outputDone != 1) {
                    // output = output + '<' + curChar + '>';
                    outputDone = 1;
                }
            }
            prevConsonant = 0;
        } else if (charCode > 3481 & charCode < 3528) {
            // Sinhala vowels
            prevConsonant = 1;
        } else if (charCode > 2964 & charCode < 3002) {
            // Tamil vowels
            prevConsonant = 2;
        }

        prevCharCode = charCode;
        if (outputDone == 0) {
            output = output + curChar;
        }
    }

    text = text.replace("ෳ", "");
    output = output.replace("<<", "<");
    output = output.replace(">>", ">");

    if (text.length != output.length) {
        var check = confirm($('#syntaxError').val() + "\n" + text + "\n" + $('#correctSyntaxError').val() + "\n " + output);
        if (check) {
            $('#' + id).val(output);
            return true;
        } else {
            return false;
        }
    }
}