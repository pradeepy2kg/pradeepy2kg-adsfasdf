<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="adoption-applicant-info-form-outer">

    <form action="eprAdoptionFind.do" method="post">
        <table style=" border:1px solid #000000; width:300px">
            <tr><s:actionerror/></tr>
            <tr>
                <td>
                    <s:label value="%{getText('adoption_court_order_serial.label')}"/>
                </td>
                <td><s:textfield name="courtOrderNo" id="courtOrderNo"/></td>
            </tr>
        </table>
        <table style=" width:300px">
            <tr>

            <tr>
                <td width="200px"></td>
                <td align="right" class="button"><s:submit name="search"
                                                           value="%{getText('adoption_search_button.label')}"
                                                           cssStyle="margin-right:10px;"/></td>
            </tr>
        </table>
    </form>
    <s:form action="eprAdoptionCertificateRequest.do">
        <table border="1" class="adoption-applicant" cellspacing="0" cellpadding="0"
               style="border:1px solid #000; border-collapse:collapse;">
            <caption></caption>
            <col/>
            <col width="330px"/>
            <col width="175px"/>
            <col width="175px"/>
            <col width="175px"/>
            <col width="175px"/>
            <tbody>
            <tr>
                <td colspan="2">අයදුම් කරු <br/>
                    Applicant
                </td>
                <td>පියා   </br>
                    Father
                </td>
                <td>
                    <s:radio name="certificateApplicantType" list="#@java.util.HashMap@{'0':''}"/>
                </td>
                <td>මව <br/>
                    Mother
                </td>
                <td>
                    <s:radio name="certificateApplicantType" list="#@java.util.HashMap@{'1':''}"/>
                </td>
            </tr>
            <tr>
                <td colspan="4">
                    අයදුම් කරුගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
                    <br>
                    தாயின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம்
                    <br>
                    Applicant's PIN / NIC Number
                    <br>
                </td>
                <td colspan="2">
                    <s:textfield id="certifcateApplicantPin" name="acertifcateApplicantPin"/>
                </td>
            </tr>
            <tr>
                <td width="30px" style="background:lightgray;"></td>
                <td>
                    විදේශිකය‍කු නම්
                    <br>
                    வெளிநாட்டவர்
                    <br>
                    If a foreigner
                </td>
                <td>
                    රට
                    <br>
                    நாடு
                    <br>
                    Country
                </td>
                <td><s:textfield id="certifcateApplicantCountry" name="certifcateApplicantCountry"/></td>
                <td>
                    ගමන් බලපත්‍ර අංකය
                    <br>
                    கடவுச் சீட்டு
                    <br>
                    Passport No.
                </td>
                <td><s:textfield id="certificateApplicantPassportNo" name="certificateApplicantPassportNo"/></td>
            </tr>
            <tr>
                <td colspan="2">
                    අයදුම් කරුගේ නම
                    <br>
                    Name of the Applicant
                </td>
                <td colspan="4">
                    <s:textarea id="certificateApplicantName" name="certificateApplicantName" cssStyle="width:98.2%;"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    ලිපිනය
                    <br>
                    Address
                </td>
                <td colspan="4">
                    <s:textarea id="certificateApplicantAddress" name="certificateApplicantAddress" cssStyle="width:98.2%;"/>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="adoption-form-submit">
            <s:submit value="%{getText('adoption.submit')}" cssStyle="margin-top:10px;"/>
        </div>
    </s:form>
</div>