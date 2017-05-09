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
	inputParam.sid 				= "codeList";
	inputParam.url 				= "/code/selectCodeGroupList.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	//inputParam.callback			= fn_callBackA;
	
	gfn_Transaction( inputParam );
}

//연동 select 코드 조회
function fn_selectLink(code){
	if(gfn_isNull(code)){
		return;
	}
	
	gfn_GetCodeList(code, $('#SUB_CD'));
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
	if(sid == "codeList"){
		var tbHiddenInfo = ["CODE_GROUP_ID", "CD","RULE_CD"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
		gfn_displayList(result.ds_list, "tb_list", tbHiddenInfo);
		gfn_displayTotCnt(result.totCnt);
		
		gfn_addPaging(result.pageInfo, 'gfn_clickPageNo');
		
		
		//gfn_addRowClickEvent("tb_list");
		//gfn_addRowClickEvent("tb_list", "fn_clickRow"); // ==>동일하다
	}
	
	// fn_srch
	if(sid == "delCd"){
		alert(sid);
	}
	
}

function fn_callBackA(sid, result, data){
	alert("fn_callBackA!!!");
}

// 공통코드 전역콜백
function fn_callbackGetCodeList(code, mycombo, codeList){
	if(onload){
		
	}
}

//사용자 찾기 팝업 callback
function fn_CallbackPopupMember(inputParam){
	$('#USER_ID').val(inputParam.code);
	$('#USER_NM').val(inputParam.name);
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
function fn_callPopup(pObj){
	var rowObj = $(pObj).parent();
	var CODE_GROUP_ID = $('input[name=CODE_GROUP_ID]', rowObj).val();
	
	// 현재로우의 위치 알기
	var index = $("#tb_list > tbody > tr").index(rowObj);
	var selObj = $("#tb_list > tbody > tr").eq(index);
	
	
	var inputParam = new Object();
	inputParam.CODE_GROUP_ID = CODE_GROUP_ID;
	
	gfn_commonGo("/template/template_popup", inputParam, "Y");
}

function fn_goDetail(pObj){
	var rowObj = $(pObj).parent();
	var CODE_GROUP_ID = $('input[name=CODE_GROUP_ID]', rowObj).val();
	var CODE_ID = $('input[name=CD]', rowObj).val();
	
	var inputParam				= gfn_makeInputData($("#srchForm"), null, "SEARCH_");
	inputParam.CODE_GROUP_ID 	= CODE_GROUP_ID;
	inputParam.CODE_ID 			= CODE_ID;
	
	//inputParam.LISTPARAMS = gfn_replaceAll($("#srchForm").serialize(), "&", "|");
	//inputParam.LISTURL = gfn_getListUrl();
	
	gfn_commonGo("/template/templateView", inputParam, "N");
}

// 그리드 버튼
function fn_btnTest(pObj){
	alert("delete");
}


// 팝업_등록폼 호출하기
function fn_popInsertForm(){
	var inputParam = {}; //gfn_makeInputData($("#srchForm"));
	
	
	gfn_commonGo("/template/templateForm_popup", inputParam, "Y");
}


//엑셀버튼
function fn_excel() {
    var url = "/commCode/codeList";
    var inputParam = new Object();
    
    inputParam 				= gfn_makeInputData($("#srchForm"), inputParam);
    inputParam.EXCEL_YN   	= 'Y';
    inputParam.SEARCH_CONDITION	= gfn_makeSearchCondition($("#search")); 
    
    gfn_excelDownload(url, inputParam);
}


// 라디오 테스트 버튼
function fn_radioTest(){
	var r = gfn_getRadioOk('tb_list');
	alert(r);
}

// 테이블 정보 읽어오기
function fn_getTable(){
	var tbID = "tb_list";
	var rowDatas = gfn_getTableData(tbID);
	
	
	var datas = ""; 
	var inputParam2 = new Object();
	var inputParam = new Object();
	inputParam.sid 				= "codeList";
	inputParam.url 				= "/commCode/codeList.do";
	inputParam.data				= gfn_makeInputData($("#srchForm"), inputParam2);
	inputParam.data.rowDatas    = rowDatas;
	//inputParam.data				= {rowDatas : rowDatas};
	
	gfn_Transaction( inputParam );
}


// row click event
function fn_clickRow(pObj){
	var inputParam = new Object();
    inputParam.CODE_GROUP_ID = $('input[name=CODE_GROUP_ID]', pObj).val();
    
    $('input[name=radio]', pObj).attr("checked", "checked");
    
    alert(inputParam.CODE_GROUP_ID);
}


////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출

//사용자 찾기 팝업
function fn_popMember(){
	var inputParam = new Object();
	inputParam.USER_NM = $('#USER_NM').val();
	
	gfn_commonGo("/POP/POP0011P", inputParam, "Y");
}



////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수
// 팝업 내용 변경시 초기화
function fn_userNmChange() {
    $('#USER_ID').val('');
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
		<h3>코드그룹 목록</h3>
		<form id="myParams" name="myParams">
			<ppe:makeHidden var="${findParams}" filter="SEARCH_" />
		</form>
		<form id="srchForm" name="srchForm" method="post" action="<c:url value='/commCode/codeList.do'/>" onsubmit="return false;">
			<input type="hidden" name="pageNo" id="pageNo" value="1"/>
			<input type="hidden" name="EXCEL_YN" id="EXCEL_YN" />
			<input type="hidden" name="SORT_COL" id="SORT_COL" value="CODE_GROUP_ID ASC"/>
			<input type="hidden" name="SORT_ACC" id="SORT_ACC" />
		<div id="search">
			<dl>
				<dt>&nbsp;</dt>
				<dd>
					<label for="SEARCH_WORD" id="S2">그룹명</label>
					<input type="text" id="SEARCH_WORD" name="SEARCH_WORD" value="" title="그룹명" maxlength=20/>
					<label for="SEARCH_USE_YN" id="S1">사용여부</label>
					<select id="SEARCH_USE_YN" name="SEARCH_USE_YN" title="사용여부">
						<option value="">전체</option>
						<option value="Y">사용</option>
						<option value="N">미사용</option>
					</select>
					<input type="submit" value="검색" onclick="javascript:gfn_fn_srch(); return false;"/>
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
		<table id="tb_list" summary="코드그룹 목록">
			<caption>리스트</caption>
			<colgroup>
				<col width="5%"/>
				<col width="15%"/>
				<col width="25%"/>
				<col width=""/>
				<col width="8%"/>
				<col width="8%"/>
			</colgroup>
			<thead> 
				<tr>
					<th cid="ROWNUM" cClass="num" cType="NUM"><spring:message code="word.num"/></th>
					<th cid="CODE_GROUP_ID" alg="center" clickevent="fn_callPopup(this);" url="/admin/commcode/commcode_list_popup.do"><spring:message code="word.codeGroupCd"/></th>
					<th cid="CODE_GROUP_NM" alg="left"><spring:message code="word.codeGroupNm"/></th>
					<th cid="REMARKS" alg="left"><spring:message code="cop.remark"/></th>
					<th cid="CODE_COUNT" alg="center" url="/code/codelist.do"><spring:message code="word.codeCount"/></th>
					<th cid="USE_YN" alg="center"><spring:message code="cop.useAt"/></th>
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
			<button type="button" id="btnBack" onClick="javascript:fn_popInsertForm()">팝업등록</button>
			<span class="btn right" onclick="javascript:fn_allDelete(); return false;">체크박스테스트</span>
			<span class="btn right"><a href="gear_input.html" onclick="javascript:fn_excel(); return false;">엑셀 </a></span>
			<button type="button" id="btnW_update" onClick="javascript:fn_radioTest()">라디오</button>
			<button type="button" id="btnW_getTable" onClick="javascript:fn_getTable()">테이블</button>
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