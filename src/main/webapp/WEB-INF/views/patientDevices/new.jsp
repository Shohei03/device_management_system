<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actPatDev" value="${ForwardConst.ACT_PATDEV.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commCrt" value="${ForwardConst.CMD_CREATE.getValue()}" />
<c:set var="commCSVImp" value="${ForwardConst.CMD_CSV_IMPORT.getValue()}" />
<c:set var="commCheck" value="${ForwardConst.CMD_CHECK.getValue()}" />



<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <h2>体内デバイス 新規登録</h2>

        <form enctype="multipart/form-data" method="POST" action="<c:url value='?action=${actPatDev}&command=${commCSVImp}' />" >
            <input name="csv" type="file" required/>
            <input type="submit" value="csvファイル読み込み" />
        </form>
        <br /><br />

        <form method="POST" action="<c:url value='?action=${actPatDev}&command=${commCheck}' />">
            <c:import url="_form.jsp" />
            <button type="submit">確認</button>
        </form>

        <p><a href="<c:url value='?action=${actPatDev}&command=${commIdx}' />">体内デバイス一覧に戻る</a></p>
    </c:param>
</c:import>