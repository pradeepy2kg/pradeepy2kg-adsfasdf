<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="dsDivisionId"
          onchange="javascript:view_BDDivs();return false;"/>

<s:url id="loadBDDivList" action="ajaxSupport_loadBDDivList"/>
<%--<s:if test="{#request.flag.equals('search-bdf-form')}">--%>
    <%--<label>කොට්ඨාශය பிரிவு Division</label>--%>
    <%--<sx:div name="birthDivisionId" id="birthDivisionId" value="birthDivisionId" href="%{loadBDDivList}"--%>
            <%--theme="ajax" cssStyle="float:right;" listenTopics="view_BDDivs" formId="search-bdf-form">--%>
    <%--</sx:div>--%>
<%--</s:if>    --%>
<%--<s:if test="{#request.flag.equals('birth-registration')}">--%>
    <tr>
        <td class="no-bottom-border" id="left-right-border"><label>කොට්ඨාශය பிரிவு Division</label></td>
        <td colspan="6" class="table_reg_cell_01" id="no-bottom-border">
            <sx:div name="birthDivisionId" id="birthDivisionId" value="birthDivisionId" href="%{loadBDDivList}"
                    theme="ajax" listenTopics="view_BDDivs" formId="birth-registration-form-1">
            </sx:div>
        </td>
    </tr>
<%--</s:if>--%>
