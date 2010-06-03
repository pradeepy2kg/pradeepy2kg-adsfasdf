<%--<html>   --%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--
 @author chathuranga
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%--
<s:form name="birthConfirmationForm2" action="eprBirthConfirmation.do" method="POST">
    <div id="birth-confirmation-form-2-body">

        <div id="birth-confirmation-father-pin">
            <s:textfield name="birthConfirm.fatherNIC"/>
        </div>
        <div id="birth-confirmation-father-name">
            <s:textarea name="birthConfirm.fatherFullName" cols="38"/>
        </div>
        <div id="birth-confirmation-fatherRace">
            <s:if test="#session.user_lang == 'en_US'"><s:select name="birthRegister.fatherRace" list="raceList"
                                                                 headerKey="0" headerValue="- Select Race -"/></s:if>
            <s:if test="#session.user_lang == 'si_LK'"><s:select name="birthRegister.fatherRace" list="raceList"
                                                                 headerKey="0" headerValue="- ජාතිය තෝරන්න -"/></s:if>
            <s:if test="#session.user_lang == 'ta_LK'"><s:select name="birthRegister.fatherRace" list="raceList"
                                                                 headerKey="0" headerValue="- In Tamil -"/> </s:if>
        </div>
        <div id="birth-confirmation-mother-pin">
            <s:textfield name="birthConfirm.motherNIC"/>
        </div>
        <div id="birth-confirmation-mother-name">
            <s:textarea name="birthConfirm.motherFullName" cols="38"/>
        </div>
        <div id="birth-confirmation-motherRace">
            <s:select name="birthRegister.motherRace" list="raceList"
                                                                 headerKey="0" headerValue="%{getText('race.label')}"/>
        </div>
        <div id="birth-confirmation-married">
            <s:select name="birthConfirm.marriedStatus" list="{'YES','NO'}" headerKey="0"
                      headerValue="%{getText('status.label')}"/>
        </div>
        <div id="birth-confirmation-confim-nic">
            <s:textfield name="birthConfirm.confirmantNIC"/>
        </div>
        <div id="birth-confirmation-confim-name">
            <s:textarea name="birthConfirm.confirmantFullName" cols="38"/>
        </div>
        <div id="birth-confirmation-date">
            <s:select list="{'2009','2010','2011'}" name=""/>
            <s:select list="{'January','February','March'}" name=""/>
            <s:select list="{'01','02','03'}" name=""/>
        </div>
        <div id="birth-confirmation-finalize-date">
            <s:select list="{'2009','2010','2011'}" name=""/>
            <s:select list="{'January','February','March'}" name=""/>
            <s:select list="{'01','02','03'}" name=""/>
        </div>

        <s:hidden name="pageNo" value="2"/>
        <div class="button"><input type="%{getText('nextButton.label')}" value="SUBMIT"/></div>
    </div>
</s:form>
<s:form id="print_2">
    <div class="button_print">
        <s:submit type="button" value="%{getText('printButton.label')}" onclick="print()"/></div>
</s:form>
</body>
</html>       --%>


<div class="birth-confirmation-form-outer">
    <s:form action="eprBirthConfirmation" name="birthConfirmation_form2" id="birth-confirmation-form-2" method="POST">
        <div id="civil-confirmation-info-sub-title" class="form-sub-title">
            * in Sinhala <br>* in Tamil *<br> Changes in Names
        </div>
        <div id="bcf-child-current-name" class="font-9">
            <div class="no">11</div>
            <label>ළම‌යාගේ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ) <br>பிறப்பு அத்... (சிங்களம் / தமிழ்) <br>Childs name in the official languages (Sinhala / Tamil)</label>
            <s:label value="%{#session.birthRegister.child.childFullNameOfficialLang}" ></s:label>
        </div>
        <div id="bcf-child-new-name" class="font-9">
            <div class="no"></div>
            <label>නම වෙනස් විය යුතු  අයුරු<br>* in Tamil <br>Corrected name</label>
            <s:textarea name="child.childFullNameOfficialLang" ></s:textarea>
        </div>
        <div id="bcf-child-current-name-in-english" class="font-9">
            <div class="no">12</div>
            <label>ළම‌යාගේ නම ඉංග්‍රීසි භාෂාවෙන්<br>பிறப்பு ...   <br>Childs name in English</label>
            <s:label value="%{#session.birthRegister.child.childFullNameEnglish}" ></s:label>
        </div>
        <div id="bcf-child-new-name-in-english" class="font-9">
            <div class="no"></div>
            <label>නම වෙනස් විය යුතු  අයුරු<br>* in Tamil <br>Corrected name</label>
            <s:textarea name="child.childFullNameEnglish" ></s:textarea>
        </div>
        <div id="bcf-father-current-name" class="font-9">
            <div class="no">13</div>
            <label>පියාගේ සම්පුර්ණ නම  <br>தந்நையின் முழுப் பெயர்<br>Father's Full Name</label>
            <s:label value="%{#session.birthRegister.parent.fatherFullName}" ></s:label>
        </div>
        <div id="bcf-father-new-name" class="font-9">
            <div class="no"></div>
            <label>නම වෙනස් විය යුතු  අයුරු<br>* in Tamil <br>Corrected name</label>
            <s:textarea name="parent.fatherFullName" ></s:textarea>
        </div>
        <div id="bcf-mother-current-name" class="font-9">
            <div class="no">14</div>
            <label>මවගේ සම්පූර්ණ නම <br>தாயின் முழுப் பெயர்<br>Mother's Full Name</label>
            <s:label value="%{#session.birthRegister.parent.motherFullName}" ></s:label>
        </div>
        <div id="bcf-mother-new-name" class="font-9">
            <div class="no"></div>
            <label>නම වෙනස් විය යුතු  අයුරු<br>* in Tamil <br>Corrected name</label>
            <s:textarea name="parent.motherFullName" ></s:textarea>
        </div>
       
        <s:hidden name="pageNo" value="2"/>
        <s:submit value="%{getText('next.label')}"/></div>
    </s:form>
</div>