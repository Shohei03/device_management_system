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

        <div id="search_item_patId" style="display:inline-flex">
            <div id ="search_item">

                <form method="POST" action="<c:url value='?action=${actSearcher}&command=${commSearchByDepartment}' />">
                    <label for="${AttributeConst.SEARCH_EXAM_ITEM.getValue()}">検査項目</label>
                        <select name="${AttributeConst.SEARCH_EXAM_ITEM.getValue()}" id="${AttributeConst.SEARCH_EXAM_ITEM.getValue()}">
                            <option value="${AttributeConst.EXAM_X_RAY.getValue()}"
                                <c:if test="${examination_item == AttributeConst.EXAM_X_RAY.getValue()}">selected</c:if>>単純X線検査
                            </option>
                            <option value="${AttributeConst.EXAM_CT.getValue()}"
                                <c:if test="${examination_item == AttributeConst.EXAM_CT.getValue()}">selected</c:if>>CT検査
                            </option>
                            <option value="${AttributeConst.EXAM_TV.getValue()}"
                                <c:if test="${examination_item == AttributeConst.EXAM_TV.getValue()}">selected</c:if>>X線TV検査
                            </option>
                            <option value="${AttributeConst.EXAM_Manma.getValue()}"
                                <c:if test="${examination_item == AttributeConst.EXAM_Manma.getValue()}">selected</c:if>>乳腺X線検査
                            </option>
                            <option value="${AttributeConst.EXAM_MRI.getValue()}"
                                <c:if test="${examination_item == AttributeConst.EXAM_MRI.getValue()}">selected</c:if>>MR検査
                            </option>
                            <option value="${AttributeConst.EXAM_ALL.getValue()}"
                                <c:if test="${examination_item == AttributeConst.EXAM_ALL.getValue()}">selected</c:if>>すべて
                            </option>
                        </select>

                    <fmt:parseDate value="${search_date}" pattern="yyyy-MM-dd" var="searchDate" type="date" />
                    <label for="${AttributeConst.SEARCH_DATE.getValue()}">検索日</label>
                    <input type="date" name="${AttributeConst.SEARCH_DATE.getValue()}" id="${AttributeConst.SEARCH_DATE.getValue()}" value="<fmt:formatDate value="${searchDate}" pattern="yyyy-MM-dd" />" />
                    <button type="submit">更新</button>
                </form>
            </div>

            <div id="search_patId">
                <form  method="POST" action="<c:url value='?action=${actSearcher}&command=${commSearchByPatId}' />">
                    <label for="${AttributeConst.PATEXAM_PAT_ID.getValue()}">患者IDで検索</label><br />
                    <input type="text" name="${AttributeConst.SEARCH_PAT_ID.getValue()}" id="${AttributeConst.SEARCH_PAT_ID.getValue()}" />
                    <button type="submit">検索</button><br /><br />
                </form>
            </div>
        </div>
            <br />


            <ul id="${AttributeConst.SEARCH_EXAM_CONDITION.getValue()}">
                <c:forEach var="search" items="${search_devices}">
                    <li>
                        <c:choose>
                            <c:when test="${examination_item == AttributeConst.EXAM_X_RAY.getValue()}">
                                <c:if test="${search.acceptability_of_X_ray_exam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                    〇 <c:out value= "${search.patient_name} 様には、${examination_item}に${search.acceptability_of_X_ray_exam}なデバイス【${search.device_name}】 があります。" />
                                </c:if>
                            </c:when>
                            <c:when test="${examination_item == AttributeConst.EXAM_CT.getValue()}">
                                <c:if test="${search.acceptability_of_CT_exam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                    〇 <c:out value= "${search.patient_name} 様には、${examination_item}に${search.acceptability_of_CT_exam}なデバイス【${search.device_name}】 があります。" />
                                </c:if>
                            </c:when>
                            <c:when test="${examination_item == AttributeConst.EXAM_TV.getValue()}">
                                <c:if test="${search.acceptability_of_TV_exam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                    〇 <c:out value= "${search.patient_name} 様には、${examination_item}に${search.acceptability_of_TV_exam}なデバイス【${search.device_name}】 があります。" />
                                </c:if>
                            </c:when>
                            <c:when test="${examination_item == AttributeConst.EXAM_Manma.getValue()}">
                                <c:if test="${search.acceptability_of_Manma_exam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                    〇 <c:out value= "${search.patient_name} 様には、${examination_item}に${search.acceptability_of_Manma_exam}なデバイス【${search.device_name}】 があります。" />
                                </c:if>
                            </c:when>
                            <c:when test="${examination_item == AttributeConst.EXAM_MRI.getValue()}">
                                <c:if test="${search.acceptability_of_MR_exam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                    〇 <c:out value= "${search.patient_name} 様には、${examination_item}に${search.acceptability_of_MR_exam}なデバイス【${search.device_name}】 があります。" />
                                </c:if>
                            </c:when>
                        </c:choose>
                    </li>
                </c:forEach>
            </ul>




            <ul id="${AttributeConst.SEARCH_NOTSAFE_COUNT.getValue()}">
                <li>
                    <c:choose>
                        <c:when test="${examination_item == AttributeConst.EXAM_X_RAY.getValue()}">
                            <c:if test="${notSafe_count.get(AttributeConst.EXAM_X_RAY.getValue()) ==  0}">
                                〇 <c:out value= "${examination_item}を実施するうえで、注意すべきデバイスはありません。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examination_item == AttributeConst.EXAM_CT.getValue()}">
                            <c:if test="${notSafe_count.get(AttributeConst.EXAM_CT.getValue()) ==  0}">
                                〇 <c:out value= "${examination_item}を実施するうえで、注意すべきデバイスはありません。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examination_item == AttributeConst.EXAM_TV.getValue()}">
                            <c:if test="${notSafe_count.get(AttributeConst.EXAM_TV.getValue()) ==  0}">
                                〇 <c:out value= "${examination_item}を実施するうえで、注意すべきデバイスはありません。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examination_item == AttributeConst.EXAM_Manma.getValue()}">
                            <c:if test="${notSafe_count.get(AttributeConst.EXAM_Manma.getValue()) ==  0}">
                                〇 <c:out value= "${examination_item}を実施するうえで、注意すべきデバイスはありません。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examination_item == AttributeConst.EXAM_MRI.getValue()}">
                            <c:if test="${notSafe_count.get(AttributeConst.EXAM_MRI.getValue()) ==  0}">
                                〇 <c:out value= "${examination_item}を実施するうえで、注意すべきデバイスはありません。" />
                            </c:if>
                        </c:when>
                    </c:choose>
                </li>
            </ul>
            <br /><br />








        <table id="search_list">
            <tbody>
                <tr>
                    <th class="search_patient_id">患者ID</th>
                    <th class="search_patient_name">氏名</th>
                    <th class="search_general_name">一般的名称</th>
                    <th class="search_device_name">デバイスの販売名</th>
                    <th class="search_condition">検査の可否</th>
                    <th class="search_action">確認</th>
                </tr>
                <c:forEach var="search" items="${search_devices}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="search_pat_id"><c:out value="${search.patient_id}" /></td>
                        <td class="search_pat_name"><c:out value="${search.patient_name}" /></td>
                        <td class="search_general_name"><c:out value="${search.general_name}" /></td>
                        <td class="search_device_name"><c:out value="${search.device_name}" /></td>
                        <td class="search_condition">
                            <c:choose>
                                <c:when test="${examination_item == AttributeConst.EXAM_X_RAY.getValue()}">${search.acceptability_of_X_ray_exam}</c:when>
                                <c:when test="${examination_item == AttributeConst.EXAM_CT.getValue()}">${search.acceptability_of_CT_exam}</c:when>
                                <c:when test="${examination_item == AttributeConst.EXAM_TV.getValue()}">${search.acceptability_of_TV_exam}</c:when>
                                <c:when test="${examination_item == AttributeConst.EXAM_Manma.getValue()}">${search.acceptability_of_Manma_exam}</c:when>
                                <c:when test="${examination_item == AttributeConst.EXAM_MRI.getValue()}">${search.acceptability_of_MR_exam}</c:when>
                            </c:choose>
                        </td>
                        <td class="search_action"><a href="<c:url value='?action=${actSearcher}&command=${commShow}&id=${search.id}&examination_item=${examination_item}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${patientDevicesCount} 件） <br />
            <c:forEach var="i" begin="1" end="${((patientDevicesCount -1) / maxRow) + 1}" step="1">
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