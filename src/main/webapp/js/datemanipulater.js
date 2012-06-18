/**
 * @author Indunil Moremada  ,amith jayasekara
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


dojo.provide("dojo.widget.DatePicker");
dojo.provide("dojo.widget.DatePicker.util");
dojo.require("dojo.widget.DomWidget");
dojo.require("dojo.date");


function setDate(id, flag) {
    var submitDatePicker = dojo.widget.byId('submitDatePicker');


    var datePicker = dojo.widget.byId('datePicker');
    var fatherDatePicker = dojo.widget.byId('fatherDatePicker');
    var motherDatePicker = dojo.widget.byId('motherDatePicker');
    var admitDatePicker = dojo.widget.byId('admitDatePicker');
    var marriageDatePicker = dojo.widget.byId('marriageDatePicker');
    var modifiedDatePicker = dojo.widget.byId('modifiedDatePicker');

    if (flag == 1) submitDatePicker.setValue(getDateWhenYearIsGiven
            ("submitYear", "submitMonth", "submitDay"));

    else if (flag == 2)  datePicker.setValue(getDateWhenYearIsGiven
            ("year", "month", "day"));

    else if (flag == 3) fatherDatePicker.setValue(getDateWhenYearIsGiven
                ("fatherYear", "fatherMonth", "fatherDay"));

        else if (flag == 4) motherDatePicker.setValue(getDateWhenYearIsGiven
                    ("motherYear", "motherMonth", "motherDay"));
            else if (flag == 5) admitDatePicker.setValue(getDateWhenYearIsGiven
                        ("admitYear", "admitMonth", "admitDay"));
                else if (flag == 6)marriageDatePicker.setValue(getDateWhenYearIsGiven
                            ("marrigeYear", "marrigeMonth", "marrigeDay"));
                    else if (flag == 7)modifiedDatePicker.setValue(getDateWhenYearIsGiven
                                ("modifiedYear", "modifiedMonth", "modifiedDay"));

}

function setValue() {
    var picker = dojo.widget.byId("picker");

    //string value
    picker.setValue('2007-01-01');

    //Date value
    picker.setValue(new Date());
}

function showValue() {
    var picker = dojo.widget.byId("picker");

    //string value
    var stringValue = picker.getValue();
    alert(stringValue);

    //date value
    var dateValue = picker.getDate();
    alert(dateValue);
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
function splitDate() {
    //todo has to be implemented
    //var stringValue = submitDatePicker.getValue();
    document.write("dddd");
    alert("ok");


}

function validateDate() {

    var datePicker = dojo.widget.byId('datePicker').inputNode.value;
    //var x = dojo.widget.byId("datePicker").datePicker.storedDate;
    alert(datePicker);
    var submitDatePicker = dojo.widget.byId('submitDatePicker').inputNode.value;
    alert(submitDatePicker);
    /*  SelectedDateDDMMMYYYY = dojo.widget.byId("atePicker").inputNode.value;
     SelectedDateYYYYMMDD = dojo.widget.byId("atePicker").datePicker.storedDate;

     alert("Date selected is " + SelectedDateDDMMMYYYY + " length " + SelectedDateDDMMMYYYY.length + " "
     + SelectedDateYYYYMMDD);*/


}

function x() {
    alert("amith");
}