<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- Web-Agent의 로그인페이지에 include 한다. -->
	<%@ include file="/sso/isign_sso_login_include.jsp" %>
</head>

<body>
	<!-------------------------------------------------------
	 * login form
	 //------------------------------------------------------>
	<form name="loginform" method="post" action="">
		
		<!----- [인증서버와 통신 방식이 ISSAC-Web인 경우만 설정]ISSAC-Web 구간 암호화를 적용하기 위하여 hidden을  추가하여야 한다. ----->
		<input type="hidden" name="issacweb_data" value="" />
		<input type="hidden" name="challenge" value="" />
		<input type="hidden" name="response" value="" />
		<!----- [인증서버와 통신 방식이 ISSAC-Web인 경우만 설정]ISSAC-Web 구간 암호화를 적용하기 위하여 hidden을  추가하여야 한다. ----->
		
		<!----- [인증 방식:ID/PWD방식의 로그인인 경우]아이디와 비밀번호 input name은 각각 id,pw로 설정 ----->
		아이디 : <input type="text" name="id" />
		비밀번호 : <input type="password" name="pw" onKeyDown="javascript:if(event.keyCode==13){idpwLogin();}"/>
		<!----- [인증 방식:ID/PWD방식의 로그인인 경우]아이디와 비밀번호 input name은 각각 id,pw로 설정 ----->
		
		<!----- [인증 방식:ID/PWD방식의 로그인인 경우]ID/PW로그인 ----->
		<input type="button" onclick="javascript:idpwLogin();" value="로그인" style="cursor:hand" />
		<!----- [인증 방식:ID/PWD방식의 로그인인 경우]ID/PW로그인 ----->
		
		<!----- [인증 방식:PKI 방식의 로그인인 경우]인증서 로그인 ----->
		<input type="button" value="인증서 로그인" onclick="javascript:pkiLogin();" />
		<!----- [인증 방식:PKI 방식의 로그인인 경우]인증서 로그인 ----->
	</form>

	<!-- WebCrypto UI include -->
	<%@ include file="/sso/webcrypto/ui_template/ui_template_body.jsp" %>
</body>
</html>