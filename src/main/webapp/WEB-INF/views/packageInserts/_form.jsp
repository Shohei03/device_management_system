<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>
    </div>
</c:if>

<fmt:parseDate value="${packageInsert.createdAt}" pattern="yyyy-MM-dd" var="createdAtDay" type="date" />
<label for="${AttributeConst.PACK_DATE.getValue()}">日付</label><br />
<input type="date" name="${AttributeConst.PACK_DATE.getValue()}" id="${AttributeConst.PACK_DATE.getValue()}" value="<fmt:formatDate value='${createdAtDay}' pattern='yyyy-MM-dd' />" />
<br /><br />

<label for="AttributeConst.PACK_APP_NUM.getValue()">添付文書承認番号</label><br />
<input type="text" name="${AttributeConst.PACK_APP_NUM.getValue()}" id="${AttributeConst.PACK_APP_NUM.getValue()}" value="${packageInsert.approval_number}" />
<br /><br />


<label for="AttributeConst.PACK_JMDN.getValue()">JMDNコード</label><br />
<input type="text" name="${AttributeConst.PACK_JMDN.getValue()}" id="${AttributeConst.PACK_JMDN.getValue()}" value="${packageInsert.JMDN_code}" />
<br /><br />

<label for="AttributeConst.PACK_GENERAL_NAME.getValue()">一般的名称</label><br />
<input type="text" name="${AttributeConst.PACK_GENERAL_NAME.getValue()}" id="${AttributeConst.PACK_GENERAL_NAME.getValue()}" value="${packageInsert.general_name}" />
<br /><br />

<label for="AttributeConst.PACK_DEV_NAME.getValue()">デバイス名（販売名）</label><br />
<input type="text" name="${AttributeConst.PACK_DEV_NAME.getValue()}" id="${AttributeConst.PACK_DEV_NAME.getValue()}" value="${packageInsert.device_name}" />
<br /><br />


<div>検査の可否</div>
<br />

<label for="AttributeConst.PACK_X_RAY.getValue()">X線検査</label><br />
<select name="${AttributeConst.PACK_X_RAY.getValue()}" id="${AttributeConst.PACK_X_RAY.getValue()}">
    <option value="${AttributeConst.PACK_EXM_SAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_X_ray_exam == AttributeConst.PACK_EXM_SAFE.getValue()}"> selected</c:if>>可能</option>
    <option value="${AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_X_ray_exam == AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue()}">selected</c:if>>条件付き可能</option>
    <option value="${AttributeConst.PACK_EXM_UNSAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_X_ray_exam == AttributeConst.PACK_EXM_UNSAFE.getValue()}"> selected</c:if>>不可能</option>
</select>
<br /><br />

<label for="AttributeConst.PACK_CT.getValue()">CT検査</label><br />
<select name="${AttributeConst.PACK_CT.getValue()}" id="${AttributeConst.PACK_CT.getValue()}">
    <option value="${AttributeConst.PACK_EXM_SAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_CT_exam == AttributeConst.PACK_EXM_SAFE.getValue()}"> selected</c:if>>可能</option>
    <option value="${AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_CT_exam == AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue()}">selected</c:if>>条件付き可能</option>
    <option value="${AttributeConst.PACK_EXM_UNSAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_CT_exam == AttributeConst.PACK_EXM_UNSAFE.getValue()}"> selected</c:if>>不可能</option>
</select>
<br /><br />

<label for="AttributeConst.PACK_TV.getValue()">TV検査</label><br />
<select name="${AttributeConst.PACK_TV.getValue()}" id="${AttributeConst.PACK_TV.getValue()}">
    <option value="${AttributeConst.PACK_EXM_SAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_TV_exam == AttributeConst.PACK_EXM_SAFE.getValue()}"> selected</c:if>>可能</option>
    <option value="${AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_TV_exam == AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue()}">selected</c:if>>条件付き可能</option>
    <option value="${AttributeConst.PACK_EXM_UNSAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_TV_exam == AttributeConst.PACK_EXM_UNSAFE.getValue()}"> selected</c:if>>不可能</option>
</select>
<br /><br />

<label for="AttributeConst.PACK_Manma.getValue()">乳腺X線検査</label><br />
<select name="${AttributeConst.PACK_Manma.getValue()}" id="${AttributeConst.PACK_Manma.getValue()}">
    <option value="${AttributeConst.PACK_EXM_SAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_Manma_exam == AttributeConst.PACK_EXM_SAFE.getValue()}"> selected</c:if>>可能</option>
    <option value="${AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_Manma_exam == AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue()}">selected</c:if>>条件付き可能</option>
    <option value="${AttributeConst.PACK_EXM_UNSAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_Manma_exam == AttributeConst.PACK_EXM_UNSAFE.getValue()}"> selected</c:if>>不可能</option>
</select>
<br /><br />

<label for="AttributeConst.PACK_MRI.getValue()">MRI検査</label><br />
<select name="${AttributeConst.PACK_MRI.getValue()}" id="${AttributeConst.PACK_MRI.getValue()}">
    <option value="${AttributeConst.PACK_EXM_SAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_MR_exam == AttributeConst.PACK_EXM_SAFE.getValue()}"> selected</c:if>>可能</option>
    <option value="${AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_MR_exam == AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue()}">selected</c:if>>条件付き可能</option>
    <option value="${AttributeConst.PACK_EXM_UNSAFE.getValue()}"<c:if test="${packageInsert.acceptability_of_MR_exam == AttributeConst.PACK_EXM_UNSAFE.getValue()}"> selected</c:if>>不可能</option>
</select>
<br /><br />


<input type="hidden" name="${AttributeConst.PACK_ID.getValue()}" value="${packageInsert.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">登録</button>




