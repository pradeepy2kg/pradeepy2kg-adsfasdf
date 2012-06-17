/* @author Mahesha Kalpanie */
function registrarLookup(pin){
    var id1 = $("input#" + pin).attr("value");
    $.getJSON('/ecivil/crs/RegistrarLookupService', {pinOrNic:id1},
            function(data1) {
                $("textarea#regNameInOfficialLang").val(data1.fullNameInOfficialLanguage);
                $("textarea#regNameInEnglishLang").val(data1.fullNameInEnglishLanguage);
                $("textarea#regPlaceInOfficialLang").val(data1.address);
            });
}