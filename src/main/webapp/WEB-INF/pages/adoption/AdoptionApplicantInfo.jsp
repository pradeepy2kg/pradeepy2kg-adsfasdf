<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="adoption-applicant-info-form-outer">
    <script>
        $(function() {
            $('select#birthDistrictId').bind('change', function(evt1) {
                var id = $("select#birthDistrictId").attr("value");
                $.getJSON('/popreg/crs/DivisionLookupService', {id:id},
                        function(data) {
                            var options1 = '';
                            var ds = data.dsDivisionList;
                            for (var i = 0; i < ds.length; i++) {
                                options1 += '<option value="' + ds[i].optionValue + '">' + ds[i].optionDisplay + '</option>';
                            }
                            $("select#dsDivisionId").html(options1);

                            var options2 = '';
                            var bd = data.bdDivisionList;
                            for (var j = 0; j < bd.length; j++) {
                                options2 += '<option value="' + bd[j].optionValue + '">' + bd[j].optionDisplay + '</option>';
                            }
                            $("select#birthDivisionId").html(options2);
                        });
            });

            $('select#dsDivisionId').bind('change', function(evt2) {
                var id = $("select#dsDivisionId").attr("value");
                $.getJSON('/popreg/crs/DivisionLookupService', {id:id, mode:2},
                        function(data) {
                            var options = '';
                            var bd = data.bdDivisionList;
                            for (var i = 0; i < bd.length; i++) {
                                options += '<option value="' + bd[i].optionValue + '">' + bd[i].optionDisplay + '</option>';
                            }
                            $("select#birthDivisionId").html(options);
                        });
            })
        })

    </script>

    <form action="eprAdoptionFind.do" method="post">
        <table style=" border:1px solid #000000; width:300px">
            <tr><s:actionerror/></tr>
            <tr>
                <td>
                    <s:label value="%{getText('adoption_court_order_serial.label')}"/>
                </td>
                <td><s:textfield name="courtOrderNo" id="courtOrderNo"/></td>
            </tr>
        </table>
        <table style=" width:300px">
            <tr>

            <tr>
                <td width="200px"></td>
                <td align="right" class="button"><s:submit name="search"
                                                           value="%{getText('adoption_search_button.label')}"
                                                           cssStyle="margin-right:10px;"/></td>
            </tr>
        </table>
    </form>
    <s:form action="eprAdoptionApplicantInfo.do">
        <s:hidden name="pageNo" value="1"/>
        <table border="1" class="adoption-applicant" cellspacing="0" cellpadding="0"
               style="border:1px solid #000; border-collapse:collapse;">
            <caption></caption>
            <col/>
            <col width="330px"/>
            <col width="175px"/>
            <col width="175px"/>
            <col width="175px"/>
            <col width="175px"/>
            <tbody>
            <tr>
                <td colspan="2">අයදුම් කරු <br/>
                    Applicant
                </td>
                <td>පියා   </br>
                    Father
                </td>
                <td>
                    <s:radio name="certificateApplicantType" list="#@java.util.HashMap@{'0':''}"/>
                </td>
                <td>මව <br/>
                    Mother
                </td>
                <td>
                    <s:radio name="certificateApplicantType" list="#@java.util.HashMap@{'1':''}"/>
                </td>
            </tr>
            <tr>
                <td colspan="4">
                    අයදුම් කරුගේ පුද්ගල අනන්‍යතා අංකය / ජාතික හැදුනුම්පත් අංකය
                    <br>
                    தாயின் தனிநபர் அடையாள எண் / தேசிய அடையாள அட்டை இலக்கம்
                    <br>
                    Applicant's PIN / NIC Number
                    <br>
                </td>
                <td colspan="2">
                    <s:textfield id="certifcateApplicantPin" name="adoption.certificateApplicantPINorNIC"/>
                </td>
            </tr>
                <%--<tr>
                    <td width="30px" style="background:lightgray;"></td>
                    <td>
                        විදේශිකය‍කු නම්
                        <br>
                        வெளிநாட்டவர்
                        <br>
                        If a foreigner
                    </td>
                    <td>
                        රට
                        <br>
                        நாடு
                        <br>
                        Country
                    </td>
                    <td><s:textfield id="certifcateApplicantCountry" name="certifcateApplicantCountry"/></td>
                    <td>
                        ගමන් බලපත්‍ර අංකය
                        <br>
                        கடவுச் சீட்டு
                        <br>
                        Passport No.
                    </td>
                    <td><s:textfield id="certificateApplicantPassportNo" name="certificateApplicantPassportNo"/></td>
                </tr>
                <tr>--%>
            <td colspan="2">
                අයදුම් කරුගේ නම
                <br>
                Name of the Applicant
            </td>
            <td colspan="4">
                <s:textarea id="certificateApplicantName" name="adoption.certificateApplicantName"
                            cssStyle="width:98.2%;"/>
            </td>
            </tr>
            <tr>
                <td colspan="2">
                    ලිපිනය
                    <br>
                    Address
                </td>
                <td colspan="4">
                    <s:textarea id="certificateApplicantAddress" name="adoption.certificateApplicantAddress"
                                cssStyle="width:98.2%;"/>
                </td>
            </tr>
            </tbody>
        </table>

        <div class="adoption-form-submit">
            <s:submit value="%{getText('adoption.submit')}" cssStyle="margin-top:10px;"/>
        </div>

    </s:form>
</div>