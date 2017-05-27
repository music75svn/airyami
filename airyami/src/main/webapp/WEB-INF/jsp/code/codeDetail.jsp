<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%@ include file="/include/admin_standard.jsp"%>
<jsp:include page="/include/common.jsp"/>

<script type="text/javascript">

$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	gfn_OnLoad();
	
	fn_init();
	if('<c:out value="${MODE}"/>' == 'DETAIL'){
		fn_srch();	// 상세조회
	}
});

// 화면내 초기화 부분
function fn_init(){
	// 체크박스 초기화
	//gfn_setCheck($('#C001001'));
	// select 박스 초기화
	//gfn_setSelect($('#SYS_CD'), 'C001002');
}

////////////////////////////////////////////////////////////////////////////////////
// 호출부분 정의
// 상세조회 조회
function fn_srch(){
	var inputParam = new Object();
	inputParam.sid 				= "getCodeDetail";
	inputParam.url 				= "/code/getCodeDetail.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	//inputParam.callback			= "fn_callBack";
	
	gfn_Transaction( inputParam );
}


////////////////////////////////////////////////////////////////////////////////////
// 콜백 함수
function fn_callBack(sid, result, data){
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	// fn_srch
	if(sid == "getCodeDetail"){
		gfn_setDetails(result.ds_detail, $("#contents"));	// 지역내 상세 내용 셋업
		
		for(var i = 0; i < result.ds_langNameList.length; i++){
			$('#CODE_NM_'+result.ds_langNameList[i].LANG_CD).val(result.ds_langNameList[i].CODE_NM);
		}
		
	}else if(sid = "saveCode"){
		// 저장
		alert("<spring:message code="success.request.msg"/>");
		
		var inputParam = gfn_makeInputData($("#findForm"));
		gfn_commonGo("/code/codeList", inputParam, "N");
	}
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
// 저장버튼클릭
function fn_goSave(){
	
	if(!gfn_validationForm($("#dataForm"))){
		return;
	}
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return false;
	}
	var inputParam = gfn_makeInputData($("#dataForm"));
	inputParam.sid 				= "saveCode";
	inputParam.url 				= "/code/saveCode.do";
	
	if('<c:out value="${MODE}"/>' == 'DETAIL'){
		$('#PROC_MODE').val("UPDATE");
	}else{
		$('#PROC_MODE').val("CREATE");
	}
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	gfn_Transaction( inputParam );
}

function goDelete(){
	if(!confirm("<spring:message code="common.delete.msg"/>")){
		return false;
	}
	
	var inputParam = gfn_makeInputData($("#dataForm"));
	inputParam.sid 				= "saveCode";
	inputParam.url 				= "/code/saveCode.do";
	$('#PROC_MODE').val("DELETE");
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( inputParam );
}

//목록 버튼
function fn_goBack(){
	var inputParam = gfn_makeInputData($("#findForm"));
	
	gfn_commonGo("/code/codeList", inputParam, "N");
}

////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출


////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수
// 팝업 내용 변경시 초기화
function fn_userNmChange() {
    $('#USER_ID').val('');
}


function fn_clearData(){
	
	gfn_clearData($("#contents"));
}

////////////////////////////////////////////////////////////////////////////////////
</script>

</head>
<body>
<!-- wrap -->
<div id="wrap">

<!--  header -->
<%@ include file="/layout/header.jsp"%>
<!--//  header --> 

<!-- container -->
<div id="container">
	<!-- menu -->
