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
var selectedDate,secondSelectedDate,thirdSelectedDate;
var sel;
function setDate(id, flag) {
    var datePicker = dojo.widget.byId('datePicker');
    var secondDatePicker = dojo.widget.byId('secondDatePicker');
    var thirdDatePicker = dojo.widget.byId('thirdDatePicker');


    if (flag == 1)
    {
        selectedDate = getDateWhenYearIsGiven("year", "month", "day");
        datePicker.setValue(selectedDate);


    }
    else if (flag == 2)
    {
        secondSelectedDate = getDateWhenYearIsGiven("secondYear", "secondMonth", "secondDay");
        secondDatePicker.setValue(secondSelectedDate);
    }
    else if (flag == 3)
        {

            thirdSelectedDate = getDateWhenYearIsGiven("thirdYear", "thirdMonth", "thirdDay");
            thirdDatePicker.setValue(thirdSelectedDate);

        }


}

function ePopGetValue(val) {
    var sel = document.getElementById(val);
    return  sel.options[sel.selectedIndex].value;
}
/**
 *this return the current status of the list boxes when the selected list box is year
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
 *this return the current status of the list boxes when the selected list box is Month
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
 *this return the current status of the list boxes when the selected list box is day
 * other entries are kept as it is
 * **/
function getDateWhenDayIsGiven(y, m, d) {
    sel = document.getElementById(d);
    year = ePopGetValue(y);
    Selectedmonth = ePopGetValue(m)
    day = sel.options[sel.selectedIndex].value;
    return year + "-" + Selectedmonth + "-" + day;
}
/**
 *  method used to populate the list boxes according to the selected value of the datepicker
 * @param id used to identify the request
 */
function splitDate(id) {
    //todo has to be implemented
}