<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<div id="birth-confirmation-form-outer">
    <s:form action="eprBirthConfirmation" name="birthConfirmationForm3" method="POST"
            onsubmit="javascript:return validate()">

        <table class="table_con_page_03" cellspacing="0">
            <caption></caption>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td colspan="5" style="text-align:center;font-size:12pt"> උපත තහවුරු කරන්නාගේ විස්තර <br>* in Tamil
                    <br>Person confirming the birth details
                </td>
            </tr>
            <tr>
                <td class="cell_01">15</td>
                <td colspan="3"><label>
                    උපත තහවුරු කරන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
                    <br>பிறப்​பை உறுதிப்படுத்துபவரின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை எண்
                    <br>PIN / NIC of person confirming the birth details
                </label></td>
                <td><s:textfield name="confirmant.confirmantNICorPIN" id="confirmantNICorPIN" size="50"/></td>
            </tr>
            <tr>
                <td>16</td>
                <td><label>
                    උපත තහවුරු කරන්නාගේ සම්පූර්ණ නම
                    <br>பிறப்​பை உறுதிப்படுத்துபவரின் முழுப் பெயர்
                    <br>Full Name of the person confirming the birth details</label></td>
                <td colspan="3"><s:textarea name="confirmant.confirmantFullName"
                                            id="confirmantFullName"></s:textarea></td>
            </tr>
            <tr>
                <td rowspan="2">17</td>
                <td rowspan="2"><label> ඉහත සදහන් තොරතුරු නිවැරදි බව සහතික කරමි
                    <br>மேற்குறிப்பிட்ட விபரங்கள் சரியானவை என இத்தால் உறுதிப்படுத்துகிறேன்.
                    <br>I hereby certify that the above information are correct </label></td>
                <td width="150px"><label>දිනය <br>திகதி <br>Date </label></td>
                <td colspan="2"><sx:datetimepicker id="datePicker" name="confirmant.confirmantSignDate"
                                                   displayFormat="yyyy-MM-dd"
                                                   onmouseover="javascript:splitDate('datePicker')"
                                                   value="%{'today'}"/></td>
            </tr>
            <tr>
                <td colspan="4" style="height:75px"></td>
            </tr>
            </tbody>
        </table>


        <s:hidden name="pageNo" value="3"/>
        <s:hidden id="p3error1" value="%{getText('cp3.error.NIC.value')}"/>
        <s:hidden id="p3error2" value="%{getText('cp3.error.FullName.value')}"/>
        <s:hidden id="p3error3" value="%{getText('cp3.error.confirm.date.value')}"/>

        <script type="text/javascript">
            function validate()
            {
                var errormsg = "";
                var element;
                var returnval;

                /*date related validations*/
                var submitDatePicker = dojo.widget.byId('datePicker').inputNode.value;
                var submit = new Date(submitDatePicker);
                if (!(submit.getTime())) {
                    errormsg = errormsg + "\n" + document.getElementById('p3error3').value;
                    flag = true;
                }
                element = document.getElementById('confirmantNICorPIN');
                if (element.value == "") {
                    errormsg = errormsg + "\n" + document.getElementById('p3error1').value;
                }
                element = document.getElementById('confirmantFullName');
                if (element.value == "") {
                    errormsg = errormsg + "\n" + document.getElementById('p3error2').value;
                }
                if (errormsg != "") {
                    alert(errormsg);
                    returnval = false;
                }
                return returnval;
            }
        </script>
        <div class="skip-validation">
            <s:checkbox name="skipjavaScript" id="skipjs" value="false">
                <s:label value="%{getText('skipvalidation.label')}"/>
            </s:checkbox>
        </div>
        <div class="form-submit">
            <s:submit value="%{getText('next.label')}"/>
        </div>
        <div class="next-previous">
            <s:url id="backUrl" action="eprBirthConfirmation">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
            </s:url>
            <s:a href="%{backUrl}"><img src="<s:url value='/images/previous.gif'/>" border="none"
                                        style="margin-top:10px;"/></s:a>
        </div>
    </s:form>
</div>
<%-- Styling Completed --%>
