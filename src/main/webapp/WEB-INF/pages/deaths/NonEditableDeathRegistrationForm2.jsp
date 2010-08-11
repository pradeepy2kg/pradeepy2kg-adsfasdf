<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/popreg/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.7.2.custom.css" type="text/css"/>


<script type="text/javascript">

    $(function() {
        $("#submitDatePicker").datepicker();
    });

    $(function() {
        $('img#declarant_lookup').bind('click', function(evt1) {
            var id1 = $("input#declarant_pinOrNic").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#declarantFullName").val(data1.fullNameInOfficialLanguage);
                        $("textarea#declarantAddress").val(data1.lastAddress);
                    });
        });

        $('img#first_witness_lookup').bind('click', function(evt2) {
            var id2 = $("input#first_witness_NICorPIN").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id2},
                    function(data2) {
                        $("textarea#firstWitnessFullName").val(data2.fullNameInOfficialLanguage);
                        $("textarea#firstWitnessAddress").val(data2.lastAddress);
                    });
        });

        $('img#second_witness_lookup').bind('click', function(evt3) {
            var id3 = $("input#second_witness_NICorPIN").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id3},
                    function(data3) {
                        $("textarea#secondWitnessFullName").val(data3.fullNameInOfficialLanguage);
                        $("textarea#secondWitnessAddress").val(data3.lastAddress);
                    });
        });

        $('img#notifying_authority_lookup').bind('click', function(evt4) {
            var id4 = $("input#notifying_authority_NICorPIN").attr("value");
            $.getJSON('/popreg/prs/PersonLookupService', {pinOrNic:id4},
                    function(data4) {
                        $("textarea#notifyingAuthorityName").val(data4.fullNameInOfficialLanguage);
                        $("textarea#notifyingAuthorityAddress").val(data4.lastAddress);
                    });
        });
    });
</script>

