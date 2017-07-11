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
	
	fn_srch();
});


//화면내 초기화 부분
function fn_init(){
}


////////////////////////////////////////////////////////////////////////////////////
//호출부분 정의
//조회
function fn_srch(){
	
	if(gfn_isNull($("#PROD_NO").val()))
		return;
	
	var inputParam = new Object();
	inputParam.sid 				= "getSalesLimitProdDetail";
	inputParam.url 				= "/product/getSalesLimitProdDetail.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	
	gfn_Transaction( inputParam );
}


//저장
function fn_save(){
	// 필수체크
	if(!gfn_validationForm($("#srchForm"))){
		return;
	}
	
	if(!gfn_validationDateTerm($('#VALID_START_DT'), $('#VALID_END_DT'))){
		return;
	}
	
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return false;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "saveSalesLimitProd";
	inputParam.url 				= "/product/saveSalesLimitProd.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	
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
	
	if(sid == "getSalesLimitProdDetail"){
		gfn_setDetails(result.ds_detail, $("#srchForm"));	// 지역내 상세 내용 셋업
	}
	else if(sid == "saveSalesLimitProd"){
		// 창을 닫던지. 부모 재조회를 하던지 
		alert('<spring:message code="success.common.save" />');
		self.close();
	}
}



</script>
</head>
<body class="popup">
<h1><spring:message code="word.salesLimit"/>&nbsp;<spring:message code="word.detail"/><a onClick="self.close();" class="close">X</a></h1>

<div class="content">
	<form id="srchForm" name="srchForm" method="post" onsubmit="return false;">
		<table cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="30%">
			<col width="70%">
			</colgroup>
			<tr>
				<th><spring:message code="word.prodNo"/></th>
				<td>
					<input type="text" name="PROD_NO" id="PROD_NO" value="<c:out value="${PROD_NO}"/>" disabled/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.salesLimitType"/></th>
				<td>
					<select id="SALES_LIMIT_TYPE" name="SALES_LIMIT_TYPE" style="width:100px" title="<spring:message code="word.salesLimitType"/>" depends="required">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="salesLimitType" items="${ds_salesLimitType}">
                            <option value="${salesLimitType.CD}">${salesLimitType.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.validDt"/></th>
				<td>
					<input type="text" style="width:100px" maxlength="10" readonly name="VALID_START_DT" id="VALID_START_DT" isDate="Y" class="datepicker" title="<spring:message code="word.startDate"/>" depends="required">
					 ~ <input type="text" style="width:100px" maxlength="10" readonly name="VALID_END_DT" id="VALID_END_DT" isDate="Y" class="datepicker" title="<spring:message code="word.endDate"/>" depends="required">
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.salesLimitCnt"/></th>
				<td>
			        <input type="text" name="SALES_LIMIT_CNT" id="SALES_LIMIT_CNT" isNum="Y" class="onlynum2" maxlength="5" title="<spring:message code="word.salesLimitCnt"/>" depends="required"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.soldOutYn"/></th>
				<td>
			        <select id="SOLD_OUT_YN" name="SOLD_OUT_YN" style="width:100px" title="<spring:message code="word.soldOutYn"/>" depends="required">
						<option value=""><spring:message code="word.select"/></option>
                        <option value="Y"><spring:message code="word.yes"/></option>
                        <option value="N"><spring:message code="word.no"/></option>
					</select>
				</td>
			</tr>
		</table>
		</form>
		<div class="btn_zone">
			<button type="button" onClick="javascript:fn_save()"><spring:message code="button.save"/></button>
	    </div>
</div>
</body>