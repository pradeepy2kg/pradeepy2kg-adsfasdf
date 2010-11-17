<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    var BrowserDetect = {
        init: function () {
            this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
            this.version = this.searchVersion(navigator.userAgent)
                    || this.searchVersion(navigator.appVersion)
                    || "an unknown version";
            this.OS = this.searchString(this.dataOS) || "an unknown OS";
        },
        searchString: function (data) {
            for (var i = 0; i < data.length; i++) {
                var dataString = data[i].string;
                var dataProp = data[i].prop;
                this.versionSearchString = data[i].versionSearch || data[i].identity;
                if (dataString) {
                    if (dataString.indexOf(data[i].subString) != -1)
                        return data[i].identity;
                }
                else if (dataProp)
                    return data[i].identity;
            }
        },
        searchVersion: function (dataString) {
            var index = dataString.indexOf(this.versionSearchString);
            if (index == -1) return;
            return parseFloat(dataString.substring(index + this.versionSearchString.length + 1));
        },
        dataBrowser: [
            {
                string: navigator.userAgent,
                subString: "Chrome",
                identity: "Chrome"
            },
            {
                string: navigator.userAgent,
                subString: "OmniWeb",
                versionSearch: "OmniWeb/",
                identity: "OmniWeb"
            },
            {
                string: navigator.vendor,
                subString: "Apple",
                identity: "Safari",
                versionSearch: "Version"
            },
            {
                prop: window.opera,
                identity: "Opera"
            },
            {
                string: navigator.vendor,
                subString: "iCab",
                identity: "iCab"
            },
            {
                string: navigator.vendor,
                subString: "KDE",
                identity: "Konqueror"
            },
            {
                string: navigator.userAgent,
                subString: "Firefox",
                identity: "Firefox"
            },
            {
                string: navigator.vendor,
                subString: "Camino",
                identity: "Camino"
            },
            {
                // for newer Netscapes (6+)
                string: navigator.userAgent,
                subString: "Netscape",
                identity: "Netscape"
            },
            {
                string: navigator.userAgent,
                subString: "MSIE",
                identity: "Explorer",
                versionSearch: "MSIE"
            },
            {
                string: navigator.userAgent,
                subString: "Gecko",
                identity: "Mozilla",
                versionSearch: "rv"
            },
            {
                // for older Netscapes (4-)
                string: navigator.userAgent,
                subString: "Mozilla",
                identity: "Netscape",
                versionSearch: "Mozilla"
            }
        ],
        dataOS : [
            {
                string: navigator.platform,
                subString: "Win",
                identity: "Windows"
            },
            {
                string: navigator.platform,
                subString: "Mac",
                identity: "Mac"
            },
            {
                string: navigator.userAgent,
                subString: "iPhone",
                identity: "iPhone/iPod"
            },
            {
                string: navigator.platform,
                subString: "Linux",
                identity: "Linux"
            }
        ]

    };
    BrowserDetect.init();
    function inVisible() {
        if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)) { //test for Firefox/x.x or Firefox x.x (ignoring remaining digits);
            var ffversion = new Number(RegExp.$1) // capture x.x portion and store as a number
            if (ffversion < 3.6) {
                alert("your FireFox  version is less than 3.6, then get the latest version from here");
                document.getElementById("login-table").style.display = 'none';
                document.getElementById("incorrectUsername").style.display = 'none';
                document.getElementById("web-browser").style.display = 'block';
            }
        }
        else {
            /* alert('You\'re using ' + BrowserDetect.browser + ' ' +
             BrowserDetect.version + ' on ' + BrowserDetect.OS + '\n But recommended web Browser is FireFox 3.6 or above \n Download the FireFox Web Browser From Here');
             window.location = "/../downloads/Firefox_Setup_3.6.8.exe";*/
            document.getElementById("login-table").style.display = 'none';
            document.getElementById("web-browser").style.display = 'block';
            document.getElementById("web-browser-lable-01").style.display = 'none';
            document.getElementById("incorrectUsername").style.display = 'none';

        }
    }


</script>

<html>
<head>
    <title>EPR Login</title>
    <link rel="stylesheet" type="text/css" href="/ecivil/css/layout.css"/>
    <style type="text/css">
        html {
            background: / ecivil / images / body-bg1 . png repeat;
        }

        body {
            clear: both;
        }
    </style>
    <script type="text/javascript">
        function setFocus() {
            inVisible();
            document.getElementById("web-browser").style.display = 'none';
            document.forms[0].userName.focus();
        }

        function initPage(){}

        function checkJS(){
            document.getElementById("jstest").value = "true";
        }

    </script>
</head>
<body onload="setFocus()">
<div id="wrapper">
    <%
        if (session.getAttribute("user_bean") != null) {
            response.sendRedirect("/ecivil/eprHome.do");
        }
    %>

    <img src="/ecivil/images/epr-header.png" align="center" width="1250px"/>

    <div id="login-error" style="text-align:center;">
        <s:actionerror cssStyle="color:red; line-height:30px; font-size:11pt; margin:150px auto -170px auto;"
                       id="incorrectUsername"/>
    </div>

    <div id="login-form">
        <div id="login-form-title">user login</div>
        <div id="login-form-body">
            <div id="web-browser" style="text-align:center;width:100%;">
                <script type="text/javascript">
                    document.write('<p class="accent">* You\'re using ' +
                            BrowserDetect.browser + ' ' + BrowserDetect.version +
                            ' on ' + BrowserDetect.OS + '!</p>');
                </script>

                <div id="web-browser-lable-01">
                    <s:label value="* your FireFox  version is less than 3.6"
                             cssStyle="width:100%"/>
                </div>
                <div id="web-browser-lable-02">
                    <s:label value="* Recommended web Browser is FireFox 3.6 or above"
                             cssStyle="width:100%"/>
                </div>
                <div id="firefox-new">
                    * Download The Recommended FireFox Web Browser <a href="/../downloads/Firefox_Setup_3.6.8.exe">from
                    here</a>
                </div>
            </div>
            <s:form action="/eprLogin.do" method="POST" name="eprLogin">
                <s:hidden name="javaScript" id="jstest" value="false"/>
                <table class="login-table" id="login-table">
                    <tr>
                        <td style="width:50%"><s:label value="User Name: "/></td>
                        <td style="width:50%"><s:textfield name="userName" cssStyle="width:95%"/></td>
                    </tr>
                    <tr>
                        <td><s:label value="Password: "/></td>
                        <td><s:password name="password" cssStyle="width:95%"/></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                        <div><s:submit value="login" onclick="checkJS()"/></div>
                        </td>
                    </tr>
                </table>
            </s:form>
        </div>
    </div>

    <div style="margin-left:auto; margin-right:auto; width:100%; text-align:center;">
        Copyright © 2010 » Registrar General&apos;s Department of Sri Lanka. All Rights Reserved.
    </div>
</div>
</body>
</html>


<%-- Styling Completed --%>