<div id="death-declaration-form-2-outer">
    <s:form name="deathRegistrationForm2" action="eprDeathDeclaration.do" id="death-registration-form-2" method="POST">
        <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
               class="font-9">
            <col width="150px"/>
            <col width="130px"/>
            <col width="120px"/>
            <col width="130px"/>
            <col width="130px"/>
            <col width="130px"/>
            <col/>
            <tbody>
            <tr class="form-sub-title">
                <td colspan="7">ප්‍රකාශකයාගේ විස්තර<br>அறிவிப்பு கொடுப்பவரின் தகவல்கள்<br>Details of the Declarant</td>
            </tr>
            <tr>
                <td colspan="4">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>தகவநபர் அடையாள எண் / அடையாள அட்டை இல.
                    <br>PIN / NIC
                </td>
                <td colspan="3" class="find-person"><s:textfield id="declarant_pinOrNic"
                                                                 name="declarant.declarantNICorPIN"/><img
                        src="<s:url value="/images/search-father.png"/>"
                        style="vertical-align:middle; margin-left:20px;" id="declarant_lookup"></td>
            </tr>
            <tr>
                <td colspan="1">නම<br>கொடுப்பவரின் பெயர்<br>Name</td>
                <td colspan="6"><s:textarea id="declarantFullName" name="declarant.declarantFullName"/></td>
            </tr>
            <tr>
                <td colspan="1">තැපැල් ලිපිනය<br>தபால் முகவரி<br>Postal Address</td>
                <td colspan="6"><s:textarea id="declarantAddress" name="declarant.declarantAddress"/></td>
            </tr>
            <tr>
                <td colspan="1">ඇමතුම් විස්තර<br>இலக்க வகை <br>Contact Details</td>
                <td colspan="1">දුරකතනය<br>தொலைபேசி இலக்கம் <br>Telephone</td>
                <td colspan="2"><s:textfield id="declarantPhone" name="declarant.declarantPhone"/></td>
                <td colspan="1">ඉ -තැපැල<br>மின்னஞ்சல்<br>Email</td>
                <td colspan="2"><s:textfield id="declarantEMail" name="declarant.declarantEMail"/></td>
            </tr>
            <tr>
                <td rowspan="2" colspan="1">දැනුම් දෙන්නේ කවරකු වශයෙන්ද<br>*in tamil<br>Capacity for giving information
                </td>
                <td colspan="1">මව <br>*in tamil<br>Mother</td>
                <td colspan="1"><s:radio id="declarantType" name="declarant.declarantType"
                                         list="#@java.util.HashMap@{'FATHER':''}"/></td>
                <td colspan="1">පියා<br>*in tamil<br>Father</td>
                <td colspan="1"><s:radio id="declarantType" name="declarant.declarantType"
                                         list="#@java.util.HashMap@{'MOTHER':''}"/></td>
                <td colspan="1">සහෝදරයා සහෝදරිය<br>*in tamil<br>Brother / Sister</td>
                <td colspan="1"><s:radio id="declarantType" name="declarant.declarantType"
                                         list="#@java.util.HashMap@{'BORTHER_OR_SISTER':''}"/></td>
            </tr>
            <tr>
                <td colspan="1">පුත්‍රයා / දියණිය <br>*in tamil<br>Son / Daughter</td>
                <td colspan="1"><s:radio id="declarantType" name="declarant.declarantType"
                                         list="#@java.util.HashMap@{'SON_OR_DAUGHTER':''}"/></td>
                <td colspan="1">නෑයන් <br>பாதுகாவலர் <br>Relative</td>
                <td colspan="1"><s:radio id="declarantType" name="declarant.declarantType"
                                         list="#@java.util.HashMap@{'RELATIVE':''}"/></td>
                <td colspan="1">වෙනත් <br>*in tamil<br>Other</td>
                <td colspan="1"><s:radio id="declarantType" name="declarant.declarantType"
                                         list="#@java.util.HashMap@{'OTHER':''}"/></td>
            </tr>
            </tbody>
        </table>

        <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
               class="font-9">
            <col width="120px"/>
            <col width="100px"/>
            <col width="300px"/>
            <col width="120px"/>
            <col width="100px"/>
            <col/>
            <tbody>
            <tr style="border:0;">
                <td colspan="6" style="border:0; text-align:center;">පහත සඳහන් සාක්ෂි කරුවන් ඉදිරිපිටදී අත්සන් කරන
                    ලදී<br>*in tamil<br>Subscribed in the presence of the
                    following witnesses
                </td>
            </tr>
            <tr>
                <td colspan="2">පු.අ.අ. / ජා.හැ.අ.<br>அடையாள எண் / அடையா<br>PIN / NIC</td>
                <td colspan="1" class="find-person"><s:textfield id="first_witness_NICorPIN"
                                                                 name="witness.firstWitnessNICorPIN"/><img
                        src="<s:url value="/images/search-father.png"/>"
                        style="vertical-align:middle; margin-left:20px;" id="first_witness_lookup"></td>
                <td colspan="2">පු.අ.අ. / ජා.හැ.අ.<br>அடையாள எண் / அடையா<br>PIN / NIC</td>
                <td colspan="1" class="find-person"><s:textfield id="second_witness_NICorPIN"
                                                                 name="witness.secondWitnessNICorPIN"/><img
                        src="<s:url value="/images/search-father.png"/>"
                        style="vertical-align:middle; margin-left:20px;" id="second_witness_lookup"></td>
            </tr>
            <tr>
                <td>නම<br>கொடு<br>Name</td>
                <td colspan="2"><s:textarea id="firstWitnessFullName" name="witness.firstWitnessFullName"/></td>
                <td>නම<br>கொடு<br>Name</td>
                <td colspan="2"><s:textarea id="secondWitnessFullName" name="witness.secondWitnessFullName"/></td>
            </tr>
            <tr>
                <td>ලිපිනය<br>முகவரி<br>Address</td>
                <td colspan="2"><s:textarea id="firstWitnessAddress" name="witness.firstWitnessAddress"/></td>
                <td>ලිපිනය<br>முகவரி<br>Address</td>
                <td colspan="2"><s:textarea id="secondWitnessAddress" name="witness.secondWitnessAddress"/></td>
            </tr>
            <tr>
                <td>අත්සන <br>தகவல் ...<br>Signature</td>
                <td colspan="2"></td>
                <td>අත්සන <br>தகவல் ...<br>Signature</td>
                <td colspan="2"></td>
            </tr>
            </tbody>
        </table>

        <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
               class="font-9">
            <col width="150px"/>
            <col width="400px"/>
            <col width="100px"/>
            <col/>
            <tbody>
            <tr class="form-sub-title">
                <td colspan="4">තොරතුරු වාර්තා කරන පාර්ශවය<br>அதிகாரியிடம் தெரிவித்தல்<br>Notifying Authority</td>
            </tr>
            <tr>
                <td colspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>அடையாள எண் / அடையாள அட்டை இல. <br>PIN /
                    NIC
                </td>
                <td colspan="2"><s:textfield id="notifying_authority_NICorPIN"
                                             name="notifyingAuthority.notifyingAuthorityPIN"> </s:textfield>
                    <img src="<s:url value="/images/search-father.png" />"
                         style="vertical-align:middle; margin-left:20px;" id="notifying_authority_lookup"></td>
                </td>
            </tr>
            <tr>
                <td colspan="1">නම<br>கொடுப்பவரின் பெயர்<br>Name</td>
                <td colspan="3"><s:textarea id="notifyingAuthorityName"
                                            name="notifyingAuthority.notifyingAuthorityName"/></td>
            </tr>
            <tr>
                <td colspan="1">තැපැල් ලිපිනය<br>தபால் முகவரி<br>Postal Address</td>
                <td colspan="3"><s:textarea id="notifyingAuthorityAddress"
                                            name="notifyingAuthority.notifyingAuthorityAddress"/></td>
            </tr>
            <tr>
                <td colspan="1">අත්සන හා නිල මුද්‍රාව<br>தகவல் ...<br>Signature and Official Seal of the Notifying
                    Authority
                </td>
                <td colspan="1"></td>
                <td colspan="1">දිනය<br>திகதி<br>Date</td>
                <td colspan="1"><s:textfield id="submitDatePicker"
                                             name="notifyingAuthority.notifyingAuthoritySignDate"></s:textfield></td>
            </tr>
            </tbody>
        </table>


        <s:label><p class="font-8">පු.අ.අ. / ජා.හැ.අ. = පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය</p></s:label>

        <div class="form-submit">
            <s:hidden name="pageNo" value="2"/>
            <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
        </div>
        <div class="next-previous">
            <s:url id="backUrl" action="eprDeathDeclaration.do">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
            </s:url>
            <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
        </div>
    </s:form>
</div>