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
		
		dfn_init();
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
		
		if(!gfn_isNull(result.ds_detail.PROD_MCATE_CD)){
			fn_selectLCate(result.ds_detail.PROD_LCATE_CD, result.ds_detail.PROD_MCATE_CD);
		}
		if(!gfn_isNull(result.ds_detail.PROD_SCATE_CD)){
			fn_selectMCate(result.ds_detail.PROD_MCATE_CD, result.ds_detail.PROD_SCATE_CD);
		}
		if(!gfn_isNull(result.ds_detail.PROD_DCATE_CD)){
			fn_selectSCate(result.ds_detail.PROD_SCATE_CD, result.ds_detail.PROD_DCATE_CD);
		}
		
		for(var i = 0; i < result.ds_langNameList.length; i++){
			$('#PROD_NM_'+result.ds_langNameList[i].LANG_CD).val(result.ds_langNameList[i].PROD_NM);
			$('#PROD_SHORT_NM_'+result.ds_langNameList[i].LANG_CD).val(result.ds_langNameList[i].PROD_SHORT_NM);
			$('#PRODUCT_EXPL_TEXT_'+result.ds_langNameList[i].LANG_CD).val(result.ds_langNameList[i].PRODUCT_EXPL_TEXT);
		}
		
		if(!gfn_isNull(result.ds_detail)){
			if(!gfn_isNull(result.ds_detail.fileList)){
				$("#IMG_FIRST_INDEX").val(0);
				$("#IMG_LIST_LENGTH").val(result.ds_detail.fileList.length);
				g_fileList = result.ds_detail.fileList;
				dfn_initFileList();
				dfn_setFileList(result.ds_detail.fileList);// fileList 셋업
			}
			
		}
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
function go_CompanyPop(){
	var inputParam				= {};
	inputParam.POP_COMP_NM 	= $('#SELLER_BIZ_ENTITY_NM').val();
	inputParam.sid 	= "findCompany";

	gfn_commonGo("/user/companyFindListPop", inputParam, "Y");
}

//팝업콜백 함수
function fn_popCallBack(sid, data){
	// fn_srch
	if(sid == "findCompany"){
		$('#SELLER_BIZ_ENTITY_ID').val(data.BIZ_ENTITY_ID);
		$('#SELLER_BIZ_ENTITY_NM').val(data.BIZ_ENTITY_NM);
	}
}

//팝업_등록폼 호출하기
function fn_popInsertForm(){
	var inputParam = {}; //gfn_makeInputData($("#srchForm"));
	inputParam.PROD_NO = "<c:out value="${PROD_NO}"/>";
	
	gfn_commonGo("/product/productImgPop", inputParam, "Y");
}
////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수
// 팝업 내용 변경시 초기화
function fn_companyNmChange() {
    $('#BIZ_ENTITY_ID').val('');
}


function fn_clearData(){
	
	gfn_clearData($("#contents"));
}

//카테고리 대분류 select 코드 조회
function fn_selectLCate(cateCd, valueCateCd){
	$('#PROD_MCATE_CD').find('option').remove();
	$('#PROD_MCATE_CD').append("<option value=''><spring:message code="word.select"/></option>");
	$('#PROD_SCATE_CD').find('option').remove();
	$('#PROD_SCATE_CD').append("<option value=''><spring:message code="word.select"/></option>");
	$('#PROD_DCATE_CD').find('option').remove();
	$('#PROD_DCATE_CD').append("<option value=''><spring:message code="word.select"/></option>");
	
	if(gfn_isNull(cateCd)){
		return;
	}
	
	gfn_GetCategoryList(cateCd, $('#PROD_MCATE_CD'), '<spring:message code="word.select"/>', valueCateCd);
}

//카테고리 중분류 select 코드 조회
function fn_selectMCate(cateCd, valueCateCd){
	$('#PROD_SCATE_CD').find('option').remove();
	$('#PROD_SCATE_CD').append("<option value=''><spring:message code="word.select"/></option>");
	$('#PROD_DCATE_CD').find('option').remove();
	$('#PROD_DCATE_CD').append("<option value=''><spring:message code="word.select"/></option>");
	
	if(gfn_isNull(cateCd)){
		return;
	}
	
	gfn_GetCategoryList(cateCd, $('#PROD_SCATE_CD'), '<spring:message code="word.select"/>', valueCateCd);
}

