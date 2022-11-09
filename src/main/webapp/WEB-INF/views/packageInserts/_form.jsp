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

<fmt:parseDate value="${packegeInsert.createdAt}" pattern="yyyy-MM-dd" var="createdAtDay" type="date" />
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

<label for="AttributeConst.PACK_MRI.getValue()">MRI適合性情報</label><br />
<input type="text" name="${AttributeConst.PACK_MRI.getValue()}" id="${AttributeConst.PACK_MRI.getValue()}" value="${packageInsert.acceptability_of_MR_exam}" />
<br /><br />

<label for="AttributeConst.PACK_MR_STR.getValue()">MRI 装置制限</label><br />
<input type="text" name="${AttributeConst.PACK_MR_STR.getValue()}" id="${AttributeConst.PACK_MR_STR.getValue()}" value="${packageInsert.MR_magnetic_field_strength}" />
<br /><br />

<input type="hidden" name="${AttributeConst.PACK_ID.getValue()}" value="${packageInsert.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">登録</button>

