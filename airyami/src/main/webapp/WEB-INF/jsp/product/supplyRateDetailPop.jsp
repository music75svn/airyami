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
		//$("#BIZ_DT").attr("readonly",true);
		//$("#FR_CURRENCY").attr("readonly",true);
		//$("#TO_CURRENCY").attr("readonly",true);
	}
	else{
		$('#VALID_START_DATE').val(gfn_getToday(true));
		$('#VALID_END_DATE').val('2099-12-31');
	}

}


////////////////////////////////////////////////////////////////////////////////////
//호출부분 정의
//조회
function fn_srch(){
	
	if(gfn_isNull($("#SEQ").val()))
		return;
	
	var inputParam = new Object();
	inputParam.sid 				= "getSupplyRateDetail";
	inputParam.url 				= "/product/getSupplyRateDetail.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	
	gfn_Transaction( inputParam );
}

// 상품과 바이어회사에 대한 최근 등록내용 조회
function fn_srchLate(){
	
	if(gfn_isNull($("#SEQ").val()))
		return;
	
	var inputParam = new Object();
	inputParam.sid 				= "getSupplyRateDetail";
	inputParam.url 				= "/product/getSupplyRateDetail.do";
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
	inputParam.sid 				= "saveSupplyRate";
	inputParam.url 				= "/product/saveSupplyRate.do";
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
	inputParam.sid 				= "deleteSupplyRate";
	inputParam.url 				= "/product/saveSupplyRate.do";
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
	
	if(sid == "getSupplyRateDetail"){
		gfn_setDetails(result.ds_detail, $("#srchForm"));	// 지역내 상세 내용 셋업
	}
	else if(sid == "saveSupplyRate"){
		// 창을 닫던지. 부모 재조회를 하던지 
		alert('<spring:message code="success.common.update" />');
		opener.fn_srch();
		self.close();
	}
	else if(sid == "deleteSupplyRate"){
		alert('<spring:message code="success.common.delete" />');
		opener.fn_srch();
		self.close();
	}
}



</script>
</head>
<body class="popup">
<h1><spring:message code="word.supplyRate"/>&nbsp;<spring:message code="word.detail"/><a onClick="self.close();" class="close">X</a></h1>

<div class="content">
	<h4><spring:message code="word.supplyRate"/>&nbsp;<spring:message code="word.detail"/></h4>
	<form id="srchForm" name="srchForm" method="post" onsubmit="return false;">
		<input type="hidden" name="SEQ" id="SEQ" value='<c:out value="${SEQ}"/>'/>
		<input type="hidden" name="MODE" id="MODE" value='<c:out value="${MODE}"/>'/>
		<input type="hidden" name="BIZ_ENTITY_ID" id="BIZ_ENTITY_ID" value='<c:out value="${BIZ_ENTITY_ID}"/>'/>
		<input type="hidden" name="USER_TYPE" id="USER_TYPE" value='<c:out value="${USER_TYPE}"/>'/>
		<table cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="30%">
			<col width="70%">
			</colgroup>
			<tr>
				<th>상품</th>
				<td>
					<input type="text" id="PROD_NO" name="PROD_NO" value='<c:out value="${PROD_NO}"/>' title="<spring:message code="word.prodNo"/>" maxlength=20/>
				</td>
			</tr>
			<tr>
				<th>매입회사</th>
				<td>
					<input type="text" id="BUYER_BIZ_ENTITY_ID" name="BUYER_BIZ_ENTITY_ID" value='<c:out value="${BUYER_BIZ_ENTITY_ID}"/>' maxlength=20/>
				</td>
			</tr>
			<tr>
				<th>유효시작일</th>
				<td>
					<input type="text" size="8" name="VALID_START_DATE" id="VALID_START_DATE" isDate="Y" class="datepicker" depends="required">
				</td>
			</tr>
			<tr>
				<th>유효종료일</th>
				<td>
					<input type="text" size="8" name="VALID_END_DATE" id="VALID_END_DATE" isDate="Y" class="datepicker" depends="required">
				</td>
			</tr>
			<tr>
				<th>화폐</th>
				<td>
					<select id="FR_CURRENCY" name="FR_CURRENCY" depends="required">
                        <c:forEach var="CURRENCY" items="${ds_cd_CURRENCY}">
                            <option value="${CURRENCY.CD}">${CURRENCY.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>공급율</th>
				<td>
					<input type="text" size="5" name="SUPPLY_RATE" id="SUPPLY_RATE" isNum="Y">
				</td>
			</tr>
			<tr>
				<th>가격</th>
				<td>
					<input type="text" size="10" name="PRICE" id="PRICE" isNum="Y" class="onlynum2" depends="required">
				</td>
			</tr>
			<tr>
				<th>할인가능최소수량</th>
				<td>
					<input type="text" size="11" name="DC_MIN_QTY" id="DC_MIN_QTY" isNum="Y" class="onlynum2">
				</td>
			</tr>
			<tr>
				<th>할인 공급율</th>
				<td>
					<input type="text" size="5" name="DC_SUPPLY_RATE" id="DC_SUPPLY_RATE" isNum="Y">
				</td>
			</tr>
			<tr>
				<th>할인 가격</th>
				<td>
					<input type="text" size="10" name="DC_SUPPLY_PRICE" id="DC_SUPPLY_PRICE" isNum="Y" class="onlynum2" depends="required">
				</td>
			</tr>
			<tr>
				<th>공급가능 유효성여부</th>
				<td>
					<select id="VALID_YN" name="VALID_YN" title="<spring:message code="cop.useAt"/>">
						<option value="Y"><spring:message code="button.use"/></option>
						<option value="N"><spring:message code="button.notUsed"/></option>
					</select>
				</td>
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