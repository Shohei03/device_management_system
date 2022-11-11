<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actPack" value="${ForwardConst.ACT_PACK.getValue()}" />
<c:set var="commCSVImp" value="${ForwardConst.CMD_CSV_IMPORT.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commCrt" value="${ForwardConst.CMD_CREATE.getValue()}" />



<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <h2>添付文書 新規登録ページ</h2>

        <form enctype="multipart/form-data" method="POST" action="<c:url value='?action=${actPack}&command=${commCSVImp}' />" >
            <input name="csv" type="file" required/>
            <input type="submit" value="csvファイル読み込み" />
        </form>

        <form method="POST" action="<c:url value='?action=${actPack}&command=${commCrt}' />">
            <c:import url="_form.jsp" />
        </form>

        <p><a href="<c:url value='?action=${actPack}&command=${commIdx}' />">一覧に戻る</a></p>
    </c:param>
</c:import>