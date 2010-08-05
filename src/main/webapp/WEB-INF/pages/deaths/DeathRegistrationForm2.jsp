<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="death-declaration-form-2-outer">
    <s:form name="deathRegistrationForm2" action="eprDeathDeclaration" id="death-registration-form-2" method="POST">
        <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
               class="font-9">
            <col width="150px"/>
            <col width="130px"/>
            <col width="120px"/>
            <col width="130px"/>
            <col width="130px"/>
            <col width="130px"/>
            <col/>
            <tbody>
            <tr class="form-sub-title">
                <td colspan="7">ප්‍රකාශකයාගේ විස්තර<br>அறிவிப்பு கொடுப்பவரின் தகவல்கள்<br>Details of the Declarant</td>
            </tr>
            <tr>
                <td colspan="4">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>தகவநபர் அடையாள எண் / அடையாள அட்டை இல.
                    <br>PIN / NIC
                </td>
                <td colspan="3"><s:textfield id="" name="" /></td>
            </tr>
            <tr>
                <td colspan="1">නම<br>கொடுப்பவரின் பெயர்<br>Name</td>
                <td colspan="6"><s:textarea id="" name="" /></td>
            </tr>
            <tr>
                <td colspan="1">තැපැල් ලිපිනය<br>தபால் முகவரி<br>Postal Address</td>
                <td colspan="6"><s:textarea id="" name="" /></td>
            </tr>
            <tr>
                <td colspan="1">ඇමතුම් විස්තර<br>இலக்க வகை <br>Contact Details</td>
                <td colspan="1">දුරකතනය<br>தொலைபேசி இலக்கம் <br>Telephone</td>
                <td colspan="2"><s:textfield id="" name="" /></td>
                <td colspan="1">ඉ -තැපැල<br>மின்னஞ்சல்<br>Email</td>
                <td colspan="2"><s:textfield id="" name="" /></td>
            </tr>
            <tr>
                <td rowspan="2" colspan="1">දැනුම් දෙන්නේ කවරකු වශයෙන්ද<br>*in tamil<br>Capacity for giving information
                </td>
                <td colspan="1">මව <br>*in tamil<br>Mother</td>
                <td colspan="1"><s:checkbox id="" name="" /></td>
                <td colspan="1">පියා<br>*in tamil<br>Father</td>
                <td colspan="1"><s:checkbox id="" name="" /></td>
                <td colspan="1">සහෝදරයා සහෝදරිය<br>*in tamil<br>Brother / Sister</td>
                <td colspan="1"><s:checkbox id="" name="" /></td>
            </tr>
            <tr>
                <td colspan="1">පුත්‍රයා / දියණිය <br>*in tamil<br>Son / Daughter</td>
                <td colspan="1"><s:checkbox id="" name="" /></td>
                <td colspan="1">නෑයන් <br>பாதுகாவலர் <br>Relative</td>
                <td colspan="1"><s:checkbox id="" name="" /></td>
                <td colspan="1">වෙනත් <br>*in tamil<br>Other</td>
                <td colspan="1"><s:checkbox id="" name="" /></td>
            </tr>
            </tbody>
        </table>

        <table border="0" class="font-9">
            <col/>
            <col width="40px"/>
            <col width="100px"/>
            <tbody>
            <tr>
                <td height="100px"><p style="text-align:justify;">ඉහත සඳහන් ප්‍රකාශය සත්‍යවුත් නිවැරදිවුත් ප්‍රකාශයක් බවට මෙයින් ප්‍රකාශ කරමි
                    20.... ක් වූ …...................................... මස ….................. දින
                    …............................................................................................ දී
                    අත්සන් කරන ලදී
                    <br>*in tamil
                    <br>I do hereby declare the above to be a true and correct statement
                    Witness my hand at
                    …....................................................................................................
                    on this …............... day of …........................... 20...</p></td>
                <td></td>
                <td style="border:1px solid #000;">ශත 25 ක මුද්දර<br>*in tamil<br>Stamp of 25 cents</td>
            </tr>
            <tr>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td>ප්‍රකාශකයාගේ අත්සන<br>*in tamil<br>Signature of Declarant</td>
                <td></td>
            </tr>
            </tbody>
        </table>

        <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse; margin-bottom:5px;"
               class="font-9">
            <col width="120px"/>
            <col width="100px"/>
            <col width="300px"/>
            <col width="120px"/>
            <col width="100px"/>
            <col/>
            <tbody>
            <tr style="border:0;">
                <td colspan="6" style="border:0; text-align:center;">පහත සඳහන් සාක්ෂි කරුවන් ඉදිරිපිටදී අත්සන් කරන ලදී<br>*in tamil<br>Subscribed in the presence of the
                    following witnesses
                </td>
            </tr>
            <tr>
                <td colspan="2">පු.අ.අ. / ජා.හැ.අ.<br>அடையாள எண் / அடையா<br>PIN / NIC</td>
                <td><s:textfield id="" name="" /></td>
                <td colspan="2">පු.අ.අ. / ජා.හැ.අ.<br>அடையாள எண் / அடையா<br>PIN / NIC</td>
                <td><s:textfield id="" name="" /></td>
            </tr>
            <tr>
                <td>නම<br>கொடு<br>Name</td>
                <td colspan="2"><s:textarea id="" name="" /></td>
                <td>නම<br>கொடு<br>Name</td>
                <td colspan="2"><s:textarea id="" name="" /></td>
            </tr>
            <tr>
                <td>ලිපිනය<br>முகவரி<br>Address</td>
                <td colspan="2"><s:textarea id="" name="" /></td>
                <td>ලිපිනය<br>முகவரி<br>Address</td>
                <td colspan="2"><s:textarea id="" name="" /></td>
            </tr>
            <tr>
                <td>අත්සන <br>தகவல் ...<br>Signature</td>
                <td colspan="2"></td>
                <td>අත්සන <br>தகவல் ...<br>Signature</td>
                <td colspan="2"></td>
            </tr>
            </tbody>
        </table>

        <s:label><p class="font-8">පු.අ.අ. / ජා.හැ.අ. = පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය</p></s:label>

        <div class="form-submit">
            <s:hidden name="pageNo" value="2"/>
            <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
        </div>
        <div class="next-previous">
            <s:url id="backUrl" action="eprDeathDeclaration.do">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
            </s:url>
            <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
        </div>
    </s:form>
</div>