<%--
  @author duminda
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<s:form action="eprBirthRegistration.do" method="post" name="birthRegistrationForm3">--%>
<%--<div id="birth-registration-form-3-body">--%>
<%--<div id="place-of-marriage"><s:textfield name="birthRegister.placeOfMarriage"/></div>--%>
<%--<div id="date-of-marriage"><s:textfield name="" value="2005/05/01"/></div>--%>
<%--<div id="signature"><s:textfield name="motherSigned" value="true"/><input type="text" name="fatherSigned"--%>
<%--value="true"/>--%>
<%--</div>--%>
<%--<div id="grandfather-name_lk"><s:textfield name="birthRegister.grandFatherFullName"/></div>--%>
<%--<div id="grandfather-dob_lk"><s:textfield name="birthRegister.grandFatherBirthYear"/></div>--%>
<%--<div id="grandfather-birth-place_lk"><s:textfield name="birthRegister.grandFatherBirthPlace"/></div>--%>
<%--<div id="grandfather-name"><s:textfield name="birthRegister.greatGrandFatherFullName"/></div>--%>
<%--<div id="grandfather-dob"><s:textfield name="birthRegister.greatGrandFatherBirthYear"/></div>--%>
<%--<div id="grandfather-birth-place"><s:textfield name="birthRegister.greatGrandFatherBirthPlace"/></div>--%>
<%--<div id="info-person">--%>
<%--<div id="mother"><s:checkbox name="birthRegister.informantType" value="1"/></div>--%>
<%--<div id="father"><s:checkbox name="birthRegister.informantType" value="0"/></div>--%>
<%--<div id="holder"><s:checkbox name="birthRegister.informantType" value="2"/></div>--%>
<%--</div>--%>
<%--<div id="info-person-name"><s:textfield name="birthRegister.informantName"/></div>--%>
<%--<div id="info-person-nic"><s:textfield name="birthRegister.informantNICorPIN"/></div>--%>
<%--<div id="info-person-address"><s:textfield name="birthRegister.informantAddress"/></div>--%>
<%--<div id="info-person-phone"><s:textfield name="birthRegister.informantPhoneNo"/></div>--%>
<%--<div id="info-person-email"><s:textfield name="birthRegister.informantEmail"/></div>--%>
<%--<div id="info-person-signature">--%>
<%--<s:datetimepicker id="datePicker" name="informantSignDate" label="Format (yyyy-MM-dd)" displayFormat="yyyy-MM-dd"--%>
<%--onmouseover=""/></div>--%>
<%--</div>--%>
<%--<s:hidden name="pageNo" value="3"/>--%>
<%--<s:hidden name="birthRegister.parentsMarried" value="0"/>--%>
<%--<div class="button"><s:submit value="NEXT"/></div>--%>
<%--</s:form>--%>


