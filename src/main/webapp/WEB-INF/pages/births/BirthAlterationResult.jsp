<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage() {
    }
</script>
<div id="birth-alteration-result-outer">
    <s:if test="pageNo==1">
        <table align="center">
            <tr>
                <td></td>
                <td style="text-align:center;">
                    <s:label value="%{getText('saveSuccess.label')}"/>
                </td>
                <td></td>

            </tr>
            <tr>
                <td></td>
                <td style="width:40%;"><s:if test="#request.allowApproveAlteration">
                    <div class="form-submit" align="center">
                        <s:form action="eprApproveSelectedAlteration.do">
                            <s:hidden name="idUKey"/>
                            <s:hidden name="bdId"/>
                            <s:submit name="approve" value="%{getText('approve.label')}"/>
                        </s:form>
                    </div>
                </s:if>
                    <div class="form-submit" style="float:left;">
                        <s:form action="eprBirthAlterationInit.do">
                            <s:submit name="approve" value="%{getText('add.new.lable')}"/>
                        </s:form>
                    </div>
                </td>
                <td></td>
            </tr>
        </table>
    </s:if>
    <s:if test="pageNo==2">
        <table align="center">
            <tr>
                <td></td>
                <td style="text-align:center;width:40%"><s:label value="%{getText('saveApprovalSuccess.label')}"/> </td>
                <td></td>
            </tr>
            <tr>
                <td></td>
                <td align="center">
                    <div class="form-submit" align="center" style="text-align:left;margin-right:31%">
                        <s:form action="eprBirthAlterationInit.do">
                            <s:submit name="approve" value="%{getText('add.new.lable')}"/>
                        </s:form>
                    </div>
                </td>
                <td></td>
            </tr>
        </table>
    </s:if>
</div>