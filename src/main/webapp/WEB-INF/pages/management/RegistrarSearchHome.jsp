<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="/ecivil/lib/jquery/jqSOAPClient.js" type="text/javascript"></script>
<script src="/ecivil/lib/jquery/jqXMLUtils.js" type="text/javascript"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script>
    $(document).ready(function() {
        $("#tabs").tabs();
    });
</script>
<s:actionerror cssStyle="color:red;font-size:10pt"/>
<s:actionmessage/>
<s:form method="post" action="eprFindRegistrar.do">
    <%--section search by death certificate number  onsubmit="javascript:return validateForm()"--%>
    <div id="tabs">
        <ul>
            <li><a href="#fragment-1"><span> <s:label
                    value="%{getText('label.tab.search.by.registrar.pin')}"/></span></a></li>
            <li><a href="#fragment-2"><span><s:label
                    value="%{getText('label.tab.search.by.registrar.name')}"/></span></a></li>
        </ul>

        <div id="fragment-1">
            <table cellpadding="2px" cellspacing="0">
                <caption></caption>
                <col width="265px">
                <col width="500px">
                <tbody>
                <tr>
                    <td align="left">
                        <s:label value="%{getText('registrar.pin')}"/>
                    </td>
                    <td align="left">
                        <s:textfield name="registrarPin" id="registrarPin" value=""/>
                    </td>
                </tr>
                </tbody>
            </table>

        </div>
        <div id="fragment-2">
                <%--section search by identification number--%>
            <table cellpadding="2px" cellspacing="0">
                <caption></caption>
                <col width="265px">
                <col width="500px">
                <tbody>
                <tr>
                    <td align="left">
                        <s:label value="%{getText('label.person.name')}"/>
                    </td>
                    <td align="left">
                        <s:textfield name="registrarName" id="registrarName" maxLength="10" value=""/>
                    </td>
                </tr>
                </tbody>
            </table>

        </div>

    </div>
    <%--section search button--%>
    <div class="form-submit">
        <s:submit type="submit" value="%{getText('button.search')}" id="searchButton"/>
    </div>
    <s:hidden name="page" value="1"/>
</s:form>