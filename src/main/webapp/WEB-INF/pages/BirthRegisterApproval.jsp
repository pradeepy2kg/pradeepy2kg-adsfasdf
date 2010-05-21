<%@ page import="java.util.ArrayList" %>
<%@ page import="lk.rgd.crs.api.domain.BirthRegisterApproval" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% String info = session.getAttribute("info").toString();%>
<div id="birth-register-approval">
    <div id="birth-register-approval-header">
        <s:form action="eprBirthRegisterApproval_getList" name="birth_register_approval_head" method="POST">
            <s:label><span>District:</span><s:textfield name="district"></s:textfield></s:label>
            <s:label><span>Division:</span><s:textfield name="division"></s:textfield></s:label>
            <s:label><span>Show Expired:</span><s:checkbox name="expired"></s:checkbox></s:label>
            <s:submit name="refresh" value="refresh"></s:submit>
        </s:form>
    </div>
    <div id="birth-register-approval-body">

        <s:form action="eprBirthRegisterApproval" name="birth_register_approval_body" method="POST">
            <table>
                <tr>
                    <th></th>
                    <th>serial</th>
                    <th>name</th>
                    <th>changes</th>
                    <th>actions</th>
                </tr>
                <% ArrayList<BirthRegisterApproval> bra = (ArrayList<BirthRegisterApproval>) session.getAttribute("data");
                    for (int i = 0; i < bra.size(); i++) {
                        BirthRegisterApproval B = bra.get(i); %>
                <tr>
                    <td><s:checkbox name="index_"/></td>
                    <% out.write("<td>" + B.getSerial() + "</td>");
                        out.write("<td>" + B.getName() + "</td>");
                        out.write("<td>" + B.isChanges() + "</td>");
                        out.write("<td>" + B.getActions() + "</td>");
                    }
                    %>
                </tr>
                <tr></tr>
            </table>
            <s:label><s:checkbox name="select_all"/><span>Select All</span></s:label>
            <s:submit name="approve" value="approve"/>
        </s:form>
    </div>
    <div id="birth-register-approval-footer">

    </div>
</div>