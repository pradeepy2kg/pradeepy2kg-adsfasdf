<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%--
  @author duminda
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:form action="eprBirthRegistration.do" method="POST" name="birthRegistrationForm2">
     <div id="birth-registration-form-2-body">
        <div id="father-nic-no"><s:textfield name="birthRegister.fathersNIC" /></div>
        <div id="father-passport-no"><s:textfield name="birthRegister.fatherForeignerPassportNo"/></div>
        <div id="father-country">
            <%--todo change this into struts tag--%>
            <% Map<Integer, String> countryList = (Map<Integer, String>) session.getAttribute("countryList");
                Iterator it = countryList.entrySet().iterator();
            %>
            <select name="birthRegister.fatherForeignerCountry">
                <option value="-1">- Select Country -</option>
                <% while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                %>
                <option value="<%= pair.getKey()%>">
                    <%= pair.getValue()%>
                </option>
                <%}%>
            </select>
            <%--<s:select name="birthRegister.fatherForeignerCountry" list="districtList" listKey="districtId"--%>
                      <%--listValue="districtName" headerKey="0" headerValue="-Select District-"/>--%>
        </div>
        <div id="father-name"><s:textfield name="birthRegister.fatherFullName"/></div>
        <%--<div id="father-dob"><s:textfield name="fatherDOB" value="2005/05/01" /></div>--%>
        <div id="father-birth-place"><s:textfield name="birthRegister.fatherBirthPlace"/></div>
        <div id="father-race">
            <%--<s:textfield name="birthRegister.fatherRace"/>--%>
            <%--todo change this into struts tag--%>
            <% Map<Integer, String> raceList = (Map<Integer, String>) session.getAttribute("raceList");
                Iterator itRace1 = raceList.entrySet().iterator();
            %>
            <select name="birthRegister.fatherRace">
                <option value="-1">- Select Race -</option>
                <% while (itRace1.hasNext()) {
                    Map.Entry pair = (Map.Entry) itRace1.next();
                %>
                <option value="<%= pair.getKey()%>">
                    <%= pair.getValue()%>
                </option>
                <%}%>
            </select>
        </div>
        <div id="mother-nic-no"><s:textfield name="birthRegister.motherNIC"/></div>
        <div id="mother-passport-no"><s:textfield name="birthRegister.motherPassportNo"/></div>
        <div id="mother-country">
            <%--todo change this into struts tag--%>
            <% Iterator iterator = countryList.entrySet().iterator();
            %>
            <select name="birthRegister.motherCountry">
                <option value="-1">- Select Country -</option>
                <% while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                %>
                <option value="<%= pair.getKey()%>">
                    <%= pair.getValue()%>
                </option>
                <%}%>
            </select>
            <%--<s:textfield name="birthRegister.motherCountry"/>--%>
        </div>
        <div id="mother-admision-no"><s:textfield name="birthRegister.motherAdmissionNoAndDate"/></div>
        <div id="mother-name"><s:textfield name="birthRegister.motherFullName"/></div>
        <%--<div id="mother-dob"><s:textfield name="motherDOB" value="2005/05/01"/></div>--%>
        <div id="mother-birth-place"><s:textfield name="birthRegister.motherBirthPlace"/></div>
        <div id="mother-race">
            <%--<s:textfield name="birthRegister.motherRace"/>--%>
            <%--todo change this into struts tag--%>
            <% Iterator itRace2 = raceList.entrySet().iterator();
            %>
            <select name="birthRegister.fatherRace">
                <option value="-1">- Select Race -</option>
                <% while (itRace2.hasNext()) {
                    Map.Entry pair = (Map.Entry) itRace2.next();
                %>
                <option value="<%= pair.getKey()%>">
                    <%= pair.getValue()%>
                </option>
                <%}%>
            </select>
        </div>
        <div id="mother-age-for-birth"><s:textfield name="birthRegister.motherAgeAtBirth" id="motherAgeAtBirth"/></div>
        <div id="mother-address"><s:textfield name="birthRegister.motherAddress"/></div>
        <div id="mother-phone"><s:textfield name="birthRegister.motherPhoneNo"/></div>
        <div id="mother-email"><s:textfield name="birthRegister.motherEmail"/></div>
    </div>
    <s:hidden name="pageNo" value="2" />
    <div class="button"><s:submit type="submit" value="NEXT"/></div>
</s:form>