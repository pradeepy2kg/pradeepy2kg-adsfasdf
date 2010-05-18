<%@ page import="java.util.List" %>
<%--
  User: chathuranga
  Date: May 13, 2010
  Time: 10:38:55 AM
  Birth Registration Confirmation Page 1
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="eprBirthConfirmation_birthConfirmFinalizer.do" method="POST">
    <div id="birth-confirmation-form-2-body">

        <div id="birth-confirmation-father-pin">
            <s:label value="%{#birthRegister.serialNumber}"/>
            <s:label value="%{#request.birthRegister.serialNumber}"/>
            <s:textfield name="birthRegister.fatherNIC"/>
        </div>
        <div id="birth-confirmation-father-name">
            <s:textarea name="%{#session.confirmBirth.fatherFullName}" cols="38"/>
        </div>
        <div id="birth-confirmation-fatherRace">
            <s:textfield name="birthRegister.fatherRace"/>
        </div>
        <div id="birth-confirmation-mother-pin" >
            <s:textfield name="birthRegister.motherNIC"/>
        </div>
        <div id="birth-confirmation-mother-name">
            <s:textarea name="birthRegister.motherFullName" cols="38"/>
        </div>
        <div id="birth-confirmation-motherRace">
            <s:textfield name="birthRegister.motherRace"/>
        </div>
        <div id="birth-confirmation-married">
            <s:select list="{'YES','NO'}" name="birthRegister.marriedStatus"/>
        </div>
        <div id="birth-confirmation-confim-nic">
            <s:textfield name="birthRegister.confirmantPINorPIN"/>                 
        </div>
        <div id="birth-confirmation-confim-name">
            <s:textarea name="birthRegister.confirmantFullName" cols="38"/>
        </div>
        <div id="birth-confirmation-date">
            <s:select list="{'2009','2010','2011'}" name="birthRegister.confirmYear" />
            <s:select list="{'January','February','March'}" name="birthRegister.confirmMonth" />
            <s:select list="{'01','02','03'}" name="birthRegister.confirmDay" />
        </div>
        <div id="birth-confirmation-finalize-date">
            <s:select list="{'2009','2010','2011'}" name="birthRegister.finalizeYear" />
            <s:select list="{'January','February','March'}" name="birthRegister.finalizeMonth" />
            <s:select list="{'01','02','03'}" name="birthRegister.finalizeDay" />
        </div>

        <div class="button"><input type="submit" value="NEXT"/></div>
    </div>
</s:form>