//카테고리 소분류 select 코드 조회
function fn_selectSCate(cateCd, valueCateCd){
	$('#PROD_DCATE_CD').find('option').remove();
	$('#PROD_DCATE_CD').append("<option value=''><spring:message code="word.select"/></option>");
	
	if(gfn_isNull(cateCd)){
		return;
	}
	
	gfn_GetCategoryList(cateCd, $('#PROD_DCATE_CD'), '<spring:message code="word.select"/>', valueCateCd);
}


var g_fileList = null;

//화면내 초기화 부분
function dfn_init(){
	
	dfn_initFileList();
}

////////////////////////////////////////////////////////////////////////////////////
//사용자 함수
function dfn_initFileList(){
	imgNm = "IMG_VIEWLIST";
	imgTd = $("[name="+imgNm+"]");
	
	if(!gfn_isNull(imgTd))
		imgTd.empty();
	
}

function dfn_changeList(addIndex){
	var firstIdx = $("#IMG_FIRST_INDEX").val();		// 이미지파일 첫 번째 위치 
	var listLength = $("#IMG_LIST_LENGTH").val();	// 전체 이미지 파일 갯수
	var listSize = $("#IMG_LIST_SIZE").val();		// 한번에 보여줄수 있는 이미지 갯수
	
	if( (firstIdx*1 + addIndex*1) < 0)
		return;
	
	if(firstIdx*1 + addIndex*1 + listSize*1 > listLength)
		return;
	
	$("#IMG_FIRST_INDEX").val(firstIdx*1 + addIndex*1);	// 새로운 첫 이미지파일 위치
	
	dfn_initFileList();
	dfn_setFileList(g_fileList);
}

function dfn_setFileList(fileList){
	
	var imgNm = "";
	var imgTd = null;
	
	var firstIdx = $("#IMG_FIRST_INDEX").val();
	var listSize = $("#IMG_LIST_SIZE").val();
	
	if(!gfn_isNull(fileList)) // filelist가 있을 경우 file 리스트 표시
	{
		//for(var idx = 0; idx < fileList.length; idx++){
		for(var idx = firstIdx; idx < firstIdx + listSize && idx < fileList.length ; idx++){
			
			imgNm = "IMG_VIEWLIST";	// (대)상품보기용
			imgTd = $("[name="+imgNm+"]");
			
			var fileLink = "";
			fileLink += "<div name='FILE_INFO_" + fileList[idx].FILE_DTL_SEQ + "'>";
			fileLink += "<img src=\"" + fileList[idx].THUMBNAIL_URL_PATH + fileList[idx].SAVE_FILE_NAME + "\" onclick='javascript:gfn_changeImgView(\"IMG_VIEW\", \"" + gfn_replaceAll(fileList[idx].URL_PATH + fileList[idx].SAVE_FILE_NAME, "\\", "/") + "\")' style=\"width:20%\" height=\"50\"  border=\"0\"/>";
			fileLink += "<div>";

			imgTd.append(fileLink);
		}
		
		
		gfn_changeImgView("IMG_VIEW", gfn_replaceAll(fileList[firstIdx].URL_PATH + fileList[firstIdx].SAVE_FILE_NAME, "\\", "/"));
	}
}

