<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>


<c:set var="actPatDev" value="${ForwardConst.ACT_PATDEV.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commCrt" value="${ForwardConst.CMD_CREATE.getValue()}" />
<c:set var="commCSVImp" value="${ForwardConst.CMD_CSV_IMPORT.getValue()}" />
<c:set var="commCheck" value="${ForwardConst.CMD_CHECK.getValue()}" />


<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
       <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>体内デバイス 確認画面</h2>

        <form enctype="multipart/form-data" method="POST" action="<c:url value='?action=${actPatDev}&command=${commCrt}' />" >
            <input name="csv" type="file" required/>
            <input type="submit" value="csvファイル読み込み" />
        </form>
        <br /><br />

        <form method="POST" action="<c:url value='?action=${actPatDev}&command=${commCrt}' />">
            <c:import url="_form.jsp" />
            <button type="submit">登録</button>
        </form>

        <p><a href="<c:url value='?action=${actPatDev}&command=${commIdx}' />">体内デバイス一覧に戻る</a></p>
    </c:param>
</c:import>