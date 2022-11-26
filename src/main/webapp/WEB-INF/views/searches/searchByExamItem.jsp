<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actSearcher" value="${ForwardConst.ACT_SEARCHER.getValue()}" />
<c:set var="commSearchByDepartment" value="${ForwardConst.CMD_SEARCH_BY_DEPARTMENT.getValue()}" />
<c:set var="commSearchByPatId" value="${ForwardConst.CMD_SEARCH_BY_PAT_ID.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />



<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>体内デバイス一覧 検索画面</h2>

        <div id="searchItemPatId" style="display:inline-flex">
            <div id ="searchItem">

                <form method="POST" action="<c:url value='?action=${actSearcher}&command=${commSearchByDepartment}' />">
                    <label for="${AttributeConst.SEARCH_EXAM_ITEM.getValue()}">検査項目</label>
                        <select name="${AttributeConst.SEARCH_EXAM_ITEM.getValue()}" id="${AttributeConst.SEARCH_EXAM_ITEM.getValue()}">
                            <option value="${AttributeConst.EXAM_X_RAY.getValue()}"
                                <c:if test="${examinationItem == AttributeConst.EXAM_X_RAY.getValue()}">selected</c:if>>単純X線検査
                            </option>
                            <option value="${AttributeConst.EXAM_CT.getValue()}"
                                <c:if test="${examinationItem == AttributeConst.EXAM_CT.getValue()}">selected</c:if>>CT検査
                            </option>
                            <option value="${AttributeConst.EXAM_TV.getValue()}"
                                <c:if test="${examinationItem == AttributeConst.EXAM_TV.getValue()}">selected</c:if>>X線TV検査
                            </option>
                            <option value="${AttributeConst.EXAM_Manma.getValue()}"
                                <c:if test="${examinationItem == AttributeConst.EXAM_Manma.getValue()}">selected</c:if>>乳腺X線検査
                            </option>
                            <option value="${AttributeConst.EXAM_MRI.getValue()}"
                                <c:if test="${examinationItem == AttributeConst.EXAM_MRI.getValue()}">selected</c:if>>MR検査
                            </option>
                            <option value="${AttributeConst.EXAM_ALL.getValue()}"
                                <c:if test="${examinationItem == AttributeConst.EXAM_ALL.getValue()}">selected</c:if>>すべて
                            </option>
                        </select>

                    <fmt:parseDate value="${searchDate}" pattern="yyyy-MM-dd" var="Date" type="date" />
                    <label for="${AttributeConst.SEARCH_DATE.getValue()}">検索日</label>
                    <input type="date" name="${AttributeConst.SEARCH_DATE.getValue()}" id="${AttributeConst.SEARCH_DATE.getValue()}" value="<fmt:formatDate value="${Date}" pattern="yyyy-MM-dd" />" />
                    <button type="submit">更新</button>
                </form>
            </div>

            <div id="searchPatId">
                <form  method="POST" action="<c:url value='?action=${actSearcher}&command=${commSearchByPatId}' />">
                    <label for="${AttributeConst.PATEXAM_PAT_ID.getValue()}">患者IDで検索</label><br />
                    <input type="text" name="${AttributeConst.SEARCH_PAT_ID.getValue()}" id="${AttributeConst.SEARCH_PAT_ID.getValue()}" />
                    <button type="submit">検索</button><br /><br />
                </form>
            </div>
        </div>
        <br />


        <ul id="${AttributeConst.SEARCH_EXAM_CONDITION.getValue()}">
            <c:forEach var="search" items="${searchDevices}">
                <li>
                    <c:choose>
                        <c:when test="${examinationItem == AttributeConst.EXAM_X_RAY.getValue()}">
                            <c:if test="${search.acceptabilityOfXrayExam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${search.patientName} 様には、${examinationItem}を${search.acceptabilityOfXrayExam}なデバイス【${search.deviceName}】 があります。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examinationItem == AttributeConst.EXAM_CT.getValue()}">
                            <c:if test="${search.acceptabilityOfCtExam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${search.patientName} 様には、${examinationItem}を${search.acceptabilityOfCtExam}なデバイス【${search.deviceName}】 があります。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examinationItem == AttributeConst.EXAM_TV.getValue()}">
                            <c:if test="${search.acceptabilityOfTvExam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${search.patientName} 様には、${examinationItem}を${search.acceptabilityOfTvExam}なデバイス【${search.deviceName}】 があります。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examinationItem == AttributeConst.EXAM_Manma.getValue()}">
                            <c:if test="${search.acceptabilityOfManmaExam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${search.patientName} 様には、${examinationItem}を${search.acceptabilityOfManmaExam}なデバイス【${search.deviceName}】 があります。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examinationItem == AttributeConst.EXAM_MRI.getValue()}">
                            <c:if test="${search.acceptabilityOfMrExam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${search.patientName} 様には、${examinationItem}を${search.acceptabilityOfMrExam}なデバイス【${search.deviceName}】 があります。" />
                            </c:if>
                        </c:when>
                    </c:choose>
                </li>
            </c:forEach>
        </ul>




        <ul id="${AttributeConst.SEARCH_NOTSAFE_COUNT.getValue()}">
            <li>
                <c:choose>
                    <c:when test="${examinationItem == AttributeConst.EXAM_X_RAY.getValue()}">
                        <c:if test="${notSafeCount.get(AttributeConst.EXAM_X_RAY.getValue()) ==  0}">
                            〇 <c:out value= "${examinationItem}を実施するうえで、注意すべきデバイスはありません。" />
                        </c:if>
                    </c:when>
                    <c:when test="${examinationItem == AttributeConst.EXAM_CT.getValue()}">
                        <c:if test="${notSafeCount.get(AttributeConst.EXAM_CT.getValue()) ==  0}">
                            〇 <c:out value= "${examinationItem}を実施するうえで、注意すべきデバイスはありません。" />
                        </c:if>
                    </c:when>
                    <c:when test="${examinationItem == AttributeConst.EXAM_TV.getValue()}">
                        <c:if test="${notSafeCount.get(AttributeConst.EXAM_TV.getValue()) ==  0}">
                            〇 <c:out value= "${examinationItem}を実施するうえで、注意すべきデバイスはありません。" />
                        </c:if>
                    </c:when>
                    <c:when test="${examinationItem == AttributeConst.EXAM_Manma.getValue()}">
                        <c:if test="${notSafeCount.get(AttributeConst.EXAM_Manma.getValue()) ==  0}">
                            〇 <c:out value= "${examinationItem}を実施するうえで、注意すべきデバイスはありません。" />
                        </c:if>
                    </c:when>
                    <c:when test="${examinationItem == AttributeConst.EXAM_MRI.getValue()}">
                        <c:if test="${notSafeCount.get(AttributeConst.EXAM_MRI.getValue()) ==  0}">
                            〇 <c:out value= "${examinationItem}を実施するうえで、注意すべきデバイスはありません。" />
                        </c:if>
                    </c:when>
                </c:choose>
            </li>
        </ul>
        <br />

        <h2>
            <c:if test="${examinationItem != AttributeConst.EXAM_ALL.getValue()}">
                ${examinationItem}をうける患者に埋め込まれているデバイス一覧
            </c:if>
            <c:if test="${examinationItem == AttributeConst.EXAM_ALL.getValue()}">
                ${examinationItem}の検査　デバイス一覧
            </c:if>
        </h2>
        <table id="searchList">
            <tbody>
                <tr>
                    <th class="${AttributeConst.SEARCH_PAT_ID.getValue()}">患者ID</th>
                    <th class="${AttributeConst.SEARCH_PAT_NAME.getValue()}">患者名</th>
                    <th class="${AttributeConst.SEARCH_GENERAL_NAME.getValue()}">一般的名称</th>
                    <th class="${AttributeConst.SEARCH_DEV_NAME.getValue()}">デバイスの販売名</th>
                    <th class="searchCondition">検査の可否</th>
                    <th class="searchAction">確認</th>
                </tr>
                <c:forEach var="search" items="${searchDevices}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="${AttributeConst.SEARCH_PAT_ID.getValue()}"><c:out value="${search.patientId}" /></td>
                        <td class="${AttributeConst.SEARCH_PAT_NAME.getValue()}"><c:out value="${search.patientName}" /></td>
                        <td class="${AttributeConst.SEARCH_GENERAL_NAME.getValue()}"><c:out value="${search.generalName}" /></td>
                        <td class="${AttributeConst.SEARCH_DEV_NAME.getValue()}"><c:out value="${search.deviceName}" /></td>
                        <td class="searchCondition">
                            <c:choose>
                                <c:when test="${examinationItem == AttributeConst.EXAM_X_RAY.getValue()}">${search.acceptabilityOfXrayExam}</c:when>
                                <c:when test="${examinationItem == AttributeConst.EXAM_CT.getValue()}">${search.acceptabilityOfCtExam}</c:when>
                                <c:when test="${examinationItem == AttributeConst.EXAM_TV.getValue()}">${search.acceptabilityOfTvExam}</c:when>
                                <c:when test="${examinationItem == AttributeConst.EXAM_Manma.getValue()}">${search.acceptabilityOfManmaExam}</c:when>
                                <c:when test="${examinationItem == AttributeConst.EXAM_MRI.getValue()}">${search.acceptabilityOfMrExam}</c:when>
                            </c:choose>
                        </td>
                        <td class="searchAction"><a href="<c:url value='?action=${actSearcher}&command=${commShow}&id=${search.id}&examinationItem=${examinationItem}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${searchesCount} 件） <br />
            <c:forEach var="i" begin="1" end="${((searchesCount -1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actSearcher}&command=${commSearchByDepartment}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

    </c:param>
</c:import>