/* @author Duminda Dharmakeerthi */

function printPage() {
    if (typeof(jsPrintSetup) == 'undefined') {
        installjsPrintSetup();
    } else {
        var printContentWrapper = $("#body-content-data").clone();

        var windowUrl = 'about:blank';
        var uniqueName = new Date();
        var windowName = 'Print' + uniqueName.getTime();
        var printWindow = window.open(windowUrl, windowName, 'left=0,top=0,width=200px,height=200px');
        printWindow.document.write("<head><link rel=\"stylesheet\" type=\"text/css\" href=\"/ecivil/css/webform.css\"/>");
        printWindow.document.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"/ecivil/css/style.css\"/>");
        printWindow.document.write("<script language=\"JavaScript\" src=\"/ecivil/js/print.js\"><\/script>");
        printWindow.document.write("<link rel=\"stylesheet\" type=\"text/css\" media=\"print\" href=\"/ecivil/css/print.css\"/></head>");
        printWindow.document.write(printContentWrapper.html());
        printWindow.document.close();
        printWindow.focus();
        printWindow.onload = printForm();
        printWindow.close();
    }
}

// Print function for forms using JSPrintSetup add-on...
function printForm() {
    // set page orientation.
    jsPrintSetup.setOption('orientation', jsPrintSetup.kPortraitOrientation);
    // set margins.
    jsPrintSetup.setOption('marginTop', 0);
    jsPrintSetup.setOption('marginBottom', 0);
    jsPrintSetup.setOption('marginLeft', 10);
    jsPrintSetup.setOption('marginRight', 0);


    // set page header
    jsPrintSetup.setOption('headerStrLeft', '');
    jsPrintSetup.setOption('headerStrCenter', '');
    jsPrintSetup.setOption('headerStrRight', '');
    // set empty page footer
    jsPrintSetup.setOption('footerStrLeft', '');
    jsPrintSetup.setOption('footerStrCenter', '');
    jsPrintSetup.setOption('footerStrRight', '');

    jsPrintSetup.print();
}

// Install JSPrintSetup
function installjsPrintSetup(){
    if(confirm("You don't have printer plugin.\nDo you want to install the Printer Plugin now?")){
        var xpi = new Object();
        xpi['jsprintsetup'] = 'https://addons.mozilla.org/en-US/firefox/downloads/latest/8966/addon-8966-latest.xpi';
        InstallTrigger.install(xpi);
    }
}