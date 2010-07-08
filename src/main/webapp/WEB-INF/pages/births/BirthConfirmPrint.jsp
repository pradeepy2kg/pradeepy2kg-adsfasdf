<%--@author Chathuranga Withana--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<script>
    function view_DSDivs() {
        dojo.event.topic.publish("view_DSDivs");
    }

    function view_BDDivs() {
        dojo.event.topic.publish("view_BDDivs");
    }
</script>
<div id="birth-certificate-print">
    <s:url id="loadDSDivList" action="../ajaxSupport_loadDSDivListBDFConfirmationPrint"/>
    <div id="birth-certificate-print-header">
        <s:actionerror/>
        <s:form action="eprFilterBirthConfirmationPrint.do" method="POST"
                id="birth-confirmation-print-form">
        <table width="100%" cellpadding="5" cellspacing="0">
            <col width="220px"/>
            <col/>
            <col width="120px"/>
            <col width="120px"/>
            <tbody>
            <tr>
                <td><s:label name="district" value="%{getText('district.label')}"/></td>
                <td><s:select name="birthDistrictId" list="districtList" value="birthDistrictId"
                              onchange="javascript:view_DSDivs();return false;"
                              cssStyle="width:100%;"/>
                </td>
                <td><s:radio list="#@java.util.HashMap@{'false':'Not Printed'}" name="printed" value="false"/></td>
                <td><s:radio list="#@java.util.HashMap@{'true':'Printed'}" name="printed"/></td>
            </tr>
            <tr>
                <td><s:label name="division" value="%{getText('select_ds_division.label')}"/></td>
                <td colspan="3"><sx:div id="dsDivisionId" value="dsDivisionId" href="%{loadDSDivList}" theme="ajax"
                                        listenTopics="view_DSDivs" formId="birth-confirmation-print-form"></sx:div></td>
            </tr>
            <tr>
                <td colspan="4" class="button" align="right">
                    <s:hidden name="confirmListFlag" value="true"/>
                    <s:submit value="%{getText('view.label')}"></s:submit>
                </td>
            </tr>
            </tbody>
        </table>
        </s:form>
    </div>

    <div>
        <s:form action="eprBirthConfirmationBulkPrint" name="birth_confirm_print">
        <s:if test="printList.size==0 && printStart==0">
            <p class="alreadyPrinted"><s:label value="%{getText('noitemMsg.label')}"/></p>
        </s:if>
        <s:else>
            <table width="100%" cellpadding="0" cellspacing="0">
                <tr class="table-title">
                    <th></th>
                    <th width="30px"></th>
                    <th width="100px"><s:label name="serial" value="%{getText('serial.label')}"/></th>
                    <th><s:label name="name" value="%{getText('name.label')}"/></th>
                    <th width="110px"><s:label name="registered_date" value="%{getText('registered_date.label')}"/></th>
                    <th width="100px"><s:label value="%{getText('print.label')}"/></th>
                </tr>

                    <%--following code used for pagination--%>
                <s:iterator status="printStatus" value="printList" id="printListId">
                    <tr class="<s:if test="#printStatus.odd == true">odd</s:if><s:else>even</s:else>">
                        <td class="table-row-index"><s:property value="%{#printStatus.count+printStart}"/></td>
                        <td><s:checkbox name="index" fieldValue="%{#printListId.idUKey}" value="%{#index}"
                                        onclick="javascript:selectall(document.birth_confirm_print,document.birth_confirm_print.allCheck)"/></td>
                        <td align="center"><s:property value="register.bdfSerialNo"/></td>
                        <td><s:property value="child.childFullNameOfficialLang"/></td>
                        <td align="center"><s:property value="register.dateOfRegistration"/></td>
                        <td align="center">
                            <s:url id="cetificatePrintUrl" action="eprBirthConfirmationPage">
                                <s:param name="bdId" value="idUKey"/>
                            </s:url>
                            <s:a href="%{cetificatePrintUrl}">
                                <img src="<s:url value='/images/print_icon.gif'/>" border="none" width="25"
                                     height="25"/>
                            </s:a>
                        </td>
                    </tr>
                </s:iterator>
            </table>

            <div class="form-submit">
                <s:label><s:checkbox name="allCheck"
                                     onclick="javascript:selectallMe(document.birth_confirm_print,document.birth_confirm_print.allCheck)"/>
                    <span><s:label name="select_all" value="%{getText('select_all.label')}"/></span></s:label> &nbsp;&nbsp;&nbsp;&nbsp;
                <s:label><span><s:label name="print_selected" value="%{getText('print_selected.label')}"/></span>
                    <s:hidden name="pageNo" value="%{#request.pageNo}"/>
                    <s:hidden name="confirmListFlag" value="true"/>
                    <s:hidden name="printed" value="%{#request.printed}"/>
                    <s:hidden name="birthDistrictId" value="%{#request.birthDistrictId}"/>
                    <s:hidden name="birthDivisionId" value="%{#request.birthDivisionId}"/>
                    <s:submit value="%{getText('print.label')}"/></s:label>
            </div>
            <div class="next-previous">
                <s:url id="previousUrl" action="eprPrintPrevious.do">
                    <s:param name="confirmListFlag" value="true"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                    <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                    <s:param name="printed" value="#request.printed"/>
                    <s:param name="printStart" value="#request.printStart"/>
                </s:url>
                <s:url id="nextUrl" action="eprPrintNext.do">
                    <s:param name="confirmListFlag" value="true"/>
                    <s:param name="pageNo" value="%{#request.pageNo}"/>
                    <s:param name="birthDistrictId" value="#request.birthDistrictId"/>
                    <s:param name="birthDivisionId" value="#request.birthDivisionId"/>
                    <s:param name="printed" value="#request.printed"/>
                    <s:param name="printStart" value="#request.printStart"/>
                </s:url>
                <s:if test="printStart!=0 & printStart>0">
                    <s:a href="%{previousUrl}">
                        <img src="<s:url value='/images/previous.gif'/>" border="none"/></s:a>
                    <s:label value="%{getText('previous.label')}"/>
                </s:if>
                <s:if test="printList.size >= 10">
                    <s:label value="%{getText('next.label')}"/><s:a href="%{nextUrl}">
                    <img src="<s:url value='/images/next.gif'/>" border="none"/></s:a>
                </s:if>
            </div>
        </s:else>
        </s:form>
    </div>
    <div id="birth-register-approval-footer">
    </div>
</div>
