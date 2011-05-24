<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:set value="rowNumber" name="row"/>
<s:if test="birthType.ordinal()==0">
    <%--still birth--%>
    <s:set name="row" value="28"/>
</s:if>
<s:elseif test="birthType.ordinal()==1 || birthType.ordinal()==3">
    <%--live birth--%>
    <s:set name="row" value="33"/>
</s:elseif>
<s:elseif test="birthType.ordinal()==2">
    <%--adoption--%>
    <s:set name="row" value="35"/>
</s:elseif>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<script type="text/javascript">
    $(function() {
        $("#modifiedDatePicker").datepicker({
            changeYear: true,
            yearRange: '1960:2020',
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });
    $(function() {
        $('img#notifier_lookup').bind('click', function(evt1) {
            var id1 = $("input#notifyingAuthorityPIN").attr("value");
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#notifyingAuthorityName").val(data1.fullNameInOfficialLanguage);
                        $("textarea#notifyingAuthorityAddress").val(data1.lastAddress);
                    });
        });
        //        $('#notifying_authority_NIC_V').bind('click', function() {
        //            if ($('#notifyingAuthorityPIN').val().length == 9) {
        //            $('#notifyingAuthorityPIN').val($("#notifyingAuthorityPIN").val() + "V");
        //            } else {
        //            alert(document.getElementById('error5').value);
        //        }
        //        });
        //
        //        $('#notifying_authority_NIC_X').bind('click', function() {
        //            if ($('#notifyingAuthorityPIN').val().length == 9) {
        //            $('#notifyingAuthorityPIN').val($("#notifyingAuthorityPIN").val() + "X");
        //            } else {
        //            alert(document.getElementById('error5').value);
        //        }
        //        });
    });

    var errormsg = "";
    function validate() {
        var domObject;
        var returnval;
        var signdate = new Date(document.getElementById('modifiedDatePicker').value);

        // notifier PIN or NIC
        domObject = document.getElementById('notifyingAuthorityPIN');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('p4error1').value;
        } else {
            validatePINorNIC(domObject, 'error1', 'p4error5');
        }

        // notifier name
        domObject = document.getElementById('notifyingAuthorityName');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('p4error2').value;
        }

        // notifier adderss
        domObject = document.getElementById('notifyingAuthorityAddress');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('p4error4').value;
        }

        /*date related validations*/
        domObject = document.getElementById('modifiedDatePicker');
        if (isFieldEmpty(domObject)) {
            errormsg = errormsg + "\n" + document.getElementById('p4error3').value;
        } else {
            isDate(domObject.value, 'error1', 'error2');
        }
        // notify date before submitted date
        var submit = new Date(document.getElementById('submitDatePicker').value);
        if (signdate.getTime() > submit.getTime()) {
            errormsg = errormsg + "\n" + document.getElementById('p4error6').value;
        }

        // notify date after informant date
        var inform = new Date(document.getElementById('informantDate').value);
        if (signdate.getTime() < inform.getTime()) {
            errormsg = errormsg + "\n" + document.getElementById('error7').value;
        }

        // notifying date after child date of birth
        var dob = new Date(document.getElementById('dateOfBirth').value);
        if (signdate.getTime() < dob.getTime()) {
            errormsg = errormsg + "\n" + document.getElementById('error6').value;
        }

        domObject = document.getElementById('bdfLateBelate');
        if (domObject.value == 1 || domObject.value == 2)
            checkLateOrBelated();

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

    function checkLateOrBelated() {
        var domObject;

        domObject = document.getElementById('caseFileNumber');
        isEmpty(domObject, '', 'error3');

        domObject = document.getElementById('comments');
        isEmpty(domObject, '', 'error4');
    }

    function initPage() {
        // TODO 
    }

    function maxLengthCalculate(id, max, divId) {
        var dom = document.getElementById(id).value;
        if (dom.length > max) {
            document.getElementById(divId).innerHTML = document.getElementById('maxLengthError').value + " : " + max
        }
        else {
            document.getElementById(divId).innerHTML = "";
        }
    }
    
