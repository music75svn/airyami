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
});


//화면내 초기화 부분
function fn_init(){
<c:forEach var="imgType" items="${ds_imgType}">
	<c:forEach var="LANG" items="${ds_cd_LANG}">
	fn_setFileList(null, "${imgType.CD}", "${LANG.CD}");
	</c:forEach>
</c:forEach>
}

////////////////////////////////////////////////////////////////////////////////////
//호출부분 정의
function fn_srch(){
	
	var inputParam = new Object();
	inputParam.sid 				= "selectProdDetail";
	inputParam.url 				= "/product/selectProdDetail.do";
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
	inputParam.url 				= "/product/saveImg.do";
	inputParam.form 			= $("#dataForm");
	
	gfn_TransactionMultipart(inputParam);
	
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

		if(!gfn_isNull(result.fileList)){
			for(var i = 0; i < result.fileList.length; i++){
				fn_setFileList(result.fileList, result.fileList[i].IMG_TYPE_CD, result.fileList[i].LANG_CD);// fileList 셋업
			}
		}
	}
	
	if(sid == "insertCate"){
		opener.fn_srch();
		self.close();
	}
	
}

////////////////////////////////////////////////////////////////////////////////////
// 사용자 함수
function fn_setFileList(fileList, imgType, langCd){
	var imgLnm = "IMG_"+imgType+"_" + langCd;	// (대)상품보기용
	var imgLTd = $("[name="+imgLnm+"]");
	
	if(!gfn_isNull(fileList)) // filelist가 있을 경우 file 리스트 표시
 	{
		for(var idx = 0; idx < fileList.length ; idx++){
			if( fileList[idx].LANG_CD != langCd )	// 다른 언어는 패스
				continue;
			
			if(!gfn_isNull(imgLTd))
				imgLTd.empty();
			
			var fileLink = "";
			fileLink += "<div name='FILE_INFO_" + imgLnm + "'>";
			fileLink += "<a href='javascript:gfn_prodImgdownFile(\"" + fileList[idx].FILE_PATH + "\", \"" + fileList[idx].ORG_FILE_NAME + "\");'>" +fileList[idx].ORG_FILE_NAME+ "</a>";
			fileLink += "<a href='javascript:fn_fileDel(\"" + imgLnm + "\", \"" + fileList[idx].FILE_DTL_SEQ + "\");'> 삭제 </a>"; // 대표이미지일 경우
			fileLink += "<br><img src=\"" + fileList[idx].URL_PATH +"\\"+ fileList[idx].SAVE_FILE_NAME + "\" border=\"0\"/>";
			fileLink += "<div>";
			
			imgLTd.append(fileLink);
			
		}
 	}
	else{
		// (대)상품보기용
		if(!gfn_isNull(imgLTd))
		{
			var fileLink = "";
			fileLink += "<div name='FILE_INFO_" + imgLnm + "'>";
			fileLink += "<input type='file' id='"+imgLnm+"' name='"+imgLnm+"' value='파일찾기' title='첨부파일찾기' size='70' onchange='fnFileUploadCheck(this.value,\"" + imgLnm + "\" );'/>";
			fileLink += "</div>";
			imgLTd.append(fileLink);
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


function fn_fileDel(imgNm, fileDtlSeq)
{
	if(!confirm("삭제하시겠습니까?"))
		return;

	var divObj = $("[name=FILE_INFO_" + imgNm + "]");
	
	if(gfn_isNull(divObj))
		return;

	var fileDelDtlSeq = $("#FILE_DEL_DTL_SEQ").val();
	fileDelDtlSeq += fileDtlSeq + "|";
	
	$("#FILE_DEL_DTL_SEQ").val(fileDelDtlSeq);
	
	divObj.empty();
	
	var fileLink = "";
	fileLink += "<input type='file' id='"+imgNm+"' name='"+imgNm+"' value='파일찾기' title='첨부파일찾기' size='70' onchange='fnFileUploadCheck(this.value,\"" + imgNm + "\" );'/>";
	
	divObj.append(fileLink);
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
		<!-- <form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;"> -->
		<table summary=" 등록" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="15%">
			<col width="15%">
			<col width="70%">
			</colgroup>
			<tr>
				<th colspan=2><spring:message code="word.prodNm"/></th>
				<td><input type="text" name="PROD_NM" id="PROD_NM" size=30  readonly/></td>
			</tr>
			<tr>
				<th colspan=2><spring:message code="word.category"/></th>
				<td><input type="text" name="CATEGORY_NM" id="CATEGORY_NM" size=72 readonly/></td>
			</tr>
			
		<c:forEach var="imgType" items="${ds_imgType}">
			<tr>
			<c:set var="listSize" value="${fn:length(ds_cd_LANG)}" />
				<th rowspan="${listSize+1}">${imgType.CD_NM}</th>
			</tr>
			<c:forEach var="LANG" items="${ds_cd_LANG}">
			<tr>
				<th>${LANG.CD_NM}</th>
				<td name="IMG_${imgType.CD}_${LANG.CD}">
				</td>
			</tr>
		</c:forEach>	
			
			</c:forEach>
		</table>
		</form>
		
		<div class="btn_zone">
			<div class="right"><button type="button" onClick="fn_insert();"><spring:message code="button.save"/></button>
			<button type="button" onClick="self.close();"><spring:message code="button.close"/></button></div>
		</div>
</div>

</body>