<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.MessageConst" %>

<c:set var="actPatExam" value="${ForwardConst.ACT_PATEXAM.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commCSVAllImp" value="${ForwardConst.CMD_CSV_ALL_IMPORT.getValue()}" />
<c:set var="commSearchByPatId" value="${ForwardConst.CMD_SEARCH_BY_PAT_ID.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <c:if test="${errors != null}">
            <div id="flush_error">
                CSV取り込み時にエラーが生じました。<br />
                <c:forEach var="error" items="${errors}">
                    ・<c:out value="${error}" /><br />
                    <c:if test="${error == MessageConst.E_UPLOAD_DATA_NUM.getMessage()}">
                         エラーが起きた行：
                        <c:forEach var="line" items="${csvErrorLine}">
                            <c:out value="${line}" />.
                        </c:forEach><br />
                    </c:if>
                    <c:if test="${error == MessageConst.E_UPLOAD_DATE.getMessage() || error == MessageConst.E_UPLOAD_TIME.getMessage()
                    || error == MessageConst.E_RESERVATION_TIME.getMessage()}">
                         日時データのエラーが起きた行：
                        <c:forEach var="dateErrLine" items="${csvDateErrorLine}">
                            <c:out value="${dateErrLine}" />.
                        </c:forEach><br />
                    </c:if>
                </c:forEach>
            </div>
        </c:if>


        <h2>検査情報 一覧</h2>
        <div id="searchPatId">
            <form  method="POST" action="<c:url value='?action=${actPatExam}&command=${commSearchByPatId}' />">
                <label for="${AttributeConst.PATEXAM_PAT_ID.getValue()}">患者IDで検索</label><br />
                <input type="text" name="${AttributeConst.PATEXAM_PAT_ID.getValue()}" id="${AttributeConst.PATEXAM_PAT_ID.getValue()}" />
                <button type="submit">検索</button><br /><br />
            </form>
        </div>

        <p><a href="<c:url value='?action=${actPatExam}&command=${commNew}' />">患者の新規検査情報を登録</a></p>

        <form enctype="multipart/form-data" method="POST" action="<c:url value='?action=${actPatExam}&command=${commCSVAllImp}' />" >
            <p>複数データCSV読込
                <input name="csv" type="file" required/>→→
                <input type="submit" value="読込" /><br />
            </p>
        </form>

        <table id="patientExamList">
            <tbody>
                <tr>
                    <th class="${AttributeConst.PATEXAM_EXAM_ITEM.getValue()}">検査項目</th>
                    <th class="${AttributeConst.PATEXAM_EXAM_DATE.getValue()}">検査日</th>
                    <th class="${AttributeConst.PATEXAM_RESERVATION_TIME.getValue()}">予約時間</th>
                    <th class="${AttributeConst.PATEXAM_PAT_ID.getValue()}">患者ID</th>
                    <th class="${AttributeConst.PATEXAM_PAT_NAME.getValue()}">患者名</th>
                    <th class="patientExamAction">操作</th>
                </tr>
                <c:forEach var="patientExamination" items="${patientExaminations}" varStatus="status">
                    <fmt:parseDate value="${patientExamination.reservationTime}" pattern='HH:mm' var='reservationTime' type='time' />
                    <fmt:parseDate value="${patientExamination.examinationDate}" pattern='yyyy-MM-dd' var='examDay' type='date' />

                    <tr class="row${status.count % 2}">
                        <td class="${AttributeConst.PATEXAM_EXAM_ITEM.getValue()}"><c:out value="${patientExamination.examinationItem}" /></td>
                        <td class="${AttributeConst.PATEXAM_EXAM_DATE.getValue()}"><fmt:formatDate value="${examDay}" pattern="yyyy-MM-dd" /></td>
                        <td class="${AttributeConst.PATEXAM_RESERVATION_TIME.getValue()}"><fmt:formatDate value='${reservationTime}' pattern='HH:mm' /></td>
                        <td class="${AttributeConst.PATEXAM_PAT_ID.getValue()}"><c:out value="${patientExamination.patientId}" /></td>
                        <td class="${AttributeConst.PATEXAM_PAT_NAME.getValue()}"><c:out value="${patientExamination.patientName}" /></td>
                        <td class="patientExamAction"><a href="<c:url value='?action=${actPatExam}&command=${commShow}&id=${patientExamination.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>


<div id="pagination">
            （全 ${patientExaminationsCountByDay} 件） <br />
            <c:forEach var="i" begin="1" end="${((patientExaminationsCountByDay -1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actPatExam}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

        <p><a href="<c:url value='?action=${actPatExam}&command=${commIdx}' />">一覧に戻る</a></p>
    </c:param>
</c:import>
