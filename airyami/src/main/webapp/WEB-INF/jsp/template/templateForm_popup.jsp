<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<jsp:include page="/include/common.jsp"/>
<%@ include file="/include/admin_standard.jsp"%>

<script type="text/javascript">

$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	gfn_OnLoad();
	
	//alert("5000 + 10000000 = " + (5000 + 10000000));
	
	fn_init();
	
	fn_srch();	// 상세조회		
	//if($("#OP_TYPE").val() != "INSERT") {
	//	fn_srch();	// 상세조회		
	//} else {
	//	fn_setFileList();// fileList 셋업
	//}
});


//화면내 초기화 부분
function fn_init(){
	<c:forEach var="LANG" items="${ds_cd_LANG}">
	fn_setFileList(null, "${LANG.CD}");
	</c:forEach>
}

////////////////////////////////////////////////////////////////////////////////////
//호출부분 정의
function fn_srch(){
	
	var inputParam = new Object();
	inputParam.sid 				= "selectProdDetail";
	inputParam.url 				= "/template/selectProdDetail.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( inputParam );
}

//사용자그룹 등록
function fn_insert(){
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "insertCate";
	inputParam.url 				= "/category/insertCate.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	//inputParam.callback			= "fn_callBack";
	
	gfn_Transaction( inputParam );
	
}


////////////////////////////////////////////////////////////////////////////////////
//콜백 함수
function fn_callBack(sid, result, data){
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	// fn_srch
	if(sid == "selectProdDetail"){
		gfn_setDetails(result.ds_detail);	// 상세 내용 셋업	

		if(!gfn_isNull(result.ds_detail)){
			<c:forEach var="LANG" items="${ds_cd_LANG}">
			fn_setFileList(result.ds_detail.fileList, "${LANG.CD}");// fileList 셋업
			</c:forEach>
		}
	}
	
	if(sid == "insertCate"){
		opener.fn_srch();
		self.close();
	}
	
}

////////////////////////////////////////////////////////////////////////////////////
// 사용자 함수

function fn_setFileList(fileList, langCd){
	
	var imgLTd = $("[name=IMG_LARGE_"+langCd+"]");
	
	if(!gfn_isNull(imgLTd))
		imgLTd.empty();
	
	
	if(!gfn_isNull(fileList)) // filelist가 있을 경우 file 리스트 표시
 	{
		for(var idx = 0; idx < fileList.length ; idx++){
			if( fileList[idx].LANG_CD != langCd )
				continue;
			
			// (대)상품보기용
			if( !gfn_isNull(fileList[idx].IMG_NM_LARGE) ){
				var fileLink = "";
				fileLink += "<div name='FILE_INFO_" + idx + "'>";
				fileLink += "<a href='javascript:fn_fileDownload(" + fileList[idx].FILE_MST_SEQ + ", " + fileList[idx].FILE_DTL_SEQ + ");'>" +fileList[idx].ORG_FILE_NAME+ "</a>";
				fileLink += "<a href='javascript:fn_fileDel(" + idx + ", " + fileList[idx].FILE_DTL_SEQ + ", \"title_img_file\")'> 삭제 </a>"; // 대표이미지일 경우
				fileLink += "<div>";
				
				imgLTd.append(fileLink);
			}
			//-- (대)상품보기용
			
		}
 	}
	else{
		// (대)상품보기용
		if(!gfn_isNull(imgLTd))
		{
			var fileLink = "";
			fileLink += "<div name='FILE_INFO_" + idx++ + "'>";
			fileLink += "<input type='file' id='title_img_file' name='title_img_file' value='파일찾기' title=첨부파일찾기' size='70' onchange='fnFileUploadCheck(this.value," + idx + ");'/>";
			fileLink += "</div>";
			imgLTd.append(fileLink);
		}
		
		
	}
	
	
}


</script>
</head>
<body class="popup">
<h1>파일업로드 테스트<a onClick="self.close();" class="close">X</a></h1>

<div class="content">
	<h4>상품 이미지 업로드</h4>	
	<form id="dataForm" name="dataForm" method="post" action="/test/fileInsert.do" enctype="multipart/form-data" onsubmit="return false;">
		<input type="hidden" name="PROD_NO" id="PROD_NO" value="${PROD_NO}"/>
		<input type="hidden" name="SEARCH_PROD_NO" id="SEARCH_PROD_NO" value="${PROD_NO}"/>
		<!-- <form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;"> -->
		<table summary=" 등록" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="15%">
			<col width="15%">
			<col width="70%">
			</colgroup>
			<tr>
				<th colspan=2>상품명</th>
				<td><input type="text" name="CATE_CODE" id="CATE_CODE" size=30  value="자동생성" readonly/></td>
			</tr>
			<tr>
				<th colspan=2>카테고리순서</th>
				<td><input type="text" name="CATE_ORDER" id="CATE_ORDER" size=72 value="자동생성 - 위치변경은 카테고리수정에서 변경" readonly/></td>
			</tr>
			<tr>
			<c:set var="listSize" value="${fn:length(ds_cd_LANG)}" />
				<th rowspan="${listSize+1}">상품보기용</th>
			</tr>
			<c:forEach var="LANG" items="${ds_cd_LANG}">
			<tr>
				<th>${LANG.CD_NM}</th>
				<td name="IMG_LARGE_${LANG.CD}">
				</td>
			</tr>
			</c:forEach>
		</table>
		</form>
		
		<div class="btn_zone">
			<div class="right"><button type="button" onClick="fn_insert();">등록</button>
			<button type="button" onClick="self.close();">취소</button></div>
		</div>
</div>

</body>