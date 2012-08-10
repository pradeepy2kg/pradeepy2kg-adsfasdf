<%--
  @author Supun Nimesh Karunathilaka
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function printAdoptionOrderPage() {
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
        jsPrintSetup.setOption('orientation', jsPrintSetup.kLandscapeOrientation);
        jsPrintSetup.setGlobalOption('paperWidth', 210);
        jsPrintSetup.setGlobalOption('paperHeight', 297);

        // Define paper size. (To be A4)
        jsPrintSetup.setPaperSizeData(9);
        //    jsPrintSetup.definePaperSize(9, 9, 'iso_a4', 'iso_a4_210x297mm', 'A4', 210, 297, jsPrintSetup.kPaperSizeMillimeters);

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
    function installjsPrintSetup() {
        if (confirm("You don't have printer plugin.\nDo you want to install the Printer Plugin now?")) {
            var xpi = new Object();
            xpi['jsprintsetup'] = '/downloads/js_print_setup-0.8.2h-fx.xpi';
            InstallTrigger.install(xpi);
        }
    }
</script>
<div id="adoption-registration-form-outer">
    <div class="form-submit">
        <s:submit type="button" value="%{getText('print.button')}" onclick="printAdoptionOrderPage()"/>
    </div>
    <br><br><br><br>
    <s:form action="eprAdoptionOrderBackToPreviousState" name="" id="" method="POST">
        <table style="width:100%; border:none; border-collapse:collapse;">
            <tr>
                <td colspan="8" align="right">ලි. ප. කි. ආ. 138 / Registration B 138</td>
            </tr>
            <tr>
                <td colspan="8" align="left">දරුකමට හදාගැනීමේ නියෝගයේ සඳහන් අන්දමට සටහන් කරන්න / Enter details as stated
                    in Adoption Order/ மகவேற்புக்கட்டளையில் உள்ளதன் பிரகாரம் நிரப்புக.
                </td>
            </tr>
        </table>
        <table id="adoption-order-details" class="adoption-reg-form-01-table01" cellspacing="0" cellpadding="0">
            <tr valign="top" align="center">
                <td>
                    සටහනේ අංකය<br>
                    பதிவு இலக்கம்<br>
                    Number of Entry
                </td>
                <td>
                    සටහනේ දිනය<br>
                    பதிவு திகத<br>
                    Date of Entry
                </td>
                <td>
                    දරුකමට හදාගත් ළමයාගේ නම <br>
                    மகவேற்கப்பட்ட பிள்ளையின் பெயர்<br>
                    Name of Adopted Child
                </td>
                <td>
                    දරුකමට හදාගත් ළමයාගේ ස්ත්‍රී පුරුෂ භාවය<br>
                    மகவேற்கப்பட்ட பிள்ளையின் பால்<br>
                    Sex of Adopted Child
                </td>
                <td>
                    දරුකමට හදාගන්නා අයගේ හෝ අයවළුන්ගේ නම සහ වාසගම, ලිපිනය සහ රක්ෂාව<br>
                    மகவேற்றவர் அல்லது மகவேற்றவர்களின் முழுப்பெயர், முகவரி மற்றும் தொழில்<br>
                    Name and Surname, Address and Occupation of Adopter or Adopters
                </td>
                <td>
                    ළමයාගේ උපන් දිනය<br>
                    பிள்ளையின் பிறந்த திகத<br>
                    Date of Birth of Child
                </td>
                <td>
                    දරුකමට හදාගැනීමේ නියෝගයේ දිනය සහ නියෝගය දෙන ලද උසාවියේ විස්තරය<br>
                    மகவேற்புக்கட்டளையின் திகதி மற்றும் கட்டளையிட்ட நீதிமன்றத்தின் விபரங்கள்<br>
                    Date of Adoption Order and particulars of court by which made
                </td>
                <td>
                    සටහන සහතික කිරීමට රෙජිස්ට්‍රාර් ජනරාල්ගෙන් බලය ලබා ඇති නිලධාරියාගේ අත්සන<br>
                    உறுதிப்படுத்துவதற்கு பதிவாளர் நாயகத்தின் அதிகாரமளிக்கப்பட்ட அதிகாரியின் கையொப்பம்<br>
                    Signature of officer authorized by Registrar-General to certify the entry
                </td>
            </tr>
            <tr valign="top">
                <td rowspan="2"><s:label name="adoption.adoptionEntryNo"/></td>
                <td rowspan="2"><s:label name="adoption.orderReceivedDate"/></td>
                <td rowspan="2"><s:label name="adoption.childExistingName"/></td>
                <td rowspan="2" align="center">
                    <s:if test="adoption.childGender == 0">
                        <s:label name="" value="%{getText('male.label')}"/>
                    </s:if>
                    <s:elseif test="adoption.childGender == 1">
                        <s:label name="" value="%{getText('female.label')}"/>
                    </s:elseif>
                    <s:elseif test="adoption.childGender == 2">
                        <s:label name="" value="%{getText('unknown.label')}"/>
                    </s:elseif></td>
                <td rowspan="2"><s:label name="adoption.applicantName"/><br>
                    <s:label name="adoption.applicantAddress"/>
                </td>
                <td rowspan="2" align="center"><s:label name="adoption.childBirthDate"/></td>
                <td rowspan="2"><s:label name="adoption.orderIssuedDate"/><br>
                    <s:label name="adoption.court.siCourtName"/>
                </td>
                <td style="border:none;"><s:label name=""/></td>
            </tr>
            <tr style="height:250px">
                <td align="center"><br><br><br><br><br><br><br><br><br><br>
                    ....................................... <br/>
                    සහකාර රෙජිස්ට්‍රාර් ජනරාල්.<br>
                    உதவி பதிவாளர் நாயகம்.<br>
                    Assistant Registrar-General.
                </td>
            </tr>
            <tr>
                <td colspan="8" align="center">වෙනස්කිරීම්/திருத்தங்கள்/AMENDMENTS</td>
            </tr>
            <tr>
                <td height="115px"><br/></td>
                <td><br/></td>
                <td><br/></td>
                <td><br/></td>
                <td><br/></td>
                <td><br/></td>
                <td><br/></td>
                <td><br/></td>
            </tr>

        </table>
        <%--<div cl="controls">
            <div class="form-submit">
                <s:submit type="button" value="%{getText('print.button')}" onclick="printAdoptionOrderPage()"/>
            </div>
        </div>
        <div class="button" align="right">
            <s:hidden name="nextFlag" value="%{#request.nextFlag}"/>
            <s:hidden name="previousFlag" value="%{#request.previousFlag}"/>
            <s:hidden name="currentStatus" value="%{#request.currentStatus}"/>
            <s:hidden name="pageNo" value="%{#request.pageNo}"/>
            <s:submit value="%{getText('back.label')}" cssStyle="margin-top:10px;"/>
        </div>--%>
    </s:form>
</div>