<%@ include file="/layout/menu_left.jsp"%>
	<!--// menu -->
  
	<div id="contents">
		<h3><spring:message code="word.codeDetail"/></h3>
		<form id="findForm" name="findForm">
			<ppe:makeHidden var="${findParams}" filter="FIND_"/>
			<ppe:makeHidden var="${findParams}" filter="FIND2_"/>
			<input type="hidden" name="CODE_GROUP_ID" id="CODE_GROUP_ID" value='<c:out value="${CODE_GROUP_ID}"/>'/>
		</form>
		<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
			<input type="hidden" name="SEARCH_CODE_GROUP_ID" id="SEARCH_CODE_GROUP_ID" value='<c:out value="${CODE_GROUP_ID}"/>'/>
			<input type="hidden" name="SEARCH_CODE_ID" id="SEARCH_CODE_ID" value='<c:out value="${CODE_ID}"/>'/>
			<input type="hidden" name="MODE" id="MODE" value='<c:out value="${MODE}"/>'/>
			<input type="hidden" name="PROC_MODE" id="PROC_MODE" value=''/>
			<input type="hidden" name="CODE_GROUP_ID" id="CODE_GROUP_ID" value='<c:out value="${CODE_GROUP_ID}"/>'/>
		<table summary="<spring:message code="word.codeDetail"/>" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="15%">
			<col width="15%">
			<col width="70%">
			</colgroup>
			<tr>
				<th colspan="2"><spring:message code="word.code"/></th>
			<c:choose>
				<c:when test="${MODE=='DETAIL'}">
				<td><input type="text" name="CODE_ID" id="CODE_ID" disabled /></td>
				</c:when>
				<c:when test="${MODE=='CREATE'}">
				<td><input type="text" name="CODE_ID" id="CODE_ID" maxlength="10" title="<spring:message code="word.code"/>" depends="required,englishNumeric"/></td>
				</c:when>
			</c:choose>
			</tr>
			<tr>
<c:set var="listSize" value="${fn:length(ds_cd_LANG)}" />
				<th rowspan="${listSize+1}"><spring:message code="word.codeNm"/></th>
<c:forEach var="LANG" items="${ds_cd_LANG}">
			<tr>
				<th>${LANG.CD_NM}</th>
				<td>
					<input type="text" name="CODE_NM_${LANG.CD}" id="CODE_NM_${LANG.CD}" maxlength="50" style="width:200px" value="${NAMELIST.CODE_NM}" title="<spring:message code="word.codeNm"/>" depends="required"/>
				</td>
			</tr>
</c:forEach>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="cop.remark"/></th>
				<td><textarea name="REMARKS" id="REMARKS" cols="50" rows="3" maxlength="500" title="<spring:message code="cop.remark"/>"/></textarea></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.sortOrder"/></th>
				<td><input type="text" name="SORT_ORDER" id="SORT_ORDER" maxlength="2" title="<spring:message code="word.sortOrder"/>" depends="required,Numeric"/></td>
			</tr>
	<c:choose>
		<c:when test="${MODE=='CREATE'}">
			<input type="hidden" name="USE_YN" id="USE_YN" value="Y"/>
		</c:when>
		<c:when test="${MODE=='DETAIL'}">
			<tr>
				<th colspan="2"><spring:message code="cop.useAt"/></th>
				<td>
					<select id="USE_YN" name="USE_YN" title="<spring:message code="cop.useAt"/>">
						<option value="Y"><spring:message code="button.use"/></option>
						<option value="N"><spring:message code="button.notUsed"/></option>
					</select>
				</td>
			</tr>
		</c:when>
	</c:choose>
		</table>
		</form>
		
		<div class="btn_zone">
			<div class="left"><span id="preLink" onClick="javascript:fn_goBack()" title="<spring:message code="button.list"/>"></span></div>
		<c:choose>
			<c:when test="${MODE=='DETAIL'}">
			<div class="right"><button type="button" id="btnW_delete" onClick="javascript:goDelete()"><spring:message code="button.delete"/></button></div>
			</c:when>
			<c:when test="${MODE=='CREATE'}">
			<div class="right"><button type="button" id="btnW_clear" onClick="javascript:fn_clearData()"><spring:message code="button.clear"/></button></div>
			</c:when>
		</c:choose>
			<div class="right"><button type="button" id="btnW_save" onClick="javascript:fn_goSave()"><spring:message code="button.save"/></button></div>
		</div>

	</div> 
</div>


<!--  footer -->
<%@ include file="/layout/footer.jsp"%>
<!--  //footer -->
	
</div>
<!-- wrap -->
</body>
</html>