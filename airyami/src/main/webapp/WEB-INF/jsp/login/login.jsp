<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>

<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>사용자 로그인</title>
<%@ include file="/include/shop_standard_new.jsp"%>
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
		inputParam.sid 				= "loginCheck";
		inputParam.url 				= "/login/checkUser.do";
		inputParam.data 			= $("#srchForm").serialize();
		//inputParam.callback			= "fn_callBack";
		
		
		gfn_Transaction( inputParam );
}

// 비밀번호 찾기 이동
function fn_findPass(){
	var inputParam				= {};
	
	gfn_commonGo("/login/goFindPass", inputParam, "N");
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
	if(sid == "loginCheck"){
		var checkResult = result.checkResult; 
		
		// 아이디 미 존재 값
		if("1" == checkResult){
			$('#error_msg').text("존재하지 않는 아이디입니다.");
			$('#error_msg').show();
		}
		// 패스워드 실패한도가 넘은경우
		else if("2" == checkResult){
			$('#error_msg').text("패스워드 실패한도가 초과 하였습니다. /n 관리자에게 문의 바랍니다.");
			$('#error_msg').show();
		}
		else if("3" == checkResult){
			$('#error_msg').text("패스워드가 틀렸습니다.");
			$('#error_msg').show();
		}
		else if("10" == checkResult){
			//debugger;
			$('#error_msg').hide();
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
<body class="grey">
	<div class="login_wrap" id="login_form">
		<h1>회원 로그인</h1>
        <a href="javascript:fn_findPass();" class="find_pw">비밀번호 찾기</a>
		<input type="hidden" name="SITE_ID" id="SITE_ID" value="<c:out value="${SITE_ID}"/>"/>
		<form id="srchForm" name="srchForm" method="post" onsubmit="return false;">
			<fieldset class="login_form">
				<legend class="blind">로그인</legend>
				<div class="wrapper">
					<label class="blind">아이디</label><input id="LOGIN_ID" name="LOGIN_ID" value="music75" class="text" onfocus="OnEnter(this)" onblur="OnExit(this)" type="text">
					<label class="blind">비밀번호</label><input id="password" name="PW" value="admin" class="text" onfocus="OnEnter(this)" onblur="OnExit(this)" type="password">
                    <div class="error" style="display:none;" ID="error_msg">
                    	<p>다시 로그인해 주세요. 아이디나, 비밀번호가 일치하지 않습니다.</p>
                    </div>
					<input type="submit" class="btn_login" value="로그인" onclick="javascript:fn_login(); return false;">
				</div>
			</fieldset>
		</form>
		
		<!-- 언어선택 -->
		<div class="lang">
			<select id="locale" name="locale" onchange="javascript:fn_setLocal(this.value);" class="select">
                   <option value="ko" selected>한국어</option>
                   <option value="en">영어</option>
			</select>
		</div>
		<!-- 언어선택 끝 -->
	</div>
</body>

</body>
</html>