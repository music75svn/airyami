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
	//<c:forEach var="LANG" items="${ds_cd_LANG}">
	//fn_setFileList(null, "${LANG.CD}");
	//</c:forEach>
	debugger;
	fn_initFileInput();
}

////////////////////////////////////////////////////////////////////////////////////
//호출부분 정의
function fn_srch(){
	$("#IMG_TYPE_CD").val("");
	
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
	inputParam.url 				= "/template/saveImg.do";
	inputParam.form 			= $("#dataForm");
	
	debugger;
	
	gfn_TransactionMultipart(inputParam);
	
}

// 이미지 순서 변경
function fn_changeSortOrder(sortOrder, dtlSeq, imgTypeCd, gubun){
	// 삭제된 내용이 있는지 확인한다.
	var fileDelDtlSeq = $("#FILE_DEL_DTL_SEQ").val();
	if(!gfn_isNull(fileDelDtlSeq)){
		alert("삭제된 이미지가 있습니다. 먼저 저장하시길 바랍니다.");
		return;
	}
	
	// 순서 up 버튼 클릭시
	if(gubun == "U"){
		if( sortOrder == 1 )
			return;
	}
	// 순서 down 버튼 클릭시
	if(gubun == "D"){
		//if( sortOrder == 1 ) // 좀더 생각해보자
		//	return;
	}
	
	
	$("#FILE_DTL_SEQ").val(dtlSeq);
	$("#IMG_TYPE_CD").val(imgTypeCd);
	$("#SORT_ORDER").val(sortOrder);
	$("#SORT_GUBUN").val(gubun);
	
	var inputParam = new Object();
	inputParam.sid 				= "insertCate";
	inputParam.url 				= "/template/changeSortOrder.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction(inputParam);
	
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
			if(!gfn_isNull(result.ds_detail.fileList)){
				fn_initFileList();
				fn_setFileList(result.ds_detail.fileList);// fileList 셋업
			}
			
		}
	}
	
	if(sid == "insertCate"){
		fn_initFileInput();
		fn_srch();
		//self.close();
	}
	
}

////////////////////////////////////////////////////////////////////////////////////
// 사용자 함수
function fn_initFileList(){
	<c:forEach var="IMG_TYPE" items="${ds_cd_IMG_TYPE}">
	var imgType = "${IMG_TYPE.CD}";
	imgNm = "IMG_VIEWLIST_"+imgType;
	imgTd = $("[name="+imgNm+"]");
	
	if(!gfn_isNull(imgTd))
		imgTd.empty();
	
	</c:forEach>
}

function fn_initFileInput(){
	<c:forEach var="IMG_TYPE" items="${ds_cd_IMG_TYPE}">
	var imgType = "${IMG_TYPE.CD}";
	imgNm = "IMG_"+imgType;
	imgTd = $("[name="+imgNm+"]");
	
	if(!gfn_isNull(imgTd))
		imgTd.empty();
	
	if(imgTd.length > 0)
	{
		var fileLink = "";
		fileLink += "<div name='FILE_INFO_" + imgNm + "'>";
		fileLink += "<input type='file' id='"+imgNm+"' name='"+imgNm+"' value='파일찾기' title='첨부파일찾기' size='70' onchange='fnFileUploadCheck(this.value,\"" + imgNm + "\" );'/>";
		fileLink += "</div>";
		imgTd.append(fileLink);
	}
	</c:forEach>
}


function fn_setFileList(fileList){
	
	debugger;
	
	var imgNm = "";
	var imgTd = null;
	
	
	if(!gfn_isNull(fileList)) // filelist가 있을 경우 file 리스트 표시
 	{
		for(var idx = 0; idx < fileList.length ; idx++){
			
			imgNm = "IMG_VIEWLIST_"+fileList[idx].IMG_TYPE_CD;	// (대)상품보기용
			imgTd = $("[name="+imgNm+"]");
			
			if( imgTd.length == 0 )
				continue;
			
			
			var fileLink = "";
			fileLink += "<div name='FILE_INFO_" + fileList[idx].FILE_DTL_SEQ + "'>";
			fileLink += "<a href='javascript:gfn_prodImgdownFile(\"" + fileList[idx].FILE_PATH + "\", \"" + fileList[idx].ORG_FILE_NAME + "\");'>" +fileList[idx].ORG_FILE_NAME+ "</a>";
			fileLink += "<a href='javascript:fn_fileDel(\"" + fileList[idx].FILE_DTL_SEQ + "\");'> 삭제 </a>"; // 대표이미지일 경우
			fileLink += "<br>";
			fileLink += "<img src='/images/btn/icon_pre_month.gif' style='cursor:hand' onclick='javascript:fn_changeSortOrder(\"" + fileList[idx].SORT_ORDER + "\", \"" + fileList[idx].FILE_DTL_SEQ + "\", \"" + fileList[idx].IMG_TYPE_CD + "\", \"U\");'/>"; // 순서변경이미지
			fileLink += "<img src=\"" + fileList[idx].THUMBNAIL_URL_PATH + fileList[idx].SAVE_FILE_NAME + "\"  style=\"width: 20%\" height=\"50\"  border=\"0\"/>";
			fileLink += "<img src='/images/btn/icon_aft_month.gif' style='cursor:hand' onclick='javascript:fn_changeSortOrder(\"" + fileList[idx].SORT_ORDER + "\", \"" + fileList[idx].FILE_DTL_SEQ + "\", \"" + fileList[idx].IMG_TYPE_CD + "\", \"D\");'/>"; // 순서변경이미지
			fileLink += "<div>";
			
			imgTd.append(fileLink);
			
		}
 	}
	
}


