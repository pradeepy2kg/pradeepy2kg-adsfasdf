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

    .bg1 {
        background: url("images/still-birth-flow-detail.gif") no-repeat;
        width: 636px;
        height: 490px;
        margin: 50px 0px 0px 150px;
    }

    .stillBirthLink1 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 240px 0px 0px 577px;
    }

    .stillBirthLink2 {
        position: absolute;
        width: 135px;
        height: 45px;
        margin: 306px 0px 0px 577px;
    }

    .stillBirthLink3 {
        position: absolute;
        width: 140px;
        height: 45px;
        margin: 375px 0px 0px 577px;
    }


</style>
<div class="birth-registration-outer">
    <s:a href="eprBirthRegistration.do">
        <div class="stillBirthLink1"></div>
    </s:a><s:a href="eprBirthRegisterApproval.do">
    <div class="stillBirthLink2"></div>
</s:a>
    <s:a href="eprbirthCetificatePrint.do">
        <div class="stillBirthLink3"></div>
    </s:a>


    <div class="bg1"></div>
</div>
