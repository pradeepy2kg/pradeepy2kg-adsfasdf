<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 10, 2010
  Time: 4:01:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-registration-form-outer">
    <s:form action="eprBirthRegistration.do" name="birthRegistrationForm4" id="birth-registration-form-4" method="POST">
        <div id="birth-registration-form-notify-autority-sub-title" class="form-sub-title">
            *in Sinhala<br>*in Tamil<br>Notifying Authority
        </div>
        <div id="notifier-nic" class="font-9">
            <label>(33) පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br>தகவல் கொடுப்பவரின் தனிநபர் அடையாள எண் / அடையாள
                அட்டை இல.<br>PIN / NIC of the Notifying Authority</label>
            <s:textfield name="notifyingAuthority.notifyingAuthorityPIN"/>
        </div>
        <div id="notifier-name" class="font-9">
            <label>(34) නම<br>கொடுப்பவரின் பெயர் <br>Name</label>
            <s:textarea name="notifyingAuthority.notifyingAuthorityName"/>
        </div>
        <div id="notifier-address" class="font-9">
            <label>තැපැල් ලිපිනය<br>தபால் முகவரி <br>Postal Address</label>
            <s:textarea name="notifyingAuthority.notifyingAuthorityAddress"/>
        </div>
        <div id="notifier-signature" class="font-9">
            <label>32) අත්සන හා නිල මුද්‍රාව<br>தகவல் ... <br>Signature and Official Seal (if available) of the
                Notifying Authority</label>
            <s:checkbox name="notifyingAuthority.notifyingAuthoritySigned"/>
        </div>
        <div id="notified-date" class="font-9">
            <label>දිනය <br>*in tamil <br>Date</label>
            <s:select list="{'2009','2010','2011'}" name="" id="fatherYear"
                      onchange="javascript:setDate('fatherYear','2')"/>
            <s:select list="{'01','02','03'}" name="" id="fatherMonth"
                      onchange="javascript:setDate('fatherMonth','2')"/>
            <s:select list="{'01','02','03'}" name="" id="fatherDay"
                      onchange="javascript:setDate('fatherDay','2')"/>
            <s:datetimepicker id="fatherDatePicker" name="fatherDOB" label="Format (yyyy-MM-dd)"
                              displayFormat="yyyy-MM-dd" onmouseover="javascript:splitDate('fatherDatePicker')"/>
        </div>
        <s:hidden name="pageNo" value="4"/>
        <s:submit value="SUBMIT"/>
    </s:form>
</div>