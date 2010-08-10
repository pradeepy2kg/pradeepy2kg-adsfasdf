<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:set value="rowNumber" name="row"/>

<script src="/popreg/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/popreg/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/popreg/lib/jqueryui/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/popreg/css/datepicker.css" type="text/css"/>


<script type="text/javascript">
    $(function() {
        $("#modifiedDatePicker").datepicker();
    });
    $(function() {
        $('img#notifier_lookup').bind('click', function(evt1) {
            var id1 = $("input#notifyingAuthorityPIN").attr("value");
            $.getJSON('http://localhost:8080/popreg/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#notifyingAuthorityName").val(data1.fullNameInOfficialLanguage);
                        $("textarea#notifyingAuthorityAddress").val(data1.lastAddress);
                    });
        });
    })
</script>

<div class="birth-registration-form-outer" id="birth-registration-form-4-outer">
    <s:form action="eprBirthRegistration.do" name="birthRegistrationForm4" id="birth-registration-form-4" method="POST"
            onsubmit="javascript:return validate()">
        <script>
            function view_NotifyerInfo() {
                dojo.event.topic.publish("view_NotifyerInfo");
            }
        </script>

        <table class="table_reg_page_04" width="100%" cellspacing="0">
            <caption></caption>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td colspan="5" style="text-align:center;font-size:12pt"> තොරතුරු වාර්තා කරන පාර්ශවය
                    <br>அதிகாரியிடம் தெரிவித்தல்
                    <br>Notifying Authority
                </td>
            </tr>
            <tr>
                <td colspan="4"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>) පුද්ගල අනන්‍යතා
                    අංකය / ජාතික හැදුනුම්පත් අංකය <br>தகவல் கொடுப்பவரின் தனிநபர்
                    அடையாள எண் / அடையாள
                    அட்டை இல.<br>PIN / NIC of the Notifying Authority</label></td>
                <td colspan="1" class="find-person" width="250px">
                    <s:textfield name="notifyingAuthority.notifyingAuthorityPIN" id="notifyingAuthorityPIN"/>
                    <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;"
                         id="notifier_lookup"/>
                </td>
            </tr>
            <tr>
                <td width="200px" colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                    නම<br>கொடுப்பவரின் பெயர் <br>Name</label></td>
                <td colspan="4">
                    <s:textarea name="notifyingAuthority.notifyingAuthorityName" id="notifyingAuthorityName"
                                cssStyle="width:98%;"/>
                </td>
            </tr>
            <tr>
                <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)තැපැල් ලිපිනය<br>தபால்
                    முகவரி <br>Postal Address</label></td>
                <td colspan="4"><s:textarea name="notifyingAuthority.notifyingAuthorityAddress"
                                            id="notifyingAuthorityAddress"
                                            cssStyle="width:98%;"/></td>
            </tr>
            <tr>
                <td width="200px"><label>දිනය <br>*in tamil <br>Date</label></td>
                <td colspan="4"><s:textfield name="notifyingAuthority.notifyingAuthoritySignDate"
                                             id="modifiedDatePicker"></s:textfield>
                </td>
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
                var dat = document.getElementsByTagName("notifyingAuthority.notifyingAuthoritySignDate");

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

        <s:if test="birthType.ordinal() == 1">
            <s:if test="bdfLateOrBelated ==1 || bdfLateOrBelated==2">
                <table class="table_reg_page_04" width="100%" cellspacing="0" style="margin-top:20px;">
                    <caption></caption>
                    <col/>
                    <col/>
                    <col/>
                    <col/>
                    <col/>
                    <tbody>
                    <tr>
                        <td colspan="5" style="text-align:center;font-size:12pt">
                            <s:if test="bdfLateOrBelated==1">
                                *Sinhala
                                <br>* Tamil
                                <br>Late Registration
                            </s:if>
                            <s:else>
                                *Sinhala
                                <br>*Tamil
                                <br>Belated Registration
                            </s:else>
                        </td>
                    </tr>
                    <tr>
                        <td width="200px"><label>*in sinhala<br>*in tamil<br>Case File Number</label></td>
                        <td colspan="2"><s:textfield name="caseFileNumber"/></td>
                    </tr>
                    <tr>
                        <td><label>*in sinhala<br>* in tamil<br>Prevoius Comments </label></td>
                        <td><s:textarea name="register.comments" disabled="true" cssStyle="width:98%;"/></td>
                    </tr>
                    <tr>
                        <td><label>*in sinhala<br>* in tamil<br>New Comments </label></td>
                        <td><s:textarea name="newComment" cssStyle="width:98%;"/></td>
                    </tr>
                    </tbody>
                </table>
            </s:if>
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