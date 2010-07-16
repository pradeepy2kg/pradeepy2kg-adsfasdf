<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<script>
    function view_ConfirmantInfo() {
        dojo.event.topic.publish("view_ConfirmentInfo");
    }
</script>
<div id="birth-confirmation-form-outer">
    <s:form action="eprBirthConfirmation" name="birthConfirmationForm3" method="POST" id="birth-confirmation-form3"
            onsubmit="javascript:return validate()">

        <table class="table_con_page_03" cellspacing="0">
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td colspan="9" style="text-align:center;font-size:12pt"> උපත තහවුරු කරන්නාගේ විස්තර
                    <br>பிறப்பு விபரங்களை உறுதிப்படுத்துபவர்
                    <br>Person confirming the birth details
                </td>
            </tr>
            <tr>
                <td colspan="3"><label>(29)තහවුරු කරන්නේ කවුරුන් විසින් ද? <br>*in tamil<br>Person
                    confirming
                    Information</label></td>

                <td width="100px"><label>මව <br>மாதா <br>Mother</label></td>
                <td align="center" width="150px"><s:radio name="confirmantRadio" list="#{'MOTHER':''}"
                                                          onchange="javascript:setConfirmPerson('MOTHER',
            '%{parent.motherNICorPIN}', '%{parent.motherFullName}')"/></td>
                </td>
                <td width="100px"><label>පියා<br> பிதா <br>Father</label></td>
                <td align="center" width="150px"><s:radio name="confirmantRadio" list="#{'FATHER':''}"
                                                          onchange="javascript:setConfirmPerson('FATHER',
            '%{parent.fatherNICorPIN}',
            '%{parent.fatherFullName}')"/></td>
                <td width="100px"><label>භාරකරු<br> பாதுகாவலர் <br>Guardian</label></td>
                <td align="center" width="150px">
                    <s:radio name="confirmantRadio" list="#{'GUARDIAN':''}"
                             onchange="javascript:setConfirmPerson('GUARDIAN','','')"/></td>
            </tr>
            <tr>
                <td width="10px">15</td>
                <td colspan="4"><label>
                    උපත තහවුරු කරන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
                    <br>பிறப்​பை உறுதிப்படுத்துபவரின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை எண்
                    <br>PIN / NIC of person confirming the birth details
                </label></td>
                <td colspan="4"><s:textfield name="confirmant.confirmantNICorPIN" id="confirmantNICorPIN"
                                             size="45"/><img
                        src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;"
                        onclick="javascript:view_ConfirmantInfo();return false;"></td>
            </tr>
            </tbody>
        </table>
        <s:url id="loadConfirmantInfo" action="../ajaxSupport_loadConfirmantInfo"/>
        <sx:div id="test" href="%{loadConfirmantInfo}" listenTopics="view_ConfirmentInfo"
                formId="birth-confirmation-form3"
                theme="ajax"/>
        <table class="table_con_page_03" cellspacing="0" style="border-top:none;">
            <tbody>
            <tr>
                <td rowspan="2" width="40px">17</td>
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
                <td colspan="2" style="height:75px">අත්සන<br>கையொப்பம்<br>Signature</td>
                <td colspan="2" style="height:75px"></td>
            </tr>
            </tbody>
        </table>
        <s:hidden name="pageNo" value="3"/>
        <s:hidden name="skipConfirmationChages" value="%{#request.skipConfirmationChages}"/>
        <s:hidden id="p3error1" value="%{getText('cp3.error.NIC.value')}"/>
        <s:hidden id="p3error2" value="%{getText('cp3.error.FullName.value')}"/>
        <s:hidden id="p3error3" value="%{getText('cp3.error.confirm.date.value')}"/>

        <script type="text/javascript">
            function setConfirmPerson(id, nICorPIN, name) {
                var confirmantName = document.getElementById("confirmantFullName");
                var confirmantNICorPIN = document.getElementById("confirmantNICorPIN");

                confirmantName.value = name;
                confirmantNICorPIN.value = nICorPIN;
            }

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
        <s:if test="#request.skipConfirmationChages==false">
            <div class="next-previous">
            <s:url id="backUrl" action="eprBirthConfirmation">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
                <s:param name="skipConfirmationChages" value="#request.skipConfirmationChages"/>
            </s:url>

            <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a> </s:if>


        </div>
    </s:form>
</div>
<%-- Styling Completed --%>
