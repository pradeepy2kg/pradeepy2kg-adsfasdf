/* @author Mahesha Kalpanie */
//TODO: calculate age at person lookup
function personLookup(pin, type) {
    var id1 = $("input#" + pin).attr("value");
    $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
            function(data1) {
                $("textarea#nameOfficial" + type).val(data1.fullNameInOfficialLanguage);
                name_english_male
                $("textarea#name_english_male" + type).val(data1.fullNameInOfficialLanguage);
                $("textarea#address" + type).val(data1.lastAddress);
                var maleDOB = data1.dateOfBirth;
                if (maleDOB != null) {
                    $("input#dateOfBirth" + type).val(maleDOB);
                }
                $("select#race" + type).val(data1.race);
            });
}


function malePersonLookUp(pin) {
    var id1 = $("input#" + pin).attr("value");
    $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
            function(data1) {
                $("textarea#name_official_male").val(data1.fullNameInOfficialLanguage);
                $("textarea#name_english_male").val(data1.fullNameInEnglishLanguage);
                $("textarea#address_male_official").val(data1.lastAddress);
                var maleDOB = data1.dateOfBirth;
                if (maleDOB != null) {
                    $("input#date_of_birth_male").val(maleDOB);
                }
                $("select#raceMaleId").val(data1.race);
            });
}


function femalePersonLookUp(pin) {
    var id1 = $("input#" + pin).attr("value");
    $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
            function(data1) {
                $("textarea#name_official_female").val(data1.fullNameInOfficialLanguage);
                $("textarea#name_english_female").val(data1.fullNameInEnglishLanguage);
                $("textarea#address_female_official").val(data1.lastAddress);
                var maleDOB = data1.dateOfBirth;
                if (maleDOB != null) {
                    $("input#date_of_birth_female").val(maleDOB);
                }
                $("select#raceFemaleId").val(data1.race);
            });
}

function malePartyFather(pin) {
    var id1 = $("input#" + pin).attr("value");
    $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
            function(data1) {
                $("textarea#father_full_name_male_official").val(data1.fullNameInOfficialLanguage);
                $("textarea#father_full_name_male_english").val(data1.fullNameInEnglishLanguage);
            });
}

function femalePartyFather(pin) {
    var id1 = $("input#" + pin).attr("value");
    $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
            function(data1) {
                $("textarea#father_full_name_female_official").val(data1.fullNameInOfficialLanguage);
                $("textarea#father_full_name_female_english").val(data1.fullNameInEnglishLanguage);
            });
}