<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css" title="currentStyle">
</style>
<script type="text/javascript">
    function initPage() {
    }
</script>
<div class="alteration-print-letter-outer">
    <table class="table_reg_page_05" cellpadding="0" cellspacing="0" id="approvalTable" width="98%">
        <%--<thead style="border-bottom:1px;">--%>
        <td>cage name</td>
        <td>exiting value</td>
        <td>new value</td>
        <td>approve</td>

        </tr>
        <tbody>
        <s:iterator value="printingList">
            <tr>
                <td align="left"><s:property value="%{getText(key)}"/></td>
                <td><s:property value="%{value.get(0)}"/></td>
                <td><s:property value="%{value.get(1)}"/></td>
                <td><s:property value="%{value.get(2)}"/></td>
            </tr>
        </s:iterator>
        </tbody>
    </table>

</div>
