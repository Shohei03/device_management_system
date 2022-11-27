<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.MessageConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actPack" value="${ForwardConst.ACT_PACK.getValue()}" />
<c:set var="actSearcher" value="${ForwardConst.ACT_SEARCHER.getValue()}" />
<c:set var="actRegiTop" value="${ForwardConst.ACT_REGI_TOP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commCSVAllImp" value="${ForwardConst.CMD_CSV_ALL_IMPORT.getValue()}" />
<c:set var="commSearchByAppNum" value="${ForwardConst.CMD_SEARCH_BY_APP_NUM.getValue()}" />


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
                    <c:if test="${error == MessageConst.E_ABNORMAL_ExamAcceptability.getMessage()}">
                         エラーが起きた行：
                        <c:forEach var="accepLine" items="${csvAcceptabilityErrorLine}">
                            <c:out value="${accepLine}" />.
                        </c:forEach><br />
                    </c:if>
                </c:forEach>
            </div>
        </c:if>

        <h2>添付文書一覧</h2>
        <div id="searchAppNum">
            <form  method="POST" action="<c:url value='?action=${actPack}&command=${commSearchByAppNum}' />">
                <label for="${AttributeConst.PACK_APP_NUM.getValue()}">添付文書承認番号で検索</label><br />
                <input type="text" name="${AttributeConst.PACK_APP_NUM.getValue()}" id="${AttributeConst.PACK_APP_NUM.getValue()}" />
                <button type="submit">検索</button><br /><br />
            </form>
        </div>

        <p><a href="<c:url value='?action=${actPack}&command=${commNew}' />">新規添付文書の登録</a></p>

        <form enctype="multipart/form-data" method="POST" action="<c:url value='?action=${actPack}&command=${commCSVAllImp}' />" >
            <p>複数データCSV読込
                <input name="csv" type="file" required/>→→
                <input type="submit" value="読込" /><br />
            </p>
        </form>



        <table id="packageInsertList">
            <tbody>
                <tr>
                    <th class="${AttributeConst.PACK_GENERAL_NAME.getValue()}">一般的名称</th>
                    <th class="${AttributeConst.PACK_DEV_NAME.getValue()}">デバイス名</th>
                    <th class="packgeInsertAction">詳細</th>
                </tr>
                <c:forEach var="packageInsert" items="${packageInserts}" varStatus="status">

                    <tr class="row${status.count % 2}">
                        <td class="${AttributeConst.PACK_GENERAL_NAME.getValue()}"><c:out value="${packageInsert.generalName}" /></td>
                        <td class="${AttributeConst.PACK_DEV_NAME.getValue()}"><c:out value="${packageInsert.deviceName}" /></td>
                        <td class="packgeInsertAction"><a href="<c:url value='?action=${actPack}&command=${commShow}&id=${packageInsert.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${packageInsertsCount} 件）<br />
            <c:forEach var="i" begin="1" end="${((packageInsertsCount - 1) / maxRow) + 1}" step="1">
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
    </c:param>
</c:import>
