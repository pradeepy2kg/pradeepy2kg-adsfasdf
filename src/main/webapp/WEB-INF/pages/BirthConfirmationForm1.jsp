<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%--
  User: chathuranga
  Date: May 13, 2010
  Time: 10:38:39 AM
  Birth Registration Confirmation Page 1
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form name="birthConfirmationForm1" action="eprBirthConfirmation.do" method="POST">
    <div id="birth-confirmation-form-1-body">
        <div id="birth-confirmation-form-1-serial">
            <s:textfield name="birthConfirm.confirmSerialNumber"/>
        </div>
        <div id="birth-confirmation-regis-serial">
            <s:textfield name="birthConfirm.serialNumber"/>
        </div>
        <div id="birth-confirmation-regis-date">
            <s:select list="{'2009','2010','2011'}" name=""/>
            <s:select list="{'January','February','March'}" name=""/>
            <s:select list="{'01','02','03'}" name=""/>
        </div>
        <div id="birth-confirmation-date-of-birth">
            <s:select list="{'2009','2010','2011'}" name=""/>
            <s:select list="{'January','February','March'}" name=""/>
            <s:select list="{'01','02','03'}" name=""/>
        </div>
        <div id="birth-confirmation-birth-place">
                <%--todo change this into struts tag--%>
            <% Map<Integer, String> districtList = (Map<Integer, String>) session.getAttribute("districtList");
                Iterator it = districtList.entrySet().iterator();
            %>
            <select name="">
                <option value="-1">- Select District -</option>
                <% while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                %>
                <option value="<%= pair.getKey()%>">
                    <%= pair.getValue()%>
                </option>
                <%}%>
            </select>
                <%--<s:select name="birthRegister.childBirthDistrict" list="districtList" listKey="districtId"--%>
                <%--listValue="districtName" headerKey="0" headerValue="- Select District -"/>--%>
                <%--<s:select name="birthRegister.childBirthDistrict" list="districtList" listKey="districtList.key" listValue="districtList.value"--%>
                <%--headerKey="0" headerValue="- Select District -"/>--%>
        </div>

        <div id="birth-confirmation-name"><s:textarea name="birthConfirm.childFullNameOfficialLang" cols="38"
                                                      rows="7"/></div>
        <div id="birth-confirmation-name-in-english"><s:textarea name="birthConfirm.childFullNameEnglish" cols="38"
                                                                 rows="5"/></div>
        <div id="birth-confirmation-gender">
            <s:select list="{'Male','Female'}" name="birthConfirm.childGender" headerKey="0"
                      headerValue="-Select Gender-"/>
        </div>
        <s:hidden name="pageNo" value="1"/>
        <div class="button"><s:submit type="submit" value="NEXT"/></div>
    </div>
</s:form>
</html>