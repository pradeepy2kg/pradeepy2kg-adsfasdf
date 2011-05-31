<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
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
    })

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

            domObject = document.getElementById('fatherFullName');
            isEmpty(domObject, "", 'error3');

            domObject = document.getElementById('motherFullName');
            isEmpty(domObject, "", 'error4');
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
                <td class="cell_01">11</td>
                <td><label>ළම‌යාගේ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ) <br>பிறப்பு அத்... (சிங்களம் / தமிழ்)
                    <br>Childs name in the official languages (Sinhala / Tamil)</label></td>
                <td><s:textarea cssClass="disable" disabled="true"
                                name="#session.birthConfirmation_db.child.childFullNameOfficialLang"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>திருத்தப்பட்ட பெயர் <br>Corrected name</label></td>
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
                <td><label>නම වෙනස් විය යුතු අයුරු<br>திருத்தப்பட்ட பெயர் <br>Corrected name</label></td>
                <td>
                    <s:textarea id="childFullNameEnglish" name="child.childFullNameEnglish"
                                cssStyle="text-transform: uppercase;"/> <br>
                    <img src="<s:url value="/images/transliterate.png"/>" style="vertical-align:middle;" id="childName">
                </td>
            </tr>
            <tr>
                <td>13</td>
                <td><label>පියාගේ සම්පුර්ණ නම <br>தந்நையின் முழுப் பெயர்<br>Father's Full Name</label></td>
                <td><s:textarea cssClass="disable" disabled="true"
                                name="#session.birthConfirmation_db.parent.fatherFullName"/></td>
            </tr>
            <tr>
                <td></td>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>திருத்தப்பட்ட பெயர் <br>Corrected name</label></td>
                <td><s:textarea id="fatherFullName" name="parent.fatherFullName"></s:textarea></td>
            </tr>
            <tr>
                <td>14</td>
                <td><label>මවගේ සම්පූර්ණ නම <br>தாயின் முழுப் பெயர்<br>Mother's Full Name</label></td>
                <td><s:textarea cssClass="disable" disabled="true"
                                name="#session.birthConfirmation_db.parent.motherFullName"/></td>
            </tr>
            <tr>
                <td></td>
                <td><label>නම වෙනස් විය යුතු අයුරු<br>திருத்தப்பட்ட பெயர் <br>Corrected name</label></td>
                <td><s:textarea id="motherFullName" name="parent.motherFullName"></s:textarea></td>
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
</div>
<%-- Styling Completed --%>