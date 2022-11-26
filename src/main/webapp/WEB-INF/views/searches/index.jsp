<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="constants.AttributeConst"%>

<c:set var="actSearcher" value="${ForwardConst.ACT_SEARCHER.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>体内デバイス一覧 検索画面</h2>


        <table id="search_list">
            <tbody>
                <tr>
                    <th class="${AttributeConst.SEARCH_PAT_ID.getValue()}">患者ID</th>
                    <th class="search_patient_name">氏名</th>
                    <th class="search_general_name">一般的名称</th>
                    <th class="search_device_name">デバイスの販売名</th>
                    <th class="search_acceptability_of_X_ray_exam">単純X線検査</th>
                    <th class="search_acceptability_of_CT_ray_exam">CT検査</th>
                    <th class="search_acceptability_of_TV_ray_exam">X線TV検査</th>
                    <th class="search_acceptability_of_MR_ray_exam">MR検査</th>
                    <th class="search_acceptability_of_Manma_ray_exam">乳腺X線検査</th>
                </tr>
                <c:forEach var="patientDevice" items="${patDevPacks}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="search_pat_id"><c:out value="${patientDevice.patientId}" /></td>
                        <td class="search_pat_name"><c:out value="${patientDevice.patientName}" /></td>
                        <td class="search_general_name"><c:out value="${patientDevice.generalName}" /></td>
                        <td class="search_device_name"><c:out value="${patientDevice.deviceName}" /></td>
                        <td class="search_acceptability_of_X_ray_exam">${patientDevice.acceptabilityOfXrayExam}</td>
                        <td class="search_acceptability_of_CT_exam">${patientDevice.acceptabilityOfCtExam}</td>
                        <td class="search_acceptability_of_TV_exam">${patientDevice.acceptabilityOfTvExam}</td>
                        <td class="search_acceptability_of_MR_exam">${patientDevice.acceptabilityOfMrExam}</td>
                        <td class="search_acceptability_of_Manma_exam">${patientDevice.acceptabilityOfManmaExam}</td>
                    </tr>

                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${patientDevicesCount} 件） <br />
            <c:forEach var="i" begin="1" end="${((patientDevicesCount -1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actSearcher}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

    </c:param>
</c:import>