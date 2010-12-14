/* @author Mahesha Kalpanie */
function populateDSDivisions(districtId, dsDivisionId, divisionId, divisionType){
    var mode = getModeForDSList(divisionType);
    var id = document.getElementById(districtId).value
    $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:mode},
    function(data) {
        var ds = data.dsDivisionList;
        var dsDivisionList = document.getElementById(dsDivisionId);
        clear_list(dsDivisionList);
        for (var i = 0; i < ds.length; i++) {
            dsDivisionList.options[i]= new Option(ds[i].optionDisplay, ds[i].optionValue);
        }
        var divisions = data.bdDivisionList;
        var divisionList = document.getElementById(divisionId);
        clear_list(divisionList);
        for (var j = 0; j < divisions.length; j++) {
            divisionList.options[j]= new Option(divisions[j].optionDisplay, divisions[j].optionValue);
        }
    });
}

function populateDivisions(dsDivisionId, divisionId, divisionType){
    var mode = getModeForDivisionList(divisionType);
    var id = document.getElementById(dsDivisionId).value
    $.getJSON('/ecivil/crs/DivisionLookupService', {id:id, mode:mode},
        function(data) {
            var divisions = data.divisionList;
            var divisionList = document.getElementById(divisionId);
            clear_list(divisionList);
            for (var i = 0; i < divisions.length; i++) {
                divisionList.options[i]= new Option(divisions[i].optionDisplay, divisions[i].optionValue);
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
        return 5;
    } else if (type == "Marriage") {
        return 6;
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

