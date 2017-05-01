<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%@ include file="/include/dwise_standard.jsp"%>
<jsp:include page="/include/common.jsp"/>

<script type="text/javascript">

$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	gfn_OnLoad();
	
	fn_init();
	
	fn_srch();
});


//화면내 초기화 부분
function fn_init(){
	
}


////////////////////////////////////////////////////////////////////////////////////
//호출부분 정의
//조회
function fn_srch(){
	var inputParam = new Object();
	inputParam.sid 				= "codeList";
	inputParam.url 				= "/commCode/codeList.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	
	gfn_Transaction( inputParam );
}

////////////////////////////////////////////////////////////////////////////////////
//콜백 함수
function fn_callBack(sid, result){
	//debugger;
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	debugger;
	// fn_srch
	if(sid == "codeList"){
		var list = result.ds_list;
		if( list ) {
		    for( var i = 0; i < list.length; i++ ) {
		    	fn_addTableTr(list[i].CD_NM, list[i].CD);
		    }
		}
	}
	
}

function fn_addTableTr(value, seq){
	
	var trHtml = "<tr>"; 
	trHtml += "<td class=\"keep\" width=\"85%\" seq=\""+seq+"\">"+value+"</td>";
	trHtml += "<td width=\"15%\"><button class=\"grey\" onClick=\"fn_delContent(this);\" ><span>삭제</span></button></td>";
	trHtml += "</tr>";
	
	$("#tb_list > tbody").append(trHtml);
}

</script>
</head>
<body class="popup">
<h1>리스트 샘플<a onClick="self.close();" class="close">X</a></h1>

<div class="content">
	<h3>장비 관리 목록</h3>
	<form id="srchForm" name="srchForm" method="post" onsubmit="return false;">
		<input type="hidden" name="GROUP_CODE" id="GROUP_CODE" value='<c:out value="${GROUP_CODE}"/>'/>
	</form>
	<!-- 테이블 -->
	<table id="tb_list" summary="장비 관리 목록" cellspacing="0" border="0" class="tbl_list_type1">
		<tbody id="tableTbody"> 
			<tr></tr>
		</tbody>
	</table>
	<!--// 테이블 -->
	<div class="paginate">
    	<strong>1</strong>
    </div>
    <div class="btn_zone">
		<span class="btn right"><a href="gear_input.html"> 등록 </a> </span>
    </div>
</div>

</body>
</html>