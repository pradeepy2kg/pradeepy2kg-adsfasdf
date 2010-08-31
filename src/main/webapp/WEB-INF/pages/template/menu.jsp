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
                   <li class="exp">
                </s:if>
                <s:else><li></s:else>
                <s:if test="%{key == 'birth'}">
                    <s:a href="/popreg/births/eprBirthRegistrationHome.do">
                        <s:label value="%{getText('category_birth_registration')}"/>
                    </s:a>
                </s:if>

                <s:if test="%{key == 'death'}">
                    <s:a href="/popreg/deaths/eprInitDeathHome.do">
                        <s:label value="%{getText('category_death_registraion')}"/>
                    </s:a>
                </s:if>

                <s:if test="%{key == 'marriage'}">
                    <s:a href="#">
                        <s:label value="%{getText('category_marrage_registraion')}"/>
                    </s:a>
                </s:if>

                <s:if test="%{key == 'adoption'}">
                    <s:a href="/popreg/adoption/eprAdoptionRegistrationHome.do">
                        <s:label value="%{getText('category_adoption')}"/>
                    </s:a>
                </s:if>

                <s:if test="%{key == 'preference'}">
                    <s:a href="#">
                        <s:label value="%{getText('category_user_preferance')}"/>
                    </s:a>
                </s:if>

                <s:if test="%{key == 'reports'}">
                    <s:a href="#">
                        <s:label value="%{getText('category_reports')}"/>
                    </s:a>
                </s:if>

                <s:if test="%{key == 'admin'}">
                    <s:a href="#">
                        <s:label value="%{getText('category_admin_task')}"/>
                    </s:a>
                </s:if>

                <s:if test="%{key == 'prs'}">
                    <s:a href="#">
                        <s:label value="%{getText('category_prs')}"/>
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