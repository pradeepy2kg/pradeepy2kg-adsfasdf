<%@ page import="lk.rgd.crs.web.action.UserPreferencesAction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-tags" %>


<div id="User_Preference_Action">

    <br>
      <s:form action="eprUserPreferencesAction" name="user_preference_form" id="user_preference" method="POST">

          <table >
<tr>
<td> <div id="set-language" class="font -12">
                <label>Select Language </label> </div></td>
<td> <s:select list="#@java.util.HashMap@{'en':'English','si':'සිංහල','ta':'Tamil'}" name="prefLanguage"
                  value="%{#session.WW_TRANS_I18N_LOCALE.language}"></s:select>
</td>
</tr>
<tr>
<td><div id="user-district" class="font-12">
                <label>දිස්ත්‍රික්කය மாவட்டம் District</label></div></td>
<td><s:select name="prefDistrictId" list="districtList" headerKey="0"
                          headerValue="-Select district-"/>
</td>
</tr>
<tr>
    <td> <div id="user-district" class="font-12">
                <label>D.S.කොට්ඨාශය பிரிவு D.S. Division</label></div></td>
    <td> <s:select name="prefDSDivisionId" list="dsDivisionList" headerKey="0"
                          headerValue="-Select DS Division-"/></td>
</tr>              
</table> 

  <br>
       <br>
            <div id="submit">
             <s:submit value="%{getText('submit.label')}"></s:submit>

       </div>
      </s:form>
</div>