<%--
  Created by IntelliJ IDEA.
  User: duminda
  Date: May 13, 2010
  Time: 12:43:43 PM
  To change this template use File | Settings | File Templates.
--%>
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
        background-color: aqua;

    }

    .bg1 {
        background: url('<s:url value="/images/birth-flow-detail-pic.gif"/>') no-repeat;
        width: 960px;
        height: 702px;
        margin: 20px 0px 0px 20px;
    }

    .link1 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 165px 0px 0px 455px;
    }

    .link2 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 228px 0px 0px 455px;
    }

    .link3 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 295px 0px 0px 457px;
    }

    .link4 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 370px 0px 0px 457px;
    }

    .link5 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 448px 0px 0px 458px;
    }

    .link6 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 375px 0px 0px 258px;
    }

    .link7 {
        text-align:justify;
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
    <s:a href="eprBirthRegistration.do">
        <div class="link1"></div>
    </s:a><s:a href="eprBirthRegisterApproval.do">
    <div class="link2"></div>
</s:a>
    <s:a href="eprbirthCetificatePrint.do">
        <div class="link3"></div>
    </s:a><s:a href="eprBirthRegisterApproval.do">
    <div class="link4"></div>
</s:a> <s:a href="eprbirthCetificatePrint.do">
    <div class="link5"></div>
</s:a><s:a href="eprbirthCetificatePrint.do">
    <div class="link6"></div>
</s:a>


    <div class="bg1"></div>
</div>
