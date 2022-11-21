<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.MessageConst"%>

<c:set var="actPatExam" value="${ForwardConst.ACT_PATEXAM.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commUpd" value="${ForwardConst.CMD_UPDATE.getValue()}" />
<c:set var="commDel" value="${ForwardConst.CMD_DESTROY.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <c:forEach var="error" items="${errors}">
            <c:if test="${error == MessageConst.E_DUPLI_DATA.getMessage()}">
                <c:set var="duplicate" value="${MessageConst.E_DUPLI_DATA.getMessage()}" />
            </c:if>
        </c:forEach>

        <h2>検査情報 編集ページ</h2>

        <form method="POST" action="<c:url value='?action=${actPatExam}&command=${commUpd}' />">
            <c:import url="_form.jsp" />

            <c:if test="${duplicate == MessageConst.E_DUPLI_DATA.getMessage()}">
                <input type="hidden" name="${AttributeConst.PATEXAM_DUPLICATE_CHECK.getValue()}" value="false" />
            </c:if>
            <button type="submit">登録</button>
        </form>

        <p>
            <a href="#" onclick="confirmDestroy();">この検査情報を削除する</a>
        </p>
        <form method="POST" action="<c:url value='?action=${actPatExam}&command=${commDel}' />">
            <input type="hidden" name="${AttributeConst.PATEXAM_ID.getValue()}" value="${patientExamination.id}" />
            <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
        </form>

        <script>
            function confirmDestroy() {
                if (confirm("本当に削除してよろしいですか？")) {
                    document.forms[1].submit();
                }
            }
        </script>

        <p>
            <a href="<c:url value='?action=${actPatExam}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>