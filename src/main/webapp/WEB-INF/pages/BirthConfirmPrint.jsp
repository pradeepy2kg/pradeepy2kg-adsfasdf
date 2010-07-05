<%--
    @author Chathuranga Withana
    @author Duminda Dharmakeerthi
    --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="birth-confirm-print">
    <div id="birth-register-approval-header">
        <s:form action="eprFilterBirthConfirmPrint.do" name="birth_register_approval_head" method="POST">
        <table  width="100%" cellpadding="0" cellspacing="0" style="margin-bottom:10px;">
            <tr>
                <td><s:label name="district" value="%{getText('district.label')} : "/></td>
                <td width="250px"><s:select name="districtId" list="districtList" cssStyle="width:230px;"/></td>
                <td><s:label name="division" value="%{getText('division.label')} : "/></td>
                <td width="250px"><s:select name="divisionId" list="divisionList" cssStyle="width:230px;"/></td>
                <td><s:radio name="selectOption" list="#@java.util.HashMap@{'Not Printed':'Not Printed'}"/></td>
                <td><s:radio name="selectOption" list="#@java.util.HashMap@{'Printed':'Printed'}"/></td>
                <td align="right" class="button"><s:submit value="%{getText('view.label')}"></s:submit></td>
            </tr>
        </table>
    </div>

    <div id="birth-register-approval-body">
        <s:if test="printList.size==0 && printStart==0">
            <p class="alreadyPrinted"><s:label value="%{getText('noitemMsg.label')}"/></p>
        </s:if>
        <s:else>
            <table id="confirm-print-table" width="100%" cellpadding="0" cellspacing="0">
                <tr class="table-title">
                    <th><s:label name="item" value="%{getText('item.label')}"/></th>
                    <th width="30px"></th>
                    <th width="100px"><s:label name="serial" value="%{getText('serial.label')}"/></th>
                    <th><s:label name="name" value="%{getText('name.label')}"/></th>
                    <th width="110px"><s:label name="registered_date" value="%{getText('registered_date.label')}"/></th>
                    <th width="100px"><s:label value="%{getText('print.label')}"/></th>
                </tr>

                    <%--following code used for pagination--%>
                <s:iterator status="printStatus" value="printList">

                    <tr class="<s:if test="#printStatus.odd == true">odd</s:if><s:else>even</s:else>">
                        <td class="table-row-index"><s:property value="%{#printStatus.count+printStart}"/></td>
                        <td><s:checkbox name="index"
                                        onclick="javascript:selectall(document.birth_confirm_print,document.birth_confirm_print.allCheck)"/></td>
                        <td align=" center"><s:property value="register.bdfSerialNo"/></td>
                        <td><s:property value="child.childFullNameOfficialLang"/></td>
                        <td align="center"><s:property value="register.dateOfRegistration"/></td>
                        <td align="center">
                            <s:url id="cetificatePrintUrl" action="eprBirthConfirmationListPage">
                                <s:param name="bdId" value="idUKey"/>
                            </s:url>
                            <s:a href="%{cetificatePrintUrl}">
                                <img src="<s:url value='/images/print_icon.png'/>" border="none" width="25"
                                     height="25"/>
                            </s:a>
                        </td>
                    </tr>
                </s:iterator>
            </table>

            <div class="next-previous">

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

                <s:if test="printStart!=0 & printStart>0"><s:a href="%{previousUrl}">
                    &lt;<s:label value="%{getText('previous.label')}"/></s:a></s:if>
                <s:if test="printList.size >= 10"><s:a href="%{nextUrl}">
                    <s:label value="%{getText('next.label')}"/>&gt;</s:a></s:if>

            </div>

            <div class="form-submit">
                <s:label><s:checkbox name="allCheck"
                                     onclick="javascript:selectallMe(document.birth_confirm_print,document.birth_confirm_print.allCheck)"/>
                <span><s:label name="select_all"
                               value="%{getText('select_all.label')}"/></span></s:label> &nbsp;&nbsp;&nbsp;&nbsp;
                <s:label><span><s:label name="print_selected"
                                        value="%{getText('print_selected.label')}"/></span><s:submit
                        value="%{getText('print.label')}"/></s:label>
            </div>
        </s:else>
        </s:form>
    </div>
</div>
