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
	// 초기값셋팅
	$('#FROM_DT').val(gfn_addDay( gfn_addMonth( gfn_getToday(true), -1, true ), 1, true)); // 기간이 한달전
	$('#TO_DT').val(gfn_getToday(true));
	
	// 그리드에 소트 기능 추가한다.
	gfn_addGridSort("tb_list");
	
	// myParams 에 넘어온 값이 있으면 이전 검색조건 셋팅한다.
	//gfn_setMyParams();
	
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
	inputParam.sid 				= "selectExchangeRateList";
	inputParam.url 				= "/exchangeRate/selectExchangeRateList.do";
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
	if(sid == "selectExchangeRateList"){
		//var tbHiddenInfo = ["CODE_GROUP_ID", "CODE_ID"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
		gfn_displayList(result.ds_list, "tb_list", null);
		gfn_displayTotCnt(result.totCnt);
		
		gfn_addPaging(result.pageInfo, 'gfn_clickPageNo');
	}
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
//뒤로 버튼
function fn_goBack(){
	var inputParam = gfn_makeInputData($("#myParams"));
	
	gfn_commonGo("/code/codeGroupList", inputParam, "N");
}

function fn_goDetail(pObj){
	var rowObj = $(pObj).parent();
	var CODE_GROUP_ID = $('input[name=CODE_GROUP_ID]', rowObj).val();
	var CODE_ID = $('input[name=CODE_ID]', rowObj).val();

	var inputParam					= gfn_makeInputData($("#srchForm"), null, "FIND2_");
	var inputParam2					= gfn_makeInputData($("#myParams"), null, "FIND_");
	
	inputParam.FIND_SORT_COL 		= inputParam2.FIND_FIND_SORT_COL;
	inputParam.FIND_RETURNURL 		= inputParam2.FIND_FIND_RETURNURL;
	inputParam.FIND_pageNo 			= inputParam2.FIND_FIND_pageNo;
	inputParam.FIND_pageRowCnt 		= inputParam2.FIND_FIND_pageRowCnt;
	
	inputParam.FIND_SEARCH_USE_YN 	= inputParam2.FIND_FIND_SEARCH_USE_YN;
	inputParam.FIND_SEARCH_WORD 	= inputParam2.FIND_FIND_SEARCH_WORD;

	inputParam.CODE_GROUP_ID 	= CODE_GROUP_ID;
	inputParam.CODE_ID 			= CODE_ID;
	inputParam.MODE				= "DETAIL";
	
	//inputParam.LISTPARAMS = gfn_replaceAll($("#srchForm").serialize(), "&", "|");
	//inputParam.LISTURL = gfn_getListUrl();
	
	gfn_commonGo("/code/codeDetail", inputParam, "N");
}

function fn_goCreate(){
	var inputParam					= gfn_makeInputData($("#srchForm"), null, "FIND2_");
	var inputParam2					= gfn_makeInputData($("#myParams"), null, "FIND_");
	
	inputParam.FIND_SORT_COL 		= inputParam2.FIND_FIND_SORT_COL;
	inputParam.FIND_RETURNURL 		= inputParam2.FIND_FIND_RETURNURL;
	inputParam.FIND_pageNo 			= inputParam2.FIND_FIND_pageNo;
	inputParam.FIND_pageRowCnt 		= inputParam2.FIND_FIND_pageRowCnt;
	
	inputParam.FIND_SEARCH_USE_YN 	= inputParam2.FIND_FIND_SEARCH_USE_YN;
	inputParam.FIND_SEARCH_WORD 	= inputParam2.FIND_FIND_SEARCH_WORD;

	inputParam.CODE_GROUP_ID 	= "<c:out value="${CODE_GROUP_ID}"/>";
	inputParam.MODE				= "CREATE";
	//inputParam.LISTPARAMS = gfn_replaceAll($("#srchForm").serialize(), "&", "|");
	//inputParam.LISTURL = gfn_getListUrl();
	
	gfn_commonGo("/code/codeDetail", inputParam, "N");
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
		<h3><spring:message code="word.exchangeRate"/></h3>
		<form id="myParams" name="myParams">
			<ppe:makeHidden var="${findParams}" filter="FIND_" />
			<ppe:makeHidden var="${findParams}" filter="FIND2_" />
		</form>
		<form id="srchForm" name="srchForm" method="post" action="<c:url value='/commCode/codeList.do'/>" onsubmit="return false;">
			<input type="hidden" name="pageNo" id="pageNo" value="1"/>
			<input type="hidden" name="EXCEL_YN" id="EXCEL_YN" />
			<input type="hidden" name="SORT_COL" id="SORT_COL" value="SEQ DESC"/>
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
					<label for="FR_CURRENCY" id="S2"><spring:message code="word.beforeCurrency"/></label>
					<select id="FR_CURRENCY" name="FR_CURRENCY">
                        <option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="CURRENCY" items="${ds_cd_CURRENCY}">
                            <option value="${CURRENCY.CD}">${CURRENCY.CD_NM}</option>
                        </c:forEach>
					</select>
					<label for="TO_CURRENCY" id="S2"><spring:message code="word.afterCurrency"/></label>
					<select id="TO_CURRENCY" name="TO_CURRENCY">
                        <option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="CURRENCY" items="${ds_cd_CURRENCY}">
                            <option value="${CURRENCY.CD}">${CURRENCY.CD_NM}</option>
                        </c:forEach>
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
					<th cid="BIZ_DATE" alg="center" cType="DATE"><spring:message code="word.dateOfTransaction"/></th>
					<th cid="FR_CURRENCY" alg="center"><spring:message code="word.beforeCurrency"/></th>
					<th cid="TO_CURRENCY" alg="center"><spring:message code="word.afterCurrency"/></th>
					<th cid="BASIC_EXT_RATE" alg="right"><spring:message code="word.baseExchangeRate"/></th>
					<th cid="BIZ_EXT_RATE" alg="right"><spring:message code="word.ExchangeRate"/></th>
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
			<div class="left"><button type="button" id="preLink" onClick="javascript:fn_goBack()"><spring:message code="button.back"/></button></div>
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