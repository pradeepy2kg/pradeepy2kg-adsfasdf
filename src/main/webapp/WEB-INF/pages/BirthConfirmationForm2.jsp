<html>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--
 @author chathuranga
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form name="birthConfirmationForm2" action="eprBirthConfirmation.do" method="POST">
    <div id="birth-confirmation-form-2-body">

        <div id="birth-confirmation-father-pin">
            <s:textfield name="birthConfirm.fatherNIC"/>
        </div>
        <div id="birth-confirmation-father-name">
            <s:textarea name="birthConfirm.fatherFullName" cols="38"/>
        </div>
        <div id="birth-confirmation-fatherRace">
            <s:if test="#session.user_lang == 'en_US'"><s:select name="birthRegister.fatherRace" list="raceList"
                                                                 headerKey="0" headerValue="- Select Race -"/></s:if>
            <s:if test="#session.user_lang == 'si_LK'"><s:select name="birthRegister.fatherRace" list="raceList"
                                                                 headerKey="0" headerValue="- ජාතිය තෝරන්න -"/></s:if>
            <s:if test="#session.user_lang == 'ta_LK'"><s:select name="birthRegister.fatherRace" list="raceList"
                                                                 headerKey="0" headerValue="- In Tamil -"/> </s:if>
        </div>
        <div id="birth-confirmation-mother-pin">
            <s:textfield name="birthConfirm.motherNIC"/>
        </div>
        <div id="birth-confirmation-mother-name">
            <s:textarea name="birthConfirm.motherFullName" cols="38"/>
        </div>
        <div id="birth-confirmation-motherRace">
            <s:if test="#session.user_lang == 'en_US'"><s:select name="birthRegister.motherRace" list="raceList"
                                                                 headerKey="0" headerValue="- Select Race -"/></s:if>
            <s:if test="#session.user_lang == 'si_LK'"><s:select name="birthRegister.motherRace" list="raceList"
                                                                 headerKey="0" headerValue="- ජාතිය තෝරන්න -"/></s:if>
            <s:if test="#session.user_lang == 'ta_LK'"><s:select name="birthRegister.motherRace" list="raceList"
                                                                 headerKey="0" headerValue="- In Tamil -"/> </s:if>
        </div>
        <div id="birth-confirmation-married">
            <s:select name="birthConfirm.marriedStatus" list="{'YES','NO'}" headerKey="0"
                      headerValue="- Select Status -"/>
        </div>
        <div id="birth-confirmation-confim-nic">
            <s:textfield name="birthConfirm.confirmantNIC"/>
        </div>
        <div id="birth-confirmation-confim-name">
            <s:textarea name="birthConfirm.confirmantFullName" cols="38"/>
        </div>
        <div id="birth-confirmation-date">
            <s:select list="{'2009','2010','2011'}" name=""/>
            <s:select list="{'January','February','March'}" name=""/>
            <s:select list="{'01','02','03'}" name=""/>
        </div>
        <div id="birth-confirmation-finalize-date">
            <s:select list="{'2009','2010','2011'}" name=""/>
            <s:select list="{'January','February','March'}" name=""/>
            <s:select list="{'01','02','03'}" name=""/>
        </div>

        <s:hidden name="pageNo" value="2"/>
        <div class="button"><input type="submit" value="SUBMIT"/></div>
    </div>
</s:form>
</body>
</html>