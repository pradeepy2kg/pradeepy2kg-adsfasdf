/**
 * @author Indunil Moremada
 * javascript for changing the datepicker dynamically with the changes done for the list boxes year,month and date
 *  @param id is used to identified the field in the jsp
 * @param flag is used to identify whether the request is coming from if flag is 1 request is from page 1 of 4
 * if flag is 2 request is from father's dob else it is from mother's dob field
 */
function setDate(id, flag) {
    var year;
    var Selectedmonth;
    var day;
    var selectedDate;
    var sel;
    var datePicker = dojo.widget.byId('datePicker');
    var fatherDatePicker = dojo.widget.byId('fatherDatePicker');
    var motherDatePicker = dojo.widget.byId('motherdatePicker');
    if (flag == 1) {
        if (id == "year") {
            sel = document.getElementById(id);
            year = sel.options[sel.selectedIndex].value;
            Selectedmonth = ePopGetValue('month');
            day = ePopGetValue('day');
            selectedDate = year + "-" + Selectedmonth + "-" + day;
            datePicker.setValue(selectedDate);
        }
        else if (id == "month") {
            sel = document.getElementById(id);
            Selectedmonth = sel.options[sel.selectedIndex].value;
            day = ePopGetValue('day');
            year = ePopGetValue('year');
            selectedDate = year + "-" + Selectedmonth + "-" + day;
            datePicker.setValue(selectedDate);
        }
        else {
            sel = document.getElementById(id);
            year = ePopGetValue('year');
            Selectedmonth = ePopGetValue('month')
            day = sel.options[sel.selectedIndex].value;
            selectedDate = year + "-" + Selectedmonth + "-" + day;
            datePicker.setValue(selectedDate);
        }
    }
    else if (flag == 2) {
        if (id == "fatherYear") {
            sel = document.getElementById(id);
            year = sel.options[sel.selectedIndex].value;
            Selectedmonth = ePopGetValue("fatherMonth");
            day = ePopGetValue("fatherDay");
            selectedDate = year + "-" + Selectedmonth + "-" + day;
            fatherDatePicker.setValue(selectedDate);
        }
    }
    else {

    }
}

function ePopGetValue(val) {
    var sel = document.getElementById(val);
    return  sel.options[sel.selectedIndex].value;
}

function splitDate(id) {
    //to do this
}