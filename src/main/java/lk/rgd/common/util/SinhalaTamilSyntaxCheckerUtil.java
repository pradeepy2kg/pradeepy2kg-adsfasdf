package lk.rgd.common.util;

/**
 * Syntax Checker was originally written by Shanmugarajah Sinnathamby.
 * Converted to Java by Duminda Dharmakeerthi.
 */
public class SinhalaTamilSyntaxCheckerUtil {

    public static String checkSyntax(String content) {
        char curChar;
        int charCode;
        String output = "";
        char[] arrChars;
        int prevConsonant = 0;
        int outputDone;
        int prevCharCode = 0;
        char nextChar;
        int nextCharCode;
        char prevprevChar;
        int prevprevCharCode;
        char nextnextChar;
        int nextnextCharCode;

        // Mistyped characters

        content = content.replace("අා", "ආ");
        content = content.replace("අැ", "ඇ");
        content = content.replace("අෑ", "ඈ");
        content = content.replace("උෟ", "ඌ");
        content = content.replace("එ්", "ඒ");
        content = content.replace("ෙඑ", "ඓ");
        content = content.replace("ඔ්", "ඕ");
        content = content.replace("ෙඑ", "ඓ");
        content = content.replace("ඔෟ", "ඖ");
        content = content.replace("ේ", "ේ");
        content = content.replace("ො", "ො");
        content = content.replace("ෝ", "ෝ");
        content = content.replace("ෝ", "ෝ");
        content = content.replace("ෞ", "ෞ");
        content = content.replace("ொ", "ொ");
        content = content.replace("ோ", " ோ");
        content = content.replace("ௌ", "ௌ");
        content = content.replace("ා්", "ෳ");

        // Check if any previously checked data
        content = content.replace("<", "");
        content = content.replace(">", "");

        arrChars = content.toCharArray();

        for (int i = 0; i < arrChars.length; i++) {
            outputDone = 0;
            curChar = arrChars[i];
            charCode = (int) curChar;

            if (charCode == 8204) {
                int isKsha = 0;
                // check for ksha kuththu character
                if (prevCharCode == 3021) {
                    // check for next char as sha
                    if (i + 1 < arrChars.length) {
                        nextChar = arrChars[i + 1];
                        nextCharCode = (int) nextChar;
                        // next char is a sha
                        if (nextCharCode == 2999)
                            isKsha = 1;
                    }
                }
                if (isKsha == 0) {
                    //output = output + '<' + Character.toString(curChar) + '>';
                    outputDone = 1;
                }
                prevConsonant = 0;
            } else if (charCode == 8205) {
                // check for tamil ksha kuththu character
                int isHanging = 0;
                if (prevCharCode == 3021) {
                    // check for next char as sha
                    if (i + 1 < arrChars.length) {
                        nextChar = arrChars[i + 1];
                        nextCharCode = (int) nextChar;
                        // next char is a sha
                        if (nextCharCode == 2999)
                            isHanging = 1;
                    }
                }
                // check for bandhi and rakar and yanse and reph
                else if (prevCharCode == 3530) {
                    // check for next char as sha
                    if (i + 1 < arrChars.length) {
                        nextChar = arrChars[i + 1];
                        nextCharCode = (int) nextChar;
                        // next char is a sinhala consonant then allow
                        if (nextCharCode > 3481 & nextCharCode < 3528) {
                            if (i - 2 > -1) {
                                prevprevChar = arrChars[i - 2];
                                prevprevCharCode = (int) prevprevChar;
                                // prev prev char is a sinhala consonant then allow
                                if (prevprevCharCode > 3481 & prevprevCharCode < 3528)
                                    isHanging = 1;
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
                        nextCharCode = (int) nextChar;
                        // next char is a sinhala alkirima then allow
                        if (nextCharCode == 3530) {
                            if (i + 2 < arrChars.length) {
                                nextChar = arrChars[i + 2];
                                nextCharCode = (int) nextChar;
                                // next char is a sinhala consonant then allow
                                if (nextCharCode > 3481 & nextCharCode < 3528)
                                    isHanging = 1;
                            }
                        }
                    }
                }

                if (isHanging == 0) {
                    //output = output + '<' + Character.toString(curChar) + '>';
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
                            nextCharCode = (int) nextChar;
                            // next char is a sinhala consonant then allow
                            if (nextCharCode > 3481 & nextCharCode < 3528) {
                                if (i + 2 < arrChars.length) {
                                    nextnextChar = arrChars[i + 2];
                                    nextnextCharCode = (int) nextnextChar;

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
                                        output = output + nextChar + Character.toString(curChar);
                                        outputDone = 1;
                                        i = i + 1;
                                    }
                                } else {
                                    // exchange the consonant and kombu
                                    output = output + nextChar + Character.toString(curChar);
                                    outputDone = 1;
                                    i = i + 1;
                                }
                            }
                        }
                    }

                    if (charCode == 3571) {
                        //output = output + "<ා><්>";
                        outputDone = 1;
                    } else if (outputDone != 1) {
                        //output = output + '<' + Character.toString(curChar) + '>';
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
                            nextCharCode = (int) nextChar;
                            // next char is a Tamil consonant then allow
                            if (nextCharCode > 2964 & nextCharCode < 3006) {
                                if (i + 2 < arrChars.length) {
                                    nextnextChar = arrChars[i + 2];
                                    nextnextCharCode = (int) nextnextChar;

                                    if (nextnextCharCode == 3006) {
                                        output = output + nextChar + "ொ";
                                        outputDone = 1;
                                        i = i + 2;
                                    } else if (nextnextCharCode == 2995) {
                                        output = output + nextChar + "ௌ";
                                        outputDone = 1;
                                        i = i + 2;
                                    } else {
                                        output = output + nextChar + Character.toString(curChar);
                                        outputDone = 1;
                                        i = i + 1;
                                    }
                                } else {
                                    // exchange the consonant and kombu
                                    output = output + nextChar + Character.toString(curChar);
                                    outputDone = 1;
                                    i = i + 1;
                                }
                            }
                        }
                    } else if (charCode == 3015) {
                        if (i + 1 < arrChars.length) {
                            nextChar = arrChars[i + 1];
                            nextCharCode = (int) nextChar;
                            // next char is a Tamil consonant then allow
                            if (nextCharCode > 2964 & nextCharCode < 3006) {
                                if (i + 2 < arrChars.length) {
                                    nextnextChar = arrChars[i + 2];
                                    nextnextCharCode = (int) nextnextChar;

                                    if (nextnextCharCode == 3006) {
                                        output = output + nextChar + "ோ";
                                        outputDone = 1;
                                        i = i + 2;
                                    } else {
                                        output = output + nextChar + Character.toString(curChar);
                                        outputDone = 1;
                                        i = i + 1;
                                    }
                                } else {
                                    // exchange the consonant and kombu
                                    output = output + nextChar + Character.toString(curChar);
                                    outputDone = 1;
                                    i = i + 1;
                                }
                            }
                        }
                    }
                    if (outputDone != 1) {
                        //output = output + '<' + Character.toString(curChar) + '>';
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
            if (outputDone == 0)
                output = output + Character.toString(curChar);
        }
        content = content.replace("ෳ", "");
        output = output.replace("<<", "<");
        output = output.replace(">>", ">");
        return output;
    }
}