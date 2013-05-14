<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<s:set value="rowNumber" name="row"/>

<script>
    $(function() {
        $('img#childName').bind('click', function(evt) {
            var text = $("textarea#childFullNameOfficialLang").attr("value");

            $.post('/ecivil/TransliterationService', {text:text,gender:'U'},
                    function(data) {
                        if (data != null) {
                            var s = data.translated;
                            $("textarea#childFullNameEnglish").val(s);
                        }
                    });
        });

        $('img#fatherName').bind('click', function(evt) {
            var text = $("textarea#fatherFullNameOfficialLang").attr("value");

            $.post('/ecivil/TransliterationService', {text:text,gender:'U'},
                    function(data) {
                        if (data != null) {
                            var s = data.translated;
                            $("textarea#fatherFullNameEnglish").val(s);
                        }
                    });
        });

         $('img#motherName').bind('click', function(evt) {
            var text = $("textarea#motherFullNameOfficialLang").attr("value");

            $.post('/ecivil/TransliterationService', {text:text,gender:'U'},
                    function(data) {
                        if (data != null) {
                            var s = data.translated;
                            $("textarea#motherFullNameEnglish").val(s);
                        }
                    });
        });
    });

    var errormsg = "";
    function validate() {
        var domObject;
        var returnval = true;
        var check = document.getElementById('skipjs');

        if (!check.checked) {
            domObject = document.getElementById('childFullNameOfficialLang');
            isEmpty(domObject, "", 'error1');

            domObject = document.getElementById('childFullNameEnglish');
            isEmpty(domObject, "", 'error2');

            domObject = document.getElementById('fatherFullNameOfficialLang');
            isEmpty(domObject, "", 'error3');

            domObject = document.getElementById('fatherFullNameEnglish');
            isEmpty(domObject, "", 'error5');

            domObject = document.getElementById('motherFullNameOfficialLang');
            isEmpty(domObject, "", 'error4');

            domObject = document.getElementById('motherFullNameEnglish');
            isEmpty(domObject, "", 'error6');
        }

        var out = checkActiveFieldsForSyntaxErrors('birth-confirmation-form-2');
        if(out != ""){
            errormsg = errormsg + out;
        }

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

    function initPage() {
    }
</script>

<div id="birth-confirmation-form-outer">
    <s:form action="eprBirthConfirmation" name="birthConfirmation_form2" id="birth-confirmation-form-2" method="POST"
            onsubmit="javascript:return validate()">
        <table class="table_con_page_02" cellspacing="0">
            <caption></caption>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td colspan="3" style="text-align:center;font-size:12pt"> නම් වල වෙනස් වීම්<br> பெயரிலுள்ள மாற்றங்கள்
                    <br> Changes in Names
                    Names
                </td>
            </tr>
            <tr>
                <td class="cell_01" rowspan="2">14</td>
                <td>
                    <label>ළම‌යාගේ නම රාජ්‍ය භාෂාවෙන් <br/>(සිංහල / දෙමළ)
                        <br>பிள்ளையின்  பெயர் அரச கரும மொழியில்  (சிங்களம் / தமிழ்)
                        <br>Child's name in the official languages (Sinhala / Tamil)</label>
                </td>
                <td><s:textarea cssClass="disable" disabled="true"
                                name="#session.birthConfirmation_db.child.childFullNameOfficialLang"/>
                </td>
            </tr>
            <tr>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>பெயர் மாற்றப்பட வேண்டிய விதம்  <br>Corrected name</label></td>
                <td><s:textarea id="childFullNameOfficialLang" onchange="checkSyntax('childFullNameOfficialLang')" name="child.childFullNameOfficialLang" rows="3"/></td>
            </tr>
            <tr>
                <td rowspan="2">15</td>
                <td>
                    <label>ළම‌යාගේ නම ඉංග්‍රීසි භාෂාවෙන් <br>பிள்ளையின்  பெயர் ஆங்கில மொழியில்<br>Child's name in English
            </label>
                </td>
                <td><s:textarea cssClass="disable" disabled="true" cssStyle="text-transform: uppercase;"
                                name="#session.birthConfirmation_db.child.childFullNameEnglish"/></td>
            </tr>
            <tr>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>பெயர் மாற்றப்பட வேண்டிய விதம்  <br>Corrected name</label></td>
                <td>
                    <s:textarea id="childFullNameEnglish" name="child.childFullNameEnglish" rows="3"
                                cssStyle="text-transform: uppercase;"/> <br>
                    <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;" id="childName">
                </td>
            </tr>
            <tr>
                <td rowspan="2">16</td>
                <td>
                    <label>පියාගේ සම්පුර්ණ නම රාජ්‍ය භාෂාවෙන් <br/>(සිංහල / දෙමළ)
                    <br>தந்தையின் முழுப் பெயர் அரச கரும மொழியில்  (சிங்களம் / தமிழ்)
                    <br>Father's Full Name in any of the official languages (Sinhala / Tamil)</label>
                </td>
                <td><s:textarea cssClass="disable" disabled="true"
                                name="#session.birthConfirmation_db.parent.fatherFullName"/></td>
            </tr>
            <tr>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>பெயர் மாற்றப்பட வேண்டிய விதம்  <br>Corrected name</label></td>
                <td>
                    <s:textarea id="fatherFullNameOfficialLang" onchange="checkSyntax('fatherFullNameOfficialLang')" name="parent.fatherFullName" rows="3"/>
                </td>
            </tr>
            <tr>
                <td rowspan="2">17</td>
                <td>
                    <label>පියාගේ සම්පුර්ණ නම ඉංග්‍රීසි භාෂාවෙන් (කැපිටල් අකුරෙන්)</label>
                        <br>தந்தையின் முழுப் பெயர் ஆங்கில மொழியில்(பெரிய எழுத்துக்களில்)
                        <br>Father's Full Name in English (in block letters)</label>
                </td>
                <td><s:textarea cssClass="disable" disabled="true"
                                name="#session.birthConfirmation_db.parent.fatherFullNameInEnglish"/></td>
            </tr>
            <tr>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>பெயர் மாற்றப்பட வேண்டிய விதம்  <br>Corrected name</label></td>
                <td>
                    <s:textarea id="fatherFullNameEnglish" name="parent.fatherFullNameInEnglish" rows="3"/><br/>
                    <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;" id="fatherName">
                </td>
            </tr>
            <tr>
                <td rowspan="2">18</td>
                <td>
                    <label>මවගේ සම්පුර්ණ නම රාජ්‍ය භාෂාවෙන් <br/>(සිංහල / දෙමළ)
                    <br>தாயின் முழுப் பெயர் அரச கரும மொழியில்  (சிங்களம் / தமிழ்)
                    <br>Mother's Full Name in any of the official languages (Sinhala / Tamil)</label>
                </td>
                <td><s:textarea cssClass="disable" disabled="true"
                                name="#session.birthConfirmation_db.parent.motherFullName"/></td>
            </tr>
            <tr>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>பெயர் மாற்றப்பட வேண்டிய விதம்  <br>Corrected name</label></td>
                <td><s:textarea id="motherFullNameOfficialLang" onchange="checkSyntax('motherFullNameOfficialLang')" name="parent.motherFullName" rows="3"/></td>
            </tr>
            <tr>
                <td rowspan="2">19</td>
                <td>
                    <label>මවගේ සම්පුර්ණ නම ඉංග්‍රීසි භාෂාවෙන් (කැපිටල් අකුරෙන්)
                    <br>தாயின் முழுப் பெயர் ஆங்கில மொழியில் (பெரிய எழுத்துக்களில்)
                    <br>Mother's Full Name in English (in block letters)</label>
                </td>
                <td><s:textarea cssClass="disable" disabled="true"
                                name="#session.birthConfirmation_db.parent.motherFullNameInEnglish"/></td>
            </tr>
            <tr>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>பெயர் மாற்றப்பட வேண்டிய விதம்  <br>Corrected name</label></td>
                <td>
                    <s:textarea id="motherFullNameEnglish" name="parent.motherFullNameInEnglish" rows="3"/><br/>
                    <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;" id="motherName">
                </td>
            </tr>
            </tbody>
        </table>

        <div class="skip-validation">
            <s:checkbox name="skipjavaScript" id="skipjs" value="false">
                <s:label value="%{getText('skipvalidation.label')}"/>
            </s:checkbox>
        </div>

        <div class="form-submit">
            <s:hidden name="pageNo" value="2"/>
            <s:hidden name="skipConfirmationChages" value="%{#request.skipConfirmationChages}"/>
            <s:hidden value="%{#request.bdId}" name="bdId"/>
            <s:submit value="%{getText('next.label')}"/>
        </div>
    </s:form>

    <s:form action="eprBirthConfirmation.do" method="post">
        <s:hidden name="back" value="true"/>
        <s:hidden name="pageNo" value="%{pageNo - 1}"/>
        <s:hidden name="skipConfirmationChages" value="%{#request.skipConfirmationChages}"/>

        <div class="form-submit">
            <s:submit value="%{getText('previous.label')}"/>
            <s:hidden value="%{#request.bdId}" name="bdId"/>
        </div>
    </s:form>

    <s:hidden id="error1" value="%{getText('p1.childName.error.value')}"/>
    <s:hidden id="error2" value="%{getText('p1.NameEnglish.error.value')}"/>
    <s:hidden id="error3" value="%{getText('p2.fatherName.error.value')}"/>
    <s:hidden id="error4" value="%{getText('p2.motherName.error.value')}"/>
    <s:hidden id="error5" value="%{getText('fatherNameInEnglish')}"/>
    <s:hidden id="error6" value="%{getText('motherNameInEnglish')}"/>
</div>
<%-- Styling Completed --%>