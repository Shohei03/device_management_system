<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="action" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>
    </div>
</c:if>
<label for="${AttributeConst.EMP_CODE.getValue()}">社員番号</label><br />
<input type="text" name="${AttributeConst.EMP_CODE.getValue()}" id="${AttributeConst.EMP_CODE.getValue()}" value="${employee.code}" />
<br /><br />

<label for="${AttributeConst.EMP_NAME.getValue()}">氏名</label><br />
<input type="text" name="${AttributeConst.EMP_NAME.getValue()}" id="${AttributeConst.EMP_NAME.getValue()}" value="${employee.name}" />
<br /><br />

<label for="${AttributeConst.EMP_DEPARTMENT.getValue()}">部署</label><br />
<select name="${AttributeConst.EMP_DEPARTMENT.getValue()}" id="${AttributeConst.EMP_DEPARTMENT.getValue()}">
    <option value="${AttributeConst.DEP_GENERAL.getValue()}"<c:if test="${employee.department == AttributeConst.DEP_GENERAL.getValue()}"> selected</c:if>>一般検査室</option>
    <option value="${AttributeConst.DEP_CT.getValue()}"<c:if test="${employee.department == AttributeConst.DEP_CT.getValue()}">selected</c:if>>CT検査室</option>
    <option value="${AttributeConst.DEP_MRI.getValue()}"<c:if test="${employee.department == AttributeConst.DEP_MRI.getValue()}">selected</c:if>>MR検査室</option>
    <option value="${AttributeConst.DEP_MAMMA.getValue()}"<c:if test="${employee.department == AttributeConst.DEP_MAMMA.getValue()}">selected</c:if>>乳房検査室</option>
    <option value="${AttributeConst.DEP_TV.getValue()}"<c:if test="${employee.department == AttributeConst.DEP_TV.getValue()}">selected</c:if>>TV検査室</option>
</select>
<br /><br />

<label for="${AttributeConst.EMP_PASS.getValue()}">パスワード</label><br />
<input type="password" name="${AttributeConst.EMP_PASS.getValue()}" id="${AttributeConst.EMP_PASS.getValue()}" />
<br /><br />

<label for="${AttributeConst.EMP_ADMIN_FLG.getValue()}">権限</label><br />
<select name="${AttributeConst.EMP_ADMIN_FLG.getValue()}" id="${AttributeConst.EMP_ADMIN_FLG.getValue()}">
    <option value="${AttributeConst.ROLE_GENERAL.getIntegerValue()}"<c:if test="${employee.adminFlag == AttributeConst.ROLE_GENERAL.getIntegerValue()}"> selected</c:if>>一般</option>
    <option value="${AttributeConst.ROLE_PARTIAL_ADMIN.getIntegerValue()}"<c:if test="${employee.adminFlag == AttributeConst.ROLE_PARTIAL_ADMIN.getIntegerValue()}">selected</c:if>>デバイスデータ管理者</option>
    <option value="${AttributeConst.ROLE_ADMIN.getIntegerValue()}"<c:if test="${employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}"> selected</c:if>>管理者</option>
</select>
<br /><br />

<input type="hidden" name="${AttributeConst.EMP_ID.getValue()}" value="${employee.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">登録</button>