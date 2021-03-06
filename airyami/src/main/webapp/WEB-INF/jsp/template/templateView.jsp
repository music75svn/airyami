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
	
	fn_srch();	// 상세조회
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
	inputParam.sid 				= "getCodeView";
	inputParam.url 				= "/template/getCodeView.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
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
	if(sid == "getCodeView"){
		//gfn_setDetails(result.ds_detail);	// 상세 내용 셋업
		gfn_setDetails(result.ds_detail, $("#contents"));	// 지역내 상세 내용 셋업
	}
	
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
// 삭제 버튼
function fn_Delte(){
	alert("delete");
}

// 등록 버튼
function fn_Insert(){
	alert("fn_Insert");
}



////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출
function goUpdateForm(){
	
	if(!gfn_validationForm($("#srchForm"))){
		return;
	}
	
	var inputParam = gfn_makeInputData($("#srchForm"));
	
	//gfn_commonGo("/template/template_popup", inputParam, "Y");
	gfn_commonGo("/template/templateForm_popup", inputParam, "N");
}


////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수
// 팝업 내용 변경시 초기화
function fn_userNmChange() {
    $('#USER_ID').val('');
}


function fn_clearData(){
	
	if(!gfn_validationForm($("#srchForm"))){
		return;
	}
	
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
		<h3>코드정보 상세보기</h3>
		<input type="hidden" name="PAGERIGHT" id="PAGERIGHT" value='<c:out value="${PAGERIGHT}"/>'/>
		<form id="findForm" name="findForm">
			<ppe:makeHidden var="${findParams}" filter="FIND_"/>
		</form>
		<form id="srchForm" name="srchForm" method="post" action="#" onsubmit="return false;">
			<input type="hidden" name="CODE_ID" id="CODE_ID" value='<c:out value="${CODE_ID}"/>'/>
		<table summary="코드 정보 상세보기" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="20%">
			<col width="80%">
			</colgroup>
			<tr>
				<th>그룹코드</th>
				<td><input type="text" name="CODE_GROUP_ID" id="CODE_GROUP_ID" value='<c:out value="${CODE_GROUP_ID}"/>'/></td>
			</tr>
			<tr>
				<th>코드</th>
				<td><span id="CD"></span></td>
			</tr>
			<tr>
				<th>코드명</th>
				<td><input type="text" name="CD_NM" id="CD_NM" regular="NUM"/></td>
			</tr>
		</table>
		</form>
		
		<div class="btn_zone">
			<div class="left"><span id="preLink" title="목록"></span></div>
			<!-- <div class="left"><button type="button" id="btnR_Return">목록</button></div> -->
			<div class="right"><button type="button" id="btnW_update" onClick="javascript:goUpdateForm()">수정</button></div>
			<div class="right"><button type="button" id="btnW_update" onClick="javascript:fn_clearData()">클리어</button></div>
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