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
	fn_setFileList(null);
}

////////////////////////////////////////////////////////////////////////////////////
//호출부분 정의
function fn_srch(){
	
	var inputParam = new Object();
	inputParam.sid 				= "selectFileInfo";
	inputParam.url 				= "/template/selectFileInfo.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( inputParam );
}

//사용자그룹 등록
function fn_insert(){
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "insertFile";
	inputParam.url 				= "/template/insertFile.do";
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
	if(sid == "selectFileInfo"){
		gfn_setDetails(result.ds_detail);	// 상세 내용 셋업	

		if(!gfn_isNull(result.ds_detail)){
			if(!gfn_isNull(result.ds_detail.fileList))
				fn_setFileList(result.ds_detail.fileList);// fileList 셋업
		}
	}
	
	if(sid == "insertFile"){
		//opener.fn_srch();
		//self.close();
	}
	
}

////////////////////////////////////////////////////////////////////////////////////
// 사용자 함수

function fn_setFileList(fileList){
	
	debugger;
	
	var imgNm = "";
	var imgTd = null;
	
	// 파일리스트
	var fileTd = $("[name=FILE_LIST_INFO]");
	
	if(!gfn_isNull(fileTd))
		fileTd.empty();

	var idx=0;
	
	if(!gfn_isNull(fileList)) // filelist가 있을 경우 file 리스트 표시
 	{
		for(; idx < fileList.length ; idx++){
			var fileLink = "";
			fileLink += "<div name='FILE_INFO_" + idx + "'>";
			fileLink += "<a href='javascript:gfn_downFile(" + fileList[idx].FILE_MST_SEQ + ", " + fileList[idx].FILE_DTL_SEQ + ");'>" +fileList[idx].ORG_FILE_NAME+ "</a>";
			fileLink += "<a href='javascript:fn_fileDel(" + idx + ", " + fileList[idx].FILE_DTL_SEQ + ", \"file_" + idx + "\")'> 삭제 </a>"; // 일반첨부파일일 경우
			fileLink += "<div>";
			
			fileTd.append(fileLink);
		}	
 	}
	
	var fileCnt = parseInt($("#FILE_CNT").val())
	
	for(; idx < fileCnt; idx++) // file리스트에 없는 항목은 input box 생성
	{
		var fileLink = "";
		fileLink += "<div name='FILE_INFO_" + idx + "'>";
		fileLink += "<input type='file' id='file_" + idx + "' name='file_" + idx + "' value='파일찾기' title=첨부파일찾기' size='70' onchange='fnFileUploadCheck(this.value," + idx + ");'/>";
		fileLink += "</div>";
		fileTd.append(fileLink);
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


function fn_fileDel(fileIdx, fileDtlSeq, name)
{
	if(!confirm("삭제하시겠습니까?"))
		return;

	var divObj = $("[name=FILE_INFO_" + fileIdx + "]")
	
	if(gfn_isNull(divObj))
		return;

	var fileDelDtlSeq = $("#FILE_DEL_DTL_SEQ").val();
	fileDelDtlSeq += fileDtlSeq + "|";
	
	$("#FILE_DEL_DTL_SEQ").val(fileDelDtlSeq);
	
	divObj.empty();
	
	var fileLink = "";
	fileLink += "<input type='file' id='" + name + "' name='" + name + "' value='파일찾기' title=첨부파일찾기' size='70' onchange='fnFileUploadCheck(this.value," + fileIdx + ");'/>";
	
	divObj.append(fileLink);
}


</script>
</head>
<body class="popup">
<h1>파일업로드 테스트<a onClick="self.close();" class="close">X</a></h1>

<div class="content">
	<h4>파일 업로드</h4>	
	<form id="dataForm" name="dataForm" method="post" action="/test/fileInsert.do" enctype="multipart/form-data" onsubmit="return false;">
		<input type="hidden" name="PROD_NO" id="PROD_NO" value="${PROD_NO}"/>
		<input type="hidden" name="SEARCH_PROD_NO" id="SEARCH_PROD_NO" value="${PROD_NO}"/>
		<input type="hidden" id="FILE_MST_SEQ" name="FILE_MST_SEQ" value='3'/>
		<input type="hidden" id="FILE_DEL_DTL_SEQ" name="FILE_DEL_DTL_SEQ" value='${FILE_DEL_DTL_SEQ}'/>
		<input type="hidden" id="FILE_CNT" name="FILE_CNT" value='5'/>
		<input type="hidden" id="FOLDER_NM" name="FOLDER_NM" value='ppe'/>
		<table summary=" 등록" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="15%">
			<col width="70%">
			</colgroup>
			<tr>
				<th>파일리스트</th>
				<td>
					<div id="FILE_LIST_INFO" name="FILE_LIST_INFO">
						
					</div>
				</td>
			</tr>
			<tr>
		</table>
		</form>
		
		<div class="btn_zone">
			<div class="right"><button type="button" onClick="fn_insert();">등록</button>
			<button type="button" onClick="self.close();">취소</button></div>
		</div>
</div>

</body>