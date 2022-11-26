<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.MessageConst"%>
<%@ page import="constants.AttributeConst"%>

<c:set var="actPatExam" value="${ForwardConst.ACT_PATEXAM.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commCrt" value="${ForwardConst.CMD_CREATE.getValue()}" />
<c:set var="commCSVImp" value="${ForwardConst.CMD_CSV_IMPORT.getValue()}" />
<c:set var="commCheck" value="${ForwardConst.CMD_CHECK.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <h2>検査情報 新規登録</h2>

        <form enctype="multipart/form-data" method="POST" action="<c:url value='?action=${actPatExam}&command=${commCSVImp}' />" >
            <input name="csv" type="file" required/>
            <input type="submit" value="csvファイル読み込み" />
        </form>
        <br /><br />

        <c:forEach var="error" items="${errors}">
            <c:if test="${error == MessageConst.E_DUPLI_DATA.getMessage()}">
                <c:set var="duplica" value="${MessageConst.E_DUPLI_DATA.getMessage()}" />
            </c:if>
        </c:forEach>

        <c:choose>
            <c:when test="${duplica == MessageConst.E_DUPLI_DATA.getMessage()}">
                <form method="POST" action="<c:url value='?action=${actPatExam}&command=${commCrt}' />">
                    <c:import url="_form.jsp" />
                    <input type="hidden" name="${AttributeConst.PATEXAM_DUPLICATE_CHECK.getValue()}" value="false" />
                    <button type="submit">登録</button>
                </form>
            </c:when>
            <c:otherwise>
                <form method="POST" action="<c:url value='?action=${actPatExam}&command=${commCheck}' />">
                    <c:import url="_form.jsp" />

                    <button type="submit">確認</button>
                </form>
            </c:otherwise>
        </c:choose>


        <p><a href="<c:url value='?action=${actPatExam}&command=${commIdx}' />">検査情報一覧に戻る</a></p>
    </c:param>
</c:import>