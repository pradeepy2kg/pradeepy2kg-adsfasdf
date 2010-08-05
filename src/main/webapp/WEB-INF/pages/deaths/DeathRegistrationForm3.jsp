<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="death-declaration-form-3-outer">
    <s:form name="deathRegistrationForm3" action="eprDeathDeclaration" id="death-registration-form-3" method="POST">
        <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
               class="font-9">
            <col width="150px"/>
            <col width="400px"/>
            <col width="100px"/>
            <col/>
            <tbody>
            <tr class="form-sub-title">
                <td colspan="4">තොරතුරු වාර්තා කරන පාර්ශවය<br>அதிகாரியிடம் தெரிவித்தல்<br>Notifying Authority</td>
            </tr>
            <tr>
                <td colspan="2">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>அடையாள எண் / அடையாள அட்டை இல. <br>PIN /
                    NIC
                </td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td colspan="1">නම<br>கொடுப்பவரின் பெயர்<br>Name</td>
                <td colspan="3"></td>
            </tr>
            <tr>
                <td colspan="1">තැපැල් ලිපිනය<br>தபால் முகவரி<br>Postal Address</td>
                <td colspan="3"></td>
            </tr>
            <tr>
                <td colspan="1">අත්සන හා නිල මුද්‍රාව<br>தகவல் ...<br>Signature and Official Seal of the Notifying
                    Authority
                </td>
                <td colspan="1"></td>
                <td colspan="1">දිනය<br>திகதி<br>Date</td>
                <td colspan="1"></td>
            </tr>
            </tbody>
        </table>
        <hr style=" border:1px dashed;"/>

        <table border="1" style="width: 100%; border:1px solid #000; border-collapse:collapse;"
               class="font-9">
            <col width="150px"/>
            <col width="140px"/>
            <col width="100px"/>
            <col width="140px"/>
            <col width="100px"/>
            <col width="140px"/>
            <col width="100px"/>
            <col/>
            <tbody>
            <tr class="form-sub-title">
                <td colspan="8">මරණය ලියාපදිංචි කිරීමේ සහතිකය<br>*in tamil<br>Certificate of Registration of Death</td>
            </tr>
            <tr>
                <td colspan="1">අනුක්‍රමික අංකය හා දිනය<br>எனும் படிவத்தின் தொடா் இலக்கம் <br>Serial Number and Date</td>
                <td colspan="7"></td>
            </tr>
            <tr>
                <td colspan="1">දිස්ත්‍රික්කය / மாவட்டம் / District</td>
                <td colspan="7"></td>
            </tr>
            <tr>
                <td colspan="1">ලියාපදිංචි කිරීමේ කොට්ඨාශය / <br>பிரிவு / <br>Registration Division</td>
                <td colspan="7"></td>
            </tr>
            <tr>
                <td colspan="1">පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>*in tamil<br>PIN / NIC of departed</td>
                <td colspan="7"></td>
            </tr>
            <tr>
                <td colspan="1">මරණයට පත් වූ පුද්ගලයාගේ නම<br>பிள்ளையின் பெயர்<br>Name of person departed</td>
                <td colspan="7"></td>
            </tr>
            <tr>
                <td colspan="1">මරණය සිදුවූ දිනය<br>பிறந்த திகதி<br>Date of Death</td>
                <td colspan="1"></td>
                <td colspan="1">වයස <br>பிறப்ப<br>Age</td>
                <td colspan="1"></td>
                <td colspan="1">ස්ත්‍රී පුරුෂ භාවය<br>பால்<br>Gender</td>
                <td colspan="1"></td>
                <td colspan="1">ජාතිය<br>பிறப்<br>Race</td>
                <td colspan="1"></td>
            </tr>
            <tr>
                <td colspan="1">මරණය සිදුවූ  ස්ථානය<br>பிறந்த இடம்<br>Place of death</td>
                <td colspan="7"></td>
            </tr>
            <tr>
                <td colspan="1">මරණයට හේතුව<br>*in tamil<br>Cause of death</td>
                <td colspan="7"></td>
            </tr>
            <tr>
                <td colspan="1">ප්‍රකාශකයාගේ නම<br>தந்தையின் பெயர்<br>Declarant's Name</td>
                <td colspan="7"></td>
            </tr>
            <tr>
                <td rowspan="2" colspan="1">තොරතුරු වාර්තා කරන්නාගේ නම,
අත්සන හා නිල මුද්‍රාව<br>தகவல் கொடுப்பவரின் பெயர், கையொப்பம்,
அலுவலக முத்திரை (இருத்தால்)<br>Name, Signature & Official Seal 
of the Notifying Authority</td>
                <td colspan="7"></td>
            </tr>
            <tr>
                <td colspan="5" height="60px"></td>
                <td colspan="1">දිනය<br>திகதி <br>Date</td>
                <td colspan="1"></td>
            </tr>
            </tbody>
        </table>

        <div class="form-submit">
            <s:hidden name="pageNo" value="3"/>
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