function fnFileUploadCheck(filePath,idNumber)
{
	if(gfn_isNull(filePath)) // 선택 파일이 없으므로 true 리턴
		return true;
	var allowExt = "<c:out value='${communityBoardInfo.FILE_ALLOW_EXT}'/>";
	
	if(allowExt == "*" || gfn_isNull(allowExt)) // 모든 확장자 허용
		return true;

	//alert(allowExt);
	
	var fileExtension = fnGetFileExtension(filePath);
	var delimiter = new Array();
	delimiter = allowExt.split(",");
	for( var i = 0; i < delimiter.length; i++){
		if(fileExtension.toLowerCase() == delimiter[i].toLowerCase() 
				|| delimiter[i] == "*")	// CSV 중에 *가 포함될 수도 있으므로. *는 모든 확장자
		{
			return true;
		}
	}
	alert( delimiter.join(' ') + ' 확장자만 지원합니다.');
//	fnFileUploadCreate(filePath,idNumber);
	return false;
}


function fnGetFileExtension(filePath)
{
	var lastidx = -1;
	lastIndex = filePath.lastIndexOf('.');
	var getFileExtension = "";
	if ( lastIndex != -1 )
	{
		getFileExtension = filePath.substring( lastIndex+1, filePath.len );
	} else {
		getFileExtension = "";
	}
	    return getFileExtension;
}


function fn_fileDel(fileDtlSeq)
{
	if(!confirm("삭제하시겠습니까?"))
		return;

	var divObj = $("[name=FILE_INFO_" + fileDtlSeq + "]");
	
	if(gfn_isNull(divObj))
		return;

	var fileDelDtlSeq = $("#FILE_DEL_DTL_SEQ").val();
	fileDelDtlSeq += fileDtlSeq + "|";
	
	$("#FILE_DEL_DTL_SEQ").val(fileDelDtlSeq);
	
	divObj.remove();
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
		<input type="hidden" id="FILE_DEL_DTL_SEQ" name="FILE_DEL_DTL_SEQ" value='${FILE_DEL_DTL_SEQ}'/>
		<input type="hidden" id="LOCAL_LANG" name="LOCAL_LANG" value="${LOCAL_LANG}"/>
		<input type="hidden" id="FILE_DTL_SEQ" name="FILE_DTL_SEQ"/>
		<input type="hidden" id="IMG_TYPE_CD" name="IMG_TYPE_CD"/>
		<input type="hidden" id="SORT_ORDER" name="SORT_ORDER"/>
		<input type="hidden" id="SORT_GUBUN" name="SORT_GUBUN"/>
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
			<c:forEach var="IMG_TYPE" items="${ds_cd_IMG_TYPE}">
			<tr>
				<th colspan=2>${IMG_TYPE.CD_NM}</th>
				<td>
					<div name="IMG_${IMG_TYPE.CD}"></div><br/>
					<div name="IMG_VIEWLIST_${IMG_TYPE.CD}" width="100%"></div>
				</td>
			</tr>
			</c:forEach>
<!-- 			<tr>
				<th>상품 대</th>
				<td name="IMG_L">
			</tr>
			<tr>
				<th>상품 중 </th>
				<td name="IMG_M">
			</tr>
			<tr>
				<th>상품 소</th>
				<td name="IMG_S">
			</tr> -->
		</table>
		</form>
		
		<div class="btn_zone">
			<div class="right"><button type="button" onClick="fn_insert();">등록</button>
			<button type="button" onClick="self.close();">취소</button></div>
		</div>
</div>

</body>