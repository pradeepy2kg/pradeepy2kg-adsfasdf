<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="birth-register-approval">
    <div id="birth-register-approval-header">
        <s:form action="eprFilterBirthConfirmPrint.do" name="birth_register_approval_head" method="POST">
            <s:label><span><s:label name="district" value= "%{getText('district.label')}" /></span><s:select name="" list="districtList" headerKey="0"
                                                     headerValue="%{getText('select_district.label')}"/> </s:label>
            <s:label><span><s:label name="division" value= "%{getText('division.label')}" /></span><s:select name="" list="divisionList" headerKey="0"
                                                     headerValue="%{getText('select_division.label')}"/></s:label>

            <s:radio name="selectOption" list="#{'Not Printed':'Not Printed'}" />
            <s:radio name="selectOption" list="#{'Printed':'Printed'}" />
            <s:submit value= "%{getText('view.label')}" ></s:submit>
        </s:form>
    </div>
    <div id="birth-register-approval-body">
        <s:form name="birth_confirm_print" action="" method="POST">
            <table cellpadding="3">
                <tr>
                    <th></th>
                    <th><s:label name="item" value= "%{getText('item.label')}" /></th>
                    <th><s:label name="serial" value= "%{getText('serial.label')}" /></th>
                    <th><s:label name="name" value= "%{getText('name.label')}" /></th>
                    <th><s:label name="registered_date" value= "%{getText('registered_date.label')}" /></th>
                    <th><s:label name="status" value= "%{getText('status.label')}" /></th>
                    <th></th>
                </tr>

                <%--following code used for pagination--%>
                <s:if test="#session.printStart == null">
                    <s:set name="printStart" value="0" scope="session"/>
                </s:if>

                <%--TODO remove subset after complete pagination --%>
                <s:subset source="#session.printList" count="10" start="#session.printStart">
                    <s:iterator status="printStatus">
                        <tr class="<s:if test="#printStatus.odd == true">odd</s:if><s:else>even</s:else>">
                            <td><s:property value="%{#printStatus.count+#session.printStart}"/></td>
                            <td><s:checkbox name="index" onclick="javascript:selectall(document.birth_confirm_print,document.birth_confirm_print.allCheck)"/></td>
                            <td align=" center"><s:property value="register.bdfSerialNo"/></td>
                            <td><s:property value="child.childFullNameOfficialLang"/></td>
                            <td><s:property value="register.dateOfRegistration"/></td>
                            <td align="right">
                                <%--<s:if test="confirmant.confirmationPrinted"><s:label value="%{getText('already_printed.label')}"/></s:if>--%>
                                <%--<s:else><s:label value="%{getText('not_printed.label')}"/></s:else>--%>
                            </td>
                            <td align="right">
                                <s:submit value="%{getText('print.label')}"/>
                            </td>
                        </tr>
                        <s:set name="printCount" value="#printStatus.count" scope="session"/>
                    </s:iterator>
                </s:subset>
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>

            <s:url id="previousUrl" action="eprPrintPrevious.do"/>
            <s:url id="nextUrl" action="eprPrintNext.do"/>

            <table>
                <tr>
                    <td>
                        <s:property value="#session.printList.size"/> <s:label name="items_found" value=" %{getText('items_found.label')} " /> <s:property
                            value="#session.printStart+1"/> to <s:property
                            value="%{#session.printCount+#session.printStart}"/>.
                    </td>
                    <td>&nbsp;</td>
                    <td width="90">
                        <s:if test="#session.printStart!=0"><s:a href="%{previousUrl}">
                            <s:label value="<<Previous"/></s:a></s:if>
                    </td>
                    <td width="10">
                        <s:if test="#session.printCount >= 10"><s:a href="%{nextUrl}">
                            <s:label value="Next>>"/></s:a></s:if>
                    </td>
                </tr>
            </table>

            <br/><br/>
            <s:label><s:checkbox name="allCheck" onclick="javascript:selectallMe(document.birth_confirm_print,document.birth_confirm_print.allCheck)"/>
                <span><s:label name="select_all" value= "%{getText('select_all.label')}" /></span></s:label> &nbsp;&nbsp;&nbsp;&nbsp;
            <s:label><span><s:label name="print_selected" value= "%{getText('print_selected.label')}" /></span><s:submit value="%{getText('print.label')}"/></s:label>
        </s:form>
    </div>
    <div id="birth-register-approval-footer">
    </div>
</div>
