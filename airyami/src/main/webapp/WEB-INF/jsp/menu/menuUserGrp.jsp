<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%-- <%@ include file="/include/admin_standard_pop.jsp"%> --%>
<%@ include file="/include/admin_standard.jsp"%>
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
	inputParam.sid 				= "menuUserGrpList";
	inputParam.url 				= "/menu/menuUserGrpList.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	
	gfn_Transaction( inputParam );
}

// 선택된 사용자그룹 등록
function fn_chkInsert(){
	var checkInfo = gfn_getCheckOk("tb_list");
	
	if(gfn_isNull(checkInfo))
		return;
	
	if(!confirm("<spring:message code="common.regist.msg"/>")){
		return false;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "menuUserGrpInsert";
	inputParam.url 				= "/menu/menuUserGrpInsert.do";
	inputParam.data 			= { "REQKEY"  : checkInfo};
	inputParam.data.MENU_TYPE	= $("#MENU_TYPE").val();
	inputParam.data.MENU_CODE	= $("#MENU_CODE").val();
	
	gfn_Transaction( inputParam );
}

// 선택된 사용자그룹 삭제
function fn_chkDelete(){
	var checkInfo = gfn_getCheckOk("tb_list");
	
	if(gfn_isNull(checkInfo))
		return;
	
	var inputParam = new Object();
	inputParam.sid 				= "menuUserGrpDelete";
	inputParam.url 				= "/menu/menuUserGrpDelete.do";
	inputParam.data 			= { "REQKEY"  : checkInfo};
	inputParam.data.MENU_TYPE	= $("#MENU_TYPE").val();
	inputParam.data.MENU_CODE	= $("#MENU_CODE").val();
	
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
	
	// fn_srch
	if(sid == "menuUserGrpList"){
		var tbHiddenInfo = ["USER_TYPE", "USER_ROLE"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
		gfn_displayList(result.ds_list, "tb_list", tbHiddenInfo);
		gfn_displayTotCnt(result.totCnt);
	}
	else if(sid == "menuUserGrpInsert"){
		alert('<spring:message code="success.common.insert"/>');
		opener.fn_srch();
		self.close();
	}
	else if(sid == "menuUserGrpDelete"){
		alert('<spring:message code="success.common.delete"/>');
		opener.fn_srch();
		self.close();
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
<h1><spring:message code="word.menuUserGrp" /><a onClick="self.close();" class="close">X</a></h1>

<div class="content">
	<h3>[<c:out value="${MENU_NAME}"/>]&nbsp;<spring:message code="word.menuUserGrp" />&nbsp;<spring:message code="button.list" /> </h3>
	<form id="srchForm" name="srchForm" method="post" onsubmit="return false;">
		<input type="hidden" name="MENU_TYPE" id="MENU_TYPE" value='<c:out value="${MENU_TYPE}"/>'/>
		<input type="hidden" name="MENU_CODE" id="MENU_CODE" value='<c:out value="${MENU_CODE}"/>'/>
	</form>
	<!-- 테이블 -->
	<table id="tb_list" summary="사용자Grp 목록" cellspacing="0" border="0" class="tbl_list_type1">
		<colgroup>
			<col width="5%"/>
			<col width="40%"/>
			<col width="40%"/>
			<col width="15%"/>
		</colgroup>
		<thead> 
			<tr>
				<th cid="CK"><input type="checkbox" name="check"  onclick="javascript:gfn_checkAll('tb_list', this);"/></th>
				<th cid="USER_TYPE_NM" alg="left"><spring:message code="word.userType"/></th>
				<th cid="USER_ROLE_NM" alg="left"><spring:message code="word.userRole"/></th>
				<th cid="USE_YN" alg="center"><spring:message code="cop.useAt"/></th>
			</tr> 
		</thead> 
		<tbody>
			<tr></tr>
		</tbody>
	</table>
	<!--// 테이블 -->
    <div class="btnArea2">
		<button type="button" class="btn3" style="width:50px;" onclick="javascript:fn_chkInsert()"><span><spring:message code="button.create"/></span></button>
		<button type="button" class="btn3" style="width:50px;" onclick="javascript:fn_chkDelete()"><span><spring:message code="button.delete"/></span></button>
    </div>
</div>

</body>
</html>