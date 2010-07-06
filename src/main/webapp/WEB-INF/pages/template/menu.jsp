<!--
amith jayasekara
-->
<script src="lib/jquery/jquery.min.js" type="text/javascript"></script>
<script src="js/menu.js" type="text/javascript"></script>
<!--script>
$(document).ready(function(){
   $("a").click(function(event){
     alert("Thanks for visiting!");
   });
 });
</script-->

<link rel="stylesheet" type="text/css" href='css/menu.css'/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="xmain-menu">
    <s:if test="{#session.check=='ok'}">
        <s:set name="checkPage" value="menu1"/>
    </s:if>
    <s:else>
        <s:set name="checkPage" value="menu"/>
    </s:else>


    <ul class="menu">
        <s:iterator value="#session.allowed_menue" id="menue">
            <s:if test="%{((value.size > 0)& (key== 'BIRTH'))||(#session.viewUsers=='ok')}">
                <li class="expand"><s:a href="eprBirthRegistrationHome.do">
                    <s:label value="%{getText('category_birth_registration')}"/></s:a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == 'ADMIN')}">
                <li><a href="#"><s:label value="%{getText('category_admin_task')}"/> </a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == 'DEATH')}">

                <li><a href="#"><s:label value="%{getText('category_death_registraion')}"/> </a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == 'MARRAGE')}">
                <li><a href="#"><s:label value="%{getText('category_marrage_registraion')}"/> </a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == 'REPORT')}">
                <li><a href="#"><s:label value="%{getText('category_reports')}"/> </a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == 'PREFERANCE')}">
                <li><a href="#"><s:label value="%{getText('category_user_preferance')}"/> </a>
                    <ul class="acitem">
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
        </s:iterator>
    </ul>
</div>