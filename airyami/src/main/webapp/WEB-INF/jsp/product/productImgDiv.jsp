<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />

<script type="text/javascript">

var g_fileList = null;

//화면내 초기화 부분
function dfn_init(){
	
	dfn_initFileList();
	dfn_srch();	// 상세조회	
}

////////////////////////////////////////////////////////////////////////////////////
//호출부분 정의
function dfn_srch(){
	
	var inputParam = new Object();
	inputParam.sid 				= "selectProdDetail";
	inputParam.url 				= "/product/selectProdDetail.do";
	inputParam.callback         = dfn_callBack;
	inputParam.data 			= gfn_makeInputData($("#prodImgViewForm"));

	gfn_Transaction( inputParam );
}


////////////////////////////////////////////////////////////////////////////////////
//콜백 함수
function dfn_callBack(sid, result, data){
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	// dfn_srch
	if(sid == "selectProdDetail"){
		gfn_setDetails(result.ds_detail);	// 상세 내용 셋업	

		if(!gfn_isNull(result.ds_detail)){
			if(!gfn_isNull(result.ds_detail.fileList)){
				$("#IMG_FIRST_INDEX").val(0);
				$("#IMG_LIST_LENGTH").val(result.ds_detail.fileList.length);
				g_fileList = result.ds_detail.fileList;
				dfn_initFileList();
				dfn_setFileList(result.ds_detail.fileList);// fileList 셋업
			}
			
		}
	}
	
}

////////////////////////////////////////////////////////////////////////////////////
// 사용자 함수
function dfn_initFileList(){
	imgNm = "IMG_VIEWLIST";
	imgTd = $("[name="+imgNm+"]");
	
	if(!gfn_isNull(imgTd))
		imgTd.empty();
	
}

function dfn_changeList(addIndex){
	var firstIdx = $("#IMG_FIRST_INDEX").val();		// 이미지파일 첫 번째 위치 
	var listLength = $("#IMG_LIST_LENGTH").val();	// 전체 이미지 파일 갯수
	var listSize = $("#IMG_LIST_SIZE").val();		// 한번에 보여줄수 있는 이미지 갯수
	
	if( (firstIdx*1 + addIndex*1) < 0)
		return;
	
	if(firstIdx*1 + addIndex*1 + listSize*1 > listLength)
		return;
	
	$("#IMG_FIRST_INDEX").val(firstIdx*1 + addIndex*1);	// 새로운 첫 이미지파일 위치
	
	dfn_initFileList();
	dfn_setFileList(g_fileList);
}

function dfn_setFileList(fileList){
	
	debugger;
	
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
<h1>상품이미지View 샘플<a onClick="self.close();" class="close">X</a></h1>

<div class="content">
	<h4>상품 View</h4>	
	<form id="prodImgViewForm" name="prodImgViewForm" method="post" action="/test/fileInsert.do" enctype="multipart/form-data" onsubmit="return false;">
		<input type="hidden" name="PROD_NO" id="PROD_NO" value="${PROD_NO}"/>
		<input type="hidden" name="SEARCH_PROD_NO" id="SEARCH_PROD_NO" value="${PROD_NO}"/>
		<input type="hidden" name="IMG_FIRST_INDEX" id="IMG_FIRST_INDEX" value="0"/>
		<input type="hidden" name="IMG_LIST_LENGTH" id="IMG_LIST_LENGTH" value="0"/>
		<input type="hidden" name="IMG_LIST_SIZE" id="IMG_LIST_SIZE" value="4"/>
		<!-- <form id="prodImgViewForm" name="prodImgViewForm" method="post" action="#" onsubmit="return false;"> -->
		<table summary="조회" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="15%">
			<col width="15%">
			<col width="70%">
			</colgroup>
			<tr>
				<th colspan=2>카테고리순서</th>
				<td>
					<select id="IMG_TYPE_CD" name="IMG_TYPE_CD" title="이미지타입" onchange="javascript:dfn_selectImgListByImgType(this.value);">
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
					<img src='/images/btn/icon_pre_month.gif' style='cursor:hand' onclick='javascript:dfn_changeList(-1)' />
					<div name="IMG_VIEWLIST" height=50>
					</div>
					<img src='/images/btn/icon_aft_month.gif' style='cursor:hand' onclick='javascript:dfn_changeList(1)' />
				</td>
			</tr>
		</table>
		</form>
</div>

</body>