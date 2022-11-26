<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.MessageConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actPatDev" value="${ForwardConst.ACT_PATDEV.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commCSVMODI" value="${ForwardConst.CMD_CSV_MODIFY.getValue()}" />
<c:set var="commCSVAllcreate" value="${ForwardConst.CMD_CSV_ALL_CREATE.getValue()}" />



<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${errors != null}">
            <div id="flush_error">
                登録内容にエラーがあります。<br />
            <c:forEach var="error" items="${errors}">
                ・<c:out value="${error}" /><br />
                <c:out value="${patDevErrPatName}" />
                <c:if test="${error == MessageConst.E_DUPLI_DATA.getMessage()}">
                    <form method="POST" action="<c:url value='?action=${actPatDev}&command=${commCSVAllcreate}' />" >
                        <input type="hidden" name="${AttributeConst.PATDEV_DUPLICATE_CHECK.getValue()}" value="false" />
                        <button type="submit">重複登録</button>
                    </form>
                </c:if>
            </c:forEach>
            </div>
        </c:if>


        <h2>取り込みデータ一覧</h2>
        <table id="csvList">
            <tbody>
                <tr>
                    <th class="${AttributeConst.PATDEV_PAT_ID.getValue()}">患者ID</th>
                    <th class="${AttributeConst.PATDEV_PAT_NAME.getValue()}">患者名</th>
                    <th class="${AttributeConst.PATDEV_PAT_NAME_KANA.getValue()}">患者名（ひらがな）</th>
                    <th class="${AttributeConst.PATDEV_APP_NUM.getValue()}">添付文書承認番号</th>
                    <th class="${AttributeConst.PATDEV_DEV_NAME.getValue()}">デバイスの販売名</th>
                    <th class="${AttributeConst.PATDEV_IMP_DATE.getValue()}">埋込日</th>
                    <th class="packgeInsertDelete">取り込まない</th>
                </tr>
                <c:forEach var="patientDevice" items="${sessionScope.patientDeviceList}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="${AttributeConst.PATDEV_PAT_ID.getValue()}"><c:out value="${patientDevice.patientId}" /></td>
                        <td class="${AttributeConst.PATDEV_PAT_NAME.getValue()}"><c:out value="${patientDevice.patientName}" /></td>
                        <td class="${AttributeConst.PATDEV_PAT_NAME_KANA.getValue()}"><c:out value="${patientDevice.patientNameKana}" /></td>
                        <td class="${AttributeConst.PATDEV_APP_NUM.getValue()}"><c:out value="${patientDevice.approvalNumber}" /></td>
                        <td class="${AttributeConst.PATDEV_DEV_NAME.getValue()}"><c:out value="${patientDevice.deviceName}" /></td>
                        <td class="${AttributeConst.PATDEV_IMP_DATE.getValue()}"><c:out value="${patientDevice.implantedAt}" /></td>
                        <td class="patDevDelete"><a href="<c:url value='?action=${actPatDev}&command=${commCSVMODI}&indexNum=${status.index}' />">取消</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <p><a href="<c:url value='?action=${actPatDev}&command=${commCSVAllcreate}' />">上記データをまとめて登録</a></p>
        <p><a href="<c:url value='?action=${actPatDev}&command=${commIdx}' />">一覧に戻る</a></p>
    </c:param>
</c:import>