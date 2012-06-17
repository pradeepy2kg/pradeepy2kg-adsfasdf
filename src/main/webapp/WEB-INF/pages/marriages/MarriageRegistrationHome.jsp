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
        margin: 0 auto;
    }

    .bg1 {
        background: url('<s:url value="/images/marriage-flow.gif"/>') no-repeat;
        width: 800px;
        height: 650px;
        margin: -10px 0 20px 200px;
        text-align: center;
    }

    .label_parties_to_DS {
        text-align: center;
        position: absolute;
        right: 700px;
        top: 285px;
    }

    .label_parties_to_registrar {
        text-align: center;
        position: absolute;
        top: 265px;
        right: 450px;
    }

    .label_capture_notice {
        text-align: center;
        position: absolute;
        right: 600px;
        top: 355px;
    }

    .label_validate_approve {
        text-align: center;
        position: absolute;
        right: 590px;
        top: 430px;
    }

    .label_capture_objections {
        text-align: center;
        position: absolute;
        right: 485px;
        top: 500px;
    }

    .label_objection_validate_approve {
        text-align: center;
        position: absolute;
        right: 470px;
        top: 570px;
    }

    .label_print_license {
        text-align: center;
        position: absolute;
        right: 610px;
        top: 645px;
    }

    .label_expire_license {
        text-align: center;
        position: absolute;
        right: 700px;
        top: 550px;
    }

    .label_license_collect {
        text-align: center;
        position: absolute;
        right: 250px;
        top: 645px;
    }

    .label_objection {
        text-align: center;
        position: absolute;
        right: 170px;
        top: 500px;
    }

    .label_ADR {
        text-align: center;
        position: absolute;
        right: 750px;
        top: 420px;
    }
</style>

<div class="birth-registration-outer">
    <div class="label_parties_to_DS">
        <s:label value="%{getText('label.both.party.visit.DS.office')}"/>
    </div>
    <div class="label_parties_to_registrar">
        <s:label value="%{getText('label.party.visit.registrar.office')}"/>
    </div>
    <s:a href="eprSelectNoticeType.do">
        <div class="label_capture_notice">
            <s:label value="%{getText('label.capture.notice')}" cssStyle="cursor:pointer;font-size:10px"/>
        </div>
    </s:a>
    <s:a href="eprMarriageNoticeSearchInit.do">
        <div class="label_validate_approve">
            <s:label value="%{getText('label.validate.approve')}" cssStyle="cursor:pointer;font-size:10px"/>
        </div>
    </s:a>
    <div class="label_capture_objections">
        <s:label value="%{getText('label.capture.objection')}"/>
    </div>
    <s:a href="eprMarriageNoticeSearchInit.do">
        <div class="label_objection_validate_approve">
            <s:label value="%{getText('label.validate.approve')}" cssStyle="cursor:pointer;font-size:10px"/>
        </div>
    </s:a>
    <s:a href="eprMarriageNoticeSearchInit.do">
        <div class="label_print_license">
            <s:label value="%{getText('label.print.license')}" cssStyle="cursor:pointer;font-size:10px"/>
        </div>
    </s:a>
    <div class="label_expire_license">
        <s:label value="%{getText('label.expire.license')}"/>
    </div>
    <div class="label_license_collect">
        <s:label value="%{getText('label.license.collect')}"/>
    </div>
    <div class="label_objection">
        <s:label value="%{getText('label.objections.to.marriage')}"/>
    </div>
    <div class="label_ADR">
        <s:label value="%{getText('label.ADR')}"/>
    </div>
    <div class="bg1"></div>
</div>
