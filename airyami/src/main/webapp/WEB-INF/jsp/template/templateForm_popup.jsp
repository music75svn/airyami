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
	
	if(gfn_isNull($("#CODE_GROUP_ID").val()))
		return;
	
	var inputParam = new Object();
	inputParam.sid 				= "getCodeView";
	inputParam.url 				= "/template/getCodeGrpView.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	
	gfn_Transaction( inputParam );
}


//저장
function fn_save(){
	
	// 필수체크
	
	var inputParam = new Object();
	inputParam.sid 				= "saveCodeGrp";
	inputParam.url 				= "/template/saveCodeGrp.do";
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
	
	if(sid == "getCodeView"){
		gfn_setDetails(result.ds_detail, $("#contents"));	// 지역내 상세 내용 셋업
	}
	else if(sid == "saveCodeGrp"){
		// 창을 닫던지. 부모 재조회를 하던지 
		alert('<spring:message code="common.save.msg" />');
	}
}



</script>
</head>
<body class="popup">
<h1>리스트 샘플<a onClick="self.close();" class="close">X</a></h1>

<div id="contents" class="content">
	<h3>코드정보</h3>
	<form id="srchForm" name="srchForm" method="post" onsubmit="return false;">
		<input type="hidden" name="CODE_GROUP_ID" id="CODE_GROUP_ID" value='<c:out value="${CODE_GROUP_ID}"/>'/>
		<table summary="코드 정보 상세보기" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="20%">
			<col width="80%">
			</colgroup>
			<tr>
				<th>그룹코드</th>
				<td><input type="text" name="CODE_GROUP_ID" id="CODE_GROUP_ID" value='<c:out value="${CODE_GROUP_ID}"/>'/></td>
			</tr>
			<tr>
				<th>그룹코드명</th>
				<td><input type="text" name="CODE_GROUP_NM" id="CODE_GROUP_NM"/></td>
			</tr>
			<tr>
				<th>사용여부</th>
				<td>
					<select id="USE_YN" name="USE_YN" title="사용여부">
                        <c:forEach var="USE_YN" items="${ds_cd_USE_YN}">
                            <option value="${USE_YN.CD}">${USE_YN.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>상세</th>
				<td><textarea cols="30" rows="5" name="REMARKS" id="REMARKS"></textarea></td>
			</tr>
		</table>
	</form>
    <div class="btn_zone">
    	<button type="button" onClick="javascript:fn_save()">등록</button>
    </div>
</div>

</body>
</html>