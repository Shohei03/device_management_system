<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actPatDev" value="${ForwardConst.ACT_PATDEV.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commCSVAllImp" value="${ForwardConst.CMD_CSV_ALL_IMPORT.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>体内デバイス 一覧</h2>

        <form enctype="multipart/form-data" method="POST" action="<c:url value='?action=${actPatDev}&command=${commCSVAllImp}' />" >
            <p>複数データCSV読込
                <input name="csv" type="file" required/>→→
                <input type="submit" value="読込" /><br />
            </p>
        </form>


        <table id="patientDevice_list">
            <tbody>
                <tr>
                    <th class="patientDevice_patient_id">患者ID</th>
                    <th class="patientDevie_patient_name">患者名</th>
                    <th class="patientDeviec_deviceName">デバイスの販売名</th>
                    <th class="patientDevice_implantedAt">埋込日</th>
                    <th class="patientDeviec_action">操作</th>
                </tr>
                <c:forEach var="patientDevice" items="${patientDevices}" varStatus="status">
                    <fmt:parseDate value="${patientDevice.implantedAt}" pattern="yyyy-MM-dd" var="implantedDay" type="date" />

                    <tr class="row${status.count % 2}">
                        <td class="patientDevice_patient_id"><c:out value="${patientDevice.patient_id}" /></td>
                        <td class="patientDevie_patient_name"><c:out value="${patientDevice.patient_name}" /></td>
                        <td class="patientDeviec_deviceName"><c:out value="${patientDevice.device_name}" /></td>
                        <td class="patientDevice_implantedAt"><fmt:formatDate value='${implantedDay}' pattern='yyyy-MM-dd' /></td>
                        <td class="patientDeviec_action">
                            <c:choose>
                                <c:when test="${patientDevice.deleteFlag == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()}">
                                    （削除済み）
                                </c:when>

                                <c:otherwise>
                                    <a href="<c:url value='?action=${actPatDev}&command=${commShow}&id=${patientDevice.id}' />">詳細を見る</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
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
                        <a href="<c:url value='?action=${actPatDev}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='?action=${actPatDev}&command=${commNew}' />">新規体内デバイス登録</a></p>
    </c:param>
</c:import>