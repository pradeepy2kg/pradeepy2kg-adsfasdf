<!--
amith jayasekara
-->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css" media="screen">

    /* Actual menu CSS starts here */

</style>

<script type="text/javascript">
    var toggleMenu = {

        init : function(sContainerClass, sHiddenClass) {
            if (!document.getElementById || !document.createTextNode) {
                return;
            } // Check for DOM support
            var arrMenus = this.getElementsByClassName(document, 'ul', sContainerClass);
            var arrSubMenus, oSubMenu, oLink,checkLink;
            checkLink = location.href;
            for (var i = 0; i < arrMenus.length; i++) {
                arrSubMenus = arrMenus[i].getElementsByTagName('ul');
                for (var j = 0; j < arrSubMenus.length; j++) {
                    oSubMenu = arrSubMenus[j];
                    oLink = oSubMenu.parentNode.getElementsByTagName('a')[0];
                    oLink.onmouseover = function() {
                        toggleMenu.toggle(this.parentNode.getElementsByTagName('ul')[0], sHiddenClass);
                        return false;
                    }
                    this.toggle(oSubMenu, sHiddenClass);
                }
            }
        },
        toggle : function(el, sHiddenClass) {
            var oRegExp = new RegExp("(^|\\s)" + sHiddenClass + "(\\s|$)");
            el.className = (oRegExp.test(el.className)) ? el.className.replace(oRegExp, '') : el.className + ' ' + sHiddenClass; // Add or remove the class name that hides the element
        },

        addEvent : function(obj, type, fn) {
            if (obj.addEventListener)
                obj.addEventListener(type, fn, false);
            else if (obj.attachEvent) {
                obj["e" + type + fn] = fn;
                obj[type + fn] = function() {
                    obj["e" + type + fn](window.event);
                }
                obj.attachEvent("on" + type, obj[type + fn]);
            }
        },

        getElementsByClassName : function(oElm, strTagName, strClassName) {
            var arrElements = (strTagName == "*" && document.all) ? document.all : oElm.getElementsByTagName(strTagName);
            var arrReturnElements = new Array();
            strClassName = strClassName.replace(/\-/g, "\\-");
            var oRegExp = new RegExp("(^|\\s)" + strClassName + "(\\s|$)");
            var oElement;
            for (var i = 0; i < arrElements.length; i++) {
                oElement = arrElements[i];
                if (oRegExp.test(oElement.className)) {
                    arrReturnElements.push(oElement);
                }
            }
            return (arrReturnElements)
        }
    };
    toggleMenu.addEvent(window, 'load', function() {
        toggleMenu.init('menu', 'hidden');


    });


</script>

<div id="main-menu">
    <s:if test="{#session.check=='ok'}">
        <s:set name="checkPage" value="menu1"/>
    </s:if>
    <s:else>
        <s:set name="checkPage" value="menu"/>
    </s:else>
    <ul class="menu">
        <s:iterator value="#session.allowed_menue" id="menue">
            <s:if test="%{((value.size > 0)& (key== 'BIRTH'))||(#session.viewUsers=='ok')}">
                <li><s:a href="eprBirthRegistrationHome.do">
                    <s:label value="%{getText('category_birth_registration')}"/></s:a>
                    <ul>
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == 'ADMIN')}">
                <li><a href="."><s:label value="%{getText('category_admin_task')}"/> </a>
                    <ul>
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == 'DEATH')}">

                <li><s:a href="%{editSelected}"><s:label value="%{getText('category_death_registraion')}"/> </s:a>
                    <ul>
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == 'MARRAGE')}">
                <li><a href=><s:label value="%{getText('category_marrage_registraion')}"/> </a>
                    <ul>
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == 'REPORT')}">
                <li><a href="."><s:label value="%{getText('category_reports')}"/> </a>
                    <ul>
                        <s:iterator value="value" id="x">
                            <li><s:a href="%{value.action}"><s:property value="%{getText(value.propertyKey)}"/>
                            </s:a></li>
                        </s:iterator>
                    </ul>
                </li>
            </s:if>
            <s:if test="%{(value.size > 0)& (key == 'PREFERANCE')}">
                <li><s:a href="."><s:label value="%{getText('category_user_preferance')}"/> </s:a>
                    <ul>
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