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
			alert('<spring:message code="fail.user.passErr1" />');
		}
		else if("2" == checkResult){
			alert('<spring:message code="fail.user.passErr2" />');
		}
		else if("3" == checkResult){
			alert('<spring:message code="fail.user.passErr3" />');
		}
		else if("0" == checkResult){
			alert('<spring:message code="fail.user.passSuccess" />');

			var inputParam				= {};
			gfn_commonGo("/login/login", inputParam, "N");
		}
	}
	
}




</script>
</head>
<body class="login">
	<div id="login_form">
		<form id="srchForm" name="srchForm" method="post"  onsubmit="return false;">
			<fieldset>
				<legend><spring:message code="word.findPass"/></legend>
				<h3><spring:message code="word.findPass"/></h3>
		            
				<div id="id">
					<label for="USER_ID" ><spring:message code="word.userId"/></label>
					<input id="USER_ID" name="USER_ID" value="" class="i_input" type="text" title="<spring:message code="word.userId"/>" depends="required">
					
				</div>
				<div id="email">
					<label for="EMAIL_ID"><spring:message code="cop.emailAdres"/></label>
					<input id="EMAIL_ID" name="EMAIL_ID" value="" class="i_input" type="text" title="<spring:message code="cop.emailAdres"/>" depends="required,email">
				</div>
				<div id="btn_login">
					<div onclick="javascript:fn_initPass(); return false;"><spring:message code="word.initPass"/></div>
				</div>
		    </fieldset>
		</form>
	</div>


</body>
</html>