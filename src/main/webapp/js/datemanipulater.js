/*@author Indunil Moremada
    javascript for changing the datepicker dynamically with the changes done for the list boxes year,month and date
  */
function setDate(id) {
            var year;
            var Selectedmonth;
            var day;
            var selectedDate;
            var sel;
            var datePicker = dojo.widget.byId('datePicker');
            if (id == "year") {
                sel = document.getElementById(id);
                year = sel.options[sel.selectedIndex].value;

                Selectedmonth=ePopGetValue('month');
                day=ePopGetValue('day');
                selectedDate=year+"-"+Selectedmonth+"-"+day;
                datePicker.setValue(selectedDate);
            }
            else if (id == "month") {
                sel=document.getElementById(id);
                Selectedmonth=sel.options[sel.selectedIndex].value;
                day=ePopGetValue('day');
                year=ePopGetValue('year');
                selectedDate=year+"-"+Selectedmonth+"-"+day;
                datePicker.setValue(selectedDate);
            }
            else {
                sel=document.getElementById(id);
                year=ePopGetValue('year');
                Selectedmonth=ePopGetValue('month')
                day=sel.options[sel.selectedIndex].value;
                selectedDate=year+"-"+Selectedmonth+"-"+day;
                datePicker.setValue(selectedDate);
            }
        }

        function ePopGetValue(val) {
            var sel = document.getElementById(val);
            return  sel.options[sel.selectedIndex].value;
        }

        function splitDate(id) {
          //still not implemented
        }