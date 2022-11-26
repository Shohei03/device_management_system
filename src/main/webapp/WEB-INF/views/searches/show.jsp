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
        <div class="searchPatient">
            <div class="searchPatientId">患者ID： <c:out value="${patient.patientId}" /></div>
            <div class="searchPatientName">患者名： <ruby><c:out value="${patient.patientName}" /><rt>${patient.patientNameKana}</rt></ruby></div>
        </div>


        <form method="POST" action="<c:url value='?action=${actSearcher}&command=${commShow}&id=${id}' />">
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
                <button type="submit">更新</button>
        </form>
        <br />

        <ul id="${AttributeConst.SEARCH_EXAM_CONDITION.getValue()}">
            <c:forEach var="searchDevice" items="${searchDevices}">
                <li>
                    <c:choose>
                        <c:when test="${examinationItem == AttributeConst.EXAM_X_RAY.getValue()}">
                            <c:if test="${searchDevice.acceptabilityOfXrayExam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${examinationItem}を${searchDevice.acceptabilityOfXrayExam}なデバイス【$searchDevice.deviceName}】 があります。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examinationItem == AttributeConst.EXAM_CT.getValue()}">
                            <c:if test="${searchDevice.acceptabilityOfCtExam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${examinationItem}を${searchDevice.acceptabilityOfCtExam}なデバイス【${searchDevice.deviceName}】 があります。" />
                            </c:if>
                            <c:if test="${searchDevice.getGeneralName() == AttributeConst.SEARCH_DEV_PACEMAKER.getValue()}"><br />
                                ☆<c:out value="${MessageConst.C_PACEMAKER.getMessage()}" />
                            </c:if>
                        </c:when>
                        <c:when test="${examinationItem == AttributeConst.EXAM_TV.getValue()}">
                            <c:if test="${searchDevice.acceptabilityOfTvExam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${examinationItem}を${searchDevice.acceptabilityOfTvExam}なデバイス【${searchDevice.deviceName}】 があります。" />
                            </c:if>
                            <c:if test="${searchDevice.getGeneralName() == AttributeConst.SEARCH_DEV_PACEMAKER.getValue()}"><br />
                                ☆<c:out value="${MessageConst.C_PACEMAKER_TV.getMessage()}" />
                            </c:if>
                        </c:when>
                        <c:when test="${examinationItem == AttributeConst.EXAM_Manma.getValue()}">
                            <c:if test="${searchDevice.acceptabilityOfManmaExam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${examinationItem}を${searchDevice.acceptabilityOfManmaExam}なデバイス【${searchDevice.deviceName}】 があります。" />
                            </c:if>
                        </c:when>
                        <c:when test="${examinationItem == AttributeConst.EXAM_MRI.getValue()}">
                            <c:if test="${searchDevice.acceptabilityOfMrExam !=  AttributeConst.PACK_EXM_SAFE.getValue()}">
                                〇 <c:out value= "${examinationItem}を${searchDevice.acceptabilityOfMrExam}なデバイス【${searchDevice.deviceName}】 があります。" />
                                <br />☆詳しくは添付文書を確認してください。
                            </c:if>
                        </c:when>
                    </c:choose>
                </li>
            </c:forEach>
        </ul>

        <a  target="_blank" href = "https://www.pmda.go.jp/PmdaSearch/kikiSearch">添付文書検索ページへ</a>

        <c:if test="${examinationItem != AttributeConst.EXAM_ALL.getValue() }">
            <table id="searchList">
                <tbody>
                    <tr>
                        <th class="${AttributeConst.SEARCH_IMP_DATE.getValue()}">埋込日</th>
                        <th class="${AttributeConst.SEARCH_APP_NUM.getValue()}">添付文書承認番号</th>
                        <th class="${AttributeConst.SEARCH_GENERAL_NAME.getValue()}">一般的名称</th>
                        <th class="${AttributeConst.SEARCH_DEV_NAME.getValue()}">デバイスの販売名</th>
                        <th class="searchDeviceCondition">${examinationItem}の可否</th>
                    </tr>
                    <c:forEach var="searchDevice" items="${searchDevices}" varStatus="status">
                        <fmt:parseDate value="${searchDevice.implantedAt}" pattern="yyyy-MM-dd" var="implantedDay" type="date" />
                        <tr class="row${status.count % 2}">
                            <td class="${AttributeConst.SEARCH_IMP_DATE.getValue()}"><fmt:formatDate value='${implantedDay}' pattern='yyyy-MM-dd' /></td>
                            <td class="${AttributeConst.SEARCH_APP_NUM.getValue()}"><c:out value="${searchDevice.approvalNumber}" /></td>
                            <td class="${AttributeConst.SEARCH_GENERAL_NAME.getValue()}"><c:out value="${searchDevice.generalName}" /></td>
                            <td class="${AttributeConst.SEARCH_DEV_NAME.getValue()}"><c:out value="${searchDevice.deviceName}" /></td>
                            <td class="searchDeviceCondition">
                                <c:choose>
                                    <c:when test="${examinationItem == AttributeConst.EXAM_X_RAY.getValue()}">${searchDevice.acceptabilityOfXrayExam}</c:when>
                                    <c:when test="${examinationItem == AttributeConst. EXAM_CT.getValue()}">${searchDevice.acceptabilityOfCtExam}</c:when>
                                    <c:when test="${examinationItem == AttributeConst. EXAM_TV.getValue()}">${searchDevice.acceptabilityOfTvExam}</c:when>
                                    <c:when test="${examinationItem == AttributeConst. EXAM_Manma.getValue()}">${searchDevice.acceptabilityOfMrExam}</c:when>
                                    <c:when test="${examinationItem == AttributeConst. EXAM_MRI.getValue()}">${searchDevice.acceptabilityOfManmaExam}</c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        </c:if>
        <c:if test="${examinationItem == AttributeConst.EXAM_ALL.getValue() }">
            <table id="searchListAllExam">
                <tbody>
                    <tr>
                        <th class="${AttributeConst.SEARCH_IMP_DATE.getValue()}" rowspan="2">埋込日</th>
                        <th class="${AttributeConst.SEARCH_APP_NUM.getValue()}"rowspan="2">添付文書承認番号</th>
                        <th class="${AttributeConst.SEARCH_GENERAL_NAME.getValue()}" rowspan="2">一般的名称</th>
                        <th class="${AttributeConst.SEARCH_DEV_NAME.getValue()}" rowspan="2">デバイスの販売名</th>
                        <th class="searchDeviceAcceptabilityOfExam" colspan="5">${examination_item}の検査の可否</th>
                    </tr>
                    <tr>
                        <th class="${AttributeConst.PACK_X_RAY.getValue()}">単純X線</th>
                        <th class="${AttributeConst.PACK_CT.getValue()}">CT</th>
                        <th class="${AttributeConst.PACK_TV.getValue()}">X線TV</th>
                        <th class="${AttributeConst.PACK_Manma.getValue()}">乳腺X線</th>
                        <th class="${AttributeConst.PACK_MRI.getValue()}">MRI</th>
                    </tr>
                    <c:forEach var="searchDevice" items="${searchDevices}" varStatus="status">
                        <fmt:parseDate value="${searchDevice.implantedAt}" pattern="yyyy-MM-dd" var="implantDate" type="date" />
                        <tr class="row${status.count % 2}">
                            <td class="${AttributeConst.SEARCH_IMP_DATE.getValue()}"><fmt:formatDate value="${implantDate}" pattern="yyyy-MM-dd" /></td>
                            <td class="${AttributeConst.SEARCH_APP_NUM.getValue()}"><c:out value="${searchDevice.approvalNumber}" /></td>
                            <td class="${AttributeConst.SEARCH_GENERAL_NAME.getValue()}"><c:out value="${searchDevice.generalName}" /></td>
                            <td class="${AttributeConst.SEARCH_DEV_NAME.getValue()}"><c:out value="${searchDevice.deviceName}" /></td>
                            <td class="${AttributeConst.PACK_X_RAY.getValue()}"><c:out value="${searchDevice.acceptabilityOfXrayExam}" /></td>
                            <td class="${AttributeConst.PACK_CT.getValue()}"><c:out value="${searchDevice.acceptabilityOfCtExam}" /></td>
                            <td class="${AttributeConst.PACK_TV.getValue()}"><c:out value="${searchDevice.acceptabilityOfTvExam}" /></td>
                            <td class="${AttributeConst.PACK_Manma.getValue()}"><c:out value="${searchDevice.acceptabilityOfManmaExam}" /></td>
                            <td class="${AttributeConst.PACK_MRI.getValue()}"><c:out value="${searchDevice.acceptabilityOfMrExam}" /></td>
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