<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actPack" value="${ForwardConst.ACT_PACK.getValue()}" />
<c:set var="actSearch" value="${ForwardConst.ACT_SEARCH.getValue()}" />
<c:set var="actRegi_top" value="${ForwardConst.ACT_REGI_TOP.getValue()}" />
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

        <h2>添付文書一覧</h2>

        <form enctype="multipart/form-data" method="POST" action="<c:url value='?action=${actPack}&command=${commCSVAllImp}' />" >
            <p>複数データCSV読込
                <input name="csv" type="file" required/>→→
                <input type="submit" value="読込" /><br />
            </p>
        </form>



        <table id="package_insert_list">
            <tbody>
                <tr>
                    <th class="packgeInsert_general_name">一般的名称</th>
                    <th class="packgeInsert_device_name">デバイス名</th>
                    <th class="packgeInsert_action">詳細</th>
                </tr>
                <c:forEach var="packageInsert" items="${packageInserts}" varStatus="status">

                    <tr class="row${status.count % 2}">
                        <td class="packgeInsert_general_name"><c:out value="${packageInsert.general_name}" /></td>
                        <td class="packgeInsert_device_name"><c:out value="${packageInsert.device_name}" /></td>
                        <td class="packgeInsert_action"><a href="<c:url value='?action=${actPack}&command=${commShow}&id=${packageInsert.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${packageInsertsCount} 件）<br />
            <c:forEach var="i" begin="1" end="${((packageInserts_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actPack}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

        <p><a href="<c:url value='?action=${actPack}&command=${commNew}' />">添付文書の登録</a></p>
        <p><a href="<c:url value='?action=${actSearch}&command=${commIdx}' />">検索画面に移動</a></p>
        <p><a href="<c:url value='?action=${actRegi_top}&command=${commIdx}' />">登録トップ画面に移動</a></p>

    </c:param>
</c:import>
