<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage(){}
</script>
<style type="text/css">
    .bg1 {
        background: url('<s:url value="/images/death.png"/>') no-repeat;
        width: 960px;
        height: 702px;
        margin: 40px 0px 0px 80px;
        text-align: center;
    }

    #death-declaration-link {
        width:110px;
        height:110px;
        position:absolute;
        margin: 170px 0px 0px 410px;
    }

    #late-death-declaration-link {
        width:110px;
        height:80px;
        position:absolute;
        margin: 130px 0px 0px 620px;
    }

    .link-data-entry {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 140px;
        height: 29px;
        margin: 399px 0px 0px 370px;
        padding-top: 11px;
    }

    .link-approval {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 140px;
        height: 26px;
        margin: 455px 0px 0px 369px;
        padding-top: 15px;
    }

    .link-print-certified {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 140px;
        margin: 541px 0px 0px 374px;
        padding-top: 1px;
    }

    .label01 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 120px;
        margin: 410px 0px 0px 540px;
    }

    .label02 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 120px;
        margin: 480px 0px 0px 510px;
    }

    #label03 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        margin: 10px 0px 0px 100px;
    }

    #label04 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        margin: 90px 0px 0px 260px;
    }

    #label05 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        margin: 290px 0px 0px 400px;
    }

    #label06 {
        font-size: 9pt;
        text-align: left;
        position: absolute;
        margin: 220px 0px 0px 640px;
    }

    #label07 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        margin: 360px 0px 0px 200px;
    }

    #label08 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        margin: 590px 0px 0px 160px;
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
    <s:a href="eprInitDeathDeclaration.do">
        <div id="death-declaration-link"></div>
    </s:a>
    <s:a href="eprInitLateDeathDeclaration.do">
        <div id="late-death-declaration-link"></div>
    </s:a>
    <div class="label01">
        <s:label value="%{getText('lb.edit.lable')}"/>
    </div>
    <div class="label02">
        <s:label value="%{getText('lb.edit.lable')}"/>
    </div>
    <div id="label03">
        <s:label value="%{getText('occurance.label')}"/>
    </div>
    <div id="label04">
        <s:label value="%{getText('establish_cause.label')}"/> <br/>
        <s:label value="%{getText('DJmoI.label')}"/>
    </div>
    <div id="label05">
        <s:label value="%{getText('death_register.label')}"/>
    </div>
    <div id="label06">
        <s:label value="%{getText('late_death.label')}"/> <br/>
        <s:label value="%{getText('late_death_missing.label')}"/> <br/>
        <s:label value="%{getText('late_death_overseas.label')}"/>
    </div>
    <div id="label07">
        <s:label value="%{getText('ds_office.label')}"/>
    </div>
    <div id="label08">
        <s:label value="%{getText('send_post.label')}"/>
    </div>
    <div class="bg1"></div>
</div>