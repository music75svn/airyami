<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%@ include file="/include/admin_standard.jsp"%>
<jsp:include page="/include/common.jsp"/>

<script type="text/javascript">
var onload = true;
$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	//alert("LOGIN_ID " + '<c:out value="${LOGIN_ID}"/>');
	
	gfn_OnLoad();
	
	fn_init();
	
	fn_srch();
});

// 화면내 초기화 부분
function fn_init(){
	// 그리드에 소트 기능 추가한다.
	gfn_addGridSort("tb_list");
	
	// myParams 에 넘어온 값이 있으면 이전 검색조건 셋팅한다.
	gfn_setMyParams();
	
	$("#SEARCH_ORIGINAL_NM").val($("#SEARCH_USER_NM").val());
}

////////////////////////////////////////////////////////////////////////////////////
// 호출부분 정의
// 리스트 조회
function fn_srch(){
	
	// 파라미터 validation
	if(!gfn_validationForm($("#srchForm"))){
		return;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "userList";
	inputParam.url 				= "/user/selectUserList.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	//inputParam.callback			= fn_callBackA;
	
	gfn_Transaction( inputParam );
}

////////////////////////////////////////////////////////////////////////////////////
// 콜백 함수
function fn_callBack(sid, result){
	//debugger;
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	
	// fn_srch
	if(sid == "userList"){
		var tbHiddenInfo = ["USER_ID", "ORIGINAL_NM"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
		gfn_displayList(result.ds_list, "tb_list", tbHiddenInfo);
		gfn_displayTotCnt(result.totCnt);
		
		gfn_addPaging(result.pageInfo, 'gfn_clickPageNo');
		gfn_addRowClickEvent("tb_list", "fn_clickRow"); // ==>동일하다
	}
	
	// fn_srch
	if(sid == "delCd"){
		alert(sid);
	}
	
}

//row click event
function fn_clickRow(rowObj){
	var USER_ID = $('input[name=USER_ID]', rowObj).val();
	var USER_NM = $('input[name=ORIGINAL_NM]', rowObj).val();

	var inputParam				= {};
	inputParam.USER_ID 			= USER_ID;
	inputParam.USER_NM 			= USER_NM;
	inputParam.sid				= $("#sid").val();
	
	opener.fn_popCallBack(inputParam.sid, inputParam);
	
	self.close();
}

</script>
</head>
<body class="popup">
<h1><spring:message code="word.userFindList"/>&nbsp;<a onClick="self.close();" class="close">X</a></h1>
	<div class="content">
		<form id="myParams" name="myParams">
			<ppe:makeHidden var="${findParams}" filter="FIND_" />
		</form>
		<form id="srchForm" name="srchForm" method="post" action="<c:url value='/commCode/codeList.do'/>" onsubmit="return false;">
			<input type="hidden" name="pageNo" id="pageNo" value="1"/>
			<input type="hidden" name="EXCEL_YN" id="EXCEL_YN" />
			<input type="hidden" name="SORT_COL" id="SORT_COL" value="INSERT_DT DESC"/>
			<input type="hidden" name="SORT_ACC" id="SORT_ACC" />
			<input type="hidden" name="SEARCH_POP_YN" id="Y" />
			<input type="hidden" name="SEARCH_USER_NM" id="SEARCH_USER_NM" value='<c:out value="${SEARCH_USER_NM}"/>'/>
			<input type="hidden" name="sid" id="sid" value='<c:out value="${sid}"/>'/>
		<div id="search" style="width:100%">
			<dl>
				<dt>&nbsp;</dt>
				<dd>
					<label for="SEARCH_USER_ID" id="S1"><spring:message code="word.userId"/></label>
					<input type="text" id="SEARCH_USER_ID" name="SEARCH_USER_ID" value="" title="<spring:message code="word.userId"/>" maxlength=20/>
					<label for="SEARCH_ORIGINAL_NM" id="S2"><spring:message code="word.userNm"/></label>
					<input type="text" id="SEARCH_ORIGINAL_NM" name="SEARCH_ORIGINAL_NM" value="" title="<spring:message code="word.userNm"/>" maxlength=20/>
					<input type="submit" value="<spring:message code="button.search"/>" onclick="javascript:gfn_fn_srch(); return false;"/>
				</dd>
			</dl>
		</div>
		<div class="tableInfoArea">
			<!-- total 총건수 -->
	        <span id="totCnt"></span>
		    <!--// total 총건수 -->
			<!-- 출력건수 -->
			<div class="tablelistQuantity" id="pageUnit"></div>
			<!--// 출력건수 -->
		</div>
		<!-- //출력건수및 리스트 페이지당 갯수 -->
		</form>
		
		<!-- table list -->
		<div class="aTypeListTbl">
		<table id="tb_list" summary="<spring:message code="word.userList"/>">
			<caption>리스트</caption>
			<colgroup>
				<col width="5%"/>
				<col width="15%"/>
				<col width=""/>
				<col width="15%"/>
				<col width="15%"/>
				<col width="15%"/>
				<col width="15%"/>
			</colgroup>
			<thead>
				<tr>
					<th cid="ROWNUM" cClass="num" cType="NUM"><spring:message code="word.num"/></th>
					<th cid="USER_ID" alg="center"><spring:message code="word.userId"/></th>
					<th cid="ORIGINAL_NM" alg="center"><spring:message code="word.userNm"/></th>
					<th cid="USER_ROLE_NM" alg="center"><spring:message code="word.userTypeRole"/></th>
					<th cid="USE_LANGUAGE_NM" alg="center"><spring:message code="word.useLanguageCd"/></th>
					<th cid="RECOMMENDER_ID" alg="center"><spring:message code="word.recommenderId"/></th>
					<th cid="LAST_ORDER_DATE" alg="center"><spring:message code="word.lastOrderDate"/></th>
				</tr> 
			</thead> 
			<tbody>
				<tr></tr>
			</tbody>
		</table>
		</div>
		<!--// table list -->
		<span id="pagingNav"></span>
</body>