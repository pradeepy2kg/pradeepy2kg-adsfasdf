<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div>
    <s:actionerror/>
    <hr/>
    <h3>Error Message</h3>
    <s:property value="%{exception.message}"/>
      </p>
      <hr/>
      <h3>Technical Details</h3>
      <p>
    <s:property value="%{exceptionStack}"/>
</div>