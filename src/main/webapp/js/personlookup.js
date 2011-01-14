/* @author Mahesha Kalpanie */
function personLookup(pin, type) {
        var id1 = $("input#" + pin).attr("value");
        $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                function(data1) {
                    $("textarea#nameOfficial" + type).val(data1.fullNameInOfficialLanguage);
                    $("textarea#address" + type).val(data1.lastAddress);
                    var maleDOB = data1.dateOfBirth;
                    if (maleDOB != null) {
                        $("input#dateOfBirth" + type).val(maleDOB);
                    }
                });
}

