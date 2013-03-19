<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>

<script>
    $(function() {
        $("#datePicker").datepicker({
            changeYear: true,
            dateFormat:'yy-mm-dd',
            startDate:'2000-01-01',
            endDate:'2020-12-31'
        });
    });

    $(function() {
        $('img#confirmant_lookup').bind('click', function(evt1) {
            var id1 = $("input#confirmantNICorPIN").attr("value");

            $("textarea#confirmantFullName").val('');
            $.getJSON('/ecivil/prs/PersonLookupService', {pinOrNic:id1},
                    function(data1) {
                        $("textarea#confirmantFullName").val(data1.fullNameInOfficialLanguage);
                    });
        });
    });

    function setConfirmPerson(id, nICorPIN, name) {
        var confirmantName = document.getElementById("confirmantFullName");
        var confirmantNICorPIN = document.getElementById("confirmantNICorPIN");

        confirmantName.value = name;
        confirmantNICorPIN.value = nICorPIN;
    }

    var errormsg = "";
    function validate() {
        var domObject;
        var returnval = true;

        // validate confirmant type
        checkConfirmantType();

        // validate confirmant PIN or NIC
        domObject = document.getElementById('confirmantNICorPIN');
        if (!isFieldEmpty(domObject))
            validatePINorNIC(domObject, 'invalid', 'confirmantPINorNIC');

        // validate confirmant full name
        domObject = document.getElementById('confirmantFullName');
        if (isFieldEmpty(domObject)) {
            isEmpty(domObject, '', 'error3');
        }

        // validate confirmant confirming date
        domObject = document.getElementById('datePicker');
        if (isFieldEmpty(domObject))
            isEmpty(domObject, '', 'error4');
        else
            isDate(domObject.value, 'invalid', 'infomantDate');

        if (errormsg != "") {
            alert(errormsg);
            returnval = false;
        }
        errormsg = "";
        return returnval;
    }

    // check confirmant type selected
    function checkConfirmantType() {
        var mother = document.getElementById('birth-confirmation-form3_confirmant_confirmantTypeMOTHER').checked;
        var father = document.getElementById('birth-confirmation-form3_confirmant_confirmantTypeFATHER').checked;
        var guardian = document.getElementById('birth-confirmation-form3_confirmant_confirmantTypeGUARDIAN').checked;
        confirmantAvailable(mother, father, guardian);
    }

    function confirmantAvailable(inf1, inf2, inf3) {
        if (!(inf1 || inf2 || inf3))
            errormsg = errormsg + "\n" + document.getElementById('error1').value;
    }

    function initPage() {
    }
</script>

