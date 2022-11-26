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
                        <button type="submit">重複登録</button><br /><br />
                        ※他にエラーがある場合は重複登録を押しても反映されません。
                    </c:if>
        </c:forEach>
    </div>
</c:if>


<label for="${AttributeConst.PATDEV_PAT_ID.getValue()}">患者ID</label><br />
<input type="text" name="${AttributeConst.PATDEV_PAT_ID.getValue()}" id="${AttributeConst.PATDEV_PAT_ID.getValue()}" value="${patientDevice.patientId}" />
<br /><br />

<label for="${AttributeConst.PATDEV_PAT_NAME.getValue()}">患者名</label><br />
<input type="text" name="${AttributeConst.PATDEV_PAT_NAME.getValue()}" id="${AttributeConst.PATDEV_PAT_NAME.getValue()}" value="${patientDevice.patientName}" />
<br /><br />

<label for="${AttributeConst.PATDEV_PAT_NAME_KANA.getValue()}">患者名（ひらがな)</label><br />
<input type="text" name="${AttributeConst.PATDEV_PAT_NAME_KANA.getValue()}" id="${AttributeConst.PATDEV_PAT_NAME_KANA.getValue()}" value="${patientDevice.patientNameKana}" />
<br /><br />

<label for="${AttributeConst.PATDEV_APP_NUM.getValue()}">デバイスの添付文書承認番号</label><br />
<input type="text" name="${AttributeConst.PATDEV_APP_NUM.getValue()}" id="${AttributeConst.PATDEV_APP_NUM.getValue()}" value="${patientDevice.approvalNumber}" />
<br /><br />

<label for="${AttributeConst.PATDEV_DEV_NAME.getValue()}">デバイスの販売名</label><br />
<input type="text" name="${AttributeConst.PATDEV_DEV_NAME.getValue()}" id="${AttributeConst.PATDEV_DEV_NAME.getValue()}" value="${patientDevice.deviceName}" />
<br /><br />

<label for="${AttributeConst.PATDEV_IMP_DATE.getValue()}">デバイスの埋込日</label><br />
<input type="date" name="${AttributeConst.PATDEV_IMP_DATE.getValue()}" id="${AttributeConst.PATDEV_IMP_DATE.getValue()}" value="${patientDevice.implantedAt}" />
<br /><br />

<input type="hidden" name="${AttributeConst.PATDEV_ID.getValue()}" value="${patientDevice.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />



