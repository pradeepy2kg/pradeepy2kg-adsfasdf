function translate(text, gender) {
    /*$.getJSON('/TransliterationService', {text:text,gender:gender},
            function(data) {
                alert(data.translated)
                return data.translated;
            });*/

    $.post('/TransliterationService', {text:text,gender:gender},
            function(data) {
                alert(data.translated)
                return data.translated;
            });

}
