<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>
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
	if('<c:out value="${MODE}"/>' == 'DETAIL'){
		fn_srch();	// 상세조회
	}
});

// 화면내 초기화 부분
function fn_init(){
	// 체크박스 초기화
	//gfn_setCheck($('#C001001'));
	// select 박스 초기화
	//gfn_setSelect($('#SYS_CD'), 'C001002');
}

////////////////////////////////////////////////////////////////////////////////////
// 호출부분 정의
// 상세조회 조회
function fn_srch(){
	var inputParam = new Object();
	inputParam.sid 				= "getSupplyRateDetail";
	inputParam.url 				= "/product/getSupplyRateDetail.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	//inputParam.callback			= "fn_callBack";
	
	gfn_Transaction( inputParam );
}


////////////////////////////////////////////////////////////////////////////////////
// 콜백 함수
function fn_callBack(sid, result, data){
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	// fn_srch
	if(sid == "getSupplyRateDetail"){
		gfn_setDetails(result.ds_detail, $("#contents"));	// 지역내 상세 내용 셋업
	}else if(sid = "saveSupplyRate"){
		// 저장
		alert("<spring:message code="success.request.msg"/>");
		
		var inputParam = gfn_makeInputData($("#findForm"));
		gfn_commonGo("/product/supplyRateList", inputParam, "N");
	}
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
// 저장버튼클릭
function fn_goSave(){
	
	if(!gfn_validationForm($("#dataForm"))){
		return;
	}
	
	// 법인일 경우 법인등록번호, 법인명 필수체크
	if($('#BIZ_TYPE').val() == "2"){
		if($('#BIZ_LICENSE_NO').val() == ""){
			alert("<spring:message code="word.bizLicenseNo"/><spring:message code="common.required.msg"/>");
			$('#BIZ_LICENSE_NO').focus();
			return;
		}
		if($('#COMP_NM').val() == ""){
			alert("<spring:message code="word.compNm"/><spring:message code="common.required.msg"/>");
			$('#COMP_NM').focus();
			return;
		}
	}
	
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return false;
	}
	
	var inputParam = gfn_makeInputData($("#dataForm"));
	inputParam.sid 				= "saveSupplyRate";
	inputParam.url 				= "/product/saveSupplyRate.do";
	
	if('<c:out value="${MODE}"/>' == 'DETAIL'){
		$('#PROC_MODE').val("UPDATE");
	}else{
		$('#PROC_MODE').val("CREATE");
	}
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	gfn_Transaction( inputParam );
}

function goDelete(){
	if(!confirm("<spring:message code="common.delete.msg"/>")){
		return false;
	}
	
	var inputParam = gfn_makeInputData($("#dataForm"));
	inputParam.sid 				= "saveSupplyRate";
	inputParam.url 				= "/product/saveSupplyRate.do";
	$('#PROC_MODE').val("DELETE");
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( inputParam );
}

//목록 버튼
function fn_goBack(){
	var inputParam = gfn_makeInputData($("#findForm"));
	
	gfn_commonGo("/product/supplyRateList", inputParam, "N");
}

////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출
function go_sallerCompanyPop(){
	var inputParam				= {};
	inputParam.POP_COMP_NM 	= $('#BIZ_ENTITY_NM').val();
	inputParam.sid 	= "findSallerCompany";

	gfn_commonGo("/user/companyFindListPop", inputParam, "Y");
}

function go_buyerCompanyPop(){
	var inputParam				= {};
	inputParam.POP_COMP_NM 	= $('#BUYER_BIZ_ENTITY_NM').val();
	inputParam.sid 	= "findBuyerCompany";

	gfn_commonGo("/user/companyFindListPop", inputParam, "Y");
}

function go_prodPop(){
	var inputParam			= {};
	inputParam.POP_PROD_NM 	= $('#PROD_NM').val();
	inputParam.sid 	= "findProd";

	gfn_commonGo("/product/productFindListPop", inputParam, "Y");
}

//팝업콜백 함수
//팝업콜백 함수
function fn_popCallBack(sid, data){
	// fn_srch
	if(sid == "findSallerCompany"){
		$('#BIZ_ENTITY_ID').val(data.BIZ_ENTITY_ID);
		$('#BIZ_ENTITY_NM').val(data.BIZ_ENTITY_NM);
	}else if(sid == "findBuyerCompany"){
		$('#BUYER_BIZ_ENTITY_ID').val(data.BIZ_ENTITY_ID);
		$('#BUYER_BIZ_ENTITY_NM').val(data.BIZ_ENTITY_NM);
	}else if(sid == "findProd"){
		$('#PROD_NO').val(data.PROD_NO);
		$('#PROD_NM').val(data.PROD_NM);
	}
}
////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수
// 팝업 내용 변경시 초기화
function fn_sallerCompanyNmChange() {
    $('#BIZ_ENTITY_ID').val('');
}
function fn_buyerCompanyNmChange() {
    $('#BUYER_BIZ_ENTITY_ID').val('');
}


