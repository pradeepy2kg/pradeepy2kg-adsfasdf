<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript ">
    function initPage() {
    }
</script>
<style type="text/css">
    .birth-registration-outer {
        background: none;
        width: 960px;
        height: 702px;
        margin: 0px 0px 0px 0px;
    }

    .bith_data_Entry {
        width: 200px;
        height: 200px;
        padding: 6px 5px;
    }

    .bg1 {
        background: url('<s:url value="/images/still-birth-flow-detail.gif"/>') no-repeat;
        width: 636px;
        height: 490px;
        margin: 50px 0px 0px 150px;
    }

    .link1 {
        text-align: center;
        background: url('<s:url value="/images/back.png"/> ') no-repeat;
        position: absolute;
        width: 100px;
        height: 75px;
        margin: 0px 0px 0px 830px;
        font-size: 11pt;
        color: black;

    }

    .text-margin {
        text-align: center;
        margin-left: 10px;
        margin-top: 28px;
    }

    .stillBirthLink1 {
        font-size: 10pt;
        color: black;
        text-align: center;
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 240px 0px 0px 577px;

    }

    .stillBirthLink2 {
        font-size: 10pt;
        color: black;
        text-align: center;
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 310px 0px 0px 577px;

    }

    .stillBirthLink3 {
        font-size: 10pt;
        color: black;
        text-align: center;
        position: absolute;
        width: 145px;
        height: 45px;
        margin: 375px 0px 0px 572px;
        cursor: pointer;

    }

    .lable-edit1 {

        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 120px;
        margin: 248px 0px 0px 685px;
    }

    .lable-edit2 {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 120px;
        margin: 314px 0px 0px 685px;
    }

    .lable-MR {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 100px 0px 0px 600px;
    }

    .lable-send-confirmation {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 150px;
        margin: 250px 0px 0px 140px;
    }

    .lable-fill-birth-declaration {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 150px;
        margin: 110px 0px 0px 370px;
    }

    .lable-submit-bundle {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 180px;
        margin: 170px 0px 0px 570px;
    }

    .lable-issue-receipt {
        font-size: 9pt;
        text-align: center;
        position: absolute;
        width: 180px;
        margin: 176px 0px 0px 350px;
    }

    .lable-DSoffice {
        font-size: 11pt;
        color: black;
        text-align: center;
        position: absolute;
        width: 150px;
        margin: 465px 0px 0px 485px;
    }

    .fontSize {
        font-size: 10px;
    }
</style>
<div class="birth-registration-outer">
    <div class="lable-edit1"><s:label value="%{getText('sb.edit.lable')}" cssClass="fontSize"/></div>
    <div class="lable-edit2"><s:label value="%{getText('sb.edit.lable')}" cssClass="fontSize"/></div>
    <div class="lable-MR"><s:label value="%{getText('lb.medical.registrar')}" cssClass="fontSize"/></div>
    <div class="lable-send-confirmation">
        <s:label value="%{getText('sb.send..birth.certificate')}" cssClass="fontSize"/>
    </div>
    <div class="lable-fill-birth-declaration">
        <s:label value="%{getText('sb.fill.birth.declaration')}" cssClass="fontSize"/>
    </div>
    <div class="lable-submit-bundle "><s:label value="%{getText('sb.submit.bundle')}" cssClass="fontSize"/></div>
    <div class="lable-issue-receipt "><s:label value="%{getText('sb.issue.receipt')}" cssClass="fontSize"/></div>
    <div class="lable-DSoffice "><s:label value="%{getText('sb.office')}"/></div>
    <s:a href="eprStillBirthRegistrationInit.do">
        <div class="stillBirthLink1">
            <s:label value="%{getText('sb.data.entry')}" cssStyle="cursor:pointer;" cssClass="fontSize"/>
        </div>
    </s:a><s:a href="eprBirthRegisterApproval.do">
    <div class="stillBirthLink2">
        <s:label value="%{getText('sb.approve.ADR')}" cssStyle="cursor:pointer;" cssClass="fontSize"/>
    </div>
</s:a>
    <s:a href="eprBirthRegistrationHome.do">
        <div class="link1">
            <div class="text-margin">
                <s:label value="%{getText('lb.live.birth')}" cssStyle="cursor:pointer" cssClass="fontSize"/>
            </div>
        </div>
    </s:a>
    <s:a href="eprBirthCertificateList.do">
        <div class="stillBirthLink3">
            <s:label value="%{getText('sb.print.certificate')}" cssStyle="cursor:pointer;" cssClass="fontSize"/>
        </div>
    </s:a>
    <div class="bg1"></div>
</div>
