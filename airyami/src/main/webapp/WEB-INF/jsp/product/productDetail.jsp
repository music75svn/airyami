<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
	inputParam.sid 				= "getProductDetail";
	inputParam.url 				= "/product/getProductDetail.do";
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
	if(sid == "getProductDetail"){
		gfn_setDetails(result.ds_detail, $("#contents"));	// 지역내 상세 내용 셋업
	}else if(sid = "saveProduct"){
		// 저장
		alert("<spring:message code="success.request.msg"/>");
		
		var inputParam = gfn_makeInputData($("#findForm"));
		gfn_commonGo("/product/productList", inputParam, "N");
	}
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
// 저장버튼클릭
function fn_goSave(){
	
	if(!gfn_validationForm($("#dataForm"))){
		return;
	}
<c:choose>
	<c:when test="${MODE=='CREATE'}">
		if($('#USER_PSWD').val() != $('#USER_PSWD_CONFIRM').val()){
			alert("비밀번호와 비밀번호확인이 틀립니다.");
			return;
		}
	</c:when>
</c:choose>
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return false;
	}
	
	var inputParam = gfn_makeInputData($("#dataForm"));
	inputParam.sid 				= "saveProduct";
	inputParam.url 				= "/product/saveProduct.do";
	
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
	inputParam.sid 				= "saveProduct";
	inputParam.url 				= "/product/saveProduct.do";
	$('#PROC_MODE').val("DELETE");
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( inputParam );
}

//목록 버튼
function fn_goBack(){
	var inputParam = gfn_makeInputData($("#findForm"));
	
	gfn_commonGo("/product/productList", inputParam, "N");
}

////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출


////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수
// 팝업 내용 변경시 초기화
function fn_companyNmChange() {
    $('#BIZ_ENTITY_ID').val('');
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
		<h3><spring:message code="word.productDetail"/></h3>
		<form id="findForm" name="findForm">
			<ppe:makeHidden var="${findParams}" filter="FIND_"/>
		</form>
		<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
			<input type="hidden" name="SEARCH_PROD_NO" id="SEARCH_PROD_NO" value='<c:out value="${PROD_NO}"/>'/>
			<input type="hidden" name="MODE" id="MODE" value='<c:out value="${MODE}"/>'/>
			<input type="hidden" name="PROC_MODE" id="PROC_MODE" value=''/>
		<table summary="<spring:message code="word.productDetail"/>" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="15%">
			<col width="15%">
			<col width="70%">
			</colgroup>
			<tr>
				<th colspan="2"><spring:message code="word.prodNo"/></th>
			<c:choose>
				<c:when test="${MODE=='DETAIL'}">
				<td><input type="text" name="PROD_ID" id="PROD_ID" disabled /></td>
				</c:when>
				<c:when test="${MODE=='CREATE'}">
				<td>시스템 자동생성</td>
				</c:when>
			</c:choose>
			</tr>
			<tr>
<c:set var="listSize" value="${fn:length(ds_cd_LANG)}" />
				<th rowspan="${listSize+1}"><spring:message code="word.prodNm"/></th>
			</tr>
<c:forEach var="LANG" items="${ds_cd_LANG}">
			<tr>
				<th>${LANG.CD_NM}</th>
				<td>
					<input type="text" name="PROD_NM_${LANG.CD}" id="PROD_NM_${LANG.CD}" maxlength="100" style="width:600px" value="${NAMELIST.CODE_NM}" title="<spring:message code="word.prodNm"/>" depends="required"/>
				</td>
			</tr>
</c:forEach>
			<tr>
<c:set var="listSize" value="${fn:length(ds_cd_LANG)}" />
				<th rowspan="${listSize+1}"><spring:message code="word.prodShortNm"/></th>
			</tr>
<c:forEach var="LANG" items="${ds_cd_LANG}">
			<tr>
				<th>${LANG.CD_NM}</th>
				<td>
					<input type="text" name="PROD_SHORT_NM_${LANG.CD}" id="PROD_SHORT_NM_${LANG.CD}" maxlength="60" style="width:500px" value="${NAMELIST.CODE_NM}" title="<spring:message code="word.prodShortNm"/>" depends="required"/>
				</td>
			</tr>
</c:forEach>
			<tr>
<c:set var="listSize" value="${fn:length(ds_cd_LANG)}" />
				<th rowspan="${listSize+1}"><spring:message code="word.prodexplText"/></th>
			</tr>
<c:forEach var="LANG" items="${ds_cd_LANG}">
			<tr>
				<th>${LANG.CD_NM}</th>
				<td>
					<textarea name="PRODUCT_EXPL_TEXT_${LANG.CD}" id="PRODUCT_EXPL_TEXT_${LANG.CD}" rows="5" cols="180" depends="required"></textarea>
				</td>
			</tr>
</c:forEach>
			<tr>
				<th colspan="2"><spring:message code="word.brand"/></th>
				<td>
			        <select id="BRAND_CD" name="BRAND_CD" title="<spring:message code="word.brand"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="brandList" items="${ds_brandList}">
                            <option value="${brandList.CD}">${brandList.CD_NM}</option>
                        </c:forEach>
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