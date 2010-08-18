<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">
    .bg1 {
        background: url('<s:url value="/images/death_flow.gif"/>') no-repeat;
        width: 960px;
        height: 702px;
        margin: 40px 0px 0px 100px;
        text-align: center;
    }

    .link-data-entry {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 140px;
        height:29px;
        margin: 169px 0px 0px 320px;
        padding-top:11px;
    }

    .link-approval {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 140px;
        height:26px;
        margin: 236px 0px 0px 319px;
        padding-top:15px;
    }

    .link-print-certified {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 140px;
        margin: 311px 0px 0px 324px;
        padding-top:1px;
    }

    .label01 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 120px;
        margin: 180px 0px 0px 460px;
    }

    .label02 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 120px;
        margin: 250px 0px 0px 460px;
    }

</style>
<%-- div tags for main links --%>
<div id="death-home-outer">
    <s:a href="eprInitDeathDeclaration.do" cssStyle="cursor:pointer;">
        <div class=" link-data-entry">
            <s:label value="%{getText('lb.data.entry')}" cssStyle="cursor:pointer;"/>
        </div>
    </s:a>
    <s:a href="eprDeathApprovalAndPrint.do" cssStyle="cursor:pointer;">
        <div class="link-approval">
            <s:label value="%{getText('lb.approve.ADR')}" cssStyle="cursor:pointer;"/>
        </div>
    </s:a>
    <s:a href="eprDeathApprovalAndPrint.do" cssStyle="cursor:pointer;">
        <div class="link-print-certified">
            <s:label value="%{getText('la.print.lable')}" cssStyle="cursor:pointer;"/><br/>
            <s:label value="%{getText('lb.deo.adr.lable')}" cssStyle="cursor:pointer;"/>
        </div>
    </s:a>
    <div class="label01">
        <s:label value="%{getText('lb.edit.lable')}"/>
    </div>
    <div class="label02">
        <s:label value="%{getText('lb.edit.lable')}"/>
    </div>
    <div class="bg1"></div>
</div>