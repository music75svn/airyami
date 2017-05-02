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
	//$('#FROM_DT').val(gfn_addDay( gfn_getToday(true), -1, true ));	// 하루전
	//$('#FROM_DT').val(gfn_addMonth( gfn_getToday(true), -1, true ));	// 한달전
	$('#FROM_DT').val(gfn_addDay( gfn_addMonth( gfn_getToday(true), -1, true ), 1, true)); // 기간이 한달전
	$('#TO_DT').val(gfn_getToday(true));
	
	// 체크박스 초기화
	if(!gfn_isNull( $('#RULE_CD').val() )){
		fn_selectLink($('#RULE_CD').val());
	}
	
	// 그리드에 소트 기능 추가한다.
	gfn_addGridSort("tb_list");
	
}

////////////////////////////////////////////////////////////////////////////////////
// 호출부분 정의
// 리스트 조회
function fn_srch(){
	
	// 기간 체크
	//if(!gfn_DateCrossCheckAll("FROM_DT", "TO_DT"))					return false;	// 유효성 + 크로스체크만 체크
	//if(!gfn_DateCrossCheckAll("FROM_DT", "TO_DT", "D"))				return false;	// 유효성 + 크로스체크만 체크
	//if(!gfn_DateCrossCheckAll("FROM_DT", "TO_DT", "D", "D7"))			return false;	// 유효성 + 크로스체크 + 기간체크
	if(!gfn_DateCrossCheckAll("FROM_DT", "TO_DT", "D", "M1", true))	return false;	// 유효성 + 크로스체크 + 기간체크 알럿은 필요없을경우 사용
	
	// 파라미터 validation
	if(!gfn_validationForm($("#srchForm"))){
		return;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "codeList";
	inputParam.url 				= "/template/selectList.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	//inputParam.callback			= fn_callBackA;
	
	/*
	inputParam.data 			= {};
	inputParam.data.A 			= "1";
	inputParam.data.B 			= "2";
	inputParam.data.C 			= "3"; 
	*/
	
	gfn_Transaction( inputParam );
}

//체크박스 선택된 로우만 뽑아내기
function fn_allDelete(){
	var checkInfo = gfn_getCheckOk("tb_list");
	
	if(gfn_isNull(checkInfo))
		return;
	
	var inputParam = new Object();
	inputParam.sid 				= "delCd";
	inputParam.url 				= "/test/deleteCd.do";
	inputParam.data 			= { "REQKEY"  : checkInfo};
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
		
		
		gfn_addRowClickEvent("tb_list");
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
	
	gfn_commonGo("/test/template_popup", inputParam, "Y");
}

function fn_goDetail(pObj){
	var rowObj = $(pObj).parent();
	var CODE_GROUP_ID = $('input[name=CODE_GROUP_ID]', rowObj).val();
	var CODE = $('input[name=CD]', rowObj).val();
	
	var inputParam = new Object();
	inputParam.CODE_GROUP_ID = CODE_GROUP_ID;
	inputParam.CODE = CODE;
	
	inputParam.LISTPARAMS = gfn_replaceAll($("#srchForm").serialize(), "&", "|");
	inputParam.LISTURL = gfn_getListUrl();
	
	gfn_commonGo("/template/templateView", inputParam, "E");
}

// 그리드 버튼
function fn_btnTest(pObj){
	alert("delete");
}


// 목록으로 돌아가기
function fn_goBack(){
	var inputParam = gfn_makeInputData($("#findForm"));
	
	
	gfn_commonGo("/template/template", inputParam, "N");
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
		<h3>장비 관리 목록</h3>
		<form id="findForm" name="findForm">
			<ppe:makeHidden var="${findParams}" filter="" prefix="FIND_" exclude="LOGIN_ID,LOGIN_NM,LANG_CD"/>
		</form>
		<form id="srchForm" name="srchForm" method="post" action="<c:url value='/commCode/codeList.do'/>" onsubmit="return false;">
			<input type="hidden" name="pageNo" id="pageNo" value="1"/>
			<input type="hidden" name="EXCEL_YN" id="EXCEL_YN" />
			<input type="hidden" name="SORT_COL" id="SORT_COL" value="CODE_GROUP_ID ASC, SORT_ORDER ASC"/>
			<input type="hidden" name="SORT_ACC" id="SORT_ACC" />
		<div id="search">
			<dl>
				<dt>&nbsp;</dt>
				<dd>
					<label for="FROM_DT" id="SDT1">시작일자</label>
					<input type="text" size="8" name="FROM_DT" id="FROM_DT" title="시작일자" class="datepicker">
					~
					<label for="TO_DT" id="SDT1">종료일자</label>
					<input type="text" size="8" name="TO_DT" id="TO_DT" title="종료일자" class="datepicker"/>
					<label for="RULE_CD" id="S1">그룹코드</label>
					<select id="RULE_CD" name="RULE_CD" title="그룹코드" onchange="javascript:fn_selectLink(this.value);">
                        <c:forEach var="GROUP" items="${ds_cd_GROUP}">
                            <option value="${GROUP.CD}">${GROUP.CD_NM}</option>
                        </c:forEach>
					</select>
					<label for="SUB_CD" id="S2">시작일</label>
					<select id="SUB_CD" name="SUB_CD" title="소그룹">
					</select>
					<select>
						<option>상태</option>
					</select>
					<select>
						<option>선택</option>
					</select>
					<label for="SEARCH_WORD" id="S3">검색어</label>
					<input type="text" id="SEARCH_WORD" name="SEARCH_WORD" value="test" title="검색어 입력" depends="required,englishNumeric" maxlength=12/>
					<!-- <input type="text" id="SEARCH_WORD" name="SEARCH_WORD" value="test" title="검색어 입력" depends="required" maxlength=12/> -->
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
		<table id="tb_list" summary="장비 관리 목록">
			<caption>리스트</caption>
			<colgroup>
				<col width="5%"/>
				<col width="5%"/>
				<col width="5%"/>
				<col width="5%"/>
				<col width="10%"/>
				<col width="15%"/>
				<col width="15%"/>
				<col width=""/>
				<col width="15%"/>
				<col width="8%"/>
				<col width="8%"/>
			</colgroup>
			<thead> 
				<tr>
					<th cid="ROWNUM" cClass="num" cType="NUM">순번</th>
					<th cid="RADIO"></th>
					<th cid="CK"><input type="checkbox" name="check"  onclick="javascript:gfn_checkAll('tb_list', this);"/></th>
					<th cid="ROWNUM_D" cType="NUM">역순번</th>
					<th cid="CODE_GROUP_ID" alg="center" clickevent="fn_callPopup(this);" url="/admin/commcode/commcode_list_popup.do">GROUPCODE</th>
					<th cid="CD" alg="center" clickevent="fn_goDetail(this);">CODE</th>
					<th cid="CD_NM" alg="left" delTag="DELYN" cExt="EXT" level="LVL">코드명</th>
					<th cid="CODE_DESC" alg="left" sImg="SIMG">코드설명</th>
					<th cid="ETC" alg="left">기타</th>
					<th cid="BTN" alg="left" cBtnClick="fn_callPopup($(this).parent());">버튼제어</th>
					<th cid="BTN_S" alg="left" cBtnNM="버튼명" cBtnClick="fn_btnTest(this);"></th>
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
			<span class="btn right"><a href="gear_input.html"> 등록 </a> </span>
			<span class="btn right" onclick="javascript:fn_allDelete(); return false;">체크박스테스트</span>
			<span class="btn right"><a href="gear_input.html" onclick="javascript:fn_excel(); return false;"> 엑셀 </a> </span>
			<button type="button" id="btnW_update" onClick="javascript:fn_radioTest()">라디오</button>
			<button type="button" id="btnW_getTable" onClick="javascript:fn_getTable()">테이블</button>
			<button type="button" id="btnBack" onClick="javascript:fn_goBack()">목록</button>
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