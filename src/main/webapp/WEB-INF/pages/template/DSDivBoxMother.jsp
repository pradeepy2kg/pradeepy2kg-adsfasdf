<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:select id="motherDSDivisionId" name="motherDSDivisionId" list="dsDivisionList"
          headerKey="0" headerValue="%{getText('select_ds_division.label')}" cssStyle="width:99%;"/>