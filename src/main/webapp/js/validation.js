/**@author Indunil Moremada
 * javascript to validate the empty fields of the birth registration form
 */
function birthRegistrationValidator() {
    var serial = document.getElementById("serialNumber");
    var childBirthWeight = document.getElementById("childBirthWeight");
    var msg = "following fields are required ! ! !\n";
    if (serial.value == "" || childBirthWeight.value == 0.0) {
        if (serial.value == "")
            msg += "\nSerial Number";
        if (childBirthWeight.value == 0.0)
            msg += "\nChild's Birth Weight";
        alert(msg);
        /*if(noOfLiveChildren.value==0)
         msg+="\nNo Live Births";
         if( noOfMultipleBirths.value==0)
         msg+="\nNo of Multiple Births"*/
        return false;
    }
    else {
        return true;
    }
}

function ageValidator() {
    var age = document.getElementById("motherAgeAtBirth");
    if (age.value == 0) {
        alert("Mother's age is required ! ! !");
        return false;
    }
    else
        return true;
}