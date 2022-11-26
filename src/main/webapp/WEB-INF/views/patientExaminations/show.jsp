<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>

<c:set var="actPatExam" value="${ForwardConst.ACT_PATEXAM.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdt" value="${ForwardConst.CMD_EDIT.getValue()}" />


<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <h2>検査情報 詳細ページ</h2>

        <table>
            <tbody>
                <tr>
                    <th>検査項目</th>
                    <td><c:out value="${patientExamination.examinationItem}" /></td>
                </tr>
                <tr>
                    <th>検査日</th>
                    <fmt:parseDate value="${patientExamination.examinationDate}" pattern="yyyy-MM-dd" var="examDay" type="date" />
                    <td><fmt:formatDate value="${examDay}" pattern="yyyy-MM-dd" /></td>
                </tr>
                <tr>
                    <th>予約時間</th>
                    <fmt:parseDate value="${patientExamination.reservationTime}" pattern="HH:mm" var="reserveTime" type="time" />
                    <td><fmt:formatDate value="${reserveTime}" pattern="HH:mm" /></td>
                </tr>
                <tr>
                    <th>患者ID</th>
                    <td><c:out value="${patientExamination.patientId}" /></td>
                </tr>
                <tr>
                    <th>患者名</th>
                    <td><c:out value="${patientExamination.patientName}" /></td>
                </tr>
                <tr>
                    <th>患者名（ひらがな）</th>
                    <th><c:out value="${patientExamination.patientNameKana}" /></th>
                </tr>
                <tr>
                    <th>登録日</th>
                    <fmt:parseDate value="${patientExamination.createdAt}" pattern="yyyy-MM-dd" var="createDay" type="date" />
                    <td><fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd" /></td>
                </tr>
            </tbody>
        </table>
        <p>
            <a href="<c:url value='?action=${actPatExam}&command=${commEdt}&id=${patientExamination.id}' />">編集する</a>
        </p>

        <p>
            <a href="<c:url value='?action=${actPatExam}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>

