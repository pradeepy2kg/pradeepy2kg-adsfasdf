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
                    <td>ළමයාගේ නම<br/> Name of the Child in ta<br/>Name of the Child</td>
                    <td>
                        <s:if test="adoption.childNewName != null">
                            <s:property value="adoption.childNewName"/>
                        </s:if>
                        <s:else>
                            <s:property value="adoption.childExistingName"/>
                        </s:else>
                    </td>
                    <td><s:property value="adoptionAlteration.childName"/></td>
                    <td align="center"><s:checkbox name="approvedIndex" fieldValue="0"/></td>
                </tr>
            </s:if>
            <s:if test="adoptionAlteration.changedFields.get(1)">
                <tr>
                    <td>ස්ත්‍රී පුරුෂ භාවය<br/>Gender in ta<br/>Gender</td>
                    <td><s:property value="adoption.childGender"/></td>
                    <td><s:property value="adoptionAlteration.childGender"/></td>
                    <td align="center"><s:checkbox name="approvedIndex" fieldValue="1"/></td>
                </tr>
            </s:if>
            <s:if test="adoptionAlteration.changedFields.get(2)">
                <tr>
                    <td>ළමයාගේ උපන්දිනය<br/>Child DOB in ta<br/>Child Date of Birth</td>
                    <td><s:property value="adoption.childBirthDate"/></td>
                    <td><s:property value="adoptionAlteration.childBirthDate"/></td>
                    <td align="center"><s:checkbox name="approvedIndex" fieldValue="2"/></td>
                </tr>
            </s:if>
            <s:if test="adoptionAlteration.changedFields.get(3)">
                <tr>
                    <td>අයදුම්කරුගේ නම<br/>Applicant Name in ta<br/>Applicant Name</td>
                    <td><s:property value="adoption.applicantName"/></td>
                    <td><s:property value="adoptionAlteration.applicantName"/></td>
                    <td align="center"><s:checkbox name="approvedIndex" fieldValue="3"/></td>
                </tr>
            </s:if>
            <s:if test="adoptionAlteration.changedFields.get(4)">
                <tr>
                    <td>අයදුම්කරුගේ ලිපිනය<br/>Applicant Address in ta<br/>Applicant Address</td>
                    <td><s:property value="adoption.applicantAddress"/></td>
                    <td><s:property value="adoptionAlteration.applicantAddress"/></td>
                    <td align="center"><s:checkbox name="approvedIndex" fieldValue="4"/></td>
                </tr>
            </s:if>
            <s:if test="adoptionAlteration.changedFields.get(5)">
                <tr>
                    <td>අයදුම්කරුගේ ලිපිනය 2<br/>Applicant Address 2 in ta<br/>Applicant Address 2</td>
                    <td><s:property value="adoption.applicantSecondAddress"/></td>
                    <td><s:property value="adoptionAlteration.applicantSecondAddress"/></td>
                    <td align="center"><s:checkbox name="approvedIndex" fieldValue="5"/></td>
                </tr>
            </s:if>
            <s:if test="adoptionAlteration.changedFields.get(6)">
                <tr>
                    <td>අයදුම්කරුගේ රැකියාව<br/>Applicant Occupation in ta<br/>Applicant Occupation</td>
                    <td><s:property value="adoption.applicantOccupation"/></td>
                    <td><s:property value="adoptionAlteration.applicantOccupation"/></td>
                    <td align="center"><s:checkbox name="approvedIndex" fieldValue="6"/></td>
                </tr>
            </s:if>
            <s:if test="adoptionAlteration.changedFields.get(7)">
                <tr>
                    <td>සහකරුගේ නම<br/>Spouse Name in ta<br/>Spouse Name</td>
                    <td><s:property value="adoption.spouseName"/></td>
                    <td><s:property value="adoptionAlteration.spouseName"/></td>
                    <td align="center"><s:checkbox name="approvedIndex" fieldValue="7"/></td>
                </tr>
            </s:if>
            <s:if test="adoptionAlteration.changedFields.get(8)">
                <tr>
                    <td>සහකරුගේ රැකියාව<br/>Spouse Occupation in ta<br/>Spouse Occupation</td>
                    <td><s:property value="adoption.spouseOccupation"/></td>
                    <td><s:property value="adoptionAlteration.spouseOccupation"/></td>
                    <td align="center"><s:checkbox name="approvedIndex" fieldValue="8"/></td>
                </tr>
            </s:if>
            <tr>
                <td colspan="4" align="right">
                    <div class="form-submit">
                        <s:hidden name="idUKey" value="%{adoptionAlteration.idUKey}"/>
                        <s:submit action="eprApproveAdoptionAlteration" value="%{getText('approve.label')}"/>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </s:form>
</div>