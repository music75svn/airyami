<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=yes, target-densitydpi=medium-dpi">
<%@ include file="/include/title.jsp"%>
<jsp:include page="/include/common.jsp"/>
<link type="text/css" rel="stylesheet" href="../css/shop/common.css" />
<link type="text/css" rel="stylesheet" href="../css/shop/slick.css" />
<%@ include file="/include/admin_standard.jsp"%>
<script type="text/javascript" src="../js/slick.js"></script>

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

// 상품이미지 등록
function fn_insertProdImg(){
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return;
	}
	
	
	var inputParam = new Object();
	inputParam.sid 				= "insertProdImg";
	inputParam.url 				= "/template/saveImg.do";
	inputParam.form 			= $("#dataForm");
	
	gfn_TransactionMultipart(inputParam);
	
}

// 상품이미지 삭제
function fn_deleteProdImg(){
	var curImg = $(".slick-current", $("[name=IMG_VIEW]")).children();
	var curListImg = $(".slick-current", $("[name=IMG_VIEWLIST]")).children();
		
	if(gfn_isNull(curImg))
		return;
	
	if(!confirm("삭제하시겠습니까?"))
		return;
	
	
	var fileDtlSeq = curImg.attr("dtlSeq");
	
	var fileDelDtlSeq = $("#FILE_DEL_DTL_SEQ").val();
	fileDelDtlSeq += fileDtlSeq + "|";
	
	$("#FILE_DEL_DTL_SEQ").val(fileDelDtlSeq);
	
	var inputParam = new Object();
	inputParam.sid 				= "deleteProdImg";
	inputParam.url 				= "/template/saveImg.do";
	inputParam.form 			= $("#dataForm");
	
	
	gfn_TransactionMultipart(inputParam);
	
	$("#FILE_DEL_DTL_SEQ").val("");
	
	//curImg.remove();
	//curListImg.remove();
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
	if(sid == "deleteProdImg"){
		fn_srch();
	}
	if(sid == "insertProdImg"){
		fn_srch();
	}
	
}

////////////////////////////////////////////////////////////////////////////////////
// 사용자 함수
function fn_initFileList(){
	
	imgNm = "IMG_MAIN";
	imgMain = $("[name="+imgNm+"]");
	
	if(!gfn_isNull(imgMain))
		imgMain.empty();
	
	var initHtml = "<div class=\"slider-for\" name=\"IMG_VIEW\"></div>"
				 + "<div class= \"slider-nav\" name=\"IMG_VIEWLIST\"></div>";
	
	imgMain.append(initHtml);
	
	
	// 파일리스트
	var fileTd = $("[name=FILE_LIST_INFO]");
	
	if(!gfn_isNull(fileTd))
		fileTd.empty();
	
	var fileCnt = parseInt($("#FILE_CNT").val())
	
	for(var idx=0; idx < fileCnt; idx++) // file리스트에 없는 항목은 input box 생성
	{
		var fileLink = "";
		fileLink += "<div name='FILE_INFO_" + idx + "'>";
		fileLink += "<input type='file' id='file_" + idx + "' name='file_" + idx + "' value='파일찾기' title=첨부파일찾기' size='70' onchange='fnFileUploadCheck(this.value," + idx + ");'/>";
		fileLink += "</div>";
		fileTd.append(fileLink);
	}
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
	
	debugger;
	
	var imgNm = "";
	var imgListNm = "";
	var imgTd = null;
	var imgListTd = null;
	
	//var firstIdx = $("#IMG_FIRST_INDEX").val();
	//var listSize = $("#IMG_LIST_SIZE").val();
	
	if(!gfn_isNull(fileList)) // filelist가 있을 경우 file 리스트 표시
 	{
		imgNm = "IMG_VIEW";	// (대)상품보기용
		imgTd = $("[name="+imgNm+"]");
		
		imgListNm = "IMG_VIEWLIST";	// (대)리스트 보기용
		imgListTd = $("[name="+imgListNm+"]");
			
		for(var idx = 0; idx < fileList.length; idx++){
		//for(var idx = firstIdx; idx < firstIdx + listSize && idx < fileList.length ; idx++){
			var fileInfo = "";
			//fileInfo += "<li style=\"float: left; list-style: none; position: relative; width: 638px;\"><img src=\"" + fileList[idx].URL_PATH + fileList[idx].SAVE_FILE_NAME + "\" /></li>";
			fileInfo += "<div><img src=\"" + fileList[idx].URL_PATH + fileList[idx].SAVE_FILE_NAME + "\" dtlSeq=\""+ fileList[idx].FILE_DTL_SEQ +"\"  /></div>";
			imgTd.append(fileInfo);
			
			var fileListLink = "";
			fileListLink += "<div><img src=\"" + fileList[idx].THUMBNAIL_URL_PATH + fileList[idx].SAVE_FILE_NAME + "\" dtlSeq=\""+ fileList[idx].FILE_DTL_SEQ +"\" /></div>";
			/* fileListLink += "<a data-slide-index=\"" + idx + "\" href=\"\"><img src=\"" + fileList[idx].THUMBNAIL_URL_PATH + fileList[idx].SAVE_FILE_NAME + "\" /></a>"; */
			imgListTd.append(fileListLink);
		}
		
		$('.slider-for').slick({
			slidesToShow: 1,
	 		slidesToScroll: 1,
	  		arrows: false,
	  		fade: true,
	  		asNavFor: '.slider-nav'		
	  	});
		$('.slider-nav').slick({
	  		slidesToShow: 4,
	  		slidesToScroll: 1,
	  		asNavFor: '.slider-for',
	  		dots: true,
	  		centerMode: true,
	  		focusOnSelect: true
		});
 	}
}




