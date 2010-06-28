<%--
  @author Chathuranga Withana
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="birth-register-approval">
    <div id="birth-register-approval-header">
        <s:if test="#request.warnings.size!=0">
            Following Warnings in Birth Declaration <b>Serial Number:<s:label
                value="%{#session.birthRegister.register.bdfSerialNo}"/></b>
            <img src="<s:url value='/images/warning.png'/>" width="27" height="27" border="none"/>
        </s:if>
        <s:elseif test="#request.warnings==null">
            Birth Declaration Saved Successfully with <b>Serial Number:
            <s:label value="%{#session.birthRegister.register.bdfSerialNo}"/></b>
            <img src="<s:url value='/images/approve.png'/>" width="25" height="25" border="none"/>
        </s:elseif>
        <s:elseif test="#request.warnings.size==0">
            Birth Declaration Approved Successfully with <b>Serial Number:
            <s:label value="%{#session.birthRegister.register.bdfSerialNo}"/></b>
            <img src="<s:url value='/images/approve.png'/>" width="25" height="25" border="none"/>
        </s:elseif>
    </div>
    <div id="birth-register-approval-body">
        <s:url id="newBDFUrl" action="eprBirthRegistration.do">
            <s:param name="addNewMode" value="true"/>
        </s:url>
        <s:url id="approveUrl" action="eprDirectApprove.do">
            <s:param name="bdId" value="%{#session.birthRegister.idUKey}"/>
        </s:url>
        <%--TODO shoud be redirected to confirmationPrinting... aproveAnd print--%>
        <s:url id="approveAndPrintUrl" action="eprConfirmationPrintPageLoad">
            <s:param name="bdId" value="#request.bdId"/>
        </s:url>
        <s:url id="mainUrl" action="eprHome.do"/>

        <%--<table>--%>
        <%--<tr>--%>
        <%--<td>--%>
        <%--<img src="<s:url value='/images/add_page.png'/>" width="35" height="35"--%>
        <%--border="none"/>--%>
        <%--</td>--%>
        <%--<td>--%>
        <%--<img src="<s:url value='/images/accept_page.png'/>" width="35" height="35"--%>
        <%--border="none"/>--%>
        <%--</td>--%>
        <%--<td>--%>
        <%--<img src="<s:url value='/images/approve.png'/>" width="35" height="35"--%>
        <%--border="none"/>--%>
        <%--</td>--%>
        <%--<td>--%>
        <%--<img src="<s:url value='/images/approve.png'/>" width="35" height="35"--%>
        <%--border="none"/>--%>
        <%--</td>--%>
        <%--</tr>--%>
        <%--</table>--%>
        <%--<table cellpadding="3">--%>
        <%--<tr>--%>
        <%--<th></th>--%>
        <%--<th><s:label name="item" value="%{getText('item.label')}"/></th>--%>
        <%--<th><s:label name="serial" value="%{getText('serial.label')}"/></th>--%>
        <%--<th><s:label name="name" value="%{getText('name.label')}"/></th>--%>
        <%--<th><s:label name="registered_date" value="%{getText('registered_date.label')}"/></th>--%>
        <%--<th><s:label value="%{getText('print.label')}"/></th>--%>
        <%--</tr>--%>

        <%--following code used for pagination--%>


        <%--<s:iterator status="printStatus" value="#session.printList">--%>
        <%--<tr class="<s:if test="#printStatus.odd == true">odd</s:if><s:else>even</s:else>">--%>
        <%--<td><s:property value="%{#printStatus.count+#session.printStart}"/></td>--%>
        <%--<td><s:checkbox name="index"--%>
        <%--onclick="javascript:selectall(document.birth_confirm_print,document.birth_confirm_print.allCheck)"/></td>--%>
        <%--<td align=" center"><s:property value="register.bdfSerialNo"/></td>--%>
        <%--<td><s:property value="child.childFullNameOfficialLang"/></td>--%>
        <%--<td align="center"><s:property value="register.dateOfRegistration"/></td>--%>
        <%--<td align="center">--%>
        <%--<s:a href="">--%>
        <%--<img src="<s:url value='/images/print_icon.png'/>" border="none" width="25"--%>
        <%--height="25"/>--%>
        <%--</s:a>--%>
        <%--</td>--%>
        <%--</tr>--%>
        <%--<s:set name="printCount" value="#printStatus.count" scope="session"/>--%>
        <%--</s:iterator>--%>

        <%--<tr>--%>
        <%--<td>&nbsp;</td>--%>
        <%--</tr>--%>
        <%--</table>--%>
        <s:if test="#request.warnings.size!=0">
            <s:iterator value="#request.warnings">
                <s:property value="message"/>
            </s:iterator>

            <div id="bdaw-action">
                <s:form action="#" name="#">
                    <s:hidden value="%{#request.bdId}" name="bdId"/>
                    <table border="0">
                        <tr>
                            <td><s:label value="%{getText('ignoreWorning.label')}" name="ignoreWorning"/></td>
                            <td><s:checkbox name="ignoreWarning"/></td>
                        </tr>
                        <tr>
                            <td><s:label value="%{getText('comment.label')}" name="comment"/></td>
                            <td><s:textarea name="comments"/></td>
                        </tr>
                        <tr>
                            <td><s:submit name="approve" value="%{getText('approve.label')}"/></td>
                        </tr>
                    </table>
                </s:form>
            </div>
        </s:if>

        <br/><br/>

    </div>
    <div id="birth-register-approval-footer">
        <s:a href="%{newBDFUrl}"><s:label value="%{getText('addNewBDF_link.label')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
        <s:if test="#session.allowApproveBDF">
		<%--<s:if test="#session.allowApproveBDF==true">--%>
            <s:a href="%{approveUrl}"><s:label value="%{getText('approve_link.label')}"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;
            <s:a href="%{approveAndPrintUrl}">
                <s:label value="%{getText('approveAndPrint_link.label')}"/></s:a> &nbsp;&nbsp;&nbsp;&nbsp;
        </s:if>
        <s:a href="%{mainUrl}"><s:label value="%{getText('goToMain_link.label')}"/></s:a>
    </div>
</div>

