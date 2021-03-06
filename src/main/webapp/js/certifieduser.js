/* @author Mahesha Kalpanie  */
function populateCertifiedUserList() {
    //TODO: to be generalized
    var id = $("select#locationId").attr('value');
    var options = '';
    if (id > 0) {
        $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:4,certificateId:0},
            function(data) {
                var users = data.authorizedUsers;
                for (var i = 0; i < users.length; i++) {
                    options += '<option value="' + users[i].optionValue + '">' + users[i].optionDisplay + '</option>';
                }
                $("select#issueUserId").html(options);

                var id = $('select#locationId').attr('value');
                var user = $('select#issueUserId').attr('value');
                var certId = $('label#serialNumber').text();
                //set user signature for given user
                $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:10,userId:user
                ,certificateId:certId,type:'marriage'},
                    function(data) {
                        var officerSign = data.officerSignature;
                        var locationSign = data.locationSignature;
                        var location = data.locationName;

                        $("label#signature").html(officerSign);
                        $("label#placeOfIssue").html(locationSign);
                        $("label#placeName").html(location);
                    });
                });

    } else {
        $("select#issueUserId").html(options);
    }
}

function printCertifiedUser() {
    var id = $('select#locationId').attr('value');
    var user = $('select#issueUserId').attr('value');
    var certId = $('label#serialNumber').text();
    $.getJSON('/ecivil/crs/CertSignUserLookupService', {userLocationId:id,mode:10,userId:user,certificateId:certId,
    type:'marriage'},
        function(data) {
            var officerSign = data.officerSignature;
            var locationSign = data.locationSignature;
            var location = data.locationName;

            $("label#signature").html(officerSign);
            $("label#placeOfIssue").html(locationSign);
            $("label#placeName").html(location);
        });
}

