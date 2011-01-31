/* @author Mahesha Kalpanie */
//TODO: calculate age at person lookup
function personLookup(pin, type) {
    var id1 = $("input#" + pin).attr("value");
    $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
            function(data1) {
                $("textarea#nameOfficial" + type).val(data1.fullNameInOfficialLanguage);
                $("textarea#name_english_male" + type).val(data1.fullNameInOfficialLanguage);
                $("textarea#address" + type).val(data1.lastAddress);
                var personDOB = data1.dateOfBirth;
                if (personDOB != null) {
                    $("input#dateOfBirth" + type).val(personDOB);
                    var personAge = calAgeAtLastBirthday(personDOB);
                    $("input#age" + type).val(personAge);
                }
                $("select#race" + type).val(data1.race);
            });
}

// calculate age at last birth day for given date of birth
function calAgeAtLastBirthday(personDOB) {
    var personAge;
    var today = new Date();
    var dob = new Date(personDOB);
          
    if (today.getMonth() > dob.getMonth()) {
        personAge = today.getYear() - dob.getYear();

    } else if(today.getMonth() == dob.getMonth()) {
        if (today.getDate() >= dob.getDate()) {
            personAge = today.getYear() - dob.getYear();
        } else {
            personAge = (today.getYear() - 1) - dob.getYear();
        }
    }  else {
        personAge = (today.getYear() - 1) - dob.getYear();
    }

    return personAge;
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
                    var maleAge = calAgeAtLastBirthday(maleDOB);
                    $("input#age_at_last_bd_male").val(maleAge);
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
                var femaleDOB = data1.dateOfBirth;
                if (femaleDOB != null) {
                    $("input#date_of_birth_female").val(femaleDOB);
                    var femaleAge = calAgeAtLastBirthday(femaleDOB);
                    $("input#age_at_last_bd_female").val(femaleAge);
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