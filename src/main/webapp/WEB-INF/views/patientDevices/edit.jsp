<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actPatDev" value="${ForwardConst.ACT_PATDEV.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commUpd" value="${ForwardConst.CMD_UPDATE.getValue()}" />
<c:set var="commDel" value="${ForwardConst.CMD_DESTROY.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <h2>体内デバイス 編集ページ</h2>
        <form method="POST" action="<c:url value='?action=${actPatDev}&command=${commUpd}' />">
            <c:import url="patLockForm.jsp" />
        </form>

        <p>
            <a href="#" onclick="confirmDestroy();">この体内デバイス情報を削除する</a>
        </p>
        <form method="POST" action="<c:url value='?action=${actPatDev}&command=${commDel}' />">
            <input type="hidden" name="${AttributeConst.PATDEV_ID.getValue()}" value="${patientDevice.id}" />
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
            <a href="<c:url value='?action=${actPatDev}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>