</script>
</head>
<body class="popup">
<h1>상품이미지등록 샘플<a onClick="self.close();" class="close">X</a></h1>

<div class="content">
	<h4>상품 등록</h4>	
	<form id="dataForm" name="dataForm" method="post" action="/test/fileInsert.do" enctype="multipart/form-data" onsubmit="return false;">
		<input type="hidden" name="PROD_NO" id="PROD_NO" value="${PROD_NO}"/>
		<input type="hidden" name="SEARCH_PROD_NO" id="SEARCH_PROD_NO" value="${PROD_NO}"/>
		<input type="hidden" name="IMG_FIRST_INDEX" id="IMG_FIRST_INDEX" value="0"/>
		<input type="hidden" name="IMG_LIST_LENGTH" id="IMG_LIST_LENGTH" value="0"/>
		<input type="hidden" name="IMG_LIST_SIZE" id="IMG_LIST_SIZE" value="4"/>
		<input type="hidden" id="FILE_CNT" name="FILE_CNT" value='5'/>
		<input type="hidden" id="FILE_DEL_DTL_SEQ" name="FILE_DEL_DTL_SEQ" value='${FILE_DEL_DTL_SEQ}'/>
		<!-- <form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;"> -->
		<table summary=" 등록" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="15%">
			<col width="15%">
			<col width="70%">
			</colgroup>
			<tr>
				<th colspan=2>상품명</th>
				<td><input type="text" name="PROD_NM" id="PROD_NM" size=30  value="자동생성" readonly/></td>
			</tr>
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
					<!-- product -->
					<div class="product">
						<!-- top -->
						<div class="top">
							<!-- center -->
							<div class="slider" name="IMG_MAIN">
								<div class="slider-for">
						    	<div><img src="../img/product/detail_img01.jpg"></div>
						    	<div><img src="http://placehold.it/350x300?text=2"></div>
						    	<div><img src="http://placehold.it/350x300?text=3"></div>
						    	<div><img src="http://placehold.it/350x300?text=4"></div>
						    	<div><img src="http://placehold.it/350x300?text=5"></div>
						    	<div><img src="http://placehold.it/350x300?text=6"></div>
						    	<div><img src="http://placehold.it/350x300?text=7"></div>
						    </div>
		
						    <div class="slider-nav">
						    	<div><img src="../img/product/detail_img01.jpg"></div>
						    	<div><img src="http://placehold.it/350x300?text=2"></div>
						    	<div><img src="http://placehold.it/350x300?text=3"></div>
						    	<div><img src="http://placehold.it/350x300?text=4"></div>
						    	<div><img src="http://placehold.it/350x300?text=5"></div>
						    	<div><img src="http://placehold.it/350x300?text=6"></div>
						    	<div><img src="http://placehold.it/350x300?text=7"></div>
						    </div>
							</div>
							<!--// center -->
						</div>
						<!--// top -->
					</div>
					<!--// product -->
					<div id="FILE_LIST_INFO" name="FILE_LIST_INFO">
					</div>
				</td>
			</tr>
		</table>
		</form>
		
		<div class="btn_zone">
			<div class="right"><button type="button" onClick="fn_insertProdImg();">등록</button>
			<div class="right"><button type="button" onClick="fn_deleteProdImg();">삭제</button>
			<button type="button" onClick="self.close();">취소</button></div>
		</div>
</div>

</body>