<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="birth-confirmation-form-outer">
        <s:form action="eprBirthConfirmation"  name="birthConfirmationForm3"  method="POST">
        <div id="civil-confirmation-info-sub-title" class="form-sub-title">
            උපත තහවුරු කරන්නාගේ විස්තර <br>* in Tamil <br>Person confirming the birth details
        </div>
        <div id="bcf-confirm-person-nic" class="font-9">
            <div class="no">15</div>
            <label>
                උපත තහවුරු කරන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
                <br>பிறப்​பை உறுதிப்படுத்துபவரின் தனிநபர் அடையாள எண் /  தேசிய அடையாள அட்டை எண்
                <br>PIN / NIC of person confirming the birth details
            </label>
            <s:textfield name="birthConfirm.confirmantNICorPIN"/>
        </div>
        <div id="bcf-confirm-person-name" class="font-9">
            <div class="no">16</div>
            <label>
                උපත තහවුරු කරන්නාගේ සම්පූර්ණ නම
                <br>பிறப்​பை உறுதிப்படுத்துபவரின் முழுப் பெயர்
                <br>Full Name of the person confirming the birth details</label>
            <s:textarea name="birthConfirm.confirmantFullName"></s:textarea>
        </div>
        <div id="bcf-confirm-person-certify" class="font-9">
            <div class="no">17</div>
            <label>
                ඉහත සදහන් තොරතුරු නිවැරදි බව සහතික කරමි
                <br>மேற்குறிப்பிட்ட விபரங்கள் சரியானவை என இத்தால் உறுதிப்படுத்துகிறேன்.
                <br>I hereby certify that the above information are correct </label>
            <div id="bcf-confirm-person-certify-date">
                <label>දිනය<br>திகதி <br>Date</label>
                      <s:select list="{'2009','2010','2011'}" name="" id="submitYear"
                              onchange="javascript:setDate('submitYear','2')"/>
                    <s:select list="{'01','02','03'}" name="" id="submitMonth"
                              onchange="javascript:setDate('submitMonth','2')"/>
                    <s:select list="{'01','02','03'}" name="" id="submitDay"
                              onchange="javascript:setDate('submitDay','2')"/>
                    <s:datetimepicker id="submitDatePicker" name="confirmantSignDate" label="Format (yyyy-MM-dd)"
                                      displayFormat="yyyy-MM-dd"
                                      onmouseover="javascript:splitDate('submitDatePicker')"/>
            </div>
            <div id="bcf-confirm-person-certify-signature">
                <label>අත්සන   <br>கையொப்பம் <br>Signature</label>
                <s:checkbox name="" onclick=""/>

            </div>
        </div>
        <div id="breaker"></div>
        <div id="office-use-only" class="form-sub-title">
            කාර්යාලයේ ප්‍රයෝජනය සඳහා පමණි
            <br>அலுவலக பாவனைக்காக மட்டும்
            <br>Only for office use
        </div>
        <div id="bcf-added-to-civil-registration" class="font-9">
            <div class="no">18</div>
            <label>
                ඉහත සදහන් තොරතුරු සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියට ඇතුලත් කරන ලදී. (දිස්ත්‍රික් රෙජිස්ත්‍රාර් / අතිරේක දිස්ත්‍රික් රෙජිස්ත්‍රාර්)
                <br>மேற்குறிப்பிட்ட விபரங்கள் ‘சிவில் பதிவு அமைப்பில்’ உள்ளடக்கப்பட்டன (மாவட்டப் பதிவாளா் / மேலதிக மாவட்டப் பதிவாளர்)
                <br>Above information has been entered into the Civil Registration System.
                (District Registrar / Additional District Registrar)
            </label>
            <div id="bcf-added-to-civil-registration-date">
                <label>දිනය<br>திகதி <br>Date</label>
                    <s:select list="{'2009','2010','2011'}" name="" id="submitYear"
                              onchange="javascript:setDate('submitYear','2')"/>
                    <s:select list="{'01','02','03'}" name="" id="submitMonth"
                              onchange="javascript:setDate('submitMonth','2')"/>
                    <s:select list="{'01','02','03'}" name="" id="submitDay"
                              onchange="javascript:setDate('submitDay','2')"/>
                    <s:datetimepicker id="submitDatePicker" name="dateOfRegistration" label="Format (yyyy-MM-dd)"
                                      displayFormat="yyyy-MM-dd"
                                      onmouseover="javascript:splitDate('submitDatePicker')"/>
            </div>
            <div id="bcf-added-to-civil-registration-signature">
                <label>අත්සන   <br>கையொப்பம் <br>Signature</label>
               <s:checkbox name="" onclick=""/>
            </div>
        </div>
             <s:submit value="%{getText('next.label')}"/>
             <s:submit type="button" value="%{getText('printButton.label')}" onclick="print()"/></div>

    </s:form>
</div>
