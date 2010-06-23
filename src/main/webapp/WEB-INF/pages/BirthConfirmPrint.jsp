<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="birth-register-approval">
    <div id="birth-register-approval-header">
        <s:form action="eprFilterBirthConfirmPrint.do" name="birth_register_approval_head" method="POST">
        <s:label><span><s:label name="district" value="%{getText('district.label')} : "/></span>
            <s:select name="districtId" list="districtList"/> </s:label>&nbsp;&nbsp;&nbsp;&nbsp;
        <s:label><span><s:label name="division" value="%{getText('division.label')} : "/></span>
            <s:select name="divisionId" list="divisionList"/></s:label>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <s:radio name="selectOption" list="#@java.util.HashMap@{'Not Printed':'Not Printed'}"/> &nbsp;&nbsp;&nbsp;
        <s:radio name="selectOption" list="#@java.util.HashMap@{'Printed':'Printed'}"/> &nbsp;&nbsp;&nbsp;&nbsp;
        <s:submit value="%{getText('view.label')}"></s:submit>
    </div>

    <s:url id="approveAndPrintUrl" action="eprConfirmationPrintPageLoad">
            <s:param name="bdId" value="%{#session.birthRegister.idUKey}"/>
        </s:url>

    <div id="birth-register-approval-body">
        <s:if test="printList.size==0 && printStart==0">
            <p class="alreadyPrinted"><s:label value="%{getText('noitemMsg.label')}"/></p>
        </s:if>
        <s:else>
            <table cellpadding="2">
                <tr>
                    <th><s:label name="item" value="%{getText('item.label')}"/></th>
                    <th></th>
                    <th><s:label name="serial" value="%{getText('serial.label')}"/></th>
                    <th><s:label name="name" value="%{getText('name.label')}"/></th>
                    <th><s:label name="registered_date" value="%{getText('registered_date.label')}"/></th>
                    <th><s:label value="%{getText('print.label')}"/></th>
                </tr>

                    <%--following code used for pagination--%>
                <s:iterator status="printStatus" value="printList">
                    <tr class="<s:if test="#printStatus.odd == true">odd</s:if><s:else>even</s:else>">
                        <td align="right"><s:property value="%{#printStatus.count+printStart}"/></td>
                        <td><s:checkbox name="index"
                                        onclick="javascript:selectall(document.birth_confirm_print,document.birth_confirm_print.allCheck)"/></td>
                        <td align=" center"><s:property value="register.bdfSerialNo"/></td>
                        <td><s:property value="child.childFullNameOfficialLang"/></td>
                        <td align="center"><s:property value="register.dateOfRegistration"/></td>
                        <td align="center">
                            <s:a href="%{approveAndPrintUrl}">
                                <img src="<s:url value='/images/print_icon.png'/>" border="none" width="25"
                                     height="25"/>
                            </s:a>
                        </td>
                    </tr>
                </s:iterator>

                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>

            <s:url id="previousUrl" action="eprPrintPrevious.do">
                <s:param name="districtId" value="#request.districtId"/>
                <s:param name="divisionId" value="#request.divisionId"/>
                <s:param name="selectOption" value="#request.selectOption"/>
                <s:param name="printStart" value="#request.printStart"/>
            </s:url>
            <s:url id="nextUrl" action="eprPrintNext.do">
                <s:param name="districtId" value="#request.districtId"/>
                <s:param name="divisionId" value="#request.divisionId"/>
                <s:param name="selectOption" value="#request.selectOption"/>
                <s:param name="printStart" value="#request.printStart"/>
            </s:url>

            <table>
                <tr>
                    <td>
                        <s:property value="printList.size"/>
                        <s:label name="items_found" value=" %{getText('items_found.label')} "/>
                        <s:if test="printList.size!=0">
                            <s:property value="printStart+1"/> to <s:property value="%{printList.size+printStart}"/>.
                        </s:if>
                    </td>
                    <td>&nbsp;</td>
                    <td>
                        <s:if test="printStart!=0 & printStart>0"><s:a href="%{previousUrl}">
                            &lt;<s:label value="%{getText('previous.label')}"/></s:a></s:if>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                        <s:if test="printList.size >= 10"><s:a href="%{nextUrl}">
                            <s:label value="%{getText('next.label')}"/>&gt;</s:a></s:if>
                    </td>
                </tr>
            </table>

            <br/><br/>
            <s:label><s:checkbox name="allCheck"
                                 onclick="javascript:selectallMe(document.birth_confirm_print,document.birth_confirm_print.allCheck)"/>
                <span><s:label name="select_all"
                               value="%{getText('select_all.label')}"/></span></s:label> &nbsp;&nbsp;&nbsp;&nbsp;
            <s:label><span><s:label name="print_selected"
                                    value="%{getText('print_selected.label')}"/></span><s:submit
                    value="%{getText('print.label')}"/></s:label>
        </s:else>
        </s:form>
    </div>
    <div id="birth-register-approval-footer">
    </div>
</div>
