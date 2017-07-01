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
	
	// 파트너 이상의 사람들이 이용할수 있음
	gfn_OnLoad();
	
	fn_init();
	
	fn_srch();
});

// 화면내 초기화 부분
function fn_init(){
	// 초기값셋팅
	$('#FROM_DT').val(gfn_addDay( gfn_addMonth( gfn_getToday(true), -1, true ), 1, true)); // 기간이 한달전
	$('#TO_DT').val(gfn_getToday(true));
	
	// 그리드에 소트 기능 추가한다.
	gfn_addGridSort("tb_list");
	
	// myParams 에 넘어온 값이 있으면 이전 검색조건 셋팅한다.
	//gfn_setMyParams();
	
	// 관리자가 아니면 자기가 관련된 내용만 수정할수 있음
	if(gfn_isAdmin()){
		$('#COMP_NM').attr("disabled", false);
		$('#COMP_ID').attr("disabled", false);
		$('#btnW_companyPop').attr("disabled", false);
	}
	else{
		// 관리자가 아니면 자기회사만 검색 가능하다.
		$("#COMP_ID").val(gfn_getSessionInfo("COMP_ID"))
		$("#COMP_NM").val(gfn_getSessionInfo("COMP_NM"))
	}
}

////////////////////////////////////////////////////////////////////////////////////
// 호출부분 정의
// 리스트 조회
function fn_srch(){
	// 기간 체크
	if(!gfn_DateCrossCheckAll("FROM_DT", "TO_DT"))					return false;	// 유효성 + 크로스체크만 체크
	
	// 파라미터 validation
	if(!gfn_validationForm($("#srchForm"))){
		return;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "selectSupplyRateList";
	inputParam.url 				= "/product/selectSupplyRateList.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	
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
	if(sid == "selectSupplyRateList"){
		var tbHiddenInfo = ["SEQ"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
		gfn_displayList(result.ds_list, "tb_list", tbHiddenInfo);
		gfn_displayTotCnt(result.totCnt);
		
		gfn_addRowClickEvent("tb_list", "fn_clickRow"); // ==>동일하다
		gfn_addPaging(result.pageInfo, 'gfn_clickPageNo');
	}
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
//뒤로 버튼
function fn_goBack(){
	var inputParam = gfn_makeInputData($("#myParams"));
	
	gfn_commonGo("/code/codeGroupList", inputParam, "Y");
}

//row click event
function fn_clickRow(pObj){
	fn_goDetail(pObj);
}

function fn_goDetail(rowObj){
	//debugger;
	//var rowObj = $(pObj).parent();
	var SEQ = $('input[name=SEQ]', rowObj).val();

	var inputParam				= {};
	inputParam.SEQ 				= SEQ;
	inputParam.MODE				= "DETAIL";
	
	gfn_commonGo("/product/supplyRateDetailPop", inputParam, "Y");
}

function fn_goCreate(){
	var inputParam				= {};
	inputParam.MODE				= "CREATE";
	inputParam.BIZ_ENTITY_ID	= SES_COMP_ID;
	inputParam.SES_USER_TYPE	= SES_USER_TYPE;
	
	gfn_commonGo("/product/supplyRateDetailPop", inputParam, "Y");
}

////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출
function go_CompanyPop(){
	var inputParam				= {};
	inputParam.POP_COMP_NM 	= $('#COMP_NM').val();
	inputParam.sid 	= "findCompany";

	gfn_commonGo("/user/companyFindListPop", inputParam, "Y");
}


////////////////////////////////////////////////////////////////////////////////////
function fn_companyNmChange() {
    $('#COMP_ID').val('');
}


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
		<h3><spring:message code="word.supplyRate"/></h3>
		<form id="myParams" name="myParams">
			<ppe:makeHidden var="${findParams}" filter="FIND_" />
			<ppe:makeHidden var="${findParams}" filter="FIND2_" />
		</form>
		<form id="srchForm" name="srchForm" method="post" action="<c:url value='/commCode/codeList.do'/>" onsubmit="return false;">
			<input type="hidden" name="pageNo" id="pageNo" value="1"/>
			<input type="hidden" name="EXCEL_YN" id="EXCEL_YN" />
			<input type="hidden" name="SORT_COL" id="SORT_COL" value="PROD_NO DESC"/>
			<input type="hidden" name="SORT_ACC" id="SORT_ACC" />
			<input type="hidden" name="SEARCH_CODE_GROUP_ID" id="SEARCH_CODE_GROUP_ID" value='<c:out value="${CODE_GROUP_ID}"/>'/>
		<div id="search">
			<dl>
				<dt>&nbsp;</dt>
				<dd>
					<label for="FROM_DT" id="SDT1"><spring:message code="word.startDate"/></label>
					<input type="text" size="8" name="FROM_DT" id="FROM_DT" class="datepicker">
					~
					<label for="TO_DT" id="SDT1"><spring:message code="word.endDate"/></label>
					<input type="text" size="8" name="TO_DT" id="TO_DT" class="datepicker"/>
					<label for="FR_CURRENCY" id="S2"><spring:message code="word.bizEntityId"/></label>
					<input type="text" name="COMP_NM" id="COMP_NM" maxlength="20" title="<spring:message code="word.bizEntityId"/>" depends="" onChange="fn_companyNmChange();" readOnly />
					<input type="text" name="COMP_ID" id="COMP_ID" maxlength="8" title="<spring:message code="word.bizEntityId"/>" depends="required" readOnly/>
					<label for="SEARCH_PROD_NO" id="S1"><spring:message code="word.prodNo"/></label>
					<input type="text" id="PROD_NO" name="PROD_NO" value="" title="<spring:message code="word.prodNo"/>" maxlength=20/>
					<label for="SEARCH_PROD_NM" id="S2"><spring:message code="word.prodNm"/></label>
					<input type="text" id="PROD_NM" name="PROD_NM" value="" title="<spring:message code="word.prodNm"/>" maxlength=20/>
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
		<table id="tb_list" summary="<spring:message code="word.codeList"/>">
			<caption>리스트</caption>
			<colgroup>
				<col width="5%"/>
				<col width="15%"/>
				<col width="20%"/>
				<col width="20%"/>
				<col width="20%"/>
				<col width="20%"/>
			</colgroup>
			<thead> 
				<tr>
					<th cid="SEQ" cClass="num" cType="NUM"><spring:message code="word.num"/></th>
					<th cid="BIZ_DT" alg="center" cType="DATE"><spring:message code="word.dateOfTransaction"/></th>
					<th cid="FR_CURRENCY" alg="center"><spring:message code="word.beforeCurrency"/></th>
					<th cid="TO_CURRENCY" alg="center"><spring:message code="word.afterCurrency"/></th>
					<th cid="BASIC_EXT_RATE" alg="right" cType="NUM"></th>
					<th cid="BIZ_EXT_RATE" alg="right" cType="NUM"></th>
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
			<div class="right"><button type="button" id="btnReg" onClick="javascript:fn_goCreate()"><spring:message code="button.create"/></button></div>
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