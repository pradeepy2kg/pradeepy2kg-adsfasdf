<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
    }
</script>
<div id="error-page-outer">
    <s:actionerror cssStyle="color:red;font-size:10pt"/>
    <s:if test="exception.message.length() > 0">
    <hr/>
    <div class="error-title"> Error Message</div>
    <div class="form-submit">
        <input type=button value="Back" onClick="history.go(-1)">
    </div>
    <div class="error"><s:property value="%{exception.message}"/></div>
    </p>
    <hr/>
    </s:if>
    <s:if test="exceptionStack.length() > 0">
    <div class="technical-msg-title">Technical Details</div>
    <p>
    <div class="technical-error-msg"><s:property value="%{exceptionStack}"/></div>
    </s:if>
    <div class="form-submit">
        <input type=button value="Back" onClick="history.go(-1)">
    </div>
</div>