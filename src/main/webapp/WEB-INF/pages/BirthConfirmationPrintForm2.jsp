<%--
 @author amith jayasekara
--%>
<%@ page import="lk.rgd.crs.api.domain.BirthDeclaration" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<div class="birth-confirmation-form-outer">
    <s:form action="eprBirthConfirmationPrint" name="birthConfirmationPrintForm2" id="birth-confirmation-print-form-2" method="POST">
        <div id="civil-confirmation-info-sub-title" class="form-sub-title">
            * in Sinhala <br>* in Tamil *<br> Changes in Names
        </div>
        <div id="bcf-child-current-name" class="font-9">
            <div class="no">11</div>
            <label>ළම‌යාගේ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ) <br>பிறப்பு அத்... (சிங்களம் / தமிழ்) <br>Childs name in the official languages (Sinhala / Tamil)</label>
            <s:textfield cssClass="disable" disabled="true" value="%{#session.birthRegister.child.childFullNameOfficialLang}" ></s:textfield>
        </div>
        <div id="bcf-child-current-name-in-english" class="font-9">
            <div class="no">12</div>
            <label>ළම‌යාගේ නම ඉංග්‍රීසි භාෂාවෙන්<br>பிறப்பு ...   <br>Childs name in English</label>
            <s:textfield cssClass="disable" disabled="true" value="%{#session.birthRegister.child.childFullNameEnglish}" ></s:textfield>
        </div>
        <div id="bcf-father-current-name" class="font-9">
            <div class="no">13</div>
            <label>පියාගේ සම්පුර්ණ නම  <br>தந்நையின் முழுப் பெயர்<br>Father's Full Name</label>
            <s:textfield cssClass="disable" disabled="true" value="%{#session.birthRegister.parent.fatherFullName}" ></s:textfield>
        </div>

        <div id="bcf-mother-current-name" class="font-9">
            <div class="no">14</div>
            <label>මවගේ සම්පූර්ණ නම <br>தாயின் முழுப் பெயர்<br>Mother's Full Name</label>
            <s:textfield cssClass="disable" disabled="true" value="%{#session.birthRegister.parent.motherFullName}" ></s:textfield>
        </div>
       
        <s:hidden name="pageNo" value="2"/>
        <s:submit value="%{getText('next.label')}"/>
            <s:submit value="%{getText('print.label')}" onclick="print()" type="button"/>
                 </div>
    </s:form>
</div>