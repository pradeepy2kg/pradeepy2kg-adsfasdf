<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
        background-color: aqua;
        padding: 6px 5px;
    }

    .stillBirthButton {
        width: 135px;
        height: 45px;
        margin: 5px 0px 0px 800px;

    }

    .bg1 {
        background: url('<s:url value="/images/birth-flow-detail-pic.gif"/>') no-repeat;
        width: 960px;
        height: 702px;
        margin: 40px 0px 0px 60px;
    }

    .link1 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 150px 0px 0px 435px;
    }

    .link2 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 220px 0px 0px 435px;
    }

    .link3 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 285px 0px 0px 435px;
    }

    .link4 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 370px 0px 0px 435px;
    }

    .link5 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 448px 0px 0px 435px;
    }

    .link6 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 355px 0px 0px 220px;
    }

    .link7 {
        text-align: justify;
        background: url('<s:url value="/images/still-birth-link.gif"/> ') no-repeat;
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 0px 0px 0px 850px;

    }
</style>
<div class="birth-registration-outer">
    <s:a href="eprStillBirth.do">
        <div class="link7"></div>
    </s:a>
    <s:a href="eprBirthRegistrationInit.do">
        <div class="link1"></div>
    </s:a>
    <s:a href="eprBirthRegisterApproval.do">
        <div class="link2"></div>
    </s:a>
    <s:a href="eprBirthConfirmationPrintList.do?confirmListFlag=true">
        <div class="link3"></div>
    </s:a>
    <s:a href="eprBirthConfirmationApproval.do">
        <div class="link4"></div>
    </s:a>
    <s:a href="eprBirthCertificateList.do">
        <div class="link5"></div>
    </s:a>
    <s:a href="eprBirthConfirmationInit.do">
        <div class="link6"></div>
    </s:a>
    <div class="bg1"></div>
</div>
