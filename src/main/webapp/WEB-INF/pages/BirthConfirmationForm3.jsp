<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<div class="birth-confirmation-form-outer">
<s:form action="eprBirthConfirmation" name="birthConfirmationForm3" method="POST"
        onsubmit="javascript:return validate()">
    <div id="civil-confirmation-info-sub-title" class="form-sub-title">
        උපත තහවුරු කරන්නාගේ විස්තර <br>* in Tamil <br>Person confirming the birth details
    </div>
    <div id="bcf-confirm-person-nic" class="font-9">
        <div class="no">15</div>
        <label>
            උපත තහවුරු කරන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
            <br>பிறப்​பை உறுதிப்படுத்துபவரின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை எண்
            <br>PIN / NIC of person confirming the birth details
        </label>
        <s:textfield name="confirmant.confirmantNICorPIN" id="confirmantNICorPIN"/>
    </div>
    <div id="bcf-confirm-person-name" class="font-9">
        <div class="no">16</div>
        <label>
            උපත තහවුරු කරන්නාගේ සම්පූර්ණ නම
            <br>பிறப்​பை உறுதிப்படுத்துபவரின் முழுப் பெயர்
            <br>Full Name of the person confirming the birth details</label>
        <s:textarea name="confirmant.confirmantFullName" id="confirmantFullName"></s:textarea>
    </div>
    <div id="bcf-confirm-person-certify" class="font-9">
        <div class="no">17</div>
        <label>
            ඉහත සදහන් තොරතුරු නිවැරදි බව සහතික කරමි
            <br>மேற்குறிப்பிட்ட விபரங்கள் சரியானவை என இத்தால் உறுதிப்படுத்துகிறேன்.
            <br>I hereby certify that the above information are correct </label>

        <div id="bcf-confirm-person-certify-date">
            <label>දිනය<br>திகதி <br>Date</label>
            <sx:datetimepicker id="datePicker" name="confirmant.confirmantSignDate"
                               displayFormat="yyyy-MM-dd"
                               onmouseover="javascript:splitDate('datePicker')"
                               value="%{'today'}"/>
        </div>
    </div>

    </div>
    <s:hidden name="pageNo" value="3"/>
    <s:hidden id="p1error1" value="%{getText('cp4.error.NIC.value')}"/>
    <s:hidden id="p2error2" value="%{getText('cp4.error.FullName.value')}"/>

    <script type="text/javascript">
        function validate()
        {
            var errormsg="";
            var element;
            var returnval;
            var check=document.getElementById('skipjs');
            if (!check.checked) {

                element=document.getElementById('confirmantNICorPIN');
                if (element.value=="") {
                    errormsg = errormsg +  "\n" + document.getElementById('p1error1').value;
                }
                element=document.getElementById('confirmantFullName');
                if (element.value=="") {
                    errormsg = errormsg + "\n" + document.getElementById('p2error2').value;
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
        <s:url id="backUrl" action="eprBirthConfirmation">
            <s:param name="back" value="true"/>
            <s:param name="pageNo" value="{pageNo - 1}"/>
        </s:url>
        <s:a href="%{backUrl}"> << </s:a>
        <s:checkbox name="skipjavaScript" id="skipjs" value="false" >
           <s:label value="%{getText('skipvalidation.label')}"/>
        </s:checkbox>
        <s:submit value="%{getText('next.label')}" />
    </div>
</s:form>
</div>
