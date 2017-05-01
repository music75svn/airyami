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

$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	gfn_OnLoad();
	
	fn_init();
	
	//fn_srch();	// 상세조회
});

// 화면내 초기화 부분
function fn_init(){
	// 체크박스 초기화
	gfn_setCheck($('#C001001'));
	// select 박스 초기화
	gfn_setSelect($('#SYS_CD'), 'C001002');
	$('#SYS_CD').val('C001002');
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


function fn_delFile(){
	var inputParam = new Object();
	inputParam.sid 				= "insertFileTest";
	inputParam.url 				= "/template/updateFileTest.do";
	inputParam.form 			= $("#frmUpload");
	
	gfn_TransactionMultipart(inputParam);
}




// 연동 select 코드 조회
function fn_selectLink(code){
	if(gfn_isNull(code)){
		return;
	}
	
	gfn_GetCodeList(code, $('#SUB_CD'));
}


//file upload
function fn_insertFile() {
	
	
	var inputParam = new Object();
	inputParam.sid 				= "insertFileTest";
	inputParam.url 				= "/test/insertFile.do";
	inputParam.form 			= $("#frmUpload");
	
	gfn_TransactionMultipart(inputParam);
	
	<%-- $('#frmUpload').ajaxSubmit({
	      url		: "<%=UrlUtil.getActionRoot(request)%>/MCS/insertVeriResult.do"
	    , dataType	: "json"
	    , success	: function(result, textStatus, data) {
	        callbackExcelUpload( result );
	    }
	    , error		: function(xhr, errorName, error) {
	        alert("에러가 발생하였습니다.");
	    }
	}); --%>
}

////////////////////////////////////////////////////////////////////////////////////
// 콜백 함수
function fn_callBack(sid, result, data){
	debugger;
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	
	// fn_srch
	if(sid == "getCodeView"){
		gfn_setDetails(result.ds_detail);	// 상세 내용 셋업
	}
	// fn_srch
	else if(sid == "insertFileTest"){
		alert("insertFileTest callback");	// 
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
		<h3>코드정보 상세보기</h3>
		<input type="hidden" name="LISTPARAMS" id="LISTPARAMS" value='<c:out value="${LISTPARAMS}"/>'/>
		<input type="hidden" name="LISTURL" id="LISTURL" value='<c:out value="${LISTURL}"/>'/>
		<form id="frmUpload" name="frmUpload" method="post" action="/test/fileInsert.do" enctype="multipart/form-data" onsubmit="return false;">
			<input type="hidden" name="CODE" id="CODE" value='<c:out value="${CODE}"/>'/>
			<input type="hidden" name="FILE_MST_SEQ" id="FILE_MST_SEQ" value="24"/>
			<input type="hidden" name="DEL_DTL_SEQS" id="DEL_DTL_SEQS" value="1|2|"/>
		<table summary="코드 정보 상세보기" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="20%">
			<col width="80%">
			</colgroup>
			<tr>
				<th>그룹코드</th>
				<td><input type="text" name="GROUP_CODE" id="GROUP_CODE" value='<c:out value="${GROUP_CODE}"/>'/></td>
			</tr>
			<tr>
				<th>코드</th>
				<td><span id="CD"></span></td>
			</tr>
			<tr>
				<th>코드명</th>
				<td><input type="text" name="CD_NM" id="CD_NM"/></td>
			</tr>
			<tr>
				<th>장비사진</th>
				<td><img src="http://placehold.it/100x50" alt="장비 사진"></td>
			</tr>
			<tr>
				<th>일련번호(S/N)</th>
				<td>132145648</td>
			</tr>
			<tr>
				<th>제조사</th>
				<td>메이커봇</td>
			</tr>
		</table>
		
		<div id="w_file_1">
		<!-- <input type="file" id="file_1" name="file_1" size="70" onchange='fnFileUploadCheck(this.value,1);'/> -->
		<input type="file" id="file_1" name="img_file_1" size="70"/>
		<input type="file" id="file_2" name="img_file_2" size="70"/>
		</div>
		</form>
		
		<div class="btn_zone">
			<span class="btn"> <a href="<c:url value='${REFPATH}'/>">취소2</a> </span>
			<div class="left"><span class="btn" id="list_link"><a href="gear.html">목록 </a></span></div>
			<div class="right">
				<button type="button" onClick="location.href='gear_modify.html">수정</button>
				<button type="button" onClick="javascript:fn_insertFile(); return false;">파일</button>
				<button type="button" onClick="javascript:gfn_downFile(25, 1); return false;">파일받기</button>
				<button type="button" onClick="javascript:fn_delFile(); return false;">파일삭제</button>
			</div>
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