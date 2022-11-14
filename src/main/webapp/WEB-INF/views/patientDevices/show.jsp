<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>

<c:set var="actPatDev" value="${ForwardConst.ACT_PATDEV.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdt" value="${ForwardConst.CMD_EDIT.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <h2>体内デバイス 詳細ページ</h2>

        <table>
            <tbody>
                <tr>
                    <th>患者ID</th>
                    <td><c:out value="${patientDevice.patient_id}" /></td>
                </tr>
                <tr>
                    <th>患者名</th>
                    <td><c:out value="${patientDevice.patient_name}" /></td>
                </tr>
                <tr>
                    <th>添付文書承認番号
                    <td><c:out value="${patientDevice.approval_number}" /></td>
                </tr>
                <tr>
                    <th>デバイスの販売名</th>
                    <th><c:out value="${patientDevice.device_name}" /></th>
                </tr>
                <tr>
                    <th>デバイスの埋込日</th>
                    <fmt:parseDate value="${patientDevice.implantedAt}" pattern="yyyy-MM-dd" var="implantedDay" type="date" />
                    <td><fmt:formatDate value='${implantedDay}' pattern='yyyy-MM-dd' /></td>
                </tr>
                <tr>
                    <th>登録日</th>
                    <fmt:parseDate value="${patientDevice.createdAt}" pattern="yyyy-MM-dd" var="createDay" type="date" />
                    <td><fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd" /></td>
                </tr>
                    <tr>
                    <th>更新日</th>
                    <fmt:parseDate value="${patientDevice.updatedAt}" pattern="yyyy-MM-dd" var="updateDay" type="date" />
                    <td><fmt:formatDate value="${updateDay}" pattern="yyyy-MM-dd" /></td>
                </tr>
            </tbody>
        </table>
        <p>
            <a href="<c:url value='?action=${actPatDev}&command=${commEdt}&id=${patientDevice.id}' />">編集する</a>
        </p>

        <p>
            <a href="<c:url value='?action=${actPatDev}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>