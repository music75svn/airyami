<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%@ include file="/include/admin_standard.jsp"%>

<script type="text/javascript">
var onload = true;
$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	//alert("LOGIN_ID " + '<c:out value="${LOGIN_ID}"/>');
	
	gfn_OnLoad();
	
	fn_init();
	
	fn_srch();
});

// 화면내 초기화 부분
function fn_init(){
	// 그리드에 소트 기능 추가한다.
	gfn_addGridSort("tb_list");
	
	// myParams 에 넘어온 값이 있으면 이전 검색조건 셋팅한다.
	gfn_setMyParams();
	fn_selectLCate('<c:out value="${FIND_SEARCH_PROD_LCATE_CD}"/>', '<c:out value="${FIND_SEARCH_PROD_MCATE_CD}"/>');
	fn_selectMCate('<c:out value="${FIND_SEARCH_PROD_MCATE_CD}"/>', '<c:out value="${FIND_SEARCH_PROD_SCATE_CD}"/>');
}

////////////////////////////////////////////////////////////////////////////////////
// 호출부분 정의
// 리스트 조회
function fn_srch(){
	
	// 파라미터 validation
	if(!gfn_validationForm($("#srchForm"))){
		return;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "productList";
	inputParam.url 				= "/product/selectProductList.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	//inputParam.callback			= fn_callBackA;
	
	gfn_Transaction( inputParam );
}

////////////////////////////////////////////////////////////////////////////////////
// 콜백 함수
function fn_callBack(sid, result){
	//debugger;
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	
	// fn_srch
	if(sid == "productList"){
		var tbHiddenInfo = ["PROD_NO"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
		gfn_displayList(result.ds_list, "tb_list", tbHiddenInfo);
		gfn_displayTotCnt(result.totCnt);
		
		gfn_addPaging(result.pageInfo, 'gfn_clickPageNo');
		
		gfn_addRowClickEvent("tb_list", "fn_clickRow"); // ==>동일하다
	}
	
	// fn_srch
	if(sid == "delCd"){
		alert(sid);
	}
	
}

//row click event
function fn_clickRow(pObj){
	fn_goDetail(pObj);
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
function fn_goDetail(rowObj){
	var PROD_NO = $('input[name=PROD_NO]', rowObj).val();

	var inputParam				= gfn_makeInputData($("#srchForm"), null, "FIND_");
	inputParam.PROD_NO 	        = PROD_NO;
	inputParam.MODE				= "DETAIL";
	
	//inputParam.LISTPARAMS = gfn_replaceAll($("#srchForm").serialize(), "&", "|");
	//inputParam.LISTURL = gfn_getListUrl();
	
	gfn_commonGo("/product/productDetail", inputParam, "N");
}

function fn_goCreate(){
	var inputParam				= gfn_makeInputData($("#srchForm"), null, "FIND_");
	inputParam.MODE				= "CREATE";
	
	//inputParam.LISTPARAMS = gfn_replaceAll($("#srchForm").serialize(), "&", "|");
	//inputParam.LISTURL = gfn_getListUrl();
	
	gfn_commonGo("/product/productDetail", inputParam, "N");
}

//카테고리 대분류 select 코드 조회
function fn_selectLCate(cateCd, valueCateCd){
	$('#SEARCH_PROD_MCATE_CD').find('option').remove();
	$('#SEARCH_PROD_MCATE_CD').append("<option value=''><spring:message code="word.all"/></option>");
	$('#SEARCH_PROD_SCATE_CD').find('option').remove();
	$('#SEARCH_PROD_SCATE_CD').append("<option value=''><spring:message code="word.all"/></option>");
	
	if(gfn_isNull(cateCd)){
		return;
	}
	
	gfn_GetCategoryList(cateCd, $('#SEARCH_PROD_MCATE_CD'), '<spring:message code="word.all"/>', valueCateCd);
}

//카테고리 중분류 select 코드 조회
function fn_selectMCate(cateCd, valueCateCd){
	$('#SEARCH_PROD_SCATE_CD').find('option').remove();
	$('#SEARCH_PROD_SCATE_CD').append("<option value=''><spring:message code="word.all"/></option>");
	
	if(gfn_isNull(cateCd)){
		return;
	}
	
	gfn_GetCategoryList(cateCd, $('#SEARCH_PROD_SCATE_CD'), '<spring:message code="word.all"/>', valueCateCd);
}
////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출

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
		<h3><spring:message code="word.productList"/></h3>
		<form id="myParams" name="myParams">
			<ppe:makeHidden var="${findParams}" filter="FIND_" />
		</form>
		<form id="srchForm" name="srchForm" method="post" action="<c:url value='/commCode/codeList.do'/>" onsubmit="return false;">
			<input type="hidden" name="pageNo" id="pageNo" value="1"/>
			<input type="hidden" name="EXCEL_YN" id="EXCEL_YN" />
			<input type="hidden" name="SORT_COL" id="SORT_COL" value="A.INSERT_DT DESC"/>
			<input type="hidden" name="SORT_ACC" id="SORT_ACC" />
		<div id="search">
			<dl>
				<dt>&nbsp;</dt>
				<dd>
					<label for="SEARCH_PROD_NO" id="S1"><spring:message code="word.prodNo"/></label>
					<input type="text" id="SEARCH_PROD_NO" name="SEARCH_PROD_NO" value="" title="<spring:message code="word.prodNo"/>" maxlength=20/>
					<label for="SEARCH_PROD_NM" id="S2"><spring:message code="word.prodNm"/></label>
					<input type="text" id="SEARCH_PROD_NM" name="SEARCH_PROD_NM" value="" title="<spring:message code="word.prodNm"/>" maxlength=20/>
					<label for="SEARCH_BRAND_CD" id="S3"><spring:message code="word.brand"/></label>
					<select id="SEARCH_BRAND_CD" name="SEARCH_BRAND_CD" style="width:150px">
						<option value=""><spring:message code="word.all"/></option>
                        <c:forEach var="brandList" items="${ds_brandList}">
                            <option value="${brandList.CD}">${brandList.CD_NM}</option>
                        </c:forEach>
					</select><br>
					<label for="SEARCH_BRAND_CD" id="S4"><spring:message code="word.category"/></label>
			        <select id="SEARCH_PROD_LCATE_CD" name="SEARCH_PROD_LCATE_CD" title="<spring:message code="word.Lcategory"/>" depends="" style="width:180px" onchange="javascript:fn_selectLCate(this.value);">
						<option value=""><spring:message code="word.all"/></option>
                        <c:forEach var="lCateList" items="${ds_lCateList}">
                            <option value="${lCateList.CATE_CODE}">${lCateList.CATE_NAME}</option>
                        </c:forEach>
					</select>
			        <select id="SEARCH_PROD_MCATE_CD" name="SEARCH_PROD_MCATE_CD" title="<spring:message code="word.Mcategory"/>" depends="" style="width:180px" onchange="javascript:fn_selectMCate(this.value);">
						<option value=""><spring:message code="word.all"/></option>
					</select>
			        <select id="SEARCH_PROD_SCATE_CD" name="SEARCH_PROD_SCATE_CD" title="<spring:message code="word.Scategory"/>" depends="" style="width:180px">
						<option value=""><spring:message code="word.all"/></option>
					</select>
					<input type="submit" value="<spring:message code="button.search"/>" onclick="javascript:gfn_fn_srch(); return false;"/>
				</dd>
			</dl>
		</div>
		<div class="tableInfoArea">
			<!-- total 총건수 -->
	        <span id="totCnt"></span>
		    <!--// total 총건수 -->
			<!-- 출력건수 -->
			<div class="tablelistQuantity" id="pageUnit"></div>
			<!--// 출력건수 -->
		</div>
		<!-- //출력건수및 리스트 페이지당 갯수 -->
		</form>
		
		<!-- table list -->
		<div class="aTypeListTbl">
		<table id="tb_list" summary="<spring:message code="word.productList"/>">
			<caption>리스트</caption>
			<colgroup>
				<col width="5%"/>
				<col width="8%"/>
				<col width="20%"/>
				<col width="15%"/>
				<col width=""/>
				<col width="12%"/>
				<col width="8%"/>
			</colgroup>
			<thead>
				<tr>
					<th cid="ROWNUM" cClass="num" cType="NUM"><spring:message code="word.num"/></th>
					<th cid="PROD_NO" alg="center"><spring:message code="word.prodNo"/></th>
					<th cid="PROD_NM" alg="left"><spring:message code="word.prodNm"/></th>
					<th cid="BRAND_NM" alg="left"><spring:message code="word.brand"/></th>
					<th cid="CATEGORY_NM" alg="left"><spring:message code="word.category"/></th>
					<th cid="ORG_PRICE" alg="right" cType="NUM"><spring:message code="word.orgPrice"/></th>
					<th cid="SUPPLY_CURRENCY_NM" alg="center"><spring:message code="word.supplyCurrency"/></th>
				</tr> 
			</thead> 
			<tbody>
				<tr></tr>
			</tbody>
		</table>
		</div>
		<!--// table list -->
		<span id="pagingNav"></span>
		
		<div class="btn_zone">
			<button type="button" id="btnReg" onClick="javascript:fn_goCreate()"><spring:message code="button.create"/></button>
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