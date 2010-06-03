<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
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
            <s:textfield name="confirmar.confirmantNICorPIN"/>
        </div>
        <div id="bcf-confirm-person-name" class="font-9">
            <div class="no">16</div>
            <label>
                උපත තහවුරු කරන්නාගේ සම්පූර්ණ නම
                <br>பிறப்​பை உறுதிப்படுத்துபவரின் முழுப் பெயர்
                <br>Full Name of the person confirming the birth details</label>
            <s:textarea name="confirmar.confirmantFullName"></s:textarea>
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
                    <sx:datetimepicker id="submitDatePicker" name="confirmantSignDate"
                                      displayFormat="yyyy-MM-dd"
                                      onmouseover="javascript:splitDate('submitDatePicker')"
                                      value="%{'today'}" />
            </div>
           
            </efrgtgss>
        </div>
       
        </div>
            <s:hidden name="pageNo" value="3"/>
             <s:submit value="%{getText('next.label')}"/>
             

    </s:form>
</div>
