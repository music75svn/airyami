<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%@ include file="/include/admin_standard.jsp"%>
<jsp:include page="/include/common.jsp"/>

<script type="text/javascript">

$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	gfn_OnLoad();
	
	fn_init();
	
	if("DETAIL" == $("#MODE").val())
		fn_srch();
});


//화면내 초기화 부분
function fn_init(){
	// 상세일때 컨트롤 비활성화
	if("DETAIL" == $("#MODE").val()){
		$("#BIZ_DT").attr("readonly",true);
		$("#FR_CURRENCY").attr("readonly",true);
		$("#TO_CURRENCY").attr("readonly",true);
	}
	else{
		$('#BIZ_DT').val(gfn_getToday(true));
	}

}


////////////////////////////////////////////////////////////////////////////////////
//호출부분 정의
//조회
function fn_srch(){
	
	if(gfn_isNull($("#SEQ").val()))
		return;
	
	var inputParam = new Object();
	inputParam.sid 				= "getExchangeRateDetail";
	inputParam.url 				= "/exchangeRate/getExchangeRateDetail.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	
	gfn_Transaction( inputParam );
}


//저장
function fn_save(){
	// 필수체크
	if(!gfn_validationForm($("#srchForm"))){
		return;
	}
	
	if($("#FR_CURRENCY").val() == $("#TO_CURRENCY").val()){
		alert("<spring:message code="errors.sameCurrency.msg"/>");
		return false;
	}
	
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return false;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "saveExchangeRate";
	inputParam.url 				= "/exchangeRate/saveExchangeRate.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	inputParam.data.PROC_MODE	= $("#MODE").val()=="CREATE" ? "CREATE" : "UPDATE";
	
	gfn_Transaction( inputParam );
}

//삭제
function fn_delete(){
	
	if(!confirm("<spring:message code="common.delete.msg"/>")){
		return false;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "deleteExchangeRate";
	inputParam.url 				= "/exchangeRate/saveExchangeRate.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	inputParam.data.PROC_MODE	= "DELETE";
	
	gfn_Transaction( inputParam );
}

////////////////////////////////////////////////////////////////////////////////////
//콜백 함수
function fn_callBack(sid, result){
	//debugger;
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	if(sid == "getExchangeRateDetail"){
		gfn_setDetails(result.ds_detail, $("#srchForm"));	// 지역내 상세 내용 셋업
	}
	else if(sid == "saveExchangeRate"){
		// 창을 닫던지. 부모 재조회를 하던지 
		alert('<spring:message code="success.common.update" />');
		opener.fn_srch();
		self.close();
	}
	else if(sid == "deleteExchangeRate"){
		alert('<spring:message code="success.common.delete" />');
		opener.fn_srch();
		self.close();
	}
}



</script>
</head>
<body class="popup">
<h1><spring:message code="word.exchangeRate"/>&nbsp;<spring:message code="word.detail"/><a onClick="self.close();" class="close">X</a></h1>

<div class="content">
	<h4><spring:message code="word.exchangeRate"/>&nbsp;<spring:message code="word.detail"/></h4>
	<form id="srchForm" name="srchForm" method="post" onsubmit="return false;">
		<input type="hidden" name="SEQ" id="SEQ" value='<c:out value="${SEQ}"/>'/>
		<input type="hidden" name="MODE" id="MODE" value='<c:out value="${MODE}"/>'/>
		<table cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="30%">
			<col width="70%">
			</colgroup>
			<tr>
				<th><spring:message code="word.dateOfTransaction"/></th>
				<td>
					<input type="text" size="8" name="BIZ_DT" id="BIZ_DT" isDate="Y" class="datepicker" title="<spring:message code="word.dateOfTransaction"/>" depends="required">
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.beforeCurrency"/></th>
				<td>
					<select id="FR_CURRENCY" name="FR_CURRENCY" title="<spring:message code="word.beforeCurrency"/>" depends="required">
                        <c:forEach var="CURRENCY" items="${ds_cd_CURRENCY}">
                            <option value="${CURRENCY.CD}">${CURRENCY.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.afterCurrency"/></th>
				<td>
					<select id="TO_CURRENCY" name="TO_CURRENCY" title="<spring:message code="word.afterCurrency"/>" depends="required">
                        <c:forEach var="CURRENCY" items="${ds_cd_CURRENCY}">
                            <option value="${CURRENCY.CD}">${CURRENCY.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.baseExchangeRate"/></th>
				<td><input type="text" name="BASIC_EXT_RATE" id="BASIC_EXT_RATE" isNum="Y" class="onlynum2" maxlength="7" title="<spring:message code="word.baseExchangeRate"/>" depends="required"/></td>
			</tr>
			<tr>
				<th><spring:message code="word.tranExchangeRate"/></th>
				<td><input type="text" name="BIZ_EXT_RATE" id="BIZ_EXT_RATE" isNum="Y" class="onlynum2" maxlength="7" title="<spring:message code="word.tranExchangeRate"/>" depends="required"/></td>
			</tr>
		</table>
		</form>
		<div class="btn_zone">
		<c:choose>
			<c:when test="${MODE=='DETAIL'}">
			<button type="button" onClick="javascript:fn_delete()"><spring:message code="button.delete"/></button>
			<button type="button" onClick="javascript:fn_save()"><spring:message code="button.update"/></button>
			</c:when>
			<c:when test="${MODE=='CREATE'}">
			<button type="button" onClick="javascript:fn_save()"><spring:message code="button.create"/></button>
			</c:when>
		</c:choose>
	    </div>
</div>
</body>