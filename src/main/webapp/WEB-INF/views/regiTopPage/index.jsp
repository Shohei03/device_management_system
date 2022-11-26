<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actPack" value="${ForwardConst.ACT_PACK.getValue()}" />
<c:set var="actPatDev" value="${ForwardConst.ACT_PATDEV.getValue()}" />
<c:set var="actPatExam" value="${ForwardConst.ACT_PATEXAM.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>データ登録ページ</h2>

        <div class="regiTopItem">
            <a href="<c:url value='?action=${actPatDev}&command=${commIdx}' />">患者のデバイス情報</a>
        </div>
        <div class="regiTopItem">
            <a href="<c:url value='?action=${actPack}&command=${commIdx}' />">添付文書情報</a>
        </div>
        <div class="regiTopItem">
            <a href="<c:url value='?action=${actPatExam}&command=${commIdx}' />">検査情報</a>
        </div>



    </c:param>
</c:import>