<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script src="lib/jquery/jquery.min.js" type="text/javascript"></script>
<script src="js/menu.js" type="text/javascript"></script>

<link rel="stylesheet" type="text/css" href='<s:url value="/css/menu.css"/>'/>

<div id="xmain-menu">
    <s:if test="{#session.check=='ok'}">
        <s:set name="checkPage" value="menu1"/>
    </s:if>
    <s:else>
        <s:set name="checkPage" value="menu"/>
    </s:else>

    <ul class="menu">
        <s:iterator value="#session.allowed_menue" id="menue">
            <s:if test="%{((value.size > 0)& (key== '1births'))||(#session.viewUsers=='ok')}">
                <li class="expand"><s:a href="/popreg/births/eprBirthRegistrationHome.do">
                    <s:label value="%{getText('category_birth_registration')}"/></s:a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{'/popreg/births/' + value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == '4management')}">
                <li><a href="#"><s:label value="%{getText('category_admin_task')}"/> </a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{'/popreg/management/' + value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == '3deaths')}">

                <li><a href="#"><s:label value="%{getText('category_death_registraion')}"/> </a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{'/popreg/deaths/' + value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == '2marriages')}">
                <li><a href="#"><s:label value="%{getText('category_marrage_registraion')}"/> </a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{'/popreg/marriages/' + value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == '5reports')}">
                <li><a href="#"><s:label value="%{getText('category_reports')}"/> </a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{'/popreg/reports/' + value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == '6preferences')}">
                <li><a href="#"><s:label value="%{getText('category_user_preferance')}"/> </a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{'/popreg/preferences/' + value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
        </s:iterator>
    </ul>
</div>