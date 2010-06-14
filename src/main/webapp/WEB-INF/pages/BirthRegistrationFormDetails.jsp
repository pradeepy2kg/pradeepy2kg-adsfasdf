<%--
  @author Chathuranga Withana
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<p>Birth Declaration Successfully Saved with <b>Serial Number: <s:label
        value="%{#session.birthRegister.register.bdfSerialNo}"/></b></p>
<s:set name="bdId" value="160"/>

<s:url id="newBDFUrl" action="eprBirthRegistration.do"/>
<s:url id="editUrl" action="eprBirthRegistration.do">
    <s:param name="bdId" value="%{#session.birthRegister.idUKey}"/>
</s:url>
<s:url id="approveUrl" action="eprApproveBirthDeclaration.do">
    <s:param name="bdId" value="%{#session.birthRegister.idUKey}"/>
</s:url>
<%--TODO aproveAnd print--%>
<s:url id="approveAndPrintUrl" action="#">   
    <s:param name="bdId" value="%{#session.birthRegister.idUKey}"/>
</s:url>
<s:url id="mainUrl" action="eprHome.do"/>

<s:a href="%{editUrl}">Edit</s:a>&nbsp;&nbsp;&nbsp;&nbsp;
<s:a href="%{newBDFUrl}">Add New BDF</s:a>&nbsp;&nbsp;&nbsp;&nbsp;
<s:a href="%{approveUrl}">Approve</s:a>&nbsp;&nbsp;&nbsp;&nbsp;
<s:a href="%{approveAndPrintUrl}">Approve & Print</s:a> &nbsp;&nbsp;&nbsp;&nbsp;
<s:a href="%{mainUrl}">Go To Main</s:a>