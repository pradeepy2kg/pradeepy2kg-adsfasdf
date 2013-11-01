<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
    }
</script>
<style type="text/css">
    .bg1 {
        background: url('<s:url value="/images/adoption_flow.gif"/>') no-repeat;
        width: 960px;
        height: 702px;
        margin: 40px 0px 0px 100px;
        text-align: center;
    }

    .link-data-entry {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 135px;
        height: 39px;
        margin: 208px 0px 0px 467px;
        padding-top: 6px;
    }

    .link-approval {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 137px;
        height: 39px;
        margin: 274px 0px 0px 465px;
        padding-top: 7px;
    }

    .link-print-application {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 146px;
        height: 94px;
        margin: 338px 0px 0px 463px;
        padding-top:20px;
        cursor: pointer;
    }

    .link-print-certified {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 134px;
        height: 48px;
        margin: 464px 0px 0px 471px;
        cursor: pointer;
        padding-top:5px;
    }

    .link-data-entry-02 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 135px;
        height: 40px;
        margin: 466px 0px 0px 302px;
        cursor: pointer;
        padding-top:3px;
    }

    .label01 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 175px;
        margin: 500px 0px 0px 55px;
    }

    .label02 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 130px;
        margin: 380px 0px 0px 125px;
    }

    .label03 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 175px;
        margin: 135px 0px 0px 165px;
    }

    .label04 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 175px;
        margin: 35px 0px 0px 205px;
    }

    .label05 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 175px;
        margin: 140px 0px 0px 544px;
    }

    .label06 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 175px;
        margin: 105px 0px 0px 55px;
    }

    .label07 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 37px;
        margin: 210px 0px 0px 608px;
    }

    .label08 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 37px;
        margin: 280px 0px 0px 608px;
    }

    .label09 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 175px;
        margin: 330px 0px 0px 605px;
    }

    .label10 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 175px;
        margin: 635px 0px 0px 435px;
    }

</style>
<%-- div tags for main links --%>
<s:a href="eprAdoptionRegistrationAction.do">
    <div class=" link-data-entry">
        <s:label value="%{getText('lb.data.entry')}" cssStyle="cursor:pointer;font-size:10px"/>
    </div>
</s:a>
<s:a href="eprAdoptionApprovalAndPrint.do">
    <div class="link-approval">
        <s:label value="%{getText('lb.approve.ADR')}" cssStyle="cursor:pointer;font-size:10px"/>
    </div>
</s:a>
<s:a href="eprAdoptionApprovalAndPrint.do">
    <div class="link-print-application">
        <s:label value="%{getText('lb.print.application.lable01')}" cssStyle="cursor:pointer;font-size:10px"/><br/>
        <s:label value="%{getText('lb.print.application.lable02')}" cssStyle="font-size:10px;"/>
    </div>
</s:a>
<s:a href="eprAdoptionApprovalAndPrint.do">
    <div class="link-print-certified">
        <s:label value="%{getText('lb.adoption.print.certified.copy')}" cssStyle="cursor:pointer;font-size:10px"/><br/>
        <s:label value="%{getText('lb.deo.adr.lable')}" cssStyle="cursor:pointer;font-size:10px"/>
    </div>
</s:a>
<s:a href="eprAdoptionApplicantInfo.do">
    <div class="link-data-entry-02">
        <s:label value="%{getText('lb.data.entry.applicant.info')}" cssStyle="cursor:pointer;font-size:10px;"/>
    </div>
</s:a>
<%-- div tags for labels of image--%>
<div class="label01"><s:label value="%{getText('lb.receive.copy')}" cssStyle="font-size:10px;"/></div>
<div class="label02">
    <s:label value="%{getText('lb.application')}" cssStyle="font-size:10px;"/><br/>
    <s:label value="%{getText('lb.fee.for.issue')}" cssStyle="font-size:10px;"/>
</div>
<div class="label03">
    <s:label value="%{getText('lb.receive.application.01')}" cssStyle="font-size:10px;"/><br/>
    <s:label value="%{getText('lb.receive.application.02')}" cssStyle="font-size:10px;"/>
</div>
<div class="label04"><s:label value="%{getText('lb.req.adoption')}" cssStyle="font-size:10px;"/></div>
<div class="label05"><s:label value="%{getText('lb.adoption.order')}" cssStyle="font-size:10px;"/></div>
<div class="label06"><s:label value="%{getText('lb.adoption.parent')}" cssStyle="font-size:10px;"/></div>
<div class="label07"><s:label value="%{getText('lb.edit.lable')}" cssStyle="font-size:10px;"/></div>
<div class="label08"><s:label value="%{getText('lb.edit.lable')}" cssStyle="font-size:10px;"/></div>
<div class="label09">
    <s:label value="%{getText('lb.approval.lable')}" cssStyle="font-size:10px;"/><br/>
    <s:label value="%{getText('lb.arg.lable')}" cssStyle="font-size:10px;"/>
</div>
<div class="label10"><s:label value="%{getText('lb.office')}"/></div>
<%-- background image div --%>
<div class="bg1"></div>


