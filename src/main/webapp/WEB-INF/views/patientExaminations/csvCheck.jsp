<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst"%>
<%@ page import="constants.MessageConst"%>

<c:set var="actPatExam" value="${ForwardConst.ACT_PATEXAM.getValue()}" />
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
                <c:out value="${errPatientName}" /><br />
                <c:if test="${error == MessageConst.E_DUPLI_DATA.getMessage()}">
                    <c:set var="duplica" value="${MessageConst.E_DUPLI_DATA.getMessage()}" />
                </c:if>
            </c:forEach>
            <c:if test="${duplica == MessageConst.E_DUPLI_DATA.getMessage()}">
                <form method="POST" action="<c:url value='?action=${actPatExam}&command=${commCSVAllcreate}' />" >
                    <input type="hidden" name="${AttributeConst.PATEXAM_DUPLICATE_CHECK.getValue()}" value="false" />
                    <button type="submit">重複登録</button><br /><br />
                    ※他にエラーがある場合は重複登録を押しても反映されません。
                </form>
            </c:if>
            </div>
        </c:if>

        <h2>取り込みデータ一覧</h2>
        <table id="patExam_csvList">
            <tbody>
                <tr>
                    <th class="${AttributeConst.PATEXAM_EXAM_ITEM.getValue()}">検査項目</th>
                    <th class="${AttributeConst.PATEXAM_EXAM_DATE.getValue()}">検査日</th>
                    <th class="${AttributeConst.PATEXAM_RESERVATION_TIME.getValue()}">予約時間</th>
                    <th class="${AttributeConst.PATEXAM_PAT_ID.getValue()}">患者ID</th>
                    <th class="${AttributeConst.PATEXAM_PAT_NAME.getValue()}">患者名</th>
                    <th class="patientDeviecAction">取り込まない</th>
                </tr>

                <c:forEach var="patientExamination" items="${sessionScope.patientExaminations}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <fmt:parseDate value="${patientExamination.reservationTime}" pattern='HH:mm' var='reservationTime' type='time' />
                        <fmt:parseDate value="${patientExamination.examinationDate}" pattern='yyyy-MM-dd' var='examDay' type='date' />

                        <td class="${AttributeConst.PATEXAM_EXAM_ITEM.getValue()}"><c:out value="${patientExamination.examinationItem}" /></td>
                        <td class="${AttributeConst.PATEXAM_EXAM_DATE.getValue()}"><fmt:formatDate value="${examDay}" pattern="yyyy-MM-dd" /></td>
                        <td class="${AttributeConst.PATEXAM_RESERVATION_TIME.getValue()}"><fmt:formatDate value='${reservationTime}' pattern='HH:mm' /></td>
                        <td class="${AttributeConst.PATEXAM_PAT_ID.getValue()}"><c:out value="${patientExamination.patientId}" /></td>
                        <td class="${AttributeConst.PATEXAM_PAT_NAME.getValue()}"><c:out value="${patientExamination.patientName}" /></td>
                        <td class="patientDeviecAction"><a href="<c:url value='?action=${actPatExam}&command=${commCSVMODI}&index_num=${status.index}' />">取消</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <p><a href="<c:url value='?action=${actPatExam}&command=${commCSVAllcreate}' />">上記データをまとめて登録</a></p>
        <p><a href="<c:url value='?action=${actPatExam}&command=${commIdx}' />">一覧に戻る</a></p>
    </c:param>
</c:import>