<div class="birth-registration-form-outer">
    <form action="#" name="#" id="birth-registration-form-3" method="POST">
        <div id="birth-registration-form-marriage-info-sub-title" class="form-sub-title">
            *in Sinhala<br>*in Tamil<br>Details of the Marriage
        </div>
        <div id="parent-marital-status" class="font-9">
            <div class="form-label">
                <label>(25)මව්පියන් විවාහකද? <br>பெற்றோர்கள் மணம் முடித்தவர்களா? <br>Were Parent's Married?</label>
            </div>
            <div id="yes-no">
                <label>*in sinhala<br>*in tamil<br>Yes</label>

                <div>
                    <input type="checkbox" name=""/>
                </div>
                <label>*in sinhala<br>*in tamil<br>No</label>

                <div>
                    <input type="checkbox" name=""/>
                </div>
                <label>*in sinhala<br>*in tamil<br>Since Married</label>

                <div>
                    <input type="checkbox" name=""/>
                </div>
            </div>
            <div id="place-of-marriage">
                <label>විවාහ වු ස්ථානය<br>விவாகம் இடம்பெற்ற இடம் <br>Place of Marriage</label>

                <div>
                    <input type="text" name=""/>
                </div>
            </div>
            <div id="date-of-marriage">
                <label>විවාහ වු දිනය<br>விவாகம் இடம்பெற்ற திகதி <br>Date of Marriage</label>

                <div>
                    <input type="text" name=""/>
                </div>
            </div>
        </div>
        <div id="parent-not-married" class="font-9">
            <div class="form-label">
                <label>(26)මව්පියන් විවාහ වි නොමැති නම් පියාගේ තොරතුරු ඇතුලත් කර ගැනිම සදහා මව සහ පියාගේ අත්සන් <br>பெற்றோர்
                    மணம் செய்யாதிருப்பின், தகப்பனின் தகவல்கள் பதிவு செய்ய வேண்டுமெனின் பெற்றோரின் கையொப்பம்<br>If
                    parents are not married, signatures of mother and father to include father's particulars</label>
            </div>
            <div id="mother-signature" class="font-9">
                <div>
                    <label>මවගේ අත්සන <br> தாயின் ஒப்பம் <br>Mother’s Signature</label>
                </div>
                <div>
                    <input type="checkbox" name=""/>
                </div>
            </div>
            <div id="father-signature" class="font-9">
                <div>
                    <label>පියාගේ අත්සන <br>தகப்பனின் ஒப்பம் <br>Father’s Signature</label>
                </div>
                <div>
                    <input type="checkbox" name=""/>
                </div>
            </div>
        </div>
        <div id="birth-registration-form-grand-father-info-sub-title" class="form-sub-title">
            *in Sinhala<br>*in Tamil<br>Details of the Grand Father / Great Grand Father
        </div>
        <div id="grand-father-in-sri-lanka" class="font-9">
            <label>(27)ළමයාගේ මුත්තා ශ්‍රී ලංකාවේ උපන්නේ නම් <br>பிள்ளையின் பாட்டனார் இலங்கையில் பிறந்திருந்தால் <br>If
                grandfather of the child born in Sri Lanka</label>
        </div>
        <div id="grand-father-info">
            <div id="grand-father-name" class="font-9">
                <label>ඔහුගේ සම්පුර්ණ නම<br>அவரின் முழுப் பேயர் <br>His Full Name</label>
                <textarea name=""></textarea>
            </div>
            <div id="grand-father-yob" class="font-9">
                <label>ඔහුගේ උපන් වර්ෂය <br>அவர் பிறந்த வருடம் <br>His Year of Birth</label>
                <input type="text" name=""/>
            </div>
            <div id="grand-father-pob" class="font-9">
                <label>උපන් ස්ථානය <br>அவர் பிறந்த இடம் <br>Place Of Birth</label>
                <input type="text" name=""/>
            </div>
        </div>
        <div id="great-grand-father-in-sri-lanka" class="font-9">
            <label>(28)ළමයාගේ පියා ශ්‍රී ලංකාවේ නොඉපිද මීමුත්තා ලංකාවේ උපන්නේ නම් මී මුත්තාගේ <br>பிள்ளையின் தந்தை
                இலங்கையில் பிறக்காமல் பூட்டன் இலங்கையில் பிறந்திருந்தால் பூட்டனாரின் தகவல்கள்<br>If the father was not
                born in Sri Lanka and if great grandfather born in Sri Lanka great grand father's</label>
        </div>
        <div id="great-grand-father-info">
            <div id="great-grand-father-name" class="font-9">
                <label>සම්පුර්ණ නම <br>முழுப் பெயர் <br>Full Name</label>
                <textarea name=""></textarea>
            </div>
            <div id="great-grand-father-yob" class="font-9">
                <label>උපන් වර්ෂය <br>பிறந்த வருடம் <br>Year of Birth</label>
                <input type="text" name=""/>
            </div>
            <div id="great-grand-father-pob" class="font-9">
                <label>උපන් ස්ථානය <br>அவர் பிறந்த இடம் <br>Place Of Birth</label>
                <input type="text" name=""/>
            </div>
        </div>
        <div id="birth-registration-form-informant-info-sub-title" class="form-sub-title">
            දැනුම් දෙන්නාගේ විස්තර<br>அறிவிப்பு கொடுப்பவரின் தகவல்கள் <br>Details of the Informant
        </div>
        <div id="informant-person" class="font-9">
            <div class="form-label">
                <label>(29)දැනුම් දෙන්නේ කවුරුන් විසින් ද? <br>தகவல் வழங்குபவா் <br>Person Giving Information</label>
            </div>
            <div id="informant-mother">
                <label>මව <br>மாதா <br>Mother</label>
                <input type="checkbox" name=""/>
            </div>
            <div id="informant-father">
                <label>පියා<br> பிதா <br>Father</label>
                <input type="checkbox" name=""/>
            </div>
            <div id="informant-gardian">
                <label>භාරකරු<br> பாதுகாவலர் <br>Guardian</label>
                <input type="checkbox" name=""/>
            </div>
        </div>
        <div id="informant-name" class="font-9">
            <label>(30) නම <br>கொடுப்பவரின் பெயர் <br>Name</label>
            <textarea name=""></textarea>
        </div>
        <div id="informant-nic" class="font-9">
            <label>(31)දැනුම් දෙන්නාගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය<br>தகவல் கொடுப்பவரின் தனிநபர்
                அடையாள எண் / அடையாள அட்டை இல. <br>PIN / NIC of the Informant</label>
            <input type="text" name=""/>
        </div>
        <div id="informant-address" class="font-9">
            <label>(32)තැපැල් ලිපිනය<br>தபால் முகவரி <br>Postal Address</label>
            <textarea name=""></textarea>
        </div>
        <div id="informant-telephone" class="font-9">
            <label>දුරකතනය<br>தொலைபேசி இலக்கம் <br>Telephone</label>
            <input type="text" name=""/>
        </div>
        <div id="informant-email" class="font-9">
            <label>ඉ -තැපැල <br>மின்னஞ்சல் <br>Email</label>
            <input type="text" name=""/>
        </div>
        <div id="informant-signature" class="font-9">
            <label>(32) අත්සන<br>தகவல் ... <br>Signature</label>
            <input type="checkbox" name=""/>
        </div>
        <div id="informed-date" class="font-9">
            <label>දිනය <br>*in tamil<br>Date</label>
            <input type="text" name=""/>
        </div>
        <input type="submit" value="next"/>
    </form>
</div>