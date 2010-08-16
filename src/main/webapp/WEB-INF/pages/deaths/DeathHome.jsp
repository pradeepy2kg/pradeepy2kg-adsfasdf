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

    .link-data-entry {font-size: 9pt;text-align: center;position: absolute;width: 135px;margin: 180px 0px 0px 320px;}

    .link-approval {font-size: 9pt;text-align: center;position: absolute;width: 135px;margin: 250px 0px 0px 330px;}

    .link-print-application {font-size: 9pt;text-align: center;position: absolute;width: 145px; margin: 335px 0px 0px 463px;}

    .link-print-certified {font-size: 9pt;text-align: center;position: absolute;width: 140px;margin: 310px 0px 0px 325px;}

    .link-data-entry-02 {font-size: 9pt; text-align: center; position: absolute; width: 135px; margin: 460px 0px 0px 302px; }

    .label01 { font-size: 9pt;text-align: center; position: absolute; width: 175px; margin: 500px 0px 0px 55px;}

    .label02 {font-size: 9pt; text-align: center; position: absolute;width: 130px; margin: 380px 0px 0px 115px; }

    .label03 {font-size: 9pt; text-align: center; position: absolute;width: 175px;margin: 120px 0px 0px 160px;}

    .label04 { font-size: 9pt; text-align: center; position: absolute; width: 175px;margin: 35px 0px 0px 205px;}

    .label05 { font-size: 9pt; text-align: center; position: absolute; width: 175px; margin: 140px 0px 0px 550px; }

    .label06 { font-size: 9pt; text-align: center; position: absolute; width: 175px; margin: 105px 0px 0px 55px; }

    .label07 { font-size: 9pt;  text-align: center;position: absolute; width: 175px; margin: 180px 0px 0px 400px; }

    .label08 {font-size: 9pt;  text-align: center; position: absolute; width: 175px; margin: 250px 0px 0px 400px;}

    .label09 {font-size: 9pt; text-align: center; position: absolute;width: 175px; margin: 330px 0px 0px 630px; }

    .label10 {font-size: 9pt; text-align: center; position: absolute; width: 175px; margin: 650px 0px 0px 420px;}

</style>
<%-- div tags for main links --%>
<div class=" link-data-entry"><s:label value="%{getText('lb.data.entry')}"/></div>
<div class="link-approval"><s:label value="%{getText('lb.approve.ADR')}"/></div
<div class="link-print-certified"><s:label value="%{getText('la.print.lable')}"/> <br/>
                                    <s:label value="%{getText('lb.deo.adr.lable')}"/></div>
 <%--div tags for labels of image--%>
<%--<div class="label01"><s:label value="%{getText('lb.receive.copy')}"/></div>--%>
<%--<div class="label02"><s:label value="%{getText('lb.application')}"/><br/><s:label value="%{getText('lb.fee.for.issue')}"/></div>--%>
<%--<div class="label03"><s:label value="%{getText('lb.receive.application.01')}"/><br/><s:label value="%{getText('lb.receive.application.02')}"/></div>--%>
<%--<div class="label04"><s:label value="%{getText('lb.req.adoption')}"/></div>--%>
<%--<div class="label05"><s:label value="%{getText('lb.adoption.order')}"/></div>--%>
<%--<div class="label06"><s:label value="%{getText('lb.adoption.parent')}"/></div>--%>
<div class="label07"><s:label value="%{getText('lb.edit.lable')}"/></div>
<div class="label08"><s:label value="%{getText('lb.edit.lable')}"/></div>
<%--<div class="label09"><s:label value="%{getText('lb.approval.lable')}"/><br/><s:label value="%{getText('lb.arg.lable')}"/></div>--%>
<%--<div class="label10"><s:label value="%{getText('lb.office')}"/></div>--%>
<%-- background image div --%>
<div class="bg1"></div>