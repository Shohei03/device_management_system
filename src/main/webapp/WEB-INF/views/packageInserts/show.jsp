<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>

<c:set var="actPack" value="${ForwardConst.ACT_PACK.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdt" value="${ForwardConst.CMD_EDIT.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <h2>デバイスの添付文書詳細</h2>

        <table>
            <tbody>
                <tr>
                    <th>添付文書番号</th>
                    <td><c:out value="${packageInsert.approval_number}" /></td>
                </tr>
                <tr>
                    <th>JMDNコード</th>
                    <td><c:out value="${packageInsert.JMDN_code}" /></td>
                </tr>
                <tr>
                    <th>一般的名称</th>
                    <td><c:out value="${packageInsert.general_name}" /></td>
                </tr>
                <tr>
                    <th>デバイス名（販売名）</th>
                    <td><c:out value="${packageInsert.device_name}" /></td>
                </tr>
                <tr>
                    <th>X線検査の可否</th>
                    <td><c:out value="${packageInsert.acceptability_of_X_ray_exam}" /></td>
                </tr>
                <tr>
                    <th>CT検査の可否</th>
                    <td><c:out value="${packageInsert.acceptability_of_CT_exam}" /></td>
                </tr>
                <tr>
                    <th>TV検査の可否</th>
                    <td><c:out value="${packageInsert.acceptability_of_TV_exam}" /></td>
                </tr>
                <tr>
                    <th>乳腺X線検査の可否</th>
                    <td><c:out value="${packageInsert.acceptability_of_Manma_exam}" /></td>
                </tr>
                <tr>
                    <th>MRI検査の可否</th>
                    <td><c:out value="${packageInsert.acceptability_of_MR_exam}" /></td>
                </tr>
                <tr>
                    <th>登録日</th>
                    <fmt:parseDate value="${packageInsert.createdAt}" pattern="yyyy-MM-dd" var="createDay" type="date" />
                    <td><fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd" /></td>
                </tr>
            </tbody>
        </table>
        <p>
            <a href="<c:url value='?action=${actPack}&command=${commEdt}&id=${packageInsert.id}' />">この添付文書を編集する</a>
        </p>

        <p>
            <a href="<c:url value='?action=${actPack}&command=${commIdx}' />">一覧に戻る</a>
        </p>

    </c:param>
</c:import>