/*
* @ author amith jayasekara
* */

function printPage(){
    if ((navigator.appName == "Netscape"))
            {
                window.print() ;
            }
            else
            {
                var WebBrowser =
    '<OBJECT ID="WebBrowser1" WIDTH=0 HEIGHT=0  CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>';
        document.body.insertAdjacentHTML('beforeEnd', WebBrowser);
        WebBrowser1.ExecWB(6, -1);
        WebBrowser1.outerHTML ;
            }
   

}