</script>

<div class="birth-registration-form-outer" id="birth-registration-form-4-outer">
    <s:form action="eprBirthRegistration.do" name="birthRegistrationForm4" id="birth-registration-form-4" method="POST"
            onsubmit="javascript:return validate()">

        <table class="table_reg_page_04" width="100%" cellspacing="0">
            <caption></caption>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td colspan="5" style="text-align:center;font-size:12pt">
                    තොරතුරු වාර්තා කරන නිලධාරියාගේ / රෙජිස්ට්‍රාර්ගේ විස්තර
                    <br>அறிக்கையிடும் அதிகாரி/பதிவாளர் பற்றிய விபரங்கள்
                    <br>Details of the Notifying Officer / Registrar
                </td>
            </tr>
            <tr>
                <td><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                    අනන්‍යතා අංකය <s:label value="*" cssStyle="color:red;font-size:14pt;"/>
                    <br>அடையாள எண்
                    <br>Identification Number</label></td>
                <td colspan="8" class="find-person" width="250px">
                    <img src="<s:url value="/images/alphabet-V.gif" />"
                         id="notifying_authority_NIC_V"
                         onclick="javascript:addXorV('notifyingAuthorityPIN','V','error5')">
                    <img src="<s:url value="/images/alphabet-X.gif" />"
                         id="notifying_authority_NIC_X"
                         onclick="javascript:addXorV('notifyingAuthorityPIN','X','error5')">
                    <br>
                    <s:textfield name="notifyingAuthority.notifyingAuthorityPIN" id="notifyingAuthorityPIN"
                                 maxLength="12"/>
                    <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;"
                         id="notifier_lookup"/>
                        <%--<s:label value="*" cssStyle="color:red;font-size:15pt"/>--%>
                </td>
            </tr>
            <tr>
                <td width="200px" colspan="1"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)
                    නම<s:label value="*" cssStyle="color:red;font-size:14pt;"/><br>கொடுப்பவரின் பெயர் <br>Name</label>
                </td>
                <td colspan="4">
                    <%--<s:textarea name="notifyingAuthority.notifyingAuthorityName" id="notifyingAuthorityName"
                                cssStyle="width:95%;"/>
                        &lt;%&ndash;<s:label value="*" cssStyle="color:red;font-size:15pt"/>&ndash;%&gt;--%>
                    <s:textarea name="notifyingAuthority.notifyingAuthorityName" id="notifyingAuthorityName" cssStyle="width:95%;"
                            onblur="maxLengthCalculate('notifyingAuthorityName','120','notifyingAuthorityName_div');"/>
                <div id="notifyingAuthorityName_div" style="color:red;font-size:8pt"></div>
                </td>
            </tr>
            <tr>
                <td width="200px"><label>(<s:property value="#row"/><s:set name="row" value="#row+1"/>)තැපැල්
                    ලිපිනය<s:label value="*" cssStyle="color:red;font-size:14pt;"/>
                    <br>தபால் முகவரி<br>Postal Address</label></td>
                <td colspan="4">
                    <%--<s:textarea name="notifyingAuthority.notifyingAuthorityAddress"
                                            id="notifyingAuthorityAddress"
                                            cssStyle="width:95%;"/>
                        &lt;%&ndash;<s:label value="*" cssStyle="color:red;font-size:15pt"/>&ndash;%&gt;--%>
                    <s:textarea name="notifyingAuthority.notifyingAuthorityAddress" id="notifyingAuthorityAddress" cssStyle="width:95%;"
                            onblur="maxLengthCalculate('notifyingAuthorityAddress','255','notifyingAuthorityAddress_div');"/>
                <div id="notifyingAuthorityAddress_div" style="color:red;font-size:8pt"></div>
                </td>
            </tr>
            <tr>
                <td width="200px"><label>දිනය<s:label value="*" cssStyle="color:red;font-size:14pt;"/> <br>திகதி <br>Date</label>
                </td>
                <td colspan="4">
                    <s:label value="YYYY-MM-DD" cssStyle="float:left;margin-left:1%;font-size:10px"/><br>
                    <s:textfield name="notifyingAuthority.notifyingAuthoritySignDate" id="modifiedDatePicker"
                                 cssStyle="float:left;margin-right:60px;" maxLength="10"/>
                        <%--<s:label value="*" cssStyle="color:red;font-size:15pt"/>--%>
                </td>
            </tr>
            </tbody>
        </table>

        <s:hidden name="pageNo" value="4"/>

        <s:if test="birthType.ordinal() == 1 || birthType.ordinal() == 3">
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
                                පමා වූ උපත් ලියාපදිංචිය
                                <br>தாமதித்த பிறப்புப் பதிவு
                                <br>Late Birth Registration
                            </s:if>
                            <s:else>
                                කල් පසු වූ උපත් ලියාපදිංචිය
                                <br>காலங் கடந்த பிறப்புப் பதிவு
                                <br>Belated Birth Registration
                            </s:else>
                        </td>
                    </tr>
                    <tr>
                        <td width="200px"><label>ලිපිගොනු අංකය
                            <s:label value="*" cssStyle="color:red;font-size:14pt;"/>
                            <br>கோவை இலக்கம் <br>Case File Number</label></td>
                        <td colspan="2"><s:textfield name="register.caseFileNumber" id="caseFileNumber"/></td>
                    </tr>
                    <tr>
                        <td><label>අදහස් දක්වන්න <s:label value="*" cssStyle="color:red;font-size:14pt;"/>
                            <br>கருத்தினை தெரிவிக்கவும் <br>Add Comments </label></td>
                        <td><s:textarea name="register.comments" id="comments" cssStyle="width:98%;" rows="10"/></td>
                    </tr>
                    </tbody>
                </table>
            </s:if>
        </s:if>
        <div class="form-submit">
            <s:if test="%{#session.birthRegister.idUKey==0}">
                <s:submit value="%{getText('add.label')}"/>
            </s:if>
            <s:else>
                <s:submit value="%{getText('save.label')}"/>
            </s:else>
        </div>
        <div class="next-previous">
            <s:url id="backUrl" action="eprBirthRegistration">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
            </s:url>
            <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
        </div>
    </s:form>
    <s:hidden id="error1" value="%{getText('p1.invalide.inputType')}"/>
    <s:hidden id="error2" value="%{getText('notifierDate.text')}"/>
    <s:hidden id="p4error1" value="%{getText('p4.NIC.error.value')}"/>
    <s:hidden id="p4error2" value="%{getText('p4.Name.error.value')}"/>
    <s:hidden id="p4error3" value="%{getText('p4.submitDate.error.value')}"/>
    <s:hidden id="p4error4" value="%{getText('p4.notifierAddress.text')}"/>
    <s:hidden id="p4error5" value="%{getText('notifierNIC.text')}"/>
    <s:hidden id="p4error6" value="%{getText('p4.notifydate.with.reg.date')}"/>
    <s:hidden id="submitDatePicker" value="%{register.dateOfRegistration}"/>
    <s:hidden id="dateOfBirth" value="%{child.dateOfBirth}"/>
    <s:hidden id="informantDate" value="%{informant.informantSignDate}"/>
    <s:hidden id="bdfLateBelate" name="bdfLateOrBelated"/>
    <s:hidden id="error3" value="%{getText('late.caseFileNo.text')}"/>
    <s:hidden id="error4" value="%{getText('late.comments.text')}"/>
    <s:hidden id="error5" value="%{getText('NIC.error.add.VX')}"/>
    <s:hidden id="error6" value="%{getText('notifyDate.and.birthDate')}"/>
    <s:hidden id="error7" value="%{getText('notifyDate.and.informDate')}"/>
    <s:hidden id="maxLengthError" value="%{getText('error.max.length')}"/>

</div>
