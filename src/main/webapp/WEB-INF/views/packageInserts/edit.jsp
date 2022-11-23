<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actPack" value="${ForwardConst.ACT_PACK.getValue()}" />
<c:set var="commUpd" value="${ForwardConst.CMD_UPDATE.getValue()}" />
<c:set var="commDel" value="${ForwardConst.CMD_DESTROY.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <h2>添付文書編集ページ</h2>
        <form method="POST" action="<c:url value='?action=${actPack}&command=${commUpd}' />">
            <c:import url="_form.jsp" />
        </form>

        <p>
            <a href="#" onclick="confirmDestroy();">この添付文書情報を削除する</a>
        </p>
        <form method="POST" action="<c:url value='?action=${actPack}&command=${commDel}' />">
            <input type="hidden" name="${AttributeConst.PACK_ID.getValue()}" value="${packageInsert.id}" />
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

        <p>
            <a href="<c:url value='?action=PackageInsert&command=index' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>