<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<table class="table_reg_page_04" width="100%" cellspacing="0">
  <tbody>
    <tr>
        <td><label>(34) නම<br>கொடுப்பவரின் பெயர் <br>Name</label></td>
        <td colspan="4">
            <s:textarea name="notifyingAuthority.notifyingAuthorityName" id="notifyingAuthorityName"/>
        </td>
    </tr>
    <tr>
        <td><label>තැපැල් ලිපිනය<br>தபால் முகவரி <br>Postal Address</label></td>
        <td colspan="4"><s:textarea name="notifyingAuthority.notifyingAuthorityAddress"/></td>
    </tr>
  </tbody>
</table>


