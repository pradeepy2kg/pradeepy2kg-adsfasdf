<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
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
    background-color: aqua;
    padding: 6px 5px;
}

.stillBirthButton {
    background: url('<s:url value="/images/for.png"/> ') no-repeat;
    width: 135px;
    height: 45px;
    margin: 5px 0px 0px 800px;

}

.bg1 {
    background: url('<s:url value="/images/birth-flow-detail-pic.gif"/>') no-repeat;
    width: 960px;
    height: 702px;
    margin: 40px 0px 0px 60px;
    text-align: center;
}

.link1 {
    text-align: center;
    position: absolute;
    width: 135px;
    height: 45px;
    margin: 154px 0px 0px 455px;
    font-size: 10pt;
    color: black;
}

.link2 {
    text-align: center;
    position: absolute;
    width: 135px;
    height: 45px;
    margin: 224px 0px 0px 455px;
    font-size: 10pt;
    color: black;
}

.link3 {
    text-align: center;
    position: absolute;
    width: 135px;
    height: 45px;
    margin: 290px 0px 0px 455px;
    font-size: 10pt;
    color: black;
}

.link4 {
    text-align: center;
    position: absolute;
    width: 135px;
    height: 45px;
    margin: 370px 0px 0px 455px;
    font-size: 10pt;
    color: black;
}

.link5 {
    text-align: center;
    position: absolute;
    width: 135px;
    height: 45px;
    margin: 450px 0px 0px 455px;
    font-size: 10pt;
    color: black;
}

.link6 {
    text-align: center;
    position: absolute;
    width: 135px;
    height: 45px;
    margin: 368px 0px 0px 240px;
    font-size: 10pt;
    color: black;
}

.link7 {
    text-align: center;
    background: url('<s:url value="/images/for.png"/> ') no-repeat;
    position: absolute;
    width: 100px;
    height: 76px;
    margin: 0px 0px 0px 840px;
    font-size: 11pt;
    color: black;

}

.text-margin {
    text-align: center;
    margin-left: 2px;
    margin-top: 24px;
}

.lable01 {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 140px;
    margin: 95px 0px 0px 45px;
}

.lable-MR {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 135px;
    height: 45px;
    margin: 70px 0px 0px 450px;
}

.lable-late-reg {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 180px;
    margin: 300px 0px 0px 735px;
}

.lable-fill {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 160px;
    margin: 100px 0px 0px 720px;
}

.lable-ADR {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 275px 0px 0px 620px;
}

.lable-parent {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 365px 0px 0px 30px;
}

.lable-edit1 {

    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 160px 0px 0px 550px;
}

.lable-edit2 {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 230px 0px 0px 550px;
}

.lable-recevie {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 520px 0px 0px 65px;
}

.lable-send-confirmation {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 420px 0px 0px 120px;
}

.lable-recevie-confirmation {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 300px 0px 0px 120px;
}

.lable-recevie-confirmation {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 300px 0px 0px 120px;
}

.lable-issue-receipt {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    top: 210px;
    left: 250px;
    width: 120px;
    margin: 100px 0px 0px 190px;
}

.lable-submit-bundle {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 110px 0px 0px 450px;
}

.lable-approve-nochanges {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 380px 0px 0px 610px;
}

.lable-changes {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 370px 0px 0px 355px;

}

.lable-nochanges {
    font-size: 9pt;
    text-align: center;
    position: absolute;
    width: 120px;
    margin: 455px 0px 0px 315px;
}

.lable-DSoffice {
    font: small-caps;
    font-size: 11pt;
    color: black;
    text-align: center;
    position: absolute;
    width: 150px;
    margin: 555px 0px 0px 445px;
}

.remove-underline {
    text-decoration: none;
    color: black;
}
</style>