function fn_clearData(){
	
	gfn_clearData($("#contents"));
}

////////////////////////////////////////////////////////////////////////////////////
</script>

</head>
<body>
<!-- wrap -->
<div id="wrap">

<!--  header -->
<%@ include file="/layout/header.jsp"%>
<!--//  header --> 

<!-- container -->
<div id="container">
	<!-- menu -->
<%@ include file="/layout/menu_left.jsp"%>
	<!--// menu -->
  
	<div id="contents">
		<h3><spring:message code="word.supplyRateDetail"/></h3>
		<form id="findForm" name="findForm">
			<ppe:makeHidden var="${findParams}" filter="FIND_"/>
		</form>
		<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
			<input type="hidden" name="SEARCH_BIZ_ENTITY_ID" id="SEARCH_BIZ_ENTITY_ID" value='<c:out value="${BIZ_ENTITY_ID}"/>'/>
			<input type="hidden" name="SEARCH_PROD_NO" id="SEARCH_PROD_NO" value='<c:out value="${PROD_NO}"/>'/>
			<input type="hidden" name="SEARCH_BUYER_BIZ_ENTITY_ID" id="SEARCH_BUYER_BIZ_ENTITY_ID" value='<c:out value="${BUYER_BIZ_ENTITY_ID}"/>'/>
			<input type="hidden" name="MODE" id="MODE" value='<c:out value="${MODE}"/>'/>
			<input type="hidden" name="PROC_MODE" id="PROC_MODE" value=''/>
		<table summary="<spring:message code="word.supplyRateDetail"/>" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="12%">
			<col width="15%">
			<col width="20%">
			<col width="15%">
			<col width="38%">
			</colgroup>
	<c:choose>
		<c:when test="${MODE=='DETAIL'}">
			<tr>
				<th colspan="2"><spring:message code="word.sallerEntity"/></th>
				<td colspan="3">
					<input type="text" name="BIZ_ENTITY_NM" id="BIZ_ENTITY_NM" maxlength="20" title="<spring:message code="word.sallerEntity"/>" depends="" onChange="fn_sallerCompanyNmChange();" disabled/>
					<button type="button" id="btnW_companyPop" onClick="javascript:go_sallerCompanyPop()" disabled><spring:message code="button.search"/></button>
					<input type="text" name="BIZ_ENTITY_ID" id="BIZ_ENTITY_ID" maxlength="8" title="<spring:message code="word.sallerEntity"/>" depends="required" disabled/>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.prod"/></th>
				<td colspan="3">
					<input type="text" name="PROD_NM" id="PROD_NM" maxlength="20" title="<spring:message code="word.prod"/>" depends="" onChange="fn_prodNmChange();" disabled/>
					<button type="button" id="btnW_userPop" onClick="javascript:go_prodPop()" disabled><spring:message code="button.search"/></button>
					<input type="text" name="PROD_NO" id="PROD_NO" maxlength="20" title="<spring:message code="word.prod"/>" depends="required" disabled/>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.buyerEntity"/></th>
				<td colspan="3">
					<input type="text" name="BUYER_BIZ_ENTITY_NM" id="BUYER_BIZ_ENTITY_NM" maxlength="20" title="<spring:message code="word.buyerEntity"/>" depends="" onChange="fn_buyerCompanyNmChange();" disabled/>
					<button type="button" id="btnW_companyPop" onClick="javascript:go_buyerCompanyPop()" disabled><spring:message code="button.search"/></button>
					<input type="text" name="BUYER_BIZ_ENTITY_ID" id="BUYER_BIZ_ENTITY_ID" maxlength="8" title="<spring:message code="word.buyerEntity"/>" depends="required" disabled/>
				</td>
			</tr>
		</c:when>
		<c:when test="${MODE=='CREATE'}">
			<tr>
				<th colspan="2"><spring:message code="word.sallerEntity"/></th>
				<td colspan="3">
					<input type="text" name="BIZ_ENTITY_NM" id="BIZ_ENTITY_NM" maxlength="20" title="<spring:message code="word.sallerEntity"/>" depends="" onChange="fn_sallerCompanyNmChange();"/>
					<button type="button" id="btnW_companyPop" onClick="javascript:go_sallerCompanyPop()"><spring:message code="button.search"/></button>
					<input type="text" name="BIZ_ENTITY_ID" id="BIZ_ENTITY_ID" maxlength="8" title="<spring:message code="word.sallerEntity"/>" depends="required" readOnly/>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.prod"/></th>
				<td colspan="3">
					<input type="text" name="PROD_NM" id="PROD_NM" maxlength="20" title="<spring:message code="word.prod"/>" depends="" onChange="fn_prodNmChange();"/>
					<button type="button" id="btnW_userPop" onClick="javascript:go_prodPop()"><spring:message code="button.search"/></button>
					<input type="text" name="PROD_NO" id="PROD_NO" maxlength="20" title="<spring:message code="word.prod"/>" depends="required" readOnly/>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.buyerEntity"/></th>
				<td colspan="3">
					<input type="text" name="BUYER_BIZ_ENTITY_NM" id="BUYER_BIZ_ENTITY_NM" maxlength="20" title="<spring:message code="word.buyerEntity"/>" depends="" onChange="fn_buyerCompanyNmChange();"/>
					<button type="button" id="btnW_companyPop" onClick="javascript:go_buyerCompanyPop()"><spring:message code="button.search"/></button>
					<input type="text" name="BUYER_BIZ_ENTITY_ID" id="BUYER_BIZ_ENTITY_ID" maxlength="8" title="<spring:message code="word.buyerEntity"/>" depends="required" readOnly/>
				</td>
			</tr>
		</c:when>
	</c:choose>
			<tr>
				<th colspan="2"><spring:message code="word.validDt"/></th>
				<td colspan="3">
					<input type="text" style="width:100px" maxlength="10" readonly name="VALID_START_DT" id="VALID_START_DT" isDate="Y" class="datepicker" title="<spring:message code="cop.ntceBgnde"/>" depends="required">
					 ~ <input type="text" style="width:100px" maxlength="10" readonly name="VALID_END_DT" id=""VALID_END_DT"" isDate="Y" class="datepicker" title="<spring:message code="cop.ntceEndde"/>" depends="required">
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.currency"/></th>
				<td colspan="3">
			        <select id="CURRENCY" name="CURRENCY" title="<spring:message code="word.currency"/>" depends="required" style="width:100px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="supplyCurrencyList" items="${ds_supplyCurrencyList}">
                            <option value="${supplyCurrencyList.CD}">${supplyCurrencyList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.supplyPrice"/></th>
				<td>
			        <input type="text" name="PRICE" id="PRICE" isNum="Y" class="onlynum2" maxlength="12" style="width:80px" title="<spring:message code="word.supplyPrice"/>" depends=""/>
				</td>
				<th><spring:message code="word.supplyRate"/></th>
				<td>
					<input type="text" name="SUPPLY_RATE" id="SUPPLY_RATE" isNum="Y" class="onlynum2" maxlength="7" title="<spring:message code="word.supplyRate"/>" depends=""/>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.dcMinQy"/></th>
				<td colspan="3">
			        <input type="text" name="DC_MIN_QTY" id="DC_MIN_QTY" isNum="Y" class="onlynum2" maxlength="6" style="width:80px" title="<spring:message code="word.dcMinQy"/>" depends=""/>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.dcSupplyPrice"/></th>
				<td>
			        <input type="text" name="DC_SUPPLY_PRICE" id="DC_SUPPLY_PRICE" isNum="Y" class="onlynum2" maxlength="12" style="width:80px" title="<spring:message code="word.dcSupplyPrice"/>" depends=""/>
				</td>
				<th><spring:message code="word.dcSupplyRate"/></th>
				<td>
					<input type="text" name="DC_SUPPLY_RATE" id="DC_SUPPLY_RATE" isNum="Y" class="onlynum2" maxlength="7" title="<spring:message code="word.dcSupplyRate"/>" depends=""/>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.validYn"/></th>
				<td colspan="3">
			        <select id="VALID_YN" name="VALID_YN" title="<spring:message code="word.validYn"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
						<option value="Y"><spring:message code="word.yes"/></option>
						<option value="N"><spring:message code="word.no"/></option>
					</select>
				</td>
			</tr>
		</table>
		</form>
		
		<div class="btn_zone">
			<div class="left"><span id="preLink" onClick="javascript:fn_goBack()" title="<spring:message code="button.list"/>"></span></div>
		<c:choose>
			<c:when test="${MODE=='DETAIL'}">
			<div class="right"><button type="button" id="btnW_delete" onClick="javascript:goDelete()"><spring:message code="button.delete"/></button></div>
			</c:when>
			<c:when test="${MODE=='CREATE'}">
			<div class="right"><button type="button" id="btnW_clear" onClick="javascript:fn_clearData()"><spring:message code="button.clear"/></button></div>
			</c:when>
		</c:choose>
			<div class="right"><button type="button" id="btnW_save" onClick="javascript:fn_goSave()"><spring:message code="button.save"/></button></div>
		</div>

	</div> 
</div>


<!--  footer -->
<%@ include file="/layout/footer.jsp"%>
<!--  //footer -->
	
</div>
<!-- wrap -->
</body>
</html>