<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.MessageConst" %>


<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
            <c:if test="${error == MessageConst.E_DUPLI_DATA.getMessage()}">
                <form method="POST" action="<c:url value='?action=${actPatDev}&command=${commCrt}' />" >
                    <input type="hidden" name="${AttributeConst.PATDEV_DUPLICATE_CHECK.getValue()}" value="false" />
                    <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
                    <input type="hidden" name="${AttributeConst.PATDEV_PAT_ID.getValue()}" value="${patientDevice.patient_id}" />
                    <input type="hidden" name="${AttributeConst.PATDEV_PAT_NAME.getValue()}" value="${patientDevice.patient_name}" />
                    <input type="hidden" name="${AttributeConst.PATDEV_PAT_NAME_KANA.getValue()}" value="${patientDevice.patient_name_kana}" />
                    <input type="hidden" name="${AttributeConst.PATDEV_APP_NUM.getValue()}" value="${patientDevice.approval_number}" />
                    <input type="hidden" name="${AttributeConst.PATDEV_DEV_NAME.getValue()}" value="${patientDevice.device_name}" />
                    <input type="hidden" name="${AttributeConst.PATDEV_IMP_DATE.getValue()}" value="${patientDevice.implantedAt}" />
                    <input type="hidden" name="${AttributeConst.PATDEV_ID.getValue()}" value="${patientDevice.id}" />
                    <button type="submit">重複登録</button>
                </form>
            </c:if>
        </c:forEach>
    </div>
</c:if>

<label for="${AttributeConst.PATDEV_PAT_ID.getValue()}">患者ID</label><br />
<input type="text" name="${AttributeConst.PATDEV_PAT_ID.getValue()}" id="${AttributeConst.PATDEV_PAT_ID.getValue()}" value="${patientDevice.patient_id}" />
<br /><br />

<label for="${AttributeConst.PATDEV_PAT_NAME.getValue()}">患者名</label><br />
<input type="text" name="${AttributeConst.PATDEV_PAT_NAME.getValue()}" id="${AttributeConst.PATDEV_PAT_NAME.getValue()}" value="${patientDevice.patient_name}" />
<br /><br />

<label for="${AttributeConst.PATDEV_PAT_NAME_KANA.getValue()}">患者名（ひらがな)</label><br />
<input type="text" name="${AttributeConst.PATDEV_PAT_NAME_KANA.getValue()}" id="${AttributeConst.PATDEV_PAT_NAME_KANA.getValue()}" value="${patientDevice.patient_name_kana}" />
<br /><br />

<label for="${AttributeConst.PATDEV_APP_NUM.getValue()}">デバイスの添付文書承認番号</label><br />
<input type="text" name="${AttributeConst.PATDEV_APP_NUM.getValue()}" id="${AttributeConst.PATDEV_APP_NUM.getValue()}" value="${patientDevice.approval_number}" />
<br /><br />

<label for="${AttributeConst.PATDEV_DEV_NAME.getValue()}">デバイスの販売名</label><br />
<input type="text" name="${AttributeConst.PATDEV_DEV_NAME.getValue()}" id="${AttributeConst.PATDEV_DEV_NAME.getValue()}" value="${patientDevice.device_name}" />
<br /><br />

<label for="${AttributeConst.PATDEV_IMP_DATE.getValue()}">デバイスの埋込日</label><br />
<input type="date" name="${AttributeConst.PATDEV_IMP_DATE.getValue()}" id="${AttributeConst.PATDEV_IMP_DATE.getValue()}" value="${patientDevice.implantedAt}" />
<br /><br />

<input type="hidden" name="${AttributeConst.PATDEV_ID.getValue()}" value="${patientDevice.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />



