<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.AttributeConst"%>
<%@ page import="constants.MessageConst"%>


<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
                    ・<c:out value="${error}" />
            <c:if test="${error == MessageConst.E_DUPLI_DATA.getMessage()}">
                <button type="submit">重複登録</button>
            </c:if>
            <br />
        </c:forEach>
    </div>
</c:if>




<label for="${AttributeConst.PATEXAM_EXAM_ITEM.getValue()}">検査項目</label>
<br />
<select name="${AttributeConst.PATEXAM_EXAM_ITEM.getValue()}"
    id="${AttributeConst.PATEXAM_EXAM_ITEM.getValue()}">
    <option value="${AttributeConst.EXAM_X_RAY.getValue()}"
        <c:if test="${patientExamination.examination_item == AttributeConst.EXAM_X_RAY.getValue()}">selected</c:if>>単純X線検査</option>
    <option value="${AttributeConst.EXAM_CT.getValue()}"
        <c:if test="${patientExamination.examination_item == AttributeConst.EXAM_CT.getValue()}">selected</c:if>>CT検査</option>
    <option value="${AttributeConst.EXAM_TV.getValue()}"
        <c:if test="${patientExamination.examination_item == AttributeConst.EXAM_TV.getValue()}">selected</c:if>>X線TV検査</option>
    <option value="${AttributeConst.EXAM_Manma.getValue()}"
        <c:if test="${patientExamination.examination_item == AttributeConst.EXAM_Manma.getValue()}">selected</c:if>>乳腺X線検査</option>
    <option value="${AttributeConst.EXAM_MRI.getValue()}"
        <c:if test="${patientExamination.examination_item == AttributeConst.EXAM_MRI.getValue()}">selected</c:if>>MR検査</option>
</select>
<br />
<br />

<label for="${AttributeConst.PATEXAM_EXAM_DATE.getValue()}">検査日</label>
<br />
<input type="date" name="${AttributeConst.PATEXAM_EXAM_DATE.getValue()}"
    id="${AttributeConst.PATEXAM_EXAM_DATE.getValue()}"
    value="${patientExamination.examination_date}" />
<br />
<br />

<label for="${AttributeConst.PATEXAM_RESERVATION_TIME.getValue()}">予約時間：</label>
<br />
<input type="time" list="data-list"
    name="${AttributeConst.PATEXAM_RESERVATION_TIME.getValue()}"
    id="${AttributeConst.PATEXAM_RESERVATION_TIME.getValue()}"
    value="${patientExamination.reservation_time}" min="09:00" max="17:00"
    step="3600" />
<span></span>
<datalist id="data-list">
    <option value="09:00"></option>
    <option value="10:00"></option>
    <option value="11:00"></option>
    <option value="12:00"></option>
    <option value="13:00"></option>
    <option value="14:00"></option>
    <option value="15:00"></option>
    <option value="16:00"></option>
    <option value="17:00"></option>
</datalist>
<br />
<br />

<label for="${AttributeConst.PATEXAM_PAT_ID.getValue()}">患者ID</label>
<br />
<input type="text" name="${AttributeConst.PATEXAM_PAT_ID.getValue()}"
    id="${AttributeConst.PATEXAM_PAT_ID.getValue()}"
    value="${patientExamination.patient_id}" />
<br />
<br />

<label for="${AttributeConst.PATEXAM_PAT_NAME.getValue()}">患者名</label>
<br />
<input type="text" name="${AttributeConst.PATEXAM_PAT_NAME.getValue()}"
    id="${AttributeConst.PATEXAM_PAT_NAME.getValue()}"
    value="${patientExamination.patient_name}" />
<br />
<br />

<label for="${AttributeConst.PATEXAM_PAT_NAME_KANA.getValue()}">患者名（ひらがな)</label>
<br />
<input type="text"
    name="${AttributeConst.PATEXAM_PAT_NAME_KANA.getValue()}"
    id="${AttributeConst.PATEXAM_PAT_NAME_KANA.getValue()}"
    value="${patientExamination.patient_name_kana}" />
<br />
<br />

<input type="hidden" name="${AttributeConst.PATEXAM_ID.getValue()}" value="${patientExamination.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />

