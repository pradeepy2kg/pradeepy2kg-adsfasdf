<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:select name="dsDivisionId" list="dsDivisionList" value="dsDivisionId"
          onchange="javascript:view_BDDivs();return false;"/></td></tr>
    <tr>
        <s:url id="loadBDDivList" action="ajaxSupport_loadBDDivList"/>
        <td><label>කොට්ඨාශය பிரிவு Division</label></td>
        <td colspan="6" class="table_reg_cell_01"><sx:div id="birthDivisionId" value="birthDivisionId" href="%{loadBDDivList}" theme="ajax" listenTopics="view_BDDivs"
            formId="birth-registration-form-1" preload="true"></sx:div></td>
    </tr>
