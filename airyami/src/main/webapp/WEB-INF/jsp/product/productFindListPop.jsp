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
	$("#SEARCH_PROD_NM").val($("#POP_PROD_NM").val());
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
	inputParam.sid 				= "dispProdOnMainList";
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
	if(sid == "dispProdOnMainList"){
		var tbHiddenInfo = ["PROD_NO", "PROD_NM"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
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
function fn_clickRow(rowObj){
	var PROD_NO = $('input[name=PROD_NO]', rowObj).val();
	var PROD_NM = $('input[name=PROD_NM]', rowObj).val();

	var inputParam		= {};
	inputParam.PROD_NO 	= PROD_NO;
	inputParam.PROD_NM 	= PROD_NM;
	inputParam.sid				= $("#sid").val();
	
	opener.fn_popCallBack(inputParam.sid, inputParam);
	
	self.close();
}

</script>
</head>
<body class="popup">
<h1><spring:message code="word.productList"/>&nbsp;<a onClick="self.close();" class="close">X</a></h1>
	<div class="content">
		<form id="myParams" name="myParams">
			<ppe:makeHidden var="${findParams}" filter="FIND_" />
		</form>
		<form id="srchForm" name="srchForm" method="post" action="<c:url value='/commCode/codeList.do'/>" onsubmit="return false;">
			<input type="hidden" name="pageNo" id="pageNo" value="1"/>
			<input type="hidden" name="EXCEL_YN" id="EXCEL_YN" />
			<input type="hidden" name="SORT_COL" id="SORT_COL" value="A.INSERT_DT DESC"/>
			<input type="hidden" name="SORT_ACC" id="SORT_ACC" />
			<input type="hidden" name="SEARCH_POP_YN" id="Y" />
			<input type="hidden" name="POP_PROD_NM" id="POP_PROD_NM" value='<c:out value="${POP_PROD_NM}"/>'/>
			<input type="hidden" name="sid" id="sid" value='<c:out value="${sid}"/>'/>
		<div id="search" style="width:100%">
			<dl>
				<dt>&nbsp;</dt>
				<dd>
					<label for="SEARCH_PROD_NO" id="S1"><spring:message code="word.prodNo"/></label>
					<input type="text" id="SEARCH_PROD_NO" name="SEARCH_PROD_NO" value="" title="<spring:message code="word.prodNo"/>" maxlength=20/>
					<label for="SEARCH_PROD_NM" id="S2"><spring:message code="word.prodNm"/></label>
					<input type="text" id="SEARCH_PROD_NM" name="SEARCH_PROD_NM" value="" title="<spring:message code="word.prodNm"/>" maxlength=20/>
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
				<col width="20%"/>
				<col width=""/>
			</colgroup>
			<thead>
				<tr>
					<th cid="ROWNUM" cClass="num" cType="NUM"><spring:message code="word.num"/></th>
					<th cid="PROD_NO" alg="center"><spring:message code="word.prodNo"/></th>
					<th cid="PROD_NM" alg="left"><spring:message code="word.prodNm"/></th>
				</tr> 
			</thead> 
			<tbody>
				<tr></tr>
			</tbody>
		</table>
		</div>
		<!--// table list -->
		<span id="pagingNav"></span>
</body>