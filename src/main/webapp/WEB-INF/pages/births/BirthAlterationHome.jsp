<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">
    .bg1 {
        background: url('<s:url value="/images/birth-alteration-flow-detail-pic.gif"/>') no-repeat;
        width: 960px;
        height: 702px;
        margin: 40px 0px 0px 100px;
        text-align: center;
    }

    .link_data_entry {
        color: #003399;
        text-align: center;
        position: absolute;
        width: 135px;
        height: 39px;
        margin: 148px 0px 0px 495px;
    }

    .link-approval {
        color: #003399;
        font-size: 11pt;
        text-align: center;
        position: absolute;
        width: 137px;
        height: 39px;
        margin: 215px 0px 0px 493px;
    }

    .link-print {
        color: #003399;
        font-size: 11pt;
        text-align: center;
        position: absolute;
        width: 144px;
        height: 43px;
        margin: 326px 0px 0px 490px;
    }

    .lable-adr {
        font-size:9pt;
        position: absolute;
        width: 102px;
        text-align: center;
        margin: 270px 0px 0px 660px;
    }

    .lable-child-birth {
        font-size:9pt;
        position: absolute;
        width: 300px;
        text-align: center;
        margin: 105px 0px 0px 130px;
    }
    .receive-birth-cetificate {
        font-size:9pt;
        position: absolute;
        width: 300px;
        text-align: center;
        margin: 50px 0px 0px 280px;
    }
    .receive-new-birth-cetificate {
        font-size:9pt;
        position: absolute;
        width: 100px;
        text-align: center;
        margin:355px 0px 0px 200px;
    }
    .request-ba {
        font-size:9pt;
        position: absolute;
        width: 150px;
        text-align: center;
        margin:103px 0px 0px 570px;
    }
</style>

<s:a href="eprBirthAlterationInit.do">
    <div class="link_data_entry"><s:label value="(1) %{getText('data.entry.lable')}" cssStyle="cursor:pointer;"/></div>
</s:a>
<s:a href="eprBirthAlterationPendingApproval.do">
    <div class=" link-approval"><s:label value="  (2)%{getText('data.approve')}" cssStyle="cursor:pointer;"/></div>
</s:a>
<s:a href="">
    <div class=" link-print"><s:label value="(3) %{getText('print.lable')}" cssStyle="cursor:pointer;"/></div>
</s:a>


<%--div for lables--%>
<div class="lable-adr"><s:label value="%{getText('data.approve')}/DR"/></div>
<div class="lable-child-birth"><s:label value="%{getText('lb.birth.child')}"/></div>
<div class="receive-birth-cetificate"><s:label value="%{getText('receive.birth.cetificate.lable')}"/></div>
<div class="receive-new-birth-cetificate"><s:label value="%{getText('receive.new.birth.cetificate.lable')}"/></div>
<div class="request-ba"><s:label value="%{getText('request.ba.lable')}"/></div>
<div class="bg1"></div>
