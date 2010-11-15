<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function initPage(){}
</script>
<div id="error-page-outer">
    <s:actionerror cssStyle="color:red;font-size:10pt"/>
    <hr>
    <h3>RGD Error Message or code - <s:property value="%{getText('RGDerror.'+ exception.getErrorCode())}"/></h3>
    <p></p>
    <hr/>
    <h3>RGD Error Event ID : <s:property value="%{exception.eventId}"/></h3>
    <hr/>
    <p><strong>The new eCivil Registration System is still being developed and tested. We value your effort in
    making this system successful, by reporting issues you encounter, and by suggesting improvements</strong></p>
    <p><strong>Please report the Event ID (<s:property value="%{exception.eventId}"/>) and its related details to the
    <a href="/index.php/help-desk.html?view=detail&cid[0]=-1" target="_blank">Help Desk</a></strong></p>
    <hr/>
    <p><strong>නව සිවිල් ලියාපදිංචි කිරීමේ පද්ධතිය තවමත් සකස්කරමින් පවතී. ඔබගේ යෝජනා හා ප්‍රශ්න ගැන අප දැනුවත් කිරීමෙන් වඩාත් හොඳ පද්ධතියක් සකස් කිරීමට සහය වන්න
</strong></p>
    <p><strong>ඔබ මුහුණ පෑ ප්‍රශ්නය අංකය (<s:property value="%{exception.eventId}"/>) සහ එයට අදාළ විස්තර ගැන අපට කියන්න 
    <a href="/index.php/help-desk.html?view=detail&cid[0]=-1" target="_blank">Help Desk</a></strong></p>
    <hr/>
    <h3>Technical Details</h3>

    <p>

    <div class="technical-error-msg">
        <s:property value="%{exceptionStack}"/>
    </div>
</div>