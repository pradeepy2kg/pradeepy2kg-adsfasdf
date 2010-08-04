<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="death-declaration-form-3-outer">
    <s:form name="deathRegistrationForm3" action="eprDeathDeclaration" id="death-registration-form-3" method="POST">

        <div class="form-submit">
            <s:hidden name="pageNo" value="3"/>
            <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
        </div>
        <div class="next-previous">
            <s:url id="backUrl" action="eprDeathDeclaration.do">
                <s:param name="back" value="true"/>
                <s:param name="pageNo" value="{pageNo - 1}"/>
            </s:url>
            <s:a href="%{backUrl}"><s:label value="%{getText('previous.label')}"/></s:a>
        </div>
    </s:form>
</div>