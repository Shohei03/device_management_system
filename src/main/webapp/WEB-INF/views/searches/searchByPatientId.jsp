<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actSearcher" value="${ForwardConst.ACT_SEARCHER.getValue()}" />
<c:set var="commSearchByPatId" value="${ForwardConst.CMD_SEARCH_BY_PAT_ID.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commSearchByDepartment" value="${ForwardConst.CMD_SEARCH_BY_DEPARTMENT.getValue()}" />



<c:import url="../layout/app.jsp">
    <c:param name="content">
        <h2>体内デバイス一覧 検索画面</h2>
        <div class="searchPatient">
            <div class="searchPatientId">患者ID：<c:out value="${patient.patientId}" /></div>
            <div class="searchPatientName">患者名：<ruby><c:out value="${patient.patientName}" /><rt>${patient.patientNameKana}</rt></ruby></div>
        </div>
        <table id="searchListByPatient">
            <tbody>
                <tr>
                    <th class="${AttributeConst.SEARCH_GENERAL_NAME.getValue()}" rowspan="2">一般的名称</th>
                    <th class="${AttributeConst.SEARCH_DEV_NAME.getValue()}" rowspan="2">デバイスの販売名</th>
                    <th class="${AttributeConst.SEARCH_IMP_DATE.getValue()}" rowspan="2">埋込日</th>
                    <th class="searchAcceptabilityOfExam" colspan="5">検査の可否</th>
                </tr>
                <tr>
                    <th class="${AttributeConst.PACK_X_RAY.getValue()}">単純X線</th>
                    <th class="${AttributeConst.PACK_CT.getValue()}">CT</th>
                    <th class="${AttributeConst.PACK_TV.getValue()}">X線TV</th>
                    <th class="${AttributeConst.PACK_Manma.getValue()}">乳腺X線</th>
                    <th class="${AttributeConst.PACK_MRI.getValue()}">MRI</th>
                </tr>
                <c:forEach var="search" items="${searchDevices}" varStatus="status">
                    <fmt:parseDate value="${search.implantedAt}" pattern="yyyy-MM-dd" var="implantDate" type="date" />
                    <tr class="row${status.count % 2}">
                        <td class="${AttributeConst.SEARCH_GENERAL_NAME.getValue()}"><c:out value="${search.generalName}" /></td>
                        <td class="${AttributeConst.SEARCH_DEV_NAME.getValue()}"><c:out value="${search.deviceName}" /></td>
                        <td class="${AttributeConst.SEARCH_IMP_DATE.getValue()}"><fmt:formatDate value="${implantDate}" pattern="yyyy-MM-dd" /></td>
                        <td class="${AttributeConst.PACK_X_RAY.getValue()}"><c:out value="${search.acceptabilityOfXrayExam}" /></td>
                        <td class="${AttributeConst.PACK_CT.getValue()}"><c:out value="${search.acceptabilityOfCtExam}" /></td>
                        <td class="${AttributeConst.PACK_TV.getValue()}"><c:out value="${search.acceptabilityOfTvExam}" /></td>
                        <td class="${AttributeConst.PACK_Manma.getValue()}"><c:out value="${search.acceptabilityOfManmaExam}" /></td>
                        <td class="${AttributeConst.PACK_MRI.getValue()}"><c:out value="${search.acceptabilityOfMrExam}" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <p>
            <a href="<c:url value='?action=${actSearcher}&command=${commSearchByDepartment}' />">一覧に戻る</a>
        </p>

    </c:param>
</c:import>