<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.MessageConst" %>

<c:set var="actSearcher" value="${ForwardConst.ACT_SEARCHER.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="searchByDepartment" value="${ForwardConst.CMD_SEARCH_BY_DEPARTMENT.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />


<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <h2>詳細ページ</h2>
        <div class="search_patient">
            <div class="search_patient_id">患者ID： <c:out value="${patient.patient_id}" /></div>
            <div class="search_patient_name">患者名： <ruby><c:out value="${patient.patient_name}" /><rt>${patient.patient_name_kana}</rt></ruby></div>
        </div>


        <form method="POST" action="<c:url value='?action=${actSearcher}&command=${commShow}&id=${id}' />">
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
                <button type="submit">更新</button>
        </form>
        <br />

        <ul id="${AttributeConst.SEARCH_EXAM_CONDITION.getValue()}">
            <c:forEach var="search_device" items="${search_devices}">
                <li>
                    <c:choose>
                        <c:when test="${examination_item == AttributeConst.EXAM_X_RAY.getValue()}">
                            <c:if test="${search_device.acceptability_of_X_ray_exam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${examination_item}を${search_device.acceptability_of_X_ray_exam}なデバイス【$search_device.device_name}】 があります。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examination_item == AttributeConst.EXAM_CT.getValue()}">
                            <c:if test="${search_device.acceptability_of_CT_exam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${examination_item}を${search_device.acceptability_of_CT_exam}なデバイス【${search_device.device_name}】 があります。" />
                            </c:if>
                            <c:if test="${search_device.getGeneral_name() == AttributeConst.SEARCH_DEV_PACEMAKER.getValue()}"><br />
                                ☆<c:out value="${MessageConst.C_PACEMAKER.getMessage()}" />
                            </c:if>
                        </c:when>
                        <c:when test="${examination_item == AttributeConst.EXAM_TV.getValue()}">
                            <c:if test="${search_device.acceptability_of_TV_exam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${examination_item}を${search_device.acceptability_of_TV_exam}なデバイス【${search_device.device_name}】 があります。" />
                            </c:if>
                            <c:if test="${search_device.getGeneral_name() == AttributeConst.SEARCH_DEV_PACEMAKER.getValue()}"><br />
                                ☆<c:out value="${MessageConst.C_PACEMAKER_TV.getMessage()}" />
                            </c:if>
                        </c:when>
                        <c:when test="${examination_item == AttributeConst.EXAM_Manma.getValue()}">
                            <c:if test="${search_device.acceptability_of_Manma_exam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${examination_item}を${search_device.acceptability_of_Manma_exam}なデバイス【${search_device.device_name}】 があります。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examination_item == AttributeConst.EXAM_MRI.getValue()}">
                            <c:if test="${search_device.acceptability_of_MR_exam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${examination_item}を${search_device.acceptability_of_MR_exam}なデバイス【${search_device.device_name}】 があります。" />
                                <br />☆詳しくは添付文書を確認してください。
                            </c:if>
                        </c:when>
                    </c:choose>
                </li>
            </c:forEach>
        </ul>

        <a  target="_blank" href = "https://www.pmda.go.jp/PmdaSearch/kikiSearch">添付文書検索ページへ</a>

        <c:if test="${examination_item != AttributeConst.EXAM_ALL.getValue() }">
            <table id="search_list">
                <tbody>
                    <tr>
                        <th class="search_device_implantedAt">埋込日</th>
                        <th class="search_device_approval_num">添付文書承認番号</th>
                        <th class="search_device_general_name">一般的名称</th>
                        <th class="search_device_device_name">デバイスの販売名</th>
                        <th class="search_device_condition">${examination_item}の可否</th>
                    </tr>
                    <c:forEach var="search_device" items="${search_devices}" varStatus="status">
                        <fmt:parseDate value="${search_device.implantedAt}" pattern="yyyy-MM-dd" var="implantedDay" type="date" />
                        <tr class="row${status.count % 2}">
                            <td class="search_device_implantedAt"><fmt:formatDate value='${implantedDay}' pattern='yyyy-MM-dd' /></td>
                            <td class="search_device_approval_num"><c:out value="${search_device.approval_number}" /></td>
                            <td class="search_device_general_name"><c:out value="${search_device.general_name}" /></td>
                            <td class="search_device_device_name"><c:out value="${search_device.device_name}" /></td>
                            <td class="search_device_condition">
                                <c:choose>
                                    <c:when test="${examination_item == AttributeConst.EXAM_X_RAY.getValue()}">${search_device.acceptability_of_X_ray_exam}</c:when>
                                    <c:when test="${examination_item == AttributeConst. EXAM_CT.getValue()}">${search_device.acceptability_of_CT_exam}</c:when>
                                    <c:when test="${examination_item == AttributeConst. EXAM_TV.getValue()}">${search_device.acceptability_of_TV_exam}</c:when>
                                    <c:when test="${examination_item == AttributeConst. EXAM_Manma.getValue()}">${search_device.acceptability_of_MR_exam}</c:when>
                                    <c:when test="${examination_item == AttributeConst. EXAM_MRI.getValue()}">${search_device.acceptability_of_Manma_exam}</c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        </c:if>
        <c:if test="${examination_item == AttributeConst.EXAM_ALL.getValue() }">
            <table id="search_list_all_exam">
                <tbody>
                    <tr>
                        <th class="search_device_implantedAt" rowspan="2">埋込日</th>
                        <th class="search_device_approval_num"rowspan="2">添付文書承認番号</th>
                        <th class="search_device_general_name" rowspan="2">一般的名称</th>
                        <th class="search_device_device_name" rowspan="2">デバイスの販売名</th>
                        <th class="search_device_acceptability_of_exam" colspan="5">${examination_item}の検査の可否</th>
                    </tr>
                    <tr>
                        <th class="search_acceptability_of_X_ray_exam">単純X線</th>
                        <th class="search_acceptability_of_CT_exam">CT</th>
                        <th class="search_acceptability_of_TV_exam">X線TV</th>
                        <th class="search_acceptability_of_Manma_exam">乳腺X線</th>
                        <th class="search_acceptability_of_MR_exam">MRI</th>
                    </tr>
                    <c:forEach var="search_device" items="${search_devices}" varStatus="status">
                        <fmt:parseDate value="${search_device.implantedAt}" pattern="yyyy-MM-dd" var="implantDate" type="date" />
                        <tr class="row${status.count % 2}">
                            <td class="search_device_implantedAt"><fmt:formatDate value="${implantDate}" pattern="yyyy-MM-dd" /></td>
                            <td class="search_device_approval_num"><c:out value="${search_device.approval_number}" /></td>
                            <td class="search_device_general_name"><c:out value="${search_device.general_name}" /></td>
                            <td class="search_device_device_name"><c:out value="${search_device.device_name}" /></td>
                            <td class="search_device_acceptability_of_X_ray_exam"><c:out value="${search_device.acceptability_of_X_ray_exam}" /></td>
                            <td class="search_device_acceptability_of_CT_exam"><c:out value="${search_device.acceptability_of_CT_exam}" /></td>
                            <td class="search_device_acceptability_of_TV_exam"><c:out value="${search_device.acceptability_of_TV_exam}" /></td>
                            <td class="search_device_acceptability_of_Manma_exam"><c:out value="${search_device.acceptability_of_Manma_exam}" /></td>
                            <td class="search_device_acceptability_of_MR_exam"><c:out value="${search_device.acceptability_of_MR_exam}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        </c:if>

        <p>
            <a href="<c:url value='?action=${actSearcher}&command=${searchByDepartment}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>