function gfn_changeImgView(imgViewOjbNm, src){
	var imgViewObj = $("[name="+imgViewOjbNm+"]");
	
	imgViewObj.attr("src", src);
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
			<input type="hidden" name="IMG_FIRST_INDEX" id="IMG_FIRST_INDEX" value="0"/>
			<input type="hidden" name="IMG_LIST_LENGTH" id="IMG_LIST_LENGTH" value="0"/>
			<input type="hidden" name="IMG_LIST_SIZE" id="IMG_LIST_SIZE" value="4"/>
		<table summary="<spring:message code="word.productDetail"/>" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="50%">
			<col width="3">
			<col width="10%">
			<col width="10%">
			<col width="30%">
			</colgroup>
			<tr>
				<th rowspan="50">
					<!-- <form id="prodImgViewForm" name="prodImgViewForm" method="post" action="#" onsubmit="return false;"> -->
					<table summary="조회" cellspacing="0" border="0" class="tbl_list_type2">
						<colgroup>
						<col width="15%">
						<col width="15%">
						<col width="70%">
						</colgroup>
						<tr>
							<th colspan=2>카테고리순서</th>
							<td>
								<select id="IMG_TYPE_CD" name="IMG_TYPE_CD" title="이미지타입" onchange="javascript:dfn_selectImgListByImgType(this.value);">
									<c:forEach var="IMG_TYPE" items="${ds_cd_IMG_TYPE}">
										<option value="${IMG_TYPE.CD}">${IMG_TYPE.CD_NM}</option>
									</c:forEach>
								</select>		
							</td>
						</tr>
						<tr>
							<td colspan=3>
								<div style="width:200px;height:200">
									<img name="IMG_VIEW"></img>
								</div>
								<img src='/images/btn/icon_pre_month.gif' style='cursor:hand' onclick='javascript:dfn_changeList(-1)' />
								<div name="IMG_VIEWLIST" style="width:200px;height:400">
								</div>
								<img src='/images/btn/icon_aft_month.gif' style='cursor:hand' onclick='javascript:dfn_changeList(1)' />
							</td>
						</tr>
					</table>
				</th>
				<td rowspan="50"></td>
				<th colspan="2"><spring:message code="word.prodNo"/></th>
			<c:choose>
				<c:when test="${MODE=='DETAIL'}">
				<td>
					<input type="text" name="PROD_NO" id="PROD_NO" disabled />
					<button type="button" id="btnBack" onClick="javascript:fn_popInsertForm()">상품이미지등록</button>
				</td>
				</c:when>
				<c:when test="${MODE=='CREATE'}">
				<td>시스템 자동생성</td>
				</c:when>
			</c:choose>
			</tr>
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
			<tr>
				<th colspan="2"><spring:message code="word.orgPrice"/></th>
				<td>
			        <input type="text" name="ORG_PRICE" id="ORG_PRICE" isNum="Y" class="onlynum2" maxlength="12" style="width:80px" title="<spring:message code="word.orgPrice"/>" depends="required"/>
			        <select id="SUPPLY_CURRENCY" name="SUPPLY_CURRENCY" title="<spring:message code="word.supplyCurrency"/>" depends="required" style="width:100px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="supplyCurrencyList" items="${ds_supplyCurrencyList}">
                            <option value="${supplyCurrencyList.CD}">${supplyCurrencyList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
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
		</table>
		<table summary="<spring:message code="word.productDetail"/>" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="15%">
			<col width="15%">
			<col width="70%">
			</colgroup>
			<tr>
				<th colspan="2"><spring:message code="word.category"/></th>
				<td>
			        <select id="PROD_LCATE_CD" name="PROD_LCATE_CD" title="<spring:message code="word.Lcategory"/>" depends="required" style="width:140px" onchange="javascript:fn_selectLCate(this.value);">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="lCateList" items="${ds_lCateList}">
                            <option value="${lCateList.CATE_CODE}">${lCateList.CATE_NAME}</option>
                        </c:forEach>
					</select>
			        <select id="PROD_MCATE_CD" name="PROD_MCATE_CD" title="<spring:message code="word.Mcategory"/>" depends="required" style="width:140px" onchange="javascript:fn_selectMCate(this.value);">
						<option value=""><spring:message code="word.select"/></option>
					</select>
			        <select id="PROD_SCATE_CD" name="PROD_SCATE_CD" title="<spring:message code="word.Scategory"/>" depends="required" style="width:140px" onchange="javascript:fn_selectSCate(this.value);">
						<option value=""><spring:message code="word.select"/></option>
					</select>
			        <select id="PROD_DCATE_CD" name="PROD_DCATE_CD" title="<spring:message code="word.Dcategory"/>" depends="" style="width:140px">
						<option value=""><spring:message code="word.select"/></option>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.volume"/></th>
				<td>
			        <input type="text" name="VOLUME" id="VOLUME" isNum="Y" class="onlynum2" maxlength="8" title="<spring:message code="word.volume"/>" depends="required"/>
			        <select id="VOLUME_UNIT" name="VOLUME_UNIT" title="<spring:message code="word.volumeUnit"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="volumeUnitList" items="${ds_volumeUnitList}">
                            <option value="${volumeUnitList.CD}">${volumeUnitList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.weight"/></th>
				<td>
			        <input type="text" name="WEIGHT" id="WEIGHT" isNum="Y" class="onlynum2" maxlength="8" title="<spring:message code="word.weight"/>" depends="required"/>
			        <select id="WEIGHT_UNIT" name="WEIGHT_UNIT" title="<spring:message code="word.weightUnit"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="weightUnitList" items="${ds_weightUnitList}">
                            <option value="${weightUnitList.CD}">${weightUnitList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.salesSku"/></th>
				<td>
			        <select id="SALES_SKU" name="SALES_SKU" title="<spring:message code="word.salesSku"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="salesSkuList" items="${ds_salesSkuList}">
                            <option value="${salesSkuList.CD}">${salesSkuList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.perBoxProdCnt"/></th>
				<td>
			        <input type="text" name="PER_BOX_PROD_CNT" id="PER_BOX_PROD_CNT" isNum="Y" class="onlynum2" maxlength="6" title="<spring:message code="word.perBoxProdCnt"/>" depends="required"/>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.sellValidMonths"/></th>
				<td>
			        <input type="text" name="SELL_VALID_MONTHS" id="SELL_VALID_MONTHS" isNum="Y" class="onlynum2" maxlength="6" title="<spring:message code="word.sellValidMonths"/>" depends="required"/>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.seenCustomerYn"/></th>
				<td>
			        <select id="SEEN_CUSTOMER_YN" name="SEEN_CUSTOMER_YN" title="<spring:message code="word.seenCustomerYn"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
						<option value="Y"><spring:message code="word.yes"/></option>
						<option value="N"><spring:message code="word.no"/></option>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.seenPartnerYn"/></th>
				<td>
			        <select id="SEEN_PARTNER_YN" name="SEEN_PARTNER_YN" title="<spring:message code="word.seenPartnerYn"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
						<option value="Y"><spring:message code="word.yes"/></option>
						<option value="N"><spring:message code="word.no"/></option>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.workingYn"/></th>
				<td>
			        <select id="WORKING_YN" name="WORKING_YN" title="<spring:message code="word.workingYn"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
						<option value="Y"><spring:message code="word.yes"/></option>
						<option value="N"><spring:message code="word.no"/></option>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.sellerBizEntity"/></th>
				<td>
					<input type="text" name="SELLER_BIZ_ENTITY_NM" id="SELLER_BIZ_ENTITY_NM" maxlength="20" title="<spring:message code="word.bizEntityId"/>" depends="" onChange="fn_companyNmChange();"/>
					<button type="button" id="btnW_companyPop" onClick="javascript:go_CompanyPop()"><spring:message code="button.search"/></button>
					<input type="text" name="SELLER_BIZ_ENTITY_ID" id="SELLER_BIZ_ENTITY_ID" maxlength="8" title="<spring:message code="word.bizEntityId"/>" depends="required" readOnly/>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.dutyFreeYn"/></th>
				<td>
			        <select id="DUTY_FREE_YN" name="DUTY_FREE_YN" title="<spring:message code="word.dutyFreeYn"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
						<option value="Y"><spring:message code="word.yes"/></option>
						<option value="N"><spring:message code="word.no"/></option>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.makeCuntry"/></th>
				<td>
			        <select id="MAKE_COUNTRY" name="MAKE_COUNTRY" title="<spring:message code="word.makeCuntry"/>" depends="required" style="width:250px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="addrCountryList" items="${ds_addrCountryList}">
                            <option value="${addrCountryList.CD}">${addrCountryList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.supplyCountry"/></th>
				<td>
			        <select id="SUPPLY_COUNTRY" name="SUPPLY_COUNTRY" title="<spring:message code="word.supplyCountry"/>" depends="required" style="width:250px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="addrCountryList" items="${ds_addrCountryList}">
                            <option value="${addrCountryList.CD}">${addrCountryList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.salesDate"/></th>
				<td>
			        <input type="text" style="width:100px" maxlength="10" readonly name="SALES_START_DT" id="SALES_START_DT" isDate="Y" class="datepicker" title="<spring:message code="word.salesStartDate"/>" depends="required">
			        ~
			        <input type="text" style="width:100px" maxlength="10" readonly name="SALES_END_DT" id="SALES_END_DT" isDate="Y" class="datepicker" title="<spring:message code="word.salesEndDate"/>" depends="required">
				</td>
			</tr>
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
				<th colspan="2"><spring:message code="word.recommendYn"/></th>
				<td>
			        <select id="RECOMMEND_YN" name="RECOMMEND_YN" title="<spring:message code="word.recommendYn"/>" depends="required" style="width:150px">
						<option value="Y"><spring:message code="word.yes"/></option>
						<option value="N" selected><spring:message code="word.no"/></option>
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