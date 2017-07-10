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
	inputParam.sid 				= "getDispProdOnMainDetail";
	inputParam.url 				= "/product/getDispProdOnMainDetail.do";
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
	if(sid == "getDispProdOnMainDetail"){
		gfn_setDetails(result.ds_detail, $("#contents"));	// 지역내 상세 내용 셋업
	}else if(sid = "saveDispProdOnMain"){
		// 저장
		alert("<spring:message code="success.request.msg"/>");
		
		var inputParam = gfn_makeInputData($("#findForm"));
		gfn_commonGo("/product/dispProdOnMainList", inputParam, "N");
	}
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
// 저장버튼클릭
function fn_goSave(){
	
	if(!gfn_validationForm($("#dataForm"))){
		return;
	}
	
	if(!gfn_validationDateTerm($('#VALID_START_DT'), $('#VALID_END_DT'))){
		return;
	}
	
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return false;
	}
	
	var inputParam = gfn_makeInputData($("#dataForm"));
	inputParam.sid 				= "saveDispProdOnMain";
	inputParam.url 				= "/product/saveDispProdOnMain.do";
	
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
	inputParam.sid 				= "saveDispProdOnMain";
	inputParam.url 				= "/product/saveDispProdOnMain.do";
	$('#PROC_MODE').val("DELETE");
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( inputParam );
}

//목록 버튼
function fn_goBack(){
	var inputParam = gfn_makeInputData($("#findForm"));
	
	gfn_commonGo("/product/dispProdOnMainList", inputParam, "N");
}

////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출
function go_ProdPop(){
	var inputParam			= {};
	inputParam.POP_PROD_NM 	= $('#PROD_NM').val();
	inputParam.sid 	= "findProd";

	gfn_commonGo("/product/productFindListPop", inputParam, "Y");
}

//팝업콜백 함수
function fn_popCallBack(sid, data){
	// fn_srch
	if(sid == "findProd"){
		$('#PROD_NO').val(data.PROD_NO);
		$('#PROD_NM').val(data.PROD_NM);
	}
}
////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수
// 팝업 내용 변경시 초기화
function fn_prodNmChange() {
    $('#PROD_NO').val('');
}


function fn_clearData(){
	
	gfn_clearData($("#contents"));
}

//date term validation 
function gfn_validationDateTerm(fromObj, toObj){
	if(fromObj.val() != '' && toObj.val() != ''){
		if(fromObj.val() > toObj.val()){
			alert('<spring:message code="errors.dateTerm"/>');
			fromObj.focus();
			return false;
		}
	}
	
	return true;
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
		<h3><spring:message code="word.dispProdOnMainDetail"/></h3>
		<form id="findForm" name="findForm">
			<ppe:makeHidden var="${findParams}" filter="FIND_"/>
		</form>
		<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
			<input type="hidden" name="SEARCH_DISPLAY_TYPE_CD" id="SEARCH_DISPLAY_TYPE_CD" value='<c:out value="${DISPLAY_TYPE_CD}"/>'/>
			<input type="hidden" name="SEARCH_PROD_NO" id="SEARCH_PROD_NO" value='<c:out value="${PROD_NO}"/>'/>
			<input type="hidden" name="MODE" id="MODE" value='<c:out value="${MODE}"/>'/>
			<input type="hidden" name="PROC_MODE" id="PROC_MODE" value=''/>
		<table summary="<spring:message code="word.dispProdOnMainDetail"/>" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="12%">
			<col width="15%">
			<col width="20%">
			<col width="15%">
			<col width="38%">
			</colgroup>
			<tr>
				<th colspan="2"><spring:message code="word.displayType"/></th>
			<c:choose>
				<c:when test="${MODE=='DETAIL'}">
				<td colspan="3">
					<select id="DISPLAY_TYPE_CD" name="DISPLAY_TYPE_CD" title="<spring:message code="word.displayType"/>" depends="required" disabled style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="displayTypeList" items="${ds_displayTypeList}">
                            <option value="${displayTypeList.CD}">${displayTypeList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
				</c:when>
				<c:when test="${MODE=='CREATE'}">
				<td colspan="3">
					<select id="DISPLAY_TYPE_CD" name="DISPLAY_TYPE_CD" title="<spring:message code="word.displayType"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="displayTypeList" items="${ds_displayTypeList}">
                            <option value="${displayTypeList.CD}">${displayTypeList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
				</c:when>
			</c:choose>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.dispProd"/></th>
				<td colspan="3">
			<c:choose>
				<c:when test="${MODE=='DETAIL'}">
					<input type="text" name="PROD_NM" id="PROD_NM" maxlength="20" title="<spring:message code="word.dispProd"/>" depends="" onChange="fn_prodNmChange();" disabled/>
					<button type="button" id="btnW_userPop" onClick="javascript:go_ProdPop()" disabled><spring:message code="button.search"/></button>
					<input type="text" name="PROD_NO" id="PROD_NO" maxlength="20" title="<spring:message code="word.dispProd"/>" depends="required" disabled/>

				</c:when>
				<c:when test="${MODE=='CREATE'}">
					<input type="text" name="PROD_NM" id="PROD_NM" maxlength="20" title="<spring:message code="word.dispProd"/>" depends="" onChange="fn_prodNmChange();"/>
					<button type="button" id="btnW_userPop" onClick="javascript:go_ProdPop()"><spring:message code="button.search"/></button>
					<input type="text" name="PROD_NO" id="PROD_NO" maxlength="20" title="<spring:message code="word.dispProd"/>" depends="required" readOnly/>

				</c:when>
			</c:choose>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.validDt"/></th>
				<td colspan="3">
					<input type="text" style="width:100px" maxlength="10" readonly name="VALID_START_DT" id="VALID_START_DT" isDate="Y" class="datepicker" title="<spring:message code="cop.ntceBgnde"/>" depends="required">
					 ~ <input type="text" style="width:100px" maxlength="10" readonly name="VALID_END_DT" id="VALID_END_DT" isDate="Y" class="datepicker" title="<spring:message code="cop.ntceEndde"/>" depends="required">
				</td>
			</tr>
		</table>
		</form>
		<br></br><br></br><br></br><br></br><br></br>
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