/* @author Mahesha Kalpanie */
var textInEnglish = "";
function transliterate(textInOfficialLang, textInEnglishLang) {
    textInEnglish = textInEnglishLang;
    var text = $("textarea#" + textInOfficialLang).attr("value");

    $.post('/ecivil/TransliterationService', {text:text,gender:'M'},
            function(data) {
                if (data != null) {
                    var s = data.translated;
                    $("textarea#" + textInEnglish).val(s);
                }
            });
}

function transliterateTextField(textInOfficialLang, textInEnglishLang) {
    textInEnglish = textInEnglishLang;
    var text = $("input#" + textInOfficialLang).attr("value");

    $.post('/ecivil/TransliterationService', {text:text,gender:'M'},
            function(data) {
                if (data != null) {
                    var s = data.translated;
                    $("input#" + textInEnglish).val(s);
                }
            });
}
