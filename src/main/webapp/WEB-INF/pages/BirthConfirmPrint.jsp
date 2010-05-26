<%@ page import="java.util.ArrayList" %>
<%@ page import="lk.rgd.crs.api.domain.BirthRegisterApproval" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>--%>
<div id="birth-register-approval">
    <div id="birth-register-approval-header">
        <s:form action="eprBirthRegisterApproval_getList" name="birth_register_approval_head" method="POST">
            <s:label><span>Division:</span><s:select name="" list="districtList" headerKey="0"
                                                     headerValue="-Select District-"/> </s:label>
            <s:label><span>Division:</span><s:select name="" list="districtList" headerKey="0"
                                                     headerValue="-Select District-"/></s:label>
            <s:submit name="View" value="refresh"></s:submit>
        </s:form>
    </div>
    <div id="birth-register-approval-body">
        <table>
            <tr>
                <td></td>
                <td align="center"><b>Serial Number</b></td>
                <td><b>Name</b></td>
                <td></td>
            </tr>

            <s:iterator value="#session.printList">
                <tr>
                    <td><s:checkbox name="index"/></td>
                    <td align="center"><s:property value="serial"/></td>
                    <td><s:property value="name"/></td>
                    <td><s:submit value="Print"/></td>
                </tr>
            </s:iterator>
            <tr>&nbsp;</tr>
        </table>


        <s:label><s:checkbox name="select_all"/><span>Select All</span></s:label>
        <s:submit name="approve" value="Print"/>


    </div>
    <div id="birth-register-approval-footer">

    </div>
</div>