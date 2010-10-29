<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage() {
    }
</script>
<div id="birth-alteration-result-outer">
    <s:if test="pageType==1">
        <table align="center">
            <tr>
                <td></td>
                <td style="text-align:center;">
                    <s:label value="* %{getText('saveSuccess.label')}."/>
                </td>
                <td></td>

            </tr>
            <tr>
                <td></td>
                <td style="width:80%;">
                    <s:if test="#request.allowApproveAlteration && approveRightsToUser">
                        <div class="form-submit" align="center">
                            <s:form action="eprApproveSelectedAlteration.do">
                                <s:hidden name="idUKey"/>
                                <s:hidden name="bdId"/>
                                <s:submit name="approve" value="%{getText('approve.label')}"/>
                            </s:form>
                        </div>
                    </s:if>
                    <div class="form-submit" align="center">
                        <s:form action="eprBirthAlterationInit.do">
                            <s:submit name="approve" value="%{getText('add.new.lable')}"/>
                        </s:form>
                    </div>
                </td>
                <td></td>
            </tr>
        </table>
    </s:if>
    <s:if test="pageType==2">
        <table align="center">
            <tr>
                <td style="text-align:center;width:60%"><s:label
                        value="* %{getText('saveApprovalSuccess.label')}"/></td>
            </tr>
            <tr>
                <td>
                    <div class="form-submit" align="center" style="text-align:left;float:right;">
                        <s:form action="eprPrintBirthAlterarionNotice.do">
                            <s:hidden name="idUKey"/>
                            <s:hidden name="bdId"/>
                            <s:submit name="approve" value="%{getText('print.label')}"/>
                        </s:form>
                    </div>
                    <div class="form-submit" align="center" style="text-align:left;float:right;">
                        <s:form action="eprBirthAlterationInit.do">
                            <s:submit name="approve" value="%{getText('add.new.lable')}"/>
                        </s:form>
                    </div>

                </td>
            </tr>
        </table>
    </s:if>
</div>