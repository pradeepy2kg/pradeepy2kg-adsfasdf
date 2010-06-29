<!--
amith jayasekara
-->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css" media="screen,print">
    @import '/css/lab.css';

    body {
        padding: 1em
    }

    #wrap {
        width: 500px;
        margin: 1em auto;
    }

    body {
        font: 76% / 1.4 Verdana, sans-serif;
    }

    /* Disable properties specified in the imported CSS file */
    .menu a {
        border: none;
        font-weight: normal;
    }

    /* Actual menu CSS starts here */
    .menu,
        .menu ul {
        margin: 0;
        padding: 0;
        list-style: none;
    }

    .menu {
        width: 200px;
    }

    .menu li {
        display: block;
        margin: 0;
        padding: 0;
        margin-bottom: 1px;
    }

    .menu a {
        display: block;
        padding: 2px 5px;
        color: #000;
        background: #f0d3af;
        text-decoration: none;
    }

    .menu a:hover,
        .menu a:focus,
        .menu a:active {
        background: #943e0e;
    }

    .menu ul li {
        padding-left: 15px;
    }

    .menu ul a {
        background: #fbf9ed;
    }

    .hidden {
        display: none;
    }
</style>

<script type="text/javascript">
    var toggleMenu = {

        init : function(sContainerClass, sHiddenClass) {
            if (!document.getElementById || !document.createTextNode) {
                return;
            } // Check for DOM support
            var arrMenus = this.getElementsByClassName(document, 'ul', sContainerClass);
            var arrSubMenus, oSubMenu, oLink;
            for (var i = 0; i < arrMenus.length; i++) {
                arrSubMenus = arrMenus[i].getElementsByTagName('ul');
                for (var j = 0; j < arrSubMenus.length; j++) {
                    oSubMenu = arrSubMenus[j];
                    oLink = oSubMenu.parentNode.getElementsByTagName('a')[0];
                    oLink.onclick = function() {
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

<body>
<div id="main-menue">
    <ul class="menu">
        <s:iterator value="#session.allowed_menue" id="menue">
            <s:if test="%{(value.size > 0)& (key== 'BIRTH')}">
                <li><s:a ondblclick="parent.location='eprBirthRegistrationHome.do'">
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
                <li><a href="."><s:label value="%{getText('category_user_preferance')}"/> </a>
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

</body>