<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function initPage() {
    }
</script>

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
        margin: 140px 0px 0px 495px;
    }

    .link-approval {
        color: #003399;
        font-size: 11pt;
        text-align: center;
        position: absolute;
        width: 137px;
        height: 39px;
        margin: 207px 0px 0px 493px;
    }

    .link-print {
        color: #003399;
        font-size: 11pt;
        text-align: center;
        position: absolute;
        width: 144px;
        height: 43px;
        margin: 293px 0px 0px 490px;
    }

    .lable-adr {
        font-size: 9pt;
        position: absolute;
        width: 102px;
        text-align: center;
        margin: 253px 0px 0px 616px;
    }

    .lable-child-birth {
        font-size: 9pt;
        position: absolute;
        width: 300px;
        text-align: center;
        margin: 109px 0px 0px 76px;
    }

    .receive-birth-cetificate {
        font-size: 9pt;
        position: absolute;
        width: 300px;
        text-align: center;
        margin: 50px 0px 0px 250px;
    }

    .receive-new-birth-cetificate {
        font-size: 9pt;
        position: absolute;
        width: 100px;
        text-align: center;
        margin: 320px 0px 0px 180px;
    }

    .request-ba {
        font-size: 9pt;
        position: absolute;
        width: 150px;
        text-align: center;
        margin: 90px 0px 0px 562px;
    }
</style>

<s:a href="eprBirthAlterationInit.do">
    <div class="link_data_entry"><s:label value="(1) %{getText('data.entry.lable')}"
                                          cssStyle="cursor:pointer;font-size:9pt"/></div>
</s:a>
<s:a href="eprBirthAlterationPendingApproval.do">
    <div class=" link-approval"><s:label value="  (2)%{getText('data.approve')}"
                                         cssStyle="cursor:pointer; font-size:9pt"/></div>
</s:a>
<s:a href="">
    <div class=" link-print"><s:label value="(3) %{getText('print.lable')}"
                                      cssStyle="cursor:pointer; font-size:9pt"/></div>
</s:a>


<%--div for lables--%>
<div class="lable-adr"><s:label value="%{getText('data.approve')}/DR" cssStyle="font-size:10px"/></div>
<div class="lable-child-birth"><s:label value="%{getText('lb.birth.child')}" cssStyle="font-size:10px"/></div>
<div class="receive-birth-cetificate"><s:label value="%{getText('receive.birth.cetificate.lable')}"
                                               cssStyle="font-size:10px"/></div>
<div class="receive-new-birth-cetificate"><s:label value="%{getText('receive.new.birth.cetificate.lable')}"
                                                   cssStyle="font-size:10px"/></div>
<div class="request-ba"><s:label value="%{getText('request.ba.lable')}" cssStyle="font-size:9px"/></div>
<div class="bg1"></div>
