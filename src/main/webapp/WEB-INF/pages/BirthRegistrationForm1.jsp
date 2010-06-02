<%--
  @author duminda
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="birth-registration-form-outer">
    <s:form action="eprBirthRegistration" name="birthRegistrationForm1" id="birth-registration-form-1" method="POST"
          onsubmit="javascript:return birthRegistrationValidator()">
        <div id="birth-registration-form-header">
            <div id="birth-registration-form-header-logo">
                <img src="<s:url value="./images/official-logo.png"/>" alt=""/>
            </div>
            <div id="birth-registration-form-header-title" class="font-12">
                <label>උපතක් ලියාපදිංචි කිරීම සඳහා විස්තර
                    <br>ஒரு பிறப்பைப் பதிவு செய்வதற்கான விபரங்கள்
                    <br>Particulars for Registration of a Birth</label>
            </div>
            <div id="serial-no">
                <label><span class="font-8">අනුක්‍රමික අංකය<br>தொடர் இலக்கம்<br>Serial Number</span>
                    <s:textfield name="child.bdfSerialNo"/>
                </label>
            </div>
            <div id="submit-date">
                <label><span class="font-8">යොමුකළ දිනය<br>----------<br>Submitted Date</span>
                    <s:select list="{'2009','2010','2011'}" name="" id="submitYear"
                              onchange="javascript:setDate('submitYear','2')"/>
                    <s:select list="{'01','02','03'}" name="" id="submitMonth"
                              onchange="javascript:setDate('submitMonth','2')"/>
                    <s:select list="{'01','02','03'}" name="" id="submitDay"
                              onchange="javascript:setDate('submitDay','2')"/>
                    <sx:datetimepicker id="submitDatePicker" name="dateOfRegistration" label="Format (yyyy-MM-dd)"
                                      displayFormat="yyyy-MM-dd"
                                      onmouseover="javascript:splitDate('submitDatePicker')"/>
                </label>
            </div>
            <div id="birth-registration-form-header-info" class="font-9">
                දැනුම් දෙන්නා (දෙමවිපියන් / භාරකරු) විසින් සම්පුර්ණ කර තොරතුරු වාර්තා කරන නිලධාරි වෙත භාර දිය යුතුය. මෙම
                තොරතුරු මත සිවිල් ලියාපදිංචි කිරිමේ පද්ධතියේ උපත ලියාපදිංචි කරනු ලැබේ.
                <br>தகவல் தருபவரால் (பெற்றோர்/பொறுப்பாளர்) பூா்த்தி செய்யப்பட்டு தகவல் சேகரிக்கும் அதிகாரியிடம்
                சமா்ப்பித்தல் வேண்டும். இத்தகவலின்படி சிவில் பதிவு அமைப்பில் பிறப்பு பதிவு செய்யப்படும்
                <br>Should be perfected by the informant (Parent / Guardian) and the duly completed form should be
                forwarded
                to the Notifying Authority. The birth will be registered in the Civil Registration System based on the
                information provided in this form.
            </div>
        </div>
        <div id="birth-registration-form-child-info-sub-title" class="form-sub-title">
            ළම‌යාගේ විස්තර
            <br>பிள்ளை பற்றிய தகவல்
            <br>Child's Information
        </div>
        <div id="child-dob" class="font-9">
            <div class="form-label">
                (1)උපන් දිනය<br> பிறந்த திகதி <br>Date of Birth
            </div>
            <div id="child-dob-year">
                <div>
                    <label>*in sinhala<br>*in tamil<br>Year</label>
                </div>
                <div>
                    <s:select list="{'2009','2010','2011'}" name="" id="year"
                              onchange="javascript:setDate('year','1')"/>
                </div>

            </div>
            <div id="child-dob-month">
                <div>
                    <label>*in sinhala<br>*in tamil<br>Month</label>
                </div>
                <div>
                    <s:select list="{'01','02','03'}" name="" id="month" onchange="javascript:setDate('month','1')"/>
                </div>
            </div>
            <div id="child-dob-day">
                <div>
                    <label>*in sinhala<br>*in tamil<br>Day</label>
                </div>
                <div>
                    <s:select list="{'01','02','03'}" name="" id="day" onchange="javascript:setDate('day','1')"/>
                </div>
                <div id="datePicker">
                    <sx:datetimepicker id="datePicker" name="childDOB" label="Format (yyyy-MM-dd)"
                                  displayFormat="yyyy-MM-dd" value="2010-05-27"
                                  onmouseover="javascript:splitDate('datePicker')"/>
                </div>
            </div>
        </div>
        <div id="child-pob" class="font-9">
            <div class="form-label">
                (2) උපන් ස්ථානය<br>பிறந்த இடம்<br> Place of Birth
            </div>
            <div id="child-pob-district">
                <div class="form-sub-label">
                    <div>
                        <label>දිස්ත්‍රික්කය மாவட்டம் District</label>
                    </div>
                    <div>
                        <s:select name="birthDistrict" list="districtList" headerKey="0"
                                  headerValue="%{getText('select_district.label')}"/>
                    </div>
                </div>
            </div>
            <div id="child-pob-division">
                <div class="form-sub-label">
                    <div>
                        <label>කොට්ඨාශය பிரிவு Division</label>
                    </div>
                    <div>
                        <s:select name="birthDivision" list="divisionList" headerKey="0"
                                  headerValue="%{getText('select_division.label')}"/>
                    </div>
                </div>
            </div>
            <div id="child-pob-place">
                <div class="form-sub-label">
                    <div>
                        <label>ස්ථානය பிறந்த இடம் Place</label>
                    </div>
                    <div>
                        <s:textfield name="child.placeOfBirth"/>
                    </div>
                </div>
            </div>
            <div id="child-pob-hospitalcode">
                <div class="form-sub-label">
                    <div>
                        <label>රෝහල් කේත අංකය හෝ ග්‍රාම නිලධාරි කේත අංකය<br>வைத்தியசாலை எண் அல்லது கிராம சேவகர் குறி
                            இலக்கம்<br>Hospital Code or GN area code</label>
                    </div>
                    <div>
                        <s:textfield name="child.hospitalCode"/>
                        <s:textfield name="child.gnCode"/>
                    </div>
                </div>
            </div>
        </div>
        <div id="child-name" class="font-9">
            <div class="form-label">
                <label>(3) නම රාජ්‍ය භාෂාවෙන් (සිංහල / දෙමළ)<br>பிறப்பு அத்தாட்சி பாத்த.... (சிங்களம் / தமிழ்) <br>Name
                    in any of the official languages (Sinhala / Tamil)</label>
            </div>
            <div>
                <s:textarea name="child.childFullNameOfficialLang"/>
            </div>
        </div>
        <div id="child-name-in-english" class="font-9">
            <div class="form-label">
                <label>(4) නම ඉංග්‍රීසි භාෂාවෙන් <br>பிறப்பு அத்தாட்சி ….. <br>Name in English </label>
            </div>
            <div>
                <s:textarea name="child.childFullNameEnglish"/>
            </div>
        </div>
        <div id="child-gender" class="font-9">
            <div class="form-label">
                <label>(5)ස්ත්‍රී පුරුෂ භාවය<br> பால் <br>Gender of the child</label>
            </div>
            <div>
                <s:select list="#@java.util.HashMap@{'0':getText('male.label'),'1':getText('female.label'),'2':getText('unknown.label')}"
                          name="child.childGender" headerKey="0" headerValue="%{getText('select_gender.label')}"/>
            </div>
        </div>
        <div id="child-weight" class="font-9">
            <div class="form-label">
                <label>(6) උපත් බර<br>பிறப்பு நிறை<br>Birth Weight (kg)</label>
            </div>
            <div>
                <s:textfield name="child.childBirthWeight"/>
            </div>
        </div>
        <div id="child-birth-order-no" class="font-9">
            <div class="form-label">
                <label>(7)සජිවි උපත් අනුපිළි‍‍වල අනුව කීවෙනි ළමයා ද? <br>பிறப்பு ஒழுங்கு <br>According to Live Birth
                    Order, number of children?</label>
            </div>
            <div>
                <s:textfield name="child.childRank"/>
            </div>
        </div>
        <div id="multiple-birth" class="font-9">
            <div class="form-label">
                <label>(8)නිවුන් දරු උපතක් නම්, දරුවන් ගනන<br>பல்வகைத்தன்மை (இரட்டையர்கள் எனின்), பிள்னளகளின்
                    எண்ணிக்கை<br>If multiple births, number of children</label>
            </div>
            <div>
                <s:textfield name="child.numberOfChildrenBorn"/>
            </div>
        </div>
        <s:hidden name="pageNo" value="1"/>
        <s:submit value="%{getText('next.label')}"/>
    </s:form>
</div>