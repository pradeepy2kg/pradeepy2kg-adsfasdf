/**
 * @author Indunil Moremada
 * javascript for changing the datepicker dynamically with the changes done for the list boxes year,month and date
 *  @param id is used to identified the field in the jsp
 * @param flag is used to identify whether the request is coming from if flag is 1 request is from page 1 of 4
 * if flag is 2 request is from father's dob else it is from mother's dob field
 */
var year;
var Selectedmonth;
var day;
var selectedDate;
var sel;
function setDate(id, flag) {
    var datePicker = dojo.widget.byId('datePicker');
    var fatherDatePicker = dojo.widget.byId('fatherDatePicker');
    var motherDatePicker = dojo.widget.byId('motherdatePicker');
    if (flag == 1) {
        if (id == "year") {
            selectedDate = getDateWhenYearIsGiven("year", "month", "day");
            datePicker.setValue(selectedDate);
        }
        else if (id == "month") {
            selectedDate = getDateWhenMonthIsGiven("year", "month", "day");
            datePicker.setValue(selectedDate);
        }
        else {
            selectedDate = getDateWhenDayIsGiven("year", "month", "day");
            datePicker.setValue(selectedDate);
        }
    }
    else if (flag == 2) {
        if (id == "fatherYear") {
            selectedDate = getDateWhenYearIsGiven("fatherYear", "fatherMonth", "fatherDay");
            fatherDatePicker.setValue(selectedDate);
        }
        else if (id == "fatherMonth") {
            selectedDate = getDateWhenMonthIsGiven("fatherYear", "fatherMonth", "fatherDay");
            fatherDatePicker.setValue(selectedDate);
        }
        else {
            selectedDate = getDateWhenDayIsGiven("fatherYear", "fatherMonth", "fatherDay");
            fatherDatePicker.setValue(selectedDate);
        }
    }
    else {
        //to do mother's datepicker
    }
}

function ePopGetValue(val) {
    var sel = document.getElementById(val);
    return  sel.options[sel.selectedIndex].value;
}
/**
 *this return the curren status of the list boxes when the selected list box is year
 * other entries are kept as it is
 * **/
function getDateWhenYearIsGiven(y, m, d) {
    sel = document.getElementById(y);
    year = sel.options[sel.selectedIndex].value;
    Selectedmonth = ePopGetValue(m);
    day = ePopGetValue(d);
    return selectedDate = year + "-" + Selectedmonth + "-" + day;
}
/**
 *this return the curren status of the list boxes when the selected list box is Month
 * other entries are kept as it is
 * **/
function getDateWhenMonthIsGiven(y, m, d) {
    sel = document.getElementById(m);
    Selectedmonth = sel.options[sel.selectedIndex].value;
    day = ePopGetValue(d);
    year = ePopGetValue(y);
    return selectedDate = year + "-" + Selectedmonth + "-" + day;
}
/**
 *this return the curren status of the list boxes when the selected list box is day
 * other entries are kept as it is
 * **/
function getDateWhenDayIsGiven(y, m, d) {
    sel = document.getElementById(d);
    year = ePopGetValue(y);
    Selectedmonth = ePopGetValue(m)
    day = sel.options[sel.selectedIndex].value;
    return year + "-" + Selectedmonth + "-" + day;
}

function splitDate(id) {
    //to do this
}