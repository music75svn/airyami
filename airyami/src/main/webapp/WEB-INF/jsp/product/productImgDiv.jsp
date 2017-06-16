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

var g_fileList = null;

//화면내 초기화 부분
function fn_init(){
	fn_initFileList();
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

function fn_selectImgListByImgType(){
	fn_srch();
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
				$("#IMG_FIRST_INDEX").val(0);
				$("#IMG_LIST_LENGTH").val(result.ds_detail.fileList.length);
				g_fileList = result.ds_detail.fileList;
				fn_initFileList();
				fn_setFileList(result.ds_detail.fileList);// fileList 셋업
			}
			
		}
	}
	
}

////////////////////////////////////////////////////////////////////////////////////
// 사용자 함수
function fn_initFileList(){
	imgNm = "IMG_VIEWLIST";
	imgTd = $("[name="+imgNm+"]");
	
	if(!gfn_isNull(imgTd))
		imgTd.empty();
	
}

function fn_changeList(addIndex){
	var firstIdx = $("#IMG_FIRST_INDEX").val();		// 이미지파일 첫 번째 위치 
	var listLength = $("#IMG_LIST_LENGTH").val();	// 전체 이미지 파일 갯수
	var listSize = $("#IMG_LIST_SIZE").val();		// 한번에 보여줄수 있는 이미지 갯수
	
	if( (firstIdx*1 + addIndex*1) < 0)
		return;
	
	if(firstIdx*1 + addIndex*1 + listSize*1 > listLength)
		return;
	
	$("#IMG_FIRST_INDEX").val(firstIdx*1 + addIndex*1);	// 새로운 첫 이미지파일 위치
	
	fn_initFileList();
	fn_setFileList(g_fileList);
}

function fn_setFileList(fileList){
	
	var imgNm = "";
	var imgTd = null;
	
	var firstIdx = $("#IMG_FIRST_INDEX").val();
	var listSize = $("#IMG_LIST_SIZE").val();
	
	if(!gfn_isNull(fileList)) // filelist가 있을 경우 file 리스트 표시
 	{
		//for(var idx = 0; idx < fileList.length; idx++){
		for(var idx = firstIdx; idx < firstIdx + listSize && idx < fileList.length ; idx++){
			
			imgNm = "IMG_VIEWLIST";	// (대)상품보기용
			imgTd = $("[name="+imgNm+"]");
			
			var fileLink = "";
			fileLink += "<div name='FILE_INFO_" + fileList[idx].FILE_DTL_SEQ + "'>";
			fileLink += "<img src=\"" + fileList[idx].THUMBNAIL_URL_PATH + fileList[idx].SAVE_FILE_NAME + "\" onclick='javascript:gfn_changeImgView(\"IMG_VIEW\", \"" + gfn_replaceAll(fileList[idx].URL_PATH + fileList[idx].SAVE_FILE_NAME, "\\", "/") + "\")' style=\"width:20%\" height=\"50\"  border=\"0\"/>";
			fileLink += "<div>";
			
			imgTd.append(fileLink);
		}
		
		
		gfn_changeImgView("IMG_VIEW", gfn_replaceAll(fileList[firstIdx].URL_PATH + fileList[firstIdx].SAVE_FILE_NAME, "\\", "/"));
 	}
}

function gfn_changeImgView(imgViewOjbNm, src){
	var imgViewObj = $("[name="+imgViewOjbNm+"]");
	
	imgViewObj.attr("src", src);
}



</script>
</head>
<body class="popup">

<div class="content">
	<form id="dataForm" name="dataForm" method="post" action="/test/fileInsert.do" enctype="multipart/form-data" onsubmit="return false;">
		<input type="hidden" name="PROD_NO" id="PROD_NO" value="${PROD_NO}"/>
		<input type="hidden" name="SEARCH_PROD_NO" id="SEARCH_PROD_NO" value="${PROD_NO}"/>
		<input type="hidden" name="IMG_FIRST_INDEX" id="IMG_FIRST_INDEX" value="0"/>
		<input type="hidden" name="IMG_LIST_LENGTH" id="IMG_LIST_LENGTH" value="0"/>
		<input type="hidden" name="IMG_LIST_SIZE" id="IMG_LIST_SIZE" value="4"/>
		<!-- <form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;"> -->
		<table summary=" 등록" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="15%">
			<col width="15%">
			<col width="70%">
			</colgroup>
			<tr>
				<th colspan=2>카테고리순서</th>
				<td>
					<select id="IMG_TYPE_CD" name="IMG_TYPE_CD" title="이미지타입" onchange="javascript:fn_selectImgListByImgType(this.value);">
						<c:forEach var="IMG_TYPE" items="${ds_cd_IMG_TYPE}">
							<option value="${IMG_TYPE.CD}">${IMG_TYPE.CD_NM}</option>
						</c:forEach>
					</select>		
				</td>
			</tr>
			<tr>
				<td colspan=3>
					<div style="width=200px" height=200>
						<img name="IMG_VIEW"></img>
					</div>
					<img src='/images/btn/icon_pre_month.gif' style='cursor:hand' onclick='javascript:fn_changeList(-1)' />
					<div name="IMG_VIEWLIST" height=50>
					</div>
					<img src='/images/btn/icon_aft_month.gif' style='cursor:hand' onclick='javascript:fn_changeList(1)' />
				</td>
			</tr>
		</table>
		</form>
</div>

</body>