<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="actAuth" value="${ForwardConst.ACT_AUTH.getValue()}" />
<c:set var="actSearcher" value="${ForwardConst.ACT_SEARCHER.getValue()}" />
<c:set var="actRegi" value="${ForwardConst.ACT_REGI_TOP.getValue()}" />

<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commOut" value="${ForwardConst.CMD_LOGOUT.getValue()}" />
<c:set var="commSearchByDep" value="${ForwardConst.CMD_SEARCH_BY_DEPARTMENT.getValue()}" />


<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>体内デバイス管理システム</title>
        <link rel="stylesheet" href="<c:url value='/css/reset.css' />">
        <link rel="stylesheet" href="<c:url value='/css/style.css' />">
    </head>
    <body>
        <div id="wrapper">
            <div id="header">
                <div id="headerMenu">
                    <h1><a href="<c:url value='/?action=${actSearch}&command=${commIdx}' />">デバイス管理システム</a></h1>&nbsp;&nbsp;&nbsp;
                    <c:if test="${sessionScope.loginEmployee != null}">
                        <c:if test="${sessionScope.loginEmployee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}">
                            <a href="<c:url value='?action=${actEmp}&command=${commIdx}' />">従業員管理</a>&nbsp;
                        </c:if>
                        <c:if test="${sessionScope.loginEmployee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue() or sessionScope.loginEmployee.adminFlag == AttributeConst.ROLE_PARTIAL_ADMIN.getIntegerValue()}">
                            <a href="<c:url value='?action=${actRegi}&command=${commIdx}' />">データ登録</a>&nbsp;
                        </c:if>
                        <a href="<c:url value='?action=${actSearcher}&command=${commSearchByDep}' />">デバイス検索</a>&nbsp;
                    </c:if>
                </div>
                <c:if test="${sessionScope.loginEmployee != null}">
                    <div id="employeeName">
                        <c:out value="${sessionScope.loginEmployee.name}" />
                        &nbsp;さん&nbsp;&nbsp;&nbsp;
                        <a href="<c:url value='?action=${actAuth}&command=${commOut}' />">ログアウト</a>
                    </div>
                </c:if>
            </div>
            <div id="content">${param.content}</div>
            <div id="footer">by Shohei Matoba.</div>
        </div>
    </body>
</html>