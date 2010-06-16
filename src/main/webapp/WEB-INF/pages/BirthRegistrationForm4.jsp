<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 10, 2010
  Time: 4:01:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-registration-form-outer" id="birth-registration-form-4-outer">
    <s:form action="eprBirthRegistration.do" name="birthRegistrationForm4" id="birth-registration-form-4" method="POST"
            onsubmit="javascript:return validate()">
        <div id="birth-registration-form-notify-autority-sub-title" class="form-sub-title">
            *in Sinhala<br>*in Tamil<br>Notifying Authority
        </div>
        <div id="notifier-nic" class="font-9">
            <label>(33) පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br>தகவல் கொடுப்பவரின் தனிநபர் அடையாள எண் / அடையாள
                அட்டை இல.<br>PIN / NIC of the Notifying Authority</label>
            <s:textfield name="notifyingAuthority.notifyingAuthorityPIN" id="notifyingAuthorityPIN"/>
        </div>
        <div id="notifier-name" class="font-9">
            <label>(34) නම<br>கொடுப்பவரின் பெயர் <br>Name</label>
            <s:textarea name="notifyingAuthority.notifyingAuthorityName" id="notifyingAuthorityName"/>
        </div>
        <div id="notifier-address" class="font-9">
            <label>තැපැල් ලිපිනය<br>தபால் முகவரி <br>Postal Address</label>
            <s:textarea name="notifyingAuthority.notifyingAuthorityAddress"/>
        </div>
        <div id="notifier-signature" class="font-9">
            <label>32) අත්සන හා නිල මුද්‍රාව<br>தகவல் ... <br>Signature and Official Seal (if available) of the
                Notifying Authority</label>
            <s:checkbox name="notifyingAuthority.notifyingAuthoritySigned" id="notifyingAuthoritySigned"/>
        </div>
        <div id="notified-date" class="font-9">
            <label>දිනය <br>*in tamil <br>Date</label>
            <sx:datetimepicker id="modifiedDatePicker" name="notifyingAuthority.notifyingAuthoritySignDate"
                               displayFormat="yyyy-MM-dd"
                               onmouseover="javascript:splitDate('modifiedDatePicker')"/>
        </div>
        <s:hidden name="pageNo" value="4"/>

        <s:hidden id="p4error1" value="%{getText('p4.NIC.error.value.value')}"/>
        <s:hidden id="p4error2" value="%{getText('p4.Name.error.value')}"/>
        <s:hidden id="p4error3" value="%{getText('p4.Signature.error.value')}"/>

        <script type="text/javascript">
            function validate()
                {
                    var errormsg="";
                    var element;
                    var returnval;
                    var check=document.getElementById('skipjs');
                    if (!check.checked) {

                        element=document.getElementById('notifyingAuthorityPIN');
                        if (element.value=="") {
                            errormsg = errormsg +  "\n" + document.getElementById('p4error1').value;
                        }
                        element=document.getElementById('notifyingAuthorityName');
                        if (element.value=="") {
                            errormsg = errormsg + "\n" + document.getElementById('p4error2').value;
                        }
                        element=document.getElementById('notifyingAuthoritySigned');
                        if (!element.checked) {
                            errormsg = errormsg + "\n" + document.getElementById('p4error3').value;
                        }

                    }
                    if(errormsg!=""){
                        alert(errormsg);
                        returnval =false;
                    }
                    return returnval;
                    }
         </script>

        <div class="form-submit">
            <s:url id="backUrl" action="eprBirthRegistration">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
            </s:url>
            <s:a href="%{backUrl}"> << </s:a>
            <s:checkbox name="skipjavaScript" label=" Skip Validations " id="skipjs" value="false" />
            <s:submit value="%{getText('next.label')}" />
        </div>
    </s:form>
</div>