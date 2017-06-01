<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>

<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>admin 로그인</title>
<%@ include file="/include/admin_standard.jsp"%>
<script type="text/javascript"> 
	$(function() {  //onready
		//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
		
		//gfn_getSiteID();
	});
	
	////////////////////////////////////////////////////////////////////////////////////
	//호출부분 정의
	//로그인 체크
	function fn_login(){
		
//		if(!gfn_validationForm($("#srchForm"))){
//			return;
//		}
		
		/* 
		var fieldlist = 
	        [ 
	            ["LOGIN_ID", "아이디"],["PW", "패스워드"]
	        ];
		
		if(!gfn_isCheckOk(fieldlist)){
			return;
		} 
		*/
		
		var inputParam = new Object();
		inputParam.sid 				= "adminCheck";
		inputParam.url 				= "/login/checkAdminUser.do";
		inputParam.data 			= $("#srchForm").serialize();
		//inputParam.callback			= "fn_callBack";
		
		
		gfn_Transaction( inputParam );
}
	
	

function fn_setLocal(value){
	return;
	alert(value);
	var inputParam = new Object();
	inputParam.sid 				= "changeLocal";
	inputParam.url 				= "/COM/setChangeLocalePage.do";
	//inputParam.application 		= false;
	inputParam.data 			= $("#srchForm").serialize();
	
	gfn_Transaction( inputParam );
}
////////////////////////////////////////////////////////////////////////////////////
//콜백 함수
function fn_callBack(sid, result){
	
	if (!result.success) {
		//alert(result.msg);
		alert('<spring:message code="fail.common.msg" />');
		return;
	}
	
	if(sid == "changeLocal"){
		//debugger;
	}
	
	// fn_login
	if(sid == "adminCheck"){
		var checkResult = result.checkResult; 
		
		// 아이디 미 존재 값
		if("1" == checkResult){
			alert("존재하지 않는 아이디입니다.");
		}
		// 패스워드 실패한도가 넘은경우
		else if("2" == checkResult){
			alert("패스워드 실패한도가 초과 하였습니다. /n 관리자에게 문의 바랍니다.");
		}
		else if("3" == checkResult){
			alert("패스워드가 틀렸습니다.");
		}
		else if("10" == checkResult){
			//debugger;
			gfn_goMain("");
		}
	}
	
}




</script>

<script type="text/javascript"> 
 function OnEnter( field ) { if( field.value == field.defaultValue ) { field.value = ""; } } 
 function OnExit( field ) { if( field.value == "" ) { field.value = field.defaultValue; } } 
</script> 
</head>
<body class="login">
	<div id="login_form">
		<input type="hidden" name="SITE_ID" id="SITE_ID" value="<c:out value="${SITE_ID}"/>"/>
		<form id="srchForm" name="srchForm" method="post"  onsubmit="return false;">
			<fieldset>
				<legend>로그인</legend>
				<h3>admin로그인</h3>
		            
				<div id="id">
					<label for="user_id" class="i_label"><spring:message code="common.save.msg" />아이디</label>
					<input id="LOGIN_ID" name="LOGIN_ID" value="admin" class="i_input" onfocus="OnEnter(this)" onblur="OnExit(this)" type="text">
					
				</div>
				<div id="pw">
					<label for="PW" class="i_label">패스워드</label>
					<input id="password" name="PW" value="admin" class="i_input" onfocus="OnEnter(this)" onblur="OnExit(this)" type="password">
				</div>
				<div>
					<label for="locale" class="i_label">언어</label>
					<select id="locale" name="locale" onchange="javascript:fn_setLocal(this.value);">
	                    <option value="ko" selected>한국어</option>
	                    <option value="en">영어</option>
					</select>
				</div>
				<div id="btn_login">
					<div onclick="javascript:fn_login(); return false;">로그인</div>
					<!-- <input type="submit" value="로그인" onclick="javascript:fn_login(); return false;"> -->
				</div>
		    </fieldset>
		</form>
	</div>


</body>
</html>