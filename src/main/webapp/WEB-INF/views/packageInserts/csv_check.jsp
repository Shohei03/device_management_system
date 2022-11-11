<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

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
                <c:out value="${pack_err}" />
            </c:forEach>
            </div>
        </c:if>


        <h2>取り込みデータ一覧</h2>
        <table id="csv_list">
            <tbody>
                <tr>
                    <th class="csvPack_approval_number">医療機器承認番号</th>
                    <th class="csvPack_JMDN_code">JMDNコード</th>
                    <th class="packgeInsert_general_name">一般的名称</th>
                    <th class="packgeInsert_device_name">デバイスの販売名</th>
                    <th class="acceptability_of_X_ray_exam">X線検査</th>
                    <th class="acceptability_of_CT_exam">CT検査</th>
                    <th class="acceptability_of_TV_exam">TV検査</th>
                    <th class="acceptability_of_Manma_exam">乳腺X線検査</th>
                    <th class="acceptability_of_MR_exam">MRI検査</th>
                    <th class="packgeInsert_delete">取り込まない</th>
                </tr>
                <c:forEach var="packageInsert" items="${packageInsertList}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="csvPack_approval_number"><c:out value="${packageInsert.approval_number}" /></td>
                        <td class="csvPack_JMDN_code"><c:out value="${packageInsert.JMDN_code}" /></td>
                        <td class="packgeInsert_general_name"><c:out value="${packageInsert.general_name}" /></td>
                        <td class="packgeInsert_device_name"><c:out value="${packageInsert.device_name}" /></td>
                        <td class="acceptability_of_X_ray_exam"><c:out value="${packageInsert.acceptability_of_X_ray_exam}" /></td>
                        <td class="acceptability_of_CT_exam"><c:out value="${packageInsert.acceptability_of_CT_exam}" /></td>
                        <td class="acceptability_of_TV_exam"><c:out value="${packageInsert.acceptability_of_TV_exam}" /></td>
                        <td class="acceptability_of_Manma_exam"><c:out value="${packageInsert.acceptability_of_Manma_exam}" /></td>
                        <td class="acceptability_of_MR_exam"><c:out value="${packageInsert.acceptability_of_MR_exam}" /></td>
                        <td class="packgeInsert_delete"><a href="<c:url value='?action=${actPack}&command=${commCSVMODI}&index_num=${status.index}' />">取消</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <p><a href="<c:url value='?action=${actPack}&command=${commCSVAllcreate}' />">添付文書をまとめて登録</a></p>
        <p><a href="<c:url value='?action=${actPack}&command=${commIdx}' />">一覧に戻る</a></p>


    </c:param>
</c:import>