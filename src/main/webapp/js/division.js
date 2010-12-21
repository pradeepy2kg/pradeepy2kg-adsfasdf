/* @author Mahesha Kalpanie */
function populateDSDivisions(districtId, dsDivisionId, divisionId, divisionType, isAllOption){
    var mode = getModeForDSList(divisionType);
    var id = document.getElementById(districtId).value
    $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:mode, withAll:isAllOption},
    function(data) {
        var ds = data.dsDivisionList;
        var dsDivisionList = document.getElementById(dsDivisionId);
        clear_list(dsDivisionList);
        for (var i = 0; i < ds.length; i++) {
            //if(isAllOption){
               dsDivisionList.options[i]= new Option(ds[i].optionDisplay, ds[i].optionValue);
            //} else {
              // dsDivisionList.options[i]= new Option(ds[i+1].optionDisplay, ds[i+1].optionValue);
            //}
        }
        var divisions = data.divisionList;
        var divisionList = document.getElementById(divisionId);
        clear_list(divisionList);
        for (var j = 0; j < divisions.length; j++) {
            //if(isAllOption){
                divisionList.options[j]= new Option(divisions[j].optionDisplay, divisions[j].optionValue);
            //} else {
               // divisionList.options[j]= new Option(divisions[j+1].optionDisplay, divisions[j+1].optionValue);
            //}
        }
    });
}

function populateDivisions(dsDivisionId, divisionId, divisionType, isAllOption){
    var mode = getModeForDivisionList(divisionType);
    var id = document.getElementById(dsDivisionId).value
    $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:mode, withAll:isAllOption},
        function(data) {
            var divisions = data.divisionList;
            var divisionList = document.getElementById(divisionId);
            clear_list(divisionList);
            for (var i = 0; i < divisions.length; i++) {
                //if(isAllOption){
                   divisionList.options[i]= new Option(divisions[i].optionDisplay, divisions[i].optionValue);
               // } else {
               //    divisionList.options[i]= new Option(divisions[i+1].optionDisplay, divisions[i+1].optionValue);
               // }

            }
        });
}

function clear_list(list) {
    while( list.hasChildNodes() ) {
        list.removeChild(list.lastChild);
    }
}

function getModeForDSList(type){
    if (type == "Birth") {
        return 11;
    } else if (type == "Marriage") {
        return 12;
    }else {
        return 0;
    }
}

function getModeForDivisionList(type){
    if (type == "Birth") {
        return 9;
    } else if (type == "Marriage") {
        return 10;
    }else {
        return 0;
    }
}

