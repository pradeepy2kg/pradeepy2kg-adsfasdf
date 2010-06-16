<%--<html>   --%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--
 @author chathuranga
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>




<div class="birth-confirmation-form-outer">
    <s:form action="eprBirthConfirmation" name="birthConfirmation_form2" id="birth-confirmation-form-2" method="POST">
        <div id="civil-confirmation-info-sub-title" class="form-sub-title">
            * in Sinhala <br>* in Tamil *<br> Changes in Names
        </div>
        <div id="bcf-child-current-name" class="font-9">
            <div class="no">11</div>
            <label>ළම‌යාගේ නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ) <br>பிறப்பு அத்... (சிங்களம் / தமிழ்) <br>Childs name in the official languages (Sinhala / Tamil)</label>
            <s:textfield cssClass="disable" disabled="true" name="child.childFullNameOfficialLang" ></s:textfield>
        </div>
        <div id="bcf-child-new-name" class="font-9">
            <div class="no"></div>
            <label>නම වෙනස් විය යුතු  අයුරු<br>* in Tamil <br>Corrected name</label>
            <s:textarea name="child.childFullNameOfficialLang" ></s:textarea>
        </div>
        <div id="bcf-child-current-name-in-english" class="font-9">
            <div class="no">12</div>
            <label>ළම‌යාගේ නම ඉංග්‍රීසි භාෂාවෙන්<br>பிறப்பு ...   <br>Childs name in English</label>
            <s:textfield cssClass="disable" disabled="true" name="child.childFullNameEnglish" ></s:textfield>
        </div>
        <div id="bcf-child-new-name-in-english" class="font-9">
            <div class="no"></div>
            <label>නම වෙනස් විය යුතු  අයුරු<br>* in Tamil <br>Corrected name</label>
            <s:textarea name="child.childFullNameEnglish" ></s:textarea>
        </div>
        <div id="bcf-father-current-name" class="font-9">
            <div class="no">13</div>
            <label>පියාගේ සම්පුර්ණ නම  <br>தந்நையின் முழுப் பெயர்<br>Father's Full Name</label>
            <s:textfield cssClass="disable" disabled="true" name="parent.fatherFullName" ></s:textfield>
        </div>
        <div id="bcf-father-new-name" class="font-9">
            <div class="no"></div>
            <label>නම වෙනස් විය යුතු  අයුරු<br>* in Tamil <br>Corrected name</label>
            <s:textarea name="parent.fatherFullName" ></s:textarea>
        </div>
        <div id="bcf-mother-current-name" class="font-9">
            <div class="no">14</div>
            <label>මවගේ සම්පූර්ණ නම <br>தாயின் முழுப் பெயர்<br>Mother's Full Name</label>
            <s:textfield cssClass="disable" disabled="true" name="parent.motherFullName" ></s:textfield>
        </div>
        <div id="bcf-mother-new-name" class="font-9">
            <div class="no"></div>
            <label>නම වෙනස් විය යුතු  අයුරු<br>* in Tamil <br>Corrected name</label>
            <s:textarea name="parent.motherFullName" ></s:textarea>
        </div>            
        <s:hidden name="pageNo" value="2"/>
        
        <div class="form-submit">
            <s:submit value="%{getText('next.label')}" />
        </div>
        </div>
    </s:form>
</div>