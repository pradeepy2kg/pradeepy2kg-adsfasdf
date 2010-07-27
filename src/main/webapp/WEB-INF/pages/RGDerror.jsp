<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    <s:actionerror/>
    <hr>
    <h3>RGD Error Message</h3>
    <s:property value="%{getText('RGDerror.'+ exception.getErrorCode())}"/>
    <p></p>
    <hr/>
    <h3>Technical Details</h3>

    <p>

    <div class="technical-error-msg">
        <s:property value="%{exceptionStack}"/>
    </div>