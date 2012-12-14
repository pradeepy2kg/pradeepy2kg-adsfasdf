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
            <s:if test="adoptionAlteration.changedFields.get(0)">
                <tr>
                    <td></td>
                    <td>
                        <s:if test="adoption.childNewName != null">
                            <s:property value="adoption.childNewName"/>
                        </s:if>
                        <s:else>
                            <s:property value="adoption.childExistingName"/>
                        </s:else>
                    </td>
                    <td><s:property value="adoptionAlteration.childName"/></td>
                    <td><s:checkbox name="childName"/></td>
                </tr>
            </s:if>
            <s:if test="adoptionAlteration.changedFields.get(1)">
                <tr>
                    <td></td>
                    <td><s:property value="adoption.childGender"/></td>
                    <td><s:property value="adoptionAlteration.childGender"/></td>
                    <td><s:checkbox name="childGender"/></td>
                </tr>
            </s:if>
            <s:if test="adoptionAlteration.changedFields.get(2)">
                <tr>
                    <td></td>
                    <td><s:property value="adoption.childBirthDate"/></td>
                    <td><s:property value="adoptionAlteration.childBirthDate"/></td>
                    <td><s:checkbox name="childBirthDate"/></td>
                </tr>
            </s:if>
            </tbody>
        </table>
    </s:form>
</div>