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

    .bg1 {
        background: url('<s:url value="/images/still-birth-flow-detail.gif"/>') no-repeat;
        width: 636px;
        height: 490px;
        margin: 50px 0px 0px 150px;
    }

    .stillBirthLink1 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 245px 0px 0px 577px;

    }

    .stillBirthLink2 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 311px 0px 0px 577px;

    }

    .stillBirthLink3 {
        position: absolute;
        width: 140px;
        height: 45px;
        margin: 380px 0px 0px 577px;

    }


</style>
<div class="birth-registration-outer">
    <s:a href="eprStillBirthRegistrationInit.do">
        <div class="stillBirthLink1"></div>
    </s:a><s:a href="eprBirthRegisterApproval.do">
        <div class="stillBirthLink2"></div>
    </s:a>
    <s:a href="eprBirthCertificateList.do">
        <div class="stillBirthLink3"></div>
    </s:a>
    <div class="bg1"></div>
</div>
