<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-registration-form-outer" id="birth-registration-form-4-outer">
    <s:form action="eprBirthRegistration.do" name="birthRegistrationForm4" id="birth-registration-form-4" method="POST"
            onsubmit="javascript:return validate()">
        <script>
            function view_NotifyerInfo() {
                dojo.event.topic.publish("view_NotifyerInfo");
            }
        </script>

        <table class="table_reg_page_04" cellspacing="0">
            <caption></caption>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td colspan="5" style="text-align:center;font-size:12pt"> *in Sinhala<br>*in Tamil<br>Notifying
                    Authority
                </td>
            </tr>
            <tr>
                <td colspan="3"><label>(33) පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය <br>தகவல் கொடுப்பவரின் தனிநபர்
                    அடையாள எண் / அடையாள
                    அட்டை இல.<br>PIN / NIC of the Notifying Authority</label></td>
                <td colspan="2" class="find-person">
                    <s:textfield name="notifyingAuthority.notifyingAuthorityPIN" id="notifyingAuthorityPIN"/>
                    <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;"
                 onclick="javascript:view_NotifyerInfo();return false;">
                </td>
            </tr>
            <tr>
                <td><label>(34) නම<br>கொடுப்பவரின் பெயர் <br>Name</label></td>
                <td colspan="4">
                    <s:url id="loadNotifyerInfo" action="../ajaxSupport_loadNotifyerInfo"/>
                    <sx:div id="notifyingAuthority.notifyingAuthorityPIN" href="%{loadNotifyerInfo}"
                            listenTopics="view_NotifyerInfo" formId="birth-registration-form-4" theme="ajax"></sx:div>
                </td>
            </tr>
            <tr>
                <td><label>තැපැල් ලිපිනය<br>தபால் முகவரி <br>Postal Address</label></td>
                <td colspan="4"><s:textarea name="notifyingAuthority.notifyingAuthorityAddress"/></td>
            </tr>
            <tr>
                <td colspan="2"><label>දිනය <br>*in tamil <br>Date</label></td>
                <td colspan="4"><sx:datetimepicker id="modifiedDatePicker" name="notifyingAuthority.notifyingAuthoritySignDate"
                                       displayFormat="yyyy-MM-dd"
                                       onmouseover="javascript:splitDate('modifiedDatePicker')"/></td>
            </tr>
            </tbody>
        </table>


        <s:hidden name="pageNo" value="4"/>

        <s:hidden id="p4error1" value="%{getText('p4.NIC.error.value')}"/>
        <s:hidden id="p4error2" value="%{getText('p4.Name.error.value')}"/>
        <s:hidden id="p4error3" value="%{getText('p4.submitDate.error.value')}"/>

        <script type="text/javascript">
            function validate()
            {
                var errormsg = "";
                var element;
                var returnval;

                /*date related validations*/
                var submitDatePicker = dojo.widget.byId('modifiedDatePicker').inputNode.value;
                var submit = new Date(submitDatePicker);
                if (!(submit.getTime())) {
                    errormsg = errormsg + "\n" + document.getElementById('p4error3').value;
                    flag = true;
                }
                element = document.getElementById('notifyingAuthorityPIN');
                if (element.value == "") {
                    errormsg = errormsg + "\n" + document.getElementById('p4error1').value;
                }
                element = document.getElementById('notifyingAuthorityName');
                if (element.value == "") {
                    errormsg = errormsg + "\n" + document.getElementById('p4error2').value;
                }


                if (errormsg != "") {
                    alert(errormsg);
                    returnval = false;
                }
                return returnval;
            }
        </script>

        <s:if test="bdfLateOrBelated ==1 || bdfLateOrBelated==2">
            <div id="late-belated-registration" class="font-9">
                <div id="late-belated-registration-title" class="font-12">
                    <s:if test="bdfLateOrBelated==1">Late Registration</s:if>
                    <s:else>Belated Registration</s:else>
                </div>
                <div id="late-belated-case-file-num">
                    <label>*in sinhala<br>*in tamil<br>Case File Number</label>
                    <s:textfield name="caseFileNumber"/>
                </div>
                <div id="late-belated-prev-comments">
                    <label>*in sinhala<br>* in tamil<br>Prevoius Comments </label>
                    <s:textarea name="register.comments" disabled="true"/>
                </div>
                <div id="late-belated-new-comments">
                    <label>*in sinhala<br>* in tamil<br>New Comments </label>
                    <s:textarea name="newComment"/>
                </div>
            </div>
        </s:if>

        <div class="form-submit">              
            <s:submit value="%{getText('next.label')}"/>
        </div>
        <div class="next-previous">
             <s:url id="backUrl" action="eprBirthRegistration">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
            </s:url>
            <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
        </div>
    </s:form>
</div>
<%-- Styling Completed --%>