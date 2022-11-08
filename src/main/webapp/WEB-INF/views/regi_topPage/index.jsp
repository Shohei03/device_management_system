<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>データ登録ページ</h2>

        <div class="regi_top_item">
            <a>患者のデバイス情報</a>
        </div>
        <div class="regi_top_item">
            <a>添付文書情報</a>
        </div>
        <div class="regi_top_item">
            <a>検査情報</a>
        </div>



    </c:param>
</c:import>