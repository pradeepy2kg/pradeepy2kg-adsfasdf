<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage(){}
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

    .link-print-application {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 146px;
        height: 94px;
        margin: 338px 0px 0px 463px;
        cursor: pointer;
    }

    .link-print-certified {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 134px;
        height: 48px;
        margin: 460px 0px 0px 471px;
        cursor: pointer;
    }

    .link-data-entry-02 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 135px;
        height:40px;
        margin: 462px 0px 0px 302px;
        cursor: pointer;
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
        margin: 380px 0px 0px 115px;
    }

    .label03 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 175px;
        margin: 120px 0px 0px 160px;
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
        margin: 140px 0px 0px 550px;
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
        margin: 210px 0px 0px 605px;
    }

    .label08 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 37px;
        margin: 280px 0px 0px 604px;
    }

    .label09 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 175px;
        margin: 330px 0px 0px 630px;
    }

    .label10 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 175px;
        margin: 650px 0px 0px 420px;
    }

</style>
<%-- div tags for main links --%>
<s:a href="eprAdoptionRegistrationAction.do">
    <div class=" link-data-entry"><s:label value="%{getText('lb.data.entry')}" cssStyle="cursor:pointer;"/></div>
</s:a>
<s:a href="eprAdoptionApprovalAndPrint.do">
    <div class="link-approval"><s:label value="%{getText('lb.approve.ADR')}" cssStyle="cursor:pointer;"/></div>
</s:a>
<s:a href="eprAdoptionApprovalAndPrint.do">
    <div class="link-print-application"><s:label value="%{getText('lb.print.application.lable01')}"
                                                 cssStyle="cursor:pointer;"/><br/>
        <s:label value="%{getText('lb.print.application.lable02')}"/></div>
</s:a>
<s:a href="eprAdoptionApprovalAndPrint.do">
    <div class="link-print-certified"><s:label value="%{getText('lb.adoption.print.certified.copy')}" cssStyle="cursor:pointer;"/>
        <br/>
        <s:label value="%{getText('lb.deo.adr.lable')}" cssStyle="cursor:pointer;"/>
    </div>
</s:a>
<s:a href="eprAdoptionApplicantInfo.do">
    <div class="link-data-entry-02"><s:label value="%{getText('lb.data.entry.applicant.info')}" cssStyle="cursor:pointer;"/></div>
</s:a>
<%-- div tags for labels of image--%>
<div class="label01"><s:label value="%{getText('lb.receive.copy')}"/></div>
<div class="label02"><s:label value="%{getText('lb.application')}"/><br/><s:label
        value="%{getText('lb.fee.for.issue')}"/></div>
<div class="label03"><s:label value="%{getText('lb.receive.application.01')}"/><br/><s:label
        value="%{getText('lb.receive.application.02')}"/></div>
<div class="label04"><s:label value="%{getText('lb.req.adoption')}"/></div>
<div class="label05"><s:label value="%{getText('lb.adoption.order')}"/></div>
<div class="label06"><s:label value="%{getText('lb.adoption.parent')}"/></div>
<div class="label07"><s:label value="%{getText('lb.edit.lable')}"/></div>
<div class="label08"><s:label value="%{getText('lb.edit.lable')}"/></div>
<div class="label09"><s:label value="%{getText('lb.approval.lable')}"/><br/><s:label
        value="%{getText('lb.arg.lable')}"/></div>
<div class="label10"><s:label value="%{getText('lb.office')}"/></div>
<%-- background image div --%>
<div class="bg1"></div>