/* @author Mahesha Kalpanie  */
function datepicker(datePickerId){
    $("#" + datePickerId).datepicker({
        changeYear: true,
        yearRange: '1960:2020',
        dateFormat:'yy-mm-dd',
        startDate:'2000-01-01',
        endDate:'2040-12-31'
    });
}
