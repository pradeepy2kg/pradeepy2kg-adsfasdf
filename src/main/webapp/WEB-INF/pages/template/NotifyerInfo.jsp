<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<table class="table_reg_page_04" width="100%" cellspacing="0" style="border-top:none;">
  <tbody>
    <tr>
        <td width="200px"><label>(34) නම<br>கொடுப்பவரின் பெயர் <br>Name</label></td>
        <td colspan="4">
            <s:textarea name="notifyingAuthority.notifyingAuthorityName" id="notifyingAuthorityName" cssStyle="width:98%;"/>
        </td>
    </tr>
    <tr>
        <td width="200px"><label>තැපැල් ලිපිනය<br>தபால் முகவரி <br>Postal Address</label></td>
        <td colspan="4"><s:textarea name="notifyingAuthority.notifyingAuthorityAddress" cssStyle="width:98%;"/></td>
    </tr>
  </tbody>
</table>


