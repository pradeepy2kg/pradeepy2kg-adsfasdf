<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="adoption-alteration-outer">
    <s:actionmessage cssStyle="color: blue; font-size: 10pt;"/>
    <s:form method="POST" name="adoption-alteration-approval">
        <table border="1"
               style="width: 100%; border:1px solid #000; border-collapse:collapse; margin:10px 0;font-size:10pt">
            <col width="200px"/>
            <col width="390px"/>
            <col width="390px"/>
            <col width="50px"/>
            <thead>
            <tr>
                <th></th>
                <th>සහතිකයේ පැවති දත්තය<br>in tamil<br> Before change</th>
                <th>සිදුකල වෙනස්කම<br>செய்யப்பட்ட திருத்தம் <br>Alteration</th>
                <th>අනුමැතිය<br> அனுமதி <br> Approve</th>
            </tr>
            </thead>
            <tbody>
            <s:iterator status="status" value="changesList">

            </s:iterator>
            </tbody>
        </table>
    </s:form>
</div>