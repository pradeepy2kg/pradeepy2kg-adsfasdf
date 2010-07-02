<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:select id="dsDivisionId" name="dsDivisionId" list="dsDivisionList" value="dsDivisionId"
          onchange="javascript:view_BDDivs();return false;" cssStyle="float:left;  width:240px;"/>

<s:url id="loadBDDivList" action="ajaxSupport_loadBDDivList"/>
<sx:div name="birthDivisionId" id="birthDivisionId" value="birthDivisionId" href="%{loadBDDivList}"
        theme="ajax" listenTopics="view_BDDivs" formId="birth-certificate-print-form">
</sx:div>
<label style="float:right; line-height:25px; vertical-align:middle;">කොට්ඨාශය /பிரிவு /Division</label>

