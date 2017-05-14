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
	
	fn_init();
	
});


//화면내 초기화 부분
function fn_init(){
	
}

////////////////////////////////////////////////////////////////////////////////////
//호출부분 정의

//사용자그룹 등록
function fn_insert(){
	var inputParam = new Object();
	inputParam.sid 				= "insertMenu";
	inputParam.url 				= "/menu/insertMenu.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	//inputParam.callback			= "fn_callBack";
	
	gfn_Transaction( inputParam );
}


////////////////////////////////////////////////////////////////////////////////////
//콜백 함수
function fn_callBack(sid, result, data){
	debugger;
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
		
	if(sid == "insertMenu"){
		alert("등록하였습니다.");
		opener.fn_srch();
		self.close();
	}
	
}


</script>
</head>
<body class="popup">
<h1>메뉴관리<a onClick="self.close();" class="close">X</a></h1>

<div class="content">
	<h4>메뉴 등록</h4>
	<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
		<table summary="메뉴 등록" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="30%">
			<col width="80%">
			</colgroup>
			<tr>
				<th>메뉴타입</th>
				<td>
					<select id="MENU_TYPE" name="MENU_TYPE">
                        <c:forEach var="MENU_TYPE" items="${ds_cd_MENU_TYPE}">
                            <option value="${MENU_TYPE.CD}">${MENU_TYPE.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>메뉴코드</th>
				<td><input type="text" name="MENU_CODE" id="MENU_CODE" size=30  value="자동생성" readonly/></td>
			</tr>
			<tr>
				<th>상위메뉴코드</th>
				<td><input type="text" name="UPPER_MENU_CODE" id="UPPER_MENU_CODE" value='<c:out value="${UPPER_MENU_CODE}"/>' readonly/></td>
			</tr>
			<tr>
				<th>메뉴 LEVEL</th>
				<td><input type="text" name="MENU_LEVEL" id="MENU_LEVEL" size=30  value='<c:out value="${MENU_LEVEL}" />' readonly/></td>
			</tr>
			<tr>
				<th>메뉴명</th>
				<td><input type="text" name="MENU_NAME" id="MENU_NAME" size=72/></td>
			</tr>
			<tr>
				<th>메뉴설명</th>
				<td><input type="text" name="MENU_DESC" id="MENU_DESC" size=72/></td>
			</tr>
			<tr>
				<th>링크 URL</th>
				<td><input type="text" name="LINK_URL" id="LINK_URL" size=72/></td>
			</tr>
			<tr>
				<th>링크 PARAMETER</th>
				<td><input type="text" name="LINK_PARAM" id="LINK_PARAM" size=72/></td>
			</tr>
			<tr>
				<th>메뉴순서</th>
				<td><input type="text" name="MENU_ORDER" id="MENU_ORDER" size=72 value="자동생성 - 위치변경은 메뉴수정에서 변경" readonly/></td>
			</tr>
			<tr>
				<th>사용자그룹</th>
				<td><input type="text" name="USER_GROUP" id="USER_GROUP" size=72 value="초기등록은 관리자그룹만 등록" readonly/></td>
			</tr>
			<tr>
				<th>사용여부</th>
				<td>					
					<select id="USE_YN" name="USE_YN">
						<option value ="Y">사용</option>
						<option value ="N">미사용</option>
					</select>
				</td>
			</tr>
		</table>
		</form>
		
		<div class="btn_zone">
			<div class="right"><button type="button" onClick="fn_insert();">등록</button>
			<button type="button" onClick="self.close();">취소</button></div>
		</div>
</div>

</body>