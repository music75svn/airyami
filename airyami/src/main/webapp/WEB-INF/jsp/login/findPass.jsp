<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>

<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>비밀번호찾기</title>
<%@ include file="/include/shop_standard_new.jsp"%>
<script type="text/javascript"> 
$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	
	//gfn_getSiteID();
});
	
////////////////////////////////////////////////////////////////////////////////////
//호출부분 정의
//로그인 체크
function fn_initPass(){
	
	if(!gfn_validationForm($("#srchForm"))){
		return;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "initPass";
	inputParam.url 				= "/login/initPass.do";
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
	
	// fn_initPass
	if(sid == "initPass"){
		var checkResult = result.checkResult; 
		
		// 아이디 미 존재 값
		if("1" == checkResult){
			$('#error_msg').text("<spring:message code="fail.user.passErr1" />");
			$('#error_msg').show();
		}
		else if("2" == checkResult){
			$('#error_msg').text("<spring:message code="fail.user.passErr2" />");
			$('#error_msg').show();
		}
		else if("3" == checkResult){
			$('#error_msg').text("<spring:message code="fail.user.passErr3" />");
			$('#error_msg').show();
		}
		else if("0" == checkResult){
			alert('<spring:message code="fail.user.passSuccess" />');
			$('#error_msg').hide();

			var inputParam				= {};
			gfn_commonGo("/login/login", inputParam, "N");
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
	<div class="login_wrap">
		<h1><spring:message code="word.findPass"/></h1>
        <p class="cmt"><spring:message code="common.passFind1.msg"/><Br />
			<spring:message code="common.passFind2.msg"/>
        </p>
		<form id="srchForm" name="srchForm" method="post"  onsubmit="return false;">
			<fieldset class="login_form">
				<legend class="blind"><spring:message code="word.findPass"/></legend>
				<div class="wrapper">
					<label class="blind"><spring:message code="word.userId"/></label><input id="USER_ID" name="USER_ID" value="<spring:message code="common.idInput.msg"/>" class="text" type="text" title="<spring:message code="word.userId"/>" onfocus="OnEnter(this)" onblur="OnExit(this)" depends="required">
					<label class="blind"><spring:message code="cop.emailAdres"/></label><input id="EMAIL_ID" name="EMAIL_ID" value="<spring:message code="common.emailInput.msg"/>" class="text" type="text" title="<spring:message code="cop.emailAdres"/>" onfocus="OnEnter(this)" onblur="OnExit(this)" depends="required,email">
                    <div class="error" style="display:none;" id="error_msg">
                    	
                    </div>
					<input type="submit" class="btn_confirm" value="<spring:message code="button.confirm"/>" onclick="javascript:fn_initPass(); return false;">
				</div>
			</fieldset>
		</form>
		
	</div>
</body>
</html>