<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.MessageConst" %>

<c:set var="actPatDev" value="${ForwardConst.ACT_PATDEV.getValue()}" />
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
                    <c:if test="${error == MessageConst.E_UPLOAD_DATE.getMessage()}">
                         エラーが起きた行：
                        <c:forEach var="dateErrLine" items="${csvDateErrorLine}">
                            <c:out value="${dateErrLine}" />.
                        </c:forEach><br />
                    </c:if>
                </c:forEach>
            </div>
        </c:if>


        <h2>体内デバイス 一覧</h2>
        <div id="searchPatId">
            <form  method="POST" action="<c:url value='?action=${actPatDev}&command=${commSearchByPatId}' />">
                <label for="${AttributeConst.PATDEV_PAT_ID.getValue()}">患者IDで検索</label>
                <input type="text" name="${AttributeConst.PATDEV_PAT_ID.getValue()}" id="${AttributeConst.PATDEV_PAT_ID.getValue()}" />
                <button type="submit">検索</button><br /><br />
            </form>
        </div>

        <p><a href="<c:url value='?action=${actPatDev}&command=${commNew}' />">新規体内デバイス登録</a></p>


        <form enctype="multipart/form-data" method="POST" action="<c:url value='?action=${actPatDev}&command=${commCSVAllImp}' />" >
            <p>複数データCSV読込
                <input name="csv" type="file" required/>→→
                <input type="submit" value="読込" /><br />
            </p>
        </form>


        <table id="patientDeviceList">
            <tbody>
                <tr>
                    <th class="${AttributeConst.PATDEV_PAT_ID.getValue()}">患者ID</th>
                    <th class="${AttributeConst.PATDEV_PAT_NAME.getValue()}">患者名</th>
                    <th class="${AttributeConst.PATDEV_DEV_NAME.getValue()}">デバイスの販売名</th>
                    <th class="${AttributeConst.PATDEV_IMP_DATE.getValue()}">埋込日</th>
                    <th class="patientDeviceAction">操作</th>
                </tr>
                <c:forEach var="patientDevice" items="${patientDevices}" varStatus="status">
                    <fmt:parseDate value="${patientDevice.implantedAt}" pattern="yyyy-MM-dd" var="implantedDay" type="date" />

                    <tr class="row${status.count % 2}">
                        <td class="${AttributeConst.PATDEV_PAT_ID.getValue()}"><c:out value="${patientDevice.patientId}" /></td>
                        <td class="${AttributeConst.PATDEV_PAT_NAME.getValue()}"><c:out value="${patientDevice.patientName}" /></td>
                        <td class="${AttributeConst.PATDEV_DEV_NAME.getValue()}"><c:out value="${patientDevice.deviceName}" /></td>
                        <td class="${AttributeConst.PATDEV_IMP_DATE.getValue()}"><fmt:formatDate value='${implantedDay}' pattern='yyyy-MM-dd' /></td>
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

        <p><a href="<c:url value='?action=${actPatDev}&command=${commIdx}' />">一覧に戻る</a></p>
    </c:param>
</c:import>