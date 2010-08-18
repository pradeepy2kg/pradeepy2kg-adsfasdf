<%-- @author Duminda Dharmakeerthi --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="death-registration-deatails-outer">
    <div id="death-registration-details-header">
        <s:actionmessage/>
        <s:actionerror/>
    </div>
    <div id="death-registration-details-body">
        <s:if test="#request.warnings.size>0">
            <s:form action="">
                <fieldset>
                    <legend><s:label value="%{getText('death_approval_warning.label')}"/></legend>
                    <table border="0">
                        <s:iterator value="#request.warnings">
                            <tr>
                                <td><s:property value="message"/></td>
                            </tr>
                        </s:iterator>
                    </table>
                    <table border="0">
                        <tr>
                            <td><s:label value="%{getText('ignoreWorning.label')}"/></td>
                            <td><s:checkbox name="ignoreWarning"/></td>
                            <td class="button" align="left"><s:submit name="approve"
                                                                      value="%{getText('approve.label')}"/></td>
                        </tr>
                    </table>
                </fieldset>
            </s:form>
        </s:if>
    </div>
    <div id="death-registration-details-footer">
        <s:url id="newDR" action="eprDeathDeclaration.do">

        </s:url>
        <s:a href="%{newDR}"><s:label value="%{getText('newDR.label')}"/></s:a>
        <s:url id="approveDR" action="eprApproveDeath.do">
            <s:param name="idUKey" value="#request.idUKey"/>
        </s:url>
        <s:a href="%{approveDR}"><s:label value="%{getText('approveDR.label')}"/></s:a>
        <s:url id="home" action="eprInitDeathHome.do"></s:url>
        <s:a href="%{home}"><s:label value="%{getText('goToMain_link.label')}"/></s:a>
    </div>
</div>
