<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<s:set value="rowNumber" name="row"/>

<script>
$(function() {
    $('img#childName').bind('click', function(evt) {
        var id=$("textarea#childFullNameOfficialLang").attr("value");
        var wsMethod = "transliterate";
        var soapNs = "http://translitwebservice.transliteration.icta.com/";

        var soapBody = new SOAPObject("trans:" + wsMethod); //Create a new request object
        soapBody.attr("xmlns:trans",soapNs);
        soapBody.appendChild(new SOAPObject('InputName')).val(id);
        soapBody.appendChild(new SOAPObject('SourceLanguage')).val(0);
        soapBody.appendChild(new SOAPObject('TargetLanguage')).val(3);
        soapBody.appendChild(new SOAPObject('Gender')).val('U');

        //Create a new SOAP Request
        var sr = new SOAPRequest(soapNs+wsMethod, soapBody); //Request is ready to be sent

        //Lets send it
        SOAPClient.Proxy = "/TransliterationWebService/TransliterationService";
        SOAPClient.SendRequest(sr, processResponse); //Send request to server and assign a callback
    });

    function processResponse(respObj) {
        //respObj is a JSON equivalent of SOAP Response XML (all namespaces are dropped)
        $("textarea#childFullNameEnglish").val(respObj.Body[0].transliterateResponse[0].return[0].Text);
    }
})
</script>

<div id="birth-confirmation-form-outer">
    <s:form action="eprBirthConfirmation" name="birthConfirmation_form2" id="birth-confirmation-form-2" method="POST">
        <table class="table_con_page_02" cellspacing="0">
            <caption></caption>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td colspan="3" style="text-align:center;font-size:12pt"> * in Sinhala <br>* in Tamil *<br> Changes in
                    Names
                </td>
            </tr>
            <tr>
                <td class="cell_01">11</td>
                <td><label>ළම‌යාගේ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ) <br>பிறப்பு அத்... (சிங்களம் / தமிழ்)
                    <br>Childs name in the official languages (Sinhala / Tamil)</label></td>
                <td><s:textarea cssClass="disable" disabled="true" name="#session.birthConfirmation_db.child.childFullNameOfficialLang" />
               </td>
            </tr>
            <tr>
                <td></td>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>* in Tamil <br>Corrected name</label></td>
                <td><s:textarea id="childFullNameOfficialLang" name="child.childFullNameOfficialLang"/></td>
            </tr>
            <tr>
                <td>12</td>
                <td><label>ළම‌යාගේ නම ඉංග්‍රීසි භාෂාවෙන්<br>பிறப்பு ... <br>Childs name in English</label></td>
                <td><s:textarea cssClass="disable" disabled="true" cssStyle="text-transform: uppercase;"
                                 name="#session.birthConfirmation_db.child.childFullNameEnglish"/></td>
            </tr>
            <tr>
                <td></td>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>* in Tamil <br>Corrected name</label></td>
                <td>
                    <s:textarea id="childFullNameEnglish" name="child.childFullNameEnglish" cssStyle="text-transform: uppercase;"/>
                    <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;" id="childName">
                </td>
            </tr>
            <tr>
                <td>13</td>
                <td><label>පියාගේ සම්පුර්ණ නම <br>தந்நையின் முழுப் பெயர்<br>Father's Full Name</label></td>
                <td><s:textarea cssClass="disable" disabled="true" name="#session.birthConfirmation_db.parent.fatherFullName"/></td>
            </tr>
            <tr>
                <td></td>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>* in Tamil <br>Corrected name</label></td>
                <td><s:textarea name="parent.fatherFullName"></s:textarea></td>
            </tr>
            <tr>
                <td>14</td>
                <td><label>මවගේ සම්පූර්ණ නම <br>தாயின் முழுப் பெயர்<br>Mother's Full Name</label></td>
                <td><s:textarea cssClass="disable" disabled="true" name="#session.birthConfirmation_db.parent.motherFullName"/></td>
            </tr>
            <tr>
                <td></td>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>* in Tamil <br>Corrected name</label></td>
                <td><s:textarea name="parent.motherFullName"></s:textarea></td>
            </tr>
            </tbody>
        </table>
        <div class="form-submit">
            <s:hidden name="pageNo" value="2"/>
            <s:hidden name="skipConfirmationChages" value="%{#request.skipConfirmationChages}"/>
            <s:submit value="%{getText('next.label')}"/>
        </div>

        <div class="next-previous">
            <s:url id="backUrl" action="eprBirthConfirmation">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
                <s:param name="skipConfirmationChages" value="#request.skipConfirmationChages"/>
            </s:url>
            <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
        </div>
    </s:form>
</div>
<%-- Styling Completed --%>