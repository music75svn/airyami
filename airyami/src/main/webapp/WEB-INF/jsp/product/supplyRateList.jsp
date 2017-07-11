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
	inputParam.sid 				= "supplyRateList";
	inputParam.url 				= "/product/selectSupplyRateList.do";
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
	if(sid == "supplyRateList"){
		var tbHiddenInfo = ["BIZ_ENTITY_ID", "PROD_NO", "BUYER_BIZ_ENTITY_ID"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
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
	var BIZ_ENTITY_ID = $('input[name=BIZ_ENTITY_ID]', rowObj).val();
	var PROD_NO = $('input[name=PROD_NO]', rowObj).val();
	var BUYER_BIZ_ENTITY_ID = $('input[name=BUYER_BIZ_ENTITY_ID]', rowObj).val();

	var inputParam					= gfn_makeInputData($("#srchForm"), null, "FIND_");
	inputParam.BIZ_ENTITY_ID 		= BIZ_ENTITY_ID;
	inputParam.PROD_NO				= PROD_NO;
	inputParam.BUYER_BIZ_ENTITY_ID	= BUYER_BIZ_ENTITY_ID;
	inputParam.MODE					= "DETAIL";
	
	//inputParam.LISTPARAMS = gfn_replaceAll($("#srchForm").serialize(), "&", "|");
	//inputParam.LISTURL = gfn_getListUrl();
	
	gfn_commonGo("/product/supplyRateDetail", inputParam, "N");
}

function fn_goCreate(){
	var inputParam				= gfn_makeInputData($("#srchForm"), null, "FIND_");
	inputParam.MODE				= "CREATE";
	
	//inputParam.LISTPARAMS = gfn_replaceAll($("#srchForm").serialize(), "&", "|");
	//inputParam.LISTURL = gfn_getListUrl();
	
	gfn_commonGo("/product/supplyRateDetail", inputParam, "N");
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
		<h3><spring:message code="word.supplyRateList"/></h3>
		<form id="myParams" name="myParams">
			<ppe:makeHidden var="${findParams}" filter="FIND_" />
		</form>
		<form id="srchForm" name="srchForm" method="post" action="<c:url value='/product/supplyRateList.do'/>" onsubmit="return false;">
			<input type="hidden" name="pageNo" id="pageNo" value="1"/>
			<input type="hidden" name="EXCEL_YN" id="EXCEL_YN" />
			<input type="hidden" name="SORT_COL" id="SORT_COL" value="A.INSERT_DT DESC"/>
			<input type="hidden" name="SORT_ACC" id="SORT_ACC" />
<c:choose>
	<c:when test="${USER_ROLE=='ADM'}">
		<div id="search">
			<dl>
				<dt>&nbsp;</dt>
				<dd>
					<label for="SEARCH_PROD_NM" id="S1"><spring:message code="word.prodNm"/></label>
					<input type="text" id="SEARCH_PROD_NM" name="SEARCH_PROD_NM" value="" style="width:80px" title="<spring:message code="word.prodNm"/>" maxlength=20/>
					<label for="SEARCH_COMP_NM" id="S2"><spring:message code="word.compNm"/></label>
					<input type="text" id="SEARCH_COMP_NM" name="SEARCH_COMP_NM" value="" title="<spring:message code="word.compNm"/>" maxlength=20/>
					<label for="SEARCH_VALID_YN" id="S3"><spring:message code="word.validYn"/></label>
					<select id="SEARCH_VALID_YN" name="SEARCH_VALID_YN" style="width:150px">
						<option value=""><spring:message code="word.all"/></option>
						<option value="Y">Y</option>
						<option value="N">N</option>
					</select>
					<input type="submit" value="<spring:message code="button.search"/>" onclick="javascript:gfn_fn_srch(); return false;"/>
				</dd>
			</dl>
		</div>
	</c:when>
</c:choose>
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
		<table id="tb_list" summary="<spring:message code="word.supplyRateList"/>">
			<caption>리스트</caption>
			<colgroup>
				<col width="5%"/>
				<col width="15%"/>
				<col width=""/>
				<col width="15%"/>
				<col width="17%"/>
				<col width="8%"/>
				<col width="8%"/>
				<col width="6%"/>
			</colgroup>
			<thead>
				<tr>
					<th cid="ROWNUM" cClass="num" cType="NUM"><spring:message code="word.num"/></th>
					<th cid="BIZ_ENTITY_NM" alg="left"><spring:message code="word.sallerEntity"/></th>
					<th cid="PROD_NM" alg=""left""><spring:message code="word.prodNm"/></th>
					<th cid="BUYER_BIZ_ENTITY_NM" alg=""left""><spring:message code="word.buyerEntity"/></th>
					<th cid="VALID_DT" alg="center"><spring:message code="word.validDt"/></th>
					<th cid="CURRENCY_NM" alg="center"><spring:message code="word.currency"/></th>
					<th cid="SUPPLY_RATE" alg="center"><spring:message code="word.supplyRate"/></th>
					<th cid="PRICE" alg="right"><spring:message code="word.supplyPrice"/></th>
					<th cid="VALID_YN" alg="center"><spring:message code="word.validYn"/></th>
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