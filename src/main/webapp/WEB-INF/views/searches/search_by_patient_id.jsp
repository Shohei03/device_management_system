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
        <div class="search_patient">
            <div class="search_patient_id">患者ID：<c:out value="${patient.patient_id}" /></div>
            <div class="search_patient_name">患者名：<ruby><c:out value="${patient.patient_name}" /><rt>${patient.patient_name_kana}</rt></ruby></div>
        </div>
        <table id="search_list_by_patient">
            <tbody>
                <tr>
                    <th class="search_general_name" rowspan="2">一般的名称</th>
                    <th class="search_device_name" rowspan="2">デバイスの販売名</th>
                    <th class="search_implantedAt" rowspan="2">埋込日</th>
                    <th class="search_acceptability_of_exam" colspan="5">検査の可否</th>
                </tr>
                <tr>
                    <th class="search_acceptability_of_X_ray_exam">単純X線</th>
                    <th class="search_acceptability_of_CT_exam">CT</th>
                    <th class="search_acceptability_of_TV_exam">X線TV</th>
                    <th class="search_acceptability_of_Manma_exam">乳腺X線</th>
                    <th class="search_acceptability_of_MR_exam">MRI</th>
                </tr>
                <c:forEach var="search" items="${search_devices}" varStatus="status">
                    <fmt:parseDate value="${search.implantedAt}" pattern="yyyy-MM-dd" var="implantDate" type="date" />
                    <tr class="row${status.count % 2}">
                        <td class="search_general_name"><c:out value="${search.general_name}" /></td>
                        <td class="search_device_name"><c:out value="${search.device_name}" /></td>
                        <td class="search_implantedAt"><fmt:formatDate value="${implantDate}" pattern="yyyy-MM-dd" /></td>
                        <td class="search_acceptability_of_X_ray_exam"><c:out value="${search.acceptability_of_X_ray_exam}" /></td>
                        <td class="search_acceptability_of_CT_exam"><c:out value="${search.acceptability_of_CT_exam}" /></td>
                        <td class="search_acceptability_of_TV_exam"><c:out value="${search.acceptability_of_TV_exam}" /></td>
                        <td class="search_acceptability_of_Manma_exam"><c:out value="${search.acceptability_of_Manma_exam}" /></td>
                        <td class="search_acceptability_of_MR_exam"><c:out value="${search.acceptability_of_MR_exam}" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <p>
            <a href="<c:url value='?action=${actSearcher}&command=${commSearchByDepartment}' />">一覧に戻る</a>
        </p>

    </c:param>
</c:import>