<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actPack" value="${ForwardConst.ACT_PACK.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commCSVMODI" value="${ForwardConst.CMD_CSV_MODIFY.getValue()}" />
<c:set var="commCSVAllcreate" value="${ForwardConst.CMD_CSV_ALL_CREATE.getValue()}" />



<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${errors != null}">
            <div id="flush_error">
                登録内容にエラーがあります。<br />
            <c:forEach var="error" items="${errors}">
                ・<c:out value="${error}" /><br />
                <c:out value="${packErr}" />
            </c:forEach>
            </div>
        </c:if>


        <h2>取り込みデータ一覧</h2>
        <table id="csvList">
            <tbody>
                <tr>
                    <th class="${AttributeConst.PACK_APP_NUM.getValue()}">承認番号</th>
                    <th class="${AttributeConst.PACK_JMDN.getValue()}">JMDNコード</th>
                    <th class="${AttributeConst.PACK_GENERAL_NAME.getValue()}">一般的名称</th>
                    <th class="${AttributeConst.PACK_DEV_NAME.getValue()}">デバイスの販売名</th>
                    <th class="${AttributeConst.PACK_X_RAY.getValue()}">X線検査</th>
                    <th class="${AttributeConst.PACK_CT.getValue()}">CT検査</th>
                    <th class="${AttributeConst.PACK_TV.getValue()}">TV検査</th>
                    <th class="${AttributeConst.PACK_Manma.getValue()}">乳腺X線検査</th>
                    <th class="${AttributeConst.PACK_MRI.getValue()}">MRI検査</th>
                    <th class="packgeInsert_delete">取り込まない</th>
                </tr>
                <c:forEach var="packageInsert" items="${packageInsertList}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="${AttributeConst.PACK_APP_NUM.getValue()}"><c:out value="${packageInsert.approvalNumber}" /></td>
                        <td class="${AttributeConst.PACK_JMDN.getValue()}"><c:out value="${packageInsert.jmdnCode}" /></td>
                        <td class="${AttributeConst.PACK_GENERAL_NAME.getValue()}"><c:out value="${packageInsert.generalName}" /></td>
                        <td class="${AttributeConst.PACK_DEV_NAME.getValue()}"><c:out value="${packageInsert.deviceName}" /></td>
                        <td class="${AttributeConst.PACK_X_RAY.getValue()}"><c:out value="${packageInsert.acceptabilityOfXrayExam}" /></td>
                        <td class="${AttributeConst.PACK_CT.getValue()}"><c:out value="${packageInsert.acceptabilityOfCtExam}" /></td>
                        <td class="${AttributeConst.PACK_TV.getValue()}m"><c:out value="${packageInsert.acceptabilityOfTvExam}" /></td>
                        <td class="${AttributeConst.PACK_Manma.getValue()}"><c:out value="${packageInsert.acceptabilityOfManmaExam}" /></td>
                        <td class="${AttributeConst.PACK_MRI.getValue()}"><c:out value="${packageInsert.acceptabilityOfMrExam}" /></td>
                        <td class="packgeInsert_delete"><a href="<c:url value='?action=${actPack}&command=${commCSVMODI}&index_num=${status.index}' />">取消</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <p><a href="<c:url value='?action=${actPack}&command=${commCSVAllcreate}' />">添付文書をまとめて登録</a></p>
        <p><a href="<c:url value='?action=${actPack}&command=${commIdx}' />">一覧に戻る</a></p>


    </c:param>
</c:import>