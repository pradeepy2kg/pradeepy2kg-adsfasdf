<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">
    .bg1 {
        background: url('<s:url value="/images/"/>') no-repeat;
        width: 960px;
        height: 702px;
        margin: 40px 0px 0px 100px;
        text-align: center;
    }
    .link_search {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 135px;
        height: 39px;
        margin: 204px 0px 0px 467px;
    }

    .link-approval {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 137px;
        height: 39px;
        margin: 270px 0px 0px 465px;
    }
</style>
    
<s:a href="eprBirthAlterationInit.do">
    <div class=" link_search"><s:label value="%{getText('data.search')}" cssStyle="cursor:pointer;"/></div>
</s:a>
<s:a href="eprBirthAlterationPendingApproval.do">
    <div class=" link-approval"><s:label value="%{getText('data.approve')}" cssStyle="cursor:pointer;"/></div>
</s:a>

<div class="bg1"></div>
