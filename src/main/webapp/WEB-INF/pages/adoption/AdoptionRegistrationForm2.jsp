<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="adoption-registration-form-outer">
    <table class="adoption-reg-form-header-table">
        <tr>
            <td>ළමයාගේ විස්තර <br/>
                Child's Information
            </td>
        </tr>
    </table>
    <s:form action="eprAdoptionRegistrationAction.do" name="" id="" method="POST">
        <table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
            <caption></caption>
            <col style="width:330px;"/>
            <col style="width:160px;"/>
            <col style="width:190px;"/>
            <col style="width:160px;"/>
            <col style="width:190px;"/>
            <tbody>
            <tr>
                <td>උපන් දිනය<br/>
                    Date of birth
                </td>
                <td colspan="2" style="text-align:right;"><sx:datetimepicker id="bdayDatePicker" name=""
                                                                             displayFormat="yyyy-MM-dd"
                                                                             onchange="javascript:splitDate('bdayDatePicker')"/></td>
                <td>ස්ත්‍රී පුරුෂ භාවය<br/>
                    Gender
                </td>
                <td><s:select
                        list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                        name="" headerKey="0" headerValue="%{getText('select_gender.label')}"
                        cssStyle="width:190px; margin-left:5px;"/></td>
            </tr>
            <tr>
                <td>වයස <br/>
                    Age
                </td>
                <td>අවුරුදු <br/>
                    Years
                </td>
                <td><s:textfield name="" id="" cssStyle="width:85%;"/></td>
                <td>මාස <br/>
                    Months
                </td>
                <td><s:textfield name="" id="" cssStyle="width:85%;"/></td>
            </tr>
            <tr>
                <td>දැනට පවතින නම <br/>
                    (නමක් දී ඇති නම්) <br/>
                    Existing Name <br/>
                    (if already given)
                </td>
                <td colspan="4"><textarea id="a" name=""></textarea></td>
            </tr>
            <tr>
                <td>ලබා දෙන නම <br/>
                    New name given
                </td>
                <td colspan="4"><textarea id="s" name=""></textarea></td>
            </tr>
            </tbody>
        </table>

        <table class="adoption-reg-form-header-table">
            <tr>
                <td>උපත දැනටත් ලියාපදිංචි කර උප්පැන්න සහතිකයක් නිකුත් කර ඇතිනම් <br/>
                    If the birth is already registered, and a Birth Certificate issued
                </td>
            </tr>
        </table>


        <table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
            <tr>
                <td width="70%">උප්පැන්න සහතිකයේ අනුක්‍රමික අංකය <br/>
                    The serial number of the Birth Certificate
                </td>
                <td width="30%"><s:textfield name="" id="" cssStyle="width:85%;"/></td>
            </tr>
        </table>
       <table class="adoption-reg-form-header-table">
            <tr>
                <td>හෝ<br/>OR
                </td>
            </tr>
        </table>
        <table class="adoption-reg-form-02-table01" cellspacing="0" cellpadding="0">
            <caption></caption>
            <col width="330px"/>
            <col width="700px"/>
            <tbody>
            <tr>
                <td colspan="2" style="text-align:center;">උපත ලියපදින්ච්චි කිරීමේ රිසීට් පතේ සටහන් <br/>
                    Birth Registration acknowledgement slip
                </td>
            </tr>
            <tr>
                <td>දිස්ත්‍රික්කය <br/>
                    District
                </td>
                <td></td>
            </tr>
            <tr>
                <td>ප්‍රාදේශීය ලේකම් කොට්ටාශය <br/>
                    Divisional Secretariat
                </td>
                <td></td>
            </tr>
            <tr>
                <td>ලියාපදිංචි කිරීමේ කොට්ටාශය <br/>
                    Registration Division
                </td>
                <td></td>
            </tr>
            <tr>
                <td>අනුක්‍රමික අංකය <br/>
                    Serial Number
                </td>
                <td></td>
            </tr>
            </tbody>
        </table>
        <s:hidden name="pageNo" value="2"/>
        <div class="adoption-form-submit">
            <s:submit value="%{getText('next.label')}" cssStyle="margin-top:10px;"/>
        </div>
    </s:form>
</div>