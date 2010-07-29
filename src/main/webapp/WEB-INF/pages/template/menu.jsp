<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" type="text/css" href='/popreg/css/menu.css'/>
<script src="/popreg/lib/jquery/jquery.min.js" type="text/javascript"></script>
<script src="/popreg/js/menu.js" type="text/javascript"></script>

<div id="xmain-menu">
    <ul class="menu">
        <s:iterator value="#session.allowed_menue" id="menue">
            <s:if test="%{value.size > 0}">
                <s:if test="%{#session.context==key}">
                    <li class="expand">
                </s:if><s:else><li></s:else>

                <s:if test="%{key=='0births'}">
                    <s:a href="/popreg/births/eprBirthRegistrationHome.do">
                        <s:label value="%{getText('category_birth_registration')}"/>
                    </s:a>
                </s:if>

                <s:if test="%{key == '5management'}">
                    <a href="#"><s:label value="%{getText('category_admin_task')}"/></a>
                </s:if>

                <s:if test="%{key == '1deaths'}">
                    <li><a href="#"><s:label value="%{getText('category_death_registraion')}"/> </a>
                </s:if>

                <s:if test="%{key == '2marriages'}">
                    <li><a href="#"><s:label value="%{getText('category_marrage_registraion')}"/> </a>
                </s:if>

                <s:if test="%{key == '4reprots'}">
                    <a href="#"><s:label value="%{getText('category_reports')}"/></a>
                </s:if>

                <s:if test="%{key == '3preferences'}">
                    <a href="#"><s:label value="%{getText('category_user_preferance')}"/></a>
                </s:if>

                <s:if test="%{key == '6prs'}">
                    <a href="#"><s:label value="%{getText('category_prs')}"/></a>
                </s:if>
                <s:if test="%{key == '7adoption'}">
                    <s:a href="">
                        <s:label value="%{getText('category_adoption')}"/>
                    </s:a>
                </s:if>

                <ul class="acitem">
                    <s:iterator value="value" id="x">
                        <s:if test="%{value.propertyKey != null}">
                            <li>
                                <s:a href="%{value.category+value.action}">
                                    <s:property value="%{getText(value.propertyKey)}"/>
                                </s:a>
                            </li>
                        </s:if>
                    </s:iterator>
                </ul>
                </li>
            </s:if>
        </s:iterator>
    </ul>
</div>