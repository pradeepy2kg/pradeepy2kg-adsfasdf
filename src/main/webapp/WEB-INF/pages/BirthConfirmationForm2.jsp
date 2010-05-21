<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%--
 @author chathuranga
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form name="birthConfirmationForm2" action="eprBirthConfirmation.do" method="POST">
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
                <%--<s:select name="birthRegister.fatherRace" list="raceList" headerKey="0" headerValue="- Select Race -"--%>
                <%--listKey="raceId" listValue="raceName"/>--%>
                <%--todo change this into struts tag--%>
            <% Map<Integer, String> raceList = (Map<Integer, String>) session.getAttribute("raceList");
                Iterator it = raceList.entrySet().iterator();
            %>
            <select name="birthRegister.fatherRace">
                <option value="-1">- Select Race -</option>
                <% while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                %>
                <option value="<%= pair.getKey()%>">
                    <%= pair.getValue()%>
                </option>
                <%}%>
            </select>
        </div>
        <div id="birth-confirmation-mother-pin">
            <s:textfield name="birthRegister.motherNIC"/>
        </div>
        <div id="birth-confirmation-mother-name">
            <s:textarea name="birthRegister.motherFullName" cols="38"/>
        </div>
        <div id="birth-confirmation-motherRace">
                <%--<s:select name="birthRegister.motherRace" list="raceList" headerKey="0" headerValue="- Select Race -" listKey="raceId" listValue="raceName"/>--%>
                <%--todo change this into struts tag--%>
            <% Iterator iterator = raceList.entrySet().iterator();
            %>
            <select name="birthRegister.motherRace">
                <option value="-1">- Select Race -</option>
                <% while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                %>
                <option value="<%= pair.getKey()%>">
                    <%= pair.getValue()%>
                </option>
                <%}%>
            </select>
        </div>
        <div id="birth-confirmation-married">
            <s:select name="birthRegister.marriedStatus" list="{'YES','NO'}" headerKey="0"
                      headerValue="- Select Status -"/>
        </div>
        <div id="birth-confirmation-confim-nic">
            <s:textfield name="birthRegister.confirmantPINorPIN"/>
        </div>
        <div id="birth-confirmation-confim-name">
            <s:textarea name="birthRegister.confirmantFullName" cols="38"/>
        </div>
        <div id="birth-confirmation-date">
            <s:select list="{'2009','2010','2011'}" name="birthRegister.confirmYear"/>
            <s:select list="{'January','February','March'}" name="birthRegister.confirmMonth"/>
            <s:select list="{'01','02','03'}" name="birthRegister.confirmDay"/>
        </div>
        <div id="birth-confirmation-finalize-date">
            <s:select list="{'2009','2010','2011'}" name="birthRegister.finalizeYear"/>
            <s:select list="{'January','February','March'}" name="birthRegister.finalizeMonth"/>
            <s:select list="{'01','02','03'}" name="birthRegister.finalizeDay"/>
        </div>

        <s:hidden name="pageNo" value="2"/>
        <div class="button"><input type="submit" value="NEXT"/></div>
    </div>
</s:form>
