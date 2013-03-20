<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/media/css/TableTools.css";
    @import "../lib/datatables/media/css/ColVis.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/ecivil/lib/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/ZeroClipboard.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/TableTools.js"></script>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/ColVis.js"></script>
<script type="text/javascript" src="<s:url value="/js/validate.js"/>"></script>
<link rel="stylesheet" href="../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<script>
    $(document).ready(function() {
        $("#tabs").tabs();
    });

    $(document).ready(function() {
        $('#registrars-list-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "bSort": true,
            "bInfo": false,
            "bAutoWidth": false,
            "bJQueryUI": true,
            "sPaginationType": "full_numbers",
            "sDom":'T,C,H<"clear">lftipr'
        });
    });
    var errormsg = "";
    function validatePage() {
        var pin = document.getElementById("registrarPin");
        var name = document.getElementById("registrarName");
        var arr = new Array(pin.value, name.value);
        var counter = 0;
        for (var i = 0; i < arr.length; i++) {
            if (arr[i] != "") {
                counter++;
            }
        }
        if (counter > 1) {
            errormsg = errormsg + document.getElementById('msg_oneMechanism').value;
        }
        if (counter == 0) {
            return false;
        }

        if (pin.value != "") {
            validatePIN(pin, "err_invalide_input_type", "filed_pin");
        }

        var out = checkActiveFieldsForSyntaxErrors('registrar-search-home');
        if(out != ""){
            errormsg = errormsg + out;
        }

        if (errormsg != "") {
            alert(errormsg)
            errormsg = "";
            return false;
        }
        errormsg = "";
        return true;
    }

    function initPage() {
    }
</script>
<fieldset style="margin-bottom:0px;margin-top:5px;border:none;">
    <s:form id="registrar-search-home" method="post" action="eprFindRegistrar.do" onsubmit="javascript:return validatePage()">
        <div id="tabs" style="font-size:10pt;">
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
                            <s:textfield name="registrarPin" id="registrarPin" value="" maxLength="12"
                                         onkeypress="return isNumberKey(event)"/>
                        </td>
                    </tr>
                    </tbody>
                </table>

            </div>
            <div id="fragment-2">
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
                            <s:textfield name="registrarName" id="registrarName" maxLength="20" value=""/>
                        </td>
                    </tr>
                    </tbody>
                </table>

            </div>

        </div>
        <div style="float:left;">
            <s:actionerror cssStyle="color:red;font-size:10pt"/>
            <s:actionmessage cssStyle="color:blue;;font-size:10pt"/>
        </div>
        <div class="form-submit">
            <s:submit type="submit" value="%{getText('button.search')}" id="searchButton"/>
        </div>
        <s:hidden name="page" value="1"/>
    </s:form>
</fieldset>


<s:if test="%{registrarList.size()>0}">
    <fieldset style="margin-bottom:0px;margin-top:5px;border:none;">
        <table id="registrars-list-table" width="100%" cellpadding="0" cellspacing="0" class="display">
            <thead>
            <tr class="table-title">
                <th width="50%"><s:label value="%{getText('label.name')}"/></th>
                <th width="35%"><s:label value="%{getText('registrar.address')}"/></th>
                <th width="15%"><s:label value="%{getText('registrar.phone')}"/></th>
                <s:if test="user.role.roleId == 'ADMIN'">
                    <th width="5%"></th>
                </s:if>
            </tr>
            </thead>
            <tbody>
            <s:iterator status="registrar" value="registrarList" id="registrarList">
                <tr>
                    <td>
                        <s:url id="registrar" action="eprRegistrarsView.do" namespace="/management">
                            <s:param name="registrarUkey" value="registrarUKey"/>
                        </s:url>
                        <s:a href="%{registrar}"><s:label value="%{fullNameInEnglishLanguage}"/> </s:a>
                    </td>
                    <td>
                        <s:label value="%{currentAddress}"/>
                    </td>
                    <td>
                        <s:label value="%{phoneNo}"/>
                    </td>
                    <s:if test="user.role.roleId == 'ADMIN'">
                        <td>
                            <s:url id="deleteSelected" action="eprRegistrarDelete.do" namespace="/management">
                                <s:param name="registrarUkey" value="registrarUKey"/>
                                <s:param name="registrarName" value="registrarName"/>
                                <s:param name="registrarPin" value="registrarPin"/>
                            </s:url>
                            <s:a href="%{deleteSelected}" title="%{getText('deleteToolTip.label')}"><img
                                    src="<s:url value='/images/delete.gif'/>" width="25" height="25"
                                    border="none" onclick="javascript:return deleteWarning('warning')"/></s:a>
                        </td>
                    </s:if>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </fieldset>
</s:if>
<s:hidden id="msg_oneMechanism" value="%{getText('search.use.one.mechanism')}"/>
<s:hidden id="no.data.entered" value="%{getText('no.data')}"/>
<s:hidden id="filed_pin" value="%{getText('label.tab.search.by.registrar.pin')}"/>
<s:hidden id="err_invalide_input_type" value="%{getText('invalide.data')}"/>
<s:hidden id="warning" value="Do you really want to delete this Registrar ?"/>