<div id="birth-confirmation-form-outer">
    <s:form action="eprBirthConfirmation" name="birthConfirmationForm3" method="POST" id="birth-confirmation-form3"
            onsubmit="javascript:return validate()">

        <table class="table_reg_page_03" cellspacing="0">
            <caption></caption>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <col/>
            <tbody>
            <tr>
                <td colspan="7" style="text-align:center;font-size:12pt">උපත තහවුරු කරන්නාගේ විස්තර
                    <br>பிறப்பினை உறுதிப்படுத்துபவரின் விபரம்
                    <br>Person Confirming Birth Details
                </td>
            </tr>

            <tr>
                <td width="40px">20</td>
                <td colspan="1"><label>තහවුරු කරන්නේ කවුරුන් විසින් ද?
                    <s:label value="*" cssStyle="color:red;font-size:10pt"/>
                    <br>பிறப்பினை உறுதிப்படுத்துவது யாரால்?<br>Person
                    Confirming Information</label></td>
                <td>
                    <table class="sub_table">
                        <tr>
                            <td><label>මව <br>மாதா <br>Mother</label></td>
                            <td align="center" width="150px">
                                <s:radio name="confirmant.confirmantType" value="confirmant.confirmantType"
                                         list="#@java.util.HashMap@{'MOTHER':''}"
                                         onchange="javascript:setConfirmPerson('MOTHER',
            '%{parent.motherNICorPIN}', '%{parent.motherFullName}')"/>
                            </td>
                        </tr>
                    </table>
                </td>
                <td colspan="2" style="width:180px">
                    <table class="sub_table">
                        <tr>
                            <td><label>පියා<br> பிதா <br>Father</label></td>
                            <td align="center" width="150px">
                                <s:radio name="confirmant.confirmantType" value="confirmant.confirmantType"
                                         list="#@java.util.HashMap@{'FATHER':''}"
                                         onchange="javascript:setConfirmPerson('FATHER', '%{parent.fatherNICorPIN}',
                                 '%{parent.fatherFullName}')"/>
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <table class="sub_table">
                        <tr>
                            <td><label>භාරකරු<br> பாதுகாவலர் <br>Guardian</label></td>
                            <td align="center" width="150px">
                                <s:radio name="confirmant.confirmantType" value="confirmant.confirmantType"
                                         list="#@java.util.HashMap@{'GUARDIAN':''}"
                                         onchange="javascript:setConfirmPerson('GUARDIAN','','')"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

            <tr>
                <td>21</td>
                <td colspan="4"><label>
                    උපත තහවුරු කරන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැඳුනුම්පත් අංකය
                    <br>பிறப்​பை உறுதிப்படுத்துபவரின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை எண்
                    <br>PIN / NIC of person confirming the birth details
                </label></td>
                <td colspan="3" class="find-person">
                    <img src="<s:url value="/images/alphabet-V.gif" />"
                         id="confirmant_NIC_V" onclick="javascript:addXorV('confirmantNICorPIN','V','error5')">
                    <img src="<s:url value="/images/alphabet-X.gif" />"
                         id="confirmant_NIC_X" onclick="javascript:addXorV('confirmantNICorPIN','X','error5')">
                    <br>
                    <s:textfield name="confirmant.confirmantNICorPIN" id="confirmantNICorPIN" maxLength="12"/>
                    <img src="<s:url value="/images/search-father.png"/>" style="vertical-align:middle;"
                         id="confirmant_lookup"/>
                </td>
            </tr>

            <tr>
                <td>22</td>
                <td colspan="1"><label>
                    උපත තහවුරු කරන්නාගේ සම්පූර්ණ නම<s:label value="*" cssStyle="color:red;font-size:10pt"/>
                    <br>பிறப்​பை உறுதிப்படுத்துபவரின் முழுப் பெயர்
                    <br>Full Name of the person confirming the birth details</label></td>
                <td colspan="4"><s:textarea name="confirmant.confirmantFullName" id="confirmantFullName" onchange="checkSyntax('confirmantFullName')"
                                            cssStyle="width:98%;"/></td>
            </tr>
            <tr>
                <td>23</td>
                <td colspan="2"><label> ඉහත සදහන් තොරතුරු නිවැරදි බව සහතික කරමි
                    <br>மேற்குறிப்பிட்ட விபரங்கள் சரியானவை என இத்தால் உறுதிப்படுத்துகிறேன்.
                    <br>I hereby certify that the above information are correct </label></td>
                <td colspan="1"><label>දිනය <s:label value="*"
                                                     cssStyle="color:red;font-size:10pt"/><br>திகதி<br>Date</label></td>
                <td colspan="4">
                        <s:label value="YYYY-MM-DD" cssStyle="margin-left:10px;font-size:10px"/><br>
                        <s:textfield id="datePicker" name="confirmant.confirmantSignDate" cssStyle="float:left;"/>
            </tr>
            </tbody>
        </table>
        <s:hidden name="pageNo" value="3"/>

        <div class="form-submit">
            <s:submit onclick="checkActiveFieldsForSyntaxErrors('birth-confirmation-form3')" value="%{getText('save.label')}"/>
        </div>
        <s:hidden name="skipConfirmationChages" value="%{#request.skipConfirmationChages}"/>
    </s:form>

    <s:if test="#request.skipConfirmationChages==false">
        <s:form action="eprBirthConfirmation.do" method="post">
            <s:hidden name="back" value="true"/>
            <s:hidden name="pageNo" value="%{pageNo - 1}"/>
            <s:hidden name="skipConfirmationChages" value="%{#request.skipConfirmationChages}"/>
            <s:hidden value="%{#request.bdId}" name="bdId"/>
            <div class="form-submit">
                <s:submit value="%{getText('previous.label')}"/>
            </div>
        </s:form>
    </s:if>

    <s:hidden id="error1" value="%{getText('confirmant.person.value')}"/>
    <s:hidden id="error2" value="%{getText('cp3.error.NIC.value')}"/>
    <s:hidden id="error3" value="%{getText('cp3.error.FullName.value')}"/>
    <s:hidden id="error4" value="%{getText('cp3.error.confirm.date.value')}"/>
    <s:hidden id="invalid" value="%{getText('p1.invalide.inputType')}"/>
    <s:hidden id="infomantDate" value="%{getText('confirmant.confirmDate.text')}"/>
    <s:hidden id="confirmantPINorNIC" value="%{getText('confirmant.PINorNIC.text')}"/>
    <s:hidden id="error5" value="%{getText('NIC.error.add.VX')}"/>

</div>
