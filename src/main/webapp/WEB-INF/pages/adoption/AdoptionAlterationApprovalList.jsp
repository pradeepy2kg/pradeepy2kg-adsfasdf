<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css" title="currentStyle">
    @import "../lib/datatables/media/css/demo_page.css";
    @import "../lib/datatables/media/css/demo_table.css";
    @import "../lib/datatables/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript" src="../lib/datatables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript">
    $(function () {
        $('#adoptionAlterationListTable').dataTable({
            "bPaginate":true,
            "bLengthChange":false,
            "bFilter":true,
            "aaSorting":[
                [0, 'desc']
            ],
            "bInfo":false,
            "bAutoWidth":false,
            "bJQueryUI":true,
            "sPaginationType":"full_numbers"
        });
    });
</script>

<div id="adoption-alteration-outer">
    <s:actionerror cssStyle="color: red; font-size: 10pt;"/>
    <s:actionmessage cssStyle="color: blue; font-size: 10pt;"/>
    <div style="float: left; width: 100%;">
        <table width="100%" id="adoptionAlterationListTable" cellpadding="0" cellspacing="0" class="display">
            <col width="80px"/>
            <col/>
            <col width="120px"/>
            <col width="100px"/>
            <col width="50px"/>
            <col width="50px"/>
            <col width="50px"/>
            <thead>
            <tr align="center">
                <th>#</th>
                <th><s:label value="%{getText('child.name.label')}"/></th>
                <th align="center"><s:label value="%{getText('method.label')}"/></th>
                <th align="center"><s:label value="%{getText('status.label')}"/></th>
                <th></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator id="adoptionAlterationList" value="adoptionAlterationList" status="status">
                <tr>
                    <td><s:property value="idUKey"/></td>
                    <td><s:property value="childName"/></td>
                    <td align="center"><s:property value="method"/></td>
                    <td align="center"><s:property value="status"/></td>
                    <td align="center">
                        <s:url id="alterAdoptionEditUrl" action="eprPopulateAdoptionAlterationForEdit.do">
                            <s:param name="idUKey" value="idUKey"/>
                            <s:param name="editMode" value="true"/>
                        </s:url>
                        <s:a href="%{alterAdoptionEditUrl}" title="%{getText('edit.label')}">
                            <img id="editImage" src="<s:url value='/images/edit.png'/>" width="25" height="25"
                                 border="none"/>
                        </s:a>
                    </td>
                    <td align="center">
                        <s:if test="(#session.user_bean.role.roleId.equals('ADR') || #session.user_bean.role.roleId.equals('ARG') || #session.user_bean.role.roleId.equals('RG'))">
                            <s:url id="alterAdoptionApproveUrl" action="eprLoadAdoptionAlterationForApprove.do">
                                <s:param name="idUKey" value="idUKey"/>
                            </s:url>
                            <s:a href="%{alterAdoptionApproveUrl}" title="%{getText('alter.label')}">
                                <img id="editImage" src="<s:url value='/images/approve.gif'/>" width="25" height="25"
                                     border="none"/>
                            </s:a>
                        </s:if>
                    </td>
                    <td align="center">
                        <s:url id="alterAdoptionDeleteUrl" action="eprPopulateAdoptionAlterationDelete.do">
                            <s:param name="idUKey" value="idUKey"/>
                        </s:url>
                        <s:a href="%{alterAdoptionDeleteUrl}" title="%{getText('alter.label')}">
                            <img id="editImage" src="<s:url value='/images/delete.gif'/>" width="25" height="25"
                                 border="none"/>
                        </s:a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </div>
</div>