<div class="birth-registration-outer">
    <div class="lable01"><s:label value="%{getText('lb.late.fill')}" cssStyle="font-size:10px"/></div>
    <div class="lable-MR"><s:label value="%{getText('lb.medical.registrar')}" cssStyle="font-size:10px"/></div>
    <div class="lable-late-reg"><s:label value="%{getText('lb.late')}" cssStyle="font-size:10px"/></div>
    <div class="lable-fill"><s:label value="%{getText('lb.late.fill')}" cssStyle="font-size:10px"/></div>
    <div class="lable-ADR"><s:label value="%{getText('lb.late.approve')}" cssStyle="font-size:10px"/></div>
    <div class="lable-parent"><s:label value="%{getText('lb.birth.child')}" cssStyle="font-size:10px"/></div>
    <div class="lable-edit1"><s:label value="%{getText('lb.edit.lable')}" cssStyle="font-size:10px"/></div>
    <div class="lable-edit2"><s:label value="%{getText('lb.edit.lable')}" cssStyle="font-size:10px"/></div>
    <div class="lable-recevie"><s:label value="%{getText('lb.receive.birth.certificate')}"
                                        cssStyle="font-size:10px"/></div>
    <div class="lable-send-confirmation"><s:label value="%{getText('lb.send.confirmation.form')}"
                                                  cssStyle="font-size:10px"/></div>
    <div class="lable-recevie-confirmation"><s:label value="%{getText('lb.receive.confirmation.form')}"
                                                     cssStyle="font-size:10px"/></div>
    <div class="lable-issue-receipt"><s:label value="%{getText('lb.issue.receipt')}" cssStyle="font-size:10px"/></div>
    <div class="lable-submit-bundle"><s:label value="%{getText('lb.submit.bundle')}" cssStyle="font-size:10px"/></div>
    <div class="lable-approve-nochanges"><s:label value="%{getText('lb.approve.nochanges')}"
                                                  cssStyle="font-size:10px"/></div>
    <div class="lable-changes"><s:label value="%{getText('lb.changes')}" cssStyle="font-size:10px"/></div>
    <div class="lable-nochanges"><s:label value="%{getText('lb.nochanges')}" cssStyle="font-size:10px"/></div>
    <div class="lable-DSoffice"><s:label value="%{getText('sb.office')}" cssStyle="font-size:10px"/></div>
    <s:a href="eprStillBirth.do">
        <div class="link7">
            <div class="text-margin ">
                <s:label value="%{getText('lb.still.birth')}" cssStyle="cursor:pointer;font-size:10px"/>
            </div>
        </div>
    </s:a>
    <s:a href="eprBirthRegistrationInit.do">
        <div class="link1">
            <s:label value="%{getText('lb.data.entry')}" cssStyle="cursor:pointer;font-size:10px"/>
        </div>
    </s:a>
    <div class="link2">
        <s:if test="(!(#session.user_bean.role.roleId.equals('DEO')))">
            <s:a href="eprBirthRegisterApproval.do" cssClass="remove-underline">
                <s:label value="%{getText('lb.approve.ADR')}" cssStyle="cursor:pointer;font-size:10px;"/>
            </s:a>
        </s:if>
        <s:else>
            <s:label value="%{getText('lb.approve.ADR')}" cssStyle="font-size:10px"/>
        </s:else>
    </div>
    <s:a href="eprBirthConfirmationPrintList.do?confirmListFlag=true">
        <div class="link3">
            <s:label value="%{getText('lb.print.confirmation')}" cssStyle="cursor:pointer;font-size:10px"/>
        </div>
    </s:a>
    <div class="link4">
        <s:if test="(!(#session.user_bean.role.roleId.equals('DEO')))">
            <s:a href="eprBirthConfirmationApproval.do" cssClass="remove-underline">
                <s:label value="%{getText('lb.approve.changes')}" cssStyle="cursor:pointer;font-size:10px"/>
            </s:a>
        </s:if>
        <s:else>
            <s:label value="%{getText('lb.approve.changes')}" cssStyle="font-size:10px"/>
        </s:else>
    </div>
    <s:a href="eprBirthCertificateList.do">
        <div class="link5">
            <s:label value="%{getText('lb.print.certificate')}" cssStyle="cursor:pointer;font-size:10px"/>
        </div>
    </s:a>
    <s:a href="eprBirthConfirmationInit.do">
        <div class="link6">
            <s:label value="%{getText('lb.capture.confirmation')}" cssStyle="cursor:pointer;font-size:10px"/>
        </div>
    </s:a>
    <div class="bg1"></div>
</div>
