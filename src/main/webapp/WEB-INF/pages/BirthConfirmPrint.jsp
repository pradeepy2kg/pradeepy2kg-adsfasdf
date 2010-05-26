<%--@author Chathuranga Withana--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="lk.rgd.crs.api.domain.PrintData" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>--%>
<div id="birth-register-approval">
    <div id="birth-register-approval-header">
        <s:form action="eprFilterBirthConfirmPrint.do" name="birth_register_approval_head" method="POST">
            <s:label><span>District:</span><s:select name="" list="districtList" headerKey="0"
                                                     headerValue="%{getText('select_district.label')}"/> </s:label>
            <s:label><span>Division:</span><s:select name="" list="divisionList" headerKey="0"
                                                     headerValue="%{getText('select_division.label')}"/></s:label>
            <s:radio name="selectOption" list="{'All','Not Printed','Printed'}"/>
            <s:submit value="View"></s:submit>
        </s:form>
    </div>
    <div id="birth-register-approval-body">
        <s:form name="birth_confirm_print" action="" method="POST">
            <table cellpadding="3">
                <tr>
                    <th></th>
                    <th>Item</th>
                    <th>Serial</th>
                    <th>Name</th>
                    <th>Status</th>
                    <th></th>
                </tr>

                <s:if test="#session.printStart == null">
                    <s:set name="printStart" value="0" scope="session"/>
                </s:if>

                <s:subset source="#session.printList" count="10" start="#session.printStart">
                    <s:iterator status="printStatus">
                        <tr class="<s:if test="#printStatus.odd == true">odd</s:if><s:else>even</s:else>">
                            <td><s:property value="%{#printStatus.count+#session.printStart}"/></td>
                            <td><s:checkbox name="index_" onclick="javascript:selectall()"/></td>
                            <td align=" center"><s:property value="serial"/></td>
                            <td><s:property value="name"/></td>
                            <td align="right">
                                <s:if test="status==0"><s:label value="Not Printed"/></s:if>
                                <s:else><s:label value="Already printed"/></s:else>
                            </td>
                            <td align="right">
                                <s:submit value="Print"/>
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
                        <s:property value="#session.printList.size"/> Items Found, displaying <s:property
                            value="#session.printStart+1"/> to <s:property
                            value="%{#session.printCount+#session.printStart}"/>.
                    </td>
                    <td>&nbsp;</td>
                    <td width="90">
                        <s:if test="#session.printStart!=0"><s:a href="%{previousUrl}">
                            <s:label value="<<Previous"/></s:a></s:if>
                    </td>
                    <td width="10">
                        <s:if test="#session.printCount != 28"><s:a href="%{nextUrl}">
                            <s:label value="Next>>"/></s:a></s:if>
                    </td>
                </tr>
            </table>

            <br/><br/>
            <s:label><s:checkbox name="allCheck" onclick="javascript:selectallMe()"/>
                <span>Select All</span></s:label> &nbsp;&nbsp;&nbsp;&nbsp;
            <s:label><span>Print Selected:</span><s:submit value="Print"/></s:label>
        </s:form>
    </div>
    <div id="birth-register-approval-footer">
    </div>
</div>
